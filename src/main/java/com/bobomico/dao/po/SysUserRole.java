package com.bobomico.dao.po;

public class SysUserRole {
    private String id;

    private Integer sysUserId;

    private String sysRoleId;

    public SysUserRole(String id, Integer sysUserId, String sysRoleId) {
        this.id = id;
        this.sysUserId = sysUserId;
        this.sysRoleId = sysRoleId;
    }

    public SysUserRole() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public Integer getSysUserId() {
        return sysUserId;
    }

    public void setSysUserId(Integer sysUserId) {
        this.sysUserId = sysUserId;
    }

    public String getSysRoleId() {
        return sysRoleId;
    }

    public void setSysRoleId(String sysRoleId) {
        this.sysRoleId = sysRoleId == null ? null : sysRoleId.trim();
    }
}