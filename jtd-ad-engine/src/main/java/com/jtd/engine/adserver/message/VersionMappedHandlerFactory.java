package com.jtd.engine.adserver.message;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
public class VersionMappedHandlerFactory implements MessageHandlerFactory {

	private static final Log log = LogFactory.getLog(VersionMappedHandlerFactory.class);

	// 消息处理映射表,key为版本值
	private Map<Version, MessageHandler> handlers = new HashMap<Version, MessageHandler>();

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.asme.adserver.message.MessageHandlerFactory#getMessageHandler(com.maxit.adserver.message.Message)
	 */
	public MessageHandler getMessageHandler(Message message) {
		return handlers.get(message.version());
	}

	/**
	 * @param handlers
	 *            the handlers to set
	 */
	public void setHandlers(MessageHandler[] handlers) {
		for(MessageHandler handler : handlers) {
			Version v = handler.version();
			this.handlers.put(v, handler);
			log.info(this.getClass().getSimpleName() + "添加Handler: [" + v + " : " + handler.getClass().getSimpleName() + "]");
		}
	}
}
