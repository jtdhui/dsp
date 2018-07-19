package com.jtd.web.dao.impl;

import com.jtd.commons.dao.BaseDaoImpl;
import com.jtd.web.dao.ICampaignDao;
import com.jtd.web.po.Campaign;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @作者 duber
 * @版本 V1.0
 * @创建日期 2016年10月19日
 * @描述 后台广告活动DAO实现类
 */
@Repository
public class CampaignDao extends BaseDaoImpl<Campaign> implements ICampaignDao {

	/**
	 * 报表查询活动
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public List<Map<String, Object>> listReportCampBy(Map<String, Object> map) {
		List<Map<String, Object>> list = getSqlSession().selectList(
				getStatement("listReportCampBy"), map);
		return list;
	}

	/**
	 * 后台活动按ID查询
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public Map<String, Object> getCampByID(long id) {
		Map<String, Object> obj = getSqlSession().selectOne(
				getStatement("getCampByID"), id);
		return obj;
	}

	@Override
	public Map<String, Object> getCampByMap(Map<String, Object> map) {
		Map<String, Object> obj = getSqlSession().selectOne(
				getStatement("getCampByMap"), map);
		return obj;
	}

	@Override
	public Map<String, Object> getCampPartnerBy(long id) {
		Map<String, Object> obj = getSqlSession().selectOne(
				getStatement("getCampPartnerBy"), id);
		return obj;
	}

	@Override
	public List<Map<String, Object>> getCreativesByCampId(
			Map<String, Object> map) {
		List<Map<String, Object>> list = getSqlSession().selectList(
				getStatement("getCreativesByCampId"), map);
		return list;
	}

	@Override
	public long updateAutoStatus(long campaignId, int autoStatus,
			int campaignStatus,int orderBy) {

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("id", campaignId);
		params.put("autoStatus", autoStatus);
		params.put("campaignStatus", campaignStatus);
        params.put("orderBy",orderBy);

		return getSqlSession().update(getStatement("updateAutoStatus"), params);
	}

	@Override
	public Campaign getByMap(Campaign camp) {
		Campaign obj = getSqlSession().selectOne( getStatement("getByMap"), camp);
		return obj;
	}
	
	@Override
	public List<Campaign> getCampaignsBy(Map<String, Object> paraMap) {
		List<Campaign> list = getSqlSession().selectList( getStatement("getCampaignsBy"), paraMap);
		return list;
	}

	@Override
	public List<Campaign> selectCampsByMap(Map<String, Object> campMap) {
		List<Campaign> list = getSqlSession().selectList( getStatement("selectCampsByMap"), campMap);
		return list;
	}

	@Override
	public void updateProfit(Campaign camp) {
		getSqlSession().update(getStatement("updateProfit"), camp);
	}

}
