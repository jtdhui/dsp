package com.jtd.engine.adserver.codec;

import java.net.InetSocketAddress;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import com.jtd.engine.message.v1.HttpResponse;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月24日
 * @项目名称 dsp-engine
 * @描述 http 编码器
 */
public class HttpEncoder extends ProtocolEncoderAdapter implements AddressMappedCodec {

	private static final Log log = LogFactory.getLog(HttpEncoder.class);

	//协议
	private static final String PROTOCOL = "HTTP/1.1";
	//行结束符
	private static final String END_LINE = "\r\n";

	// 编码器服务的地址
	private List<InetSocketAddress> serviceAddresses;

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.apache.mina.filter.codec.ProtocolEncoder#encode(org.apache.mina.core.session.IoSession,
	 *      java.lang.Object,
	 *      org.apache.mina.filter.codec.ProtocolEncoderOutput)
	 */
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {

		HttpResponse response = (HttpResponse) message;
		StringBuilder sb = new StringBuilder(PROTOCOL).append((char) 32).append(String.valueOf(response.getStatus())).append((char) 32).append(response.getStatusText()).append(END_LINE);
		for (String head : response.getHeaders()) sb.append(head).append(END_LINE);
		sb.append(END_LINE);
		byte[] header = sb.toString().getBytes("UTF-8");
		byte[] body = response.getContent();
		IoBuffer buf = IoBuffer.allocate(header.length + body.length);
		buf.put(header);
		buf.put(body);
		buf.flip();
		out.write(buf);
		out.flush();
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
