package com.bobomico.shiro.matcher;

import com.bobomico.bo.UserLoginRetryInfo;
import com.bobomico.common.Const;
import com.bobomico.dao.po.SysUserLogin;
import com.bobomico.observer.Observer;
import com.bobomico.observer.Subject;
import com.bobomico.observer.UserStatusSubject;
import com.bobomico.quartz.stevexie.scheduler.UserScheduler;
import com.bobomico.service.IUserService;
import com.bobomico.shiro.cache.PasswordRetryCache;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

/**
 * @ClassName: com.timisakura.shiro.matcher.bobomiccore2
 * @Author: Lion
 * @Date: 2019/3/22  4:11
 * @Description: 实现自己的凭证匹配器
 * @version:
 */
public class RetryLimitHashedCredentialsMatcher extends HashedCredentialsMatcher implements Observer {
    private String observerName;
    // 用户状态
    private boolean isUnlock = Boolean.FALSE;
    private String username;
    private static final Logger logger = Logger.getLogger("RetryLimitHashedCredentialsMatcher");
    // 记录用户登录信息
    @Autowired
    private PasswordRetryCache passwordRetryCache;
    @Autowired
    private IUserService iUserService;
    @Autowired
    private UserScheduler userScheduler;

    public RetryLimitHashedCredentialsMatcher(UserStatusSubject userStatusSubject) {
        // 设置订阅名
        this.setObserverName("RetryLimitHashedCredentialsMatcherObserver");
        // 注册订阅
        userStatusSubject.attach(this);
    }

    /**
     * 执行凭证匹配器 回调方法
     * @param token
     * @param info
     * @return
     */
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        // 获取用户名
        username = (String)token.getPrincipal();

        // 获取用户多次登录记录
        UserLoginRetryInfo userLoginRetryInfo = passwordRetryCache.getPasswordRetryCache().get(username);

        // 如果用户初次登录
        if (userLoginRetryInfo == null) {
            try {
                // 获取用户登录时间
                SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                Date now = new Date();
                now = simpleFormat.parse(simpleFormat.format(now));
                long currentTime = now.getTime();
                // 设置用户登录信息
                userLoginRetryInfo = new UserLoginRetryInfo(new AtomicInteger(0), currentTime);
                logger.info("用户第一次登录时间: " + now);
                // 缓存用户信息
                passwordRetryCache.getPasswordRetryCache().put(username, userLoginRetryInfo);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        // 检查登录次数
        int userRetryCount = userLoginRetryInfo.getAtomicInteger().incrementAndGet();  //查看incrementAndGet()源码 retryCount + 1
        if (userRetryCount > 1 && userRetryCount < Const.shiro.RETRYNUMBER) {
            try {
                // 获取本次登录时间
                SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                Date now = new Date();
                now = simpleFormat.parse(simpleFormat.format(now));
                logger.info("用户本次登录时间: " + now);
                long currentTime = now.getTime();
                if(!isFast(userLoginRetryInfo.getLoginTime(), currentTime)){
                    // 清除用户登录记录
                    passwordRetryCache.getPasswordRetryCache().remove(username);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else if(userRetryCount >= Const.shiro.RETRYNUMBER){
            // 建议用户状态改为常量
            // 修改数据库字段
            SysUserLogin sysUserLogin = iUserService.findSysUserByTokenType(token);
            if (sysUserLogin != null && Const.userStatus.LOCKED != sysUserLogin.getUserStats()) {
                // 修改数据库的状态字段为锁定
                sysUserLogin.setUserStats((byte) Const.userStatus.LOCKED);
                // 调用服务层更新用户状态 todo 自定义异常处理
                iUserService.updateUserStatus(sysUserLogin);
                // 创建定时解锁任务
                userScheduler.unlockJob(sysUserLogin.getSysUserId());
            }
            logger.info("该用户已被锁定" + username);
            // 用户锁定异常
            throw new LockedAccountException();
        }

        // 判断用户账号和密码是否正确
        if (super.doCredentialsMatch(token, info)) {
            // 如果正确 从缓存中将用户登录计数清除
            passwordRetryCache.getPasswordRetryCache().remove(username);
            /**
             * 从缓存中清理已经存在的session 达到强制下线的目的
             * 前端要想办法通知被强制下线的用户
             */
            // DefaultWebSecurityManager securityManager = (DefaultWebSecurityManager) SecurityUtils.getSecurityManager();
            // DefaultWebSessionManager sessionManager = (DefaultWebSessionManager)securityManager.getSessionManager();
            // Collection<Session> sessions = sessionManager.getSessionDAO().getActiveSessions();//获取当前已登录的用户session列表
            // for(Session session : sessions){
            //     // 清除该用户其他在线的session
            //     if(token.getPrincipal().equals(String.valueOf(session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY)))) {
            //         sessionManager.getSessionDAO().delete(session);
            //     }
            // }
            return Boolean.TRUE;
        }else{
            switch(userRetryCount){
                case 1:
                    throw new IncorrectCredentialsException();
                default:
                    // 用户登录错误次数太多异常
                    throw new ExcessiveAttemptsException();
            }
        }
    }

    /**
     * 细节 不要在update中取消订阅 因为map在迭代时改变状态将终止迭代 容易引起错误
     * 细节 记得状态的同步
     * @param subjectIsLock
     */
    @Override
    public void update(Subject subjectIsLock) {
        isUnlock = ((UserStatusSubject)subjectIsLock).isUnlock();
        if(isUnlock){
            // 用户登录次数记录清空
            passwordRetryCache.getPasswordRetryCache().remove(username);
            logger.info("该用户登录次数已重置: " + username);
        }
    }

    /**
     * 距离第一次登录时间小于2分钟返回true
     * @param first
     * @param now
     * @return
     */
    public boolean isFast(long first, long now){
        int minutes = (int) ((now - first) / (1000 * 60));
        System.out.println("用户距离上次错误时间差: " + minutes);
        return minutes < 1 ? Boolean.TRUE : Boolean.FALSE;
    }

    public String getObserverName() {
        return observerName;
    }

    public void setObserverName(String observerName) {
        this.observerName = observerName;
    }

}