package com.jtd.web.dao;

import java.util.List;
import java.util.Map;

import com.jtd.commons.dao.BaseDao;
import com.jtd.commons.page.Pagination;
import com.jtd.web.po.count.CampAdTpd;

public interface ICampAdTpdCountDao extends BaseDao<CampAdTpd> {

	
	/**
	 * 为活动类型报表echarts图表查询结果
	 * 
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> listByMapForCampTypeChart(
            Map<String, Object> params);

}
