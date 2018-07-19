package com.jtd.web.jms;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月24日
 * @项目名称 dsp-common-entity
 * @描述 <p>竞价次数消息/p>
 */
public class BidCountMsg extends Message {

	private static final long serialVersionUID = -2400742645501699836L;

	private long partnerid;
	private long groupid;
	private long campid;
	private long creativeid;
	private int bidNum;

	/**
	 * @return the partnerid
	 */
	public long getPartnerid() {
		return partnerid;
	}

	/**
	 * @param partnerid
	 *            the partnerid to set
	 */
	public void setPartnerid(long partnerid) {
		this.partnerid = partnerid;
	}

	/**
	 * @return the groupid
	 */
	public long getGroupid() {
		return groupid;
	}

	/**
	 * @param groupid
	 *            the groupid to set
	 */
	public void setGroupid(long groupid) {
		this.groupid = groupid;
	}

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
	 * @return the creativeid
	 */
	public long getCreativeid() {
		return creativeid;
	}

	/**
	 * @param creativeid
	 *            the creativeid to set
	 */
	public void setCreativeid(long creativeid) {
		this.creativeid = creativeid;
	}

	/**
	 * @return the bidNum
	 */
	public int getBidNum() {
		return bidNum;
	}

	/**
	 * @param bidNum
	 *            the bidNum to set
	 */
	public void setBidNum(int bidNum) {
		this.bidNum = bidNum;
	}

	@Override
	public MsgType getType() {
		return MsgType.BID_COUNT;
	}
}
