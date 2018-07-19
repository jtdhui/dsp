package com.jtd.web.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.jtd.commons.dao.BaseDaoImpl;
import com.jtd.web.dao.IPartnerStatusQualiDocDao;
import com.jtd.web.po.PartnerStatusQualiDoc;

@Repository
public class PartnerStatusQualiDocDao extends
		BaseDaoImpl<PartnerStatusQualiDoc> implements IPartnerStatusQualiDocDao {

	@Override
	public List<PartnerStatusQualiDoc> listByMap(HashMap<String, Object> params) {
		if (params == null)
			throw new RuntimeException("params is null");
		return getSqlSession().selectList(getStatement("listByMap"), params);

	}

	public PartnerStatusQualiDoc findWhenChannelAudit(long partnerId,
			long docTypeId, long channelId) {
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("partnerId", partnerId);
		params.put("docTypeId", docTypeId);
		params.put("channelId", channelId);
		
		return getSqlSession().selectOne(getStatement("listByMap"), params);

	}

	@Override
	public long updateWhenChannelAudit(long qualiDocId, long submitUserId,
			long partnerId, long docTypeId, long channelId) {

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("partnerId", partnerId);
		params.put("docTypeId", docTypeId);
		params.put("channelId", channelId);

		params.put("qualiDocId", qualiDocId);
		params.put("submitUserId", submitUserId);

		return getSqlSession().update(getStatement("updateUploadQualiDoc"),
				params);
	}

	// public long deleteBy(long partnerId,long docTypeId) {
	//
	// HashMap<String,Object> map = new HashMap<String, Object>();
	// map.put("partnerId", partnerId);
	// map.put("docTypeId", docTypeId);
	//
	// return getSqlSession().delete(getStatement("deleteByMap"), map);
	//
	// }
}
