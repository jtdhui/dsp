package com.jtd.engine.adserver.filter;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.write.WriteRequest;

import com.alibaba.fastjson.JSON;
import com.jtd.engine.message.v1.AdwoBidRequest;
import com.jtd.engine.message.v1.AdwoBidResponse;
import com.jtd.engine.message.v1.HttpRequest;
import com.jtd.engine.message.v1.HttpResponse;
import com.jtd.engine.message.v1.XTraderBidResponse;
import com.jtd.engine.utils.SystemTime;

public class AdwoJsonFilter extends IoFilterAdapter{
	
	private static final Log log = LogFactory.getLog(AdwoJsonFilter.class);

	private static final String ADWO_BID_URI = "/adwo";
	
	/** 这个Filter在哪个本地地址上起作用  */
	private Set<InetSocketAddress> interceptAddresses = new HashSet<InetSocketAddress>();
	
	/** 系统时间工具 */
	private SystemTime systemTime;

	
	@Override
	public void messageReceived(NextFilter nextFilter, IoSession session,Object message) throws Exception 
	{
		if (interceptAddresses.contains(session.getLocalAddress())) {
			if(message instanceof HttpRequest){
				HttpRequest req = (HttpRequest)message;
				if(req.getMethod() == HttpRequest.POST && ADWO_BID_URI.equals(req.getUri())){
					//long pstart = System.currentTimeMillis();
					byte[] body = req.getBody();
					String json = new String(body, 0, body.length, "UTF-8");
					//logMyDebug.info(json);
					AdwoBidRequest msg = null;
					try{
						msg = JSON.parseObject(json, AdwoBidRequest.class);
					}catch(Exception ex){
						ex.printStackTrace();
					}
					nextFilter.messageReceived(session, msg);
//					int time=(int) (System.currentTimeMillis() - pstart);
//					if(time>=120){
//						logMyDebug.info(new Date(System.currentTimeMillis())+":"+time);
//					}
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
	public void filterWrite(NextFilter nextFilter, IoSession session,WriteRequest writeRequest) throws Exception {
		if (interceptAddresses.contains(session.getLocalAddress())) {
			Object message = writeRequest.getMessage();
			if(message instanceof AdwoBidResponse) {
				
				AdwoBidResponse rsp = (AdwoBidResponse) message;
				if(rsp.getSeatbid().size() > 0) {
					final HttpResponse response = new HttpResponse();
					byte[] body = JSON.toJSONString(message).getBytes("UTF-8");
					response.setStatus(200);
					response.setStatusText("OK");
					response.setHeaders(new String[] {
						"Server: AsmeServer",
						new StringBuilder("Date: ").append(systemTime.getGmt()).toString(),
						"Content-Type: application/json",
						new StringBuilder("Content-Length: ").append(body.length).toString(),
						"Connection: Keep-Alive"
					});
					response.setContent(body);
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
				} else {
					final HttpResponse response = new HttpResponse();
					response.setStatus(204);
					response.setStatusText("No Content");
					response.setHeaders(new String[] {
						"Server: AsmeServer",
						new StringBuilder("Date: ").append(systemTime.getGmt()).toString(),
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
	
	public void setInterceptAddresses(Set<InetSocketAddress> interceptAddresses) {
		for(InetSocketAddress addr : interceptAddresses) {
			this.interceptAddresses.add(addr);
			log.info(this.getClass().getSimpleName() + "拦截地址: " + addr);
		}
	}
	
	public void setSystemTime(SystemTime systemTime) {
		this.systemTime = systemTime;
	}
	
}
