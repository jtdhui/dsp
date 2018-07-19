package com.jtd.statistic.handler;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.http.HttpRequest;

import com.jtd.statistic.Handler;
import com.jtd.statistic.HttpHandler;
import com.jtd.statistic.util.HandlerUtil;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月11日
 * @项目名称 dsp-counter
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
