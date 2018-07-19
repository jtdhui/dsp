package com.jtd.effect.handler;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.http.HttpRequest;

import com.jtd.effect.Handler;
import com.jtd.effect.HttpHandler;
import com.jtd.effect.util.HandlerUtil;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月12日
 * @项目名称 dsp-tracker
 * @描述 <p></p>
 */
@Handler(uri = "/favicon.ico")
public class FavIcoHandler  implements HttpHandler{

	/* (non-Javadoc)
	 * @see com.doddata.net.HttpHandler#process(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.handler.codec.http.HttpRequest)
	 */
	@Override
	public void process(ChannelHandlerContext ctx, HttpRequest req) throws Exception {
		HandlerUtil.empty(ctx);
	}
}
