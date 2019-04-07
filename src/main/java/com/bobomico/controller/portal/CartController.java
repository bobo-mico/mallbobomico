package com.bobomico.controller.portal;

import com.bobomico.common.Const;
import com.bobomico.common.ServerResponse;
import com.bobomico.controller.BaseController;
import com.bobomico.controller.vo.CartVo;
import com.bobomico.dao.po.SysUserLogin;
import com.bobomico.service.ICartService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @ClassName: com.bobomico.controller.portal.mallbobomico
 * @Author: DELL
 * @Date: 2019/4/1  7:04
 * @Description: 前台 - 购物车
 *
 * @version:
 */
@RestController
@RequestMapping("/cart/")
public class CartController extends BaseController {

    @Autowired
    private ICartService iCartService;

    /**
     * 向购物车添加商品
     * @param productId 商品ID
     * @param count     数量
     * @param session
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("add.do")
    @RequiresAuthentication
    public ServerResponse<CartVo> add(
            Integer productId, Integer count, HttpSession session, HttpServletRequest request, HttpServletResponse response){
        SysUserLogin sul = (SysUserLogin) getUserInfo(session, request, response);
        return iCartService.add(sul.getSysUserId(), productId, count);
    }

    /**
     * 删除购物车商品
     * @param session
     * @param productIds
     * @return
     */
    @RequestMapping("delete_product.do")
    @RequiresAuthentication
    public ServerResponse<CartVo> deleteProduct(
            String productIds, HttpSession session, HttpServletRequest request, HttpServletResponse response){
        SysUserLogin sul = (SysUserLogin) getUserInfo(session, request, response);
        return iCartService.deleteProduct(sul.getSysUserId(), productIds);
    }

    /**
     * 更新购物车
     * @param session
     * @param count
     * @param productId
     * @return
     */
    @RequestMapping("update.do")
    @RequiresAuthentication
    public ServerResponse<CartVo> update(
            Integer count, Integer productId, HttpSession session, HttpServletRequest request, HttpServletResponse response){
        SysUserLogin sul = (SysUserLogin) getUserInfo(session, request, response);
        return iCartService.update(sul.getSysUserId(), productId, count);
    }

    /**
     * 查询购物车
     * @param session
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("list.do")
    @RequiresAuthentication
    public ServerResponse<CartVo> list(HttpSession session, HttpServletRequest request, HttpServletResponse response){
        SysUserLogin sul = (SysUserLogin) getUserInfo(session, request, response);
        return iCartService.list(sul.getSysUserId());
    }

    /**
     * 全选
     * @param session
     * @return
     */
    @RequestMapping("select_all.do")
    @RequiresAuthentication
    public ServerResponse<CartVo> selectAll(HttpSession session, HttpServletRequest request, HttpServletResponse response){
        SysUserLogin sul = (SysUserLogin) getUserInfo(session, request, response);
        return iCartService.selectOrUnSelect(sul.getSysUserId(), null, Const.Cart.CHECKED);
    }

    /**
     * 全选或全反选
     * @param session
     * @return
     */
    @RequestMapping("un_select_all.do")
    @RequiresAuthentication
    public ServerResponse<CartVo> unSelectAll(HttpSession session, HttpServletRequest request, HttpServletResponse response){
        SysUserLogin sul = (SysUserLogin) getUserInfo(session, request, response);
        return iCartService.selectOrUnSelect(sul.getSysUserId(), null, Const.Cart.UN_CHECKED);
    }


    @RequestMapping("select.do")
    @RequiresAuthentication
    public ServerResponse<CartVo> select(
            Integer productId, HttpSession session, HttpServletRequest request, HttpServletResponse response){
        SysUserLogin sul = (SysUserLogin) getUserInfo(session, request, response);
        return iCartService.selectOrUnSelect(sul.getSysUserId(), productId, Const.Cart.CHECKED);
    }

    @RequestMapping("un_select.do")
    @RequiresAuthentication
    public ServerResponse<CartVo> unSelect(
            Integer productId, HttpSession session, HttpServletRequest request, HttpServletResponse response){
        SysUserLogin sul = (SysUserLogin) getUserInfo(session, request, response);
        return iCartService.selectOrUnSelect(sul.getSysUserId(), productId, Const.Cart.UN_CHECKED);
    }

    @RequestMapping("get_cart_product_count.do")
    @RequiresAuthentication
    public ServerResponse<Integer> getCartProductCount(HttpSession session, HttpServletRequest request, HttpServletResponse response){
        SysUserLogin sul = (SysUserLogin) getUserInfo(session, request, response);
        return iCartService.getCartProductCount(sul.getSysUserId());
    }
}
