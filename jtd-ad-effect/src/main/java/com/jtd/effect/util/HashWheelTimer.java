package com.jtd.effect.util;

import com.jtd.effect.util.TimerTask.Type;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月12日
 * @项目名称 dsp-tracker
 * @描述 <p>定时器，默认是daemon的</p>
 */
@Component("timer")
public class HashWheelTimer implements Timer {

	private static final Log log = LogFactory.getLog(HashWheelTimer.class);

	// 计时器全局ID
	private static final AtomicInteger TIMER_ID = new AtomicInteger();

	// 最大的锁的数目
	private static final int MAX_LOCK_SIZE = 16;

	// 这个计时器的ID
	private int id = TIMER_ID.getAndIncrement();

	// 存放计时任务的hashWheel以及访问他们的锁
	private Set<HashWheelTimerFuture>[] hashWheels;
	private Lock[] wheelLocks;
	private int lockPointerMask;

	// 存放需要立刻执行的任务的队列
	private LinkedBlockingQueue<TimerTask> taskQueue;

	// 需要独立线程执行的任务的executor
	private ExecutorService es;

	// wheel的指针和计算时使用的掩码
	private volatile int wheelPointer;
	private int wheelPointerMask;

	// 开始计时的时间
	private long startTime;

	// 多少毫秒跳动一次
	private int tick;

	// 一圈多少毫秒
	private int round;

	// 计时线程
	private Thread timerThread;

	// 执行线程
	private Thread execThread;

	// 是否已经停止计时
	private volatile boolean stoped;

	/**
	 * 放在HashWheel中的task
	 * 
	 * @author ASME
	 * 
	 *         2011-7-25
	 */
	private final class HashWheelTimerFuture implements TimerFuture {

		// 计时任务
		private TimerTask task;

		// 到期时间
		private long deadline;

		// 还剩下多少圈
		private volatile long remainingRounds;

		// 计时任务是否已经取消
		private volatile boolean cancelled;

		/**
		 * 使用计时任务构造HashWheelTimerFuture
		 * @param task
		 */
		private HashWheelTimerFuture(TimerTask task) {
			this.task = task;
		}

		/** 
		 * (non-Javadoc)
		 *
		 */
		public void cancel() {
			if (cancelled || isExpired()) return;
			cancelled = true;
		}

		/** 
		 * (non-Javadoc)
		 *
		 */
		public TimerTask getTimerTask() {
			return task;
		}

		/** 
		 * (non-Javadoc)
		 *
		 */
		public boolean isCancelled() {
			return cancelled;
		}

		/**
		 * (non-Javadoc)
		 * 
		 */
		public boolean isExpired() {

			// interval类型的任务到期的标志为取消了
			if (task.type() == Type.INTERVAL) return cancelled;
			return cancelled || System.currentTimeMillis() >= deadline;
		}

	}

	/**
	 * 使用缺省值的构造器
	 */
	public HashWheelTimer() {
		this(true);
	}
	public HashWheelTimer(boolean daemon) {
		this(daemon, 20);
	}
	public HashWheelTimer(boolean daemon, int tick) {
		this(daemon, tick, 1024);
	}

	/**
	 * 使用tick, ticksPerWheel, es构造计时器
	 * @param tick
	 * @param ticksPerWheel
	 */
	@SuppressWarnings("unchecked")
	public HashWheelTimer(final boolean daemon, final int tick, int ticksPerWheel) {

		final int normalizedTicksPerWheel = normalizeTicksPerWheel(ticksPerWheel);
		this.tick = tick;
		round = tick * normalizedTicksPerWheel;
		wheelPointerMask = normalizedTicksPerWheel - 1;
		this.es = Executors.newCachedThreadPool(new ThreadFactory() {

			// 线程ID
			private AtomicInteger threadId = new AtomicInteger();

			/** 
			 * (non-Javadoc)
			 *
			 * @see ThreadFactory#newThread(Runnable)
			 */
			public Thread newThread(Runnable r) {
				Thread t = new Thread(r, "TimerIndependentExecThread-" + threadId.getAndIncrement());
				t.setDaemon(daemon);
				return t;
			}
		});

		// 初始化任务队列
		taskQueue = new LinkedBlockingQueue<TimerTask>();

		// 初始化任务执行线程
		initTaskThread(daemon);

		hashWheels = new HashSet[normalizedTicksPerWheel];

		// 初始化Wheels
		for(int i = 0; i < normalizedTicksPerWheel; i++) {
			hashWheels[i] = new HashSet<HashWheelTimerFuture>();
		}

		// 初始化锁
		int lockSize = normalizedTicksPerWheel > MAX_LOCK_SIZE ? MAX_LOCK_SIZE : normalizedTicksPerWheel;
		lockPointerMask = lockSize - 1;
		wheelLocks = new ReentrantLock[lockSize];
		for (int i = 0; i < lockSize; i++) {
			wheelLocks[i] = new ReentrantLock();
		}

		// 初始化计时线程
		initTimerThread(daemon);
	}

	/**
	 * 初始化任务执行线程
	 */
	private void initTaskThread(boolean daemon) {

		execThread = new Thread("TaskThread[" + id + "]") {
			public void run() {
				try {
					for (; !Thread.interrupted();) {
						TimerTask task = taskQueue.take();
						try {
							task.run();
						} catch (Throwable e) {
							log.error("执行计时任务发生错误", e);
						}
					}
				} catch (InterruptedException e) {
					// nothing to do
				}
			}
		};
		execThread.setDaemon(daemon);
	}

	/**
	 * 初始化计时线程
	 */
	private void initTimerThread(boolean daemon) {

		timerThread = new Thread("TimerThread[" + id + "]") {

			// 计时器启动了多久
			long tickedTime = 0;

			/** 
			 * (non-Javadoc)
			 *
			 * @see Thread#run()
			 */
			public void run() {
				startTime = System.currentTimeMillis();
				try {
					for (; !Thread.interrupted(); notifyExpiredTasks(tick()));
				} catch (InterruptedException e) {
					// nothing to do
				}
			}

			/**
			 * tick the time
			 * 
			 * @return
			 * @throws InterruptedException
			 */
			private long tick() throws InterruptedException {
				tickedTime += tick;
				long nextTickTime = startTime + tickedTime;
				for (long sleepMills = nextTickTime - System.currentTimeMillis(); sleepMills > 0;) {
					Thread.sleep(sleepMills);
					sleepMills = nextTickTime - System.currentTimeMillis();
				}
				return nextTickTime;
			}

			/**
			 * 触发所有到期的计时任务
			 * @param now
			 */
			private void notifyExpiredTasks(long now) {

				// 计算指针
				wheelPointer = wheelPointer + 1 & wheelPointerMask;
				Set<HashWheelTimerFuture> wheel = hashWheels[wheelPointer];

				// 计算应该使用哪个锁
				int lockIndex = wheelPointer & lockPointerMask;

				try{
					wheelLocks[lockIndex].lock();
					for(Iterator<HashWheelTimerFuture> it = wheel.iterator(); it.hasNext();) {
						HashWheelTimerFuture future = it.next();

						// 如果已经取消了,就删掉这个任务
						if(future.cancelled) {
							it.remove();
							continue;
						}

						if(future.remainingRounds > 0) {

							// 还不是最后一圈
							future.remainingRounds--;
						} else {

							// 是最后一圈了
							it.remove();
							if(future.deadline <= now){

								TimerTask task = future.task;

								// 时间到了,触发
								if(task.isTriggerIndependently()) {
									es.submit(task);
								} else {
									taskQueue.offer(task);
								}

								// 如果是周期性任务, 重新放入计时器
								if(task.type() == Type.INTERVAL) {
									scheduleTimerFuture(future);
								}
							} else {

								int index = wheelPointer + 1 & wheelPointerMask;
								int lIndex = index & lockPointerMask;
								wheelLocks[lIndex].lock();
								hashWheels[index].add(future);
								wheelLocks[lIndex].unlock();
							}
						}
					}
				} finally {
					wheelLocks[lockIndex].unlock();
				}
			}
		};
		timerThread.setDaemon(daemon);
	}

	/**
	 * 调度计时任务
	 * @param future
	 */
	private void scheduleTimerFuture(HashWheelTimerFuture future) {

		// 如果还没有启动,那么先启动
		if (!execThread.isAlive()) execThread.start();
		if (!timerThread.isAlive()) timerThread.start();

		// 计算延迟时间
		long delay = future.task.delayOrIntervalMillis() < tick ? tick : future.task.delayOrIntervalMillis();

		// 计算deadline和剩下的圈数
		future.deadline = System.currentTimeMillis() + delay;
		future.remainingRounds = delay / round - (delay % round == 0 ? 1 : 0);

		// 放到正确的Set里去
		long lastRoundDelay = delay % round;
		long lastTickDelay = lastRoundDelay % tick;
		int rIndex = (int) lastRoundDelay / tick + (lastTickDelay != 0 ? 1 : 0);
		int pointer = wheelPointer + rIndex & wheelPointerMask;
		int lockIndex = pointer & lockPointerMask;
		try {
			wheelLocks[lockIndex].lock();
			hashWheels[pointer].add(future);
		} finally {
			wheelLocks[lockIndex].unlock();
		}
	}

	/** 
	 * (non-Javadoc)
	 *
	 *
	 */
	@PostConstruct
	public void startup() {
		if (stoped) throw new IllegalStateException("计时器停止后不能再启动");
		if (!execThread.isAlive()) execThread.start();
		if (!timerThread.isAlive()) timerThread.start();
	}

	/** 
	 * (non-Javadoc)
	 *
	 */
	@PreDestroy
	public void shutdown() {
		if (stoped) return;

		stoped = true;
		timerThread.interrupt();
		execThread.interrupt();
		es.shutdown();
		try {
			es.awaitTermination(100, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			// nothing to do
		}
	}

	/** 
	 * (non-Javadoc)
	 *
	 */
	public TimerFuture timing(TimerTask task) {

		if (stoped) throw new IllegalStateException("计时器已停止");

		HashWheelTimerFuture future = new HashWheelTimerFuture(task);
		scheduleTimerFuture(future);
		return future;
	}

    /**
     * 规范化wheel的size
     * @param ticksPerWheel
     * @return
     */
	private static int normalizeTicksPerWheel(int ticksPerWheel) {
		int nTicks = 1;
		for (nTicks <<= 1; nTicks < ticksPerWheel; nTicks <<= 1);
		return nTicks;
	}
}
