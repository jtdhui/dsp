package com.jtd.logcollector.service;

import com.alibaba.fastjson.JSON;
import com.jtd.logcollector.dao.CookieDataDAO;
import com.jtd.logcollector.po.Click;
import com.jtd.logcollector.po.Landing;
import com.jtd.logcollector.po.PV;
import com.jtd.logcollector.po.Param;
import com.jtd.logcollector.util.IPBUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.util.Base64;

import javax.sql.DataSource;
import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月12日
 * @项目名称 dsp-log-collector
 * @描述 <p>将数据写入数据</p>
 * 
 */
class Collector extends Thread {

	private static final Log log = LogFactory.getLog(Collector.class);
	//目录名称
	private static final String[] DIR_NAMES = { "pv", "click", "landing" };
	//162为cookiegid表中，基本的cookiegid最大值，就是说<=162的是基本cookiegid，大于则是非基本cookiegid
	private static final long TYPE1MAXID = 162;
	private static final int MAX_DETAIL_HOST = 100;
	private static final int MAX_DETAIL_ADP = 200;

	// z，当天的表中已经存在的数据记录，用来判断是insert还是update
	//数据库表定义
	public static TableDesc[] tableDesc = new TableDesc[16];
	static {
		int i = 0;
		tableDesc[i++] = new TableDesc("par_channeld", new String[] { "partner_id", "channel_id","host" }, new Class[] { Long.class, Long.class, String.class });
		tableDesc[i++] = new TableDesc("campgrp_channeld", new String[] { "campgroup_id","channel_id","host" }, new Class[] { Long.class, Long.class, String.class });
		tableDesc[i++] = new TableDesc("camp_channeld", new String[] { "camp_id", "channel_id", "host" }, new Class[] { Long.class, Long.class, String.class });
		tableDesc[i++] = new TableDesc("cre_channeld", new String[] { "camp_id", "creative_id", "channel_id", "host" }, new Class[] { Long.class, Long.class, Long.class, String.class });

		tableDesc[i++] = new TableDesc("par_adpd", new String[] { "partner_id", "channel_id","adp_id" }, new Class[] { Long.class, Long.class, String.class });
		tableDesc[i++] = new TableDesc("campgrp_adpd", new String[] { "campgroup_id","channel_id","adp_id" }, new Class[] { Long.class, Long.class, String.class });
		tableDesc[i++] = new TableDesc("camp_adpd", new String[] { "camp_id", "channel_id", "adp_id" }, new Class[] { Long.class, Long.class, String.class });
		tableDesc[i++] = new TableDesc("cre_adpd", new String[] { "camp_id", "creative_id", "channel_id", "adp_id" }, new Class[] { Long.class, Long.class, Long.class, String.class });
		
		tableDesc[i++] = new TableDesc("par_ckd", new String[] { "partner_id", "ck_id" }, new Class[] { Long.class, Long.class });
		tableDesc[i++] = new TableDesc("campgrp_ckd", new String[] { "campgroup_id", "ck_id" }, new Class[] { Long.class, Long.class });
		tableDesc[i++] = new TableDesc("camp_ckd", new String[] { "camp_id", "ck_id" }, new Class[] { Long.class, Long.class });
		tableDesc[i++] = new TableDesc("cre_ckd", new String[] { "camp_id", "creative_id", "ck_id" }, new Class[] { Long.class, Long.class, Long.class });

		tableDesc[i++] = new TableDesc("par_cityd", new String[] { "partner_id", "city_id" }, new Class[] { Long.class, Long.class });
		tableDesc[i++] = new TableDesc("campgrp_cityd", new String[] { "campgroup_id", "city_id" }, new Class[] { Long.class, Long.class });
		tableDesc[i++] = new TableDesc("camp_cityd", new String[] { "camp_id", "city_id" }, new Class[] { Long.class, Long.class });
		tableDesc[i++] = new TableDesc("cre_cityd", new String[] { "camp_id", "creative_id", "city_id" }, new Class[] { Long.class, Long.class, Long.class });
	}
	
	private CookieDataDAO cookieDataDAO;
	private DataSource countDataSource;
	private DataSource webDataSource;

	private IPBUtil iPBUtil;

	private File logDir;

	// z,任务队列[partnerid, ymd]
	private LinkedBlockingQueue<long[]> taskQueue = new LinkedBlockingQueue<long[]>();
	
	//要插入的数据
	private Map<String, Map<String, int[]>> tableMap = new TreeMap<String, Map<String, int[]>>();
	{
		//广告主,推广组，活动，创意，分别对应不同渠道的日报
		tableMap.put("par_channeld", new TreeMap<String, int[]>()); // 合作伙伴/渠道
		tableMap.put("campgrp_channeld", new TreeMap<String, int[]>()); // 推广组/渠道
		tableMap.put("camp_channeld", new TreeMap<String, int[]>()); // 推广活动/渠道
		tableMap.put("cre_channeld", new TreeMap<String, int[]>()); // 素材/渠道
		//广告主,推广组，活动，创意，分别对应不同广告位日报
		tableMap.put("par_adpd", new TreeMap<String, int[]>()); // 合作伙伴/广告位
		tableMap.put("campgrp_adpd", new TreeMap<String, int[]>()); // 推广组/广告位
		tableMap.put("camp_adpd", new TreeMap<String, int[]>()); // 推广活动/广告位
		tableMap.put("cre_adpd", new TreeMap<String, int[]>()); // 素材/广告位
		//广告主,推广组，活动，创意，分别对应不同人群的日报
		tableMap.put("par_ckd", new TreeMap<String, int[]>()); // 合作伙伴/人群
		tableMap.put("campgrp_ckd", new TreeMap<String, int[]>()); // 推广组/人群
		tableMap.put("camp_ckd", new TreeMap<String, int[]>()); // 推广活动/人群
		tableMap.put("cre_ckd", new TreeMap<String, int[]>()); // 素材/人群
		//广告主,推广组，活动，创意，分别对应不同地域的日报
		tableMap.put("par_cityd", new TreeMap<String, int[]>()); // 合作伙伴/地域
		tableMap.put("campgrp_cityd", new TreeMap<String, int[]>()); // 推广组/地域
		tableMap.put("camp_cityd", new TreeMap<String, int[]>()); // 推广活动/地域
		tableMap.put("cre_cityd", new TreeMap<String, int[]>()); // 素材/地域
	}
	/**
	 * 构造函数
	 * @param threadId						
	 * @param cookieDataDAO					redisA 15
	 * @param countDataSource				统计数据，数据源
	 * @param webDataSource					业务数据数据源
	 * @param logDir						日志目录
	 * @param iPBUtil						ip操作类，主要是通过ip，可以活动ip对应的地址
	 */
	Collector(int threadId, CookieDataDAO cookieDataDAO, DataSource countDataSource, DataSource webDataSource, File logDir, IPBUtil iPBUtil) {
		super("CollectorThread-" + threadId);
		this.cookieDataDAO = cookieDataDAO;
		this.countDataSource = countDataSource;
		this.webDataSource = webDataSource;
		this.logDir = logDir;
		this.iPBUtil = iPBUtil;
	}
	
	/**
	 * 向任务队列中添加任务对象
	 * @param task
	 * @return void
	 */
	public void addTask(long[] task) {
		taskQueue.offer(task);
	}

	/**
	 * Collector的run方法
	 */
	public void run() {
		log.info("CollectorThread is running");
		//如果线程没有被阻断，则一直运转下去
		for (; !Thread.interrupted();) {
			/**
			 * long[]数据结构：
			 * 		[0]=广告主id，就是拆分后的目录名
			 * 		[1]=yyyyMMdd
			 */
			long[] task = null;
			try {
				//从队列中取出任务数组
				//如果task为null，则阻断当前线程，直到队列中有了task数据时，才开始继续运转。
				task = taskQueue.take();
			} catch (InterruptedException e) {
				log.warn("CollectorThread被中断退出");
				break;
			}
			//调用日志中数据收集方法
			collect(task[0], (int) task[1]);
		}
	}

	/**
	 * old
	 * 汇总入库
	 * @param ymd						
	 * @param hour
	 * @param ids
	 */
	/**
	 * 汇总入库
	 * @param partnerId				广告主id
	 * @param ymd					yyyyMMdd
	 * @return void
	 */
	private void collect(long partnerId, final int ymd) {
		
		//目录: "目标目录/广告主id"
		File dir = new File(logDir, String.valueOf(partnerId));
		if (!dir.exists()) {
			log.info("日志目录没找到 partnerId:" + partnerId);
			return;
		}
		
		//文件过滤器；file对象是文件，并且文件名字是 yyyyMMdd 返回true
		FileFilter filter = new FileFilter() {
			@Override
			public boolean accept(File f) {
				return f.isFile() && f.getName().startsWith(String.valueOf(ymd));
			}
		};

		// z，当前伙伴相关的访客召回ID
		//从RETARGETPACKET表中取出，所有的访客找回对应的gid(gid就是cookiegid表中的id)
		Set<Long> retargetgids = getRetargetGids(partnerId);
		
		//DIR_NAMES[0]=pv,DIR_NAMES[1]=click,DIR_NAMES[2]=landing
		File pvdir = new File(dir, DIR_NAMES[0]);
		//如果“pv”不存在在，说明这个广告主没有对应的pv数据（这里为拆分后的日志数据）
		if (!pvdir.exists()) {
			log.info("PV日志目录没找到 partnerId:" + partnerId + ", ymd:" + ymd);
		} else {
			File[] files = pvdir.listFiles(filter);
			for (File file : files) {
				countPvFile(file, retargetgids);
				log.info(file.getAbsolutePath() + " 统计完成");
			}
		}
		
		//DIR_NAMES[0]=pv,DIR_NAMES[1]=click,DIR_NAMES[2]=landing
		File clickdir = new File(dir, DIR_NAMES[1]);
		//如果“click”不存在在，说明这个广告主没有对应的click数据（这里为拆分后的日志数据）
		if (!clickdir.exists()) {
			log.info("Click日志目录没找到 partnerId:" + partnerId + ", ymd:" + ymd);
		} else {
			File[] files = clickdir.listFiles(filter);
			for (File file : files) {
				countClickFile(file, retargetgids);
				log.info(file.getAbsolutePath() + " 统计完成");
			}
		}

		//DIR_NAMES[0]=pv,DIR_NAMES[1]=click,DIR_NAMES[2]=landing
		File landdir = new File(dir, DIR_NAMES[2]);
		//如果“landing”不存在在，说明这个广告主没有对应的landing数据（这里为拆分后的日志数据）
		if (!landdir.exists()) {
			log.info("Landing日志目录没找到 partnerId:" + partnerId + ", ymd:" + ymd);
		} else {
			File[] files = landdir.listFiles(filter);
			for (File file : files) {
				countLandingFile(file, retargetgids);
				log.info(file.getAbsolutePath() + " 统计完成");
			}
		}

		updateData(ymd);
		log.info(Thread.currentThread().getName() + "处理完" + partnerId + "_" + ymd);
	}
	
	/**
	 * 统计pv数据
	 * @param file						广告主pv日志文件
	 * @param retargetgids				广告主，所有访客找回的gid，就是RETARGETPACKET表中，这个广告主对应的gid
	 * @return void
	 */
	private void countPvFile(File file, Set<Long> retargetgids) {

		BufferedReader r = null;
		try {
			//这个文件读取缓冲对象
			r = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
			//一行一行的读取文件内容，每一行都是一个pv对象
			for(String line = r.readLine(); line != null; line = r.readLine()) {
				PV pv = null;
				try {
					pv = JSON.parseObject(line, PV.class);
				} catch (Exception e) {
					log.error("解析PV日志[" + line + "]发生错误", e);
				}
				//如果pv对象为null，则不处理
				if(pv == null) continue;
				//如果pv中的参数对象为null，则不处理。
				Param p = pv.getParam();
				if(p == null) continue;
				//如果这条数据是独立用户pv
				boolean isNew = pv.isUv();
				//获取cookieid在reidsA 15中对应的所有的cookiegid
				Map<Long, String[]> gids = null;
				String cookieid = pv.getCookieid();
				if (!StringUtils.isEmpty(cookieid)) {
					gids = cookieDataDAO.getCookieData(cookieid);
				}
				//如果请求是app请求，则返回app的packageName，否则返回请求的url。
				String host = getHostFromParam(p);
				if(StringUtils.isEmpty(host)) {
					host = "未知";
				}
				host = Base64.encodeBase64String(host.getBytes("UTF-8"));
				//拿到pv广告位id
				String adpid = p.getAdp();
				if(StringUtils.isEmpty(adpid)) {
					adpid = "未知广告位";
				}
				adpid = Base64.encodeBase64String(adpid.getBytes("UTF-8"));
				
				//通过参数列表中的ip，获得城市编号
				long cityid = getCityIdFromParam(p);
				
				Set<Long> ids = new TreeSet<Long>();
				if(gids != null) {
					for(Long id : gids.keySet()) {
						//这里是保证在ids中的cookiegid，一定是这个用户选择的，或是在基本cookiegid范围内。
						if(id <= TYPE1MAXID || retargetgids.contains(id)) {
							ids.add(id);
						}
					}
				}
				//--------------------广告主,推广组，活动，创意，分别对应不同渠道的日报-------------------
				//1、-----------------par_channeld,广告主、渠道每天统计------------------
				//key=广告主id_渠道id_请求时唯一标识
				String key = p.getP() + "_" + p.getCh() + "_" + host;
				//包含app应用id+广告位id+应用名称
				//String key1 = p.getAppid()+"$"+p.getAdp()+"$"+p.getAppn();
				
				int[] v = tableMap.get("par_channeld").get(key);
				if (v == null) {
					v = new int[9];
					tableMap.get("par_channeld").put(key, v);
				}
				//增加pv+1
				v[1]++;
				//如果是新增用户，则uv+1
				if (isNew) v[2]++;
				//如果pv的花费不为null，则v[7]=v[7]+pv花费，这里v[7]就是花费
				if (pv.getExpend() != null) v[7] += pv.getExpend().intValue();
				//增加成本，就是co
				v[8] += pv.getPrice().intValue();
				
				//2、---------------campgrp_channeld，推广组、渠道每天统计----------------
				//key=推广组id_渠道id_请求时唯一标识
				key = p.getG() + "_" + p.getCh() + "_" + host;
				v = tableMap.get("campgrp_channeld").get(key);
				if(v == null) {
					v = new int[9];
					tableMap.get("campgrp_channeld").put(key, v);
				}
				v[1]++;
				if(isNew) v[2]++;
				if(pv.getExpend() != null) v[7] += pv.getExpend().intValue();
				v[8] += pv.getPrice().intValue();
				
				//3、----------------camp_channeld,活动、渠道每天统计-----------------
				//key=活动id_渠道id_请求时唯一标识
				key = p.getCp() + "_" + p.getCh() + "_" + host;
				v = tableMap.get("camp_channeld").get(key);
				if(v == null) {
					v = new int[9];
					tableMap.get("camp_channeld").put(key, v);
				}
				v[1]++;
				if(isNew) v[2]++;
				if(pv.getExpend() != null) v[7] += pv.getExpend().intValue();
				v[8] += pv.getPrice().intValue();
				
				//4、-----------------cre_channeld,创意、渠道每天统计-------------------
				//key=创意id_渠道id)_请求时唯一标识
				key = p.getCp() + "_" + p.getCr() + "_" + p.getCh() + "_" + host;
				v = tableMap.get("cre_channeld").get(key);
				if(v == null) {
					v = new int[9];
					tableMap.get("cre_channeld").put(key, v);
				}
				v[1]++;
				if(isNew) v[2]++;
				if(pv.getExpend() != null) v[7] += pv.getExpend().intValue();
				v[8] += pv.getPrice().intValue();
				
				//--------------------广告主,推广组，活动，创意，分别对应不同广告位的日报-------------------
				//5、-----------------par_adpd，广告主、广告位每天统计------------------------
				key = p.getP() + "_" + p.getCh() + "_" + adpid;
				v = tableMap.get("par_adpd").get(key);
				if (v == null) {
					v = new int[9];
					tableMap.get("par_adpd").put(key, v);
				}
				v[1]++;
				if (isNew) v[2]++;
				if (pv.getExpend() != null) v[7] += pv.getExpend().intValue();
				v[8] += pv.getPrice().intValue();
				
				//6、-----------------campgrp_adpd，推广组、广告位每天统计------------------------
				key = p.getG() + "_" + p.getCh() + "_" + adpid;
				v = tableMap.get("campgrp_adpd").get(key);
				if(v == null) {
					v = new int[9];
					tableMap.get("campgrp_adpd").put(key, v);
				}
				v[1]++;
				if(isNew) v[2]++;
				if(pv.getExpend() != null) v[7] += pv.getExpend().intValue();
				v[8] += pv.getPrice().intValue();
				
				//7、-----------------camp_adpd，活动、广告位每天统计------------------------
				key = p.getCp() + "_" + p.getCh() + "_" + adpid;
				v = tableMap.get("camp_adpd").get(key);
				if(v == null) {
					v = new int[9];
					tableMap.get("camp_adpd").put(key, v);
				}
				v[1]++;
				if(isNew) v[2]++;
				if(pv.getExpend() != null) v[7] += pv.getExpend().intValue();
				v[8] += pv.getPrice().intValue();
				
				//8、-----------------cre_adpd，创意、广告位每天统计------------------------
				key = p.getCp() + "_" + p.getCr() + "_" + p.getCh() + "_" + adpid;
				v = tableMap.get("cre_adpd").get(key);
				if(v == null) {
					v = new int[9];
					tableMap.get("cre_adpd").put(key, v);
				}
				v[1]++;
				if(isNew) v[2]++;
				if(pv.getExpend() != null) v[7] += pv.getExpend().intValue();
				v[8] += pv.getPrice().intValue();
				
				//--------------------广告主,推广组，活动，创意，分别对应不同人群的日报-------------------
				//循环当前pv，对应的cookieid，所以对应的gid
				for(Long ckid : ids) {
					//9、-----------------par_ckd，广告主、cookiegid每天统计------------------------
					//key=广告主id_cookie对应的gid
					key = p.getP() + "_" + ckid;
					v = tableMap.get("par_ckd").get(key);
					if(v == null) {
						v = new int[9];
						tableMap.get("par_ckd").put(key, v);
					}
					v[1]++;
					if(isNew) v[2]++;
					if(pv.getExpend() != null) v[7] += pv.getExpend().intValue();
					v[8] += pv.getPrice().intValue();
					
					//10、-----------------campgrp_ckd，推广组、cookiegid每天统计------------------------
					//key=推广组id_cookiegid
					key = p.getG() + "_" + ckid;
					v = tableMap.get("campgrp_ckd").get(key);
					if(v == null) {
						v = new int[9];
						tableMap.get("campgrp_ckd").put(key, v);
					}
					v[1]++;
					if(isNew) v[2]++;
					if(pv.getExpend() != null) v[7] += pv.getExpend().intValue();
					v[8] += pv.getPrice().intValue();
					
					//11、-----------------camp_ckd，活动、cookiegid每天统计------------------------
					//key=活动id_cookiegid
					key = p.getCp() + "_" + ckid;
					v = tableMap.get("camp_ckd").get(key);
					if(v == null) {
						v = new int[9];
						tableMap.get("camp_ckd").put(key, v);
					}
					v[1]++;
					if(isNew) v[2]++;
					if(pv.getExpend() != null) v[7] += pv.getExpend().intValue();
					v[8] += pv.getPrice().intValue();
					
					//12、-----------------cre_ckd，活动、cookiegid每天统计------------------------
					//key=创意id_cookiegid
					key = p.getCp() + "_" + p.getCr() + "_" + ckid;
					v = tableMap.get("cre_ckd").get(key);
					if(v == null) {
						v = new int[9];
						tableMap.get("cre_ckd").put(key, v);
					}
					v[1]++;
					if(isNew) v[2]++;
					if(pv.getExpend() != null) v[7] += pv.getExpend().intValue();
					v[8] += pv.getPrice().intValue();
				}
				
				//--------------------广告主,推广组，活动，创意，分别对应不同人群的日报-------------------
				//13、-----------------par_cityd，广告主、地域每天统计------------------------
				//key=广告主id_地域id
				key = p.getP() + "_" + cityid;
				v = tableMap.get("par_cityd").get(key);
				if(v == null) {
					v = new int[9];
					tableMap.get("par_cityd").put(key, v);
				}
				v[1]++;
				if(isNew) v[2]++;
				if(pv.getExpend() != null) v[7] += pv.getExpend().intValue();
				v[8] += pv.getPrice().intValue();
				
				//14、-----------------campgrp_cityd，推广组、地域每天统计------------------------
				//key=广告主id_地域id
				key = p.getG() + "_" + cityid;
				v = tableMap.get("campgrp_cityd").get(key);
				if(v == null) {
					v = new int[9];
					tableMap.get("campgrp_cityd").put(key, v);
				}
				v[1]++;
				if(isNew) v[2]++;
				if(pv.getExpend() != null) v[7] += pv.getExpend().intValue();
				v[8] += pv.getPrice().intValue();
				
				//15、-----------------camp_cityd，活动、地域每天统计------------------------
				//key=活动id_地域id
				key = p.getCp() + "_" + cityid;
				v = tableMap.get("camp_cityd").get(key);
				if(v == null) {
					v = new int[9];
					tableMap.get("camp_cityd").put(key, v);
				}
				v[1]++;
				if(isNew) v[2]++;
				if(pv.getExpend() != null) v[7] += pv.getExpend().intValue();
				v[8] += pv.getPrice().intValue();
				
				//16、-----------------cre_cityd，创意、地域每天统计------------------------
				//key=活动id_地域id
				key = p.getCp() + "_" + p.getCr() + "_" + cityid;
				v = tableMap.get("cre_cityd").get(key);
				if(v == null) {
					v = new int[9];
					tableMap.get("cre_cityd").put(key, v);
				}
				v[1]++;
				if(isNew) v[2]++;
				if(pv.getExpend() != null) v[7] += pv.getExpend().intValue();
				v[8] += pv.getPrice().intValue();
			}
		} catch (Exception e) {
			log.error("统计" + file.getAbsolutePath() + "发生错误", e);
		} finally {
			if(r != null) try { r.close(); } catch (IOException e) {}
		}
	}
	
	/**
	 * 统计点击文件
	 * @param file							拆分好的文件
	 * @param retargetgids					获得这个广告主，关于访客找回的所有 gid
	 * @return void
	 */
	private void countClickFile(File file, Set<Long> retargetgids) {
		
		BufferedReader r = null;
		try {
			r = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
			for(String line = r.readLine(); line != null; line = r.readLine()) {
				Click click = null;
				try {
					click = JSON.parseObject(line, Click.class);
				} catch (Exception e) {
					log.error("解析Click日志[" + line + "]发生错误", e);
				}
				//拿到点击对象
				if(click == null) continue;
				//拿到点击是的参数对象
				Param p = click.getParam();
				if(p == null) continue;
				//获取这次点击是否为新增用户
				boolean isNew = click.isUv();
				//拿到点击时的cookieid，并从redisA 15，中cookieid对应的所有的cookiegid
				Map<Long, String[]> gids = null;
				String cookieid = click.getCookieid();
				if(!StringUtils.isEmpty(cookieid)) {
					gids = cookieDataDAO.getCookieData(cookieid);
				}
				//请求时的唯一标识，如果是app时为 packageName；如果为web时则为请求的url
				String host = getHostFromParam(p);
				if(StringUtils.isEmpty(host)) {
					host = "未知";
				}
				host = Base64.encodeBase64String(host.getBytes("UTF-8"));
				
				//拿到请求时的广告位id
				String adpid = p.getAdp();
				if(StringUtils.isEmpty(adpid)) {
					adpid = "未知广告位";
				}
				adpid = Base64.encodeBase64String(adpid.getBytes("UTF-8"));
				//拿到请求时的城市id
				long cityid = getCityIdFromParam(p);
				
				
				Set<Long> ids = new TreeSet<Long>();
				if(gids != null) {
					for(Long id : gids.keySet()) {
						//这里是保证在ids中的cookiegid，一定是这个用户选择的，或是在基本cookiegid范围内。
						if(id <= TYPE1MAXID || retargetgids.contains(id)) {
							ids.add(id);
						}
					}
				}

				//--------------------广告主,推广组，活动，创意，分别对应不同渠道的click日报-------------------
				//1、-----------------par_channeld,广告主、渠道每天click统计------------------
				//key=广告主id_渠道id_请求时唯一标识
				String key = p.getP() + "_" + p.getCh() + "_" + host;
				int[] v = tableMap.get("par_channeld").get(key);
				if(v == null) {
					v = new int[9];
					tableMap.get("par_channeld").put(key, v);
				}
				v[3]++;
				if(isNew) v[4]++;
				if(click.getExpend() != null) v[7] += click.getExpend().intValue();
				
				//2、-----------------campgrp_channeld,推广组、渠道每天click统计------------------
				//key=推广组id_渠道id_请求时唯一标识
				key = p.getG() + "_" + p.getCh() + "_" + host;
				v = tableMap.get("campgrp_channeld").get(key);
				if(v == null) {
					v = new int[9];
					tableMap.get("campgrp_channeld").put(key, v);
				}
				v[3]++;
				if(isNew) v[4]++;
				if(click.getExpend() != null) v[7] += click.getExpend().intValue();
				
				//3、-----------------camp_channeld，活动、渠道每天click统计------------------
				//key=活动id_渠道id_请求时唯一标识
				key = p.getCp() + "_" + p.getCh() + "_" + host;
				v = tableMap.get("camp_channeld").get(key);
				if(v == null) {
					v = new int[9];
					tableMap.get("camp_channeld").put(key, v);
				}
				v[3]++;
				if(isNew) v[4]++;
				if(click.getExpend() != null) v[7] += click.getExpend().intValue();
				
				//4、-----------------cre_channeld，创意、渠道每天click统计------------------
				//key=活动id_创意id_渠道id_请求时唯一标识
				key = p.getCp() + "_" + p.getCr() + "_" + p.getCh() + "_" + host;
				v = tableMap.get("cre_channeld").get(key);
				if(v == null) {
					v = new int[9];
					tableMap.get("cre_channeld").put(key, v);
				}
				v[3]++;
				if(isNew) v[4]++;
				if(click.getExpend() != null) v[7] += click.getExpend().intValue();
				
				//--------------------广告主,推广组，活动，创意，分别对应不同广告位的click日报-------------------
				//5、-----------------par_adpd,广告主、广告位每天click统计------------------
				//key=广告主id_渠道id_广告位id
				key = p.getP() + "_" + p.getCh() + "_" + adpid;
				v = tableMap.get("par_adpd").get(key);
				if(v == null) {
					v = new int[9];
					tableMap.get("par_adpd").put(key, v);
				}
				v[3]++;
				if(isNew) v[4]++;
				if(click.getExpend() != null) v[7] += click.getExpend().intValue();
				//6、-----------------campgrp_adpd,推广组、广告位每天click统计------------------
				//key=推广组id_渠道id_广告位id
				key = p.getG() + "_" + p.getCh() + "_" + adpid;
				v = tableMap.get("campgrp_adpd").get(key);
				if(v == null) {
					v = new int[9];
					tableMap.get("campgrp_adpd").put(key, v);
				}
				v[3]++;
				if(isNew) v[4]++;
				if(click.getExpend() != null) v[7] += click.getExpend().intValue();
				
				//7、-----------------camp_adpd,活动、广告位每天click统计------------------
				//key=活动id_渠道id_广告位id
				key = p.getCp() + "_" + p.getCh() + "_" + adpid;
				v = tableMap.get("camp_adpd").get(key);
				if(v == null) {
					v = new int[9];
					tableMap.get("camp_adpd").put(key, v);
				}
				v[3]++;
				if(isNew) v[4]++;
				if(click.getExpend() != null) v[7] += click.getExpend().intValue();
				
				//8、-----------------cre_adpd,创意、广告位每天click统计------------------
				//key=活动id_创意id_渠道id_广告位id
				key = p.getCp() + "_" + p.getCr() + "_" + p.getCh() + "_" + adpid;
				v = tableMap.get("cre_adpd").get(key);
				if(v == null) {
					v = new int[9];
					tableMap.get("cre_adpd").put(key, v);
				}
				v[3]++;
				if(isNew) v[4]++;
				if(click.getExpend() != null) v[7] += click.getExpend().intValue();
				
				//--------------------广告主,推广组，活动，创意，分别对应不同cookiegid click日报-------------------
				for(Long ckid : ids) {
					//9、-----------------par_ckd,广告主、gid每天click统计------------------
					//key=广告主id_gid
					key = p.getP() + "_" + ckid;
					v = tableMap.get("par_ckd").get(key);
					if(v == null) {
						v = new int[9];
						tableMap.get("par_ckd").put(key, v);
					}
					v[3]++;
					if(isNew) v[4]++;
					if(click.getExpend() != null) v[7] += click.getExpend().intValue();
					
					//10、-----------------campgrp_ckd,推广组、gid每天click统计------------------
					//key=推广组id_gid
					key = p.getG() + "_" + ckid;
					v = tableMap.get("campgrp_ckd").get(key);
					if(v == null) {
						v = new int[9];
						tableMap.get("campgrp_ckd").put(key, v);
					}
					v[3]++;
					if(isNew) v[4]++;
					if(click.getExpend() != null) v[7] += click.getExpend().intValue();
					
					//11、-----------------camp_ckd,活动、gid每天click统计------------------
					//key=活动id_gid
					key = p.getCp() + "_" + ckid;
					v = tableMap.get("camp_ckd").get(key);
					if(v == null) {
						v = new int[9];
						tableMap.get("camp_ckd").put(key, v);
					}
					v[3]++;
					if(isNew) v[4]++;
					if(click.getExpend() != null) v[7] += click.getExpend().intValue();
					
					//12、-----------------cre_ckd,创意、gid每天click统计------------------
					//key=活动id_创意id_gid
					key = p.getCp() + "_" + p.getCr() + "_" + ckid;
					v = tableMap.get("cre_ckd").get(key);
					if(v == null) {
						v = new int[9];
						tableMap.get("cre_ckd").put(key, v);
					}
					v[3]++;
					if(isNew) v[4]++;
					if(click.getExpend() != null) v[7] += click.getExpend().intValue();
				}
				
				//--------------------广告主,推广组，活动，创意，分别对应不同地域 click日报-------------------
				//13、-----------------par_cityd,广告主、地域每天click统计------------------
				//key=广告主id_地域id
				key = p.getP() + "_" + cityid;
				v = tableMap.get("par_cityd").get(key);
				if(v == null) {
					v = new int[9];
					tableMap.get("par_cityd").put(key, v);
				}
				v[3]++;
				if(isNew) v[4]++;
				if(click.getExpend() != null) v[7] += click.getExpend().intValue();
				
				//14、-----------------campgrp_cityd,推广组、地域每天click统计------------------
				//key=推广组id_地域id
				key = p.getG() + "_" + cityid;
				v = tableMap.get("campgrp_cityd").get(key);
				if(v == null) {
					v = new int[9];
					tableMap.get("campgrp_cityd").put(key, v);
				}
				v[3]++;
				if(isNew) v[4]++;
				if(click.getExpend() != null) v[7] += click.getExpend().intValue();
				
				//15、-----------------camp_cityd,活动、地域每天click统计------------------
				//key=活动id_地域id
				key = p.getCp() + "_" + cityid;
				v = tableMap.get("camp_cityd").get(key);
				if(v == null) {
					v = new int[9];
					tableMap.get("camp_cityd").put(key, v);
				}
				v[3]++;
				if(isNew) v[4]++;
				if(click.getExpend() != null) v[7] += click.getExpend().intValue();
				
				//16、-----------------cre_cityd,创意、地域每天click统计------------------
				//key=活动id_创意id_地域id
				key = p.getCp() + "_" + p.getCr() + "_" + cityid;
				v = tableMap.get("cre_cityd").get(key);
				if(v == null) {
					v = new int[9];
					tableMap.get("cre_cityd").put(key, v);
				}
				v[3]++;
				if(isNew) v[4]++;
				if(click.getExpend() != null) v[7] += click.getExpend().intValue();
			}
		} catch (Exception e) {
			log.error("统计" + file.getAbsolutePath() + "发生错误", e);
		} finally {
			if(r != null) try { r.close(); } catch (IOException e) {}
		}
	}
	
	/**
	 * 后效果统计
	 * @param file						按广告主拆分后的日志文件
	 * @param retargetgids				获得这个广告主，关于访客找回的所有 gid
	 * @return void
	 */
	private void countLandingFile(File file, Set<Long> retargetgids) {
		BufferedReader r = null;
		try {
			r = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
			//一行一行的读取文件
			for(String line = r.readLine(); line != null; line = r.readLine()) {
				Landing landing = null;
				try {
					landing = JSON.parseObject(line, Landing.class);
				} catch (Exception e) {
					log.error("解析Landing日志[" + line + "]发生错误", e);
				}
				//如果landing对象为null，则不做任何处理
				if(landing == null) continue;
				//获取landing时的参数对象，如果为null，则不做任何处理
				Param p = landing.getClick().getParam();
				if(p == null) continue;
				
				//获取是否为独立用户
				boolean isNew = landing.isUv();
				Map<Long, String[]> gids = null;
				//拿到landing时的cookieid
				String cookieid = landing.getCookieid();
				//通过cookieid，获取关于这个cookieid的所有 cookiegid对象
				if(!StringUtils.isEmpty(cookieid)) {
					gids = cookieDataDAO.getCookieData(cookieid);
				}
				
				//获取请求的唯一标示，如果是app则这里是packageName，否则为url
				String host = getHostFromParam(p);
				if(StringUtils.isEmpty(host)) {
					host = "未知";
				}
				host = Base64.encodeBase64String(host.getBytes("UTF-8"));
				
				//拿到广告位id
				String adpid = p.getAdp();
				if(StringUtils.isEmpty(adpid)) {
					adpid = "未知广告位";
				}
				adpid = Base64.encodeBase64String(adpid.getBytes("UTF-8"));
				
				//获取地域id
				long cityid = getCityIdFromParam(p);
				
				Set<Long> ids = new TreeSet<Long>();
				if(gids != null) {
					for(Long id : gids.keySet()) {
						//检查cookiegid是否在广告主对应的RETARGETPACKET表中存在，或者是否是小于基础cookiegid，如果是则参与统计
						if(id <= TYPE1MAXID || retargetgids.contains(id)) {
							ids.add(id);
						}
					}
				}
				
				//--------------------广告主,推广组，活动，创意，分别对应不同渠道的landing日报-------------------
				//1、-----------------par_channeld,广告主、渠道每天landing统计------------------
				//key=广告主id_渠道id_请求时唯一标识
				String key = p.getP() + "_" + p.getCh() + "_" + host;
				int[] v = tableMap.get("par_channeld").get(key);
				if(v == null) {
					v = new int[9];
					tableMap.get("par_channeld").put(key, v);
				}
				v[5]++;
				if(isNew) v[6]++;
				
				//2、-----------------campgrp_channeld,推广组、渠道每天landing统计------------------
				//key=推广组id_渠道id_请求时唯一标识
				key = p.getG() + "_" + p.getCh() + "_" + host;
				v = tableMap.get("campgrp_channeld").get(key);
				if(v == null) {
					v = new int[9];
					tableMap.get("campgrp_channeld").put(key, v);
				}
				v[5]++;
				if(isNew) v[6]++;
				
				//3、-----------------camp_channeld,活动、渠道每天landing统计------------------
				//key=活动id_渠道id_请求时唯一标识
				key = p.getCp() + "_" + p.getCh() + "_" + host;
				v = tableMap.get("camp_channeld").get(key);
				if(v == null) {
					v = new int[9];
					tableMap.get("camp_channeld").put(key, v);
				}
				v[5]++;
				if(isNew) v[6]++;
				
				//4、-----------------cre_channeld,创意、渠道每天landing统计------------------
				//key=活动id_创意id_渠道id_请求时唯一标识
				key = p.getCp() + "_" + p.getCr() + "_" + p.getCh() + "_" + host;
				v = tableMap.get("cre_channeld").get(key);
				if(v == null) {
					v = new int[9];
					tableMap.get("cre_channeld").put(key, v);
				}
				v[5]++;
				if(isNew) v[6]++;
				
				//--------------------广告主,推广组，活动，创意，分别对应不同广告位的landing日报-------------------
				//5、-----------------par_adpd,广告主、广告位每天landing统计------------------
				//key=广告主id_渠道id_广告位id
				key = p.getP() + "_" + p.getCh() + "_" + adpid;
				v = tableMap.get("par_adpd").get(key);
				if(v == null) {
					v = new int[9];
					tableMap.get("par_adpd").put(key, v);
				}
				v[5]++;
				if(isNew) v[6]++;
				
				//6、-----------------campgrp_adpd,推广组、广告位每天landing统计------------------
				//key=推广组id_渠道id_广告位id
				key = p.getG() + "_" + p.getCh() + "_" + adpid;
				v = tableMap.get("campgrp_adpd").get(key);
				if(v == null) {
					v = new int[9];
					tableMap.get("campgrp_adpd").put(key, v);
				}
				v[5]++;
				if(isNew) v[6]++;
				
				//7、-----------------camp_adpd,活动、广告位每天landing统计------------------
				//key=活动id_渠道id_广告位id
				key = p.getCp() + "_" + p.getCh() + "_" + adpid;
				v = tableMap.get("camp_adpd").get(key);
				if(v == null) {
					v = new int[9];
					tableMap.get("camp_adpd").put(key, v);
				}
				v[5]++;
				if(isNew) v[6]++;
				
				//8、-----------------cre_adpd,创意、广告位每天landing统计------------------
				//key=活动id_创意id_渠道id_广告位id
				key = p.getCp() + "_" + p.getCr() + "_" + p.getCh() + "_" + adpid;
				v = tableMap.get("cre_adpd").get(key);
				if(v == null) {
					v = new int[9];
					tableMap.get("cre_adpd").put(key, v);
				}
				v[5]++;
				if(isNew) v[6]++;
				
				//--------------------广告主,推广组，活动，创意，分别对应不同cookiegid landing日报-------------------
				for(Long ckid : ids) {
					//9、-----------------par_ckd,广告主、cookiegid每天landing统计------------------
					//key=广告主id_cookiegid
					key = p.getP() + "_" + ckid;
					v = tableMap.get("par_ckd").get(key);
					if(v == null) {
						v = new int[9];
						tableMap.get("par_ckd").put(key, v);
					}
					v[5]++;
					if(isNew) v[6]++;
					
					//10、-----------------campgrp_ckd,推广组、cookiegid每天landing统计------------------
					//key=推广组id_cookiegid
					key = p.getG() + "_" + ckid;
					v = tableMap.get("campgrp_ckd").get(key);
					if(v == null) {
						v = new int[9];
						tableMap.get("campgrp_ckd").put(key, v);
					}
					v[5]++;
					if(isNew) v[6]++;
					
					//11、-----------------camp_ckd,活动、cookiegid每天landing统计------------------
					//key=活动id_cookiegid
					key = p.getCp() + "_" + ckid;
					v = tableMap.get("camp_ckd").get(key);
					if(v == null) {
						v = new int[9];
						tableMap.get("camp_ckd").put(key, v);
					}
					v[5]++;
					if(isNew) v[6]++;
					
					//12、-----------------cre_ckd,创意、cookiegid每天landing统计------------------
					//key=活动id_创意id_cookiegid
					key = p.getCp() + "_" + p.getCr() + "_" + ckid;
					v = tableMap.get("cre_ckd").get(key);
					if(v == null) {
						v = new int[9];
						tableMap.get("cre_ckd").put(key, v);
					}
					v[5]++;
					if(isNew) v[6]++;
				}
				
				//--------------------广告主,推广组，活动，创意，分别对应不同地域 landing日报-------------------
				//13、-----------------par_cityd,广告主、地域每天landing统计------------------
				//key=广告主id_地域id
				key = p.getP() + "_" + cityid;
				v = tableMap.get("par_cityd").get(key);
				if(v == null) {
					v = new int[9];
					tableMap.get("par_cityd").put(key, v);
				}
				v[5]++;
				if(isNew) v[6]++;
				
				//14、-----------------campgrp_cityd,推广组、地域每天landing统计------------------
				//key=推广组id_地域id
				key = p.getG() + "_" + cityid;
				v = tableMap.get("campgrp_cityd").get(key);
				if(v == null) {
					v = new int[9];
					tableMap.get("campgrp_cityd").put(key, v);
				}
				v[5]++;
				if(isNew) v[6]++;
				
				//15、-----------------camp_cityd,活动、地域每天landing统计------------------
				//key=活动id_地域id
				key = p.getCp() + "_" + cityid;
				v = tableMap.get("camp_cityd").get(key);
				if(v == null) {
					v = new int[9];
					tableMap.get("camp_cityd").put(key, v);
				}
				v[5]++;
				if(isNew) v[6]++;
				
				//16、-----------------cre_cityd,创意、地域每天landing统计------------------
				//key=活动id_创意id_地域id
				key = p.getCp() + "_" + p.getCr() + "_" + cityid;
				v = tableMap.get("cre_cityd").get(key);
				if(v == null) {
					v = new int[9];
					tableMap.get("cre_cityd").put(key, v);
				}
				v[5]++;
				if(isNew) v[6]++;
			}
		} catch(Exception e) {
			log.error("统计" + file.getAbsolutePath() + "发生错误", e);
		} finally {
			if(r != null) try { r.close(); } catch (IOException e) {}
		}
	}
	/**
	 * 如果请求是app请求，则返回app的packageName，否则返回请求的url。
	 * @param p				pv中保存的参数列表
	 * @return
	 * @return String
	 */
	private String getHostFromParam(Param p) {
		if (p.isApp()) {
			return p.getAppp();
		} else {
			String url = p.getUrl();
			if (!StringUtils.isEmpty(url)) {
				try {
					URL u = new URL(url);
					return u.getHost();
				} catch (Exception e) {
				}
			}
		}
		return null;
	}
	/**
	 * 通过参数列表中的ip，获得城市编号
	 * @param p
	 * @return
	 * @return long
	 */
	private long getCityIdFromParam(Param p) {
		String ip = p.getIp();
		Integer i = iPBUtil.getCityIdByIp(ip);
		return i == null ? 0 : i.longValue();
	}
	
	/**
	 * 获得这个广告主，关于访客找回的所有 gid
	 * @param partnerid
	 * @return
	 * @return Set<Long>
	 */
	private Set<Long> getRetargetGids(long partnerid) {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		Set<Long> ret = new TreeSet<Long>();
		try {
			conn = webDataSource.getConnection();
			st = conn.createStatement();
			rs = st.executeQuery("select cookie_gid from retarget_packet where delete_status=0 and owner_partner_id=" + partnerid);
			for(;rs.next();) {
				ret.add(rs.getLong(1));
			}
		} catch (Exception e) {
			log.error("加载访客召回ID发生错误", e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e) {
				}
			}
		}
		return ret;
	}

	/**
	 * 入库
	 * 
	 * @param ymd
	 */
	private void updateData(int ymd) {
		Connection conn = null;
		try {
			conn = countDataSource.getConnection();
			conn.setAutoCommit(false);
			
			//循环表定义数组
			for(TableDesc desc : tableDesc) {
				//根据表名取出，应该向这个表中添加的数据
				Map<String, int[]> data = tableMap.get(desc.tableName);
				
				//如果desc对应的表是 广告主渠道、推广组渠道、活动渠道、创意渠道，这4张表中的任何一张，则都要合并长尾流量
				if ("par_channeld".equals(desc.tableName)
						|| "campgrp_channeld".equals(desc.tableName)
						|| "camp_channeld".equals(desc.tableName)
						|| "cre_channeld".equals(desc.tableName)) {
					// 渠道的四张表要把长尾的域名合并成其他
					data = mergeData(data);
				}
				
				//如果desc对应的表是 广告主广告位、推广组广告位、活动广告位、创意广告位，这4张表中的任何一张，则都要合并长尾流量
				if ("par_adpd".equals(desc.tableName)
						|| "campgrp_adpd".equals(desc.tableName)
						|| "camp_adpd".equals(desc.tableName)
						|| "cre_adpd".equals(desc.tableName)) {
					// 渠道的四张表要把长尾的域名合并成其他
					data = mergeAdpData(data);
				}
				
				//如果data为null则不做入库处理
				if(data.size() == 0) continue;
				//拼装insert语句
				PreparedStatement insertPst = buildInsertPst(conn, desc);
				
				//填充insert语句中的？
				for(Iterator<Entry<String, int[]>> it = data.entrySet().iterator(); it.hasNext();) {
					Entry<String, int[]> e = it.next();
					String key = e.getKey();
					String[] filedvs = key.split("_");
					int[] value = e.getValue();
					value[7] /= 1000;
					value[8] /= 1000;

					int i = 1;
					for (int j = 0; j < desc.filedTypes.length; j++) {
						if (desc.filedTypes[j] == Long.class) {
							insertPst.setLong(i++, Long.parseLong(filedvs[j]));
						} else if (desc.filedTypes[j] == String.class) {
							insertPst.setString(i++, new String(Base64.decodeBase64(filedvs[j]), "UTF-8"));
						} else if (desc.filedTypes[j] == Integer.class) {
							insertPst.setInt(i++, Integer.parseInt(filedvs[j]));
						}
					}
					for (int v : value) {
						insertPst.setInt(i++, v);
					}
					insertPst.setInt(i++, ymd);
					insertPst.addBatch();
				}
				//执行这条sql语句
				insertPst.executeBatch();
				log.info("插入" + desc.tableName + "统计结果");
			}
			//提交所有的sql语句
			conn.commit();

			// 已入库的清空
			for (TableDesc desc : tableDesc) {
				tableMap.get(desc.tableName).clear();
			}
		} catch (Exception e) {
			log.error("数据入库发生错误", e);
			try { conn.rollback(); } catch (Exception e1) {}
		} finally {
			if (conn != null) {
				try { conn.close(); } catch (Exception e) {}
			}
		}
	}

	/**
	 * 把长尾的host的数据累积成其他
	 * 流量只占总流量的10%以下的媒体或是排名100名以后的媒体，同一归结为长尾流量
	 * @param data
	 * @return
	 */
	private Map<String, int[]> mergeData(Map<String, int[]> data) {
		Map<String, List<MergeData>> hmap = new TreeMap<String, List<MergeData>>();
		//data.key=(广告主id||推广组id||活动id||创意id)_(渠道id||广告为id||cookiegid||地域id)_||host
		//data.value=   [0]=bid，[1]=pv,[2]=uv,[3]=click,[4]=uclick,[5]=landing,[6]=vlanding,[7]=e,[8]=co
		for(Iterator<Entry<String, int[]>> it = data.entrySet().iterator(); it.hasNext();) {
			Entry<String, int[]> e = it.next();
			String key = e.getKey();
			int i = key.lastIndexOf("_");
			//如果有host，则获取host前面的key，例如：广告主id_渠道id
			String newKey = key.substring(0, i);
			//获取host
			String host = key.substring(i + 1);
			//获取统计的值
			int[] value = e.getValue();
			//创建合并数据的
			List<MergeData> l = hmap.get(newKey);
			if(l == null) {
				l = new ArrayList<MergeData>();
				hmap.put(newKey, l);
			}
			l.add(new MergeData(host,value));
		}
		/**
		 * 上面的操作 hmap 结构为：
		 * key=广告主id_渠道id||推广组id_渠道id||活动id_渠道id||活动id_创意id_渠道id
		 * value=List<MergeData>
		 * 			MergeData.host=host
		 * 			MergeData.统计值=[0]=bid，[1]=pv,[2]=uv,[3]=click,[4]=uclick,[5]=landing,[6]=vlanding,[7]=e,[8]=co
		 */
		
		Map<String, int[]> ret = new TreeMap<String, int[]>();
		/**
		 * 定义对象比较器
		 */
		Comparator<MergeData> c = new Comparator<MergeData>() {
			@Override
			public int compare(MergeData d1, MergeData d2) {
				return d2.data[1] - d1.data[1];
			}
		};
		//循环准备好的，hmap集合对象
		for(Iterator<Entry<String, List<MergeData>>> it = hmap.entrySet().iterator(); it.hasNext();) {
			Entry<String, List<MergeData>> e = it.next();
			String prefix = e.getKey() + "_";
			List<MergeData> l = e.getValue();
			Collections.sort(l, c);
			int totalpv = 0;
			for(MergeData hd : l) totalpv += hd.data[1];
			int tpv = 0;
			int[] other = null;
			for(int i = 0; i < l.size(); i++) {

				MergeData h = l.get(i);
				if(totalpv <= 0) {
					ret.put(prefix + h.datekey, h.data);
					continue;
				}
				//流量只占总流量的10%以下的媒体或是排名100名以后的媒体，同一归结为长尾流量
				if (tpv * 100 / totalpv > 90 || i >= MAX_DETAIL_HOST) {
					// 超过90%的或者排名超过MAX_DETAIL_HOST的累加到其他
					if(other == null) other = new int[9];
					for(int j = 0; j < 9; j++) {
						other[j] += h.data[j];
					}
				} else {
					tpv += h.data[1];
					ret.put(prefix + h.datekey, h.data);
				}
			}

			if(other != null) {
				try { ret.put(prefix + Base64.encodeBase64String("其他".getBytes("UTF-8")), other); } catch (Exception e1) {}
			}
		}
		return ret;
	}
	
	/**
	 * 把长尾的host的数据累积成其他
	 * @param data
	 * @return
	 */
	private Map<String, int[]> mergeAdpData(Map<String, int[]> data) {
		Map<String, List<MergeData>> hmap = new TreeMap<String, List<MergeData>>();
		for(Iterator<Entry<String, int[]>> it = data.entrySet().iterator(); it.hasNext();) {
			Entry<String, int[]> e = it.next();
			String key = e.getKey();
			int i = key.lastIndexOf("_");
			String newKey = key.substring(0, i);
			String adpid = key.substring(i + 1);
			int[] value = e.getValue();
			
			List<MergeData> l = hmap.get(newKey);
			if(l == null) {
				l = new ArrayList<MergeData>();
				hmap.put(newKey, l);
			}
			l.add(new MergeData(adpid,value));
		}
		
		Map<String, int[]> ret = new TreeMap<String, int[]>();
		Comparator<MergeData> c = new Comparator<MergeData>() {
			@Override
			public int compare(MergeData d1, MergeData d2) {
				return d2.data[1] - d1.data[1];
			}
		};
		for(Iterator<Entry<String, List<MergeData>>> it = hmap.entrySet().iterator(); it.hasNext();) {
			Entry<String, List<MergeData>> e = it.next();
			String prefix = e.getKey() + "_";
			List<MergeData> l = e.getValue();
			Collections.sort(l, c);
			int totalpv = 0;
			for(MergeData hd : l) totalpv += hd.data[1];
			int tpv = 0;
			int[] other = null;
			for(int i = 0; i < l.size(); i++) {

				MergeData h = l.get(i);
				if(totalpv <= 0) {
					ret.put(prefix + h.datekey, h.data);
					continue;
				}
				
				if (tpv * 100 / totalpv > 90 || i >= MAX_DETAIL_ADP) {
					// 超过90%的或者排名超过MAX_DETAIL_ADP的累加到其他
					if(other == null) other = new int[9];
					for(int j = 0; j < 9; j++) {
						other[j] += h.data[j];
					}
				} else {
					tpv += h.data[1];
					ret.put(prefix + h.datekey, h.data);
				}
			}

			if(other != null) {
				try { ret.put(prefix + Base64.encodeBase64String("其他".getBytes("UTF-8")), other); } catch (Exception e1) {}
			}
		}
		return ret;
	}
	
	/**
	 * @作者 Amos Xu
	 * @版本 V1.0
	 * @配置 
	 * @创建日期 2016年11月13日
	 * @项目名称 dsp-log-collector
	 * @描述 <p></p>
	 */
	private static class MergeData {
		private String datekey;
		private int[] data;
		private MergeData(String datekey, int[] data){
			this.datekey = datekey;
			this.data = data;
		}
	}
	
	/**
	 * 构建Insert语句
	 * @param conn
	 * @param desc
	 * @return
	 * @throws Exception
	 * @return PreparedStatement
	 */
	private PreparedStatement buildInsertPst(Connection conn, TableDesc desc) throws Exception {
		//获取表名
		String tablename = desc.tableName;
		//insert语句前半部分
		StringBuilder sb = new StringBuilder("insert into ").append(tablename).append(" values(");
		//特殊字段个数+统计值个数，如果是小时统计在加上小时字段；得到的数字，是在sql语句中应该拼装多少个 ？
		int filedNum = desc.filedNames.length + (desc.isHour ? 12 : 10);
		for(int i = 0; i < filedNum; i++) {
			sb.append("?,");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append(")");
		PreparedStatement pst = conn.prepareStatement(sb.toString());
		return pst;
	}

	public static class TableDesc {
		private String tableName;
		private String[] filedNames;
		private Class<?>[] filedTypes;
		private boolean isHour;

		private TableDesc(String tableName, String[] filedNames, Class<?>[] filedTypes) {
			this(tableName, filedNames, filedTypes, false);
		}
		private TableDesc(String tableName, String[] filedNames, Class<?>[] filedTypes, boolean isHour) {
			this.tableName = tableName;
			this.filedNames = filedNames;
			this.filedTypes = filedTypes;
			this.isHour = isHour;
		}
		public String getTableName() {
			return tableName;
		}
		public String[] getFiledNames() {
			return filedNames;
		}
		public Class<?>[] getFiledTypes() {
			return filedTypes;
		}
		public boolean isHour() {
			return isHour;
		}
	}
}
