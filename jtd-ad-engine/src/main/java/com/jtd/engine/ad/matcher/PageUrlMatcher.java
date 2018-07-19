package com.jtd.engine.ad.matcher;

import static com.jtd.engine.ad.em.MatchType.PAGEURL;

import java.util.HashSet;
import java.util.Set;

import com.jtd.engine.message.BaiduRealtimeBiddingV26;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.jtd.engine.ad.Session;
import com.jtd.engine.dao.BlackListDAO;
import com.jtd.engine.message.v1.XTraderBidRequest;
import com.jtd.engine.utils.HostUtil;
import com.jtd.web.model.Campaign;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月8日
 * @项目名称 dsp-engine
 * @描述 <p>匹配域名,并且设置域名的全局黑名单</p>
 */
public class PageUrlMatcher extends AbstractChannelCampMatcher {
	
//	private static final Log log = LogFactory.getLog(PageUrlMatcher.class);
	private final Logger logMyDebug = LogManager.getLogger("myDebugLog"); 
	private final Logger hzDebugLog = LogManager.getLogger("hzDebugLog");
	
	private BlackListDAO blackListDAO;
	
	
	
	@Override
	protected boolean matchADWO(Session session, Campaign camp) {
		return true;
	}
	@Override
	protected boolean matchXTRADER(Session session, Campaign camp) {
		
//		logMyDebug.info("xtrader:PageUrlMatcher--------------------------------------------");
		// APP流量不匹配页面URL
		if(session.isInApp()) return true;

		XTraderBidRequest req = session.getReq();
		
		String pagehost = HostUtil.getHost(req.getSite().getPage());
//		logMyDebug.info("66、零集广告位所在域名，pagehost>>xtrader:PageUrlMatcher>>"+pagehost);
		//如果获取不到请求的url地址，则直接通过
		if (StringUtils.isEmpty(pagehost)) return true;
		
		// 全局黑名单或者当前活动的黑名单
		if (matchGlobalBl(pagehost) || matchCampBl(pagehost, camp.getId())) return false;
		
		String urljson = getDim("pageurls", camp); 
//		logMyDebug.info("67、页面的域名，urljson"+">>xtrader:PageUrlMatcher>>"+urljson);
		// 没有按媒体域名来投
		if(StringUtils.isEmpty(urljson)) return true;
		Set<?> urls = JSON.parseObject(urljson, HashSet.class);
		if(matchUrl(pagehost, urls)) {
			session.addMatched(camp.getId(), PAGEURL);
			return true;
		}
		return false;
	}
	private boolean matchGlobalBl(String pagehost){
		Set<String> gbl = blackListDAO.getBlackList();
		return matchUrl(pagehost, gbl);
	}
	

	@Override
	protected boolean matchXHDT(Session session, Campaign camp) {
		return false;
	}
	private boolean matchCampBl(String pagehost, long campid) {
		Set<String> cbl = blackListDAO.getBlackList(campid);
		return matchUrl(pagehost, cbl);
	}

	
	@Override
	protected boolean matchAdView(Session session, Campaign camp) {
		return true;
	}

	@Override
	protected boolean matchAdBES(Session session, Campaign camp) {
				logMyDebug.info("xtrader:PageUrlMatcher--------------------------------------------");
		// APP流量不匹配页面URL
		if(session.isInApp()) return true;

		BaiduRealtimeBiddingV26.BidRequest  req = session.getReq();

		String pagehost = HostUtil.getHost(req.getUrl());
//		logMyDebug.info("66、零集广告位所在域名，pagehost>>xtrader:PageUrlMatcher>>"+pagehost);
		//如果获取不到请求的url地址，则直接通过
		if (StringUtils.isEmpty(pagehost)) return true;

		// 全局黑名单或者当前活动的黑名单
		if (matchGlobalBl(pagehost) || matchCampBl(pagehost, camp.getId())) return false;

		String urljson = getDim("pageurls", camp);
//		logMyDebug.info("67、页面的域名，urljson"+">>xtrader:PageUrlMatcher>>"+urljson);
		// 没有按媒体域名来投
		if(StringUtils.isEmpty(urljson)) return true;
		Set<?> urls = JSON.parseObject(urljson, HashSet.class);
		if(matchUrl(pagehost, urls)) {
			session.addMatched(camp.getId(), PAGEURL);
			return true;
		}
    return false;

	}
	/**
	 * @param blackListDAO the blackListDAO to set
	 */
	public void setBlackListDAO(BlackListDAO blackListDAO) {
		this.blackListDAO = blackListDAO;
	}

	/**
	 * 匹配URL
	 * @param
	 * @param urls
	 * @return
	 */
	private boolean matchUrl(String pagehost, Set<?> urls) {
		for (Object url : urls) {
			String lurl = url.toString().toLowerCase();
			if (pagehost.equalsIgnoreCase(lurl)) return true;
			if (!lurl.startsWith("www.") && pagehost.endsWith("." + lurl)) return true;
		}
		return false;
	}
}
