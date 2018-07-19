package com.jtd.statistic.service;

import com.alibaba.druid.util.StringUtils;
import com.jtd.statistic.dao.*;
import com.jtd.statistic.po.Click;
import com.jtd.statistic.po.Landing;
import com.jtd.statistic.po.PV;
import com.jtd.statistic.po.Param;
import com.jtd.statistic.util.SystemTime;
import com.jtd.web.constants.CampaignAutoStatus;
import com.jtd.web.constants.CampaignManulStatus;
import com.jtd.web.constants.CampaignType;
import com.jtd.web.constants.ExpendType;
import com.jtd.web.model.Campaign;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.LinkedBlockingQueue;

/**
* @作者: Amos Xu
* @日期: 17/8/16
* @时间: 下午3:41
* @配置:
* @描述: <p></p>
*/
class CountThread  extends Thread {

	private final Logger logMyDebug = LogManager.getLogger("myDebugLog");
	
	private static final Log log = LogFactory.getLog(CountThread.class);
	//线程需要处理的消息队列，这里主要存放了，pv，click，landing等三种不同的消息。
	private LinkedBlockingQueue<Object> queue;
	//日志队列
	private LinkedBlockingQueue<Object> logqueue;
	private int maxLogQueueSize;
	private CampaignDAO campaignDAO;
	private FreqDataDAO freqDataDAO;
	private DistinctDAO distinctDAO;
	private CountDAO countDAO;
	private CountDAO countDAO4Click;
	private ClickDAO clickDAO;
	//写日志线程
	private WriteLogThread writeLogThread;
	//统计线程构造函数
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
	CountThread(int id, LinkedBlockingQueue<Object> queue, CampaignDAO campaignDAO, FreqDataDAO freqDataDAO, DistinctDAO distinctDAO, CountDAO countDAO, CountDAO countDAO4Click, ClickDAO clickDAO, SystemTime sysTime, String logDir, String nodeName, UncaughtExceptionHandler h, int maxLogQueueSize) {
		super("CountThread-" + id);
		//这个线程需要处理的数据
		this.queue = queue;
		//活动相关的数据访问层，本地数据库
		this.campaignDAO = campaignDAO;
		//投放频次数据访问层，redis集群A
		this.freqDataDAO = freqDataDAO;
		//redis，集群B
		this.distinctDAO = distinctDAO;
		//集群B
		this.countDAO = countDAO;
		//集群B
		this.countDAO4Click = countDAO4Click;
		//集群B
		this.clickDAO = clickDAO;
		//队列所能存放的数据的上线个数
		this.maxLogQueueSize = maxLogQueueSize;
		//日志队列，里面存放的是需要写成日志文件的数据
		logqueue = new LinkedBlockingQueue<Object>();
		//实例化一个写日志的的线程，专门处理，写日志的操作，
		writeLogThread = new WriteLogThread(id, logqueue, sysTime, logDir, nodeName);
		writeLogThread.setUncaughtExceptionHandler(h);
		writeLogThread.start();
	}
	
	public int isThreadAlive() {
		boolean countAlive = this.isAlive();
		boolean logAlive = writeLogThread.isAlive();
		//如果当前的统计线程状态是激活，则取值 0 ，否则 取值 2
		//如果当前的写日志线程状态是激活，则取值 0，否则 取值 1
		//那么当两个线程状态都为激活状态的话 返回0
		//当统计线程为激活状态，写入状态为非活动状态，返回1
		//当统计状态为非活动，写入线程为活动状态，返回 2
		//当两个都为非活动状态，返回 3
		return (countAlive ? 0 : 2) + (logAlive ? 0 : 1);
	}
	/**
	 * 当destroy被都要用,说明系统要停止，统计线程和日志写入线程<br/>
	 * 系统先要调用当前线程的 interrupt，用于停止当前统计线程
	 * 在调用日志写入线程的 interrupt,停止日志写入线程
	 */
	public void destroy() {
		this.interrupt();
		writeLogThread.interrupt();
	}

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		log.info(Thread.currentThread().getName() + " is running");
		//如果线程为非阻断状态，则一直循环
		for (; !Thread.interrupted();) {
			Object o = null;
			try {
				//取出队列中排在第一位的数据，如果队列中没有数据，则当前线程阻断，直到队列中有数据的时候继续运行。
				o = queue.take();
			} catch (InterruptedException e) {
				break;
			}

			if (o instanceof PV) {
				increPv((PV) o);
			} else if (o instanceof Click) {
				increClick((Click) o);
			} else if (o instanceof Landing) {
				increLanding((Landing) o);
			}
		}
	}

	/**
	 * 增加pv数据
	 * @param pv
	 */
	private void increPv(PV pv) {

		Param p = pv.getParam();
		//活动id
		long campid = p.getCp();
		//通过活动id，从本地reids中取出活动数据
		Campaign camp = campaignDAO.getCampaignById(campid);
		//如果活动数据为null，则写入错误日志数据
		if(camp == null) {
			log.error("根据推广活动ID[" + p.getCp() + "]没有找到推广活动");
			return;
		}
		//如果活动的自动状态，和手动状态都不是线上
		if(!(camp.getAutoStatus() == CampaignAutoStatus.ONLINE && camp.getManulStatus() == CampaignManulStatus.ONLINE)) {
			//控制台输出
			log.info("暂停状态的推广活动: " + camp.getId() + "/" + camp.getCampName());
			return;
		}
		//获取活动的广告主id
		long partnerid = camp.getPartnerId();
		//获取活动的推广组id
		long groupid = camp.getGroupId();

		// 看是否是独立UV
		String userid = null;
		boolean isuv  = true;
		if(camp.getCampType() == CampaignType.PC) {//如果活动类型是PC
			userid = pv.getCookieid();
			isuv = distinctDAO.isNew(pv.getCookieid(), campid, 0);
		} else if(camp.getCampType() == CampaignType.APP) {
			userid = p.getDev();
			isuv = distinctDAO.isNew(p.getDev(), campid, 0);
		} else if(camp.getCampType() == CampaignType.WAP) {
			userid = pv.getCookieid();
			isuv = distinctDAO.isNew(pv.getCookieid(), campid, 0);
		}
		pv.setUv(isuv);

		// 频次
		if(!StringUtils.isEmpty(userid)) {
			int[] freqt = campaignDAO.getCampFreqType(campid);
			//频次数据不为null，并且频次设置的是展示，或是展示和点击的，写入到redis中
			if(freqt != null && (freqt[0] == 1 || freqt[0] == 3)) {
				freqDataDAO.increPv(userid, campid, freqt[1] * 3600);
			}
		}

		// 插redis
		//广告曝光的竞价价格
		int cost = pv.getPrice().intValue();
		logMyDebug.info("count->写入缓存>>cost,成本>>"+cost);
		if (camp.getExpendType() == ExpendType.CPM) {//如果是cpm结算
			//从本地redis中获取毛利率
			int gross = campaignDAO.getCampGrossProfit(camp.getPartnerId(), camp.getId());
			//如果毛利大于100，说明设置错误。则将毛利的值设置为30，并输出个错误日志
			if(gross >= 100) {
				gross = 30;
				log.error("错误的毛利率:" + gross);
			}
			//
			//成交价格*100/(100-毛利)
			int expend = pv.getPrice().multiply(new BigDecimal(100)).divide(new BigDecimal(100 - gross), 2, RoundingMode.CEILING).intValue();
			//获取活动的出价，这个价格是活动的出价价格
			int pprice = camp.getPrice();
			//去最小的成交价格和出价价格，取最小的价格，最为用户的消耗价格
			expend = Math.min(expend, pprice);
			
			logMyDebug.info("count->写入缓存>>expend>>"+expend);
			//将消耗价格写入pv对象中
			pv.setExpend(new BigDecimal(expend));
			countDAO.increPvCostExpend(partnerid, groupid, campid, p.getCr(), isuv, cost, expend);
		} else {//非cpm结算
			pv.setExpend(null);
			countDAO.increPvCost(partnerid, groupid, campid, p.getCr(), isuv, cost);
		}

		// 写日志，如果负载高,PV的日志可能会扔一部分
		if(maxLogQueueSize > 0) {
			if(logqueue.size() < maxLogQueueSize) {
				logqueue.offer(pv);
			} else {
				log.error("统计线程" + Thread.currentThread().getName() + "日志队列达到上限，暂停写PV日志");
			}
		} else {
			logqueue.offer(pv);
		}
	}

	/**
	 * 增加点击数据
	 * @param click
	 */
	private void increClick(Click click) {
		//取出点击对象中的参数对象
		Param p = click.getParam();
		//取出广告活动id
		long campid = p.getCp();
		//根据广告活动获取活动对象
		Campaign camp = campaignDAO.getCampaignById(campid);
		if(camp == null) {
			log.error("根据推广活动ID[" + p.getCp() + "]没有找到推广活动");
			return;
		}
		//如果活动的自动状态和手动状态，不是ONLINE，则控制台输出信息
		if (!(camp.getAutoStatus() == CampaignAutoStatus.ONLINE && camp.getManulStatus() == CampaignManulStatus.ONLINE)) {
			log.info("不是投放中状态的推广活动: " + camp.getId() + "/" + camp.getCampName());
			return;
		}
		//获取广告主id
		long partnerid = camp.getPartnerId();
		//获取推广组id
		long groupid = camp.getGroupId();

		String userid = null;
		boolean isuv  = true;
		//根据活动的类型是pc，app，还是wap的不同，使用cookieid，还是deviceId
		if(camp.getCampType() == CampaignType.PC) {
			userid = click.getCookieid();
			isuv = distinctDAO.isNew(click.getCookieid(), campid, 1);
		} else if(camp.getCampType() == CampaignType.APP) {
			userid = p.getDev();
			isuv = distinctDAO.isNew(p.getDev(), campid, 1);
		} else if(camp.getCampType() == CampaignType.WAP) {
			userid = click.getCookieid();
			isuv = distinctDAO.isNew(click.getCookieid(), campid, 1);
		}
		//设置是否为独立用户
		click.setUv(isuv);

		// 频次
		if(!StringUtils.isEmpty(userid)) {
			int[] freqt = campaignDAO.getCampFreqType(campid);
			//频次数据不为null，并且频次设置的是点击，或是展示和点击的，写入到redis中
			if(freqt != null && (freqt[0] == 2 || freqt[0] == 3)) {
				freqDataDAO.increClick(userid, campid, freqt[1] * 3600);
			}
		}

		// 插redis
		if (camp.getExpendType() == ExpendType.CPC) {//如果是cpc结算
			//获取活动的毛利
			int gross = campaignDAO.getCampGrossProfit(camp.getPartnerId(), camp.getId());
			//如果活动毛利草果100，说明设置错误，输出错误日志，然后将毛利设置为30.
			if(gross >= 100) {
				gross = 30;
				log.error("错误的毛利率:" + gross);
			}
			//countDAO4Click表示非管道方式执行reids命令
			//获取点击的平均价格
			int clk = countDAO4Click.getCampCostPerClick(campid);
			//获取一个cpm价格，这里是用，用户设置的单次曝光价格*1000
			int pprice = camp.getPrice() * 1000;
			//如果没有点击的平均价格，则默认使用，用户设置的曝光数*1000，
			if(clk == Integer.MAX_VALUE) {
				click.setExpend(new BigDecimal(pprice));
				//使用管道方式，向redis中写入用户的花费
				countDAO.increClickExpend(partnerid, groupid, campid, p.getCr(), isuv, pprice);
			} else {
				//如果有点击的平均价格，则用户的花费=点击价格*100/(100-毛利)
				int expend = new BigDecimal(clk).multiply(new BigDecimal(100)).divide(new BigDecimal(100 - gross), 2, RoundingMode.CEILING).intValue();
				//用户的实际花费和用户设置的cpc价格，哪个低使用哪个
				expend = Math.min(expend, pprice);
				//给click对象设置实际花费
				click.setExpend(new BigDecimal(expend));
				//使用管道方式，想reids中写入用户的花费
				countDAO.increClickExpend(partnerid, groupid, campid, p.getCr(), isuv, expend);
			}
		} else {
			//如果用户的结算类型是cpm，那么在点击的时候，不设置用户花费
			click.setExpend(null);
			countDAO.increClick(partnerid, groupid, campid, p.getCr(), isuv);
		}

		// 写日志
		logqueue.offer(click);
	}

	/**
	 * 增加落地页数据
	 * @param landing
	 */
	private void increLanding(Landing landing) {
		
		Click click = clickDAO.getClick(landing.getClickKey());
		if(click == null) return;
		landing.setClick(click);

		Param p = click.getParam();
		long campid = p.getCp();
		Campaign camp = campaignDAO.getCampaignById(campid);
		if(camp == null) {
			log.error("根据推广活动ID[" + p.getCp() + "]没有找到推广活动");
			return;
		}
		long partnerid = camp.getPartnerId();
		long groupid = camp.getGroupId();

		// 看是否是独立UV
		boolean isuv  = true;
		if(camp.getCampType() == CampaignType.PC) {
			isuv = distinctDAO.isNew(click.getCookieid(), campid, 2);
		} else if(camp.getCampType() == CampaignType.APP) {
			isuv = distinctDAO.isNew(p.getDev(), campid, 2);
		} else if(camp.getCampType() == CampaignType.WAP) {
			isuv = distinctDAO.isNew(click.getCookieid(), campid, 2);
		}
		landing.setUv(isuv);

		// 插redis
		countDAO.increLanding(partnerid, groupid, campid, p.getCr(), isuv);

		// 写日志
		logqueue.offer(landing);

	}
}
