package com.bobomico.ehcache.test.dao;

import com.bobomico.ehcache.test.po.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: com.bobomico.ehcache.test.dao.mallbobomico
 * @Author: DELL
 * @Date: 2019/4/29  2:44
 * @Description: 模拟数据库
 * @version:
 */
@Slf4j
@Repository("userDao")
public class UserDAO {
    public List<User> userList = initUsers();

    /**
     * 根据id查询用户
     * @param id
     * @return
     */
    public User findById(Integer id) {
        log.info(">> 执行数据库 查询" + id + "操作");
        for(User user : userList){
            if(user.getId().equals(id)){
                return user;
            }
        }
        return null;
    }

    /**
     * 删除用户
     * @param id
     */
    public void removeById(Integer id) {
        log.info(">> 执行数据库 删除操作");
        User delUser = null;
        for(User user : userList){
            if(user.getId().equals(id)){
                delUser = user;
            }
        }
        userList.remove(delUser);
    }

    /**
     * 新增用户
     * @param user
     */
    public void addUser(User user){
        log.info(">> 执行数据库 新增操作");
        userList.add(user);
    }

    /**
     * 更新 - 执行的是添加
     * @param user
     */
    public void updateUser(User user){
        addUser(user);
    }

    /**
     * 模拟数据源
     * @return
     */
    private List<User> initUsers(){
        List<User> users = new ArrayList<User>();
        User u1 = new User(1,"张三","123");
        User u2 = new User(2,"李四","124");
        User u3 = new User(3,"王五","125");
        users.add(u1);
        users.add(u2);
        users.add(u3);
        return users;
    }
}