package com.jtd.web.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.jtd.commons.dao.BaseDaoImpl;
import com.jtd.web.dao.ICampaignCategoryDao;
import com.jtd.web.po.CampaignCategory;

/**
 * 
 * @作者 duber
 * @版本 V1.0
 * @创建日期 2016年10月20日
 * @描述 广告活动的行业类别DAO实现类
 */
@Repository
public class CampaignCategoryDao extends BaseDaoImpl<CampaignCategory> implements ICampaignCategoryDao {

	@Override
	public List<CampaignCategory> selectByCampaignId(long campaign_id) {
		List<CampaignCategory> list = getSqlSession().selectList(getStatement("selectByCampaignId"),campaign_id);
		return list;
	}

	@Override
	public long deleteByCampaignId(long campaign_id) {
		return  getSqlSession().delete(getStatement("deleteByCampaignId"), campaign_id);
	}

	@Override
	public List<Map<String, Object>> selectChannelCatgByCampId(Map<String, Object> map) {
		List<Map<String, Object>> list = getSqlSession().selectList(getStatement("selectChannelCatgByCampId"),map);
		return list;
	}

}
