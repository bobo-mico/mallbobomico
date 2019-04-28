package com.bobomico.shiro.cachemanager2.cachemanager;

import com.bobomico.redis.utils.RedisUtil;
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
 * @Description: 要为shiro提供缓存服务必须实现Cache接口
 *               在这里可以定义sessionDAO的逻辑
 *               该类可以有多个实例，为不同的realm提供服务
 *               K 为String的name
 *               V 为reids实现
 * @version:
 */
@Slf4j
public class RedisShiroCache<K, V> implements Cache<K, V> {

    // redis中的缓存前缀
    private final String SHIRO_CACHE_TOKEN_PREFIX = "shiro-cache:";
    
    private String name;

    // public RedisShiroCache(String name, RedisManager jedisManager) {
    //     this.name = name;
    //     this.cache = jedisManager;
    // }

    public RedisShiroCache(String name) {
        this.name = name;
    }

    /**
     * 从缓存中获取值
     * @param key
     * @return
     * @throws CacheException
     */
    @Override
    public V get(K key) throws CacheException {
        try {
            if (key == null) {
                return null;
            } else {
                // 原始数据
                // byte[] rawValue = cache.get(getByteKey(key));

                // 原始数据
                byte[] rawValue = RedisUtil.get(getByteKey(key));
                V value = (V) SerializeUtils.deserialize(rawValue);
                return value;
            }
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    /**
     * 添加缓存
     * @param key
     * @param value
     * @return
     * @throws CacheException
     */
    @Override
    public V put(K key, V value) throws CacheException {
        try {
            RedisUtil.set(getByteKey(key), SerializeUtils.serialize(value));
            return value;
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    /**
     * 从缓存中删除
     * @param key
     * @return
     * @throws CacheException
     */
    @Override
    public V remove(K key) throws CacheException {
        try {
            // 先查询
            V previous = get(key);
            if(previous == null){
                return null;
            }
            // 删除并返回该对象
            RedisUtil.del(getByteKey(key));
            return previous;
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    /**
     * 清理缓存
     * @throws CacheException
     */
    @Override
    public void clear() throws CacheException {
        try {
            String preKey = this.SHIRO_CACHE_TOKEN_PREFIX + "*";
            byte[] keysPattern = preKey.getBytes();
            RedisUtil.del(keysPattern);
            // RedisUtil.flushDB();
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    /**
     * 缓存统计
     * @return
     */
    @Override
    public int size() {
        if (keys() == null)
            return 0;
        return keys().size();
    }

    /**
     * 查询所有key
     * @return
     */
    @Override
    public Set<K> keys() {
        try {
            Set<byte[]> keys = RedisUtil.keys(this.SHIRO_CACHE_TOKEN_PREFIX + "*");
            if(CollectionUtils.isEmpty(keys)) {
                return Collections.emptySet();
            }else{
                // 强转
                Set<K> newKeys = new HashSet();
                for (byte[] key : keys) {
                    newKeys.add((K) key);
                }
                return newKeys;
            }
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    /**
     * 查询所有value
     * @return
     */
    @Override
    public Collection<V> values() {
        try {
            Set<byte[]> keys = RedisUtil.keys(this.SHIRO_CACHE_TOKEN_PREFIX + "*");
            if (!CollectionUtils.isEmpty(keys)) {
                List<V> values = new ArrayList(keys.size());
                for (byte[] key : keys) {
                    V value = get((K) key);
                    if (value != null) {
                        values.add(value);
                    }
                }
                return Collections.unmodifiableList(values);    // unmodifiableList 不可变集合
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
     * 小工具 - 获得byte[]型的key
     * @param key
     * @return
     */
    private byte[] getByteKey(K key) {
        if (key instanceof String) {
            String preKey = this.SHIRO_CACHE_TOKEN_PREFIX + getName() + ":" + key;
            return preKey.getBytes();
        } else {
            return SerializeUtils.serialize(key);
        }
    }
}