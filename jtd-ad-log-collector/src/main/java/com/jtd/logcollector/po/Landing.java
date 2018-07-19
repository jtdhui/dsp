package com.jtd.logcollector.po;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月12日
 * @项目名称 dsp-log-collector
 * @描述 <p></p>
 */
public class Landing {

	private long time;
	private String cookieid;
	private boolean uv;
	private String userip;
	private String userAgent;
	private String pageUrl;
	private String pageReferer;
	private String clickKey;
	
	private Click click;

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
	 * @return the uv
	 */
	public boolean isUv() {
		return uv;
	}

	/**
	 * @param uv the uv to set
	 */
	public void setUv(boolean uv) {
		this.uv = uv;
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
	 * @return the clickKey
	 */
	public String getClickKey() {
		return clickKey;
	}

	/**
	 * @param clickKey
	 *            the clickKey to set
	 */
	public void setClickKey(String clickKey) {
		this.clickKey = clickKey;
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
