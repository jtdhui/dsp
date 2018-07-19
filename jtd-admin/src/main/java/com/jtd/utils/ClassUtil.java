package com.jtd.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.util.ClassUtils;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月25日
 * @项目名称 dsp-admin
 * @描述 <p>class 反射工具</p>
 */
public class ClassUtil {

	/**
	 * @param clazz
	 * @param recursion
	 * @return
	 */
	public static Map<String, Field> getDeclaredFieldsMap(Class<?> clazz, Boolean recursion) {
		Map<String, Field> fieldsMap = new HashMap<String, Field>();
		Field[] fields = getDeclaredFields(clazz, recursion);
		for (Field field : fields) {
			String fieldName = field.getName();
			Field existField = fieldsMap.get(fieldName);
			if (existField == null) {
				fieldsMap.put(fieldName, field);
			} else {
				// 有重名的属性，保留子类的，覆盖父类的
				Class<?> declaringClass = field.getDeclaringClass();
				Class<?> existDeclaringClass = existField.getDeclaringClass();
				if (existDeclaringClass.isAssignableFrom(declaringClass)) {
					// 已存在的是父类，覆盖
					fieldsMap.put(fieldName, field);
				}
			}
		}
		return fieldsMap;
	}

	/**
	 * 获取目标定义的属性
	 * 
	 * @param clazz
	 *            目标类定义
	 * @return
	 */
	public static Field[] getDeclaredFields(Class<?> clazz) {
		return getDeclaredFields(clazz, false);
	}

	/**
	 * 获取目标类定义的属性
	 * 
	 * @param clazz
	 *            目标类定义
	 * @param recursion
	 *            是否递归获取父类的的属性
	 * @return
	 */
	public static Field[] getDeclaredFields(Class<?> clazz, Boolean recursion) {
		if (clazz == null) {
			return new Field[0];
		}
		Field[] fields = clazz.getDeclaredFields();
		if (recursion) {
			Class<?> superClass = clazz.getSuperclass();
			Field[] superFields = getDeclaredFields(superClass, recursion);
			fields = (Field[]) ArrayUtils.addAll(fields, superFields);
		}
		return fields;
	}

	/**
	 * 获取类定义的命名(驼峰式)
	 * @param clazz
	 * @return
	 */
	public static String getHumpName(Class<?> clazz) {
		String className = ClassUtils.getShortName(clazz);
		className = className.substring(0, 1).toLowerCase() + className.substring(1);
		return className;
	}
}
