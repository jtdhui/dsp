package com.jtd.web.constants;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月24日
 * @项目名称 dsp-common-entity
 * @描述 <p>交易类型</p>
 */
public enum TransType {
	RTB(0,"RTB"),PMP(1,"PMP");
	private TransType(int code, String desc) {
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
	
	public static TransType fromCode(int code) {
		if (code < 0 || code > 1)
			return null;
		return new TransType[] { RTB, PMP }[code];
	}
}
