package com.jtd.web.constants;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月24日
 * @项目名称 dsp-common-entity
 * @描述 运营商类型
 */
public enum OperatorType {
	
	// code值不要调
	UNKNOW(0, "未知"), CHINAMOBILE(1, "中国移动"), CHINAUNICOM(2, "中国联通"), CHINATELECOM(3, "中国电信");

	private OperatorType(int code, String desc) {
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
	
	public static OperatorType getByMccMncCode(int mccmnc) {
		switch (mccmnc) {
		case 46000:
			return CHINAMOBILE;// 中国移动TD
		case 46001:
			return CHINAUNICOM;// 中国联通
		case 46002:
			return CHINAMOBILE;// 中国移动GSM
		case 46003:
			return CHINATELECOM;// 中国电信CDMA
		default:
			return null;
		}
	}

    public static OperatorType fromCode(int code) {
        for (OperatorType r : OperatorType.values()) {
            if(r.code == code){
                return r ;
            }
        }
        return null ;
    }
}
