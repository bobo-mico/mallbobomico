package com.bobomico.dao;

import com.bobomico.dao.po.MallOrderItem;

public interface MallOrderItemMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MallOrderItem record);

    int insertSelective(MallOrderItem record);

    MallOrderItem selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MallOrderItem record);

    int updateByPrimaryKey(MallOrderItem record);
}