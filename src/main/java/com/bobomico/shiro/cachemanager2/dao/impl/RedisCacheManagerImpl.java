package com.bobomico.shiro.cachemanager2.dao.impl;

import com.bobomico.shiro.cachemanager2.cachemanager.RedisShiroCache;
import com.bobomico.shiro.cachemanager2.dao.IShiroCacheManager;
import com.bobomico.shiro.cachemanager2.util.RedisManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.cache.Cache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @ClassName: com.bobomico.shiro.cachemanager2.mallbobomico
 * @Author: DELL
 * @Date: 2019/4/8  17:51
 * @Description: 实现了缓存管理器接口
 *                  目的是为了将安全管理器的缓存实现指向Redis远程字典服务
 * @version:
 */
@Slf4j
public class RedisCacheManagerImpl implements IShiroCacheManager {

    // 可用redisUtil替代
    private RedisManager redisManager;

    // fast lookup by name map 按名称映射快速查找
    // Map并发集合 避免多次查询远程字典服务而使用一个map集合在当前JVM环境中管理起来 同时保证单例管理 有连接池之后这个东西是不是可以不用了?
    private final ConcurrentMap<String, Cache> caches = new ConcurrentHashMap();

    /**
     * getCache的主要消费对象是realm
     * todo 这里在集群环境下会不会有问题 ?
     * @param name
     * @param <k>
     * @param <v>
     * @return
     */
    @Override
    public <k, v> Cache<k, v> getCache(String name) {
        // com.bobomico.shiro.realm.EmailRealm.authorizationCache
        // com.bobomico.shiro.realm.UserPassRealm.authorizationCache
        log.debug("get instance of RedisCache, name: " + name);

        // 检索是否存在该realm的缓存信息
        Cache c = caches.get(name);
        if (c == null) {
            // initialize the Redis manager instance
            redisManager.init();

            // todo 在这里实现不同的session同步策略
            // create a new cache instance
            c = new RedisShiroCache<k, v>(name, redisManager);
            // add it to the cache collection
            caches.put(name, c);
        }
        return c;
    }

    /**
     * 销毁
     */
    @Override
    public void destroy() {
        log.debug("destroy the cache");
        redisManager.init();
        redisManager.flushDB();
    }

    /**
     * 可用redisUtil替换
     * @return
     */
    public RedisManager getRedisManager() {
        return redisManager;
    }

    public void setRedisManager(RedisManager redisManager) {
        this.redisManager = redisManager;
    }

}