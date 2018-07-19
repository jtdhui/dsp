package com.jtd.web.service.front;

import com.jtd.web.po.CampaignDim;
import com.jtd.web.service.IBaseService;

public interface IFrontCampDimService extends IBaseService<CampaignDim> {

	public CampaignDim selectByCampAndDimName(CampaignDim obj);
	
	public long updateByCampAndDimName(CampaignDim obj);

	public void deleteListByCampId(long campId);
}
