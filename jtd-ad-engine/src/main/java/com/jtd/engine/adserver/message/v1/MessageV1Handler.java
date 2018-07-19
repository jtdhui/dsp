package com.jtd.engine.adserver.message.v1;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.session.IoSession;

import com.alibaba.fastjson.JSON;
import com.jtd.engine.ad.AdService;
import com.jtd.engine.adserver.message.MessageHandler;
import com.jtd.engine.message.Message;
import com.jtd.engine.message.Message.Version;
import com.jtd.engine.message.v1.AVBidRequest;
import com.jtd.engine.message.v1.AdwoBidRequest;
import com.jtd.engine.message.v1.BesBidRequest;
import com.jtd.engine.message.v1.HttpResponse;
import com.jtd.engine.message.v1.SystemConfigRequest;
import com.jtd.engine.message.v1.SystemConfigResponse;
import com.jtd.engine.message.v1.XHDTBidRequest;
import com.jtd.engine.message.v1.XTraderBidRequest;
import com.jtd.engine.utils.SessionUtil;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月8日
 * @项目名称 dsp-engine
 * @描述 <p></p>
 */
public class MessageV1Handler implements MessageHandler {

	private static final Log log = LogFactory.getLog(MessageV1Handler.class);

	// 广告服务，为广告投放的主类
	private AdService adService;

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.maxit.datadriver.message.MessageHandler#handle(org.apache.mina.core.session.IoSession,
	 *      com.maxit.datadriver.message.Message)
	 */
	public void handle(IoSession session, Message message) {

		MessageV1 msg = (MessageV1) message;
		switch (msg.type()) {

		case XTraderBidRequest:
			session.write(adService.bidXTrader((XTraderBidRequest) message));
			break;
		case AVBidRequest:
			session.write(adService.bidAdView((AVBidRequest)message));
		case BesBidRequest:
			session.write(adService.bidBes((BesBidRequest)message));
			break;
		case SystemConfigRequest:
			
			SystemConfigRequest sysReq=(SystemConfigRequest) message;
			SystemConfigResponse sysRsp=new SystemConfigResponse();
			
			session.write(adService.sysConfig((SystemConfigRequest) message));
			
			break;
			
		case XHDTBidRequest:
			session.write(adService.bidXHDT((XHDTBidRequest)message));
			break;
		case AdwoBidRequest:
			session.write(adService.bidAdwo((AdwoBidRequest)message));
			
			
		case HttpRequest:
			log.info("request: " + JSON.toJSONString(message));
			HttpResponse response = new HttpResponse();
			response.setStatus(404);
			response.setStatusText("Not Found");
			String[] rspHeaders = new String[] { "Server: engine", "Content-Length: 0", "Connection: close" };
			response.setHeaders(rspHeaders);
			session.write(response).addListener(IoFutureListener.CLOSE);
			break;
		default:
			SessionUtil.blockSession(session, log, "未处理的消息类型" + msg.type().name());
			break;
		}
	}

	/**
	 * @param adService
	 *            the adService to set
	 */
	public void setAdService(AdService adService) {
		this.adService = adService;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.asme.adserver.message.MessageHandler#version()
	 */
	public Version version() {
		return Version.V1;
	}
}
