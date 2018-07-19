package com.jtd.web.vo;
/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月25日
 * @项目名称 dsp-admin
 * @描述 <p></p>
 */
public class UserVo {

	private Long id;
	private Long userId;
	private Long partnerId;
	private Integer balance;
	private String email;
	private Boolean isReciveAlarm;

	public UserVo() {
	}

	public UserVo(Long id, Long userId, Long partnerId, Integer balance, String email, Boolean isReciveAlarm) {
		super();
		this.id = id;
		this.userId = userId;
		this.partnerId = partnerId;
		this.balance = balance;
		this.email = email;
		this.isReciveAlarm = isReciveAlarm;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(Long partnerId) {
		this.partnerId = partnerId;
	}

	public Integer getBalance() {
		return balance;
	}

	public void setBalance(Integer balance) {
		this.balance = balance;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getIsReciveAlarm() {
		return isReciveAlarm;
	}

	public void setIsReciveAlarm(Boolean isReciveAlarm) {
		this.isReciveAlarm = isReciveAlarm;
	}

}
