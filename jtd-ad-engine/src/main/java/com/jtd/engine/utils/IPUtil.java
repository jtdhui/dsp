package com.jtd.engine.utils;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月8日
 * @项目名称 dsp-engine
 * @描述 <p></p>
 */
public class IPUtil {

	/**
	 * 点分IP转整型IP,转不成功返回-1
	 * @param str
	 * @return
	 */
	public static final long str2Ip(String str) {
		if (null == str) return -1;
		String[] strs = str.split("[.]");
		if (strs.length != 4) {
			return -1;
		}
		try {
			int[] ips = new int[4];
			for (int i = 0; i < 4; i++) {
				ips[i] = Integer.parseInt(strs[i]);
			}
			return (long)ips[0] << 24 | ips[1] << 16 | ips[2] << 8 | ips[3];
		} catch (Exception e) {
			return -1;
		}
	}

	/**
	 * 整型IP转点分IP
	 * @param ip
	 * @return
	 */
	public static final String ip2Str(long ip) {
		return new StringBuilder(String.valueOf(ip >>> 24)).append(".")
				.append(ip >>> 16 & 255).append(".").append(ip >>> 8 & 255)
				.append(".").append(ip & 255).toString();
	}

}
