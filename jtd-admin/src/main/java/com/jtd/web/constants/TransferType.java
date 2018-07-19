package com.jtd.web.constants;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月24日
 * @项目名称 dsp-common-entity
 * @描述 <p>转化类型</p>
 */
public enum TransferType {
	ARRIVE(0, "到达转化"), ORDER(1, "订单转化"), ADDCRAT(2, "加购物车转化"), INTENTIONREGISTER(3, "注册意向转化"), REGISTER(4, "注册转化"), SHARE(5, "分享转化"), COLLECT(6, "收藏转化"), CONSULT(7, "咨询转化"), LOGIN(8, "登陆转化"), COMMENT(
			9, "评论转化");
	private TransferType(int code, String desc) {
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
}
