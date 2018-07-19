package com.jtd.utils.mk;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月12日
 * @项目名称 dsp-tracker
 * @描述 <p>Handler工具类</p>
 */
public class HandlerUtil {

	public final static String LOCATION="location";
	
	
	
	 public static void setCookie(HttpServletResponse response, Map<String,String> cookies) {
		 	
		 	response.addHeader("Server", "Counter");
		 	response.addHeader("Pragma", "No-cache");
		 	response.addHeader("Cache-Control", "no-cache");
		 	response.addHeader("Expires", "Thu, 01 Jan 1970 00:00:00 GMT");
		 	response.addHeader("Location", LOCATION);
		 	
	        if (cookies != null && cookies.size() > 0) {
		 		response.addHeader("P3P", "CP='IDC DSP COR ADM DEVi TAIi PSA PSD IVAi IVDi CONi HIS OUR IND CNT'");
		 		
		 		for(Map.Entry<String, String> entry : cookies.entrySet()){
		 			response.addHeader("Set-Cookie", entry.getValue());
		 		}
		 		
		 		/*
		 		for (Map.Entry<String, String> entry : cookies.entrySet()){
		 		// new一个Cookie对象,键值对为参数
			        Cookie cookie = new Cookie(entry.getKey(), entry.getValue());
			        // tomcat下多应用共享
			        cookie.setPath("/");
			        // 如果cookie的值中含有中文时，需要对cookie进行编码，不然会产生乱码
			        try {
			            URLEncoder.encode(entry.getValue(), "utf-8");
			        } catch (UnsupportedEncodingException e) {
			            e.printStackTrace();
			        }
			        cookie.setMaxAge(Integer.MAX_VALUE);
			        //cookie.setValue(ck);
			        response.addCookie(cookie);
		 		}
		 		*/
//				for (String ck : cookies) {
//					
//				}
			}
		 
	    }
}
