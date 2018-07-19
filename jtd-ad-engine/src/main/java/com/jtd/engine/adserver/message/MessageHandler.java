package com.jtd.engine.adserver.message;

import org.apache.mina.core.session.IoSession;

import com.jtd.engine.message.Message;
import com.jtd.engine.message.Message.Version;



/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月24日
 * @项目名称 dsp-engine
 * @描述
 */
public interface MessageHandler {

	/**
	 * 处理消息
	 * 
	 * @param session
	 * @param message
	 */
	public void handle(IoSession session, Message message);

	/**
	 * 支持的版本
	 * @return
	 */
	public Version version();
}
