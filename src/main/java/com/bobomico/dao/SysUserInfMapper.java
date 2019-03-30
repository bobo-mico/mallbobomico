package com.bobomico.dao;

import com.bobomico.dao.po.SysUserInf;

public interface SysUserInfMapper {
    int deleteByPrimaryKey(Integer sysUserInfId);

    int insert(SysUserInf record);

    int insertSelective(SysUserInf record);

    SysUserInf selectByPrimaryKey(Integer sysUserInfId);

    int updateByPrimaryKeySelective(SysUserInf record);

    int updateByPrimaryKey(SysUserInf record);
}