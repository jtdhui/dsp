package com.jtd.statistic.po;

import java.math.BigDecimal;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月11日
 * @项目名称 dsp-counter
 * @描述 <p></p>
 */
public class PV {

	private Param param;
	private long time;
	private String cookieid;
	private boolean uv;
	private String userip;
	private String userAgent;
	private String redirect;
	private String referer;
	private BigDecimal price;

	private BigDecimal expend;

	/**
	 * @return the param
	 */
	public Param getParam() {
		return param;
	}

	/**
	 * @param param
	 *            the param to set
	 */
	public void setParam(Param param) {
		this.param = param;
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
	 * @return the redirect
	 */
	public String getRedirect() {
		return redirect;
	}

	/**
	 * @param redirect
	 *            the redirect to set
	 */
	public void setRedirect(String redirect) {
		this.redirect = redirect;
	}

	/**
	 * @return the referer
	 */
	public String getReferer() {
		return referer;
	}

	/**
	 * @param referer
	 *            the referer to set
	 */
	public void setReferer(String referer) {
		this.referer = referer;
	}

	/**
	 * @return the price
	 */
	public BigDecimal getPrice() {
		return price;
	}

	/**
	 * @param price
	 *            the price to set
	 */
	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	/**
	 * @return the expend
	 */
	public BigDecimal getExpend() {
		return expend;
	}

	/**
	 * @param expend the expend to set
	 */
	public void setExpend(BigDecimal expend) {
		this.expend = expend;
	}
}
