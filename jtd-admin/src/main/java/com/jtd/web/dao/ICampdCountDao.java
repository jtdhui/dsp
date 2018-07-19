package com.jtd.web.dao;

import java.util.List;
import java.util.Map;

import com.jtd.commons.dao.BaseDao;
import com.jtd.commons.page.Pagination;
import com.jtd.web.po.count.Campd;

public interface ICampdCountDao extends BaseDao<Campd> {
	
	/**
	 * 查询Campd表的各项sum，包括点击率，投 放cpm，投 放cpc
	 * 
	 * @param params
	 * @return
	 */
	public Map<String, Object> getReportSumSix(Map<String, Object> params);

	/**
	 * 为时间报表echarts图表查询结果
	 * 
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> listByMapForTimeChart(
            Map<String, Object> params, boolean needGroupBy);

	/**
	 * 为活动类型报表echarts图表查询结果
	 * 
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> listByMapForCampTypeChart(
            Map<String, Object> params, boolean needGroupBy);
	
	/**
	 * 为活动报表echarts图表查询结果
	 * 
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> listByMapForCampChart(
            Map<String, Object> params, boolean needGroupBy);

	/**
	 * 为时间报表分页记录查询结果
	 * 
	 * @param page
	 * @return
	 */
	public List<Map<String, Object>> getMapPageForTimeReport(
            Pagination<Map<String, Object>> page);
	
	/**
	 * 为活动类型报表分页记录查询结果
	 * 
	 * @param page
	 * @return
	 */
	public List<Map<String, Object>> getMapPageForCampTypeReport(
            Pagination<Map<String, Object>> page);
	
	/**
	 * 为活动报表分页记录查询结果
	 * 
	 * @param page
	 * @return
	 */
	public List<Map<String, Object>> getMapPageForCampReport(
            Pagination<Map<String, Object>> page);

	
	public List<Campd> findListByCampIds(String ids);
}
