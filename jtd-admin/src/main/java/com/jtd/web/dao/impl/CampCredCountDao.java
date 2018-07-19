package com.jtd.web.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.jtd.commons.dao.BaseDaoImpl;
import com.jtd.commons.page.Pagination;
import com.jtd.web.dao.ICampCredCountDao;
import com.jtd.web.po.count.CampCred;

@Repository
public class CampCredCountDao extends BaseDaoImpl<CampCred> implements
		ICampCredCountDao {

	@Override
	public List<Map<String, Object>> listByMapForCreativeChart(
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
				getStatement("listMapForCreativeChart"), params);
	}

	@Override
	public List<Map<String, Object>> getMapPageForCreativeReport(
			Pagination<Map<String, Object>> page) {

		if (page == null)
			throw new RuntimeException("page is null");

		return getSqlSession().selectList(
				getStatement("listMapForCreativeReport"), page);
	}

}
