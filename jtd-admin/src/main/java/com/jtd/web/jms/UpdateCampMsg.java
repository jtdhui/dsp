package com.jtd.web.jms;

import com.jtd.web.model.Campaign;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月24日
 * @项目名称 dsp-common-entity
 * @描述 <p>更新活动</p>
 */
public class UpdateCampMsg extends Message{
	
	private static final long serialVersionUID = 3647050923732868512L;
	private Campaign campaign;

	/**
	 * @return the campaign
	 */
	public Campaign getCampaign() {
		return campaign;
	}

	/**
	 * @param campaign the campaign to set
	 */
	public void setCampaign(Campaign campaign) {
		this.campaign = campaign;
	}

	@Override
	public MsgType getType() {
		return MsgType.UPDATE_CAMPAIGN;
	}
}
