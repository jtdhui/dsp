package com.jtd.collector.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.jtd.collector.dao.CampaignDAO;
import com.jtd.collector.dao.CountDAO;
import com.jtd.collector.util.SystemTime;
import com.jtd.collector.util.Timer;
import com.jtd.collector.util.TimerTask;
import com.jtd.collector.util.TrafficHoursTrendsUtil;
import com.jtd.web.constants.CampaignManulStatus;
import com.jtd.web.jms.HeartBeatMsg;
import com.jtd.web.model.Campaign;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import javax.sql.DataSource;
import java.util.concurrent.Semaphore;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月12日
 * @项目名称 dsp-cache-collector
 * @描述 <p></p>
 */
@Component("collectService")
public class CollectServiceImpl implements CollectService {

	private static final Log log = LogFactory.getLog(CollectServiceImpl.class);
	//操作本地redis
	@Resource
	private CampaignDAO campaignDAO;
	//操作redis集群B
	@Resource
	private CountDAO countDAO;
	//mysql数据
	@Resource
	private DataSource dataSource;

	@Resource
	private Timer timer;

	@Resource
	private SystemTime systemTime;

	@Resource
	private JmsTemplate jmsTemplate;
	//交易趋势数据，这里面是一天中每个小时对应的投放比例。
	@Resource
	private TrafficHoursTrendsUtil trafficHoursTrendsUtil;

	// 监控和汇总线程
	private Monitor monitor;
	private Collector collector;

	/**
	 * 汇总Redis里的统计数据然后写入MySQL<br/>
	 * 该方法在spring启动完成后，由容器调用
	 * 
	 */
	@PostConstruct
	public void collect() {

		// 启动监控, 每5秒扫一次
		final Semaphore semphore = new Semaphore(1);
		//监控逻辑
		monitor = new Monitor(semphore, campaignDAO, countDAO, systemTime, jmsTemplate, trafficHoursTrendsUtil);
		//每5秒钟，释放一次线程资源
		timer.timing(new TimerTask() {
			public void run() { semphore.release(); }
			@Override
			public Type type() { return Type.INTERVAL; }
			@Override
			public long delayOrIntervalMillis() { return 5000l; }//每5秒钟，执行一次
			@Override
			public boolean isTriggerIndependently() { return false; }
		});

		// 启动汇总线程，将redisB中的统计数据，写到mysql中
		collector = new Collector(campaignDAO, countDAO, dataSource, systemTime, timer);
		collector.start();

		timer.timing(new TimerTask() {
			@Override
			public void run() {
				// 每3秒发一次心跳，如果统计线程和监控线程，都是激活状态，则向引擎发送心跳消息
				if (collector.isAlive() && monitor.isAlive()) {
					try {
						jmsTemplate.send(new MessageCreator() {
							@Override
							public Message createMessage(Session session) throws JMSException {
								HeartBeatMsg msg = new HeartBeatMsg();
								msg.setNodeName("Collector");
								return session.createTextMessage(JSON.toJSONString(msg, SerializerFeature.WriteClassName));
							}
						});
					} catch (JmsException e) {
						log.error("发送心跳消息发生错误", e);
					}
				} else {
					if (!collector.isAlive()) {
						log.error("Collector线程状态异常");
					}
					if (!monitor.isAlive()) {
						log.error("Monitor线程状态异常");
					}
				}
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
	 * 当系统关闭时，由spring调用该方法，用户结束监控和统计线程
	 * @return void
	 */
	@PreDestroy
	public void destroy() {
		monitor.interrupt();
		collector.interrupt();
	}

	/**
	 * 接收admin中发送过来的活动数据，如果没有活动数据则添加reids，如果有则更新
	 */
	@Override
	public boolean updateCampaign(Campaign campaign) {
		return monitor.updateCampaign(campaign);
	}

	/**
	 * 接收admin中发送过来的手动状态，修改reids中的Campaign中得手动状态。
	 */
	@Override
	public boolean changeManulStatus(long campaignId, CampaignManulStatus status) {
		return monitor.changeManulStatus(campaignId, status);
	}
}
