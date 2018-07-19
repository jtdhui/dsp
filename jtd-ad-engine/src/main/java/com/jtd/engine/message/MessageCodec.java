package com.jtd.engine.message;

import com.jtd.engine.message.Message.Version;
/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月24日
 * @项目名称 dsp-engine
 * @描述
 */
public interface MessageCodec {

	/**
	 * 解码消息
	 * 
	 * @param msg
	 * @return
	 */
	public Message decode(byte[] msg);

	/**
	 * 编码消息
	 * 
	 * @param msg
	 * @return
	 */
	public byte[] encode(Message msg);

	/**
	 * 支持的版本
	 * @return
	 */
	public Version version();
}
