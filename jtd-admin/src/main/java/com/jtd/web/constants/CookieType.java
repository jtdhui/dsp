package com.jtd.web.constants;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月24日
 * @项目名称 dsp-common-entity
 * @描述
 */
public enum CookieType {

	POPULATION(0, "人群属性"), INSTRESTING(1, "兴趣"), RETARGET(2, "全站访客找回"), COOKIEPACK(3, "人群包"),ADVANCERETARGET(4,"高级访客找回");
	private CookieType(int code, String typeName) {
		this.code = code;
		this.typeName = typeName;
	}

	private final int code;
	private final String typeName;

	public int getCode() {
		return code;
	}

	public String getTypeName() {
		return typeName;
	}
}
