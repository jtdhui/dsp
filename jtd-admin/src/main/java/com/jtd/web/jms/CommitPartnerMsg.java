package com.jtd.web.jms;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月24日
 * @项目名称 dsp-common-entity
 * @描述 <p>提交伙伴(广告主)</p>
 */
public class CommitPartnerMsg extends Message{

	private static final long serialVersionUID = -2400742645501699836L;
	private long partnerid;
	private Long channelId; // 当是DSP本身审核时，此处的channelId不填为空

	/**
	 * @return the partnerid
	 */
	public long getPartnerid() {
		return partnerid;
	}
	/**
	 * @param partnerid the partnerid to set
	 */
	public void setPartnerid(long partnerid) {
		this.partnerid = partnerid;
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
		return MsgType.COMMIT_PARTNER;
	}
}
