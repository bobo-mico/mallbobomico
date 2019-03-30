package com.bobomico.dao;

import com.bobomico.dao.po.MallShipping;

public interface MallShippingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MallShipping record);

    int insertSelective(MallShipping record);

    MallShipping selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MallShipping record);

    int updateByPrimaryKey(MallShipping record);
}