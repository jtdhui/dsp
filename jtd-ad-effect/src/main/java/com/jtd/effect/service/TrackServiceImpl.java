package com.jtd.effect.service;

import com.jtd.effect.dao.CampaignDAO;
import com.jtd.effect.dao.ClickDAO;
import com.jtd.effect.dao.CookieDataDAO;
import com.jtd.effect.dao.TrackDAO;
import com.jtd.effect.po.Track;
import com.jtd.effect.util.SystemTime;
import com.jtd.effect.util.Timer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月12日
 * @项目名称 dsp-tracker
 * @描述 <p></p>
 */
@Component
public class TrackServiceImpl implements TrackService {

	private static final Log log = LogFactory.getLog(TrackServiceImpl.class);

	@Value("${trackThreadNum}")
	private int trackThreadNum;
	//存放效果跟踪日志的队列，最多能够存储的数据个数
	@Value("${stopWriteLogWhenQueueSizeGreaterThen}")
	private int stopWriteLogWhenQueueSizeGreaterThen;

	//访问本地redis库的访问层对象
	@Resource
	private CampaignDAO campaignDAO;
	
	//访问redis集群A的访问层对象
	@Resource
	private CookieDataDAO cookieDataDAO;
	//redis集群C的访问对象
	@Resource
	private ClickDAO clickDAO;
	
	//这里直接访问数据库了
	@Resource
	private TrackDAO trackDAO;

	@Resource
	private Timer timer;

	@Resource
	private SystemTime systemTime;

	@Value("${logDir}")
	private String logDir;

	// 统计队列数组,保证统计请求立即返回
	private LinkedBlockingQueue<Track>[] queues;
	//线程数组
	private TrackThread[] trackThreads;
	//数组下标
	private AtomicInteger qpvindex = new AtomicInteger();

	private boolean dynamicSwitchWrite;

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {

		queues = new LinkedBlockingQueue[trackThreadNum];
		trackThreads = new TrackThread[trackThreadNum];
		//初始化队列数组，线程数组
		for(int i = 0 ; i < trackThreadNum; i++) {
			queues[i] = new LinkedBlockingQueue<Track>();
			trackThreads[i] = new TrackThread(i, queues[i], cookieDataDAO, clickDAO, trackDAO, systemTime, logDir);
			//初始化完队列和线程，则直接启动线程，处理track数据
			trackThreads[i].start();
			try { Thread.sleep(10); } catch (InterruptedException e) {}
		}
		dynamicSwitchWrite = stopWriteLogWhenQueueSizeGreaterThen > 0;
	}
	
	/**
	 * 当容器销毁时调用这个方法，将线程数组中的线程一个一个销毁。
	 * @return void
	 */
	@PreDestroy
	public void destroy() {
		for(int i = 0 ; i < trackThreadNum; i++) {
			trackThreads[i].interrupt();
		}
	}

	/* (non-Javadoc)
	 * @see com.doddata.net.service.CountService#increPv(com.doddata.net.po.PV)
	 */
	@Override
	public void track(Track track) {
		//将track，均匀的放到队列数组中
		int idx = qpvindex.getAndIncrement();
		if(idx >= trackThreadNum) {
			idx = 0;
			qpvindex.set(1);
		}
		queues[idx].offer(track);
		
		if(!dynamicSwitchWrite) return;
		switchWrite(idx);
	}

	/**
	 * 切换是否写日志
	 * @param idx
	 */
	private void switchWrite(int idx) {
		if(queues[idx].size() >= stopWriteLogWhenQueueSizeGreaterThen) {
			if(trackThreads[idx].isWrite()) {
				log.warn("统计线程" + idx + "的队列超长，暂停写日志");
				trackThreads[idx].stopWrite();
			}
		} else {
			if(!trackThreads[idx].isWrite()) {
				log.info("统计线程" + idx + "恢复写日志");
				trackThreads[idx].resumeWrite();
			}
		}
	}
}
