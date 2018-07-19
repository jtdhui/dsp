package com.jtd.web.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.jtd.commons.dao.BaseDaoImpl;
import com.jtd.web.dao.ICamphCountDao;
import com.jtd.web.po.count.Camph;

/**
 * 统计库报表数据
 * 
 * @作者 duber
 * @版本 V1.0
 * @创建日期 2016年11月1日
 * @描述
 */
@Repository
public class CamphCountDao extends BaseDaoImpl<Camph> implements ICamphCountDao {

	@Override
	public List<Map<String, Object>> listMapForHourSumChart(
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
				getStatement("listMapForHourSumChart"), params);
	}

	@Override
	public List<Map<String, Object>> getMapPageForHourSumReport(
			Map<String, Object> params) {

		if (params == null)
			throw new RuntimeException("page is null");

		return getSqlSession().selectList(
				getStatement("listMapForHourSumReport"), params);
	}

	@Override
	public List<Map<String, Object>> listMapForHourAvgChart(
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
				getStatement("listMapForHourAvgChart"), params);
	}

	@Override
	public List<Map<String, Object>> getMapPageForHourAvgReport(
			Map<String, Object> params) {

		if (params == null)
			throw new RuntimeException("params is null");

		return getSqlSession().selectList(
				getStatement("listMapForHourAvgReport"), params);
	}
}
