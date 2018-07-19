package com.jtd.engine.adserver.jms;

import com.alibaba.fastjson.JSON;
import com.jtd.engine.ad.AdService;
import com.jtd.engine.ad.em.Adx;
import com.jtd.engine.dao.AuditInfoDAO;
import com.jtd.engine.dao.BlackListDAO;
import com.jtd.engine.dao.CampaignDAO;
import com.jtd.engine.dao.HeartBeatDAO;
import com.jtd.web.constants.PartnerStatus;
import com.jtd.web.jms.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;


/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月8日
 * @项目名称 dsp-engine
 * @描述 <p></p>
 */
public class JMSMessageListener implements MessageListener {

	private final Logger logMyDebug = LogManager.getLogger("myDebugLog"); 
	private static final Log log = LogFactory.getLog(JMSMessageListener.class);

	private AdService adService;
	private AuditInfoDAO auditInfoDAO;
	private BlackListDAO blackListDAO;
	private CampaignDAO campaignDAO;
	private HeartBeatDAO heartBeatDAO;

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
				//e.printStackTrace();
				log.error("读取message发生异常", e);
			}
			if(message == null) return;

			switch (message.getType()) {

			case UPDATE_CAMPAIGN: // 更新活动
				logMyDebug.info("广告更新："+JSON.toJSONString(message));
				adService.updateCampaign(((UpdateCampMsg) message).getCampaign());
				break;

			case CHANGE_AUTO_STATUS: // 修改活动自动状态
				logMyDebug.info("修改自动状态："+JSON.toJSONString(message));
				ChangeAutoStatusMsg casm = (ChangeAutoStatusMsg) message;
				adService.changeAutoStatus(casm.getCampid(), casm.getStatus());
				break;

			case CHANGE_MANUL_STATUS: // 修改活动手动状态
				logMyDebug.info("修改手动状态："+JSON.toJSONString(message));
				ChangeManulStatusMsg cmsm = (ChangeManulStatusMsg) message;
				adService.changeManulStatus(cmsm.getCampid(), cmsm.getStatus());
				break;

			case CHANGE_PARTNER_STATUS: // 修改广告主状态，对应后台的开启或者是暂停
				ChangePartnerStatusMsg cpsm = (ChangePartnerStatusMsg) message;
				long partnerid = cpsm.getPartnerid();
				if (cpsm.getStatus() == PartnerStatus.START) {
					logMyDebug.info("修改广告主状态=start："+JSON.toJSONString(message));
					auditInfoDAO.startPartner(partnerid);
				} else if (cpsm.getStatus() == PartnerStatus.STOP) {
					logMyDebug.info("修改广告主状态=stop："+JSON.toJSONString(message));
					auditInfoDAO.stopPartner(partnerid);
				}
				break;
				
			case COMMIT_PARTNER: // 提交广告主
				logMyDebug.info("提交广告主审核："+JSON.toJSONString(message));
				CommitPartnerMsg cpm = (CommitPartnerMsg) message;
				auditInfoDAO.commitPartner(Adx.fromChannelId(cpm.getChannelId().intValue()), cpm.getPartnerid());
				break;
				
			case COMMIT_AD: // 提交广告
				logMyDebug.info("提交创意审核："+JSON.toJSONString(message));
				CommitAdMsg cadm = (CommitAdMsg) message;
				auditInfoDAO.commitAd(Adx.fromChannelId(cadm.getChannelId().intValue()), cadm.getAdid());
				break;

			case AUDIT_PARTNER: // 审核伙伴(广告主)
				AuditPartnerMsg apm = (AuditPartnerMsg) message;
				Long channelId = apm.getChannelId();
				if (channelId == null) {
					logMyDebug.info("广告主dsp审核："+apm.isAuditpassed()+"-"+JSON.toJSONString(message));
					// DSP 审核
					if (apm.isAuditpassed()) {
						auditInfoDAO.passPartner(apm.getPartnerid());
					} else {
						auditInfoDAO.refusePartner(apm.getPartnerid());
					}
				} else {
					logMyDebug.info("广告主渠道审核："+JSON.toJSONString(message));
					// 渠道审核
					Adx adx = Adx.fromChannelId(channelId.intValue());
					if (adx == null) {
//						logMyDebug.error("引擎不能识别的渠道ID:" + channelId);
					} else {
						if (apm.isAuditpassed()) {
							auditInfoDAO.passPartner(adx, apm.getPartnerid());
						} else {
							auditInfoDAO.refusePartner(adx, apm.getPartnerid());
						}
					}
				}
				break;

			case AUDIT_AD: // 审核广告
				AuditAdMsg aam = (AuditAdMsg) message;
				Long channelid = aam.getChannelId();
				log.info("创意审核:channelid="+channelid);
				log.info("创意审核:aam="+JSON.toJSONString(message));
				//log.info("------------------------------------------------------------------");
				if (channelid == null) {
//					logMyDebug.info("dsp创意审核："+JSON.toJSONString(message));
					// DSP 审核
					if (aam.isAuditpassed()) {
						auditInfoDAO.passAd(aam.getAdid());
					} else {
						auditInfoDAO.refuseAd(aam.getAdid());
					}
				} else {
//					logMyDebug.info("创意渠道审核："+JSON.toJSONString(message));
					// 渠道审核
					Adx adx = Adx.fromChannelId(channelid.intValue());
					if (adx == null) {
						log.error("引擎不能识别的渠道ID:" + channelid);
					} else {
						if (aam.isAuditpassed()) {
							auditInfoDAO.passAd(adx, aam.getAdid());
						} else {
							auditInfoDAO.refuseAd(adx, aam.getAdid());
						}
					}
				}
				break;

			case SET_BLACKLIST: // 设置平台黑名单列表
				SetBlackListMsg sbm = (SetBlackListMsg) message;
				blackListDAO.setBlackList(sbm.getBlackList());
				break;

			case SET_CAMP_BLACKLIST: // 设置活动黑名单列表
//				logMyDebug.info("活动黑名单："+JSON.toJSONString(message));
				SetCampBlackListMsg scbm = (SetCampBlackListMsg) message;
				blackListDAO.setBlackList(scbm.getBlackList(), scbm.getCampid());
				break;
				
			case SET_CAMP_HOUR_PAUSE: // 设置活动小时投放控制
//				logMyDebug.info("广告小时投放控制："+JSON.toJSONString(message));
				SetCampHourPauseMsg schpm = (SetCampHourPauseMsg) message;
				campaignDAO.setCampHourPause(schpm.getCampid(), schpm.getHour());
				break;

			case CHANGE_CAMP_CATG: // 修改活动的行业类别
//				logMyDebug.info("修改活动的行业类别："+JSON.toJSONString(message));
				ChangeCampCatgMsg cccm = (ChangeCampCatgMsg) message;
				adService.changeCampCatg(cccm.getCampid(), cccm.getCatgserial(), cccm.getCatgid());
				break;

			case CHANGE_AD_CATG: // 修改广告的行业类别
//				logMyDebug.info("修改广告的行业类别："+JSON.toJSONString(message));
				ChangeAdCatgMsg cacm = (ChangeAdCatgMsg) message;
				adService.changeAdCatg(cacm.getAdid(), cacm.getCatgserial(), cacm.getCatgid());
				break;

			case CHANGE_CAMP_PRICE_FACTOR: // 修改活动的价格因子
//				logMyDebug.info("修改价格因子："+JSON.toJSONString(message));
				ChangeCampPriceFactorMsg ccpfm = (ChangeCampPriceFactorMsg) message;
				campaignDAO.changeCampPriceFactor(ccpfm.getCampid(), ccpfm.getPricefactor());
				break;

			case CHANGE_CAMP_CTR: // 修改活动最近周期的CTR
//				logMyDebug.info("修改点击率："+JSON.toJSONString(message));
				ChangeCampCtrMsg cccms = (ChangeCampCtrMsg) message;
				campaignDAO.changeCampCtr(cccms.getCampid(), cccms.getCtr());
				break;

			case CHANGE_CAMP_GROUP: // 修改推广组设置
				break;

			case SET_PARTNER_GROSS_PROFIT: // 设置广告主毛利率
//				logMyDebug.info("修改广告主毛利率："+JSON.toJSONString(message));
				SetPartnerGrossProfitMsg spgpm = (SetPartnerGrossProfitMsg) message;
				campaignDAO.setPartnerGrossProfit(spgpm.getPartnerid(), spgpm.getGrossProfit());
				break;

			case SET_CAMP_GROSS_PROFIT: // 设置活动毛利率
//				logMyDebug.info("修改活动毛利率："+JSON.toJSONString(message));
				SetCampGrossProfitMsg scgpm = (SetCampGrossProfitMsg) message;
				campaignDAO.setCampGrossProfit(scgpm.getCampid(), scgpm.getGrossProfit());
				break;

			case SET_MAX_NEGATIVE_GROSS_PROFIT: // 设置最大的负毛利
				// 引擎不管,统计系统管
				break;

			case HEARTBEAT: // 统计系统心跳
				HeartBeatMsg hbm = (HeartBeatMsg) message;
//				logMyDebug.info("统计系统心跳："+JSON.toJSONString(message));
				heartBeatDAO.heartBeat(hbm.getNodeName());
				break;

			case CHANGE_AF: // 修改AF
				
				ChangeAfMsg cam = (ChangeAfMsg) message;
//				logMyDebug.info("修改AF值："+JSON.toJSONString(cam));
				campaignDAO.setAf(cam.getCampid(), cam.getAf());
				break;

			case BID_COUNT: // 竞价量
				break;

			default:
				break;
			}
		}
	}

	/**
	 * @param adService the adService to set
	 */
	public void setAdService(AdService adService) {
		this.adService = adService;
	}

	/**
	 * @param auditInfoDAO the auditInfoDAO to set
	 */
	public void setAuditInfoDAO(AuditInfoDAO auditInfoDAO) {
		this.auditInfoDAO = auditInfoDAO;
	}

	/**
	 * @param blackListDAO the blackListDAO to set
	 */
	public void setBlackListDAO(BlackListDAO blackListDAO) {
		this.blackListDAO = blackListDAO;
	}

	/**
	 * @param campaignDAO the campaignDAO to set
	 */
	public void setCampaignDAO(CampaignDAO campaignDAO) {
		this.campaignDAO = campaignDAO;
	}

	/**
	 * @param heartBeatDAO the heartBeatDAO to set
	 */
	public void setHeartBeatDAO(HeartBeatDAO heartBeatDAO) {
		this.heartBeatDAO = heartBeatDAO;
	}
}
