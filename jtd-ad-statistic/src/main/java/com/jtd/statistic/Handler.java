package com.jtd.statistic;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Component;
/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月11日
 * @项目名称 dsp-counter
 * @描述 <p></p>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface Handler {
	String value() default "";
	/**
	 * 当前的处理器处理哪个URI的http请求
	 * 
	 * @return
	 */
	String uri() default "/";
}
