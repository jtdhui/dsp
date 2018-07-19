package com.jtd.engine.adserver.message;

import com.jtd.engine.message.Message;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月24日
 * @项目名称 dsp-engine
 * @描述
 */
public interface MessageHandlerFactory {

	/**
	 * 获取消息处理器
	 * 
	 * @param message
	 * @return
	 */
	public MessageHandler getMessageHandler(Message message);
}
