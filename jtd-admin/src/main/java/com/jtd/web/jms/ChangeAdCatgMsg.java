package com.jtd.web.jms;

import com.jtd.web.constants.CatgSerial;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月24日
 * @项目名称 dsp-common-entity
 * @描述 <p>广告改变消息</p>
 */
public class ChangeAdCatgMsg extends Message {

	private static final long serialVersionUID = 414964624155231252L;
	private long adid;
	private CatgSerial catgserial;
	private String catgid;

	/**
	 * @return the adid
	 */
	public long getAdid() {
		return adid;
	}

	/**
	 * @param adid
	 *            the adid to set
	 */
	public void setAdid(long adid) {
		this.adid = adid;
	}

	/**
	 * @return the catgserial
	 */
	public CatgSerial getCatgserial() {
		return catgserial;
	}

	/**
	 * @param catgserial
	 *            the catgserial to set
	 */
	public void setCatgserial(CatgSerial catgserial) {
		this.catgserial = catgserial;
	}

	/**
	 * @return the catgid
	 */
	public String getCatgid() {
		return catgid;
	}

	/**
	 * @param catgid
	 *            the catgid to set
	 */
	public void setCatgid(String catgid) {
		this.catgid = catgid;
	}

	@Override
	public MsgType getType() {
		return MsgType.CHANGE_AD_CATG;
	}
}
