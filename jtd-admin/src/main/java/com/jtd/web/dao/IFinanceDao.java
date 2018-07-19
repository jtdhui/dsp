package com.jtd.web.dao;

import java.util.List;
import java.util.Map;

/**
 * Created by duber on 2017/6/19.
 */
public interface IFinanceDao{

	/**
	 * 充值明细查询 by map
	 * 
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> listFinaceDetailByMap(Map<String, Object> params);
	
	/**
	 * 代理充值明细查询 by map
	 * 
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> proxyFinaceDetailList(Map<String, Object> params);

	/**
	 * 代理商为下级广告主充值以及退款 by map
	 * 
	 * @param pMap
	 * @return
	 */
	public List<Map<String, Object>> clawBackAndAmountBack(Map<String, Object> pMap);

	/**
	 * 根据tradeId获取上级广告主的余额
	 * 
	 * @param tradeId
	 * @return
	 */
	public String getAccBalanceResult(String tradeId);

	public List<Map<String,Object>> listFinanceBy(Map<String, Object> paraMap);

	


}
