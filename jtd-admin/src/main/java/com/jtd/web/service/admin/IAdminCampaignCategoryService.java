package com.jtd.web.service.admin;

import java.util.List;

import com.jtd.web.po.CampaignCategory;
import com.jtd.web.service.IBaseService;

public interface IAdminCampaignCategoryService extends IBaseService<CampaignCategory>{

	public List<CampaignCategory> selectByCampaignId(long campaign_id);
	
	public void deleteByCampaignId(long campaign_id);
}
