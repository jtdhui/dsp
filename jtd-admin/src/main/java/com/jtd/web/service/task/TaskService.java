package com.jtd.web.service.task;

import java.util.Calendar;
import java.util.List;


import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jtd.commons.mybatis.support.CustomerContextHolder;
import com.jtd.web.constants.AuditStatus;
import com.jtd.web.constants.CampaignManulStatus;
import com.jtd.web.constants.CatgSerial;
import com.jtd.web.constants.Constants;
import com.jtd.web.dao.IAdAuditStatusDao;
import com.jtd.web.dao.ICampaignDao;
import com.jtd.web.dao.IPartnerDao;
import com.jtd.web.dao.IPartnerStatusDao;
import com.jtd.web.dao.IReportDemoDataSettingDao;
import com.jtd.web.po.AdAuditStatus;
import com.jtd.web.po.Campaign;
import com.jtd.web.po.Partner;
import com.jtd.web.po.PartnerStatusPO;
import com.jtd.web.po.ReportDemoDataSetting;
import com.jtd.web.service.AuditResult;
import com.jtd.web.service.IMQConnectorService;
import com.jtd.web.service.admin.IAdminPartnerService;
import com.jtd.web.service.admin.IReportDemoDataService;
import com.jtd.web.service.adx.IBaiduAPIService;
import com.jtd.web.service.adx.IHZengAPIService;
import com.jtd.web.service.adx.IVamAPIService;
import com.jtd.web.service.adx.IXTraderAPIService;
import com.jtd.web.service.front.IFrontCampService;

@Service
public class TaskService {

	private Logger log = LoggerFactory.getLogger(TaskService.class);
	private Logger creativeAuditLog = LoggerFactory.getLogger("creativeAuditLog");

	@Autowired
	private IPartnerDao partnerDao;

	@Autowired
	private IPartnerStatusDao partnerStatusDao;

	@Autowired
	private IAdminPartnerService partnerService;

	@Autowired
	private IAdAuditStatusDao adAuditStatusDao;

	@Autowired
	private IMQConnectorService mqConnectorService;

	@Autowired
	private IFrontCampService frontCampService;

	@Autowired
	private ICampaignDao campaignDao;

	@Autowired
	private IReportDemoDataService reportDemoDataService;
	
	@Autowired
	private IReportDemoDataSettingDao reportDemoDataSettingDao ;
	
	@Autowired
	private IBaiduAPIService baiduAPIService;
	
	@Autowired
	private IXTraderAPIService xTraderAPIService;
	
	@Autowired
	private IVamAPIService vamAPIService;
	
	@Autowired
	private IHZengAPIService hzengAPIService;
	
	/**
	 * 系统启动后10分钟一次
	 * 
	 * 检查partner账户真实余额，如账户真实余额小于等于0，则暂停该用户投放并通知引擎
	 * 
	 */
	public void taskOfCheckPartnerAccBalance() {

		CustomerContextHolder
				.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);

		List<Partner> partnerList = partnerDao.listAll();
		if (null != partnerList && !partnerList.isEmpty()) {
			for (Partner partner : partnerList) {

				log.info("taskOfCheckPartnerAccBalance----开始检查广告主[id="
						+ partner.getId() + "]的账户余额");

				boolean isPass = partnerService.checkPartnerAccBalance(partner);

				/**
				 * 如果账户余额不足，则停止广告主所有的投放活动 (【jtd公司】即使账户检查不通过，也不能把账户状态停止)
				 */
				if (!isPass && partner.getId() != Constants.CE_PARTNER_ID) {

					CustomerContextHolder
							.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);

					/**
					 * 停止此广告主所有的投放活动
					 */
					Campaign param = new Campaign();
					param.setPartnerId(partner.getId());

					List<Campaign> campList = campaignDao.listBy(param);

					if (campList != null) {

						String[] campIdArray = new String[campList.size()];
						String campIdStr = "";
						for (int i = 0; i < campList.size(); i++) {

							campIdArray[i] = campList.get(i).getId() + "";

							if (i == 0) {
								campIdStr = campIdArray[i];
							} else {
								campIdStr = "," + campIdArray[i];
							}
						}
						frontCampService.changeCampaignManulStatus(campIdArray,
								CampaignManulStatus.PAUSE);

						log.info("taskOfCheckPartnerAccBalance----广告主[id="
								+ partner.getId()
								+ "]账户余额检查不通过，将其所有活动状态设为暂停，活动id=[" + campIdStr
								+ "]");
					}
					/**
					 * 停止此广告主所有的投放活动 end
					 */
					
					/**
					 * 此广告主不再生成报表模拟数据
					 */
					reportDemoDataSettingDao.deleteByPartnerId(partner.getId());
					/**
					 * 此广告主不再生成报表模拟数据 end
					 */
				}
			}
		}
	}

	/**
	 * 检查所有审核中的广告主审核记录，向adx发送请求，更新审核状态
	 * 
	 */
	public void taskOfCheckAdvertiserAuditStatus() {

		CustomerContextHolder
				.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);

		List<PartnerStatusPO> pslist = partnerStatusDao.listAll();
		if (null != pslist && !pslist.isEmpty()) {

			for (PartnerStatusPO ps : pslist) {

				log.info("taskOfCheckAdvertiserAuditStatus----检测到待审核的广告主["
						+ ps.getPartnerId() + "],审核渠道为["
						+ ps.getChannelId() + "]");

				long partnerId = ps.getPartnerId();
				long channelId = ps.getChannelId();
				CatgSerial channelEnum = CatgSerial.fromChannelId(channelId);
				
				AuditResult auditResult = null ;

				//百度
				if (channelId == CatgSerial.BES.getChannelId()) {

					auditResult = baiduAPIService
							.queryAdvertiserAuditStatus(ps.getPartnerId());

				}
				//互众
//				if (channelId == CatgSerial.HZ.getChannelId()) {
//
//					auditResult = hzengAPIService
//							.queryAdvertiserAuditStatus(ps.getPartnerId());
//
//				}

				log.info("taskOfCheckAdvertiserAuditStatus----广告主["
						+ ps.getPartnerId() + "]在渠道["
						+ ps.getChannelId() + "]的审核状态查询结果为" + JSONObject.fromObject(auditResult));
				
				if (auditResult != null && auditResult.getAuditStatus() != null) {

					CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);

					AuditStatus auditStatusEnum = auditResult.getAuditStatus() ;

					String auditInfo = "" ;

					if (auditStatusEnum == AuditStatus.STATUS_SUCCESS) {
						auditInfo = "审核通过" ;
					}
					if (auditStatusEnum == AuditStatus.STATUS_FAIL) {

						auditInfo = auditResult.getRefuseReason();

						log.info("taskOfCheckCreativeAuditStatus----广告主["
								+ partnerId
								+ "]在渠道["
								+ channelId
								+ "]的拒绝原因为"
								+ auditInfo);

					}
					
					int auditMqSuccess = 0 ;
					
					try {
						// 向MQ发送广告主审核状态变更的消息
						mqConnectorService.sendAuditPartnerMessage(
								partnerId, channelEnum, auditStatusEnum);
						
						auditMqSuccess = 1 ;
						
					} catch (Exception e) {
						log.error("taskOfCheckAdvertiserAuditStatus----广告主["
								+ ps.getPartnerId()
								+ "]在渠道["
								+ ps.getChannelId() + "]审核状态为" + auditStatusEnum.getDesc() + "，发送MQ失败",e);
					}

					partnerStatusDao.updateStatus(
							auditStatusEnum.getCode(), auditInfo , null, partnerId, channelId , auditMqSuccess);

					log.info("taskOfCheckAdvertiserAuditStatus----广告主["
							+ partnerId
							+ "]在渠道["
							+ channelId + "]的数据库状态修改为" + auditStatusEnum.getDesc());
					
				}

			}
		}

	}

	public void taskOfCheckCreativeAuditStatus() {

		log.info("taskOfCheckCreativeAuditStatus----定时任务开始");
		
		CustomerContextHolder
				.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);

		List<AdAuditStatus> adslist = adAuditStatusDao.listAll();

		if (null != adslist && !adslist.isEmpty()) {

			for (AdAuditStatus ads : adslist) {

				log.info("taskOfCheckCreativeAuditStatus----检测到待审核的创意id["
						+ ads.getAdId() + "],审核渠道为["
						+ ads.getChannelId() + "]");
				
				long adId = ads.getAdId();

				AuditResult auditResult = null ;
				//百度
				if (ads.getChannelId() == CatgSerial.BES.getChannelId()) {

					auditResult = baiduAPIService
							.queryCreativeAuditStatus(adId);

				}
				//灵集
//				if (ads.getChannelId() == CatgSerial.XTRADER.getChannelId()) {
//
//					auditResult = xTraderAPIService
//							.queryCreativeAuditStatus(adId);
//
//				}
				//互众
				if (ads.getChannelId() == CatgSerial.HZ.getChannelId()) {

					auditResult = hzengAPIService
							.queryCreativeAuditStatus(adId);

				}
				//万流客
//				if (ads.getChannelId() == CatgSerial.VAM.getChannelId()) {
//
//					auditResult = vamAPIService
//							.queryCreativeAuditStatus(adId);
//
//				}
				//TODO:追加其他ADX
				
				if (auditResult != null && auditResult.getAuditStatus() != null) {
					
					CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
					
					AuditStatus auditStatusEnum = auditResult.getAuditStatus() ;

					log.info("taskOfCheckCreativeAuditStatus----创意adId=["
							+ ads.getAdId()
							+ "]在渠道["
							+ ads.getChannelId() + "]审核状态为[" + auditStatusEnum.getDesc() + "]");
					
					String auditInfo = "" ;

					if (auditStatusEnum == AuditStatus.STATUS_SUCCESS) {
						auditInfo = "审核通过" ;
					}
					if (auditStatusEnum == AuditStatus.STATUS_FAIL) {

						auditInfo = auditResult.getRefuseReason();

						log.info("taskOfCheckCreativeAuditStatus----创意adId=["
								+ ads.getAdId()
								+ "]在渠道["
								+ ads.getChannelId()
								+ "]的拒绝原因为"
								+ auditInfo);

					}

					int auditMqSuccess = 0 ;
					// 向MQ发送广告审核状态变更的消息
					try {
						mqConnectorService.sendAuditAdMessage(adId,
								CatgSerial.fromChannelId(ads.getChannelId()),
								auditStatusEnum);
						
						auditMqSuccess = 1 ;
					} catch (Exception e) {
						log.error("taskOfCheckCreativeAuditStatus----创意adId=["
							+ ads.getAdId()
							+ "]在渠道["
							+ ads.getChannelId() + "]审核状态为[" + auditStatusEnum.getDesc() + "],发送MQ失败",e);
					}
					
					try {
						adAuditStatusDao.updateStatus(
								auditStatusEnum.getCode(),
								auditInfo, adId,
								ads.getChannelId(),auditMqSuccess,null);
					} catch (Exception e) {

						log.error("taskOfCheckCreativeAuditStatus----创意adId=["
								+ ads.getAdId()
								+ "]在渠道["
								+ ads.getChannelId() + "]的数据库状态修改为[" + auditStatusEnum.getDesc() + "]失败" , e);
					}
					
				}

			}
			
			
		}
		
		log.info("taskOfCheckCreativeAuditStatus----定时任务结束");

	}

	public void taskOfBesSettingRefresh() {

		baiduAPIService.syncSSPSetting();
	}

	/**
	 * 设置每个小时整点执行，生成当前小时的报表数据
	 */
	public void taskOfGenerateReportDemoData() {
		
		int hour = Calendar.getInstance().get(Calendar.HOUR);
		
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		
		List<ReportDemoDataSetting> settingList = reportDemoDataService
				.listAll();
		if (settingList != null) {
			for (ReportDemoDataSetting rdds : settingList) {
				
				long partnerId = rdds.getPartnerId();

				CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
				
				Partner partner = partnerDao.getById(partnerId);
				if (partner == null) {
					continue;
				}
				
				Campaign param = new Campaign();
				param.setPartnerId(partnerId);
				List<Campaign> campList = campaignDao.listBy(param);
				if (campList != null) {

					for (Campaign camp : campList) {
						
						reportDemoDataService.saveCampDataPerHour(rdds, camp, partner , hour);
						
					}
				}
			}
		}

	}

}
