// package com.bobomico.shiro.cachemanager;
//
// import lombok.extern.slf4j.Slf4j;
// import org.apache.shiro.cache.Cache;
// import org.apache.shiro.cache.CacheException;
// import org.apache.shiro.cache.CacheManager;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Component;
//
// /**
//  * @ClassName: com.bobomico.shiro.cachemanager.mallbobomico
//  * @Author: DELL
//  * @Date: 2019/4/8  8:01
//  * @DESCRIPTION: 自定义缓存管理器
//  * @version:
//  */
// @Component
// @Slf4j
// public class RedisCacheManager implements CacheManager {
//
//     @Autowired
//     private Cache shiroRedisCache;
//
//     @Override
//     public <K, V> Cache<K, V> getCache(String s) throws CacheException {
//         return shiroRedisCache;
//     }
// }
