package com.jtd.web.po;

import com.jtd.commons.entity.BaseEntity;

/**
 * 
 * @作者 duber
 * @版本 V1.0
 * @创建日期 2016年10月18日
 * @描述 广告数据实体
 */
public class Ad extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long campaignId;

	private Long creativeId;

	private String clickUrl;

	private String landingPage;

	private Integer landingType;
	
	/** 内部审核状态(0:待审核 1:已通过 2:已拒绝) */
	private Integer internalAudit;

	private String internalAuditInfo;

	private Integer deleteStatus;

	private String phoneTitle;

	private String phonePropaganda;

	private String phoneDescribe;

	private String originalPrice;

	private String discountPrice;

	private String salesVolume;

	private String optional;

	private String deepLink;

	private String monitor;

	private String phoneDeepLinkUrl;

	private String pvUrls;

	public Long getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(Long campaignId) {
		this.campaignId = campaignId;
	}

	public Long getCreativeId() {
		return creativeId;
	}

	public void setCreativeId(Long creativeId) {
		this.creativeId = creativeId;
	}

	public String getClickUrl() {
		return clickUrl;
	}

	public void setClickUrl(String clickUrl) {
		this.clickUrl = clickUrl == null ? null : clickUrl.trim();
	}

	public String getLandingPage() {
		return landingPage;
	}

	public void setLandingPage(String landingPage) {
		this.landingPage = landingPage == null ? null : landingPage.trim();
	}

	public Integer getLandingType() {
		return landingType;
	}

	public void setLandingType(Integer landingType) {
		this.landingType = landingType;
	}

	public Integer getInternalAudit() {
		return internalAudit;
	}

	public void setInternalAudit(Integer internalAudit) {
		this.internalAudit = internalAudit;
	}

	public String getInternalAuditInfo() {
		return internalAuditInfo;
	}

	public void setInternalAuditInfo(String internalAuditInfo) {
		this.internalAuditInfo = internalAuditInfo;
	}

	public Integer getDeleteStatus() {
		return deleteStatus;
	}

	public void setDeleteStatus(Integer deleteStatus) {
		this.deleteStatus = deleteStatus;
	}

	public String getPhoneTitle() {
		return phoneTitle;
	}

	public void setPhoneTitle(String phoneTitle) {
		this.phoneTitle = phoneTitle == null ? null : phoneTitle.trim();
	}

	public String getPhonePropaganda() {
		return phonePropaganda;
	}

	public void setPhonePropaganda(String phonePropaganda) {
		this.phonePropaganda = phonePropaganda == null ? null : phonePropaganda.trim();
	}

	public String getPhoneDescribe() {
		return phoneDescribe;
	}

	public void setPhoneDescribe(String phoneDescribe) {
		this.phoneDescribe = phoneDescribe == null ? null : phoneDescribe.trim();
	}

	public String getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(String originalPrice) {
		this.originalPrice = originalPrice == null ? null : originalPrice.trim();
	}

	public String getDiscountPrice() {
		return discountPrice;
	}

	public void setDiscountPrice(String discountPrice) {
		this.discountPrice = discountPrice == null ? null : discountPrice.trim();
	}

	public String getSalesVolume() {
		return salesVolume;
	}

	public void setSalesVolume(String salesVolume) {
		this.salesVolume = salesVolume == null ? null : salesVolume.trim();
	}

	public String getOptional() {
		return optional;
	}

	public void setOptional(String optional) {
		this.optional = optional == null ? null : optional.trim();
	}

	public String getDeepLink() {
		return deepLink;
	}

	public void setDeepLink(String deepLink) {
		this.deepLink = deepLink == null ? null : deepLink.trim();
	}

	public String getMonitor() {
		return monitor;
	}

	public void setMonitor(String monitor) {
		this.monitor = monitor == null ? null : monitor.trim();
	}

	public String getPhoneDeepLinkUrl() {
		return phoneDeepLinkUrl;
	}

	public void setPhoneDeepLinkUrl(String phoneDeepLinkUrl) {
		this.phoneDeepLinkUrl = phoneDeepLinkUrl == null ? null : phoneDeepLinkUrl.trim();
	}

	public String getPvUrls() {
		return pvUrls;
	}

	public void setPvUrls(String pvUrls) {
		this.pvUrls = pvUrls == null ? null : pvUrls.trim();
	}
}