package com.jtd.web.po;

import com.jtd.commons.entity.BaseEntity;

public class CreativeGroup extends BaseEntity{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    private Long partnerId;

    private String groupName;

    private Integer deleteStatus;

    public Long getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Long partnerId) {
        this.partnerId = partnerId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName == null ? null : groupName.trim();
    }

    public Integer getDeleteStatus() {
        return deleteStatus;
    }

    public void setDeleteStatus(Integer deleteStatus) {
        this.deleteStatus = deleteStatus;
    }
}