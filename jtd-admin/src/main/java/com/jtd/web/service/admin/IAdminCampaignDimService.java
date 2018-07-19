package com.jtd.web.service.admin;

import com.jtd.web.po.CampaignDim;
import com.jtd.web.service.IBaseService;

public interface IAdminCampaignDimService extends IBaseService<CampaignDim> {

	public CampaignDim selectByCampAndDimName(CampaignDim obj);
	
	public long updateByCampAndDimName(CampaignDim obj);
}
