package com.bobomico.ehcache.test.service;

import com.bobomico.ehcache.test.dao.UserDAO;
import com.bobomico.ehcache.test.po.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName: com.bobomico.ehcache.test.dao.impl.mallbobomico
 * @Author: DELL
 * @Date: 2019/4/29  2:50
 * @Description:
 * @version:
 */
@Slf4j
@Service("userService")
public class UserService {

    @Autowired
    private UserDAO userDao;

    /**
     * 查询所有数据，不需要key
     */
    @Cacheable(value="serviceCache")
    public List<User> getAll(){
        printInfo("getAll");
        return userDao.userList;
    }

    /**
     * 根据ID来获取数据，ID可能是主键也可能是唯一键
     *  注解 Cacheable 由Spring代理查询缓存
     * @param id
     * @return
     */
    @Cacheable(value="serviceCache", key="#id")
    public User findById(Integer id){
        printInfo(id);
        return userDao.findById(id);
    }

    /**
     * 通过id进行删除
     *  注解 CacheEvict 由Spring代理删除缓存
     * @param id
     */
    @CacheEvict(value="serviceCache", key="#id")
    public void removeById(Integer id){
        userDao.removeById(id);
    }

    /**
     * 添加数据
     * @param user
     */
    public void addUser(User user){
        // 数据校验
        if(user != null && user.getId() != null){
            userDao.addUser(user);
        }
    }

    /**
     * 修改
     * key 支持条件，包括 属性condition ，可以 id < 20 等等
     */
    @CacheEvict(value="serviceCache", key="#u.id")
    public void updateUser(User u){
        removeById(u.getId());
        userDao.updateUser(u);
    }

    /**
     * 清空缓存 - 但是没操作数据库
     * allEntries 表示调用之后，清空缓存，默认false,
     * 还有个beforeInvocation 属性，表示先清空缓存，再进行查询
     */
    @CacheEvict(value="serviceCache", allEntries=true)
    public void removeAll(){
        System.out.println("清除所有缓存");
    }

    /**
     * 信息
     * @param str
     */
    private void printInfo(Object str){
        log.info(">> 非缓存查询 " + str);
    }
}