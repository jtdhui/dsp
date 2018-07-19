package com.jtd.web.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jtd.commons.dao.BaseDao;
import com.jtd.web.po.PartnerStatusPO;

public interface IPartnerStatusDao extends BaseDao<PartnerStatusPO> {

	public List<PartnerStatusPO> listByPartnerId(Long partnerId);

	public List<Map<String, Object>> listAllChannelAuditStatusByPartnerId(Long partnerId);

	public List<PartnerStatusPO> listByMap(HashMap<String, Object> params);

	/**
	 * 按照partnerId和channelId来修改 status,auditInfo,submitUserId (submitUserId可能为空)
	 * 
	 * @param status
	 * @param auditInfo
	 * @param submitUserId
	 * @param partnerId
	 * @param channelId
	 * @return
	 */
	public long updateStatus(int status, String auditInfo, Long submitUserId, long partnerId, long channelId);
	
	/**
	 * 
	 * @param status
	 * @param auditInfo
	 * @param submitUserId
	 * @param partnerId
	 * @param channelId
	 * @param submitMqSuccess
	 * @return
	 */
	public long updateStatus(Integer status,String auditInfo,Long submitUserId,Long partnerId,Long channelId,Integer auditMqSuccess);
}
