package com.bobomico.dao;

import com.bobomico.dao.po.MallOrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MallOrderItemMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MallOrderItem record);

    int insertSelective(MallOrderItem record);

    MallOrderItem selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MallOrderItem record);

    int updateByPrimaryKey(MallOrderItem record);

    List<MallOrderItem> getByOrderNoUserId(@Param("orderNo")Long orderNo, @Param("userId")Integer userId);

    List<MallOrderItem> getByOrderNo(@Param("orderNo")Long orderNo);

    void batchInsert(@Param("orderItemList") List<MallOrderItem> orderItemList);
}