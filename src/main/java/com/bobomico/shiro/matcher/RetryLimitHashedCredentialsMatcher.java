package com.bobomico.shiro.matcher;

import com.bobomico.bo.UserLoginRetryInfo;
import com.bobomico.common.Const;
import com.bobomico.dao.po.SysUserLogin;
import com.bobomico.ehcache.MicoCacheManager;
import com.bobomico.ehcache.origin.Cache;
import com.bobomico.observer.Observer;
import com.bobomico.observer.Subject;
import com.bobomico.observer.UserStatusSubject;
import com.bobomico.pojo.ActiveUser;
import com.bobomico.quartz.stevexie.scheduler.UserScheduler;
import com.bobomico.service.IUserService;
import com.bobomico.shiro.cache.PasswordRetryCache;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

/**
 * @ClassName: com.timisakura.shiro.matcher.bobomiccore2
 * @Author: Lion
 * @Date: 2019/3/22  4:11
 * @Description: 凭证匹配器
 *                  1、记录用户登录信息
 *                      包括用户登录时间，用户登录错误次数等。
 *                      采用ehcache进行记录，该信息采用RMI进行同步
 * @version:
 */
@Slf4j
public class RetryLimitHashedCredentialsMatcher extends HashedCredentialsMatcher implements Observer {

    // @Autowired
    private MicoCacheManager vieMallCacheManager;

    private AtomicInteger atomicInteger;

    private String observerName;

    // 用户状态
    private boolean isUnlock = Boolean.FALSE;

    private String username;

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
     * 回调函数
     * @param token 待认证信息
     * @param info  用户信息
     * @return
     */
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        // 获取用户名
        username = (String)token.getPrincipal();

        // 获取登录信息
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
                log.info("用户第一次登录时间: " + now);
                // 缓存用户信息
                passwordRetryCache.getPasswordRetryCache().put(username, userLoginRetryInfo);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        // 检查登录次数
        int userRetryCount = userLoginRetryInfo.getAtomicInteger().incrementAndGet();  //查看incrementAndGet()源码 retryCount + 1
        if (userRetryCount > 1 && userRetryCount < Const.shiro.RETRYNUMBER) {
            // ehcache rmi
            // try {
                // 获取本次登录时间
                // SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                // Date now = new Date();
                // now = simpleFormat.parse(simpleFormat.format(now));
                // logger.info("用户本次登录时间: " + now);
                // long currentTime = now.getTime();
                // if(!isFast(userLoginRetryInfo.getLoginTime(), currentTime)){
                //     // 清除用户登录记录
                //     passwordRetryCache.getPasswordRetryCache().remove(username);
                // }
            // } catch (ParseException e) {
            //     e.printStackTrace();
            // }
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
            log.info("该用户已被锁定" + username);
            // 用户锁定异常
            throw new LockedAccountException();
        }

        // 判断用户账号和密码是否正确
        if (super.doCredentialsMatch(token, info)) {
            // 如果正确 从缓存中将用户登录计数清除
            passwordRetryCache.getPasswordRetryCache().remove(username);
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
            log.info("该用户登录次数已重置: " + username);
        }
    }

    public String getObserverName() {
        return observerName;
    }

    public void setObserverName(String observerName) {
        this.observerName = observerName;
    }

    /**
     * 检索
     * @param username
     * @return
     */
    public int findById(String username) {
        log.info("cache miss, invoke find by id, id:" + username);
        return (int) vieMallCacheManager.get("user", username);
    }

    /**
     * 设置和重置
     * @param user
     * @return
     */
    public ActiveUser save(ActiveUser user) {
        vieMallCacheManager.set("user", user.getId(), user);
        return user;
    }

    // /**
    //  * 清除缓存中的某个数据
    //  * @param name Cache region name
    //  * @param key Cache key
    //  */
    // public  void evict(String name, Object key){
    //     if(name!=null && key != null) {
    //         Cache cache = cacheManager.getCache(name);
    //         if (cache != null)
    //             cache.evict(key);
    //     }
    // }

}