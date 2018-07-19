package com.jtd.web.constants;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置
 * @创建日期 2016年8月24日
 * @项目名称 dsp-common-entity
 * @描述 合作伙伴状态
 */
public enum PartnerStatus {

	START(0, "开启"), STOP(1, "停止");
	private PartnerStatus(int code, String desc) {
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

	public static PartnerStatus fromCode(int code) {
		for (PartnerStatus ps : PartnerStatus.values()) {
			if (ps.getCode() == code) {
				return ps;
			}
		}
		return null;
	}
}
