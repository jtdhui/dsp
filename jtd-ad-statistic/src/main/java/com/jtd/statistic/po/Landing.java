package com.jtd.statistic.po;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月11日
 * @项目名称 dsp-counter
 * @描述 <p></p>
 */
public class Landing {
	
	//请求的时间
	private long time;
	//landing的cookieid
	private String cookieid;
	//是否为独立用户
	private boolean uv;
	//用户的ip
	private String userip;
	//用户的ua数据
	private String userAgent;
	//请求服务器的，页面url地址
	private String pageUrl;
	//请求服务器的，上一个页面地址
	private String pageReferer;
	//点击的key
	private String clickKey;
	//点击对象
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
