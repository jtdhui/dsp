package com.jtd.web.service.adx.hzeng;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class HZengUtil {

	static Logger logger = Logger.getLogger("errorLog");

	private static final Properties props = new Properties();

	public static final int DSPID;
	public static final String TOKEN;
	public static final String SERVER_URL;
	
	public static final String ADVERTISER_GETALL_URL;
	public static final String ADVERTISER_GET_URL;
	public static final String ADVERTISER_ADD_URL;
	public static final String ADVERTISER_UPDATE_URL;
	public static final String ADVERTISER_QUERY_QUALIFICATION_URL;
	
	public static final String CREATIVE_GETALL_URL;
	public static final String CREATIVE_GET_URL;
	public static final String CREATIVE_ADD_URL;
	public static final String CREATIVE_UPDATE_URL;
	public static final String CREATIVE_QUERY_AUDIT_STATE_URL;
	
	
	static {

		try {
			
			Thread thread = Thread.currentThread();
			
			ClassLoader classLoder  = thread.getContextClassLoader();
			
			InputStream is = classLoder.getResourceAsStream("hzeng.properties");
			
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
		SERVER_URL = props.getProperty("SERVER_URL") + "?dspId=" + DSPID + "&token=" + TOKEN ;
		
		ADVERTISER_ADD_URL = SERVER_URL + "&method=hzeng.advertiser.add";
		ADVERTISER_UPDATE_URL = SERVER_URL + "&method=hzeng.advertiser.update";
		ADVERTISER_GET_URL = SERVER_URL + "&method=hzeng.advertiser.get";
		ADVERTISER_GETALL_URL = SERVER_URL + "&method=hzeng.advertiser.getAll";
		ADVERTISER_QUERY_QUALIFICATION_URL = SERVER_URL + "&method=hzeng.advertiser.queryQualification";
		
		CREATIVE_ADD_URL = SERVER_URL + "&method=hzeng.creative.add";
		CREATIVE_UPDATE_URL = SERVER_URL + "&method=hzeng.creative.update";
		CREATIVE_GET_URL = SERVER_URL + "&method=hzeng.creative.get";
		CREATIVE_GETALL_URL = SERVER_URL + "&method=hzeng.creative.getAll";
		CREATIVE_QUERY_AUDIT_STATE_URL = SERVER_URL + "&method=hzeng.creative.queryAuditState";
		
	}

}
