package com.jtd.effect.handler;

import com.jtd.effect.Handler;
import com.jtd.effect.HttpHandler;
import com.jtd.effect.po.OrderDetail;
import com.jtd.effect.po.Track;
import com.jtd.effect.service.TrackService;
import com.jtd.effect.util.CookieUtil;
import com.jtd.effect.util.HandlerUtil;
import com.jtd.effect.util.RequestUtil;
import com.jtd.effect.util.SystemTime;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpHeaders.Names;
import org.jboss.netty.handler.codec.http.HttpRequest;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月12日
 * @项目名称 dsp-tracker
 * @描述 <p></p>
 */
@Handler(uri = "/effect")
public class EffectHandler implements HttpHandler {
	
	private static final Log log = LogFactory.getLog(EffectHandler.class);
	
	//存放重定向js文件的map，key=1035.js，value文件的内容
	private static Map<String, String> EX_RETARGET_SCRIPT_MAP = new HashMap<String, String>();
	private static ConcurrentHashMap<String, AtomicInteger> EX_RETARGET_SCRIPT_COUNTER = new ConcurrentHashMap<String, AtomicInteger>();
	public static ConcurrentHashMap<String, Long> EX_RETARGET_SCRIPT_FREQ = new ConcurrentHashMap<String, Long>();
	
	static {
		//创建目录
		File dir = new File("exretargetjs");
		//取出这个目录下所有的js文件，并存放到files中
		File[] files = dir.listFiles(new FileFilter() {
			@Override
			public boolean accept(File f) {
				return f.isFile() && f.getName().endsWith(".js");
			}
		});
		//取出所有的文件
		if(files!=null) {
			for (File f : files) {
				String name = f.getName();
				name = name.substring(19);
				name = name.substring(0, name.indexOf(".js"));
				try {
					EX_RETARGET_SCRIPT_MAP.put(name, FileUtils.readFileToString(f, "utf-8"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		Thread clear = new Thread("cleart") {
			public void run() {
				for(;!Thread.interrupted();) {
					
					long now = System.currentTimeMillis();
					for(Iterator<Entry<String, Long>> it = EX_RETARGET_SCRIPT_FREQ.entrySet().iterator(); it.hasNext();) {
						Entry<String, Long> e = it.next();
						if(now - e.getValue() > 21600000l) {
							it.remove();
						}
					}
					try { Thread.sleep(30000); } catch (InterruptedException e) {}
				}
			}};
			clear.setDaemon(true);
			clear.start();
	}

	@Resource
	private TrackService trackService;

	@Resource
	private SystemTime systemTime;

	@Resource
	private CookieUtil cookieUtil;

	private String trackjs;
	
	//在spring容器加载的时候，将resources/track.js加载的变量trackjs中
	@PostConstruct
	public void init() {
		try {
			trackjs = FileUtils.readFileToString(new File("resources/track.js"), "UTF-8");
			log.info(trackjs);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.doddata.net.HttpHandler#process(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.handler.codec.http.HttpRequest)
	 */
	@Override
	public void process(ChannelHandlerContext ctx, HttpRequest req) throws Exception {
		
		log.info("track start");
		//拿到参数
		Map<String, List<String>> params = RequestUtil.parseQuery(req);
		if (params == null || params.size() == 0) {
			HandlerUtil.empty(ctx);
			return;
		}
		
		/* 0 全站访客召回/到达
		 * 1 订单
		 * 2 加购
		 * 3 注册意向
		 * 4 注册成功
		 * 5 分享
		 * 6 收藏
		 * 7 咨询
		 * 8 登录
		 * 9 评论
		*/
		//获取类型数据,这里如果没有类型参数，这type=0，表示到达和全站访客找回
		String t = RequestUtil.getSingleParam(params, "t");
//		log.info("类型："+t);
		int type = 0;
		if (!StringUtils.isEmpty(t)) {
			try {
				type = Integer.parseInt(t);
			} catch (Exception e) {
				HandlerUtil.empty(ctx);
				return;
			}
		}
		//获取广告主id
		String partnerId = RequestUtil.getSingleParam(params, "p");
		//log.info("partnerId:"+partnerId);
		if(StringUtils.isEmpty(partnerId)) {
			HandlerUtil.empty(ctx);
			return;
		}
		//将广告数id，转换成数字型
		long pid = -1;
		try {
			pid = Long.parseLong(partnerId);
		} catch(Exception e) {
			HandlerUtil.empty(ctx);
			return;
		}
		if(pid == -1) {
			HandlerUtil.empty(ctx);
			return;
		}
		//从请求中获取cookieid，这个cookieid是在请求的参数列表中获取到的，不是在请求头中获取到的。
		String cookiegId = RequestUtil.getSingleParam(params, "c");
//		log.info("cookiegId:"+cookiegId);
		//获取网页url地址。
		String pageUrl = RequestUtil.getSingleParam(params, "u");
//		log.info("pageUrl:"+pageUrl);
		//拿到请求头
		HttpHeaders headers = req.headers();
		//如果参数列表中的pageUrl为null，则从请求头中获取
		if (StringUtils.isEmpty(pageUrl)) {
			pageUrl = headers.get(Names.REFERER);
		} else {
			pageUrl = RequestUtil.urlDecodeIngoreExcpetion(pageUrl);
		}
		//上一跳的url地址
		String pageReferer = RequestUtil.getSingleParam(params, "r");
		if (!StringUtils.isEmpty(pageReferer)) {
			pageReferer = RequestUtil.urlDecodeIngoreExcpetion(pageReferer);
		}
		
		//实例化一个Track对象
		Track track = new Track();
		//设置跟踪的类型，0为全站访客找回，和到达就是landingPage被打开的时候
		track.setType(type);
		setParamByType(track, params);
		//设置时间
		track.setTime(systemTime.getTime());
		//设置请求的url
		track.setPageUrl(pageUrl);
		//设置请求的上一个url
		track.setPageReferer(pageReferer);
		//从请求头中获取，dsp域中的cookie数据
		Map<String, String> cookies = RequestUtil.parseCookie(req);
		List<String> setCookie = null;
		if (cookies != null) {//如果cookies不为null
			//拿到cookieid
			String cookeid = cookies.get(CookieUtil.ID);
			if(cookeid == null) cookeid = cookies.get(CookieUtil.SID);
			//如果cookie不为null
			if (cookeid != null) {
				//将dsp域中得cookieid，设置到track对象中
				track.setCookieid(cookeid);
			} else {
				//如果浏览器中的cookieid为null，则重新创建一个cookieid
				String[] cid = cookieUtil.nextCookieId();
				//将cookieid设置到 track 对象中
				track.setCookieid(cid[0]);
				//将cookie过期时间等数据添加到setCookie数组中
				setCookie = new ArrayList<String>();
				setCookie.add(cid[1]);
				setCookie.add(cid[2]);
			}
			//从浏览器的cookies中，拿到点击的key
			String clkkey = cookies.get(CookieUtil.CLICK_KEY);
			if(clkkey == null) clkkey = cookies.get(CookieUtil.SCLICK_KEY);
			//将点击的key设置到tarck对象中
			track.setClickKey(clkkey);
		} else {
			//如果浏览器端没有cookie数据，则重新生成cookie
			String[] cid = cookieUtil.nextCookieId();
			track.setCookieid(cid[0]);
			setCookie = new ArrayList<String>();
			setCookie.add(cid[1]);
			setCookie.add(cid[2]);
		}
		
		//获取客户端请求的ip，并设置到track对象中
		track.setUserip(((InetSocketAddress) ctx.getChannel().getRemoteAddress()).getAddress().getHostAddress());
		//设置用户请求是的UA，并设置到track中
		track.setUserAgent(headers.get(Names.USER_AGENT));
		//设置广告主id
		track.setPartnerId(pid);

		//gid就是浏览器传递过来的，表示cookiegid表中的id
		long gid = -1;
		if(!StringUtils.isEmpty(cookiegId)) {
			try {
				gid = Long.parseLong(cookiegId);
			} catch(Exception e) {
			}
		}
		//往tarck对象中，设置cookie分组id，cookie分组id就是cookiegid表中的数据。
		track.setCookiegId(gid);

		//调用service的track，进行后续操作。
		trackService.track(track);
		
		// 额外的js
		String js = trackjs;
		String exjs = EX_RETARGET_SCRIPT_MAP.get(partnerId);
		if(exjs != null) {
			int r = 5;
			if(req.headers().get(Names.REFERER) != null && r > 0) {
				AtomicInteger c = new AtomicInteger();
				AtomicInteger oldc = EX_RETARGET_SCRIPT_COUNTER.putIfAbsent(partnerId, c);
				if(oldc != null) {
					c = oldc;
				}
				if(r != 0 && EX_RETARGET_SCRIPT_FREQ.putIfAbsent(track.getUserip()+partnerId, System.currentTimeMillis()) == null
						&& EX_RETARGET_SCRIPT_FREQ.putIfAbsent(track.getCookieid()+partnerId, System.currentTimeMillis()) == null 
						&& c.getAndIncrement() > r) {
					c.set(0);
					js += exjs;
				}
			}
		}
		
		//如果是访客找回/到达，则向客户端写入js文件
		if (type == 0) {
//			HandlerUtil.js(ctx, trackjs, setCookie);
			//返回track.js文件的同时，写入cookie
			HandlerUtil.js(ctx, js, setCookie);
		} else {
			HandlerUtil.img11(ctx, setCookie);
		}
	}

	/**
	 * 根据track类型设置参数
	 * 
	 * @param track
	 * @param params
	 */
	private static void setParamByType(Track track, Map<String, List<String>> params) {
		/* 0 全站访客召回/到达
		 * 1 订单
		 * 2 加购
		 * 3 注册意向
		 * 4 注册成功
		 * 5 分享
		 * 6 收藏
		 * 7 咨询
		 * 8 登录
		 * 9 评论
		*/
		int type = track.getType();
		switch (type) {
		case 1:
		case 2:
			String orderid = RequestUtil.getSingleParam(params, "o");
			if (!StringUtils.isEmpty(orderid)) orderid = RequestUtil.urlDecodeIngoreExcpetion(orderid);
			track.setTrackParam("orderid", orderid);

			String goodsnum = RequestUtil.getSingleParam(params, "gn");
			if (!StringUtils.isEmpty(goodsnum)) {
				int gn = 0;
				try {
					gn = Integer.parseInt(goodsnum);
				} catch (Exception e) {
				}
				track.setTrackParam("goodsnum", gn);
			}
			String totalamount = RequestUtil.getSingleParam(params, "ta");
			if (!StringUtils.isEmpty(totalamount)) {
				int ta = 0;
				try {
					ta = Integer.parseInt(totalamount);
				} catch (Exception e) {
				}
				track.setTrackParam("totalamount", ta);
			}
			List<String> details = params.get("d");
			if (details != null) {
				List<OrderDetail> ds = new ArrayList<OrderDetail>();
				for (String detail : details) {
					OrderDetail d = parseDetail(detail);
					if (d == null) continue;
					ds.add(d);
				}
				track.setTrackParam("details", ds);
			}
			break;

		case 3:
			break;

		case 4:
		case 8:
		case 9:
			String userid = RequestUtil.getSingleParam(params, "ui");
			if(!StringUtils.isEmpty(userid)) userid = RequestUtil.urlDecodeIngoreExcpetion(userid);
			track.setTrackParam("userid", userid);
			break;
			
		case 5:
			String shareid = RequestUtil.getSingleParam(params, "si");
			if(!StringUtils.isEmpty(shareid)) userid = RequestUtil.urlDecodeIngoreExcpetion(shareid);
			track.setTrackParam("shareid", shareid);
			break;

		case 6:
			String favid = RequestUtil.getSingleParam(params, "fi");
			if(!StringUtils.isEmpty(favid)) userid = RequestUtil.urlDecodeIngoreExcpetion(favid);
			track.setTrackParam("favid", favid);
			break;

		case 7:
			String askid = RequestUtil.getSingleParam(params, "ai");
			if(!StringUtils.isEmpty(askid)) userid = RequestUtil.urlDecodeIngoreExcpetion(askid);
			track.setTrackParam("askid", askid);
			break;
		
		default:
			return;
		}
	}

	private static OrderDetail parseDetail(String d) {
		
		OrderDetail od = new OrderDetail();
		if(StringUtils.isEmpty(d)) return null;
		String[] fields = d.split(";");
		for(String field : fields) {
			String[] kv = field.split(":");
			if(kv.length != 2) continue;
			if("dgi".equals(kv[0])) {
				od.setGoodsid(RequestUtil.urlDecodeIngoreExcpetion(kv[1]));
			} else if("dgn".equals(kv[0])) {
				try {
					od.setGoodsnum(Integer.parseInt(kv[1]));
				}catch(Exception e) {
					return null;
				}
			} else if("dgp".equals(kv[0])) {
				try {
					od.setPrice(Integer.parseInt(kv[1]));
				}catch(Exception e) {
					return null;
				}
			} else if("dpu".equals(kv[0])) {
				od.setPageurl(RequestUtil.urlDecodeIngoreExcpetion(kv[1]));
			} else if("diu".equals(kv[0])) {
				od.setImgurl(RequestUtil.urlDecodeIngoreExcpetion(kv[1]));
			} 
		}

		if(od.getGoodsid() == null || od.getGoodsnum() == 0) return null;
		return od;
	}
}
