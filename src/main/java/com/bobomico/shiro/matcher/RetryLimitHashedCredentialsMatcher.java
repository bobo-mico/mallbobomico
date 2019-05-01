package com.bobomico.shiro.matcher;

import com.bobomico.common.Const;
import com.bobomico.dao.po.SysUserLogin;
import com.bobomico.ehcache.MicoCacheManager;
import com.bobomico.notify.observer.Observer;
import com.bobomico.notify.subject.Subject;
import com.bobomico.notify.UserStatusSubject;
import com.bobomico.quartz.exception.InvokeRepeatingException;
import com.bobomico.quartz.scheduler.UserStatusScheduler;
import com.bobomico.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName: com.timisakura.shiro.matcher.bobomiccore2
 * @Author: Lion
 * @Date: 2019/3/22  4:11
 * @Description: 凭证匹配器
 *                  需维护一组用户登录状态
 *                  包括用户登录时间，用户登录错误次数等。
 *                  采用ehcache进行记录，该信息采用RMI进行集群同步
 * @version:
 */
@Slf4j
public class RetryLimitHashedCredentialsMatcher extends HashedCredentialsMatcher implements Observer {

    // 订阅者
    private String observerName;
    // 因为保存在订阅者列表中 所以引用会被更新
    private String username;
    @Autowired
    private MicoCacheManager micoCacheManager;
    @Autowired
    private IUserService iUserService;
    @Autowired
    private UserStatusScheduler userScheduler;

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
        // 重复尝试次数
        int tryCount = getCount(username);

        if (tryCount < Const.shiro.RETRYNUMBER && tryCount != 1) {
            // 更新用户登录缓存信息
            micoCacheManager.update(Const.cache.CACHE_REGION, username, new AtomicInteger(tryCount));
        }else if (tryCount == Const.shiro.RETRYNUMBER){
            // 数据库锁定用户
            SysUserLogin sysUserLogin = iUserService.findSysUserByTokenType(token);
            if (sysUserLogin != null && Const.userStatus.LOCKED != sysUserLogin.getUserStats()) {
                // 修改数据库的状态字段为锁定
                sysUserLogin.setUserStats((byte) Const.userStatus.LOCKED);
                // 调用服务层更新用户状态
                iUserService.updateUserStatus(sysUserLogin);
                // 创建定时解锁任务
                try {
                    userScheduler.unlockJob(sysUserLogin.getSysUserId());
                } catch (InvokeRepeatingException e) {
                    e.printStackTrace();
                }
            }
            log.info("{}用户已被锁定", username);
            // 用户锁定异常
            throw new LockedAccountException();
        }else if(tryCount > Const.shiro.RETRYNUMBER){
            throw new LockedAccountException();
        }

        // 判断用户账号和密码是否正确
        if (super.doCredentialsMatch(token, info)) {
            // 如果正确 从缓存中将用户登录信息清除
            micoCacheManager.evict(Const.cache.CACHE_REGION, username);
            return Boolean.TRUE;
        }else{
            if(tryCount == 1){
                // 凭证错误异常
                throw new IncorrectCredentialsException();
            }
            // 多次错误异常
            throw new ExcessiveAttemptsException();
        }
    }

    /**
     * 获取用户重复登录次数
     * @param username
     * @return
     */
    private int getCount(String username){
        // 用户尝试次数
        int tryCount = 1;
        // 登录信息
        AtomicInteger loginInfo;
        try{
            // 获取登录信息
            loginInfo = (AtomicInteger) micoCacheManager.get(Const.cache.CACHE_REGION, username);
            if (loginInfo == null) {
                // 创建用户登录缓存信息
                micoCacheManager.set(Const.cache.CACHE_REGION, username, new AtomicInteger(1));
            }else{
                // 查看incrementAndGet()源码 retryCount + 1
                tryCount = loginInfo.incrementAndGet();
            }
        }catch (NullPointerException e){
            log.debug("ehcache缓存异常 获取不到对象");
            throw e;
        }
        return tryCount;
    }

    /**
     * 同步缓存中的用户登录状态
     * 细节 不要在update中取消订阅 因为map在迭代时改变状态将终止迭代 容易引起错误
     * 细节 记得状态的同步
     * @param subjectIsLock
     */
    @Override
    public void update(Subject subjectIsLock) {
        if(((UserStatusSubject)subjectIsLock).isUnlock()){
            // 用户登录次数记录清空
            micoCacheManager.evict(Const.cache.CACHE_REGION, username);
            log.info("用户 {} 登录信息已清空", username);
        }
    }

    /**
     * 构造函数 - 订阅
     * @param userStatusSubject
     */
    public RetryLimitHashedCredentialsMatcher(UserStatusSubject userStatusSubject) {
        // 设置订阅名
        this.setObserverName("RetryLimitHashedCredentialsMatcherObserver");
        // 注册订阅
        userStatusSubject.attach(this);
    }

    /**
     * 获取订阅者名称
     * @return
     */
    public String getObserverName() {
        return this.observerName;
    }

    /**
     * 设置订阅者名称
     * @param observerName
     */
    public void setObserverName(String observerName) {
        this.observerName = observerName;
    }
}

// EOF