package com.jtd.web.dao.impl;

import com.jtd.commons.dao.BaseDaoImpl;
import com.jtd.web.dao.IAdAuditStatusDao;
import com.jtd.web.po.AdAuditStatus;

import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 创意渠道审核状态
 * @作者 duber
 * @版本 V1.0
 * @创建日期 2016年10月31日
 * @描述
 */
@Repository
public class AdAuditStatusDao extends BaseDaoImpl<AdAuditStatus> implements IAdAuditStatusDao {

	@Override
	public List<Map<String,Object>> getListByAdId(Map<String, Object> paraMap) {
		List<Map<String,Object>> list = getSqlSession().selectList(getStatement("selectByAdId"), paraMap);
		return list;
	}

	@Override
	public long deleteByAdId(Map<String, Object> paraMap) {
		long ret = getSqlSession().delete(getStatement("deleteByAdId"),paraMap);
		return ret;
	}

	@Override
	public long updateStatus(int status,String auditInfo,long adId ,long channelId , Integer auditMqSuccess , Integer internalAuditMqSuccess) {
		
		HashMap<String,Object> params = new HashMap<String, Object>();
		params.put("status", status);
		params.put("auditInfo", auditInfo);
		params.put("auditTime", new Date()); // 更新时间
		params.put("auditMqSuccess", auditMqSuccess); // 更新时间
		params.put("adId", adId);
		params.put("channelId", channelId);
		
		return getSqlSession().update(getStatement("updateStatusByMap"), params);

	}

	@Override
	public AdAuditStatus getBy(AdAuditStatus adAuditStatus) {
		AdAuditStatus ret = getSqlSession().selectOne(getStatement("selectBy"),adAuditStatus);
		return ret;
	}
}
