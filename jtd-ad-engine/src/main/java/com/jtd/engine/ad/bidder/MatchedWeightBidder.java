package com.jtd.engine.ad.bidder;

import com.jtd.engine.ad.Session;
import com.jtd.engine.ad.em.MatchType;
import com.jtd.web.model.Ad;
import com.jtd.web.model.Campaign;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.jtd.engine.ad.em.MatchType.*;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月8日
 * @项目名称 dsp-engine
 * @描述 <p></p>
 */
public class MatchedWeightBidder extends com.jtd.engine.ad.bidder.AbstractBidder {

	private static final Map<MatchType, Float> WEIGHT_TABLE = new HashMap<MatchType, Float>();
	static {
		WEIGHT_TABLE.put(CITY, 0.05f);
		WEIGHT_TABLE.put(GENDER, 0.03f);
		WEIGHT_TABLE.put(AGE, 0.03f);
		WEIGHT_TABLE.put(MARRIAGE, 0.03f);
		WEIGHT_TABLE.put(PARENTING, 0.03f);
		WEIGHT_TABLE.put(JOBTYPE, 0.03f);
		WEIGHT_TABLE.put(JOBSTATUS, 0.03f);
		WEIGHT_TABLE.put(EDUCATION, 0.03f);
		WEIGHT_TABLE.put(FORTEST, 0.03f);
		WEIGHT_TABLE.put(HOME, 0.03f);
		WEIGHT_TABLE.put(INTEREST, 0.02f);
		WEIGHT_TABLE.put(COOKIEPACKET, 0.12f);
		WEIGHT_TABLE.put(RETARGET, 0.1f);
		WEIGHT_TABLE.put(IPSEG, 0.08f);
		WEIGHT_TABLE.put(PAGEURL, 0.05f);
		WEIGHT_TABLE.put(WEBCATG, 0.05f);
		WEIGHT_TABLE.put(APPCATG, 0.05f);
		WEIGHT_TABLE.put(APPPKNAME, 0.05f);
		WEIGHT_TABLE.put(DEVICETYPE, 0.02f);
		WEIGHT_TABLE.put(NETWORK, 0.03f);
		WEIGHT_TABLE.put(OPERATOR, 0.03f);
		WEIGHT_TABLE.put(OS, 0.02f);
		WEIGHT_TABLE.put(BRAND, 0.04f);
		WEIGHT_TABLE.put(FIRSTSCREEN, 0.08f);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dspx.engine.ad.bidder.AbstractBidder#doBid(com.dspx.engine.ad.Session,
	 * net.doddata.web.dsp.model.Campaign, net.doddata.web.dsp.model.Ad)
	 */
	@Override
	protected float doBid(Session session, Campaign camp, Ad ad) {
		Set<MatchType> matcheds = session.getMatched(camp.getId());
		if(matcheds == null) return 0f;
		float v = 0f;
		for (MatchType mt : matcheds) {
			v += WEIGHT_TABLE.get(mt);
		}
		return v;
	}
}
