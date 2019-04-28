package com.bobomico.pojo;

import com.bobomico.ehcache.MicoCacheManager;

import java.util.HashSet;
import java.util.Set;

/**
 * @ClassName: com.bobomico.pojo.mallbobomico
 * @Author: DELL
 * @Date: 2019/4/11  14:23
 * @Description:
 * @version:
 */
public class UserServiceImpl implements UserService {

    private MicoCacheManager vieMallCacheManager = new MicoCacheManager();

    Set<ActiveUser> users = new HashSet();

    @Override
    public ActiveUser save(ActiveUser user) {
        users.add(user);
        vieMallCacheManager.set("user", user.getId(), user);
        return user;
    }

    @Override
    public ActiveUser update(ActiveUser user) {
        users.remove(user);
        users.add(user);
        vieMallCacheManager.set("user", user.getId(), user);
        return user;
    }

    @Override
    public ActiveUser delete(ActiveUser user) {
        users.remove(user);
        vieMallCacheManager.evict("user", user.getId());
        return user;
    }

    @Override
    public void deleteAll() {
        users.clear();
        vieMallCacheManager.clear("user");
    }

    @Override
    public ActiveUser findById(Long id) {
        System.out.println("cache miss, invoke find by id, id:" + id);
        return (ActiveUser) vieMallCacheManager.get("user", id);
    }

    public void setVieMallCacheManager(MicoCacheManager vieMallCacheManager) {
        this.vieMallCacheManager = vieMallCacheManager;
    }
}
