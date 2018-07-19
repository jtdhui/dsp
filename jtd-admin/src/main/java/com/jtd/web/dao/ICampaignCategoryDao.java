package com.jtd.web.dao;

import java.util.List;
import java.util.Map;

import com.jtd.commons.dao.BaseDao;
import com.jtd.web.po.CampaignCategory;

public interface ICampaignCategoryDao extends BaseDao<CampaignCategory>{

	public List<CampaignCategory> selectByCampaignId(long campaign_id);
	
	public List<Map<String,Object>> selectChannelCatgByCampId(Map<String, Object> map);
	
	public long deleteByCampaignId(long campaign_id);
	
}
