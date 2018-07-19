package com.jtd.engine.utils;

/**
 * 
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月24日
 * @项目名称 dsp-engine
 * @描述 定时接触接口
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
