package com.jtd.web.dao;

import com.jtd.commons.dao.BaseDao;
import com.jtd.web.po.Campaign;

import java.util.List;
import java.util.Map;

/**
 * 
 * @作者 duber
 * @版本 V1.0
 * @创建日期 2016年10月19日
 * @描述 后台广告活动DAO接口
 */
public interface ICampaignDao extends BaseDao<Campaign>{

	/**
	 * 后台活动列表
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> listReportCampBy(Map<String, Object> map);

	/**
	 * 后台活动按ID查询
	 * @param id
	 * @return
	 */
	public Map<String, Object> getCampByID(long id);

	/**
	 * 后台活动查询
	 * @param map
	 * @return
	 */
	public Map<String, Object> getCampByMap(Map<String, Object> map);

	/**
	 * 活动与广告主关联查询
	 * @param id
	 * @return
	 */
	public Map<String, Object> getCampPartnerBy(long id);

	public List<Map<String, Object>> getCreativesByCampId(Map<String, Object> map);
	
	/**
	 * 修改自动状态和综合状态（综合状态为自动状态和手动状态结合计算出的一个状态）
	 * @param campaignId
	 * @param autoStatus
	 * @param campaignStatus
	 * @return
	 */
	public long updateAutoStatus(long campaignId, int autoStatus, int campaignStatus,int orderBy);

	public Campaign getByMap(Campaign camp);

	public List<Campaign> getCampaignsBy(Map<String, Object> paraMap);

    public List<Campaign> selectCampsByMap(Map<String, Object> campMap);

    public void updateProfit(Campaign camp);
}
