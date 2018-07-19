package com.jtd.utils;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;


/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月25日
 * @项目名称 dsp-admin
 * @描述 <p>Json工具类</p>
 */
public class JsonUtil {

	public static String toJSONString(Object javaObject, SerializerFeature... serializerFeatures) {

		SerializeWriter out = new SerializeWriter();
		String jsonStr;
		try {
			JSONSerializer serializer = new JSONSerializer(out);
			if (serializerFeatures != null) {
				for (SerializerFeature serializerFeature : serializerFeatures) {
					serializer.config(serializerFeature, true);
				}
			}
			serializer.config(SerializerFeature.WriteEnumUsingToString, false);
			serializer.write(javaObject);
			jsonStr = out.toString();
		} finally {
			out.close();
		}
		return jsonStr;
	}

	public static void main(String[] args) {
//		Partner a = new Partner();
//		a.setAuditType(AuditType.ADUIT);
//		System.out.println(toJSONString(a));
	}
}
