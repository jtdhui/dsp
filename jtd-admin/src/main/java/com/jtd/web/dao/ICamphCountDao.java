package com.jtd.web.dao;

import java.util.List;
import java.util.Map;

import com.jtd.commons.dao.BaseDao;
import com.jtd.web.po.count.Camph;

public interface ICamphCountDao extends BaseDao<Camph> {

	/**
	 * 为小时报表echarts图表查询结果（按汇总）
	 * 
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> listMapForHourSumChart(
            Map<String, Object> params, boolean needGroupBy);

	/**
	 * 为小时报表分页记录查询结果（按汇总）
	 * 
	 * @param page
	 * @return
	 */
	public List<Map<String, Object>> getMapPageForHourSumReport(
            Map<String, Object> params);

	/**
	 * 为小时报表echarts图表查询结果（按平均）
	 * 
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> listMapForHourAvgChart(
            Map<String, Object> params, boolean needGroupBy);

	/**
	 * 为小时报表分页记录查询结果（按平均）
	 * 
	 * @param page
	 * @return
	 */
	public List<Map<String, Object>> getMapPageForHourAvgReport(
            Map<String, Object> params);

}
