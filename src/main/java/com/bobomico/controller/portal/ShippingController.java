package com.bobomico.controller.portal;

import com.bobomico.common.ServerResponse;
import com.bobomico.controller.BaseController;
import com.bobomico.dao.po.MallShipping;
import com.bobomico.dao.po.SysUserLogin;
import com.bobomico.service.IShippingService;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @ClassName: com.bobomico.controller.portal.mallbobomico
 * @Author: DELL
 * @Date: 2019/4/4  5:55
 * @Description: 客户端 - 地址管理
 * @version:
 */
@RestController
@RequestMapping("/shipping/")
public class ShippingController extends BaseController {

    @Autowired
    private IShippingService iShippingService;

    @RequestMapping("add.do")
    @RequiresAuthentication
    public ServerResponse add(
            MallShipping shipping, HttpSession session, HttpServletRequest request, HttpServletResponse response){
        SysUserLogin sul = (SysUserLogin) getUserInfo(session, request, response);
        return iShippingService.add(sul.getSysUserId(), shipping);
    }

    @RequestMapping("del.do")
    @RequiresAuthentication
    public ServerResponse del(
            Integer shippingId, HttpSession session, HttpServletRequest request, HttpServletResponse response){
        SysUserLogin sul = (SysUserLogin) getUserInfo(session, request, response);
        return iShippingService.del(sul.getSysUserId(), shippingId);
    }

    @RequestMapping("update.do")
    @RequiresAuthentication
    public ServerResponse update(
            MallShipping shipping, HttpSession session, HttpServletRequest request, HttpServletResponse response){
        SysUserLogin sul = (SysUserLogin) getUserInfo(session, request, response);
        return iShippingService.update(sul.getSysUserId(), shipping);
    }

    @RequestMapping("select.do")
    @RequiresAuthentication
    public ServerResponse<MallShipping> select(
            Integer shippingId, HttpSession session, HttpServletRequest request, HttpServletResponse response){
        SysUserLogin sul = (SysUserLogin) getUserInfo(session, request, response);
        return iShippingService.select(sul.getSysUserId(), shippingId);
    }

    @RequestMapping("list.do")
    @RequiresAuthentication
    public ServerResponse<PageInfo> list(@RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                                         @RequestParam(value = "pageSize",defaultValue = "10")int pageSize,
                                         HttpSession session, HttpServletRequest request, HttpServletResponse response){
        SysUserLogin sul = (SysUserLogin) getUserInfo(session, request, response);
        return iShippingService.list(sul.getSysUserId(), pageNum,pageSize);
    }
}
