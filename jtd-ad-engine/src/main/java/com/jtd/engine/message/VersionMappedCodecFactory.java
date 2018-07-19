package com.jtd.engine.message;

import java.util.HashMap;
import java.util.Map;

import com.jtd.engine.message.Message.Version;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月8日
 * @项目名称 dsp-engine
 * @描述 <p></p>
 */
public class VersionMappedCodecFactory implements MessageCodecFactory {

	// 消息编码解码器映射表,key为版本值
	private Map<Version, MessageCodec> codecs = new HashMap<Version, MessageCodec>();

	// 消息编码解码器映射表,key为版本值的value
	private Map<Integer, MessageCodec> codecs2 = new HashMap<Integer, MessageCodec>();

	/** 
	 * (non-Javadoc)
	 *
	 * @see com.maxit.adserver.message.MessageCodecFactory#getMessageCodec(byte[])
	 */
	public MessageCodec getMessageCodec(byte[] msg) {
		if (msg.length >= 2) {
			int version = MessageUtil.makeIntFromByte2(msg);
			return codecs2.get(version);
		}
		return null;
	}

	/** 
	 * (non-Javadoc)
	 *
	 * @see com.maxit.adserver.message.MessageCodecFactory#getMessageCodec(com.maxit.adserver.message.Message)
	 */
	public MessageCodec getMessageCodec(Message msg) {
		return codecs.get(msg.version());
	}

	/**
	 * @param codecs
	 *            the codecs to set
	 */
	public void setCodecs(MessageCodec[] codecs) {
		for (MessageCodec codec : codecs) {
			Version v = codec.version();
			this.codecs.put(v, codec);
			this.codecs2.put(v.value(), codec);
		}
	}
}
