package com.jtd.engine.utils;


/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月24日
 * @项目名称 dsp-engine
 * @描述	
 */
public interface TimerTask extends Runnable {

	/**
	 * @作者 Amos Xu
	 * @版本 V1.0
	 * @配置 
	 * @创建日期 2016年8月24日
	 * @项目名称 dsp-engine
	 * @描述 超时性的或间隔性的计时任务
	 */
	public static enum Type {
		TIMEOUT, INTERVAL;
	}

	/**
	 * 获取计时任务类型
	 * @return
	 * @return Type
	 */
	public Type type();

	/**
	 * 以毫秒计算的计时时间或间隔时间
	 * @return
	 * @return long
	 */
	public long delayOrIntervalMillis();

	/**
	 * 是否使用独立的线程触发
	 * 计时器内部使用一个线程触发排队的任务
	 * 对于比较耗时的任务,最好使用独立的线程触发避免其他任务的执行受到影响
	 * @return
	 * @return boolean
	 */
	public boolean isTriggerIndependently();

}
