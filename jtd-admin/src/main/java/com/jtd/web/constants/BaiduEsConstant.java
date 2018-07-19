package com.jtd.web.constants;

public class BaiduEsConstant {
	
	public static int DSPID;
	public static String TOKEN;
	
	public static String SERVER_URL;
	public static String VERSION;
	public static String CREATIVE_GETALL_URL;
	public static String CREATIVE_GET_URL;
	public static String CREATIVE_ADD_URL;
	public static String CREATIVE_UPDATE_URL;
	public static String CREATIVE_QUERY_AUDIT_STATE_URL;

	public static String ADVERTISER_GETALL_URL;
	public static String ADVERTISER_GET_URL;
	public static String ADVERTISER_ADD_URL;
	public static String ADVERTISER_UPDATE_URL;
	public static String ADVERTISER_UPLOAD_QUALIFICATION_URL;
	public static String ADVERTISER_UPDATE_MAIN_QUALIFICATION_URL;

	public static String ADVERTISER_UPDATE_OPTIONAL_QUALIFICATION_URL;
	public static String ADVERTISER_QUERY_QUALIFICATION_URL;
	
	public static String REPORT_RTB_URL;
	public static String REPORT_CONSUME_URL;

	public static final String ACCEPT = "application/json;charset=utf-8";
	public static final String CONTENT_TYPE = "application/json;charset=utf-8";
	
	static {
		DSPID = Integer.parseInt(BaiduEsConfig.getValue("DSPID"));
		TOKEN = BaiduEsConfig.getValue("TOKEN");
		SERVER_URL = BaiduEsConfig.getValue("SERVER_URL");
		VERSION = BaiduEsConfig.getValue("VERSION");
		
		CREATIVE_GETALL_URL = SERVER_URL + "/" + VERSION + "/creative/getAll";
		CREATIVE_GET_URL = SERVER_URL + "/" + VERSION + "/creative/get";
		CREATIVE_ADD_URL = SERVER_URL + "/" + VERSION + "/creative/add";
		CREATIVE_UPDATE_URL = SERVER_URL + "/" + VERSION + "/creative/update";
		CREATIVE_QUERY_AUDIT_STATE_URL = SERVER_URL + "/" + VERSION + "/creative/queryAuditState";

		ADVERTISER_GETALL_URL = SERVER_URL + "/" + VERSION + "/advertiser/getAll";
		ADVERTISER_GET_URL = SERVER_URL + "/" + VERSION + "/advertiser/get";
		ADVERTISER_ADD_URL = SERVER_URL + "/" + VERSION + "/advertiser/add";
		ADVERTISER_QUERY_QUALIFICATION_URL = SERVER_URL + "/" + VERSION + "/advertiser/queryQualificationInfo";
		ADVERTISER_UPLOAD_QUALIFICATION_URL=SERVER_URL + "/" + VERSION + "/advertiser/uploadQualification";
		ADVERTISER_UPDATE_MAIN_QUALIFICATION_URL= SERVER_URL+ "/" + VERSION + "/advertiser/updateMainQualification";
		ADVERTISER_UPDATE_OPTIONAL_QUALIFICATION_URL=SERVER_URL+ "/" + VERSION + "/advertiser/updateMainQualification";
		ADVERTISER_UPDATE_URL = SERVER_URL + "/" + VERSION + "/advertiser/update";
		
		REPORT_RTB_URL = SERVER_URL + "/" + VERSION + "/report/rtb";
		REPORT_CONSUME_URL = SERVER_URL + "/" + VERSION + "/report/consume";
	}


}
