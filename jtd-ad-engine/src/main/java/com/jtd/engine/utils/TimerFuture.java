package com.jtd.engine.utils;


/**
 * 
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月24日
 * @项目名称 dsp-engine
 * @描述 预指定定时器接口
 */
public interface TimerFuture {

	/**
	 * 获取跟它关联的计时任务
	 * 
	 * @return
	 */
	public TimerTask getTimerTask();

	/**
	 * 计时任务是否已经到期
	 * 对于interval类型的任务,Cancel掉后才返回true
	 * 
	 * @return
	 */
	public boolean isExpired();

	/**
	 * 计时任务是否已经取消
	 * 
	 * @return
	 */
	public boolean isCancelled();

	/**
	 * 取消计时任务
	 */
	public void cancel();
}
