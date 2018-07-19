package com.jtd.collector.dao;

import com.jtd.collector.util.SystemTime;
import com.jtd.collector.util.Timer;
import com.jtd.collector.util.TimerTask;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月12日
 * @项目名称 dsp-cache-collector
 * @描述 <p></p>
 */
public class CountDAOImpl extends AbstractDBSelectedShardedRedisDAO implements CountDAO {

	private static final byte[] B0 = new byte[0];

	// 用来排重，避免频繁操作sadd
	private ConcurrentHashMap<String, Object> distinctMap = new ConcurrentHashMap<String, Object>();

	private SystemTime systemTime;

	private Timer timer;

	public void init() {
		timer.timing(new TimerTask(){
			@Override
			public void run() { distinctMap.clear(); }
			@Override
			public Type type() { return Type.INTERVAL; }
			@Override
			public long delayOrIntervalMillis() { return 600000; }
			@Override
			public boolean isTriggerIndependently() { return false; }
		});
	}

	/**
	 * 在消息监听中调用该方法；消息是从engin中发送过来。<br/>
	 * 方法中操作的key：活动小时数据、活动创意每天数据、活动每天数据、推广组每天数据、活动汇总、推广组汇总<br/>
	 * 方法中操作的reids数据结构：hasp<br/>
	 * 方法说明：向操作的key对应的hash，hash中的field为b对应的value +1；增加竞价次数
	 */
	@Override
	public void increBid(long partnerid, long groupid, long campid, long creativeid, int increBy) {
		//将partnerid, groupid, campid, creativeid添加到reids，key为IDS对应的set中
		addIfAbsent(partnerid, groupid, campid, creativeid);
		//拿到当前时间的小时
		int hour = systemTime.getHour();
		//拿到当前时间的年、月、日
		int yyyyMMdd = systemTime.getYyyyMMdd();
		//获取活动的小时key
		String key = getCampHourKey(campid, yyyyMMdd, hour);
		hincreexby(key, 7200, "b", increBy, false);

		key = getCampCreativeDayKey(campid, creativeid, yyyyMMdd);
		hincreexby(key, 172800, "b", increBy, false);

		key = getCampDayKey(campid, yyyyMMdd);
		hincreexby(key, 172800, "b", increBy, false);
		
		key = getCampGrpDayKey(groupid, yyyyMMdd);
		hincreexby(key, 172800, "b", increBy, false);
		
		key = getCampTotalKey(campid);
		hincreby(key, "b", increBy);
		
		key = getCampGrpTotalKey(groupid);
		hincreby(key, "b", increBy);
	}

	/**
	 * >>>>>>>>>>>>>>>>>>>>>>>>>>>
	 * 方法中操作的key：活动小时数据、活动创意每天数据、活动每天数据、推广组每天数据、活动汇总、推广组汇总<br/>
	 * 方法中操作的reids数据结构：hasp<br/>
	 * 方法说明：向操作的key对应的hash，hash中的field为p，u对应的value +1；增加pv和独立用户数
	 */
	@Override
	public void increPv(long partnerid, long groupid, long campid, long creativeid, boolean isNew) {

		addIfAbsent(partnerid, groupid, campid, creativeid);

		int hour = systemTime.getHour();
		int yyyyMMdd = systemTime.getYyyyMMdd();

		String key = getCampHourKey(campid, yyyyMMdd, hour);
		hincreexby(key, 7200, "p", 1, false);
		if (isNew) hincreexby(key, 7200, "u", 1, false);

		key = getCampCreativeDayKey(campid, creativeid, yyyyMMdd);
		hincreexby(key, 172800, "p", 1, false);
		if (isNew) hincreexby(key, 172800, "u", 1, false);
		
		key = getCampDayKey(campid, yyyyMMdd);
		hincreexby(key, 172800, "p", 1, false);
		if (isNew) hincreexby(key, 172800, "u", 1, false);
		
		key = getCampGrpDayKey(groupid, yyyyMMdd);
		hincreexby(key, 172800, "p", 1, false);
		if (isNew) hincreexby(key, 172800, "u", 1, false);
		
		key = getCampTotalKey(campid);
		hincreby(key, "p", 1);
		if (isNew) hincreby(key, "u", 1);

		key = getCampGrpTotalKey(groupid);
		hincreby(key, "p", 1);
		if (isNew) hincreby(key, "u", 1);
	}

	/**
	 * >>>>>>>>>>>>>>>>>>>>>>>>>>>
	 * 方法中操作的key：活动小时数据、活动创意每天数据、活动每天数据、推广组每天数据、活动汇总、推广组汇总<br/>
	 * 方法中操作的reids数据结构：hasp<br/>
	 * 方法说明：向操作的key对应的hash，hash中的field为c，uc对应的value +1；增加click和独立点击用户数
	 */
	@Override
	public void increClick(long partnerid, long groupid, long campid, long creativeid, boolean isNew) {

		addIfAbsent(partnerid, groupid, campid, creativeid);

		int hour = systemTime.getHour();
		int yyyyMMdd = systemTime.getYyyyMMdd();

		String key = getCampHourKey(campid, yyyyMMdd, hour);
		hincreexby(key, 7200, "c", 1, false);
		if (isNew) hincreexby(key, 7200, "uc", 1, false);

		key = getCampCreativeDayKey(campid, creativeid, yyyyMMdd);
		hincreexby(key, 172800, "c", 1, false);
		if (isNew) hincreexby(key, 172800, "uc", 1, false);
		
		key = getCampDayKey(campid, yyyyMMdd);
		hincreexby(key, 172800, "c", 1, false);
		if (isNew) hincreexby(key, 172800, "uc", 1, false);
		
		key = getCampGrpDayKey(groupid, yyyyMMdd);
		hincreexby(key, 172800, "c", 1, false);
		if (isNew) hincreexby(key, 172800, "uc", 1, false);
		
		key = getCampTotalKey(campid);
		hincreby(key, "c", 1);
		if (isNew) hincreby(key, "uc", 1);

		key = getCampGrpTotalKey(groupid);
		hincreby(key, "c", 1);
		if (isNew) hincreby(key, "uc", 1);
	}

	/**
	 * >>>>>>>>>>>>>>>>>>>>>>>>>>>
	 * 方法中操作的key：活动小时数据、活动创意每天数据、活动每天数据、推广组每天数据、活动汇总、推广组汇总<br/>
	 * 方法中操作的reids数据结构：hasp<br/>
	 * 方法说明：向操作的key对应的hash，hash中的field为l，ul对应的value +1；增加有效到达和独立有效到达用户数
	 */
	@Override
	public void increLanding(long partnerid, long groupid, long campid, long creativeid, boolean isNew) {

		addIfAbsent(partnerid, groupid, campid, creativeid);

		int hour = systemTime.getHour();
		int yyyyMMdd = systemTime.getYyyyMMdd();

		String key = getCampHourKey(campid, yyyyMMdd, hour);
		hincreexby(key, 7200, "l", 1, false);
		if (isNew) hincreexby(key, 7200, "ul", 1, false);

		key = getCampCreativeDayKey(campid, creativeid, yyyyMMdd);
		hincreexby(key, 172800, "l", 1, false);
		if (isNew) hincreexby(key, 172800, "ul", 1, false);

		key = getCampDayKey(campid, yyyyMMdd);
		hincreexby(key, 172800, "l", 1, false);
		if (isNew) hincreexby(key, 172800, "ul", 1, false);
		
		key = getCampGrpDayKey(groupid, yyyyMMdd);
		hincreexby(key, 172800, "l", 1, false);
		if (isNew) hincreexby(key, 172800, "ul", 1, false);
		
		key = getCampTotalKey(campid);
		hincreby(key, "l", 1);
		if (isNew) hincreby(key, "ul", 1);

		key = getCampGrpTotalKey(groupid);
		hincreby(key, "l", 1);
		if (isNew) hincreby(key, "ul", 1);
	}

	/**
	 * >>>>>>>>>>>>>>>>>>>>>>>>>>>
	 * 方法中操作的key：活动小时数据、活动创意每天数据、活动每天数据、推广组每天数据、活动汇总、推广组汇总<br/>
	 * 方法中操作的reids数据结构：hasp<br/>
	 * 方法说明：向操作的key对应的hash，hash中的field为co对应的value +1；增加成本
	 */
	@Override
	public void increCost(long partnerid, long groupid, long campid, long creativeid, int cost) {

		addIfAbsent(partnerid, groupid, campid, creativeid);

		int hour = systemTime.getHour();
		int yyyyMMdd = systemTime.getYyyyMMdd();

		String key = getCampHourKey(campid, yyyyMMdd, hour);
		hincreexby(key, 7200, "co", cost, false);

		key = getCampCreativeDayKey(campid, creativeid, yyyyMMdd);
		hincreexby(key, 172800, "co", cost, false);

		key = getCampDayKey(campid, yyyyMMdd);
		hincreexby(key, 172800, "co", cost, false);

		key = getCampGrpDayKey(groupid, yyyyMMdd);
		hincreexby(key, 172800, "co", cost, false);

		key = getCampTotalKey(campid);
		hincreby(key, "co", cost);

		key = getCampGrpTotalKey(groupid);
		hincreby(key, "co", cost);
	}

	/**
	 * >>>>>>>>>>>>>>>>>>>>>>>>>>>
	 * 方法中操作的key：活动小时数据、活动创意每天数据、活动每天数据、推广组每天数据、活动汇总、推广组汇总<br/>
	 * 方法中操作的reids数据结构：hasp<br/>
	 * 方法说明：向操作的key对应的hash，hash中的field为e对应的value +1；增加用户花费。
	 */
	@Override
	public void increExpend(long partnerid, long groupid, long campid, long creativeid, int expend) {

		addIfAbsent(partnerid, groupid, campid, creativeid);

		int hour = systemTime.getHour();
		int yyyyMMdd = systemTime.getYyyyMMdd();

		String key = getCampHourKey(campid, yyyyMMdd, hour);
		hincreexby(key, 7200, "e", expend, false);

		key = getCampCreativeDayKey(campid, creativeid, yyyyMMdd);
		hincreexby(key, 172800, "e", expend, false);

		key = getCampDayKey(campid, yyyyMMdd);
		hincreexby(key, 172800, "e", expend, false);

		key = getCampGrpDayKey(groupid, yyyyMMdd);
		hincreexby(key, 172800, "e", expend, false);

		key = getCampTotalKey(campid);
		hincreby(key, "e", expend);

		key = getCampGrpTotalKey(groupid);
		hincreby(key, "e", expend);
	}

	/**
	 * 获取活动的小时数据
	 */
	@Override
	public int[] getCampHourCount(long campid, int yyyyMMdd, int hour) {
		String key = getCampHourKey(campid, yyyyMMdd, hour);
		//拿到key对应的hash中所有的field和field对应的value，并放到map中
		Map<String, String> c = hgetAll(key);
		//如果reids中没有数据，则直接返回null
		if(c == null || c.size() == 0) return null;
		//实例化返回的数组
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

	/**
	 * 获取活动创意，每天的数据
	 */
	@Override
	public int[] getCampCreativeDayCount(long campid, long creativeid, int yyyyMMdd) {
		String key = getCampCreativeDayKey(campid, creativeid, yyyyMMdd);
		Map<String, String> c = hgetAll(key);
		if(c == null || c.size() == 0) return null;
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

	/**
	 * 获取活动，每天的统计数据
	 */
	@Override
	public int[] getCampDayCount(long campid, int yyyyMMdd) {
		String key = getCampDayKey(campid, yyyyMMdd);
		Map<String, String> c = hgetAll(key);
		if(c == null || c.size() == 0) return null;
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

	/**
	 * 获取推广组每天的统计数据
	 */
	@Override
	public int[] getCampGrpDayCount(long groupid, int yyyyMMdd) {
		String key = getCampGrpDayKey(groupid, yyyyMMdd);
		Map<String, String> c = hgetAll(key);
		if(c == null || c.size() == 0) return null;
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

	/**
	 * 获取活动的汇总数据
	 */
	@Override
	public long[] getCampTotalCount(long campid) {
		String key = getCampTotalKey(campid);
		Map<String, String> c = hgetAll(key);
		if(c == null || c.size() == 0) return null;
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
	 * 获取推广组每天的数据
	 */
	@Override
	public long[] getCampGrpTotalCount(long groupid) {
		String key = getCampGrpTotalKey(groupid);
		Map<String, String> c = hgetAll(key);
		if(c == null || c.size() == 0) return null;
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
	 * <<<<<<<<<<<<<<<<<<<<<<<
	 * 计算这个小时的平均点击价格
	 */
	@Override
	public int getCampCostPerClick(long campid) {
		int hour = systemTime.getHour();
		int yyyyMMdd = systemTime.getYyyyMMdd();
		//获取活动小时数据
		String key = getCampHourKey(campid, yyyyMMdd, hour);
		//获取活动小时数据hash中所有的field对应的value
		Map<String, String> all = hgetAll(key);
		//如果没有数据，则返回整数最大值
		if (all == null) return Integer.MAX_VALUE;
		//获取点击次数，和这个小时总的点击成本
		String clk = all.get("c");
		String co = all.get("co");
		//如果这个小时的点击次数和这个小时的点击成本任何一个为null，都返回整数最大值
		if (clk == null || co == null) return Integer.MAX_VALUE;
		int click = Integer.parseInt(clk);
		int cost = Integer.parseInt(co);
		if (click == 0) return Integer.MAX_VALUE;
		//用成本 / 点击次数，精度为小数点后两位，并做四舍五入，转换成整型返回
		return new BigDecimal(cost).divide(new BigDecimal(click), 2, RoundingMode.CEILING).intValue();
	}

	/**
	 * 获取IDS对应的所有，广告主id，推广组id、活动id，创意id
	 * 返回值结构
	 * ret结构，key=partnerid_groupid_campid
	 *         value=set<Long>,set中存放的是创意id
	 */
	@Override
	public Map<String, Set<Long>> getAllIds() {
		Map<String, Set<Long>> ret = new TreeMap<String, Set<Long>>();
		Set<String> ids = smembers("IDS");
		if (ids == null) return ret;
		//把具有相同的 partnerid_groupid_campid 创意抽取出来，放到set中，在把partnerid_groupid_campid为key，存放创意id的set为value，放到ret中。
		for (String ccid : ids) {
			//ccid=partnerid_groupid_campid_creativeid
			String[] id = ccid.split("_");
			if (id.length != 4) {
				log.error("ID集合存储的ID串格式不正确: " + ccid);
				continue;
			}
			int i = ccid.lastIndexOf("_");
			//pgcid是partnerid_groupid_campid
			String pgcid = ccid.substring(0, i);
			//创意id
			long creativeid = Long.parseLong(ccid.substring(i + 1));
			Set<Long> s = ret.get(pgcid);
			if (s == null) {
				s = new TreeSet<Long>();
				ret.put(pgcid, s);
			}
			s.add(creativeid);
		}
		//ret结构，key=partnerid_groupid_campid
		//value=set<Long>,set中存放的是创意id
		return ret;
	}

	/**
	 * 删除IDS中，set集合中的id
	 */
	@Override
	public void removeId(String id) {
		srem("IDS", id);
	}

	/**
	 * 将广告主id、推广组id，活动id，创意id，添加到本地reids中<br/>
	 * 所有对竞价，pv，uv，click，userclic，landing，userlanding，花费、成本，有操作的时候，都会向IDS这个key对应的set添加，广告主id、推广组id、活动id、创意id
	 * @param campid
	 * @param creativeid
	 */
	private void addIfAbsent(long partnerid, long groupid, long campid, long creativeid) {
		String ids = partnerid + "_" + groupid + "_" + campid + "_" + creativeid;
		//向distinctMap中添加数据，如果返回值为null，说明是新添加的数据，则向redis中添加id数据
		if (distinctMap.putIfAbsent(ids, B0) == null) sadd("IDS", ids);
	}

	/**
	 * 拼Key
	 * @param campid
	 * @param yyyyMMdd
	 * @param hour
	 * @return
	 */
	/**
	 * 活动小时数据的key
	 * @param campid
	 * @param yyyyMMdd
	 * @param hour
	 * @return
	 * @return String
	 */
	private String getCampHourKey(long campid, int yyyyMMdd, int hour) {
		return "DHC_" + yyyyMMdd + "_" + hour + "_" + campid;
	}
	/**
	 * 活动创意每天数据的key
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
	 * 活动每天数据的key
	 * @param campid
	 * @param yyyyMMdd
	 * @return
	 * @return String
	 */
	private String getCampDayKey(long campid, int yyyyMMdd) {
		return "DC_" + yyyyMMdd + "_" + campid;
	}
	/**
	 * 推广组每天数据的key
	 * @param groupid
	 * @param yyyyMMdd
	 * @return
	 * @return String
	 */
	private String getCampGrpDayKey(long groupid, int yyyyMMdd) {
		return "DG_" + yyyyMMdd + "_" + groupid;
	}
	/**
	 * 活动汇总数据的key
	 * @param campid
	 * @return
	 * @return String
	 */
	private String getCampTotalKey(long campid) {
		return "TC_" + campid;
	}
	/**
	 * 推广组汇总数据的key
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
}