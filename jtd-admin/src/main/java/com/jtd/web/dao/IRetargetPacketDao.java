package com.jtd.web.dao;

import java.util.List;
import java.util.Map;

import com.jtd.commons.dao.BaseDao;
import com.jtd.web.po.RetargetPacket;

public interface IRetargetPacketDao extends BaseDao<RetargetPacket> {
	
	public RetargetPacket findByPartnerIdAndRemark(long partnerId, String remark);
	
	public List<RetargetPacket> listByPartnerId(long partnerId);

	public List<Map<String,Object>> listMapByPartnerId(long partnerId);

}
