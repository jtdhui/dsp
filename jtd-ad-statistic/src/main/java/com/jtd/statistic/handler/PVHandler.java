package com.jtd.statistic.handler;

import java.math.BigDecimal;
import java.net.URLDecoder;
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
import com.jtd.statistic.em.Adx;
import com.jtd.statistic.po.PV;
import com.jtd.statistic.po.Param;
import com.jtd.statistic.service.CountService;
import com.jtd.statistic.util.CookieUtil;
import com.jtd.statistic.util.HandlerUtil;
import com.jtd.statistic.util.LikeBase64;
import com.jtd.statistic.util.PriceDecodeUtil;
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
@Handler(uri = "/np")
public class PVHandler implements HttpHandler{

	private static final Log log = LogFactory.getLog(PVHandler.class);
	
	private final Logger logMyDebug = LogManager.getLogger("myDebugLog");
	
	@Resource
	private CountService countService;

	@Resource
	private SystemTime systemTime;

	@Resource
	private CookieUtil cookieUtil;

	@Resource
	private PriceDecodeUtil priceDecodeUtil;

	/* (non-Javadoc)
	 * @see com.doddata.net.HttpHandler#process(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.handler.codec.http.HttpRequest)
	 */
	@Override
	public void process(ChannelHandlerContext ctx, HttpRequest req) throws Exception {
		log.info("曝光请求>>>>>>>>>>>>>>>>>>>"+req);
		//增加请求次数
		countService.increPvReq();
		//获取pv请求参数，如果没有拿到参数，则返回一个像素点的图片的响应
		Map<String, List<String>> params = RequestUtil.parseQuery(req);
		log.info("pv请求参数>>>>>>>>>>>>>>>>>>>"+params);
		if (params == null || params.size() == 0) {
			HandlerUtil.img11(ctx);
			return;
		}

		// 参数
		String q = RequestUtil.getSingleParam(params, "q");
		log.info("q请求参数编码前>>>>>>>>>>>>>>>>>>>"+q);
		// 成交价格
		String p = RequestUtil.getSingleParam(params, "p");
		log.info("成交价格，解密前>>>>>>>>>>>>>>>>>>>"+p);
		// 跳转地址，这个跳转地址目前暂时用不到
		String r = RequestUtil.getSingleParam(params, "r");
		//获取参数
		if(q != null) q = LikeBase64.decode(q);
//		if(q != null) {
//			try {
//				q = URLDecoder.decode(q, "UTF-8");
//				log.info("q请求参数解码后>>>>>>>>>>>>>>>>>>>" + q);
//			} catch (Exception e) {
//				log.info("编码请求参数出错>>>>>>>>>>>>>>>>>>>" + e.getMessage());
//			}
//		}
		log.info("q请求参数解码后>>>>>>>>>>>>>>>>>>>"+q);
		//获取调转地址
		if(r != null) r = LikeBase64.decode(r);
		//如果参数为null，则直接返回一个像素点的响应
		if (q == null) {
			ret(ctx, r);
			return;
		}
		//将参数从json字符串，转换成Param对象
		Param param = null;
		try {
			param = JSON.parseObject(q, Param.class);
		} catch (Exception e) {
			log.error("解析参数发生错误:" + q, e);
		}
		//如果param为null，则返回一个像素点的响应
		if(param == null) {
			ret(ctx, r);
			return;
		}
		//从参数中获取渠道id
		int channelId = param.getCh();
		BigDecimal price = null;
		//根据不同的渠道，解析实际的竞价价格
		if (Adx.TANX.channelId() == channelId) {
			//解析tanx的竞价价格,tanx要求把加密后的价格和竞价的请求时的竞价id一起发送给方法
			try {
				price = priceDecodeUtil.decodeTanxPrice(p, param.getB());
				log.info("tanx成交价格，解密后>>>>>>>>>>>>>>>>>>"+price);
			} catch (Exception e) {
				// Nothing to do
			}
		} else if (Adx.BES.channelId() == channelId) {
			//解析百度的竞价价格
			try {
				price = priceDecodeUtil.decodeBesPrice(p);
				log.info("百度成交价格，解密后>>>>>>>>>>>>>>>>>>"+price);
			} catch (Exception e) {
				// Nothing to do
			}
		} else if(Adx.VAM.channelId() == channelId) {
			//解析万流客竞价价格
			try {
				price = priceDecodeUtil.decodeVamPrice(p);
				log.info("万流客成交价格，解密后>>>>>>>>>>>>>>>>>>"+price);
			} catch (Exception e) {
				// Nothing to do
			}
		} else if(Adx.XTRADER.channelId() == channelId){
			//解析零集竞价价格
			try {
				price = priceDecodeUtil.decodeXtraderPrice(p);
				log.info("零集成交价格，解密后>>>>>>>>>>>>>>>>>>"+price);
			} catch (Exception e) {
				// Nothing to do
			}
		}else if(Adx.HZ.channelId() == channelId){
			try{
				price = priceDecodeUtil.decodeHzPrice(p);
				log.info("互众成交价格，解密后>>>>>>>>>>>>>>>>>>"+price);
			}catch(Exception ex){
			}
		} else {
			log.error("不认识的渠道ID:" + channelId);
		}
		//如果竞价价格为null，则将price设置为0
		if (price == null) price = new BigDecimal(0);
		//实例化pv对象
		PV pv = new PV();
		//将请求参数设置到pv对象中
		pv.setParam(param);
		//设置请求的时间
		pv.setTime(systemTime.getTime());

		//获取请求的cookie
		Map<String, String> cookies = RequestUtil.parseCookie(req);
		log.info("cookies信息>>>>>>>>>>>>>>>>>>"+cookies);
		List<String> setCookie = null;
		if (cookies != null) {
			//如果请求的cookie不为null
			//拿到cookieid
			String cookeid = cookies.get(CookieUtil.ID);
			if (cookeid != null) {
				//如果cookieid不为null，
				pv.setCookieid(cookeid);
			} else {
				//如果cookieid为null，则获取一个cookieid
				String[] cid = cookieUtil.nextCookieId();
				//将cookieid，设置到pv对象中
				pv.setCookieid(cid[0]);
				setCookie = new ArrayList<String>();
				//将之前写入的cookie对应的值，添加到setCookie liset集合中
				//int[1]=ck=95d945c6093124e49829b3e0f27e2275;Max-Age=2592000; Path=/; Domain=配置文件中得域名
				//int[2]=ck=95d945c6093124e49829b3e0f27e2275;Max-Age=2592000; Path=/; Domain=配置文件中得域名;Secure
				setCookie.add(cid[1]);
				setCookie.add(cid[2]);
			}
		} else {
			//获取内容如下
			//int[0]=95d945c6093124e49829b3e0f27e2275
			//int[1]=ck=95d945c6093124e49829b3e0f27e2275;Max-Age=2592000; Path=/; Domain=配置文件中得域名
			//int[2]=ck=95d945c6093124e49829b3e0f27e2275;Max-Age=2592000; Path=/; Domain=配置文件中得域名;Secure
			String[] cid = cookieUtil.nextCookieId();
			//将cid数组中的字符串，分别设置到pv对象、setCookie中，备用
			pv.setCookieid(cid[0]);
			setCookie = new ArrayList<String>();
			setCookie.add(cid[1]);
			setCookie.add(cid[2]);
		}
		//获取请求的头信息
		HttpHeaders headers = req.headers();
		//拿到请求的ip
		pv.setUserip(RequestUtil.getRequestIP(ctx, req));
		//拿到请求的ua信息
		pv.setUserAgent(headers.get(Names.USER_AGENT));
		//设置跳转地址
		pv.setRedirect(r);
		//获取跳转到请求页面之前的页面地址
		pv.setReferer(headers.get(Names.REFERER));
		//竞价价格
		pv.setPrice(price);
		log.info("pv对象>>>>>>>>>>>>>>>>>>"+JSON.toJSONString(pv));
		log.info("setCookie对象>>>>>>>>>>>>>>>>>>"+JSON.toJSONString(setCookie));
		//写入队列，会有专门的线程写入到redis和日志中
		countService.increPv(pv);
		//立即返回，这里如果r=null，则返回一个像素点的响应，否则返回302重定向，重定向地址是r
		ret(ctx, r, setCookie);
	}

//	@Override
//	public void process(ChannelHandlerContext ctx, HttpRequest req) throws Exception {
//		//获取请求的cookie
//		Map<String, String> cookies = RequestUtil.parseCookie(req);
//		log.info("cookies信息>>>>>>>>>>>>>>>>>>"+cookies);
//		List<String> setCookie = null;
//		if (cookies != null) {
//			//如果请求的cookie不为null
//			//拿到cookieid
//			String cookeid = cookies.get(CookieUtil.ID);
//			if (cookeid != null) {
//
//			} else {
//				//如果cookieid为null，则获取一个cookieid
//				String[] cid = cookieUtil.nextCookieId();
//				setCookie = new ArrayList<String>();
//				//将之前写入的cookie对应的值，添加到setCookie liset集合中
//				//int[1]=ck=95d945c6093124e49829b3e0f27e2275;Max-Age=2592000; Path=/; Domain=配置文件中得域名
//				//int[2]=ck=95d945c6093124e49829b3e0f27e2275;Max-Age=2592000; Path=/; Domain=配置文件中得域名;Secure
//				setCookie.add(cid[1]);
//				setCookie.add(cid[2]);
//			}
//		} else {
//			//获取内容如下
//			//int[0]=95d945c6093124e49829b3e0f27e2275
//			//int[1]=ck=95d945c6093124e49829b3e0f27e2275;Max-Age=2592000; Path=/; Domain=配置文件中得域名
//			//int[2]=ck=95d945c6093124e49829b3e0f27e2275;Max-Age=2592000; Path=/; Domain=配置文件中得域名;Secure
//			String[] cid = cookieUtil.nextCookieId();
//			setCookie = new ArrayList<String>();
//			setCookie.add(cid[1]);
//			setCookie.add(cid[2]);
//		}
//		//获取请求的头信息
//		HttpHeaders headers = req.headers();
//		//拿到请求的ip
//		String userId=RequestUtil.getRequestIP(ctx, req);
//		//拿到请求的ua信息
//		String userAgent=headers.get(Names.USER_AGENT);
//		//获取跳转到请求页面之前的页面地址
//		String referer=headers.get(Names.REFERER);
//		log.info("setCookie对象>>>>>>>>>>>>>>>>>>"+JSON.toJSONString(setCookie));
//		//立即返回，这里如果r=null，则返回一个像素点的响应，否则返回302重定向，重定向地址是r
//		ret(ctx, null, setCookie);
//	}

	/**
	 * @param ctx
	 * @param redirect
	 */
	private void ret(ChannelHandlerContext ctx, String redirect) {
		ret(ctx, redirect, null);
	}

	/**
	 * @param ctx
	 * @param redirect
	 * @param cookieid 要种的cookieid
	 */
	private void ret(ChannelHandlerContext ctx, String redirect, List<String> cookieid) {
		if (redirect != null && redirect.length() > 4 && "http".equalsIgnoreCase(redirect.substring(0, 4))) {
			HandlerUtil.redirect(ctx, redirect, cookieid);
		} else {
			HandlerUtil.img11(ctx, cookieid);
		}
	}
}
