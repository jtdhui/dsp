package com.jtd.web.vo;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月25日
 * @项目名称 dsp-admin
 * @描述 <p></p>
 */
public class CampaignResult {
	private Integer result; 
	private String msg;
	
	public CampaignResult(){}
	public CampaignResult(Integer result,String msg){
		this.result=result;
		this.msg=msg;
	}
	public Integer getResult() {
		return result;
	}
	public void setResult(Integer result) {
		this.result = result;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
