package com.jtd.web.jms;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月24日
 * @项目名称 dsp-common-entity
 * @描述 <p>设置活动小时投放控制</p>
 */
public class SetCampHourPauseMsg extends Message {

	private static final long serialVersionUID = 1578884350418206515L;

	private long campid;
	private int hour;

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

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	@Override
	public MsgType getType() {
		return MsgType.SET_CAMP_HOUR_PAUSE;
	}
}
