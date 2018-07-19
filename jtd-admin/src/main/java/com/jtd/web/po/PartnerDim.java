package com.jtd.web.po;

import com.jtd.commons.entity.BaseEntity;

public class PartnerDim extends BaseEntity{
    /**
	 * 
	 */
	private static final long serialVersionUID = -1266497288772144190L;

	private Long id;

    private Long partnerId;

    private String dimName;

    private String dimValue;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Long partnerId) {
        this.partnerId = partnerId;
    }

    public String getDimName() {
        return dimName;
    }

    public void setDimName(String dimName) {
        this.dimName = dimName == null ? null : dimName.trim();
    }

    public String getDimValue() {
        return dimValue;
    }

    public void setDimValue(String dimValue) {
        this.dimValue = dimValue == null ? null : dimValue.trim();
    }
}