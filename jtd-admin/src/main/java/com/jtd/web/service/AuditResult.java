package com.jtd.web.service;

import com.jtd.web.constants.AuditStatus;

public class AuditResult {

	private AuditStatus auditStatus;
	private String refuseReason;

	public AuditStatus getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(AuditStatus auditStatus) {
		this.auditStatus = auditStatus;
	}

	public String getRefuseReason() {
		return refuseReason;
	}

	public void setRefuseReason(String refuseReason) {
		this.refuseReason = refuseReason;
	}

}
