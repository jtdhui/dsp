package com.jtd.engine.ad.matcher;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.jtd.engine.message.BaiduRealtimeBiddingV26;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.jtd.engine.ad.Session;
import com.jtd.engine.ad.em.Adx;
import com.jtd.engine.ad.em.MatchType;
import com.jtd.engine.message.v1.AVBidRequest;
import com.jtd.engine.message.v1.AdwoBidRequest;
import com.jtd.engine.message.v1.XTraderBidRequest;
import com.jtd.web.constants.AdType;
import com.jtd.web.model.Campaign;


/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月8日
 * @项目名称 dsp-engine
 * @描述 <p>app定向匹配</p>
 */
public class AppMatcher extends AbstractChannelCampMatcher {

//	private static final Log log = LogFactory.getLog(AppMatcher.class);
	private final Logger logMyDebug = LogManager.getLogger("myDebugLog"); 

	
	@Override
	protected boolean matchADWO(Session session, Campaign camp) 
	{
		logMyDebug.info("adwo:AppMatcher--------------------------------------------");
		/** PC流量直接返回  */
		if(session.getAdType() == AdType.PC_BANNER) return true;

		AdwoBidRequest req = session.getReq();
		AdwoBidRequest.App adwoApp = req.getApp();
		/** 如果安沃没有app对象说明不是app流量，则当前活动不匹配 */
		if(adwoApp==null){
			return true;
		}
		/** 获取广告主设置的APP类型  */
		String appcatgjson = getDim("appcatg", camp); 
		logMyDebug.info("60、页面的app媒体类型>>xtrader:AppMatcher>>"+appcatgjson);

		boolean matched = false;
		// 没有做app定向，则定向条件不起作用，直接返回true
		if(!StringUtils.isEmpty(appcatgjson)) {
			AppCatg catg = JSON.parseObject(appcatgjson, AppCatg.class);
			Set<String> pageCatg = catg.getXtrader();
//			logMyDebug.info("61、页面的app媒体类型>>xtrader:AppMatcher>>"+JSON.toJSONString(pageCatg));
			
			if(pageCatg != null && pageCatg.size() > 0) {
				List<String> adwoAppcatgs = new ArrayList<String>();//adwoApp.getCat();
//				logMyDebug.info("62、零集的app媒体类型>>xtrader:AppMatcher>>"+JSON.toJSONString(xtraderAppcatgs));
				//如果零集没有app分类数据，则该定向条件不起作用
				if(adwoAppcatgs==null||adwoAppcatgs.size()<=0){
					matched = true;
				}else{
					for(String appcatg : adwoAppcatgs){
						if(pageCatg.contains(appcatg)){
							matched = true;
							break;
						}
					}
					/*
					//如果常规方式没有匹配到
					if(!matched){
						StringBuffer tmpcatg=new StringBuffer();
						for(String appcatg : xtraderAppcatgs){
							//清空StringBuffer
							tmpcatg.delete(0, tmpcatg.length());
							//如果匹配到了，则推出for
							if(matched) break;
							
							try{
								tmpcatg.append(appcatg.split("-")[0]);
							}catch(Exception e){}
							if(tmpcatg.toString().length()<=0){
								continue;
							}
							Iterator<String> it = pageCatg.iterator();  
							while (it.hasNext()) {  
							  String str = it.next();
							  if(str.indexOf(tmpcatg.toString())>=0){
								  matched = true;
								  break;
							  } 
							} 
						}
					}
					*/
					//如果类型没有匹配上，则过滤掉当前活动
					if(!matched) return false;
				}
			}else{
				//如果页面没有发app类型,则直接匹配上当前活动，因为没有发就是不用匹配
				matched = true;
			}
		}
		
		
//		logMyDebug.info("63、matched>>xtrader:AppMatcher>>"+matched);
		
		session.addMatched(camp.getId(), MatchType.APPCATG);
		
		/*
		// 匹配应用包名
		String appjson = getDim("app", camp);
		logMyDebug.info("64、页面的应用包名>>xtrader:AppMatcher>>"+appjson);
		if(StringUtils.isEmpty(appjson)) return true;

		Set<?> apps = JSON.parseObject(appjson, HashSet.class);
		if(apps == null || apps.size() == 0) return true;

		String pkname = "";
		try{
			pkname=xtraderApp.getBundle();
		}catch(Exception ex){
			pkname = "";
		}
		logMyDebug.info("65、零集的应用包名>>xtrader:AppMatcher>>"+pkname);
		//如果零集发过来的包名为空，则该条件不起作用
		if(StringUtils.isEmpty(pkname)) return true;
		if(apps.contains(pkname.toLowerCase())) {
			session.addMatched(camp.getId(), MatchType.APPPKNAME);
			return true;
		}
		*/
		// 匹配应用包名
		Set<?> apps = null;
		String appjson = getDim("app", camp);
		if(!StringUtils.isEmpty(appjson)){
			apps = JSON.parseObject(appjson, HashSet.class);
		}
		if(apps != null && apps.size() > 0){
			String pkname = "";
			try{
				pkname=adwoApp.getBundle();
			}catch(Exception ex){
				pkname = "";
			}
			if(!StringUtils.isEmpty(pkname)&&apps.contains(pkname.toLowerCase())) {
				session.addMatched(camp.getId(), MatchType.APPPKNAME);
				return true;
			}
		}
		
		AdwoBidRequest.Imp adwoImp = null;
		try{
			adwoImp=req.getImp();
			if(adwoImp==null) return true;
		}catch(Exception ex){
			return true;
		}
		//应用id匹配
		String appIdJson = getDim("appid_"+Adx.ADWO.channelId(), camp);
		if(StringUtils.isEmpty(appjson)&&StringUtils.isEmpty(appIdJson)) return true;
		if(StringUtils.isEmpty(appIdJson)) return false;
		
		Set<?> appIds = JSON.parseObject(appIdJson, HashSet.class);
		if(appIds==null||appIds.size()<=0) return false;
		
		String appId=adwoImp.getTagid();
    	if(StringUtils.isEmpty(appId)) return false;
    	
    	if(appIds.contains(appId)){
    		session.addMatched(camp.getId(), MatchType.APPPKNAME);
			return true;
    	}
    	
		return false;
	}


	@Override
	protected boolean matchXTRADER(Session session, Campaign camp) {
//		logMyDebug.info("xtrader:AppMatcher--------------------------------------------");
		// PC流量
		if(session.getAdType() == AdType.PC_BANNER) return true;

		XTraderBidRequest req = session.getReq();
		XTraderBidRequest.App xtraderApp = req.getApp();
		//如果零集没有app对象说明不是app流量，则当前活动不匹配
		if(xtraderApp==null){
			return true;
		}
		
		String appcatgjson = getDim("appcatg", camp);
//		logMyDebug.info("60、页面的app媒体类型>>xtrader:AppMatcher>>"+appcatgjson);
		// 页面上发过来app类型
		boolean matched = false;
		// 没有做app定向，则定向条件不起作用，直接返回true
		if(!StringUtils.isEmpty(appcatgjson)) {
			AppCatg catg = JSON.parseObject(appcatgjson, AppCatg.class);
			Set<String> pageCatg = catg.getXtrader();
//			logMyDebug.info("61、页面的app媒体类型>>xtrader:AppMatcher>>"+JSON.toJSONString(pageCatg));
			
			if(pageCatg != null && pageCatg.size() > 0) {
				List<String> xtraderAppcatgs = xtraderApp.getCat();
//				logMyDebug.info("62、零集的app媒体类型>>xtrader:AppMatcher>>"+JSON.toJSONString(xtraderAppcatgs));
				//如果零集没有app分类数据，则该定向条件不起作用
				if(xtraderAppcatgs==null||xtraderAppcatgs.size()<=0){
					matched = true;
				}else{
					for(String appcatg : xtraderAppcatgs){
						if(pageCatg.contains(appcatg)){
							matched = true;
							break;
						}
					}
					/*
					//如果常规方式没有匹配到
					if(!matched){
						StringBuffer tmpcatg=new StringBuffer();
						for(String appcatg : xtraderAppcatgs){
							//清空StringBuffer
							tmpcatg.delete(0, tmpcatg.length());
							//如果匹配到了，则推出for
							if(matched) break;
							
							try{
								tmpcatg.append(appcatg.split("-")[0]);
							}catch(Exception e){}
							if(tmpcatg.toString().length()<=0){
								continue;
							}
							Iterator<String> it = pageCatg.iterator();  
							while (it.hasNext()) {  
							  String str = it.next();
							  if(str.indexOf(tmpcatg.toString())>=0){
								  matched = true;
								  break;
							  } 
							} 
						}
					}
					*/
					//如果类型没有匹配上，则过滤掉当前活动
					if(!matched) return false;
				}
			}else{
				//如果页面没有发app类型,则直接匹配上当前活动，因为没有发就是不用匹配
				matched = true;
			}
		}
		
		
//		logMyDebug.info("63、matched>>xtrader:AppMatcher>>"+matched);
		
		session.addMatched(camp.getId(), MatchType.APPCATG);
		
		/*
		// 匹配应用包名
		String appjson = getDim("app", camp);
		logMyDebug.info("64、页面的应用包名>>xtrader:AppMatcher>>"+appjson);
		if(StringUtils.isEmpty(appjson)) return true;

		Set<?> apps = JSON.parseObject(appjson, HashSet.class);
		if(apps == null || apps.size() == 0) return true;

		String pkname = "";
		try{
			pkname=xtraderApp.getBundle();
		}catch(Exception ex){
			pkname = "";
		}
		logMyDebug.info("65、零集的应用包名>>xtrader:AppMatcher>>"+pkname);
		//如果零集发过来的包名为空，则该条件不起作用
		if(StringUtils.isEmpty(pkname)) return true;
		if(apps.contains(pkname.toLowerCase())) {
			session.addMatched(camp.getId(), MatchType.APPPKNAME);
			return true;
		}
		*/
		// 匹配应用包名
		Set<?> apps = null;
		String appjson = getDim("app", camp);
		if(!StringUtils.isEmpty(appjson)){
			apps = JSON.parseObject(appjson, HashSet.class);
		}
		if(apps != null && apps.size() > 0){
			String pkname = "";
			try{
				pkname=xtraderApp.getBundle();
			}catch(Exception ex){
				pkname = "";
			}
			if(!StringUtils.isEmpty(pkname)&&apps.contains(pkname.toLowerCase())) {
				session.addMatched(camp.getId(), MatchType.APPPKNAME);
				return true;
			}
		}
		
		XTraderBidRequest.Imp xtraderImp = null;
		try{
			xtraderImp=req.getImp().get(0);
			if(xtraderImp==null) return true;
		}catch(Exception ex){
			return true;
		}
		//应用id匹配
		String appIdJson = getDim("appid_"+Adx.XTRADER.channelId(), camp);
		if(StringUtils.isEmpty(appjson)&&StringUtils.isEmpty(appIdJson)) return true;
		if(StringUtils.isEmpty(appIdJson)) return false;
		
		Set<?> appIds = JSON.parseObject(appIdJson, HashSet.class);
		if(appIds==null||appIds.size()<=0) return false;
		
		String appId=xtraderImp.getTagid();
    	if(StringUtils.isEmpty(appId)) return false;
    	
    	if(appIds.contains(appId)){
    		session.addMatched(camp.getId(), MatchType.APPPKNAME);
			return true;
    	}
    	
		return false;
	}

	
	@Override
	protected boolean matchAdView(Session session, Campaign camp) 
	{
		logMyDebug.info("adView:AppMatcher--------------------------------------------");
		// PC流量
		if(session.getAdType() == AdType.PC_BANNER) return true;

		AVBidRequest req = session.getReq();
		AVBidRequest.App avApp = req.getApp();
		//如果adView没有app对象说明不是app流量，则当前活动不匹配
		if(avApp == null){
			return true;
		}
		
		String appcatgjson = getDim("appcatg", camp);
//		logMyDebug.info("60、页面的app媒体类型>>xtrader:AppMatcher>>"+appcatgjson);
		// 页面上发过来app类型
		boolean matched = false;
		// 没有做app定向，则定向条件不起作用，直接返回true
		if(!StringUtils.isEmpty(appcatgjson)) {
			AppCatg catg = JSON.parseObject(appcatgjson, AppCatg.class);
			Set<String> pageCatg = catg.getAdView();
//			logMyDebug.info("61、页面的app媒体类型>>xtrader:AppMatcher>>"+JSON.toJSONString(pageCatg));
			
			if(pageCatg != null && pageCatg.size() > 0) {
				List<Integer> avAppcatgs = avApp.getCat();
//				logMyDebug.info("62、零集的app媒体类型>>xtrader:AppMatcher>>"+JSON.toJSONString(xtraderAppcatgs));
				//如果零集没有app分类数据，则该定向条件不起作用
				if(avAppcatgs==null||avAppcatgs.size()<=0){
					matched = true;
				}else{
					for(Integer appcatg : avAppcatgs){
						if(pageCatg.contains(String.valueOf(appcatg))){
							matched = true;
							break;
						}
					}
					/*
					//如果常规方式没有匹配到
					if(!matched){
						StringBuffer tmpcatg=new StringBuffer();
						for(String appcatg : xtraderAppcatgs){
							//清空StringBuffer
							tmpcatg.delete(0, tmpcatg.length());
							//如果匹配到了，则推出for
							if(matched) break;
							
							try{
								tmpcatg.append(appcatg.split("-")[0]);
							}catch(Exception e){}
							if(tmpcatg.toString().length()<=0){
								continue;
							}
							Iterator<String> it = pageCatg.iterator();  
							while (it.hasNext()) {  
							  String str = it.next();
							  if(str.indexOf(tmpcatg.toString())>=0){
								  matched = true;
								  break;
							  } 
							} 
						}
					}
					*/
					//如果类型没有匹配上，则过滤掉当前活动
					if(!matched) return false;
				}
			}else{
				//如果页面没有发app类型,则直接匹配上当前活动，因为没有发就是不用匹配
				matched = true;
			}
		}
		
		
//		logMyDebug.info("63、matched>>xtrader:AppMatcher>>"+matched);
		
		session.addMatched(camp.getId(), MatchType.APPCATG);
		
		/*
		// 匹配应用包名
		String appjson = getDim("app", camp);
		logMyDebug.info("64、页面的应用包名>>xtrader:AppMatcher>>"+appjson);
		if(StringUtils.isEmpty(appjson)) return true;

		Set<?> apps = JSON.parseObject(appjson, HashSet.class);
		if(apps == null || apps.size() == 0) return true;

		String pkname = "";
		try{
			pkname=xtraderApp.getBundle();
		}catch(Exception ex){
			pkname = "";
		}
		logMyDebug.info("65、零集的应用包名>>xtrader:AppMatcher>>"+pkname);
		//如果零集发过来的包名为空，则该条件不起作用
		if(StringUtils.isEmpty(pkname)) return true;
		if(apps.contains(pkname.toLowerCase())) {
			session.addMatched(camp.getId(), MatchType.APPPKNAME);
			return true;
		}
		*/
		// 匹配应用包名
		Set<?> apps = null;
		String appjson = getDim("app", camp);
		if(!StringUtils.isEmpty(appjson)){
			apps = JSON.parseObject(appjson, HashSet.class);
		}
		if(apps != null && apps.size() > 0){
			String pkname = "";
			try{
				pkname=avApp.getBundle();
			}catch(Exception ex){
				pkname = "";
			}
			if(!StringUtils.isEmpty(pkname)&&apps.contains(pkname.toLowerCase())) {
				session.addMatched(camp.getId(), MatchType.APPPKNAME);
				return true;
			}
		}
		
		AVBidRequest.Imp avImp = null;
		try{
			avImp=req.getImp().get(0);
			if(avImp==null) return true;
		}catch(Exception ex){
			return true;
		}
		//应用id匹配
		String appIdJson = getDim("appid_"+Adx.AdView.channelId(), camp);
		if(StringUtils.isEmpty(appjson)&&StringUtils.isEmpty(appIdJson)) return true;
		if(StringUtils.isEmpty(appIdJson)) return false;
		
		Set<?> appIds = JSON.parseObject(appIdJson, HashSet.class);
		if(appIds==null||appIds.size()<=0) return false;
		
		String appId=avImp.getTagid();
    	if(StringUtils.isEmpty(appId)) return false;
    	
    	if(appIds.contains(appId)){
    		session.addMatched(camp.getId(), MatchType.APPPKNAME);
			return true;
    	}
    	
		return false;
	}
	@Override
	protected boolean matchAdBES(Session session, Campaign camp)
	{
		logMyDebug.info("bes:AppMatcher--------------------------------------------");
		// PC流量
		if(session.getAdType() == AdType.PC_BANNER) return true;

		BaiduRealtimeBiddingV26.BidRequest req= session.getReq();
		BaiduRealtimeBiddingV26.BidRequest.Mobile mobile=req.getMobile();
		BaiduRealtimeBiddingV26.BidRequest.Mobile.MobileApp mobileApp=mobile.getMobileApp();
		//如果bes没有app对象说明不是app流量，则当前活动不匹配
		if(mobile == null){
			return true;
		}

		String appcatgjson = getDim("appcatg", camp);
//		logMyDebug.info("60、页面的app媒体类型>>xtrader:AppMatcher>>"+appcatgjson);
		// 页面上发过来app类型
		boolean matched = false;
		// 没有做app定向，则定向条件不起作用，直接返回true
		if(!StringUtils.isEmpty(appcatgjson)) {
			AppCatg catg = JSON.parseObject(appcatgjson, AppCatg.class);
			Set<String> pageCatg = catg.getBes();
//			logMyDebug.info("61、页面的app媒体类型>>xtrader:AppMatcher>>"+JSON.toJSONString(pageCatg));

			if(pageCatg != null && pageCatg.size() > 0) {
				int catId= mobileApp.getAppCategory();
//				logMyDebug.info("62、bes的app媒体类型>>bes:AppMatcher>>"+JSON.toJSONString(besAppcatgs));
				//如果bes没有app分类数据，则该定向条件不起作用

						if(pageCatg.contains(String.valueOf(catId))){
							matched = true;
						}

					/*
					//如果常规方式没有匹配到
					if(!matched){
						StringBuffer tmpcatg=new StringBuffer();
						for(String appcatg : xtraderAppcatgs){
							//清空StringBuffer
							tmpcatg.delete(0, tmpcatg.length());
							//如果匹配到了，则推出for
							if(matched) break;

							try{
								tmpcatg.append(appcatg.split("-")[0]);
							}catch(Exception e){}
							if(tmpcatg.toString().length()<=0){
								continue;
							}
							Iterator<String> it = pageCatg.iterator();
							while (it.hasNext()) {
							  String str = it.next();
							  if(str.indexOf(tmpcatg.toString())>=0){
								  matched = true;
								  break;
							  }
							}
						}
					}
					*/
					//如果类型没有匹配上，则过滤掉当前活动
					if(!matched) return false;
				}
			}else{
				//如果页面没有发app类型,则直接匹配上当前活动，因为没有发就是不用匹配
				matched = true;
			}



//		logMyDebug.info("63、matched>>xtrader:AppMatcher>>"+matched);

		session.addMatched(camp.getId(), MatchType.APPCATG);

		// 匹配应用包名
		Set<?> apps = null;
		String appjson = getDim("app", camp);
		if(!StringUtils.isEmpty(appjson)){
			apps = JSON.parseObject(appjson, HashSet.class);
		}
		if(apps != null && apps.size() > 0){
			String pkname = "";
			try{
				pkname=mobileApp.getAppBundleId();
			}catch(Exception ex){
				pkname = "";
			}
			if(!StringUtils.isEmpty(pkname)&&apps.contains(pkname.toLowerCase())) {
				session.addMatched(camp.getId(), MatchType.APPPKNAME);
				return true;
			}
		}

		//应用id匹配
//		String appIdJson = getDim("appid_"+Adx.BES.channelId(), camp);
//		if(StringUtils.isEmpty(appjson)&&StringUtils.isEmpty(appIdJson)) return true;
//		if(StringUtils.isEmpty(appIdJson)) return false;
//
//		Set<?> appIds = JSON.parseObject(appIdJson, HashSet.class);
//		if(appIds==null||appIds.size()<=0) return false;
//
//		String appId=mobileApp.getAppId();
//		if(StringUtils.isEmpty(appId)) return false;
//
//		if(appIds.contains(appId)){
//			session.addMatched(camp.getId(), MatchType.APPPKNAME);
//			return true;
//		}

		return false;
	}


	@Override
	protected boolean matchXHDT(Session session, Campaign camp) {
		System.out.println("apMatchers");
		return false;
	}



	public static class AppCatg {
		private Set<String> dsp;
		private Set<String> tanx;
		private Set<String> bes;
		private Set<String> vam;
		private Set<String> xtrader;
		private Set<String> hz;
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
		public Set<String> getAdView() {
			return adView;
		}
		public void setAdView(Set<String> adView) {
			this.adView = adView;
		}
	}
	
	
	public static void main(String[] args) {
		String tmp="[{\"id\":13634,\"app_id\":\"fff73141\",\"pk_name\":\"\",\"app_name\":\"掌上学英语\",\"channel\":2},{\"id\":13633,\"app_id\":\"fff587be\",\"pk_name\":\"\",\"app_name\":\"极品家丁\",\"channel\":2},{\"id\":6415,\"app_id\":\"181080\",\"pk_name\":\"\",\"channel\":8,\"app_name\":\"插屏-神庙逃亡2-Android\"}]";
		Set<?> apps = JSON.parseObject(tmp, HashSet.class);
		System.out.println(apps.contains("fff73141"));
		if(apps.contains("181080")){
			System.out.println(apps.contains("181080"));
		}
		
		
	}
}
