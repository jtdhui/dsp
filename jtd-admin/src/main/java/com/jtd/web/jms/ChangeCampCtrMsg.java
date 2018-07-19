package com.jtd.web.jms;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月24日
 * @项目名称 dsp-common-entity
 * @描述 <p>修改活动最近周期的CTR</p>
 */
public class ChangeCampCtrMsg extends Message {

	private static final long serialVersionUID = -8325900777890862251L;
	private long campid;
	private float ctr;

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
	 * @return the ctr
	 */
	public float getCtr() {
		return ctr;
	}

	/**
	 * @param ctr the ctr to set
	 */
	public void setCtr(float ctr) {
		this.ctr = ctr;
	}

	@Override
	public MsgType getType() {
		return MsgType.CHANGE_CAMP_CTR;
	}
}
