package com.jtd.engine.ad.matcher;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jtd.engine.ad.Session;
import com.jtd.web.model.Ad;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月8日
 * @项目名称 dsp-engine
 * @描述 <p></p>
 */
public class LandingPageMatcher extends AbstractChannelAdMatcher {
	
	private final Logger logMyDebug = LogManager.getLogger("myDebugLog");
	private static final Log log = LogFactory.getLog(LandingPageMatcher.class);

	private BesSettings besSettings;

	
	
	@Override
	protected boolean matchADWO(Session session, Ad ad) {
		return true;
	}


	@Override
	protected boolean matchXTRADER(Session session, Ad ad) {
//		logMyDebug.info("xtrader:LandingPageMatcher--------------------------------------------");
//		logMyDebug.info("78、零集没有黑名单匹配");
		//零集没有黑名单
		return true;
	}
	
	@Override
	protected boolean matchAdView(Session sesssion, Ad ad) {
		return true;
	}


	@Override
	protected boolean matchXHDT(Session session, Ad ad) {
		System.out.println("landingPageMatcher");
		return false;
	}



	/**
	 * @param besSettings the besSettings to set
	 */
	public void setBesSettings(BesSettings besSettings) {
		this.besSettings = besSettings;
	}


	@Override
	protected boolean matchAdBES(Session session, Ad ad) {
		return true;
	}
}
