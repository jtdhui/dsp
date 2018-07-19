package com.jtd.web.po;

import com.jtd.commons.entity.BaseEntity;
import com.jtd.utils.AccountAmountUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PartnerAccFlow extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 321132148941968865L;

	private Long partnerId;

	private Long fromPartnerId;

	private String tradeId;

	private Integer tradeType; /* 0为充值，1为消费 */

	private Long amount;
	
	private Long accBalanceResult;
	
	private Date tradeTime;

	private Long operatorId;

	private String operatorName;

	public Long getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(Long partnerId) {
		this.partnerId = partnerId;
	}

	public Long getFromPartnerId() {
		return fromPartnerId;
	}

	public void setFromPartnerId(Long fromPartnerId) {
		this.fromPartnerId = fromPartnerId;
	}

	public String getTradeId() {
		return tradeId;
	}

	public void setTradeId(String tradeId) {
		this.tradeId = tradeId == null ? null : tradeId.trim();
	}

	public Integer getTradeType() {
		return tradeType;
	}

	public void setTradeType(Integer tradeType) {
		this.tradeType = tradeType;
	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public Date getTradeTime() {
		return tradeTime;
	}

	public void setTradeTime(Date tradeTime) {
		this.tradeTime = tradeTime;
	}

	public Long getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(Long operatorId) {
		this.operatorId = operatorId;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName == null ? null : operatorName.trim();
	}

	public Long getAccBalanceResult() {
		return accBalanceResult;
	}

	public void setAccBalanceResult(Long accBalanceResult) {
		this.accBalanceResult = accBalanceResult;
	}

	public static String generateNewTradeId(long userId, long partnerId) {
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmm");
		// yyyyMMddhhmm_操作人id_partnerId
		String tradeId = sdf.format(now) + "_" + userId + "_" + partnerId;
		return tradeId;
	}

	public String getAmountYuan() {
		if (this.amount != null) {
			return AccountAmountUtil.getAmountYuanString(this.amount);
		}
		return "";
	}

	public String getAccBalanceResultYuan() {
		if (this.accBalanceResult != null) {
			return AccountAmountUtil.getAmountYuanString(this.accBalanceResult);
		}
		return "";
	}
	
}