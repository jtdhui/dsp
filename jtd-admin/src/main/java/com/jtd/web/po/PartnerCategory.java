package com.jtd.web.po;

import com.jtd.commons.entity.BaseEntity;

public class PartnerCategory extends BaseEntity{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1932811628324860227L;

	private Long id;

    private Long partnerId;

    private String catgserial;

    private String catgid;

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

    public String getCatgserial() {
        return catgserial;
    }

    public void setCatgserial(String catgserial) {
        this.catgserial = catgserial == null ? null : catgserial.trim();
    }

    public String getCatgid() {
        return catgid;
    }

    public void setCatgid(String catgid) {
        this.catgid = catgid == null ? null : catgid.trim();
    }

}