package com.jtd.engine.ad.em;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月8日
 * @项目名称 dsp-engine
 * @描述 <p></p>
 */
public enum MatchType {
	//城市
	CITY(1, "ct"),
	//性别
	GENDER(2, "gd"), 
	//年龄
	AGE(3, "ag"),
	//婚否
	MARRIAGE(4, "ma"), 
	//教育
	PARENTING(5, "pr"), 
	//工作类型
	JOBTYPE(6, "jt"), 
	//工作状态
	JOBSTATUS(7, "js"), 
	//教育
	EDUCATION(8, "ed"), 
	//
	FORTEST(9, "ft"), 
	HOME(10, "hm"), 
	//兴趣
	INTEREST(11, "in"), 
	//cookie包
	COOKIEPACKET(12, "pk"), 
	//目标
	RETARGET(13, "rt"),
	IPSEG(14, "ip"),
	//页面地址
	PAGEURL(15, "url"),
	//网站类型
	WEBCATG(16, "wc"),
	//app类型
	APPCATG(17, "act"),
	//app packageName
	APPPKNAME(18, "apn"),
	//设备类型
	DEVICETYPE(19, "dev"),
	//网络
	NETWORK(20, "nt"),
	//操作类型
	OPERATOR(21, "op"),
	//操作系统
	OS(22, "os"),
	//品牌
	BRAND(23, "br"),
	//首屏
	FIRSTSCREEN(24, "fs");

	private final int id;
	private final String name;

	private MatchType(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public int id() {
		return id;
	}

	public String matchName() {
		return name;
	}
}
