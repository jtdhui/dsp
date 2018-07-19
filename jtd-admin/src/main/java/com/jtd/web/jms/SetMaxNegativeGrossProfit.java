package com.jtd.web.jms;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月24日
 * @项目名称 dsp-common-entity
 * @描述 <p>设置最大的负毛利</p>
 */
public class SetMaxNegativeGrossProfit extends Message {

	private static final long serialVersionUID = -6328579116109840458L;
	private long campid;
	private long maxNegativeGrossProfit; // 分为单位的钱数

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
	 * @return the maxNegativeGrossProfit
	 */
	public long getMaxNegativeGrossProfit() {
		return maxNegativeGrossProfit;
	}

	/**
	 * @param maxNegativeGrossProfit
	 *            the maxNegativeGrossProfit to set
	 */
	public void setMaxNegativeGrossProfit(long maxNegativeGrossProfit) {
		this.maxNegativeGrossProfit = maxNegativeGrossProfit;
	}

	@Override
	public MsgType getType() {
		return MsgType.SET_MAX_NEGATIVE_GROSS_PROFIT;
	}
}
