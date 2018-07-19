package com.jtd.engine.message.v1;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月8日
 * @项目名称 dsp-engine
 * @描述 <p></p>
 */
public class MessageCommons {

	// 状态类型
	/** 自动状态 */
	public static final int PUB_STATUS_TYPE_AUTO = 0;
	/** 手动状态 */
	public static final int PUB_STATUS_TYPE_MANUAL = 1;

	// 投放状态(自动状态)
	/** 待投放 */
	public static final int PUB_STATUS_A_WAIT = 0;
	/** 投放中 */
	public static final int PUB_STATUS_A_PUBBING = 1;
	/** 自动暂停 */
	public static final int PUB_STATUS_A_PAUSE = 2;
	/** 自动下线 */
	public static final int PUB_STATUS_A_OFFLINE = 3;

	// 投放状态(手动)
	/** 可投放 */
	public static final int PUB_STATUS_M_PUBBING = 1;
	/** 手动暂停 */
	public static final int PUB_STATUS_M_PAUSE = 2;
	/** 手动下线 */
	public static final int PUB_STATUS_M_OFFLINE = 3;
}
