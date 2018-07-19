package com.jtd.web.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.jtd.commons.dao.BaseDaoImpl;
import com.jtd.web.dao.IQualiDocDao;
import com.jtd.web.po.QualiDoc;

@Repository
public class QualiDocDao extends BaseDaoImpl<QualiDoc> implements IQualiDocDao {

	@Override
	public List<QualiDoc> listByPartnerId(Long partnerId) {
		if (partnerId == null)
			throw new RuntimeException("partnerId is null");
		return getSqlSession().selectList(getStatement("listByPartnerId"),
				partnerId);

	}

	@Override
	public List<Map<String, Object>> listMapByPartnerId(Long partnerId) {

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("partnerId", partnerId);

		return getSqlSession().selectList(getStatement("listMapByMap"),
				params);

	}

	@Override
	public Map<String, Object> findByPartnerIdAndDocType(Long partnerId,
			Long docTypeId) {

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("partnerId", partnerId);
		params.put("docTypeId", docTypeId);

		return getSqlSession().selectOne(getStatement("listMapByMap"), params);
	}

	@Override
	public long updateStatus(HashMap<String, Object> map) {
		if (map == null)
			throw new RuntimeException("params is null");
		return getSqlSession().update(getStatement("updateStatus"), map);
	}

}
