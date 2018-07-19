package com.jtd.web.dao;

import com.jtd.commons.dao.BaseDao;
import com.jtd.web.po.AdAuditStatus;

import java.util.List;
import java.util.Map;

public interface IAdAuditStatusDao extends BaseDao<AdAuditStatus> {

	public List<Map<String,Object>> getListByAdId(Map<String, Object> paraMap);

	public long deleteByAdId(Map<String, Object> paraMap);

	/**
	 * 按照adId和channelId来修改 status,auditInfo , auditMqSuccess
	 * 
	 * @param status
	 * @param auditInfo
	 * @param adId
	 * @param channelId
	 * @param auditMqSuccess
	 * @param internalAuditMqSuccess
	 * @return
	 */
	public long updateStatus(int status, String auditInfo, long adId, long channelId , Integer auditMqSuccess , Integer internalAuditMqSuccess);

	public AdAuditStatus getBy(AdAuditStatus adAuditStatus);
}
