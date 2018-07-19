package com.jtd.engine.message;

import java.io.Serializable;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月24日
 * @项目名称 dsp-engine
 * @描述 消息抽象类
 */
public abstract class Message implements Serializable {

	private static final long serialVersionUID = -6114582309243612248L;

	/**
	 * @作者 Amos Xu
	 * @版本 V1.0
	 * @配置 
	 * @创建日期 2016年8月24日
	 * @项目名称 dsp-engine
	 * @描述
	 */
	public enum Version {
		V1(1);
		// 协议版本值
		private final int version;
		private Version(int version) {
			this.version = version;
		}
		/**
		 * 返回协议版本值
		 * 
		 * @return
		 */
		public int value() {
			return version;
		}
	}
	/**
	 * 返回协议版本
	 * 
	 * @return
	 */
	public abstract Version version();
}
