package com.jtd.statistic.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpHeaders.Names;
import org.jboss.netty.handler.codec.http.HttpRequest;

import com.alibaba.fastjson.JSON;
import com.jtd.statistic.Handler;
import com.jtd.statistic.HttpHandler;
import com.jtd.statistic.po.Click;
import com.jtd.statistic.po.Param;
import com.jtd.statistic.service.CountService;
import com.jtd.statistic.util.CookieUtil;
import com.jtd.statistic.util.HandlerUtil;
import com.jtd.statistic.util.LikeBase64;
import com.jtd.statistic.util.RequestUtil;
import com.jtd.statistic.util.SystemTime;
/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月11日
 * @项目名称 dsp-counter
 * @描述 <p></p>
 */ 
@Handler(uri = "/nc")
public class ClickHandler implements HttpHandler{

	private static final Log log = LogFactory.getLog(ClickHandler.class);

	@Resource
	private CountService countService;

	@Resource
	private SystemTime systemTime;

	@Resource
	private CookieUtil cookieUtil;
	
	private final Logger logMyDebug = LogManager.getLogger("myDebugLog");

	/* (non-Javadoc)
	 * @see com.doddata.net.HttpHandler#process(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.handler.codec.http.HttpRequest)
	 */
	@Override
	public void process(ChannelHandlerContext ctx, HttpRequest req) throws Exception {

		log.info("点击请求>>>>>>>>>>>>>>>>>>>"+req);
		
		Map<String, List<String>> params = RequestUtil.parseQuery(req);
		if (params == null || params.size() == 0) {
			log.error("请求无参数");
			HandlerUtil.img11(ctx);
			return;
		}

		// 参数
		String q = RequestUtil.getSingleParam(params, "q");

		log.info("参数 q>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+q);
		
		// 跳转地址
		String r = RequestUtil.getSingleParam(params, "r");

		log.info("参数 r>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+r);

		if(q != null) q = LikeBase64.decode(q);
		if(r != null) r = LikeBase64.decode(r);
		if (q == null) {
			//HandlerUtil.img11(ctx);
			ret(ctx, r);
			log.error("没解析出参数");
			return;
		}
		
		//取出参数
		Param param = null;
		try {
			param = JSON.parseObject(q, Param.class);
		} catch (Exception e) {
			log.error("解析参数发生错误:" + q, e);
		}
		if(param == null) {
			//HandlerUtil.img11(ctx);
			ret(ctx, r);
			log.error("解析出的参数为空");
			return;
		}
//		log.info("参数:"+q);
//		log.info("跳转地址:"+r);
		//实例化一个click对象
		Click click = new Click();
		click.setParam(param);
		click.setTime(systemTime.getTime());

		Map<String, String> cookies = RequestUtil.parseCookie(req);
		//是否有cookieid
		boolean hasCookieId = false;
		List<String> setCookie = null;
		//如果从用户本地中取出cookie
		if (cookies != null) {
			//获取cookieid
			String cookeid = cookies.get(CookieUtil.ID);
			if (cookeid != null) {
				//如果取到了cookieid，则将cookieid设置到click对象中。
				click.setCookieid(cookeid);
				//将是否有cookieid设置为true
				hasCookieId = true;
			} else {
				//如果没有从cookies中去到cookieid，则获取一个新的cookie
				String[] cid = cookieUtil.nextCookieId();
				//获取cookieid，并设置到click对象中
				click.setCookieid(cid[0]);
				//将httpcookie字符串和httpscookie字符串，添加到setCookie List中。
				setCookie = new ArrayList<String>();
				//http cookie 字符串
				setCookie.add(cid[1]);
				//https cookie 字符串
				setCookie.add(cid[2]);
			}
		} else {
			//如果从请求中本身就没有拿到任何cookie数据，则重新创建cookieid，httpcookie字符串，httpscookie字符串
			String[] cid = cookieUtil.nextCookieId();
			click.setCookieid(cid[0]);
			setCookie = new ArrayList<String>();
			setCookie.add(cid[1]);
			setCookie.add(cid[2]);
		}
		//拿到http的请求头
		HttpHeaders headers = req.headers();
		//获取这次用户点击的ip地址，并设置到click对象中
		click.setUserip(RequestUtil.getRequestIP(ctx, req));
		//获取这次用户点击的 User-Agent 并设置到click中
		click.setUserAgent(headers.get(Names.USER_AGENT));
		//获取跳转地址(这个跳转地址是引擎中拼装的)，并设置到click中
		click.setRedirect(r);
		//从请求头中，获取用户浏览器的上一跳，并设置到click对象中
		click.setReferer(headers.get(Names.REFERER));
		//创意一个这次点击的key，返回值为int[]数组，
		//int[0]=不重复的key
		//int[1]=ck=95d945c6093124e49829b3e0f27e2275;Max-Age=2592000; Path=/; Domain=配置文件中得域名
		//int[2]=ck=95d945c6093124e49829b3e0f27e2275;Max-Age=2592000; Path=/; Domain=配置文件中得域名;Secure
		String[] key = cookieUtil.nextClickKey();
		//将此次点击的key，设置在click对象中
		click.setClickKey(key[0]);

		countService.increClick(click);
		List<String> ck = new ArrayList<String>(4);
		if(hasCookieId) {
			// 有cookieid的只种clickkey
			ck.add(key[1]);
			ck.add(key[2]);
			log.info("ck对象>>>>>>>>>>>>>>>>>>"+JSON.toJSONString(ck));
			ret(ctx, r, ck);
		} else {
			ck.addAll(setCookie);
			ck.add(key[1]);
			ck.add(key[2]);
			log.info("ck对象>>>>>>>>>>>>>>>>>>"+JSON.toJSONString(ck));
			ret(ctx, r, ck);
		}
	}

	/**id=712c05f0e90fdb8c35c3ee8f5ad8e0e7;Max-Age=2147483647;Path=/;Domain=域名;Secure
	 * @param ctx			netty上下文
	 * @param redirect		广告要跳转的地址
	 * @param ck 			要种的cookie，如果客户端有cookie，
	 * 						则：ck[0]="ck=95d945c6093124e49829b3e0f27e2275;Max-Age=2592000; Path=/; Domain=配置文件中得域名;"
	 * 						   ck[1]="ck=95d945c6093124e49829b3e0f27e2275;Max-Age=2592000; Path=/; Domain=配置文件中得域名;Secure"
	 * 						如果客户端没有cookie
	 * 					    则：ck[0]= cookieid
	 * 						   ck[1]= id=712c05f0e90fdb8c35c3ee8f5ad8e0e7;Max-Age=2147483647;Path=/;Domain=域名
	 * 						   ck[2]= id=712c05f0e90fdb8c35c3ee8f5ad8e0e7;Max-Age=2147483647;Path=/;Domain=域名;Secure
	 * 						   ck[3]= ck=95d945c6093124e49829b3e0f27e2275;Max-Age=2592000; Path=/; Domain=配置文件中得域名
	 * 						   ck[4]= ck=95d945c6093124e49829b3e0f27e2275;Max-Age=2592000; Path=/; Domain=配置文件中得域名;Secure
	 */
	private void ret(ChannelHandlerContext ctx, String redirect, List<String> ck) {
		//如果调转地址不为null，并且长度大于4，并且是以“http”开头的，则使用跳转网页的方式向浏览器写入cookie
		if (redirect != null && redirect.length() > 4 && "http".equalsIgnoreCase(redirect.substring(0, 4))) {
			HandlerUtil.redirect(ctx, redirect, ck);
		} else {
			//否则 使用图片请求的方式向浏览器写入cookie
			HandlerUtil.img11(ctx, ck);
		}
	}
	
	/**
	 * @param ctx
	 * @param redirect
	 */
	private void ret(ChannelHandlerContext ctx, String redirect) {
		ret(ctx, redirect, null);
	}
}
