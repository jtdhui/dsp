package com.jtd.web.po;

import java.util.Date;

import com.jtd.commons.entity.BaseEntity;

public class PartnerStatusPO extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8210830359418502009L;

	private Long id;

	private Long partnerId;

	private Long channelId;

	private Integer status;

	private String auditInfo;

	private Date updateTime;

	private Long submitUserId;
	
	private Integer auditMqSuccess ;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(Long partnerId) {
		this.partnerId = partnerId;
	}

	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getAuditInfo() {
		return auditInfo;
	}

	public void setAuditInfo(String auditInfo) {
		this.auditInfo = auditInfo == null ? null : auditInfo.trim();
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Long getSubmitUserId() {
		return submitUserId;
	}

	public void setSubmitUserId(Long submitUserId) {
		this.submitUserId = submitUserId;
	}

	public Integer getAuditMqSuccess() {
		return auditMqSuccess;
	}

	public void setAuditMqSuccess(Integer auditMqSuccess) {
		this.auditMqSuccess = auditMqSuccess;
	}

	
	
}