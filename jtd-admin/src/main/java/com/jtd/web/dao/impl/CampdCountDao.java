package com.jtd.web.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.jtd.commons.dao.BaseDaoImpl;
import com.jtd.commons.page.Pagination;
import com.jtd.web.dao.ICampdCountDao;
import com.jtd.web.po.count.Campd;

/**
 * 统计库报表数据
 * 
 * @作者 duber
 * @版本 V1.0
 * @创建日期 2016年11月1日
 * @描述
 */
@Repository
public class CampdCountDao extends BaseDaoImpl<Campd> implements ICampdCountDao {

	public Map<String, Object> getReportSumSix(Map<String, Object> params) {
		if (params == null)
			throw new RuntimeException("params is null");

		return getSqlSession().selectOne(getStatement("getReportSumSix"),
				params);
	}

	@Override
	public List<Map<String, Object>> listByMapForTimeChart(
			Map<String, Object> params,boolean needGroupBy) {
		if (params == null)
			throw new RuntimeException("params is null");
		
		if(needGroupBy){
			params.put("needGroupBy", 1);
		}
		else{
			params.remove("needGroupBy");
		}

		return getSqlSession().selectList(getStatement("listMapForTimeChart"),
				params);
	}

	@Override
	public List<Map<String, Object>> listByMapForCampTypeChart(
			Map<String, Object> params,boolean needGroupBy) {

		if (params == null)
			throw new RuntimeException("params is null");
		
		if(needGroupBy){
			params.put("needGroupBy", 1);
		}
		else{
			params.remove("needGroupBy");
		}
		
		return getSqlSession().selectList(getStatement("listMapForCampTypeChart"),
				params);
	}
	
	@Override
	public List<Map<String, Object>> listByMapForCampChart(
			Map<String, Object> params,boolean needGroupBy) {

		if (params == null)
			throw new RuntimeException("params is null");
		
		if(needGroupBy){
			params.put("needGroupBy", 1);
		}
		else{
			params.remove("needGroupBy");
		}
		
		return getSqlSession().selectList(getStatement("listMapForCampChart"),
				params);
	}
	
	@Override
	public List<Map<String, Object>> getMapPageForTimeReport(
			Pagination<Map<String, Object>> page) {
		
		if (page == null)
			throw new RuntimeException("page is null");
		
		return getSqlSession().selectList(getStatement("listMapForTimeReport"),
				page);
	}

	@Override
	public List<Map<String, Object>> getMapPageForCampTypeReport(
			Pagination<Map<String, Object>> page) {
		
		if (page == null)
			throw new RuntimeException("page is null");
		
		return getSqlSession().selectList(getStatement("listMapForCampTypeReport"),
				page);
	}
	
	@Override
	public List<Map<String, Object>> getMapPageForCampReport(
			Pagination<Map<String, Object>> page) {
		
		if (page == null)
			throw new RuntimeException("page is null");
		
		return getSqlSession().selectList(getStatement("listMapForCampReport"),
				page);
	}
	
	@Override
	public List<Campd> findListByCampIds(String ids) {
		String[] id_str = ids.split(",");
		Long[] id_arr = new Long[id_str.length];
		for(int i=0;i<id_str.length;i++){
			id_arr[i] = Long.parseLong(id_str[i]);
		}
		Map<String, Object> params = new HashMap<String, Object>(2);
		params.put("ids", id_arr);
		List<Campd> list = getSqlSession().selectList(getStatement("findListByCampIds"), params);
		return list;
	}

}
