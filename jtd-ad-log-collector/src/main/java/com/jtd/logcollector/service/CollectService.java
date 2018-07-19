package com.jtd.logcollector.service;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月12日
 * @项目名称 dsp-log-collector
 * @描述 <p></p>
 */
public interface CollectService {

	/**
	 * 汇总日志里的统计数据然后写入MySQL
	 */
	public void collect();
}
