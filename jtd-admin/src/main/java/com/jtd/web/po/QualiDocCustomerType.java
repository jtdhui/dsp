package com.jtd.web.po;

import com.jtd.commons.entity.BaseEntity;

public class QualiDocCustomerType extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	private String customerTypeName;
	private int isForMainQualidoc;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCustomerTypeName() {
		return customerTypeName;
	}

	public void setCustomerTypeName(String customerTypeName) {
		this.customerTypeName = customerTypeName;
	}

	public int getIsForMainQualidoc() {
		return isForMainQualidoc;
	}

	public void setIsForMainQualidoc(int isForMainQualidoc) {
		this.isForMainQualidoc = isForMainQualidoc;
	}

}
