package com.jtd.web.converter;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月27日
 * @项目名称 springmvc-mybatis-test2
 * @描述 <p>自定义日期转换器</p>
 */
public class CustomDateConverter implements Converter<String, Date>{

	public Date convert(String source) {
		try {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(source);
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return null;
	}

}
