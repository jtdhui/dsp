package com.jtd.web.dao;

import java.util.List;
import java.util.Map;

import com.jtd.commons.dao.BaseDao;
import com.jtd.commons.page.Pagination;
import com.jtd.web.po.count.CampCred;

public interface ICampCredCountDao extends BaseDao<CampCred> {

	/**
	 * 为创意报表图表查询结果
	 * 
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> listByMapForCreativeChart(
            Map<String, Object> params, boolean needGroupBy);

	/**
	 * 为创意报表分页列表查询结果
	 * 
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> getMapPageForCreativeReport(
            Pagination<Map<String, Object>> page);

}
