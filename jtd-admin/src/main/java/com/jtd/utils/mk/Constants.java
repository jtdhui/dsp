package com.jtd.utils.mk;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.SimpleTimeZone;
/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月12日
 * @项目名称 dsp-tracker
 * @描述 <p></p>
 */
public class Constants {

	public static final byte[] IMG11 = {71, 73, 70, 56, 57, 97, 1, 0, 1, 0, -128, -1, 0, -64, -64, -64, 0, 0, 0, 33, -7, 4, 1, 0, 0, 0, 0, 44, 0, 0, 0, 0, 1, 0, 1, 0, 0, 2, 2, 68, 1, 0, 59};
	public static final char[] HEXS = {'0', '1', '2', '3', '4', '5', '6', '7' ,'8','9','a','b','c','d','e','f'};

	// 日期格式化
	public static final ThreadLocal<SimpleDateFormat> GMT_FORMATERS = new ThreadLocal<SimpleDateFormat>() {
		protected SimpleDateFormat initialValue() {
			SimpleDateFormat gmtFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
			gmtFormat.setTimeZone(new SimpleTimeZone(0, "GMT"));
			return gmtFormat;
		}
	};

	public static final ThreadLocal<MessageDigest> MD5 = new ThreadLocal<MessageDigest>() {
		public MessageDigest initialValue() {
			try {
				return MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException e) {
				return null;
			}
		}
	};
}
