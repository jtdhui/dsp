package com.jtd.web.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.jtd.commons.dao.BaseDao;
import com.jtd.web.constants.TradeType;
import com.jtd.web.po.PartnerAccFlow;

public interface IPartnerAccFlowDao extends BaseDao<PartnerAccFlow> {
	
	/**
	 * 
	 * @param params
	 * @return
	 */
	public List<PartnerAccFlow> listByMap(Map<String, Object> params);

	/**
	 * 
	 * @param partnerId
	 * @return
	 */
	public List<PartnerAccFlow> listByPartnerId(Long partnerId);

	/**
	 * 查询partner（相对截止日期）最近一次的消费记录
	 * 
	 * @param partnerId
	 * @param operatorUserId
	 * @param tradeType
	 * @param endDate 设定截止日期
	 * @return
	 */
	public PartnerAccFlow getLastRecord(Long partnerId,
			Long operatorUserId, TradeType tradeType, Date endDate);

	/**
	 * 按partnerId,tradeType,开始日期，结束日期统计流水数额
	 * 
	 * @param partnerId
	 * @param tradeType
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public long getAmountSum(Long partnerId, TradeType tradeType,
			String startDate, String endDate);
}
