package com.bobomico.redis.utils;

import com.bobomico.redis.common.RedisPool;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

import java.util.Set;

/**
 * @ClassName: com.bobomico.redis.utils.mallbobomico
 * @Author: DELL
 * @Date: 2019/4/8  3:30
 * @Description: Redis的基本操作
 * @version:
 */
@Slf4j
public class RedisUtil {

    /**
     * 设置(或重置) key的有效期
     * @param key
     * @param exTime 单位是秒
     * @return
     */
    public static Long expire(byte[] key, int exTime){
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = RedisPool.getJedis();
            result = jedis.expire(key, exTime);
        } catch (Exception e) {
            log.error("expire key:{} error", key, e);
            RedisPool.returnBrokenResource(jedis);
            return null;
        }
        RedisPool.returnResource(jedis);
        return result;
    }

    /**
     * set 限时key !
     * @param key
     * @param value
     * @param exTime 单位是秒
     * @return
     */
    public static String setEx(byte[] key, byte[] value, int exTime){
        Jedis jedis = null;
        String result = null;
        try {
            jedis = RedisPool.getJedis();
            result = jedis.setex(key, exTime, value);
        } catch (Exception e) {
            log.error("setex key:{} value:{} error", key, value, e);
            RedisPool.returnBrokenResource(jedis);
            return null;
        }
        RedisPool.returnResource(jedis);
        return result;
    }

    /**
     * set!
     * @param key
     * @param value
     * @return
     */
    public static String set(byte[] key, byte[] value){
        Jedis jedis = null;
        String result = null;
        try {
            jedis = RedisPool.getJedis();
            result = jedis.set(key, value);
        } catch (Exception e) {
            log.error("set key:{} value:{} error", key, value, e);
            // 中止操作
            RedisPool.returnBrokenResource(jedis);
            return null;
        }
        RedisPool.returnResource(jedis);
        return result;
    }

    /**
     * get!
     * @param key
     * @return
     */
    public static byte[] get(byte[] key){
        byte[] value = null;
        Jedis jedis = null;
        try {
            jedis = RedisPool.getJedis();
            value = jedis.get(key);
        } catch (Exception e) {
            log.error("get key:{} error",key,e);
            RedisPool.returnBrokenResource(jedis);
            return null;
        }
        RedisPool.returnResource(jedis);
        return value;
    }

    /**
     * del!
     * @param key
     * @return
     */
    public static Long del(byte[] key){
        Jedis jedis = null;
        Long result;
        try {
            jedis = RedisPool.getJedis();
            result = jedis.del(key);
        } catch (Exception e) {
            log.error("del key:{} error", key, e);
            RedisPool.returnBrokenResource(jedis);
            return null;
        }
        RedisPool.returnResource(jedis);
        return result;
    }

    /**
     * flush!
     */
    public static void flushDB() {
        Jedis jedis = null;
        try {
            jedis = RedisPool.getJedis();
            jedis.flushDB();
        } catch (Exception e) {
            log.error("flush error: {}", e);
            RedisPool.returnBrokenResource(jedis);
        }
        RedisPool.returnResource(jedis);
    }

    /**
     * size!
     */
    public static Long dbSize() {
        Jedis jedis = null;
        Long result;
        try {
            jedis = RedisPool.getJedis();
            result = jedis.dbSize();
        } catch (Exception e) {
            log.error("get size error: {}", e);
            RedisPool.returnBrokenResource(jedis);
            return null;
        }
        RedisPool.returnResource(jedis);
        return result;
    }

    /**
     * keys
     * @param pattern
     * @return
     */
    public static Set<byte[]> keys(String pattern) {
        Jedis jedis = null;
        Set<byte[]> result;
        try {
            jedis = RedisPool.getJedis();
            result = jedis.keys(pattern.getBytes());
        } catch (Exception e) {
            log.error("get keys: {} error", pattern, e);
            // 中止操作
            RedisPool.returnBrokenResource(jedis);
            return null;
        }
        RedisPool.returnResource(jedis);
        return result;
    }
}
