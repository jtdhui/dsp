package com.jtd.tencent;

import java.io.InputStreamReader;
import java.util.Properties;

public class Config {
	
	private static Properties properties = new Properties();
	
	/** 腾讯社交广告HOST */
	public static String TENCENT_HOST = null;
	
	/** 应用回调服务器地址，仅支持 http 和 https，不支持指定端口号 */
	public static String SERVER_HOST = null;
	
	/** 应用 id，在开发者官网创建应用后获得 */
	public static Integer CLIENT_ID = null;
	
	/** 应用 secret，在开发者官网创建应用后获得 */
	public static String CLIENT_SECRET = null;
	
	/** 授权token */
	public static String TOKEN = null;
	
	/** 应用 refresh token */
	public static String REFRESH_TOKEN = null;
	
	/** token状态，token不是null，并且在有效期内为true，否为为false */
	public static boolean TOKEN_STATUS = false;
	
	/** refresh token状态，REFRESH_TOKEN不是null，并且再有效期内，否则为false */
	public static boolean REFRESH_TOKEN_STATUS = false;
	
	
	static
	{
		try{
			InputStreamReader inputStream = new InputStreamReader(Config.class.getClassLoader().getResourceAsStream("tencent.properties"),"UTF-8");
			properties.load(inputStream);
			
			TENCENT_HOST = properties.getProperty("tencent_host");
			CLIENT_ID = Integer.parseInt(properties.getProperty("client_id"));
			CLIENT_SECRET = properties.getProperty("client_secret");
			SERVER_HOST = properties.getProperty("server_host");
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
}
