package com.jtd.web.constants;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月24日
 * @项目名称 dsp-common-entity
 * @描述 活动-自动状态
 */
public enum CampaignAutoStatus {
	//自动结束，设置的任何一个总目标到达后，或是金额到达后结束。
	READY(0,"未开始"),ONLINE(1, "投放中"), PAUSE_NOTTIME(2, "自动下线-不在投放时段"), PAUSE_ACCOMPLISH_GOALS(3, "自动下线-到达当日目标"), FINISHED(4, "自动结束");
	private CampaignAutoStatus(int code, String desc) {
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
	
	public static CampaignAutoStatus fromCode(int code) {
		if (code < 0 || code > 4)
			return null;
		return new CampaignAutoStatus[] { READY, ONLINE, PAUSE_NOTTIME,
				PAUSE_ACCOMPLISH_GOALS, FINISHED }[code];
	}
}