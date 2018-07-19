package com.jtd.engine.adserver.handler;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import com.jtd.engine.adserver.message.MessageHandlerFactory;
import com.jtd.engine.message.Message;
import com.jtd.engine.utils.SessionUtil;



/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月24日
 * @项目名称 dsp-engine
 * @描述 根据消息的版本动态派发处理器处理
 */
public class DynamicIOHandler extends IoHandlerAdapter {

	private static final Log log = LogFactory.getLog(DynamicIOHandler.class);

	// 消息处理器工厂
	private MessageHandlerFactory messageHandlerFactory;

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.apache.mina.core.service.IoHandlerAdapter#exceptionCaught(org.apache.mina.core.session.IoSession,
	 *      java.lang.Throwable)
	 */
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		if (cause instanceof IOException) {
			IOException e = (IOException) cause;
			String msg = e.getMessage();
			if (msg != null && msg.indexOf("Connection reset by peer") != -1){
				session.close(true);
				return;
			}
		}
		SessionUtil.blockSession(session, log, cause);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.apache.mina.core.service.IoHandlerAdapter#messageReceived(org.apache.mina.core.session.IoSession,
	 *      java.lang.Object)
	 */
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		Message msg = (Message) message;
		messageHandlerFactory.getMessageHandler(msg).handle(session, msg);
	}

	/**
	 * @param messageHandlerFactory the messageHandlerFactory to set
	 */
	public void setMessageHandlerFactory(MessageHandlerFactory messageHandlerFactory) {
		this.messageHandlerFactory = messageHandlerFactory;
	}
}
