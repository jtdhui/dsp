package com.jtd.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.jtd.utils.JsonUtil;
import com.jtd.web.constants.AdType;
import com.jtd.web.constants.CatgSerial;
import com.jtd.web.constants.Constants;
import com.jtd.web.constants.CookieType;
import com.jtd.web.dao.ICookieGidDao;
import com.jtd.web.dao.ICreativeSizeDao;
import com.jtd.web.dao.ICreativeSizeFlowDao;
import com.jtd.web.exception.PlatformException;
import com.jtd.web.po.CookieGid;
import com.jtd.web.po.CreativeSizeFlow;
import com.jtd.web.vo.AppType;
import com.jtd.web.vo.ChannelCategory;
import com.jtd.web.vo.City;
import com.jtd.web.vo.WebsiteType;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月25日
 * @项目名称 dsp-admin
 * @描述 <p>字典数据工具类</p>
 * 
 */
@Component
public class DicController {
	private static final Log log = LogFactory.getLog(DicController.class);
	/**
	 * 城市文件路径
	 */
	private static final String CITY_FILE_PATH = "dic/dic_city.txt";

	/**
	 * 大区
	 */
	private static final Map<Integer, City> AREAS = new HashMap<Integer, City>();
	/**
	 * 省
	 */
	private static final Map<Integer, City> PROVINCES = new HashMap<Integer, City>();
	/**
	 * 城市
	 */
	private static final Map<Integer, City> CITYS = new HashMap<Integer, City>();

	private static final String DSP_CATEGORY_FILE_PATH = "dic/category_mapping.txt";
	private static final String TANX_CATEGORY_FILE_PATH = "dic/category_tanx.txt";
	private static final String BES_CATEGORY_FILE_PATH = "dic/category_bes.txt";
	private static final String VAM_CATEGORY_FILE_PATH = "dic/category_vam.txt";
    private static final String XTRADER_CATEGORY_FILE_PATH = "dic/category_xtrader.txt";
    private static final String HZENG_CATEGORY_FILE_PATH = "dic/category_hzeng.txt";
    private static final String ADVIEW_CATEGORY_FILE_PATH = "dic/category_adView.txt";
	/**
	 * 所有渠道的行业类别
	 */
	private static final Map<CatgSerial, Map<Long, ChannelCategory>> CHANNEL_CATEGORYS = new HashMap<CatgSerial, Map<Long, ChannelCategory>>();
	/**
	 * 所有渠道的行业类别后台调用
	 */
	private static final Map<CatgSerial,List<ChannelCategory>> CHANNEL_CATEGORYS_LIST = new HashMap<CatgSerial,List<ChannelCategory>>();
	/**
	 * 一级app类型
	 */
	private static final String APP_LEVEL1_FILE_PATH = "dic/app_1.txt";
	/**
	 * 二级app类型以及和渠道类型的对照
	 */
	private static final String APP_LEVEL2_FILE_PATH = "dic/app_2.txt";
	/**
	 * 所有的app类型 <br>
	 * 保存的是一级的类型，二级类型在一级类型对象subAppTypes里
	 */
	private static final Map<String, AppType> APPTYPES = new HashMap<String, AppType>();
	/**
	 * 所有app类型
	 */
	private static final Map<String, AppType> ALL_APPTYPES = new HashMap<String, AppType>();

	/**
	 * adx的apptype类型对应dsp的类型 可能会出现一对多的情况
	 */
	private static final Map<Long, Map<String, Set<String>>> ADX_APPTYPE_TO_DSP_APPTYPE_LEVEL1 = new HashMap<Long, Map<String, Set<String>>>();
	private static final Map<Long, Map<String, Set<String>>> ADX_APPTYPE_TO_DSP_APPTYPE_LEVEL2 = new HashMap<Long, Map<String, Set<String>>>();

	private static final String WEBSITE_LEVLE1_FILE_PATH = "dic/website_1.txt";
	private static final String WEBSITE_LEVLE2_FILE_PATH = "dic/website_2.txt";
	/**
	 * 所有的website类型<br>
	 * 保存的是一级的类型，二级类型在一级类型对象subWebsiteTypes里
	 */
	private static final Map<String, WebsiteType> WEBSITETYPES = new HashMap<String, WebsiteType>();
	/**
	 * 所有网站类型
	 */
	private static final Map<String, WebsiteType> ALL_WEBSITETYPES = new HashMap<String, WebsiteType>();
	/**
	 * adx的网站类型对应dsp的网站类型 可能会出现一对多的情况
	 */
	private static final Map<Long, Map<String, Set<String>>> ADX_WEBSITETYPE_TO_DSP_WEBSITETYPE_LEVEL1 = new HashMap<Long, Map<String, Set<String>>>();
	private static final Map<Long, Map<String, Set<String>>> ADX_WEBSITETYPE_TO_DSP_WEBSITETYPE_LEVEL2 = new HashMap<Long, Map<String, Set<String>>>();

	private static final Map<String, List<CookieGid>> POPULATIONS = new LinkedHashMap<String, List<CookieGid>>();
	private static final Map<String, List<CookieGid>> INSTRESTING = new LinkedHashMap<String, List<CookieGid>>();

    private static final Map<Long, CookieGid> ALL_POPULATIONS = new HashMap<Long, CookieGid>();
    private static final Map<Long, CookieGid> ALL_INSTRESTING = new HashMap<Long, CookieGid>();

	private static final JSONArray  CREATIVESIZEFLOW = new JSONArray();

	private static ICookieGidDao cookieGidDao;
	private static ICreativeSizeFlowDao creativeSizeFlowDAO;
	private static ICreativeSizeDao creativeSizeDAO;

	/**
	 * 初始化字典文件
	 */
	@PostConstruct
	public static void init() {
		try {
			initCity();	//省市
			initCategory();	//渠道行业类别
			initApp();
			initWebsite();
			initCookieGid();
//			initCreativeSizeFlow();
			writeDicJs();
		} catch (Exception e) {
			log.warn("初始化字典文件失败", e);
		}
	}

	private static void initCreativeSizeFlow() {
		Map<Long, String> sizeMap = creativeSizeDAO.getSizeIdMap();
		List<CreativeSizeFlow> sizeList = creativeSizeFlowDAO.listAllCreativeSizeFlow();
		for (CreativeSizeFlow creativeSizeFlow : sizeList) {
			Long sizeId = creativeSizeFlow.getSizeId();
			String size = sizeMap.get(sizeId);
			creativeSizeFlow.setSize(size);
		}
		CREATIVESIZEFLOW.addAll(sizeList);
	}

	/**
	 * 初始化人群定向和兴趣定向的数据
	 */
	private static void initCookieGid() {
		POPULATIONS.clear();
		INSTRESTING.clear();
        ALL_POPULATIONS.clear();
        ALL_INSTRESTING.clear();

		List<CookieGid> populations = cookieGidDao.listByCkType(CookieType.POPULATION);
		for (CookieGid cookieGid : populations) {

            ALL_POPULATIONS.put(cookieGid.getId(),cookieGid);

			String ckProperty = cookieGid.getCkProperty();
			List<CookieGid> subCookieGids = POPULATIONS.get(ckProperty);
			if (subCookieGids == null) {
				subCookieGids = new ArrayList<CookieGid>();
				POPULATIONS.put(ckProperty, subCookieGids);
			}
			subCookieGids.add(cookieGid);
		}
		List<CookieGid> instrestings = cookieGidDao.listByCkType(CookieType.INSTRESTING);
		for (CookieGid cookieGid : instrestings) {

            ALL_INSTRESTING.put(cookieGid.getId(),cookieGid);

			String ckProperty = cookieGid.getCkProperty();
			List<CookieGid> subCookieGids = INSTRESTING.get(ckProperty);
			if (subCookieGids == null) {
				subCookieGids = new ArrayList<CookieGid>();
				INSTRESTING.put(ckProperty, subCookieGids);
			}
			subCookieGids.add(cookieGid);
		}
		log.info("ALL_POPULATIONS"+JSON.toJSONString(ALL_POPULATIONS));
		log.info("POPULATIONS"+JSON.toJSONString(ALL_POPULATIONS));
		log.info("ALL_INSTRESTING"+JSON.toJSONString(ALL_INSTRESTING));
		log.info("INSTRESTING"+JSON.toJSONString(INSTRESTING));

	}

	/**
	 * 生成数据字典js文件
	 * 
	 * @throws IOException
	 */
	private static void writeDicJs() throws IOException {
		String rootPath = DicController.class.getResource("/").getFile() + "../../js/";
		File dir = new File(rootPath);
		if(!dir.exists()){
			dir.mkdir();
		}
		File greenkooDicJs = new File(rootPath + "campaignDicData.js");
		if (greenkooDicJs.exists()) {
			greenkooDicJs.delete();
		}
		greenkooDicJs.createNewFile();
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(greenkooDicJs), "UTF-8"));
		bw.append("var _campaignDicData = {};\r\n");
		Map<CatgSerial, List<ChannelCategory>> channelCategorys = new HashMap<CatgSerial, List<ChannelCategory>>();
		// Map<CatgSerial, Map<Long, ChannelCategory>> toWriteCategorys = new
		// HashMap<CatgSerial, Map<Long, ChannelCategory>>(CHANNEL_CATEGORYS);
		// toWriteCategorys.remove(CatgSerial.DSP);
		for (Entry<CatgSerial, Map<Long, ChannelCategory>> entry : CHANNEL_CATEGORYS.entrySet()) {
			CatgSerial catgSerial = entry.getKey();
			Map<Long, ChannelCategory> values = entry.getValue();
			List<ChannelCategory> ids = new ArrayList<ChannelCategory>(values.values());
			Collections.sort(ids, new Comparator<ChannelCategory>() {
				@Override
				public int compare(ChannelCategory o1, ChannelCategory o2) {
					return o1.getId().compareTo(o2.getId());
				}
			});
			channelCategorys.put(catgSerial, ids);
		}

		bw.append("_campaignDicData.channelCategorys=" + JSON.toJSONString(channelCategorys) + ";\r\n");
		bw.append("_campaignDicData.areas=" + JSON.toJSONString(AREAS) + ";\r\n");
		bw.append("_campaignDicData.provinces=" + JSON.toJSONString(PROVINCES) + ";\r\n");
		bw.append("_campaignDicData.citys=" + JSON.toJSONString(CITYS) + ";\r\n");
		bw.append("_campaignDicData.appTypes=" + JSON.toJSONString(APPTYPES) + ";\r\n");
		bw.append("_campaignDicData.websiteTypes=" + JSON.toJSONString(WEBSITETYPES) + ";\r\n");
		bw.append("_campaignDicData.populations=" + JSON.toJSONString(POPULATIONS) + ";\r\n");
		bw.append("_campaignDicData.instresting=" + JSON.toJSONString(INSTRESTING) + ";\r\n");
		bw.append("_campaignDicData.creativeSizeFlow=" + JsonUtil.toJSONString(CREATIVESIZEFLOW) + ";\r\n");
		bw.append("var campaignDicData =_campaignDicData;");
		bw.close();
	}

	@SuppressWarnings("unchecked")
	private static void initWebsite() throws IOException, PlatformException {
		List<String> lines = FileUtils.readLines(getFile(WEBSITE_LEVLE1_FILE_PATH), "UTF-8");
		for (String line : lines) {
			String[] fields = line.split(",");
			String id = fields[0];
			String name = fields[1];
			WebsiteType websiteType = new WebsiteType(id, name);
			WEBSITETYPES.put(id, websiteType);
			ALL_WEBSITETYPES.put(id, websiteType);
		}
		lines = FileUtils.readLines(getFile(WEBSITE_LEVLE2_FILE_PATH), "UTF-8");
		for (String line : lines) {
//            System.out.println(line);
			String[] fields = line.split(",");
			String parentId = fields[0];
			String id = fields[1];
			String name = fields[2];
			String besId = fields[3];
			String tanxId = fields[4];
			String vamId = fields[5];
            String xtraderId = fields[6];
			WebsiteType websiteType = new WebsiteType(id, name);
			ALL_WEBSITETYPES.put(id, websiteType);
			Map<CatgSerial, String> channelWebsiteTypes = new HashMap<CatgSerial, String>();
			channelWebsiteTypes.put(CatgSerial.BES, besId);
			channelWebsiteTypes.put(CatgSerial.TANX, tanxId);
			channelWebsiteTypes.put(CatgSerial.VAM, vamId);
            channelWebsiteTypes.put(CatgSerial.XTRADER, xtraderId);
			websiteType.setChannelWebsiteTypes(channelWebsiteTypes);
			WebsiteType parentWebsiteType = WEBSITETYPES.get(parentId);
			parentWebsiteType.addSubWebsiteType(websiteType);
			initAdx2DspWebsiteType(1L, id, parentId, tanxId);
			initAdx2DspWebsiteType(2L, id, parentId, besId);
			initAdx2DspWebsiteType(6L, id, parentId, vamId);
            initAdx2DspWebsiteType(8L, id, parentId, xtraderId);

		}
	}

	private static void initAdx2DspWebsiteType(Long channelId, String dspId, String parentId, String adxId) {
		Map<String, Set<String>> adxWebsiteTypesLevel2 = ADX_WEBSITETYPE_TO_DSP_WEBSITETYPE_LEVEL2.get(channelId);
		if (adxWebsiteTypesLevel2 == null) {
			adxWebsiteTypesLevel2 = new HashMap<String, Set<String>>();
			ADX_WEBSITETYPE_TO_DSP_WEBSITETYPE_LEVEL2.put(channelId, adxWebsiteTypesLevel2);
		}
		Set<String> adx2DspWebsiteType2 = adxWebsiteTypesLevel2.get(adxId);
		if (adx2DspWebsiteType2 == null) {
			adx2DspWebsiteType2 = new HashSet<String>();
			adxWebsiteTypesLevel2.put(adxId, adx2DspWebsiteType2);
		}
		adx2DspWebsiteType2.add(dspId);

		Map<String, Set<String>> adxWebsiteTypesLevel1 = ADX_WEBSITETYPE_TO_DSP_WEBSITETYPE_LEVEL1.get(channelId);
		if (adxWebsiteTypesLevel1 == null) {
			adxWebsiteTypesLevel1 = new HashMap<String, Set<String>>();
			ADX_WEBSITETYPE_TO_DSP_WEBSITETYPE_LEVEL1.put(channelId, adxWebsiteTypesLevel1);
		}
		Set<String> adx2DspWebsiteType1 = adxWebsiteTypesLevel1.get(adxId);
		if (adx2DspWebsiteType1 == null) {
			adx2DspWebsiteType1 = new HashSet<String>();
			adxWebsiteTypesLevel1.put(adxId, adx2DspWebsiteType1);
		}
		adx2DspWebsiteType1.add(parentId);
	}

	/**
	 * @throws IOException
	 * @throws PlatformException 
	 * 
	 */
	@SuppressWarnings("unchecked")
	private static void initApp() throws IOException, PlatformException {
		List<String> lines = FileUtils.readLines(getFile(APP_LEVEL1_FILE_PATH), "UTF-8");
		for (String line : lines) {
			String[] fields = line.split(",");
			String id = fields[0];
			String name = fields[1];
			AppType appType = new AppType(id, name);
			APPTYPES.put(id, appType);
			ALL_APPTYPES.put(id, appType);
		}
		lines = FileUtils.readLines(getFile(APP_LEVEL2_FILE_PATH), "UTF-8");
		for (String line : lines) {
			String[] fields = line.split(",");
			String parentId = fields[0];
			String id = fields[1];
			String name = fields[2];
			String besId = fields[3];  //bes
			String tanxId = fields[4]; //tanx
			String vamId = fields[5];  //vam
            String xtraderId = fields[6]; //xtraderId
            String adViewId = fields[7];  //adView
			AppType appType = new AppType(id, name);
			ALL_APPTYPES.put(id, appType);
			Map<CatgSerial, String> channelAppTypes = new HashMap<CatgSerial, String>();
			channelAppTypes.put(CatgSerial.BES, besId);
			channelAppTypes.put(CatgSerial.TANX, tanxId);
			channelAppTypes.put(CatgSerial.VAM, vamId);
            channelAppTypes.put(CatgSerial.XTRADER, xtraderId);
            channelAppTypes.put(CatgSerial.ADVIEW, adViewId);
			appType.setChannelAppTypes(channelAppTypes);

			AppType parentAppType = APPTYPES.get(parentId);
			parentAppType.addSubAppType(appType);
			initAdx2DspAppType(1L, id, parentId, tanxId);
			initAdx2DspAppType(2L, id, parentId, besId);
			String[] vamTypes = vamId.split("_");
			for (String vamType : vamTypes) {
				initAdx2DspAppType(6L, id, parentId, vamType);
			}
            initAdx2DspAppType(8L, id, parentId, xtraderId);
		}
	}

	private static void initAdx2DspAppType(Long channelId, String dspId, String parentId, String adxId) {
		Map<String, Set<String>> adx2DspAppTypeLeve2 = ADX_APPTYPE_TO_DSP_APPTYPE_LEVEL2.get(channelId);
		if (adx2DspAppTypeLeve2 == null) {
			adx2DspAppTypeLeve2 = new HashMap<String, Set<String>>();
			ADX_APPTYPE_TO_DSP_APPTYPE_LEVEL2.put(channelId, adx2DspAppTypeLeve2);
		}
		Set<String> dspAppTypeLevel2 = adx2DspAppTypeLeve2.get(adxId);
		if (dspAppTypeLevel2 == null) {
			dspAppTypeLevel2 = new HashSet<String>();
			adx2DspAppTypeLeve2.put(adxId, dspAppTypeLevel2);
		}
		dspAppTypeLevel2.add(dspId);
		Map<String, Set<String>> adx2DspAppTypeLeve1 = ADX_APPTYPE_TO_DSP_APPTYPE_LEVEL1.get(channelId);
		if (adx2DspAppTypeLeve1 == null) {
			adx2DspAppTypeLeve1 = new HashMap<String, Set<String>>();
			ADX_APPTYPE_TO_DSP_APPTYPE_LEVEL1.put(channelId, adx2DspAppTypeLeve1);
		}
		Set<String> dspAppTypeLevel1 = adx2DspAppTypeLeve1.get(adxId);
		if (dspAppTypeLevel1 == null) {
			dspAppTypeLevel1 = new HashSet<String>();
			adx2DspAppTypeLeve1.put(adxId, dspAppTypeLevel1);
		}
		dspAppTypeLevel1.add(parentId);
	}

	/**
	 * 初始化城市数据
	 * 
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	private static void initCity() throws Exception {
		
		
		
		List<String> lines = FileUtils.readLines(getFile(CITY_FILE_PATH), "UTF-8");
		for (String line : lines) {
			String[] fields = line.split(",");
			String id = fields[0];
			String cityName = fields[1];
			String parentId = fields[2];
			String cityLevel = fields[3];
			String cityType = fields[4];
			City city = new City(Integer.valueOf(id), cityName, Integer.valueOf(parentId), Integer.valueOf(cityLevel), Integer.valueOf(cityType));
			if (cityType.equals("1")) {
				// 大区
				AREAS.put(city.getId(), city);
			} else if (cityType.equals("2")) {
				// 省
				PROVINCES.put(city.getId(), city);
			} else if (cityType.equals("3")) {
				// 城市
				CITYS.put(city.getId(), city);
				//log.info("initCity........id " + city.getId() + ",name:" + city.getName());
			}
		}
	}

	/**
	 * 渠道行业类别
	 * @throws IOException
	 * @throws PlatformException
	 */
	@SuppressWarnings("unchecked")
	private static void initCategory() throws IOException, PlatformException {
		Map<Long, ChannelCategory> channelCategoryMap = new HashMap<Long, ChannelCategory>();
		List<ChannelCategory> ccList = new ArrayList<ChannelCategory>();
		List<String> lines = FileUtils.readLines(getFile(DSP_CATEGORY_FILE_PATH), "UTF-8");
		for (String line : lines) {
			String[] fields = line.split(",");
			Long categoryId = Long.valueOf(fields[0]);
			String categoryName = fields[1];
			Map<CatgSerial, Long> channelCategorys = new HashMap<CatgSerial, Long>();
			Long tanxCategoryId = Long.valueOf(fields[2]);   //tanx
			Long besCategoryId = Long.valueOf(fields[3]);    //bes
			Long vamCategoryId = Long.valueOf(fields[4]);    //vam
			Long xtradeCategoryId = Long.valueOf(fields[5]); //xtrade
			Long hzengCategoryId = Long.valueOf(fields[6]);  //hzeng
			Long adViewCategoryId = Long.valueOf(fields[7]); //adView
			channelCategorys.put(CatgSerial.TANX, tanxCategoryId);
			channelCategorys.put(CatgSerial.BES, besCategoryId);
			channelCategorys.put(CatgSerial.VAM, vamCategoryId);
			channelCategorys.put(CatgSerial.XTRADER, xtradeCategoryId);
			channelCategorys.put(CatgSerial.HZ, hzengCategoryId);
			channelCategorys.put(CatgSerial.ADVIEW, adViewCategoryId);
			ChannelCategory dspCategory = new ChannelCategory();
			dspCategory.setId(categoryId);
			dspCategory.setName(categoryName);
			dspCategory.setChannelCategorys(channelCategorys);
			channelCategoryMap.put(categoryId, dspCategory);
			ccList.add(dspCategory);
		}
		CHANNEL_CATEGORYS.put(CatgSerial.DSP, channelCategoryMap);
		CHANNEL_CATEGORYS_LIST.put(CatgSerial.DSP, ccList);

		Map<CatgSerial, String> channelCategoryFilePaths = new HashMap<CatgSerial, String>();
		channelCategoryFilePaths.put(CatgSerial.TANX, TANX_CATEGORY_FILE_PATH);
		channelCategoryFilePaths.put(CatgSerial.BES, BES_CATEGORY_FILE_PATH);
		channelCategoryFilePaths.put(CatgSerial.VAM, VAM_CATEGORY_FILE_PATH);
        channelCategoryFilePaths.put(CatgSerial.XTRADER, XTRADER_CATEGORY_FILE_PATH);
        channelCategoryFilePaths.put(CatgSerial.HZ, HZENG_CATEGORY_FILE_PATH);
        channelCategoryFilePaths.put(CatgSerial.ADVIEW, ADVIEW_CATEGORY_FILE_PATH);
        
		for (Entry<CatgSerial, String> entry : channelCategoryFilePaths.entrySet()) {
			CatgSerial catgSerial = entry.getKey();
			String filePath = entry.getValue();
			lines = FileUtils.readLines(getFile(filePath), "UTF-8");
			channelCategoryMap = new HashMap<Long, ChannelCategory>();
			ccList = new ArrayList<ChannelCategory>();
			for (String line : lines) {
				String[] fields = line.split(",");
				String categoryName = fields[0];
				Long categoryId = Long.valueOf(fields[1]);
				ChannelCategory channelCategory = new ChannelCategory();
				channelCategory.setId(categoryId);
				channelCategory.setName(categoryName);
				channelCategoryMap.put(categoryId, channelCategory);
				ccList.add(channelCategory);
			}
			CHANNEL_CATEGORYS.put(catgSerial, channelCategoryMap);
			CHANNEL_CATEGORYS_LIST.put(catgSerial, ccList);
		}
	}

	private static File getFile(String resource) throws PlatformException {
		try {
			File file = ResourceUtils.getFile(DicController.class.getClassLoader().getResource(resource));
			return file;
		} catch (FileNotFoundException e) {
			throw new PlatformException(e.getMessage());
		}
	}

	@Resource
	public void setCookieGidDAO(ICookieGidDao cookieGidDAO) {
		this.cookieGidDao = cookieGidDAO;
	}

	public static WebsiteType getWebsiteType(String websiteTypeId) {
		return ALL_WEBSITETYPES.get(websiteTypeId);
	}
	
	public static String getWebsiteTypeDescInfo(String websiteTypeId) {
		if(StringUtils.isEmpty(websiteTypeId)){
			return "未知";
		}
		WebsiteType websiteType = ALL_WEBSITETYPES.get(websiteTypeId);
		if(null != websiteType){
			return websiteType.getName();
		}
		//精确匹配未找到
		String[] websiteTypeIds = websiteTypeId.split(Constants.UNDERLINE_STR);
		if(null ==websiteTypeIds || websiteTypeIds.length ==0){
				return "未知";	
		}
		String websiteTypeDesc = Constants.BANK_STR;
	  for(int i =0; i < websiteTypeIds.length; i++){
		  String websiteTypeid = websiteTypeIds[i];
		  if(StringUtils.isEmpty(websiteTypeid)){
			  continue;
		  }
		  if(ALL_WEBSITETYPES.containsKey(websiteTypeid)){
			websiteType = ALL_WEBSITETYPES.get(websiteTypeid);
			websiteTypeDesc += websiteType.getName();
			if(i < websiteTypeIds.length -1){
				websiteTypeDesc += Constants.UNDERLINE_STR;
			}
		}
	 }
	  if(StringUtils.isEmpty(websiteTypeDesc)){
			return "未知";
	  }
	 return websiteTypeDesc;
	}
	
	
	

	
    /**
     * 获取app应用描述
     * @param appTypeId
     * @return
     */
	public static String getAppTypeDescInfo(String appTypeId) {
		if(StringUtils.isEmpty(appTypeId)){
			return "媒体类型";
		}
		AppType appType = ALL_APPTYPES.get(appTypeId);
		if(null != appType){
			return appType.getName();
		}
		//精确匹配未找到
		String[] appTypeIds = appTypeId.split(Constants.UNDERLINE_STR);
		if(null ==appTypeIds || appTypeIds.length ==0){
				return "媒体类型";	
		}
	   String appTypeDesc = Constants.BANK_STR;
	   for(int i =0; i < appTypeIds.length; i++){
		  String appid = appTypeIds[i];
		  if(StringUtils.isEmpty(appid)){
			  continue;
		  }
		  if(ALL_APPTYPES.containsKey(appid)){
			appType = ALL_APPTYPES.get(appid);
			appTypeDesc += appType.getName();
			if(i < appTypeIds.length -1){
				appTypeDesc += Constants.UNDERLINE_STR;
			}
		}
	 }
	  if(StringUtils.isEmpty(appTypeDesc)){
			return "媒体类型";
	  }
	 return appTypeDesc;
	}
	
	
	
	
	public static ChannelCategory getDSPChannelCategory(Long catgId) {
		return CHANNEL_CATEGORYS.get(CatgSerial.DSP).get(catgId);
	}

	public static Map<String,AppType> getAllAppTypes(){
        return ALL_APPTYPES;
    }
	public static AppType getAppType(String appTypeId) {
		return ALL_APPTYPES.get(appTypeId);
	}

	public static Set<Long> getAllPopulationIds() {
		Set<Long> ids = new HashSet<Long>();
		for (List<CookieGid> cookieGids : POPULATIONS.values()) {
			for (CookieGid cookieGid : cookieGids) {
				ids.add(cookieGid.getId());
			}
		}
		return ids;
	}

	public static Map<Long,CookieGid> getAllPopulations(){
	    return ALL_POPULATIONS;
    }

    public static Map<Long,CookieGid> getAllInstresting(){
        return ALL_INSTRESTING;
    }

	public static Set<Long> getAllInstrestingIds() {
		Set<Long> ids = new HashSet<Long>();
		for (List<CookieGid> cookieGids : INSTRESTING.values()) {
			for (CookieGid cookieGid : cookieGids) {
				ids.add(cookieGid.getId());
			}
		}
		return ids;
	}

	@Resource
	public void setcreativeSizeFlowDAO(ICreativeSizeFlowDao creativeSizeFlowDAO) {
		DicController.creativeSizeFlowDAO = creativeSizeFlowDAO;
	}

	public static List<Long> getMatchcreativeSizeFlow(AdType adType, List<Long> channelIds) {
		List<Long> matchSizeId = new ArrayList<Long>();
		for (Object obj : CREATIVESIZEFLOW) {
			CreativeSizeFlow creativeSizeFlow = (CreativeSizeFlow) obj;
			Long channelId = creativeSizeFlow.getChannelId();
			if (!channelIds.contains(channelId)) {
				continue;
			}
			if (!adType.equals(creativeSizeFlow.getAdType())) {
				continue;
			}
			matchSizeId.add(creativeSizeFlow.getSizeId());
		}
		return matchSizeId;
	}

	public static Long getCatgSerial(Long dspCatgId, CatgSerial catgSerial) {
		Map<Long, ChannelCategory> dspChannelCategory = CHANNEL_CATEGORYS.get(CatgSerial.DSP);
		ChannelCategory channelCategory = dspChannelCategory.get(dspCatgId);
		Map<CatgSerial, Long> channelCategorys = channelCategory.getChannelCategorys();
		return channelCategorys.get(catgSerial);
	}

	public static City getCity(Integer cityId) {
		return CITYS.get(cityId);
	}
	
	public static Map<Integer,City> getAllCities() {
		return CITYS;
	}

	public static City getProvince(Integer provinceId) {
		return PROVINCES.get(provinceId);
	}
	
	public static Map<Integer,City> getAllProvinces() {
		return PROVINCES;
	}

	public static int getCitySize() {
		return CITYS.size();
	}

	@Resource
	public void setCreativeSizeDAO(ICreativeSizeDao creativeSizeDAO) {
		DicController.creativeSizeDAO = creativeSizeDAO;
	}

	public static Set<String> getDspLevel1AppTypes(Long channelId, String adxId) {
		Map<String, Set<String>> adx2dsp = ADX_APPTYPE_TO_DSP_APPTYPE_LEVEL1.get(channelId);
		return adx2dsp.get(adxId);
	}

	public static Set<String> getDspLevel2AppTypes(Long channelId, String adxId) {
		Map<String, Set<String>> adx2dsp = ADX_APPTYPE_TO_DSP_APPTYPE_LEVEL2.get(channelId);
		return adx2dsp.get(adxId);
	}

	public static Map<String, WebsiteType> getAllWebSiteTypes(){
		return ALL_WEBSITETYPES;
	}

	public static Set<String> getDspLevel1WebsiteTypes(Long channelId, String adxId) {
		Map<String, Set<String>> adx2dsp = ADX_WEBSITETYPE_TO_DSP_WEBSITETYPE_LEVEL1.get(channelId);
		return adx2dsp.get(adxId);
	}

	public static Set<String> getDspLevel2WebsiteTypes(Long channelId, String adxId) {
		Map<String, Set<String>> adx2dsp = ADX_WEBSITETYPE_TO_DSP_WEBSITETYPE_LEVEL2.get(channelId);
		return adx2dsp.get(adxId);
	}

	public static Map<CatgSerial, List<ChannelCategory>> getChannelCategorysList() {
		return CHANNEL_CATEGORYS_LIST;
	}
}
