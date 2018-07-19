package com.jtd.engine.ad.matcher;

import com.jtd.engine.ad.Session;
import com.jtd.web.model.Ad;


/**
 * 广告创意macth控制器
 * @author zl
 *
 */
public abstract class AbstractChannelAdMatcher extends com.jtd.engine.ad.matcher.AbstractAdMatcher {
	
	protected boolean doMatch(Session session, Ad ad) {
		switch (session.getAdx()) {
		case XTRADER:
			return matchXTRADER(session,ad);
		case XHDT:
			return matchXHDT(session,ad);  /** 新汇达通match匹配创意*/
		case ADWO:
			return matchADWO(session, ad); /** 安沃match匹配创意 */
		case AdView:
			return matchAdView(session,ad); /** adView match匹配创意 */ 
		case BES:
			return matchAdBES(session,ad); /** bes match匹配创意 */ 
		default:
		}
		return false;
	}

	protected abstract boolean matchXTRADER(Session session, Ad ad);
	
	/** 新汇达通match匹配创意  */
	protected abstract boolean matchXHDT(Session session,Ad ad);
	
	/** 安沃match匹配创意 */
	protected abstract boolean matchADWO(Session session,Ad ad);
	
	/** adView match匹配创意 */
	protected abstract boolean matchAdView(Session session,Ad ad);
	
	/** adView match匹配创意 */
	protected abstract boolean matchAdBES(Session session,Ad ad);
}
