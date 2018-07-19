package com.jtd.effect.handler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.http.HttpHeaders.Names;
import org.jboss.netty.handler.codec.http.HttpRequest;

import com.jtd.effect.Handler;
import com.jtd.effect.HttpHandler;
import com.jtd.effect.util.HandlerUtil;
import com.jtd.effect.util.RequestUtil;
import com.jtd.effect.util.SystemTime;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月12日
 * @项目名称 dsp-tracker
 * @描述 <p></p>
 */
@Handler(uri = "/info")
public class TempHandler implements HttpHandler{

	private LinkedTransferQueue<String> queue = new LinkedTransferQueue<String>();
	
	@Resource
	private SystemTime systemTime;
	
	@PostConstruct
	public void init() {
		
		Thread t = new Thread() {
			
			public void run() {
				BufferedWriter w = null;
				File f = new File("plugin.txt");
				try {
					w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f, f.exists()), "UTF-8"));
				} catch (Exception e) {
				}
				
				for(;!Thread.interrupted();) {
					String line = null;
					try {
						line = queue.poll(10, TimeUnit.SECONDS);
					} catch (InterruptedException e) {
					}
					if(line == null) {
						try {
							w.flush();
						} catch (IOException e) {
						}
						continue;
					}
					try {
						w.write(line + "\r\n");
					} catch (IOException e) {
					}
				}
				try {
					w.flush();
				} catch (IOException e) {
				}
			}
		};
		t.setDaemon(true);
		t.start();
	}

	/* (non-Javadoc)
	 * @see com.doddata.net.HttpHandler#process(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.handler.codec.http.HttpRequest)
	 */
	@Override
	public void process(ChannelHandlerContext ctx, HttpRequest req) throws Exception {
		
		Map<String, List<String>> params = RequestUtil.parseQuery(req);
		if(params == null || params.size() == 0) {
			HandlerUtil.img11(ctx);
			return;
		}

		String plugin = RequestUtil.getSingleParam(params, "p");
		if(plugin == null) {
			plugin = "noplugin";
		} else {
			plugin = URLDecoder.decode(plugin, "UTF-8");
		}
		String ua = RequestUtil.getSingleParam(params, "u");
		if(ua == null) ua = "noua";
		ua = URLDecoder.decode(ua, "UTF-8");
		String hua = req.headers().get(Names.USER_AGENT);
		if(hua == null) hua = "noua";
		String cookie = req.headers().get(Names.COOKIE);
		if(cookie == null) cookie = "nocookie";
		String ip = RequestUtil.getRequestIP(ctx, req);
		if(systemTime.getYyyyMMdd() < 20160501) queue.tryTransfer(ip + "\t" +ua + "\t" + hua + "\t" + cookie + "\t" + plugin);
		HandlerUtil.img11(ctx);
	}
}
