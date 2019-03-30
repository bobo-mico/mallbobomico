package com.bobomico.pojo;

import com.bobomico.dao.po.SysUserInf;
import com.bobomico.dao.po.SysUserLogin;

/**
 * @ClassName: com.bobomico.pojo.mallbobomico
 * @Author: DELL
 * @Date: 2019/3/29  2:49
 * @Description: 用户登录信息 + 用户详细信息
 * @version:
 */
public class SysUser extends SysUserInf {
    private SysUserLogin sysUserLogin;

    public SysUserLogin getSysUserLogin() {
        return sysUserLogin;
    }

    public void setSysUserLogin(SysUserLogin sysUserLogin) {
        this.sysUserLogin = sysUserLogin;
    }
}
