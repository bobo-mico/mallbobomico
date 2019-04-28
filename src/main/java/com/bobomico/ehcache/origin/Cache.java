package com.bobomico.ehcache.origin;

import com.bobomico.ehcache.exception.CacheException;

import java.util.Set;

/**
 * @ClassName: com.bobomico.ehcache.origin.mallbobomico
 * @Author: DELL
 * @Date: 2019/4/11  13:49
 * @Description: 定义了Cache的基本功能
 * @version:
 */
public interface Cache {

    /**
     * 返回缓存的名称
     * @return
     */
    String getName() throws CacheException;

    /**
     * 获取缓存值
     * @param key 缓存key
     * @return 缓存值
     */
    Object get(Object key) throws CacheException;

    /**
     * 设置缓存
     * @param key 缓存key
     * @param value 缓存值
     */
    void put(Object key, Object value) throws CacheException;

    /**
     * 设置计时缓存
     * @param key
     * @param value
     * @param cacheName
     */
    void put(Object key, Object value, String cacheName);

    /**
     * 回收缓存
     * @param key
     */
    void evict(Object key) throws CacheException;

    /**
     * 清空所有缓存
     */
    void clear() throws CacheException;

    /**
     * 获得所有的key
     * @return key集合
     */
    Set<Object> keys() throws CacheException;
}
