package com.bobomico.shiro.realm;

import com.bobomico.common.Const;
import com.bobomico.dao.SysRoleMapper;
import com.bobomico.dao.po.SysPermission;
import com.bobomico.dao.po.SysRole;
import com.bobomico.dao.po.SysUserLogin;
import com.bobomico.service.IUserService;
import com.bobomico.shiro.cache.util.CustomSimpleByteSource;
import com.bobomico.shiro.token.EmailPasswordToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName: com.bobomico.shiro.realm.mallbobomico
 * @Author: DELL
 * @Date: 2019/3/28  2:54
 * @Description: 自定义Realm
 * @version:    AuthenticatingRealm getAuthenticationInfo
 */
@Slf4j
public class EmailRealm extends AuthorizingRealm {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    // 设置realm的名称
    @Override
    public void setName(String name) {
        super.setName("EmailRealm");
    }

    /**
     * 获取认证信息
     * @param email 邮件身份
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken email) throws AuthenticationException {
        System.out.println("realm查询认证信息");
        SysUserLogin sysUserLogin = null;

        EmailPasswordToken token = (EmailPasswordToken) email;
        try {
            sysUserLogin = iUserService.findSysUserByTokenType(token);
        } catch (Exception e) {
            throw e;
        }

        if(sysUserLogin == null){
            throw new UnknownAccountException();
        }

        // 账户被锁定
        if(sysUserLogin.getUserStats() == Const.userStatus.LOCKED){
            throw new LockedAccountException();
        }

        // 获取密码
        String password = sysUserLogin.getPassword();
        // 销毁密码
        sysUserLogin.setPassword(null);

        // 如果查询到就返回认证信息 AuthenticationInfo(用户认证信息)
        return new SimpleAuthenticationInfo(
                sysUserLogin, password, new CustomSimpleByteSource(sysUserLogin.getSalt()), this.getName());
    }

    /**
     * 获取授权信息
     * @param principalCollection 认证后的身份信息 userLogin
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("email realm检索权限");
        SysUserLogin sysUserLogin = (SysUserLogin) principalCollection.getPrimaryPrincipal();

        // 检索用户权限
        List<SysPermission> permissions = null;
        try {
            permissions = iUserService.findPermissionListByUserId(sysUserLogin.getSysUserId());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 填充用户权限
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        List<String> permissionList = permissions.stream().map(SysPermission::getPercode).collect(Collectors.toList());
        simpleAuthorizationInfo.addStringPermissions(permissionList);

        // 检索用户角色
        List<SysRole> roleList = sysRoleMapper.getRolesByUserId(sysUserLogin.getSysUserId());
        List<String> roles = roleList.stream().map(SysRole::getName).collect(Collectors.toList());
        // 填充用户角色
        simpleAuthorizationInfo.setRoles(new HashSet(roles));

        // 在realm中调用checkPermission会引起递归
        return simpleAuthorizationInfo;
    }

    /**
     * 清除缓存
     */
    // public void clearCached(){
    //     PrincipalCollection principal = SecurityUtils.getSubject().getPrincipals();
    //     super.clearCache(principal);
    // }

    /**
     * 让Realm支持token的子类或相同 跳转到getAuthenticationTokenClass查看源码
     * @param var1
     * @return
     */
    @Override
    public boolean supports(AuthenticationToken var1){
        // return token != null && getAuthenticationTokenClass().isAssignableFrom(token.getClass());
        return var1 instanceof EmailPasswordToken;
    }

}