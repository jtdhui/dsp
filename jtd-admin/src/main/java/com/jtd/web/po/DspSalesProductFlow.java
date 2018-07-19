package com.jtd.web.po;

import com.jtd.commons.entity.BaseEntity;

public class DspSalesProductFlow extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String bizType;

	private String bossPartnerCode;

	private Long partnerId;

	private Integer type;

    private Integer status;

	private Long regionId;

	private Long companyId;

	private Long salesmanId;

	private Integer source;

	private Integer productId;

	private Integer openAmount;

	private Integer serviceAmount;

	private Integer deliverAmount;

	private Integer productOriginalPrice;
	
	private Integer openOriginalPrice;
	
	private Integer serviceOriginalPrice;

	private Long partnerAccFlowId;

	public String getBizType() {
		return bizType;
	}

	public void setBizType(String bizType) {
		this.bizType = bizType;
	}

	public String getBossPartnerCode() {
		return bossPartnerCode;
	}

	public void setBossPartnerCode(String bossPartnerCode) {
		this.bossPartnerCode = bossPartnerCode;
	}

	public Long getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(Long partnerId) {
		this.partnerId = partnerId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Long getRegionId() {
		return regionId;
	}

	public void setRegionId(Long regionId) {
		this.regionId = regionId;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public Long getSalesmanId() {
		return salesmanId;
	}

	public void setSalesmanId(Long salesmanId) {
		this.salesmanId = salesmanId;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public Integer getOpenAmount() {
		return openAmount;
	}

	public void setOpenAmount(Integer openAmount) {
		this.openAmount = openAmount;
	}

	public Integer getServiceAmount() {
		return serviceAmount;
	}

	public void setServiceAmount(Integer serviceAmount) {
		this.serviceAmount = serviceAmount;
	}

	public Integer getDeliverAmount() {
		return deliverAmount;
	}

	public void setDeliverAmount(Integer deliverAmount) {
		this.deliverAmount = deliverAmount;
	}

	public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}

	public Long getPartnerAccFlowId() {
		return partnerAccFlowId;
	}

	public void setPartnerAccFlowId(Long partnerAccFlowId) {
		this.partnerAccFlowId = partnerAccFlowId;
	}

	public Integer getProductOriginalPrice() {
		return productOriginalPrice;
	}

	public void setProductOriginalPrice(Integer productOriginalPrice) {
		this.productOriginalPrice = productOriginalPrice;
	}

	public Integer getOpenOriginalPrice() {
		return openOriginalPrice;
	}

	public void setOpenOriginalPrice(Integer openOriginalPrice) {
		this.openOriginalPrice = openOriginalPrice;
	}

	public Integer getServiceOriginalPrice() {
		return serviceOriginalPrice;
	}

	public void setServiceOriginalPrice(Integer serviceOriginalPrice) {
		this.serviceOriginalPrice = serviceOriginalPrice;
	}

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}