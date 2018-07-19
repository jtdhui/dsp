package com.jtd.effect.handler;

import com.jtd.effect.Handler;
import com.jtd.effect.HttpHandler;
import com.jtd.effect.em.Adx;
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
 * @描述 <p>万流客 cookie Mapping</p>
 */
@Handler(uri = "/vamc")
public class VamCMHandler implements HttpHandler{

	private static final Log log = LogFactory.getLog(VamCMHandler.class);
	private final Logger logMyDebug = LogManager.getLogger("myDebugLog");

	@Resource
	private CookieDataService cookieDataService;

	/* (non-Javadoc)
	 * @see com.doddata.net.HttpHandler#process(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.handler.codec.http.HttpRequest)
	 */
	@Override
	public void process(ChannelHandlerContext ctx, HttpRequest req) throws Exception {
		
		logMyDebug.info("Vam mapping:1 开始");
		//如果没有参数，则返回一个像素点的图片
		Map<String, List<String>> params = RequestUtil.parseQuery(req);
		if(params == null || params.size() == 0) {
			HandlerUtil.img11(ctx);
			return;
		}
		logMyDebug.info("Vam mapping:2 获取万流客参数正常");
		//从相应头中拿到cookie，如果没有则返回一个像素点得图片
		Map<String, String> cookies = RequestUtil.parseCookie(req);
		if(cookies == null || cookies.size() == 0) {
			HandlerUtil.img11(ctx);
			return;
		}
		//dsp的cookieId
		String cookeid = cookies.get(CookieUtil.ID);
		logMyDebug.info("Vam mapping:3 cookeid="+cookeid);
		//vam的用户id
		List<String> vids = params.get("vamaker_id");
		//vam的用户id版本
		List<String> vers = params.get("vamaker_cver");
		//如果vam的id不为null，并且id版本不为null，并且id列表中只有一个，并且版本列表中只有一个，才进入
		//否则直接返回一个像素点的图片
		if (vids != null && vers != null && vids.size() == 1 && vers.size() == 1) {
			logMyDebug.info("Vam mapping:4 vids vers 不为null");
			String vid = vids.get(0);
			//vam的id和dsp的cookieid都不为null，才写入映射；否则直接返回
			if (vid != null && cookeid != null) {
				try {
//					vid = URLDecoder.decode(vid, "UTF-8");
//					vid = new String(WebSafeBase64.decode(vid));
					String ver = vers.get(0);
					logMyDebug.info("Vam mapping:5 vid="+vid+" ver="+ver+" cookeid="+cookeid);
					cookieDataService.cookieMapping(String.valueOf(Adx.VAM), vid + "_" + ver, cookeid);
				} catch (Exception e) {
					log.error("解码VID[" + vids.get(0) + "]发生错误", e);
				}
			}
		}

		HandlerUtil.img11(ctx);
	}
}
