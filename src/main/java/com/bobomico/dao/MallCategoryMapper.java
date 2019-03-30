package com.bobomico.dao;

import com.bobomico.dao.po.MallCategory;

public interface MallCategoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MallCategory record);

    int insertSelective(MallCategory record);

    MallCategory selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MallCategory record);

    int updateByPrimaryKey(MallCategory record);
}