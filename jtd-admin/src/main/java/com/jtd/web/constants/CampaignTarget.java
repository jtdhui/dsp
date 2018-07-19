package com.jtd.web.constants;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月24日
 * @项目名称 dsp-common-entity
 * @描述 投放目标
 */
public enum CampaignTarget {
	BRAND_VIEW(0, "品牌展现"), CLICK_LANDING(1, "点击到达"), REG_RATE(2, "注册转化"), ORDER_RATE(
			3, "订单转化"), ADV_RATE(4, "咨询转化"), OTHER(5, "其他");
	private CampaignTarget(int code, String desc) {
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
