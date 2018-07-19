package com.jtd.commons;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月22日
 * @项目名称 dsp-common
 * @描述 
 */
public class CommonRuntimeException extends RuntimeException{
	private static final long serialVersionUID = -6934400067425359134L;
	private int exceptionCode;
	public CommonRuntimeException() {
		super("发生错误");
	}

	public CommonRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
	
	public CommonRuntimeException(int exceptionCode,String message, Throwable cause){
		this(message, cause,true,true);
		this.exceptionCode=exceptionCode;
	}
	public CommonRuntimeException(String message, Throwable cause) {
		this(message, cause,true,true);
	}

	public CommonRuntimeException(String message) {
		super(message,null);
	}

	public CommonRuntimeException(Throwable cause) {
		this("发生错误",cause);
	}

	public int getExceptionCode() {
		return exceptionCode;
	}

	public void setExceptionCode(int exceptionCode) {
		this.exceptionCode = exceptionCode;
	}
	
}
