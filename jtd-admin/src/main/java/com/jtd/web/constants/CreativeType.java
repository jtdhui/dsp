package com.jtd.web.constants;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月24日
 * @项目名称 dsp-common-entity
 * @描述 素材类型
 */
public enum CreativeType {

	IMG(0, "图片"), FLASH(1, "Flash"), FLV(2, "Flv"), MP4(3, "MP4");
	private CreativeType(int code, String typeName) {
		this.code = code;
		this.typeName = typeName;
	}

	private final int code;
	private final String typeName;

	public int getCode() {
		return code;
	}

	public String getTypeName() {
		return typeName;
	}

	public static CreativeType fromCode(int code) {
		if (code < 0 || code > 3)
			return null;
		return new CreativeType[] { IMG, FLASH,FLV,MP4 }[code];
	}
}
