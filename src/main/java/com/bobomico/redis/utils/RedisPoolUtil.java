package com.bobomico.redis.utils;

import com.bobomico.redis.common.RedisPool;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

/**
 * @ClassName: com.bobomico.redis.utils.mallbobomico
 * @Author: DELL
 * @Date: 2019/4/8  3:30
 * @Description: Redis的基本操作
 * @version:
 */
@Slf4j
public class RedisPoolUtil {

    /**
     * 设置(或重置) key的有效期
     * @param key
     * @param exTime 单位是秒
     * @return
     */
    public static Long expire(String key, int exTime){
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
     * set 限时key
     * @param key
     * @param value
     * @param exTime 单位是秒
     * @return
     */
    public static String setEx(String key,String value,int exTime){
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
     * set
     * @param key
     * @param value
     * @return
     */
    public static String set(String key, String value){
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
     * get
     * @param key
     * @return
     */
    public static String get(String key){
        Jedis jedis = null;
        String result = null;
        try {
            jedis = RedisPool.getJedis();
            result = jedis.get(key);
        } catch (Exception e) {
            log.error("get key:{} error",key,e);
            RedisPool.returnBrokenResource(jedis);
            return result;
        }
        RedisPool.returnResource(jedis);
        return result;
    }

    /**
     * 删除
     * @param key
     * @return
     */
    public static Long del(String key){
        Jedis jedis = null;
        Long result = null;
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
     * 测试
     * @param args
     */
    public static void main(String[] args) {
        Jedis jedis = RedisPool.getJedis();

        RedisPoolUtil.set("keyTest", "value");

        String value = RedisPoolUtil.get("keyTest");

        RedisPoolUtil.setEx("keyex", "valueex", 60*10);

        RedisPoolUtil.expire("keyTest", 60*20);

        RedisPoolUtil.del("keyTest");

        String aaa = RedisPoolUtil.get(null);

        System.out.println(aaa);
        System.out.println("end");
    }
}
