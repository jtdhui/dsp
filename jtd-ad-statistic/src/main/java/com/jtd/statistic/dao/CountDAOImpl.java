package com.jtd.statistic.dao;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import com.jtd.statistic.util.SystemTime;
import com.jtd.statistic.util.Timer;
import com.jtd.statistic.util.TimerTask;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月11日
 * @项目名称 dsp-counter
 * @描述 <p></p>
 */
public class CountDAOImpl extends AbstractDBSelectedShardedRedisDAO implements CountDAO {

	private static final byte[] B0 = new byte[0];
	
	private int index;

	// 用来排重，避免频繁操作sadd
	private ConcurrentHashMap<String, Object> distinctMap = new ConcurrentHashMap<String, Object>();

	private SystemTime systemTime;

	private Timer timer;

	public void init() {
		timer.timing(new TimerTask(){
			private int date = systemTime.getDate();
			@Override
			public void run() {
				//每一个小时执行一次，如果两次的小时不一样，则清除distinctMap中得数据。
				int nowd = systemTime.getDate();
				if (nowd != date) {
					distinctMap.clear();
					date = nowd;
				}
			}
			@Override
			public Type type() { return Type.INTERVAL; }
			@Override
			public long delayOrIntervalMillis() { return 3600000; }//每一个小时执行一次
			@Override
			public boolean isTriggerIndependently() { return false; }
		});
	}

	/**
	 * 增加竞价次数
	 * 向reids中写入活动小时数据、活动&&创意每天的数据、活动每天的数据、推广组每天的数据、活动汇总数据、推广组每天数据，
	 * 以上key的，曝光数。
	 * @param partnerid		广告主id
	 * @param groupid		推广组id
	 * @param campid		活动id
	 * @param creativeid	创意id
	 * @param increBy		增加的次数
	 */
	@Override
	public void increBid(long partnerid, long groupid, long campid, long creativeid, int increBy) {
		//把广告主id，推广组id，活动id，创意id，以 key=partnerid_groupid_campid_creativeid value=B0的方式，
		//存放在本地的distinctMap中
		String ids = addIfAbsent(partnerid, groupid, campid, creativeid);

		int hour = systemTime.getHour();
		int yyyyMMdd = systemTime.getYyyyMMdd();
		
		List<String[]> params = new ArrayList<String[]>(7);
		if(ids != null) params.add(new String[]{ids});
		//获取活动小时key，"DHC_" + yyyyMMdd + "_" + hour + "_" + campid
		String key = getCampHourKey(campid, yyyyMMdd, hour);
		//[0]=key="DHC_" + yyyyMMdd + "_" + hour + "_" + campid
		//[1]=b，表示redis中hash的field，就是竞价
		//[2]=incrBy,增加的次数，就是field对应的值
		//[3]=过期时间，单位秒
		params.add(new String[]{key, "b", String.valueOf(increBy), "7200"});//2小时过期
		//获取活动、创意每天key，"DCC_" + yyyyMMdd + "_" + campid + "_" + creativeid
		key = getCampCreativeDayKey(campid, creativeid, yyyyMMdd);
		params.add(new String[]{key, "b", String.valueOf(increBy), "172800"});//48小时后过期
		//获取活动每天key，"DC_" + yyyyMMdd + "_" + campid
		key = getCampDayKey(campid, yyyyMMdd);
		params.add(new String[]{key, "b", String.valueOf(increBy), "172800"});//48小时后过期
		//获取推广组每天key，"DG_" + yyyyMMdd + "_" + groupid
		key = getCampGrpDayKey(groupid, yyyyMMdd);
		params.add(new String[]{key, "b", String.valueOf(increBy), "172800"});//48小时后过期
		//获取活动汇总key，"TC_" + campid
		key = getCampTotalKey(campid);
		params.add(new String[]{key, "b", String.valueOf(increBy)});
		//获取推广组汇总key，"TG_" + groupid
		key = getCampGrpTotalKey(groupid);
		params.add(new String[]{key, "b", String.valueOf(increBy)});
		
		//将以上的竞价数据写到reids中
		plhincreby(params);
	}

	/* (non-Javadoc)
	 * @see com.doddata.net.dao.CountDAO#increPv(long, long, boolean)
	 * 向reids中写入曝光数据，这个方法是，产生曝光的时候，但活动的结算方式是cpc，这时调用这个方法，只写入曝光数和曝光独立用户数
	 * 向reids中写入活动小时数据、活动&&创意每天的数据、活动每天的数据、推广组每天的数据、活动汇总数据、推广组每天数据，
	 * 以上key的，p=曝光数，u=独立访客数
	 */
	@Override
	public void increPv(long partnerid, long groupid, long campid, long creativeid, boolean isNew) {
		//把广告主id，推广组id，活动id，创意id，以 key=partnerid_groupid_campid_creativeid value=B0的方式，
		//存放在本地的distinctMap中
		String ids = addIfAbsent(partnerid, groupid, campid, creativeid);

		int hour = systemTime.getHour();
		int yyyyMMdd = systemTime.getYyyyMMdd();

		List<String[]> params = new ArrayList<String[]>();
		if(ids != null) params.add(new String[]{ids});

		String key = getCampHourKey(campid, yyyyMMdd, hour);
		params.add(new String[]{key, "p", "1", "7200"});
		if (isNew) params.add(new String[]{key, "u", "1", "7200"});

		key = getCampCreativeDayKey(campid, creativeid, yyyyMMdd);
		params.add(new String[]{key, "p", "1", "172800"});
		if (isNew) params.add(new String[]{key, "u", "1", "172800"});
		
		key = getCampDayKey(campid, yyyyMMdd);
		params.add(new String[]{key, "p", "1", "172800"});
		if (isNew) params.add(new String[]{key, "u", "1", "172800"});
		
		key = getCampGrpDayKey(groupid, yyyyMMdd);
		params.add(new String[]{key, "p", "1", "172800"});
		if (isNew) params.add(new String[]{key, "u", "1", "172800"});
		
		key = getCampTotalKey(campid);
		params.add(new String[]{key, "p", "1"});
		if (isNew) params.add(new String[]{key, "u", "1"});

		key = getCampGrpTotalKey(groupid);
		params.add(new String[]{key, "p", "1"});
		if (isNew) params.add(new String[]{key, "u", "1"});

		plhincreby(params);
	}


	/* (non-Javadoc)
	 * @see com.doddata.net.dao.CountDAO#increCost(long, long, int)
	 * 将成本数据写到reids中
	 */
	@Override
	public void increCost(long partnerid, long groupid, long campid, long creativeid, int cost) {

		String ids = addIfAbsent(partnerid, groupid, campid, creativeid);

		int hour = systemTime.getHour();
		int yyyyMMdd = systemTime.getYyyyMMdd();

		List<String[]> params = new ArrayList<String[]>(7);
		if(ids != null) params.add(new String[]{ids});

		String key = getCampHourKey(campid, yyyyMMdd, hour);
		params.add(new String[]{key, "co", String.valueOf(cost), "7200"});

		key = getCampCreativeDayKey(campid, creativeid, yyyyMMdd);
		params.add(new String[]{key, "co", String.valueOf(cost), "172800"});

		key = getCampDayKey(campid, yyyyMMdd);
		params.add(new String[]{key, "co", String.valueOf(cost), "172800"});

		key = getCampGrpDayKey(groupid, yyyyMMdd);
		params.add(new String[]{key, "co", String.valueOf(cost), "172800"});

		key = getCampTotalKey(campid);
		params.add(new String[]{key, "co", String.valueOf(cost)});

		key = getCampGrpTotalKey(groupid);
		params.add(new String[]{key, "co", String.valueOf(cost)});
		
		plhincreby(params);
	}

	/* (non-Javadoc)
	 * @see com.doddata.net.dao.CountDAO#increExpend(long, long, int)
	 * 将用户花费写到redis中
	 */
	@Override
	public void increExpend(long partnerid, long groupid, long campid, long creativeid, int expend) {

		String ids = addIfAbsent(partnerid, groupid, campid, creativeid);

		int hour = systemTime.getHour();
		int yyyyMMdd = systemTime.getYyyyMMdd();

		List<String[]> params = new ArrayList<String[]>(7);
		if(ids != null) params.add(new String[]{ids});

		String key = getCampHourKey(campid, yyyyMMdd, hour);
		params.add(new String[]{key, "e", String.valueOf(expend), "7200"});

		key = getCampCreativeDayKey(campid, creativeid, yyyyMMdd);
		params.add(new String[]{key, "e", String.valueOf(expend), "172800"});

		key = getCampDayKey(campid, yyyyMMdd);
		params.add(new String[]{key, "e", String.valueOf(expend), "172800"});

		key = getCampGrpDayKey(groupid, yyyyMMdd);
		params.add(new String[]{key, "e", String.valueOf(expend), "172800"});

		key = getCampTotalKey(campid);
		params.add(new String[]{key, "e", String.valueOf(expend)});

		key = getCampGrpTotalKey(groupid);
		params.add(new String[]{key, "e", String.valueOf(expend)});
		
		plhincreby(params);
	}

	
	/**
	 * 当产生pv请求，并且活动的结算方式是cpm，调用这个方法向reids写入相关数据
	 * 计算方式cpm，写入活动小时数据、活动&&创意每天的数据、活动每天的数据、推广组每天的数据、活动汇总数据、推广组每天数据，
	 * 以上key的，p=pv，u=uv，co=成本费用，e=用户花费，给用户看的钱
	 * partnerid			广告主id
	 * groupid				推广组id
	 * campid				广告活动id
	 * creativeid			广告创意id
	 * isNew				是否为新增的用户(是否是新增的cookieid)
	 * cost					成本（成交价格）
	 * expend				用户的花费
	 */
	@Override
	public void increPvCostExpend(long partnerid, long groupid, long campid, long creativeid, boolean isNew, int cost, int expend) {
		String ids = addIfAbsent(partnerid, groupid, campid, creativeid);

		int hour = systemTime.getHour();
		int yyyyMMdd = systemTime.getYyyyMMdd();

		List<String[]> params = new ArrayList<String[]>();
		if(ids != null) params.add(new String[]{ids});
		
		// 添加活动的小时数据，key=DHC_yyyyMMdd_小时_活动id
		//					value=hash,field=p,value=pv数（这个小时，这个活动的pv数）
		//							   field=u,value=独立用户数(这个小时，这个活动的独立用户数)
		//							   field=b,value=竞价次数
		String key = getCampHourKey(campid, yyyyMMdd, hour);
		params.add(new String[]{key, "p", "1", "7200"});
		if (isNew) params.add(new String[]{key, "u", "1", "7200"});
		
		// 添加活动、创意的天数据数据，key=DCC_yyyyMMdd_活动id_创意id
		//						   value=hash,field=p,value=pv数（每天，这个活动，这个创意的pv数）
	    //							          field=u,value=独立用户数(每天，这个活动，这个创意的独立用户数)
		//		   							  field=b,value=竞价次数
		key = getCampCreativeDayKey(campid, creativeid, yyyyMMdd);
		params.add(new String[]{key, "p", "1", "172800"});
		if (isNew) params.add(new String[]{key, "u", "1", "172800"});
		
		// 添加活动、 每天 数据数据， key=DC_yyyyMMdd_campid
		//						   value=hash,field=p,value=pv数（每天，这个活动的pv数）
		//							          field=u,value=独立用户数(每天，这个活动的独立用户数)
		//		   							  field=b,value=竞价次数
		key = getCampDayKey(campid, yyyyMMdd);
		params.add(new String[]{key, "p", "1", "172800"});
		if (isNew) params.add(new String[]{key, "u", "1", "172800"});
		
		//DG_yyyyMMdd_groupid,推广组每天的数据，结构和活动一样，有p表示pv，u表示访问独立用户，b表示竞价次数
		key = getCampGrpDayKey(groupid, yyyyMMdd);
		params.add(new String[]{key, "p", "1", "172800"});
		if (isNew) params.add(new String[]{key, "u", "1", "172800"});

		// 活动的汇总数据， key=TC_campid
		//			      value=hash,field=p,value=pv数
		//							 field=u,value=独立用户数
		//		   				     field=b,value=竞价次数
		key = getCampTotalKey(campid);
		params.add(new String[]{key, "p", "1"});
		if (isNew) params.add(new String[]{key, "u", "1"});

		key = getCampGrpTotalKey(groupid);
		params.add(new String[]{key, "p", "1"});
		if (isNew) params.add(new String[]{key, "u", "1"});
		
		// cost
		key = getCampHourKey(campid, yyyyMMdd, hour);
		params.add(new String[]{key, "co", String.valueOf(cost), "7200"});

		key = getCampCreativeDayKey(campid, creativeid, yyyyMMdd);
		params.add(new String[]{key, "co", String.valueOf(cost), "172800"});

		key = getCampDayKey(campid, yyyyMMdd);
		params.add(new String[]{key, "co", String.valueOf(cost), "172800"});

		key = getCampGrpDayKey(groupid, yyyyMMdd);
		params.add(new String[]{key, "co", String.valueOf(cost), "172800"});

		key = getCampTotalKey(campid);
		params.add(new String[]{key, "co", String.valueOf(cost)});

		key = getCampGrpTotalKey(groupid);
		params.add(new String[]{key, "co", String.valueOf(cost)});

		// expend
		key = getCampHourKey(campid, yyyyMMdd, hour);
		params.add(new String[]{key, "e", String.valueOf(expend), "7200"});

		key = getCampCreativeDayKey(campid, creativeid, yyyyMMdd);
		params.add(new String[]{key, "e", String.valueOf(expend), "172800"});

		key = getCampDayKey(campid, yyyyMMdd);
		params.add(new String[]{key, "e", String.valueOf(expend), "172800"});

		key = getCampGrpDayKey(groupid, yyyyMMdd);
		params.add(new String[]{key, "e", String.valueOf(expend), "172800"});

		key = getCampTotalKey(campid);
		params.add(new String[]{key, "e", String.valueOf(expend)});

		key = getCampGrpTotalKey(groupid);
		params.add(new String[]{key, "e", String.valueOf(expend)});
		
		plhincreby(params);
	}

	/**
	 * 当产生vp请求，并且活动的结算方式是非cpm，调用这个方法向reids写入相关数据
	 * 非cpm结算方式，写入活动小时数据、活动&&创意每天的数据、活动每天的数据、推广组每天的数据、活动汇总数据、推广组每天数据，
	 * 以上key的，p=pv，u=uv，co（成本）
	 */
	public void increPvCost(long partnerid, long groupid, long campid, long creativeid, boolean isNew, int cost) {
		String ids = addIfAbsent(partnerid, groupid, campid, creativeid);

		int hour = systemTime.getHour();
		int yyyyMMdd = systemTime.getYyyyMMdd();

		List<String[]> params = new ArrayList<String[]>();
		if(ids != null) params.add(new String[]{ids});
		
		// pv
		String key = getCampHourKey(campid, yyyyMMdd, hour);
		params.add(new String[]{key, "p", "1", "7200"});
		if (isNew) params.add(new String[]{key, "u", "1", "7200"});

		key = getCampCreativeDayKey(campid, creativeid, yyyyMMdd);
		params.add(new String[]{key, "p", "1", "172800"});
		if (isNew) params.add(new String[]{key, "u", "1", "172800"});
		
		key = getCampDayKey(campid, yyyyMMdd);
		params.add(new String[]{key, "p", "1", "172800"});
		if (isNew) params.add(new String[]{key, "u", "1", "172800"});
		
		key = getCampGrpDayKey(groupid, yyyyMMdd);
		params.add(new String[]{key, "p", "1", "172800"});
		if (isNew) params.add(new String[]{key, "u", "1", "172800"});
		
		key = getCampTotalKey(campid);
		params.add(new String[]{key, "p", "1"});
		if (isNew) params.add(new String[]{key, "u", "1"});

		key = getCampGrpTotalKey(groupid);
		params.add(new String[]{key, "p", "1"});
		if (isNew) params.add(new String[]{key, "u", "1"});
		
		// cost
		key = getCampHourKey(campid, yyyyMMdd, hour);
		params.add(new String[]{key, "co", String.valueOf(cost), "7200"});

		key = getCampCreativeDayKey(campid, creativeid, yyyyMMdd);
		params.add(new String[]{key, "co", String.valueOf(cost), "172800"});

		key = getCampDayKey(campid, yyyyMMdd);
		params.add(new String[]{key, "co", String.valueOf(cost), "172800"});

		key = getCampGrpDayKey(groupid, yyyyMMdd);
		params.add(new String[]{key, "co", String.valueOf(cost), "172800"});

		key = getCampTotalKey(campid);
		params.add(new String[]{key, "co", String.valueOf(cost)});

		key = getCampGrpTotalKey(groupid);
		params.add(new String[]{key, "co", String.valueOf(cost)});
		
		plhincreby(params);
	}

	
	/**
	 * 当产生点击请求，并且活动的计算类型为cpc，调用这个方法向reids写入相关数据
	 * 向reids中写入活动小时数据、活动&&创意每天的数据、活动每天的数据、推广组每天的数据、活动汇总数据、推广组每天数据，
	 * 以上key的，c=点击数，uc=点击独立用户数，e=用户花费（如果是cpm结算，累加的是单次曝光的价格；如果是cpc结算，累加的是单次单次点击的价格）
	 */
	@Override
	public void increClickExpend(long partnerid, long groupid, long campid, long creativeid, boolean isNew, int expend) {
		String ids = addIfAbsent(partnerid, groupid, campid, creativeid);

		int hour = systemTime.getHour();
		int yyyyMMdd = systemTime.getYyyyMMdd();

		List<String[]> params = new ArrayList<String[]>();
		
		if(ids != null) params.add(new String[]{ids});

		// click
		//DHC_yyyyMMdd_hour_campid
		String key = getCampHourKey(campid, yyyyMMdd, hour);
		params.add(new String[]{key, "c", "1", "7200"});
		if (isNew) params.add(new String[]{key, "uc", "1", "7200"});

		key = getCampCreativeDayKey(campid, creativeid, yyyyMMdd);
		params.add(new String[]{key, "c", "1", "172800"});
		if (isNew) params.add(new String[]{key, "uc", "1", "172800"});
		
		key = getCampDayKey(campid, yyyyMMdd);
		params.add(new String[]{key, "c", "1", "172800"});
		if (isNew) params.add(new String[]{key, "uc", "1", "172800"});
		
		key = getCampGrpDayKey(groupid, yyyyMMdd);
		params.add(new String[]{key, "c", "1", "172800"});
		if (isNew) params.add(new String[]{key, "uc", "1", "172800"});
		
		key = getCampTotalKey(campid);
		params.add(new String[]{key, "c", "1"});
		if (isNew) params.add(new String[]{key, "uc", "1"});

		key = getCampGrpTotalKey(groupid);
		params.add(new String[]{key, "c", "1"});
		if (isNew) params.add(new String[]{key, "uc", "1"});

		// expend
		key = getCampHourKey(campid, yyyyMMdd, hour);
		params.add(new String[]{key, "e", String.valueOf(expend), "7200"});

		key = getCampCreativeDayKey(campid, creativeid, yyyyMMdd);
		params.add(new String[]{key, "e", String.valueOf(expend), "172800"});

		key = getCampDayKey(campid, yyyyMMdd);
		params.add(new String[]{key, "e", String.valueOf(expend), "172800"});

		key = getCampGrpDayKey(groupid, yyyyMMdd);
		params.add(new String[]{key, "e", String.valueOf(expend), "172800"});

		key = getCampTotalKey(campid);
		params.add(new String[]{key, "e", String.valueOf(expend)});

		key = getCampGrpTotalKey(groupid);
		params.add(new String[]{key, "e", String.valueOf(expend)});
		
		plhincreby(params);
	}

	/**
	 * 当产生点击请求，并且活动的结算方式是cpm的时候，调用这个方法将数据写到reids中
	 * 将点击数据写到reids中，这个方法是，产生点击的时候，但活动的结算方式是cpm，这时只调用这个方法，只写入点击数和点击独立用户数
	 * 向reids中写入活动小时数据、活动&&创意每天的数据、活动每天的数据、推广组每天的数据、活动汇总数据、推广组每天数据，
	 * 以上key的，c=点击数，uc=点击独立用户数
	 */
	@Override
	public void increClick(long partnerid, long groupid, long campid, long creativeid, boolean isNew) {

		String ids = addIfAbsent(partnerid, groupid, campid, creativeid);

		int hour = systemTime.getHour();
		int yyyyMMdd = systemTime.getYyyyMMdd();

		List<String[]> params = new ArrayList<String[]>();
		if(ids != null) params.add(new String[]{ids});

		String key = getCampHourKey(campid, yyyyMMdd, hour);
		params.add(new String[]{key, "c", "1", "7200"});
		if (isNew) params.add(new String[]{key, "uc", "1", "7200"});

		key = getCampCreativeDayKey(campid, creativeid, yyyyMMdd);
		params.add(new String[]{key, "c", "1", "172800"});
		if (isNew) params.add(new String[]{key, "uc", "1", "172800"});
		
		key = getCampDayKey(campid, yyyyMMdd);
		params.add(new String[]{key, "c", "1", "172800"});
		if (isNew) params.add(new String[]{key, "uc", "1", "172800"});
		
		key = getCampGrpDayKey(groupid, yyyyMMdd);
		params.add(new String[]{key, "c", "1", "172800"});
		if (isNew) params.add(new String[]{key, "uc", "1", "172800"});
		
		key = getCampTotalKey(campid);
		params.add(new String[]{key, "c", "1"});
		if (isNew) params.add(new String[]{key, "uc", "1"});

		key = getCampGrpTotalKey(groupid);
		params.add(new String[]{key, "c", "1"});
		if (isNew) params.add(new String[]{key, "uc", "1"});
		
		plhincreby(params);
	}
	
	/* *
	 * 当产生landingPage请求时调用
	 * (non-Javadoc)
	 * @see com.doddata.net.dao.CountDAO#increLanding(long, long, boolean)
	 * 向redis中写入，Landing page 数据
	 * 向reids中写入活动小时数据、活动&&创意每天的数据、活动每天的数据、推广组每天的数据、活动汇总数据、推广组每天数据，
	 * 以上key的，l=落地页打开次数，ul=落地页打开独立用户数
	 */
	@Override
	public void increLanding(long partnerid, long groupid, long campid, long creativeid, boolean isNew) {

		String ids = addIfAbsent(partnerid, groupid, campid, creativeid);

		int hour = systemTime.getHour();
		int yyyyMMdd = systemTime.getYyyyMMdd();

		List<String[]> params = new ArrayList<String[]>();
		if(ids != null) params.add(new String[]{ids});

		String key = getCampHourKey(campid, yyyyMMdd, hour);
		params.add(new String[]{key, "l", "1", "7200"});
		if (isNew) params.add(new String[]{key, "ul", "1", "7200"});

		key = getCampCreativeDayKey(campid, creativeid, yyyyMMdd);
		params.add(new String[]{key, "l", "1", "172800"});
		if (isNew) params.add(new String[]{key, "ul", "1", "172800"});

		key = getCampDayKey(campid, yyyyMMdd);
		params.add(new String[]{key, "l", "1", "172800"});
		if (isNew) params.add(new String[]{key, "ul", "1", "172800"});
		
		key = getCampGrpDayKey(groupid, yyyyMMdd);
		params.add(new String[]{key, "l", "1", "172800"});
		if (isNew) params.add(new String[]{key, "ul", "1", "172800"});
		
		key = getCampTotalKey(campid);
		params.add(new String[]{key, "l", "1"});
		if (isNew) params.add(new String[]{key, "ul", "1"});

		key = getCampGrpTotalKey(groupid);
		params.add(new String[]{key, "l", "1"});
		if (isNew) params.add(new String[]{key, "ul", "1"});
		
		plhincreby(params);
	}
	
	
	/* (non-Javadoc)
	 * @see net.doddata.collector.dao.CountDAO#getHourCount(long, int)
	 * 获取活动小时数据
	 */
	@Override
	public int[] getCampHourCount(long campid, int yyyyMMdd, int hour) {
		String key = getCampHourKey(campid, yyyyMMdd, hour);
		Map<String, String> c = hgetAll(key);
		if(c == null) return null;
		int[] ret = new int[9];

		// 竞价次数
		String v = c.get("b");
		if(v != null) ret[0] = Integer.parseInt(v);

		// PV UV
		v = c.get("p");
		if(v != null) ret[1] = Integer.parseInt(v);
		v = c.get("u");
		if(v != null) ret[2] = Integer.parseInt(v);

		// Click UserClick
		v = c.get("c");
		if(v != null) ret[3] = Integer.parseInt(v);
		v = c.get("uc");
		if(v != null) ret[4] = Integer.parseInt(v);

		// Landing UserLanding
		v = c.get("l");
		if(v != null) ret[5] = Integer.parseInt(v);
		v = c.get("ul");
		if(v != null) ret[6] = Integer.parseInt(v);

		// 消耗和媒体成本
		v = c.get("e");
		if(v != null) ret[7] = Integer.parseInt(v);
		v = c.get("co");
		if(v != null) ret[8] = Integer.parseInt(v);

		return ret;
	}

	/* (non-Javadoc)
	 * @see net.doddata.collector.dao.CountDAO#getDayCount(long, long, int)
	 * 获取活动创意，每天的数据
	 */
	@Override
	public int[] getCampCreativeDayCount(long campid, long creativeid, int yyyyMMdd) {
		String key = getCampCreativeDayKey(campid, creativeid, yyyyMMdd);
		Map<String, String> c = hgetAll(key);
		if(c == null) return null;
		int[] ret = new int[9];

		// 竞价次数
		String v = c.get("b");
		if(v != null) ret[0] = Integer.parseInt(v);

		// PV UV
		v = c.get("p");
		if(v != null) ret[1] = Integer.parseInt(v);
		v = c.get("u");
		if(v != null) ret[2] = Integer.parseInt(v);

		// Click UserClick
		v = c.get("c");
		if(v != null) ret[3] = Integer.parseInt(v);
		v = c.get("uc");
		if(v != null) ret[4] = Integer.parseInt(v);

		// Landing UserLanding
		v = c.get("l");
		if(v != null) ret[5] = Integer.parseInt(v);
		v = c.get("ul");
		if(v != null) ret[6] = Integer.parseInt(v);

		// 消耗和媒体成本
		v = c.get("e");
		if(v != null) ret[7] = Integer.parseInt(v);
		v = c.get("co");
		if(v != null) ret[8] = Integer.parseInt(v);

		return ret;
	}

	/* (non-Javadoc)
	 * @see net.doddata.counter.dao.CountDAO#getCampDayCount(long, int)
	 * 获取活动每天的数据数
	 */
	@Override
	public int[] getCampDayCount(long campid, int yyyyMMdd) {
		String key = getCampDayKey(campid, yyyyMMdd);
		Map<String, String> c = hgetAll(key);
		if(c == null) return null;
		int[] ret = new int[9];

		// 竞价次数
		String v = c.get("b");
		if(v != null) ret[0] = Integer.parseInt(v);

		// PV UV
		v = c.get("p");
		if(v != null) ret[1] = Integer.parseInt(v);
		v = c.get("u");
		if(v != null) ret[2] = Integer.parseInt(v);

		// Click UserClick
		v = c.get("c");
		if(v != null) ret[3] = Integer.parseInt(v);
		v = c.get("uc");
		if(v != null) ret[4] = Integer.parseInt(v);

		// Landing UserLanding
		v = c.get("l");
		if(v != null) ret[5] = Integer.parseInt(v);
		v = c.get("ul");
		if(v != null) ret[6] = Integer.parseInt(v);

		// 消耗和媒体成本
		v = c.get("e");
		if(v != null) ret[7] = Integer.parseInt(v);
		v = c.get("co");
		if(v != null) ret[8] = Integer.parseInt(v);

		return ret;
	}

	/* (non-Javadoc)
	 * @see net.doddata.counter.dao.CountDAO#getCampGrpDayCount(long, int)
	 * 获取推广组每天的数据
	 */
	@Override
	public int[] getCampGrpDayCount(long groupid, int yyyyMMdd) {
		String key = getCampGrpDayKey(groupid, yyyyMMdd);
		Map<String, String> c = hgetAll(key);
		if(c == null) return null;
		int[] ret = new int[9];

		// 竞价次数
		String v = c.get("b");
		if(v != null) ret[0] = Integer.parseInt(v);

		// PV UV
		v = c.get("p");
		if(v != null) ret[1] = Integer.parseInt(v);
		v = c.get("u");
		if(v != null) ret[2] = Integer.parseInt(v);

		// Click UserClick
		v = c.get("c");
		if(v != null) ret[3] = Integer.parseInt(v);
		v = c.get("uc");
		if(v != null) ret[4] = Integer.parseInt(v);

		// Landing UserLanding
		v = c.get("l");
		if(v != null) ret[5] = Integer.parseInt(v);
		v = c.get("ul");
		if(v != null) ret[6] = Integer.parseInt(v);

		// 消耗和媒体成本
		v = c.get("e");
		if(v != null) ret[7] = Integer.parseInt(v);
		v = c.get("co");
		if(v != null) ret[8] = Integer.parseInt(v);

		return ret;
	}

	/* (non-Javadoc)
	 * @see net.doddata.counter.dao.CountDAO#getCampCount(long)
	 * 获取活动汇总数据
	 */
	@Override
	public long[] getCampTotalCount(long campid) {
		String key = getCampTotalKey(campid);
		Map<String, String> c = hgetAll(key);
		if(c == null) return null;
		long[] ret = new long[9];

		// 竞价次数
		String v = c.get("b");
		if(v != null) ret[0] = Long.parseLong(v);

		// PV UV
		v = c.get("p");
		if(v != null) ret[1] = Long.parseLong(v);
		v = c.get("u");
		if(v != null) ret[2] = Long.parseLong(v);

		// Click UserClick
		v = c.get("c");
		if(v != null) ret[3] = Long.parseLong(v);
		v = c.get("uc");
		if(v != null) ret[4] = Long.parseLong(v);

		// Landing UserLanding
		v = c.get("l");
		if(v != null) ret[5] = Long.parseLong(v);
		v = c.get("ul");
		if(v != null) ret[6] = Long.parseLong(v);

		// 消耗和媒体成本
		v = c.get("e");
		if(v != null) ret[7] = Long.parseLong(v);
		v = c.get("co");
		if(v != null) ret[8] = Long.parseLong(v);

		return ret;
	}

	/* (non-Javadoc)
	 * @see net.doddata.counter.dao.CountDAO#getCampGrpCount(long)
	 * 获取推广组汇总数据
	 */
	@Override
	public long[] getCampGrpTotalCount(long groupid) {
		String key = getCampGrpTotalKey(groupid);
		Map<String, String> c = hgetAll(key);
		if(c == null) return null;
		long[] ret = new long[9];

		// 竞价次数
		String v = c.get("b");
		if(v != null) ret[0] = Long.parseLong(v);

		// PV UV
		v = c.get("p");
		if(v != null) ret[1] = Long.parseLong(v);
		v = c.get("u");
		if(v != null) ret[2] = Long.parseLong(v);

		// Click UserClick
		v = c.get("c");
		if(v != null) ret[3] = Long.parseLong(v);
		v = c.get("uc");
		if(v != null) ret[4] = Long.parseLong(v);

		// Landing UserLanding
		v = c.get("l");
		if(v != null) ret[5] = Long.parseLong(v);
		v = c.get("ul");
		if(v != null) ret[6] = Long.parseLong(v);

		// 消耗和媒体成本
		v = c.get("e");
		if(v != null) ret[7] = Long.parseLong(v);
		v = c.get("co");
		if(v != null) ret[8] = Long.parseLong(v);

		return ret;
	}

	/**
	 * 获取每小时某个活动的点击数，和每小时某个活动的成本；通过这个这两个数，计算这个小时点击的平均价格
	 * 
	 */
	@Override
	public int getCampCostPerClick(long campid) {
		//获取系统时间，小时
		int hour = systemTime.getHour();
		//获取系统时间
		int yyyyMMdd = systemTime.getYyyyMMdd();
		//获取活动小时key=DHC_yyyyMMdd_hour_campid
		String key = getCampHourKey(campid, yyyyMMdd, hour);
		//获取所有活动小时数据
		Map<String, String> all = hgetAll(key);
		//如果没有数据，则返回 Integer.MAX_VALUE
		if (all == null) return Integer.MAX_VALUE;
		//返回点击数据
		String clk = all.get("c");
		//返回成本
		String co = all.get("co");
		//如果点击数或成本为null，返回 Integer.MAX_VALUE
		if (clk == null || co == null) return Integer.MAX_VALUE;
		//点击数转换成整型
		int click = Integer.parseInt(clk);
		//成本转换成整型
		int cost = Integer.parseInt(co);
		//如果点击数为0，返回 Integer.MAX_VALUE
		if (click == 0) return Integer.MAX_VALUE;
		//使用累加的成本，除以点击次数，保留两位小数，得到的是一次点击的平均价，RoundingMode.CEILING，如果是正数，舍弃小数，整数+1；如果是负数则舍弃小数，整数-1；
		return new BigDecimal(cost).divide(new BigDecimal(click), 2, RoundingMode.CEILING).intValue();
	}

	/* (non-Javadoc)
	 * @see net.doddata.collector.dao.CountDAO#getAllIds()
	 * 获取reids中存储的所有的 广告主id_推广组id_活动id_创意id 组成的key
	 * 返回 
	 */
	@Override
	public Map<String, Set<Long>> getAllIds() {
		Map<String, Set<Long>> ret = new TreeMap<String, Set<Long>>();
		//从reids中读取数据
		Set<String> ids = smembers("IDS");
		if (ids == null) return ret;
		for (String ccid : ids) {
			String[] id = ccid.split("_");
			if (id.length != 4) {
				log.error("ID集合存储的ID串格式不正确: " + ccid);
				continue;
			}
			//如果key最后一个 "_" 字符  广告主id_推广组id_活动id_创意id
			int i = ccid.lastIndexOf("_");
			//pgcid=广告主id_推广组id_活动id
			String pgcid = ccid.substring(0, i);
			//取出创意id
			long creativeid = Long.parseLong(ccid.substring(i + 1));
			//从ret中，取出id=广告主id_推广组id_活动id 的set
			Set<Long> s = ret.get(pgcid);
			if (s == null) {
				//如果ret中没有 set，则新创建一个set
				s = new HashSet<Long>();
				//将set put到ret中，key=广告主id_推广组id_活动id，value=装有广告创意id的set
				ret.put(pgcid, s);
			}
			//将创意id，添加的set中
			s.add(creativeid);
		}
		return ret;
	}

	/**
	 * 把活动和创意ID添加到ID集合中去
	 * @param campid
	 * @param creativeid
	 */
	private String addIfAbsent(long partnerid, long groupid, long campid, long creativeid) {
		String ids = partnerid + "_" + groupid + "_" + campid + "_" + creativeid;
		return distinctMap.putIfAbsent(ids, B0) == null ? ids : null;
	}

	/**
	 * 活动小时key
	 * 拼Key
	 * @param campid
	 * @param yyyyMMdd
	 * @param hour
	 * @return
	 */
	private String getCampHourKey(long campid, int yyyyMMdd, int hour) {
		return "DHC_" + yyyyMMdd + "_" + hour + "_" + campid;
	}
	/**
	 * 活动每天的数据
	 * @param campid
	 * @param yyyyMMdd
	 * @return
	 * @return String
	 */
	private String getCampDayKey(long campid, int yyyyMMdd) {
		return "DC_" + yyyyMMdd + "_" + campid;
	}
	/**
	 * 活动、创意每天的key
	 * @param campid
	 * @param creativeid
	 * @param yyyyMMdd
	 * @return
	 * @return String
	 */
	private String getCampCreativeDayKey(long campid, long creativeid, int yyyyMMdd) {
		return "DCC_" + yyyyMMdd + "_" + campid + "_" + creativeid;
	}
	/**
	 * 活动，推广组，每天的数据
	 * @param groupid
	 * @param yyyyMMdd
	 * @return
	 * @return String
	 */
	private String getCampGrpDayKey(long groupid, int yyyyMMdd) {
		return "DG_" + yyyyMMdd + "_" + groupid;
	}
	/**
	 * 活动每天的汇总
	 * @param campid
	 * @return
	 * @return String
	 */
	private String getCampTotalKey(long campid) {
		return "TC_" + campid;
	}
	/**
	 * 推广组每天的汇总
	 * @param groupid
	 * @return
	 * @return String
	 */
	private String getCampGrpTotalKey(long groupid) {
		return "TG_" + groupid;
	}

	/**
	 * @param systemTime the systemTime to set
	 */
	public void setSystemTime(SystemTime systemTime) {
		this.systemTime = systemTime;
	}

	/**
	 * @param timer the timer to set
	 */
	public void setTimer(Timer timer) {
		this.timer = timer;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
}