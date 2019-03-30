package com.bobomico.dao.po;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysUserLogin implements Serializable {
    private Integer sysUserId;

    private String loginName;

    private String loginEmail;

    private Integer loginPhone;

    private String password;

    @JsonIgnore
    @JSONField(serialize=false)
    private String salt;

    @JsonIgnore
    @JSONField(serialize=false)
    private Byte userStats;

    @JsonFormat(pattern="yyyy-mm-dd")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date modifiedTime;

    public SysUserLogin(Integer sysUserId, String loginName, String loginEmail, Integer loginPhone, String password, String salt, Byte userStats, Date modifiedTime) {
        this.sysUserId = sysUserId;
        this.loginName = loginName;
        this.loginEmail = loginEmail;
        this.loginPhone = loginPhone;
        this.password = password;
        this.salt = salt;
        this.userStats = userStats;
        this.modifiedTime = modifiedTime;
    }

    public SysUserLogin(Integer sysUserId, String salt, String password) {
        this.sysUserId = sysUserId;
        this.salt = salt;
        this.password = password;
    }

    public SysUserLogin() {
        super();
    }

    public Integer getSysUserId() {
        return sysUserId;
    }

    public void setSysUserId(Integer sysUserId) {
        this.sysUserId = sysUserId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName == null ? null : loginName.trim();
    }

    public String getLoginEmail() {
        return loginEmail;
    }

    public void setLoginEmail(String loginEmail) {
        this.loginEmail = loginEmail == null ? null : loginEmail.trim();
    }

    public Integer getLoginPhone() {
        return loginPhone;
    }

    public void setLoginPhone(Integer loginPhone) {
        this.loginPhone = loginPhone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt == null ? null : salt.trim();
    }

    public Byte getUserStats() {
        return userStats;
    }

    public void setUserStats(Byte userStats) {
        this.userStats = userStats;
    }

    public Date getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
    }
}