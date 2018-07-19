package com.jtd.web.service.adx.vam;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.net.util.Base64;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.apache.log4j.Logger;

import com.jtd.utils.FileUtil;

public class VamUtil {

	static Logger logger = Logger.getLogger("errorLog");

	private static final Properties props = new Properties();

	public static final int USER_NAME;
	public static final String PASSWORD;
	public static final String SERVER_URL;

	/***********************************pc***************************************************************************************
	
	/**
	 * 添加PC静态创意
	 */
	 public static final String PC_STATIC_CREATIVE_ADD_URL ;
	/**
	 * 更新PC静态创意
	 */
	 public static final String PC_STATIC_CREATIVE_UPDATE_URL ;
	 /**
	 * 查询PC静态创意
	 */
	 public static final String PC_STATIC_CREATIVE_QUERY_URL ;
	/**
	 * 查询pc静态创意审核结果
	 */
	public static final String PC_STATIC_CREATIVE_QUERY_STATUS_URL ;
	/**
	 * 查询pc动态创意
	 */
	public static final String PC_DYNAMIC_CREATIVE_QUERY_URL;
	/**
	 * 查询pc动态创意审核结果
	 */
	public static final String PC_DYNAMIC_CREATIVE_QUERY_STATUS_URL;
	
	/***********************************移动端***************************************************************************************
	
	/**
	 * 添加WAP静态创意
	 */
	public static final String WAP_STATIC_CREATIVE_ADD_URL;
	/**
	 * 更新WAP静态创意
	 */
	public static final String WAP_STATIC_CREATIVE_UPDATE_URL;
	/**
	 * 添加INAPP静态创意
	 */
	public static final String APP_STATIC_CREATIVE_ADD_URL;
	/**
	 * 更新IN_APP静态创意
	 */
	public static final String APP_STATIC_CREATIVE_UPDATE_URL;
	/**
	 * 查询移动静态创意
	 */
	public static final String MOBILE_STATIC_CREATIVE_QUERY_URL;
	/**
	 * 查询移动静态创意审核结果
	 */
	public static final String MOBILE_STATIC_CREATIVE_QUERY_STATUS_URL;
	/**
	 * 查询移动动态创意
	 */
	public static final String MOBILE_DYNAMIC_CREATIVE_QUERY_URL ;
	/**
	 * 查询移动动态创意审核结果
	 */
	public static final String MOBILE_DYNAMIC_CREATIVE_QUERY_STATUS_URL ;
	
	
	/***********************************pc视频***************************************************************************************
	
	/**
	 * 添加pc视频创意
	 */
	public static final String PC_VIDEO_CREATIVE_ADD_URL;
	/**
	 * 更新pc视频创意
	 */
	public static final String PC_VIDEO_CREATIVE_UPDATE_URL;
	/**
	 * 查询视频创意
	 */
	public static final String PC_VIDEO_CREATIVE_QUERY_URL ;
	/**
	 * 查询pc视频创意审核结果
	 */
	public static final String PC_VIDEO_CREATIVE_QUERY_STATUS_URL ;
	
	
	/***********************************移动视频***************************************************************************************
	
	/**
	 * 添加移动视频创意
	 */
	public static final String MOBILE_VIDEO_CREATIVE_ADD_URL;
	/**
	 * 更新移动视频创意
	 */
	public static final String MOBILE_VIDEO_CREATIVE_UPDATE_URL;
	/**
	 * 查询移动视频创意审核结果
	 */
	public static final String MOBILE_VIDEO_CREATIVE_QUERY_URL;
	/**
	 * 查询移动视频创意审核结果
	 */
	public static final String MOBILE_VIDEO_CREATIVE_QUERY_STATUS_URL;
	
	/**
	 * http请求头
	 */
	public static final Header AUTH_HEADER ;
	/**
	 * wap创意的html
	 */
	public static final String MOBILE_WAP_HTML ;
	
	static {

		try {

			Thread thread = Thread.currentThread();

			ClassLoader classLoder = thread.getContextClassLoader();

			InputStream is = classLoder.getResourceAsStream("vam.properties");

			props.load(is);

		} catch (FileNotFoundException e) {
			logger.error(e.getMessage(), e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		USER_NAME = Integer.parseInt(props.getProperty("USER_NAME"));
		PASSWORD = props.getProperty("PASSWORD");
		SERVER_URL = props.getProperty("SERVER_URL");
		
		String userNameAndPassword = USER_NAME + ":" + PASSWORD;

		String base64String = new String(Base64.encodeBase64(userNameAndPassword.getBytes()));
		AUTH_HEADER = new BasicHeader("Authorization", "Basic " + base64String);

		PC_STATIC_CREATIVE_ADD_URL = SERVER_URL + "/banner/add" ;
		PC_STATIC_CREATIVE_UPDATE_URL = SERVER_URL + "/banner/update" ;
		PC_STATIC_CREATIVE_QUERY_URL =  SERVER_URL + "/banner/get?id=${id}" ;
		PC_STATIC_CREATIVE_QUERY_STATUS_URL = SERVER_URL + "/banner/status?id=${id}";
		PC_DYNAMIC_CREATIVE_QUERY_URL = SERVER_URL + "/banner/get?id=${id}&type=d";
		PC_DYNAMIC_CREATIVE_QUERY_STATUS_URL = SERVER_URL + "/banner/status?id=${id}&type=d";
		
		WAP_STATIC_CREATIVE_ADD_URL = SERVER_URL + "/mobile/web-add";
		WAP_STATIC_CREATIVE_UPDATE_URL = SERVER_URL + "/mobile/web-update";
		APP_STATIC_CREATIVE_ADD_URL = SERVER_URL + "/mobile/inapp-add";
		APP_STATIC_CREATIVE_UPDATE_URL = SERVER_URL + "/mobile/inapp-update";
		MOBILE_STATIC_CREATIVE_QUERY_URL= SERVER_URL + "/mobile/get?id=${id}";
		MOBILE_STATIC_CREATIVE_QUERY_STATUS_URL = SERVER_URL + "/mobile/status?id=${id}";
		MOBILE_DYNAMIC_CREATIVE_QUERY_URL = SERVER_URL + "/mobile/get?id=${id}&type=d";
		MOBILE_DYNAMIC_CREATIVE_QUERY_STATUS_URL = SERVER_URL + "/mobile/status?id=${id}&type=d";

		PC_VIDEO_CREATIVE_ADD_URL = SERVER_URL + "/video/add";
		PC_VIDEO_CREATIVE_UPDATE_URL = SERVER_URL + "/video/update";
		PC_VIDEO_CREATIVE_QUERY_URL = SERVER_URL + "/video/get?id=${id}";
		PC_VIDEO_CREATIVE_QUERY_STATUS_URL = SERVER_URL + "/video/status?id=${id}";

		MOBILE_VIDEO_CREATIVE_ADD_URL = SERVER_URL + "/mobile-video/add";
		MOBILE_VIDEO_CREATIVE_UPDATE_URL = SERVER_URL + "/mobile-video/update";
		MOBILE_VIDEO_CREATIVE_QUERY_URL = SERVER_URL + "/mobile-video/get?id=${id}";
		MOBILE_VIDEO_CREATIVE_QUERY_STATUS_URL = SERVER_URL + "/mobile-video/status?id=${id}";
		
		File file = FileUtil.getFileInClassPath("dic/vamImg.tpl");
		String tpl = "" ;
		try {
			tpl = FileUtils.readFileToString(file);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			tpl = "" ;
		}
		MOBILE_WAP_HTML = tpl ;
	}

}
