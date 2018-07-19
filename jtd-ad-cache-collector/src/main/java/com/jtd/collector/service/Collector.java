package com.jtd.collector.service;

import com.jtd.collector.dao.CampaignDAO;
import com.jtd.collector.dao.CountDAO;
import com.jtd.collector.util.SystemTime;
import com.jtd.collector.util.Timer;
import com.jtd.collector.util.TimerTask;
import com.jtd.web.model.Campaign;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月12日
 * @项目名称 dsp-cache-collector
 * @描述 <p>将redsB中的统计数据，汇总存放到mysql中</p>
 */
class Collector extends Thread {

	private static final Log log = LogFactory.getLog(Collector.class);

	private SystemTime systemTime;
	private CampaignDAO campaignDAO;
	private CountDAO countDAO;
	private DataSource dataSource;
	
	/**
	 * key=表名
	 * value=Map<String,int[]>
	 * 		 key=统计的维度，例如：广告主统计，则是广告主id；如果统计的广告主,创意，则key就是 广告主id_创意id
	 *       value=[0]=竞价,[1]=pv,[2]=uv,[3]=click,[4]=userClick,[5]=landing,[6]=userLanding,[7]=花费,[8]=成本
	 * 一般情况下，一个表名，对应着一个Map对象；
	 */
	private Map<String, Map<String, int[]>> tableMap = new TreeMap<String, Map<String, int[]>>();
	//定义数据库表
	private TableDesc[] tableDesc = new TableDesc[21];
	{
		int i = 0;
		tableMap.put("pard", new TreeMap<String, int[]>()); // 合作伙伴分日
		tableDesc[i++] = new TableDesc("pard", new String[] { "partner_id" }, new Class[] { Long.class }, false);
		
		tableMap.put("parh", new TreeMap<String, int[]>()); // 合作伙伴分日/小时
		tableDesc[i++] = new TableDesc("parh", new String[] { "partner_id" }, new Class[] { Long.class }, true);
		
		tableMap.put("par_adtpd", new TreeMap<String, int[]>()); // 合作伙伴/广告形式分日
		tableDesc[i++] = new TableDesc("par_adtpd", new String[] { "partner_id", "ad_type" }, new Class[] { Long.class, Integer.class }, false);
		
		tableMap.put("par_trans_tpd", new TreeMap<String, int[]>()); // 合作伙伴/交易类型分日
		tableDesc[i++] = new TableDesc("par_trans_tpd", new String[] { "partner_id", "trans_type" }, new Class[] { Long.class, Integer.class }, false);
		//------------------
		tableMap.put("campgrpd", new TreeMap<String, int[]>()); // 推广组分日
		tableDesc[i++] = new TableDesc("campgrpd", new String[] { "partner_id", "campgroup_id" }, new Class[] { Long.class, Long.class }, false);
		
		tableMap.put("campgrph", new TreeMap<String, int[]>()); // 推广组分日/小时
		tableDesc[i++] = new TableDesc("campgrph", new String[] { "campgroup_id" }, new Class[] { Long.class }, true);
		
		tableMap.put("campgrp_ad_tpd", new TreeMap<String, int[]>()); // 推广组/广告形式分日
		tableDesc[i++] = new TableDesc("campgrp_ad_tpd", new String[] { "campgroup_id", "ad_type" }, new Class[] { Long.class, Integer.class }, false);
		
		tableMap.put("campgrp_trans_tpd", new TreeMap<String, int[]>()); // 推广组/交易类型分日
		tableDesc[i++] = new TableDesc("campgrp_trans_tpd", new String[] { "campgroup_id", "trans_type" }, new Class[] { Long.class, Integer.class }, false);
		//------------
		tableMap.put("campd", new TreeMap<String, int[]>()); // 推广活动分日
		tableDesc[i++] = new TableDesc("campd", new String[] { "partner_id", "campgroup_id", "camp_id", "camp_type" }, new Class[] { Long.class, Long.class, Long.class, Integer.class }, false);
		
		tableMap.put("camph", new TreeMap<String, int[]>()); // 推广活动分日/小时
		tableDesc[i++] = new TableDesc("camph", new String[] { "partner_id", "campgroup_id", "camp_id", "camp_type" }, new Class[] { Long.class, Long.class, Long.class, Integer.class }, true);
		
		tableMap.put("camp_ad_tpd", new TreeMap<String, int[]>()); // 推广活动/广告形式分日
		tableDesc[i++] = new TableDesc("camp_ad_tpd", new String[] { "camp_id", "ad_type" }, new Class[] { Long.class, Integer.class }, false);
		
		tableMap.put("camp_trans_tpd", new TreeMap<String, int[]>()); // 推广活动/交易类型分日
		tableDesc[i++] = new TableDesc("camp_trans_tpd", new String[] { "camp_id", "trans_type" }, new Class[] { Long.class, Integer.class }, false);
		//-------------
		tableMap.put("par_cred", new TreeMap<String, int[]>()); // 合作伙伴/素材分日
		tableDesc[i++] = new TableDesc("par_cred", new String[] { "partner_id", "creative_id" }, new Class[] { Long.class, Long.class }, false);
		
		tableMap.put("campgrp_cred", new TreeMap<String, int[]>()); // 推广组/素材分日
		tableDesc[i++] = new TableDesc("campgrp_cred", new String[] { "campgroup_id", "creative_id" }, new Class[] { Long.class, Long.class }, false);
		
		tableMap.put("camp_cred", new TreeMap<String, int[]>()); // 推广活动/素材分日
		tableDesc[i++] = new TableDesc("camp_cred", new String[] { "camp_id", "creative_id" }, new Class[] { Long.class, Long.class }, false);
		//--------------
		tableMap.put("par_cregrpd", new TreeMap<String, int[]>()); // 合作伙伴/素材组分日
		tableDesc[i++] = new TableDesc("par_cregrpd", new String[] { "partner_id", "cregrp_id" }, new Class[] { Long.class, Long.class }, false);
		
		tableMap.put("campgrp_cregrpd", new TreeMap<String, int[]>()); // 推广组/素材组分日
		tableDesc[i++] = new TableDesc("campgrp_cregrpd", new String[] { "campgroup_id", "cregrp_id" }, new Class[] { Long.class, Long.class }, false);
		
		tableMap.put("camp_cre_grpd", new TreeMap<String, int[]>()); // 推广活动/素材组分日
		tableDesc[i++] = new TableDesc("camp_cre_grpd", new String[] { "camp_id", "cregrp_id" }, new Class[] { Long.class, Long.class }, false);
		//--------------
		tableMap.put("par_sized", new TreeMap<String, int[]>()); // 合作伙伴/素材尺寸分日
		tableDesc[i++] = new TableDesc("par_sized", new String[] { "partner_id", "size_id" }, new Class[] { Long.class, Long.class }, false);
		
		tableMap.put("campgrp_sized", new TreeMap<String, int[]>()); // 推广组/素材尺寸分日
		tableDesc[i++] = new TableDesc("campgrp_sized", new String[] { "campgroup_id", "size_id" }, new Class[] { Long.class, Long.class }, false);
		
		tableMap.put("camp_sized", new TreeMap<String, int[]>()); // 推广活动/素材尺寸分日
		tableDesc[i++] = new TableDesc("camp_sized", new String[] { "camp_id", "size_id" }, new Class[] { Long.class, Integer.class }, false);
	}

	// 没有数据的key和第一次发现没有数据的时间
	// 超过三天没有数据的，从Redis中移除
	////如果返回的grpsizeid中没有数据，将广告主id，推广组id，活动id，创意id，拼装成一个key，放的本地缓存中，经过3天后的时间，从redis中清除
	private ConcurrentHashMap<String, Long> nodataids = new ConcurrentHashMap<String, Long>();

	// 当天和当前小时表中已经存在的数据记录，用来判断是insert还是update
	private Set<String> existRecordSet = new TreeSet<String>();

	Collector(CampaignDAO campaignDAO, final CountDAO countDAO, DataSource dataSource, final SystemTime systemTime, Timer timer) {
		super("CollectorThread");
		this.systemTime = systemTime;
		this.campaignDAO = campaignDAO;
		this.countDAO = countDAO;
		this.dataSource = dataSource;
		initExistRecordSet();
		timer.timing(new TimerTask() {
			@Override
			public void run() {
				long now = systemTime.getTime();
				for(Iterator<Entry<String, Long>> it = nodataids.entrySet().iterator(); it.hasNext();) {
					Entry<String, Long> e = it.next();
					if(now - e.getValue() > 259200000l) {//检查本地缓存中的数据，是否已经超过3天。
						it.remove();
						countDAO.removeId(e.getKey());
					}
				}
			}
			@Override
			public Type type() { return Type.INTERVAL; }
			@Override
			public long delayOrIntervalMillis() { return 3600000l; }//每一小时执行一次
			@Override
			public boolean isTriggerIndependently() { return false; }
		});
	}

	/**
	 * 查各张表把当前小时/日的记录的key加载进来
	 */
	private void initExistRecordSet() {
		int hour = systemTime.getHour();
		int ymd = systemTime.getYyyyMMdd();
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection();
			st = conn.createStatement();
			//经过下面一些列操作最终生成的 recordKey 能唯一标示整个统计数据库中的某条数据
			//将这个唯一的key，添加到 existRecordSet 中，便于之后判断，是做更新还是做插入操作
			for(TableDesc desc : tableDesc) {
				//拼装查询sql，并执行sql语句
				rs = st.executeQuery(buildSql(desc, ymd, hour));
				Class<?>[] types = desc.filedTypes;
				for(;rs.next();) {
					int i = 1;
					//拼装能唯一标示记录的key
					StringBuilder recordKey = new StringBuilder((desc.isHour ? ymd + "_" + hour : ymd) + "_" + desc.tableName);
					//循环 TableDesc 对象，中配置的类型集合，这个类型集合是，每个统计表中特殊的字段的类型，比如：广告主id，创意id等
					//由于这些特殊字段，和表中的字段顺序一致，所以，可以在rs中指定下标，获取特殊字段的值
					for(Class<?> t : types) {
						if(t == Long.class) {
							recordKey.append(rs.getLong(i++)).append("_");
						} else if(t == Integer.class) {
							recordKey.append(rs.getInt(i++)).append("_");
						}
					}
					recordKey.deleteCharAt(recordKey.length() - 1);
					addRecord(recordKey.toString());
				}
				rs.close();
			}
		} catch (Exception e) {
			log.error("加载已存在的记录发生错误", e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e) {
				}
			}
		}
	}
	
	/**
	 * 构建查询sql语句，查询的，字段 是TableDesc对象中设置的字段，表名是TableDesc对象中的表名，
	 * where 是date，如果是小事统计，则条件增加 hour
	 * @param desc
	 * @param ymd
	 * @param hour
	 * @return
	 * @return String
	 */
	private String buildSql(TableDesc desc, int ymd, int hour) {
		StringBuilder sql = new StringBuilder("select ");
		for (String flied : desc.filedNames) {
			sql.append(flied).append(",");
		}
		sql.deleteCharAt(sql.length() - 1);
		sql.append(" from ").append(desc.tableName).append(" where date=").append(ymd);
		if (desc.isHour) {
			sql.append(" and hour=").append(hour);
		}
		return sql.toString();
	}

	/**
	 * 启动当前线程
	 */
	public void run() {

		log.debug("CollectorThread is running");
		//获取系统时间的小时
		int hour = systemTime.getHour();
		//获取系统时间，yyyyMMdd
		int ymd = systemTime.getYyyyMMdd();
		//如果当前线程没有被阻断，则一直循环下去
		for (; !Thread.interrupted();) {
			//获取当前时间，的小时，和yyyyMMdd
			int nowHour = systemTime.getHour();
			int nowYmd = systemTime.getYyyyMMdd();
			//拿到的ids结构为
			//key=partnerid_groupid_campid
			//value=set<Long>,set中存放的是创意id
			Map<String, Set<Long>> ids = countDAO.getAllIds();
			if (nowHour != hour) {//如果执行程序的时候，跨不同的小时

				// 上一小时的入库
				collect(ymd, hour, ids);

				// 清理existRecordSet
				clearRecordSet(ymd, hour);
				if(nowYmd != ymd) clearRecordSet(ymd);

				// 当前小时的入库
				collect(nowYmd, nowHour, ids);
				hour = nowHour;
				ymd = nowYmd;
			} else {//执行程序的时候，是在同一个小时内，则只刷新小时数据
				// 刷新当前小时的
				collect(nowYmd, nowHour, ids);
			}
			try {
				Thread.sleep(5000);//每个5秒钟运行一次
			} catch (InterruptedException e) {
				log.error("统计汇总线程被中断", e);
				break;
			}
		}
	}

	/**
	 * 清理不需要的记录
	 * @param ymd
	 * @param hour
	 */
	private void clearRecordSet(int ymd, int hour) {
		String prefix = ymd + "_" + hour;
		for(Iterator<String> it = existRecordSet.iterator(); it.hasNext();) {
			String key = it.next();
			if(key.startsWith(prefix)) it.remove();
		}
	}
	private void clearRecordSet(int ymd) {
		for(Iterator<String> it = existRecordSet.iterator(); it.hasNext();) {
			String key = it.next();
			if(key.startsWith(String.valueOf(ymd))) it.remove();
		}
	}

	/**
	 * 汇总入库
	 * @param ymd
	 * @param hour
	 * @param ids 结构	key=partnerid_groupid_campid
	 * 					value=set<Long>,set中存放的是创意id
	 */
	private void collect(int ymd, int hour, Map<String, Set<Long>> ids) {
		for (Iterator<Entry<String, Set<Long>>> it = ids.entrySet().iterator(); it.hasNext();) {
			Entry<String, Set<Long>> e = it.next();
			//key的内容 partnerid_groupid_campid
			String pgcid = e.getKey();
			//拆分，fileds[0]=partnerid,fileds[1]，fileds[2]=campid
			String[] fileds = pgcid.split("_");
			//如果取出的id字符串，格式不正确，则不入库
			if(fileds.length != 3) {
				log.error("ID串格式错误: " + pgcid);
				continue;
			}
			//广告主id
			String partnerid = fileds[0];
			//推广组id
			String groupid = fileds[1];
			//活动id
			String campid = fileds[2];
			//通过活动id取出本地缓存中的，活动那个对象
			Campaign c = campaignDAO.getCampaignById(Long.parseLong(campid));
			if (c == null) {
				log.error("根据ID[" + campid + "]查找推广活动没找到");
				continue;
			}
			//PC_BANNER(0, "pc banner"), PC_VIDEO(1, "pc视频"), PC_URL(2, "文字链"), PC_IMGANDTEXT(3, "图文"), APP_BANNER(4, "banner in app"), 
			//APP_FULLSCREEN(5, "插屏全屏 in app"), APP_MESSAGE(6, "原生-信息流"),APP_FOCUS(7, "原生-焦点图"),APP_VIDEO(8,"视频贴片 in app"),
			//WAP(9, "wap"),  WAP_VIDEO(10, "视频贴片-wap");
			//拿到广告展现形式的code，
			int adtype = c.getAdType().getCode();
			//获取活动的交易类型RTB(0,"RTB"),PMP(1,"PMP");
			int transtype = c.getTransType().getCode();
			//获取活动那个的类型PC(0, "pc广告"), APP(1, "app广告"), WAP(2, "wap广告");
			int camptype = c.getCampType().getCode();
			//-------------------------一、拿到活动每天的统计数据----------------------------
			//获取活动 天的 统计数据，[0]=竞价,[1]=pv,[2]=uv,[3]=click,[4]=userClick,[5]=landing,[6]=userLanding,[7]=花费,[8]=成本
			int[] campday = countDAO.getCampDayCount(Long.parseLong(campid), ymd);
			if(campday != null) {//如果活动的天数据不为null
				//1、记录成广告主每天数据
				int[] v = tableMap.get("pard").get(partnerid);
				if (v == null) {
					v = new int[campday.length];
					tableMap.get("pard").put(partnerid, v);
				}
				add(campday, v);
				//2、记录成广告主，广告类型，每天的数据
				String key = partnerid + "_" + adtype;
				v = tableMap.get("par_adtpd").get(key);
				if (v == null) {
					v = new int[campday.length];
					tableMap.get("par_adtpd").put(key, v);
				}
				add(campday, v);
				
				//3、记录成广告主，交易类型每天数据
				key = partnerid + "_" + transtype;
				v = tableMap.get("par_trans_tpd").get(key);
				if (v == null) {
					v = new int[campday.length];
					tableMap.get("par_trans_tpd").put(key, v);
				}
				add(campday, v);
				
				//4、推广组每天的数据
				key = partnerid + "_" + groupid;
				v = tableMap.get("campgrpd").get(key);
				if (v == null) {
					v = new int[campday.length];
					tableMap.get("campgrpd").put(key, v);
				}
				add(campday, v);
				
				//5、记录成推广组，广告类型每天的数据
				key = groupid + "_" + adtype;
				v = tableMap.get("campgrp_ad_tpd").get(key);
				if (v == null) {
					v = new int[campday.length];
					tableMap.get("campgrp_ad_tpd").put(key, v);
				}
				add(campday, v);
				
				//6、记录成推广组，交易类型每天的数据
				key = groupid + "_" + transtype;
				v = tableMap.get("campgrp_trans_tpd").get(key);
				if (v == null) {
					v = new int[campday.length];
					tableMap.get("campgrp_trans_tpd").put(key, v);
				}
				add(campday, v);
				
				//7、记录活动，每天的数据，这里的类型是活动的类型，例如，pc，app，wap
				tableMap.get("campd").put(partnerid + "_" + groupid + "_" + campid + "_" + camptype, campday);
				//8、记录成活动，广告类型，每天的数据
				tableMap.get("camp_ad_tpd").put(campid + "_" + adtype, campday);
				//9、记录成活动，交易类型每天的数据
				tableMap.get("camp_trans_tpd").put(campid + "_" + transtype, campday);
			}
			
			//----------------二、从redis中，获取活动每小时数据，返回的数据  [0]=竞价,[1]=pv,[2]=uv,[3]=click,[4]=userClick,[5]=landing,[6]=userLanding,[7]=花费,[8]=成本
			int[] camphour = countDAO.getCampHourCount(Long.parseLong(campid), ymd, hour);
			if(camphour != null) {
				//1、写入广告主小时数据
				int[] v = tableMap.get("parh").get(partnerid);
				if(v == null) {
					v = new int[camphour.length];
					tableMap.get("parh").put(partnerid, v);
				}
				add(camphour, v);
				//2、写入推广组小时数据
				v = tableMap.get("campgrph").get(groupid);
				if(v == null) {
					v = new int[camphour.length];
					tableMap.get("campgrph").put(groupid, v);
				}
				add(camphour, v);
				
				//3、记录成活动小时数据
				tableMap.get("camph").put(partnerid + "_" + groupid + "_" + campid + "_" + camptype, camphour);
			}
			
			//拿到 广告主id_推广组id_活动id，对应的所有的创意id
			Set<Long> creativeids = e.getValue();
			//循环创意id
			for(Long creativeid : creativeids) {
				//根据创意id，推广组id，创意尺寸id [0]为创意组id，[1]为创意尺寸id，这个数据是添加或更新广告活动的时候写到redis中的
				long[] grpsizeid = campaignDAO.getGroupidSizeIdByCreativeId(creativeid);
				//如果没有这样的数据
				if(grpsizeid == null) {
					log.error("根据创意ID[" + creativeid + "]查素材组和尺寸ID没找到");
					String key = partnerid + "_" + groupid + "_" + campid + "_" + creativeid;
					//如果返回的grpsizeid中没有数据，将广告主id，推广组id，活动id，创意id，拼装成一个key，放的本地缓存中，经过3天后的时间，从redis中清除
					nodataids.putIfAbsent(key, systemTime.getTime());
					continue;
				}
				//------------三、获取活动、创意每天的数据 [0]=竞价,[1]=pv,[2]=uv,[3]=click,[4]=userClick,[5]=landing,[6]=userLanding,[7]=花费,[8]=成本
				int[] creday = countDAO.getCampCreativeDayCount(Long.parseLong(campid), creativeid, ymd);
				String key = partnerid + "_" + groupid + "_" + campid + "_" + creativeid;
				if(creday != null) {
					//如果有数据先将本地集合nodataids中的， partnerid + "_" + groupid + "_" + campid + "_" + creativeid，数据删掉
					nodataids.remove(key);
					
					//1、记录成广告主，创意每天的数据
					String k = partnerid + "_" + creativeid;
					int[] v = tableMap.get("par_cred").get(k);
					if(v == null) {
						v = new int[creday.length];
						tableMap.get("par_cred").put(k, v);
					}
					add(creday, v);
					
					//2、记录推广组，创意每天的数据
					k = groupid + "_" + creativeid;
					v = tableMap.get("campgrp_cred").get(k);
					if(v == null) {
						v = new int[creday.length];
						tableMap.get("campgrp_cred").put(k, v);
					}
					add(creday, v);
					
					//3、记录成活动，创意每天的数据
					k = campid + "_" + creativeid;
					v = tableMap.get("camp_cred").get(k);
					if(v == null) {
						v = new int[creday.length];
						tableMap.get("camp_cred").put(k, v);
					}
					add(creday, v);
					
					//4、记录成广告主，创意组每天的数据，grpsizeid[0]，创意对应的创意组数据
					k = partnerid + "_" + grpsizeid[0];
					v = tableMap.get("par_cregrpd").get(k);
					if(v == null) {
						v = new int[creday.length];
						tableMap.get("par_cregrpd").put(k, v);
					}
					add(creday, v);
					
					//5、记录成推广组，创意组每天的数据
					k = groupid + "_" + grpsizeid[0];
					v = tableMap.get("campgrp_cregrpd").get(k);
					if(v == null) {
						v = new int[creday.length];
						tableMap.get("campgrp_cregrpd").put(k, v);
					}
					add(creday, v);
					
					//6、记录成活动，创意组每天的数据
					k = campid + "_" + grpsizeid[0];
					v = tableMap.get("camp_cre_grpd").get(k);
					if(v == null) {
						v = new int[creday.length];
						tableMap.get("camp_cre_grpd").put(k, v);
					}
					add(creday, v);
					
					//7、记录成广告主，创意尺寸每天的数据
					k = partnerid + "_" + grpsizeid[1];
					v = tableMap.get("par_sized").get(k);
					if(v == null) {
						v = new int[creday.length];
						tableMap.get("par_sized").put(k, v);
					}
					add(creday, v);
					
					//8、记录成推广组，创意尺寸每天的数据
					k = groupid + "_" + grpsizeid[1];
					v = tableMap.get("campgrp_sized").get(k);
					if(v == null) {
						v = new int[creday.length];
						tableMap.get("campgrp_sized").put(k, v);
					}
					add(creday, v);
					
					//9、记录成活动，创意尺寸每天的数据
					k = campid + "_" + grpsizeid[1];
					v = tableMap.get("camp_sized").get(k);
					if(v == null) {
						v = new int[creday.length];
						tableMap.get("camp_sized").put(k, v);
					}
					add(creday, v);
				} else {
					nodataids.putIfAbsent(key, systemTime.getTime());
				}
			}
		}

		updateData(ymd, hour);
	}

	/**
	 * 将准备好的数据写入到数据库中
	 * 
	 * @param ymd
	 * @param hour
	 */
	private void updateData(int ymd, int hour) {
		//获取数据库链接
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			//设置数据库事务 非自动提交
			conn.setAutoCommit(false);

			Set<String> insertKeys = new TreeSet<String>();
			//循环库表定义的集合，取出表定义对象，一个一个的处理
			for(TableDesc desc : tableDesc) {
				Map<String, int[]> data = tableMap.get(desc.tableName);
				//如果没有数据，则不入库
				if(data.size() == 0) continue;
				//更新的Statement
				PreparedStatement updatePst = null;
				//插入的Statement
				PreparedStatement insertPst = null;
				//取出data中的所有数据一条一条的处理
				for(Iterator<Entry<String, int[]>> it = data.entrySet().iterator(); it.hasNext();) {
					Entry<String, int[]> e = it.next();
					//拿到key，一般是 类似 广告主id_创意id，这样的数据格式，这个数据是在 collect 方法中设置的；
					//key,也可能通过  _  拆分后，只有一条数据
					String key = e.getKey();
					//拆分key
					String[] filedvs = key.split("_");
					//获取所有的
					int[] value = e.getValue();
					//取出花费，除1000
					value[7] /= 1000;
					//取出成本，除以1000
					value[8] /= 1000;
					//生成记录集key，判断是否为小时统计，如果是小时统计,则：
					//recordKey=ymd_hour_表名_在collect方法中写入的key，例如：活动id
					//如果是每天的统计则：
					//recordKey=ymd_表名_在collect方法中写入的key，例如：活动id
					String recordKey = (desc.isHour ? ymd + "_" + hour : ymd) + "_" + desc.tableName + key;
					
					//如果在 existRecordSet 中有key，则做更新操作，否则做插入操作
					if (existRecord(recordKey)) {
						//如果更新的Statement，没有被构建，则重新构建一个Statement
						if(updatePst == null) updatePst = buildUpdatePst(conn, desc);
						int i = 1;
						//填充 "？" 对应的值，填充的就是 set bid=?,pv=?,uv=?,click=?,uclick=?,arrpv=?,arruv=?,expend=?,cost=? where 这里的 "?" 对应的值
						for (int v : value) {
							updatePst.setInt(i++, v);
						}
						//设置每个表特殊字段，对应的数据类型，例如这里设置的是，广告主id对应的数据类型，或是创意id对应的数据类型。
						for (int j = 0; j < desc.filedTypes.length; j++) {
							if (desc.filedTypes[j] == Long.class) {
								updatePst.setLong(i++, Long.parseLong(filedvs[j]));
							} else if (desc.filedTypes[j] == Integer.class) {
								updatePst.setInt(i++, Integer.parseInt(filedvs[j]));
							}
						}
						//填充统计日期对应的值
						updatePst.setInt(i++, ymd);
						//如果是小时统计，填充小时字段，对应的值
						if (desc.isHour) {
							updatePst.setInt(i++, hour);
						}
						//将拼装好的，Statement对象，添加到批量执行中
						updatePst.addBatch();
					} else {
						//构建执行插入语句的Statement对象
						if(insertPst == null) insertPst = buildInsertPst(conn, desc);
						int i = 1;
						//设置表中几个特殊的字段数据，例如：广告主id，活动id等，就是把 “？”替换成值
						for (int j = 0; j < desc.filedTypes.length; j++) {
							if (desc.filedTypes[j] == Long.class) {
								insertPst.setLong(i++, Long.parseLong(filedvs[j]));
							} else if (desc.filedTypes[j] == Integer.class) {
								insertPst.setInt(i++, Integer.parseInt(filedvs[j]));
							}
						}
						//用统计的值，替换 "?"
						for (int v : value) {
							insertPst.setInt(i++, v);
						}
						//设置日期值
						insertPst.setInt(i++, ymd);
						//如果是小时统计
						if (desc.isHour) {
							//设置小时字段字段数据
							insertPst.setInt(i++, hour);
							//写入，yyyyMMddhh,数据，如果hour小于10，则补0
							insertPst.setInt(i++, Integer.parseInt(ymd + (hour < 10 ? "0" + hour : String.valueOf(hour))));
						}
						//将插入的Statement对象添加的批量执行对象中
						insertPst.addBatch();
						//recordKey本身的意义，就是能够确认统计库中某个表的某条数据。
						insertKeys.add(recordKey);
					}
				}
				//批量执行所有的更新语句
				if(updatePst != null) updatePst.executeBatch();
				//批量执行所有的插入语句
				if(insertPst != null) insertPst.executeBatch();
			}
			//提交到数据库中
			conn.commit();
			addRecords(insertKeys);

			// 已入库的清空；
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
	 * existRecordSet中存储的是类似，ymd_hour_表名_活动id  ->   小时统计，或是  ymd_表名_广告主id_活动id ->   每天的统计
	 * existRecord方法是查看 key 是否在 existRecordSet 中。如果在则做更新操作，否则做插入操作
	 * @param key
	 * @return
	 * @return boolean
	 */
	private boolean existRecord(String key) {
		return existRecordSet.contains(key);
	}
	/**
	 * 向 existRecordSet 结合中添加 key
	 * existRecordSet中存储的是类似，ymd_hour_表名_活动id  ->   小时统计，或是  ymd_表名_广告主id_活动id ->   每天的统计
	 * @param key
	 * @return void
	 */
	private void addRecord(String key) {
		existRecordSet.add(key);
	}
	private void addRecords(Set<String> keys) {
		for (String key : keys) {
			addRecord(key);
		}
	}
	/**
	 * 将dst中的数据取出来，和src相应下标的数据相加，并设置会dst数组相应下标对应的值
	 * @param src				从redis中最新取出来的数据，[0]=竞价,[1]=pv,[2]=uv,[3]=click,[4]=userClick,[5]=landing,[6]=userLanding,[7]=花费,[8]=成本
	 * @param dst				在本地 tableMap 缓存中的数据，合适与src一样						
	 * @return void
	 */
	private void add(int[] src, int[] dst) {
		for(int i = 0 ; i < src.length; i++) {
			dst[i] += src[i];
		}
	}
	
	/**
	 * 创建用于执行插入sql语句的Statement对象
	 * @param conn							数据库链接对象
	 * @param desc							数据库表描述
	 * @return
	 * @throws Exception
	 * @return PreparedStatement
	 */
	private PreparedStatement buildInsertPst(Connection conn, TableDesc desc) throws Exception {
		//获取表名
		String tablename = desc.tableName;
		//拼装sql语句
		StringBuilder sb = new StringBuilder("insert into ").append(tablename).append(" values(");
		//从TableDesc描述对象中，拿到表的特殊字段，例如：广告主id，活动id等等。
		//判断是否小时统计，如果是小时统计，则字段个数再加12个，如果非小时统计，则字段个数再加10个。
		int filedNum = desc.filedNames.length + (desc.isHour ? 12 : 10);
		//继续拼装sql语句
		for(int i = 0; i < filedNum; i++) {
			sb.append("?,");
		}
		//删除最后一个 ","
		sb.deleteCharAt(sb.length() - 1);
		sb.append(")");
		//返回Statement对象
		PreparedStatement pst = conn.prepareStatement(sb.toString());
		return pst;
	}
	/**
	 * 创建用于执行更新sql语句的Statument对象
	 * @param conn							数据库链接对象			
	 * @param desc							数据库表描述
	 * @return
	 * @throws Exception
	 * @return PreparedStatement
	 */
	private PreparedStatement buildUpdatePst(Connection conn, TableDesc desc) throws Exception {
		//获取表名
		String tablename = desc.tableName;
		//拼装sql语句
		StringBuilder sb = new StringBuilder("update ").append(tablename).append(" set bid=?,pv=?,uv=?,click=?,uclick=?,arrpv=?,arruv=?,expend=?,cost=? where ");
		String[] fileds = desc.filedNames;
		//拼装条件，这里是字段名
		for(String filed : fileds) {
			sb.append(filed).append("=? and ");
		}
		//统计时间
		sb.append("date=?");
		//如果是小时统计，则添加，hour 条件
		if(desc.isHour) {
			sb.append(" and hour=?");
		}
		//返回Statement对象
		PreparedStatement pst = conn.prepareStatement(sb.toString());
		return pst;
	}

	private static class TableDesc {
		//表名称
		private String tableName;
		//表字段名称数组
		private String[] filedNames;
		//表字段类型，数组
		private Class<?>[] filedTypes;
		//是否为小时数据
		private boolean isHour;

		private TableDesc(String tableName, String[] filedNames, Class<?>[] filedTypes, boolean isHour) {
			this.tableName = tableName;
			this.filedNames = filedNames;
			this.filedTypes = filedTypes;
			this.isHour = isHour;
		}
	}
}
