package com.jtd.web.jms;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月24日
 * @项目名称 dsp-common-entity
 * @描述 <p>提交广告</p>
 */
public class CommitAdMsg extends Message{

	private static final long serialVersionUID = -2400742645501699836L;
	private long adid;
	private Long channelId;
	
	public long getAdid() {
		return adid;
	}
	public void setAdid(long adid) {
		this.adid = adid;
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
		return MsgType.COMMIT_AD;
	}
}
