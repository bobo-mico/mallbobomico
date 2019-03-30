package com.bobomico.dao;

import com.bobomico.dao.po.MallPayInf;

public interface MallPayInfMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MallPayInf record);

    int insertSelective(MallPayInf record);

    MallPayInf selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MallPayInf record);

    int updateByPrimaryKey(MallPayInf record);
}