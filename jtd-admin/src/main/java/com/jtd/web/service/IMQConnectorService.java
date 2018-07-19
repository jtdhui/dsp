package com.jtd.web.service;

import com.jtd.web.constants.AuditStatus;
import com.jtd.web.constants.CatgSerial;
import com.jtd.web.constants.PartnerStatus;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置
 * @创建日期 2016年10月26日
 * @项目名称 dsp-admin
 * @描述 <p>
 *     向mq发送数据
 *     </p>
 */
public interface IMQConnectorService {
	/**
	 * 返回1表示发送成功，0表示失败
	 * @param message
	 */
	public int sendMessage(com.jtd.web.jms.Message message);

	/**
	 * 发送广告主状态变更消息
	 * 
	 * @param partnerId
	 * @param status
	 */
	public void sendPartnerStatusChangeMessage(long partnerId,
                                               PartnerStatus status);

	/**
	 * 发送广告主提交渠道消息
	 * 
	 * @param partnerId
	 * @param channel
	 */
	public void sendPartnerCommitChannelMessage(long partnerId,
                                                CatgSerial channel);

	/**
	 * 发送广告提交渠道消息
	 * 
	 * @param partnerId
	 * @param channel
	 */
	public void sendAdCommitChannelMessage(long adId, CatgSerial channel);

	/**
	 * 发送广告主审核状态变更消息
	 * 
	 * @param partnerId
	 * @param channelId
	 * @param status
	 */
	public void sendAuditPartnerMessage(long partnerId, CatgSerial channel,
                                        AuditStatus status);

	/**
	 * 发送广告审核状态变更消息
	 * 
	 * @param partnerId
	 * @param channel
	 * @param status
	 */
	public void sendAuditAdMessage(long adId, CatgSerial channel,
                                   AuditStatus status);

	/**
	 * 发送广告主毛利率变更消息
	 * 
	 * @param partnerId
	 * @param channelId
	 * @param status
	 */
	public void sendSetPartnerGrossProfitMessage(long partnerId, int grossProfit);

}
