package com.jtd.engine.ad.matcher;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jtd.engine.ad.Session;
import com.jtd.engine.ad.em.Adx;
import com.jtd.engine.dao.CampaignDAO;
import com.jtd.web.model.Campaign;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月8日
 * @项目名称 dsp-engine
 * @描述 <p></p>
 */
public class BidFloorMatcher extends AbstractCampMatcher {
	
//	private static final Log log = LogFactory.getLog(BidFloorMatcher.class);
	private final Logger xtarderDebugLog = LogManager.getLogger("xtarderDebugLog");
	private final Logger besDebugLog = LogManager.getLogger("besDebugLog");
	private final Logger hzDebugLog = LogManager.getLogger("hzDebugLog");
	
	private CampaignDAO campaignDAO;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.asme.ad.matcher.AbstractCampMatcher#doMatch(com.asme.ad.Session,
	 * net.doddata.web.dsp.model.Campaign)
	 */
	@Override
	protected boolean doMatch(Session session, Campaign camp) {
		//获得毛利率
		int gross = campaignDAO.getCampGrossProfit(camp.getPartnerId(), camp.getId());
		//获得最大的竞价价格；先取出用户输入的cpm价格，*（100-毛利）/100;
		//例如：查出的单价是 0.1 ,毛利是30则：0.1*（100-30）/100=0.07
		int maxBidPrice = (int) (campaignDAO.getCpmPrice(camp) * (float) (100 - gross) / 100.0f);
		xtarderDebugLog.info("43、maxBidPrice>>xtrader:BidFloorMatcher>>"+maxBidPrice+">>活动id>>"+camp.getId());
		xtarderDebugLog.info("44、session.getBidFloor()>>xtrader:BidFloorMatcher>>"+session.getBidFloor()+">>活动id>>"+camp.getId());
		xtarderDebugLog.info("45、maxBidPrice >= session.getBidFloor()>>xtrader:BidFloorMatcher>>"+(maxBidPrice >= session.getBidFloor())+">>活动id>>"+camp.getId());
		xtarderDebugLog.info("maxBidPrice >= session.getBidFloor()>>xtrader:"+(maxBidPrice >= session.getBidFloor())+">>活动id>>"+camp.getId());

		besDebugLog.info("maxBidPrice>>bes:BidFloorMatcher>>"+maxBidPrice+"==活动id>>"+camp.getId());
		besDebugLog.info("session.getBidFloor()>>bes:BidFloorMatcher>>"+session.getBidFloor()+"==活动id>>"+camp.getId());
		besDebugLog.info("maxBidPrice >= session.getBidFloor()>>bes:BidFloorMatcher>>"+(maxBidPrice >= session.getBidFloor())+"==活动id>>"+camp.getId());
		besDebugLog.info("maxBidPrice >= session.getBidFloor()>>bes:"+(maxBidPrice >= session.getBidFloor())+"==活动id>>"+camp.getId());

		hzDebugLog.info("007hz-价格,活动id=="+camp.getId());
		hzDebugLog.info("007hz-价格,活动id=="+camp.getId()+"session.getBidFloor()>>"+session.getBidFloor());
		hzDebugLog.info("007hz-价格,活动id=="+camp.getId()+"maxBidPrice >= session.getBidFloor()>>"+(maxBidPrice >= session.getBidFloor()));
		if (maxBidPrice >= session.getBidFloor()) {
			session.setMaxBidPrice(camp.getId(), maxBidPrice);
			return true;
		}
		return false;
	}

	/**;
	 * @param campaignDAO
	 *            the campaignDAO to set
	 */
	public void setCampaignDAO(CampaignDAO campaignDAO) {
		this.campaignDAO = campaignDAO;
	}
}
