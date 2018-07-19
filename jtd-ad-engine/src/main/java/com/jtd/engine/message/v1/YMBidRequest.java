package com.jtd.engine.message.v1;

import com.jtd.engine.message.YoumiAdxApi.BidRequest;
import com.jtd.engine.adserver.message.v1.MessageV1;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月8日
 * @项目名称 dsp-engine
 * @描述 <p></p>
 */
public class YMBidRequest extends MessageV1 {

	private static final long serialVersionUID = 2629665476986273948L;

	private BidRequest bidReq;

	/**
	 * @return the bidReq
	 */
	public BidRequest getBidReq() {
		return bidReq;
	}

	/**
	 * @param bidReq
	 *            the bidReq to set
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
		return Type.YMBidRequest;
	}
}
