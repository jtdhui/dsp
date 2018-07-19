package com.jtd.effect.util;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.jtd.effect.em.Constants;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月12日
 * @项目名称 dsp-tracker
 * @描述 <p>cookie工具类</p>
 */
@Component
public class CookieUtil {
	//cookieId的key
	public static final String ID = "i";
	//https cookieId前缀
	public static final String SID = "si";
	//click key
	public static final String CLICK_KEY = "ck";
	//https click key
	public static final String SCLICK_KEY = "sck";

	@Value("${domain}")
	private String domain;

	public String[] nextCookieId() {
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

	public String[] nextClickKey() {
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
