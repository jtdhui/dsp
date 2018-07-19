package com.jtd.web.service.admin.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jtd.commons.dao.BaseDao;
import com.jtd.commons.mybatis.support.CustomerContextHolder;
import com.jtd.web.constants.AuditStatus;
import com.jtd.web.constants.CatgSerial;
import com.jtd.web.constants.Constants;
import com.jtd.web.dao.IAdDao;
import com.jtd.web.jms.AuditAdMsg;
import com.jtd.web.jms.CommitAdMsg;
import com.jtd.web.po.Ad;
import com.jtd.web.po.AdAuditStatus;
import com.jtd.web.po.AdCategory;
import com.jtd.web.service.IMQConnectorService;
import com.jtd.web.service.admin.IAdminAdAuditStatusService;
import com.jtd.web.service.admin.IAdminAdCategoryService;
import com.jtd.web.service.admin.IAdminAdService;
import com.jtd.web.service.adx.IBaiduAPIService;
import com.jtd.web.service.adx.IHZengAPIService;
import com.jtd.web.service.adx.IVamAPIService;
import com.jtd.web.service.adx.IXTraderAPIService;
import com.jtd.web.service.impl.BaseService;

/**
 * 
 * @作者 duber
 * @版本 V1.0
 * @创建日期 2016年10月28日
 * @描述 后台创意管理Service
 */
@Service
public class AdminAdService extends BaseService<Ad> implements IAdminAdService {

	@Autowired
	private IAdDao  adDao;

	@Autowired
	private IAdminAdCategoryService adminAdCategoryService;

	@Autowired
	private IAdminAdAuditStatusService adminAdAuditStatusService;

	@Autowired
	private IMQConnectorService mQConnectorService;

	@Autowired
	private IBaiduAPIService baiduAPIService;

    @Autowired
    private IXTraderAPIService ixTraderAPIService;

    @Autowired
    private IVamAPIService vamAPIService;
    
    @Autowired
    private IHZengAPIService hzengAPIService;

	@Override
	protected BaseDao<Ad> getDao() {
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		return adDao;
	}

	@Override
	public Map<String, Object> findAdMapById(Map<String, Object> paraMap) {
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		return adDao.findAdMapById(paraMap);
	}

	@Override
	public void saveAuditAdx(long campaign_id, String channelIds, Long adId, int internalAudit) {

		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		AdCategory ac = null;
		
		//内部审核发送MQ是否成功
		int internalAuditMqSuccess = -1 ;

        // 内部审核发送请求到MQ.
        AuditAdMsg auditAdMsg = new AuditAdMsg();
        auditAdMsg.setAdid(adId);
		if(internalAudit == 1 ){
			auditAdMsg.setAuditpassed(true);
		}else {
            auditAdMsg.setAuditpassed(false);
        }
        internalAuditMqSuccess = mQConnectorService.sendMessage(auditAdMsg);

		if (channelIds != null && channelIds.length() > 0) {
			
			String[] channels = channelIds.split(",");
			Date now = new Date();
			for (int i = 0; i < channels.length; i++) {
				
				//可修改创意行业分类
				ac = new AdCategory();
				String ccStr = channels[i].toString();
				String[] ccArray = ccStr.split("\\$\\$\\$");
				ac.setCampaignId(campaign_id);
				ac.setCatgId(ccArray[1]);
				ac.setCatgserial(ccArray[2]);
				ac.setAdId(adId);
				ac.setCreateTime(now);
				adminAdCategoryService.updateAdCategoryByAdId(ac);

				// 只有审核通过时才同步到渠道,并且把创意信息推送到Mq
				if ( internalAudit == 1 ) {
					
					AdAuditStatus aas = new AdAuditStatus();
					aas.setAdId(adId);
					aas.setChannelId(Long.parseLong(ccArray[0]));
					aas = adminAdAuditStatusService.findBy(aas);

					// 未提交或者渠道审核未成功都可以提交
					if(aas == null || aas.getStatus()!= AuditStatus.STATUS_SUCCESS ){
                        String ch_id = ccArray[0];
                        syncAdxAndMq(ch_id,adId,1,internalAuditMqSuccess);
					}
				}
			}
		}
	}

    @Override
    public void batchAdAudit(String ad_ids) {

		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);

        String arr_id[] = ad_ids.split(",");
		AdCategory ac = null;
		Ad ad = null;
		for(String id: arr_id){
			
			long ad_id = Long.parseLong(id);
			
			// 提交广告内部审核信息到引擎
			AuditAdMsg auditAdMsg = new AuditAdMsg();
			auditAdMsg.setAdid(ad_id);
			auditAdMsg.setAuditpassed(true);
			int internalAuditMqSuccess = mQConnectorService.sendMessage(auditAdMsg);
			
			ac = new AdCategory();
			ac.setAdId(ad_id);
			List<AdCategory> list = adminAdCategoryService.listBy(ac);

			if(list.size()>0){
				for(AdCategory adCategory : list){
                    String ch_id = CatgSerial.fromCode(adCategory.getCatgserial()).getChannelId().toString();
                    //发送创意到Adx,并发送到引擎
                    syncAdxAndMq(ch_id,ad_id,1,internalAuditMqSuccess);
				}
			} // end if

			//更新广告审核状态
			ad = new Ad();
			ad.setId(ad_id);
			ad.setInternalAudit(1);
			adDao.update(ad);
			
		} // end 循环广告ID
    }

    /**
     * 发送创意到Adx,并发送到引擎
     * @param ch_id
     * @param adId
     * @param internalAudit
     * @param internalAuditMqSuccess
     */
    private void syncAdxAndMq(String ch_id, long adId, int internalAudit,int internalAuditMqSuccess) {

        int ret = -1;
        switch (ch_id){
            case "1": //淘宝
                break;
            case "2": //百度
                if(!Constants.IS_IGNORE_ADX){
                    ret = baiduAPIService.syncCreative(adId);
                }
                break;
            case "6": //VAM
                if(!Constants.IS_IGNORE_ADX){
                    ret = vamAPIService.syncCreative(adId) ? 0 : -1 ;
                }
                break;
            case "7": //腾讯
                break;
//            case "8": //灵集
//                if(!Constants.IS_IGNORE_ADX){
//                    ret = ixTraderAPIService.syncCreative(adId);
//                }
//                break;
            case "9": //互众
                if(!Constants.IS_IGNORE_ADX){
                    ret = hzengAPIService.syncCreative(adId) ? 0 : -1 ;
                }
                break;
        }

        //如果ret已经等于0表示API提交成功，或者配置了忽略ADX（ret还等于-1），都发送MQ
        if(ret == 0 || Constants.IS_IGNORE_ADX){
            // 发送MQ
            CommitAdMsg commitAdMsg = new CommitAdMsg();
            commitAdMsg.setAdid(adId);
            commitAdMsg.setChannelId(Long.parseLong(ch_id));
            int auditMqSuccess = mQConnectorService.sendMessage(commitAdMsg);

            // 审核状态
            AdAuditStatus adAuditStatus = new AdAuditStatus();
            adAuditStatus.setAdId(adId);
            adAuditStatus.setChannelId(Long.parseLong(ch_id));
            adAuditStatus = adminAdAuditStatusService.findBy(adAuditStatus);
            if(adAuditStatus == null){
                adAuditStatus = new AdAuditStatus();
                adAuditStatus.setAdId(adId);
                adAuditStatus.setChannelId(Long.parseLong(ch_id));
                adAuditStatus.setStatus(AuditStatus.getAuditStatus(internalAudit));
            }else{
                if(adAuditStatus.getStatus() != AuditStatus.STATUS_SUCCESS){ //如何不是通过,则待审核状态
                    adAuditStatus.setStatus(AuditStatus.getAuditStatus(internalAudit));
                    adAuditStatus.setAuditInfo("");
                }
            }
            adAuditStatus.setAuditTime(new Date());
            adAuditStatus.setAuditMqSuccess(auditMqSuccess);  //ADX审核发送MQ是否成功
            adAuditStatus.setInternalAuditMqSuccess(internalAuditMqSuccess); //内部审核发送MQ是否成功
            adminAdAuditStatusService.updateAdAuditStatus(adAuditStatus);
        }
    }

}
