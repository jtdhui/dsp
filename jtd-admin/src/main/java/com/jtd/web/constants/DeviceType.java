package com.jtd.web.constants;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月24日
 * @项目名称 dsp-common-entity
 * @描述 
 */
public enum DeviceType {
	
	// code值不要调
	UNKNOW(0, "未知"), MOBILEPHONE(1, "手机"), TABLET(2, "平板电脑"),PC(3,"pc"), INTETV(4,"互联网电视");

	private DeviceType(int code, String desc) {
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

    public static DeviceType fromCode(int code) {
        for (DeviceType r : DeviceType.values()) {
            if(r.code == code){
                return r ;
            }
        }
        return null ;
    }
}
