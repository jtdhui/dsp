package com.jtd.statistic.util;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.jtd.statistic.em.Constants;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月11日
 * @项目名称 dsp-counter
 * @描述 <p></p>
 */
@Component
public class CookieUtil {

	public static final String ID = "i";
	public static final String SID = "si";
	public static final String CLICK_KEY = "ck";
	public static final String SCLICK_KEY = "sck";

	@Value("${domain}")
	private String domain;

	/**
	 * @return [cookieid httpcookie httpscookie]
	 * 生成一个cookie
	 */
	public String[] nextCookieId() {
		//生成一个唯一的cookieid
		String cookieId = md5(UUID.randomUUID().toString());
		StringBuilder sb = new StringBuilder();
		//拼装写入到客户端的，cookie字符串，格式为 id=712c05f0e90fdb8c35c3ee8f5ad8e0e7;Max-Age=2147483647;Path=/;Domain=域名;Secure
		/**
		 * id：为cookieid  特殊
		 * Max-Age:为cookie过期时间
		 * Path:它指定与cookie关联在一起的网页。在默认的情况下cookie会与创建它的网页，及该网页处于同一目录下的网页以及与这个网页所在目录下的子目录下的网页关联
		 * Domain：domain属性可以使多个web服务器共享cookie。domain属性的默认值是创建cookie的网页所在服务器的主机名。不能将一个cookie的域设置成服务器所在的域之外的域。
		 * Secure：它是一个布尔值，指定在网络上如何传输cookie，默认是不安全的，通过一个普通的http连接传输
		 */
		sb.append(ID).append("=").append(cookieId).append("; Max-Age=").append(Integer.MAX_VALUE).append("; Path=/; Domain=").append(domain);
//		sb.append(ID).append("=").append(cookieId).append("; Max-Age=").append(Integer.MAX_VALUE).append("; Path=/");
		String c = sb.toString();
		sb.setLength(0);
		sb.append(SID).append("=").append(cookieId).append("; Max-Age=").append(Integer.MAX_VALUE).append("; Path=/; Domain=").append(domain).append("; Secure");
//		sb.append(SID).append("=").append(cookieId).append("; Max-Age=").append(Integer.MAX_VALUE).append("; Path=/; Secure");
		String sc = sb.toString();
		return new String[]{cookieId, c, sc};
	}

	public static void main(String[] args) {
		CookieUtil aa=new CookieUtil();
		
		System.out.println(aa.md5(UUID.randomUUID().toString()));
	}
	
	/**
	 * @return  [clickkey httpkey httpskey]
	 * 
	 */
	public String[] nextClickKey() {
		//获取一个唯一不重复的key
		String key = md5(UUID.randomUUID().toString());
		StringBuilder sb = new StringBuilder();
		//ck=95d945c6093124e49829b3e0f27e2275;Max-Age=2592000; Path=/; Domain=配置文件中得域名;Secure
		sb.append(CLICK_KEY).append("=").append(key).append("; Max-Age=2592000; Path=/; Domain=").append(domain);
//		sb.append(CLICK_KEY).append("=").append(key).append("; Max-Age=2592000; Path=/");
		String c = sb.toString();
		sb.setLength(0);
		sb.append(SCLICK_KEY).append("=").append(key).append("; Max-Age=2592000; Path=/; Domain=").append(domain).append("; Secure");
//		sb.append(SCLICK_KEY).append("=").append(key).append("; Max-Age=2592000; Path=/; Secure");
		String sc = sb.toString();
		return new String[]{key, c, sc};
	}

	private String md5(String data){
		byte[] b = Constants.MD5.get().digest(data.getBytes());
		StringBuilder sb = new StringBuilder();
		for(int j = 0; j < b.length; j++) {
			sb.append(Constants.HEXS[b[j] >>> 4 & 15]);
			sb.append(Constants.HEXS[b[j] & 15]);
		}
		return sb.toString();
	}
}
