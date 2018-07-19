package com.jtd.web.service.adx.vam;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.jtd.utils.FileUtil;
import com.jtd.utils.HTTPUtil;
import com.jtd.web.constants.AdType;
import com.jtd.web.constants.AuditStatus;
import com.jtd.web.constants.CatgSerial;
import com.jtd.web.dao.IAdCategoryDao;
import com.jtd.web.dao.IAdDao;
import com.jtd.web.dao.ICampaignDao;
import com.jtd.web.dao.ICreativeDao;
import com.jtd.web.dao.IPartnerDao;
import com.jtd.web.po.Ad;
import com.jtd.web.po.AdCategory;
import com.jtd.web.po.Campaign;
import com.jtd.web.po.Creative;
import com.jtd.web.po.Partner;
import com.jtd.web.service.AuditResult;
import com.jtd.web.service.adx.IVamAPIService;

/**
 * 万流客ADX的对接API
 * 
 * @author zhangrui
 *
 */
@Service
public class VamAPIService implements IVamAPIService {

	private Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	private IPartnerDao partnerDao;
	@Autowired
	private IAdDao adDao;
	@Autowired
	private ICampaignDao campaignDao;
	@Autowired
	private ICreativeDao creativeDao;
	@Autowired
	private IAdCategoryDao adCategoryDao;

	@Override
	public boolean syncCreative(long adId) {

		Ad ad = adDao.getById(adId);
		if (ad == null) {
			logger.error("VamAPIService--syncCreative--查询ad为空，adId=" + adId);
			return false;
		}

		Long campaignId = ad.getCampaignId();
		Long creativeId = ad.getCreativeId();
		if (campaignId == null) {
			logger.error("VamAPIService--syncCreative--campaignId为空，adId="
					+ adId);
			return false;
		}
		if (creativeId == null) {
			logger.error("VamAPIService--syncCreative--creativeId为空，adId="
					+ adId);
			return false;
		}

		Campaign campaign = campaignDao.getById(campaignId);
		Creative creative = creativeDao.getById(creativeId);
		if (campaign == null) {
			logger.error("VamAPIService--syncCreative--campaign为空，adId=" + adId);
			return false;
		}
		if (creative == null) {
			logger.error("VamAPIService--syncCreative--creative为空，adId=" + adId);
			return false;
		}

		AdCategory adCategory = new AdCategory();
		adCategory.setAdId(adId);
		adCategory.setCatgserial(CatgSerial.VAM.getCode());
		adCategory = adCategoryDao.getAdCategory(adCategory);
		if (adCategory == null) {
			logger.error("VamAPIService--syncCreative--adCategory为空，adId="
					+ adId);
			return false;
		}

		AdType adType = AdType.getAdType(campaign.getAdType());
		if (adType == null) {
			logger.error("VamAPIService--syncCreative--adType为空，adId=" + adId);
			return false;
		}
		
		String queryUrl = null ;
		
		switch (adType) {
		case PC_BANNER:
			queryUrl = VamUtil.PC_STATIC_CREATIVE_QUERY_URL ;
			break;
		case APP_BANNER:
		case APP_FULLSCREEN:
			// 移动广告，in app
			queryUrl = VamUtil.APP_STATIC_CREATIVE_ADD_URL;
			break;
		case WAP:
			// 移动广告 ，wap
			queryUrl = VamUtil.WAP_STATIC_CREATIVE_ADD_URL;
			break;
		case PC_VIDEO:
			queryUrl = VamUtil.PC_VIDEO_CREATIVE_ADD_URL;
			break;
		case WAP_VIDEO:
		case APP_VIDEO:
			queryUrl = VamUtil.MOBILE_VIDEO_CREATIVE_ADD_URL;
			break;
		default:
			logger.error("VamAPIService--syncCreative--queryCreative，adType无法处理，adType=" + adType);
			return false;
		}
		
		logger.info("VamAPIService--syncCreative--queryCreative，url:\n"
				+ queryUrl);

		JSONObject queryResponse = null ;
		try {
			queryResponse = HTTPUtil.getJSON(queryUrl, false , VamUtil.AUTH_HEADER );
		} catch (Exception e1) {
			logger.error("VamAPIService--syncCreative--queryCreative出错" , e1);
			return false ;
		}
		
		logger.info("VamAPIService--syncCreative--queryCreative，queryResponse:\n" + queryResponse);
		
		CreativeEntity entity = null;
		String url = null;

		try {
			switch (adType) {
			case PC_BANNER:
				if(queryResponse != null){
					url = VamUtil.PC_STATIC_CREATIVE_UPDATE_URL;
				}
				else{
					url = VamUtil.PC_STATIC_CREATIVE_ADD_URL;
				}
				break;
			case APP_BANNER:
			case APP_FULLSCREEN:
				// 移动广告，in app
				if(queryResponse != null){
					url = VamUtil.APP_STATIC_CREATIVE_UPDATE_URL;
				}
				else{
					url = VamUtil.APP_STATIC_CREATIVE_ADD_URL;
				}
				entity = createMobileCreativeEntity(adType, campaign, ad,
						creative, adCategory.getId().intValue(), true);
				break;
			case WAP:
				// 移动广告 ，wap
				if(queryResponse != null){
					url = VamUtil.WAP_STATIC_CREATIVE_UPDATE_URL;
				}
				else{
					url = VamUtil.WAP_STATIC_CREATIVE_ADD_URL;
				}
				entity = createMobileCreativeEntity(adType, campaign, ad,
						creative, adCategory.getId().intValue(), false);
				break;
			case PC_VIDEO:
				if(queryResponse != null){
					url = VamUtil.PC_VIDEO_CREATIVE_UPDATE_URL;
				}
				else{
					url = VamUtil.PC_VIDEO_CREATIVE_ADD_URL;
				}
				entity = creativeVideoCreativeEntity(adType, campaign, ad,
						creative, adCategory.getId().intValue());
				break;
			case WAP_VIDEO:
			case APP_VIDEO:
				if(queryResponse != null){
					url = VamUtil.MOBILE_VIDEO_CREATIVE_UPDATE_URL;
				}
				else{
					url = VamUtil.MOBILE_VIDEO_CREATIVE_ADD_URL;
				}
				entity = creativeVideoCreativeEntity(adType, campaign, ad,
						creative, adCategory.getId().intValue());
				break;
			default:
				logger.error("VamAPIService--syncCreative--adType无法处理，adType=" + adType);
				return false;
			}
		} catch (Exception e) {
			logger.error("VamAPIService--syncCreative--发生错误，adId=" + adId, e);
			return false;
		}

		logger.info("VamAPIService--syncCreative--url=" + url);
		logger.info("VamAPIService--syncCreative--entity=" + entity);

		if (entity != null) {

			String requestJSON = JSONObject.toJSONString(entity);

			logger.info("VamAPIService--syncCreative--request:\n" + requestJSON);

			JSONObject response = HTTPUtil.postJSON(url, requestJSON, false , VamUtil.AUTH_HEADER);

			logger.info("VamAPIService--syncCreative--response:\n" + response);

			if(response != null){
				
				//status是错误状态码，如果错误码为空，就认为成功
				if(response.get("status") == null){
					return true ;
				}
				
			}
		}

		return false;
	}

	@Override
	public AuditResult queryCreativeAuditStatus(long adId) {

		Ad ad = adDao.getById(adId);
		if (ad == null) {
			logger.error("VamAPIService--queryCreativeAuditStatus--查询ad为空，adId="
					+ adId);
			return null;
		}

		Long campaignId = ad.getCampaignId();
		if (campaignId == null) {
			logger.error("VamAPIService--queryCreativeAuditStatus--campaignId为空，adId="
					+ adId);
			return null;
		}

		Campaign campaign = campaignDao.getById(campaignId);
		if (campaign == null) {
			logger.error("VamAPIService--queryCreativeAuditStatus--campaign为空，adId="
					+ adId);
			return null;
		}

		AdType adType = AdType.getAdType(campaign.getAdType());
		if (adType == null) {
			logger.error("VamAPIService--queryCreativeAuditStatus--adType为空，adId="
					+ adId);
			return null;
		}

		String url = null;
		switch (adType) {
		case PC_BANNER:
			// pc广告
			url = VamUtil.PC_STATIC_CREATIVE_QUERY_STATUS_URL ;
			break;
		case APP_BANNER:
		case APP_FULLSCREEN:
		case WAP:
			// 移动广告
			url = VamUtil.MOBILE_STATIC_CREATIVE_QUERY_STATUS_URL;
			break;
		case PC_VIDEO:
			// pc视频
			url = VamUtil.PC_VIDEO_CREATIVE_QUERY_STATUS_URL;
			break;
		case APP_VIDEO:
		case WAP_VIDEO:
			// 移动视频
			url = VamUtil.MOBILE_VIDEO_CREATIVE_QUERY_STATUS_URL;
			break;
		default:
			return null;
		}
		
		url = url.replace("${id}", ad.getId().toString());

		logger.info("VamAPIService--queryCreativeAuditStatus--url:\n"
				+ url);

		JSONObject response = HTTPUtil.getJSON(url , false , VamUtil.AUTH_HEADER);

		logger.info("VamAPIService--queryCreativeAuditStatus--response:\n"
				+ response);

		// { "id": "creativeid","status": 2 } 1:待审核 2:通过 3:不通过
		if (response != null) {
			String id = response.getString("id");
			if (Long.parseLong(id) == adId) {
				int status = response.getInteger("status");

				AuditResult ar = new AuditResult();
				switch (status) {
				case 1:
					ar.setAuditStatus(AuditStatus.STATUS_WAIT);
					break;
				case 2:
					ar.setAuditStatus(AuditStatus.STATUS_SUCCESS);
					break;
				case 3:
					ar.setAuditStatus(AuditStatus.STATUS_FAIL);
					break;
				default:
					break;
				}
				return ar ;

			}
		}
		
		return null ;
	}

	/**
	 * 创建移动广告实体
	 * 
	 * @param adCategory
	 * @param campaign
	 * @param ad
	 * @param creative
	 * @param mode
	 * @return
	 */
	private MobileCreativeEntity createMobileCreativeEntity(AdType adType,
			Campaign campaign, Ad ad, Creative creative, Integer adCategoryId,
			boolean isInApp) throws Exception {

		String creativeUrl = creative.getCreativeUrl();

		String extendName = FileUtil.getExtendName(creativeUrl);
		int format = getFormat(extendName);
		String size = creative.getSize();
		if (size.indexOf("x") == -1) {
			throw new Exception("解析创意尺寸错误[" + size + "]");
		}
		int width = Integer.parseInt(creative.getSize().split("x")[0]);// 创意宽
		int height = Integer.parseInt(creative.getSize().split("x")[1]);// 创意高

		String[] domains = getDomains(ad);

		if (isInApp) {

			int vam_ad_type = 0;

			if (AdType.APP_BANNER.equals(adType)) {
				vam_ad_type = 1;
			} else if (AdType.APP_FULLSCREEN.equals(adType)) {
				// 万流客区分 2-开屏广告和 3-插屏广告
				// 我们的系统不区分这两种，所以只返回一种类型
				vam_ad_type = 3;
			} else {
				vam_ad_type = 0;
			}
			// 万流客的 4-信息流广告 暂时不支持

			String[] pic_urls = new String[] { creativeUrl };
			String title = "";
			String text = "";

			return new MobileCreativeEntity(ad.getId().toString(), "", width,
					height, vam_ad_type, format, adCategoryId, domains,
					pic_urls, title, text);
		} else {
			String html_snippet = VamUtil.MOBILE_WAP_HTML
					.replace("#WIDTH#", width + "")
					.replace("#HEIGHT#", height + "")
					.replace("#ADURL#", creativeUrl);

			return new MobileCreativeEntity(ad.getId().toString(),
					html_snippet, width, height, 0, format, adCategoryId,
					domains);
		}

	}

	/**
	 * 创建视频广告实体
	 * 
	 * @param adCategory
	 * @param campaign
	 * @param ad
	 * @param creative
	 * @return
	 */
	private VideoCreativeEntity creativeVideoCreativeEntity(AdType adType,
			Campaign campaign, Ad ad, Creative creative, Integer adCategoryId)
			throws Exception {

		String creativeUrl = creative.getCreativeUrl();
		String extendName = FileUtil.getExtendName(creativeUrl);
		int format = getFormat(extendName);
		String size = creative.getSize();
		if (size.indexOf("x") == -1) {
			throw new Exception("解析创意尺寸错误[" + size + "]");
		}
		int width = Integer.parseInt(creative.getSize().split("x")[0]);// 创意宽
		int height = Integer.parseInt(creative.getSize().split("x")[1]);// 创意高

		String[] domains = getDomains(ad);

		Integer creativeType = null;
		switch (adType) {
		case PC_VIDEO:
		case APP_VIDEO:
		case WAP_VIDEO:
			creativeType = 0;
			break;
		default:
			return null;
		}
		Partner partner = partnerDao.getById(campaign.getPartnerId());
		VideoCreativeEntity videoCreativeEntity = new VideoCreativeEntity(ad
				.getId().toString(), adCategoryId, creative.getDuration()
				.intValue(), creativeUrl, "{!vam_click_url}{!dsp_click_url}",
				domains, width, height, format, partner.getPartnerName(),
				creativeType);
		return videoCreativeEntity;
	}

	/**
	 * 创建pc创意实体
	 * 
	 * @param ad
	 * @return
	 */
	private PcCreativeEntity createPcCreativeEntity(AdCategory adCategory,
			Campaign campaign, Ad ad, Creative creative, Integer adCategoryId)
			throws Exception {

		String html = "";// TODO 缺少html代码
		String creativeUrl = creative.getCreativeUrl();
		String extendName = FileUtil.getExtendName(creativeUrl);
		int format = getFormat(extendName);
		String size = creative.getSize();
		if (size.indexOf("x") == -1) {
			throw new Exception("解析创意尺寸错误[" + size + "]");
		}
		int width = Integer.parseInt(creative.getSize().split("x")[0]);// 创意宽
		int height = Integer.parseInt(creative.getSize().split("x")[1]);// 创意高

		String[] domains = getDomains(ad);

		PcCreativeEntity webCreativeEntity = new PcCreativeEntity(ad.getId()
				.toString(), width, height, format, adCategoryId, html, domains);
		return webCreativeEntity;
	}

	private String[] getDomains(Ad ad) throws Exception {

		String landingPage = ad.getLandingPage();
		String host = "";
		try {
			// 如果没有"http://"则加上
			landingPage = HTTPUtil.addHttpProtocol(landingPage);
			URL url = new URL(landingPage);
			// 如"http://www.baidu.com"则得到"www.baidu.com"
			host = url.getHost();
			// 以"."拆分"www.baidu.com"
			String[] fields = host.split("\\.");
			// 得到"baidu.com"
			host = fields[fields.length - 2] + "." + fields[fields.length - 1];

		} catch (MalformedURLException e) {
			throw new Exception("落地页[" + landingPage + "]地址格式错误");
		}

		return new String[] { host };
	}

	private int getFormat(String extendName) {
		if ("bmp".equalsIgnoreCase(extendName)
				|| "jpg".equalsIgnoreCase(extendName)
				|| "jpeg".equalsIgnoreCase(extendName)
				|| "png".equalsIgnoreCase(extendName)) {
			return 1;// static_pic
		}
		if ("gif".equalsIgnoreCase(extendName)) {
			return 2;// dynamic_pic
		}
		if ("swf".equalsIgnoreCase(extendName)) {
			return 3;// swf
		}

		// 文字链的判断方法未知，暂时不支持
		// if ("".equalsIgnoreCase(extendName)) {
		// return 4;// text
		// }

		if ("flv".equalsIgnoreCase(extendName)) {
			return 5;// fla
		}
		if ("mp4".equalsIgnoreCase(extendName)) {
			return 6;// mp4
		}

		return 1;
	}

}
