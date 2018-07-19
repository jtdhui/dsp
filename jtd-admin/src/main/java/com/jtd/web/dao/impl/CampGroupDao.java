package com.jtd.web.dao.impl;

import org.springframework.stereotype.Repository;

import com.jtd.commons.dao.BaseDaoImpl;
import com.jtd.web.dao.ICampGroupDao;
import com.jtd.web.po.CampGroup;

@Repository
public class CampGroupDao extends BaseDaoImpl<CampGroup> implements ICampGroupDao {

	@Override
	public CampGroup getByMap(CampGroup cg) {
		return getSqlSession().selectOne(getStatement("getByMap"),cg);
	}

}
