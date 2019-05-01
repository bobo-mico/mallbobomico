package com.bobomico.shiro.cache.dao.impl;

import com.bobomico.shiro.cache.cachemanager.RedisShiroCache;
import com.bobomico.shiro.cache.dao.IShiroCacheManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @ClassName: com.bobomico.shiro.cachemanager2.mallbobomico
 * @Author: DELL
 * @Date: 2019/4/8  17:51
 * @Description: 缓存管理器代理现实
 *                  目的是为了将安全管理器的缓存实现指向Redis远程字典服务
 * @version:
 */
@Component("redisCacheManagerImpl")
@Slf4j
public class RedisCacheManagerImpl implements IShiroCacheManager {

    // 可用redisUtil替代
    // private RedisManager redisManager;

    // Redis连接池
    // @Autowired
    // private RedisPool redisPool;

    // fast lookup by name map 按名称映射快速查找
    private final ConcurrentMap<String, Cache> caches = new ConcurrentHashMap();

    /**
     * 获取具有指定名称的缓存。如果还不存在具有该名称的缓存，将使用该名称创建一个新的缓存并返回。
     * 该方法需保证线程安全。
     * SESSIONDAO和REALM都是该方法的消费者。
     *         com.bobomico.shiro.realm.EmailRealm.authorizationCache
     *         com.bobomico.shiro.realm.UserPassRealm.authorizationCache
     *         ACTIVE_SESSION_CACHE_BOBOMICO
     * todo 在集群环境下还未测试。
     *
     * Acquires the cache with the specified <code>name</code>.
     * If a cache does not yet exist with that name, a new one
     * will be created with that name and returned.
     *
     * @param name the name of the cache to acquire.
     * @return the Cache with the given name
     * @throws CacheException if there is an error acquiring the Cache instance.
     */
    @Override
    public <k, v> Cache<k, v> getCache(String name) {
        log.debug("get instance of RedisCache, name: " + name);
        Cache c = caches.get(name);
        if (c == null) {
            // initialize the Redis manager instance
            // redisManager.init();

            // create a new cache instance
            // todo 在这里实现不同的缓存技术 c = new MangoDBShiroCache<k, v>(name, redisManager);
            c = new RedisShiroCache<k, v>(name);
            // add it to the cache collection
            caches.put(name, c);
        }
        return c;
    }

    /**
     * todo 销毁
     */
    @Override
    public void destroy() {
        log.debug("destroy the cache");
        // redisManager.init();
        // redisManager.flushDB();
    }

    // public RedisManager getRedisManager() {
    //     return redisManager;
    // }
    //
    // public void setRedisManager(RedisManager redisManager) {
    //     this.redisManager = redisManager;
    // }

}