package com.jtd.web.dao;

import java.util.List;
import java.util.Map;

import com.jtd.commons.dao.BaseDao;
import com.jtd.web.po.count.ParChanneld;

public interface IParChanneldDao extends BaseDao<ParChanneld> {

	/**
	 * 活动渠道报表list
	 * 
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> listByMap(Map<String, Object> param);

}
