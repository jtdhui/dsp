package com.jtd.effect.handler;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.handler.codec.http.HttpVersion;

import com.jtd.effect.Handler;
import com.jtd.effect.HttpHandler;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月12日
 * @项目名称 dsp-tracker
 * @描述 <p></p>
 */
@Handler(uri = "/crossdomain.xml")
public class CDXmlHandler  implements HttpHandler{
	
	private static final byte[] XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><cross-domain-policy><allow-access-from domain=\"*\"/><allow-http-request-headers-from domain=\"*\" headers=\"*\" /></cross-domain-policy>".getBytes();

	/* (non-Javadoc)
	 * @see com.doddata.net.HttpHandler#process(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.handler.codec.http.HttpRequest)
	 */
	@Override
	public void process(ChannelHandlerContext ctx, HttpRequest req) throws Exception {
		DefaultHttpResponse rsp = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
		rsp.headers().add("Server", "Counter");
		rsp.headers().add("Content-Length", XML.length);
		rsp.headers().add("Content-Type", "text/xml");
		rsp.headers().add("Connection", "close");
		ChannelBuffer buf = ChannelBuffers.dynamicBuffer();
		buf.writeBytes(XML);
		rsp.setContent(buf);
		ctx.getChannel().write(rsp).addListener(ChannelFutureListener.CLOSE);
	}
}
