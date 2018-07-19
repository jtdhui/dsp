package com.jtd.web.dao;

import com.jtd.commons.dao.BaseDao;
import com.jtd.web.po.CreativeGroup;

public interface ICreativeGroupDao extends BaseDao<CreativeGroup> {

	public CreativeGroup getByMap(CreativeGroup cg);

}
