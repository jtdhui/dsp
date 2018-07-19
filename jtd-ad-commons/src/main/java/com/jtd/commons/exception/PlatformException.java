package com.jtd.commons.exception;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置
 * @创建日期 2016年9月28日
 * @项目名称 springmvc-mybatis-test3
 * @描述
 *     <p>
 *     系统自定义的异常类型，实际开发中可能要定义多种异常类型
 *     </p>
 *   * 业务异常基类，所有业务异常都必须继承于此异常
 *         定义异常时，需要先确定异常所属模块。例如：添加用户报错 可以定义为 [10020001] 前四位数为系统模块编号，后4位为错误代码 ,唯一 <br>
 *         用户异常 1002 <br>
 *         广告主异常 1004 <br>
 *         广告活动异常 1005 <br>
 *         广告素材 异常 1008 <br>
 *         广告异常 1009 <br>
 *         广告策略异常 1010 <br>
 *		   .........
 */
public class PlatformException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * 数据库操作,insert返回0
	 */
	public static final PlatformException DB_INSERT_RESULT_0 = new PlatformException(90040001, "数据库操作,insert返回0");

	/**
	 * 数据库操作,update返回0
	 */
	public static final PlatformException DB_UPDATE_RESULT_0 = new PlatformException(90040002, "数据库操作,update返回0");

	/**
	 * 数据库操作,selectOne返回null
	 */
	public static final PlatformException DB_SELECTONE_IS_NULL = new PlatformException(90040003, "数据库操作,selectOne返回null");

	/**
	 * 数据库操作,list返回null
	 */
	public static final PlatformException DB_LIST_IS_NULL = new PlatformException(90040004, "数据库操作,list返回null");

	/**
	 * Token 验证不通过
	 */
	public static final PlatformException TOKEN_IS_ILLICIT = new PlatformException(90040005, "Token 验证非法");
	/**
	 * 会话超时 获取session时，如果是空，throws 下面这个异常 拦截器会拦截爆会话超时页面
	 */
	public static final PlatformException SESSION_IS_OUT_TIME = new PlatformException(90040006, "会话超时");

	/**
	 * 获取序列出错
	 */
	public static final PlatformException DB_GET_SEQ_NEXT_VALUE_ERROR = new PlatformException(90040007, "获取序列出错");

	// 异常信息
	private String message;

	// 具体异常码
	protected int code;

	public PlatformException(int code, String msgFormat, Object... args) {
		super(String.format(msgFormat, args));
		this.code = code;
		this.message = String.format(msgFormat, args);
	}

	public PlatformException(String message) {
		super(message);
		this.message = message;

	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
