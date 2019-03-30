package com.bobomico.shiro.cache;

import com.bobomico.bo.UserLoginRetryInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;

/**
 * @ClassName: com.bobomico.shiro.cache.mallbobomico
 * @Author: DELL
 * @Date: 2019/3/27  15:36
 * @Description: 缓存用户登录信息
 * @version:
 */
public class PasswordRetryCache {
    private Cache<String, UserLoginRetryInfo> passwordRetryCache;

    public Cache<String, UserLoginRetryInfo> getPasswordRetryCache() {
        return passwordRetryCache;
    }

    private void setPasswordRetryCache(Cache<String, UserLoginRetryInfo> passwordRetryCache) {
        this.passwordRetryCache = passwordRetryCache;
    }

    public PasswordRetryCache(CacheManager cacheManager) {
        // 初始化缓存
        passwordRetryCache = cacheManager.getCache("passwordRetryCache");
    }
}
