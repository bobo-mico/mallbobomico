package com.bobomico.bo;

import org.springframework.stereotype.Component;

import java.util.Observable;
import java.util.Observer;

/**
 * @ClassName: com.timisakura.VO.bobomiccore2
 * @Author: Lion
 * @Date: 2019/3/22  1:49
 * @Description:
 * @version:
 */
@Component
public class LoginType implements Observer {

    private String loginType;

    private int retryCount;

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public int getRetryCount() {
        return retryCount;
    }

    /**
     * 获取用户登录次数
     * @param observable
     * @param arg
     */
    @Override
    public void update(Observable observable, Object arg) {
        retryCount = (int)arg;
    }
}