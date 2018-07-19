package com.jtd.engine.ad.matcher;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.jtd.engine.ad.Session;
import com.jtd.engine.ad.em.Adx;
import com.jtd.engine.ad.em.MatchType;
import com.jtd.engine.cookie.CookieDataService;
import com.jtd.engine.message.BaiduRealtimeBiddingV26;
import com.jtd.engine.message.BaiduRealtimeBiddingV26.BidRequest.BaiduId;
import com.jtd.engine.message.VamRealtimeBidding.VamRequest;
import com.jtd.engine.utils.IPUtil;
import com.jtd.web.constants.AdType;
import com.jtd.web.model.Campaign;

import Tanx.TanxBidding.BidRequest;

import static com.jtd.engine.ad.em.MatchType.*;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月8日
 * @项目名称 dsp-engine
 * @描述 <p></p>
 */
public class CookieAndIpMatcher extends AbstractChannelCampMatcher {

	private final Logger logMyDebug = LogManager.getLogger("myDebugLog"); 
	
	private CookieDataService cookieDataService;
	
	
	
	@Override
	protected boolean matchADWO(Session session, Campaign camp) {
		return true;
	}


	@Override
	protected boolean matchXTRADER(Session session, Campaign camp) {
		String ckjson = getDim("cookiegid", camp);

//		private Set<Long> i;// 兴趣
//		private Set<Long> p;// 人群包
//		private Set<Long> r;// 访客召回
		
		/**
		 ckjson，内容：
		 {
		 	"a":[3,4,5,6,7,8,9,10], 		//年龄段
		 	"e":[32,33,34,35,36],   		// 教育水平
		 	"g":[1,2],						// 性别
		 	"mt":"or",						// 人口属性和其他4项与或
		 	"h":[41,42,43,44],				// 居住阶段
		 	"jt":[27,28,29,30,31],			// 职业状态
		 	"job":[21,22,23,24,25,26],		// 职业类型
		 	"fr":[37,38,39,40],				// 备考阶段
		 	"m":[11,12,13,14,15],			// 婚姻状况
		 	"ca":[16,17,18,19,20],			// 育儿阶段
		 	"i":[51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73]			//兴趣定向
		 	"p":[734,755,756,757,758],      //人群表
		 	"r":[1144,1145,1146,1147]       //访客找回
		 	}
		 * */
		// 没有做人群定向
		if(StringUtils.isEmpty(ckjson)) return true;

		CookieGid cg = JSON.parseObject(ckjson, CookieGid.class);

//		// 人口属性和其他的可以是与或者或, 人口属性外其他的几项都是或
//		boolean matched = true;
//		if(!matchCookieGid4BES(session, cg.g, 2, GENDER, camp, true)) matched = false;
//		if(!matchCookieGid4BES(session, cg.a, 8, AGE, camp, true)) matched = false;
//		if(!matchCookieGid4BES(session, cg.m, 5, MARRIAGE, camp, true)) matched = false;
//		if(!matchCookieGid4BES(session, cg.ca, 5, PARENTING, camp, true)) matched = false;
//		if(!matchCookieGid4BES(session, cg.job, 6, JOBTYPE, camp, true)) matched = false;
//		if(!matchCookieGid4BES(session, cg.jt, 5, JOBSTATUS, camp, true)) matched = false;
//		if(!matchCookieGid4BES(session, cg.e, 5, EDUCATION, camp, true)) matched = false;
//		if(!matchCookieGid4BES(session, cg.fr, 4, FORTEST, camp, true)) matched = false;
//		if(!matchCookieGid4BES(session, cg.h, 4, HOME, camp, true)) matched = false;
//
//		boolean isor = "or".equalsIgnoreCase(cg.mt);
//		if(isor && matched) return true;
//		if(!isor && !matched) return false;
//
//		// 人口属性外其他的几项都是或
//		if (matchCookieGid4BES(session, cg.i, 0, INTEREST, camp, true)) return true;
		if (!matchCookieGidXTrader(session, cg.p, 0, COOKIEPACKET, camp, false)) return false;
		if (!matchCookieGidXTrader(session, cg.r, 0, RETARGET, camp, false)) return false;

		// IP段匹配
		if(!matchIp(session, cg.ip, IPSEG, camp)) return false;
		return true;
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
		String ckjson = getDim("cookiegid", camp);
//
////		private Set<Long> i;// 兴趣
////		private Set<Long> p;// 人群包
////		private Set<Long> r;// 访客召回
//
//		/**
//		 ckjson，内容：
//		 {
//		 "a":[3,4,5,6,7,8,9,10], 		//年龄段
//		 "e":[32,33,34,35,36],   		// 教育水平
//		 "g":[1,2],						// 性别
//		 "mt":"or",						// 人口属性和其他4项与或
//		 "h":[41,42,43,44],				// 居住阶段
//		 "jt":[27,28,29,30,31],			// 职业状态
//		 "job":[21,22,23,24,25,26],		// 职业类型
//		 "fr":[37,38,39,40],				// 备考阶段
//		 "m":[11,12,13,14,15],			// 婚姻状况
//		 "ca":[16,17,18,19,20],			// 育儿阶段
//		 "i":[51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73]			//兴趣定向
//		 "p":[734,755,756,757,758],      //人群表
//		 "r":[1144,1145,1146,1147]       //访客找回
//		 }
//		 * */
//		// 没有做人群定向
		if(StringUtils.isEmpty(ckjson)) return true;

		CookieGid cg = JSON.parseObject(ckjson, CookieGid.class);

//		// 人口属性和其他的可以是与或者或, 人口属性外其他的几项都是或
		boolean matched = true;

		// 性别
		if(!matchCookieGid4BES(session, cg.g, 2, GENDER, camp, true)) matched = false;
		// 年龄段
		if(!matchCookieGid4BES(session, cg.a, 8, AGE, camp, true)) matched = false;
		// 婚姻状况
		if(!matchCookieGid4BES(session, cg.m, 5, MARRIAGE, camp, true)) matched = false;
		// 育儿阶段
		if(!matchCookieGid4BES(session, cg.ca, 5, PARENTING, camp, true)) matched = false;
		// 职业类型
		if(!matchCookieGid4BES(session, cg.job, 6, JOBTYPE, camp, true)) matched = false;
		// 职业状态
		if(!matchCookieGid4BES(session, cg.jt, 5, JOBSTATUS, camp, true)) matched = false;
		// 教育水平
		if(!matchCookieGid4BES(session, cg.e, 5, EDUCATION, camp, true)) matched = false;
		// 备考阶段
		if(!matchCookieGid4BES(session, cg.fr, 4, FORTEST, camp, true)) matched = false;
		// 居住阶段
		if(!matchCookieGid4BES(session, cg.h, 4, HOME, camp, true)) matched = false;

		boolean isor = "or".equalsIgnoreCase(cg.mt);
		if(isor && matched) return true;
		if(!isor && !matched) return false;

		// 兴趣
		if (!matchCookieGid4BES(session, cg.i, 0, INTEREST, camp, true)) return true;
		// 人群包
		if (!matchCookieGid4BES(session, cg.p, 0, COOKIEPACKET, camp, false)) return false;
		//访客找回
		if (!matchCookieGid4BES(session, cg.r, 0, RETARGET, camp, false)) return false;
//		// IP段匹配
		if(!matchIp(session, cg.ip, IPSEG, camp)) return false;
//

		return true;
	}



	/**
	 * 匹配用户的cookie数据和选择的目标cookie
	 * @param session
	 * @param gids
	 * @param fullSelectedSize
	 * @param mt 确实匹配中时，打的匹配类型标记
	 * @return
	 */
	private boolean matchCookieGid4TANX(Session session, Set<Long> gids, int fullSelectedSize, MatchType mt, Campaign camp, boolean defaultValue) {
		if (gids == null || gids.size() == 0 || gids.size() == fullSelectedSize) {
			// 没选或者全部选的，算匹配上
			return true;
		} else {
			String[] userIds = getTankSessionUserId(session);
			if(StringUtils.isEmpty(userIds[0])) {

				if(!StringUtils.isEmpty(userIds[1])) {
					Map<Long, String[]> userdata = getCookieDataByUserId(session, userIds[1]);
					if(userdata.size() == 0) {
						// 没查到数据无法匹配的，默认算匹配上
						return defaultValue;
					} else {
						for(Long g : gids) {
							if(userdata.containsKey(g)) {
								session.addMatched(camp.getId(), mt);
								return true;
							}
						}
						return false;
					}
				}

				// 没查到用户ID无法匹配的，默认算匹配上
				return defaultValue;
			} else {
				Map<Long, String[]> userdata = getCookieDataByUserId(session, userIds[0]);
				
				if(userdata.size() == 0) {
					
					if(!StringUtils.isEmpty(userIds[1])) {
						userdata = getCookieDataByUserId(session, userIds[1]);
						if(userdata.size() == 0) {
							// 没查到数据无法匹配的，默认算匹配上
							return defaultValue;
						} else {
							for(Long g : gids) {
								if(userdata.containsKey(g)) {
									session.addMatched(camp.getId(), mt);
									return true;
								}
							}
							return false;
						}
					}
					
					// 没查到数据无法匹配的，默认算匹配上
					return defaultValue;
				} else {
					for(Long g : gids) {
						if(userdata.containsKey(g)) {
							session.addMatched(camp.getId(), mt);
							return true;
						}
					}
					return false;
				}
			}
		}
	}

	/**
	 * 匹配用户的cookie数据和选择的目标cookie
	 * @param session
	 * @param gids
	 * @param fullSelectedSize
	 * @param mt 确实匹配中时，打的匹配类型标记
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private boolean matchCookieGid4BES(Session session, Set<Long> gids, int fullSelectedSize, MatchType mt, Campaign camp, boolean defaultValue) {
		if (gids == null || gids.size() == 0 || gids.size() == fullSelectedSize) {
			// 没选或者全部选的，算匹配上
			return true;
		} else {
			BaiduRealtimeBiddingV26.BidRequest bidReq = session.getReq();
			List<String> devid = (List<String>)session.getAttr("BESDEVIDS");
			if(devid == null) {
				//获取百度的用户id
				List<BaiduId> baiduids =  bidReq.getBaiduIdListList();
				String baiduid = null;
				for(BaiduId id: baiduids) {
					// 目前版本为2
					if(id.getBaiduUserIdVersion() == 2) {
						baiduid = id.getBaiduUserId();
					}
				}
				//如果百度的用户id为null，则算是匹配上了，直接返回
				if(StringUtils.isEmpty(baiduid)) {
					// 没查到用户ID无法匹配的，默认算匹配上
					return defaultValue;
				} else {
					//从reids集群A中获取百度的用户数据
					Map<Long, String[]> userdata = getCookieDataByBesid(session, baiduid, "2");
					if(userdata == null || userdata.size() == 0) {
						//如果百度的用户数据为null则通过百度的用户id获取dsp的coolieid
						String cid = cookieDataService.getCidByBesid(baiduid, "2");
						//如果dsp的用户id不为null，则获取dsp的用户数据
						if(!StringUtils.isEmpty(cid)) {
							userdata = cookieDataService.getDataByUserId(cid);
						}
					}
					//没有匹配上用户，则算是默认匹配上了
					if(userdata.size() == 0) {
						// 没查到数据无法匹配的，默认算匹配上
						return defaultValue;
					} else {
						for(Long g : gids) {
							if(userdata.containsKey(g)) {
								session.addMatched(camp.getId(), mt);
								return true;
							}
						}
						return false;
					}
				}
			} else {
				//当deviceid不为空说明这次请求是移动端请求
				Map<Long, String[]> userdata = null;
				for(String did : devid) {
					userdata = cookieDataService.getDataByUserId(did);
					if(userdata != null) break;
				}
				if(userdata == null) {
					if(userdata == null) userdata = new HashMap<Long, String[]>();
					session.setAttr("userdata", userdata);
					// 没查到数据无法匹配的，默认算匹配上
					return defaultValue;
				}
				for(Long g : gids) {
					if(userdata.containsKey(g)) {
						session.addMatched(camp.getId(), mt);
						return true;
					}
				}
				return false;
			}
		}
	}
	
	/**
	 * 匹配用户的cookie数据和选择的目标cookie
	 * @param session
	 * @param gids
	 * @param fullSelectedSize
	 * @param mt 确实匹配中时，打的匹配类型标记
	 * @return
	 */
	private boolean matchCookieGid4VAM(Session session, Set<Long> gids, int fullSelectedSize, MatchType mt, Campaign camp, boolean defaultValue) {
		if (gids == null || gids.size() == 0 || gids.size() == fullSelectedSize) {
			// 没选或者全部选的，算匹配上
			return true;
		} else {
			String[] userIds = getVamSessionUserId(session);
			if(StringUtils.isEmpty(userIds[0])) {

				if(!StringUtils.isEmpty(userIds[1])) {
					Map<Long, String[]> userdata = getCookieDataByUserId(session, userIds[1]);
					if(userdata.size() == 0) {
						// 没查到数据无法匹配的，默认算匹配上
						return defaultValue;
					} else {
						for(Long g : gids) {
							if(userdata.containsKey(g)) {
								session.addMatched(camp.getId(), mt);
								return true;
							}
						}
						return false;
					}
				}

				// 没查到用户ID无法匹配的，默认算匹配上
				return defaultValue;
			} else {
				Map<Long, String[]> userdata = getCookieDataByUserId(session, userIds[0]);
				
				if(userdata.size() == 0) {
					
					if(!StringUtils.isEmpty(userIds[1])) {
						userdata = getCookieDataByUserId(session, userIds[1]);
						if(userdata.size() == 0) {
							// 没查到数据无法匹配的，默认算匹配上
							return defaultValue;
						} else {
							for(Long g : gids) {
								if(userdata.containsKey(g)) {
									session.addMatched(camp.getId(), mt);
									return true;
								}
							}
							return false;
						}
					}
					
					// 没查到数据无法匹配的，默认算匹配上
					return defaultValue;
				} else {
					for(Long g : gids) {
						if(userdata.containsKey(g)) {
							session.addMatched(camp.getId(), mt);
							return true;
						}
					}
					return false;
				}
			}
		}
	}

	
	/**
	 * 匹配用户的cookie数据和选择的目标cookie
	 * @param session
	 * @param gids
	 * @param fullSelectedSize
	 * @param mt 确实匹配中时，打的匹配类型标记
	 * @return
	 */
	private boolean matchCookieGidXTrader(Session session, Set<Long> gids, int fullSelectedSize, MatchType mt, Campaign camp, boolean defaultValue) {
		return true;
	}
	/**
	 * 匹配用户的cookie数据和选择的目标cookie
	 * @param session
	 * @param gids
	 * @param fullSelectedSize
	 * @param mt
	 * @param camp
	 * @param defaultValue
	 * @return
	 */
	private boolean matchCookieGidHz(Session session, Set<Long> gids, int fullSelectedSize, MatchType mt, Campaign camp, boolean defaultValue) {
		return true;
	}
	
	/**
	 * IP匹配
	 * @param session
	 * @param ips
	 * @param mt
	 * @return
	 */
	private boolean matchIp(Session session, List<String> ips, MatchType mt, Campaign camp) {
		String uip = session.getUserip();
		if(uip == null) return false;
		if(ips == null) return true;
		long lip = IPUtil.str2Ip(uip);
		for(String ipseg : ips) {
			if(ipseg.indexOf("-") != -1) {
				String[] ipse = ipseg.split("-");
				if(ipse.length != 2) continue;
				long t = IPUtil.str2Ip(ipse[0]);
				if(lip < t) continue;
				t = IPUtil.str2Ip(ipse[1]);
				if(lip > t) continue;
				session.addMatched(camp.getId(), mt);
				return true;
			} else if(ipseg.indexOf("/") != -1) {
				int t = ipseg.indexOf("/");
				long ip = IPUtil.str2Ip(ipseg.substring(0, ipseg.indexOf("/")));
				int ms = Integer.parseInt(ipseg.substring(t + 1));
				long mask = 0xFFFFFFFF;
				int len = 32 - ms;
				if (len < 0) continue;
				for (int i = 0; i < len; i++) {
					mask ^= 1l << i;
				}
				if ((ip & mask) == (lip & mask)) {
					session.addMatched(camp.getId(), mt);
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @param session
	 * @return
	 */
	private String[] getTankSessionUserId(Session session) {
		String userid = null;
		String tanxid = null;
		if(session.getAdType() == AdType.PC_BANNER) {
			BidRequest req = session.getReq();
			String tid = req.getTid();
			int tver = req.getTidVersion();
			String cid = session.getCid();
			if (cid == null) {
				cid = cookieDataService.getCidByTanxid(tid, tver);
				if (cid == null) cid = "";
				session.setCid(cid);
			}
			userid = cid;
			if(!StringUtils.isEmpty(tid)) tanxid = tid + "_" + tver;
		} else {
			String devid = (String)session.getAttr("TANXDEVID");
			if(devid == null) {
				BidRequest req = session.getReq();
				String cid = session.getCid();
				String tid = req.getTid();
				int tver = req.getTidVersion();
				if (cid == null) {
					cid = cookieDataService.getCidByTanxid(tid, tver);
					if (cid == null) cid = "";
					session.setCid(cid);
				}
				userid = cid;
				if(!StringUtils.isEmpty(tid)) tanxid = tid + "_" + tver;
			} else {
				userid = devid;
			}
		}
		return new String[]{userid, tanxid};
	}

	/**
	 * @param session
	 * @return
	 */
	private String[] getVamSessionUserId(Session session) {
		String userid = null;
		String vamid = null;
		if(session.getAdType() == AdType.PC_BANNER) {
			VamRequest req = session.getReq();
			String vid = req.getCookie();
			int ver = req.getCookieVersion();
			String cid = session.getCid();
			if (cid == null) {
				cid = cookieDataService.getCidByPartercid(String.valueOf(Adx.VAM.channelId()), vid + "_" + ver);
				if (cid == null) cid = "";
				session.setCid(cid);
			}
			userid = cid;
			if(!StringUtils.isEmpty(vid)) vamid = vid + "_" + ver;
		} else {
			String devid = session.getAttr("MD5IMEI");
			if(devid == null) {
				devid = session.getAttr("IDFA");
			}
			if(devid == null) {
				VamRequest req = session.getReq();
				String vid = req.getCookie();
				int ver = req.getCookieVersion();
				String cid = session.getCid();
				if (cid == null) {
					cid = cookieDataService.getCidByPartercid(String.valueOf(Adx.VAM.channelId()), vid + "_" + ver);
					if (cid == null) cid = "";
					session.setCid(cid);
				}
				userid = cid;
				if(!StringUtils.isEmpty(vid)) vamid = vid + "_" + ver;
			} else {
				userid = devid;
			}
		}
		return new String[]{userid, vamid};
	}

	@SuppressWarnings("unchecked")
	private Map<Long, String[]> getCookieDataByUserId(Session session, String userId) {
		Map<Long, String[]> data = (Map<Long, String[]>) session.getAttr("userdata");
		if(data != null) return data;
		data = cookieDataService.getDataByUserId(userId);
		if(data == null) data = new HashMap<Long, String[]>();
		session.setAttr("userdata", data);
		return data;
	}
	
	/**
	 * 获取百度的用户数据
	 * @param session
	 * @param besid
	 * @param besver
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Map<Long, String[]> getCookieDataByBesid(Session session, String besid, String besver) {
		Map<Long, String[]> data = (Map<Long, String[]>) session.getAttr("userdata");
		if(data != null) return data;
		//取出百度的用户数据
		data = cookieDataService.getDataByBesid(besid, besver);
		if(data == null) data = new HashMap<Long, String[]>();
		session.setAttr("userdata", data);
		return data;
	}
	
	@SuppressWarnings("unchecked")
	private Map<Long, String[]> getCookieDataByXtraderid(Session session, String xtraderid) {
		Map<Long, String[]> data = (Map<Long, String[]>) session.getAttr("userdata");
		if(data != null) return data;
		//取出百度的用户数据
		data = cookieDataService.getDataByUserId(xtraderid);
		if(data == null) data = new HashMap<Long, String[]>();
		session.setAttr("userdata", data);
		return data;
	}

	/**
	 * @param cookieDataService the cookieDataService to set
	 */
	public void setCookieDataService(CookieDataService cookieDataService) {
		this.cookieDataService = cookieDataService;
	}

	public static class CookieGid {

		private Set<Long> g;// 性别
		private Set<Long> a;// 年龄段
		private Set<Long> m;// 婚姻状况
		private Set<Long> ca;// 育儿阶段
		private Set<Long> job;// 职业类型
		private Set<Long> jt;// 职业状态
		private Set<Long> e;// 教育水平
		private Set<Long> fr;// 备考阶段
		private Set<Long> h;// 居住阶段

		private Set<Long> i;// 兴趣
		private Set<Long> p;// 人群包
		private Set<Long> r;// 访客召回
		private List<String> ip;
		private String mt; // 人口属性和其他4项与或

		/**
		 * @return the g
		 */
		public Set<Long> getG() {
			return g;
		}

		/**
		 * @param g
		 *            the g to set
		 */
		public void setG(Set<Long> g) {
			this.g = g;
		}

		/**
		 * @return the a
		 */
		public Set<Long> getA() {
			return a;
		}

		/**
		 * @param a
		 *            the a to set
		 */
		public void setA(Set<Long> a) {
			this.a = a;
		}

		/**
		 * @return the m
		 */
		public Set<Long> getM() {
			return m;
		}

		/**
		 * @param m
		 *            the m to set
		 */
		public void setM(Set<Long> m) {
			this.m = m;
		}

		/**
		 * @return the ca
		 */
		public Set<Long> getCa() {
			return ca;
		}

		/**
		 * @param ca
		 *            the ca to set
		 */
		public void setCa(Set<Long> ca) {
			this.ca = ca;
		}

		/**
		 * @return the job
		 */
		public Set<Long> getJob() {
			return job;
		}

		/**
		 * @param job
		 *            the job to set
		 */
		public void setJob(Set<Long> job) {
			this.job = job;
		}

		/**
		 * @return the jt
		 */
		public Set<Long> getJt() {
			return jt;
		}

		/**
		 * @param jt
		 *            the jt to set
		 */
		public void setJt(Set<Long> jt) {
			this.jt = jt;
		}

		/**
		 * @return the e
		 */
		public Set<Long> getE() {
			return e;
		}

		/**
		 * @param e
		 *            the e to set
		 */
		public void setE(Set<Long> e) {
			this.e = e;
		}

		/**
		 * @return the fr
		 */
		public Set<Long> getFr() {
			return fr;
		}

		/**
		 * @param fr
		 *            the fr to set
		 */
		public void setFr(Set<Long> fr) {
			this.fr = fr;
		}

		/**
		 * @return the h
		 */
		public Set<Long> getH() {
			return h;
		}

		/**
		 * @param h
		 *            the h to set
		 */
		public void setH(Set<Long> h) {
			this.h = h;
		}

		/**
		 * @return the i
		 */
		public Set<Long> getI() {
			return i;
		}

		/**
		 * @param i
		 *            the i to set
		 */
		public void setI(Set<Long> i) {
			this.i = i;
		}

		/**
		 * @return the p
		 */
		public Set<Long> getP() {
			return p;
		}

		/**
		 * @param p
		 *            the p to set
		 */
		public void setP(Set<Long> p) {
			this.p = p;
		}

		/**
		 * @return the r
		 */
		public Set<Long> getR() {
			return r;
		}

		/**
		 * @param r
		 *            the r to set
		 */
		public void setR(Set<Long> r) {
			this.r = r;
		}

		/**
		 * @return the ip
		 */
		public List<String> getIp() {
			return ip;
		}

		/**
		 * @param ip
		 *            the ip to set
		 */
		public void setIp(List<String> ip) {
			this.ip = ip;
		}

		/**
		 * @return the mt
		 */
		public String getMt() {
			return mt;
		}

		/**
		 * @param mt
		 *            the mt to set
		 */
		public void setMt(String mt) {
			this.mt = mt;
		}
	}
}
