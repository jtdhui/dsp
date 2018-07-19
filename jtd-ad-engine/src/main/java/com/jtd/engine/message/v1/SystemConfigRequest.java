package com.jtd.engine.message.v1;


import java.util.List;
import java.util.Map;

import com.jtd.engine.adserver.message.v1.MessageV1;


/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2017年3月20日
 * @项目名称 dsp-engine
 * @描述 <p></p>
 */
public class SystemConfigRequest extends MessageV1{
	
	
	private static final long serialVersionUID = -1705468674848871181L;
	
	//操作表标识
	/**
	 * =1 修改是否进行特殊人群匹配的操作
	 * =2 修改是否log日志输出级别
	 * =3 
	 */
	private String opt = "" ;
    
	//0表示不进行特殊人群匹配，1表示进行特殊人群匹配
	private String customMatcher="0";
	
	//
	private String logLevel="";
	
	//参数集合，从url中取出的参数集合
	Map<String,List<String>> params;
	
	
	public String getOpt() {
		return opt;
	}




	public void setOpt(String opt) {
		this.opt = opt;
	}




	public String getCustomMatcher() {
		return customMatcher;
	}




	public void setCustomMatcher(String customMatcher) {
		this.customMatcher = customMatcher;
	}




	public String getLogLevel() {
		return logLevel;
	}




	public void setLogLevel(String logLevel) {
		this.logLevel = logLevel;
	}


	


	public Map<String, List<String>> getParams() {
		return params;
	}




	public void setParams(Map<String, List<String>> params) {
		this.params = params;
		try{
			opt=params.get("opt").get(0);
		}catch(Exception ex){
			opt="-0";
		}
		
		try{
			customMatcher=params.get("custom_matcher").get(0);
		}catch(Exception ex){
			customMatcher="-1";
		}
		
		try{
			logLevel=params.get("log_level").get(0);
		}catch(Exception ex){
			logLevel="-1";
		}
	}




	/**
	 * (non-Javadoc)
	 * 
	 * @see com.maxit.adserver.message.v1.MessageV1#type()
	 */
	public Type type() {
		return Type.SystemConfigRequest;
	}

}
