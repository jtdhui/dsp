package com.jtd.web.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.jtd.commons.dao.BaseDaoImpl;
import com.jtd.web.po.PartnerDim;

@Repository
public class PartnerDimDao extends BaseDaoImpl<PartnerDim> {

	public List<PartnerDim> selectByPartnerId(Long partnerId)
			throws Exception {
		if (partnerId == null)
			throw new RuntimeException("partnerId is null");
		return getSqlSession().selectList(getStatement("selectByPartnerId"),
				partnerId);

	}
	
	public PartnerDim selectBlacklistByPartnerId(Long partnerId)
			throws Exception {
		if (partnerId == null)
			throw new RuntimeException("partnerId is null");
		List<PartnerDim> list = getSqlSession().selectList(getStatement("selectBlacklistByPartnerId"),
				partnerId);
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		return null ;
	}
	
	public long insertByMap(HashMap<String, Object> map) throws Exception {
		if (map == null)
			throw new RuntimeException("params is null");
		return getSqlSession().insert(getStatement("insertByMap"), map);

	}

	public long updateByMap(HashMap<String, Object> map) throws Exception {
		if (map == null)
			throw new RuntimeException("params is null");
		return getSqlSession().update(getStatement("updateByMap"), map);

	}
	
}
