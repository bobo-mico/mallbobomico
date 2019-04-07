package com.bobomico.service;

import com.bobomico.common.ServerResponse;
import com.bobomico.controller.vo.CartVo;

/**
 * @ClassName: com.bobomico.service.mallbobomico
 * @Author: DELL
 * @Date: 2019/4/1  7:04
 * @Description:
 * @version:
 */
public interface ICartService {
    // 添加商品到购物车
    ServerResponse<CartVo> add(Integer userId, Integer productId, Integer count);
    // 更新购物车状态
    ServerResponse<CartVo> update(Integer userId,Integer productId,Integer count);
    // 删除购物车商品
    ServerResponse<CartVo> deleteProduct(Integer userId,String productIds);
    // 购物车商品列表
    ServerResponse<CartVo> list (Integer userId);
    // 检索商品
    ServerResponse<CartVo> selectOrUnSelect (Integer userId,Integer productId,Integer checked);
    //
    ServerResponse<Integer> getCartProductCount(Integer userId);
}
