package com.jtd.engine.ad;

import com.alibaba.fastjson.util.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月8日
 * @项目名称 dsp-engine
 * @描述 <p></p>
 */
public class TanxDeviceidDecoder {

	private static final ThreadLocal<MessageDigest> MD5 = new ThreadLocal<MessageDigest>() {
		public MessageDigest initialValue() {
			try {
				return MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException e) {
				return null;
			}
		}
	};

	// 设备号解码的key
	private String tanxKey = "425ccd064173774576a619f95aabbd18";

	private byte[] tkey = new byte[16];
	private byte[] md5key = null;

	public void init() {
		char[] chars = tanxKey.toCharArray();
		for (int i = 0, j = 0; i < 16; i++, j++) {
			tkey[j] = (byte) (Integer.parseInt(String.valueOf(chars[i * 2]), 16) << 4 | Integer.parseInt(String.valueOf(chars[i * 2 + 1]), 16));
		}
		md5key = MD5.get().digest(tkey);
	}

	public String decode(String encdevid) {
		if(encdevid == null) return null;
		try {
			byte[] encbs = Base64.decodeFast(encdevid);
			int len = encbs[1];
			byte[] deviceid = new byte[len];
			for (int i = 0; i < len; i++) {
				deviceid[i] = (byte) ((encbs[2 + i] ^ md5key[i % 16]) & 0xFF);
			}

			// Check CRC
			MessageDigest d = MD5.get();
			d.update(encbs, 0, 2);
			d.update(deviceid);
			d.update(tkey);
			byte[] h4md5 = MD5.get().digest();
			if (h4md5[0] == encbs[2 + len] && h4md5[1] == encbs[3 + len] && h4md5[2] == encbs[4 + len] && h4md5[3] == encbs[5 + len])
				return new String(deviceid);
			return null;
		} catch (Throwable e) {
			return null;
		}
	}

	/**
	 * @param tanxKey the tanxKey to set
	 */
	public void setTanxKey(String tanxKey) {
		this.tanxKey = tanxKey;
	}
}
