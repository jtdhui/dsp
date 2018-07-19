package com.jtd.web.jms;

import com.jtd.web.constants.PartnerStatus;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月24日
 * @项目名称 dsp-common-entity
 * @描述 <p>修改广告主状态，对应后台的开启或者是暂停</p>
 */
public class ChangePartnerStatusMsg extends Message{
	
	private static final long serialVersionUID = -3837873245472987798L;
	private long partnerid;
	private PartnerStatus status;// 后台的开启和停止广告主

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
	 * @return the status
	 */
	public PartnerStatus getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(PartnerStatus status) {
		this.status = status;
	}
	@Override
	public MsgType getType() {
		return MsgType.CHANGE_PARTNER_STATUS;
	}
}
