package com.bobomico.ehcache.origin;

import com.bobomico.ehcache.exception.CacheException;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @ClassName: com.bobomico.ehcache.mallbobomico
 * @Author: DELL
 * @Date: 2019/4/11  13:48
 * @Description: 缓存的基本实现
 * @version:
 */
public class EhCacheAware implements Cache {

    private final Ehcache cache;

    /**
     * Create an {@link EhCacheAware} instance.
     * @param ehcache backing Ehcache instance
     */
    public EhCacheAware(Ehcache ehcache) {
        if (ehcache == null) {
            throw new CacheException("error ehcache");
        }
        this.cache = ehcache;
    }

    /**
     * 返回缓存的名称
     * @return
     * @throws CacheException
     */
    @Override
    public String getName() throws CacheException {
        return this.cache.getName();
    }

    /**
     * 获取缓存值
     * @param key 缓存key
     * @return
     * @throws CacheException
     */
    @Override
    public Object get(Object key) throws CacheException {
        if (key == null) {
            return null;
        } else {
            Element element = cache.get(key);
            if (element == null) {
                return null;
            } else {
                return element.getObjectValue();
            }
        }
    }

    /**
     * 设置缓存
     * @param key 缓存key
     * @param value 缓存值
     * @throws CacheException
     */
    @Override
    public void put(Object key, Object value) throws CacheException {
        this.cache.put(new Element(key, value));
    }

    /**
     * 不重置定时缓存
     * @param key
     * @param value
     * @param cacheName
     */
    @Override
    public void put(Object key, Object value, String cacheName) {
        Element element = this.cache.get(key);
        if (element == null) {
            element = new Element(key, value);
        } else {
            if (element.getTimeToLive() > 0 && element.getTimeToIdle() == 0) {
                element = new Element(
                        key, value, 1L, element.getCreationTime(), element.getLastAccessTime(),
                        element.getLastUpdateTime(), element.getHitCount());
            } else {
                element = new Element(key, value);
            }
        }
        this.cache.put(element);
    }

    /**
     * 回收缓存
     * @param key
     * @throws CacheException
     */
    @Override
    public void evict(Object key) throws CacheException {
        cache.remove(key);
    }

    /**
     * 清空所有缓存
     * @throws CacheException
     */
    @Override
    public void clear() throws CacheException {
        this.cache.removeAll();
    }

    /**
     * 获得所有的key
     * @return
     * @throws CacheException
     */
    @Override
    public Set<Object> keys() throws CacheException {
        List<Object> keys = cache.getKeys();
        if (!(keys == null || keys.isEmpty())) {
            return Collections.unmodifiableSet(new LinkedHashSet<Object>(keys));
        } else {
            return Collections.emptySet();
        }
    }

    /**
     * 获取Element个数
     * @return
     * @throws CacheException
     */
    public long size() throws CacheException {
        return cache.getSize();
    }
}
