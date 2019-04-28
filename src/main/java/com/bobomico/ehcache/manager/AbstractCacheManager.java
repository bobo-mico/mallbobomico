package com.bobomico.ehcache.manager;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.bobomico.ehcache.origin.Cache;
import org.springframework.beans.factory.InitializingBean;

/**
 * @ClassName: com.bobomico.ehcache.manager.mallbobomico
 * @Author: DELL
 * @Date: 2019/4/11  13:53
 * @Description: 缓存管理器的基本实现
 * @version:
 */
public abstract class AbstractCacheManager implements CacheManager , InitializingBean {

    private final ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap(16);

    private Set<String> cacheNames = new LinkedHashSet(16);

    /**
     *
     */
    @Override
    public void afterPropertiesSet() {
        Collection<? extends Cache> caches = loadCaches();
        // Preserve the initial order of the cache names
        this.cacheMap.clear();
        this.cacheNames.clear();
        for (Cache cache : caches) {
            addCache(cache);
        }
    }

    /**
     *
     * @param cache
     */
    protected final void addCache(Cache cache) {
        this.cacheMap.put(cache.getName(),cache);
        this.cacheNames.add(cache.getName());
    }

    @Override
    public Cache getCache(String name) {
        return this.cacheMap.get(name);
    }

    @Override
    public Collection<String> getCacheNames() {
        return Collections.unmodifiableSet(this.cacheNames);
    }

    /**
     * Load the initial caches for this cache manager.
     * <p>Called by {@link #afterPropertiesSet()} on startup.
     * The returned collection may be empty but must not be {@code null}.
     */
    protected abstract Collection<? extends Cache> loadCaches();

}
