package com.jtd.web.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.jtd.commons.dao.BaseDaoImpl;
import com.jtd.web.constants.CatgSerial;
import com.jtd.web.dao.IAdCategoryDao;
import com.jtd.web.po.AdCategory;

@Repository
public class AdCategoryDao extends BaseDaoImpl<AdCategory> implements IAdCategoryDao {

	@Override
	public List<Map<String, Object>> selectChannelCatgByAdId(Map<String, Object> map) {
		List<Map<String, Object>> list = getSqlSession().selectList(getStatement("selectChannelCatgByAdId"),map);
		return list;
	}

	@Override
	public void deleteByAdId(Long adId) {
		getSqlSession().delete(getStatement("deleteByAdId"), adId);
	}

	@Override
	public List<AdCategory> selectByAdId(Long adId) {
		List<AdCategory> list = getSqlSession().selectList(getStatement("selectByAdId"),adId);
		return list;
	}

	@Override
	public AdCategory getAdCategory(AdCategory ac) {
		AdCategory ret = getSqlSession().selectOne(getStatement("getAdCategory"),ac);
		return ret;
	}

	@Override
	public List<AdCategory> findAdCategoryBy(AdCategory ac) {
		List<AdCategory> ret = getSqlSession().selectList(getStatement("findAdCategoryBy"),ac);
		return ret;
	}

	@Override
	public void deleteAdCategory(AdCategory adCategory) {
		getSqlSession().delete(getStatement("deleteAdCategory"), adCategory);
	}

	@Override
	public void updateCatgId(AdCategory ac) {
		getSqlSession().update(getStatement("updateCatgId"), ac);
	}
}
