package com.jtd.engine.ad.matcher;


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
public class AdFieldMatcher extends AbstractChannelAdMatcher {

	
	@Override
	protected boolean matchADWO(Session session, Ad ad) {
		return true;
	}

	@Override
	protected boolean matchXTRADER(Session session, Ad ad) {
		return true;
	}

	@Override
	protected boolean matchXHDT(Session session, Ad ad) {
		System.out.println("adFiedlMatcher");
		return false;
	}

	@Override
	protected boolean matchAdView(Session sesssion, Ad ad) {
		return true;
	}

	@Override
	protected boolean matchAdBES(Session session, Ad ad) {
		return true;
	}
}
