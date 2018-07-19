package com.jtd.web.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.jtd.commons.dao.BaseDaoImpl;
import com.jtd.web.dao.IAdDao;
import com.jtd.web.po.Ad;

/**
 * 
 * @作者 duber
 * @版本 V1.0
 * @创建日期 2016年10月27日
 * @描述
 */
@Repository
public class AdDao extends BaseDaoImpl<Ad> implements IAdDao {

	@Override
	public Map<String, Object> findAdMapById(Map<String, Object> paraMap) {
		Map<String, Object> obj = getSqlSession().selectOne( getStatement("findAdMapById"), paraMap);
		return obj;
	}

	@Override
	public List<Map<String, Object>> listByMap(Map<String, Object> paraMap) {
		if (paraMap == null) {
			throw new RuntimeException("params is null");
		}
		return getSqlSession().selectList(getStatement("listByMap"), paraMap);
	}

	@Override
	public long updateAdListByCampId(Ad ad) {
		long result = getSqlSession().update(getStatement("updateAdListByCampId"), ad);
		return result;
	}

	@Override
	public Map<String, Object> getFullAdById(Ad ad) {
		if (ad == null) {
			throw new RuntimeException("ad is null");
		}
		return getSqlSession().selectOne(getStatement("getFullAdById"), ad);
	}

	@Override
	public void deleteAdByCamapignId(Long campaignId) {
		getSqlSession().delete(getStatement("deleteAdByCamapignId"), campaignId);
	}
}
