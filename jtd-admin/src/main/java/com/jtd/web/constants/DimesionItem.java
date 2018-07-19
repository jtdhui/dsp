package com.jtd.web.constants;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月24日
 * @项目名称 dsp-common-entity
 * @描述 推广维度-维度名称
 */
public enum DimesionItem {
	AREA("area","地域定向"),
	CUSTOMER("customer","人群定向"),
	MEDIA("media","媒体定向"),
	APP("app","应用定向"),
	DEVICE("device","设备定向");
	private DimesionItem(String name, String desc) {
		this.name = name;
		this.desc = desc;
	}

	private final String name;
	private final String desc;

	public String getName() {
		return name;
	}

	public String getDesc() {
		return desc;
	}
}