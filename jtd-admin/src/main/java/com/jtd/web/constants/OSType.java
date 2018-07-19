package com.jtd.web.constants;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月24日
 * @项目名称 dsp-common-entity
 * @描述 操作系统类型
 */
public enum OSType {
	
	// code值不要调
	UNKNOW("unknow", "未知"), ANDROID("android", "Android"), IOS("ios", "iOS"), WINDOWSPHONE("windowsphone", "WindowsPhone");

	private OSType(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	private final String code;
	private final String desc;

	public String getCode() {
		return code;
	}
	public String getDesc() {
		return desc;
	}
}
