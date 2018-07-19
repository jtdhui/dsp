package com.jtd.commons.mybatis.support;

public abstract class CustomerContextHolder {

	public final static String SESSION_FACTORY_BUSINESS= "business";
	public final static String SESSION_FACTORY_COUNT = "count";

	private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();

	public static void setContextType(String contextType) {
		contextHolder.set(contextType);
	}

	public static String getContextType() {
		return contextHolder.get();
	}

	public static void clearContextType() {
		contextHolder.remove();
	}
}
