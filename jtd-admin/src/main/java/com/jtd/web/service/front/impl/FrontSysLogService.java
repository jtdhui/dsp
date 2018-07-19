package com.jtd.web.service.front.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jtd.commons.dao.BaseDao;
import com.jtd.commons.mybatis.support.CustomerContextHolder;
import com.jtd.utils.DateUtil;
import com.jtd.web.constants.*;
import com.jtd.web.controller.DicController;
import com.jtd.web.dao.ICookiePacketDao;
import com.jtd.web.dao.IRetargetPacketDao;
import com.jtd.web.dao.ISysLogDao;
import com.jtd.web.po.*;
import com.jtd.web.service.front.IFrontCampDimService;
import com.jtd.web.service.front.IFrontSysLogService;
import com.jtd.web.service.impl.BaseService;
import com.jtd.web.vo.AppType;
import com.jtd.web.vo.City;
import com.jtd.web.vo.WebsiteType;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by duber on 2017/4/18.
 */
@Service
public class FrontSysLogService extends BaseService<SysLog> implements IFrontSysLogService {

    private static final String JSON_BEFORE = "before";
    private static final String JSON_AFTER = "after";
    private static final String JSON_DESC = "desc";

    @Autowired
    private ISysLogDao sysLogDao;

    @Autowired
    private IFrontCampDimService campDimService;

    @Autowired
    private ICookiePacketDao cookiePacketDao;

    @Autowired
    private IRetargetPacketDao retargetPacketDao;

    @Override
    protected BaseDao<SysLog> getDao() {
        CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
        return sysLogDao;
    }

    @Override
    public void saveCampaignStep1(ActiveUser activeUser,Campaign campaign, Campaign oldCampaign) {
        CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
        SysLog sysLog = new SysLog();
        sysLog.setBusinessId(campaign.getId());
        sysLog.setCreatorId(activeUser.getUserId());
        sysLog.setName("广告活动-第一步");
        sysLog.setType(2);
        sysLog.setOperateType(2);
        sysLog.setCreateTime(new Date());
        sysLog.setRemark("广告活动-第一步");

        boolean flag = false;
        String logStr = "";
        JSONObject jsonObject = new JSONObject();
        JSONObject beforeJson = new JSONObject();
        JSONObject afterJson = new JSONObject();
        JSONObject logJson = new JSONObject();

        beforeJson.put("campaignName",oldCampaign.getCampaignName());
        afterJson.put("campaignName",campaign.getCampaignName());
        if(oldCampaign.getCampaignName().equals(campaign.getCampaignName()) == false){
            flag = true;
            logStr = oldCampaign.getCampaignName()+"->"+campaign.getCampaignName();
            logJson.put("活动名称",logStr);
        }

        beforeJson.put("campaignType",oldCampaign.getCampaignType());
        afterJson.put("campaignType",campaign.getCampaignType());
        if(oldCampaign.getCampaignType().equals(campaign.getCampaignType()) == false){
            flag = true;
            logStr = ""+ CampaignType.getName(oldCampaign.getCampaignType())+"->"+CampaignType.getName(campaign.getCampaignType());
            logJson.put("活动类型",logStr);
        }

        beforeJson.put("adType",oldCampaign.getAdType());
        afterJson.put("adType",campaign.getAdType());
        if(oldCampaign.getAdType().equals(campaign.getAdType()) == false){
            flag = true;
            logStr = ""+ AdType.getAdType(oldCampaign.getAdType()).getDesc()+"->"+AdType.getAdType(campaign.getAdType()).getDesc();
            logJson.put("广告形式",logStr);
        }

        beforeJson.put("budgetctrltype",oldCampaign.getBudgetctrltype());
        afterJson.put("budgetctrltype",campaign.getBudgetctrltype());
        if(oldCampaign.getBudgetctrltype().equals(campaign.getBudgetctrltype()) == false){
            flag = true;
            String begin = oldCampaign.getBudgetctrltype().intValue() == 0 ? "标准":"尽速";
            String end = campaign.getBudgetctrltype().intValue() == 0 ? "标准":"尽速";
            logStr = ""+begin+"->"+end;
            logJson.put("预算控制",logStr);
        }

        if(!StringUtils.isEmpty(oldCampaign.getStartTime())){
            beforeJson.put("startTime",DateUtil.getDateStr(oldCampaign.getStartTime(),"yyyy-MM-dd"));
        }else {
            beforeJson.put("startTime",null);
        }

        if(!StringUtils.isEmpty(campaign.getStartTime())){
            afterJson.put("startTime",DateUtil.getDateStr(campaign.getStartTime(),"yyyy-MM-dd"));
        }else {
            afterJson.put("startTime",null);
        }

        if(!StringUtils.isEmpty(oldCampaign.getStartTime()) && !StringUtils.isEmpty(campaign.getStartTime())){
            if(oldCampaign.getStartTime().equals(campaign.getStartTime()) == false){
                flag = true;
                logStr = ""+ DateUtil.getDateStr(oldCampaign.getStartTime(), "yyyy-MM-dd")+"->"+DateUtil.getDateStr(campaign.getStartTime(),"yyyy-MM-dd");
                logJson.put("开始时间",logStr);
            }
        }else if(!StringUtils.isEmpty(oldCampaign.getStartTime())){
            flag = true;
            logStr = ""+DateUtil.getDateStr(oldCampaign.getStartTime(),"yyyy-MM-dd")+"->空";
            logJson.put("开始时间",logStr);
        }else if(!StringUtils.isEmpty(campaign.getStartTime())){
            flag = true;
            logStr = "空->"+DateUtil.getDateStr(campaign.getStartTime(),"yyyy-MM-dd");
            logJson.put("开始时间",logStr);
        }

        if(!StringUtils.isEmpty(oldCampaign.getEndTime())){
            beforeJson.put("endTime",DateUtil.getDateStr(oldCampaign.getEndTime(),"yyyy-MM-dd"));
        }else {
            beforeJson.put("endTime",null);
        }

        if(!StringUtils.isEmpty(campaign.getEndTime())){
            afterJson.put("endTime",DateUtil.getDateStr(campaign.getEndTime(),"yyyy-MM-dd"));
        }else {
            afterJson.put("endTime",null);
        }

        if(!StringUtils.isEmpty(oldCampaign.getEndTime()) && !StringUtils.isEmpty(campaign.getEndTime())){
            if(oldCampaign.getEndTime().equals(campaign.getEndTime()) == false){
                flag = true;
                logStr = ""+DateUtil.getDateStr(oldCampaign.getEndTime(),"yyyy-MM-dd")+"->"+DateUtil.getDateStr(campaign.getEndTime(),"yyyy-MM-dd");
                logJson.put("结束时间",logStr);
            }
        }else if(!StringUtils.isEmpty(oldCampaign.getEndTime())){
            flag = true;
            logStr = ""+DateUtil.getDateStr(oldCampaign.getEndTime(),"yyyy-MM-dd")+"->空";
            logJson.put("结束时间",logStr);
        }else if(!StringUtils.isEmpty(campaign.getEndTime())){
            flag = true;
            logStr = "空->"+DateUtil.getDateStr(campaign.getEndTime(),"yyyy-MM-dd");
            logJson.put("结束时间",logStr);
        }

        // 投放时间段
        beforeJson.put("weekHour",oldCampaign.getWeekHour());
        afterJson.put("weekHour",campaign.getWeekHour());
        if(!StringUtils.isEmpty(oldCampaign.getWeekHour()) && !StringUtils.isEmpty(campaign.getWeekHour())){
            if(oldCampaign.getWeekHour().equals(campaign.getWeekHour()) == false){
                flag = true;
                logJson.put("weekHour",0);
//                logStr = "->投放时间段:"+oldCampaign.getWeekHour()+"->"+campaign.getWeekHour();
            }
        }else if(!StringUtils.isEmpty(oldCampaign.getWeekHour())){
            flag = true;
            logJson.put("weekHour",1);
//            logStr = "->投放时间段:"+oldCampaign.getWeekHour()+"->空";
        }else if(StringUtils.isEmpty(campaign.getWeekHour()) == false){
            flag = true;
            logJson.put("weekHour",2);
//            logStr = "->投放时间段:空->"+campaign.getWeekHour();
        }
        DecimalFormat decimalFormat = new DecimalFormat("######.00");
        if(!StringUtils.isEmpty(oldCampaign.getDailyBudget())){
            beforeJson.put("dailyBudget",decimalFormat.format(oldCampaign.getDailyBudget()/100));
        }else {
            beforeJson.put("dailyBudget",null);
        }
        if(!StringUtils.isEmpty(campaign.getDailyBudget())){
            afterJson.put("dailyBudget", decimalFormat.format(campaign.getDailyBudget()/100));
        }else {
            afterJson.put("dailyBudget",null);
        }

        if(!StringUtils.isEmpty(oldCampaign.getDailyBudget()) && !StringUtils.isEmpty(campaign.getDailyBudget())){
            if(oldCampaign.getDailyBudget().equals(campaign.getDailyBudget()) == false){
                flag = true;
                logStr = ""+decimalFormat.format(oldCampaign.getDailyBudget()/100)+"->"+decimalFormat.format(campaign.getDailyBudget()/100);
                logJson.put("每日预算",logStr);
            }
        }else if(!StringUtils.isEmpty(oldCampaign.getDailyBudget())){
            flag = true;
            logStr = ""+decimalFormat.format(oldCampaign.getDailyBudget()/100)+"->不限预算";
            logJson.put("每日预算",logStr);
        }else if(StringUtils.isEmpty(campaign.getDailyBudget()) == false){
            flag = true;
            logStr = "不限预算->"+decimalFormat.format(campaign.getDailyBudget()/100);
            logJson.put("每日预算",logStr);
        }

        jsonObject.put(JSON_BEFORE, beforeJson);
        jsonObject.put(JSON_AFTER, afterJson);
        jsonObject.put(JSON_DESC, logJson.toJSONString());
        sysLog.setOperateDesc(jsonObject.toJSONString());

        if(flag) {
            sysLogDao.insert(sysLog);
        }

    }

    @Override
    public void saveCampaignStep2(ActiveUser activeUser,Campaign campaign, Campaign oldCampaign, List<CampaignDim> listCD) {
        CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);

        String logStr = "";
        JSONObject jsonObject = new JSONObject();
        JSONObject beforeJson = new JSONObject();
        JSONObject afterJson = new JSONObject();
        JSONObject logJson = new JSONObject();

        SysLog sysLog = new SysLog();
        sysLog.setBusinessId(campaign.getId());
        sysLog.setCreatorId(activeUser.getUserId());
        sysLog.setName("广告活动-第二步");
        sysLog.setType(2);
        sysLog.setOperateType(2);
        sysLog.setCreateTime(new Date());
        sysLog.setRemark("广告活动-第二步");

        // 最高限价
        DecimalFormat decimalFormat = new DecimalFormat("######.00");
        if(oldCampaign.getPrice() != null && campaign.getPrice().equals(oldCampaign.getPrice()) == false){
            logStr = decimalFormat.format(oldCampaign.getPrice()/100) + "->" + decimalFormat.format(campaign.getPrice()/100) ;
            logJson.put("最高限价",logStr);
        }

        //计费方式
        if(oldCampaign.getExpendType() != null && campaign.getExpendType().equals(oldCampaign.getExpendType()) == false){
            String begin = oldCampaign.getExpendType().intValue() == 0 ? "CPM":"CPC";
            String end = campaign.getExpendType().intValue() == 0 ? "CPM":"CPC";
            logStr = ""+begin+"->"+end;
            logJson.put("计费方式",logStr);
        }

        CampaignDim campaignDim = new CampaignDim();
        campaignDim.setCampaignId(campaign.getId());
        List<CampaignDim> oldCampaignDims = campDimService.listBy(campaignDim);
        Map<String,Object> dimMap = campDimKeyMaps();

        boolean saveFlag = false;
        for(CampaignDim old: oldCampaignDims) {
            boolean flag = false;
            for (CampaignDim cd : listCD) {
                if (old.getDimName().equals(cd.getDimName())) {
                    flag = true;
                    if(!old.getDimValue().equals(cd.getDimValue())){ //活动定向策略发生变化则记录日志
                        saveFlag = true;
                        beforeJson.put(old.getDimName(),old.getDimValue());
                        afterJson.put(old.getDimName(),cd.getDimValue());
                        logStr = CampaignDimTransfor(old.getDimName(),old.getDimValue(),cd.getDimValue());
                        logJson.put(dimMap.get(old.getDimName()).toString(),logStr);
                    }
                    break;
                }
            }

            if(!flag){ //如果修改之前的活动定向在新的定向中不存在,则记录
                saveFlag = true;
                beforeJson.put(old.getDimName(),old.getDimValue());
                afterJson.put(old.getDimName(),"");
                logStr = CampaignDimTransfor(old.getDimName(),old.getDimValue(),null);
                logJson.put(dimMap.get(old.getDimName()).toString(),logStr);
            }
        }

        for (CampaignDim cd : listCD) {
            boolean flag = false;
            for(CampaignDim old: oldCampaignDims) {
                if (old.getDimName().equals(cd.getDimName())) {
                    flag = true;
                    break;
                }
            }
            if(!flag){ //如果修改之后的活动定向在以前的定向中不存在,则记录
                saveFlag = true;
                beforeJson.put(cd.getDimName(),"");
                afterJson.put(cd.getDimName(),cd.getDimValue());
                logStr = CampaignDimTransfor(cd.getDimName(),null,cd.getDimValue());
                logJson.put(dimMap.get(cd.getDimName()).toString(),logStr);
            }
        }

        jsonObject.put(JSON_BEFORE,beforeJson);
        jsonObject.put(JSON_AFTER,afterJson);
        jsonObject.put(JSON_DESC,logJson.toJSONString());

        sysLog.setOperateDesc(jsonObject.toJSONString());

        if(saveFlag) {
            sysLogDao.insert(sysLog);
        }
    }

    /**
     * 编码解析
     * @param dimName
     * @param oldDimValue
     * @param newDimValue
     */
    private String CampaignDimTransfor(String dimName, String oldDimValue, String newDimValue) {

        String logStr = "";
        String beforeStr = "";
        String afterStr = "";

        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();

        switch (dimName){
            case "channels": //渠道
                beforeStr = channel_trans(oldDimValue);
                afterStr = channel_trans(newDimValue);
                if(beforeStr.equals(afterStr) == false) {
                    logStr = beforeStr + "->" + afterStr;
                }
                break;

            case "areas": //地域定向
                Map<Integer, City> areasMap = new HashMap<Integer,City>();
                if(session.getAttribute("areas") != null) {
                    areasMap = (Map<Integer, City>) session.getAttribute("areas");
                }else {
                    areasMap = DicController.getAllCities();
                    session.setAttribute("areas",areasMap);
                }
                beforeStr = area_trans(oldDimValue,areasMap);
                afterStr = area_trans(newDimValue,areasMap);
                if(beforeStr.equals(afterStr) == false) {
                    logStr = beforeStr + "->" + afterStr;
                }
                break;

            case "media": //媒体定向

                Map<String, WebsiteType> websiteTypeMap = new HashMap<String,WebsiteType>();
                if(session.getAttribute("websiteType") != null) {
                    websiteTypeMap = (Map<String, WebsiteType>) session.getAttribute("websiteType");
                }else {
                    websiteTypeMap = DicController.getAllWebSiteTypes();
                    session.setAttribute("websiteType",websiteTypeMap);
                }
                beforeStr = media_trans(oldDimValue,websiteTypeMap);
                afterStr = media_trans(newDimValue,websiteTypeMap);
                if(beforeStr.equals(afterStr) == false) {
                    logStr = beforeStr + "->" + afterStr;
                }
                break;

            case "app": //应用定向

                Map<String, AppType> appMap = new HashMap<String,AppType>();
                if(session.getAttribute("app") != null) {
                    appMap = (Map<String, AppType>) session.getAttribute("app");
                }else {
                    appMap = DicController.getAllAppTypes();
                    session.setAttribute("app",appMap);
                }
                beforeStr = app_trans(oldDimValue,appMap);
                afterStr = app_trans(newDimValue,appMap);
                if(beforeStr.equals(afterStr) == false) {
                    logStr = beforeStr + "->" + afterStr;
                }
                break;

            case "populations": //人群定向
                Map<Long, CookieGid> map = new HashMap<Long,CookieGid>();
                if(session.getAttribute("populations") != null) {
                    map = (Map<Long, CookieGid>) session.getAttribute("populations");
                }else {
                    map = DicController.getAllPopulations();
                    session.setAttribute("populations",map);
                }
                beforeStr = cookieGid_trans(oldDimValue,map);
                afterStr = cookieGid_trans(newDimValue,map);
                if(beforeStr.equals(afterStr) == false) {
                    logStr = beforeStr + "->" + afterStr;
                }
                break;
            case "instresting": //兴趣定向
                map = new HashMap<Long,CookieGid>();
                if(session.getAttribute("instresting") != null) {
                    map = (Map<Long, CookieGid>) session.getAttribute("instresting");
                }else {
                    map = DicController.getAllInstresting();
                    session.setAttribute("instresting",map);
                }
                beforeStr = cookieGid_trans(oldDimValue,map);
                afterStr = cookieGid_trans(newDimValue,map);
                if(beforeStr.equals(afterStr) == false) {
                    logStr = beforeStr + "->" + afterStr;
                }
                break;
            case "cookiePacket": //个性化人群定向
                CookiePacket cp = new CookiePacket();
                List<CookiePacket> listCP =cookiePacketDao.listBy(cp);
                Map<Long,String> cookieMap = new HashMap<Long,String>();
                if(listCP.size()>0){
                    for(CookiePacket entity:listCP){
                        cookieMap.put(entity.getId(),entity.getPacketName());
                    }
                    beforeStr = map_trans(oldDimValue,cookieMap);
                    afterStr = map_trans(newDimValue,cookieMap);
                    if(beforeStr.equals(afterStr) == false) {
                        logStr = beforeStr + "->" + afterStr;
                    }
                }
                break;

            case "retargetPacket": //访客找回
                List<RetargetPacket> listRP = retargetPacketDao.listBy(new RetargetPacket());
                Map<Long,String> rpMap = new HashMap<Long,String>();
                if(listRP.size()>0){
                    for(RetargetPacket rp: listRP){
                        rpMap.put(rp.getCookieGid(),rp.getPacketName());
                    }
                    beforeStr = map_trans(oldDimValue,rpMap);
                    afterStr = map_trans(newDimValue,rpMap);
                    if(beforeStr.equals(afterStr) == false) {
                        logStr = beforeStr + "->" + afterStr;
                    }
                }
                break;
            case "freq": // 投放频次
                beforeStr = freq_trans(oldDimValue);
                afterStr = freq_trans(newDimValue);
                if(beforeStr.equals(afterStr) == false) {
                    logStr = beforeStr + "->" + afterStr;
                }
                break;
            case "screen": // 屏数控制
                beforeStr = screen_trans(oldDimValue);
                afterStr = screen_trans(newDimValue);
                if(beforeStr.equals(afterStr) == false) {
                    logStr = beforeStr + "->" + afterStr;
                }
                break;
            case "osType": // 操作系统
                beforeStr = osType_trans(oldDimValue);
                afterStr = osType_trans(newDimValue);
                if(beforeStr.equals(afterStr) == false) {
                    logStr = beforeStr + "->" + afterStr;
                }
                break;
            case "deviceType": // 设备
                beforeStr = device_trans(oldDimValue,"deviceType");
                afterStr = device_trans(newDimValue,"deviceType");
                logStr = beforeStr + "->" + afterStr;
                break;
            case "brand": // 设备品牌
                beforeStr = device_trans(oldDimValue,"brand");
                afterStr = device_trans(newDimValue,"brand");
                if(beforeStr.equals(afterStr) == false) {
                    logStr = beforeStr + "->" + afterStr;
                }
                break;
            case "operatorType": // 网络运营商
                beforeStr = device_trans(oldDimValue,"operatorType");
                afterStr = device_trans(newDimValue,"operatorType");
                if(beforeStr.equals(afterStr) == false) {
                    logStr = beforeStr + "->" + afterStr;
                }
                break;
            case "networkType": // 网络类型
                beforeStr = device_trans(oldDimValue,"networkType");
                afterStr = device_trans(newDimValue,"networkType");
                logStr = beforeStr + "->" + afterStr;
                break;
            case "matchType": //人口属性标签
                beforeStr = matchType_trans(oldDimValue);
                afterStr = matchType_trans(newDimValue);
                if(beforeStr.equals(afterStr) == false) {
                    logStr = beforeStr + "->" + afterStr;
                }
                break;

        }
        return logStr;
    }

    /**
     * 人口属性标签
     * @param dim
     * @return
     */
    private String matchType_trans(String dim) {
        String ret = "";
        if(StringUtils.isEmpty(dim)){
            ret = "或";
        }else {
            ret = "or".equals(dim) ? "或":"与";
        }
        return ret;
    }

    /**
     * 设备定向
     * @param dimValue
     * @return
     */
    private String device_trans(String dimValue,String dimName) {
        String ret = "";
        JSONObject dim = null;
        if(StringUtils.isEmpty(dimValue) == false) {
            dim = JSONObject.parseObject(dimValue);
        }
        if(dim == null){
            ret = "不限";
        }else {
            Integer type = dim.getInteger("type");
            if (type == 0 || type == null) {
                ret = "不限";
            } else {
                JSONArray selected = dim.getJSONArray("list");
                Iterator<Object> it = selected.iterator();
                while (it.hasNext()) {
                    switch (dimName){
                        case "deviceType":
                            Integer deviceTypeid = (Integer) it.next();
                            ret += DeviceType.fromCode(deviceTypeid).getDesc() + ",";
                            break;
                        case "brand":
                            String brandId = (String) it.next();
                            ret += BrandType.fromCode(brandId).getBrandName() + ",";
                            break;
                        case "operatorType":
                            Integer operatorTypeId = (Integer) it.next();
                            ret += OperatorType.fromCode(operatorTypeId).getDesc() + ",";
                            break;
                        case "networkType":
                            Integer networkTypeid = (Integer) it.next();
                            ret += NetWorkType.fromCode(networkTypeid).getDesc() + ",";
                            break;

                    }

                }
                if (StringUtils.isEmpty(ret) == false) {
                    ret = ret.substring(0, ret.length() - 1);
                }
            }
        }
        return ret;
    }

    private String osType_trans(String dim) {
        String ret = "";
        if(StringUtils.isEmpty(dim)){
            ret = "不限";
        }else {
            ret = dim;
        }
        return ret;
    }

    /**
     * 屏数控制
     * @param dimValue
     * @return
     */
    private String screen_trans(String dimValue) {
        String ret = "";
        JSONObject dim = null;
        if(StringUtils.isEmpty(dimValue) == false) {
            dim = JSONObject.parseObject(dimValue);
        }
        if(dim == null){
            ret = "不限";
        }else {
            Integer type = dim.getInteger("type");
            if (type == 0 || type == null) {
                ret = "不限";
            } else if(type ==1){
                ret = "首屏";
            } else if(type ==2){
                ret = "非首屏";
            }
        }
        return ret;
    }

    private String freq_trans(String dimValue) {
        String ret = "";
        JSONObject dim = null;
        if(StringUtils.isEmpty(dimValue) == false) {
            dim = JSONObject.parseObject(dimValue);
        }

        if(dim == null){
            ret = "不限";
        }else {
            Integer type = dim.getInteger("type");
            if (type == 0 || type == null) {
                ret = "不限";
            } else {
                ret = "每个活动每天对每个用户，展现："+dim.getBigInteger("freqValue").toString()+"次";
            }
        }
        return ret;
    }

    private String map_trans(String dimValue, Map<Long, String> map) {

        String ret = "";

        if(StringUtils.isEmpty(dimValue) == false) {
            JSONArray selected = JSON.parseArray(dimValue);
            Iterator<Object> it = selected.iterator();
            while (it.hasNext()) {
                Integer key = (Integer) it.next();
                if(map.containsKey(key.longValue())) {
                    ret += map.get(key.longValue()).toString() + ",";
                }
            }
            if (StringUtils.isEmpty(ret) == false) {
                ret = ret.substring(0, ret.length() - 1);
            }
        }
        return ret;
    }

    private String cookieGid_trans(String dimValue, Map<Long, CookieGid> map) {
        String ret = "";

        if(StringUtils.isEmpty(dimValue) == false) {
            JSONArray selected = JSON.parseArray(dimValue);
            Iterator<Object> it = selected.iterator();
            while (it.hasNext()) {
                Integer id = (Integer) it.next();
                CookieGid cookieGid = map.get(id.longValue());
                ret += cookieGid.getCkGroupName()+ ",";
            }
            if (StringUtils.isEmpty(ret) == false) {
                ret = ret.substring(0, ret.length() - 1);
            }
        }

        return ret;
    }

    /**
     * 媒体定向编码转名称
     * @param dimValue
     * @param websiteTypeMap
     * @return
     */
    private String media_trans(String dimValue, Map<String, WebsiteType> websiteTypeMap) {
        String ret = "";

        JSONObject dim = null;
        if(StringUtils.isEmpty(dimValue) == false) {
            dim = JSONObject.parseObject(dimValue);
        }

        if(dim == null){
            ret = "不限";
        }else {
            Integer type = dim.getInteger("type");
            if (type == 0 || type == null) {
                ret = "不限";
            } else if(type == 1) {
                ret = "按照媒体类型投放:";
                JSONArray selected = dim.getJSONArray("websiteType");
                Iterator<Object> it = selected.iterator();
                while (it.hasNext()) {
                    Integer id = (Integer) it.next();
                    WebsiteType websiteType = websiteTypeMap.get(id.toString());
                    ret += websiteType.getName() + ",";
                }
                if (StringUtils.isEmpty(ret) == false) {
                    ret = ret.substring(0, ret.length() - 1);
                }
            }else if(type == 2) {
                ret = "按照媒体域名投放:";
                JSONArray selected = dim.getJSONArray("webDomain");
                Iterator<Object> it = selected.iterator();
                while (it.hasNext()) {
                    String domain = (String) it.next();
                    ret += domain + ",";
                }
                if (StringUtils.isEmpty(ret) == false) {
                    ret = ret.substring(0, ret.length() - 1);
                }
            }
        }
        return ret;
    }

    /**
     * 应用定向编码转名称
     * @param dimValue
     * @param appMap
     * @return
     */
    private String app_trans(String dimValue, Map<String, AppType> appMap) {
        String ret = "";

        JSONObject dim = null;
        if(StringUtils.isEmpty(dimValue) == false) {
            dim = JSONObject.parseObject(dimValue);
        }

        if(dim == null){
            ret = "不限";
        }else {
            Integer type = dim.getInteger("type");
            if (type == 0 || type == null) {
                ret = "不限";
            } else if(type == 1) {
                ret = "按照应用类型投放:";
                JSONArray selected = dim.getJSONArray("appType");
                Iterator<Object> it = selected.iterator();
                while (it.hasNext()) {
                    Integer id = (Integer) it.next();
                    AppType appType = appMap.get(id.toString());
                    ret += appType.getName() + ",";
                }
                if (StringUtils.isEmpty(ret) == false) {
                    ret = ret.substring(0, ret.length() - 1);
                }
            }else if(type == 2) {
                ret = "按照应用包投放:";
                JSONArray selected = dim.getJSONArray("appPacket");
                Iterator<Object> it = selected.iterator();
                while (it.hasNext()) {
                    JSONObject domain = (JSONObject) it.next();
                    ret += domain.toJSONString() + ",";
                }
                if (StringUtils.isEmpty(ret) == false) {
                    ret = ret.substring(0, ret.length() - 1);
                }
            }
        }
        return ret;
    }

    private String channel_trans(String dimValue){
        String ret = "";

        JSONObject dim = null;
        if(StringUtils.isEmpty(dimValue) == false) {
            dim = JSONObject.parseObject(dimValue);
        }

        if(dim == null){
            ret = "不限";
        }else {
            Integer type = dim.getInteger("type");
            if (type == 0 || type == null) {
                ret = "不限";
            } else {
                JSONArray selected = dim.getJSONArray("selected");
                Iterator<Object> it = selected.iterator();
                while (it.hasNext()) {
                    Integer vv = (Integer) it.next();
                    ret += CatgSerial.fromChannelId(vv.longValue()).getName() + ",";
                }
                if (StringUtils.isEmpty(ret) == false) {
                    ret = ret.substring(0, ret.length() - 1);
                }
            }
        }
        return ret;
    }

    /**
     * 地域定向编码转名称
     * @param dimValue
     * @param areasMap
     * @return
     */
    private String area_trans(String dimValue, Map<Integer, City> areasMap) {
        String ret = "";

        JSONObject dim = null;
        if(StringUtils.isEmpty(dimValue) == false) {
            dim = JSONObject.parseObject(dimValue);
        }

        if(dim == null){
            ret = "不限";
        }else {
            Integer type = dim.getInteger("type");
            if (type == 0 || type == null) {
                ret = "不限";
            } else {
                JSONArray selected = dim.getJSONArray("citys");
                Iterator<Object> it = selected.iterator();
                while (it.hasNext()) {
                    Integer id = (Integer) it.next();
                    City city = areasMap.get(id);
                    ret += city.getName() + ",";
                }
                if (StringUtils.isEmpty(ret) == false) {
                    ret = ret.substring(0, ret.length() - 1);
                }
            }
        }
        return ret;
    }

    @Override
    public void saveCampaignStep3(ActiveUser activeUser, Campaign campaign, Campaign oldCampaign, String operateDesc) {
        CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
        SysLog sysLog = new SysLog();
        sysLog.setBusinessId(campaign.getId());
        sysLog.setCreatorId(activeUser.getUserId());
        sysLog.setName("广告活动-第三步");
        sysLog.setType(2);
        sysLog.setOperateType(2);
        sysLog.setCreateTime(new Date());
        sysLog.setRemark("广告活动-第三步");

        boolean saveFlag = false;

        JSONObject jsonObject = new JSONObject();
        JSONObject beforeJson = new JSONObject();
        JSONObject afterJson = new JSONObject();
        JSONObject logJson = new JSONObject();

        if(StringUtils.isEmpty(operateDesc)){
            String logStr = "";

            String before = "";
            String after = "";
            before = oldCampaign.getClickUrl() == null ? "无":oldCampaign.getClickUrl();
            after = campaign.getClickUrl() == null ? "无":campaign.getClickUrl();
            beforeJson.put("clickUrl",before);
            afterJson.put("clickUrl",after);
            if(!before.equals(after)){ //活动定向策略发生变化则记录日志
                saveFlag = true;
                logStr = ""+before+"->"+after;
                logJson.put("点击链接",logStr);
            }
            before = oldCampaign.getLandingPage() == null ? "无":oldCampaign.getLandingPage();
            after = campaign.getLandingPage() == null ? "无":campaign.getLandingPage();
            beforeJson.put("landingPage",before);
            afterJson.put("landingPage",after);
            if(!before.equals(after)){ //活动定向策略发生变化则记录日志
                saveFlag = true;
                logStr = ""+before+"->"+after;
                logJson.put("落地页链接",logStr);
            }
            before = oldCampaign.getPvUrls() == null ? "无":oldCampaign.getPvUrls();
            after = campaign.getPvUrls() == null ? "无":campaign.getPvUrls();
            beforeJson.put("pvUrls",before);
            afterJson.put("pvUrls",after);
            if(!before.equals(after)){ //活动定向策略发生变化则记录日志
                saveFlag = true;
                logStr = ""+before+"->"+after;
                logJson.put("第三方展现监测",logStr);
            }
        }else {
            saveFlag = true;
            logJson.put("创意上传",operateDesc);
        }

        jsonObject.put(JSON_BEFORE,beforeJson);
        jsonObject.put(JSON_AFTER,afterJson);
        jsonObject.put(JSON_DESC,logJson.toJSONString());

        sysLog.setOperateDesc(jsonObject.toJSONString());

        if(saveFlag){
            sysLogDao.insert(sysLog);
        }
    }

    private Map<String,Object> campDimKeyMaps(){
        Map<String,Object> retMap = new HashMap<String,Object>();
        retMap.put("blacklist","黑名单");
        retMap.put("channels","渠道");
        retMap.put("areas","地域定向");
        retMap.put("populations","人群属性定向");
        retMap.put("instresting","兴趣定向");
        retMap.put("cookiePacket","个性化人群定向");
        retMap.put("retargetPacket","访客找回");
        retMap.put("matchType","人口属性标签");
        retMap.put("media","媒体定向");
        retMap.put("app","应用定向");
        retMap.put("freq","投放频次");
        retMap.put("screen","屏数控制");
        retMap.put("osType","操作系统");
        retMap.put("deviceType","设备");
        retMap.put("brand","设备品牌");
        retMap.put("operatorType","网络运营商");
        retMap.put("networkType","网络类型");

        return retMap;
    }
}
