package com.jtd.engine.ad;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.jtd.engine.ad.em.Adx;
import com.jtd.engine.dao.CampaignDAO;
import com.jtd.engine.utils.TimerTask;
import com.jtd.web.constants.CampaignAutoStatus;
import com.jtd.web.constants.CampaignManulStatus;
import com.jtd.web.constants.CatgSerial;
import com.jtd.web.jms.BidCountMsg;
import com.jtd.web.model.Ad;
import com.jtd.web.model.Campaign;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月8日
 * @项目名称 dsp-engine
 * @描述 <p>监控任务,只负责显示状态,根据状态进行操作,不管状态变化
 * 手动状态由管理端控制，自动状态变化由统计服务器控制</p>
 */
class Monitor implements TimerTask {

	private static final Log log = LogFactory.getLog(Monitor.class);
	private final Logger hzDebugLog = LogManager.getLogger("hzDebugLog");
	
	// 当前所有正在投放的推广活动信息
	private static final String ALL_CAMP_INFO_FILE = "allcamp.txt";

	// 10秒扫一次
	private static final int MONITOR_INTERVAL = 10000;

	// PC,移动和视频的在投活动列表 key: channelId + "_" + size
	private ConcurrentHashMap<String, CopyOnWriteArrayList<Campaign>> pc;
	private ConcurrentHashMap<String, CopyOnWriteArrayList<Campaign>> app;
	private ConcurrentHashMap<String, CopyOnWriteArrayList<Campaign>> wap;

	// 当前正在投放的所有的活动, key campid
	private ConcurrentHashMap<Long, Campaign> allCamps = new ConcurrentHashMap<Long, Campaign>();

	// 竞价次数计数器
	// key:adid, value[tanxbid, besbid, vambid]，广告的竞价计数器
	private ConcurrentHashMap<Long, AtomicInteger[]> adBidCounter = new ConcurrentHashMap<Long, AtomicInteger[]>();
	// 可投放列表变化时清空 key:campid, value[tanxbid, besbid, vambid]，活动的竞价计数器
	private ConcurrentHashMap<Long, AtomicInteger[]> campBidCounter = new ConcurrentHashMap<Long, AtomicInteger[]>();
	// key partnerid_groupid_campid_creativeid value[tanxbid, besbid, vambid]，创意的竞价计数器
	private ConcurrentHashMap<String, AtomicInteger[]> creativeBidCounter = new ConcurrentHashMap<String, AtomicInteger[]>();
	//资源同时可以被线程访问的个数
	private Semaphore semphore = new Semaphore(1);
	//等待的需要重新加载的活动集合
	private LinkedBlockingQueue<Long> waitingReloadCamp = new LinkedBlockingQueue<Long>();
	private LinkedBlockingQueue<long[]> waitingChangeStatus = new LinkedBlockingQueue<long[]>();

	private CampaignDAO campaignDAO;

	private JmsTemplate jmsTemplate;

	/**
	 * 构造监控任务,包可见
	 * @param pc
	 * @param mobile
	 * @param campaignDAO
	 */
	Monitor(JmsTemplate jmsTemplate, ConcurrentHashMap<String, CopyOnWriteArrayList<Campaign>> pc, ConcurrentHashMap<String, CopyOnWriteArrayList<Campaign>> app, ConcurrentHashMap<String, CopyOnWriteArrayList<Campaign>> wap, CampaignDAO campaignDAO) {
		this.jmsTemplate = jmsTemplate;
		this.pc = pc;
		this.app = app;
		this.wap = wap;
		this.campaignDAO = campaignDAO;
		//加载左右的广告活动，
		loadAll();
		MonitorThread mt = new MonitorThread();
		mt.setDaemon(true);
		mt.start();

		NotifyBidThread nt = new NotifyBidThread();
		nt.setDaemon(true);
		nt.start();
	}

	/**
	 * 定时发送广告竞价次数消息供统计系统统计
	 *
	 */
	private class NotifyBidThread extends Thread {
		public NotifyBidThread() {
			super("NotifyBidThread");
		}

		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		public void run() {

			log.info("NotifyBidThread is running");
			//如果当前线程没有中断，则一直循环
			for (; !Thread.interrupted();) {

				long t = System.currentTimeMillis();
				//ConcurrentHashMap<String, AtomicInteger[]> creativeBidCounter,key=partnerid_groupid_campid_creativeid,value=AtomicInteger[]数组，
				//[0]=tanx竞价次数，[1]=bes竞价次数，[2]=万流客竞价次数
				for(Iterator<Entry<String, AtomicInteger[]>> it = creativeBidCounter.entrySet().iterator(); it.hasNext();) {
					Entry<String, AtomicInteger[]> e = it.next();
					String key = e.getKey();
					AtomicInteger[] bid = e.getValue();
					it.remove();

					String[] ids = key.split("_");
					long partnerid = Long.parseLong(ids[0]);
					long groupid = Long.parseLong(ids[1]);
					long campid = Long.parseLong(ids[2]);
					long creativeid = Long.parseLong(ids[3]);
					//这里这里是将所有渠道的最细颗粒度的创意的竞价次数，加在一起，
					int bidnum = bid[0].get() + bid[1].get() + bid[2].get();
					send(partnerid, groupid, campid, creativeid, bidnum);
				}

				t = System.currentTimeMillis() - t;
				if(t < 5000l) {
					try {
						Thread.sleep(5000l - t);
					} catch (InterruptedException e) {
						log.error("MonitorThread被中断，退出");
						break;
					}
				}
			}
		}
		
		/**
		 * 将创意所有渠道竞价次数总和，发送到mq中，由dsp-cache-collector接收。
		 * @param partnerid
		 * @param groupid
		 * @param campid
		 * @param creativeid
		 * @param bid 				同一个创意所有渠道加在一起的的竞价次数总和
		 * @return void
		 */
		private void send(final long partnerid, final long groupid, final long campid, final long creativeid, final int bid) {
			try {
				jmsTemplate.send(new MessageCreator() {
					@Override
					public Message createMessage(Session session) throws JMSException {
						BidCountMsg msg = new BidCountMsg();
						msg.setPartnerid(partnerid);
						msg.setGroupid(groupid);
						msg.setCampid(campid);
						msg.setCreativeid(creativeid);
						msg.setBidNum(bid);
						return session.createTextMessage(JSON.toJSONString(msg, SerializerFeature.WriteClassName));
					}
				});
			} catch (JmsException e) {
				log.error("发送竞价次数消息发生错误", e);
			}
		}
	}

	/**
	 * @作者 Amos Xu
	 * @版本 V1.0
	 * @配置 
	 * @创建日期 2016年10月31日
	 * @项目名称 dsp-engine
	 * @描述 
	 * <p>
	 * 一、线程中用到的主要存储数据的集合和队列：<br/>
	 * 1、LinkedBlockingQueue<Long> waitingReloadCamp：该队列中的数据，主要是外部调用updateCampaign(Campaign campaign)方法时，向该队列中写入广告活动数据<br/>
	 * 也是在com.jtd.engine.adserver.jms.JMSMessageListener中，从mq中获取的需要更新或添加的活动数据。<br/>
	 * 2、LinkedBlockingQueue<long[]> waitingChangeStatus：该队列中的数据，主要是外部调用 changeAutoStatus(long campaignId, CampaignManulStatus status) <br/>
	 * 和changeManulStatus(long campaignId, CampaignManulStatus status)方法时，向该队列中写入需要修改自动或是手动状态的活动id；也是在<br/>
	 * com.jtd.engine.adserver.jms.JMSMessageListener中，从mq中获取的需要修改活动的手动或自动状态的消息数据。<br/>
	 * 其中long[]结构为，long[0]=活动id，long[1]=0为手动状态，long[1]=1为自动状态，long[2]=状态值<br/>
	 * 3、ConcurrentHashMap<Long, Campaign> allCamps：为Map，存放的是当前所有正在投放的活动数据，key=活动id，value=活动的对象<br/>
	 * 4、ConcurrentHashMap<String, CopyOnWriteArrayList<Campaign>> pc或app或wap：这几个结合中存放的是key=channel+"_"+广告尺寸，value=符合渠道、广告类型、广告尺寸的所有广告活动列表。<br/>
	 * 二、大概逻辑：<br/>
	 * 1、该线程启动后，会一直运转。并不停的读取waitingReloadCamp，waitingChangeStatus队列中的数据。
	 * 2、将waitingReloadCamp中的活动id全部取出；
	 * 3、取出后先删除allCamps集合中的需要更新的活动和pc、wap、app中需要更新的活动数据。
	 * 4、然后将需要更新的活动数据，从本地的reids中取出，一个一个的再添加到allCamps、pc、wap、app集合中。
	 * 5、从waitingChangeStatus队列中取出所有待修改状态的活动id。
	 * 6、根据状态是手动还是自动状态，修改待投放列表“allCamps”集合中活动的状态，并修改本地redis中所对应的活动的状态。
	 * 7、将下线的活动从待投放列表“allCamps“列表和pc、app、wap列表中删除。
	 * 8、再将所有待投放列表”allCamps“中的活动在写到allcamp.txt文件中。
	 * </p>
	 */
	private class MonitorThread extends Thread {
		//使用Campaing 的 id比较两个Campaing对象是否相等
		private Comparator<Campaign> comparator = new Comparator<Campaign>() {
			@Override
			public int compare(Campaign c1, Campaign c2) {
				return (int) (c1.getId() - c2.getId());
			}
		};
		public MonitorThread() {
			super("MonitorThread");
		}
		public void run() {

			log.info("MonitorThread is running");
			//需要重载的活动集合
			List<Long> needReload = new ArrayList<Long>();
			//需要删除的活动集合
			List<Long> needRemove = new ArrayList<Long>();
			//需要改变的活动集合
			List<long[]> needChangest = new ArrayList<long[]>();
			//如果当前线程没有中断，则一直执行循环
			for (; !Thread.interrupted();) {
				try {
					//设置从 semphore.acquire() 开始直到调用了 semphore.release() 方法之间的这些变量，只能同时被一个线程访问。
					//而在这里设置的是，直到调用了 updateCampaign(Campaign campaign) 这个方法后
					semphore.acquire();
				} catch (InterruptedException e) {
					break;
				}

				// 先把要重新加载的加载，重新加载一遍
				//从waitingReloadCamp获取排在首位的需要重新加载的活动id，如果不为空，则将id添加到reedReload集合中，
				//直到把waitingReloadCamp队列中所有的id，取完为止。
				for (Long id = waitingReloadCamp.poll(); id != null; id = waitingReloadCamp.poll()) {
					needReload.add(id);
				}
				//该活动是否为新添加的广告活动，true为新添加，false不是新添加
				boolean loadNew = false;
				if(needReload.size() > 0) {
					// 先把需要重新加载的活动，从再投的活动集合中删除
					for(Long id : needReload){
						allCamps.remove(id);
					}
					//把不渠道下，pc，app、wap，广告活动需要重新加载的活动从pc、app、wap下移除。
					remove(pc, needReload, false);
					remove(app, needReload, false);
					remove(wap, needReload, false);

					// 挨个加载
					for(Long id : needReload){
						Campaign c = campaignDAO.getCampaignById(id);
						if(c == null) {
							log.error("等待加载的活动没有找到, ID: " + id);
							continue;
						}
						//将广告活动按照按照，不同的渠道、尺寸，和活动类型，分别存放到pc，app、wap中。同时将活动以活动id为key，活动为value存放到allCamps中。
						//pc中的key为 channel+"_"+广告尺寸，vlaue为list，而list存放的是 campaign
						load(c);
					}
					needReload.clear();
					loadNew = true;
				}

				// 修改状态
				//hasOnline标识是否为新上线的活动，true为新上线，false不是
				boolean hasOnline = false;
				//从等待修改状态的集合中取出队列中所有的，等待修改状态的所有活动，把活动id添加到needChangest集合中
				//waitingChangeStatus结构，waitingChangeStatus存储的是一个long数组，每一个数组代表一个广告活动的部分信息
				//long数组，long[0]=活动id，long[1]=手动还是自动自动状态，=0为手动状态，=1为自动状态，long[2]=活动的状态值
				for (long[] st = waitingChangeStatus.poll(); st != null; st = waitingChangeStatus.poll()) {
					needChangest.add(st);
				}
				for (long[] st : needChangest) {
					Campaign camp = allCamps.get(st[0]);
					if(camp == null) continue;
					if(st[1] == 0) {
						//将活动的状态值，转换成枚举状态值
						CampaignManulStatus s = CampaignManulStatus.fromCode((int)st[2]);
						//获取活动原有的状态值
						CampaignManulStatus old = camp.getManulStatus();
						//将活动的手动状态，改变为新的状态值
						camp.setManulStatus(s);
						//将新的状态值，写到redis中。
						campaignDAO.changeManulStatus(camp.getId(), s);
						log.info("推广活动[" + camp.getId() + "]" + camp.getCampName() + "手动状态变更: " + old.getDesc() + " --> " + s.getDesc());
						//如果手动状态为下线，则将活动id添加的需要删除的数组中
						if(s == CampaignManulStatus.OFFLINE){
							needRemove.add(st[0]);
						} 
						//如果hasOnline的值为非上线，并且如果手动状态为上线，则将hasOnline设置为上线状态
						if(!hasOnline && s == CampaignManulStatus.ONLINE) {
							hasOnline = true;
						}
					} else if(st[1] == 1) {
						//将活动的自动状态值，转换成枚举状态值
						CampaignAutoStatus s = CampaignAutoStatus.fromCode((int)st[2]);
						//获取活动原有的自动状态值
						CampaignAutoStatus old = camp.getAutoStatus();
						//将活动的自动状态，改变为新的状态值
						camp.setAutoStatus(s);
						//将新的自动状态值，写到redis中。
						campaignDAO.changeAutoStatus(camp.getId(), s);
						log.info("推广活动[" + camp.getId() + "]" + camp.getCampName() + "自动状态变更: " + old.getDesc() + " --> " + s.getDesc());
						//如果活动的自动状态值为投放完成，则将该活动添加到，需要删除的集合中
						if(s == CampaignAutoStatus.FINISHED){
							needRemove.add(st[0]);
						}
						//如果hasOnline的值为非上线，并且如果自动状态为上线，则将hasOnline设置为上线状态
						if(!hasOnline && s == CampaignAutoStatus.ONLINE) {
							hasOnline = true;
						}
					}
				}
				needChangest.clear();
				// 有新加载的或者新上线的，把推广活动的竞价计数器清0
				if(loadNew || hasOnline) campBidCounter.clear();

				// 把下线的删除
				if(needRemove.size() > 0) {
					for(Long id : needRemove){
						allCamps.remove(id);
					}
					remove(pc, needRemove, true);
					remove(app, needRemove, true);
					remove(wap, needRemove, true);
					needRemove.clear();
				}

				// 把所有在投的显示一遍，实际是显示在文件
				File f = new File(ALL_CAMP_INFO_FILE);
				BufferedWriter w = null;
				try {
					w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f), "UTF-8"));
					display(pc, w);
					display(app, w);
					display(wap, w);
					w.flush();
				} catch (Exception e) {
					log.error("监控任务发生错误", e);
				} finally {
					if (w != null) try { w.close(); } catch (Exception e) {}
				}
			}
		}

		/**
		 * 删除，pc，app，wap缓存中，下线的活动那
		 * @param map，缓存在本地的pc，wap，app，广告活动，其中pc，wap，app为三个不同的集合
		 * @param ids，下线的就是待删除的广告活动id
		 * @param delete 是否删除本地reids库中得广告活动
		 */
		private void remove(ConcurrentHashMap<String, CopyOnWriteArrayList<Campaign>> map, List<Long> ids, boolean delete) {
			for(Iterator<Entry<String, CopyOnWriteArrayList<Campaign>>> it = map.entrySet().iterator(); it.hasNext();) {
				Entry<String, CopyOnWriteArrayList<Campaign>> e = it.next();
				CopyOnWriteArrayList<Campaign> l = e.getValue();
				for(Iterator<Campaign> cit = l.iterator(); cit.hasNext();) {
					Campaign c = cit.next();
					if(ids.contains(c.getId())) {
						l.remove(c);
					}
				}
				if(l.size() == 0) it.remove();
			}

			// 先从投放列表拿除，再删库
			if(delete) {
				for(Long id : ids) {
					campaignDAO.delCampaignById(id);
				}
			}
		}

		/**
		 * 显示状态，将再投的状态活动，写入到“allcamp.txt”文件中
		 * @param map
		 * @param w
		 * @throws Exception
		 */
		private void display(ConcurrentHashMap<String, CopyOnWriteArrayList<Campaign>> map, BufferedWriter w) throws Exception {
			//将参数map中的广告活动，取出来放到list中
			List<Campaign> list = new ArrayList<Campaign>();
			//将参数map中的广告活动的id，取出来放到 ids 中。
			Set<Long> ids = new HashSet<Long>();
			//第一个循环取出的是key=channel+"_"+广告尺寸，value=广告活动列表
			for(Iterator<CopyOnWriteArrayList<Campaign>> it = map.values().iterator(); it.hasNext();){
				CopyOnWriteArrayList<Campaign> l = it.next();
				for(Campaign c : l) {
					if(!ids.contains(c.getId())) {
						ids.add(c.getId());
						list.add(c);
					}
				}
			}
			//将list转为 Campaign[] 数组，并且排序,排序使用的是广告活动id的升序排序
			Campaign[] camps = list.toArray(new Campaign[list.size()]);
			Arrays.sort(camps, comparator);
			//将排序好的 camps数组，输出到文件中
			for(Campaign camp : camps) {
				//输出活动那个信息到文件
				w.write(camp.getId() + ":"+ camp.getCampName() + " [" + camp.getPartnerId() + ":" + camp.getPartnerName() + "/" + camp.getGroupId() + ":" + camp.getGroupName()+ "] " + camp.getManulStatus().getDesc() + " | " + camp.getAutoStatus().getDesc() + (campaignDAO.isPause(camp.getId()) ? " | 平滑控制":"") + "\r\n");
				Map<String, List<Ad>> ads = camp.getAds();
				//输出广告信息到文件
				for(List<Ad> adlist : ads.values()) {
					for(Ad ad : adlist) {
						AtomicInteger[] bid = adBidCounter.get(ad.getId());
						if(bid == null) {
							w.write("\tadid:" + ad.getId() + "\tcreid:" + ad.getCreativeId() + "\tsize:"+ ad.getSize() + "\tTANX[0]\tBES[0]\tVAM[0]\r\n");
						} else {
							w.write("\tadid:" + ad.getId() + "\tcreid:" + ad.getCreativeId() + "\tsize:"+ ad.getSize() + "\tTANX[" + bid[0].get() + "]\tBES[" + bid[1].get() + "]\tVAM[" + bid[2].get() + "]\r\n");
						}
					}
				}
				w.write("\r\n");
			}
		}
	}

	/**
	 * 加载所有的推广活动
	 * 从本地的reids中加载所有手动状态、自动状态为再投的广告活动，到allCamps，pc，app、wap集合中。
	 */
	private void loadAll() {
		List<Campaign> allcamp = campaignDAO.getAllCampaigns();
		for (Campaign camp : allcamp) {
			load(camp);
		}
		
//		for(Entry<String, CopyOnWriteArrayList<Campaign>> entry : wap.entrySet()){
//			String strKey=entry.getKey();
//			CopyOnWriteArrayList<Campaign> campList=entry.getValue();
//			for(Campaign camp : campList){
//				hzDebugLog.info("strKey:"+strKey+",campid:"+camp.getId());
//			}
//		}
	}

	/**
	 * 加载一个推广活动
	 * @param camp
	 */
	private void load(Campaign camp) {
		Map<String, String> dims = camp.getCampaignDims();
		//判断投放策略是否为空，或是策略中的channel是否为空
		if (dims == null || dims.size() == 0 || StringUtils.isEmpty(dims.get("channel"))) {
			log.error("推广活动[" + camp.getId() + "]" + camp.getCampName() + "没有指定的投放渠道，请检查");
			return;
		}
		String chns = dims.get("channel");
		long[] chnids = JSON.parseObject(chns, long[].class);
		//判断策略中的渠道id是否为空
		if(chnids.length == 0) {
			log.error("推广活动[" + camp.getId() + "]" + camp.getCampName() + "没有指定的投放渠道，请检查");
			return;
		}
		//判断活动的手动状态和自动状态是否结束
		if(camp.getAutoStatus() == CampaignAutoStatus.FINISHED || camp.getManulStatus() == CampaignManulStatus.OFFLINE) {
			log.info("推广活动[" + camp.getId() + "]" + camp.getCampName() + "已结束");
			campaignDAO.delCampaignById(camp.getId());
			return;
		}
		//获取活动中的广告
		Map<String, List<Ad>> ads = camp.getAds();
		for (long chnid : chnids) {
			switch (camp.getCampType()) {
			case PC:
				for(List<Ad> adlist : ads.values()) {
					for (Ad ad : adlist) {
						String key = chnid + "_" + ad.getSize();
						CopyOnWriteArrayList<Campaign> l = pc.get(key);
						if(l == null) {
							l = new CopyOnWriteArrayList<Campaign>();
							pc.put(key, l);
						}
						l.add(camp);
					}
				}
				allCamps.putIfAbsent(camp.getId(), camp);
				break;
			case APP:
				for(List<Ad> adlist : ads.values()) {
					for (Ad ad : adlist) {
						String key = chnid + "_" + ad.getSize();
						CopyOnWriteArrayList<Campaign> l = app.get(key);
						if(l == null) {
							l = new CopyOnWriteArrayList<Campaign>();
							app.put(key, l);
						}
						l.add(camp);
					}
				}
				allCamps.putIfAbsent(camp.getId(), camp);
				break;
			case WAP:
				for(List<Ad> adlist : ads.values()) {
					for (Ad ad : adlist) {
						String key = chnid + "_" + ad.getSize();
						CopyOnWriteArrayList<Campaign> l = wap.get(key);
						if(l == null) {
							l = new CopyOnWriteArrayList<Campaign>();
							wap.put(key, l);
						}
						l.add(camp);
					}
				}
				allCamps.putIfAbsent(camp.getId(), camp);
				break;
			default:
				log.error("引擎未知的活动类型" + camp.getCampType().getDesc());
			}
		}
		log.info("加载推广活动[" + camp.getId() + "]" + camp.getCampName() + ", [" + camp.getManulStatus().getDesc() + "][" + camp.getAutoStatus().getDesc() + "]");
	}

	/**
	 * 更新活动
	 * com.jtd.engine.adserver.jms.JMSMessageListener，中接收到更新活动数据的消息时调用该方法。
	 */
	public boolean updateCampaign(Campaign campaign) {
		//把活动先放到本地的redis中
		campaignDAO.updateCampaign(campaign);
		//再把活动id添加到等待加载的队列中
		waitingReloadCamp.offer(campaign.getId());
		//释放线程MonitorThread上次执行时占用的资源
		semphore.release();
		return true;
	}

	/**
	 * com.jtd.engine.adserver.jms.JMSMessageListener，中接收到changeAutoStatus修改自动状态的消息时调用该方法。
	 * 修改自动状态
	 * @param campaignId
	 * @param status
	 * @return
	 */
	public boolean changeAutoStatus(long campaignId, CampaignAutoStatus status) {
		//直接将需要修改自动状态的活动id添加到队列waitingChangeStatus中
		waitingChangeStatus.offer(new long[] { campaignId, 1, status.getCode() });
		semphore.release();
		return true;
	}

	/**
	 * com.jtd.engine.adserver.jms.JMSMessageListener，中接收到修改手动状态的消息时调用该方法。
	 * 修改手动状态
	 * @param campaignId
	 * @param status
	 * @return
	 */
	public boolean changeManulStatus(long campaignId, CampaignManulStatus status) {
		//直接将需要修改手动状态的活动id添加到队列waitingChangeStatus中
		waitingChangeStatus.offer(new long[] { campaignId, 0, status.getCode() });
		semphore.release();
		return true;
	}

	/**
	 * com.jtd.engine.adserver.jms.JMSMessageListener，中接收到修改活动行业类别的消息时调用该方法。
	 * 修改活动的行业类别
	 * @param campid
	 * @param serial
	 * @param catgid
	 */
	public void changeCampCatg(long campid, CatgSerial serial, String catgid) {
		Campaign camp = allCamps.get(campid);
		if(camp == null) return;
		Map<String, List<Ad>> ads = camp.getAds();
		for(Iterator<Entry<String, List<Ad>>> it = ads.entrySet().iterator(); it.hasNext();) {
			Entry<String, List<Ad>> e = it.next();
			List<Ad> adlist = e.getValue();
			for (Ad ad : adlist) {
				Map<String, String> catg = ad.getAdCategorys();
				if (catg != null) {
					catg.put(serial.name(), catgid);
				} else {
					log.error("广告[" + ad.getId() + "/" + ad.getCreativeName() + "]的行业类别为空,请检查");
				}
			}
		}
		campaignDAO.updateCampaign(camp);
	}

	/**
	 * com.jtd.engine.adserver.jms.JMSMessageListener，中接收到修改广告行业类别的消息时调用该方法。
	 * 修改广告的行业类别
	 * @param adid
	 * @param serial
	 * @param catgid
	 */
	public void changeAdCatg(long adid, CatgSerial serial, String catgid) {
		for(Iterator<Entry<Long, Campaign>> cit = allCamps.entrySet().iterator(); cit.hasNext();){
			Entry<Long, Campaign> en = cit.next();
			Campaign camp = en.getValue();
			Map<String, List<Ad>> ads = camp.getAds();
			boolean changed = false;
			for(Iterator<Entry<String, List<Ad>>> it = ads.entrySet().iterator(); it.hasNext();) {
				Entry<String, List<Ad>> e = it.next();
				List<Ad> adlist = e.getValue();
				for (Ad ad : adlist) {
					if(ad.getId() != adid) continue;
					Map<String, String> catg = ad.getAdCategorys();
					if (catg != null) {
						catg.put(serial.name(), catgid);
						changed = true;
					} else {
						log.error("广告[" + ad.getId() + "/" + ad.getCreativeName() + "]的行业类别为空,请检查");
					}
				}
			}
			if(changed) campaignDAO.updateCampaign(camp);
		}
	}

	/**
	 * @param adx
	 * @param campaign
	 * 分渠道的增加活动竞价次数
	 */
	public void increCampBid(Adx adx, Campaign campaign) {
		//获取活动的竞价计数器
		AtomicInteger[] oldc = campBidCounter.get(campaign.getId());
		if (oldc != null) {
			switch (adx) {
			case TANX:
				//将tanx竞价次数加1
				oldc[0].getAndIncrement();
				break;
			case BES:
				//将bes竞价次数加1
				oldc[1].getAndIncrement();
				break;
			case VAM:
				//将vam竞价次数加1
				oldc[2].getAndIncrement();
				break;
			case XTRADER:
				oldc[3].getAndIncrement();
				break;
			case HZ:
				oldc[4].getAndIncrement();
				break;
			default:
				break;
			}
		} else {
			//如果campBidCounter ConcurrentHashMap 中没有这个活动的竞价次数，则重新创建一个记录活动竞价次数的数组。
			AtomicInteger[] c = new AtomicInteger[]{new AtomicInteger(), new AtomicInteger(), new AtomicInteger(),new AtomicInteger(),new AtomicInteger()};
			oldc = campBidCounter.putIfAbsent(campaign.getId(), c);
			if (oldc != null) {
				//分别给不同渠道的竞价次数加1
				switch (adx) {
				case TANX:
					oldc[0].getAndIncrement();
					break;
				case BES:
					oldc[1].getAndIncrement();
					break;
				case VAM:
					oldc[2].getAndIncrement();
					break;
				case XTRADER:
					oldc[3].getAndIncrement();
					break;
				case HZ:
					oldc[4].getAndIncrement();
					break;
				default:
					break;
				}
			} else {
				switch (adx) {
				case TANX:
					c[0].getAndIncrement();
					break;
				case BES:
					c[1].getAndIncrement();
					break;
				case VAM:
					c[2].getAndIncrement();
					break;
				case XTRADER:
					c[3].getAndIncrement();
					break;
				case HZ:
					c[4].getAndIncrement();
					break;
				default:
					break;
				}
			}
		}
	}

	/**
	 * @param adx
	 * @param campaign
	 * 分渠道的增加广告的竞价次数，同时增加创意的竞价次数
	 */
	public void increAdBid(Adx adx, Ad ad, long partnerid, long groupid, long campid, long creativeid) {

		AtomicInteger[] oldc = adBidCounter.get(ad.getId());
		if(oldc != null) {
			switch (adx) {
			case TANX:
				oldc[0].getAndIncrement();
				break;
			case BES:
				oldc[1].getAndIncrement();
				break;
			case VAM:
				oldc[2].getAndIncrement();
				break;
			case XTRADER:
				oldc[3].getAndIncrement();
				break;
			case HZ:
				oldc[4].getAndIncrement();
				break;
			default:
				break;
			}
		} else {
			AtomicInteger[] c = new AtomicInteger[] { new AtomicInteger(), new AtomicInteger(), new AtomicInteger(),new AtomicInteger(),new AtomicInteger()};
			oldc = adBidCounter.putIfAbsent(ad.getId(), c);
			if (oldc != null) {
				switch (adx) {
				case TANX:
					oldc[0].getAndIncrement();
					break;
				case BES:
					oldc[1].getAndIncrement();
					break;
				case VAM:
					oldc[2].getAndIncrement();
					break;
				case XTRADER:
					oldc[3].getAndIncrement();
					break;
				case HZ:
					oldc[4].getAndIncrement();
					break;
				default:
					break;
				}
			} else {
				switch (adx) {
				case TANX:
					c[0].getAndIncrement();
					break;
				case BES:
					c[1].getAndIncrement();
					break;
				case VAM:
					c[2].getAndIncrement();
					break;
				case XTRADER:
					c[3].getAndIncrement();
					break;
				case HZ:
					c[4].getAndIncrement();
					break;
				default:
					break;
				}
			}
		}

		String key = partnerid + "_" + groupid + "_" + campid + "_" + creativeid;
		oldc = creativeBidCounter.get(key);
		if(oldc != null) {
			switch (adx) {
			case TANX:
				oldc[0].getAndIncrement();
				break;
			case BES:
				oldc[1].getAndIncrement();
				break;
			case VAM:
				oldc[2].getAndIncrement();
				break;
			case XTRADER:
				oldc[3].getAndIncrement();
				break;
			case HZ:
				oldc[4].getAndIncrement();
				break;
			default:
				break;
			}
		} else {
			AtomicInteger[] c = new AtomicInteger[] { new AtomicInteger(), new AtomicInteger(), new AtomicInteger(),new AtomicInteger(),new AtomicInteger() };
			oldc = creativeBidCounter.putIfAbsent(key, c);
			if (oldc != null) {
				switch (adx) {
				case TANX:
					oldc[0].getAndIncrement();
					break;
				case BES:
					oldc[1].getAndIncrement();
					break;
				case VAM:
					oldc[2].getAndIncrement();
					break;
				case XTRADER:
					oldc[3].getAndIncrement();
					break;
				case HZ:
					oldc[4].getAndIncrement();
					break;
				default:
					break;
				}
			} else {
				switch (adx) {
				case TANX:
					c[0].getAndIncrement();
					break;
				case BES:
					c[1].getAndIncrement();
					break;
				case VAM:
					c[2].getAndIncrement();
					break;
				case XTRADER:
					c[3].getAndIncrement();
				case HZ:
					c[4].getAndIncrement();
				default:
					break;
				}
			}
		}
	}

	/**
	 * @param adx
	 * @param campaign
	 */
	public int getCampBid(Adx adx, Campaign campaign) {
		AtomicInteger[] c = campBidCounter.get(campaign.getId());
		if (c == null) return 0;
		switch (adx) {
		case TANX:
			return c[0].get();
		case BES:
			return c[1].get();
		case VAM:
			return c[2].get();
		case XTRADER:
			return c[3].get();
		case HZ:
			return c[4].get();
		default:
			return 0;
		}
	}
	
	/**
	 * @param adx
	 * @param campaign
	 */
	public int getAdBid(Adx adx, Ad ad) {
		AtomicInteger[] c = adBidCounter.get(ad.getId());
		if (c == null) return 0;
		switch (adx) {
		case TANX:
			return c[0].get();
		case BES:
			return c[1].get();
		case VAM:
			return c[2].get();
		case XTRADER:
			return c[3].get();
		case HZ:
			return c[4].get();
		default:
			return 0;
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() { semphore.release(); }
	public long delayOrIntervalMillis() { return MONITOR_INTERVAL; }
	public boolean isTriggerIndependently() { return false; }
	public Type type() { return Type.INTERVAL; }
}
