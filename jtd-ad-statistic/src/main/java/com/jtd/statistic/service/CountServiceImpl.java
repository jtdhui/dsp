package com.jtd.statistic.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.jtd.statistic.dao.*;
import com.jtd.statistic.po.Click;
import com.jtd.statistic.po.Landing;
import com.jtd.statistic.po.PV;
import com.jtd.statistic.util.SystemTime;
import com.jtd.statistic.util.Timer;
import com.jtd.statistic.util.TimerTask;
import com.jtd.web.jms.HeartBeatMsg;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;


/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月11日
 * @项目名称 dsp-counter
 * @描述 <p></p>
 */
@Component
public class CountServiceImpl implements CountService {

	private static final Log log = LogFactory.getLog(CountServiceImpl.class);
	//曝光次数
	private static final AtomicLong PV = new AtomicLong();
	//请求次数
	private static final AtomicLong REQ = new AtomicLong();

	@Value("${countThreadNum}")
	private int countThreadNum;

	@Value("${stopWriteLogWhenQueueSizeGreaterThen}")
	private int stopWriteLogWhenQueueSizeGreaterThen;

	@Resource
	private CampaignDAO campaignDAO;

	@Resource
	private FreqDataDAO freqDataDAO;

	@Resource
	private DistinctDAO distinctDAO;

	@Resource(name = "countDAO")
	private CountDAO countDAO;
	
	@Resource(name = "countDAO4Click")
	private CountDAO countDAO4Click;
	

	@Resource(name = "clickDAO")
	private ClickDAO clickDAO;
	
	@Resource(name = "click90DAO")
	private ClickDAO click90DAO;

	@Resource
	private Timer timer;

	@Resource
	private SystemTime systemTime;
	
	@Resource
	private JmsTemplate jmsTemplate;

	@Value("${logDir}")
	private String logDir;
	
	@Value("${serviceAddresses}")
	private String serviceAddresses;

	@Value("${amq.clientId}")
	private String counterName;

	// 统计队列,保证统计请求立即返回
	//队列数组，数组中每个队列接收的是Object对象
	private LinkedBlockingQueue<Object>[] queues;
	//统计的线程数组
	private CountThread[] countThreads;
	//pv队列的索引
	private AtomicInteger qpvindex = new AtomicInteger();
	//click队列的索引
	private AtomicInteger qclkindex = new AtomicInteger();
	//LandingPage队里索引
	private AtomicInteger qldindex = new AtomicInteger();
	
	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {
		//设置数组队列大小
		queues = new LinkedBlockingQueue[countThreadNum];
		//设置线程数组大小
		countThreads = new CountThread[countThreadNum];
		//用于捕获未检测异常，就是当线程出现未监测异常，如果在代码中调用
		//Thread.currentThread().setUncaughtExceptionHandler(h);那么这个方法会接受到异常，并且程序不会退出。
		//这里是初始化一个 UncaughtExceptionHandler 对象。
		UncaughtExceptionHandler h = new UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				log.error("线程" + t.getName() + "发生错误", e);
			}
		};
		//countThreadNum,为处理统计的线程个数
		for(int i = 0 ; i < countThreadNum; i++) {
			//统计队列，每个处理的线程对应这一个消息队列
			//给第 i 个处理线程，分配一个队里实例
			queues[i] = new LinkedBlockingQueue<Object>();
			/**
			 * id											线程id
			 * queues[i]									这个线程需要处理的消息，在这个队列中存放
			 * campaignDAO									广告活动的数据访问层，本地redis
			 * freqDataDAO									投放频次的数据访问层，redis集群A
			 * distinctDAO									数据存放在redis集群B
			 * countDAO										统计计数，数据访问层,redis集群B
			 * countDAO4Click								redis集群B
			 * clickDAO										reids集群B
			 * systemTime									系统时间
			 * logDir										日志文件的存放目录，配置文件中设置
			 * counterName									当前统计节点服务名称，配置文件中，mq客户端id；amq.clientId
			 * h											线程未监测异常捕获对象
			 * stopWriteLogWhenQueueSizeGreaterThen			队列中有多少数据就停止写入数据了
			 */
			countThreads[i] = new CountThread(i, queues[i], campaignDAO, freqDataDAO, distinctDAO, countDAO, countDAO4Click, clickDAO, systemTime, logDir, counterName, h, stopWriteLogWhenQueueSizeGreaterThen);
			//设置线程的未补货异常，对象
			countThreads[i].setUncaughtExceptionHandler(h);
			//启动这个线程
			countThreads[i].start();
			try { Thread.sleep(10); } catch (InterruptedException e) {}
		}

		timer.timing(new TimerTask() {

			private String nodeName = "Counter[" + serviceAddresses + "]";
			
			private long lpv, lreq;
			private StringBuilder sb = new StringBuilder();

			@Override
			public void run() {
				// 每3秒发一次心跳
				int i = 0;
				for(CountThread t : countThreads) {
					int status = t.isThreadAlive();
					switch (status) {
					case 0:
						i++;
						break;
					case 1:
						log.error("日志线程" + t.getName() + "已死亡");
						break;
					case 2:
						log.error("统计线程" + t.getName() + "已死亡");
						break;
					case 3:
						log.error("日志和统计线程" + t.getName() + "已死亡");
						break;
					}
				}
				if(i < countThreadNum) {
					log.error("线程只剩下" + i + "个，请检查");
				} else {
					// 发送心跳消息，给引擎，通知引擎，统计系统在正常运转
					try {
						jmsTemplate.send(new MessageCreator() {
							@Override
							public Message createMessage(Session session) throws JMSException {
								HeartBeatMsg msg = new HeartBeatMsg();
								//这里的nodeName是，统计系统所在服务的ip:端口
								msg.setNodeName(nodeName);
								return session.createTextMessage(JSON.toJSONString(msg, SerializerFeature.WriteClassName));
							}
						});
					} catch (JmsException e) {
						log.error("发送心跳消息发生错误", e);
					}
				}
				//输出控制到控制台，曝光数，请求数
				long pv = PV.get();
				long req = REQ.get();
				//pv:req:qps每秒钟pv:rqps每秒的请求数
				sb.append("PV: ").append(pv).append("\tREQ: ").append(req).append("\tQPS: ").append((pv - lpv) / 3).append("\tRQPS: ").append((req - lreq) / 3);
				lpv = pv;
				lreq = req;
				//输出到控制台
				log.info(sb.toString());
				sb.setLength(0);
			}

			@Override
			public Type type() { return Type.INTERVAL; }
			@Override
			public long delayOrIntervalMillis() { return 3000; }
			@Override
			public boolean isTriggerIndependently() { return false; }
		});
	}
	
	/**
	 * 该方法会在spring销毁的时候执行<br/>
	 * 将每个处理统计的线程，从数组中取出，一个一个的销毁线程
	 * 
	 * @return void
	 */
	@PreDestroy
	public void destroy() {
		for(int i = 0 ; i < countThreadNum; i++) {
			countThreads[i].destroy();
		}
	}
	
	/**
	 * 增加请求次数，这里只要是统计系统接受到一次请求，这个数就会增加
	 */
	@Override
	public void increPvReq() {
		REQ.getAndIncrement();
	}

	/* (non-Javadoc)
	 * @see com.doddata.net.service.CountService#increPv(com.doddata.net.po.PV)
	 * 增加pv数，就是统计系统采集到的广告曝光。<br/>
	 * 由于系统处理统计计数是，通过多线程的方式实现；至于处理的线程个数，是通过配置文件配置，例如：countThreadNum=8<br/>
	 * 那么这个方法的主要作用，是将这次pv计数的操作，均匀的分布到不同的队列中；而这些队列是不同的线程进行处理的。
	 */
	@Override
	public void increPv(PV pv) {
		//增加本地变量，qpvindex
		int idx = qpvindex.getAndIncrement();
		//如果pv的索引超过了处理的线程总数，则将idx设置为0，并将qpvindex设置为1
		if(idx >= countThreadNum) {
			idx = 0;
			qpvindex.set(1);
		}
		//将这次PV的对象放到队列数组 queues[idx] 中
		queues[idx].offer(pv);
		log.info("queues队列数据:"+queues);
		//增加PV次数
		PV.getAndIncrement();
	}

	/* (non-Javadoc)
	 * @see com.doddata.net.service.CountService#increClick(com.doddata.net.po.Click)
	 */
	@Override
	public void increClick(Click click) {
		//向redis集群B中添加一个click对象
		clickDAO.addClick(click);
		//向redis集群C中添加一个click对象
		click90DAO.addClick(click);
		//以下的逻辑是将点击的后续操作处理，均匀的分配到多个队列中，由多个线程处理。
		int idx = qclkindex.getAndIncrement();
		if(idx >= countThreadNum) {
			idx = 0;
			qclkindex.set(1);
		}
		queues[idx].offer(click);
	}

	/* (non-Javadoc)
	 * @see com.doddata.net.service.CountService#increLanding(com.doddata.net.po.Landing)
	 * 和方法 increPv 一样，该方法是将这次landing数据后续的处理，均匀的分配到不同的队列中，由多个线程处理。
	 */
	@Override
	public void increLanding(Landing landing) {
		
		int idx = qldindex.getAndIncrement();
		if(idx >= countThreadNum) {
			idx = 0;
			qldindex.set(1);
		}

		queues[idx].offer(landing);
	}
}
