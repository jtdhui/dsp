package com.jtd.engine.adserver.codec;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月24日
 * @项目名称 dsp-engine
 * @描述 地址与编解码器映射
 */
public interface AddressMappedCodec {

	/**
	 * 获取编解码器对应的地址
	 * @return
	 * @return List<InetSocketAddress>
	 */
	public List<InetSocketAddress> getServiceAddresses();
}
