/**
 * 2012-11-29 下午07:57:35 by asme
 */
package com.jtd.statistic.util;

import java.util.Arrays;
/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月11日
 * @项目名称 dsp-counter
 * @描述 <p></p>
 */
public class WebSafeBase64 {

	private static final char CA[];
	private static final int IA[];

	static {
		CA = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_".toCharArray();
		IA = new int[256];
		Arrays.fill(IA, -1);
		int i = 0;
		for (int iS = CA.length; i < iS; i++) IA[CA[i]] = i;
		IA[61] = 0;
	}

	public static final byte[] decode(String s) {
		int sLen = s.length();
		if (sLen == 0)
			return new byte[0];
		int sIx = 0;
		int eIx;
		for (eIx = sLen - 1; sIx < eIx && IA[s.charAt(sIx) & 255] < 0; sIx++);
		for (; eIx > 0 && IA[s.charAt(eIx) & 255] < 0; eIx--);
		int pad = s.charAt(eIx) != '=' ? 0 : ((int) (s.charAt(eIx - 1) != '=' ? 1 : 2));
		int cCnt = (eIx - sIx) + 1;
		int sepCnt = sLen <= 76 ? 0 : (s.charAt(76) != '\r' ? 0 : cCnt / 78) << 1;
		int len = ((cCnt - sepCnt) * 6 >> 3) - pad;
		byte dArr[] = new byte[len];
		int d = 0;
		int cc = 0;
		int eLen = (len / 3) * 3;
		do {
			if (d >= eLen) break;
			int i = IA[s.charAt(sIx++)] << 18 | IA[s.charAt(sIx++)] << 12 | IA[s.charAt(sIx++)] << 6 | IA[s.charAt(sIx++)];
			dArr[d++] = (byte) (i >> 16);
			dArr[d++] = (byte) (i >> 8);
			dArr[d++] = (byte) i;
			if (sepCnt > 0 && ++cc == 19) {
				sIx += 2;
				cc = 0;
			}
		} while (true);
		if (d < len) {
			int i = 0;
			for (int j = 0; sIx <= eIx - pad; j++) i |= IA[s.charAt(sIx++)] << 18 - j * 6;
			for (int r = 16; d < len; r -= 8) dArr[d++] = (byte) (i >> r);
		}
		return dArr;
	}
}
