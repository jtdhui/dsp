package com.jtd.web.constants;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月24日
 * @项目名称 dsp-common-entity
 * @描述 网络类型形式
 */
public enum NetWorkType {

	// 注意code值已经跟引擎端对应,不要调
	UNKNOW(0, "未知"), WIFI(1, "WIFI"), _2G(2, "2G"), _3G(3, "3G"), _4G(4, "4G");

	private NetWorkType(int code, String desc) {
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

    public static NetWorkType fromCode(int code) {
        for (NetWorkType r : NetWorkType.values()) {
            if(r.code == code){
                return r ;
            }
        }
        return null ;
    }
}
