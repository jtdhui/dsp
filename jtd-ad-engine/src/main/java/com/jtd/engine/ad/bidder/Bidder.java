package com.jtd.engine.ad.bidder;


import com.jtd.engine.ad.Session;
import com.jtd.web.model.Ad;
import com.jtd.web.model.Campaign;

/**
 * @author asme 2015年12月25日 上午11:03:52
 *
 */
public interface Bidder {

	/**
	 * 调整出价系数, 不调整返回0
	 * 
	 * @param session
	 * @param camp
	 * @return
	 */
	public float bid(Session session, Campaign camp, Ad ad);
}
