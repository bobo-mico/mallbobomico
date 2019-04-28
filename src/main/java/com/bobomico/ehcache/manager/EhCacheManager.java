package com.bobomico.ehcache.manager;

import java.util.Collection;
import java.util.LinkedHashSet;

import com.bobomico.ehcache.origin.EhCacheAware;
import com.bobomico.ehcache.origin.Cache;
import net.sf.ehcache.Ehcache;

/**
 * @ClassName: com.bobomico.ehcache.manager.mallbobomico
 * @Author: DELL
 * @Date: 2019/4/11  13:59
 * @Description:
 * @version:
 */
public class EhCacheManager extends AbstractCacheManager {

    private net.sf.ehcache.CacheManager cacheManager;

    /**
     * Create a new EhCacheCacheManager, setting the target EhCache CacheManager
     * through the {@link #setCacheManager} bean property.
     */
    public EhCacheManager() {
    }

    /**
     * Create a new EhCacheCacheManager for the given backing EhCache CacheManager.
     * @param cacheManager the backing EhCache {@link net.sf.ehcache.CacheManager}
     */
    public EhCacheManager(net.sf.ehcache.CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    /**
     * Set the backing EhCache {@link net.sf.ehcache.CacheManager}.
     */
    public void setCacheManager(net.sf.ehcache.CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    /**
     * Return the backing EhCache {@link net.sf.ehcache.CacheManager}.
     */
    public net.sf.ehcache.CacheManager getCacheManager() {
        return this.cacheManager;
    }

    /**
     *
     * @return
     */
    @Override
    protected Collection<? extends Cache> loadCaches() {
        String[] names = getCacheManager().getCacheNames();
        Collection<Cache> caches = new LinkedHashSet<Cache>(names.length);
        for (String name : names) {
            caches.add(new EhCacheAware(getCacheManager().getEhcache(name)));
        }
        return caches;
    }

    /**
     *
     * @param name
     * @return
     */
    @Override
    public Cache getCache(String name) {
        Cache cache = super.getCache(name);
        if (cache == null) {
            Ehcache ehcache = this.cacheManager.getEhcache(name);
            if (ehcache != null) {
                addCache(new EhCacheAware(ehcache));
                cache = super.getCache(name); // potentially decorated
            }
        }
        return cache;
    }
}