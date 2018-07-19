package com.jtd.web.dao.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.jtd.commons.dao.BaseDaoImpl;
import com.jtd.utils.DateUtil;
import com.jtd.web.constants.TradeType;
import com.jtd.web.dao.IPartnerAccFlowDao;
import com.jtd.web.po.PartnerAccFlow;

@Repository
public class PartnerAccFlowDao extends BaseDaoImpl<PartnerAccFlow> implements IPartnerAccFlowDao{

	@Override
	public List<PartnerAccFlow> listByPartnerId(Long partnerId) {
		if (partnerId == null)
			throw new RuntimeException("partnerId is null");
		
		HashMap<String,Object> params = new HashMap<String, Object>();
		params.put("partnerId", partnerId);
		
		return getSqlSession().selectList(getStatement("listByMap"),
				params);

	}
	
	@Override
	public List<PartnerAccFlow> listByMap(Map<String,Object> params) {
		
		if (params == null)
			throw new RuntimeException("params is null");
		
		return getSqlSession().selectList(getStatement("listByMap"),
				params);
	}

	@Override
	public long getAmountSum(Long partnerId,TradeType tradeType,String startDate,String endDate){
		
		HashMap<String,Object> params = new HashMap<String, Object>();
		params.put("partnerId", partnerId);
		params.put("tradeType", tradeType.getCode());
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		
		List<Map<String,Object>> list = getSqlSession().selectList(getStatement("getAmountSum"),
				params);
		if(list != null && list.size() == 1){
			Map<String,Object> map = list.get(0);
			if(map != null && map.get("sum") != null){
				return ((BigDecimal)map.get("sum")).longValue();
			}
		}
		return 0 ;
	}
	
	@Override
	public PartnerAccFlow getLastRecord(Long partnerId, Long operatorUserId,
			TradeType tradeType, Date endDate) {
		
		if (partnerId == null)
			throw new RuntimeException("partnerId is null");
		
		if (tradeType == null)
			throw new RuntimeException("tradeType is null");
		
		HashMap<String,Object> params = new HashMap<String, Object>();
		params.put("partnerId", partnerId);
		params.put("operatorId", operatorUserId);
		params.put("tradeType", tradeType.getCode());
		params.put("endDate", DateUtil.getDateStr(endDate, "yyyy-MM-dd"));
		
		List<PartnerAccFlow> list = getSqlSession().selectList(getStatement("getLastRecord"),
				params);
		
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		return null ;
	}

}
