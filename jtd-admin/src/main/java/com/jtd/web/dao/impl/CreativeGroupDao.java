package com.jtd.web.dao.impl;

import org.springframework.stereotype.Repository;

import com.jtd.commons.dao.BaseDaoImpl;
import com.jtd.web.dao.ICreativeGroupDao;
import com.jtd.web.po.CreativeGroup;

/**
 * 素材分组
 * @作者 duber
 * @版本 V1.0
 * @创建日期 2016年11月12日
 * @描述
 */
@Repository
public class CreativeGroupDao extends BaseDaoImpl<CreativeGroup> implements ICreativeGroupDao {

	@Override
	public CreativeGroup getByMap(CreativeGroup cg) {
		return getSqlSession().selectOne(getStatement("getByMap"),cg);
	}

}
