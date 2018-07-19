package com.jtd.web.po;

import com.jtd.commons.entity.BaseEntity;

public class PartnerStatusQualiDoc extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8210830359418502009L;

	private Long id;

	private Long partnerId;

	private Long channelId;

	private Long qualiDocId;

	private Long docTypeId;

	private Integer isMainDoc;

	private Long submitUserId;

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

	public Long getQualiDocId() {
		return qualiDocId;
	}

	public void setQualiDocId(Long qualiDocId) {
		this.qualiDocId = qualiDocId;
	}

	public Long getDocTypeId() {
		return docTypeId;
	}

	public void setDocTypeId(Long docTypeId) {
		this.docTypeId = docTypeId;
	}

	public Integer getIsMainDoc() {
		return isMainDoc;
	}

	public void setIsMainDoc(Integer isMainDoc) {
		this.isMainDoc = isMainDoc;
	}

	public Long getSubmitUserId() {
		return submitUserId;
	}

	public void setSubmitUserId(Long submitUserId) {
		this.submitUserId = submitUserId;
	}

}