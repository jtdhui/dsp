package com.jtd.commons;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月22日
 * @项目名称 dsp-common
 * @描述 读取我配置文件
 */
public class PropertyConfig extends PropertyPlaceholderConfigurer{
	private Properties properties;
	private Set<Object> keys;
	
	@Override
	protected Properties mergeProperties() throws IOException {
		properties = super.mergeProperties();
		keys = properties.keySet();
		return properties;
	}
	
	/**
	 * 通过key获取value
	 * @param key
	 * @return
	 * @return String
	 */
	public String getProperty(String key) {
		String value = null;
		try {
			value = new String(properties.getProperty(key).getBytes("iso-8859-1"));
		} catch (Exception e) {
		}
		return value;
	}

	/**
	 * 前匹配查找
	 * @param matchKey
	 * @return 符合要求的结果集合[key,value]
	 */
	public List<String[]> findProperty(final String matchKey) {
		if (matchKey == null || matchKey.isEmpty()) {
			return null;
		}
		List<String> matchKeys = new ArrayList<String>();
		for (Object objectKey : keys) {
			String key = objectKey.toString();
			if (key.startsWith(matchKey)) {
				matchKeys.add(key);
			}
		}
		Collections.sort(matchKeys, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				o1 = o1.replace(matchKey, "");
				o2 = o2.replace(matchKey, "");
				Integer i1 = null, i2 = null;
				try {
					i1 = Integer.valueOf(o1);
				} catch (NumberFormatException e) {
				}
				try {
					i2 = Integer.valueOf(o2);
				} catch (NumberFormatException e) {
				}
				if (i1 != null && i2 != null) {
					return i1.compareTo(i2);
				}
				return 0;
			}
		});
		List<String[]> values = new ArrayList<String[]>();
		for (String key : matchKeys) {
			String value = properties.getProperty(key);
			try {
				value = new String(value.getBytes("iso-8859-1"));
			} catch (UnsupportedEncodingException e) {
			}
			values.add(new String[] { key, value });
		}
		return values;
	}

//	public static void main(String[] args) {
//		PropertyConfig p = new PropertyConfig();
//		Set<Object> keys = new HashSet<Object>();
//		keys.add("redis1");
//		keys.add("redis2");
//		keys.add("redis3");
//		keys.add("redis.group4");
//		p.keys = keys;
//		try {
//			p.mergeProperties();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		System.out.println(p.findProperty("redis"));
//	}
	
}
