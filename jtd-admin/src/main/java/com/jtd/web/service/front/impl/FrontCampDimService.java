package com.jtd.web.service.front.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jtd.commons.dao.BaseDao;
import com.jtd.commons.mybatis.support.CustomerContextHolder;
import com.jtd.web.dao.ICampaignDimDao;
import com.jtd.web.po.CampaignDim;
import com.jtd.web.service.front.IFrontCampDimService;
import com.jtd.web.service.impl.BaseService;

@Service
public class FrontCampDimService extends BaseService<CampaignDim> implements IFrontCampDimService {

	@Autowired
	private ICampaignDimDao campaignDimDao;
	
	@Override
	protected BaseDao<CampaignDim> getDao() {
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		return campaignDimDao;
	}
	
	@Override
	public CampaignDim selectByCampAndDimName(CampaignDim obj) {
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		return campaignDimDao.selectByCampAndDimName(obj);
	}

	@Override
	public long updateByCampAndDimName(CampaignDim obj) {
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		return campaignDimDao.updateByCampAndDimName(obj);
	}

	@Override
	public void deleteListByCampId(long campId) {
		campaignDimDao.deleteCampaignDim(campId);
	}

}
