package com.jtd.utils;

import java.text.DecimalFormat;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.jtd.commons.CommonRuntimeException;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月25日
 * @项目名称 dsp-admin
 * @描述 <p></p>
 */
public class NumberUtil {
	private static ThreadLocal<DecimalFormat> dfLocal = new ThreadLocal<DecimalFormat>() {

		@Override
		protected DecimalFormat initialValue() {
			return new DecimalFormat("######0.00");
		}
	};

	public static String changeDoubleToStr(Double number) {
		DecimalFormat df = dfLocal.get();
		return df.format(number);
	}
	
	/**
	 * 是否未整数
	 * @param input
	 * @return
	 */
	public static boolean isInteger(String input){
		if(StringUtils.isEmpty(input)){
			throw new CommonRuntimeException("输入处理数字为空", null);
		}
		String regex ="[1-9]\\d+";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(input);
	   return matcher.matches();
	}
	
	/**
	 * 在一定范围内获取一个随机数
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	public static int getRandomInt(int min, int max) {
		Random random = new Random();
		int s = random.nextInt(max) % (max - min + 1) + min;
		return s;
	}
}
