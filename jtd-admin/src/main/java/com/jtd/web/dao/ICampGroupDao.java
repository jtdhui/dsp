package com.jtd.web.dao;

import com.jtd.commons.dao.BaseDao;
import com.jtd.web.po.CampGroup;

public interface ICampGroupDao extends BaseDao<CampGroup> {

	public CampGroup getByMap(CampGroup cg);

}
