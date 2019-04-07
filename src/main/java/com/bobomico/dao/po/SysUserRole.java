package com.bobomico.dao.po;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SysUserRole {
    private String id;

    private Integer sysUserId;

    private String sysRoleId;
}