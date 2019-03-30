package com.bobomico.dao.po;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysUserInf {

    @JsonIgnore
    @JSONField(serialize=false)
    private Integer sysUserInfId;

    private Integer sysUserId;

    private String userName;

    @JsonIgnore
    @JSONField(serialize=false)
    private Byte identityCardType;

    private String identityCardNo;

    private Long mobilePhone;

    private String userEmail;

    @JsonIgnore
    @JSONField(serialize=false)
    private String gender;

    private Integer userPoint;

    private Date registerTime;

    private String birthday;

    private Byte userLevel;

    private BigDecimal userMoney;

    private Date modifiedTime;

    public SysUserInf(Integer sysUserInfId, Integer sysUserId, String userName, Byte identityCardType, String identityCardNo, Long mobilePhone, String userEmail, String gender, Integer userPoint, Date registerTime, String birthday, Byte userLevel, BigDecimal userMoney, Date modifiedTime) {
        this.sysUserInfId = sysUserInfId;
        this.sysUserId = sysUserId;
        this.userName = userName;
        this.identityCardType = identityCardType;
        this.identityCardNo = identityCardNo;
        this.mobilePhone = mobilePhone;
        this.userEmail = userEmail;
        this.gender = gender;
        this.userPoint = userPoint;
        this.registerTime = registerTime;
        this.birthday = birthday;
        this.userLevel = userLevel;
        this.userMoney = userMoney;
        this.modifiedTime = modifiedTime;
    }

    public SysUserInf() {
        super();
    }

    public Integer getSysUserInfId() {
        return sysUserInfId;
    }

    public void setSysUserInfId(Integer sysUserInfId) {
        this.sysUserInfId = sysUserInfId;
    }

    public Integer getSysUserId() {
        return sysUserId;
    }

    public void setSysUserId(Integer sysUserId) {
        this.sysUserId = sysUserId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public Byte getIdentityCardType() {
        return identityCardType;
    }

    public void setIdentityCardType(Byte identityCardType) {
        this.identityCardType = identityCardType;
    }

    public String getIdentityCardNo() {
        return identityCardNo;
    }

    public void setIdentityCardNo(String identityCardNo) {
        this.identityCardNo = identityCardNo == null ? null : identityCardNo.trim();
    }

    public Long getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(Long mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail == null ? null : userEmail.trim();
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender == null ? null : gender.trim();
    }

    public Integer getUserPoint() {
        return userPoint;
    }

    public void setUserPoint(Integer userPoint) {
        this.userPoint = userPoint;
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public Byte getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(Byte userLevel) {
        this.userLevel = userLevel;
    }

    public BigDecimal getUserMoney() {
        return userMoney;
    }

    public void setUserMoney(BigDecimal userMoney) {
        this.userMoney = userMoney;
    }

    public Date getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public char[] getSex(){
        if(this.gender.equals("0")){
            return new char[]{'小','姐','姐'};
        }
        return new char[]{'小', '哥' , '哥'};
    }

    public char[] getCardType(){
        switch(this.identityCardType){
            case 1:
                return new char[]{'身','份','证'};
            case 2:
                return new char[]{'驾','驶','证'};
            case 3:
                return new char[]{'军','官','证'};
            case 4:
                return new char[]{'护','照'};
        }
        return null;
    }
}