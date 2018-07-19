package com.jtd.web.service.impl;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.jtd.web.constants.AuditStatus;
import com.jtd.web.constants.CatgSerial;
import com.jtd.web.constants.PartnerStatus;
import com.jtd.web.jms.AuditAdMsg;
import com.jtd.web.jms.AuditPartnerMsg;
import com.jtd.web.jms.ChangePartnerStatusMsg;
import com.jtd.web.jms.CommitAdMsg;
import com.jtd.web.jms.CommitPartnerMsg;
import com.jtd.web.jms.SetPartnerGrossProfitMsg;
import com.jtd.web.service.IMQConnectorService;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置
 * @创建日期 2016年10月26日
 * @项目名称 dsp-admin
 * @描述 <p>
 *     向MQ发送信息
 *     </p>
 */
@Service
public class MQConnectorService implements IMQConnectorService {
	
	private static final Logger log = LoggerFactory.getLogger("mqInfoLog");
	
	@Resource
	private JmsTemplate jmsTemplate;

	@Override
	public int sendMessage(final com.jtd.web.jms.Message message) {
		
		log.info("发送到引擎---开始----");
		try {
			jmsTemplate.send(new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					TextMessage textMessage = session.createTextMessage();
					String jsonStr = JSON.toJSONString(message, SerializerFeature.WriteClassName);
					log.info("发送到引擎: "+jsonStr);
					textMessage.setText(jsonStr);
					return textMessage;
				}
			});
		} catch (JmsException e) {
			log.error("发送到引擎发生错误",e);
			e.printStackTrace();
			return 0 ;
		}
		log.info("发送到引擎---结束----");
	
		return 1 ;
	}

	@Override
	public void sendPartnerStatusChangeMessage(long partnerId, PartnerStatus status) {

		ChangePartnerStatusMsg msg = new ChangePartnerStatusMsg();
		msg.setPartnerid(partnerId);
		msg.setStatus(status);
		this.sendMessage(msg);
	}

	@Override
	public void sendPartnerCommitChannelMessage(long partnerId, CatgSerial channel) {

		CommitPartnerMsg msg = new CommitPartnerMsg();
		msg.setChannelId(channel.getChannelId());
		msg.setPartnerid(partnerId);
		this.sendMessage(msg);
	}

	@Override
	public void sendAdCommitChannelMessage(long adId, CatgSerial channel) {
		CommitAdMsg commitAdMsg = new CommitAdMsg();
		commitAdMsg.setAdid(adId);
		commitAdMsg.setChannelId(channel.getChannelId());
		this.sendMessage(commitAdMsg);
	}

	public void sendAuditPartnerMessage(long partnerId, CatgSerial channel,
			AuditStatus status) {

		AuditPartnerMsg auditPartnerMsg = new AuditPartnerMsg();
		auditPartnerMsg.setPartnerid(partnerId);
		//如果为空就是partner内部审核
		if(channel != null){
			auditPartnerMsg.setChannelId(channel.getChannelId());
		}
		if (status == AuditStatus.STATUS_SUCCESS) {
			auditPartnerMsg.setAuditpassed(true);
		} else if (status == AuditStatus.STATUS_FAIL) {
			auditPartnerMsg.setAuditpassed(false);
		}
		this.sendMessage(auditPartnerMsg);

	}

	public void sendAuditAdMessage(long adId, CatgSerial channel, AuditStatus status) {

		AuditAdMsg auditAdMsg = new AuditAdMsg();
		auditAdMsg.setAdid(adId);
		if (status == AuditStatus.STATUS_SUCCESS) {
			auditAdMsg.setAuditpassed(true);
		} else if (status == AuditStatus.STATUS_FAIL) {
			auditAdMsg.setAuditpassed(false);
		}
		auditAdMsg.setChannelId(channel.getChannelId());
		this.sendMessage(auditAdMsg);

	}

	public void sendSetPartnerGrossProfitMessage(long partnerId, int grossProfit) {

		SetPartnerGrossProfitMsg setPartnerGrossProfitMsg = new SetPartnerGrossProfitMsg();
		setPartnerGrossProfitMsg.setPartnerid(partnerId);
		setPartnerGrossProfitMsg.setGrossProfit(grossProfit);
		this.sendMessage(setPartnerGrossProfitMsg);

	}
}
