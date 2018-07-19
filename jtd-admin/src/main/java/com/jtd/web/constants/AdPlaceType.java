package com.jtd.web.constants;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月24日
 * @项目名称 dsp-common-entity
 * @描述 广告为类型
 */
public enum AdPlaceType {
	FIX(0,"固定"),
	FLOW(1,"浮窗"),
	DOUBLE(2,"双边对联"),
	SINGLE(3,"单边对联"),
	EXTEND(4,"固定可扩展"),
	HOVER(5,"悬停"),
	FOLD(6,"折叠"),
	RP(7,"背投"),
	OPEN_SCREEN(8,"开屏"),
	TABLE_SCREEN(9,"插屏"),
	BANNER(10,"横幅");
	

	private AdPlaceType(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	private final int code;
	private final String desc;

	public int getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}
}
