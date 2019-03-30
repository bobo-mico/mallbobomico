package com.bobomico.pojo;

import com.bobomico.dao.po.SysUserLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName: com.bobomico.pojo.bobomicomall
 * @Author: DELL
 * @Date: 2019/3/27  12:53
 * @Description:
 * @version:
 */
@Component
public class RegisterUser {
    @Autowired
    private SysUserLogin sysUserLogin;

    private int resultCount;

    public SysUserLogin getSysUserLogin() {
        return sysUserLogin;
    }

    public void setSysUserLogin(SysUserLogin sysUserLogin) {
        this.sysUserLogin = sysUserLogin;
    }

    public int getResultCount() {
        return resultCount;
    }

    public void setResultCount(int resultCount) {
        this.resultCount = resultCount;
    }
}
