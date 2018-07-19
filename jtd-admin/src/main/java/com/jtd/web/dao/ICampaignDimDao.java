package com.jtd.web.dao;

import com.jtd.commons.dao.BaseDao;
import com.jtd.web.po.CampaignDim;

public interface ICampaignDimDao  extends BaseDao<CampaignDim>{

	public CampaignDim selectByCampAndDimName(CampaignDim obj);
	
	public long updateByCampAndDimName(CampaignDim obj);

	public void deleteCampaignDim(Long campaignId);
}
