package com.jtd.engine.message;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月24日
 * @项目名称 dsp-engine
 * @描述
 */
public interface MessageCodecFactory {

	/**
	 * 根据字节数组获取消息编解码器
	 * @param msg
	 * @return
	 */
	public MessageCodec getMessageCodec(byte[] msg);

	/**
	 * 根据消息对象获取消息编解码器
	 * @param msg
	 * @return
	 */
	public MessageCodec getMessageCodec(Message msg);
}
