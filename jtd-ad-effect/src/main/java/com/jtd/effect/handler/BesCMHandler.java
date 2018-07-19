package com.jtd.effect.handler;

import com.alibaba.fastjson.JSON;
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
import java.net.URLDecoder;
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
@Handler(uri = "/besc")
public class BesCMHandler implements HttpHandler{

	private static final Log log = LogFactory.getLog(BesCMHandler.class);

	private final Logger logMyDebug = LogManager.getLogger("myDebugLog");
	
	@Resource
	private CookieDataService cookieDataService;

	/* (non-Javadoc)
	 * @see com.doddata.net.HttpHandler#process(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.handler.codec.http.HttpRequest)
	 */
	@Override
	public void process(ChannelHandlerContext ctx, HttpRequest req) throws Exception {
//		log.info("百度cookie映射");
		//获取参数，如果参数为null，则返回一个像素点的空响应
		logMyDebug.info("bes mapping:1 开始mapping");
		logMyDebug.info("bes mapping 请求参数:"+req);
		Map<String, List<String>> params = RequestUtil.parseQuery(req);
		logMyDebug.info("bes mapping 请求参数:"+params);
		if(params == null || params.size() == 0) {
			HandlerUtil.img11(ctx);
			return;
		}
		logMyDebug.info("bes mapping:2 获取dsp cookie");
		//从请求头中获取dsp的cookie数据，如果为null，则直接返回一个像素点的响应
		Map<String, String> cookies = RequestUtil.parseCookie(req);
		logMyDebug.info("dsp cookies数据:"+cookies);
		if(cookies == null || cookies.size() == 0) {
			HandlerUtil.img11(ctx);
			return;
		}
		//获取dsp的cookieid
		String cookeid = cookies.get(CookieUtil.ID);
		logMyDebug.info("bes mapping:3 获取dsp cookie="+cookeid);
		//获取百度的错误参数
		List<String> biderrs = params.get("baidu_error");
		//获取百度的用户id
		List<String> bids = params.get("baidu_user_id");
		//获取百度的cookie版本
		List<String> bvers = params.get("cookie_version");
		
		//如果错误参数为null，直接返回
		if (biderrs == null || biderrs.size() == 0) {
			logMyDebug.info("bes mapping:4 biderrs=null");
			//百度的 用户id不为空且只有一个，cookie版本不为空且只有一个，dsp的cookieid不为null，才做cookie映射
			//否则直接返回一个像素点的响应
			if (bids != null && bids.size() == 1 && bvers != null && bvers.size() == 1 && cookeid != null) {
				logMyDebug.info("bes mapping:5 bids="+bids.toString());
				logMyDebug.info("bes mapping:6 bvers="+bvers.toString());
				try {
					//使用UTF-8编码方式，反编码百度的用户id
					String besid = URLDecoder.decode(bids.get(0), "UTF-8");
//					log.info("百度用户id，besid>>>>"+besid);
					//获取百度的cookie版本数据
					String besver = bvers.get(0);
//					log.info("百度用户版本，besid>>>>"+besver);
					logMyDebug.info("bes mapping:7 bids="+besid);
					logMyDebug.info("bes mapping:8 besver="+besver);
					logMyDebug.info("bes mapping:9 bids="+besid+" besver="+besver+" cookeid="+cookeid);
					//写入百度和dsp的cookie映射
					if (besid.length() != 0) cookieDataService.besCookieMapping(besid, besver, cookeid);
				} catch (Exception e) {
					log.error("解码BESID[" + bids.get(0) + "]发生错误", e);
				}
			}
		}
		
		HandlerUtil.img11(ctx);
	}
}
