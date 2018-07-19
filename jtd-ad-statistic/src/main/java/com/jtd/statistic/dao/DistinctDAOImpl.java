package com.jtd.statistic.dao;

import org.apache.commons.lang3.StringUtils;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月11日
 * @项目名称 dsp-counter
 * @描述 <p></p>
 */
public class DistinctDAOImpl extends AbstractDBSelectedShardedRedisDAO implements DistinctDAO {
	
	private int index;

	/**
	 * 
	 * @param		cid				cookieid
	 * @param		campid			广告活动id
	 * @param		type			类型，0为展示，1为点击，2为landingPage打开次数
	 */
	@Override
	public boolean isNew(String cid, long campid, int type) {
		//只有新添加的用户会更新过期时间，并且某个用户，某个活动，某个类型的次数+1；否则只增加某个用户，某个活动，某个类型的次数+1
		//这里过期时间，是一天，86400秒
		if (StringUtils.isEmpty(cid)) return true;
		Long ret = increexby(cid + "_" + campid + "_" + type, 86400, 1, false);
		//如果ret不为null，并且ret==1，说明对 cid 这个用户、campid这个活动，type这个类型，是新增的用户
		return ret != null && ret == 1l;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
}