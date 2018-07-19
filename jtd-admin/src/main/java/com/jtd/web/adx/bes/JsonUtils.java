package com.jtd.web.adx.bes;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ser.StdSerializerProvider;
import org.codehaus.jackson.type.TypeReference;

import com.jtd.web.constants.BaiduEsConfig;

public class JsonUtils {

	private static final Log log = LogFactory.getLog(BaiduEsConfig.class);

	final static ObjectMapper objectMapper;

	static {
		StdSerializerProvider sp = new StdSerializerProvider();
		//sp.setNullValueSerializer(new NullNullSerializer());
		objectMapper = new ObjectMapper(null, sp, null);
		//objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
	}

	public static ObjectMapper getObjectMapper() {
		return objectMapper;
	}

	/**
	 * JSON串转换为Java泛型对象，可以是各种类型，此方法最为强大。用法看测试用例。
	 * @param <T>
	 * @param jsonString JSON字符串
	 * @param tr TypeReference,例如: new TypeReference< List<FamousUser> >(){}
	 * @return List对象列表
	 */
	@SuppressWarnings("unchecked")
	public static <T> T json2GenericObject(String jsonString, TypeReference<T> tr) {

		if (jsonString == null || "".equals(jsonString)) {
			return null;
		} else {
			try {
				return (T) objectMapper.readValue(jsonString, tr);
			} catch (Exception e) {
				log.warn("json error:" + e.getMessage());
			}
		}
		return null;
	}

	/**
	 * Java对象转Json字符串
	 */
	public static String toJson(Object object) {
		String jsonString = "";
		try {
			jsonString = objectMapper.writeValueAsString(object);
		} catch (Exception e) {
			log.warn("json error:" + e.getMessage());
		}
		return jsonString;

	}

	/**
	 * Json字符串转Java对象
	 */
	public static Object json2Object(String jsonString, Class<?> c) {

		if (jsonString == null || "".equals(jsonString)) {
			return "";
		} else {
			try {
				return objectMapper.readValue(jsonString, c);
			} catch (Exception e) {
				log.warn("json error:" + e.getMessage());
			}

		}
		return "";
	}

}

