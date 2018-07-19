package com.jtd.tencent.service.impl;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jtd.tencent.Config;
import com.jtd.tencent.TimerTask;
import com.jtd.tencent.service.AuthService;
import com.jtd.tencent.util.HttpRequestor;

@Service
public class AuthServiceImpl implements AuthService
{
	private static Logger log = Logger.getLogger(AuthServiceImpl.class);
	
	@Override
	public void getToken(Integer grantType,String authorizationCode)
	{
		String url = null;
		/** 授权码方式获取 token */
		if(grantType.equals(1))  
		{
			url = Config.TENCENT_HOST + "/oauth/token"
				+ "?client_id=" + Config.CLIENT_ID 
				+ "&client_secret=" + Config.CLIENT_SECRET 
				+ "&grant_type=authorization_code"
				+ "&authorization_code=" + authorizationCode 
				+ "&redirect_uri=http://" + Config.SERVER_HOST + "/qq/auth.action";;
		}
		 /** refresh_token （刷新 token ） */
		else{
			url = Config.TENCENT_HOST + "/oauth/token"
				+ "?client_id=" + Config.CLIENT_ID 
				+ "&client_secret=" + Config.CLIENT_SECRET 
				+ "&grant_type=refresh_token"
				+ "&refresh_token=" + Config.REFRESH_TOKEN 
				+ "&redirect_uri=http://" + Config.SERVER_HOST + "/qq/auth.action";;
		}
		log.info("获取token请求url：" + url);
		try{
			String rspStr = HttpRequestor.doGet(url);
			if(rspStr == null || rspStr.trim().length() == 0)
			{
				log.info("获取token失败！请求响应为空！" + rspStr);
				return ;
			}
			/** 腾讯返回token内容 */
			JSONObject rspJson = JSON.parseObject(rspStr);
			if(!rspJson.getInteger("code").equals(0))
			{
				log.info("获取token失败！" + rspStr);
				return ;
			}
			/** token内容中data信息 */
			JSONObject dataJson =JSON.parseObject(rspJson.getString("data"));
			
			Config.TOKEN = dataJson.getString("access_token");                         //应用 access token
			Config.REFRESH_TOKEN = dataJson.getString("refresh_token");                       //应用 refresh token，当 grant_type=refresh_token 时不返回
			Integer accessTokenExpiresIn = dataJson.getInteger("access_token_expires_in");   //access_token 过期时间，单位（秒）
			Integer refreshTokenExpiresIn = dataJson.getInteger("refresh_token_expires_in"); //refresh_token 过期时间，单位（秒），当 grant_type=refresh_token 时不返回
			
			if(Config.TOKEN != null && Config.TOKEN.trim().length() > 0
					&& Config.REFRESH_TOKEN != null && Config.REFRESH_TOKEN.trim().length() > 0)
			{
				Config.TOKEN_STATUS = true;
				Config.REFRESH_TOKEN_STATUS = true;
				/** 开启token超时监听定时任务 */
				TimerTask task = new TimerTask(accessTokenExpiresIn,refreshTokenExpiresIn);  
				task.excute(task);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
