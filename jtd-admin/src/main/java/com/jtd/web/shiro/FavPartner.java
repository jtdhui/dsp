package com.jtd.web.shiro;

public class FavPartner {

	private static final long serialVersionUID = 90677066151383970L;

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

}
