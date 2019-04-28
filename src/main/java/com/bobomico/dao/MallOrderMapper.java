package com.bobomico.dao;

import com.bobomico.dao.po.MallOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MallOrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MallOrder record);

    int insertSelective(MallOrder record);

    MallOrder selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MallOrder record);

    int updateByPrimaryKey(MallOrder record);

    // 查询用户订单
    MallOrder selectByUserIdAndOrderNo(@Param("userId")Integer userId, @Param("orderNo")Long orderNo);

    // 查看订单
    MallOrder selectByOrderNo(Long orderNo);

    //
    List<MallOrder> selectByUserId(Integer userId);

}