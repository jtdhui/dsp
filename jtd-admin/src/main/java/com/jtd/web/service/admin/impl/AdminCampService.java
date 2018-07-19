package com.jtd.web.service.admin.impl;

import com.jtd.commons.dao.BaseDao;
import com.jtd.commons.mybatis.support.CustomerContextHolder;
import com.jtd.commons.page.Pagination;
import com.jtd.web.constants.CampaignManulStatus;
import com.jtd.web.constants.CampaignStatus;
import com.jtd.web.dao.ICampaignCategoryDao;
import com.jtd.web.dao.ICampaignDao;
import com.jtd.web.jms.ChangeManulStatusMsg;
import com.jtd.web.po.Campaign;
import com.jtd.web.po.CampaignCategory;
import com.jtd.web.service.IMQConnectorService;
import com.jtd.web.service.admin.IAdminCampService;
import com.jtd.web.service.admin.IAdminPartnerService;
import com.jtd.web.service.impl.BaseService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @作者 duber
 * @版本 V1.0
 * @创建日期 2016年10月19日
 * @描述 后台广告活动Service实现类
 */
@Service
public class AdminCampService  extends BaseService<Campaign> implements IAdminCampService {

	private Logger logger = Logger.getLogger("errorLog");
	
	@Autowired
	private ICampaignDao  campaignDao;

	@Autowired
	private IAdminPartnerService partnerService;

	@Autowired
	private ICampaignCategoryDao campaignCategoryDao;

	@Autowired
	private IMQConnectorService mQConnectorService;

	@Override
	protected BaseDao<Campaign> getDao() {
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		return campaignDao;
	}

	/**
	 * 后台活动列表
	 * @param paraMap
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@Override
	public Pagination<Map<String, Object>> listCampBy(Map<String, Object> paraMap, Integer pageNo, Integer pageSize) {
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		Pagination<Map<String, Object>> page = new Pagination<Map<String, Object>>();
		page = this.findMapPageBy(paraMap,pageNo, pageSize);
		if(page.getListMap().size()>0){
			List<CampaignCategory> list = null;
			CampaignCategory cc = null;
			for(Map<String,Object> map: page.getListMap()){
				cc  = new CampaignCategory();
				long id = Long.parseLong(map.get("id").toString());
				cc.setCampaignId(id);
				list = campaignCategoryDao.listBy(cc);
				// 渠道是否设置
				if(list.size()>0){
					map.put("channel_flag",true);
				}else{
					map.put("channel_flag",false);
				}
			}
		}
		return page;
	}
	
	/**
	 * 后台活动按ID查询
	 * @param id
	 * @return
	 */
	@Override
	public Map<String, Object> findCampBy(long id) {
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		Map<String, Object> map = campaignDao.getCampByID(id);
		return map;
	}

	@Override
	public Map<String, Object> findCampByMap(Map<String, Object> map) {
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		Map<String, Object> ret = campaignDao.getCampByMap(map);
		return ret;
	}

	@Override
	public Map<String, Object> findCampPartnerBy(long id) {
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		Map<String, Object> ret = campaignDao.getCampPartnerBy(id);
		return ret;
	}

	@Override
	public String stopCampaigns(Long[] partnerIds) {
		
		String retStr = "" ;
		
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);

		if (partnerIds != null && partnerIds.length > 0) {

			Map<String,Object> campMap = new HashMap<String,Object>();
			campMap.put("partner_ids", partnerIds);
			campMap.put("manulStatus", CampaignManulStatus.ONLINE.getCode());
			List<Campaign> list = campaignDao.selectCampsByMap(campMap);
			
			for (Campaign campaign : list){
				
				campaign.setManulStatus(CampaignManulStatus.PAUSE);
				campaignDao.update(campaign);

				try {
					ChangeManulStatusMsg changeManulStatusMsg = new ChangeManulStatusMsg();
					changeManulStatusMsg.setCampid(campaign.getId());
					changeManulStatusMsg.setStatus(CampaignManulStatus.PAUSE);
					mQConnectorService.sendMessage(changeManulStatusMsg);
				} catch (Exception e) {
					 logger.error("AdminCampService ----stopCampaigns---停止活动[id=" + campaign.getId() + "]后发送MQ错误", e);
				}
			}
		}
		else{
			
			retStr += "要停止活动的广告主id[]为空" ;
		}
		return retStr;
	}

	@Override
	public void updateProfit(Campaign camp) {
		campaignDao.updateProfit(camp);
	}

    @Override
    public String checkCampaignAndSendMq(Campaign campaign) {
        String ret = "";
        // 如果正在投放中,则发起暂停的请求
        if(campaign.getCampaignStatus().equals(CampaignStatus.ONLINE)){
            campaign.setManulStatus(CampaignManulStatus.PAUSE);
            campaignDao.update(campaign);
            try {
                ChangeManulStatusMsg changeManulStatusMsg = new ChangeManulStatusMsg();
                changeManulStatusMsg.setCampid(campaign.getId());
                changeManulStatusMsg.setStatus(CampaignManulStatus.PAUSE);
                mQConnectorService.sendMessage(changeManulStatusMsg);
            } catch (Exception e) {
                logger.error("渠道发生变化,活动暂停[id=" + campaign.getId() + "]后发送MQ错误", e);
            }
            ret = "广告活动已暂停,请重新开启活动!";
        }
        return ret;
    }


}
