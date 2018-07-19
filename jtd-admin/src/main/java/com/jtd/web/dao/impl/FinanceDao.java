package com.jtd.web.dao.impl;

import java.util.List;
import java.util.Map;

import com.jtd.commons.dao.BaseDaoImpl;
import com.jtd.web.dao.IFinanceDao;
import com.jtd.web.po.PartnerPreFlow;

import org.springframework.stereotype.Repository;

/**
 * Created by duber on 2017/6/19.
 */
@Repository
public class FinanceDao extends BaseDaoImpl<PartnerPreFlow> implements
		IFinanceDao {

	@Override
	public List<Map<String, Object>> listFinaceDetailByMap(
			Map<String, Object> params) {
		if (params == null)
			throw new RuntimeException("params is null");

		return getSqlSession().selectList(
				getStatement("listFinaceDetailByMap"), params);
	}
	
	@Override
	public List<Map<String, Object>> proxyFinaceDetailList(
			Map<String, Object> params) {
		if (params == null)
			throw new RuntimeException("params is null");

		return getSqlSession().selectList(
				getStatement("proxyFinaceDetailList"), params);
	}
	
	@Override
	public List<Map<String, Object>> clawBackAndAmountBack(Map<String, Object> pMap){
		if (pMap == null)
			throw new RuntimeException("params is null");

		return getSqlSession().selectList(
				getStatement("clawBackAndAmountBack"), pMap);
	}

	@Override
	public List<Map<String, Object>> listFinanceBy(Map<String, Object> paraMap) {
		List<Map<String, Object>> list = getSqlSession().selectList(
				getStatement("listFinanceBy"), paraMap);
		return list;
	}

	@Override
	public String getAccBalanceResult(String tradeId) {
		return getSqlSession().selectOne(getStatement("getAccBalanceResult"), tradeId);
	}
}
