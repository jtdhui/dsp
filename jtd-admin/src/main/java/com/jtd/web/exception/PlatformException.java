package com.jtd.web.exception;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年10月8日
 * @项目名称 dsp-admin
 * @描述 <p></p>
 */
public class PlatformException extends RuntimeException {
	
	public static String USER_INFO_NULL_MESSAGE = "登录失效" ;
	public static String PARAMETER_ERROR_MESSAGE = "参数错误" ;
	public static String DATABASE_ERROR_MESSAGE = "数据库操作失败" ;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//异常信息
	private String message;
	private boolean loginError = false ;
	
	public PlatformException(String message){
		super(message);
		this.message = message;
		
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isLoginError() {
		return loginError;
	}

	public void setLoginError(boolean loginError) {
		this.loginError = loginError;
	}
	
	

}
