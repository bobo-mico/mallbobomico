package com.bobomico.dao.po;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Optional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 在Controller中返回的json都是走的阿里fastjson
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    public char[] getSex(){
        Optional<String> optional = Optional.fromNullable(gender);
        if (optional.isPresent()) {
            switch (gender) {
                case "0":
                    return new char[]{'小','姐','姐'};
                case "1":
                    return new char[]{'小', '哥' , '哥'};
            }
        }
        return null;
    }

    public char[] getCardType(){
        Optional<Byte> optional = Optional.fromNullable(identityCardType);
        if (optional.isPresent()) {
            switch (identityCardType) {
                case 1:
                    return new char[]{'身','份','证'};
                case 2:
                    return new char[]{'驾','驶','证'};
                case 3:
                    return new char[]{'军','官','证'};
                case 4:
                    return new char[]{'护','照'};
            }
        }
        return null;
    }
}