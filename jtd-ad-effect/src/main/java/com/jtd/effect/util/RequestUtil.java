package com.jtd.effect.util;

import org.apache.commons.lang3.StringUtils;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.http.HttpHeaders.Names;
import org.jboss.netty.handler.codec.http.HttpRequest;

import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月12日
 * @项目名称 dsp-tracker
 * @描述 <p></p>
 */
public class RequestUtil {
	
	/**
	 * 解析请求参数
	 * @param req
	 * @return
	 */
	public static Map<String, List<String>> parseQuery(HttpRequest req) {
		String uri = req.getUri();
		if(StringUtils.isEmpty(uri)) return null;
		int i = uri.indexOf("?");
		if(i == -1) return null;
		String query = uri.substring(i + 1);
		if(StringUtils.isEmpty(query)) return null;
		Map<String, List<String>> ret = new HashMap<String, List<String>>();
		String[] entrys = query.split("&");
		for (String entry : entrys) {
			String[] kv = entry.split("=");
			
			// 没有参数名
			if (kv.length >= 1 && kv[0].trim().length() == 0) continue;
			if (kv.length == 1) {
				if (!ret.containsKey(kv[0])) {
					// 只有参数名
					ret.put(kv[0], null);
				}
			} else if (kv.length == 2) {
				if (ret.containsKey(kv[0])) {
					ret.get(kv[0]).add(kv[1].trim());
				} else {
					List<String> list = new ArrayList<String>(1);
					list.add(kv[1].trim());
					ret.put(kv[0], list);
				}
			}
		}
		return ret;
	}

	/**
	 * 解析Cookie，从相应头里拿到cookie，这里拿到的是dsp域下的cookie
	 * @param req
	 * @return
	 */
	public static Map<String, String> parseCookie(HttpRequest req) {
		String cookies = req.headers().get(Names.COOKIE);
		if(StringUtils.isEmpty(cookies)) return null;
		Map<String, String> ret = new HashMap<String, String>();
		String[] cks = cookies.split(";");
		for(String ck : cks) {
			int i = ck.indexOf("=");
			if(i == -1) continue;
			ret.put(ck.substring(0, i).trim(), ck.substring(i + 1).trim());
		}
		return ret;
	}

	/**
	 * @param params
	 * @param param
	 * @return
	 */
	public static String getSingleParam(Map<String, List<String>> params, String param) {
		List<String> p = params.get(param);
		if (p == null || p.size() != 1) return null;
		return p.get(0);
	}

	public static String urlDecodeIngoreExcpetion(String enc) {
		String ret = enc;
		try {
			ret = URLDecoder.decode(ret, "UTF-8");
		} catch (Exception e) {
		}
		return ret;
	}
	
	/**
	 * @param ctx
	 * @param req
	 * @return
	 */
	public static String getRequestIP(ChannelHandlerContext ctx, HttpRequest req) {
		String xff = req.headers().get("X-Forwarded-For");
		if(!StringUtils.isEmpty(xff)) {
			return xff.split(",")[0].trim();
		} else {
			return ((InetSocketAddress) ctx.getChannel().getRemoteAddress()).getAddress().getHostAddress();
		}
	}
}
