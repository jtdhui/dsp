package com.jtd.statistic.util;

import java.util.List;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.handler.codec.http.HttpVersion;

import com.jtd.statistic.em.Constants;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月11日
 * @项目名称 dsp-counter
 * @描述 <p></p>
 */
public class HandlerUtil {
	
	/**
	 * 1x1图片
	 * @param ctx
	 * @param cookies
	 */
	public static void img11(ChannelHandlerContext ctx, boolean close){
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
		ChannelFuture cf = ctx.getChannel().write(rsp);
		if(close) cf.addListener(ChannelFutureListener.CLOSE);
	}

	/**
	 * 1x1图片
	 * @param ctx
	 * @param cookies
	 */
	public static void img11(ChannelHandlerContext ctx){
		img11(ctx, true);
	}
	
	/**
	 * 1x1图片带种cookie
	 * @param ctx
	 * @param cookies
	 */
	public static void img11(ChannelHandlerContext ctx, String cookie, boolean close) {
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
		ChannelFuture cf = ctx.getChannel().write(rsp);
		if(close) cf.addListener(ChannelFutureListener.CLOSE);
	}

	/**
	 * 1x1图片带种cookie
	 * @param ctx
	 * @param cookies
	 */
	public static void img11(ChannelHandlerContext ctx, String cookie) {
		img11(ctx, cookie, true);
	}

	/**
	 * 1x1图片带种cookie
	 * @param ctx					上下文环境
	 * @param cookies				需要写入浏览器的cookie数据
	 * @param close					是否关闭连接
	 */
	public static void img11(ChannelHandlerContext ctx, List<String> cookies, boolean close) {
		DefaultHttpResponse rsp = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
		rsp.headers().add("Server", "Counter");//响应的服务名称
		rsp.headers().add("Pragma", "No-cache");//本地不保存响应参数
		rsp.headers().add("Cache-Control", "no-cache");//本地不存储响应数据
		rsp.headers().add("Expires", "Thu, 01 Jan 1970 00:00:00 GMT");//过期时间
		rsp.headers().add("Content-Length", "43");
		rsp.headers().add("Content-Type", "image/gif");
		if (cookies != null && cookies.size() > 0) {
			//p3p,意思是网站向浏览器声明自己的隐私政策，比如网站是否收集访问者的个人信息，设置cookie的用途等等，
			//浏览器会依据设置，决定在第三方请求的条件下是否接受网站的cookie
			rsp.headers().add("P3P", "CP='IDC DSP COR ADM DEVi TAIi PSA PSD IVAi IVDi CONi HIS OUR IND CNT'");
			for (String ck : cookies) {
				rsp.headers().add("Set-Cookie", ck);
			}
		}
		rsp.headers().add("Connection", "close");
		ChannelBuffer buf = ChannelBuffers.dynamicBuffer();
		buf.writeBytes(Constants.IMG11);
		rsp.setContent(buf);
		ChannelFuture cf = ctx.getChannel().write(rsp);
		if(close) cf.addListener(ChannelFutureListener.CLOSE);
	}

	/**
	 * 1x1图片带种cookie
	 * @param ctx					上下文环境
	 * @param cookies				需要写入浏览器的cookie数据
	 */
	public static void img11(ChannelHandlerContext ctx, List<String> cookies) {
		img11(ctx, cookies, true);
	}

	/**
	 * 跳转
	 * @param ctx
	 * @param location
	 */
	public static void redirect(ChannelHandlerContext ctx, String location, boolean close) {
		DefaultHttpResponse rsp = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FOUND);
		rsp.headers().add("Server", "Counter");
		rsp.headers().add("Pragma", "No-cache");
		rsp.headers().add("Cache-Control", "no-cache");
		rsp.headers().add("Expires", "Thu, 01 Jan 1970 00:00:00 GMT");
		rsp.headers().add("Location", location);
		rsp.headers().add("Connection", "close");
		ChannelFuture cf = ctx.getChannel().write(rsp);
		if(close) cf.addListener(ChannelFutureListener.CLOSE);
	}

	/**
	 * 跳转
	 * @param ctx
	 * @param location
	 */
	public static void redirect(ChannelHandlerContext ctx, String location) {
		redirect(ctx, location, true);
	}

	/**
	 * 跳转带种cookie
	 * @param ctx
	 * @param location
	 * @param cookie
	 */
	public static void redirect(ChannelHandlerContext ctx, String location, String cookie, boolean close) {
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
		ChannelFuture cf = ctx.getChannel().write(rsp);
		if(close) cf.addListener(ChannelFutureListener.CLOSE);
	}

	/**
	 * 跳转带种cookie
	 * @param ctx
	 * @param location
	 * @param cookie
	 */
	public static void redirect(ChannelHandlerContext ctx, String location, String cookie) {
		redirect(ctx, location, cookie, true);
	}

	/**
	 * 跳转带种cookie
	 * @param ctx					上下文环境
	 * @param location				跳转的地址
	 * @param cookies				需要写入浏览器的cookie数据
	 * @param close					是否关闭连接
	 */
	public static void redirect(ChannelHandlerContext ctx, String location, List<String> cookies, boolean close) {
		//构建一个http响应对象，响应状态为302(HttpResponseStatus.FOUND值就是302)，就是重定向的相应
		DefaultHttpResponse rsp = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FOUND);
		//设置响应头信息
		rsp.headers().add("Server", "Counter");	//响应的服务名称
		rsp.headers().add("Pragma", "No-cache");//本地不保存响应参数
		rsp.headers().add("Cache-Control", "no-cache");//本地不存储响应数据
		rsp.headers().add("Expires", "Thu, 01 Jan 1970 00:00:00 GMT");//过期时间
		rsp.headers().add("Location", location);//跳转地址
		if (cookies != null && cookies.size() > 0) {
			//p3p,意思是网站向浏览器声明自己的隐私政策，比如网站是否收集访问者的个人信息，设置cookie的用途等等，
			//浏览器会依据设置，决定在第三方请求的条件下是否接受网站的cookie
			rsp.headers().add("P3P", "CP='IDC DSP COR ADM DEVi TAIi PSA PSD IVAi IVDi CONi HIS OUR IND CNT'");
			for (String ck : cookies) {
				//将cookie设置添加在相应头中，并写到浏览器中
				rsp.headers().add("Set-Cookie", ck);
			}
		}
		//关闭这次连接
		rsp.headers().add("Connection", "close");
		//向客户端，写入请求头
		ChannelFuture cf = ctx.getChannel().write(rsp);
		if(close) cf.addListener(ChannelFutureListener.CLOSE);
	}

	/**
	 * 跳转带种cookie
	 * @param ctx					上下文环境
	 * @param location				跳转的地址
	 * @param cookies				需要写入浏览器的cookie数据
	 */
	public static void redirect(ChannelHandlerContext ctx, String location, List<String> cookies) {
		redirect(ctx, location, cookies, true);
	}

	/**
	 * 空响应
	 * @param ctx
	 */
	public static void empty(ChannelHandlerContext ctx, boolean close){
		DefaultHttpResponse rsp = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
		rsp.headers().add("Server", "Counter");
		rsp.headers().add("Content-Length", "0");
		rsp.headers().add("Connection", "close");
		ChannelFuture cf = ctx.getChannel().write(rsp);
		if(close) cf.addListener(ChannelFutureListener.CLOSE);
	}

	/**
	 * 空响应
	 * @param ctx
	 */
	public static void empty(ChannelHandlerContext ctx){
		empty(ctx, true);
	}
}
