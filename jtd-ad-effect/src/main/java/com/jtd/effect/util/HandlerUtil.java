package com.jtd.effect.util;

import com.jtd.effect.em.Constants;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.handler.codec.http.HttpVersion;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月12日
 * @项目名称 dsp-tracker
 * @描述 <p>Handler工具类</p>
 */
public class HandlerUtil {

	/**
	 * js
	 * @param ctx
	 */
	public static void js(ChannelHandlerContext ctx, String js, String cookie){
		DefaultHttpResponse rsp = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
		byte[] bs = null;
		try {
			bs = js.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
		}
		rsp.headers().add("Server", "Counter");
		rsp.headers().add("Pragma", "No-cache");
		rsp.headers().add("Cache-Control", "no-cache");
		rsp.headers().add("Expires", "Thu, 01 Jan 1970 00:00:00 GMT");
		rsp.headers().add("Content-Length", bs.length);
		rsp.headers().add("Content-Type", "application/x-javascript;charset=UTF-8");
		if (cookie != null) {
			rsp.headers().add("P3P", "CP='IDC DSP COR ADM DEVi TAIi PSA PSD IVAi IVDi CONi HIS OUR IND CNT'");
			rsp.headers().add("Set-Cookie", cookie);
		}
		rsp.headers().add("Connection", "close");
		ChannelBuffer buf = ChannelBuffers.dynamicBuffer();
		buf.writeBytes(bs);
		rsp.setContent(buf);
		ctx.getChannel().write(rsp).addListener(ChannelFutureListener.CLOSE);
	}
	
	/**
	 * 向客户端写入js代码
	 * js
	 * @param ctx
	 * @param cookies
	 */
	public static void js(ChannelHandlerContext ctx, String js, List<String> cookies){
		DefaultHttpResponse rsp = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
		byte[] bs = null;
		try {
			bs = js.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
		}
		rsp.headers().add("Server", "Counter");
		rsp.headers().add("Pragma", "No-cache");
		rsp.headers().add("Cache-Control", "no-cache");
		rsp.headers().add("Expires", "Thu, 01 Jan 1970 00:00:00 GMT");
		rsp.headers().add("Content-Length", bs.length);
		rsp.headers().add("Content-Type", "application/x-javascript;charset=UTF-8");
		if (cookies != null && cookies.size() > 0) {
			rsp.headers().add("P3P", "CP='IDC DSP COR ADM DEVi TAIi PSA PSD IVAi IVDi CONi HIS OUR IND CNT'");
			for (String ck : cookies) {
				rsp.headers().add("Set-Cookie", ck);
			}
		}
		rsp.headers().add("Connection", "close");
		ChannelBuffer buf = ChannelBuffers.dynamicBuffer();
		buf.writeBytes(bs);
		rsp.setContent(buf);
		ctx.getChannel().write(rsp).addListener(ChannelFutureListener.CLOSE);
	}

	/**
	 * 1x1图片
	 * @param ctx
	 */
	public static void img11(ChannelHandlerContext ctx){
		DefaultHttpResponse rsp = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
		rsp.headers().add("Server", "Counter");
		rsp.headers().add("Pragma", "No-cache");
		rsp.headers().add("Cache-Control", "no-cache");
		rsp.headers().add("Expires", "Thu, 01 Jan 1970 00:00:00 GMT");
		rsp.headers().add("Content-Length", "43");
		rsp.headers().add("Content-Type", "image/gif");
		rsp.headers().add("Connection", "close");
		ChannelBuffer buf = ChannelBuffers.dynamicBuffer();
		buf.writeBytes(Constants.IMG11);
		rsp.setContent(buf);
		ctx.getChannel().write(rsp).addListener(ChannelFutureListener.CLOSE);
	}

	/**
	 * 1x1图片带种cookie
	 * @param ctx
	 */
	public static void img11(ChannelHandlerContext ctx, String cookie) {
		DefaultHttpResponse rsp = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
		rsp.headers().add("Server", "Counter");
		rsp.headers().add("Pragma", "No-cache");
		rsp.headers().add("Cache-Control", "no-cache");
		rsp.headers().add("Expires", "Thu, 01 Jan 1970 00:00:00 GMT");
		rsp.headers().add("Content-Length", "43");
		rsp.headers().add("Content-Type", "image/gif");
		if (cookie != null) {
			rsp.headers().add("P3P", "CP='IDC DSP COR ADM DEVi TAIi PSA PSD IVAi IVDi CONi HIS OUR IND CNT'");
			rsp.headers().add("Set-Cookie", cookie);
		}
		rsp.headers().add("Connection", "close");
		ChannelBuffer buf = ChannelBuffers.dynamicBuffer();
		buf.writeBytes(Constants.IMG11);
		rsp.setContent(buf);
		ctx.getChannel().write(rsp).addListener(ChannelFutureListener.CLOSE);
	}

	/**
	 * 1x1图片带种cookie
	 * @param ctx
	 * @param cookies
	 */
	public static void img11(ChannelHandlerContext ctx, List<String> cookies) {
		DefaultHttpResponse rsp = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
		rsp.headers().add("Server", "Counter");
		rsp.headers().add("Pragma", "No-cache");
		rsp.headers().add("Cache-Control", "no-cache");
		rsp.headers().add("Expires", "Thu, 01 Jan 1970 00:00:00 GMT");
		rsp.headers().add("Content-Length", "43");
		rsp.headers().add("Content-Type", "image/gif");
		if (cookies != null && cookies.size() > 0) {
			rsp.headers().add("P3P", "CP='IDC DSP COR ADM DEVi TAIi PSA PSD IVAi IVDi CONi HIS OUR IND CNT'");
			for (String ck : cookies) {
				rsp.headers().add("Set-Cookie", ck);
			}
		}
		rsp.headers().add("Connection", "close");
		ChannelBuffer buf = ChannelBuffers.dynamicBuffer();
		buf.writeBytes(Constants.IMG11);
		rsp.setContent(buf);
		ctx.getChannel().write(rsp).addListener(ChannelFutureListener.CLOSE);
	}

	/**
	 * 跳转
	 * @param ctx
	 * @param location
	 */
	public static void redirect(ChannelHandlerContext ctx, String location) {
		DefaultHttpResponse rsp = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FOUND);
		rsp.headers().add("Server", "Counter");
		rsp.headers().add("Pragma", "No-cache");
		rsp.headers().add("Cache-Control", "no-cache");
		rsp.headers().add("Expires", "Thu, 01 Jan 1970 00:00:00 GMT");
		rsp.headers().add("Location", location);
		rsp.headers().add("Connection", "close");
		ctx.getChannel().write(rsp).addListener(ChannelFutureListener.CLOSE);
	}

	/**
	 * 跳转带种cookie
	 * @param ctx
	 * @param location
	 * @param cookie
	 */
	public static void redirect(ChannelHandlerContext ctx, String location, String cookie) {
		DefaultHttpResponse rsp = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FOUND);
		rsp.headers().add("Server", "Counter");
		rsp.headers().add("Pragma", "No-cache");
		rsp.headers().add("Cache-Control", "no-cache");
		rsp.headers().add("Expires", "Thu, 01 Jan 1970 00:00:00 GMT");
		rsp.headers().add("Location", location);
		if (cookie != null) {
			rsp.headers().add("P3P", "CP='IDC DSP COR ADM DEVi TAIi PSA PSD IVAi IVDi CONi HIS OUR IND CNT'");
			rsp.headers().add("Set-Cookie", cookie);
		}
		rsp.headers().add("Connection", "close");
		ctx.getChannel().write(rsp).addListener(ChannelFutureListener.CLOSE);
	}

	/**
	 * 跳转带种cookie
	 * @param ctx
	 * @param location
	 * @param cookies
	 */
	public static void redirect(ChannelHandlerContext ctx, String location, List<String> cookies) {
		DefaultHttpResponse rsp = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FOUND);
		rsp.headers().add("Server", "Counter");
		rsp.headers().add("Pragma", "No-cache");
		rsp.headers().add("Cache-Control", "no-cache");
		rsp.headers().add("Expires", "Thu, 01 Jan 1970 00:00:00 GMT");
		rsp.headers().add("Location", location);
		if (cookies != null && cookies.size() > 0) {
			rsp.headers().add("P3P", "CP='IDC DSP COR ADM DEVi TAIi PSA PSD IVAi IVDi CONi HIS OUR IND CNT'");
			for (String ck : cookies) {
				rsp.headers().add("Set-Cookie", ck);
			}
		}
		rsp.headers().add("Connection", "close");
		ctx.getChannel().write(rsp).addListener(ChannelFutureListener.CLOSE);
	}

	/**
	 * 空响应
	 * @param ctx
	 */
	public static void empty(ChannelHandlerContext ctx){
		DefaultHttpResponse rsp = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
		rsp.headers().add("Server", "Counter");
		rsp.headers().add("Content-Length", "0");
		rsp.headers().add("Connection", "close");
		ctx.getChannel().write(rsp).addListener(ChannelFutureListener.CLOSE);
	}
}
