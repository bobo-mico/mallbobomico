package com.bobomico.shiro.util;

import com.bobomico.ehcache.manager.EhCacheManager;
import com.bobomico.ehcache.origin.Cache;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.ehcache.EhCacheCache;

/**
 * @ClassName: com.bobomico.ehcache.mallbobomico
 * @Author: DELL
 * @Date: 2019/4/11  18:30
 * @Description: 用户尝试登录次数
 * @version:
 */
public class ServiceBase {

    @Autowired
    protected EhCacheManager ehCacheManager;

    protected Element getCacheValue(String cacheName, String key) {
        Ehcache ehcache = this.getEhcache(cacheName);
        return ehcache.get(key);
    }

    protected void removeCache(String cacheName, String key) {
        Ehcache ehcache = this.getEhcache(cacheName);
        ehcache.remove(key);
    }

    protected void setCache(String cacheName, String key, Object value) {
        Ehcache ehcache = this.getEhcache(cacheName);
        Element element = ehcache.get(key);
        if (element == null) {
            element = new Element(key, value);
        } else {
            if (element.getTimeToLive() > 0 && element.getTimeToIdle() == 0) {
                element = new Element(key, value, 1L, element.getCreationTime(), element.getLastAccessTime(),
                        element.getLastUpdateTime(), element.getHitCount());
            } else {
                element = new Element(key, value);
            }
        }
        ehcache.put(element);
    }

    protected Ehcache getEhcache(String cacheName) {
        Cache cache = ehCacheManager.getCache(cacheName);
        EhCacheCache ehCacheCache = (EhCacheCache) cache;
        Ehcache nativeCache = ehCacheCache.getNativeCache();
        return nativeCache;
    }
}
