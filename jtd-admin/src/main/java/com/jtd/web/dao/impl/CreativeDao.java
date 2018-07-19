package com.jtd.web.dao.impl;

import org.springframework.stereotype.Repository;

import com.jtd.commons.dao.BaseDaoImpl;
import com.jtd.web.dao.ICreativeDao;
import com.jtd.web.po.Creative;

import java.util.Map;

/**
 * 素材数据
 * @作者 duber
 * @版本 V1.0
 * @创建日期 2016年11月12日
 * @描述
 */
@Repository
public class CreativeDao extends BaseDaoImpl<Creative> implements ICreativeDao {

	@Override
	public Creative getByMap(Creative entity) {
		return getSqlSession().selectOne(getStatement("getByMap"), entity);
	}

	@Override
	public Map<String, Object> getAdsBy(Map<String, Object> map) {
		Map<String, Object> ret = getSqlSession().selectOne(getStatement("getAdsBy"), map);
		return ret;
	}
}
