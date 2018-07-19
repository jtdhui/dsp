package com.jtd.web.constants;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月24日
 * @项目名称 dsp-common-entity
 * @描述 活动类型
 */
public enum CampaignType {
	PC(0, "pc广告"), APP(1, "app广告"), WAP(2, "wap广告");
	private CampaignType(int code, String desc) {
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
        for (CampaignType c : CampaignType.values()) {  
            if (c.getCode() == code) {  
                return c.desc; 
            } 
        } 
        return null;  
    }
    
    public static CampaignType fromCode(int code) {
		if (code < 0 || code > 2)
			return null;
		return new CampaignType[] { PC, APP, WAP }[code];
	}
}
