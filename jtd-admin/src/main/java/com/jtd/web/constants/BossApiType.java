package com.jtd.web.constants;

public enum BossApiType {
	
	ACCOUNT_CREATE(1, "开户"), ACCOUNT_RECHARGE(2, "充值"), ACCOUNT_UPGRADE(3, "升级") , 
	VALID_ACCOUNT_CREATE(4, "验证开户"), VALID_ACCOUNT_RECHARGE(5, "验证充值") , VALID_ACCOUNT_UPGRADE(6, "验证升级"), CALLBACK_BOSS(7,"回调boss");
	
	private BossApiType(int code, String desc) {
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

	public static BossApiType fromCode(int code) {
		for (BossApiType tt : BossApiType.values()) {
			if (tt.code == code) {
				return tt;
			}
		}
		return null;
	}
	
	public boolean isAccountUpgrade(){
		return this.code == ACCOUNT_UPGRADE.code ;
	}
}
