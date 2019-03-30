package com.bobomico.shiro.realm;

import com.bobomico.common.Const;
import com.bobomico.dao.po.SysUserLogin;
import com.bobomico.service.IUserService;
import com.bobomico.shiro.token.EmailPasswordToken;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: com.bobomico.shiro.realm.mallbobomico
 * @Author: DELL
 * @Date: 2019/3/28  2:54
 * @Description: 自定义Realm
 * @version:
 */
public class UserPassRealm extends AuthorizingRealm {

    @Autowired
    private IUserService iUserService;

    // 设置realm的名称
    @Override
    public void setName(String name) {
        super.setName("UserPassRealm");
    }

    /**
     * 获取认证信息
     * @param user 待认证信息
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken user) throws AuthenticationException {
        SysUserLogin sysUserLogin = null;

        UsernamePasswordToken token = (UsernamePasswordToken) user;
        try {
            sysUserLogin = iUserService.findSysUserByTokenType(token);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 账户不存在
        if (sysUserLogin == null) {
            return null;
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
                sysUserLogin, password, ByteSource.Util.bytes(sysUserLogin.getSalt()), this.getName());
    }

    /**
     * 获取授权信息
     * @param principalCollection 认证后的身份信息 userLogin
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        // SysUserLogin sysUserLogin = (SysUserLogin) principalCollection.getPrimaryPrincipal();

        List<String> permissions = new ArrayList();
        permissions.add("user:update");
        permissions.add("item:add");
        permissions.add("item:query");

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.addStringPermissions(permissions);

        return simpleAuthorizationInfo;
    }

    /**
     * 让Realm支持token的子类或相同 跳转到getAuthenticationTokenClass查看源码
     * @param var1
     * @return
     */
    @Override
    public boolean supports(AuthenticationToken var1){
        return var1 instanceof UsernamePasswordToken;
    }
}