package com.bobomico.ehcache.manager;

import com.bobomico.ehcache.origin.Cache;

import java.util.Collection;

/**
 * @ClassName: com.bobomico.ehcache.manager.mallbobomico
 * @Author: DELL
 * @Date: 2019/4/11  13:54
 * @Description: 缓存管理器接口
 *                  灵感来源自Redis的Cache接口
 *                  各不同的缓存技术实现各自的CacheManager
 * @version:
 */
public interface CacheManager {

    /**
     * Return the cache associated with the given name.
     * @param name the cache identifier (must not be {@code null})
     * @return the associated cache, or {@code null} if none found
     */
    Cache getCache(String name);

    /**
     * Return a collection of the cache names known by this manager.
     * @return the names of all caches known by the cache manager
     */
    Collection<String> getCacheNames();

}
