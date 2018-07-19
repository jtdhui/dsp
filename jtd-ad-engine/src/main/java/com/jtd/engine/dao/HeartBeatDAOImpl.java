package com.jtd.engine.dao;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jtd.engine.utils.SystemTime;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月8日
 * @项目名称 dsp-engine
 * @描述 <p></p>
 */
public class HeartBeatDAOImpl implements HeartBeatDAO {

	private static final Log log = LogFactory.getLog(HeartBeatDAOImpl.class);
	
	private final Logger logMyDebug = LogManager.getLogger("myDebugLog"); 
	
	//心跳超时时间
	private static final long TTL = 10000l;

	private ConcurrentHashMap<String, Long> heartBeats = new ConcurrentHashMap<String, Long>();

	private SystemTime systemTime;
	private long ttl = TTL;

	private volatile boolean isAlive = false;

	/**
	 * 接收统计系统的心跳
	 * nodeName类似 "Counter[" + serviceAddresses + "]" 这样的结构
	 */
	@Override
	public void heartBeat(String nodeName) {
		heartBeats.put(nodeName, systemTime.getTime() + ttl);
//		logMyDebug.info("heartBeat>>nodeName:"+nodeName+"\nheartBeat>>value:"+heartBeats.get(nodeName));
	}

	/**
	 * 是否活着
	 */
	@Override
	public boolean isAlive() {
		return isAlive;
	}

	/**
	 * 刷新状态
	 * dsp-cache-collector、dsp-counter、dsp-log-collector，只要有一个不正常，引擎就停止运行。
	 */
	@Override
	public void refresh() {
		boolean alive = true;
		long now = systemTime.getTime();
		for(Iterator<Entry<String, Long>> it = heartBeats.entrySet().iterator(); it.hasNext();) {
			Entry<String, Long> e = it.next();
			if(e.getValue() < now) {
				log.error("统计系统[" + e.getKey() + "]心跳消息超时,请检查");
				alive = false;
			}
		}
		isAlive = alive;
	}

	/**
	 * @param systemTime
	 *            the systemTime to set
	 */
	public void setSystemTime(SystemTime systemTime) {
		this.systemTime = systemTime;
	}

	/**
	 * @param ttl the ttl to set
	 */
	public void setTtl(long ttl) {
		this.ttl = ttl;
	}
}
