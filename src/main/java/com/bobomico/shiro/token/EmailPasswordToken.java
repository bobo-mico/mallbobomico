package com.bobomico.shiro.token;

import org.apache.shiro.authc.HostAuthenticationToken;
import org.apache.shiro.authc.RememberMeAuthenticationToken;

/**
 * @ClassName: com.bobomico.shiro.token.mallbobomico
 * @Author: DELL
 * @Date: 2019/3/28  4:17
 * @Description: 邮箱 Token
 * @version:
 */
public class EmailPasswordToken implements HostAuthenticationToken, RememberMeAuthenticationToken {

    private String loginEmail;
    private char[] password;
    private boolean rememberMe;
    private String host;

    @Override
    public String getHost() {
        return null;
    }

    @Override
    public boolean isRememberMe() {
        return false;
    }

    @Override
    public Object getPrincipal() {
        return this.loginEmail;
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
        this.loginEmail = null;
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
        sb.append(this.loginEmail);
        sb.append(", rememberMe=").append(this.rememberMe);
        if (this.host != null) {
            sb.append(" (").append(this.host).append(")");
        }
        return sb.toString();
    }

    public EmailPasswordToken(String loginEmail, char[] password) {
        this(loginEmail, password, Boolean.FALSE, null);
    }

    public EmailPasswordToken() {
    }

    public EmailPasswordToken(String loginEmail, char[] password, boolean rememberMe, String host) {
        this.loginEmail = loginEmail;
        this.password = password;
        this.rememberMe = rememberMe;
        this.host = host;
    }
}
