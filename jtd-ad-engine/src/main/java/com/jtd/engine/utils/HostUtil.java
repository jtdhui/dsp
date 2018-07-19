package com.jtd.engine.utils;

import org.apache.commons.lang3.StringUtils;

import java.net.URL;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月8日
 * @项目名称 dsp-engine
 * @描述 <p></p>
 */
public class HostUtil {
	
	public static String getHost(String url) {
		if(!url.startsWith("http://")&&!url.startsWith("https://")){
			url="http://"+url;
		}
		if (StringUtils.isEmpty(url)) return null;
		String host = null;
		try {
			URL u = new URL(url);
			host = u.getHost();
		} catch (Exception e) {
			return null;
		}
		return host;
	}
}
