package com.jtd.logcollector.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.jtd.logcollector.dao.CookieDataDAO;
import com.jtd.logcollector.service.Collector.TableDesc;
import com.jtd.logcollector.service.ReWriter.Task;
import com.jtd.logcollector.util.IPBUtil;
import com.jtd.logcollector.util.SystemTime;
import com.jtd.logcollector.util.Timer;
import com.jtd.logcollector.util.TimerTask;
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
import javax.sql.DataSource;
import java.io.File;
import java.io.FileFilter;
import java.sql.Connection;
import java.sql.Statement;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月12日
 * @项目名称 dsp-log-collector
 * @描述 <p>
 * 			1、该类是整个logCollect的入口。
 * 			2、在启动的时候会实例化，入库程序Collector对象数据,个数根据配置文件中CollectorThreadNum而定。
 * 			3、刚启动后就先进行一次，拆分和入库。
 * 			4、之后每30秒扫描一次，如果时间分别到了0:15,9:15,16:15时，分别进行一次分拆和入库操作。
 * 			   1、如果时间没有到 0:15,9:15,16:15 这些时间点，则每小时都做一次拆分。
 * 		</p>
 */
@Component
public class CollectServiceImpl implements CollectService {

	private static final Log log = LogFactory.getLog(CollectServiceImpl.class);

	@Resource
	private Timer timer;

	@Resource
	private SystemTime systemTime;

	@Resource
	private JmsTemplate jmsTemplate;
	
	//redisA 索引15的访问层
	@Resource
	private CookieDataDAO cookieDataDAO;
	
	//统计数据的数据源
	@Resource(name = "countDataSource")
	private DataSource countDataSource;
	
	//业务库的数据源
	@Resource(name = "webDataSource")
	private DataSource webDataSource;
	
	//源目录
	@Value("${rawLogDir}")
	private String rawLogDir;
	
	//目标目录
	@Value("${destLogDir}")
	private String destLogDir;
	
	//收集数据线程的线程数
	@Value("${CollectorThreadNum}")
	private int CollectorThreadNum;

	@Resource
	private IPBUtil iPBUtil;

	// 汇总线程
	private Collector[] collectors;

	/* (non-Javadoc)
	 * @see net.doddata.collector.service.CollectService#collect()
	 */
	@PostConstruct
	public void collect() {
		//创建源目录
		File raw = new File(rawLogDir);
		if(!raw.exists()) raw.mkdirs();
		
		//创建目标目录
		final File dst = new File(destLogDir);
		if (!dst.exists()) dst.mkdirs();
		
		/**
		 * 初始化数据入库程序，这些线程实例化后立即启动，Collector 线程使用 LinkedBlockingQueue<long[]> 和ReWriter通讯；
		 * ReWritier向队列中写数据，而Collector将以广告主id拆分好的数据写入到数据中
		 */
		// z，启动数据汇总线程
		//CollectorThreadNum为配置文件中，配置的收集数据的线程数。
		collectors = new Collector[CollectorThreadNum];
		//实例化每个Collector对象，并将实化的Collector对象放到collectors数组中。
		//随后起送 collector 线程
		for(int i = 0; i < CollectorThreadNum; i++) {
			collectors[i] = new Collector(i, cookieDataDAO, countDataSource, webDataSource, dst, iPBUtil);
			collectors[i].start();
			try { Thread.sleep(10); } catch (InterruptedException e) {}
		}
		
		//把顺序写的日志文件按广告主拆分
		//实例化拆分日志对象
		final ReWriter rewriter = new ReWriter(raw, dst);

		// z，启动时汇总一次
		/**
		 * 一、这里是将 raw 源目录下的日志，以广告主id拆分。
		 * 二、拆分后的目录和文件：
		 *  1、"目标目录destDir/广告主id/pv/yyyyMMddHH.txt"
		 *  2、"目标目录destDir/广告主id/click/yyyyMMddHH.txt"
		 *  3、"目标目录destDir/广告主id/landing/yyyyMMddHH.txt"
		 * 三、拆分后，立即调用,当前类的 doCollectc方法
		 *  
		 */
		rewriter.rewrite(new Task(systemTime.getYyyyMMddHH(), new Runnable(){
			public void run(){
				doColletc(dst);
			}
		}));

		// 每天的0点，9点和16点15分时运行一次
		timer.timing(new TimerTask() {
			private int ymd = systemTime.getYyyyMMdd();
			private int hour = systemTime.getHour();
			@Override
			public void run() {
				//当前时间的 yyyyMMdd
				int nowYmd = systemTime.getYyyyMMdd();
				//当前时间的分钟数
				int minute = systemTime.getMinute();
				//如果ymd和线程刚获取的yyyyMMdd不相等，表示线程执行时跨天了
				//这样也代表着当前小时数为0点15分的时候，执行分拆和入库操作
				if(ymd != nowYmd) {
					if(minute == 15) {
						//表示是前一天的23点，例如：ymd * 100 + 23=2016111223
						//拆分程序，拆分前一天23点的数据，拆分后，生成如下数据，供Collector读取入库
						/**
						 * 1、"目标目录destDir/广告主id/pv/yyyyMMddHH.txt"
						 * 2、"目标目录destDir/广告主id/click/yyyyMMddHH.txt"
						 * 3、"目标目录destDir/广告主id/landing/yyyyMMddHH.txt"
						 */
						rewriter.rewrite(new Task(ymd * 100 + 23, new Runnable(){
							private int t = ymd;
							public void run() {
								doColletc(dst, t);
							}
						}));
						//将当前时间赋值给ymd
						ymd = nowYmd;
						return;
					}
				} else {//ymd == nowYmd，说明线程执行的时候，没有跨天
					//获取当前时间，小时数
					int nowHour = systemTime.getHour();
					//如果 hour != nowHour 说明可以执行分拆入库。
					if(hour != nowHour) {
						//如果分钟数为15，则执行
						if(minute == 15) {
							//如果当前小时数是9点15分，和16点15分，则执行分拆和入库
							switch (nowHour) {
							case 9:
							case 16:
								rewriter.rewrite(new Task(ymd * 100 + hour, new Runnable(){
									public void run() {
										doColletc(dst);
									}
								}));
								break;
							default:
								//其它小时的15分的时候，都只进行分拆
								rewriter.rewrite(new Task(ymd * 100 + hour));
							}
							hour = nowHour;
						}
					}
				}
			}
			@Override
			public Type type() { return Type.INTERVAL; }
			@Override
			public long delayOrIntervalMillis() { return 30000; }//每30秒，跑一次run方法
			@Override
			public boolean isTriggerIndependently() { return false; }
		});

		timer.timing(new TimerTask() {
			@Override
			public void run() {
				// 每3秒发一次心跳
				int alive = 0;
				for(int i = 0; i < CollectorThreadNum; i++) {
					if(collectors[i].isAlive()) {
						alive++;
					} else {
						log.error("CollectorThread-" + i + "状态异常");
					}
				}
				//如果线程的活跃个数和入库程序的线程个数相等，则发送心跳消息。
				if (alive == CollectorThreadNum) {
					try {
						jmsTemplate.send(new MessageCreator() {
							@Override
							public Message createMessage(Session session) throws JMSException {
								HeartBeatMsg msg = new HeartBeatMsg();
								msg.setNodeName("LogCollector");
								return session.createTextMessage(JSON.toJSONString(msg, SerializerFeature.WriteClassName));
							}
						});
					} catch (JmsException e) {
						log.error("发送心跳消息发生错误", e);
					}
				} else {
					log.error("Collector线程只剩下" + alive + "个, 请检查");
				}
			}

			@Override
			public Type type() { return Type.INTERVAL; }
			@Override
			public long delayOrIntervalMillis() { return 3000; }//每3秒钟发送一次心跳消息
			@Override
			public boolean isTriggerIndependently() { return false; }
		});
	}
	
	/**
	 * 执行日志收集程序
	 * @param dir			拆分后的日志文件总目录
	 * @return void
	 */
	private void doColletc(File dir) {
		log.info(dir+"<<<<<<<<<<<<<<<<<<");
		doColletc(dir, systemTime.getYyyyMMdd());
	}
	private void doColletc(File dir, int ymd) {
		
		// z，先把当天的数据清除
		//删除当天的临时表
		clearData(ymd);
		
		//获取所有目录下所有的文件，这里每个目录名就是一个 广告主id 
		File[] partnerdirs = dir.listFiles(new FileFilter() {
			@Override
			public boolean accept(File f) {
				return f.isDirectory();
			}
		});
		
		int i = 0;
		//循环目录集合
		for (File pd : partnerdirs) {
			//将文件均匀的添加到，就是如果CollectorThreadNum=8，则service在spring启动的时候，就会生成8个Collector对象，放到collectors数组中。
			//而Collector对象中有用户存放任务的队列，而下面程序就是轮训的把目录下的文件，均匀的放到 Collector对象中，分别执行
			if (i == CollectorThreadNum) i = 0;
			/**
			 * Collector中 LinkedBlockingQueue<long[]> taskQueue 队列中存储的数据为 long[]数组；
			 * 而 taskQueue队列中，只要接收到数据，则立刻运转。
			 * long[]数据结构：
			 * 		[0]=广告主id，就是拆分后的目录名
			 * 		[1]=yyyyMMdd
			 */
			log.info("pd.getName():"+pd.getName()+">>"+ymd);
			collectors[i++].addTask(new long[] { Long.parseLong(pd.getName()), ymd });
		}
	}
	
	/**
	 * 清除当天的数据
	 * @param ymd				yyyyMMdd
	 * @return void
	 */
	private void clearData(int ymd) {
		//jdbc 链接
		Connection conn = null;
		try {
			//从数据源中获取链接
			conn = countDataSource.getConnection();
			//将链接的事务提交，设置为非自动提交
			conn.setAutoCommit(false);
			//从链接中获取Statement
			Statement delst = conn.createStatement();
			//获取Coolector类操作的表，对象
			for(TableDesc desc : Collector.tableDesc) {
				//删除当天的表
				delst.execute("delete from " + desc.getTableName() + " where date=" + ymd);
				log.info("删除表" + desc.getTableName() + "的" + ymd + "数据");
			}
			//提交事务
			conn.commit();
			log.info("删除" + ymd + "数据成功");
		} catch (Exception e) {
			log.error("删除" + ymd + "数据发生错误", e);
			try { conn.rollback(); } catch (Exception e1) {}
		} finally {
			if (conn != null) {
				try { conn.close(); } catch (Exception e) {}
			}
		}
	}
	
	/**
	 * 当spring销毁时，一个一个的将线程销毁
	 * 
	 * @return void
	 */
	@PreDestroy
	public void destroy() {
		for (Thread t : collectors) t.interrupt();
	}
}
