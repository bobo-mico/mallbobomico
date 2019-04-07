package com.bobomico.dao;

import com.bobomico.dao.po.MallCart;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MallCartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MallCart record);

    int insertSelective(MallCart record);

    MallCart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MallCart record);

    int updateByPrimaryKey(MallCart record);

    // 先查看该商品在不在用户的购物车
    MallCart selectCartByUserIdProductId(@Param("userId") Integer userId, @Param("productId")Integer productId);

    //
    List<MallCart> selectCartByUserId(Integer userId);

    //
    int selectCartProductCheckedStatusByUserId(Integer userId);

    // 删除购物车商品
    int deleteByUserIdProductIds(@Param("userId") Integer userId, @Param("productIdList")List<String> productIdList);

    // 检索商品选中状态
    int checkedOrUncheckedProduct(
            @Param("userId") Integer userId, @Param("productId")Integer productId, @Param("checked") Integer checked);

    int selectCartProductCount(@Param("userId") Integer userId);

    List<MallCart> selectCheckedCartByUserId(Integer userId);

}