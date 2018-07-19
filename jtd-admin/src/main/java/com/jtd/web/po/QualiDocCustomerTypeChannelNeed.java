package com.jtd.web.po;

import com.jtd.commons.entity.BaseEntity;

public class QualiDocCustomerTypeChannelNeed extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	private Long customerTypeId;
	private Long channelId;
	private Long docTypeId;
	private Integer isMainQualidoc;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCustomerTypeId() {
		return customerTypeId;
	}

	public void setCustomerTypeId(Long customerTypeId) {
		this.customerTypeId = customerTypeId;
	}

	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}

	public Long getDocTypeId() {
		return docTypeId;
	}

	public void setDocTypeId(Long docTypeId) {
		this.docTypeId = docTypeId;
	}

	public Integer getIsMainQualidoc() {
		return isMainQualidoc;
	}

	public void setIsMainQualidoc(Integer isMainQualidoc) {
		this.isMainQualidoc = isMainQualidoc;
	}

}
