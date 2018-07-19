package com.jtd.web.service.front;

import com.jtd.web.po.CampGroup;
import com.jtd.web.po.Campaign;
import com.jtd.web.service.IBaseService;

public interface IFrontCampGroupService extends IBaseService<CampGroup> {

	public CampGroup getByMap(CampGroup cg);
	
	/**
	 * 发送消息到引擎
	 * @param entity
	 */
	public void sendCampGroupMessage(CampGroup campGroup);

	/**
	 * 保存活动
	 * @param newCampaign
	 */
	public void saveCampaign(Campaign entity);
}
