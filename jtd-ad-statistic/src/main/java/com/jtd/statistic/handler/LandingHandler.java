package com.jtd.statistic.handler;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpHeaders.Names;
import org.jboss.netty.handler.codec.http.HttpRequest;

import com.jtd.statistic.Handler;
import com.jtd.statistic.HttpHandler;
import com.jtd.statistic.po.Landing;
import com.jtd.statistic.service.CountService;
import com.jtd.statistic.util.CookieUtil;
import com.jtd.statistic.util.HandlerUtil;
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
@Handler(uri = "/nl")
public class LandingHandler implements HttpHandler{

	private static final Log log = LogFactory.getLog(LandingHandler.class);

	@Resource
	private CountService countService;

	@Resource
	private SystemTime systemTime;

	@Resource
	private CookieUtil cookieUtil;

	/* (non-Javadoc)
	 * @see com.doddata.net.HttpHandler#process(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.handler.codec.http.HttpRequest)
	 */
	@Override
	public void process(ChannelHandlerContext ctx, HttpRequest req) throws Exception {

		log.info("落地请求>>>>>>>>>>>>>>>>>>>"+req);
		//获取请求参数
		Map<String, List<String>> params = RequestUtil.parseQuery(req);
		if (params == null || params.size() == 0) {
			HandlerUtil.img11(ctx);
			return;
		}
		//获取请求页面的url
		String pageUrl = RequestUtil.getSingleParam(params, "u");

		String pageReferer = RequestUtil.getSingleParam(params, "r");

		if (pageUrl == null) {
			HandlerUtil.img11(ctx);
			return;
		}
		try {
			pageUrl = URLDecoder.decode(pageUrl, "UTF-8");
			log.info("请求页面的url>>>>>>>>>>>>>>>>>>>"+pageUrl);
			if(pageReferer != null) pageReferer = URLDecoder.decode(pageReferer, "UTF-8");
			log.info("跳转页面的url>>>>>>>>>>>>>>>>>>>"+pageReferer);
		} catch (Exception e) {
			// nothing to do
		}

		Landing ld = new Landing();
		ld.setTime(systemTime.getTime());
		ld.setPageUrl(pageUrl);
		ld.setPageReferer(pageReferer);

		Map<String, String> cookies = RequestUtil.parseCookie(req);
		List<String> setCookie = null;
		if (cookies != null) {
			String cookeid = cookies.get(CookieUtil.ID);
			if(cookeid == null) cookeid = cookies.get(CookieUtil.SID);
			if (cookeid != null) {
				ld.setCookieid(cookeid);
			} else {
				String[] cid = cookieUtil.nextCookieId();
				ld.setCookieid(cid[0]);
				setCookie = new ArrayList<String>();
				setCookie.add(cid[1]);
				setCookie.add(cid[2]);
			}
			String clkkey = cookies.get(CookieUtil.CLICK_KEY);
			if(clkkey == null) clkkey = cookies.get(CookieUtil.SCLICK_KEY);
			ld.setClickKey(clkkey);
		} else {
			String[] cid = cookieUtil.nextCookieId();
			ld.setCookieid(cid[0]);
			setCookie = new ArrayList<String>();
			setCookie.add(cid[1]);
			setCookie.add(cid[2]);
		}
		
		if(StringUtils.isEmpty(ld.getClickKey())) {
			HandlerUtil.img11(ctx, setCookie);
			return;
		}

		HttpHeaders headers = req.headers();
		ld.setUserip(RequestUtil.getRequestIP(ctx, req));
		ld.setUserAgent(headers.get(Names.USER_AGENT));

		countService.increLanding(ld);
		HandlerUtil.img11(ctx, setCookie);
	}
}
