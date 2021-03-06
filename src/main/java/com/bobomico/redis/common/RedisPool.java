package com.bobomico.redis.common;

import com.bobomico.util.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @ClassName: com.bobomico.redis.common.mallbobomico
 * @Author: DELL
 * @Date: 2019/4/7  17:23
 * @Description: jedis连接池
 * @version:
 */
@Slf4j
public class RedisPool {
    // jedis连接池
    private static JedisPool pool;
    // 最大连接数
    private static Integer maxTotal = PropertiesUtil.getIntegerProperty("redis.max.total",20);
    // 在jedispool中最大的idle状态(空闲的)的jedis实例的个数
    private static Integer maxIdle = PropertiesUtil.getIntegerProperty("redis.max.idle",20);
    // 在jedispool中最小的idle状态(空闲的)的jedis实例的个数
    private static Integer minIdle = PropertiesUtil.getIntegerProperty("redis.min.idle",20);
    // 在borrow一个jedis实例的时候，是否要进行验证操作，如果赋值true。则借用的jedis实例肯定是可以用的
    private static Boolean testOnBorrow = PropertiesUtil.getBooleanProperty("redis.test.borrow", Boolean.TRUE);
    // 在return一个jedis实例的时候，是否要进行验证操作，如果赋值true。则放回jedispool的jedis实例肯定是可以用的
    private static Boolean testOnReturn = PropertiesUtil.getBooleanProperty("redis.test.return", Boolean.TRUE);

    private static String redisIp = PropertiesUtil.getStringProperty("redis.ip");
    private static Integer redisPort = PropertiesUtil.getIntegerProperty("redis.port");

    private static void initPool(){
        JedisPoolConfig config = new JedisPoolConfig();

        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);

        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);

        // 连接耗尽的时候，是否阻塞，false会抛出异常，true阻塞直到超时。默认为true。
        config.setBlockWhenExhausted(true);

        pool = new JedisPool(config, redisIp, redisPort, 1000*2);
    }

    static{
        initPool();
    }

    public static Jedis getJedis(){
        return pool.getResource();
    }

    public static void returnBrokenResource(Jedis jedis){
        pool.returnBrokenResource(jedis);
    }

    public static void returnResource(Jedis jedis){
        pool.returnResource(jedis);
    }

    public static void main(String[] args) {
        Jedis jedis = pool.getResource();
        jedis.set("y","y");
        returnResource(jedis);
        // 演示 销毁连接池中的所有连接 一般不要这样做
        pool.destroy();
        System.out.println("program is end");
    }
}
