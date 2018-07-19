package com.jtd.web.constants;

import com.jtd.commons.BeanFactory;
import com.jtd.commons.PropertyConfig;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.IOException;


public final class Constants {

	private static final Log log = LogFactory.getLog(Constants.class);

	private Constants() {
	}
	/**
	 * 超级管理员userid
	 */
	public static final long ADMIN_USER_ID = 1 ;
	/**
	 * 【jtd公司】的partner_id
	 */
	public static final long CE_PARTNER_ID = 1 ;
	/**
	 * 【jtd公司】的pid
	 */
	public static final long CE_PID = -1 ;
	
	public static final String USER_ENTITY = "USER_ENTITY";
	/**
	 * 当前操作的对象
	 */
	public static final String CURRENT_OPERAT_PATNER = "CURRENT_OPERAT_PATNER";
	/**
	 * 正在页面展示的数据结果集
	 */
	public static final String CURRENT_SHOW_COUNTRESULTS = "CURRENT_SHOW_COUNTRESULTS";

	/**
	 * 数据目录
	 */
	public static final String DATA_DIR_PATH;

	/**
	 * 临时目录，用于临时保存数据，用完删除
	 */
	public static final String TMP_DIR_PATH = System.getProperty("java.io.tmpdir");

	/**
	 * 素材保存目录
	 */
	public static final String CREATIVE_DIR_PATH;
	/**
	 * 资质文件保存
	 */
	public static final String QUALIDOC_DIR_PATH;
	/**
	 * 百度过滤条件
	 */
	public static final String BESSETTINGS_DIR_PATH;
	
	public static final String TRACKER_URL ;
	
	/**
	 * 广告主上传logo存放路径（相对路径）
	 */
	public static final String LOGO_IMG_PATH ;
	
	/**
	 * 是否为测试环境
	 */
	public static final boolean IS_TEST ;
	
	/**
	 * 是否忽略adx
	 */
	public static final boolean IS_IGNORE_ADX ;

	/**
	 * boss系统广告主回调接口url
	 */
	public static final String BOSS_PARTNER_CALLBACK_URL ; 
	
	/**
	 * 广告主账户余额限制（单位为分）
	 */
	public static final Long PARTNER_BALANCE_LIMIT ;
	
	/**
	 * 前台数据中心的查询条件
	 */
	public static final String FRONT_REPORT_PARAMS_MAP = "reportParamsMap" ;
	/**
	 * 前台数据中心的查询条件
	 */
	public static final String FRONT_REPORT_UNKNOWN_AREA_DISTRIBUTE_MAP = "unknownAreaDistributeMap" ;

	/**
	 * 结案截屏时用到，保存特殊用户的cookie
	 */
	public static final String TRACKER_URL_CC;
	/**
	 * 结案截屏时用到，保存完特殊用户后，回调的url
	 */
	public static final String CALLBACK_URL_CC;
	
	/**
	 * bes maping url & dspid
	 */
	public static final String BES_ISMAPPING;
	public static final String BES_MAPPING_URL;
	public static final String BES_MAPPING_DSPID;
	public static final String BES_MAPPING_TOKEN;
	
	/**
	 * xtraker maping url & dspid
	 */
	public static final String XTRADER_ISMAPPING;
	public static final String XTRADER_MAPPING_URL;
	public static final String XTRADER_MAPPING_DSPID;
	public static final String XTRADER_MAPPING_TOKEN;
	
	/**
	 * tanx mapping url & dspid
	 */
	public static final String TANX_ISMAPPING;
	public static final String TANX_MAPPING_URL;
	public static final String TANX_MAPPING_DSPID;
	public static final String TANX_MAPPING_TOKEN;
	
	/**
	 * vam mapping url & dspid
	 */
	public static final String VAM_ISMAPPING;
	public static final String VAM_MAPPING_URL;
	public static final String VAM_MAPPING_DSPID;
	public static final String VAM_MAPPING_TOKEN;
	
	/**
	 * hz mapping url & dspid
	 */
	public static final String HZ_ISMAPPING;
	public static final String HZ_MAPPING_URL;
	public static final String HZ_MAPPING_DSPID;
	public static final String HZ_MAPPING_TOKEN;
	
	
	static {
		PropertyConfig propertyConfig = (PropertyConfig) BeanFactory.getBean("propertyConfig");
		DATA_DIR_PATH = propertyConfig.getProperty("data.dir.path");
		File dataDir = new File(DATA_DIR_PATH);
		if (!dataDir.exists()) {
			try {
				FileUtils.forceMkdir(dataDir);
			} catch (IOException e) {
				log.warn("初始化数据目录失败", e);
			}
		}
		log.info("数据目录[" + DATA_DIR_PATH + "]");

		CREATIVE_DIR_PATH = DATA_DIR_PATH + "/creative";
		File creativeDir = new File(CREATIVE_DIR_PATH);
		if (!creativeDir.exists()) {
			try {
				FileUtils.forceMkdir(creativeDir);
			} catch (IOException e) {
				log.warn("初始化素材保存目录失败", e);
			}
		}
		log.info("素材保存目录[" + CREATIVE_DIR_PATH + "]");

		QUALIDOC_DIR_PATH = DATA_DIR_PATH + "/qualiDoc";
		File qualiDocDir = new File(QUALIDOC_DIR_PATH);
		if (!qualiDocDir.exists()) {
			try {
				FileUtils.forceMkdir(qualiDocDir);
			} catch (IOException e) {
				log.warn("初始化资质文件保存目录失败", e);
			}
		}
		log.info("资质文件保存目录[" + QUALIDOC_DIR_PATH + "]");
		
		BESSETTINGS_DIR_PATH = DATA_DIR_PATH + "/bessettings";
		File bessettingsDir = new File(BESSETTINGS_DIR_PATH);
		if (!bessettingsDir.exists()) {
			try {
				FileUtils.forceMkdir(bessettingsDir);
			} catch (IOException e) {
				log.warn("初始百度过滤条件保存目录失败", e);
			}
		}
		log.info("百度过滤条件保存目录[" + BESSETTINGS_DIR_PATH + "]");
		
		TRACKER_URL = propertyConfig.getProperty("tracker.url");
		
		LOGO_IMG_PATH = DATA_DIR_PATH + "/logo";
		
		IS_TEST = Boolean.valueOf(propertyConfig.getProperty("isTest"));
		
		IS_IGNORE_ADX = Boolean.valueOf(propertyConfig.getProperty("adx.ignore"));
		
		BOSS_PARTNER_CALLBACK_URL = propertyConfig.getProperty("boss.partnerCallBackUrl");
		
		PARTNER_BALANCE_LIMIT = Long.valueOf(propertyConfig.getProperty("partnerBalanceLimit"));
		
		TRACKER_URL_CC = propertyConfig.getProperty("tracker.url.cc");
		
		CALLBACK_URL_CC = propertyConfig.getProperty("callback.url.cc");
		
		/**
		 * bes maping url & dspid
		 */
		BES_ISMAPPING = propertyConfig.getProperty("bes.ismapping");
		BES_MAPPING_URL = propertyConfig.getProperty("bes.mapping.url");
		BES_MAPPING_DSPID = propertyConfig.getProperty("bes.mapping.dspid");
		BES_MAPPING_TOKEN = propertyConfig.getProperty("bes.mapping.token");
		
		/**
		 * xtraker maping url & dspid
		 */
		XTRADER_ISMAPPING = propertyConfig.getProperty("xtrader.ismapping");
		XTRADER_MAPPING_URL = propertyConfig.getProperty("xtrader.mapping.url");
		XTRADER_MAPPING_DSPID = propertyConfig.getProperty("xtrader.mapping.dspid");
		XTRADER_MAPPING_TOKEN = propertyConfig.getProperty("xtrader.mapping.token");
		
		/**
		 * tanx mapping url & dspid
		 */
		TANX_ISMAPPING = propertyConfig.getProperty("tanx.ismapping");
		TANX_MAPPING_URL = propertyConfig.getProperty("tanx.mapping.url");
		TANX_MAPPING_DSPID = propertyConfig.getProperty("tanx.mapping.dspid");
		TANX_MAPPING_TOKEN = propertyConfig.getProperty("tanx.mapping.token");
		
		/**
		 * vam mapping url & dspid
		 */
		VAM_ISMAPPING = propertyConfig.getProperty("vam.ismapping");
		VAM_MAPPING_URL = propertyConfig.getProperty("vam.mapping.url");
		VAM_MAPPING_DSPID = propertyConfig.getProperty("vam.mapping.dspid");
		VAM_MAPPING_TOKEN = propertyConfig.getProperty("vam.mapping.token");
		
		/**
		 * hz mapping url & dspid
		 */
		HZ_ISMAPPING = propertyConfig.getProperty("hz.ismapping");
		HZ_MAPPING_URL = propertyConfig.getProperty("hz.mapping.url");
		HZ_MAPPING_DSPID = propertyConfig.getProperty("hz.mapping.dspid");
		HZ_MAPPING_TOKEN = propertyConfig.getProperty("hz.mapping.token");
		
	}

	public static final String CHARSET_UTF_8 = "UTF-8";
	public static final String CHARSET_ISO = "ISO-8859-1";
	// ""字符串
	public final static String BANK_STR = "";
	// " "字符串
	public final static String BLANK_STR = " ";
	// ""数字10
	public final static int NUM_TEN = 10;
	// ""数字1
	public final static int NUM_ONE = 1;
	// ""数字0
	public final static int NUM_ZERO = 0;
	// 数字6
	public final static int NUM_SIX = 6;
	// 数字4
	public final static int NUM_FOUR = 4;
	// 数字3
	public final static int NUM_THREE = 3;
	// 数字2
	public final static int NUM_TWO = 2;
	// 数字-1
	public final static int NEGATIVE_ONE = -1;
	// 数字18
	public final static int NUM_EIGHTEEN = 18;
	// 数字100
	public final static int NUM_ONE_HUNDRED = 100;
	// 数字2000
	public final static int TWO_THOUSAND = 2000;
	// 字符串1
	public final static String ONE_STR = "1";
	//字符串-1
	public final static String NEGATIVE_ONE_STR = "-1";
	// 字符串0
	public final static String ZERO_STR = "0";
	// 字符串2
	public final static String TWO_STR = "2";
	// 字符串3
	public final static String THREE_STR = "3";
	// 字符串4
	public final static String FOUR_STR = "4";
	// 字符串8
	public final static String EIGHT_STR = "8";

	// 年月日
	public final static String PATTERN_YMD = "yyyy-MM-dd";
	// 年月日时分秒
	public final static String PATTERN_YMD_HMS = "yyyy-MM-dd HH:mm:ss";
	// 时分
	public final static String PATTERN_HM = "HH:mm";
	// 逗号,
	public final static String COMMA_STR = ",";
	// 冒号
	public final static String COLON_STR = ":";
	// 点
	public final static String  SPOT_STR =".";
	// 并且号
	public final static String AND_STR = "&";
	// js数组
	public final static String JS_ARRAY_STR = "[]";
	
	//下划线
	public final static String  UNDERLINE_STR = "_";
	//横线
	public final static String  LINE_STR = "-";
	// 00字符串
	public final static String DOUBLE_ZERO_STR = "00";
	// 01字符串
	public final static String ZERO_ONE_STR = "01";
	// 02字符串
	public final static String ZERO_TWO_STR = "02";
	// 03字符串
	public final static String ZERO_THREE_STR = "03";
	// 04字符串
	public final static String ZERO_FOUR_STR = "04";
	// 05字符串
	public final static String ZERO_FIVE_STR = "05";
	// 06字符串
	public final static String ZERO_SIX_STR = "06";
	// 07字符串
	public final static String ZERO_SEVEN_STR = "07";
	// 08字符串
	public final static String ZERO_EIGHT_STR = "08";
	// 09字符串
	public final static String ZERO_NINE_STR = "09";
	// 10字符串
	public final static String TEN_STR = "10";
	// 11字符串
	public final static String ELEVEN_STR = "11";
	// 12字符串
	public final static String TWELVE_STR = "12";
	// 13字符串
	public final static String THIRTEEN_STR = "13";
	// 14字符串
	public final static String FOURTEEN_STR = "14";
	// 15字符串
	public final static String FIFTEEN_STR = "15";
	// 16字符串
	public final static String SIXTEEN_STR = "16";
	// 17字符串
	public final static String SEVENTEEN_STR = "17";
	// 18字符串
	public final static String EIGHTEEN_STR = "18";
	// 19字符串
	public final static String NINETEEN_STR = "19";
	// 20字符串
	public final static String TWENTY_STR = "20";
	// 21字符串
	public final static String TWENTY_ONE_STR = "21";
	// 22字符串
	public final static String TWENTY_TWO_STR = "22";
	// 23字符串
	public final static String TWENTY_THREE_STR = "23";
	// 24字符串
	public final static String TWENTY_FOUR_STR = "24";
	// 25字符串
	public final static String TWENTY_FIVE_STR = "25";
	// 26字符串
	public final static String TWENTY_SIX_STR = "26";
	// 99字符串
	public final static String NINETY_NINE_STR = "99";
	// 037字符串
	public final static String ZERO_THIRTYSEVEN_STR = "037";
    //余额不足邮件提示主体
	public static final String SEND_EMAIL_SUBJECT= "余额到期提醒";

    //广告活动投放时,广告主帐户最低余额限制(单位为分) 暂定100元
    public final static int CAMP_LOW_ACCOUNT = 10000;

	//余额不足邮件提示内容
	public static final String SEND_EMAIL_CONTENT= "广告主partnerName的账号余额已经不足accBalance元,特此提醒该广告主需及时充值,否则将影响项目投放,谢谢！";
	// /r/n分割符
	public static final String  SPILT_R_N_STR = "\r\n";
	//邮件下次发送时间
	public static final String  EMAIL_NEXT_TIME_STR = "09:00:00";
	//mp4视频
	public final static String VIDEO_MP4 = "mp4";
	//flv视频
	public final static String VIDEO_FLV = "flv";

   
}
