package com.bobomico.dao;

import com.bobomico.dao.po.MallCart;

public interface MallCartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MallCart record);

    int insertSelective(MallCart record);

    MallCart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MallCart record);

    int updateByPrimaryKey(MallCart record);
}