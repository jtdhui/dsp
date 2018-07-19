package com.jtd.web.constants;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月24日
 * @项目名称 dsp-common-entity
 * @描述 活动-手动状态
 */
public enum CampaignManulStatus {
	//编辑中，表示活动还没有编辑完成
	//暂停，是待提交，就是在活动中没有点开始按钮。
	//投放中，就是点完开始按钮之后。
	//暂停,手动点击暂停
	//结束，手动点击了那个终止
	//已删除，逻辑删除
	EDIT(0, "编辑中"),TOCOMMIT(1,"暂停"),ONLINE(2,"投放中"),PAUSE(3,"暂停"),OFFLINE(4,"结束"),DELETE(5,"已删除");
	private CampaignManulStatus(int code, String desc) {
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
	
	public static CampaignManulStatus fromCode(int code) {
		if (code < 0 || code > 5)
			return null;
		return new CampaignManulStatus[] { EDIT, TOCOMMIT, ONLINE,
				PAUSE, OFFLINE,DELETE}[code];
	}
}