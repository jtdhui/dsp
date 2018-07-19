package com.jtd.web.po;

import com.jtd.commons.entity.BaseEntity;

public class SysRolePermission extends BaseEntity {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String sysRoleId;

    private String sysPermissionId;

    public String getSysRoleId() {
        return sysRoleId;
    }

    public void setSysRoleId(String sysRoleId) {
        this.sysRoleId = sysRoleId == null ? null : sysRoleId.trim();
    }

    public String getSysPermissionId() {
        return sysPermissionId;
    }

    public void setSysPermissionId(String sysPermissionId) {
        this.sysPermissionId = sysPermissionId == null ? null : sysPermissionId.trim();
    }
}