package com.bobomico.service;

import com.bobomico.common.ServerResponse;
import com.bobomico.controller.vo.UserLoginVO;
import com.bobomico.dao.po.SysPermission;
import com.bobomico.dao.po.SysUserInf;
import com.bobomico.dao.po.SysUserLogin;
import org.apache.shiro.authc.AuthenticationToken;

import java.util.List;

public interface IUserService {
    // 注册
    ServerResponse<String> register(UserLoginVO userLogin);

    // 根据用户ID重置密码
    ServerResponse<String> resetPassword(String passwordOld, String passwordNew, Integer sysUserId);

    // 不同的token类型都能查询到用户数据(但暂时只支持ID查询)
    SysUserLogin findSysUserByTokenType(AuthenticationToken token);

    // 根据用户id更新用户状态码
    int updateUserStatus(SysUserLogin sysUserLogin);

    // 根据用户账号查询用户信息
    // SysUser findSysUserByUserCode(String userCode)throws Exception;

    // 根据用户id查询权限范围的菜单
    List<SysPermission> findMenuListByUserId(Integer userId) throws Exception;

    // 根据用户id查询权限范围的url
    List<SysPermission> findPermissionListByUserId(Integer userId) throws Exception;

    // 获取密码提示问题
    ServerResponse<String> selectQuestion(Integer userId);

    // 验证答案
    ServerResponse<String> checkAnswer(Integer userId, String question, String answer);

    // 重置密码 - 忘记密码通道
    ServerResponse<String> forgetResetPassword(Integer userId, String passwordNew, String forgetToken);

    // 设置及更新个人资料
    ServerResponse<SysUserInf> updateInformation(SysUserInf sysUserInf);

    // 获取个人详细信息
    ServerResponse<SysUserInf> getInformation(Integer userId);
}