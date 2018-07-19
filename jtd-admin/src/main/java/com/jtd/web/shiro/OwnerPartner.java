package com.jtd.web.shiro;

public class OwnerPartner {

	private static final long serialVersionUID = -490677066151383970L;
	/**主键*/
	private Long id;
	/**广告主上级ID*/
	private Long pid;
	/**上级代理名称*/
	private String pName;
	/**广告主类型*/
	private Long partnerType;
	/**广告主简称*/
	private String simpleName;
	/**广告主全称*/
	private String partnerName;
	/**广告主状态 */
	private Integer status;
	/**帐户余额*/
	private Long accBalance;
	/** logo图标的位置 */
	private String logoImg;
    /**系统个性化样式*/
	private String styleCode;
	/**OEM代理访问系统的url，也是版权所有的网址*/
	private String loginUrl;
	/**版权所有开始年份*/
	private String copyrightStartYear ;
	/**网站备案号，如京ICP备16066321号*/
	private String websiteRecordCode ;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getAccBalance() {
		return accBalance;
	}

	public void setAccBalance(Long accBalance) {
		this.accBalance = accBalance;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getpName() {
		return pName;
	}

	public void setpName(String pName) {
		this.pName = pName;
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

}
