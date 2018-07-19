package com.jtd.engine.ad.bidder;

import com.jtd.web.model.Ad;
import com.jtd.web.model.Campaign;
import com.jtd.engine.ad.Session;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月8日
 * @项目名称 dsp-engine
 * @描述 <p></p>
 */
public abstract class AbstractBidder implements com.jtd.engine.ad.bidder.Bidder {

	// 下一个bidder
	private com.jtd.engine.ad.bidder.Bidder nextBidder;

	/* (non-Javadoc)
	 * @see com.asme.ad.bidder.Bidder#bid(com.asme.ad.Session, net.doddata.web.dsp.model.Campaign, net.doddata.web.dsp.model.Ad)
	 */
	@Override
	public float bid(Session session, Campaign camp, Ad ad) {
		float ret = doBid(session, camp, ad);
		return nextBidder == null ? ret : ret + nextBidder.bid(session, camp, ad);
	}

	/**
	 * 执行匹配动作
	 * @param session
	 * @param camp
	 * @param ad
     * @return
     */
	protected abstract float doBid(Session session, Campaign camp, Ad ad);

	/**
	 * @return the nextBidder
	 */
	public com.jtd.engine.ad.bidder.Bidder getNextBidder() {
		return nextBidder;
	}

	/**
	 * @param nextBidder the nextBidder to set
	 */
	public void setNextBidder(com.jtd.engine.ad.bidder.Bidder nextBidder) {
		this.nextBidder = nextBidder;
	}
}
