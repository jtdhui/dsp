package com.jtd.web.constants;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月24日
 * @项目名称 dsp-common-entity
 * @描述 用户个性化设置-设置项名称
 */
public enum FavoriteItem {
	CAMPGROUP("camp_group","推广组"),CAMPAIGN("camp_list","推广活动");
	private FavoriteItem(String name, String desc) {
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