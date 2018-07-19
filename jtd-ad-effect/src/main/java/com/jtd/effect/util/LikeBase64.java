package com.jtd.effect.util;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月12日
 * @项目名称 dsp-tracker
 * @描述 <p></p>
 */
public class LikeBase64 {

	// 64个字符
	private static final char[] CHARS = { 
		'L', 'B', '6', 'F', 'E', 'D', 'G', '4', 
		'I', 'J', 'K', 'A', 'k', '8', 'O', 'P', 
		'Q', 'R', 'S', 'T', 'U', 'V', '1', 'g', 
		'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 
		'X', '9', 'i', 'j', 'M', 'l', 'm', 'n', 
		'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 
		'w', 'x', 'y', 'z', '0', 'W', '2', '3', 
		'H', '5', 'C', '7', 'N', 'h', '-', '_' };

	// 64个字符的index map
	private static final int[] CHARS_MAP = new int[128];
	static { for(int i = 0; i < 64; CHARS_MAP[CHARS[i]] = i++); }

	/**
	 * @param src
	 * @return
	 */
	public static final String encode(String src) {
		byte[] bs = null;
		try {
			bs = src.getBytes("UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		StringBuilder sb = new StringBuilder();

		int status = 0, left = 0, index = 0;
		for (byte b : bs) {
			switch (status) {
			case 0:
				index = (b >>> 2) & 0x3F;
				sb.append(CHARS[index]);
				left |= (b & 0x03) << 4;
				status = 1;
				break;
			case 1:
				left |= (b >>> 4) & 0x0F;
				index = left;
				sb.append(CHARS[index]);
				left = (b & 0x0F) << 2;
				status = 2;
				break;
			case 2:
				left |= (b >>> 6) & 0x03;
				index = left;
				sb.append(CHARS[index]);
				index = b & 0x3F;
				sb.append(CHARS[index]);

				// 状态复原
				status = left = index = 0;
			}
		}
		if (status > 0) {
			index = left;
			sb.append(CHARS[index]);
		}

		return sb.toString();
	}

	/**
	 * @param src
	 * @return
	 */
	public static final String decode(String src) {
		try {
			char[] chars = src.toCharArray();
			int status = 0, dstIndex = 0, len = chars.length * 6 >>> 3;
			byte[] dst = new byte[len];
			for(char c : chars) {
				int index = CHARS_MAP[c];
				switch(status) {
				case 0:
					dst[dstIndex] |= index << 2;
					status = 1;
					break;
				case 1:
					dst[dstIndex++] |= (index >>> 4) & 0x03;
					if(len == dstIndex) break;
					dst[dstIndex] |= (index & 0x0F) << 4;
					status = 2;
					break;
				case 2:
					dst[dstIndex++] |= (index >>> 2) & 0x0F;
					if(len == dstIndex) break;
					dst[dstIndex] |= (index & 0x03) << 6;
					status = 3;
					break;
				case 3:
					dst[dstIndex++] |= index;
					status = 0;
				}
			}
			return new String(dst, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
