package com.jtd.engine.ad;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.jtd.engine.ad.em.Adx;
import com.jtd.engine.ad.em.MatchType;
import com.jtd.web.constants.AdType;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月8日
 * @项目名称 dsp-engine
 * @描述 <p></p>
 */
public class Session {

	// 渠道ID
	private Adx adx;

	/** 竞价请求ID */
	private String bid;

	// 分/CPM的竞价底价
	private int bidFloor;

	// 分/CPM的竞价出价
	private int bidPrice;

	// 广告类型
	private AdType adType;

	// 是否是来自APP的流量
	private boolean inApp;

	// 页面和APP包名
	private String pageReferer; // TANX没有这个字段，BES和万流客有
	private String pageUrl;
	private String webCatg; // 万流客的是个JSON格式的int数组
	private String appName;
	private String appPackageName;
	private String appCatg;

	// 是否进行cookiemapping
	private boolean isCookieMapping;
	
	// 当前用户的IP
	private String userip;

	// DSP的用户cookieId
	private String cid;

	// 竞价请求
	private Object req;
	
	// appid
	private String appId;
	
	//广告为id
	private String adp;

	// 记录匹配过程中匹配中的维度，只有强匹配中的才会记录
	private Map<Long, Set<MatchType>> matched = new HashMap<Long, Set<MatchType>>();

	// BidFloorMatcher计算出来的最大bidprice
	private Map<Long, Integer> maxBidPrice = new HashMap<Long, Integer>();

	// 属性
	private Map<String, Object> attr = new HashMap<String, Object>();

	/**
	 * @return the adx
	 */
	public Adx getAdx() {
		return adx;
	}

	/**
	 * @param adx
	 *            the adx to set
	 */
	public void setAdx(Adx adx) {
		this.adx = adx;
	}

	/**
	 * @return the bid
	 */
	public String getBid() {
		return bid;
	}

	/**
	 * @param bid
	 *            the bid to set
	 */
	public void setBid(String bid) {
		this.bid = bid;
	}

	/**
	 * @return the bidFloor
	 */
	public int getBidFloor() {
		return bidFloor;
	}

	/**
	 * @param bidFloor the bidFloor to set
	 */
	public void setBidFloor(int bidFloor) {
		this.bidFloor = bidFloor;
	}

	/**
	 * @return the bidPrice
	 */
	public int getBidPrice() {
		return bidPrice;
	}

	/**
	 * @param bidPrice the bidPrice to set
	 */
	public void setBidPrice(int bidPrice) {
		this.bidPrice = bidPrice;
	}

	public AdType getAdType() {
		return adType;
	}

	public void setAdType(AdType adType) {
		this.adType = adType;
	}

	/**
	 * @return the inApp
	 */
	public boolean isInApp() {
		return inApp;
	}

	/**
	 * @param inApp the inApp to set
	 */
	public void setInApp(boolean inApp) {
		this.inApp = inApp;
	}

	/**
	 * @return
	 */
	public String getPageReferer() {
		return pageReferer;
	}

	/**
	 * @param pageReferer
	 */
	public void setPageReferer(String pageReferer) {
		this.pageReferer = pageReferer;
	}

	/**
	 * @return the pageUrl
	 */
	public String getPageUrl() {
		return pageUrl;
	}

	/**
	 * @param pageUrl the pageUrl to set
	 */
	public void setPageUrl(String pageUrl) {
		this.pageUrl = pageUrl;
	}

	/**
	 * @return the webCatg
	 */
	public String getWebCatg() {
		return webCatg;
	}

	/**
	 * @param webCatg the webCatg to set
	 */
	public void setWebCatg(String webCatg) {
		this.webCatg = webCatg;
	}

	/**
	 * @return the appCatg
	 */
	public String getAppCatg() {
		return appCatg;
	}

	/**
	 * @param appCatg the appCatg to set
	 */
	public void setAppCatg(String appCatg) {
		this.appCatg = appCatg;
	}

	/**
	 * @return the appName
	 */
	public String getAppName() {
		return appName;
	}

	/**
	 * @param appName the appName to set
	 */
	public void setAppName(String appName) {
		this.appName = appName;
	}

	/**
	 * @return the appPackageName
	 */
	public String getAppPackageName() {
		return appPackageName;
	}

	/**
	 * @param appPackageName the appPackageName to set
	 */
	public void setAppPackageName(String appPackageName) {
		this.appPackageName = appPackageName;
	}

	/**
	 * @return the userip
	 */
	public String getUserip() {
		return userip;
	}

	/**
	 * @param userip the userip to set
	 */
	public void setUserip(String userip) {
		this.userip = userip;
	}

	/**
	 * @return the cid
	 */
	public String getCid() {
		return cid;
	}

	/**
	 * @param cid the cid to set
	 */
	public void setCid(String cid) {
		this.cid = cid;
	}
	
	
	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	/**
	 * @return the req
	 */
	@SuppressWarnings("unchecked")
	public <T> T getReq() {
		return (T)req;
	}

	/**
	 * @param req
	 *            the req to set
	 */
	public <T> void setReq(T req) {
		this.req = req;
	}

	public void addMatched(long campid, MatchType matched) {
		Set<MatchType> mts = this.matched.get(campid);
		if(mts == null) {
			mts = new HashSet<MatchType>();
			this.matched.put(campid, mts);
		}
		mts.add(matched);
	}
	public boolean isMatched(long campid, MatchType matched) {
		Set<MatchType> mts = this.matched.get(campid);
		return mts != null && mts.contains(matched);
	}
	public Set<MatchType> getMatched(long campid) {
		return matched.get(campid);
	}

	public void setMaxBidPrice(long campid, int price) {
		maxBidPrice.put(campid, price);
	}

	public int getMaxBidPrice(long campid) {
		return maxBidPrice.get(campid);
	}

	/**
	 * @return the attr
	 */
	@SuppressWarnings("unchecked")
	public <T> T getAttr(String key) {
		return (T) attr.get(key);
	}

	/**
	 * @param attr
	 *            the attr to set
	 */
	public <T> void setAttr(String key, T value) {
		attr.put(key, value);
	}

	public boolean isCookieMapping() {
		return isCookieMapping;
	}

	public void setCookieMapping(boolean isCookieMapping) {
		this.isCookieMapping = isCookieMapping;
	}

	public String getAdp() {
		return adp;
	}

	public void setAdp(String adp) {
		this.adp = adp;
	}
	
	
	
	
}
