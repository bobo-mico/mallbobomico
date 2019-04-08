package com.bobomico.shiro.cachemanager2.cache;

import org.apache.shiro.cache.Cache;

/**
 * @ClassName: com.bobomico.shiro.cachemanager2.mallbobomico
 * @Author: DELL
 * @Date: 2019/4/8  17:49
 * @Description:
 * @version:
 */
public interface ShiroCacheManager {
    <k, v> Cache<k, v> getCache(String name);
    void destroy();
}
