package com.jtd.engine.utils;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月8日
 * @项目名称 dsp-engine
 * @描述 <p></p>
 */
public class TextLineDelimiter {

	/** 换行符 */
	public static final String DELIMITER;
	static {
		ByteArrayOutputStream out = new ByteArrayOutputStream(3);
		PrintWriter writer = new PrintWriter(out);
		writer.println();
		writer.flush();
		DELIMITER = new String(out.toByteArray(), 0, out.size());
	}
}
