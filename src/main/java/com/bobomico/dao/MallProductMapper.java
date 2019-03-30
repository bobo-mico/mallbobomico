package com.bobomico.dao;

import com.bobomico.dao.po.MallProduct;

public interface MallProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MallProduct record);

    int insertSelective(MallProduct record);

    MallProduct selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MallProduct record);

    int updateByPrimaryKey(MallProduct record);
}