package com.jtd.web.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.jtd.commons.dao.BaseDaoImpl;
import com.jtd.commons.page.Pagination;
import com.jtd.web.dao.ICampCitydCountDao;
import com.jtd.web.po.count.CampCityd;

@Repository
public class CampCitydCountDao extends BaseDaoImpl<CampCityd> implements
		ICampCitydCountDao {

	@Override
	public List<Map<String, Object>> listByMapForCityChart(
			Map<String, Object> params,boolean needGroupBy) {

		if (params == null)
			throw new RuntimeException("params is null");
		
		if(needGroupBy){
			params.put("needGroupBy", 1);
		}
		else{
			params.remove("needGroupBy");
		}

		return getSqlSession().selectList(
				getStatement("listMapForCityChart"), params);
	}
	
	@Override
	public List<Map<String, Object>> listByMapForProvinceChart(
			Map<String, Object> params,boolean needGroupBy) {
		
		if (params == null)
			throw new RuntimeException("params is null");
		
		if(needGroupBy){
			params.put("needGroupBy", 1);
		}
		else{
			params.remove("needGroupBy");
		}

		return getSqlSession().selectList(
				getStatement("listMapForProvinceChart"), params);
	}

	@Override
	public List<Map<String, Object>> getMapPageForCityReport(
			Pagination<Map<String, Object>> page) {

		if (page == null)
			throw new RuntimeException("page is null");

		return getSqlSession().selectList(
				getStatement("listMapForCityReport"), page);
	}


	@Override
	public List<Map<String, Object>> getMapPageForProvinceReport(
			Pagination<Map<String, Object>> page) {
		
		if (page == null)
			throw new RuntimeException("page is null");

		return getSqlSession().selectList(
				getStatement("listMapForProvinceReport"), page);
	}

}
