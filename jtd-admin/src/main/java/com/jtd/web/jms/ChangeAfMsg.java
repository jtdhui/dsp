package com.jtd.web.jms;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月24日
 * @项目名称 dsp-common-entity
 * @描述 <p>AF值改变消息</p>
 */
public class ChangeAfMsg extends Message {

	private static final long serialVersionUID = 414964624155231252L;
	private long campid;
	private int af;

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
	 * @return the af
	 */
	public int getAf() {
		return af;
	}

	/**
	 * @param af
	 *            the af to set
	 */
	public void setAf(int af) {
		this.af = af;
	}

	@Override
	public MsgType getType() {
		return MsgType.CHANGE_AF;
	}
}
