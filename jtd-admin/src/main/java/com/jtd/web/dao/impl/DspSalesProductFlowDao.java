package com.jtd.web.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.jtd.commons.dao.BaseDaoImpl;
import com.jtd.web.constants.BossApiType;
import com.jtd.web.dao.IDspSalesProductFlowDao;
import com.jtd.web.po.DspSalesProductFlow;

@Repository
public class DspSalesProductFlowDao extends BaseDaoImpl<DspSalesProductFlow>
		implements IDspSalesProductFlowDao {
	
	public DspSalesProductFlow getPartnerLastUpgrade(long partnerId) {

		HashMap<String, Object> params = new HashMap<String, Object>();

		params.put("partnerId", partnerId);
		params.put("type", BossApiType.ACCOUNT_UPGRADE);
		
		List<DspSalesProductFlow> list = getSqlSession().selectList(
				getStatement("getPartnerLastUpgrade"), params);
	
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		
		return null ;
	}

}
