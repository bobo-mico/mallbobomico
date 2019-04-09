package com.bobomico.shiro.cachemanager2.cachemanager;

import com.bobomico.shiro.cachemanager2.util.RedisManager;
import com.bobomico.shiro.cachemanager2.util.SerializeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @ClassName: com.bobomico.shiro.cachemanager2.mallbobomico
 * @Author: DELL
 * @Date: 2019/4/8  17:55
 * @Description: 必须实现Cache接口才能作为shiro的缓存管理器
 *                  在这里修改cookie的实现
 * @version:
 */
@Slf4j
public class RedisShiroCache<K, V> implements Cache<K, V> {

    private final String REDIS_SHIRO_CACHE = "shiro-cache:";

    // redisUtil
    private RedisManager cache;

    private String name;

    public RedisShiroCache(String name, RedisManager jedisManager) {
        this.name = name;
        this.cache = jedisManager;
    }

    @Override
    public V get(K key) throws CacheException {
        try {
            if (key == null) {
                return null;
            } else {
                byte[] rawValue = cache.get(getByteKey(key));
                V value = (V) SerializeUtils.deserialize(rawValue);
                return value;
            }
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    @Override
    public V put(K key, V value) throws CacheException {
        try {
            cache.set(getByteKey(key), SerializeUtils.serialize(value));
            return value;
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    @Override
    public V remove(K key) throws CacheException {
        try {
            V previous = get(key);
            cache.del(getByteKey(key));
            return previous;
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    @Override
    public void clear() throws CacheException {
        try {
            String preKey = this.REDIS_SHIRO_CACHE + "*";
            byte[] keysPattern = preKey.getBytes();
            cache.del(keysPattern);
            cache.flushDB();
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    @Override
    public int size() {
        if (keys() == null)
            return 0;
        return keys().size();
    }

    @Override
    public Set<K> keys() {
        try {
            Set<byte[]> keys = cache.keys(this.REDIS_SHIRO_CACHE + "*");
            if (CollectionUtils.isEmpty(keys)) {
                return Collections.emptySet();
            } else {
                Set<K> newKeys = new HashSet<K>();
                for (byte[] key : keys) {
                    newKeys.add((K) key);
                }
                return newKeys;
            }
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    @Override
    public Collection<V> values() {
        try {
            Set<byte[]> keys = cache.keys(this.REDIS_SHIRO_CACHE + "*");
            if (!CollectionUtils.isEmpty(keys)) {
                List<V> values = new ArrayList<V>(keys.size());
                for (byte[] key : keys) {
                    V value = get((K) key);
                    if (value != null) {
                        values.add(value);
                    }
                }
                return Collections.unmodifiableList(values);
            } else {
                return Collections.emptyList();
            }
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    /**
     * 自定义relm中的授权/认证的类名加上授权/认证英文名字
     * @return
     */
    public String getName() {
        if (name == null)
            return "";
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获得byte[]型的key
     * @param key
     * @return
     */
    private byte[] getByteKey(K key) {
        if (key instanceof String) {
            String preKey = this.REDIS_SHIRO_CACHE + getName() + ":" + key;
            return preKey.getBytes();
        } else {
            return SerializeUtils.serialize(key);
        }
    }
}