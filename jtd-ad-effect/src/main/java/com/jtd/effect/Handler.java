package com.jtd.effect;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月12日
 * @项目名称 dsp-tracker
 * @描述 <p>自定义注解，被这个注解标注过的类会handler处理</p>
 */
@Target(ElementType.TYPE)//表示Handler这个注解只能用户修饰类型，这里类也是类型;{ElementType.FIELD, ElementType.METHOD}
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
