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
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;
/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月12日
 * @项目名称 dsp-tracker
 * @描述 <p>该类是做tanx cookie Mapping</p>
 */
@Handler(uri = "/tanxc")
public class TanxCMHandler implements HttpHandler{

	private static final Log log = LogFactory.getLog(TanxCMHandler.class);
	
	private final Logger logMyDebug = LogManager.getLogger("myDebugLog");
	
	@Resource
	private CookieDataService cookieDataService;

	/* (non-Javadoc)
	 * @see com.doddata.net.HttpHandler#process(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.handler.codec.http.HttpRequest)
	 */
	@Override
	public void process(ChannelHandlerContext ctx, HttpRequest req) throws Exception {
		
		logMyDebug.info("Tanx mapping:1 开始mapping");
		Map<String, List<String>> params = RequestUtil.parseQuery(req);
		if(params == null || params.size() == 0) {
			HandlerUtil.img11(ctx);
			return;
		}
		logMyDebug.info("Tanx mapping:2 获取tanx参数成功");
		Map<String, String> cookies = RequestUtil.parseCookie(req);
		if(cookies == null || cookies.size() == 0) {
			HandlerUtil.img11(ctx);
			return;
		}
		String cookeid = cookies.get(CookieUtil.ID);
		
		logMyDebug.info("Tanx mapping:3 dsp cookieid="+cookeid);
		
		List<String> tids = params.get("tanx_tid");
		List<String> tvers = params.get("tanx_ver");
		logMyDebug.info(">>>>>"+tids+">>>"+tvers);
		if (tids != null && tvers != null && tids.size() == 1 && tvers.size() == 1) {
			logMyDebug.info("Tanx mapping:4 tids tvers 不为null");
			String tid = tids.get(0);
			if (!"E0".equalsIgnoreCase(tid) && cookeid != null) {
				logMyDebug.info("Tanx mapping:5 cookeid 不为null >>> "+(!"E0".equalsIgnoreCase(tid)));
				try {
					tid = URLDecoder.decode(tid, "UTF-8");
					String tver = tvers.get(0);
					logMyDebug.info("Tanx mapping:6 tid="+tid+" tver="+tver+" cookeid="+cookeid);
					cookieDataService.tanxCookieMapping(tid, Integer.parseInt(tver), cookeid);
				} catch (Exception e) {
					log.error("解码TID[" + tids.get(0) + "]发生错误", e);
				}
			}
		}

		HandlerUtil.img11(ctx);
	}
}
