package com.jtd.collector.jms;

import com.alibaba.fastjson.JSON;
import com.jtd.collector.dao.CampaignDAO;
import com.jtd.collector.dao.CountDAO;
import com.jtd.collector.service.CollectService;
import com.jtd.web.jms.BidCountMsg;
import com.jtd.web.jms.ChangeCampGrpMsg;
import com.jtd.web.jms.ChangeManulStatusMsg;
import com.jtd.web.jms.UpdateCampMsg;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import static com.jtd.web.jms.MsgType.CHANGE_AUTO_STATUS;
import static com.jtd.web.jms.MsgType.UPDATE_CAMPAIGN;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月12日
 * @项目名称 dsp-cache-collector
 * @描述 <p></p>
 */
public class JMSMessageListener implements MessageListener {

	private static final Log log = LogFactory.getLog(JMSMessageListener.class);
	private final Logger logMyDebug = LogManager.getLogger("myDebugLog");
	private CampaignDAO campaignDAO;

	private CountDAO countDAO;
	
	private CollectService collectService;

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

			case UPDATE_CAMPAIGN: // 更新活动，admin发送过来的消息
				logMyDebug.info("更新广告活动-UPDATE_CAMPAIGN>>>>");
				collectService.updateCampaign(((UpdateCampMsg) message).getCampaign());
				break;
				
			case CHANGE_AUTO_STATUS: // 修改活动自动状态
				break;
				
			case CHANGE_MANUL_STATUS: // 修改活动手动状态，admin发送过来的消息
				ChangeManulStatusMsg cmsm = (ChangeManulStatusMsg)message;
				logMyDebug.info("修改手动状态-CHANGE_MANUL_STATUS>>>>"+cmsm.toString());
				collectService.changeManulStatus(cmsm.getCampid(), cmsm.getStatus());
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
				
			case CHANGE_CAMP_PRICE_FACTOR: // 修改价格因子
				break;
				
			case CHANGE_CAMP_CTR: // 修改活动最近周期的CTR
				break;

			case CHANGE_CAMP_GROUP: // 修改推广组设置，admin发送过来的消息
				ChangeCampGrpMsg ccgm = (ChangeCampGrpMsg) message;
				campaignDAO.updateCampgrp(
						ccgm.getGroupid(),
						new Long[] { 
								ccgm.getDailyBudgetGoal(),
								ccgm.getTotalBudgetGoal(),
								ccgm.getDailyPvGoal(), 
								ccgm.getTotalPvGoal(),
								ccgm.getDailyClickGoal(),
								ccgm.getTotalClickGoal() });
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
				//引擎发过来的竞价次数
				BidCountMsg bcm = (BidCountMsg) message;
				logMyDebug.info("引擎发过来的竞价次数-BID_COUNT>>>>"+bcm.toString());
				countDAO.increBid(bcm.getPartnerid(),bcm.getGroupid(), bcm.getCampid(), bcm.getCreativeid(), bcm.getBidNum());
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

	/**
	 * @param countDAO the countDAO to set
	 */
	public void setCountDAO(CountDAO countDAO) {
		this.countDAO = countDAO;
	}

	/**
	 * @param collectService the collectService to set
	 */
	public void setCollectService(CollectService collectService) {
		this.collectService = collectService;
	}
}
