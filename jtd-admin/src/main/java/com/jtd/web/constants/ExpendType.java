package com.jtd.web.constants;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月24日
 * @项目名称 dsp-common-entity
 * @描述 计费方式
 */
public enum ExpendType {

	CPM(0, "CPM"), CPC(1, "CPC")
	// , CPD(2, "CPD") 本期无CPD
	;
	private ExpendType(int code, String desc) {
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
	
	// 根据code返回名称  
    public static String getName(int code) {  
        for (ExpendType c : ExpendType.values()) {  
            if (c.getCode() == code) {  
                return c.desc;  
            }  
        }  
        return null;  
    }
    
    public static ExpendType fromCode(int code) {
		if (code < 0 || code > 1)
			return null;
		return new ExpendType[] { CPM, CPC }[code];
    }
}
