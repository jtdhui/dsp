package com.jtd.web.service.admin.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jtd.commons.dao.BaseDao;
import com.jtd.commons.mybatis.support.CustomerContextHolder;
import com.jtd.web.dao.ICampaignCategoryDao;
import com.jtd.web.po.CampaignCategory;
import com.jtd.web.po.SysUser;
import com.jtd.web.service.admin.IAdminCampaignCategoryService;
import com.jtd.web.service.impl.BaseService;

/**
 * 
 * @作者 duber
 * @版本 V1.0
 * @创建日期 2016年10月20日
 * @描述 后台广告活动行业类型Service 实现类
 */
@Service
public class AdminCampaignCategoryService extends BaseService<CampaignCategory>  implements IAdminCampaignCategoryService {

	@Autowired
	private ICampaignCategoryDao  campaignCategoryDao;
	
	@Override
	protected BaseDao<CampaignCategory> getDao() {
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		return campaignCategoryDao;
	}
	
	@Override
	public List<CampaignCategory> selectByCampaignId(long campaign_id) {
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		return campaignCategoryDao.selectByCampaignId(campaign_id);
	}

	@Override
	public void deleteByCampaignId(long campaign_id) {
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		campaignCategoryDao.deleteByCampaignId(campaign_id);
	}

}
