package com.jtd.web.service.admin;

import java.util.List;
import java.util.Map;

import com.jtd.web.po.AdAuditStatus;
import com.jtd.web.service.IBaseService;

public interface IAdminAdAuditStatusService extends IBaseService<AdAuditStatus> {

	public List<Map<String,Object>> findListByAdId(Map<String, Object> paraMap);

	public long removeByAdId(Map<String, Object> paraMap);

	public AdAuditStatus findBy(AdAuditStatus adAuditStatus);

	public void updateAdAuditStatus(AdAuditStatus adAuditStatus);
}
