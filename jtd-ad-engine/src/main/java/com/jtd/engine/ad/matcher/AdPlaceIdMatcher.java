package com.jtd.engine.ad.matcher;

import com.jtd.engine.ad.Session;
import com.jtd.web.model.Campaign;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月8日
 * @项目名称 dsp-engine
 * @描述 <p></p>
 */
public class AdPlaceIdMatcher extends AbstractChannelCampMatcher {

	@Override
	protected boolean matchADWO(Session session, Campaign camp) {
		return true;
	}

	@Override
	protected boolean matchXTRADER(Session session, Campaign camp) {
		return true;
	}

	@Override
	protected boolean matchXHDT(Session session, Campaign camp) {
		System.out.println("adPlaceMatcher");
		return false;
	}

	@Override
	protected boolean matchAdView(Session session, Campaign camp) {
		return true;
	}

	@Override
	protected boolean matchAdBES(Session session, Campaign camp) {
		System.out.println("bes:adPlaceldMatcher");
		return true;
	}
}
