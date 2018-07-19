package com.jtd.statistic.util;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.http.HttpHeaders.Names;
import org.jboss.netty.handler.codec.http.HttpRequest;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月11日
 * @项目名称 dsp-counter
 * @描述 <p></p>
 */
public class RequestUtil {
	
	/**
	 * 
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
	 * 解析Cookie
	 * @param req
	 * @return
	 */
	public static Map<String, String> parseCookie(HttpRequest req) {
		//从请求头中，获取名称为 “Cookie”的cookies
		String cookies = req.headers().get(Names.COOKIE);
		//如果没有拿到cookie，则返回null
		if(StringUtils.isEmpty(cookies)) return null;
		Map<String, String> ret = new HashMap<String, String>();
		//如果拿到cookie，则使用";"拆分数据
		String[] cks = cookies.split(";");
		for(String ck : cks) {
			//使用“=”拆分每一小段字符串，找到cookie中的key和value，put到ret中
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
	
	/**
	 * 获取http请求中，真是的ip
	 * @param ctx
	 * @param req
	 * @return
	 */
	public static String getRequestIP(ChannelHandlerContext ctx, HttpRequest req) {
		//拿到参数 X-Forwarded-For 表示真是ip，只用通过代理转发，或是负载转发后的请求，才会添加这个参数
		String xff = req.headers().get("X-Forwarded-For");
		if(!StringUtils.isEmpty(xff)) {
			//如果xff不为空，则将xff以 “，”分割，然后返回第0个值，这个值就是真实ip
			return xff.split(",")[0].trim();
		} else {
			//如果xff为空，则从netty的上下文中获取，ip地址
			return ((InetSocketAddress) ctx.getChannel().getRemoteAddress()).getAddress().getHostAddress();
		}
	}
}
