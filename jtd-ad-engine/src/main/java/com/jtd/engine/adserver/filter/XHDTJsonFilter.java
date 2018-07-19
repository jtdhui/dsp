package com.jtd.engine.adserver.filter;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.write.WriteRequest;

import com.alibaba.fastjson.JSON;
import com.jtd.engine.message.v1.HttpRequest;
import com.jtd.engine.message.v1.HttpResponse;
import com.jtd.engine.message.v1.XHDTBidRequest;
import com.jtd.engine.message.v1.XHDTBidResponse;
import com.jtd.engine.utils.SystemTime;


/**
 * 新汇通达广告接口filter，json参数
 * @author zl
 * 2017-09-26
 */
public class XHDTJsonFilter extends IoFilterAdapter{

	private static final String JX_BID_URI = "/xhdt";

	private static final Log log = LogFactory.getLog(BesProtoBufFilter.class);
	
	// 这个Filter在哪个本地地址上起作用
	private Set<InetSocketAddress> interceptAddresses = new HashSet<InetSocketAddress>();

	// 系统时间工具
	private SystemTime systemTime;

	@Override
	public void messageReceived(NextFilter nextFilter, IoSession session,Object message) throws Exception 
	{
		if (interceptAddresses.contains(session.getLocalAddress())) {

			if(message instanceof HttpRequest){
				HttpRequest req = (HttpRequest)message;
				//if(req.getMethod() == HttpRequest.POST && JX_BID_URI.equals(req.getUri()))  上线时修改为post
				if(JX_BID_URI.equals(req.getUri()))
				{
					byte[] body = req.getBody();
					if(body == null || body.length == 0)
						nextFilter.messageReceived(session, message);
					String json = new String(body, 0, body.length, "UTF-8");
					XHDTBidRequest msg = JSON.parseObject(json, XHDTBidRequest.class);
					nextFilter.messageReceived(session, msg);
				} else {
					nextFilter.messageReceived(session, message);
				}
			} else {
				nextFilter.messageReceived(session, message);
			}
		} else {
			nextFilter.messageReceived(session, message);
		}
		
	}

	@Override
	public void filterWrite(NextFilter nextFilter, IoSession session, WriteRequest writeRequest) throws Exception {
		if (interceptAddresses.contains(session.getLocalAddress())) 
		{
			Object message = writeRequest.getMessage();
			if(message instanceof XHDTBidResponse) 
			{
				XHDTBidResponse rsp = (XHDTBidResponse) message;
				if(rsp.getAds() != null && rsp.getAds().size() > 0) /** 如果response中有广告则返回 */
				{
					final HttpResponse response = new HttpResponse();
					byte[] body = JSON.toJSONString(message).getBytes("UTF-8");
					response.setStatus(200);
					response.setStatusText("OK");
					response.setHeaders(new String[] {
						"Server: AsmeServer",
						new StringBuilder("Date: ").append(systemTime.getGmt()).toString(),
						"Content-Type: application/json",
						"x-adviewrtb-version: 1.0",
						new StringBuilder("Content-Length: ").append(body.length).toString(),
						"Connection: Keep-Alive"
					});
					response.setContent(body);
					final WriteRequest wr = writeRequest;
					nextFilter.filterWrite(session, new WriteRequest() 
					{
						@Override
						public SocketAddress getDestination() { return wr.getDestination(); }
						@Override
						public WriteFuture getFuture() { return wr.getFuture(); }
						@Override
						public Object getMessage() { return response; }
						@Override
						public WriteRequest getOriginalRequest() { return wr; }
					});
				} else {
					final HttpResponse response = new HttpResponse();
					response.setStatus(204);
					response.setStatusText("No Content");
					response.setHeaders(new String[] {
						"Server: AsmeServer",
						new StringBuilder("Date: ").append(systemTime.getGmt()).toString(),
						"x-adviewrtb-version: 1.0",
						"Content-Length: 0",
						"Connection: Keep-Alive"
					});
					final WriteRequest wr = writeRequest;
					nextFilter.filterWrite(session, new WriteRequest() {
						@Override
						public SocketAddress getDestination() { return wr.getDestination(); }
						@Override
						public WriteFuture getFuture() { return wr.getFuture(); }
						@Override
						public Object getMessage() { return response; }
						@Override
						public WriteRequest getOriginalRequest() { return wr; }
					});
				}
			} else {
				nextFilter.filterWrite(session, writeRequest);
			}
		} else {
			nextFilter.filterWrite(session, writeRequest);
		}
	}

	
	
	public void setInterceptAddresses(List<InetSocketAddress> interceptAddresses) {
		for(InetSocketAddress addr : interceptAddresses) {
			this.interceptAddresses.add(addr);
			log.info(this.getClass().getSimpleName() + "拦截地址: " + addr);
		}
	}

	public void setSystemTime(SystemTime systemTime) {
		this.systemTime = systemTime;
	}

}
