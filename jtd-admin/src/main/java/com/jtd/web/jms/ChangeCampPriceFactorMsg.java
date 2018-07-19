package com.jtd.web.jms;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月24日
 * @项目名称 dsp-common-entity
 * @描述 <p>修改活动的价格因子,由统计系统发给引擎</p>
 */
public class ChangeCampPriceFactorMsg extends Message {

	private static final long serialVersionUID = 414964624155231252L;
	private long campid;
	private float pricefactor;

	/**
	 * @return the campid
	 */
	public long getCampid() {
		return campid;
	}

	/**
	 * @param campid
	 *            the campid to set
	 */
	public void setCampid(long campid) {
		this.campid = campid;
	}

	/**
	 * @return the pricefactor
	 */
	public float getPricefactor() {
		return pricefactor;
	}

	/**
	 * @param pricefactor
	 *            the pricefactor to set
	 */
	public void setPricefactor(float pricefactor) {
		this.pricefactor = pricefactor;
	}

	@Override
	public MsgType getType() {
		return MsgType.CHANGE_CAMP_PRICE_FACTOR;
	}
}
