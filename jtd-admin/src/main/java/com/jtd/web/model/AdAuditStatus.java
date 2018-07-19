package com.jtd.web.model;



import com.jtd.web.constants.AuditStatus;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月25日
 * @项目名称 dsp-admin
 * @描述 <p>广告-渠道审核状态</p>
 */
public class AdAuditStatus implements java.io.Serializable {

	private static final long serialVersionUID = -8284286099801531563L;

	private Long id;
	/**
	 * 广告ID
	 */
	private Long adId;
	/**
	 * 渠道ID
	 */
	private Long channelId;
	/**
	 * 状态
	 * STATUS_NOTCOMMIT(0, "未提交"), STATUS_WAIT(1, "待审核"), STATUS_SUCCESS(2, "审核通过"), STATUS_FAIL(3, "审核拒绝") , STATUS_ERROR(4, "接口报错");
	 */
	private AuditStatus status;
	/**
	 * 审核结果信息
	 */
	private String auditInfo;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAdId() {
		return adId;
	}

	public void setAdId(Long adId) {
		this.adId = adId;
	}

	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}

	public AuditStatus getStatus() {
		return status;
	}

	public void setStatus(AuditStatus status) {
		this.status = status;
	}

	public String getAuditInfo() {
		return auditInfo;
	}

	public void setAuditInfo(String auditInfo) {
		this.auditInfo = auditInfo;
	}

}