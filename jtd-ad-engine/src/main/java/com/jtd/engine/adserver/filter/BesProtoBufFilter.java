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

import com.jtd.engine.message.BaiduRealtimeBiddingV26.BidRequest;
import com.jtd.engine.message.v1.BesBidRequest;
import com.jtd.engine.message.v1.BesBidResponse;
import com.jtd.engine.message.v1.HttpRequest;
import com.jtd.engine.message.v1.HttpResponse;
import com.jtd.engine.utils.SystemTime;


/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月24日
 * @项目名称 dsp-engine
 * @描述 百度adx 过滤器
 */
public class BesProtoBufFilter extends IoFilterAdapter {

//	private static final String BES_BID_URI = "/besb";
//	private static final String BES_BID_URI = "/";
    private static final String BES_BID_URI = "/bes";
	private static final Log log = LogFactory.getLog(BesProtoBufFilter.class);
	
	// 这个Filter在哪个本地地址上起作用
	private Set<InetSocketAddress> interceptAddresses = new HashSet<InetSocketAddress>();

	// 系统时间工具
	private SystemTime systemTime;

	/** ZL ：  MINA 过滤器链条  **/
	public void messageReceived(NextFilter nextFilter, IoSession session, Object message) throws Exception {
		log.info("进入百度过滤器");
		if (interceptAddresses.contains(session.getLocalAddress())) {

			if(message instanceof HttpRequest){
				HttpRequest req = (HttpRequest)message;
				log.info("百度过滤器请求方式:" +req.getMethod());
				log.info("百度过滤器url:"+req.getUri());
				if(req.getMethod() == HttpRequest.POST && BES_BID_URI.equals(req.getUri())){
					BesBidRequest msg = new BesBidRequest();
					msg.setBidReq(BidRequest.parseFrom(req.getBody()));
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
	public void filterWrite(NextFilter nextFilter, IoSession session,
			WriteRequest writeRequest) throws Exception {
		if (interceptAddresses.contains(session.getLocalAddress())) {
			Object message = writeRequest.getMessage();
			if(message instanceof BesBidResponse) {
				final HttpResponse response = new HttpResponse();
				byte[] body = ((BesBidResponse) message).getBidRsp().toByteArray();
				response.setStatus(200);
				response.setStatusText("OK");
				response.setHeaders(new String[] {
					"Server: AsmeServer",
					new StringBuilder("Date: ").append(systemTime.getGmt()).toString(),
					"Content-Type: application/octet-stream",
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
				nextFilter.filterWrite(session, writeRequest);
			}
		} else {
			nextFilter.filterWrite(session, writeRequest);
		}
	}

	/**
	 * @param interceptAddress
	 *            the interceptAddress to set
	 */
	public void setInterceptAddresses(List<InetSocketAddress> interceptAddresses) {
		for(InetSocketAddress addr : interceptAddresses) {
			this.interceptAddresses.add(addr);
			log.info(this.getClass().getSimpleName() + "拦截地址: " + addr);
		}
	}

	/**
	 * @param systemTime the systemTime to set
	 */
	public void setSystemTime(SystemTime systemTime) {
		this.systemTime = systemTime;
	}
}
