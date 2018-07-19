package com.jtd.engine.ad.matcher;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jtd.engine.ad.Session;
import com.jtd.engine.ad.em.Adx;
import com.jtd.engine.dao.AuditInfoDAO;
import com.jtd.web.model.Ad;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月8日
 * @项目名称 dsp-engine
 * @描述 <p></p>
 */
public class AdStatusMatcher extends AbstractChannelAdMatcher {

	private final Logger logMyDebug = LogManager.getLogger("myDebugLog");
	private static final Log log = LogFactory.getLog(AdStatusMatcher.class);
	
	private AuditInfoDAO auditInfoDAO;

	/**
	 * @param auditInfoDAO
	 *            the auditInfoDAO to set
	 */
	public void setAuditInfoDAO(AuditInfoDAO auditInfoDAO) {
		this.auditInfoDAO = auditInfoDAO;
	}
	
	@Override
	protected boolean matchXTRADER(Session session, Ad ad) {
		
//		logMyDebug.info("xtrader:AdStatusMatcher--------------------------------------------");
//		logMyDebug.info("广告id:"+ad.getId());
//		logMyDebug.info("创意id:"+ad.getCreativeId());
//		logMyDebug.info("79、!auditInfoDAO.isRefusedAd(ad.getCreativeId())>>xtrader:AdStatusMatcher>>"+(!auditInfoDAO.isRefusedAd(ad.getCreativeId())));
//		logMyDebug.info("80、!auditInfoDAO.isRefusedAd(Adx.XTRADER, ad.getId())>>xtrader:AdStatusMatcher>>"+(!auditInfoDAO.isRefusedAd(Adx.XTRADER, ad.getId())));
		
		//return !auditInfoDAO.isRefusedAd(ad.getCreativeId());
		return !auditInfoDAO.isRefusedAd(ad.getCreativeId()) && !auditInfoDAO.isRefusedAd(Adx.XTRADER, ad.getId());
	}
	
	
	
	
	@Override
	protected boolean matchADWO(Session session, Ad ad) 
	{
		return !auditInfoDAO.isRefusedAd(ad.getCreativeId()) && !auditInfoDAO.isRefusedAd(Adx.ADWO, ad.getId());
	}

	@Override
	protected boolean matchXHDT(Session session, Ad ad) {
		System.out.println("adStatusMatcher");
		return false;
	}

	@Override
	protected boolean matchAdView(Session sesssion, Ad ad) 
	{
		return !auditInfoDAO.isRefusedAd(ad.getCreativeId()) && !auditInfoDAO.isRefusedAd(Adx.AdView, ad.getId());
	}

	@Override
	protected boolean matchAdBES(Session session, Ad ad) {
		return !auditInfoDAO.isRefusedAd(ad.getCreativeId()) && !auditInfoDAO.isRefusedAd(Adx.BES, ad.getId());
	}
	
}
