package com.bobomico.dao;

import com.bobomico.dao.po.MallCategory;

import java.util.List;

public interface MallCategoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MallCategory record);

    int insertSelective(MallCategory record);

    MallCategory selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MallCategory record);

    int updateByPrimaryKey(MallCategory record);

    // 获取商品子分类
    List<MallCategory> selectCategoryChildrenByParentId(Integer categoryId);
}