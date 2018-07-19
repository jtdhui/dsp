package com.jtd.web.jms;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月24日
 * @项目名称 dsp-common-entity
 * @描述 <p>广告审核消息</p>
 */
public class AuditAdMsg extends Message{

	private static final long serialVersionUID = -2400742645501699836L;
	private long adid;
	private boolean auditpassed; // 审核通过填true, 审核拒绝填false
	private Long channelId; // 当是DSP本身审核时，此处的channelId不填为空

	/**
	 * @return the adid
	 */
	public long getAdid() {
		return adid;
	}
	/**
	 * @param adid the adid to set
	 */
	public void setAdid(long adid) {
		this.adid = adid;
	}
	/**
	 * @return the auditpassed
	 */
	public boolean isAuditpassed() {
		return auditpassed;
	}
	/**
	 * @param auditpassed the auditpassed to set
	 */
	public void setAuditpassed(boolean auditpassed) {
		this.auditpassed = auditpassed;
	}
	/**
	 * @return the channelId
	 */
	public Long getChannelId() {
		return channelId;
	}
	/**
	 * @param channelId the channelId to set
	 */
	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}
	@Override
	public MsgType getType() {
		return MsgType.AUDIT_AD;
	}
	
}
