package com.jtd.web.po;

import com.jtd.commons.entity.BaseEntity;

public class BossRequestLog extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String requestIp;
	
	private String requestPort;

	private String requestUrl;
	
	private String bossPartnerCode;

	private Integer requestType;

	private String requestData;

	private String resultCode;

	private String resultMessage;

	public String getRequestIp() {
		return requestIp;
	}

	public void setRequestIp(String requestIp) {
		this.requestIp = requestIp;
	}

	public String getRequestPort() {
		return requestPort;
	}

	public void setRequestPort(String requestPort) {
		this.requestPort = requestPort;
	}

	public String getRequestUrl() {
		return requestUrl;
	}

	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}

	public String getBossPartnerCode() {
		return bossPartnerCode;
	}

	public void setBossPartnerCode(String bossPartnerCode) {
		this.bossPartnerCode = bossPartnerCode;
	}

	public Integer getRequestType() {
		return requestType;
	}

	public void setRequestType(Integer requestType) {
		this.requestType = requestType;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode == null ? null : resultCode.trim();
	}

	public String getResultMessage() {
		return resultMessage;
	}

	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage == null ? null : resultMessage
				.trim();
	}

	public String getRequestData() {
		return requestData;
	}

	public void setRequestData(String requestData) {
		this.requestData = requestData == null ? null : requestData.trim();
	}
}