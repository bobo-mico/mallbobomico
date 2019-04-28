package com.bobomico.ehcache.test;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.PersistentCacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.EntryUnit;
import org.ehcache.config.units.MemoryUnit;

/**
 * @ClassName: com.bobomico.ehcache.test.mallbobomico
 * @Author: DELL
 * @Date: 2019/4/29  1:23
 * @Description: https://www.jianshu.com/p/5a0669d6305e/
 * @version:
 */
public class start {
    /**
     * 这段代码介绍了Ehcache3.0的缓存使用生命周期的一个过程
     * @param args
     */
    public static void main(String[] args) {
        CacheManager cacheManager
                = CacheManagerBuilder.newCacheManagerBuilder() // 1、静态方法CacheManagerBuilder.newCacheManagerBuilder将返回一个新的org.ehcache.config.builders.CacheManagerBuilder的实例。
                .withCache("preConfigured", // 2、当我们要构建一个缓存管理器的时候，使用CacheManagerBuilder来创建一个预配置（pre-configured)缓存。第一个参数是一个别名，用于Cache和Cachemanager进行配合。第二个参数是org.ehcache.config.CacheConfiguration主要用来配置Cache。我们使用org.ehcache.config.builders.CacheConfigurationBuilder的静态方法newCacheConfigurationBuilder来创建一个默认配置实例。
                        CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class, ResourcePoolsBuilder.heap(10)))
                .build();   // 3、最后调用.build方法返回一个完整的实例，当然我们也能使用CacheManager来初始化。

        // 4、在你开始使用CacheManager的时候，需要使用init()方法进行初始化。
        cacheManager.init();

        // 5、我们能取回在第二步中设定的pre-configured别名，我们对于key和要传递的值类型，要求是类型安全的（使用泛型），否则将抛出ClassCastException异常。
        Cache<Long, String> preConfigured = cacheManager.getCache("preConfigured", Long.class, String.class);

        Cache<Long, String> myCache = cacheManager.createCache("myCache",   // 6、可以根据需求，通过CacheManager创建出新的Cache。实例化和完整实例化的Cache将通过CacheManager.getCache API返回。
                CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class, ResourcePoolsBuilder.heap(10)).build());

        // 7、使用put方法存储数据。
        myCache.put(1L, "da one!");

        // 8、使用get方法获取数据。
        String value = myCache.get(1L);
        System.out.println(value);

        // 9、我们可以通过CacheManager.removeCache方法来获取Cache，但是Cache取出来以后CacheManager将会删除自身保存的Cache实例。
        cacheManager.removeCache("preConfigured");

        // 10、close方法将释放CacheManager所管理的缓存资源。
        cacheManager.close();

        /**
         * 关于磁盘持久化
         * 如果您想使用持久化机制，就需要提供一个磁盘存储的位置给CacheManagerBuilder.persistence这个方法，另外在使用的过程中，你还需要定义一个磁盘使用的资源池。
         * 上面的例子其实是分配了非常少的磁盘存储量，不过我们需要注意的是由于存储在磁盘上我们需要做序列化和反序列化，以及读和写的操作。它的速度肯定比内存要慢的多。
         */
        PersistentCacheManager persistentCacheManager = CacheManagerBuilder.newCacheManagerBuilder()
                .with(CacheManagerBuilder.persistence("ehcache/myData"))
                .withCache("", CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class,
                        ResourcePoolsBuilder.newResourcePoolsBuilder()
                            .heap(10, EntryUnit.ENTRIES)
                            .disk(10, MemoryUnit.MB, true))
                        )
                .build(true);

        persistentCacheManager.close();
    }
}
