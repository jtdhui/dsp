package com.jtd.web.converter;


import org.springframework.core.convert.converter.Converter;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月27日
 * @项目名称 springmvc-mybatis-test2
 * @描述 <p>去除字符串两边空格转换器</p>
 */
public class StringTrimConverter implements Converter<String, String>{

	public String convert(String source) {
		try {
			if(source!=null){
				source=source.trim();
				if(source.equals("")){
					return null;
				}
			}
		} catch (Exception e) {
		}
		return source; 
	}

}
