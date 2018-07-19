package com.jtd.web.jms;

import java.util.List;
/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月24日
 * @项目名称 dsp-common-entity
 * @描述 <p>设置平台黑名单列表</p>
 */
public class SetBlackListMsg extends Message {
	private static final long serialVersionUID = 1578884350418206515L;
	private List<String> blackList;
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
		return MsgType.SET_BLACKLIST;
	}
}
