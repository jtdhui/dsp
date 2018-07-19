package com.jtd.web.dao;

import java.util.List;
import java.util.Map;

import com.jtd.commons.dao.BaseDao;
import com.jtd.web.po.count.CampChanneld;

public interface ICampChanneldDao extends BaseDao<CampChanneld> {

	/**
	 * 活动渠道图形报表 || 活动渠道报表总计
	 * 
	 * @param param
	 * @param needGroupBy
	 * @return
	 */
	public List<Map<String, Object>> listSumByMap(Map<String, Object> param,
			boolean needGroupBy);
	
	/**
	 * 活动渠道报表list
	 * 
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> listByMap(Map<String, Object> param);

}
