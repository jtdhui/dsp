package com.jtd.web.dao.impl;

import java.util.HashMap;

import org.springframework.stereotype.Repository;

import com.jtd.commons.dao.BaseDaoImpl;
import com.jtd.web.dao.IQualiDocTypeChannelCodeDao;
import com.jtd.web.po.QualiDocTypeChannelCode;

@Repository
public class QualiDocTypeChannelCodeDao extends
		BaseDaoImpl<QualiDocTypeChannelCode> implements
		IQualiDocTypeChannelCodeDao {

	@Override
	public QualiDocTypeChannelCode findBy(long channelId, long docTypeId) {

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("channelId", channelId);
		params.put("docTypeId", docTypeId);

		return getSqlSession().selectOne(getStatement("listByMap"), params);

	}

}
