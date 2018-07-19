package com.jtd.web.po;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.jtd.commons.entity.BaseEntity;

public class QualiDoc extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8953274371747615316L;

	private Long id;

	private Long partnerId;

	private Long uploadUserId;

	private Long docTypeId;

	private String docName;

	private String docOldName;

	private String docPath;

	private String docNumber;

	private String docValidDate;

	private Date validityStart;

	private Date validityEnd;

	private Integer status;

	private Date internalAuditTime;

	private Long auditUserId;

	private Date updateTime;

	private Integer auditMqSuccess;

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

	public String getDocName() {
		return docName;
	}

	public void setDocName(String docName) {
		this.docName = docName == null ? null : docName.trim();
	}

	public String getDocPath() {
		return docPath;
	}

	public void setDocPath(String docPath) {
		this.docPath = docPath == null ? null : docPath.trim();
	}

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getValidityStart() {
		return validityStart;
	}

	public void setValidityStart(Date validityStart) {
		this.validityStart = validityStart;
	}

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getValidityEnd() {
		return validityEnd;
	}

	public void setValidityEnd(Date validityEnd) {
		this.validityEnd = validityEnd;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getDocTypeId() {
		return docTypeId;
	}

	public void setDocTypeId(Long docTypeId) {
		this.docTypeId = docTypeId;
	}

	public String getDocOldName() {
		return docOldName;
	}

	public void setDocOldName(String docOldName) {
		this.docOldName = docOldName;
	}

	public Date getInternalAuditTime() {
		return internalAuditTime;
	}

	public void setInternalAuditTime(Date internalAuditTime) {
		this.internalAuditTime = internalAuditTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getDocNumber() {
		return docNumber;
	}

	public void setDocNumber(String docNumber) {
		this.docNumber = docNumber;
	}

	public String getDocValidDate() {
		return docValidDate;
	}

	public void setDocValidDate(String docValidDate) {
		this.docValidDate = docValidDate;
	}

	public Long getUploadUserId() {
		return uploadUserId;
	}

	public void setUploadUserId(Long uploadUserId) {
		this.uploadUserId = uploadUserId;
	}

	public Long getAuditUserId() {
		return auditUserId;
	}

	public void setAuditUserId(Long auditUserId) {
		this.auditUserId = auditUserId;
	}

	public Integer getAuditMqSuccess() {
		return auditMqSuccess;
	}

	public void setAuditMqSuccess(Integer auditMqSuccess) {
		this.auditMqSuccess = auditMqSuccess;
	}

}