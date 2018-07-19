package com.jtd.web.dao;

import java.util.List;
import java.util.Map;

import com.jtd.commons.dao.BaseDao;
import com.jtd.commons.page.Pagination;
import com.jtd.web.po.count.CampCityd;



public interface ICampCitydCountDao extends BaseDao<CampCityd> {

	/**
	 * 为地域报表图表查询结果（按城市）
	 * 
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> listByMapForCityChart(
            Map<String, Object> params, boolean needGroupBy);
	
	/**
	 * 为地域报表图表查询结果（按省份）
	 * 
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> listByMapForProvinceChart(
            Map<String, Object> params, boolean needGroupBy);

	/**
	 * 为地域报表分页列表查询结果（按城市）
	 * 
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> getMapPageForCityReport(
            Pagination<Map<String, Object>> page);
	
	/**
	 * 为地域报表分页列表查询结果（按省份）
	 * 
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> getMapPageForProvinceReport(
            Pagination<Map<String, Object>> page);

}
