package com.jtd.engine.message.v1;

import Tanx.TanxBidding.BidResponse;
import com.jtd.engine.adserver.message.v1.MessageV1;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月24日
 * @项目名称 dsp-engine
 * @描述
 */
public class TanxBidResponse extends MessageV1 {

	private static final long serialVersionUID = 7111722535943207240L;

	private BidResponse bidRsp;

	/**
	 * @return the bidRsp
	 */
	public BidResponse getBidRsp() {
		return bidRsp;
	}

	/**
	 * @param bidRsp
	 *            the bidRsp to set
	 */
	public void setBidRsp(BidResponse bidRsp) {
		this.bidRsp = bidRsp;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.maxit.adserver.message.v1.MessageV1#type()
	 */
	public Type type() {
		return Type.TanxBidResponse;
	}
}
