package com.jtd.web.po.count;

import java.util.Date;

import com.jtd.commons.entity.BaseEntity;

public class PartnerExpendDailySnapshot extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long partnerId;

	private Long expendSnapshot;

	private Long accountBalance;

	private Long accountBalanceResult;

	private Date dateTime;

	public Long getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(Long partnerId) {
		this.partnerId = partnerId;
	}

	public Long getExpendSnapshot() {
		return expendSnapshot;
	}

	public void setExpendSnapshot(Long expendSnapshot) {
		this.expendSnapshot = expendSnapshot;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public Long getAccountBalance() {
		return accountBalance;
	}

	public void setAccountBalance(Long accountBalance) {
		this.accountBalance = accountBalance;
	}

	public Long getAccountBalanceResult() {
		return accountBalanceResult;
	}

	public void setAccountBalanceResult(Long accountBalanceResult) {
		this.accountBalanceResult = accountBalanceResult;
	}

}