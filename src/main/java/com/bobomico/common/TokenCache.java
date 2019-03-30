package com.bobomico.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @ClassName: com.bobomico.common.mallbobomico
 * @Author: DELL
 * @Date: 2019/3/29  6:54
 * @Description: Guava 缓存
 * @version:
 */
public class TokenCache {

    private static Logger logger = LoggerFactory.getLogger(TokenCache.class);

    public static final String TOKEN_PREFIX = "token_"; // token前缀

    // 本地缓存 LRU算法 最少使用算法
    private static LoadingCache<String,String> localCache = CacheBuilder
            .newBuilder().initialCapacity(1000)             // 设置缓存的初始化容量
            .maximumSize(10000)                             // 缓存的最大容量 当超过该容量 guava会使用LRU算法清理缓存
            .expireAfterAccess(12, TimeUnit.HOURS)  // 有效期
            .build(new CacheLoader<String, String>() {
                @Override
                public String load(String s) throws Exception {
                    return "null";
                }
            });    // 默认数据加载实现

    public static void setKey(String key,String value){
        localCache.put(key,value);
    }

    public static String getKey(String key){
        String value;
        try {
            value = localCache.get(key);    // 当调用get取值时 如果key没有对应的值 就调用默认数据加载实现 而它会返回一个"null"
            if("null".equals(value)){       // 如果缓存中不存在用户的token 直接返回null
                return null;
            }
            return value;
        }catch (Exception e){
            logger.error("localCache get error", e);
        }
        return null;
    }
}
