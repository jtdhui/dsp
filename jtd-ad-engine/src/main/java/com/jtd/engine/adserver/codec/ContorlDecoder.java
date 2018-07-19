package com.jtd.engine.adserver.codec;

import java.net.InetSocketAddress;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderAdapter;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import com.jtd.engine.message.Message;
import com.jtd.engine.message.MessageCodec;
import com.jtd.engine.message.MessageCodecFactory;
import com.jtd.engine.utils.SessionUtil;

/**
 * 
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月24日
 * @项目名称 dsp-engine
 * @描述
 */
public class ContorlDecoder extends ProtocolDecoderAdapter implements AddressMappedCodec {

	private static final Log log = LogFactory.getLog(ContorlDecoder.class);
	private static final AttributeKey DECODE_BUFFER_KEY = new AttributeKey(ContorlDecoder.class, "ContorlDecodeBufferKey");

	// 解码器服务的地址
	private List<InetSocketAddress> serviceAddresses;

	// 消息编解码器工厂
	private MessageCodecFactory messageCodecFactory;

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.apache.mina.filter.codec.CumulativeProtocolDecoder#decode(org.apache.mina.core.session.IoSession,
	 *      org.apache.mina.core.buffer.IoBuffer,
	 *      org.apache.mina.filter.codec.ProtocolDecoderOutput)
	 */
	public void decode(IoSession session, IoBuffer in, ProtocolDecoderOutput out)
			throws Exception {

		if (in.hasRemaining()) {
			Context context = getContext(session);
			context.read(in);

			// 拆到拆不出来消息为止
			for (; context.splitedMsg();) {

				MessageCodec condec = messageCodecFactory.getMessageCodec(context.msg);

				if (condec == null) {
					SessionUtil.blockSession(session, log, "收到不能识别版本的消息");
					return;
				}

				Message msg = condec.decode(context.msg);
				if (msg == null) {
					SessionUtil.blockSession(session, log, "收到的消息解码失败");
					return;
				}

				out.write(msg);
			}
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.apache.mina.filter.codec.ProtocolDecoderAdapter#dispose(org.apache.mina.core.session.IoSession)
	 */
	public void dispose(IoSession session) throws Exception {
		removeContext(session);
	}

	/**
	 * 获取session中的context
	 * 
	 * @param session
	 * @return
	 */
	private Context getContext(IoSession session) {
		Context context = (Context) session.getAttribute(DECODE_BUFFER_KEY);
		if (context == null) {
			context = new Context();
			session.setAttribute(DECODE_BUFFER_KEY, context);
		}
		return context;
	}

	/**
	 * 清除session中的context
	 * 
	 * @param session
	 */
	private void removeContext(IoSession session) {
		Context context = (Context) session.getAttribute(DECODE_BUFFER_KEY);
		if (context != null) {
			context.msgBuf.clear();
		}
		session.removeAttribute(DECODE_BUFFER_KEY);
	}

	/**
	 * @author ASME
	 * 
	 *         2010-6-11
	 */
	private class Context {

		// 消息长度
		private int msgLen;

		// 读缓存
		private IoBuffer msgBuf;

		// 解出来的消息
		private byte[] msg;

		private Context() {
			msgLen = 0;
			msgBuf = IoBuffer.allocate(1024).setAutoExpand(true);
		}

		/**
		 * 读数据
		 * 
		 * @param in
		 */
		private void read(IoBuffer in) {
			msgBuf = msgBuf.put(in);
		}

		/**
		 * 是否能从字节流中拆出完整消息来
		 * 
		 * @return
		 */
		private boolean splitedMsg() {

			if (msgLen == 0 && msgBuf.position() < 4) {

				// 还没有读到消息长度
				return false;
			}

			if (msgLen == 0) {

				// 读消息长度
				msgBuf.flip();
				msgLen = msgBuf.getInt();
			} else {

				// 消息长度已经读出来了,越过4个字节的消息长度
				msgBuf.position(4);
			}

			if (msgLen <= msgBuf.remaining()) {

				// 有完整的消息了
				msg = new byte[msgLen];
				msgBuf.get(msg, 0, msgLen);
				msgLen = 0;

				// 把余下的部分移到buffer头部
				msgBuf.compact();

				return true;
			} else {

				// 消息还不完整
				msgBuf.position(msgBuf.limit());
			}

			return false;
		}
	}

	/**
	 * @param messageCodecFactory the messageCodecFactory to set
	 */
	public void setMessageCodecFactory(MessageCodecFactory messageCodecFactory) {
		this.messageCodecFactory = messageCodecFactory;
	}

	/**
	 * @return the serviceAddress
	 */
	public List<InetSocketAddress> getServiceAddresses() {
		return serviceAddresses;
	}

	/**
	 * @param serviceAddress the serviceAddress to set
	 */
	public void setServiceAddresses(List<InetSocketAddress> serviceAddresses) {
		this.serviceAddresses = serviceAddresses;
		for(InetSocketAddress addr : serviceAddresses)
		log.info(this.getClass().getSimpleName() + "服务地址: " + addr);
	}
}
