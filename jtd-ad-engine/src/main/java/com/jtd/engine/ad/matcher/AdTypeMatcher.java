package com.jtd.engine.ad.matcher;

import com.jtd.engine.ad.Session;
import com.jtd.web.constants.AdType;
import com.jtd.web.constants.CreativeType;
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
public class AdTypeMatcher extends AbstractChannelAdMatcher {
	
	private static final Log log = LogFactory.getLog(AdTypeMatcher.class);
	
	private final Logger logMyDebug = LogManager.getLogger("myDebugLog");
	private final Logger hzDebugLog = LogManager.getLogger("hzDebugLog");
	
	private com.jtd.engine.ad.matcher.BesSettings besSettings;

	
	@Override
	protected boolean matchXTRADER(Session session, Ad ad) {
		
//		logMyDebug.info("xtrader:AdTypeMatcher--------------------------------------------");
//		XTraderBidRequest bidReq = session.getReq();
//		XTraderBidRequest.Imp.Banner banner = bidReq.getImp().get(0).getBanner();
//		XTraderBidRequest.Imp.Video video = bidReq.getImp().get(0).getVideo();
		
		CreativeType ct = ad.getCreativeType();
		
//		logMyDebug.info("77、广告的类型ct---零集的类型>>xtrader:AdTypeMatcher>>"+ct.getTypeName()+"----"+session.getAdType().getDesc());
		
		if(session.getAdType()==AdType.APP_BANNER||session.getAdType()==AdType.APP_FULLSCREEN||session.getAdType()==AdType.WAP
				||session.getAdType()==AdType.PC_BANNER||session.getAdType()==AdType.PC_VIDEO_PAUSE
				||session.getAdType()==AdType.WAP_VIDEO_PAUSE||session.getAdType()==AdType.APP_VIDEO_PAUSE){
			if(ct==CreativeType.IMG){
				return true;
			}else{
				return false;
			}
		}else{
			if(ct==CreativeType.FLASH||ct==CreativeType.FLV||ct==CreativeType.MP4){
				return true;
			}else{
				return false;
			}
		}
			
	}
	
	
	@Override
	protected boolean matchADWO(Session session, Ad ad) 
	{
		CreativeType ct = ad.getCreativeType(); /** 物料类型：IMG(0, "图片"), FLASH(1, "Flash"), FLV(2, "Flv"), MP4(3, "MP4"); */
		
//		logMyDebug.info("77、广告的类型ct---零集的类型>>xtrader:AdTypeMatcher>>"+ct.getTypeName()+"----"+session.getAdType().getDesc());
		
		if(session.getAdType()==AdType.APP_BANNER||session.getAdType()==AdType.APP_FULLSCREEN||session.getAdType()==AdType.WAP
				||session.getAdType()==AdType.PC_BANNER||session.getAdType()==AdType.PC_VIDEO_PAUSE
				||session.getAdType()==AdType.WAP_VIDEO_PAUSE||session.getAdType()==AdType.APP_VIDEO_PAUSE){
			if(ct==CreativeType.IMG){
				return true;
			}else{
				return false;
			}
		}else{
			if(ct==CreativeType.FLASH||ct==CreativeType.FLV||ct==CreativeType.MP4){
				return true;
			}else{
				return false;
			}
		}
	}


	@Override
	protected boolean matchAdView(Session session, Ad ad) 
	{
		/** 物料类型：IMG(0, "图片"), FLASH(1, "Flash"), FLV(2, "Flv"), MP4(3, "MP4"); */
		CreativeType ct = ad.getCreativeType(); 
		
		if(session.getAdType()==AdType.APP_BANNER||session.getAdType()==AdType.APP_FULLSCREEN||session.getAdType()==AdType.WAP
				||session.getAdType()==AdType.PC_BANNER||session.getAdType()==AdType.PC_VIDEO_PAUSE
				||session.getAdType()==AdType.WAP_VIDEO_PAUSE||session.getAdType()==AdType.APP_VIDEO_PAUSE){
			if(ct==CreativeType.IMG){
				return true;
			}else{
				return false;
			}
		}else{
			if(ct==CreativeType.FLASH||ct==CreativeType.FLV||ct==CreativeType.MP4){
				return true;
			}else{
				return false;
			}
		}
	}


	@Override
	protected boolean matchXHDT(Session session, Ad ad) 
	{
		System.out.println("adTypeMatcher");
		return false;
	}




	/**
	 * @param besSettings the besSettings to set
	 */
	public void setBesSettings(com.jtd.engine.ad.matcher.BesSettings besSettings) {
		this.besSettings = besSettings;
	}


	@Override
	protected boolean matchAdBES(Session session, Ad ad) {
		/** 物料类型：IMG(0, "图片"), FLASH(1, "Flash"), FLV(2, "Flv"), MP4(3, "MP4"); */
		CreativeType ct = ad.getCreativeType(); 
		
		if(session.getAdType()==AdType.APP_BANNER||session.getAdType()==AdType.APP_FULLSCREEN||session.getAdType()==AdType.WAP
				||session.getAdType()==AdType.PC_BANNER||session.getAdType()==AdType.PC_VIDEO_PAUSE
				||session.getAdType()==AdType.WAP_VIDEO_PAUSE||session.getAdType()==AdType.APP_VIDEO_PAUSE){
			if(ct==CreativeType.IMG){
				return true;
			}else{
				return false;
			}
		}else{
			if(ct==CreativeType.FLASH||ct==CreativeType.FLV||ct==CreativeType.MP4){
				return true;
			}else{
				return false;
			}
		}
	}
}
