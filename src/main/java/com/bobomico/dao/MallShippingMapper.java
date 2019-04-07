package com.bobomico.dao;

import com.bobomico.dao.po.MallShipping;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MallShippingMapper {
    int deleteByPrimaryKey(Integer id);

    // 重写SQL 返回插入的地址的id
    int insert(MallShipping record);

    int insertSelective(MallShipping record);

    MallShipping selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MallShipping record);

    int updateByPrimaryKey(MallShipping record);

    // 根据用户id和地址id删除地址信息 防止横向越权
    int deleteByShippingIdUserId(@Param("userId")Integer userId, @Param("shippingId") Integer shippingId);

    // 根据用户id和地址id修改地址信息 防止横向越权
    int updateByShipping(MallShipping record);

    // 根据用户id和地址id查询地址信息 防止横向越权
    MallShipping selectByShippingIdUserId(@Param("userId")Integer userId,@Param("shippingId") Integer shippingId);

    // 根据用户id查询地址信息
    List<MallShipping> selectByUserId(@Param("userId")Integer userId);
}