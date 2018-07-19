package com.jtd.web.constants;

public enum RoleType {
	
	ADMIN(1,"超级管理员"),
	OPERATE(2,"运营"),
	MARKET(3,"市场"),
	SALE(4,"销售"),
	FINANCE(5,"财务"),
	PARTNER(6,"直客-可读可写"),
	PARTNER_PROXY(7,"代理"),
	OPERATE_MANAGER(8,"运营经理"),
	OPERATE_DIRECTOR(9,"运营总监"),
	MANAGER(10,"管理员"),
	PARTNER_READONLY(11,"直客-只读");

	private final int code;
	private final String desc;
	
	private RoleType(int code,String desc){
		this.code = code ;
		this.desc = desc ;
	}

	public int getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}
	
	public static RoleType fromCode(int code) {
		for (RoleType r : RoleType.values()) {
			if(r.code == code){
				return r ;
			}
		}
		return null ;
	}
}
