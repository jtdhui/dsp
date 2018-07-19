package com.jtd.engine.ad.matcher;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.jtd.engine.ad.Session;
import com.jtd.engine.ad.em.Adx;
import com.jtd.engine.ad.em.MatchType;
import com.jtd.engine.cookie.CookieDataService;
import com.jtd.engine.dao.FreqDataDAO;
import com.jtd.engine.message.BaiduRealtimeBiddingV26;
import com.jtd.engine.message.BaiduRealtimeBiddingV26.BidRequest.BaiduId;
import com.jtd.engine.message.VamRealtimeBidding.VamRequest;
import com.jtd.engine.message.v1.HzBidRequest;
import com.jtd.engine.message.v1.XTraderBidRequest;
import com.jtd.web.model.Campaign;

import Tanx.TanxBidding.BidRequest;

 /**
  * @作者 Amos Xu
  * @版本 V1.0
  * @配置 
  * @创建日期 2016年9月8日
  * @项目名称 dsp-engine
  * @描述 <p></p>
  */
public class FreqMatcher extends AbstractChannelCampMatcher {
//	private static final Log log = LogFactory.getLog(FreqMatcher.class);
	private final Logger logMyDebug = LogManager.getLogger("myDebugLog");
	 private final Logger besDebugLog = LogManager.getLogger("besDebugLog");
	private FreqDataDAO freqDataDAO;
	private CookieDataService cookieDataService;

	

	@Override
	protected boolean matchADWO(Session session, Campaign camp) {
		return true;
	}

	 @Override
	 protected boolean matchAdBES(Session session, Campaign camp) {
		 besDebugLog.info("bes:FreqMatcher--------------------------------------------");

		 String fjson = getDim("freqandscreen", camp);
		 // 没有做频次控制
		 if(StringUtils.isEmpty(fjson)) return true;
		 FreqAndScreen freq = JSON.parseObject(fjson, FreqAndScreen.class);
		 BaiduRealtimeBiddingV26.BidRequest bidReq= session.getReq();
		 BaiduRealtimeBiddingV26.BidRequest.AdSlot adSlot =bidReq.getAdslotList().get(0);
		 int vs = 0;
		 if(adSlot!=null){
			 int pos=adSlot.getSlotVisibility();
			 if(pos==1){
				 vs=1;
			 }else if(pos!=1){
				 vs=2;
			 }
		 }
		besDebugLog.info("70、百度是首屏，vs>>bes:FreqMatcher>>"+vs);

		 // 先看首屏控制
		 boolean fsmatched = false;
		 int fs = freq.getFirsts();
		 besDebugLog.info("71、页面的是否首屏0不限、1首屏、2非首屏，freq.getFirsts()>>bes:FreqMatcher>>"+fs);
		 if(fs == 0) {// 不限制
			 fsmatched = true;
		 } else if(fs == 1) {// 首屏
			 // 0 其他屏 1 第一屏 2 第二屏
			 if (vs == 1) {
				 session.addMatched(camp.getId(), MatchType.FIRSTSCREEN);
				 fsmatched = true;
			 }
		 } else if(fs == 2) {// 非首屏
			 if (vs != 1) {
				 session.addMatched(camp.getId(), MatchType.FIRSTSCREEN);
				 fsmatched = true;
			 }
		 }

		 besDebugLog.info("72、是否首屏的匹配，fsmatched>>bes:FreqMatcher>>"+fsmatched);
		 if(!fsmatched) return false;

		 // 看是否有频次控制
		 String userid = null;
		 if(!session.isInApp()) {
			 String cid = getSessionCid(session);
			 if(cid == null) return true;
			 userid = cid;
		 } else {
			 // 频次控制时使用第一个
//			List<String> devid = (List<String>)session.getAttr("XTRADERDEVIDS");
//			if(devid == null) {
//				String cid = getSessionCid(session);
//				if(cid == null) return true;
//				userid = cid;
//			} else {
//				userid = devid.get(0);
//			}
			 //这里为了保险起见，先把userid设置成null
//			userid=null;
			 return true;
		 }

		 return checkFreq(userid, freq, camp.getId());

	 }


	@SuppressWarnings("unchecked")
	@Override
	protected boolean matchXTRADER(Session session, Campaign camp) {
//		logMyDebug.info("xtrader:FreqMatcher--------------------------------------------");
		
		String fjson = getDim("freqandscreen", camp);
		// 没有做频次控制
		if(StringUtils.isEmpty(fjson)) return true;

		FreqAndScreen freq = JSON.parseObject(fjson, FreqAndScreen.class);

		XTraderBidRequest req = session.getReq();
		XTraderBidRequest.Imp.Banner banner = req.getImp().get(0).getBanner();
		XTraderBidRequest.Imp.Video video = req.getImp().get(0).getVideo();
		int vs = 0;
		if(banner!=null){
			int pos=banner.getPos();
			if(pos==1){
				vs=1;
			}else if(pos!=1){
				vs=2;
			}
		}else{
			int pos=video.getPos();
			if(pos==1){
				vs=1;
			}else if(pos!=1){
				vs=2;
			}
		}
//		logMyDebug.info("70、零集是首屏，vs>>xtrader:FreqMatcher>>"+vs);
		
		// 先看首屏控制
		boolean fsmatched = false;
		int fs = freq.getFirsts();
//		logMyDebug.info("71、页面的是否首屏0不限、1首屏、2非首屏，freq.getFirsts()>>xtrader:FreqMatcher>>"+fs);
		if(fs == 0) {// 不限制
			fsmatched = true;
		} else if(fs == 1) {// 首屏
			// 0 其他屏 1 第一屏 2 第二屏
			if (vs == 1) {
				session.addMatched(camp.getId(), MatchType.FIRSTSCREEN);
				fsmatched = true;
			}
		} else if(fs == 2) {// 非首屏
			if (vs != 1) {
				session.addMatched(camp.getId(), MatchType.FIRSTSCREEN);
				fsmatched = true;
			}
		}
		
//		logMyDebug.info("72、是否首屏的匹配，fsmatched>>xtrader:FreqMatcher>>"+fsmatched);
		if(!fsmatched) return false;

		// 看是否有频次控制
		String userid = null;
		if(!session.isInApp()) {
			String cid = getSessionCid(session);
			if(cid == null) return true;
			userid = cid;
		} else {
			// 频次控制时使用第一个
//			List<String> devid = (List<String>)session.getAttr("XTRADERDEVIDS");
//			if(devid == null) {
//				String cid = getSessionCid(session);
//				if(cid == null) return true;
//				userid = cid;
//			} else {
//				userid = devid.get(0);
//			}
			//这里为了保险起见，先把userid设置成null
//			userid=null;
			return true;
		}

		return checkFreq(userid, freq, camp.getId());
	}
	

	@Override
	protected boolean matchXHDT(Session session, Campaign camp) {
		// TODO Auto-generated method stub
		return false;
	}

	

	@Override
	protected boolean matchAdView(Session session, Campaign camp) {
		// TODO Auto-generated method stub
		return true;
	}


	/**
	 * @param userid
	 * @param freq
	 * @param campid
	 * @return
	 */
	private boolean checkFreq(String userid, FreqAndScreen freq, long campid) {
		if(StringUtils.isEmpty(userid)) return true;
		int[] freqv = freq.freq;
		if(freqv == null) return true;
		//从缓存中取出userid对于活动campid,展示的信息
		Map<String, String> f = freqDataDAO.getFreq(userid, campid);
		if(f == null) return true;
		String pv = f.get("p");
		if(pv != null && Integer.parseInt(pv) >= freqv[1]) return false;
		String click = f.get("c");
		if(click != null && Integer.parseInt(click) >= freqv[2]) return false;
		return true;
	}

	/**
	 * @param session
	 * @return
	 */
	private String getSessionCid(Session session) {
		String cid = session.getCid();
		if (cid == null) {
			if(session.getAdx() == Adx.TANX) {
				BidRequest req = session.getReq();
				String tid = req.getTid();
				int tver = req.getTidVersion();
				cid = cookieDataService.getCidByTanxid(tid, tver);
				if (cid == null) cid = "";
				session.setCid(cid);
			} else if(session.getAdx() == Adx.BES){
				BaiduRealtimeBiddingV26.BidRequest bidReq = session.getReq();
				List<BaiduId> baiduids =  bidReq.getBaiduIdListList();
				String baiduid = null;
				for(BaiduId id: baiduids) {
					// 目前版本为2
					if(id.getBaiduUserIdVersion() == 2) {
						baiduid = id.getBaiduUserId();
					}
				}
				if(baiduid == null) {
					cid = "";
					session.setCid(cid);
				} else {
					cid = cookieDataService.getCidByBesid(baiduid, "2");
					if (cid == null) cid = "";
					session.setCid(cid);
				}
			} else if (session.getAdx() == Adx.VAM) {
				VamRequest req = session.getReq();
				String vid = req.getCookie();
				int ver = req.getCookieVersion();
				cid = cookieDataService.getCidByPartercid(String.valueOf(Adx.VAM.channelId()), vid + "_" + ver);
				if (cid == null) cid = "";
				session.setCid(cid);
			} else if (session.getAdx() == Adx.XTRADER){
				XTraderBidRequest req = session.getReq();
				String xid = req.getUser().getId();
				
				cid = cookieDataService.getCidByXtraderid(xid);
				if (cid == null) cid = "";
				session.setCid(cid);
			}else if(session.getAdx()==Adx.HZ){
				HzBidRequest req=session.getReq();
				HzBidRequest.User user=null;
				try{
					user=req.getUser();
				}catch(Exception ex){
					user=null;
				}
				
				if(user==null){
					cid = "";
					session.setCid(cid);
				}else{
					cid = cookieDataService.getCidByPartercid(String.valueOf(Adx.HZ.channelId()), user.getId());
					if (cid == null) cid = "";
					session.setCid(cid);
				}
				
			}else{
				cid = "";
				session.setCid(cid);
			}
		}
		return cid;
	}

	/**
	 * @param freqDataDAO the freqDataDAO to set
	 */
	public void setFreqDataDAO(FreqDataDAO freqDataDAO) {
		this.freqDataDAO = freqDataDAO;
	}

	/**
	 * @param cookieDataService the cookieDataService to set
	 */
	public void setCookieDataService(CookieDataService cookieDataService) {
		this.cookieDataService = cookieDataService;
	}

	public static class FreqAndScreen {
		private int[] freq;
		private int firsts;

		public int[] getFreq() {
			return freq;
		}

		public void setFreq(int[] freq) {
			this.freq = freq;
		}

		public int getFirsts() {
			return firsts;
		}

		public void setFirsts(int firsts) {
			this.firsts = firsts;
		}
	}
}
