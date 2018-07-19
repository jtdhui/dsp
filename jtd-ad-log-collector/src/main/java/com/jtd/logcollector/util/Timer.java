package com.jtd.logcollector.util;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月12日
 * @项目名称 dsp-log-collector
 * @描述 <p></p>
 */
public interface Timer {

	/**
	 * 开始计时
	 */
	public void startup();

	/**
	 * 停止计时
	 */
	public void shutdown();

	/**
	 * 添加一个计时任务
	 * 
	 * @param task
	 * @return
	 */
	public TimerFuture timing(TimerTask task);

}
