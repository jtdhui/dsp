package com.jtd.engine.message.v1;


import com.jtd.engine.adserver.message.v1.MessageV1;

import java.util.Map;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2017年6月7日
 * @项目名称 dsp-engine
 * @描述 <p>系统配置响应对象</p>
 */
public class SystemConfigResponse extends MessageV1{

	
	private static final long serialVersionUID = 8476874741858450157L;
	
	public String code;
	
	public String msg;
	
	public Map<String,Object> data;
	
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	public Type type() {
		return Type.SystemConfigResponse;
	}
}
