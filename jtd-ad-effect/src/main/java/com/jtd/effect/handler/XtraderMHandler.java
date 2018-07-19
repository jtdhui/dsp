package com.jtd.effect.handler;

import com.jtd.effect.Handler;
import com.jtd.effect.HttpHandler;
import com.jtd.effect.service.CookieDataService;
import com.jtd.effect.util.CookieUtil;
import com.jtd.effect.util.HandlerUtil;
import com.jtd.effect.util.RequestUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.http.HttpRequest;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月12日
 * @项目名称 dsp-tracker
 * @描述 <p>该类是做百度cookieMapping</p>
 */
@Handler(uri = "/xtraderc")
public class XtraderMHandler implements HttpHandler{

	private static final Log log = LogFactory.getLog(XtraderMHandler.class);
	private final Logger logMyDebug = LogManager.getLogger("myDebugLog");
	@Resource
	private CookieDataService cookieDataService;

	/* (non-Javadoc)
	 * @see com.doddata.net.HttpHandler#process(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.handler.codec.http.HttpRequest)
	 */
	@Override
	public void process(ChannelHandlerContext ctx, HttpRequest req) throws Exception {
		logMyDebug.info("零集cookie映射");
		//获取参数，如果参数为null，则返回一个像素点的空响应
		Map<String, List<String>> params = RequestUtil.parseQuery(req);
		if(params == null || params.size() == 0) {
			logMyDebug.info("tracker:零集cookie映射params为null>>>>"+params);
			HandlerUtil.img11(ctx);
			return;
		}
		//从请求头中获取dsp的cookie数据，如果为null，则直接返回一个像素点的响应
		Map<String, String> cookies = RequestUtil.parseCookie(req);
		if(cookies == null || cookies.size() == 0) {
			logMyDebug.info("tracker:零集cookie映射，从请求头中获取dsp的cookie为null>>>>"+cookies);
			HandlerUtil.img11(ctx);
			return;
		}

		//获取dsp的cookieid
		String cookieid = cookies.get(CookieUtil.ID);
		//获取零集的用户id
		List<String> xtid = params.get("xtid");
		if(cookieid!=null&&cookieid.length()>0){
			logMyDebug.info("tracker:零集cookie映射，cookieid不为null>>>>"+cookies);
			if(xtid!=null&&xtid.get(0).length()>0){
				logMyDebug.info("tracker:零集cookie映射，零集不为null>>>>"+xtid);
				cookieDataService.xtraderCookieMapping(xtid.get(0), cookieid);;
			}
		}
		
		HandlerUtil.img11(ctx);
	}
}
