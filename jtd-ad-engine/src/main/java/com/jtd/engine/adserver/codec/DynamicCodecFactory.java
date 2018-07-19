package com.jtd.engine.adserver.codec;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月24日
 * @项目名称 dsp-engine
 * @描述 根据session的本地监听端口决定使用哪对Codec
 */
public class DynamicCodecFactory implements ProtocolCodecFactory {

	private static final Log log = LogFactory.getLog(DynamicCodecFactory.class);

	// 解码器map
	private Map<InetSocketAddress, ProtocolDecoder> decoders = new HashMap<InetSocketAddress, ProtocolDecoder>();

	// 编码器map
	private Map<InetSocketAddress, ProtocolEncoder> encoders = new HashMap<InetSocketAddress, ProtocolEncoder>();

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.apache.mina.filter.codec.ProtocolCodecFactory#getDecoder(org.apache.mina.core.session.IoSession)
	 */
	public ProtocolDecoder getDecoder(IoSession session) throws Exception {
		return decoders.get(session.getLocalAddress());
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.apache.mina.filter.codec.ProtocolCodecFactory#getEncoder(org.apache.mina.core.session.IoSession)
	 */
	public ProtocolEncoder getEncoder(IoSession session) throws Exception {
		return encoders.get(session.getLocalAddress());
	}

	/**
	 * @param decoders
	 *            the decoders to set
	 */
	public void setCodecs(AddressMappedCodec[] codecs) {
		for (AddressMappedCodec codec : codecs) {
			if (codec instanceof ProtocolDecoder) {
				List<InetSocketAddress> address = codec.getServiceAddresses();
				for(InetSocketAddress addr : address) {
					decoders.put(addr, (ProtocolDecoder) codec);
					log.info(this.getClass().getSimpleName() + "添加解码器: [" + addr + " : " + codec.getClass().getSimpleName() + "]");
				}
			}
			if (codec instanceof ProtocolEncoder) {
				List<InetSocketAddress> address = codec.getServiceAddresses();
				for(InetSocketAddress addr : address) {
					encoders.put(addr, (ProtocolEncoder) codec);
					log.info(this.getClass().getSimpleName() + "添加编码器: [" + addr + " : " + codec.getClass().getSimpleName() + "]");
				}
			}
		}
	}
}