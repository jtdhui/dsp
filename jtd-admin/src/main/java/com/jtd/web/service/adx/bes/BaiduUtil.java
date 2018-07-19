package com.jtd.web.service.adx.bes;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;


public class BaiduUtil {

	static Logger logger = Logger.getLogger("errorLog");

	private static final Properties props = new Properties();

	public static final int DSPID;
	public static final String TOKEN;

	public static final String SERVER_URL;
	public static final String VERSION;
	
	public static final String SETTING_PATH ;
	public static final String DEFAULT_DOC_VALID_DATE ;

	public static final String CREATIVE_GETALL_URL;
	public static final String CREATIVE_GET_URL;
	public static final String CREATIVE_ADD_URL;
	public static final String CREATIVE_UPDATE_URL;
	public static final String CREATIVE_QUERY_AUDIT_STATE_URL;

	public static final String ADVERTISER_GETALL_URL;
	public static final String ADVERTISER_GET_URL;
	public static final String ADVERTISER_ADD_URL;
	public static final String ADVERTISER_UPDATE_URL;
	
	public static final String ADVERTISER_QUERY_QUALIFICATION_URL;
	public static final String ADVERTISER_UPLOAD_QUALIFICATION_URL ;
	public static final String ADVERTISER_UPDATE_MAIN_QUALIFICATION_URL ;
	public static final String ADVERTISER_UPDATE_OPTIONAL_QUALIFICATION_URL ;
	
	public static final String VIEWCONFIG_SSP_SETTING_URL ;
	
	public static final String REPORT_RTB_URL;
	public static final String REPORT_CREATIVE_RTB_URL;
	public static final String REPORT_CONSUME_URL;
	public static final String REPORT_ADVERTISER_CONSUME_URL;
	
	static {

		try {
			
			Thread thread = Thread.currentThread();
			
			ClassLoader classLoder  = thread.getContextClassLoader();
			
			InputStream is = classLoder.getResourceAsStream("baiduconfig.properties");
			
			props.load(is);
			
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage(), e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		DSPID = Integer.parseInt(props.getProperty("DSPID"));
		TOKEN = props.getProperty("TOKEN");
		SERVER_URL = props.getProperty("SERVER_URL");
		VERSION = props.getProperty("VERSION");
		SETTING_PATH = props.getProperty("SETTING_PATH");
		DEFAULT_DOC_VALID_DATE = props.getProperty("DEFAULT_DOC_VALID_DATE");

		CREATIVE_GETALL_URL = SERVER_URL + "/" + VERSION + "/creative/getAll";
		CREATIVE_GET_URL = SERVER_URL + "/" + VERSION + "/creative/get";
		CREATIVE_ADD_URL = SERVER_URL + "/" + VERSION + "/creative/add";
		CREATIVE_UPDATE_URL = SERVER_URL + "/" + VERSION + "/creative/update";
		CREATIVE_QUERY_AUDIT_STATE_URL = SERVER_URL + "/" + VERSION
				+ "/creative/queryAuditState";

		ADVERTISER_GETALL_URL = SERVER_URL + "/" + VERSION
				+ "/advertiser/getAll";
		ADVERTISER_GET_URL = SERVER_URL + "/" + VERSION + "/advertiser/get";
		ADVERTISER_ADD_URL = SERVER_URL + "/" + VERSION + "/advertiser/add";
		ADVERTISER_UPDATE_URL = SERVER_URL + "/" + VERSION
				+ "/advertiser/update";
		ADVERTISER_QUERY_QUALIFICATION_URL = SERVER_URL + "/" + VERSION
				+ "/advertiser/queryQualification";

		ADVERTISER_UPLOAD_QUALIFICATION_URL = SERVER_URL + "/" + VERSION
				+ "/advertiser/uploadQualification" ;
		ADVERTISER_UPDATE_MAIN_QUALIFICATION_URL = SERVER_URL + "/" + VERSION
				+ "/advertiser/updateMainQualification" ;
		ADVERTISER_UPDATE_OPTIONAL_QUALIFICATION_URL = SERVER_URL + "/" + VERSION
				+ "/advertiser/updateOptionalQualification" ;
		
		VIEWCONFIG_SSP_SETTING_URL = SERVER_URL + "/" + VERSION + "/viewconfig/ssp/setting" ;
		
		REPORT_RTB_URL = SERVER_URL + "/" + VERSION + "/report/rtb";
		REPORT_CONSUME_URL = SERVER_URL + "/" + VERSION + "/report/consume";
		REPORT_ADVERTISER_CONSUME_URL = SERVER_URL + "/" + VERSION + "/report/advertiser";
		REPORT_CREATIVE_RTB_URL = SERVER_URL + "/" + VERSION + "/report/creativeRTB";
		
	}

	public static JSONObject getAuthHeader() {
		JSONObject authHeader = new JSONObject();
		authHeader.put("dspId", DSPID);
		authHeader.put("token", TOKEN);
		return authHeader;
	}
}