package com.jtd.utils.mk;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;




/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置
 * @创建日期 2016年9月12日
 * @项目名称 dsp-tracker
 * @描述
 *     <p>
 *     </p>
 */
public class CookieUtil {

	// cookieId的key
	public static final String ID = "i";
	// https cookieId前缀
	public static final String SID = "si";
	// click key
	public static final String CLICK_KEY = "ck";
	// https click key
	public static final String SCLICK_KEY = "sck";
	
	public static final String domain = "localhost";

	
	
	public static final String COOKIE = "Cookie";

	
	public static void main(String[] args) {
		Map<String,String> cookies=createCookie();
		for(Map.Entry<String, String> entry : cookies.entrySet()){
			System.out.println(entry.getKey()+"---"+entry.getValue());
		}
	}
	
	/**
	 * 生成cookie
	 */
	public static Map<String,String> createCookie() {
		//如果浏览器端没有cookie数据，则重新生成cookie
		String[] cid = nextCookieId();
		Map<String,String> map=new HashMap<String,String>();
		map.put(ID, cid[1]);
		map.put(SID, cid[2]);
		return map;
	}
	
	public static String[] nextCookieId() {
		String cookieId = md5(UUID.randomUUID().toString());
		StringBuilder sb = new StringBuilder();
		sb.append(ID).append("=").append(cookieId).append("; Max-Age=").append(Integer.MAX_VALUE).append("; Path=/; Domain=").append(domain);
//		sb.append(ID).append("=").append(cookieId).append("; Max-Age=").append(Integer.MAX_VALUE).append("; Path=/");
		String c = sb.toString();
		sb.setLength(0);
		sb.append(SID).append("=").append(cookieId).append("; Max-Age=").append(Integer.MAX_VALUE).append("; Path=/; Domain=").append(domain).append("; Secure");
//		sb.append(SID).append("=").append(cookieId).append("; Max-Age=").append(Integer.MAX_VALUE).append("; Path=/; Secure");
		String sc = sb.toString();
		return new String[]{cookieId, c, sc};
	}

	public static String[] nextClickKey() {
		String key = md5(UUID.randomUUID().toString());
		StringBuilder sb = new StringBuilder();
		sb.append(CLICK_KEY).append("=").append(key).append("; Max-Age=60; Path=/; Domain=").append(domain);
//		sb.append(CLICK_KEY).append("=").append(key).append("; Max-Age=60; Path=/");
		String c = sb.toString();
		sb.setLength(0);
		sb.append(SCLICK_KEY).append("=").append(key).append("; Max-Age=60; Path=/; Domain=").append(domain).append("; Secure");
//		sb.append(SCLICK_KEY).append("=").append(key).append("; Max-Age=60; Path=/; Secure");
		String sc = sb.toString();
		return new String[]{key, c, sc};
	}

	private static String md5(String data){
		byte[] b = Constants.MD5.get().digest(data.getBytes());
		StringBuilder sb = new StringBuilder();
		for(int j = 0; j < b.length; j++) {
			sb.append(Constants.HEXS[b[j] >>> 4 & 15]);
			sb.append(Constants.HEXS[b[j] & 15]);
		}
		return sb.toString();
	}
	
	
	

	/**
	 * 解析Cookie，从相应头里拿到cookie，这里拿到的是dsp域下的cookie
	 * 
	 * @param req
	 * @return
	 */
	/*
	public static Map<String, String> parseCookie(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();// 这样便可以获取一个cookie数组
		for (Cookie cookie : cookies) {
			cookie.getName();// get the cookie name
			cookie.getValue(); // get the cookie value
		}
		String strCookies = getCookieByName(request).getValue();
		if (StringUtils.isEmpty(strCookies))
			return null;
		Map<String, String> ret = new HashMap<String, String>();
		String[] cks = strCookies.split(";");
		for (String ck : cks) {
			int i = ck.indexOf("=");
			if (i == -1)
				continue;
			ret.put(ck.substring(0, i).trim(), ck.substring(i + 1).trim());
		}
		return ret;
	}
	*/

	/**
	 * 根据名字获取cookie
	 * 
	 * @param request
	 * @param name
	 *            cookie名字
	 * @return
	 */
	public static String getCookieByName(HttpServletRequest request) {
		Map<String, String> cookies = ReadCookieMap(request);
		
		String cookieid = cookies.get(CookieUtil.ID);
//		if (cookieMap.containsKey(COOKIE)) {
//			Cookie cookie = (Cookie) cookieMap.get(COOKIE);
//			return cookie;
//		} else {
//			return null;
//		}
		return cookieid;
	}
	
	/**
	 * 获取dsp的cookieid
	 * @param request
	 * @return
	 */
	public static String getCookieId(HttpServletRequest request) {
		Map<String, String> cookies = ReadCookieMap(request);
		String cookieid = cookies.get(CookieUtil.ID);
		return cookieid;
	}
	

	/**
	 * 将cookie放入到map中
	 * 
	 * @param request
	 * @return
	 */
	private static Map<String, String> ReadCookieMap(HttpServletRequest request) {
//		Map<String, Cookie> cookieMap = new HashMap<String, Cookie>();
		String cookies = request.getHeader("Cookie");
		
		//如果没有拿到cookie，则返回null
		if(cookies==null) return null;
		Map<String, String> ret = new HashMap<String, String>();
		//如果拿到cookie，则使用";"拆分数据
		String[] cks = cookies.split(";");
		for(String ck : cks) {
			//使用“=”拆分每一小段字符串，找到cookie中的key和value，put到ret中
			int i = ck.indexOf("=");
			if(i == -1) continue;
			ret.put(ck.substring(0, i).trim(), ck.substring(i + 1).trim());
		}
		return ret;
				
//				
//				
//				
//		Cookie[] cookies = request.getCookies();
//		if (null != cookies) {
//			for (Cookie cookie : cookies) {
//				cookieMap.put(cookie.getName(), cookie);
//			}
//		}
//		return cookieMap;
	}

}
