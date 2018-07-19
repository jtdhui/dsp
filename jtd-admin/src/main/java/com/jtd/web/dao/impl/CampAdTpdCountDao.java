package com.jtd.web.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.jtd.commons.dao.BaseDaoImpl;
import com.jtd.web.dao.ICampAdTpdCountDao;
import com.jtd.web.po.count.CampAdTpd;

@Repository
public class CampAdTpdCountDao extends BaseDaoImpl<CampAdTpd> implements
		ICampAdTpdCountDao {

	@Override
	public List<Map<String, Object>> listByMapForCampTypeChart(
			Map<String, Object> params) {

		if (params == null)
			throw new RuntimeException("params is null");

		return getSqlSession().selectList(
				getStatement("listMapForCampTypeReport"), params);
	}

}
