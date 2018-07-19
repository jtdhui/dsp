package com.jtd.web.constants;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月24日
 * @项目名称 dsp-common-entity
 * @描述	预算控制方式
 */
public enum BudgetCtrlType {
	STANDARD(0, "标准"), FAST(1, "尽速");
	private BudgetCtrlType(int code, String desc) {
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

	public static BudgetCtrlType fromCode(int code) {
		if (code < 0 || code > 1)
			return null;
		return new BudgetCtrlType[] { STANDARD, FAST }[code];
	}
}
