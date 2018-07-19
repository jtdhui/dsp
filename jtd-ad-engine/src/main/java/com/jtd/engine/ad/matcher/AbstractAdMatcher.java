package com.jtd.engine.ad.matcher;

import com.jtd.web.model.Ad;
import com.jtd.engine.ad.Session;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月8日
 * @项目名称 dsp-engine
 * @描述 <p></p>
 */
public abstract class AbstractAdMatcher implements com.jtd.engine.ad.matcher.AdMatcher {
	
//	private static final Log log = LogFactory.getLog(AbstractAdMatcher.class);

	// 下一个匹配器
	private com.jtd.engine.ad.matcher.AdMatcher nextMatcher;

	/* (non-Javadoc)
	 * @see com.asme.ad.matcher.CampMatcher#match(com.asme.ad.Session, net.doddata.web.dsp.model.Campaign)
	 */
	@Override
	public boolean match(Session session, Ad ad) {
		if (doMatch(session, ad)) {
			if (nextMatcher != null) {
				return nextMatcher.match(session, ad);
			} else {
				return true;
			}
		} else {
//			log.info(ad.getCampId() + ": " + this.getClass());
			return false;
		}
	}

	/**
	 * 执行匹配动作
	 * @param ad
	 * @return
	 */
	protected abstract boolean doMatch(Session session, Ad ad);
	
	/**
	 * @param nextMatcher
	 *            the nextMatcher to set
	 */
	public void setNextMatcher(com.jtd.engine.ad.matcher.AdMatcher nextMatcher) {
		this.nextMatcher = nextMatcher;
	}

	/**
	 * @return the nextMatcher
	 */
	public com.jtd.engine.ad.matcher.AdMatcher getNextMatcher() {
		return nextMatcher;
	}
}
