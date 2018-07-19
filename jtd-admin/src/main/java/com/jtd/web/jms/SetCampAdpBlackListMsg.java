package com.jtd.web.jms;

import java.util.Map;
import java.util.Set;
/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月24日
 * @项目名称 dsp-common-entity
 * @描述 <p>设置活动的广告位黑名单</p>
 */
public class SetCampAdpBlackListMsg extends Message {

	private static final long serialVersionUID = 1578884350418206515L;

	private long campid;
	private Map<Long, Set<String>> blacklist; // key:渠道ID, value:黑名单的广告位ID集合

	public long getCampid() {
		return campid;
	}

	/**
	 * @param campid
	 * @return void
	 */
	public void setCampid(long campid) {
		this.campid = campid;
	}

	public Map<Long, Set<String>> getBlacklist() {
		return blacklist;
	}

	public void setBlacklist(Map<Long, Set<String>> blacklist) {
		this.blacklist = blacklist;
	}

	@Override
	public MsgType getType() {
		return MsgType.SET_CAMP_ADP_BLACKLIST;
	}
}
