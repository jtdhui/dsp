package com.jtd.effect.po;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月12日
 * @项目名称 dsp-tracker
 * @描述 <p></p>
 */
public class OrderDetail {

	private String goodsid;
	private int goodsnum;
	private int price;
	private String pageurl;
	private String imgurl;

	/**
	 * @return the goodsid
	 */
	public String getGoodsid() {
		return goodsid;
	}

	/**
	 * @param goodsid
	 *            the goodsid to set
	 */
	public void setGoodsid(String goodsid) {
		this.goodsid = goodsid;
	}

	/**
	 * @return the goodsnum
	 */
	public int getGoodsnum() {
		return goodsnum;
	}

	/**
	 * @param goodsnum
	 *            the goodsnum to set
	 */
	public void setGoodsnum(int goodsnum) {
		this.goodsnum = goodsnum;
	}

	/**
	 * @return the price
	 */
	public int getPrice() {
		return price;
	}

	/**
	 * @param price
	 *            the price to set
	 */
	public void setPrice(int price) {
		this.price = price;
	}

	/**
	 * @return the pageurl
	 */
	public String getPageurl() {
		return pageurl;
	}

	/**
	 * @param pageurl
	 *            the pageurl to set
	 */
	public void setPageurl(String pageurl) {
		this.pageurl = pageurl;
	}

	/**
	 * @return the imgurl
	 */
	public String getImgurl() {
		return imgurl;
	}

	/**
	 * @param imgurl
	 *            the imgurl to set
	 */
	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}
}
