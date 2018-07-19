package com.jtd.web.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.jtd.commons.dao.BaseDaoImpl;
import com.jtd.web.constants.CookieType;
import com.jtd.web.dao.IRetargetPacketDao;
import com.jtd.web.po.RetargetPacket;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月25日
 * @项目名称 dsp-admin
 * @描述 <p>访客找回定向包</p>
 */
@Repository
public class RetargetPacketDao extends BaseDaoImpl<RetargetPacket> implements IRetargetPacketDao {

	@Override
	public List<RetargetPacket> listByPartnerId(long partnerId) {
		HashMap<String,Object> params = new HashMap<String, Object>();
		params.put("partnerId", partnerId);
		
		return getSqlSession().selectList(getStatement("listByMap"),params);
	}
	
	@Override
	public List<Map<String,Object>> listMapByPartnerId(long partnerId){
		
		HashMap<String,Object> params = new HashMap<String, Object>();
		params.put("partnerId", partnerId);
		
		return getSqlSession().selectList(getStatement("listMapByMap"),params);
	}

	@Override
	public RetargetPacket findByPartnerIdAndRemark(long partnerId,String remark){
		
		HashMap<String,Object> params = new HashMap<String, Object>();
		params.put("partnerId", partnerId);
		params.put("remark", remark);
		
		return getSqlSession().selectOne(getStatement("listByMap"),params);
		
	}

	

}
