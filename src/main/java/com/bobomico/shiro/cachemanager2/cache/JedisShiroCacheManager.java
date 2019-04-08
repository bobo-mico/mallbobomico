package com.bobomico.shiro.cachemanager2.cache;

import com.bobomico.shiro.cachemanager2.util.RedisManager;
import org.apache.shiro.cache.Cache;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @ClassName: com.bobomico.shiro.cachemanager2.mallbobomico
 * @Author: DELL
 * @Date: 2019/4/8  17:51
 * @Description:
 * @version:
 */
@Slf4j
public class JedisShiroCacheManager implements ShiroCacheManager {

    private RedisManager redisManager;

    // fast lookup by name map
    private final ConcurrentMap<String, Cache> caches = new ConcurrentHashMap<String, Cache>();

    @Override
    public <k, v> Cache<k, v> getCache(String name) {
        log.debug("get instance of RedisCache,name: " + name);

        Cache c = caches.get(name);

        if (c == null) {

            // initialize the Redis manager instance
            redisManager.init();

            // create a new cache instance
            c = new JedisShiroCache<k, v>(name, redisManager);

            // add it to the cache collection
            caches.put(name, c);
        }
        return c;
    }

    @Override
    public void destroy() {
        redisManager.init();
        redisManager.flushDB();
    }

    public RedisManager getRedisManager() {
        return redisManager;
    }

    public void setRedisManager(RedisManager redisManager) {
        this.redisManager = redisManager;
    }

}