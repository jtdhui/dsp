package com.jtd.collector.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.jtd.collector.dao.CampaignDAO;
import com.jtd.collector.dao.CountDAO;
import com.jtd.collector.util.SystemTime;
import com.jtd.collector.util.TrafficHoursTrendsUtil;
import com.jtd.web.constants.BudgetCtrlType;
import com.jtd.web.constants.CampaignAutoStatus;
import com.jtd.web.constants.CampaignManulStatus;
import com.jtd.web.jms.ChangeAutoStatusMsg;
import com.jtd.web.jms.ChangeCampCtrMsg;
import com.jtd.web.jms.ChangeCampPriceFactorMsg;
import com.jtd.web.jms.SetCampHourPauseMsg;
import com.jtd.web.model.Campaign;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月12日
 * @项目名称 dsp-cache-collector
 * @描述 <p></p>
 */
class Monitor {

	private static final Log log = LogFactory.getLog(Monitor.class);

	// 当前所有正在投放的推广活动信息，要输出的文件
	private static final String ALL_CAMP_INFO_FILE = "allcamp.txt";

	// 当前正在投放的所有的活动, key campid
	private ConcurrentHashMap<Long, Campaign> allCamps = new ConcurrentHashMap<Long, Campaign>();

	// 等待重新加载的活动的队列
	private LinkedBlockingQueue<Long> waitingReloadCamp = new LinkedBlockingQueue<Long>();
	// 等待改变状态的队列
	private LinkedBlockingQueue<long[]> waitingChangeStatus = new LinkedBlockingQueue<long[]>();

	// 监控，计数和数据通知线程
	private MonitorThread monitorThread;
	//统计线程
	private CountThread countThread;
	//通知线程
	private NotifyThread notifyThread;

	// 触发监控的信号量
	private Semaphore semphore;
	//本地reids访问层
	private CampaignDAO campaignDAO;
	//redisB,访问层
	private CountDAO countDAO;
	private SystemTime systemTime;
	private JmsTemplate jmsTemplate;
	private TrafficHoursTrendsUtil trafficHoursTrendsUtil;

	/**
	 * 构造监控任务,包可见
	 * @param semphore								信号量，是负责协调各个线程, 以保证它们能够正确、合理的使用公共资源。也是操作系统中用于控制进程同步互斥的量。
	 * @param campaignDAO							
	 * @param countDAO
	 * @param systemTime
	 * @param jmsTemplate
	 * @param trafficHoursTrendsUtil
	 */
	Monitor(Semaphore semphore, CampaignDAO campaignDAO, CountDAO countDAO, SystemTime systemTime, JmsTemplate jmsTemplate, TrafficHoursTrendsUtil trafficHoursTrendsUtil) {
		this.semphore = semphore;
		this.campaignDAO = campaignDAO;
		this.countDAO = countDAO;
		this.systemTime = systemTime;
		this.jmsTemplate = jmsTemplate;
		this.trafficHoursTrendsUtil = trafficHoursTrendsUtil;
		loadAll();
		/**
		 * 在一个线程中，有一些是未捕获异常；这些未捕获异常，在线程中被抛出，都会被这个对象捕获；
		 * 主要作用是保证线程不会停止的一直运转下去。
		 */
		UncaughtExceptionHandler h = new UncaughtExceptionHandler(){
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				log.error("线程" + t.getName() + "发生异常", e);
			}
		};
		
		//创建一个监控线程并启动
		monitorThread = new MonitorThread();
		monitorThread.setUncaughtExceptionHandler(h);
		monitorThread.start();
		
		//创建一个统计线程并启动
		countThread = new CountThread();
		countThread.setUncaughtExceptionHandler(h);
		countThread.start();
		
		//创建一个通知线程并启动
		notifyThread = new NotifyThread();
		notifyThread.setUncaughtExceptionHandler(h);
		notifyThread.start();
	}

	/**
	 * @作者 Amos Xu
	 * @版本 V1.0
	 * @配置 
	 * @创建日期 2016年11月10日
	 * @项目名称 dsp-cache-collector
	 * @描述 <p>
	 * 		监控线程功能：<br/>
	 * 		1、加载从MQ发送过来的活动，如果本地reids中有这个活动则重新加载，否则添加。活动对象存在 allCamps Map中，同时存储在本地redis key=CMP活动id对应的value中。
	 * 		2、拿到从MQ发送过来的修改活动手动状态的消息，和在方法执行过程中，填充的需要修改自动状态的队列；修改活动的自动状态，和手动状态。
	 * 			在修改状态的过程中，如果，手动状态为 OFFLINE，或是自动状态为 FINISHED，则将该活动添加到待删除列表中。
	 * 		3、把待删除活动，从本地内存和本地redis中删除。
	 * 		4、调用scan方法，重新扫描一遍，扫描一遍allCamps里的所有的活动，根据活动当前状态，和投放时间和时段，修改活动的状态。
	 * 		</p>
	 */
	private class MonitorThread extends Thread {
		
		private Comparator<Campaign> comparator = new Comparator<Campaign>() {
			/**
			 * 定义比较器，两个id相减，如果返回负数，说明 c1比c2小；如果返回正数说明，c1比2大；如果返回0说明相等。
			 */
			@Override
			public int compare(Campaign c1, Campaign c2) {
				return (int) (c1.getId() - c2.getId());
			}
		};

		private int[][] defaultWh = new int[7][24];

		public MonitorThread() {
			super("MonitorThread");
			for (int i = 0; i < 7; i++) {
				//实例第二维数组
				//实际的操作就是，defaultWh[0]=new int[24]；一直到defaultWh[6]=new int[24]；
				defaultWh[i] = new int[24];
				//给第二维数组，中的24个位置，全部填充上 1 
				Arrays.fill(defaultWh[i], 1);
			}
		}

		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		public void run() {

			log.debug("MonitorThread is running");
			
			//需要加载的
			List<Long> needReload = new ArrayList<Long>();
			//需要删除的
			List<Long> needRemove = new ArrayList<Long>();
			//需要改变的
			List<long[]> needChangest = new ArrayList<long[]>();
			
			//如果线程没有被阻断，则一直循环下去
			for (; !Thread.interrupted();) {
				//当前线程获取资源访问权限
				try {
					semphore.acquire();
				} catch (InterruptedException e) {
					break;
				}

				// 先把要重新加载的加载，取出队列中排在首位的对象，如果娶不到则返回null
				// 如果能取到，则将从消息队列中最新拿到的活动id，添加到数据 needReload 里。
				for (Long id = waitingReloadCamp.poll(); id != null; id = waitingReloadCamp.poll()) {
					needReload.add(id);
				}
				//如果有需要从新加载的活动，则进行下列操作
				if(needReload.size() > 0) {
					// 先删除本地缓存，所有正在投放的广告活动
					for(Long id : needReload){
						allCamps.remove(id);
					}

					// 挨个加载
					//然后在一个一个的从本地reids中，将活动加载到本地内存中
					for(Long id : needReload){
						Campaign c = campaignDAO.getCampaignById(id);
						if(c == null) {
							log.error("等待加载的活动没有找到, ID: " + id);
							continue;
						}
						//加载活动,将选择了渠道，渠道id不为null，自动状态不为 完成 ，手动状态不为 下线的活动放到
						//allCamps集合中  key=活动id，value=活动对象
						load(c);
					}
					//重新加载完，将needReload清空。
					needReload.clear();
				}

				// 修改状态，从队列中取出需要修改状态的对象，对象是数组，[0]=活动状态；[1]=活动标识，1为自动状态，0为手动状态；[2]=要修改成的状态码
				for (long[] st = waitingChangeStatus.poll(); st != null; st = waitingChangeStatus.poll()) {
					needChangest.add(st);
				}
				//循环需要修改状态的集合
				for (long[] st : needChangest) {
					//使用活动id，从再投活动的集合中取出活动对象
					Campaign camp = allCamps.get(st[0]);
					if(camp == null) continue;
					//修改修改手动状态
					if(st[1] == 0) {
						CampaignManulStatus s = CampaignManulStatus.fromCode((int)st[2]);
						CampaignManulStatus old = camp.getManulStatus();
						camp.setManulStatus(s);
						log.debug("手动状态变更: " + camp.getPartnerId() + ":" + camp.getPartnerName() + " | " + camp.getGroupId() + ":" + camp.getGroupName()+ " | " + camp.getId() + ":"+ camp.getCampName() + " | " + old.getDesc() + " --> " + camp.getManulStatus().getDesc() + " | " + camp.getAutoStatus().getDesc() + "\r\n");
						campaignDAO.changeManulStatus(camp.getId(), s);
						//如果手动状态为投放结束，则将活动id添加到待删除的列表中
						if(s == CampaignManulStatus.OFFLINE) needRemove.add(st[0]);
					} else if(st[1] == 1) {
						//修改自动状态
						CampaignAutoStatus s = CampaignAutoStatus.fromCode((int)st[2]);
						CampaignAutoStatus old = camp.getAutoStatus();
						camp.setAutoStatus(s);
						doChangeAutoStatus(st[0], s);
						log.debug("自动状态变更: " + camp.getPartnerId() + ":" + camp.getPartnerName() + " | " + camp.getGroupId() + ":" + camp.getGroupName()+ " | " + camp.getId() + ":"+ camp.getCampName() + " | " + camp.getManulStatus().getDesc() + " | " + old.getDesc() + " --> " + camp.getAutoStatus().getDesc() + "\r\n");
						//如果活动自动状态为完成，则将活动id添加到，待删除的列表中
						if(s == CampaignAutoStatus.FINISHED) needRemove.add(st[0]);
					}
				}
				//清空需要改变状态的集合
				needChangest.clear();
				
				
				// 把下线的删除
				// 把手动下线，自动完成的活动，从待投放的集合中删除
				if(needRemove.size() > 0) {
					for(Long id : needRemove){
						allCamps.remove(id);
					}
					needRemove.clear();
				}

				// 把所有在投的扫一遍
				scan();
			}
		}

		/**
		 * 显示状态
		 * 
		 * @throws Exception
		 */
		private void scan() {
			
			//再投的活动输出的文件
			File f = new File(ALL_CAMP_INFO_FILE);
			BufferedWriter w = null;
			try {
				w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f), "UTF-8"));
				//将本地内存中所有再投的所有活动取出，将活动对象放到 list 中。
				List<Campaign> list = new ArrayList<Campaign>();
				for(Iterator<Campaign> it = allCamps.values().iterator(); it.hasNext();){
					Campaign c = it.next();
					list.add(c);
				}
				//将list转换成 Campaign数组。
				Campaign[] camps = list.toArray(new Campaign[list.size()]);
				//给数组排序
				Arrays.sort(camps, comparator);

				// 星期和小时和当前时间
				int week = systemTime.getWeek();
				int hour = systemTime.getHour();
				long now = systemTime.getTime();
				
				//循环活动数组
				for(Campaign camp : camps) {
					//拿到活动开始时间
					long timebegin = camp.getStartTime().getTime();
					//活动的结束时间，这里先预设一个long最大值
					long timeend = Long.MAX_VALUE;
					//取出活动的结束时间
					Date endt = camp.getEndTime();
					if (endt != null) {
						timeend = endt.getTime() + 86399999l;
					}
					//取出活动的投放时段，这里使用一个二维数组，第一维代表周一到周日，第二维代表24小时
					//从活动中取出周、小时数据
					String weekhour = camp.getWeekhour();
					int[][] wh = null;
					//如果周小时数据不为null
					if (!StringUtils.isEmpty(weekhour)) {
						//如果取出的字符串是以 双引号开头，则去掉第一个引号
						if(weekhour.startsWith("\""))weekhour = weekhour.substring(1);
						//如果取出的字符串十一 双引号结尾，则去掉最后一个引号
						if(weekhour.endsWith("\""))weekhour = weekhour.substring(0, weekhour.length() - 1);
						//将字符串，转换成二维数据组对象
						wh = JSON.parseObject(weekhour, int[][].class);
						//如果取出的周、时段大小为0，则默认全选，就是全时段都投放
						if(wh.length == 0) wh = defaultWh;
					} else {
						// 没填则是全选
						wh = defaultWh;
					}
					
					//如果自动状态为，未开始则判断是否到达了投放时段
					if (camp.getAutoStatus() == CampaignAutoStatus.READY) {
						//判断当前时间是否大于开始时间，并且当前时间是否小于结束时间，并且将当前周和小时数据取出来，如果==1说明当前周和小时允许投放
						if (now >= timebegin && now <= timeend && wh[week][hour] == 1) {

							// 将状态修改为投放中
							doChangeAutoStatus(camp.getId(), CampaignAutoStatus.ONLINE);
							//获取活动中原有状态
							CampaignAutoStatus old = camp.getAutoStatus();
							//将本地缓存中活动的自动状态，修改为投放中
							camp.setAutoStatus(CampaignAutoStatus.ONLINE);
							//向 “allcamp.txt” 文件输出，活动的各项值，和活动的状态变化。
							showStatus(camp, w, old);
						} else if (now > timeend) {
							//如果当前时间大于活动投放的结束时间，则将状态改为自动结束
							//到下线了还是待投放,直接下线
							doChangeAutoStatus(camp.getId(), CampaignAutoStatus.FINISHED);
							CampaignAutoStatus old = camp.getAutoStatus();
							//修改活动自动状态为，自动结束
							camp.setAutoStatus(CampaignAutoStatus.FINISHED);
							//向 “allcamp.txt” 文件输出活动的各项值，并输出活动的
							showStatus(camp, w, old);
						} else {

							// 还没到投放时间，继续等
							showStatus(camp, w);
						}
					}
					//当自动状态为投放中时
					if (camp.getAutoStatus() == CampaignAutoStatus.ONLINE) {
						//判断是否已经到了投放结束的时间，如果是，则将自动状态改为自动结束
						if (now > timeend) {
							
							// 到下线了,下线；修改内存中和本地reids中活动的自动状态，然后向MQ中发送自动状态修改消息。
							doChangeAutoStatus(camp.getId(), CampaignAutoStatus.FINISHED);
							CampaignAutoStatus old = camp.getAutoStatus();
							camp.setAutoStatus(CampaignAutoStatus.FINISHED);
							//向 “allcamp.txt” 文件输出活动的各项值，并输出活动的
							showStatus(camp, w, old);
						} else if (now >= timebegin && now <= timeend && wh[week][hour] == 0) {
							//如果活动的开始时间小于现在时间，并且结束时间大于现在的时间，并且投放时段为0，则自动状态为暂停
							// 到暂停了，修改内存和本地reids中活动的状态，并向mq中发送消息；消息为自动下线-不在投放时段
							doChangeAutoStatus(camp.getId(), CampaignAutoStatus.PAUSE_NOTTIME);
							CampaignAutoStatus old = camp.getAutoStatus();
							camp.setAutoStatus(CampaignAutoStatus.PAUSE_NOTTIME);
							//向 “allcamp.txt” 文件输出活动的各项值，并输出活动的
							showStatus(camp, w, old);
						} else if(now < timebegin) {
							// 把开始时间往后推了
							//如果开始时间大于现在时间，说明修改了开始时间；这时将自动状态修改为未开始，并发送消息到MQ中
							doChangeAutoStatus(camp.getId(), CampaignAutoStatus.READY);
							CampaignAutoStatus old = camp.getAutoStatus();
							camp.setAutoStatus(CampaignAutoStatus.READY);
							//向 “allcamp.txt” 文件输出活动的各项值，并输出活动的
							showStatus(camp, w, old);
						} else {
							//以上条件都不满足，所有活动还是投放中状态，不修改任何状态
							//向 “allcamp.txt” 文件输出活动的各项值，并输出活动的
							showStatus(camp, w);
						}
					}
					
					//如果活动自动状态是 自动下线-不在投放时段
					if (camp.getAutoStatus() == CampaignAutoStatus.PAUSE_NOTTIME) {
						
						//先判断是否到了活动的投放结束时间
						if (now > timeend) {

							// 到下线了,下线
							doChangeAutoStatus(camp.getId(), CampaignAutoStatus.FINISHED);
							CampaignAutoStatus old = camp.getAutoStatus();
							camp.setAutoStatus(CampaignAutoStatus.FINISHED);
							showStatus(camp, w, old);
						} else if (now >= timebegin && now <= timeend && wh[week][hour] == 1) {
							//判断当前时间是否在活动投放的开始时间和结束时间之间，并且活动是否在投放时段内；
							//如果是，在修改自动状态为投放中
							// 可以投放了
							doChangeAutoStatus(camp.getId(), CampaignAutoStatus.ONLINE);
							CampaignAutoStatus old = camp.getAutoStatus();
							camp.setAutoStatus(CampaignAutoStatus.ONLINE);
							showStatus(camp, w, old);
						} else if(now < timebegin) {
							// 把开始时间往后推了
							doChangeAutoStatus(camp.getId(), CampaignAutoStatus.READY);
							CampaignAutoStatus old = camp.getAutoStatus();
							camp.setAutoStatus(CampaignAutoStatus.READY);
							showStatus(camp, w, old);
						} else {
							showStatus(camp, w);
						}
					}
					//如果活动的自动状态，为自动下线-到达当日目标
					if (camp.getAutoStatus() == CampaignAutoStatus.PAUSE_ACCOMPLISH_GOALS) {
						if (now > timeend) {
							// 到下线了,下线
							doChangeAutoStatus(camp.getId(), CampaignAutoStatus.FINISHED);
							CampaignAutoStatus old = camp.getAutoStatus();
							camp.setAutoStatus(CampaignAutoStatus.FINISHED);
							showStatus(camp, w, old);
						} else if(now < timebegin) {
							// 把开始时间往后推了
							doChangeAutoStatus(camp.getId(), CampaignAutoStatus.READY);
							CampaignAutoStatus old = camp.getAutoStatus();
							camp.setAutoStatus(CampaignAutoStatus.READY);
							showStatus(camp, w, old);
						} else {
							showStatus(camp, w);
						}
					}
					//如果活动自动状态为，投放结束，则将reids从本地缓存中删除
					if (camp.getAutoStatus() == CampaignAutoStatus.FINISHED) {
						allCamps.remove(camp.getId());
						showStatus(camp, w);
					}
				}
				//将缓存中的内容，输出到文件
				w.flush();
			} catch (Exception e) {
				log.error("监控任务发生错误", e);
			} finally {
				if (w != null) {
					try {
						w.close();
					} catch (Exception e) {
					}
				}
			}
		}
	}

	/**
	 * 不断监视各计划的投放量，到量了或者预算不足了或亏血本了则暂停或者下线
	 * @作者 Amos Xu
	 * @版本 V1.0
	 * @配置 
	 * @创建日期 2016年11月10日
	 * @项目名称 dsp-cache-collector
	 * @描述 <p>统计监控线程</p>
	 */
	private class CountThread extends Thread {

		public CountThread() {
			super("CountThread");
		}

		public void run() {
			log.debug("CountThread is running");

			// 缓存推广组的日量和总量，这里目前设置的都是最大值，所以理论上推广组中设置的条件，不起作用。
			Map<Long, int[]> groupDayCount = new TreeMap<Long, int[]>();
			Map<Long, long[]> groupTotalCount = new TreeMap<Long, long[]>();
			
			//如果线程没有被阻断，则会一直运转下去
			for (; !Thread.interrupted();) {
				//获取当前时间的，yyyyMMdd
				int ymd = systemTime.getYyyyMMdd();
				//获取本地 allCamps ConcurrentMap 中的所有的活动，该Map中，会在获取到MQ发送的修改活动数据，和(MonitorThread中改变活动数据)在监测到活动下线后，里面的数据内容会增加或减少。
				for(Iterator<Entry<Long, Campaign>> it = allCamps.entrySet().iterator(); it.hasNext();) {

					Entry<Long, Campaign> e = it.next();
					//拿到活动id
					long campid = e.getKey();
					//拿到活动对象
					Campaign c = e.getValue();
					//拿到活动自动状态
					CampaignAutoStatus status = c.getAutoStatus();
					//从集群B中获取活动的汇总数据 [0]=竞价次数，[1]=pv，[2]=uv,[3]=click,[4]=userClick,[5]=landing,[6]=userLanding,[7]=e，[8]=co
					long[] total = countDAO.getCampTotalCount(campid);
					//获取推广组数据，这里为页面中设置的推广组指标
					long[] groupTotal = groupTotalCount.get(c.getGroupId());
					//如果推广组的汇总数据为null，则总redisB中重新加载一遍，推广组汇总数据
					if(groupTotal == null) {
						//从redisB中获取推广组汇总数据
						groupTotal = countDAO.getCampGrpTotalCount(c.getGroupId());
						if(groupTotal != null) {
							//将redisB中获取的推广组汇总数据，添加到groupTotalCount中
							//key=推广组id，value=long数组，内容为：[0]=竞价次数，[1]=pv，[2]=uv,[3]=click,[4]=userClick,[5]=landing,[6]=userLanding,[7]=e，[8]=co
							groupTotalCount.put(c.getGroupId(), groupTotal);
						}
					}
					/**
					 * 		[0]=推广组日预算
					 * 		[1]=推广组总预算
					 * 		[2]=日展现量目标
					 * 		[3]=总展现量目标
					 * 		[4]=日点击量目标
					 * 		[5]=总点击量目标
					 */
					Long[] groupSetting = campaignDAO.getCampgrp(c.getGroupId());
					//------------页面中设置的活动总目标，与redisB中活动的汇总数据对比，主要是总pv，总点击，总花费的对比以及负毛利的判断--------------------
					//------------redisB，活动的汇总数量不等于null-----------------
					if(total != null) {
						// 最大负毛利额；这里暂时为null
						//如果（成本/花费）/1000后，大于最大负毛利率，则立即停止，因为不能再亏了
						//比如：由于投放设置的原因，这个活动的成本一直在增加，而花费却不增加，这样说明，dsp给adx的钱，比dsp扣除广告主的钱要多，说明亏了。
						Integer maxneggrossprofit = c.getMaxNeGrossProfit();
						if(maxneggrossprofit != null && (total[8] - total[7]) / 1000 > maxneggrossprofit) {
							// 亏得不能再亏了
							log.debug("推广活动[" + c.getId() + "]" + c.getCampName() + "达到最大负毛利额，下线");
							changeAutoStatus(campid, CampaignAutoStatus.FINISHED);
							continue;
						}

						// 活动总展现量
						Long totalPv = c.getTotalPv();
						//如果展现总量到达活动设置的展现总量，则将活动的自动状态修改为自动结束
						if(totalPv != null && totalPv > 0) {
							if(total[1] >= totalPv) {
								// 总展现量达到了，下线
								log.debug("推广活动[" + c.getId() + "]" + c.getCampName() + "达到总展现量，下线");
								changeAutoStatus(campid, CampaignAutoStatus.FINISHED);
								continue;
							}
						}
						// 活动总点击量
						Long totalClick = c.getTotalClick();
						// 活动的点击总量到达了，页面设置的活动的点击总量，则将活动的自动状态设置成 自动结束
						if(totalClick != null && totalClick > 0) {
							if(total[3] >= totalClick) {
								// 总点击量达到了，下线
								log.debug("推广活动[" + c.getId() + "]" + c.getCampName() + "达到总点击量，下线");
								changeAutoStatus(campid, CampaignAutoStatus.FINISHED);
								continue;
							}
						}
						// 活动总预算
						Long totalBudget = c.getTotalBudget();
						// 如果redisB中活动的总预算，到达页面设置的总预算，则将活动的自动状态，设置成 自动结束
						if(totalBudget != null && totalBudget > 0) {
							if(total[7] / 1000 >= totalBudget) {
								// 总点击量达到了，下线
								log.debug("推广活动[" + c.getId() + "]" + c.getCampName() + "达到总预算，下线");
								changeAutoStatus(campid, CampaignAutoStatus.FINISHED);
								continue;
							}
						}
					}
					//-------页面设置的推广组总目标数据，和redisB中推广组的汇总数据对比，主要有，推广组的总pv，总click，总花费------------
					//-------redisB中的推广组汇总数据，和页面中设置的推广组数据都不为null------------
					if(groupTotal != null && groupSetting != null) {
						// 页面-推广组总展现量
						Long totalPv = groupSetting[3];
						if(totalPv != null && totalPv > 0) {
							//如果页面中设置的推广组总展现目标，>= redisB中存储的推广组汇总数据，pv数；
							//则自动状态，设置为自动结束
							if(groupTotal[1] >= totalPv) {
								// 组总展现量达到了，下线
								log.debug("推广活动[" + c.getId() + "]" + c.getCampName() + "所属推广组达到总展现量，下线");
								changeAutoStatus(campid, CampaignAutoStatus.FINISHED);
								continue;
							}
						}
						// 页面-推广组总点击量
						Long totalClick = groupSetting[5];
						if(totalClick != null && totalClick > 0) {
							//如果页面中设置的推广组总click目标 >= redisB中存储的推广组汇总数据，click数；
							//则自动状态，设置为自动结束
							if(groupTotal[3] >= totalClick) {
								// 组总点击量达到了，下线
								log.debug("推广活动[" + c.getId() + "]" + c.getCampName() + "所属推广组达到总点击量，下线");
								changeAutoStatus(campid, CampaignAutoStatus.FINISHED);
								continue;
							}
						}
						// 页面-推广组总预算
						Long totalBudget = groupSetting[1];
						if(totalBudget != null && totalBudget > 0) {
							//如果页面中设置的推广组总预算目标，>= redisB中存储的推广组汇总数据，花费；
							//则自动状态，设置为自动结束
							if(groupTotal[7] / 1000 >= totalBudget) {
								// 组总点击量达到了，下线
								log.debug("推广活动[" + c.getId() + "]" + c.getCampName() + "所属推广组达到总预算，下线");
								changeAutoStatus(campid, CampaignAutoStatus.FINISHED);
								continue;
							}
						}
					}
					
					//从reidsB中取出活动每天的统计数据
					int[] day = countDAO.getCampDayCount(campid, ymd);
					//从redisB中取出推广组每天的统计数据
					int[] groupDay = groupDayCount.get(c.getGroupId());
					if(groupDay == null) {
						groupDay = countDAO.getCampGrpDayCount(c.getGroupId(), ymd);
						if(groupDay != null) {
							groupDayCount.put(c.getGroupId(), groupDay);
						}
					}
					
					// ---------------------------判断活动每天的pv目标
					// 日展现量，日展示量是否达到目标
					boolean pvnotenough = true;
					//拿到页面设置的活动的每天pv目标
					Long dayPv = c.getDailyPv();
					if(day != null && dayPv != null && dayPv > 0) {
						//使用RedisB中活动每天的pv数，和页面中设置的活动的每天pv数，对比。
						if(day[1] >= dayPv) {
							// 日展现量达到了，暂停
							pvnotenough = false;
							//如果活动的日展示量已经到达目标，则判断活动的状态如果是投放中，则将活动的自动状态改为，到达日展示量，暂停。
							//并修改活动自动状态，放到状态修改队列中。
							if(status == CampaignAutoStatus.ONLINE) {
								log.debug("推广活动[" + c.getId() + "]" + c.getCampName() + "达到日展现量，暂停");
								changeAutoStatus(campid, CampaignAutoStatus.PAUSE_ACCOMPLISH_GOALS);
								continue;
							}
						}
					}
					//-------------------判断推广组每天的pv目标-
					Long groupDayPv = groupSetting != null ? groupSetting[2] : null;
					// 组日展现量
					if(groupDay != null && groupDayPv != null && groupDayPv > 0) {
						if(groupDay[1] >= groupDayPv) {
							// 组日展现量达到了，暂停
							pvnotenough = false;
							//并修改活动自动状态，放到状态修改队列中。
							if(status == CampaignAutoStatus.ONLINE) {
								log.debug("推广活动[" + c.getId() + "]" + c.getCampName() + "所属推广组达到日展现量，暂停");
								changeAutoStatus(campid, CampaignAutoStatus.PAUSE_ACCOMPLISH_GOALS);
								continue;
							}
						}
					}
					//-------------------判断活动每天的click目标
					// 日点击量
					Long dayClick = c.getDailyClick();
					boolean clicknotenough = true;
					if(day != null && dayClick != null && dayClick > 0) {
						if(day[3] >= dayClick) {
							// 日点击量达到了，下线
							clicknotenough = false;
							if(status == CampaignAutoStatus.ONLINE) {
								log.debug("推广活动[" + c.getId() + "]" + c.getCampName() + "达到日点击量，暂停");
								changeAutoStatus(campid, CampaignAutoStatus.PAUSE_ACCOMPLISH_GOALS);
								continue;
							}
						}
					}
					// ----------------------判断推广组每天的click目标
					// 组日点击量
					Long groupDayClick = groupSetting != null ? groupSetting[4] : null;
					if(groupDay != null && groupDayClick != null && groupDayClick > 0) {
						if(groupDay[3] >= groupDayClick) {
							// 组日点击量达到了，下线
							clicknotenough = false;
							if(status == CampaignAutoStatus.ONLINE) {
								log.debug("推广活动[" + c.getId() + "]" + c.getCampName() + "所属推广组达到日点击量，暂停");
								changeAutoStatus(campid, CampaignAutoStatus.PAUSE_ACCOMPLISH_GOALS);
								continue;
							}
						}
					}
					//---------------------判断活动每天的预算目标
					// 日预算
					Long dayBudget = c.getDailyBudget();
					boolean budgetnotenough = true;
					if(day != null && dayBudget != null && dayBudget > 0) {
						if(day[7] / 1000 >= dayBudget) {
							// 日预算量达到了，下线
							budgetnotenough = false;
							if(status == CampaignAutoStatus.ONLINE) {
								log.debug("推广活动[" + c.getId() + "]" + c.getCampName() + "达到日预算，暂停");
								changeAutoStatus(campid, CampaignAutoStatus.PAUSE_ACCOMPLISH_GOALS);
								continue;
							}
						}
					}
					//--------------------判断推广组每天的预算目标
					// 组日预算
					Long groupDayBudget = groupSetting != null ? groupSetting[0] : null;
					if(groupDay != null && groupDayBudget != null && groupDayBudget > 0) {
						if(groupDay[7] / 1000 >= groupDayBudget) {
							// 组日预算达到了，下线
							budgetnotenough = false;
							if(status == CampaignAutoStatus.ONLINE) {
								log.debug("推广活动[" + c.getId() + "]" + c.getCampName() + "所属推广组达到日预算，暂停");
								changeAutoStatus(campid, CampaignAutoStatus.PAUSE_ACCOMPLISH_GOALS);
								continue;
							}
						}
					}
					//如果pv的活动目标、组目标；click的活动目标、组目标；预算的活动目标、组目标，都没有达到
					//并且如果活动的自动状态是 到达目标暂停状态，则将活动的自动状态修改为未开始
					if (pvnotenough && clicknotenough && budgetnotenough) {
						if (c.getAutoStatus() == CampaignAutoStatus.PAUSE_ACCOMPLISH_GOALS) {
							changeAutoStatus(campid, CampaignAutoStatus.READY);
						}
					}
				}
				//将推广组的天目标数据，和总目标数据删除
				groupDayCount.clear();
				groupTotalCount.clear();

				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					log.debug("CountThread 被中断退出");
					break;
				}
			}
		}
	}

	/**
	 * 数据通知线程，通知引擎各活动当前的CTR，以及价格因子
	 * @作者 Amos Xu
	 * @版本 V1.0
	 * @配置 
	 * @创建日期 2016年11月10日
	 * @项目名称 dsp-cache-collector
	 * @描述 <p></p>
	 */
	private class NotifyThread extends Thread {
		
		// 存放活动数据
		// key campid, value[pv, click, oldpv, oldclick, oldExpend]
		private Map<Long, long[]> ctrMap = new TreeMap<Long, long[]>();

		public NotifyThread() {
			super("NotifyThread");
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Thread#run()
		 */
		public void run() {
			log.debug("NotifyThread is running");
			//如果当前当前线程没有阻断则一直循环下去
			for (; !Thread.interrupted();) {
				//获取系统当前时间的毫秒数
				long t = System.currentTimeMillis();
				//获取当前时间的，yyyyMMdd
				int ymd = systemTime.getYyyyMMdd();
				//获取当前时间的，周数据
				int week = systemTime.getWeek();
				//获取当前时间的，小时数
				int hour = systemTime.getHour();
				//获取当前时间的分钟数
				int minute = systemTime.getMinute();
				String defaultH = "111111111111111111111111";
				
				//循环内存中所有再投活动的集合
				for (Iterator<Entry<Long, Campaign>> it = allCamps.entrySet().iterator(); it.hasNext();) {
					Entry<Long, Campaign> e = it.next();
					//拿到活动对象
					Campaign c = e.getValue();
					//拿到活动id
					long campid = c.getId();
					//从RedisB中拿到，这个活动的汇总数据
					long[] total = countDAO.getCampTotalCount(campid);
					//如果redisB中没有汇总数据，则不做任何操作
					if(total == null) continue;
					//ctrmj=value[pv, click, oldpv, oldclick, oldExpend]
					long[] ctrm = ctrMap.get(campid);
					if(ctrm == null) {
						//如果ctrm中没有数据，则实例化好，数组做好准备。
						ctrm = new long[5];
						//想ctrMap中添加数据
						ctrMap.put(campid, ctrm);
					}
					
					//如果活动的投放速度，选择的是标准，则以为这将投放均匀的分布在，广告主选择的时段对应的每个小时，每一分钟
					if(c.getBudgetCtrlType() == BudgetCtrlType.STANDARD) {
						//获取投放时段
						String weekhour = c.getWeekhour();
						int[][] wh = null;
						if (!StringUtils.isEmpty(weekhour)) {
							if(weekhour.startsWith("\""))weekhour = weekhour.substring(1);
							if(weekhour.endsWith("\""))weekhour = weekhour.substring(0, weekhour.length() - 1);
							wh = JSON.parseObject(weekhour, int[][].class);
						}
						//sb 中存放的是，当前周，24小时，都哪个小时被选择了，1为这个小时投放，0为不投放
						//例如：sb值为，001111111111111111111111，这24个字符分别代表，24小时，如例子中，0点和1点不投放，其它小时都投放
						StringBuilder sb = new StringBuilder();
						//如果投放时段为null，则使用默认投放时段数据
						if (wh == null || wh.length == 0) {
							//sb的值为24个1，代表全天24小时都投放
							sb.append(defaultH);
						} else {
							//如果页面设置了投放时段，则根据当前时间为周几，取出这一天24小时的数据，就是24个0或1组成的字符串
							int[] h = wh[week];
							for (int i : h) {
								sb.append(i);
							}
						}
						//24个0或1组成的字符串
						String hours = sb.toString();
						//从RedisB中取出，活动每天的统计数据
						int[] todayCount = countDAO.getCampDayCount(campid, ymd);
						//从redisB中取出，活动每小时的统计数据
						int[] hourCount = countDAO.getCampHourCount(c.getId(), ymd, hour);
						//拿到活动的每天的pv目标数据
						Long dailyPv = c.getDailyPv();
						//拿到活动每天的click目标数据
						Long dailyClick = c.getDailyClick();
						//拿到活动每天的预算数据
						long dayBudget = c.getDailyBudget();
						//如果活动每天的统计数据和每小时的汇总数据都不为null
						if(todayCount != null && hourCount != null) {
							//设置暂停标识为false
							boolean paused = false;
							//如果活动每天的pv目标值不为null
							if(dailyPv != null && dailyPv > 0) {
								// z，如果设置了日投放量，按投放量来控制
								// x,获取活动每天统计数据中的pv数
								int dpv = todayCount[1];
								// x,获取活动每小时统计数据中的pv数
								int hpv = hourCount[1];

								// 包含当前小时当天剩下的要投放的量，活动每天的pv目标+当前小时pv数-当天已经投放了pv数
								long totalleft = dailyPv + hpv - dpv;
								// 根据权重数据，拿到当前小时应该投放的次数
								int chpv = trafficHoursTrendsUtil.calcHourNum(hour, hours, totalleft);
								// 如果redisB中活动的小时统计中pv数大于当前小时应该投放的数，则向MQ发送，当前活动，小时暂停的消息。
								if(hpv >= chpv) {
									// 发送PAUSE消息
									sendPause(c.getId(), hour);
									log.debug("[" + c.getId() + ":" + c.getCampName() + "]达到小时PV限制[" + chpv + "/" + hpv + "]，暂停投放");
									paused = true;
								}
							}
							
							//如果当前小时没有暂停，并且当天活动的点击目标不为null
							if(!paused && dailyClick != null && dailyClick > 0) {
								// z，如果设置了日点击量，按点击量来控制
								//redisB中，活动的每天的统计中，click数
								int dc = todayCount[3];
								//redisB中，活动的每小时的统计中，click数
								int hc = hourCount[3];

								// 包含当前小时当天剩下的要投放的量
								long totalleft = dailyClick + hc - dc;
								int chc = trafficHoursTrendsUtil.calcHourNum(hour, hours, totalleft);
								//如果当前小时已经投的click数，大于 当前小时应投的，则已经到达小时点击限制，则设置当前活动小时暂停
								if(hc >= chc) {
									// 发送PAUSE消息
									sendPause(c.getId(), hour);
									log.debug("[" + c.getId() + ":" + c.getCampName() + "]达到小时点击限制[" + chc + "/" + hc + "]，暂停投放");
									paused = true;
								}
							}
							
							//如果活动没有暂停，或是活动没有设置每天的pv目标，没有设置每天的click目标，则按照每日预算控制
							if(!paused) {

								// z，按日预算控制
								//每天的花费
								int de = todayCount[7] / 1000;
								//每小时的花费
								int he = hourCount[7] / 1000;

								// z，包含当前小时当天剩下的要投放的量
								//当天还剩下的投放预算
								long totalleft = dayBudget + he - de;
								//根据权重，计算出当前小时应该投多少钱
								int che = trafficHoursTrendsUtil.calcHourNum(hour, hours, totalleft);
								//如果当前小时已经投放的金额，大于 当前小时剩余的金额，则发送小时暂停消息
								if(he >= che) {
									// 发送PAUSE消息
									sendPause(c.getId(), hour);
									log.debug("[" + c.getId() + ":" + c.getCampName() + "]达到小时预算限制[" + che + "/" + he + "]，暂停投放");
								}
							}
						}//如果活动的每天统计数据和每小时统计数据，为null
						
						//ctrmj=value[pv, click, oldpv, oldclick, oldExpend]
						long lastExpend = total[7] - ctrm[4];
						ctrm[4] = total[7];
						if(lastExpend <= 0) {
							continue;
						}
						
						//如果当天的统计数据不为null，则用活动的预算数据-=当天统计数据中的花费，得到剩下可以的金额
						if (todayCount != null) {
							dayBudget -= todayCount[7] / 1000;
						}
						
						//得到未来一分钟要消耗的金额
						int budget1minute = trafficHoursTrendsUtil.calc1MinuteBudget(hour, minute, hours, (int) dayBudget);
						
						float priceFactor = ((float)budget1minute) * 1000 / lastExpend;
						log.debug("[" + c.getId() + ":" + c.getCampName() + "]剩余预算: " + dayBudget + " 分钟预算: " + budget1minute + " 上分钟消耗: " + ((float)lastExpend/1000) + " PF: " + priceFactor);
						
						//发送价格因子数据
						sendPriceFactor(campid, priceFactor);
					}//标准结束

					ctrm[0] = total[1]; // pv
					ctrm[1] = total[3]; // click

					long pv = ctrm[0] - ctrm[2];
					ctrm[2] = ctrm[0];
					if (pv <= 0) continue;

					long click = ctrm[1] - ctrm[3];
					ctrm[3] = ctrm[1];
					if (click <= 0) continue;
					float ctr = (float) 1000.0 * click / pv;
					
					//发送点击率数据
					sendCtr(campid, ctr);
				}
				log.debug("--------------------");    
				
				//Set<Long> campids = allCamps.keySet();
				Set<Long> campids = new TreeSet<Long>();
				for(Iterator<Entry<Long, Campaign>> it = allCamps.entrySet().iterator(); it.hasNext();){
					Entry<Long, Campaign> e = it.next();
					campids.add(e.getKey());
				}
				
				for (Iterator<Entry<Long, long[]>> it = ctrMap.entrySet().iterator(); it.hasNext();) {
					Entry<Long, long[]> e = it.next();
					if (!campids.contains(e.getKey())) it.remove();
				}

				t = System.currentTimeMillis() - t;

				// 1分钟运行一次
				if (t < 60000l) {
					try {
						Thread.sleep(60000l - t);
					} catch (InterruptedException e) {
						log.debug("NotifyThread 被中断退出");
						break;
					}
				}
			}
		}
		//发送点击率消息
		private void sendCtr(final long campid, final float ctr) {
			try {
				jmsTemplate.send(new MessageCreator() {
					@Override
					public Message createMessage(Session session) throws JMSException {
						ChangeCampCtrMsg msg = new ChangeCampCtrMsg();
						msg.setCampid(campid);
						msg.setCtr(ctr);
						return session.createTextMessage(JSON.toJSONString(msg, SerializerFeature.WriteClassName));
					}
				});
			} catch (JmsException e) {
				log.error("发送活动CTR消息发生错误", e);
			}
		}
		//发送竞价因子数据
		private void sendPriceFactor(final long campid, final float priceFactor) {
			try {
				jmsTemplate.send(new MessageCreator() {
					@Override
					public Message createMessage(Session session) throws JMSException {
						ChangeCampPriceFactorMsg msg = new ChangeCampPriceFactorMsg();
						msg.setCampid(campid);
						msg.setPricefactor(priceFactor);
						return session.createTextMessage(JSON.toJSONString(msg, SerializerFeature.WriteClassName));
					}
				});
			} catch (JmsException e) {
				log.error("发送活动PriceFactor消息发生错误", e);
			}
		}
		//发送活动是否暂停消息
		private void sendPause(final long campid, final int hour) {
			try {
				jmsTemplate.send(new MessageCreator() {
					@Override
					public Message createMessage(Session session) throws JMSException {
						SetCampHourPauseMsg msg = new SetCampHourPauseMsg();
						msg.setCampid(campid);
						msg.setHour(hour);
						return session.createTextMessage(JSON.toJSONString(msg, SerializerFeature.WriteClassName));
					}
				});
			} catch (JmsException e) {
				log.error("发送活动平滑控制消息发生错误", e);
			}
		}
	}

	/**
	 * 现在活动的状态
	 * @param camp
	 * @param w
	 * @throws Exception
	 */
	private void showStatus(Campaign camp, BufferedWriter w) throws Exception {
		w.write(camp.getPartnerId() + ":" + camp.getPartnerName() + " | " + camp.getGroupId() + ":" + camp.getGroupName()+ " | " + camp.getId() + ":"+ camp.getCampName() + " | " + camp.getManulStatus().getDesc() + " | " + camp.getAutoStatus().getDesc() + "\r\n");
		log.debug("current camp status->"+camp.getPartnerId() + ":" + camp.getPartnerName() + " | " + camp.getGroupId() + ":" + camp.getGroupName()+ " | " + camp.getId() + ":"+ camp.getCampName() + " | " + camp.getManulStatus().getDesc() + " | " + camp.getAutoStatus().getDesc() + "\r\n");
	}
	/**
	 * 向文件输出，活动各项字段值；并输出活动自动状态的变化
	 * @param camp						活动对象
	 * @param w							文件输出流对象
	 * @param old
	 * @throws Exception
	 * @return void
	 */
	private void showStatus(Campaign camp, BufferedWriter w, CampaignAutoStatus old) throws Exception {
		w.write(camp.getPartnerId() + ":" + camp.getPartnerName() + " | " + camp.getGroupId() + ":" + camp.getGroupName()+ " | " + camp.getId() + ":"+ camp.getCampName() + " | " + camp.getManulStatus().getDesc() + " | " + old.getDesc() + " --> " + camp.getAutoStatus().getDesc() + "\r\n");
		log.debug("camp status change->"+camp.getPartnerId() + ":" + camp.getPartnerName() + " | " + camp.getGroupId() + ":" + camp.getGroupName()+ " | " + camp.getId() + ":"+ camp.getCampName() + " | " + camp.getManulStatus().getDesc() + " | " + old.getDesc() + " --> " + camp.getAutoStatus().getDesc() + "\r\n");
	}

	/**
	 * 加载所有的推广活动
	 */
	private void loadAll() {
		List<Campaign> allcamp = campaignDAO.getAllCampaigns();
		for (Campaign camp : allcamp) {
			load(camp);
		}
	}

	/**
	 * 加载一个推广活动
	 * @param camp
	 */
	private void load(Campaign camp) {
		//活动投放策略，从活动中取出活动策略数据
		Map<String, String> dims = camp.getCampaignDims();
		//如果活动策略中没有设置渠道数据，则记录错误日志，这个活动不加载
		if (dims == null || dims.size() == 0 || StringUtils.isEmpty(dims.get("channel"))) {
			log.error("推广活动[" + camp.getId() + "]" + camp.getCampName() + "没有指定的投放渠道，请检查");
			return;
		}
		//获取策略中，渠道id的集合
		String chns = dims.get("channel");
		long[] chnids = JSON.parseObject(chns, long[].class);
		//如果没有渠道id，则不加载活动，直接返回
		if(chnids.length == 0) {
			log.error("推广活动[" + camp.getId() + "]" + camp.getCampName() + "没有指定的投放渠道，请检查");
			return;
		}
		//如果互动的自动状态是 完成FINISHED，自动状态是结束，则不加载
		if(camp.getAutoStatus() == CampaignAutoStatus.FINISHED || camp.getManulStatus() == CampaignManulStatus.OFFLINE) {
			log.debug("推广活动[" + camp.getId() + "]" + camp.getCampName() + "已结束");
			return;
		}
		//将活动那个数据，put到再投活动的集合中
		allCamps.putIfAbsent(camp.getId(), camp);
		log.debug("加载推广活动[" + camp.getId() + "]" + camp.getCampName() + ", [" + camp.getManulStatus().getDesc() + "][" + camp.getAutoStatus().getDesc() + "]");
	}

	/**
	 * 更新活动
	 * 
	 */
	public boolean updateCampaign(Campaign campaign) {
		campaignDAO.updateCampaign(campaign);
		waitingReloadCamp.offer(campaign.getId());
		semphore.release();
		return true;
	}

	/**
	 * 执行修改自动状态，并向mq中发送消息
	 * @param campaignId
	 * @param status
	 * @return
	 */
	private boolean doChangeAutoStatus(long campaignId, CampaignAutoStatus status) {
		campaignDAO.changeAutoStatus(campaignId, status);

		final ChangeAutoStatusMsg msg = new ChangeAutoStatusMsg();
		msg.setCampid(campaignId);
		msg.setStatus(status);

		// 发修改状态的msg
		try {
			jmsTemplate.send(new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					log.debug("-->"+JSON.toJSONString(msg, SerializerFeature.WriteClassName));
					return session.createTextMessage(JSON.toJSONString(msg, SerializerFeature.WriteClassName));
				}
			});
		} catch (JmsException e) {
			log.error("发送修改自动状态消息发生错误", e);
		}
		return true;
	}

	/**
	 * 修改自动状态
	 * @param campaignId
	 * @param status
	 * @return
	 */
	public boolean changeAutoStatus(long campaignId, CampaignAutoStatus status) {
		//将用 活动id、自动状态标识、要修改的状态码，组成的数组，添加到 waitingChangeStatus 队列中
		waitingChangeStatus.offer(new long[] { campaignId, 1, status.getCode() });
		semphore.release();
		return true;
	}

	/**
	 * 修改手动状态
	 * @param campaignId
	 * @param status
	 * @return
	 */
	public boolean changeManulStatus(long campaignId, CampaignManulStatus status) {
		//将用 活动id、手动状态标识、要修改的状态码，组成的数组，添加到 waitingChangeStatus 队列中
		waitingChangeStatus.offer(new long[] { campaignId, 0, status.getCode() });
		semphore.release();
		return true;
	}

	public boolean isAlive() {
		return monitorThread.isAlive() && countThread.isAlive() && notifyThread.isAlive();
	}
	
	public void interrupt() {
		monitorThread.interrupt();
		countThread.interrupt();
		notifyThread.interrupt();
	}
}
