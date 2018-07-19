package com.jtd.engine.dao;

import java.util.List;
import java.util.Set;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月8日
 * @项目名称 dsp-engine
 * @描述 <p></p>
 */
public interface BlackListDAO {

	/**
	 * 设置全局黑名单
	 * 
	 * @param blackList
	 * @return
	 */
	public boolean setBlackList(List<String> blackList);

	/**
	 * 设置活动的黑名单
	 * 
	 * @param blackList
	 * @param campid
	 * @return
	 */
	public boolean setBlackList(List<String> blackList, long campid);

	/**
	 * 获取全局黑名单
	 * 
	 * @return
	 */
	public Set<String> getBlackList();

	/**
	 * 获取活动的黑名单
	 * 
	 * @return
	 */
	public Set<String> getBlackList(long campid);
}
