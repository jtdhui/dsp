package com.jtd.tencent.service;

import java.util.HashMap;

/**
 * 帐号服务
 * @author zl
 *
 */
public interface AdvertiserService {

	/**添加服务商子客户 */
	public Object addAdvertiser(HashMap<String,Object> params); 
	
	/** 更新广告主信息 */
	public Object updateAdvertiser(HashMap<String,Object> params);
	
	/** 获取广告主信息 */
	public Object getAdvertiser(HashMap<String,Object> parmas);
	
	/** 发起代理商与子客户之间的转账 */
	public Object transferFund();
	
	/** 获取资金账户信息 */
	public Object getFunds();
	
	/** 获取资金账户日结明细 */
	public Object getFundStatementsDaily();
	
	/** 获取资金账户流水 */
	public Object getFundStatementsDetailed();
	
	/** 获取实时消耗 */
	public Object getRealtimeCost();
}
