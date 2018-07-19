package com.jtd.web.service.adx.xtrader;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jtd.utils.DateUtil;
import com.jtd.utils.HTTPUtil;
import com.jtd.web.constants.AdType;
import com.jtd.web.constants.AuditStatus;
import com.jtd.web.dao.IAdDao;
import com.jtd.web.service.AuditResult;
import com.jtd.web.service.adx.IXTraderAPIService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 灵集ADX的对接API
 *
 * @author zhangrui
 *
 */
@Service
public class XTraderAPIService implements IXTraderAPIService {

	private Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	private IAdDao adDao;

	public int syncCreative(long adId) {

		// 查询创意信息
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("id", adId);
		Map<String, Object> ad = adDao.findAdMapById(map);

		if (ad == null) {
			throw new RuntimeException("查不到广告[id=" + adId + "]的信息");
		}

		int adType = (Integer) ad.get("ad_type");
		
		JSONObject request = new JSONObject();
		request.put("dspid", XTraderUtil.DSPID + "");
		request.put("token", XTraderUtil.TOKEN);
		
		JSONObject createive = new JSONObject();
		
		// DSP平台的创意ID，和BidResponse.crid一致
		createive.put("creativeId", ad.get("id") + ""); 
		// 素材URL地址
		createive.put("url", ad.get("creative_url")); 

		String creative_size = (String) ad.get("creative_size");
		if (StringUtils.isEmpty(creative_size) == false) {
			try {
				// 创意宽，必填
				createive.put("width",
						Integer.parseInt(creative_size.split("x")[0]));
				// 创意高，必填
				createive.put("height",
						Integer.parseInt(creative_size.split("x")[1]));

			} catch (Exception e) {
				logger.error("XTraderAPIService--syncCreative，尺寸存在错误", e);
			}
		}
		
		if(isBanner(adType)){
			createive.put("duration", 0);
		}
		if(isVideo(adType)){
			//createiveMap.put("duration", value);
		}

		// 广告落地页，广告点击后跳转的最终地址
		String landingpage = (String)ad.get("landing_page");
		createive.put("landingpage", HTTPUtil.addHttpProtocol(landingpage)); 
		
		// DSP平台广告主名称
		createive.put("advertiser", ad.get("partner_name")); 
		
		// 第三方监控
		String pv_urls = (String)ad.get("pv_urls");
		if(StringUtils.isEmpty(pv_urls) == false ){
			
			JSONArray pv_urls_array = JSONObject.parseArray(pv_urls);
			
			for (int i = 0; i < pv_urls_array.size(); i++) {
				String url = pv_urls_array.getString(i);
				pv_urls_array.set(i, addHttp(url));
			}
			
			createive.put("monitor", pv_urls_array); 
		}

		// 物料生效时间，格式要求：YYYY-mm-dd，必须在有效期内的物料才能在XTrader平台投放
		if (ad.get("camp_start_time") != null) {
			Date startDate = (Date)ad.get("camp_start_time");
			createive.put("startdate", DateUtil.getDateStr(startDate, "yyyy-MM-dd"));
		}
		// 物料失效时间，格式要求：YYYY-mm-dd，必须在有效期内的物料才能在XTrader平台投放
		if (ad.get("camp_end_time") != null) {
			Date endDate = (Date)ad.get("camp_end_time");
			createive.put("enddate", DateUtil.getDateStr(endDate, "yyyy-MM-dd"));
		} else {
			// 如果没有结束日期，则在开始日期基础上加50年
			if (ad.get("camp_start_time") != null) {
				Date startDate = DateUtil.getDate(ad.get("camp_start_time")
						.toString());
				Calendar col = Calendar.getInstance();
				col.setTime(startDate);
				col.add(Calendar.YEAR, 50);

				String endDate = DateUtil.getDateStr(col.getTime(), "yyyy-MM-dd");
				createive.put("enddate", endDate);
			}
		}
		
		// 广告交互类型，1-打开网页 2-下载
		createive.put("action", 1); 
		
		if(isSohu(adType)){
			createive.put("showtext", "");
			createive.put("deliveryType", "1");
		}

		JSONArray creativeList = new JSONArray();
		creativeList.add(createive);

		if (isBanner(adType) || isVideo(adType)) {
			request.put("creativeType", "1");
			request.put("material", creativeList);
		} else if (isNatived(adType)) {
			request.put("natived", creativeList);
		} else if (isWaxFeed(adType)) {
			request.put("wax_feed", creativeList);
		} else if (isWaxFeedActivity(adType)) {
			request.put("wax_feed_activity", creativeList);
		} else if (isSohu(adType)) {
			request.put("creativeType", "5");
			request.put("sohu", creativeList);
		}

		logger.info("XTraderAPIService--syncCreative--request:\n" + request);

		JSONObject response = null;

		// 发送添加请求
		try {
			response = HTTPUtil.postJSON(XTraderUtil.CREATIVE_ADD_URL, request.toJSONString() , false , null);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("XTraderAPIService--syncCreative发生错误", e);
		}

		logger.info("XTraderAPIService--syncCreative--response:\n" + response);

		if (response != null) {
			// 接口调用状态，0：执行成功 1:系统认证失败 2:请求参数错误 3:其他错误
			int result = response.getIntValue("result");
			JSONObject message = response.getJSONObject("message");
			if (result != 0) {
				return -1 ;
			}
			else{
				if(message != null){
					if(message.isEmpty()){
						return 0 ;
					}
					else{
						return -1 ;
					}
					
				}
				else{
					return -1 ;
				}
			}
		}
		return -1 ;

	}
	
	private String addHttp(String url){
		if(StringUtils.isEmpty(url) == false ){
			if(url.startsWith("http://") == false){
				return "http://" + url ;
			}
			else{
				return url ;
			}
		}
		return "" ;
	}

	public AuditResult queryCreativeAuditStatus(long adId) {
		
		AuditResult auditResult = new AuditResult();
		
		JSONObject request = new JSONObject();
		request.put("dspid", XTraderUtil.DSPID+"");
		request.put("token", XTraderUtil.TOKEN);
		request.put("creativeIds", new long[]{adId});

		logger.debug("XTraderAPIService--queryCreativeAuditStatus--request:\n"
				+ request);
		
		JSONObject response = null;
		try {
			response = HTTPUtil.postJSON(XTraderUtil.CREATIVE_QUERY_AUDIT_STATE_URL, request.toJSONString(), false , null);
		} catch (Exception e) {
			logger.error("XTraderAPIService--queryCreativeAuditStatus发生错误",e);
		}

		logger.debug("XTraderAPIService--queryCreativeAuditStatus--response:\n"
				+ response);

		if (response != null) {

			// 接口调用状态，0：执行成功 1：系统认证失败 2：请求参数错误 3：其他错误
			int result = response.getInteger("result");

			JSONObject message = response.getJSONObject("message");

			if (result == 0) {
				if (message != null) {
					
					JSONArray records = message.getJSONArray("records");
					if(records != null && records.size() > 0){
						
						JSONObject response0 = records.getJSONObject(0);
						
						//状态直接为汉字：待审核，通过，不通过
						String auditStatus = response0.getString("result") != null ? response0.getString("result") : "" ;
						String refuseReason = response0.getString("reason");
						
						auditResult.setRefuseReason(refuseReason);
						if(auditStatus.equals("通过")){
							auditResult.setAuditStatus(AuditStatus.STATUS_SUCCESS);
						}
						else if(auditStatus.equals("待审核")){
							auditResult.setAuditStatus(AuditStatus.STATUS_WAIT);
						}
						else if(auditStatus.equals("不通过")){
							auditResult.setAuditStatus(AuditStatus.STATUS_FAIL);
						}
						else{
							auditResult.setAuditStatus(AuditStatus.STATUS_FAIL);
						}
						
						return auditResult ;
						
					}
				}
			}
		}
		
		return null ;
	}

	private boolean isBanner(int adType) {
		return adType == AdType.PC_BANNER.getCode()
				|| adType == AdType.APP_BANNER.getCode()
				|| adType == AdType.APP_FULLSCREEN.getCode()
				|| adType == AdType.WAP.getCode();
	}
	
	private boolean isVideo(int adType) {
		return adType == AdType.PC_VIDEO.getCode()
				|| adType == AdType.APP_VIDEO.getCode()
				|| adType == AdType.WAP_VIDEO.getCode();
	}

	private boolean isNatived(int adType) {
		return adType == AdType.APP_MESSAGE.getCode();
	}

	private boolean isWaxFeed(int adType) {
		return false;
	}

	private boolean isWaxFeedActivity(int adType) {
		return false;
	}

	private boolean isSohu(int adType) {
		return false;
	}
}
