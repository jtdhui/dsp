package com.jtd.effect.util;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月12日
 * @项目名称 dsp-tracker
 * @描述 <p></p>
 */
public interface TimerTask extends Runnable {

	// 超时性的或间隔性的计时任务
	public static enum Type {
		TIMEOUT, INTERVAL;
	}

	/**
	 * 获取计时任务类型
	 * 
	 * @return
	 */
	public Type type();

	/**
	 * 以毫秒计算的计时时间或间隔时间
	 * 
	 * @return
	 */
	public long delayOrIntervalMillis();

	/**
	 * 是否使用独立的线程触发
	 * 计时器内部使用一个线程触发排队的任务
	 * 对于比较耗时的任务,最好使用独立的线程触发避免其他任务的执行受到影响
	 * @return
	 */
	public boolean isTriggerIndependently();

}
