package com.jtd.web.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.jtd.commons.dao.BaseDaoImpl;
import com.jtd.web.dao.IQualiDocCustomerTypeDao;
import com.jtd.web.po.QualiDocCustomerType;

@Repository
public class QualiDocCustomerTypeDao extends BaseDaoImpl<QualiDocCustomerType> implements
		IQualiDocCustomerTypeDao {

	@Override
	public List<QualiDocCustomerType> listByMap(Map<String, Object> params) {
		if (params == null)
			throw new RuntimeException("params is null");
		return getSqlSession().selectList(getStatement("listByMap"), params);

	}

}
