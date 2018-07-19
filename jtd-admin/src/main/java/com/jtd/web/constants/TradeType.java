package com.jtd.web.constants;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月24日
 * @项目名称 dsp-common-entity
 * @描述 <p>流水类型</p>
 */
public enum TradeType {

	RECHARGE(0, "充值"),
	AD_CONSUME(1, "消费"),
	REFUND(2, "退款"),
	PROXY_RECHARGE(5, "代理充值"),
	PROXY_TRANSFER(6, "代理转帐"),
	CLAW_BACK(7, "被收回"),
	AMOUNT_BACK(8, "金额收回");

	private TradeType(int code, String desc) {
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
	
	public static TradeType fromCode(int code){
		for (TradeType tt : TradeType.values()) {
			if(tt.code == code){
				return tt ;
			}
		}
		return null ;
	}
}
