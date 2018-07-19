package com.jtd.web.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.jtd.commons.dao.BaseDaoImpl;
import com.jtd.web.dao.IPartnerStatusDao;
import com.jtd.web.po.PartnerStatusPO;

@Repository
public class PartnerStatusDao extends BaseDaoImpl<PartnerStatusPO> implements IPartnerStatusDao{

	public List<PartnerStatusPO> listByPartnerId(Long partnerId) {
		if (partnerId == null)
			throw new RuntimeException("partnerId is null");
		return getSqlSession().selectList(getStatement("listByPartnerId"),
				partnerId);

	}

	public List<Map<String, Object>> listAllChannelAuditStatusByPartnerId(Long partnerId) {
		if (partnerId == null)
			throw new RuntimeException("partnerId is null");
		return getSqlSession().selectList(getStatement("listAllChannelAuditStatusByPartnerId"),
				partnerId);

	}

	public List<PartnerStatusPO> listByMap(HashMap<String, Object> params) {
		if (params == null)
			throw new RuntimeException("params is null");
		return getSqlSession().selectList(getStatement("listByMap"),
				params);

	}

	@Override
	public long updateStatus(int status,String auditInfo,Long submitUserId,long partnerId,long channelId) {
		
		return updateStatus(status,auditInfo,submitUserId,partnerId,channelId,0);

	}
	
	public long updateStatus(Integer status,String auditInfo,Long submitUserId,Long partnerId,Long channelId,Integer auditMqSuccess){
		
		HashMap<String,Object> params = new HashMap<String, Object>();
		params.put("status", status);
		params.put("auditInfo", auditInfo);
		params.put("submitUserId", submitUserId);
		params.put("updateTime", new Date()); // 更新时间
		params.put("partnerId", partnerId);
		params.put("channelId", channelId);
		params.put("auditMqSuccess", auditMqSuccess);
		
		return getSqlSession().update(getStatement("updateStatusByMap"), params);
		
	}
}
