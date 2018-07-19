package com.jtd.effect.po;

import java.util.HashMap;
import java.util.Map;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月12日
 * @项目名称 dsp-tracker
 * @描述 <p></p>
 */
public class Track {

	/* 0 全站访客召回/到达
	 * 1 订单
	 * 2 加购
	 * 3 注册意向
	 * 4 注册成功
	 * 5 分享
	 * 6 收藏
	 * 7 咨询
	 * 8 登录
	 * 9 评论
	*/
	private int type;

	private long time;
	private String cookieid;
	private String userip;
	private String userAgent;
	private String pageUrl;
	private String pageReferer;
	private long partnerId;
	private long cookiegId;

	// 这个效果的访客如果点击过我们的广告则会有这个cookie
	private String clickKey;

	// 全站访客召回之外其他跟踪类型的参数
	private Map<String, Object> trackParam;

	private Click click;

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @return the time
	 */
	public long getTime() {
		return time;
	}

	/**
	 * @param time
	 *            the time to set
	 */
	public void setTime(long time) {
		this.time = time;
	}

	/**
	 * @return the cookieid
	 */
	public String getCookieid() {
		return cookieid;
	}

	/**
	 * @param cookieid
	 *            the cookieid to set
	 */
	public void setCookieid(String cookieid) {
		this.cookieid = cookieid;
	}

	/**
	 * @return the userip
	 */
	public String getUserip() {
		return userip;
	}

	/**
	 * @param userip
	 *            the userip to set
	 */
	public void setUserip(String userip) {
		this.userip = userip;
	}

	/**
	 * @return the userAgent
	 */
	public String getUserAgent() {
		return userAgent;
	}

	/**
	 * @param userAgent
	 *            the userAgent to set
	 */
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	/**
	 * @return the pageUrl
	 */
	public String getPageUrl() {
		return pageUrl;
	}

	/**
	 * @param pageUrl
	 *            the pageUrl to set
	 */
	public void setPageUrl(String pageUrl) {
		this.pageUrl = pageUrl;
	}

	/**
	 * @return the pageReferer
	 */
	public String getPageReferer() {
		return pageReferer;
	}

	/**
	 * @param pageReferer
	 *            the pageReferer to set
	 */
	public void setPageReferer(String pageReferer) {
		this.pageReferer = pageReferer;
	}

	/**
	 * @return the partnerId
	 */
	public long getPartnerId() {
		return partnerId;
	}

	/**
	 * @param partnerId
	 *            the partnerId to set
	 */
	public void setPartnerId(long partnerId) {
		this.partnerId = partnerId;
	}

	/**
	 * @return the cookiegId
	 */
	public long getCookiegId() {
		return cookiegId;
	}

	/**
	 * @param cookiegId
	 *            the cookiegId to set
	 */
	public void setCookiegId(long cookiegId) {
		this.cookiegId = cookiegId;
	}

	/**
	 * @return the trackParam
	 */
	public Map<String, Object> getTrackParam() {
		return trackParam;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getTrackParam(String key) {
		return (T) trackParam.get(key);
	}

	/**
	 * @return the clickKey
	 */
	public String getClickKey() {
		return clickKey;
	}

	/**
	 * @param clickKey the clickKey to set
	 */
	public void setClickKey(String clickKey) {
		this.clickKey = clickKey;
	}

	/**
	 * @param trackParam the trackParam to set
	 */
	public void setTrackParam(Map<String, Object> trackParam) {
		this.trackParam = trackParam;
	}

	/**
	 * @param trackParam the trackParam to set
	 */
	public void setTrackParam(String name, Object value) {
		if(trackParam == null) trackParam = new HashMap<String, Object>();
		trackParam.put(name, value);
	}

	/**
	 * @return the click
	 */
	public Click getClick() {
		return click;
	}

	/**
	 * @param click the click to set
	 */
	public void setClick(Click click) {
		this.click = click;
	}
}
