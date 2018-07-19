package com.jtd.web.jms;
/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月24日
 * @项目名称 dsp-common-entity
 * @描述 <p>消息类型枚举</p>
 */
public enum MsgType {
	UPDATE_CAMPAIGN, // 更新活动
	CHANGE_AUTO_STATUS, // 修改活动自动状态
	CHANGE_MANUL_STATUS, // 修改活动手动状态
	CHANGE_PARTNER_STATUS, // 修改广告主状态，对应后台的开启或者是暂停
	AUDIT_PARTNER, // 审核伙伴(广告主)
	COMMIT_PARTNER, // 提交伙伴(广告主)
	COMMIT_AD, // 提交广告
	AUDIT_AD, // 审核广告
	SET_BLACKLIST, // 设置平台黑名单列表
	SET_CAMP_BLACKLIST, // 设置活动黑名单列表
	SET_CAMP_ADP_BLACKLIST, // 设置活动的广告位黑名单
	SET_PARTNER_BLACKLIST, // 设置广告主黑名单列表
	SET_PARTNER_ADP_BLACKLIST, // 设置广告主的广告位黑名单
	SET_AUTO_PAR_ADP_HOST_BLACKLIST, // 设置自动黑名单
	SET_CAMP_POOR_TRAFF_NOTIFY, // 设置劣质流量告警
	SET_CAMP_HOUR_PAUSE, // 设置活动小时投放控制
	CHANGE_CAMP_CATG, // 修改活动的行业类别
	CHANGE_AD_CATG, // 修改广告的行业类别
	CHANGE_CAMP_PRICE_FACTOR, // 修改活动的价格因子,由统计系统发给引擎，管理端不用理会
	CHANGE_CAMP_CTR, // 修改活动最近周期的CTR，管理端不用理会
	CHANGE_CAMP_GROUP, // 修改推广组的设置字段
	SET_PARTNER_GROSS_PROFIT, // 设置广告主毛利率
	SET_CAMP_GROSS_PROFIT, // 设置活动毛利率
	SET_MAX_NEGATIVE_GROSS_PROFIT, // 设置最大的负毛利
	HEARTBEAT, // 统计系统发送的心跳
	CHANGE_AF, // 修改AF
	BID_COUNT, // 发送竞价次数
	SET_AUTO_ADP_BLACKLIST,
	REFRESH_ONLINE_DATA;// 刷新线上数据
}
