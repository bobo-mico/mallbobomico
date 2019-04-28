package com.bobomico.dao;

import com.bobomico.dao.po.SysUserLogin;
import com.bobomico.pojo.RegisterUser;
import org.apache.ibatis.annotations.Param;

public interface SysUserLoginMapper {

    int deleteByPrimaryKey(Integer sysUserId);

    int insert(SysUserLogin record);

    int insertSelective(SysUserLogin record);

    SysUserLogin selectByPrimaryKey(Integer sysUserId);

    int updateByPrimaryKeySelective(SysUserLogin record);

    int updateByPrimaryKey(SysUserLogin record);

    // 以下都是自定义
    void register(RegisterUser registerUser);

    // 密码重置
    int resetPassword(SysUserLogin userLogin);

    int checkEmail(@Param("login_email") String principal);

    int checkUsername(@Param("login_name") String principal);

    int checkUserId(Integer userId);

    SysUserLogin selectLoginForUsername(@Param("login_name") String principal);

    SysUserLogin selectLoginForEmail(@Param("login_email") String principal);

    SysUserLogin selectLoginForPhone(@Param("login_phone") String principal);

    // 根据用户ID修改密码和盐
    // int updatePasswordByUsername(Integer userId, String originSalt, String md5Password);
}