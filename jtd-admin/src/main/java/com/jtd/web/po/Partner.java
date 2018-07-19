package com.jtd.web.po;

import java.util.Date;

import com.jtd.commons.entity.BaseEntity;
import com.jtd.utils.AccountAmountUtil;

public class Partner extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = -490677066151383970L;

	private Long pid;
	/**
	 * 上级代理名称，非po属性
	 */
	private String pName;
	private Long partnerType;
	private Long categoryId;
	/**
	 * 行业名称，非po属性
	 */
	private String categoryName;
	private String simpleName;
	private String partnerName;
	private String websiteName;
	private String websiteUrl;
	private String taxid;
	private Long accBalance;
	private Long credits;
	private Double grossProfit;
	private String grossProfitString;
	private String contactName;
	private String contactTelephone;
	private String contactEmail;
	private String salesName;
	private String salesTelephone;
	private String salesEmail;
	private String companyAddress;
	private String companyTelephone;
	private String postcode;
	private Integer status;
	private Integer deleteStatus;
	private Integer tanxAdvertiserid;
	private Long qualiDocMainCustomerType;
	private Long qualiDocOptionalCustomerType;
	private String logoImg;
	private String styleCode; // 参见com.jtd.web.constants.WebsiteStyleType
	private String loginUrl;
	private String copyrightStartYear;
	private String websiteRecordCode;
	private String bossPartnerCode;
	private String bossBizType;
	private Integer dspSalesProductId;
	private String advertiseUrlList;
	private String region;
	private String city;
	private Date firstOnlineTime;
	private Long createUserId;
	private Integer partnerLevel;
	
	private Integer childrenSize ;
	/**
	 * 是否已经成功回调boss系统，0为成功，非0为失败，非po属性
	 */
	private Integer bossCallbackResult;

	public Long getPid() {
		return pid;
	}

	public void setPid(Long pid) {
		this.pid = pid;
	}

	public Long getPartnerType() {
		return partnerType;
	}

	public void setPartnerType(Long partnerType) {
		this.partnerType = partnerType;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getSimpleName() {
		return simpleName;
	}

	public void setSimpleName(String simpleName) {
		this.simpleName = simpleName;
	}

	public String getPartnerName() {
		return partnerName;
	}

	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
	}

	public String getWebsiteName() {
		return websiteName;
	}

	public void setWebsiteName(String websiteName) {
		this.websiteName = websiteName;
	}

	public String getWebsiteUrl() {
		return websiteUrl;
	}

	public void setWebsiteUrl(String websiteUrl) {
		this.websiteUrl = websiteUrl;
	}

	public String getTaxid() {
		return taxid;
	}

	public void setTaxid(String taxid) {
		this.taxid = taxid;
	}

	public Long getAccBalance() {
		return accBalance;
	}

	public void setAccBalance(Long accBalance) {
		this.accBalance = accBalance;
	}

	public Long getCredits() {
		return credits;
	}

	public void setCredits(Long credits) {
		this.credits = credits;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactTelephone() {
		return contactTelephone;
	}

	public void setContactTelephone(String contactTelephone) {
		this.contactTelephone = contactTelephone;
	}

	public String getCompanyAddress() {
		return companyAddress;
	}

	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}

	public String getCompanyTelephone() {
		return companyTelephone;
	}

	public void setCompanyTelephone(String companyTelephone) {
		this.companyTelephone = companyTelephone;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getDeleteStatus() {
		return deleteStatus;
	}

	public void setDeleteStatus(Integer deleteStatus) {
		this.deleteStatus = deleteStatus;
	}

	public Integer getTanxAdvertiserid() {
		return tanxAdvertiserid;
	}

	public void setTanxAdvertiserid(Integer tanxAdvertiserid) {
		this.tanxAdvertiserid = tanxAdvertiserid;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getSalesName() {
		return salesName;
	}

	public void setSalesName(String salesName) {
		this.salesName = salesName;
	}

	public String getSalesTelephone() {
		return salesTelephone;
	}

	public void setSalesTelephone(String salesTelephone) {
		this.salesTelephone = salesTelephone;
	}

	public String getContactEmail() {
		return contactEmail;
	}

	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

	public String getSalesEmail() {
		return salesEmail;
	}

	public void setSalesEmail(String salesEmail) {
		this.salesEmail = salesEmail;
	}

	public String getpName() {
		return pName;
	}

	public void setpName(String pName) {
		this.pName = pName;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getAccBalanceYuan() {
		if (this.accBalance != null) {
			return AccountAmountUtil.getAmountYuanString(this.accBalance);
		}
		return "";
	}

	public String getGrossProfitString() {
		return grossProfitString;
	}

	public void setGrossProfitString(String grossProfitString) {
		this.grossProfitString = grossProfitString;
	}

	public Double getGrossProfit() {
		return grossProfit;
	}

	public void setGrossProfit(Double grossProfit) {
		this.grossProfit = grossProfit;
	}

	public Long getQualiDocMainCustomerType() {
		return qualiDocMainCustomerType;
	}

	public void setQualiDocMainCustomerType(Long qualiDocMainCustomerType) {
		this.qualiDocMainCustomerType = qualiDocMainCustomerType;
	}

	public Long getQualiDocOptionalCustomerType() {
		return qualiDocOptionalCustomerType;
	}

	public void setQualiDocOptionalCustomerType(
			Long qualiDocOptionalCustomerType) {
		this.qualiDocOptionalCustomerType = qualiDocOptionalCustomerType;
	}

	public String getLogoImg() {
		return logoImg;
	}

	public void setLogoImg(String logoImg) {
		this.logoImg = logoImg;
	}

	public String getLoginUrl() {
		return loginUrl;
	}

	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

	public String getStyleCode() {
		return styleCode;
	}

	public void setStyleCode(String styleCode) {
		this.styleCode = styleCode;
	}

	public String getCopyrightStartYear() {
		return copyrightStartYear;
	}

	public void setCopyrightStartYear(String copyrightStartYear) {
		this.copyrightStartYear = copyrightStartYear;
	}

	public String getWebsiteRecordCode() {
		return websiteRecordCode;
	}

	public void setWebsiteRecordCode(String websiteRecordCode) {
		this.websiteRecordCode = websiteRecordCode;
	}

	public String getBossPartnerCode() {
		return bossPartnerCode;
	}

	public void setBossPartnerCode(String bossPartnerCode) {
		this.bossPartnerCode = bossPartnerCode;
	}

	public Integer getBossCallbackResult() {
		return bossCallbackResult;
	}

	public void setBossCallbackResult(Integer bossCallbackResult) {
		this.bossCallbackResult = bossCallbackResult;
	}

	public String getBossBizType() {
		return bossBizType;
	}

	public void setBossBizType(String bossBizType) {
		this.bossBizType = bossBizType;
	}

	public Integer getDspSalesProductId() {
		return dspSalesProductId;
	}

	public void setDspSalesProductId(Integer dspSalesProductId) {
		this.dspSalesProductId = dspSalesProductId;
	}

	public String getAdvertiseUrlList() {
		return advertiseUrlList;
	}

	public void setAdvertiseUrlList(String advertiseUrls) {
		this.advertiseUrlList = advertiseUrls;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public Date getFirstOnlineTime() {
		return firstOnlineTime;
	}
	public void setFirstOnlineTime(Date firstOnlineTime) {
		this.firstOnlineTime = firstOnlineTime;
	}

	public Long getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}

	public Integer getPartnerLevel() {
		return partnerLevel;
	}

	public void setPartnerLevel(Integer partnerLevel) {
		this.partnerLevel = partnerLevel;
	}

	public Integer getChildrenSize() {
		return childrenSize;
	}

	public void setChildrenSize(Integer childrenSize) {
		this.childrenSize = childrenSize;
	}
	
}
