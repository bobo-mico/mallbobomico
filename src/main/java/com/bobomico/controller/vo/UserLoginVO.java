package com.bobomico.controller.vo;

import com.bobomico.dao.po.SysUserLogin;
import org.springframework.stereotype.Component;

@Component
public class UserLoginVO extends SysUserLogin {
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}