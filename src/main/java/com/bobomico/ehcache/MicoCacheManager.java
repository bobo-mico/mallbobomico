package com.bobomico.ehcache;

import com.bobomico.ehcache.exception.CacheException;
import com.bobomico.ehcache.manager.CacheManager;
import com.bobomico.ehcache.origin.Cache;
import com.bobomico.ehcache.test.po.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * @ClassName: com.bobomico.ehcache.mallbobomico
 * @Author: DELL
 * @Date: 2019/4/11  14:01
 * @Description: 对外开放的EhCache管理器
 * @version: beta
 */
@Slf4j
@Component
public class MicoCacheManager {

    private  CacheManager cacheManager;

    /**
     * 获取缓存中的数据
     * @param name Cache region name
     * @param key Cache key
     * @return object
     */
    public Object get(String name, Object key){
        if(name!=null && key != null) {
            Cache cache = cacheManager.getCache(name);
            if (cache != null)
                return cache.get(key);
        }
        return null;
    }

    /**
     * 修改缓存中的数据
     * @param name
     * @param key
     * @param value
     */
    public void update(String name, Object key, Object value){
        if(name!=null && key != null) {
            evict(name, key);
            set(name, key, value);
        }
    }

    /**
     * 写入缓存 带定时功能
     * @param name Cache region name
     * @param key Cache key
     * @param value Cache value
     */
    public  void set(String name, Object key, Object value){
        if(name!=null && key != null && value!=null) {
            Cache cache = cacheManager.getCache(name);
            if (cache != null)
                cache.put(key, value, name);
        }
    }

    /**
     * 清除缓存中的某个数据
     * @param name Cache region name
     * @param key Cache key
     */
    public  void evict(String name, Object key){
        if(name!=null && key != null) {
            Cache cache = cacheManager.getCache(name);
            if (cache != null)
                cache.evict(key);
        }
    }

    /**
     * 批量删除缓存中的一些数据
     * @param name Cache region name
     * @param keys Cache keys
     */
    public  void batchEvict(String name, List<String> keys) {
        if(name!=null && keys != null && keys.size() > 0) {
            Cache cache = cacheManager.getCache(name);
            if (cache != null)
                cache.evict(keys);
        }
    }

    /**
     * Clear the cache
     * @param name cache region name
     */
    public  void clear(String name) throws CacheException {
        Cache cache = cacheManager.getCache(name);
        if(cache != null)
            cache.clear();
    }

    /**
     * list cache keys
     * @param name cache region name
     * @return Key List
     */
    public Set<Object> keys(String name) throws CacheException {
        Cache cache = cacheManager.getCache(name);
        if(cache!=null){
            return cache.keys();
        }
        return null;
    }

    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

}
