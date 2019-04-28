package com.bobomico.ehcache.test;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.Configuration;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.xml.XmlConfiguration;

import java.net.URL;

/**
 * @ClassName: com.bobomico.ehcache.test.mallbobomico
 * @Author: DELL
 * @Date: 2019/4/29  2:04
 * @Description: 使用配置文件创建实例
 * @version:
 */
public class start2 {
    public static void main(String[] args) {
        final URL myUrl = start2.class.getResource("/ehcache/my-config.xml");
        Configuration xmlConfig = new XmlConfiguration(myUrl);
        CacheManager myCacheMananger = CacheManagerBuilder.newCacheManager(xmlConfig);

        myCacheMananger.init();

        // 可以定义泛型
        Cache<String, String> foo = myCacheMananger.getCache("foo", String.class, String.class);

    }
}
