package com.jtd.web.constants;
/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月24日
 * @项目名称 dsp-common-entity
 * @描述 投放目标
 */
public enum PutTarget {
	ARRIVALS_TRANSFORM(0,"到达转化"),
	ORDER_TRANSFORM(1,"订单转化"),
	ADDPURCHASE_TRANSFORM(2,"加购转化"),
	REGISTERED_TRANSFORM(3,"注册转化"),
	ADVISORY_TRANSFORM(4,"咨询转化"),
	LOGIN_TRANSFORM(5,"登录转化"),
	KEEP_TRANSFORM(6,"收藏转化"),
	SHARE_TRANSFORM(7,"分享转化"),
	REVIEW_TRANSFORM(8,"评论/发步转化");

	private final int code;
	private final String desc;
	public int getCode() {
		return code;
	}
	public String getDesc() {
		return desc;
	}

	private PutTarget(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}
	
	public static PutTarget fromCode(int code) {
		if (code < 0 || code > 8)
			return null;
		return new PutTarget[] { ARRIVALS_TRANSFORM, ORDER_TRANSFORM,ADDPURCHASE_TRANSFORM, REGISTERED_TRANSFORM,ADVISORY_TRANSFORM,LOGIN_TRANSFORM,KEEP_TRANSFORM,SHARE_TRANSFORM, REVIEW_TRANSFORM }[code];
	}
}
