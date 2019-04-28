package com.bobomico.controller.cacheTest;

import com.bobomico.common.ServerResponse;
import com.bobomico.controller.vo.UserLoginVo;
import com.bobomico.ehcache.test.dao.UserDAO;
import com.bobomico.ehcache.test.po.User;
import com.bobomico.ehcache.test.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName: com.bobomico.controller.cacheTest.mallbobomico
 * @Author: DELL
 * @Date: 2019/4/29  3:12
 * @Description: 测试一下ehcache和spring整合
 * @version:
 */
@RestController()
@RequestMapping("/ehcache/")
public class Ehcache {
    @Autowired
    private UserService userService;

    /**
     * 新增数据
     * @return
     */
    @RequestMapping("add.do")
    public ServerResponse<User> add() {
        User u = new User();
        u.setId(1);
        u.setName("波波&米可流浪动物救援平台");
        u.setPassword("1234qwer");
        userService.addUser(u);
        return ServerResponse.createBySuccess(u);
    }

    /**
     * 删除数据
     * @return
     */
    @RequestMapping("del.do")
    public ServerResponse del(Integer id) {
        userService.removeById(id);
        return ServerResponse.createBySuccess("删除成功");
    }

    /**
     * 查询数据
     * @return
     */
    @RequestMapping("select.do")
    public ServerResponse select(Integer id) {
        User u = userService.findById(id);
        return ServerResponse.createBySuccess(u);
    }

    /**
     * 查询所有数据
     * @return
     */
    @RequestMapping("select_all.do")
    public ServerResponse<List<User>> selectAll() {
        List<User> users = userService.getAll();
        return ServerResponse.createBySuccess(users);
    }
}