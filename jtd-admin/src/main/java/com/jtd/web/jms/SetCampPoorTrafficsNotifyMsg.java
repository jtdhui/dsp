package com.jtd.web.jms;
/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月24日
 * @项目名称 dsp-common-entity
 * @描述 <p>设置劣质流量告警</p>
 */
public class SetCampPoorTrafficsNotifyMsg extends Message {

	private static final long serialVersionUID = -6328579116109840458L;

	private long campid;
	private int ntype; // 1 设置广告位告警， 2设置域名告警

	private int pvBaseLine; // 展现量累计大于
	private int minCtr; // 点击率小于,填万分之N的N，即界面上为1%时，填写100
	private int minLtr; // 到达率小于,填万分之N的N，即界面上为1%时，填写100
	private int minRegHopetr; // 注册意向率小于,填万分之N的N，即界面上为1%时，填写100
	private int minRegtr; // 注册转化率小于,填万分之N的N，即界面上为1%时，填写100
	private int minAddCarttr; // 加购率小于,填万分之N的N，即界面上为1%时，填写100
	private int minOrdertr; // 加购率小于,填万分之N的N，即界面上为1%时，填写100
	private int minQuerytr; // 咨询率小于,填万分之N的N，即界面上为1%时，填写100

	public long getCampid() {
		return campid;
	}

	public void setCampid(long campid) {
		this.campid = campid;
	}

	public int getNtype() {
		return ntype;
	}

	public void setNtype(int ntype) {
		this.ntype = ntype;
	}

	public int getPvBaseLine() {
		return pvBaseLine;
	}

	public void setPvBaseLine(int pvBaseLine) {
		this.pvBaseLine = pvBaseLine;
	}

	public int getMinCtr() {
		return minCtr;
	}

	public void setMinCtr(int minCtr) {
		this.minCtr = minCtr;
	}

	public int getMinLtr() {
		return minLtr;
	}

	public void setMinLtr(int minLtr) {
		this.minLtr = minLtr;
	}

	public int getMinRegHopetr() {
		return minRegHopetr;
	}

	public void setMinRegHopetr(int minRegHopetr) {
		this.minRegHopetr = minRegHopetr;
	}

	public int getMinRegtr() {
		return minRegtr;
	}

	public void setMinRegtr(int minRegtr) {
		this.minRegtr = minRegtr;
	}

	public int getMinAddCarttr() {
		return minAddCarttr;
	}

	public void setMinAddCarttr(int minAddCarttr) {
		this.minAddCarttr = minAddCarttr;
	}

	public int getMinOrdertr() {
		return minOrdertr;
	}

	public void setMinOrdertr(int minOrdertr) {
		this.minOrdertr = minOrdertr;
	}

	public int getMinQuerytr() {
		return minQuerytr;
	}

	public void setMinQuerytr(int minQuerytr) {
		this.minQuerytr = minQuerytr;
	}

	@Override
	public MsgType getType() {
		return MsgType.SET_CAMP_POOR_TRAFF_NOTIFY;
	}
}
