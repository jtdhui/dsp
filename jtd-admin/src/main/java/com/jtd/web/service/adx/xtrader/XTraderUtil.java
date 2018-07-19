package com.jtd.web.service.adx.xtrader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;

public class XTraderUtil {

	static Logger logger = Logger.getLogger("errorLog");

	private static final Properties besProps = new Properties();

	public static final int DSPID;
	public static final String TOKEN;
	public static final String SERVER_URL;
	public static final String CREATIVE_ADD_URL;
	public static final String CREATIVE_QUERY_AUDIT_STATE_URL;

	static {

		try {
			
			Thread thread = Thread.currentThread();
			
			ClassLoader classLoder  = thread.getContextClassLoader();
			
			InputStream is = classLoder.getResourceAsStream("xtrader.properties");
			
			besProps.load(is);
			
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage(), e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		DSPID = Integer.parseInt(besProps.getProperty("DSPID"));
		TOKEN = besProps.getProperty("TOKEN");
		SERVER_URL = besProps.getProperty("SERVER_URL");
		
		CREATIVE_ADD_URL = SERVER_URL + "/dsp/v2/api/upload";
		CREATIVE_QUERY_AUDIT_STATE_URL = SERVER_URL + "/dsp/v2/api/status";

	}

}
