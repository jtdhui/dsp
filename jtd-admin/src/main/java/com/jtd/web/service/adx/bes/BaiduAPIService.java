package com.jtd.web.service.adx.bes;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jtd.commons.mybatis.support.CustomerContextHolder;

import com.jtd.utils.DateUtil;
import com.jtd.utils.HTTPUtil;
import com.jtd.utils.LikeBase64;
import com.jtd.web.adx.bes.APIAdvertiser;
import com.jtd.web.adx.bes.APIAdvertiserGetRequest;
import com.jtd.web.adx.bes.APICreativeAddRequest;
import com.jtd.web.service.adx.bes.BaiduUtil;
import com.jtd.web.adx.bes.BaseHttp;
import com.jtd.web.constants.AdType;
import com.jtd.web.constants.AuditStatus;
import com.jtd.web.constants.BaiduEsConstant;
import com.jtd.web.constants.CampaignType;
import com.jtd.web.constants.CatgSerial;
import com.jtd.web.controller.DicController;
import com.jtd.web.dao.*;
import com.jtd.web.po.Partner;
import com.jtd.web.po.PartnerStatusQualiDoc;
import com.jtd.web.po.QualiDocType;
import com.jtd.web.po.QualiDocTypeChannelCode;
import com.jtd.web.service.AuditResult;
import com.jtd.web.service.adx.IBaiduAPIService;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * 百度ADX的对接API
 * 
 * @author zhangrui
 *
 */
@Service
public class BaiduAPIService implements IBaiduAPIService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private Logger creativeAuditLog = LoggerFactory.getLogger("creativeAuditLog");

	@Autowired
	private IPartnerDao partnerDao;

	@Autowired
	private IQualiDocCustomerTypeChannelNeedDao qualiDocCustomerTypeChannelNeedDao;

	@Autowired
	private IQualiDocTypeChannelCodeDao qualiDocTypeChannelCodeDao;

	@Autowired
	private IQualiDocDao qualiDocDao;

	@Autowired
	private IPartnerStatusQualiDocDao partnerStatusQualiDocDao;

	@Autowired
	private IAdDao adDao;

	@Autowired(required=false)
	private HttpServletRequest request ;
	
	@Override
	public int syncAdvertiser(long submitUserId, long partnerId,
			String qualiDocImgBaseURL) {

		CustomerContextHolder
				.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);

		JSONObject advertiserData = queryAdvertiserById(partnerId);

		Partner partner = partnerDao.getById(partnerId);

		if (partner == null) {
			return 1;
		}

		// 封装查询客户的参数
//		ArrayList<Map<String, Object>> listParam = new ArrayList<Map<String, Object>>();
//		HashMap<String, Object> mapParam = new HashMap<String, Object>();
//		mapParam.put("advertiserId", partner.getId()); // 广告客户ID
//		mapParam.put("advertiserLiteName", partner.getPartnerName()); // 广告客户名称
//		mapParam.put("advertiserName", partner.getPartnerName()); // 广告客户资质名称
//		mapParam.put("siteName", partner.getWebsiteName()); // 网站名称
//		mapParam.put("siteUrl", partner.getWebsiteUrl()); // 网站URL
//		mapParam.put("telephone", partner.getCompanyTelephone()); // 联系电话
//		// mapParam.put("address", advisters.get(key)); //联系地址
//		listParam.add(mapParam);

//		JSONObject request = new JSONObject();
//		request.put("request", listParam);
//		request.put("authHeader",  BaiduUtil.getAuthHeader());
		
		APICreativeAddRequest request = new APICreativeAddRequest();
		request.setAuthHeader(BaseHttp.getAuthHeader());
		 List<APIAdvertiser> advertiserList =new ArrayList<APIAdvertiser>();
		 APIAdvertiser advertiser=new APIAdvertiser();
		 advertiser.setAdvertiserId(partner.getId());
		 advertiser.setAdvertiserLiteName(partner.getSimpleName());
		 advertiser.setAdvertiserName(partner.getPartnerName());
		 advertiser.setSiteName( partner.getWebsiteName());
		 advertiser.setSiteUrl(partner.getWebsiteUrl());
		 advertiser.setTelephone(partner.getContactTelephone());
		 advertiserList.add(advertiser);
		request.setRequest(advertiserList);
		int isWhiteUser = -1 ;
		
		int tryCnt = 1 ;
		
		while(tryCnt <= 3){
			
			logger.info("BaiduAPIService--syncAdvertiser，添加广告主，尝试第" + tryCnt + "次--request:\n" + request);
			
			JSONObject response = null;

			if (advertiserData == null) {

				// 发送添加请求
				try {
					/*response = HTTPUtil.post(BaiduUtil.ADVERTISER_ADD_URL,
							request.toJSONString());*/
					response=BaseHttp.post(BaiduEsConstant.ADVERTISER_ADD_URL, request);
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("BaiduAPIService--syncAdvertiser发生错误",e);
				}

			} else {

				// 发送修改请求
				try {
//					response = HTTPUtil.post(BaiduUtil.ADVERTISER_UPDATE_URL,
//							request.toJSONString());
					response=BaseHttp.post(BaiduEsConstant.ADVERTISER_UPDATE_URL, request);
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("BaiduAPIService--syncAdvertiser发生错误",e);
				}

			}

			logger.info("BaiduAPIService--syncAdvertiser，添加广告主，尝试第" + tryCnt + "次--response:\n" + response);

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
						}
					}
					
					break ;
					
				}
			}
			
			tryCnt++ ;
			
		}
		
		if(isWhiteUser == 0 || isWhiteUser == 1){
			
			logger.info("BaiduAPIService--syncAdvertiser，添加广告主，partnerId=" + partnerId + "，" + (isWhiteUser == 0 ? "非白名单":"属于白名单"));
			
			tryCnt = 1 ;
			
			//0：非白名单（需要上传资质）
			if(isWhiteUser == 0){
				
				while(tryCnt <= 3){
					
					int ret_code2 = uploadQualiDoc(submitUserId, partnerId,partner.getPartnerName(),
							partner.getQualiDocMainCustomerType(),
							partner.getQualiDocOptionalCustomerType(),
							tryCnt);
					
					tryCnt++ ;
					
					if(ret_code2 == 0){
						
						logger.info("BaiduAPIService--syncAdvertiser，上传广告主资质成功，partnerId=" + partnerId);
						
						return 0 ;
					}
				}
			}
			else{
				
				logger.info("BaiduAPIService--syncAdvertiser，白名单不需要上传广告主资质，partnerId=" + partnerId);
				
				return 100 ;
			}
		}
		
		return 1;

	}

	@Override
	public JSONObject queryAdvertiserById(long partnerId) {
//		JSONObject request = new JSONObject();
//		request.put("advertiserIds", new long[]{partnerId});
//		request.put("authHeader", BaiduUtil.getAuthHeader());
		APIAdvertiserGetRequest request = new APIAdvertiserGetRequest();
		request.setAuthHeader(BaseHttp.getAuthHeader());
		Long[] advertiserIds = new Long[] { partnerId };
		request.setAdvertiserIds(Arrays.asList(advertiserIds));
		logger.info("BaiduAPIService--queryAdvertiserById--request:\n" + request);

		// 发送查询请求
		JSONObject response = null;
		try {
//			response = HTTPUtil.post(BaiduUtil.ADVERTISER_GET_URL,
//					request.toJSONString());
			response =BaseHttp.post(BaiduEsConstant.ADVERTISER_GET_URL, request);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("BaiduAPIService--queryAdvertiserById 发生错误",e);
		}
		// {"response":[{"address":null,"advertiserId":74,"telephone":"400-6626626","advertiserName":"孩之宝","siteUrl":"http://www.hasbro.com","isWhiteUser":0,"
		// advertiserLiteName":"孩之宝商贸（中国）有限公司","siteName":"孩之宝商贸（中国）有限公司"}],"status":0,"errors":[]}

		logger.info("BaiduAPIService--queryAdvertiserById--response:\n"
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

	/**
	 * 上传资质
	 * 
	 * @param submitUserId
	 * @param partnerId
	 * @param qualiMainCustomerType
	 * @param qualiOptionCustomerType
	 */
	private int uploadQualiDoc(long submitUserId, long partnerId, String partnerName ,
			Long qualiMainCustomerType, Long qualiOptionCustomerType, int tryCnt) {

		CustomerContextHolder
				.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);

		long channelId = CatgSerial.BES.getChannelId();

		HashMap<String, Object> mainAPIAdvertiserLicence = new HashMap<String, Object>();
		List<Map<String, Object>> optionalAPIAdvertiserLicenceList = new ArrayList<Map<String, Object>>();

		// 记录主体资质是否曾经上报过
		boolean isMainQualiHasUploaded = false;
		// 记录可选资质是否曾经上报过
		boolean isOptionalQualiHasUploaded = false;

		// 主体资质
		if (qualiMainCustomerType != null) {

			// 查询百度对于qualiMainCustomerType需要的主体资质（每个渠道对同一种客户类型，需要的主体资质类型和个数，也许是不一样的）
			List<QualiDocType> baiduMainDocTypeList = qualiDocCustomerTypeChannelNeedDao
					.listDocTypeForChannel(CatgSerial.BES.getChannelId(),
							qualiMainCustomerType, 1);

			if (baiduMainDocTypeList != null && baiduMainDocTypeList.size() > 0) {
				QualiDocType mainDocType = baiduMainDocTypeList.get(0);

				// 查询当前资质类型在百度里对应的编号
				QualiDocTypeChannelCode docChannelCode = qualiDocTypeChannelCodeDao
						.findBy(CatgSerial.BES.getChannelId(),
								mainDocType.getId());
				String docChannelCodeStr = docChannelCode.getChannelCode();

				// 查询用户上传的当前资质类型的文件
				Map<String, Object> mainDoc = qualiDocDao
						.findByPartnerIdAndDocType(partnerId,
								mainDocType.getId());
				if (mainDoc != null) {

					long docId = (long) mainDoc.get("id");
					long docTypeId = (long) mainDoc.get("doc_type_id");

					//资质类型
					mainAPIAdvertiserLicence.put("type", docChannelCodeStr);
					
					//资质名称
					mainAPIAdvertiserLicence.put("name", partnerName);
					
					//资质编号
					mainAPIAdvertiserLicence.put("number",
							mainDoc.get("doc_number"));
					
					//资质有效期，如果未填写有效期，就默认填写2030-12-31
					mainAPIAdvertiserLicence.put("validDate",
							mainDoc.get("doc_valid_date") != null ? mainDoc.get("doc_valid_date") : BaiduUtil.DEFAULT_DOC_VALID_DATE);
					
					
					String host = request != null ? request.getHeader("Host") : "http://www.xinmt.com/" ;
					String contextPath = request != null ? request.getContextPath() : "dspadmin" ;
					String url = host + contextPath + "/admin/qualidoc/showImage.action?id=" + docId ;
					
					List<String> imgUrls = new ArrayList<String>();
					//imgUrls.add(url);
					imgUrls.add("https://mjs.sinaimg.cn/wap/online/public/images/addToHome/sina_57X57_v1.png");
					mainAPIAdvertiserLicence.put("imgUrls", imgUrls);
					
//					List<String> imgDatas = new ArrayList<String>();
//					String filePath = (String) mainDoc.get("doc_path") + "/"
//							+ (String) mainDoc.get("doc_name");
//					File file = new File(filePath);
//					String base64String = FileUtil.getBase64String(file);
//					imgDatas.add(base64String);
//					mainAPIAdvertiserLicence.put("imgDatas", imgDatas);

					// 查询partner是否曾经提交过相应类型的资质
					PartnerStatusQualiDoc history = partnerStatusQualiDocDao
							.findWhenChannelAudit(partnerId, docTypeId,
									channelId);
					if (history != null) {
						// 如果上传过，则修改提交记录，用isMainQualiHasUploaded标识符控制之后调用adx的修改资质接口
						isMainQualiHasUploaded = true;
						partnerStatusQualiDocDao.updateWhenChannelAudit(docId,
								submitUserId, partnerId, docTypeId, channelId);
					} else {
						// 将此主体资质记入partner_status_quali_doc表（资质送审记录）
						this.insertPartnerStatusQualiDoc(submitUserId,
								partnerId, channelId, docId, docTypeId, 1);
					}

				} else {
					// 如果查不到主体资质，则直接退出
					return 1;
				}

			} else {
				// 如果查不到渠道对资质要求的配置，则直接退出
				return 1;
			}
		}

		// 可选资质
		if (qualiOptionCustomerType != null) {
			// 查询百度对于qualiMainCustomerType需要的可选资质
			List<QualiDocType> baiduOptionDocTypeList = qualiDocCustomerTypeChannelNeedDao
					.listDocTypeForChannel(CatgSerial.BES.getChannelId(),
							qualiOptionCustomerType, 0);

			if (baiduOptionDocTypeList != null) {

				for (Iterator<QualiDocType> iterator = baiduOptionDocTypeList
						.iterator(); iterator.hasNext();) {
					QualiDocType optDocType = (QualiDocType) iterator.next();

					// 查询当前资质类型在百度里对应的编号
					QualiDocTypeChannelCode docChannelCode = qualiDocTypeChannelCodeDao
							.findBy(CatgSerial.BES.getChannelId(),
									optDocType.getId());
					String docChannelCodeStr = docChannelCode.getChannelCode();

					// 查询用户上传的当前资质类型的文件
					Map<String, Object> optDoc = qualiDocDao
							.findByPartnerIdAndDocType(partnerId,
									optDocType.getId());
					if (optDoc != null) {

						long docId = (long) optDoc.get("id");
						long docTypeId = (long) optDoc.get("doc_type_id");

						HashMap<String, Object> optAPIAdvertiserLicence = new HashMap<String, Object>();

						optAPIAdvertiserLicence.put("type", docChannelCodeStr);
						optAPIAdvertiserLicence.put("name",partnerName);
						optAPIAdvertiserLicence.put("number",
								optDoc.get("doc_number"));
						optAPIAdvertiserLicence.put("validDate",
								optDoc.get("doc_valid_date"));
						
						String host = request.getHeader("Host");
						String url = host + request.getContextPath() + "/admin/qualidoc/showImage.action?id=" + docId ;
						
						List<String> imgUrls = new ArrayList<String>();
						//imgUrls.add(url);
						imgUrls.add("https://mjs.sinaimg.cn/wap/online/public/images/addToHome/sina_57X57_v1.png");
						optAPIAdvertiserLicence.put("imgUrls", imgUrls);

//						List<String> imgDatas = new ArrayList<String>();
//						String filePath = (String) optDoc.get("doc_path") + "/"
//								+ (String) optDoc.get("doc_name");
//						File file = new File(filePath);
//						String base64String = FileUtil.getBase64String(file);
//						imgDatas.add(base64String);
//						optAPIAdvertiserLicence.put("imgDatas", imgDatas);

						optionalAPIAdvertiserLicenceList
								.add(optAPIAdvertiserLicence);

						// 查询partner是否曾经提交过相应类型的资质
						PartnerStatusQualiDoc history = partnerStatusQualiDocDao
								.findWhenChannelAudit(partnerId, docTypeId,
										channelId);
						if (history != null) {
							// 如果上传过，则修改提交记录，用isMainQualiHasUploaded标识符控制之后调用adx的修改资质接口
							isOptionalQualiHasUploaded = true;
							partnerStatusQualiDocDao.updateWhenChannelAudit(
									docId, submitUserId, partnerId, docTypeId,
									channelId);
						} else {
							// 将此主体资质记入partner_status_quali_doc表（资质送审记录）
							this.insertPartnerStatusQualiDoc(submitUserId,
									partnerId, channelId, docId, docTypeId, 0);
						}

					}
				}
			}
		}

		Map<String, Object> APIAdvertiserQualificationUpload = new HashMap<String, Object>();
		APIAdvertiserQualificationUpload.put("advertiserId", partnerId);
		APIAdvertiserQualificationUpload.put("mainLicence",
				mainAPIAdvertiserLicence);
		APIAdvertiserQualificationUpload.put("optionalLicences",
				optionalAPIAdvertiserLicenceList);

		List<Map<String, Object>> qualifications = new ArrayList<Map<String, Object>>();
		qualifications.add(APIAdvertiserQualificationUpload);

		JSONObject request = new JSONObject();
		request.put("qualifications", qualifications);
		//request.put("authHeader", BaiduUtil.getAuthHeader());
		request.put("authHeader", BaseHttp.getAuthHeader());

		JSONObject response = null;
		
		if (isMainQualiHasUploaded) {

			APIAdvertiserQualificationUpload.remove("optionalLicences");
			
			logger.info("BaiduAPIService--uploadQualiDoc--修改主体资质，尝试第" + tryCnt + "次--request:\n" + request);

			try {
//				response = HTTPUtil.post(
//						BaiduUtil.ADVERTISER_UPDATE_MAIN_QUALIFICATION_URL,
//						request.toJSONString());
				response=BaseHttp.post(BaiduEsConstant.ADVERTISER_UPDATE_MAIN_QUALIFICATION_URL, request);
			} catch (Exception e) {
				logger.error("BaiduAPIService--uploadQualiDoc发生错误",e);
			}

			logger.info("BaiduAPIService--uploadQualiDoc--修改主体资质，尝试第" + tryCnt + "次--response:\n"
					+ response);

		}
		if (isOptionalQualiHasUploaded) {

			APIAdvertiserQualificationUpload.remove("mainLicence");
			
			logger.info("BaiduAPIService--uploadQualiDoc--修改可选资质，尝试第" + tryCnt + "次--request:\n" + request);

			try {
//				response = HTTPUtil.post(
//						BaiduUtil.ADVERTISER_UPDATE_OPTIONAL_QUALIFICATION_URL,
//						request.toJSONString());
				response=BaseHttp.post(BaiduEsConstant.ADVERTISER_UPDATE_OPTIONAL_QUALIFICATION_URL, request);
			} catch (Exception e) {
				logger.error("BaiduAPIService--uploadQualiDoc发生错误",e);
			}

			logger.info("BaiduAPIService--uploadQualiDoc--修改可选资质，尝试第" + tryCnt + "次--response:\n"
					+ response);

		}
		if (!isMainQualiHasUploaded && !isOptionalQualiHasUploaded) {
			
			logger.info("BaiduAPIService--uploadQualiDoc--上传所有资质，尝试第" + tryCnt + "次--request:\n" + request);

			try {
//				response = HTTPUtil.post(
//						BaiduUtil.ADVERTISER_UPLOAD_QUALIFICATION_URL,
//						request.toJSONString());
				response=BaseHttp.post(BaiduEsConstant.ADVERTISER_UPLOAD_QUALIFICATION_URL, request);
			} catch (Exception e) {
				logger.error("BaiduAPIService--uploadQualiDoc发生错误",e);
			}

			logger.info("BaiduAPIService--uploadQualiDoc--上传所有资质，尝试第" + tryCnt + "次--response:\n"
					+ response);

		}
		
		if(response != null){
			// 接口调用状态，0=无错误，1=部分错误，2=全部错误
			int ret_code = response.getIntValue("status");
			logger.info("BaiduAPIService--uploadQualiDoc--ret_code:" + ret_code);
			if (ret_code == 0) {
				return 0 ;
			}
			else{
				return ret_code ;
			}
		}
		return 1 ;
	}

	private void insertPartnerStatusQualiDoc(long submitUserId, long partnerId,
			long channelId, long qualiDocId, long docTypeId, int isMainDoc) {

		CustomerContextHolder
				.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);

		PartnerStatusQualiDoc psqd = new PartnerStatusQualiDoc();
		psqd.setPartnerId(partnerId);
		psqd.setSubmitUserId(submitUserId);
		psqd.setChannelId(channelId);
		psqd.setQualiDocId(qualiDocId);
		psqd.setDocTypeId(docTypeId);
		psqd.setIsMainDoc(isMainDoc);

		partnerStatusQualiDocDao.insert(psqd);
	}

	@Override
	public AuditResult queryAdvertiserAuditStatus(long partnerId) {
		
		AuditResult auditResult = new AuditResult();
		CustomerContextHolder
				.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		JSONObject request = new JSONObject();
		request.put("advertiserIds", new long[]{partnerId});
		request.put("authHeader",BaseHttp.getAuthHeader());

		logger.info("BaiduAPIService--queryAdvertiserAuditStatus--request:\n"
				+ request);
		
		JSONObject response = null;
		try {
			response=BaseHttp.post(BaiduEsConstant.ADVERTISER_GET_URL,request);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("BaiduAPIService--queryAdvertiserAuditStatus发生错误",e);
		}
		logger.info("BaiduAPIService--queryAdvertiserAuditStatus--response:\n"
				+ response);
		if (response != null) {
			//{"response":[{"state":0,"advertiserId":158,"refuseReason":""}],"status":0,"errors":[]}
			int status = response.getInteger("status");
			if(status == 0){
				JSONArray responseArray = response.getJSONArray("response");
				if(responseArray != null && responseArray.size() > 0){
					JSONObject response0 = responseArray.getJSONObject(0);
					int isWhiteUser = response0.getInteger("isWhiteUser");
					if(isWhiteUser==0) {
						// 0：审核通过； 1：审核中；2：审核拒绝；3：缺省状态
						JSONObject qualificationeRquest = new JSONObject();
						qualificationeRquest.put("advertiserId", partnerId);
						qualificationeRquest.put("authHeader",BaseHttp.getAuthHeader());
						qualificationeRquest.put("needLicenceImgUrl",true);
						logger.info("BaiduAPIService--queryQualificationInfoAuditStatus--request:\n"
								+ request);
						JSONObject qualificationResponse = null;
						try {
							qualificationResponse=BaseHttp.post(BaiduEsConstant.ADVERTISER_QUERY_QUALIFICATION_URL,qualificationeRquest);

						} catch (Exception e) {
							e.printStackTrace();
							logger.error("BaiduAPIService--queryQualificationInfoAuditStatus发生错误",e);
						}
						if(null!=qualificationResponse) {
							int auditStatus = qualificationResponse.getIntValue("auditState");
							switch (auditStatus) {
								case 0:
									auditResult.setAuditStatus(AuditStatus.STATUS_SUCCESS);
									break;
								case 1:
									auditResult.setAuditStatus(AuditStatus.STATUS_WAIT);
									break;
								case 2:
									auditResult.setAuditStatus(AuditStatus.STATUS_FAIL);
									break;
								case 3:
									auditResult.setAuditStatus(AuditStatus.STATUS_COMMIT_FAIL);
									break;
								default:
									auditResult.setAuditStatus(AuditStatus.STATUS_COMMIT_FAIL);
									break;
							}
						}else{
							auditResult.setAuditStatus(AuditStatus.STATUS_NOTCOMMIT);
						}
					}
					if(isWhiteUser==1) {
						auditResult.setAuditStatus(AuditStatus.STATUS_SUCCESS);
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
	public int syncCreative(long adId) {

		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);

		// 查询创意信息
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("id", adId);
		Map<String, Object> ad = adDao.findAdMapById(map);

		if (ad == null) {
			return 1;
		}

		// 封装查询客户的参数
		ArrayList<Map<String, Object>> listParam = new ArrayList<Map<String, Object>>();
		HashMap<String, Object> mapParam = new HashMap<String, Object>();
		// 此 id 为 dsp 系统的创意 id，需唯一
		mapParam.put("creativeId", ad.get("id"));
		
		// 流量类型，非必填 1：Web 流量 2：Mobile 流量 3：Video 流量，默认为 1，在我dsp中对应活动表的活动类型
		CampaignType campType = CampaignType.fromCode(ad.get("campaign_type") != null ? (int)ad.get("campaign_type") : -1);
		if(campType == CampaignType.PC){
			mapParam.put("adviewType", 1);
		}
		else if(campType == CampaignType.APP){
			mapParam.put("adviewType", 2);
		}
		
		// 创意类型，必填 1：图片 2：flash 3：视频 4：图文（原生/信息流），在我dsp中对应活动表的广告形式
		AdType adType = AdType.getAdType(ad.get("ad_type") == null ? (int)ad.get("ad_type") : -1);
		if(adType == AdType.PC_BANNER || adType == AdType.APP_BANNER){
			mapParam.put("type", 1);
		}
		else if(adType == AdType.PC_VIDEO || adType == AdType.APP_VIDEO || adType == AdType.WAP_VIDEO){
			mapParam.put("type", 3);
		}
		else{
			mapParam.put("type", 1); 
		}

		String baseUrl = "http://58.87.67.115/nc?q=%%EXT_DATA%%";


		// 创意 URL 1.API 会在后台访问创意的 URL地址，抓取图片或者 flash创意 2.图文创意该字段不生效，请使用
        String creativeUrl = ad.get("creative_url").toString();
        if(creativeUrl.startsWith("http") == false){
            creativeUrl = "http://"+creativeUrl;
        }
        mapParam.put("creativeUrl", creativeUrl);
		// 点击链接，当 adviewType 为 2 时，且创意尺寸为 640*960、 480*800 时，该字段为非必填，其余情况下为必填

        String targetUrl = ad.get("click_url").toString();
        if(targetUrl.startsWith("http") == false){
            targetUrl = "http://"+targetUrl;
        }
		String baseUrl2 = "http://58.87.67.115/nl?u="+targetUrl;
        mapParam.put("targetUrl", baseUrl + "&r=" + LikeBase64.encode(targetUrl));
//		mapParam.put("targetUrl",targetUrl);
		// 到达页面，当 adviewType 为 2 时，且创意尺寸为 640*960、 480*800 时，该字段为非必填，其余情况下为必填
        String landingPage = ad.get("landing_page").toString();
        if(landingPage.startsWith("http") == false){
            landingPage = "http://"+landingPage;
        }
//        mapParam.put("landingPage", landingPage);
		mapParam.put("landingPage",baseUrl2 + "&r=" + landingPage);
		String pv_urls = (String) ad.get("pv_urls");
        List<String> pvList = new ArrayList<String>();
       //pvList.add("http://counter.xinmt.com/np?q=%%EXT_DATA%%&p=%%PRICE%%");
		pvList.add("http://58.87.67.115/np?q=%%EXT_DATA%%&p=%%PRICE%%");
		if (StringUtils.isEmpty(pv_urls) == false) {
			JSONArray array = JSONObject.parseArray(pv_urls);
			// 广告展现监测链接数组，必填 ，最多包含 3 个链接，最多包含 3 个链接
			if(array != null){
				 for(int i=0;i<array.size();i++){
					if(i>=2){
						break;
					}
					pvList.add(array.getString(i).toString());
				 }
			}
//			mapParam.put("monitorUrls", Arrays.asList(array);
//			mapParam.put("monitorUrls", pvList);
		}
        mapParam.put("monitorUrls", pvList);

		String creative_size = (String) ad.get("creative_size");
		if (StringUtils.isEmpty(creative_size) == false) {
			try {
				// 创意宽，必填
				mapParam.put("width",
						Integer.parseInt(creative_size.split("x")[0]));
				// 创意高，必填
				mapParam.put("height",
						Integer.parseInt(creative_size.split("x")[1]));
				
			} catch (Exception e) {
				logger.error("BaiduCreativeService--syncCreative，尺寸存在错误", e);
				return 1;
			}
		}

		mapParam.put("advertiserId", ad.get("partner_id")); // 广告主 id，必填

		Long dspCategoryId = Long.parseLong((String) ad.get("parter_catgid"));
		Long besCategoryId = DicController.getCatgSerial(dspCategoryId,
				CatgSerial.BES);
		// 创意所属广告行业，必填 注：必须指定到第 2 级行业。广告行业体系见数据字典
		mapParam.put("creativeTradeId", besCategoryId);

		// 当 adviewType 为 2 时，该字段为必填
		// mapParam.put("interactiveStyle", "");
		// 当 adviewType 为 2 且interactiveStyle 为 1 时，该字段为必填；
		// mapParam.put("telNo", "");

		listParam.add(mapParam);

		JSONObject request = new JSONObject();
		request.put("request", listParam);
		//request.put("authHeader", BaiduUtil.getAuthHeader());
		request.put("authHeader", BaseHttp.getAuthHeader());


		JSONObject creative = queryCreativeById(adId);
		logger.info(adId+"在百度的创意值:"+creative);
		int tryCnt = 1 ;
		
		while(tryCnt <= 3){
			
			JSONObject response = null;

			logger.info("BaiduAPIService--syncCreative，尝试第" + tryCnt + "次--request:\n" + request);

			if (creative == null) {

				// 发送添加请求
				try {
//					response = HTTPUtil.post(BaiduUtil.CREATIVE_ADD_URL,
//							request.toJSONString());
					response = HTTPUtil.post(BaiduEsConstant.CREATIVE_ADD_URL,
							request.toJSONString());
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("BaiduAPIService--syncCreative发生错误",e);
				}

			} else {

				// 发送修改请求
				try {
					response = HTTPUtil.post(BaiduEsConstant.CREATIVE_UPDATE_URL,
							request.toJSONString());
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("BaiduAPIService--syncCreative发生错误",e);
				}

			}

			logger.info("BaiduAPIService--syncCreative，尝试第" + tryCnt + "次--response:\n" + response);

			if (response != null) {

				// 接口调用状态，0=无错误，1=部分错误，2=全部错误
				int ret_code = response.getIntValue("status");
				if (ret_code == 0) {

					return 0;
				}
			}
			
			tryCnt++ ;
		}
		
		return 1;

	}

	@Override
	public JSONObject queryCreativeById(long adId) {

		JSONObject request = new JSONObject();
		request.put("creativeIds", new long[]{adId});
		//request.put("authHeader", BaiduUtil.getAuthHeader());
		request.put("authHeader", BaseHttp.getAuthHeader());

		logger.info("BaiduAPIService--queryCreativeById--request:\n"
				+ request);

		// 发送查询请求
		JSONObject response = null;
		try {
//			response = HTTPUtil.post(BaiduUtil.CREATIVE_GET_URL,
//					request.toJSONString());
			response = HTTPUtil.post(BaiduEsConstant.CREATIVE_GET_URL,
					request.toJSONString());
		} catch (Exception e) {
			logger.error("BaiduAPIService--queryCreativeById发生错误",e);
		}

		// {"response":[{"adviewType":2,"appName":"","interactiveStyle":0,"telNo":"18611563258","downloadUrl":"http://www.baidu.com.cn","appDesc":"",
		// "appPackageSize":0.0,"state":1,"type":1,"height":50,"width":320,"advertiserId":76,"creativeId":215,"targetUrl":"http://www.baidu.com.cn",
		// "creativeUrl":"http://223.203.216.232:9098/20150922_KxxK8Xan.jpg","landingPage":"http://www.baidu.com.cn",
		// "monitorUrls":["http://monitor.baidu.com.cn/0","http://monitor.baidu.com.cn/1"],"creativeTradeId":7801,"frameAgreementNo":null,"refuseReason":null}],"status":0,"errors":[]}

		logger.info("BaiduAPIService--queryCreativeById--response:\n"
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
	public AuditResult queryCreativeAuditStatus(long adId) {
		
		AuditResult auditResult = new AuditResult();

		JSONObject request = new JSONObject();
		request.put("creativeIds", new long[]{adId});
//		request.put("authHeader", BaiduUtil.getAuthHeader());
		request.put("authHeader",BaseHttp.getAuthHeader());

//		creativeAuditLog.info("BaiduAPIService--queryCreativeAuditStatus--request:\n"
//				+ request);
		logger.info("BaiduAPIService--queryCreativeAuditStatus--request:\n"
				+ request);
		
		JSONObject response = null;
		try {
//			response = HTTPUtil.post(
//					BaiduUtil.CREATIVE_QUERY_AUDIT_STATE_URL,
//					request.toJSONString());
			response = HTTPUtil.post(BaiduEsConstant.CREATIVE_QUERY_AUDIT_STATE_URL,
					request.toJSONString());
		} catch (Exception e) {
			creativeAuditLog.error("BaiduAPIService--queryCreativeAuditStatus发生错误",e);
		}

//		creativeAuditLog.info("BaiduAPIService--queryCreativeAuditStatus--response:\n"
//				+ response);
		logger.info("BaiduAPIService--queryCreativeAuditStatus--response:\n"
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
	
	@Override
	public void syncSSPSetting() {

		int pageSize = 500 ;
		
		JSONObject request = new JSONObject();
		request.put("pageIndex", 1);
		request.put("pageSize", pageSize);
		request.put("authHeader", BaiduUtil.getAuthHeader());
		
		logger.info("BaiduAPIService-----syncSSPSetting ------request----- " + request);

		JSONObject response = null;
		try {
			response = HTTPUtil.post(
					BaiduUtil.VIEWCONFIG_SSP_SETTING_URL, request.toJSONString());
		} catch (Exception e) {
			logger.error("BaiduAPIService--syncSSPSetting-----发送http请求发生错误",e);
		}

		//logger.info("BaiduAPIService-----syncSSPSetting ------response----- " + response);

		if(response != null){
			
			logger.info("BaiduAPIService.syncSSPSetting ---------settingPath = " + BaiduUtil.SETTING_PATH);
			
			if(StringUtils.isEmpty(BaiduUtil.SETTING_PATH)){
				logger.error("BaiduAPIService.syncSSPSetting ----- settingPath配置为空");
				return ;
			}
			
			//如果文件夹不存在则创建
			File settingDir = new File(BaiduUtil.SETTING_PATH);
			if(settingDir.exists() == false){
				settingDir.mkdirs();
			}
			
			String fileName = "" ;
			if(BaiduUtil.SETTING_PATH.endsWith(File.separator) == false){
				fileName = BaiduUtil.SETTING_PATH + File.separator + DateUtil.getYYYYMMdd(new Date()) + ".txt" ;
			}
			else{
				fileName = BaiduUtil.SETTING_PATH + DateUtil.getYYYYMMdd(new Date()) + ".txt" ;
			}
			
			logger.info("BaiduAPIService.syncSSPSetting ---------fileName = " + fileName);
			
			//如果文件不存在则创建
			File settingFile = new File(fileName);
			if(settingFile.exists() == false){
				try {
					settingFile.createNewFile();
				} catch (IOException e) {
					logger.error("BaiduAPIService.syncSSPSetting ----- 创建settingFile出错,fileName=" + fileName,e);
					return ;
				}
			}
			
			//创建文件流
			FileWriter fileWriter = null ;
			try {
				fileWriter = new FileWriter(settingFile);
			} catch (IOException e) {
				logger.error("BaiduAPIService.syncSSPSetting ----- 创建fileWriter出错,fileName=" + fileName,e);
				return ;
			}
			
			Integer pageTotal = response.getInteger("pageTotal");
			
			logger.info("BaiduAPIService.syncSSPSetting ---------pageSize = " + pageSize);
			logger.info("BaiduAPIService.syncSSPSetting ---------pageTotal = " + pageTotal);
			
			if(pageTotal != null){
				for (int i = 1; i <= pageTotal; i++) {
					
					//{"response":[{"modTime":1483216219000,"settingContent":"{\"settingId\":1,\"excludedAdvertiserWebsiteUrl\":[],\"excludedKeyword\":[],
					// \"excludedProductCategoryIdList\":[],\"isExcludedVulgar\":null}","settingId":1,"addTime":1483216219000},........................................],
					// "errors":[],"pageTotal":3,"status":0,"pageSize":9000,"rowCount":18272}
					JSONObject pageResponse = getSyncSSPSetting(i,pageSize);
					if(pageResponse != null){
						
						JSONArray settingArray = pageResponse.getJSONArray("response");
						if(pageResponse != null){
							
							logger.info("BaiduAPIService.syncSSPSetting ---------settingArray.size = " + settingArray.size());
							
							for (int j = 0; j < settingArray.size() ; j++) {
								
								JSONObject setting = settingArray.getJSONObject(j);
								
								String settingContent = setting.getString("settingContent");
								
								//"{\"settingId\":1,\"excludedAdvertiserWebsiteUrl\":[],\"excludedKeyword\":[],\"excludedProductCategoryIdList\":[],\"isExcludedVulgar\":null}"
								
								if(settingContent != null){
									
									settingContent = settingContent.replaceAll("settingId", "setting_id");
									settingContent = settingContent.replaceAll("excludedAdvertiserWebsiteUrl", "excluded_advertiser_website_url");
									settingContent = settingContent.replaceAll("excludedKeyword", "excluded_keyword");
									settingContent = settingContent.replaceAll("excludedProductCategoryIdList", "excluded_product_category_id_list");
									settingContent = settingContent.replaceAll("isExcludedVulgar", "is_excluded_vulgar");
									settingContent += "\n" ;
									try {
										//每天第一次都是将原内容覆盖，之后都是追加
										if(i == 1){
											fileWriter.write(settingContent);
										}
										else{
											fileWriter.append(settingContent);
										}
									} catch (IOException e) {
										logger.error("BaiduAPIService-----syncSSPSetting，执行写入出错, settingContent=" + settingContent , e);
									}
								}
							}
							
						}
					}
				}
			}
			
			try {
				fileWriter.close();
			} catch (IOException e) {
				logger.error("BaiduAPIService.syncSSPSetting ----- 关闭fileWriter出错",e);
			}
		}
		
		String yesterdayFileName = BaiduUtil.SETTING_PATH + (BaiduUtil.SETTING_PATH.endsWith(File.separator) ? "" : File.separator) + DateUtil.getYestodayStr() + ".txt" ;
		
		logger.info("BaiduAPIService.syncSSPSetting ---------yesterdayFileName = " + yesterdayFileName);
		
		//如果文件不存在则创建
		File yesterdaySettingFile = new File(yesterdayFileName);
		if(yesterdaySettingFile.exists()){
			yesterdaySettingFile.delete();
			logger.info("BaiduAPIService.syncSSPSetting ----- 删除昨日setting文件,fileName=" + yesterdayFileName );
		}
		
		logger.info("BaiduAPIService-----syncSSPSetting ------完毕----- ");
		
	}
	
	public JSONObject getSyncSSPSetting(int pageIndex,int pageSize) {

		JSONObject request = new JSONObject();
		request.put("pageIndex", pageIndex);
		request.put("pageSize", pageSize);
		request.put("authHeader", BaiduUtil.getAuthHeader());
		
		JSONObject response = null;
		try {
			response = HTTPUtil.post(
					BaiduUtil.VIEWCONFIG_SSP_SETTING_URL, request.toJSONString());
		} catch (Exception e) {
			logger.error("BaiduAPIService--syncSSPSetting发生错误",e);
			return null ;
		}
		
		return response ;
	}

	public JSONObject queryAllAdvertiser(String startDate,String endDate){
		
		JSONObject request = new JSONObject();
		request.put("authHeader", BaiduUtil.getAuthHeader());
		request.put("startDate", startDate);
		request.put("endDate", endDate);
		
		logger.info("BaiduAPIService.getAllAdvertiser --- request:\n" + request);
		
		JSONObject response = null;
		try {
			response = HTTPUtil.post(
					BaiduUtil.ADVERTISER_GETALL_URL, request.toJSONString());
		} catch (Exception e) {
			logger.error("BaiduAPIService--getAllAdvertiser发生错误",e);
			return null ;
		}
		
		logger.info("BaiduAPIService.getAllAdvertiser --- response:\n" + response);
		
		return response ;
	}
	
	public JSONObject queryAllCreative(String startDate,String endDate){
		
		JSONObject request = new JSONObject();
		request.put("authHeader", BaiduUtil.getAuthHeader());
		request.put("startDate", startDate);
		request.put("endDate", endDate);
		
		logger.info("BaiduAPIService.getAllCreative --- request:\n" + request);
		
		JSONObject response = null;
		try {
			response = HTTPUtil.post(
					BaiduUtil.CREATIVE_GETALL_URL, request.toJSONString());
		} catch (Exception e) {
			logger.error("BaiduAPIService--getAllCreative发生错误",e);
			return null ;
		}
		
		logger.info("BaiduAPIService.getAllCreative --- response:\n" + response);
		
		return response ;
	}
	
	public JSONObject reportRTB(String startDate,String endDate){
		
		JSONObject request = new JSONObject();
		request.put("authHeader", BaiduUtil.getAuthHeader());
		request.put("startDate", startDate);
		request.put("endDate", endDate);
		
		logger.info("BaiduAPIService.reportRTB --- request:\n" + request);
		
		JSONObject response = null;
		try {
			response = HTTPUtil.post(
					BaiduUtil.REPORT_RTB_URL, request.toJSONString());
		} catch (Exception e) {
			logger.error("BaiduAPIService--reportRTB发生错误",e);
			return null ;
		}
		
		logger.info("BaiduAPIService.reportRTB --- response.length :\n" + (response != null ? response.toJSONString().length() : "null") );
		
		return response ;
	}
	
	public JSONObject reportConsume(String startDate,String endDate){
		
		JSONObject request = new JSONObject();
		request.put("authHeader", BaiduUtil.getAuthHeader());
		request.put("startDate", startDate);
		request.put("endDate", endDate);
		
		logger.info("BaiduAPIService.reportConsume --- request:\n" + request);
		
		JSONObject response = null;
		try {
			response = HTTPUtil.post(
					BaiduUtil.REPORT_CONSUME_URL, request.toJSONString());
		} catch (Exception e) {
			logger.error("BaiduAPIService--reportConsume发生错误",e);
			return null ;
		}
		
		logger.info("BaiduAPIService.reportConsume --- response.length :\n" + (response != null ? response.toJSONString().length() : "null") );
		
		return response ;
	}
	
	public JSONObject reportAdvertiserConsume(String startDate,String endDate){
		
		JSONObject request = new JSONObject();
		request.put("authHeader", BaiduUtil.getAuthHeader());
		request.put("startDate", startDate);
		request.put("endDate", endDate);
		
		logger.info("BaiduAPIService.reportAdvertiserConsume --- request:\n" + request);
		
		JSONObject response = null;
		try {
			response = HTTPUtil.post(
					BaiduUtil.REPORT_ADVERTISER_CONSUME_URL, request.toJSONString());
		} catch (Exception e) {
			logger.error("BaiduAPIService--reportAdvertiser发生错误",e);
			return null ;
		}
		
		logger.info("BaiduAPIService.reportAdvertiserConsume --- response.length :\n" + (response != null ? response.toJSONString().length() : "null") );
		
		return response ;
	}
	
	public JSONArray reportCreativeRTB(String startDate,String endDate){
		
		JSONObject request = new JSONObject();
		request.put("authHeader", BaiduUtil.getAuthHeader());
		request.put("startDate", startDate);
		request.put("endDate", endDate);
		
		logger.info("BaiduAPIService.reportCreativeRTB --- request:\n" + request);
		
//		File file = new File("d:/aaa.txt");
//		StringBuffer sb = new StringBuffer();
//		try {
//			FileReader fr = new FileReader(file);
//			BufferedReader br = new BufferedReader(fr);
//			if(br != null){
//				String s = null ;
//				while((s = br.readLine()) != null){
//					sb.append(s);
//				}
//			}
//			
//			fr.close();
//			br.close();
//			
//		} catch (Exception e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		
//		JSONObject response = JSONObject.parseObject(sb.toString());
		
		JSONObject response = null ;
		try {
			response = HTTPUtil.post(
					BaiduUtil.REPORT_CREATIVE_RTB_URL, request.toJSONString());
		} catch (Exception e) {
			logger.error("BaiduAPIService--reportCreativeRTB发生错误",e);
			return null ;
		}
		
		logger.info("BaiduAPIService.reportCreativeRTB --- response.length :\n" + (response != null ? response.toJSONString().length() : "null") );
		
		if(response != null){
			
			// 接口调用结果，0=无错误，1=部分错误，2=全部错误
			int responseStatus = response.getInteger("status");

			if (responseStatus == 0) {
				
				//response里才是真正的数据
				JSONArray array = response.getJSONArray("response");
				
				return array ;
			}
			
		}
		
		return null ;
	}
	
	public static void main(String[] args){
		BaiduAPIService bs = new BaiduAPIService();
		JSONArray array = bs.reportCreativeRTB("", "");
		System.out.println(array.size());
	}
}
