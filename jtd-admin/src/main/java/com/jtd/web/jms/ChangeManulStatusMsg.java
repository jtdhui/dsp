package com.jtd.web.jms;

import com.jtd.web.constants.CampaignManulStatus;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月24日
 * @项目名称 dsp-common-entity
 * @描述 <p>修改活动手动状态</p>
 */
public class ChangeManulStatusMsg extends Message{
	
	private static final long serialVersionUID = -9195179218892837003L;
	private long campid;
	private CampaignManulStatus status;
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
	public CampaignManulStatus getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(CampaignManulStatus status) {
		this.status = status;
	}
	@Override
	public MsgType getType() {
		return MsgType.CHANGE_MANUL_STATUS;
	}
}
