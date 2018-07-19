package com.jtd.statistic.util;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.annotation.PostConstruct;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.util.Base64;
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
public class PriceDecodeUtil {

	@Value("${tanxKey}")
	private String tanxKey;

	@Value("${besDKey}")
	private String besDKey;

	@Value("${besCKey}")
	private String besCKey;
	
	@Value("${vamEKey}")
	private String vamEKey;
	
	@Value("${vamIKey}")
	private String vamIKey;
	
	@Value("${xtraderToken}")
	private String xtraderToken;
	
	//互众价格解密秘钥
	@Value("${htKey}")
	private String hzKey;
	
	

	private byte[] tkey = new byte[16];
	private byte[] besekey = new byte[32];
	private byte[] besikey = new byte[32];

	@PostConstruct
	public void init() {
		char[] chars = tanxKey.toCharArray();
		for (int i = 0, j = 0; i < 16; i++, j++) {
			tkey[j] = (byte) (Integer.parseInt(String.valueOf(chars[i * 2]), 16) << 4 | Integer.parseInt(String.valueOf(chars[i * 2 + 1]), 16));
		}
		char[] pchars = besDKey.toCharArray();
		char[] cchars = besCKey.toCharArray();
		for (int i = 0, j = 0; i < 32; i++, j++) {
			besekey[j] = (byte) (Integer.parseInt(String.valueOf(pchars[i * 2]), 16) << 4 | Integer.parseInt(String.valueOf(pchars[i * 2 + 1]), 16));
			besikey[j] = (byte) (Integer.parseInt(String.valueOf(cchars[i * 2]), 16) << 4 | Integer.parseInt(String.valueOf(cchars[i * 2 + 1]), 16));
		}
	}

	/**
	 * 解码TANX的成交价格
	 * @param p
	 * @param bid
	 * @return
	 * @throws Exception
	 */
	public BigDecimal decodeTanxPrice(String encprice, String bid) throws Exception {
		byte[] encbs = Base64.decodeFast(URLDecoder.decode(encprice, "UTF-8"));
		StringBuilder bidb = new StringBuilder();
		for(int j = 1; j <= 16; j++) {
			bidb.append(Constants.HEXS[encbs[j] >>> 4 & 15]);
			bidb.append(Constants.HEXS[encbs[j] & 15]);
		}
		String ebid = bidb.toString();
		if(ebid.equalsIgnoreCase(bid)) {
			byte[] h4md5 = new byte[32];
			System.arraycopy(encbs, 1, h4md5, 0, 16);
			System.arraycopy(tkey, 0, h4md5, 16, 16);
			h4md5 = h4Md5(h4md5);
			byte[] settle = new byte[4];
			for(int j = 0; j < 4; j++) {
				settle[j] = (byte)((encbs[17 + j] ^ h4md5[j]) & 0xFF);
			}
			int set = littleEndianInt(settle, 0);
			byte[] crcbase = new byte[37];
			System.arraycopy(encbs, 0, crcbase, 0, 17);
			System.arraycopy(settle, 0, crcbase, 17, 4);
			System.arraycopy(tkey, 0, crcbase, 21, 16);
			byte[] crc = h4Md5(crcbase);
			if(crc[0] == encbs[21] && crc[1] == encbs[22] && crc[2] == encbs[23] &&  crc[3] == encbs[24]) {
				return new BigDecimal(set);
			}
		}
		return null;
	}

	/**
	 * 解码BES的成交价格
	 * @param p
	 * @return
	 * @throws Exception
	 */
	public BigDecimal decodeBesPrice(String encprice) throws Exception {
		byte[] encbs = WebSafeBase64.decode(encprice);
		byte[] iv = new byte[16];
		System.arraycopy(encbs, 0, iv, 0, 16);
		byte[] pad = hmacSha1(besekey, iv);
		byte[] settle = new byte[8];
		for(int j = 0; j < 8; j++) {
			settle[j] = (byte)((encbs[16 + j] ^ pad[j]) & 0xFF);
		}
		long set = littleEndianLong(settle, 0);
		byte[] priv = new byte[24];
		System.arraycopy(settle, 0, priv, 0, 8);
		System.arraycopy(encbs, 0, priv, 8, 16);
		byte[] cfs = hmacSha1(besikey, priv);
		if(cfs[0] == encbs[24] && cfs[1] == encbs[25] && cfs[2] == encbs[26] && cfs[3] == encbs[27]) {
			return new BigDecimal(set);
		}
		return null;
	}
	
	/**
	 * 解码VAM的成交价格
	 * @param p
	 * @param bid
	 * @return
	 * @throws Exception
	 */
	public BigDecimal decodeVamPrice(String encprice) throws Exception {
		byte[] bytes = Decrypter.decryptSafeWebStringToByte(encprice, vamEKey, vamIKey);
		DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bytes));
		long value = 0;
		try {
			value = dis.readLong();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// VAM的结果是乘10之后的分/CPM
		return new BigDecimal(value).divide(new BigDecimal(10));
	}

	/**
	 * 解码零集的成交价格
	 * @param p
	 * @param bid
	 * @return
	 * @throws Exception
	 */
	public BigDecimal decodeXtraderPrice(String encprice) throws Exception {
		String retStr = Decrypter.decrypt(encprice, xtraderToken);
		long value=0;
		try {
			if(retStr!=null&&retStr.length()>0){
				value=Long.parseLong(retStr.split("_")[0]);
			}else{
				throw new Exception("没有取到成交价格。");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// VAM的结果是乘10之后的分/CPM
		return new BigDecimal(value);//.divide(new BigDecimal(10));
	}
	
	public BigDecimal decodeHzPrice(String encprice) throws Exception {
		float value=0;
		try {
			byte[] b_decode = HzPriceDec.safeUrlBase64Decode(encprice);
			
			String strPrice = HzPriceDec.decry_RC4(b_decode,hzKey);
			value=Float.parseFloat(strPrice);
		} catch (Exception e) {
			throw new Exception("没有取到成交价格。");
		}
		return new BigDecimal(value);
	}
	
	// 一堆工具方法
	private static byte[] hmacSha1(byte[] key, byte[] data) throws InvalidKeyException {
		Mac mac = null;
		try {
			mac = Mac.getInstance("HmacSHA1");
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
		mac.init(new SecretKeySpec(key, "HmacSHA1"));
		return mac.doFinal(data);
	}
	private static int littleEndianInt(byte[] b, int offset) {
		 return b[offset + 3] << 24 | (b[offset + 2] & 0xff) << 16
	                | (b[offset + 1] & 0xff) << 8 | (b[offset] & 0xff);
	}
	private static long littleEndianLong(byte[] b, int offset) {
		 return b[offset] << 56 
				 	| (b[offset + 1] & 0xff) << 48
	                | (b[offset + 2] & 0xff) << 40 
	                | (b[offset + 3] & 0xff) << 32
	                | (b[offset + 4] & 0xff) << 24 
	                | (b[offset + 5] & 0xff) << 16
	                | (b[offset + 6] & 0xff) << 8 
	                | (b[offset + 7] & 0xff);
	}
	private static byte[] h4Md5(byte[] src) {
		byte[] ret = new byte[4];
		byte[] md5 = Constants.MD5.get().digest(src);
		System.arraycopy(md5, 0, ret, 0, 4);
		return ret;
	}
}
