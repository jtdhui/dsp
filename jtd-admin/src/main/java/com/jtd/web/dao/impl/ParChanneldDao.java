package com.jtd.web.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.jtd.commons.dao.BaseDaoImpl;
import com.jtd.web.dao.IParChanneldDao;
import com.jtd.web.po.count.ParChanneld;

@Repository
public class ParChanneldDao extends BaseDaoImpl<ParChanneld> implements IParChanneldDao {
	
	@Override
	public List<Map<String, Object>> listByMap(Map<String, Object> param) {
		
		if (param == null)
			throw new RuntimeException("param is null");
		
		return getSqlSession().selectList(getStatement("listByMap"), param);
	}
	
}
