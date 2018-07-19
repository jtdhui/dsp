package com.jtd.web.jms;
/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月24日
 * @项目名称 dsp-common-entity
 * @描述 <p>设置自动黑名单</p>
 */
public class SetAutoParAdpHostBlackListMsg extends Message {

	private static final long serialVersionUID = -3850678598718876706L;

	private int bltype; // 1 设置广告位黑名单， 2设置域名黑名单
	private long partnerid; // 广告主ID

	private int pvBaseLine; // 展现量累计大于
	private int minCtr; // 点击率小于,填万分之N的N，即界面上为1%时，填写100
	private int minLUVDividePV; // 到达UV/展现量小于,填万分之N的N，即界面上为1%时，填写100
	private int minLRate; // 到达率小于,填万分之N的N，即界面上为1%时，填写100
	private int maxLPVDivideLUV; // 到达PV/UV大于,填万分之N的N，即界面上为1%时，填写100

	public int getBltype() {
		return bltype;
	}

	public void setBltype(int bltype) {
		this.bltype = bltype;
	}

	public long getPartnerid() {
		return partnerid;
	}

	public void setPartnerid(long partnerid) {
		this.partnerid = partnerid;
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

	public int getMinLUVDividePV() {
		return minLUVDividePV;
	}

	public void setMinLUVDividePV(int minLUVDividePV) {
		this.minLUVDividePV = minLUVDividePV;
	}

	public int getMinLRate() {
		return minLRate;
	}

	public void setMinLRate(int minLRate) {
		this.minLRate = minLRate;
	}

	public int getMaxLPVDivideLUV() {
		return maxLPVDivideLUV;
	}

	public void setMaxLPVDivideLUV(int maxLPVDivideLUV) {
		this.maxLPVDivideLUV = maxLPVDivideLUV;
	}

	@Override
	public MsgType getType() {
		return MsgType.SET_AUTO_PAR_ADP_HOST_BLACKLIST;
	}
}
