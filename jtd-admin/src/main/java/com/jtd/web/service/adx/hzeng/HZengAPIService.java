package com.jtd.web.service.adx.hzeng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jtd.commons.mybatis.support.CustomerContextHolder;
import com.jtd.utils.HTTPUtil;
import com.jtd.utils.LikeBase64;
import com.jtd.web.constants.AdType;
import com.jtd.web.constants.AuditStatus;
import com.jtd.web.constants.CatgSerial;
import com.jtd.web.controller.DicController;
import com.jtd.web.dao.IAdDao;
import com.jtd.web.dao.IPartnerDao;
import com.jtd.web.po.Partner;
import com.jtd.web.service.AuditResult;
import com.jtd.web.service.adx.IHZengAPIService;

/**
 * 互众ADX的对接API
 * 
 * @author zhangrui
 *
 */
@Service
public class HZengAPIService implements IHZengAPIService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private IPartnerDao partnerDao;

	@Autowired
	private IAdDao adDao;
	
	@Override
	public boolean syncAdvertiser(long submitUserId, long partnerId) {

		CustomerContextHolder
				.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);

		JSONObject advertiserData = queryAdvertiserById(partnerId);

		Partner partner = partnerDao.getById(partnerId);

		if (partner == null) {
			return false ;
		}

		// 封装查询客户的参数
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("advertiserId", partner.getId()); // 广告客户ID
		jsonObject.put("advertiserLiteName", partner.getPartnerName()); // 广告客户名称
		jsonObject.put("advertiserName", partner.getPartnerName()); // 广告客户资质名称
		jsonObject.put("siteName", partner.getWebsiteName()); // 网站名称
		jsonObject.put("siteUrl", partner.getWebsiteUrl()); // 网站URL
		jsonObject.put("telephone", partner.getContactTelephone()); // 联系电话

		JSONArray array = new JSONArray();
		array.add(jsonObject);

		HashMap<String,String> params = new HashMap<String, String>();
		params.put("request", array.toJSONString());
		
		int isWhiteUser = 0 ;
		
		int tryCnt = 1 ;
		
		while(tryCnt <= 3){
			
			logger.info("HZengAPIService--syncAdvertiser，添加广告主，尝试第" + tryCnt + "次--request:\n" + array);
			
			JSONObject response = null;

			if (advertiserData == null) {

				// 发送添加请求
				try {
					response = HTTPUtil.postSimpleForm(HZengUtil.ADVERTISER_ADD_URL, params);
				} catch (Exception e) {
					logger.error("HZengAPIService--syncAdvertiser发生错误",e);
				}

			} else {

				// 发送修改请求
				try {
					response = HTTPUtil.postSimpleForm(HZengUtil.ADVERTISER_UPDATE_URL, params);
				} catch (Exception e) {
					logger.error("HZengAPIService--syncAdvertiser发生错误",e);
				}

			}

			logger.info("HZengAPIService--syncAdvertiser，添加广告主，尝试第" + tryCnt + "次--response:\n" + response);

			if (response != null) {

				// 接口调用状态，0=无错误，1=部分错误，2=全部错误
				int ret_code = response.getIntValue("status");
				if (ret_code == 0) {
					
					JSONArray advertiserArray = response.getJSONArray("response");
					if(advertiserArray != null){
						JSONObject adver = advertiserArray.getJSONObject(0);
						if(adver != null){
							// 0：非白名单（需要上传资质）；1：白名单
							isWhiteUser = adver.getIntValue("isWhiteUser");
							
							logger.info("HZengAPIService--syncAdvertiser，添加广告主，partnerId=" + partnerId + "，" + (isWhiteUser == 0 ? "属于非白名单":"属于白名单"));
						}
					}
					
					return true ;
					
				}
			}
			
			tryCnt++ ;
			
		}
		
		return false;

	}

	@Override
	public JSONObject queryAdvertiserById(long partnerId) {

		JSONObject request = new JSONObject();
		request.put("advertiserIds", new long[]{partnerId});

		logger.info("HZengAPIService--queryAdvertiserById--request:\n" + request);

		HashMap<String,String> params = new HashMap<String, String>();
		params.put("request", request.toJSONString());
		
		// 发送查询请求
		JSONObject response = null;
		try {
			response = HTTPUtil.postSimpleForm(HZengUtil.ADVERTISER_GET_URL,params);
		} catch (Exception e) {
			logger.error("HZengAPIService--queryAdvertiserById 发生错误",e);
		}

		logger.info("HZengAPIService--queryAdvertiserById--response:\n"
				+ response);

		if (response != null) {

			// 接口调用状态，0=无错误，1=部分错误，2=全部错误
			int responseStatus = response.getInteger("status");

			// 如果用户已经存在于百度平台，则array的长度会是1
			JSONArray array = response.getJSONArray("response");

			if (responseStatus == 0) {
				if (array != null && array.size() > 0) {
					return array.getJSONObject(0);
				}
			}
		}

		return null;

	}


	@Override
	public AuditResult queryAdvertiserAuditStatus(long partnerId) {
		
		AuditResult auditResult = new AuditResult();

		CustomerContextHolder
				.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);

		JSONObject request = new JSONObject();
		request.put("advertiserIds", new long[]{partnerId});
		
		HashMap<String,String> params = new HashMap<String, String>();
		params.put("request", request.toJSONString());

		logger.info("HZengAPIService--queryAdvertiserAuditStatus--request:\n"
				+ request);
		
		JSONObject response = null;
		try {
			response = HTTPUtil.postSimpleForm(
					HZengUtil.ADVERTISER_QUERY_QUALIFICATION_URL,params);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("HZengAPIService--queryAdvertiserAuditStatus发生错误",e);
		}

		logger.info("HZengAPIService--queryAdvertiserAuditStatus--response:\n"
				+ response);
		
		if (response != null) {
			
			//{"response":[{"state":0,"advertiserId":158,"refuseReason":""}],"status":0,"errors":[]}
			int status = response.getInteger("status");
			if(status == 0){
				
				JSONArray responseArray = response.getJSONArray("response");
				if(responseArray != null && responseArray.size() > 0){
					
					JSONObject response0 = responseArray.getJSONObject(0);
					
					// 0：审核通过； 1：审核中；2：审核拒绝；3：缺省状态
					int auditStatus = response0.getIntValue("state");
					
					switch(auditStatus){
					case 0 :
						auditResult.setAuditStatus(AuditStatus.STATUS_SUCCESS);
						break ;
					case 1 :
						auditResult.setAuditStatus(AuditStatus.STATUS_WAIT);
						break ;
					case 2 :
						auditResult.setAuditStatus(AuditStatus.STATUS_FAIL);
						break ;
					case 3 :
						auditResult.setAuditStatus(AuditStatus.STATUS_COMMIT_FAIL);
						break ;
					default:
						auditResult.setAuditStatus(AuditStatus.STATUS_COMMIT_FAIL);
						break ;
					}
					
					String refuseReason = response0.getString("refuseReason");
					auditResult.setRefuseReason(refuseReason);
					
					return auditResult ;
				}
			}
			
		}
		
		return null ;

	}

	@Override
	public boolean syncCreative(long adId) {

		CustomerContextHolder
				.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);

		// 查询创意信息
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("id", adId);
		Map<String, Object> ad = adDao.findAdMapById(map);
		if (ad == null) {
			return false ;
		}

		// 封装查询客户的参数
		JSONArray creativeList = new JSONArray();
		JSONObject creativeMap = new JSONObject();
		
		// 此 id 为 dsp 系统的创意 id，需唯一
		creativeMap.put("creativeId", ad.get("id"));
		// 广告主 id，必填
		creativeMap.put("advertiserId", ad.get("partner_id")); 
		
		// 创意类型，必填 1-图片（jpg/png/gif），2-flash（swf），3-视频（flv/swf/mp4），4-动态创意，5-图文
		AdType adType = AdType.getAdType(ad.get("ad_type") != null ? (int)ad.get("ad_type") : -1);
		if(adType == AdType.PC_BANNER || adType == AdType.APP_BANNER){
			creativeMap.put("type", 1);
		}
		else if(adType == AdType.PC_VIDEO || adType == AdType.APP_VIDEO || adType == AdType.WAP_VIDEO){
			creativeMap.put("type", 3);
		}
		else{
			creativeMap.put("type", 1); 
		}
		
		// 创意 URL
		creativeMap.put("creativeUrl", ad.get("creative_url"));
		/**
		 * 点击链接，http://或https://开头，长度限制1024个字节，如需使用自定义的参数，可以在链接后拼接${EXT}，该参数将会由bidresponse中dsp传来的对应内容做替换。
		 * 填“no”，代表此物料不需要点击。
		 */
		
		String adClickUrl = HTTPUtil.addHttpProtocol(ad.get("click_url") != null ? ad.get("click_url").toString() : null);
		creativeMap.put("targetUrl", "http://counter.xinmt.com/nc?q=${EXT_RAW}&r=" + LikeBase64.encode(adClickUrl));
		
		// 到达页面，http://或https://开头，长度限制2048个字节。填“no”，代表此物料不需要点击。
		creativeMap.put("landingPage", HTTPUtil.addHttpProtocol(ad.get("landing_page") != null ? ad.get("landing_page").toString() : null));
		
		/*
		 * 
		 * http://或https://开头，最多包含3个链接。每个链接长度限制1024个字节。
		 * 	如需使用自定义的参数，可以在monitorUrl后面拼接
			${EXT}或${EXT_RAW}，该参数将会由BidResponse中dsp传来
			的对应内容做替换。${EXT}会转义，${EXT_RAW}不会转义。展
			示监测支持${AUCTION_PRICE}宏，详见5.3。注：若该url不含$
			{EXT}宏，则最终会在其后拼接一个hzext的参数，其值为ext的内
			容经url转义后的内容。
		 * 
		 */
		String pv_urls = (String) ad.get("pv_urls");
		if (StringUtils.isEmpty(pv_urls) == false) {
			JSONArray array = JSONObject.parseArray(pv_urls);
			// 广告展现监测链接数组，必填 ，最多包含 3 个链接，最多包含 3 个链接
			List<String> pvList = new ArrayList<String>();
			if(array != null){
				 for(int i=0;i<array.size();i++){
					if(i>=3){
						break;
					}
					pvList.add(array.getString(i).toString());
				}
			}
			creativeMap.put("monitorUrls", pvList);
		}

		String creative_size = (String) ad.get("creative_size");
		if (StringUtils.isEmpty(creative_size) == false) {
			try {
				// 创意宽，必填
				creativeMap.put("width",
						Integer.parseInt(creative_size.split("x")[0]));
				// 创意高，必填
				creativeMap.put("height",
						Integer.parseInt(creative_size.split("x")[1]));
				
			} catch (Exception e) {
				logger.error("BaiduCreativeService--syncCreative，尺寸存在错误", e);
				return false ;
			}
		}

		Long dspCategoryId = Long.parseLong((String) ad.get("parter_catgid"));
		Long hzengCategoryId = DicController.getCatgSerial(dspCategoryId,
				CatgSerial.HZ);
		// 创意所属广告行业，必填 注：必须指定到第 2 级行业。广告行业体系见数据字典
		creativeMap.put("creativeTradeId", hzengCategoryId);

		creativeList.add(creativeMap);

		HashMap<String,String> params = new HashMap<String, String>();
		params.put("request", creativeList.toJSONString());
		
		JSONObject oldData = queryCreativeById(adId);

		int tryCnt = 1 ;
		
		while(tryCnt <= 3){
			
			JSONObject response = null;

			logger.info("HZengAPIService--syncCreative，尝试第" + tryCnt + "次，--request:\n" + creativeList);

			if (oldData == null) {

				// 发送添加请求
				try {
					response = HTTPUtil.postSimpleForm(HZengUtil.CREATIVE_ADD_URL, params);
				} catch (Exception e) {
					logger.error("HZengAPIService--syncCreative发生错误",e);
				}

			} else {

				// 发送修改请求
				try {
					response = HTTPUtil.postSimpleForm(HZengUtil.CREATIVE_UPDATE_URL, params);
				} catch (Exception e) {
					logger.error("HZengAPIService--syncCreative发生错误",e);
				}

			}

			logger.info("HZengAPIService--syncCreative，尝试第" + tryCnt + "次，--response:\n" + response);

			if (response != null) {

				// 接口调用状态，0=无错误，1=部分错误，2=全部错误
				int ret_code = response.getIntValue("status");
				if (ret_code == 0) {

					return true ;
				}
			}
			
			tryCnt++ ;
		}
		
		return false ;

	}

	@Override
	public JSONObject queryCreativeById(long adId) {

		JSONObject request = new JSONObject();
		request.put("creativeIds", new long[]{adId});
		
		HashMap<String,String> params = new HashMap<String, String>();
		params.put("request", request.toJSONString());

		logger.info("HZengAPIService--queryCreativeById--request:\n"
				+ request);

		// 发送查询请求
		JSONObject response = null;
		try {
			response = HTTPUtil.postSimpleForm(HZengUtil.CREATIVE_GET_URL, params);
		} catch (Exception e) {
			logger.error("HZengAPIService--queryCreativeById发生错误",e);
		}

		// {"response":[{"adviewType":2,"appName":"","interactiveStyle":0,"telNo":"18611563258","downloadUrl":"http://www.baidu.com.cn","appDesc":"",
		// "appPackageSize":0.0,"state":1,"type":1,"height":50,"width":320,"advertiserId":76,"creativeId":215,"targetUrl":"http://www.baidu.com.cn",
		// "creativeUrl":"http://223.203.216.232:9098/20150922_KxxK8Xan.jpg","landingPage":"http://www.baidu.com.cn",
		// "monitorUrls":["http://monitor.baidu.com.cn/0","http://monitor.baidu.com.cn/1"],"creativeTradeId":7801,"frameAgreementNo":null,"refuseReason":null}],"status":0,"errors":[]}

		logger.info("HZengAPIService--queryCreativeById--response:\n" + response);

		if (response != null) {

			// 接口调用状态，0=无错误，1=部分错误，2=全部错误
			int responseStatus = response.getInteger("status");

			// 如果用户已经存在于百度平台，则array的长度会是1
			JSONArray array = response.getJSONArray("response");

			if (responseStatus == 0) {
				if (array != null && array.size() > 0) {
					return array.getJSONObject(0);
				}
			}
		}

		return null;
	}

	@Override
	public AuditResult queryCreativeAuditStatus(long adId) {
		
		AuditResult auditResult = new AuditResult();

		JSONObject request = new JSONObject();
		request.put("creativeIds", new long[]{adId});
		
		HashMap<String,String> params = new HashMap<String, String>();
		params.put("request", request.toJSONString());

		logger.debug("HZengAPIService--queryCreativeAuditStatus--request:\n"
				+ request);
		
		JSONObject response = null;
		try {
			response = HTTPUtil.postSimpleForm(HZengUtil.CREATIVE_QUERY_AUDIT_STATE_URL , params);
		} catch (Exception e) {
			logger.error("HZengAPIService--queryCreativeAuditStatus发生错误",e);
		}

		logger.debug("HZengAPIService--queryCreativeAuditStatus--response:\n"
				+ response);

		if (response != null) {

			// 接口调用状态，0=无错误，1=部分错误，2=全部错误
			int status = response.getInteger("status");

			JSONArray responseArray = response.getJSONArray("response");

			if (status == 0) {
				if (responseArray != null && responseArray.size() > 0) {

					JSONObject response0 = responseArray.getJSONObject(0);
					
					// 0：检查通过 , 1：待检查（默认）2：检查未通过
					int auditStatus = response0.getIntValue("state");
					String refuseReason = response0.getString("refuseReason");
					
					auditResult.setRefuseReason(refuseReason);
					if(auditStatus == 0){
						auditResult.setAuditStatus(AuditStatus.STATUS_SUCCESS);
					}
					else if(auditStatus == 1){
						auditResult.setAuditStatus(AuditStatus.STATUS_WAIT);
					}
					else if(auditStatus == 2){
						auditResult.setAuditStatus(AuditStatus.STATUS_FAIL);
					}
					else{
						auditResult.setAuditStatus(AuditStatus.STATUS_WAIT);
					}
					
					return auditResult ;
				}
			}
		}
		
		return null ;
	}
	
	public static void main(String[] args){
		
		
	}
	
}
