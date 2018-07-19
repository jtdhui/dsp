package com.jtd.engine.ad.matcher;

import java.util.Map;

import com.jtd.engine.ad.Session;
import com.jtd.web.model.Ad;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月8日
 * @项目名称 dsp-engine
 * @描述 <p></p>
 */
public class AdCatgMatcher extends AbstractChannelAdMatcher {

	private static final Log log = LogFactory.getLog(AdCatgMatcher.class);
	
	private final Logger logMyDebug = LogManager.getLogger("myDebugLog"); 

	private com.jtd.engine.ad.matcher.BesSettings besSettings;
	
	@Override
	protected boolean matchADWO(Session session, Ad ad) 
	{
		/** 安沃没有广告类型 */
		System.out.println("ADWO AdCatgMatcher 空跑");
		return true;
	}



	@Override
	protected boolean matchXTRADER(Session session, Ad ad) {
		//零集没有广告类型，所以不需要匹配广告类型
		return true;
	}
	
	

	@Override
	protected boolean matchXHDT(Session session, Ad ad) {
		System.out.println("adCatgMatcher");
		return false;
	}

	
	@Override
	protected boolean matchAdView(Session sesssion, Ad ad) {
		
		//Map<String, String> adcatg = ad.getAdCategorys();
		//String vamAdcatg = adcatg.get("VAM");
		return true;
	}

	/**
	 * @param besSettings
	 *            the besSettings to set
	 */
	public void setBesSettings(com.jtd.engine.ad.matcher.BesSettings besSettings) {
		this.besSettings = besSettings;
	}



	@Override
	protected boolean matchAdBES(Session session, Ad ad) {
		// TODO Auto-generated method stub
		return true;
	}
}
