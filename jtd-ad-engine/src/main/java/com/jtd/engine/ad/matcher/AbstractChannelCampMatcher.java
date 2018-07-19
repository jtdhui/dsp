package com.jtd.engine.ad.matcher;

import com.jtd.engine.ad.Session;
import com.jtd.web.model.Campaign;

/**
 * 广告活动match控制器
 * @author zl
 *
 */
public abstract class AbstractChannelCampMatcher extends AbstractCampMatcher {
	
	protected boolean doMatch(Session session, Campaign camp) 
	{
		switch (session.getAdx()) {
		case XTRADER:
			return matchXTRADER(session, camp);
		case XHDT:
			return matchXHDT(session, camp);
		case ADWO:
			return matchADWO(session, camp);
		case AdView:
			return matchAdView(session, camp);
			case BES:
				return matchAdBES(session, camp);

		default:
		}
		return false;
	}

	/** 零集  */
	protected abstract boolean matchXTRADER(Session session,Campaign camp);
	
	/** 匹配新汇达通广告 */
	protected abstract boolean matchXHDT(Session session,Campaign camp);
	
	/** 安沃 */
	protected abstract boolean matchADWO(Session session,Campaign camp);
	
	/** adView */
	protected abstract boolean matchAdView(Session session,Campaign camp);

	/** bes */
	protected abstract boolean matchAdBES(Session session,Campaign camp);
}
