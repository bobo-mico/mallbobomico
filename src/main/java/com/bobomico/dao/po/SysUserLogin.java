package com.bobomico.dao.po;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SysUserLogin implements Serializable {
    private Integer sysUserId;

    private String loginName;

    private String loginEmail;

    private Integer loginPhone;

    private String password;

    // @JsonIgnore
    // @JSONField(serialize=false)
    private String salt;

    // @JsonIgnore
    // @JSONField(serialize=false)
    private Byte userStats;

    // @JsonFormat(pattern="yyyy-mm-dd")
    // @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date modifiedTime;

    public SysUserLogin(Integer sysUserId, String salt, String password) {
        this.sysUserId = sysUserId;
        this.salt = salt;
        this.password = password;
    }
}