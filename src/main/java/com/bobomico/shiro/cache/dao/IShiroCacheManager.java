package com.bobomico.shiro.cache.dao;

import org.apache.shiro.cache.Cache;

/**
 * @ClassName: com.bobomico.shiro.cachemanager2.mallbobomico
 * @Author: DELL
 * @Date: 2019/4/8  17:49
 * @Description: 缓存管理接口 通过实现该接口扩展不同的Cache实现 如MangoDB
 * @version:
 */
public interface IShiroCacheManager {
    // 获取缓存
    <k, v> Cache<k, v> getCache(String name);
    // 销毁缓存
    void destroy();
}
