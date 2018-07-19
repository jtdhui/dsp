package com.jtd.effect.jms;

import com.alibaba.fastjson.JSON;
import com.jtd.effect.dao.CampaignDAO;
import com.jtd.web.jms.ChangeAutoStatusMsg;
import com.jtd.web.jms.ChangeManulStatusMsg;
import com.jtd.web.jms.UpdateCampMsg;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import static com.jtd.web.jms.MsgType.*;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月12日
 * @项目名称 dsp-tracker
 * @描述 <p>监听MQ消息</p>
 */
public class JMSMessageListener implements MessageListener {

	private static final Log log = LogFactory.getLog(JMSMessageListener.class);

	private CampaignDAO campaignDAO;

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.jms.MessageListener#onMessage(javax.jms.Message)
	 */
	@Override
	public void onMessage(Message msg) {

		if (msg instanceof TextMessage) {
			TextMessage tm = (TextMessage) msg;
			com.jtd.web.jms.Message message = null;
			try {
				message = JSON.parseObject(tm.getText(), com.jtd.web.jms.Message.class);
			} catch (Exception e) {
				log.error("读取message发生异常", e);
			}
			if(message == null) return;

			switch (message.getType()) {

			case UPDATE_CAMPAIGN: // 更新活动
				campaignDAO.updateCampaign(((UpdateCampMsg) message).getCampaign());
				break;
				
			case CHANGE_AUTO_STATUS: // 修改活动自动状态
				ChangeAutoStatusMsg casm = (ChangeAutoStatusMsg)message;
				campaignDAO.changeAutoStatus(casm.getCampid(), casm.getStatus());
				break;
				
			case CHANGE_MANUL_STATUS: // 修改活动手动状态
				ChangeManulStatusMsg cmsm = (ChangeManulStatusMsg)message;
				campaignDAO.changeManulStatus(cmsm.getCampid(), cmsm.getStatus());
				break;
				
			case CHANGE_PARTNER_STATUS: // 修改广告主状态，对应后台的开启或者是暂停
				break;

			case COMMIT_PARTNER:  // 提交广告主
				break;

			case AUDIT_PARTNER: // 审核伙伴(广告主)
				break;
				
			case AUDIT_AD: // 审核广告
				break;
				
			case SET_BLACKLIST: // 设置平台黑名单列表
				break;
				
			case SET_CAMP_BLACKLIST: // 设置活动黑名单列表
				break;

			case CHANGE_CAMP_CATG: // 修改活动的行业类别
				break;
				
			case CHANGE_AD_CATG: // 修改广告的行业类别
				break;
				
			case CHANGE_CAMP_PRICE_FACTOR: // 修改广告的行业类别
				break;
				
			case CHANGE_CAMP_CTR: // 修改活动最近周期的CTR
				break;

			case CHANGE_CAMP_GROUP: // 修改推广组设置
				break;
				
			case SET_PARTNER_GROSS_PROFIT: // 设置广告主毛利率
				break;
				
			case SET_CAMP_GROSS_PROFIT: // 设置活动毛利率
				break;
				
			case SET_MAX_NEGATIVE_GROSS_PROFIT: // 设置最大的负毛利
				// 引擎不管,统计系统管
				break;

			case HEARTBEAT:
				break;

			case CHANGE_AF:
				break;

			case BID_COUNT:
				break;

			default:
				break;
			}
		}
	}

	/**
	 * @param campaignDAO the campaignDAO to set
	 */
	public void setCampaignDAO(CampaignDAO campaignDAO) {
		this.campaignDAO = campaignDAO;
	}
}
