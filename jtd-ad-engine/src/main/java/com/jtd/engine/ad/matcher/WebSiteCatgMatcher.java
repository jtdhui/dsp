package com.jtd.engine.ad.matcher;

import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.jtd.engine.ad.Session;
import com.jtd.engine.ad.em.MatchType;
import com.jtd.web.model.Campaign;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月8日
 * @项目名称 dsp-engine
 * @描述 <p></p>
 */
public class WebSiteCatgMatcher extends AbstractChannelCampMatcher {
	
//	private static final Log log = LogFactory.getLog(WebSiteCatgMatcher.class);
	private final Logger logMyDebug = LogManager.getLogger("myDebugLog");
	private final Logger besDebugLog = LogManager.getLogger("besDebugLog");

	
	
	@Override
	protected boolean matchADWO(Session session, Campaign camp) {
		logMyDebug.info("xtrader:WebSiteCatgMatcher--------------------------------------------");
		// APP流量不匹配网站类型,app的类型在AppMatcher中匹配
		if(session.isInApp()) return true;
		/**如果是pc端**/
		String webcatgjson = getDim("webcatg", camp);
		// 没有做网站类型定向
		if(StringUtils.isEmpty(webcatgjson)) return true;
		WebCatg catg = JSON.parseObject(webcatgjson, WebCatg.class);
		Set<String> adwoCatg = catg.getAdwo();
//		logMyDebug.info("49、catg.getXtrader()>>xtrader:媒体类型匹配--WebSiteCatgMatcher>>"+JSON.toJSONString(xtraderCatg));
		//如果从活动策略中取出的渠道网站类型没有或数量为0，说明在前端没有选，那么直接通过
		if(adwoCatg == null || adwoCatg.size() == 0) return true;
		//获取零集请求过来时，携带的发起这个请求的网站类型
		String webcatg = session.getWebCatg();
		if(webcatg==null||webcatg.equals("")){
			//如果竞价时，零集没有发送分类类型，则不做类型匹配
			return true;
		}
		//如果活动策略中选择的渠道网站类型集合中包括，发起这个请求的网站类型
//		logMyDebug.info("50、xtraderCatg.contains(webcatg)>>xtrader:媒体类型匹配--WebSiteCatgMatcher>>"+(xtraderCatg.contains(webcatg)));
		if(adwoCatg.contains(webcatg)){
			session.addMatched(camp.getId(), MatchType.WEBCATG);
			return true;
		}
		
		return false;
	}


	@Override
	protected boolean matchXTRADER(Session session, Campaign camp) {
		/**
		// 如果是零集的流量，那么无论是移动端还是pc端都要进行类型匹配j
		if(session.isInApp()){
		
			String appcatgjson = getDim("appcatg", camp);
			// 没有做网站类型定向
			if(StringUtils.isEmpty(appcatgjson)) return true;
			WebCatg catg = JSON.parseObject(appcatgjson, WebCatg.class);
			Set<String> xtraderCatg = catg.getXtrader();
			//如果从活动策略中取出的渠道网站类型没有或数量为0，说明在前端没有选，那么直接通过
			if(xtraderCatg == null || xtraderCatg.size() == 0) return true;
			//获取零集请求过来时，携带的发起这个请求的网站类型
			String appcatg = session.getAppCatg();
			if(appcatg==null||appcatg.equals("")){
				//如果竞价时，零集没有发送分类类型，则不做类型匹配
				return true;
			}
			//如果活动策略中选择的渠道网站类型集合中包括，发起这个请求的网站类型
			if(xtraderCatg.contains(appcatg)){
				session.addMatched(camp.getId(), MatchType.APPCATG);
				return true;
			}
		}else{
			
			String webcatgjson = getDim("webcatg", camp);
			// 没有做网站类型定向
			if(StringUtils.isEmpty(webcatgjson)) return true;
			WebCatg catg = JSON.parseObject(webcatgjson, WebCatg.class);
			Set<String> xtraderCatg = catg.getXtrader();
			//如果从活动策略中取出的渠道网站类型没有或数量为0，说明在前端没有选，那么直接通过
			if(xtraderCatg == null || xtraderCatg.size() == 0) return true;
			//获取零集请求过来时，携带的发起这个请求的网站类型
			String webcatg = session.getWebCatg();
			if(webcatg==null||webcatg.equals("")){
				//如果竞价时，零集没有发送分类类型，则不做类型匹配
				return true;
			}
			//如果活动策略中选择的渠道网站类型集合中包括，发起这个请求的网站类型
			if(xtraderCatg.contains(webcatg)){
				session.addMatched(camp.getId(), MatchType.WEBCATG);
				return true;
			}
		}
		**/
		
//		logMyDebug.info("xtrader:WebSiteCatgMatcher--------------------------------------------");
		// APP流量不匹配网站类型,app的类型在AppMatcher中匹配
		if(session.isInApp()) return true;
		/**如果是pc端**/
		String webcatgjson = getDim("webcatg", camp);
		// 没有做网站类型定向
		if(StringUtils.isEmpty(webcatgjson)) return true;
		WebCatg catg = JSON.parseObject(webcatgjson, WebCatg.class);
		Set<String> xtraderCatg = catg.getXtrader();
//		logMyDebug.info("49、catg.getXtrader()>>xtrader:媒体类型匹配--WebSiteCatgMatcher>>"+JSON.toJSONString(xtraderCatg));
		//如果从活动策略中取出的渠道网站类型没有或数量为0，说明在前端没有选，那么直接通过
		if(xtraderCatg == null || xtraderCatg.size() == 0) return true;
		//获取零集请求过来时，携带的发起这个请求的网站类型
		String webcatg = session.getWebCatg();
		if(webcatg==null||webcatg.equals("")){
			//如果竞价时，零集没有发送分类类型，则不做类型匹配
			return true;
		}
		//如果活动策略中选择的渠道网站类型集合中包括，发起这个请求的网站类型
//		logMyDebug.info("50、xtraderCatg.contains(webcatg)>>xtrader:媒体类型匹配--WebSiteCatgMatcher>>"+(xtraderCatg.contains(webcatg)));
		if(xtraderCatg.contains(webcatg)){
			session.addMatched(camp.getId(), MatchType.WEBCATG);
			return true;
		}
		
		return false;
	}


	@Override
	protected boolean matchXHDT(Session session, Campaign camp) {
		// TODO Auto-generated method stub
		return false;
	}

	
	@Override
	protected boolean matchAdView(Session session, Campaign camp) {
		return true;
	}

	@Override
	protected boolean matchAdBES(Session session, Campaign camp) {
		besDebugLog.info("bes:WebSiteCatgMatcher--------------------------------------------");
		 //APP流量不匹配网站类型,app的类型在AppMatcher中匹配
		if(session.isInApp()) return true;
		/**如果是pc端**/
		String webcatgjson = getDim("webcatg", camp);
		// 没有做网站类型定向
		if(StringUtils.isEmpty(webcatgjson)) return true;
		WebCatg catg = JSON.parseObject(webcatgjson, WebCatg.class);
		Set<String> besCatg = catg.getBes();
		besDebugLog.info("49、catg.getBes()>>bes:媒体类型匹配--WebSiteCatgMatcher>>"+JSON.toJSONString(besCatg));
		//如果从活动策略中取出的渠道网站类型没有或数量为0，说明在前端没有选，那么直接通过
		if(besCatg == null || besCatg.size() == 0) return true;
		//获取百度请求过来时，携带的发起这个请求的网站类型
		String webcatg = session.getWebCatg();
		if(webcatg==null||webcatg.equals("")){
			//如果竞价时，百度没有发送分类类型，则不做类型匹配
			return true;
		}
		//如果活动策略中选择的渠道网站类型集合中包括，发起这个请求的网站类型
		besDebugLog.info("50、besCatg.contains(webcatg)>>bes:媒体类型匹配--WebSiteCatgMatcher>>"+(besCatg.contains(webcatg)));
		if(besCatg.contains(webcatg)){
			session.addMatched(camp.getId(), MatchType.WEBCATG);
			return true;
		}
		return false;
	}




	public static class WebCatg {
		private Set<String> dsp;
		private Set<String> tanx;
		private Set<String> bes;
		private Set<String> vam;
		private Set<String> xtrader;
		private Set<String> hz;
		private Set<String> adwo;
		private Set<String> adView;
		
		public Set<String> getDsp() {
			return dsp;
		}
		public void setDsp(Set<String> dsp) {
			this.dsp = dsp;
		}
		public Set<String> getTanx() {
			return tanx;
		}
		public void setTanx(Set<String> tanx) {
			this.tanx = tanx;
		}
		public Set<String> getBes() {
			return bes;
		}
		public void setBes(Set<String> bes) {
			this.bes = bes;
		}
		public Set<String> getVam() {
			return vam;
		}
		public void setVam(Set<String> vam) {
			this.vam = vam;
		}
		public Set<String> getXtrader() {
			return xtrader;
		}
		public void setXtrader(Set<String> xtrader) {
			this.xtrader = xtrader;
		}
		public Set<String> getHz() {
			return hz;
		}
		public void setHz(Set<String> hz) {
			this.hz = hz;
		}
		public Set<String> getAdwo() {
			return adwo;
		}
		public void setAdwo(Set<String> adwo) {
			this.adwo = adwo;
		}
		public Set<String> getAdView() {
			return adView;
		}
		public void setAdView(Set<String> adView) {
			this.adView = adView;
		}
	}
}
