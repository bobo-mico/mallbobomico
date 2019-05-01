package com.bobomico.shiro.cache.cachemanager;

import com.bobomico.shiro.cache.dao.IShiroCacheManager;
import lombok.Getter;
import lombok.Setter;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.util.Destroyable;

/**
 * @ClassName: com.bobomico.shiro.cachemanager2.mallbobomico
 * @Author: DELL
 * @Date: 2019/4/8  17:48
 * @Description: 自定义缓存管理器 必须实现CacheManager接口
 *                  这里采用了代理模式，也就是说，真正调用的是IShiroCacheManager的实现类
 *                  该缓存管理器会注入到安全管理器 由shiro自行调用
 * @version:
 */
@Getter
@Setter
public class CustomCacheManager implements CacheManager, Destroyable {

    // 指定自定义缓存管理器现实
    private IShiroCacheManager shrioCacheManager;

    /**
     * 该方法为shiro提供缓存技术
     * @param name 必须指定名字且保证线程安全
     * @param <k>
     * @param <v>
     * @return
     * @throws CacheException
     */
    @Override
    public <k, v> Cache<k, v> getCache(String name) throws CacheException {
        return getShrioCacheManager().getCache(name);
    }

    /**
     * 销毁
     * @throws Exception
     */
    @Override
    public void destroy() throws Exception {
        getShrioCacheManager().destroy();
    }
}