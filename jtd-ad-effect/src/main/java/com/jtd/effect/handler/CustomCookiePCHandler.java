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
 * @创建日期 2017年5月23日
 * @项目名称 dsp-tracker
 * @描述 <p>相应特殊人员cookie保存服务</p>
 */
@Handler(uri = "/cc")
public class CustomCookiePCHandler implements HttpHandler{

	private static final Log log = LogFactory.getLog(CustomCookiePCHandler.class);
	private final Logger logMyDebug = LogManager.getLogger("myDebugLog");
	@Resource
	private CookieDataService cookieDataService;

	/* (non-Javadoc)
	 * @see com.doddata.net.HttpHandler#process(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.handler.codec.http.HttpRequest)
	 */
	@Override
	public void process(ChannelHandlerContext ctx, HttpRequest req) throws Exception {
		//获取参数，如果参数为null，则返回一个像素点的空响应
		Map<String, List<String>> params = RequestUtil.parseQuery(req);
		if(params == null || params.size() == 0) {
			logMyDebug.info("没有拿到参数>>>>"+params);
			HandlerUtil.img11(ctx);
			return;
		}
		//从请求头中获取dsp的cookie数据，如果为null，则直接返回一个像素点的响应
		Map<String, String> cookies = RequestUtil.parseCookie(req);
		if(cookies == null || cookies.size() == 0) {
			logMyDebug.info("没有拿到dsp的cookie>>>>"+cookies);
			HandlerUtil.img11(ctx);
			return;
		}

		//获取dsp的cookieid
		String cookieid = cookies.get(CookieUtil.ID);
		List<String> campid = params.get("cid");
		List<String> parentid = params.get("pid");
		List<String> callback = params.get("callback");
		
		//删除特殊用户优先权
		List<String> del = params.get("del");
		if(del!=null&&del.size()>0){
			if(del.get(0).equals("1")){
				//删除操作
				try{
					long retl=cookieDataService.delCustomCookie(cookieid);
					if(retl<0){
						log.error("删除优先匹配广告用户错误。");
					}
				}catch(Exception ex){
					log.error("删除优先匹配广告用户错误。");
				}
				HandlerUtil.redirect(ctx, callback.get(0));
				return ;
			}
		}
		
		cookieDataService.writeCustomCookie(cookieid, "cid", campid.get(0));
		cookieDataService.writeCustomCookie(cookieid, "pid", parentid.get(0));
		
		HandlerUtil.redirect(ctx, callback.get(0)+"?pid="+parentid.get(0)+"&campid="+campid.get(0)+"&dspuid="+cookieid);
		
		//HandlerUtil.img11(ctx);
	}
}
