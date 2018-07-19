package com.jtd.web.jms;

import java.util.List;
/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月24日
 * @项目名称 dsp-common-entity
 * @描述 <p>设置活动黑名单列表</p>
 */
public class SetCampBlackListMsg extends Message {

	private static final long serialVersionUID = 1578884350418206515L;

	private long campid;
	private List<String> blackList;

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
	 * @return the blackList
	 */
	public List<String> getBlackList() {
		return blackList;
	}

	/**
	 * @param blackList
	 *            the blackList to set
	 */
	public void setBlackList(List<String> blackList) {
		this.blackList = blackList;
	}

	@Override
	public MsgType getType() {
		return MsgType.SET_CAMP_BLACKLIST;
	}
}
