package com.jtd.web.jms;

import com.jtd.web.constants.CampaignAutoStatus;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月24日
 * @项目名称 dsp-common-entity
 * @描述 <p>自动状态改变消息</p>
 */
public class ChangeAutoStatusMsg extends Message{
	
	private static final long serialVersionUID = -3837873245472987798L;
	private long campid;
	private CampaignAutoStatus status;
	/**
	 * @return the campid
	 */
	public long getCampid() {
		return campid;
	}
	/**
	 * @param campid the campid to set
	 */
	public void setCampid(long campid) {
		this.campid = campid;
	}
	/**
	 * @return the status
	 */
	public CampaignAutoStatus getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(CampaignAutoStatus status) {
		this.status = status;
	}
	@Override
	public MsgType getType() {
		return MsgType.CHANGE_AUTO_STATUS;
	}
}
