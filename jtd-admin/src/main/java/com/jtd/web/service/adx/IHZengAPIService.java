package com.jtd.web.service.adx;

import com.alibaba.fastjson.JSONObject;
import com.jtd.web.service.AuditResult;

public interface IHZengAPIService {

	/**
	 * 同步（添加/修改)广告主 + 向MQ发送广告主提交审核消息
	 * 
	 * @param partnerId
	 * @return
	 */
	public boolean syncAdvertiser(long submitUserId, long partnerId);

	/**
	 * 查询广告主，如果存在，直接返回广告主json对象
	 * 
	 * @param partnerId
	 * @return
	 */
	public JSONObject queryAdvertiserById(long partnerId);

	/**
	 * 由定时任务调用，同步渠道审核情况 + 向MQ发送广告主审核状态变更消息
	 * 
	 * @param partnerId
	 * @return TODO
	 * @return
	 */
	public AuditResult queryAdvertiserAuditStatus(long partnerId);

	/**
	 * 同步（添加/修改)创意 + 向MQ发送广告提交审核消息
	 * 
	 * @param adId
	 * @return
	 */
	public boolean syncCreative(long adId);

	/**
	 * 查询创意是否存在
	 * 
	 * @param adId
	 * @return
	 */
	public JSONObject queryCreativeById(long adId);

	/**
	 * 由定时任务调用，同步渠道审核情况 + 向MQ发送广告审核情况变更消息
	 * 
	 * @param adIdArray
	 * @return TODO
	 * @return
	 */
	public AuditResult queryCreativeAuditStatus(long adId);

}
