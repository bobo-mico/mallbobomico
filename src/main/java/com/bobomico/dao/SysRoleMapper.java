package com.bobomico.dao;

import com.bobomico.dao.po.SysRole;

import java.util.List;

public interface SysRoleMapper {
    int deleteByPrimaryKey(String id);

    int insert(SysRole record);

    int insertSelective(SysRole record);

    SysRole selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(SysRole record);

    int updateByPrimaryKey(SysRole record);

    // 通过用户ID检索用户角色
    List<SysRole> getRolesByUserId(Integer sysUserId);
}