package com.jtd.statistic.dao;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月11日
 * @项目名称 dsp-counter
 * @描述 <p></p>
 */
public interface DistinctDAO {

	/**
	 * 是否是新用户
	 * @param cid cookieid
	 * @param campid 推广活动id
	 * @param type 0 pv 1 click 2 landing
	 * @return
	 */
	public boolean isNew(String cid, long campid, int type);
}
