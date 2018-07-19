package com.jtd.engine.ad.matcher;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jtd.engine.ad.Session;
import com.jtd.web.model.Campaign;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月8日
 * @项目名称 dsp-engine
 * @描述 <p>广告展示类型匹配</p>
 */
public class DisplayTypeMatcher extends AbstractCampMatcher {
	
//	private static final Log log = LogFactory.getLog(DisplayTypeMatcher.class);
	private final Logger logMyDebug = LogManager.getLogger("myDebugLog");
	private final Logger besDebugLog = LogManager.getLogger("besDebugLog");
	/* (non-Javadoc)
	 * @see com.asme.ad.matcher.AbstractCampMatcher#doMatch(com.asme.ad.Session, net.doddata.web.dsp.model.Campaign)
	 */
	@Override
	protected boolean doMatch(Session session, Campaign camp) {
		besDebugLog.info("bes:DisplayTypeMatcher--------------------------------------------");
		besDebugLog.info("48、session.getAdType() == camp.getAdType()>>bes:广告展示类型匹配-DisplayTypeMatcher>>"+session.getAdType().getDesc()+"----"+camp.getAdType().getDesc()+">>活动id:"+camp.getId());
		//比较广告为可接收的广告类型和广告活动中广告类型是否一致
		return session.getAdType() == camp.getAdType();
	}
}
