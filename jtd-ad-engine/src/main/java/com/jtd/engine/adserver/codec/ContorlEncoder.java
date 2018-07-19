package com.jtd.engine.adserver.codec;

import java.net.InetSocketAddress;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import com.jtd.engine.message.Message;
import com.jtd.engine.message.MessageCodecFactory;


/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月24日
 * @项目名称 dsp-engine
 * @描述
 */
public class ContorlEncoder extends ProtocolEncoderAdapter implements AddressMappedCodec {

	private static final Log log = LogFactory.getLog(ContorlEncoder.class);

	// 编码器服务的地址
	private List<InetSocketAddress> serviceAddresses;

	// 消息编解码器工厂
	private MessageCodecFactory messageCodecFactory;

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.apache.mina.filter.codec.ProtocolEncoder#encode(org.apache.mina.core.session.IoSession,
	 *      java.lang.Object,
	 *      org.apache.mina.filter.codec.ProtocolEncoderOutput)
	 */
	public void encode(IoSession session, Object message,
			ProtocolEncoderOutput out) throws Exception {

		Message msg = (Message) message;
		byte[] m = messageCodecFactory.getMessageCodec(msg).encode(msg);
		out.write(IoBuffer.allocate(m.length + 4).putInt(m.length).put(m).flip());
	}

	/**
	 * @param messageCodecFactory
	 *            the messageCodecFactory to set
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
