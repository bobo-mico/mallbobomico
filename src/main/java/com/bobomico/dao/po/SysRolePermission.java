package com.bobomico.dao.po;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SysRolePermission {
    private String id;

    private String sysRoleId;

    private String sysPermissionId;
}