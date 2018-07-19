package com.jtd.statistic.util;

import java.net.InetSocketAddress;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月11日
 * @项目名称 dsp-counter
 * @描述 <p></p>
 */
@Component("inetSocketAddressConverter")
public class InetSocketAddressConverter implements Converter<String, InetSocketAddress>{

	/** (non-Javadoc)
	 * @see Converter#convert(Object)
	 */
	@Override
	public InetSocketAddress convert(String text) {
		if (text == null) throw new IllegalArgumentException("需要转换的地址为空");
		int i = text.indexOf(":");
		if (i == -1) throw new IllegalArgumentException("需要转换的地址格式不正确,请使用host:port格式");
		String host = text.substring(0, i);
		String ports = text.substring(i + 1);
		int port = 0;
		try {
			port = Integer.parseInt(ports);
		} catch (Exception e) {
			throw new IllegalArgumentException("端口配置错误: " + ports, e);
		}
		return new InetSocketAddress(host, port);
	}
}
