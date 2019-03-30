package com.bobomico.shiro.token;

import org.apache.shiro.authc.HostAuthenticationToken;
import org.apache.shiro.authc.RememberMeAuthenticationToken;

/**
 * @ClassName: com.bobomico.shiro.token.mallbobomico
 * @Author: DELL
 * @Date: 2019/3/28  4:18
 * @Description: 手机 Token
 * @version:
 */
public class PhonePasswordToken implements HostAuthenticationToken, RememberMeAuthenticationToken {

    private String loginPhone;
    private char[] password;
    private boolean rememberMe;
    private String host;

    @Override
    public String getHost() {
        return this.host;
    }

    @Override
    public boolean isRememberMe() {
        return this.rememberMe;
    }

    @Override
    public Object getPrincipal() {
        return this.loginPhone;
    }

    @Override
    public Object getCredentials() {
        return this.password;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void clear() {
        this.loginPhone = null;
        this.host = null;
        this.rememberMe = false;
        if (this.password != null) {
            for(int i = 0; i < this.password.length; ++i) {
                this.password[i] = 0;
            }
            this.password = null;
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getClass().getName());
        sb.append(" - ");
        sb.append(this.loginPhone);
        sb.append(", rememberMe=").append(this.rememberMe);
        if (this.host != null) {
            sb.append(" (").append(this.host).append(")");
        }
        return sb.toString();
    }

    public PhonePasswordToken(String loginPhone, char[] password) {
        this(loginPhone, password, Boolean.FALSE, null);
    }

    public PhonePasswordToken() {
    }

    public PhonePasswordToken(String loginPhone, char[] password, boolean rememberMe, String host) {
        this.loginPhone = loginPhone;
        this.password = password;
        this.rememberMe = rememberMe;
        this.host = host;
    }
}
