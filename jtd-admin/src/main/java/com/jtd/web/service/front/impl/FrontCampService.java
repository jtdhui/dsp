package com.jtd.web.service.front.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jtd.commons.dao.BaseDao;
import com.jtd.commons.mybatis.support.CustomerContextHolder;
import com.jtd.commons.page.Pagination;
import com.jtd.utils.BeanUtils;
import com.jtd.utils.DateUtil;
import com.jtd.utils.ExcelUtil;
import com.jtd.utils.FileUtil;
import com.jtd.web.constants.*;
import com.jtd.web.controller.DicController;
import com.jtd.web.dao.*;
import com.jtd.web.jms.ChangeManulStatusMsg;
import com.jtd.web.jms.SetCampBlackListMsg;
import com.jtd.web.jms.SetCampGrossProfitMsg;
import com.jtd.web.jms.UpdateCampMsg;
import com.jtd.web.po.*;
import com.jtd.web.po.count.Campd;
import com.jtd.web.service.IMQConnectorService;
import com.jtd.web.service.front.*;
import com.jtd.web.service.impl.BaseService;
import com.jtd.web.vo.AppType;
import com.jtd.web.vo.ChannelCategory;
import com.jtd.web.vo.WebsiteType;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.Map.Entry;

/**
 * @作者 duber
 * @版本 V1.0
 * @创建日期 2016年11月4日
 * @描述
 */
@Service
public class FrontCampService extends BaseService<Campaign> implements IFrontCampService {

    private static final Log log = LogFactory.getLog(FrontCampService.class);

    /**
     * 活动黑名单
     */
    private static final String CAMPAIGN_DIM_KEY_BLACKLIST = "blacklist";
    /**
     * 活动广告为黑名单
     */
    private static final String CAMPAIGN_DIM_KEY_PLACE_BLACKLIST = "placeblacklist";
    /**
     * 地域定向key
     */
    private static final String CAMPAIGN_DIM_KEY_AREAS = "areas";
    /**
     * 人口定向匹配模式
     */
    private static final String CAMPAIGN_DIM_KEY_MATCHTYPE = "matchType";
    /**
     * 人群属性
     */
    private static final String CAMPAIGN_DIM_KEY_POPULATIONS = "populations";
    /**
     * 兴趣
     */
    private static final String CAMPAIGN_DIM_KEY_INSTRESTING = "instresting";
    /**
     * 个性化人群包
     */
    private static final String CAMPAIGN_DIM_KEY_COOKIEPACKET = "cookiePacket";
    /**
     * 访客找回包
     */
    private static final String CAMPAIGN_DIM_KEY_RETARGETPACKET = "retargetPacket";
    /**
     * ip访客定向
     */
    private static final String CAMPAIGN_DIM_KEY_IP = "ip";
    /**
     * 渠道
     */
    private static final String CAMPAIGN_DIM_KEY_CHANNELS = "channels";
    /**
     * 渠道行业类别
     */
    private static final String CAMPAIGN_DIM_KEY_CATEGORYS = "categorys";
    /**
     * 已选中的渠道id
     */
    private static final String CAMPAIGN_DIM_KEY_CHANNELS_SELECTED = "selected";
    /**
     * app定向
     */
    private static final String CAMPAIGN_DIM_KEY_APP = "app";
    /**
     * 按app类型
     */
    private static final String CAMPAIGN_DIM_KEY_APPTYPE = "appType";
    /**
     * 按应用宝
     */
    private static final String CAMPAIGN_DIM_KEY_APPPACKET = "appPacket";
    /**
     * 媒体
     */
    private static final String CAMPAIGN_DIM_KEY_MEDIA = "media";
    /**
     * 媒体类型
     */
    private static final String CAMPAIGN_DIM_KEY_MEDIA_WEBSITETYPE = "websiteType";
    /**
     * 媒体域名
     */
    private static final String CAMPAIGN_DIM_KEY_MEDIA_WEBDOMAIN = "webDomain";

    /**
     * 媒体包
     */
    private static final String CAMPAIGN_DIM_KEY_MEDIA_MEDIAPACKET = "mediaPacket";
    /**
     * 广告位
     */
    private static final String CAMPAIGN_DIM_KEY_MEDIA_ADPLACE = "adplace";
    /**
     * 频次
     */
    private static final String CAMPAIGN_DIM_KEY_FREQ = "freq";
    /**
     * 屏数
     */
    private static final String CAMPAIGN_DIM_KEY_SCREEN = "screen";
    /**
     * 设备类型
     */
    private static final String CAMPAIGN_DIM_KEY_DEVICE_DEVICETYPE = "deviceType";
    /**
     * 操作系统类型<br>
     *
     * @see OSType
     */
    private static final String CAMPAIGN_DIM_KEY_DEVICE_OSTYPE = "osType";
    /**
     * 品牌
     */
    private static final String CAMPAIGN_DIM_KEY_DEVICE_BRAND = "brand";
    /**
     * 网络类型
     *
     * @see NetWorkType
     */
    private static final String CAMPAIGN_DIM_KEY_DEVICE_NETWORKTYPE = "networkType";
    /**
     * 运营商类型
     *
     * @see OperatorType
     */
    private static final String CAMPAIGN_DIM_KEY_DEVICE_OPERATORTYPE = "operatorType";


    /**
     * 推广活动编辑所需要的所有的一级的campaignDimKey的集合
     */
    private static final List<String> CAMPAIGN_DIM_KEYS;

    static {
        CAMPAIGN_DIM_KEYS = new ArrayList<String>();
        CAMPAIGN_DIM_KEYS.add(CAMPAIGN_DIM_KEY_APP);
        CAMPAIGN_DIM_KEYS.add(CAMPAIGN_DIM_KEY_AREAS);
        CAMPAIGN_DIM_KEYS.add(CAMPAIGN_DIM_KEY_CATEGORYS);
        CAMPAIGN_DIM_KEYS.add(CAMPAIGN_DIM_KEY_CHANNELS);
        CAMPAIGN_DIM_KEYS.add(CAMPAIGN_DIM_KEY_COOKIEPACKET);
        CAMPAIGN_DIM_KEYS.add(CAMPAIGN_DIM_KEY_DEVICE_BRAND);
        CAMPAIGN_DIM_KEYS.add(CAMPAIGN_DIM_KEY_DEVICE_DEVICETYPE);
        CAMPAIGN_DIM_KEYS.add(CAMPAIGN_DIM_KEY_DEVICE_NETWORKTYPE);
        CAMPAIGN_DIM_KEYS.add(CAMPAIGN_DIM_KEY_DEVICE_OPERATORTYPE);
        CAMPAIGN_DIM_KEYS.add(CAMPAIGN_DIM_KEY_DEVICE_OSTYPE);
        CAMPAIGN_DIM_KEYS.add(CAMPAIGN_DIM_KEY_FREQ);
        CAMPAIGN_DIM_KEYS.add(CAMPAIGN_DIM_KEY_INSTRESTING);
        CAMPAIGN_DIM_KEYS.add(CAMPAIGN_DIM_KEY_IP);
        CAMPAIGN_DIM_KEYS.add(CAMPAIGN_DIM_KEY_MEDIA);
        CAMPAIGN_DIM_KEYS.add(CAMPAIGN_DIM_KEY_MATCHTYPE);
        CAMPAIGN_DIM_KEYS.add(CAMPAIGN_DIM_KEY_POPULATIONS);
        CAMPAIGN_DIM_KEYS.add(CAMPAIGN_DIM_KEY_RETARGETPACKET);
        CAMPAIGN_DIM_KEYS.add(CAMPAIGN_DIM_KEY_SCREEN);
    }

    @Autowired
    private IMQConnectorService mQConnectorService;

    @Autowired
    private ICampaignDao campaignDao;

    @Autowired
    private ICampdCountDao campdCountDao;

    @Autowired
    private ICampaignDimDao campaignDimDao;

    @Autowired
    private ICampaignCategoryDao campaignCategoryDao;

    @Autowired
    private IFrontCreativeSizeFlowService frontCreativeSizeFlowService; //创意尺寸

    @Autowired
    private IFrontPartnerService partnerService;

    @Autowired
    private IFrontCampGroupService campGroupService;

    @Autowired
    private IChannelDao channelDao;

    @Autowired
    private IFrontAdService adService;

    @Autowired
    private IFrontAdCategoryService adCategoryService;

    @Autowired
    private ICreativeDao creativeDao;

    @Autowired
    private ICookieGidDao cookieGidDao;

    @Autowired
    private IFrontCookiePacketService frontCookiePacketService;

    @Autowired
    private IFrontRetargetPacketService frontRetargetPacketService;

    @Autowired
    private IAdAuditStatusDao adAuditStatusDao;

    @Override
    protected BaseDao<Campaign> getDao() {
        CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
        return campaignDao;
    }

    @Override
    public Pagination<Campaign> findFrontCampListPageBy(Map<String, Object> paraMap, Integer pageNo, Integer pageSize) {
        //1. 分页查询数据库
        CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
        Pagination<Campaign> pc = null;
        if (paraMap.get("endDate") != null) {
            String endDate = paraMap.get("endDate").toString();
            paraMap.put("endDate", endDate + " 23:59:59");
            pc = findFrontPageBy(paraMap, pageNo, pageSize);
            paraMap.put("endDate", endDate);
        } else {
            pc = findFrontPageBy(paraMap, pageNo, pageSize);
        }
        //2. 遍历结果集
        List<Campaign> listC = pc.getList();

        if (listC.size() == 0) {
            return pc;
        }


        String cpIds = "";
        Campaign cp = null;
        for (int i = 0; i < listC.size(); i++) {
            cp = listC.get(i);
            cpIds += cp.getId() + ",";
        }

        cpIds = cpIds.substring(0, cpIds.length() - 1);
        //3. 根据活动ID查询统计库中的报表数据
        CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_COUNT);
        List<Campd> listCD = campdCountDao.findListByCampIds(cpIds);

        Map<String, Object> map = null;
        //封装数据
        Campd cd = null;
        List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < listC.size(); i++) {
            cp = listC.get(i);
            map = new HashMap<String, Object>();
            map.put("camp_id", cp.getId());
            map.put("camp_name", cp.getCampaignName());
            map.put("camp_type", CampaignType.getName(cp.getCampaignType()));
            map.put("expend_type", ExpendType.getName(cp.getExpendType()));
            map.put("daily_budget", cp.getDailyBudget());

            com.jtd.web.model.Campaign cmpmodel = new com.jtd.web.model.Campaign();
            cmpmodel.setManulStatus(cp.getManulStatus());
            cmpmodel.setAutoStatus(cp.getAutoStatus());
            map.put("campaign_status", cmpmodel.getCampaignStatus().getDesc());
            map.put("status", cmpmodel.getCampaignStatus().getCode());

            boolean flag = false;
            for (int j = 0; j < listCD.size(); j++) {
                cd = listCD.get(j);
                if (cp.getId().equals(cd.getCampId())) {
                    flag = true;
                    map.put("pv", cd.getPv());
                    map.put("click", cd.getClick());
                    map.put("expend", cd.getExpend());
                }
            }

            if (flag == false) {
                map.put("pv", 0);
                map.put("click", 0);
                map.put("expend", 0);
            }
            listMap.add(map);
        }
        pc.setListMap(listMap);
        return pc;
    }

    @Override
    public List<Map<String, Object>> getCampaignsBy(Map<String, Object> paraMap) {
        CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
        List<Campaign> listC = null;
        if (paraMap.get("endDate") != null) {
            String endDate = paraMap.get("endDate").toString();
            paraMap.put("endDate", endDate + " 23:59:59");
            listC = campaignDao.getCampaignsBy(paraMap);
            paraMap.put("endDate", endDate);
        } else {
            listC = campaignDao.getCampaignsBy(paraMap);
        }
        if (listC.size() < 1) {
            return null;
        }
        String cpIds = "";
        Campaign cp = null;
        for (int i = 0; i < listC.size(); i++) {
            cp = listC.get(i);
            cpIds += cp.getId() + ",";
        }
        cpIds = cpIds.substring(0, cpIds.length() - 1);
        // 3. 根据活动ID查询统计库中的报表数据
        CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_COUNT);
        List<Campd> listCD = campdCountDao.findListByCampIds(cpIds);

        Map<String, Object> map = null;
        // 封装数据
        Campd cd = null;
        List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < listC.size(); i++) {
            cp = listC.get(i);
            map = new HashMap<String, Object>();
            map.put("camp_id", cp.getId());
            map.put("camp_name", cp.getCampaignName());
            map.put("camp_type", CampaignType.getName(cp.getCampaignType()));
            map.put("expend_type", ExpendType.getName(cp.getExpendType()));
            map.put("daily_budget", cp.getDailyBudget());

            com.jtd.web.model.Campaign cmpmodel = new com.jtd.web.model.Campaign();
            cmpmodel.setManulStatus(cp.getManulStatus());
            cmpmodel.setAutoStatus(cp.getAutoStatus());
            map.put("campaign_status", cmpmodel.getCampaignStatus().getDesc());

            boolean flag = false;
            for (int j = 0; j < listCD.size(); j++) {
                cd = listCD.get(j);
                if (cp.getId() == cd.getCampId()) {
                    flag = true;
                    map.put("pv", cd.getPv());
                    map.put("click", cd.getClick());
                    map.put("expend", cd.getExpend());
                }
            }

            if (flag == false) {
                map.put("pv", 0);
                map.put("click", 0);
                map.put("expend", 0);
            }
            listMap.add(map);
        }
        return listMap;
    }

    @Override
    public List<Map<String, Object>> findCreativesByCampId(Map<String, Object> map) {
        CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
        return campaignDao.getCreativesByCampId(map);
    }

    @Override
    public void modifyCatgoryChannels(CampaignDim cd) {
        CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
        long campaign_id = cd.getCampaignId();
        String channelIds = cd.getDimValue();
        List<CampaignCategory> listCC = new ArrayList<CampaignCategory>();
        List<Integer> listID = new ArrayList<Integer>();
        //处理页面参数
        CampaignCategory cc = null;
        AdCategory ac = null;
        if (channelIds.length() > 0) {
            Date now = new Date();
            String[] channels = channelIds.split(",");
            for (int i = 0; i < channels.length; i++) {
                cc = new CampaignCategory();
                ac = new AdCategory();
                String ccStr = channels[i].toString();
                String[] ccArray = ccStr.split("\\$\\$\\$");
                String catgid = "0";
                if(StringUtils.isNotEmpty(ccArray[1])){
                    catgid = ccArray[1];
                }
                Integer channelID = Integer.parseInt(ccArray[0]);
                cc.setCampaignId(campaign_id);
                cc.setCatgid(catgid);
                cc.setCatgserial(ccArray[2]);
                cc.setCreateTime(now);

                listID.add(channelID);
                listCC.add(cc);

                ac.setCampaignId(campaign_id);
                ac.setCatgId(catgid);
                ac.setCatgserial(ccArray[2]);
                ac.setCreateTime(now);
                adCategoryService.updateCatgId(ac);
            }

        }
        //封装成json对象
//		Map<String,Object> map = new HashMap<String,Object>();
//		if(listID.size()>0){
//			map.put("selected", listID);
//			map.put("type", 1);
//		}else{
//			map.put("selected", "[]");
//			map.put("type", 0);
//		}
//
//		CampaignDim obj = new CampaignDim();
//		obj.setCampaignId(campaign_id);
//		obj.setDimName("channels");
//		obj.setDimValue(JsonUtil.toJSONString(map));
//		CampaignDim campaignDim = campaignDimDao.selectByCampAndDimName(obj);
//		if(campaignDim==null){
//			campaignDimDao.insert(obj);
//		}else{
//			campaignDimDao.updateByCampAndDimName(obj);
//		}

        //删除活动关联的行业类型
        campaignCategoryDao.deleteByCampaignId(campaign_id);
        //增加活动关联的行业类型
        campaignCategoryDao.insert(listCC);
    }

    @Override
    public Campaign getByMap(Campaign camp) {
        CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
        return campaignDao.getByMap(camp);
    }


    @Override
    public void savaCampAndSendMessage(Campaign camp) {
        CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
        campaignDao.insert(camp);
        // 根据当前的状态来判断是否需要
        checkCampaignStatusForUpdate(camp.getId());
    }

    @Override
    public void modifyCampAndSendMessage(Campaign camp,Campaign oldCampaign) {
        CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);

        // 活动类型或者广告类型发生变化,则广告活动的定向策略重置,该活动下面的广告创意也要删除
        if(oldCampaign.getCampaignType().intValue() != camp.getCampaignType().intValue() || oldCampaign.getAdType().intValue() != camp.getAdType().intValue()){

            //活动的定向策略
            campaignDimDao.deleteCampaignDim(camp.getId());

            //活动的行业类别
            campaignCategoryDao.deleteByCampaignId(camp.getId());

            //广告的行业类别
            AdCategory adCategory = new AdCategory();
            adCategory.setCampaignId(camp.getId());
            adCategoryService.deleteAdCategory(adCategory);

            // 遍历活动下的广告数据,根据广告删除广告、广告的审核状态和创意
            Ad ad = new Ad();
            ad.setCampaignId(camp.getId());
            List<Ad> adList = adService.listBy(ad);
            if(adList.size()>0){
                Map<String,Object> map = null;
                for(Ad entity: adList){

                    //删除广告的审核状态信息
                    map = new HashMap<String,Object>();
                    map.put("adId",entity.getId());
                    adAuditStatusDao.deleteByAdId(map);

                    //删除创意
                    creativeDao.deleteById(entity.getCreativeId());

                    //删除广告
                    adService.deleteById(entity.getId());
                }
            }
        }

        //更新活动
        campaignDao.update(camp);
        // 根据当前的状态来判断是否需要
        checkCampaignStatusForUpdate(camp.getId());
    }

    @Override
    public String changeCampaignManulStatus(String[] campaignIds, CampaignManulStatus campaignManulStatus) {
        CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
        Ad ad = null;
        String ret = new String();
        for (String campaignId : campaignIds) {
            Campaign campaign = campaignDao.getById(Long.parseLong(campaignId));  /** 根据活动ID，获取活动信息，campaign表 */
            if (campaign == null) {
                log.warn("推广活动[" + campaignId + "]不存在");
                continue;
            }
            
            ad = new Ad();
            ad.setCampaignId(campaign.getId());
            List<Ad> listAd = adService.listBy(ad);  /** 根据活动ID，获取此活动的创意列表，ad表 */
            boolean flag = false;
            for (Ad po : listAd) {
                if (po.getInternalAudit() != 1 && campaignManulStatus.equals(CampaignManulStatus.ONLINE)) /** InternalAudit内部审核状态(0:待审核 1:已通过 2:已拒绝) */
                {
                    flag = true;
                }
            }
            if (flag) { /** 不是有效的活动创意，当前活动下，只有有一个创意没有后台审核通过，都不能开启广告 */
                ret += campaignId + ",";
                continue;
            }
            
            CampaignManulStatus oldCampaignManulStatus = campaign.getManulStatus();
            if (oldCampaignManulStatus.equals(CampaignManulStatus.DELETE)) {
                log.debug("推广活动[" + campaignId + "]已删除，不能操作");
                continue;
            }
            if (oldCampaignManulStatus.equals(CampaignManulStatus.EDIT)) {
                log.debug("推广活动[" + campaignId + "]正在编辑中，不能操作");
                continue;
            }
            if (oldCampaignManulStatus.equals(CampaignManulStatus.OFFLINE)) {
                log.debug("推广活动[" + campaignId + "]已经结束，不能操作");
                continue;
            }
            campaign.setManulStatus(campaignManulStatus);
            if (StringUtils.isEmpty(campaign.getWeekHour())) {
                campaign.setWeekHour(
                        "[[1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1],[1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1],[1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1],[1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1],[1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1],[1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1],[1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1]]");
            }
            /**
             * manul_status修改为传入的参数campaignManulStatus（ '手动状态值，0为编辑中，1为暂停（已提交），2为投放中，3为暂停，4为结束，5为已删除'）
             */
            campaignDao.update(campaign);

            switch (campaignManulStatus) {
                case ONLINE:
                    // 开启
                    if (oldCampaignManulStatus.equals(CampaignManulStatus.ONLINE)) {
                        log.debug("推广活动[" + campaignId + "]正在投放中，不能重复开启");
                        continue;
                    }
                    sendCamapaignToEngine(campaign);  /** 发送广告到engine引擎 */
                    break;
                case PAUSE:
                    // 暂停
                    if (oldCampaignManulStatus.equals(CampaignManulStatus.TOCOMMIT)
                            || oldCampaignManulStatus.equals(CampaignManulStatus.PAUSE)) {
                        log.debug("推广活动[" + campaignId + "]正在暂停，不能暂停");
                        continue;
                    }
                case OFFLINE:
                    // 终止
                    // 待提交、投放中、暂停的状态都可以终止
                    ChangeManulStatusMsg changeManulStatusMsg = new ChangeManulStatusMsg();
                    changeManulStatusMsg.setCampid(campaign.getId());
                    changeManulStatusMsg.setStatus(campaignManulStatus);
                    mQConnectorService.sendMessage(changeManulStatusMsg);
                    break;
                default:
                    break;
            }

        }// end for
        if (ret.length() > 0) {
            ret = ret.substring(0, ret.length() - 1);
        }
        return ret;
    }

    /**
     * 编辑活动后检查当前活动的状态，判断如何修改活动状态
     *
     * @param campaignId
     */
    private void checkCampaignStatusForUpdate(Long campaignId) {
        Campaign campaign = campaignDao.getById(campaignId);
        if (campaign.getCampaignStatus().equals(CampaignStatus.EDIT)
                || campaign.getCampaignStatus().equals(CampaignStatus.TOCOMMIT)) {
            // 编辑中和待提交的的活动，修改后不改变状态
            return;
        }
        if (campaign.getCampaignStatus().equals(CampaignStatus.READY)
                || campaign.getCampaignStatus().equals(CampaignStatus.ONLINE)
                || campaign.getCampaignStatus().equals(CampaignStatus.PAUSED)
                || campaign.getCampaignStatus().equals(CampaignStatus.OFFLINE)) {
            // 重新发送活动对象到引擎
            sendCamapaignToEngine(campaign);
        }
        if (campaign.getCampaignStatus().equals(CampaignStatus.FINISHED)) {
            // 修改活动状态,恢复为待提交状态
            campaign.setAutoStatus(CampaignAutoStatus.READY);
            campaign.setManulStatus(CampaignManulStatus.TOCOMMIT);
            campaignDao.update(campaign);
        }
    }

    /**
     * 发送活动到引擎服务器
     *
     * @param oldCampaign
     */
    private void sendCamapaignToEngine(Campaign oldCampaign) {

        com.jtd.web.model.Campaign campaign = new com.jtd.web.model.Campaign();
        //复制对象
        campaign = copyCampaignToModel(oldCampaign, campaign);

        Long partnerId = campaign.getPartnerId();
        Partner partner = partnerService.getById(partnerId);
        campaign.setPartnerName(partner.getPartnerName());
        String tanxPartnerId = "";
        if (partner.getTanxAdvertiserid() == null) {
            campaign.setTanxPartnerId(tanxPartnerId);
        } else {
            tanxPartnerId = partner.getTanxAdvertiserid().toString();
            campaign.setTanxPartnerId(tanxPartnerId);
        }


        Long campGroupId = campaign.getGroupId();
        CampGroup campGroup = campGroupService.getById(campGroupId);
        campaign.setGroupName(campGroup.getGroupName());
        campaign.setGroupDailyBudgetGoal(campGroup.getDailybudget());
        campaign.setGroupTotalBudgetGoal(campGroup.getTotalbudget());
        campaign.setGroupDailyPvGoal(campGroup.getDailypv());
        campaign.setGroupTotalPvGoal(campGroup.getTotalpv());
        campaign.setGroupDailyClickGoal(campGroup.getDailyclick());
        campaign.setGroupTotalClickGoal(campGroup.getTotalclick());

        Map<String, String> campaignDims = getCampaignDimsForEngine(campaign.getId());
        campaign.setCampaignDims(campaignDims);

        Map<String, List<com.jtd.web.model.Ad>> ads = getAdsForEngine(campaign.getId());
        campaign.setAds(ads);

        UpdateCampMsg updateCampMsg = new UpdateCampMsg();
        updateCampMsg.setCampaign(campaign);
        mQConnectorService.sendMessage(updateCampMsg);
    }

    private com.jtd.web.model.Campaign copyCampaignToModel(Campaign oldCampaign, com.jtd.web.model.Campaign campaign) {

        campaign.setId(oldCampaign.getId());
        campaign.setPartnerId(oldCampaign.getPartnerId());
        campaign.setGroupId(oldCampaign.getGroupId());
        campaign.setCampName(oldCampaign.getCampaignName());
        campaign.setCampType(CampaignType.fromCode(oldCampaign.getCampaignType()));
        if (oldCampaign.getAdType() != null) {
            campaign.setAdType(AdType.getAdType(oldCampaign.getAdType()));
        }
        if (oldCampaign.getTransType() != null) {
            campaign.setTransType(TransType.fromCode(oldCampaign.getTransType()));
        }
        if (oldCampaign.getBudgetctrltype() != null) {
            campaign.setBudgetCtrlType(BudgetCtrlType.fromCode(oldCampaign.getBudgetctrltype()));
        }
        campaign.setStartTime(oldCampaign.getStartTime());
        campaign.setEndTime(oldCampaign.getEndTime());
        if (!StringUtils.isEmpty(oldCampaign.getWeekHour())) {
            campaign.setWeekhour(oldCampaign.getWeekHour());
        }
        campaign.setExpendType(ExpendType.fromCode(oldCampaign.getExpendType()));
        campaign.setPrice(oldCampaign.getPrice());

        if (oldCampaign.getDailyBudget() != null) {
            campaign.setDailyBudget(oldCampaign.getDailyBudget());
        } else {
            campaign.setDailyBudget(2147483647L);
        }

        if (oldCampaign.getTotalBudget() != null) {
            campaign.setTotalBudget(oldCampaign.getTotalBudget());
        } else {
            campaign.setTotalBudget(2147483647L);
        }

        if (oldCampaign.getDailyPv() == null) {
            campaign.setDailyPv(2147483647L);
        } else {
            campaign.setDailyPv(oldCampaign.getDailyPv());
        }

        if (oldCampaign.getDailyClick() == null) {
            campaign.setDailyClick(2147483647L);
        } else {
            campaign.setDailyClick(oldCampaign.getDailyClick());
        }

        if (oldCampaign.getTotalClick() == null) {
            campaign.setTotalClick(2147483647L);
        } else {
            campaign.setTotalClick(oldCampaign.getTotalClick());
        }

        if (oldCampaign.getTotalPv() == null) {
            campaign.setTotalPv(2147483647L);
        } else {
            campaign.setTotalPv(oldCampaign.getTotalPv());
        }

        campaign.setGrossProfit(oldCampaign.getGrossProfit());
        campaign.setMaxNeGrossProfit(oldCampaign.getMaxNegrossProfit());
        campaign.setAutoStatus(oldCampaign.getAutoStatus());
        campaign.setManulStatus(oldCampaign.getManulStatus());
        campaign.setAfValue(oldCampaign.getAfValue());
        if (oldCampaign.getPuttarget() != null) {
            campaign.setPutTarget(PutTarget.fromCode(oldCampaign.getPuttarget()));
        }
        if (!StringUtils.isEmpty(oldCampaign.getPhoneTitle())) {
            campaign.setPhoneTitle(oldCampaign.getPhoneTitle());
        }
        if (!StringUtils.isEmpty(oldCampaign.getPhoneDescribe())) {
            campaign.setPhoneDescribe(oldCampaign.getPhoneDescribe());
        }
        if (!StringUtils.isEmpty(oldCampaign.getPhonePropaganda())) {
            campaign.setPhonePropaganda(oldCampaign.getPhonePropaganda());
        }
        if (!StringUtils.isEmpty(oldCampaign.getOriginalPrice())) {
            campaign.setOriginalPrice(oldCampaign.getOriginalPrice());
        }
        if (!StringUtils.isEmpty(oldCampaign.getDiscountPrice())) {
            campaign.setDiscountPrice(oldCampaign.getDiscountPrice());
        }
        if (!StringUtils.isEmpty(oldCampaign.getSalesVolume())) {
            campaign.setSalesVolume(Integer.parseInt(oldCampaign.getSalesVolume()));
        }
        if (!StringUtils.isEmpty(oldCampaign.getOptional())) {
            campaign.setOptional(oldCampaign.getOptional());
        }
        if (!StringUtils.isEmpty(oldCampaign.getDeepLink())) {
            campaign.setDeepLink(oldCampaign.getDeepLink());
        }
        if (!StringUtils.isEmpty(oldCampaign.getMonitor())) {
            campaign.setMonitor(oldCampaign.getMonitor());
        }
        if (!StringUtils.isEmpty(oldCampaign.getPhoneDeepLinkUrl())) {
            campaign.setPhoneDeepLinkUrl(oldCampaign.getPhoneDeepLinkUrl());
        }

        campaign.setAdList(null);
        campaign.setCampaignCategorys(null);
        campaign.setCampaignDimJsonObject(null);
        campaign.setCampaignStatus(null);
        campaign.setClickUrl(null);
        campaign.setLandingPage(null);
        campaign.setLandingType(null);
        campaign.setPvUrls(null);
        campaign.setCreateTime(null);
        campaign.setCreatorId(null);
        campaign.setModifierId(null);
        campaign.setLastModifyTime(null);
        campaign.setDeleteStatus(null);
        campaign.setEditStepStatus(null);

        return campaign;
    }

    private Map<String, List<com.jtd.web.model.Ad>> getAdsForEngine(Long campaignId) {
        Campaign campaign = campaignDao.getById(campaignId);
        Map<String, List<com.jtd.web.model.Ad>> results = new HashMap<String, List<com.jtd.web.model.Ad>>();
        Ad adPO = new Ad();
        adPO.setCampaignId(campaignId);
        List<Ad> ads = adService.listBy(adPO);
        Map<String, Object> map = null;
        AdCategory ac = null;
        List<AdCategory> listAc = null;
        for (Ad oldAd : ads) {
            com.jtd.web.model.Ad ad = new com.jtd.web.model.Ad();
//			BeanUtils.copyProperties(oldAd, ad);
            copyAdToModel(oldAd, ad);
            // 查询相关数据
            map = adService.findFullAdById(oldAd);
            if (map == null) {
                continue;
            }
            String size = map.get("size").toString();
            ac = new AdCategory();
            ac.setAdId(oldAd.getId());
            ac.setCampaignId(oldAd.getCampaignId());
            listAc = adCategoryService.findAdCategoryBy(ac);
            Map<String, String> acMap = new HashMap<String, String>();
            if (listAc.size() > 0) {

                for (AdCategory adCategory : listAc) {
                    acMap.put(adCategory.getCatgserial(), adCategory.getCatgId());
                }
            }
            ad.setAdCategorys(acMap);
            ad.setSize(size);
            ad.setSizeid(Long.parseLong(map.get("size_id").toString()));
            ad.setGroupId(Long.parseLong(map.get("group_id").toString()));
            ad.setGroupName(map.get("group_name").toString());
            Creative creative = creativeDao.getById(ad.getCreativeId());
            if (creative != null) {
                ad.setCreativeType(CreativeType.fromCode(creative.getCreativeType()));
                ad.setCreativeUrl(creative.getCreativeUrl());
                ad.setSuffix(creative.getSuffix());
            }
            ad.setAdAuditStatuses(null);
            ad.setInternalAudit(null);
            ad.setCampaignName(null);
            ad.setDeleteStatus(null);
            if (ad.getClickUrl() == null) {
                ad.setClickUrl(campaign.getClickUrl());
            }
            if (ad.getLandingPage() == null) {
                ad.setLandingPage(campaign.getLandingPage());
            }
            if (ad.getPvUrls() == null) {
                ad.setPvUrls(campaign.getPvUrls());
            }
            List<com.jtd.web.model.Ad> list = results.get(size);
            if (list == null) {
                list = new ArrayList<com.jtd.web.model.Ad>();
                results.put(size, list);
            }
            list.add(ad);
        }
        return results;
    }

    private com.jtd.web.model.Ad copyAdToModel(Ad oldAd, com.jtd.web.model.Ad ad) {
        ad.setId(oldAd.getId());
        ad.setCampId(oldAd.getCampaignId());
        ad.setCreativeId(oldAd.getCreativeId());
        ad.setClickUrl(oldAd.getClickUrl());
        ad.setLandingPage(oldAd.getLandingPage());
        if (oldAd.getLandingType() != null) {
            ad.setLandingType(LandingType.fromCode(oldAd.getLandingType()));
        }
        if (oldAd.getInternalAudit() != null) {
            ad.setInternalAudit(Boolean.parseBoolean(oldAd.getInternalAudit().toString()));
        }
        if (oldAd.getPhoneTitle() != null) {
            ad.setPhoneTitle(oldAd.getPhoneTitle());
        }
        if (oldAd.getPhonePropaganda() != null) {
            ad.setPhonePropaganda(oldAd.getPhonePropaganda());
        }
        if (oldAd.getPhoneDescribe() != null) {
            ad.setPhoneDescribe(oldAd.getPhoneDescribe());
        }
        if (oldAd.getOriginalPrice() != null) {
            ad.setOriginalPrice(oldAd.getOriginalPrice());
        }
        if (oldAd.getDiscountPrice() != null) {
            ad.setDiscountPrice(oldAd.getDiscountPrice());
        }
        if (oldAd.getSalesVolume() != null) {
            ad.setSalesVolume(Integer.parseInt(oldAd.getSalesVolume()));
        }

        ad.setOptional(oldAd.getOptional());
        ad.setDeepLink(oldAd.getDeepLink());
        ad.setMonitor(oldAd.getMonitor());
        ad.setPhoneDeepLinkUrl(oldAd.getPhoneDeepLinkUrl());
        ad.setPvUrls(oldAd.getPvUrls());
        if (oldAd.getDeleteStatus() != null) {
            ad.setDeleteStatus(Boolean.parseBoolean(oldAd.getDeleteStatus().toString()));
        }

        return ad;
    }

    /**
     * 创建一个符合引擎规范的campaignDim数据
     *
     * @param campaignId
     * @return
     */
    private Map<String, String> getCampaignDimsForEngine(Long campaignId) {

        Map<String, String> camapignDimMap = new HashMap<String, String>();
        Map<String, CampaignDim> tempCampaignDims = new HashMap<String, CampaignDim>();
        CampaignDim cd = new CampaignDim();
        cd.setCampaignId(campaignId);
        List<CampaignDim> campaignDims = campaignDimDao.listBy(cd);
        for (CampaignDim campaignDim : campaignDims) {
            String key = campaignDim.getDimName();
            if (CAMPAIGN_DIM_KEYS.contains(key)) {
                tempCampaignDims.put(key, campaignDim);
            }
        }

        // 检查是否有地域定向数据
        CampaignDim areas = tempCampaignDims.get(CAMPAIGN_DIM_KEY_AREAS);
        if (areas != null) {
            JSONObject areasObject = JSON.parseObject(areas.getDimValue());
            Integer type = areasObject.getInteger("type");
            if (type != 0) {
                JSONArray citys = areasObject.getJSONArray("citys");
                Set<Integer> selectCityIds = new HashSet<Integer>();
                for (Object cityId : citys) {
                    selectCityIds.add(Integer.valueOf(cityId.toString()));
                }
                String cityIdStr = "";
                int allCitySize = DicController.getCitySize();
                for (int i = 1; i <= allCitySize; i++) {
                    if (selectCityIds.contains(i)) {
                        // 选中的城市置1
                        cityIdStr += "1";
                    } else {
                        // 选中的城市置0
                        cityIdStr += "0";
                    }
                }
                camapignDimMap.put("cityids", cityIdStr);
            }
        }

        // 检查人群定向
        List<Long> cookieIds = new ArrayList<Long>();
        CampaignDim populationDim = tempCampaignDims.get(CAMPAIGN_DIM_KEY_POPULATIONS);
        if (populationDim != null) {
            JSONArray populationsArray = JSON.parseArray(populationDim.getDimValue());
            for (Object ckid : populationsArray) {
                cookieIds.add(Long.valueOf(ckid.toString()));
            }
        }
        CampaignDim instrestingDim = tempCampaignDims.get(CAMPAIGN_DIM_KEY_INSTRESTING);
        if (instrestingDim != null) {
            for (Object ckid : JSON.parseArray(instrestingDim.getDimValue())) {
                cookieIds.add(Long.valueOf(ckid.toString()));
            }
        }
        CampaignDim cookiePacketDim = tempCampaignDims.get(CAMPAIGN_DIM_KEY_COOKIEPACKET);
        if (cookiePacketDim != null) {
            for (Object ckid : JSON.parseArray(cookiePacketDim.getDimValue())) {
                cookieIds.add(Long.valueOf(ckid.toString()));
            }
        }
        CampaignDim retargetPacketDim = tempCampaignDims.get(CAMPAIGN_DIM_KEY_RETARGETPACKET);
        if (retargetPacketDim != null) {
            for (Object ckid : JSON.parseArray(retargetPacketDim.getDimValue())) {
                cookieIds.add(Long.valueOf(ckid.toString()));
            }
        }
        JSONObject cookiegidObject = new JSONObject();
        if (cookieIds.size() > 0) {
            List<CookieGid> cookieGids = cookieGidDao.listCookieGidsByIds(cookieIds);
            if (cookieGids != null && !cookieGids.isEmpty()) {
                for (CookieGid cookieGid : cookieGids) {
                    String cookieGidDimName = cookieGid.getDimName();
                    JSONArray ids = cookiegidObject.getJSONArray(cookieGidDimName);
                    if (ids == null) {
                        ids = new JSONArray();
                        cookiegidObject.put(cookieGidDimName, ids);
                    }
                    ids.add(cookieGid.getId());
                }
            }
        }
        CampaignDim ipDim = tempCampaignDims.get(CAMPAIGN_DIM_KEY_IP);
        if (ipDim != null) {
            JSONArray ipArray = JSON.parseArray(ipDim.getDimValue());
            cookiegidObject.put("ip", ipArray);
        }
        if (!cookiegidObject.isEmpty()) {
            String matchType = tempCampaignDims.get(CAMPAIGN_DIM_KEY_MATCHTYPE).getDimValue();
            cookiegidObject.put("mt", matchType);
            camapignDimMap.put("cookiegid", JSON.toJSONString(cookiegidObject));
        }

        // 检查投放频次和屏数维度
        JSONObject freqandscreen = new JSONObject();
        CampaignDim freqDim = tempCampaignDims.get(CAMPAIGN_DIM_KEY_FREQ);
        if (freqDim != null) {
            JSONObject freqObject = JSON.parseObject(freqDim.getDimValue());
            Integer type = freqObject.getInteger("type");
            if (type == 1) {
                Integer timeInterval = freqObject.getInteger("timeInterval");
                String freqType = freqObject.getString("freqType");
                Integer freqValue = freqObject.getInteger("freqValue");
                Integer[] freqs = new Integer[]{0, 0, 0};
                freqs[0] = timeInterval;
                if (freqType.equals("pv")) {
                    freqs[1] = freqValue;
                } else if (freqType.equals("click")) {
                    freqs[2] = freqValue;
                }
                freqandscreen.put("freq", freqs);
            }
        }

        //屏数定向
        CampaignDim screenDim = tempCampaignDims.get(CAMPAIGN_DIM_KEY_SCREEN);
        if (screenDim != null) {
            JSONObject screenObject = JSON.parseObject(screenDim.getDimValue());
            Integer type = screenObject.getInteger("type");
            freqandscreen.put("firsts", type);
        }
        if (!freqandscreen.isEmpty()) {
            camapignDimMap.put("freqandscreen", JSON.toJSONString(freqandscreen));
        }
        // 域名定向
        CampaignDim mediaDim = tempCampaignDims.get(CAMPAIGN_DIM_KEY_MEDIA);
        if (mediaDim != null) {
            JSONObject mediaObject = JSON.parseObject(mediaDim.getDimValue());
            Integer type = mediaObject.getInteger("type");
            if (type != 0) {
                // 检查按媒体域名投放
                JSONArray domainList = new JSONArray();
                JSONArray webDomainArray = mediaObject.getJSONArray(CAMPAIGN_DIM_KEY_MEDIA_WEBDOMAIN);
                if (webDomainArray != null && !webDomainArray.isEmpty()) {
                    for(Object obj : webDomainArray){
                        String webDomain = obj.toString();
                        if(!webDomain.startsWith("http")){
                            webDomain="http://"+webDomain;
                        }
                        if (StringUtils.isEmpty(webDomain)) return null;
                        String host = null;
                        try {
                            URL u = new URL(webDomain);
                            host = u.getHost();
                            domainList.add(host);
                        } catch (Exception e) {
                            return null;
                        }
                    }
//                    domainList.addAll(webDomainArray);
                }
                if (!domainList.isEmpty()) {
                    camapignDimMap.put("pageurls", JSON.toJSONString(domainList));
                }

                // 媒体类型
                JSONArray websiteTypeArray = mediaObject.getJSONArray(CAMPAIGN_DIM_KEY_MEDIA_WEBSITETYPE);
                if (websiteTypeArray != null && !websiteTypeArray.isEmpty()) {
                    JSONObject websiteTypeMapObject = new JSONObject();
                    for (CatgSerial catgSerial : CatgSerial.values()) {
                        websiteTypeMapObject.put(catgSerial.toString().toLowerCase(), new JSONArray());
                    }
                    for (Object obj : websiteTypeArray) {
                        String websiteTypeId = obj.toString();
                        websiteTypeMapObject.getJSONArray("dsp").add(websiteTypeId);
                        WebsiteType websiteType = DicController.getWebsiteType(websiteTypeId);
                        Map<CatgSerial, String> channelWebsiteTypes = websiteType.getChannelWebsiteTypes();
                        for (Entry<CatgSerial, String> entry : channelWebsiteTypes.entrySet()) {
                            CatgSerial catgSerial = entry.getKey();
                            websiteTypeMapObject.getJSONArray(catgSerial.toString().toLowerCase()).add(entry.getValue());
                        }
                    }
                    camapignDimMap.put("webcatg", JSON.toJSONString(websiteTypeMapObject));
                }
            }

        }

        CampaignDim appDim = tempCampaignDims.get(CAMPAIGN_DIM_KEY_APP);
        if (appDim != null) {
            JSONObject appObject = JSON.parseObject(appDim.getDimValue());
            // 应用类型维度
            JSONArray appTypeList = appObject.getJSONArray(CAMPAIGN_DIM_KEY_APPTYPE);
            if (appTypeList != null && !appTypeList.isEmpty()) {
                JSONObject appTypeMapObject = new JSONObject();
                for (CatgSerial catgSerial : CatgSerial.values()) {
                    appTypeMapObject.put(catgSerial.toString().toLowerCase(), new JSONArray());
                }
                for (Object obj : appTypeList) {
                    String appTypeId = obj.toString();
                    appTypeMapObject.getJSONArray("dsp").add(appTypeId);
                    AppType appType = DicController.getAppType(appTypeId);
                    Map<CatgSerial, String> channelappTypes = appType.getChannelAppTypes();
                    for (Entry<CatgSerial, String> entry : channelappTypes.entrySet()) {
                        CatgSerial catgSerial = entry.getKey();
                        appTypeMapObject.getJSONArray(catgSerial.toString().toLowerCase()).add(entry.getValue());
                    }
                }
                camapignDimMap.put("appcatg", JSON.toJSONString(appTypeMapObject));
            }
            // 应用包名维度
            JSONArray appPacketList = appObject.getJSONArray(CAMPAIGN_DIM_KEY_APPPACKET);
            if (appPacketList != null && !appPacketList.isEmpty()) {
                JSONArray jsonArray = new JSONArray();

                JSONArray _1jsonArray = new JSONArray();
                JSONArray _2jsonArray = new JSONArray();
                JSONArray _6jsonArray = new JSONArray();
                JSONArray _7jsonArray = new JSONArray();
                JSONArray _8jsonArray = new JSONArray();
                JSONArray _9jsonArray = new JSONArray();

                Iterator<Object> it = appPacketList.iterator();
                while (it.hasNext()) {
                    JSONObject ob = (JSONObject) it.next();
                    String pk_name = ob.get("pk_name").toString();
                    String app_id = ob.get("app_id").toString();

                    if(StringUtils.isNotEmpty(pk_name)){
                        jsonArray.add(pk_name);
                    }
                    int channel = Integer.parseInt(ob.get("channel").toString());
                    switch (channel){
                        case 1:
                            _1jsonArray.add(app_id);
                            break;
                        case 2:
                            _2jsonArray.add(app_id);
                            break;
                        case 6:
                            _6jsonArray.add(app_id);
                            break;
                        case 7:
                            _7jsonArray.add(app_id);
                            break;
                        case 8:
                            _8jsonArray.add(app_id);
                            break;
                        case 9:
                            _9jsonArray.add(app_id);
                            break;
                    }
                }

                if(_1jsonArray.size()>0){
                    camapignDimMap.put("appid_1", JSON.toJSONString(_1jsonArray));
                }
                if(_2jsonArray.size()>0){
                    camapignDimMap.put("appid_2", JSON.toJSONString(_2jsonArray));
                }
                if(_6jsonArray.size()>0){
                    camapignDimMap.put("appid_6", JSON.toJSONString(_6jsonArray));
                }
                if(_7jsonArray.size()>0){
                    camapignDimMap.put("appid_7", JSON.toJSONString(_7jsonArray));
                }
                if(_8jsonArray.size()>0){
                    camapignDimMap.put("appid_8", JSON.toJSONString(_8jsonArray));
                }
                if(_9jsonArray.size()>0){
                    camapignDimMap.put("appid_9", JSON.toJSONString(_9jsonArray));
                }
                if(jsonArray.size()>0){
                    camapignDimMap.put("app", JSON.toJSONString(jsonArray));
                }

//                camapignDimMap.put("appInfo", JSON.toJSONString(appPacketList));
            }
        }

        // 渠道 channel
        JSONArray selectedChannelIds = new JSONArray();
        CampaignDim channelsDim = tempCampaignDims.get(CAMPAIGN_DIM_KEY_CHANNELS);
        if (channelsDim != null) {
            JSONObject channelsObject = JSON.parseObject(channelsDim.getDimValue());
            Integer type = channelsObject.getInteger("type");
            if (type == 0 || type == null) {
                List<Channel> channels = channelDao.listAll();
                JSONArray selected = new JSONArray();
                for (Channel channel : channels) {
                    selected.add(channel.getId());
                }
                selectedChannelIds.addAll(selected);
            } else {
                JSONArray selected = channelsObject.getJSONArray(CAMPAIGN_DIM_KEY_CHANNELS_SELECTED);
                selectedChannelIds.addAll(selected);
            }
        } else {
            List<Channel> channels = channelDao.listAll();
            JSONArray selected = new JSONArray();
            for (Channel channel : channels) {
                selected.add(channel.getId());
            }
            selectedChannelIds.addAll(selected);
        }
        camapignDimMap.put("channel", JSON.toJSONString(selectedChannelIds));

        JSONObject netopObject = new JSONObject();
        CampaignDim networkTypeDim = tempCampaignDims.get(CAMPAIGN_DIM_KEY_DEVICE_NETWORKTYPE);
        if (networkTypeDim != null) {
            JSONObject networkTypeObject = JSON.parseObject(networkTypeDim.getDimValue());
            JSONArray networkTypeArray = networkTypeObject.getJSONArray("list");
            netopObject.put("net", networkTypeArray);
        }
        CampaignDim operatorTypeDim = tempCampaignDims.get(CAMPAIGN_DIM_KEY_DEVICE_OPERATORTYPE);
        if (operatorTypeDim != null) {
            JSONObject operatorTypeObject = JSON.parseObject(operatorTypeDim.getDimValue());
            JSONArray operatorTypeArray = operatorTypeObject.getJSONArray("list");
            netopObject.put("op", operatorTypeArray);
        }
        if (!netopObject.isEmpty()) {
            camapignDimMap.put("netop", JSON.toJSONString(netopObject));
        }

        JSONObject deviceObject = new JSONObject();
        CampaignDim deviceTypeDim = tempCampaignDims.get(CAMPAIGN_DIM_KEY_DEVICE_DEVICETYPE);
        if (deviceTypeDim != null) {
            JSONObject deviceTypeObjcet = JSON.parseObject(deviceTypeDim.getDimValue());
            JSONArray deviceTypeArray = deviceTypeObjcet.getJSONArray("list");
            deviceObject.put("dev", deviceTypeArray);
        }
        CampaignDim osTypeDim = tempCampaignDims.get(CAMPAIGN_DIM_KEY_DEVICE_OSTYPE);
        if (osTypeDim != null) {
            JSONArray osTypeArray = new JSONArray();
            osTypeArray.add(osTypeDim.getDimValue());
            deviceObject.put("os", osTypeArray);
        }
        CampaignDim brandDim = tempCampaignDims.get(CAMPAIGN_DIM_KEY_DEVICE_BRAND);
        if (brandDim != null) {
            JSONObject brandObject = JSON.parseObject(brandDim.getDimValue());
            JSONArray brandArray = brandObject.getJSONArray("list");
            deviceObject.put("brand", brandArray);
        }
        if (deviceObject != null) {
            camapignDimMap.put("device", JSON.toJSONString(deviceObject));
        }

        return camapignDimMap;
    }

    @Override
    public void batchUpdateCampaigns(String[] campaignIds, Integer price, Long dailyBudget) {
        CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
        for (String campaignId : campaignIds) {
            Campaign campaign = campaignDao.getById(Long.parseLong(campaignId));
            if (campaign == null) {
                log.warn("推广活动[" + campaignId + "]不存在");
                continue;
            }
            if (dailyBudget != null) {
                campaign.setDailyBudget(dailyBudget);
            }
            if (price != null && campaign.getEditStepStatus() >= 2) {
                campaign.setPrice(price);
            }
            if (StringUtils.isEmpty(campaign.getWeekHour())) {
                campaign.setWeekHour(
                        "[[1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1],[1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1],[1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1],[1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1],[1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1],[1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1],[1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1]]");
            }
            campaignDao.update(campaign);
            checkCampaignStatusForUpdate(campaign.getId());
//			sendCamapaignToEngine(campaign);
        }
    }

    /**
     * 活动复制
     */
    @Override
    public void saveDuplicateCampaign(ActiveUser activeUser, Long campaignId) {

        CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
        Campaign campaign = campaignDao.getById(campaignId);
        String date = DateUtil.getDateStr(new Date(), "MMddHHmmss");

        // 复制广告数据
        CampGroup cg = campGroupService.getById(campaign.getGroupId());
        //复制广告组时,广告组名称的命名规则  xxxxx_date
        // 如果广告组名称是已复制的,则取"_"前面的名称(广告活动同理)
        String old_group_name = cg.getGroupName();
        if (old_group_name.contains("_")) {
            old_group_name = old_group_name.split("_")[0];
        }
        CampGroup newCg = new CampGroup();
        BeanUtils.copyProperties(cg, newCg);
        newCg.setId(null);

        newCg.setGroupName(old_group_name + "_" + date);
        long newCgId = campGroupService.insert(newCg);
        newCg = campGroupService.getById(newCgId);
        campGroupService.sendCampGroupMessage(newCg);

        // 复制活动基础数据
        Campaign newCampaign = new Campaign();
        BeanUtils.copyProperties(campaign, newCampaign);
        newCampaign.setId(null);
        newCampaign.setGroupId(newCgId);
        String old_camp_name = newCampaign.getCampaignName();
        if (old_camp_name.contains("_")) {
            old_camp_name = old_camp_name.split("_")[0];
        }
        newCampaign.setCampaignName(old_camp_name + "_" + date);

        // 重置状态
        Date now = new Date();
        Long loginUserId = activeUser.getUserId();
        CampaignManulStatus oldCampaignManulStatus = campaign.getManulStatus();
        CampaignManulStatus newCampaignManulStatus = CampaignManulStatus.EDIT;
//		if (!oldCampaignManulStatus.equals(CampaignManulStatus.EDIT)) {
//			newCampaignManulStatus = CampaignManulStatus.TOCOMMIT;
//		}
        newCampaign.setManulStatus(newCampaignManulStatus);
        newCampaign.setAutoStatus(CampaignAutoStatus.READY);
        newCampaign.setCreateTime(now);
        newCampaign.setCreatorId(loginUserId);
        newCampaign.setLastModifyTime(now);
        newCampaign.setModifierId(loginUserId);
        newCampaign.setEditStepStatus(2);
        newCampaign.setOrderBy(4);
        //保存活动
        Long newCampaignId = campaignDao.insert(newCampaign);

        // 复制campaigndim数据
        CampaignDim campDim = new CampaignDim();
        campDim.setCampaignId(campaignId);

        List<CampaignDim> campaignDims = campaignDimDao.listBy(campDim);
        for (CampaignDim campaignDim : campaignDims) {
            CampaignDim newCampaignDim = new CampaignDim();
            BeanUtils.copyProperties(campaignDim, newCampaignDim);
            newCampaignDim.setId(null);
            newCampaignDim.setCampaignId(newCampaignId);
            campaignDimDao.insert(newCampaignDim);
        }

        // 复制活动行业类别数据
        CampaignCategory cc = new CampaignCategory();
        cc.setCampaignId(campaignId);
        List<CampaignCategory> campCategories = campaignCategoryDao.listBy(cc);
        for (CampaignCategory campCategory : campCategories) {
            CampaignCategory newCampCategory = new CampaignCategory();
            BeanUtils.copyProperties(campCategory, newCampCategory);
            newCampCategory.setId(null);
            newCampCategory.setCampaignId(newCampaignId);
            campaignCategoryDao.insert(newCampCategory);
        }

        // 复制广告数据
        // 不再复制广告数据有可能和之前的活动有岐义 modify by duber 2016-12-8
        //adService.saveDuplicateAd(campaignId, newCampaignId);

        // 发送毛利
        Integer grossProfit = newCampaign.getGrossProfit();
        if (grossProfit != null && grossProfit > 0) {
            SetCampGrossProfitMsg setCampGrossProfitMsg = new SetCampGrossProfitMsg();
            setCampGrossProfitMsg.setCampid(newCampaignId);
            setCampGrossProfitMsg.setGrossProfit(grossProfit);
            mQConnectorService.sendMessage(setCampGrossProfitMsg);
        }
        // 发送黑名单
        CampaignDim blackDim = new CampaignDim();
        blackDim.setCampaignId(newCampaignId);
        blackDim.setDimName(CAMPAIGN_DIM_KEY_BLACKLIST);
        blackDim = campaignDimDao.selectByCampAndDimName(blackDim);
        if (blackDim != null && StringUtils.isNotEmpty(blackDim.getDimValue())) {
            String blackDimValue = blackDim.getDimValue();
            List<String> blackList = new ArrayList<String>(Arrays.asList(blackDimValue.split("\r\n")));
            SetCampBlackListMsg setCampBlackListMsg = new SetCampBlackListMsg();
            setCampBlackListMsg.setCampid(newCampaignId);
            setCampBlackListMsg.setBlackList(blackList);
            mQConnectorService.sendMessage(setCampBlackListMsg);
        }

    }

    private void deleteCamapign(Long campaignId) {

        Campaign campaign = campaignDao.getById(campaignId);
        // 逻辑删除活动
        campaign.setDeleteStatus(1);
        campaign.setManulStatus(CampaignManulStatus.DELETE);
        campaign.setLastModifyTime(new Date());
        campaignDao.update(campaign);
        /**
         * 删除活动逻辑删除,不再删除该活动的定向数据及广告数据,供以后查询但不可编辑
         */
        // 删除campaignDim
//        campaignDimDao.deleteCampaignDim(campaignId);
        // 删除渠道行业类别
//        campaignCategoryDao.deleteByCampaignId(campaignId);
        // 删除活动时向服务器发送下线消息
        ChangeManulStatusMsg changeManulStatusMsg = new ChangeManulStatusMsg();
        changeManulStatusMsg.setCampid(campaignId);
        changeManulStatusMsg.setStatus(CampaignManulStatus.OFFLINE);
        mQConnectorService.sendMessage(changeManulStatusMsg);
        // 删除对应的 广告
//        adService.deleteAdByCamapignId(campaignId);
    }

    @Override
    public void deleteCampaigns(String[] campaignIds) {
        CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
        for (String campaignId : campaignIds) {
            deleteCamapign(Long.parseLong(campaignId));
        }
    }

    @Override
    public void exportCampaign(List<Map<String, Object>> listCampaigns, HttpServletResponse response) {
        Map<String, List<String[]>> sheets = getSheets(listCampaigns);
        File excelFile = null;
        try {
            String baseDir = System.getProperty("java.io.tmpdir");
            excelFile = new File(baseDir + "/" + System.currentTimeMillis() + ".xlsx");
            ExcelUtil.create07ExcelFile(excelFile, sheets);
            FileUtil.downloadFile(response, "广告位列表.xlsx", excelFile);
        } catch (IOException e) {
//			log.error(e.getMessage(), e);
        } finally {
            if (excelFile != null && excelFile.exists()) {
                try {
                    excelFile.delete();
                } catch (Exception e) {
                }
            }
        }
    }

    /**
     * 查询该活动下的投放定向数据
     *
     * @param model
     * @param cd
     */
    @Override
    public void findCampDimData(Model model, CampaignDim cd) {
        List<CampaignDim> listCampDim = campaignDimDao.listBy(cd);
        if (listCampDim.size() > 0) {
            for (CampaignDim campDim : listCampDim) {
                String dimValue = campDim.getDimValue();
                String dimName = campDim.getDimName();
                JSONObject jsonObj = null;
                switch (dimName) {
                    case "channels": //渠道定向
                        jsonObj = JSONObject.parseObject(dimValue);
                        if (jsonObj.containsKey("selected") && jsonObj.containsKey("type")) {
                            String selectVal = jsonObj.getString("selected");
                            String typeVal = jsonObj.getString("type");
                            if ("1".equals(typeVal)) {
                                selectVal = selectVal.substring(1, selectVal.length() - 1);
                                String[] ids = selectVal.split(",");
                                model.addAttribute("channel_sel_value", ids);
                                model.addAttribute("channel_sel", selectVal);
                            }
                        }
                        break;
                    case "screen": //屏数定向
                        jsonObj = JSONObject.parseObject(dimValue);
                        if (jsonObj.containsKey("type")) {
                            String typeVal = jsonObj.getString("type");
                            model.addAttribute("screen_sel_value", typeVal);
                        }
                        break;
                    case "media": //媒体定向
                        jsonObj = JSONObject.parseObject(dimValue);
                        if (jsonObj.containsKey("type")) {
                            String typeVal = jsonObj.getString("type");
                            model.addAttribute("media_type_value", typeVal);
                        }
                        break;
                    case "app": //应用定向
                        jsonObj = JSONObject.parseObject(dimValue);
                        if (jsonObj.containsKey("type")) {
                            String typeVal = jsonObj.getString("type");
                            model.addAttribute("appSelectValue", typeVal);
                            if("1".equals(typeVal)){
                                model.addAttribute("appData", jsonObj.getString("appType"));
                            }else if("2".equals(typeVal)){
                                String appDataStr = jsonObj.getString("appPacket");
//                                appDataStr = appDataStr.substring(1, appDataStr.length() - 1);
//                                String[] appData = appDataStr.split(",");
                                model.addAttribute("appData", appDataStr);
                            }
                        }
                        break;
                    case "freq": //投放频次控制
                        jsonObj = JSONObject.parseObject(dimValue);
                        if (jsonObj.containsKey("type")) {
                            String typeVal = jsonObj.getString("type");
                            if ("1".equals(typeVal)) {
                                model.addAttribute("freq_type", 1);

                                if (jsonObj.containsKey("timeInterval")) {
                                    model.addAttribute("timeInterval", jsonObj.getString("timeInterval"));
                                }
                                if (jsonObj.containsKey("freqType")) {
                                    model.addAttribute("freqType", jsonObj.getString("freqType"));
                                }
                                if (jsonObj.containsKey("freqValue")) {
                                    model.addAttribute("freqValue", jsonObj.getString("freqValue"));
                                }
                            }
                        }
                        break;
                    case "areas": //地域定向
                        jsonObj = JSONObject.parseObject(dimValue);
                        if (jsonObj.containsKey("citys") && jsonObj.containsKey("type")) {
                            String selectVal = jsonObj.getString("citys");
                            selectVal = selectVal.substring(1, selectVal.length() - 1);
                            String typeVal = jsonObj.getString("type");
                            if ("1".equals(typeVal)) {
                                model.addAttribute("areas_sel_value", selectVal);
                            }
                        }
                        break;
                    case "populations": //人群属性数据
//						jsonObj = JSONObject.parseObject(dimValue);
                        dimValue = dimValue.substring(1, dimValue.length() - 1);
                        model.addAttribute("populations", dimValue);
                        break;
                    case "matchType":  //人口定向匹配模式
                        model.addAttribute("matchType", dimValue);
                        break;
                    case "instresting": //兴趣定向
                        dimValue = dimValue.substring(1, dimValue.length() - 1);
                        model.addAttribute("instresting_sel", dimValue);
                        break;
                    case "cookiePacket": //个性人群包定向
                        dimValue = dimValue.substring(1, dimValue.length() - 1);
                        model.addAttribute("cookiePacket_sel", dimValue);
                        break;
                    case "retargetPacket": //访客找回
                        dimValue = dimValue.substring(1, dimValue.length() - 1);
                        model.addAttribute("retargetPacket_sel", dimValue);
                        break;
                    case "osType": //操作系统
                        model.addAttribute("osTypeSet", dimValue);
                        break;
                    case "deviceType": //设备
                        jsonObj = JSONObject.parseObject(dimValue);
                        if (jsonObj.containsKey("type")) {
                            String typeVal = jsonObj.getString("type");
                            if ("1".equals(typeVal)) {
                                String list = jsonObj.getString("list");
                                list = list.substring(1,list.length()-1).replaceAll("\"","");
                                model.addAttribute("deviceTypeSet", list);
                            }
                        }
                        break;
                    case "brand": //设备品牌
                        jsonObj = JSONObject.parseObject(dimValue);
                        if (jsonObj.containsKey("type")) {
                            String typeVal = jsonObj.getString("type");
                            if ("1".equals(typeVal)) {
                                String list = jsonObj.getString("list");
                                list = list.substring(1,list.length()-1).replaceAll("\"","");
                                model.addAttribute("brandSet", list);
                            }
                        }
                        break;
                    case "networkType": //网络类型
                        jsonObj = JSONObject.parseObject(dimValue);
                        if (jsonObj.containsKey("type")) {
                            String typeVal = jsonObj.getString("type");
                            if ("1".equals(typeVal)) {
                                String list = jsonObj.getString("list");
                                list = list.substring(1,list.length()-1).replaceAll("\"","");
                                model.addAttribute("networkTypeSet", list);
                            }
                        }
                        break;
                    case "operatorType": //网络运营商
                        jsonObj = JSONObject.parseObject(dimValue);
                        if (jsonObj.containsKey("type")) {
                            String typeVal = jsonObj.getString("type");
                            if ("1".equals(typeVal)) {
                                String list = jsonObj.getString("list");
                                list = list.substring(1,list.length()-1).replaceAll("\"","");
                                model.addAttribute("operateTypeSet", list);
                            }
                        }
                        break;
                }

            }
        }
    }

    /**
     * 查询并封装数据集合 第三步
     *
     * @param model
     * @param camp
     */
    @Override
    public void queryAndPackageData(Model model, Campaign camp) {
        //1. 创意尺寸集合
        CampaignDim cd = new CampaignDim();
        cd.setCampaignId(camp.getId());
        cd.setDimName("channels");
        CampaignDim campDim = campaignDimDao.selectByCampAndDimName(cd);
        Map<String, Object> map = new HashMap<String, Object>();
        if (campDim != null) {
            String dimValue = campDim.getDimValue();
            JSONObject jsonObj = JSONObject.parseObject(dimValue);
            String selectVal = jsonObj.getString("selected");
            String typeVal = jsonObj.getString("type");

            if ("1".equals(typeVal)) {
                selectVal = selectVal.substring(1, selectVal.length() - 1);
                String[] ids = selectVal.split(",");
                map.put("channel_ids", ids);
            } else {
                List<Channel> listChannel = channelDao.listAll();
                String[] ids = new String[listChannel.size()];
                Channel ch = null;
                for (int i = 0; i < listChannel.size(); i++) {
                    ch = listChannel.get(i);
                    ids[i] = ch.getId().toString();
                }
                map.put("channel_ids", ids);
            }
            map.put("adType", camp.getAdType());

            List<Map<String, Object>> listCSF = frontCreativeSizeFlowService.selectListByMap(map);
            if (listCSF.size() > 0) {
                Map<String, Object> sumMap = frontCreativeSizeFlowService.selectSumFlowByMap(map);
                double sumFlow = 999999999;
                if (sumMap.get("flow") != null) {
                    sumFlow = Double.parseDouble(sumMap.get("flow").toString());
                }
                for (Map<String, Object> mobj : listCSF) {
                    double flow = Double.parseDouble(mobj.get("flow").toString());
                    String result = String.format("%.4f", flow / sumFlow);
                    mobj.put("cp", result);
                }
                model.addAttribute("listCSF", listCSF);
                model.addAttribute("sumFlow", sumFlow);
            }
        }
        //2. 查询该活动下面的素材列表
        Map<String, Object> adMap = new HashMap<String, Object>();
        adMap.put("campaign_id", camp.getId());
        List<Map<String, Object>> creativeList = this.findCreativesByCampId(adMap);
        if (creativeList.size() > 0) {
            model.addAttribute("creativeList", creativeList);
        }
        //3. 创意行业类型
        adMap = new HashMap<String, Object>();
        adMap.put("campaign_id", camp.getId());
        adMap.put("channel_ids", map.get("channel_ids"));
        List<Map<String, Object>> adCatgList = campaignCategoryDao.selectChannelCatgByCampId(adMap);
        model.addAttribute("adCatgList", adCatgList);
        //活动下面渠道设置的行业类型基础数据
        Map<CatgSerial, List<ChannelCategory>> channelDIC = DicController.getChannelCategorysList();
        model.addAttribute("dspChannelDIC", channelDIC.get(CatgSerial.DSP));
        model.addAttribute("tanxChannelDIC", channelDIC.get(CatgSerial.TANX));
        model.addAttribute("besChannelDIC", channelDIC.get(CatgSerial.BES));
        model.addAttribute("vamChannelDIC", channelDIC.get(CatgSerial.VAM));
        model.addAttribute("tencentChannelDIC", channelDIC.get(CatgSerial.TENCENT));
        model.addAttribute("xtraderChannelDIC", channelDIC.get(CatgSerial.XTRADER));

    }

    @Override
    public Map<String, Object> isDelivery(ActiveUser activeUser, String[] campaignIds) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        String retStr = "";
        long partnerId = activeUser.getFavPartner().getId();
        boolean result_flag = false;
        /** 查询该广告主下面的渠道是否可以投放广告活动 */
        List<Long> listChannel = partnerService.checkPartnerWhenStartCampaign(partnerId);

        //查询投放活动选择的渠道
        CampaignCategory cc = null;
        List<CampaignCategory> listCC = null;

        for (String camp_id : campaignIds) {
            long id = Long.parseLong(camp_id);
            cc = new CampaignCategory();
            cc.setCampaignId(id);
            listCC = campaignCategoryDao.listBy(cc);  /** 获取添加广告活动时设置的投放渠道列表  */
            String cStr = "";
            for (CampaignCategory obj : listCC) {
                long ch_id = CatgSerial.fromCode(obj.getCatgserial()).getChannelId();
                boolean check_flag = listChannel.contains(ch_id); /** 判断设置的投放渠道，渠道方是否审核通过 */
                if (!check_flag) {
                    cStr += obj.getCatgserial() + ",";
                }
            }
            if (cStr.length() > 0) {
                cStr = cStr.substring(0, cStr.length() - 1);
                retStr += "<br/>广告活动ID[" + id + "][" + cStr + "]";
            }
        }

        if (retStr.length() > 0) {
            retStr = "广告活动下面的渠道暂时不能投放,请确认是否继续投放! 详情如下: " + retStr;
            resultMap.put("message", retStr);
            resultMap.put("success", false);
        } else {
            resultMap.put("success", true);
        }
        return resultMap;
    }

    /**
     * 初始化投放基础数据
     * @param camp
     * @param activeUser
     */
    @Override
    public void initDirectData(Model model,Campaign camp, ActiveUser activeUser) {

        //交易渠道
        List<Channel> listChannel = channelDao.listAll();
        if(listChannel.size()>0) {
            model.addAttribute("listChannel", listChannel);
        }

        //个性化人群包
        CookiePacket cp = new CookiePacket();
        cp.setIsPublic(1);
        List<CookiePacket> listCP =frontCookiePacketService.listBy(cp);
        if(listCP.size()>0) {
            model.addAttribute("cookiePacketPublic", listCP);// 公共人群包
        }
        cp.setIsPublic(0);
        cp.setOwnerPartnerId(activeUser.getFavPartner().getId());
        listCP =frontCookiePacketService.listBy(cp);
        if(listCP.size()>0) {
            model.addAttribute("cookiePacketPersonal", listCP);// 私有人群包
        }

        //访客找回
        List<RetargetPacket> listRP = frontRetargetPacketService.listByPartnerId(activeUser.getFavPartner().getId());
        if(listRP.size()>0) {
            model.addAttribute("retargetPacketList", listRP);
        }

        //设备
        EnumSet<DeviceType> deviceTypeList = EnumSet.allOf(DeviceType.class);
        model.addAttribute("deviceTypeList",deviceTypeList);

        //网络运营商
        EnumSet<OperatorType> operatorTypeList = EnumSet.allOf(OperatorType.class);
        model.addAttribute("operatorTypeList",operatorTypeList);

        //网络类型
        EnumSet<NetWorkType> netWorkTypeList = EnumSet.allOf(NetWorkType.class);
        model.addAttribute("netWorkTypeList",netWorkTypeList);
    }

    /**
     * 投放指标
     */
    private static final String[] CAMPAIGN = new String[]{"活动ID", "活动名称", "类 型", "状 态", "结算方式", "日预算"};
    /**
     * 投放指标
     */
    private static final String[] PUB = new String[]{"展现量", "点击量", "点击率", "投放CPM", "投放CPC", "投放花费"};

    /**
     * 成本指标
     */
    //private static final String[] COST = new String[] { "参与竞价量", "点击访客数", "竞价胜出率", "成本花费", "成本CPM", "成本CPC", "毛利率", "毛利额" };
    private String[] getTitle() {
        List<String> titles = new ArrayList<String>();
        titles.addAll(Arrays.asList(CAMPAIGN));
        titles.addAll(Arrays.asList(PUB));
        //titles.addAll(Arrays.asList(COST));

        return titles.toArray(new String[titles.size()]);
    }

    private Map<String, List<String[]>> getSheets(List<Map<String, Object>> listCampaigns) {
        List<String[]> rows = new ArrayList<String[]>();
        rows.add(getTitle());
        String[] row = null;
        DecimalFormat pc = new DecimalFormat("0.00%");
        NumberFormat df = new DecimalFormat("0.00");
        for (Map<String, Object> camp : listCampaigns) {
            row = new String[getTitle().length];
            int i = 0;
            row[i++] = camp.get("camp_id").toString();
            row[i++] = camp.get("camp_name").toString();
            row[i++] = camp.get("camp_type").toString();
            row[i++] = camp.get("campaign_status").toString();
            row[i++] = camp.get("expend_type").toString();
            Integer daily_budget = camp.get("daily_budget") == null ? 0 : Integer.parseInt(camp.get("daily_budget").toString());
            row[i++] = df.format(daily_budget / 100);

            Integer pv = camp.get("pv") == null ? 0 : Integer.parseInt(camp.get("pv").toString());
            row[i++] = pv.toString();

            Integer click = camp.get("click") == null ? 0 : Integer.parseInt(camp.get("click").toString());
            row[i++] = click.toString();

            Integer expend = camp.get("expend") == null ? 0 : Integer.parseInt(camp.get("expend").toString());
            row[i++] = pc.format(pv == 0 ? 0 : click / pv);
            row[i++] = pc.format(pv == 0 ? 0 : expend / pv * 1000);
            row[i++] = pc.format(click == 0 ? 0 : expend / click);
            row[i++] = df.format(expend);

            rows.add(row);
        }
        Map<String, List<String[]>> sheets = new HashMap<String, List<String[]>>();
        sheets.put("广告活动", rows);
        return sheets;
    }

}
