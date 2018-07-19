package com.jtd.commons;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月22日
 * @项目名称 dsp-common
 */

@Component
public class BeanFactory implements ApplicationContextAware{
	//上下文环境
	private static ApplicationContext context;

	@Override
	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		context = arg0;
	}
	/**
	 * 根据名称获取bean
	 * @param beanName
	 * @return
	 * @return Object
	 */
	public static Object getBean(String beanName) {
		return context.getBean(beanName);
	}
	/**
	 * 根据名称和类型获取bean
	 * @param beanName
	 * @param requiredType
	 * @return
	 * @return T
	 */
	public static <T> T getBean(String beanName, Class<T> requiredType) {
		return context.getBean(beanName, requiredType);
	}
	/**
	 * 根据类型获取bean
	 * @param requiredType
	 * @return
	 * @return T
	 */
	public static <T> T getBean(Class<T> requiredType) {
		return context.getBean(requiredType);
	}
}
