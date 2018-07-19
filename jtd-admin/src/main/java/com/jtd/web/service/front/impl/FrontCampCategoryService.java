package com.jtd.web.service.front.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jtd.commons.dao.BaseDao;
import com.jtd.commons.mybatis.support.CustomerContextHolder;
import com.jtd.web.dao.ICampaignCategoryDao;
import com.jtd.web.po.CampaignCategory;
import com.jtd.web.service.front.IFrontCampCategoryService;
import com.jtd.web.service.impl.BaseService;

@Service
public class FrontCampCategoryService extends BaseService<CampaignCategory> implements IFrontCampCategoryService {

	@Autowired
	private ICampaignCategoryDao campaignCategoryDao;

	@Override
	protected BaseDao<CampaignCategory> getDao() {
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		return campaignCategoryDao;
	}
	
	@Override
	public List<Map<String, Object>> findChannelCatgByCampId(Map<String, Object> map) {
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		return campaignCategoryDao.selectChannelCatgByCampId(map);
	}


}
