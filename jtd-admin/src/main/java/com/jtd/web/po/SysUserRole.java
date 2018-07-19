package com.jtd.web.po;

import com.jtd.commons.entity.BaseEntity;

public class SysUserRole extends BaseEntity {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String sysUserId;

    private String sysRoleId;

    public String getSysUserId() {
        return sysUserId;
    }

    public void setSysUserId(String sysUserId) {
        this.sysUserId = sysUserId == null ? null : sysUserId.trim();
    }

    public String getSysRoleId() {
        return sysRoleId;
    }

    public void setSysRoleId(String sysRoleId) {
        this.sysRoleId = sysRoleId == null ? null : sysRoleId.trim();
    }
}