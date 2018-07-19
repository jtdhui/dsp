package com.jtd.web.dao.impl;

import org.springframework.stereotype.Repository;

import com.jtd.commons.dao.BaseDaoImpl;
import com.jtd.web.dao.ICampaignDimDao;
import com.jtd.web.po.CampaignDim;

@Repository
public class CampaignDimDao extends BaseDaoImpl<CampaignDim> implements ICampaignDimDao {

	@Override
	public CampaignDim selectByCampAndDimName(CampaignDim obj) {
		CampaignDim campaignDim = getSqlSession().selectOne(getStatement("selectByCampAndDimName"),obj);
		return campaignDim;
	}

	@Override
	public long updateByCampAndDimName(CampaignDim obj) {
		long result = getSqlSession().update(getStatement("updateByCampAndDimName"), obj);
		return result;
	}

	@Override
	public void deleteCampaignDim(Long campaignId) {
		getSqlSession().delete(getStatement("deleteCampaignDim"), campaignId);
	}

}
