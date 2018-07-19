package com.jtd.web.constants;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置
 * @创建日期 2016年8月24日
 * @项目名称 dsp-common-entity
 * @描述 活动-显示状态
 */
public enum CampaignStatus {
	EDIT(0, "编辑中"), TOCOMMIT(1, "编辑完成"), READY(2, "未开始"), ONLINE(3, "投放中"), PAUSED(4, "已暂停"),
	OFFLINE(5, "已下线"), FINISHED(6, "已结束"), DELETE(7, "已删除");
	private CampaignStatus(int code, String desc) {
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

	public static CampaignStatus getCampaignStatus(int code) {
		CampaignStatus[] array = CampaignStatus.values();
		for (int i = 0; i < array.length; i++) {
			CampaignStatus cs = array[i];
			if (cs.getCode() == code) {
				return cs;
			}
		}
		return null;
	}
}