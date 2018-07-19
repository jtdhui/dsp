package com.jtd.engine.ad.matcher;

import com.jtd.engine.ad.Session;
import com.jtd.engine.ad.em.Adx;
import com.jtd.engine.dao.AuditInfoDAO;
import com.jtd.engine.dao.CampaignDAO;
import com.jtd.engine.dao.HeartBeatDAO;
import com.jtd.web.constants.BudgetCtrlType;
import com.jtd.web.constants.CampaignAutoStatus;
import com.jtd.web.constants.CampaignManulStatus;
import com.jtd.web.model.Campaign;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月8日
 * @项目名称 dsp-engine
 * @描述 <p></p>
 */
public class StatusMatcher extends AbstractChannelCampMatcher {
	
//	private static final Log log = LogFactory.getLog(StatusMatcher.class);
	private final Logger xtarderDebugLog = LogManager.getLogger("xtarderDebugLog");
	private final Logger besDebugLog = LogManager.getLogger("besDebugLog");
	private final Logger hzDebugLog = LogManager.getLogger("hzDebugLog");
	
	private final Logger adwoDebugLog = LogManager.getLogger("adwoDebugLog");
	
	private AuditInfoDAO auditInfoDAO;
	private HeartBeatDAO heartBeatDAO;
	private CampaignDAO campaignDAO;

	

	@Override
	protected boolean matchADWO(Session session, Campaign camp) {
		adwoDebugLog.info("35、heartBeatDAO.isAlive()>>adwo:StatusMatch>>"+heartBeatDAO.isAlive()+">>活动id>>"+camp.getId());
		adwoDebugLog.info("36、camp.getAutoStatus() == CampaignAutoStatus.ONLINE>>adwo:StatusMatch>>"+(camp.getAutoStatus() == CampaignAutoStatus.ONLINE)+">>活动id>>"+camp.getId());
		adwoDebugLog.info("36~37、camp.getAutoStatus()自动状态>>adwo:StatusMatch>>"+camp.getAutoStatus()+">>活动id>>"+camp.getId());
		adwoDebugLog.info("36~37、camp.getAutoStatus()手动状态>>adwo:StatusMatch>>"+camp.getManulStatus()+">>活动id>>"+camp.getId());
		adwoDebugLog.info("37、camp.getManulStatus() == CampaignManulStatus.ONLINE>>adwo:StatusMatch>>"+(camp.getManulStatus() == CampaignManulStatus.ONLINE)+">>活动id>>"+camp.getId());
		adwoDebugLog.info("38、!(camp.getBudgetCtrlType() == BudgetCtrlType.STANDARD && campaignDAO.isPause(camp.getId()))>>adwo:StatusMatch>>"+(!(camp.getBudgetCtrlType() == BudgetCtrlType.STANDARD && campaignDAO.isPause(camp.getId())))+">>活动id>>"+camp.getId());
		adwoDebugLog.info("39、auditInfoDAO.isPartnerStarted(camp.getPartnerId())>>adwo:StatusMatch>>"+(auditInfoDAO.isPartnerStarted(camp.getPartnerId()))+">>活动id>>"+camp.getId());
		adwoDebugLog.info("40、auditInfoDAO.iscommittedPartner(Adx.adwo, camp.getPartnerId())>>adwo:StatusMatch>>"+(auditInfoDAO.iscommittedPartner(Adx.ADWO, camp.getPartnerId()))+">>活动id>>"+camp.getId());
		adwoDebugLog.info("41、!auditInfoDAO.isRefusedPartner(camp.getPartnerId())>>adwo:StatusMatch>>"+(!auditInfoDAO.isRefusedPartner(camp.getPartnerId()))+">>"+camp.getId());
		adwoDebugLog.info("42、!auditInfoDAO.isRefusedPartner(Adx.adwo, camp.getPartnerId())>>adwo:StatusMatch>>"+(!auditInfoDAO.isRefusedPartner(Adx.ADWO, camp.getPartnerId()))+">>活动id>>"+camp.getId());
		
		
		boolean ret=heartBeatDAO.isAlive() /** 定时接收统计系统的心跳，统计系统一旦有节点死掉即停止竞价 */
				&& camp.getAutoStatus() == CampaignAutoStatus.ONLINE   /** 广告活动自动状态为投放中 */
				&& camp.getManulStatus() == CampaignManulStatus.ONLINE /** 广告活动手动状态为投放中 */
				&& !(camp.getBudgetCtrlType() == BudgetCtrlType.STANDARD && campaignDAO.isPause(camp.getId()))/** BudgetCtrlType:预算控制方式 STANDARD(0, "标准"), FAST(1, "尽速"); */
				&& auditInfoDAO.isPartnerStarted(camp.getPartnerId()) /** 判断广告主是否已经开启 */
				//&& auditInfoDAO.iscommittedPartner(Adx.ADWO, camp.getPartnerId()) /** 验证广告主是否提交adx审核 */
				&& !auditInfoDAO.isRefusedPartner(camp.getPartnerId())  /** 验证广告主是否审核未通过 */
				&& !auditInfoDAO.isRefusedPartner(Adx.ADWO, camp.getPartnerId()); /** 验证广告主是否adx审核未通过 */
		
		
		return ret;
	}


	@Override
	protected boolean matchXTRADER(Session session, Campaign camp) {
//		xtarderDebugLog.info("35、heartBeatDAO.isAlive()>>xtrader:StatusMatch>>"+heartBeatDAO.isAlive()+">>活动id>>"+camp.getId());
//		xtarderDebugLog.info("36、camp.getAutoStatus() == CampaignAutoStatus.ONLINE>>xtrader:StatusMatch>>"+(camp.getAutoStatus() == CampaignAutoStatus.ONLINE)+">>活动id>>"+camp.getId());
//		xtarderDebugLog.info("36~37、camp.getAutoStatus()自动状态>>xtrader:StatusMatch>>"+camp.getAutoStatus()+">>活动id>>"+camp.getId());
//		xtarderDebugLog.info("36~37、camp.getAutoStatus()手动状态>>xtrader:StatusMatch>>"+camp.getManulStatus()+">>活动id>>"+camp.getId());
//		xtarderDebugLog.info("37、camp.getManulStatus() == CampaignManulStatus.ONLINE>>xtrader:StatusMatch>>"+(camp.getManulStatus() == CampaignManulStatus.ONLINE)+">>活动id>>"+camp.getId());
//		xtarderDebugLog.info("38、!(camp.getBudgetCtrlType() == BudgetCtrlType.STANDARD && campaignDAO.isPause(camp.getId()))>>xtrader:StatusMatch>>"+(!(camp.getBudgetCtrlType() == BudgetCtrlType.STANDARD && campaignDAO.isPause(camp.getId())))+">>活动id>>"+camp.getId());
//		xtarderDebugLog.info("39、auditInfoDAO.isPartnerStarted(camp.getPartnerId())>>xtrader:StatusMatch>>"+(auditInfoDAO.isPartnerStarted(camp.getPartnerId()))+">>活动id>>"+camp.getId());
//		xtarderDebugLog.info("40、auditInfoDAO.iscommittedPartner(Adx.XTRADER, camp.getPartnerId())>>xtrader:StatusMatch>>"+(auditInfoDAO.iscommittedPartner(Adx.XTRADER, camp.getPartnerId()))+">>活动id>>"+camp.getId());
//		xtarderDebugLog.info("41、!auditInfoDAO.isRefusedPartner(camp.getPartnerId())>>xtrader:StatusMatch>>"+(!auditInfoDAO.isRefusedPartner(camp.getPartnerId()))+">>"+camp.getId());
//		xtarderDebugLog.info("42、!auditInfoDAO.isRefusedPartner(Adx.XTRADER, camp.getPartnerId())>>xtrader:StatusMatch>>"+(!auditInfoDAO.isRefusedPartner(Adx.XTRADER, camp.getPartnerId()))+">>活动id>>"+camp.getId());
		boolean ret=heartBeatDAO.isAlive()
				&& camp.getAutoStatus() == CampaignAutoStatus.ONLINE
				&& camp.getManulStatus() == CampaignManulStatus.ONLINE
				&& !(camp.getBudgetCtrlType() == BudgetCtrlType.STANDARD && campaignDAO.isPause(camp.getId()))
				&& auditInfoDAO.isPartnerStarted(camp.getPartnerId())
				&& auditInfoDAO.iscommittedPartner(Adx.XTRADER, camp.getPartnerId())
				&& !auditInfoDAO.isRefusedPartner(camp.getPartnerId())
				&& !auditInfoDAO.isRefusedPartner(Adx.XTRADER, camp.getPartnerId());
		
//		xtarderDebugLog.info("零集状态的匹配结果:+"+ret+">>活动id>>"+camp.getId());
		
		return ret;
		/*
		return heartBeatDAO.isAlive()
				&& camp.getAutoStatus() == CampaignAutoStatus.ONLINE
				&& camp.getManulStatus() == CampaignManulStatus.ONLINE
				&& !(camp.getBudgetCtrlType() == BudgetCtrlType.STANDARD && campaignDAO.isPause(camp.getId()))
				&& auditInfoDAO.isPartnerStarted(camp.getPartnerId())
				&& auditInfoDAO.iscommittedPartner(Adx.XTRADER, camp.getPartnerId())
				&& !auditInfoDAO.isRefusedPartner(camp.getPartnerId())
				&& !auditInfoDAO.isRefusedPartner(Adx.XTRADER, camp.getPartnerId());
				*/
	}
	

	@Override
	protected boolean matchXHDT(Session session, Campaign camp) 
	{
		return heartBeatDAO.isAlive()
		&& camp.getAutoStatus() == CampaignAutoStatus.ONLINE
		&& camp.getManulStatus() == CampaignManulStatus.ONLINE
		&& !(camp.getBudgetCtrlType() == BudgetCtrlType.STANDARD && campaignDAO.isPause(camp.getId()))
		&& auditInfoDAO.isPartnerStarted(camp.getPartnerId())
		&& !auditInfoDAO.isRefusedPartner(camp.getPartnerId());
		//return true;
	}

	@Override
	protected boolean matchAdView(Session session, Campaign camp) {
		return true;
	}

	@Override
	protected boolean matchAdBES(Session session, Campaign camp) {
		besDebugLog.info("35、heartBeatDAO.isAlive()>>bes:StatusMatch>>"+heartBeatDAO.isAlive()+">>活动id>>"+camp.getId());
		besDebugLog.info("36、camp.getAutoStatus() == CampaignAutoStatus.ONLINE>>bes:StatusMatch>>"+(camp.getAutoStatus() == CampaignAutoStatus.ONLINE)+">>活动id>>"+camp.getId());
		besDebugLog.info("36~37、camp.getAutoStatus()自动状态>>bes:StatusMatch>>"+camp.getAutoStatus()+">>活动id>>"+camp.getId());
		besDebugLog.info("36~37、camp.getAutoStatus()手动状态>>bes:StatusMatch>>"+camp.getManulStatus()+">>活动id>>"+camp.getId());
		besDebugLog.info("37、camp.getManulStatus() == CampaignManulStatus.ONLINE>>bes:StatusMatch>>"+(camp.getManulStatus() == CampaignManulStatus.ONLINE)+">>活动id>>"+camp.getId());
		besDebugLog.info("38、!(camp.getBudgetCtrlType() == BudgetCtrlType.STANDARD && campaignDAO.isPause(camp.getId()))>>xtrader:StatusMatch>>"+(!(camp.getBudgetCtrlType() == BudgetCtrlType.STANDARD && campaignDAO.isPause(camp.getId())))+">>活动id>>"+camp.getId());
		besDebugLog.info("39、auditInfoDAO.isPartnerStarted(camp.getPartnerId())>>bes:StatusMatch>>"+(auditInfoDAO.isPartnerStarted(camp.getPartnerId()))+">>活动id>>"+camp.getId());
		besDebugLog.info("40、auditInfoDAO.iscommittedPartner(Adx.BES,camp.getPartnerId())>>bes:StatusMatch>>"+(auditInfoDAO.iscommittedPartner(Adx.BES, camp.getPartnerId()))+">>活动id>>"+camp.getId());
		besDebugLog.info("41、!auditInfoDAO.isRefusedPartner(camp.getPartnerId())>>bes:StatusMatch>>"+(!auditInfoDAO.isRefusedPartner(camp.getPartnerId()))+">>"+camp.getId());
		besDebugLog.info("42、!auditInfoDAO.isRefusedPartner(Adx.BES, camp.getPartnerId())>>bes:StatusMatch>>"+(!auditInfoDAO.isRefusedPartner(Adx.BES, camp.getPartnerId()))+">>活动id>>"+camp.getId());
		boolean ret=heartBeatDAO.isAlive()
				&& camp.getAutoStatus() == CampaignAutoStatus.ONLINE
				&& camp.getManulStatus() == CampaignManulStatus.ONLINE
				&& !(camp.getBudgetCtrlType() == BudgetCtrlType.STANDARD && campaignDAO.isPause(camp.getId()))
				&& auditInfoDAO.isPartnerStarted(camp.getPartnerId())
				//&& auditInfoDAO.iscommittedPartner(Adx.BES, camp.getPartnerId())
				&& !auditInfoDAO.isRefusedPartner(camp.getPartnerId())
				&& !auditInfoDAO.isRefusedPartner(Adx.BES, camp.getPartnerId());

		besDebugLog.info("百度状态的匹配结果:+"+ret+">>活动id>>"+camp.getId());

		return ret;
	}


	/**
	 * @param auditInfoDAO the auditInfoDAO to set
	 */
	public void setAuditInfoDAO(AuditInfoDAO auditInfoDAO) {
		this.auditInfoDAO = auditInfoDAO;
	}

	/**
	 * @param heartBeatDAO the heartBeatDAO to set
	 */
	public void setHeartBeatDAO(HeartBeatDAO heartBeatDAO) {
		this.heartBeatDAO = heartBeatDAO;
	}
	/**
	 * @param campaignDAO
	 */
	public void setCampaignDAO(CampaignDAO campaignDAO) {
		this.campaignDAO = campaignDAO;
	}
	
	
}
