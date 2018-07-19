package com.jtd.effect;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.http.HttpRequest;
/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月12日
 * @项目名称 dsp-tracker
 * @描述 <p>自定义Handler接口</p>
 */
public interface HttpHandler {

	/**
	 * 处理http请求
	 * 
	 * @param ctx
	 * @param req
	 * @throws Exception
	 */
	public void process(ChannelHandlerContext ctx, HttpRequest req) throws Exception;
}
