package com.jtd.engine.message.v1;

import Tanx.TanxBidding.BidRequest;
import com.jtd.engine.adserver.message.v1.MessageV1;
/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月24日
 * @项目名称 dsp-engine
 * @描述
 */
public class TanxBidRequest extends MessageV1 {

	private static final long serialVersionUID = -565343167575307127L;

	private BidRequest bidReq;

	/**
	 * @return the bidReq
	 */
	public BidRequest getBidReq() {
		return bidReq;
	}

	/**
	 * @param bidReq the bidReq to set
	 */
	public void setBidReq(BidRequest bidReq) {
		this.bidReq = bidReq;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.maxit.adserver.message.v1.MessageV1#type()
	 */
	public Type type() {
		return Type.TanxBidRequest;
	}
}
