package com.jtd.engine.message.v1;

import com.jtd.engine.message.Message.Version;
import com.jtd.engine.message.MessageCodec;
import com.jtd.engine.message.MessageUtil;
import com.jtd.engine.message.Message;
import com.jtd.engine.adserver.message.v1.MessageV1;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月8日
 * @项目名称 dsp-engine
 * @描述 <p></p>
 */
public class MessageV1Codec implements MessageCodec {

	// 标志包,共用一个对象
	private static final Message000 MSG000 = new Message000();
	private static final Message001 MSG001 = new Message001();

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.maxit.adserver.message.MessageCodec#decode(byte[])
	 */
	public Message decode(byte[] msg) {

		int type = MessageUtil.makeIntFromByte2(msg, 2);
		switch (type) {

		case 0: // 心跳请求消息

			return msg.length == 4 ? MSG000 : null;

		case 1: // 心跳响应消息

			return msg.length == 4 ? MSG001 : null;
		default:
			return null;
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.maxit.adserver.message.MessageCodec#encode(com.maxit.adserver.message.Message)
	 */
	public byte[] encode(Message msg) {

		MessageV1 msgV1 = (MessageV1) msg;

		switch (msgV1.type()) {

		case MSG000: // 心跳请求

			byte[] msg000 = new byte[4];

			// 协议版本
			MessageUtil.writeShortBigEndian((short) 1, msg000, 0);

			// 消息类型
			MessageUtil.writeShortBigEndian((short) 0, msg000, 2);
			return msg000;

		case MSG001: // 心跳响应

			byte[] msg001 = new byte[4];

			// 协议版本
			MessageUtil.writeShortBigEndian((short) 1, msg001, 0);

			// 消息类型
			MessageUtil.writeShortBigEndian((short) 1, msg001, 2);
			return msg001;
		default:
			return null;
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.maxit.adserver.message.MessageCodec#version()
	 */
	public Version version() {
		return Version.V1;
	}
}
