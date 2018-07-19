package com.jtd.web.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.jtd.commons.dao.BaseDaoImpl;
import com.jtd.web.dao.IQualiDocTypeDao;
import com.jtd.web.po.QualiDocType;

@Repository
public class QualiDocTypeDao extends BaseDaoImpl<QualiDocType> implements
		IQualiDocTypeDao {

	@Override
	public List<QualiDocType> listByMap(Map<String, Object> params) {
		if (params == null)
			throw new RuntimeException("params is null");
		return getSqlSession().selectList(getStatement("listByMap"), params);

	}

	@Override
	public QualiDocType getByRemark(String remark) {
		
		HashMap<String,Object> map = new HashMap<String, Object>();
		map.put("remark", remark);
		
		return getSqlSession().selectOne(getStatement("listByMap"), map);
	}

}
