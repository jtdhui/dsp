package com.jtd.statistic.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSON;
import com.jtd.statistic.po.Click;
import com.jtd.statistic.po.Landing;
import com.jtd.statistic.po.PV;
import com.jtd.statistic.util.SystemTime;
/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月11日
 * @项目名称 dsp-counter
 * @描述 <p>写统计日志</p>
 */
class WriteLogThread extends Thread {

	private static final Log log = LogFactory.getLog(WriteLogThread.class);

	private LinkedBlockingQueue<Object> queue;
	private SystemTime systemTime;

	private BufferedWriter[] writers = new BufferedWriter[3];
	private File[] dirs;
	private String nodeName;
	private int threadId;

	WriteLogThread(int id, LinkedBlockingQueue<Object> queue, SystemTime sysTime, String logDir, String nodeName) {
		super("WriteLogThread-" + id);
		threadId = id;
		this.queue = queue;
		this.systemTime = sysTime;
		this.nodeName = nodeName;
		File dir = new File(logDir);
		if (!dir.exists()) dir.mkdirs();
		dirs = new File[3];
		dirs[0] = new File(dir, "pv");
		if (!dirs[0].exists()) dirs[0].mkdirs();
		dirs[1] = new File(dir, "click");
		if (!dirs[1].exists()) dirs[1].mkdirs();
		dirs[2] = new File(dir, "landing");
		if (!dirs[2].exists()) dirs[2].mkdirs();
	}

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		log.info(Thread.currentThread().getName() + " is running");
		long lastFlushTime = systemTime.getTime();
		int datehour = systemTime.getYyyyMMddHH();
		
		if(!switchWriters(datehour)) return;

		for (; !Thread.interrupted();) {
			Object o = null;
			try {
				o = queue.poll(10, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				break;
			}
			if (o == null) {
				flushAll();
				continue;
			}

			if (systemTime.getTime() - lastFlushTime > 60000) {
				// 保证flush间隔不超1分钟
				lastFlushTime = systemTime.getTime();
				flushAll();
			}
			
			int dh = systemTime.getYyyyMMddHH();
			if(datehour != dh) {
				datehour = dh;
				if(!switchWriters(dh)) {
					log.error("切换日志文件发生错误");
					break;
				}
			}

			if (o instanceof PV) {
				write(0, o);
			} else if (o instanceof Click) {
				write(1, o);
			} else if (o instanceof Landing) {
				write(2, o);
			}
		}
		
		flushAll();
		log.warn("写日志线程" + Thread.currentThread().getName() + "已退出");
	}

	/**
	 * @param writers
	 * @param datehour
	 * @return
	 */
	private boolean switchWriters(int datehour) {
		boolean ret = true;
		for(int i = 0; i < 3; i++) {
			if(writers[i] != null) {
				try {
					writers[i].close();
				} catch (Exception e) {
					log.error("关闭日志文件发生错误", e);
				}
			}
			File f = new File(dirs[i], datehour + "_" + nodeName + "_" + threadId + ".txt");
			try {
				writers[i] = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f, f.exists()), "UTF-8"));
			} catch (Exception e) {
				ret = false;
				try { log.error("创建文件["+f.getCanonicalPath()+"]发生错误"); } catch (Exception e1) {}
			}
		}
		return ret;
	}

	private void write(int i, Object o) {
		try {
			writers[i].write(JSON.toJSONString(o) + "\r\n");
		} catch (Exception e) {
			log.error("写日志发生错误", e);
		}
	}

	private void flushAll() {
		for (BufferedWriter w : writers) {
			try {
				w.flush();
			} catch (Exception e) {
				log.error("刷新日志发生错误", e);
			}
		}
	}
}
