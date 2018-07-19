package com.jtd.web.jms;
/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月24日
 * @项目名称 dsp-common-entity
 * @描述 <p>设置活动毛利率</p>
 */
public class SetCampGrossProfitMsg extends Message {

	private static final long serialVersionUID = -6328579116109840458L;
	private long campid;
	private int grossProfit; // 设置为30%时这个字段填30

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
	 * @return the grossProfit
	 */
	public int getGrossProfit() {
		return grossProfit;
	}

	/**
	 * @param grossProfit
	 *            the grossProfit to set
	 */
	public void setGrossProfit(int grossProfit) {
		this.grossProfit = grossProfit;
	}

	@Override
	public MsgType getType() {
		return MsgType.SET_CAMP_GROSS_PROFIT;
	}
}
