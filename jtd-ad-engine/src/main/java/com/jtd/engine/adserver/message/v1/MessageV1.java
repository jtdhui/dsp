package com.jtd.engine.adserver.message.v1;

import com.jtd.engine.message.Message;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年10月20日
 * @项目名称 dsp-engine
 * @描述 <p></p>
 */
public abstract class MessageV1 extends Message {

	private static final long serialVersionUID = -530170725255723219L;

	/**
	 * 消息类型
	 * 
	 * @author ASME
	 * 
	 *         2010-6-10
	 */
	public enum Type {

		MSG000(0), // 控制端口心跳请求消息
		MSG001(1), // 控制端口心跳响应消息

		HttpRequest(-1), // HTTP端口请求消息
		HttpResponse(-2), // HTTP端口响应消息
		TanxBidRequest(-3), // TANX竞价请求消息
		TanxBidResponse(-4), // TANX竞价响应消息
		SaxBidRequest(-5), // SAX竞价请求消息
		SaxBidResponse(-6), // SAX竞价响应消息
		MMBidRequest(-7), // MediaMax竞价请求消息
		MMBidResponse(-8), // MediaMax竞价响应消息
		BesBidRequest(-9), // BES竞价请求消息
		BesBidResponse(-10), // BES竞价响应消息
		MZBidRequest(-11), // old秒针竞价请求消息，暂时不用
		MZBidResponse(-12), // old秒针竞价响应消息，暂时不用
		AVBidRequest(-13), // adview竞价请求消息
		AVBidResponse(-14), // adview竞价响应消息
		YMBidRequest(-15), // 有米竞价请求消息
		YMBidResponse(-16), // 有米竞价响应消息
		IMBidRequest(-17), // inmobi竞价请求消息
		IMBidResponse(-18), // inmobi竞价响应消息
		VamBidRequest(-19), // inmobi竞价请求消息
		VamBidResponse(-20), // inmobi竞价响应消息
		XTraderBidRequest(-21), //最新秒针请求封装对象
		XTraderBidResponse(-22), //最新秒针相应对象
		SystemConfigRequest(-23), //系统配置请求
		SystemConfigResponse(-24), //系统配置响应
		HzBidRequest(-25),	//互众广告请求
		HzBidResponse(-26), //互众广告响应
		
		
		XHDTBidRequest(-25),	/**新汇达通广告请求*/
		XHDTBidResponse(-26),   /**新汇达通广告广告响应*/
		
		AdwoBidRequest(-27),    /** 安沃广告请求*/
		AdwoBidResponse(-28),   /** 安沃广告响应*/
		
		SmaatoBidRequest(-29),  /** Smaato广告请求 */
		SmaatoBidResponse(-30); /** Smaato广告响应 */

		// 消息类型值
		private final int type;

		/**
		 * @param type
		 */
		private Type(int type) {
			this.type = type;
		}

		/**
		 * 返回消息类型值
		 * 
		 * @return
		 */
		public int value() {
			return type;
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.maxit.datadriver.message.Message#type()
	 */
	public abstract Type type();

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.maxit.datadriver.message.Message#version()
	 */
	public Version version() {
		return Version.V1;
	}
}
