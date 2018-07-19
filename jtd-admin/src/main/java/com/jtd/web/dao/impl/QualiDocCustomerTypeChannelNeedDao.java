package com.jtd.web.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.jtd.commons.dao.BaseDaoImpl;
import com.jtd.web.dao.IQualiDocCustomerTypeChannelNeedDao;
import com.jtd.web.po.QualiDocCustomerTypeChannelNeed;
import com.jtd.web.po.QualiDocType;

@Repository
public class QualiDocCustomerTypeChannelNeedDao extends BaseDaoImpl<QualiDocCustomerTypeChannelNeed> implements
		IQualiDocCustomerTypeChannelNeedDao {

	@Override
	public List<QualiDocCustomerTypeChannelNeed> listByMap(Map<String, Object> params) {
		if (params == null)
			throw new RuntimeException("params is null");
		return getSqlSession().selectList(getStatement("listByMap"), params);

	}
	
	public List<QualiDocType> listDocTypeByCustomerTypeId(long customerTypeId) {
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("customerTypeId", customerTypeId);
		
		return getSqlSession().selectList(getStatement("listDocTypeByMap"), params);

	}

	public List<QualiDocType> listDocTypeForChannel(long channelId , long customerTypeId , int isMainQualidoc) {
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("channelId", channelId);
		params.put("customerTypeId", customerTypeId);
		params.put("isMainQualidoc", isMainQualidoc);
		
		return getSqlSession().selectList(getStatement("listDocTypeByMap"), params);

	}
}
