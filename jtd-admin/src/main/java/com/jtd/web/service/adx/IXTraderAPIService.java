package com.jtd.web.service.adx;

import com.jtd.web.service.AuditResult;

public interface IXTraderAPIService {

	/**
	 * 同步（添加/修改)创意  + 向MQ发送广告提交审核消息
	 * 
	 * @param adId
	 * @return
	 */
	public int syncCreative(long adId);


	/**
	 * 由定时任务调用，同步渠道审核情况 + 向MQ发送广告审核情况变更消息
	 * 
	 * @param adIdArray
	 * @return TODO
	 * @return
	 */
	public AuditResult queryCreativeAuditStatus(long adId);
	
}
