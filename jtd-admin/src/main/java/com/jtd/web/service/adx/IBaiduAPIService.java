package com.jtd.web.service.adx;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jtd.web.service.AuditResult;

public interface IBaiduAPIService {

	/**
	 * 同步（添加/修改)广告主 + 向MQ发送广告主提交审核消息
	 * 
	 * @param partnerId
	 * @return
	 */
	public int syncAdvertiser(long submitUserId, long partnerId,
                              String qualiDocImgBaseURL);

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
	 * 同步（添加/修改)创意  + 向MQ发送广告提交审核消息
	 * 
	 * @param adId
	 * @return
	 */
	public int syncCreative(long adId);

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
	 * @param adId
	 * @return TODO
	 * @return
	 */
	public AuditResult queryCreativeAuditStatus(long adId);
	
	/**
	 * 同步供应方媒体过滤设置信息
	 */
	public void syncSSPSetting();
	
	/**
	 * 获取供应方媒体过滤设置分页信息
	 * 
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public JSONObject getSyncSSPSetting(int pageIndex, int pageSize);
	
	/**
	 * 查询所有已提交广告主，日期格式为yyyy-MM-dd，跨度不得大于30天
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public JSONObject queryAllAdvertiser(String startDate, String endDate);
	
	/**
	 * 查询所有已提交创意，日期格式为yyyy-MM-dd，跨度不得大于30天
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public JSONObject queryAllCreative(String startDate, String endDate);
	
	/**
	 * 获取bes平台的竞价参数报表的数据
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public JSONObject reportRTB(String startDate,String endDate);
	
	/**
	 * 获取bes平台的竞价消费报表的数据
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public JSONObject reportConsume(String startDate,String endDate);
	
	/**
	 * 获取bes平台的竞价消费报表-分广告主报表的数据
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public JSONObject reportAdvertiserConsume(String startDate,String endDate);
	
	/**
	 * 获取bes平台的竞价参数报表-分创意报表的数据
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public JSONArray reportCreativeRTB(String startDate,String endDate);
}
