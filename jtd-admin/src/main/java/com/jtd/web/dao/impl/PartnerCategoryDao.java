package com.jtd.web.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.jtd.commons.dao.BaseDaoImpl;
import com.jtd.web.po.PartnerCategory;

@Repository
public class PartnerCategoryDao extends BaseDaoImpl<PartnerCategory> {

	public List<PartnerCategory> selectByPartnerId(Long partnerId)
			throws Exception {
		if (partnerId == null)
			throw new RuntimeException("partnerId is null");
		return getSqlSession().selectList(getStatement("selectByPartnerId"),
				partnerId);

	}
	
	public void deleteByPartnerId(Long partnerId)
			throws Exception {
		if (partnerId == null)
			throw new RuntimeException("partnerId is null");
		getSqlSession().delete(getStatement("deleteByPartnerId"),
				partnerId);

	}

}
