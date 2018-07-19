package com.jtd.collector.dao;

import com.alibaba.fastjson.JSON;
import com.jtd.collector.util.SystemTime;
import com.jtd.collector.util.Timer;
import com.jtd.collector.util.TimerTask;
import com.jtd.web.constants.CampaignAutoStatus;
import com.jtd.web.constants.CampaignManulStatus;
import com.jtd.web.model.Ad;
import com.jtd.web.model.Campaign;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月12日
 * @项目名称 dsp-cache-collector
 * @描述 <p>操作本地reids数据；对活动、推广组、创意数据的添加、修改、删除；对价格因子的修改、添加、查询</p>
 */
public class CampaignDAOImpl extends AbstractRedisDAO implements CampaignDAO {

	private static final Log log = LogFactory.getLog(CampaignDAOImpl.class);
	////广告活动在redis中的过期时间，时间为15分钟
	private static final long EXPIRE = 1000L * 60 * 15;
	//广告活动在reids中key的前缀
	private static final String CAMP_KEY_PREFIX = "CMP";
	//创意key，key前缀
	private static final String CREATIVE_KEY_PREFIX = "CRE";
	//推广组，key前缀
	private static final String CAMP_GRP_KEY_PREFIX = "GRP";

	// 查询缓存, 15分钟不使用的数据退出内存,这里存放的是 广告活动数据
	private ConcurrentHashMap<Long, Node> cache = new ConcurrentHashMap<Long, Node>();
	// 查询缓存，15分钟不使用的数据退出内存，这里存放的是 广告创意数据
	private ConcurrentHashMap<Long, Node> creativeCache = new ConcurrentHashMap<Long, Node>();
	// 查询缓存，15分钟不使用的数据退出内存，这里存放的是 推广组数据
	private ConcurrentHashMap<Long, Node> groupCache = new ConcurrentHashMap<Long, Node>();

	// 预算控制,key campid value 出价系统
	private ConcurrentHashMap<Long, Float> priceFactor = new ConcurrentHashMap<Long, Float>();

	// 系统时间
	private SystemTime systemTime;

	// 计时器, 定期清理内存里过期的数据
	private Timer timer;

	/**
	 * 初始化时启动定时任务
	 */
	public void init() {
		super.init();
		timer.timing(new TimerTask(){
			@Override
			public void run() {
				long now = systemTime.getTime();
				//循环内存中得创意数据，如果自后一次访问的时间+过期的时间，小于系统的当前时间，则将这个活动移除本地内存
				for(Iterator<Entry<Long, Node>> it = cache.entrySet().iterator(); it.hasNext();) {
					if(it.next().getValue().lastAccessTime + EXPIRE <= now) it.remove();
				}
				//删除内存中，过期的创意数据
				for(Iterator<Entry<Long, Node>> it = creativeCache.entrySet().iterator(); it.hasNext();) {
					if(it.next().getValue().lastAccessTime + EXPIRE <= now) it.remove();
				}
				//删除内存中得推广组数据
				for(Iterator<Entry<Long, Node>> it = groupCache.entrySet().iterator(); it.hasNext();) {
					if(it.next().getValue().lastAccessTime + EXPIRE <= now) it.remove();
				}
			}
			@Override
			public Type type() { return Type.INTERVAL; }
			@Override
			public long delayOrIntervalMillis() { return 60000; }//每间隔一分钟，执行一次清空过期数据的操作
			@Override
			public boolean isTriggerIndependently() { return false; }
		});
	}

	/**
	 * 更新广告、推广组、活动数据
	 */
	@Override
	public boolean updateCampaign(Campaign campaign) {
		//将活动中的广告取出来，使用“CRE创意id”为key，组id_尺寸id，为value，存在本地reids中
		updateAd(campaign);
		//取出活动中推广组的指标数据，放到Long数组中；以"GRP推广组id"key，推广组指标数据数组json串，为值存到本地redis数据库中。
		updateGrp(campaign);
		long id = campaign.getId();
		//将活动写入redis，如果成功，则将活动对象封装到Node对象中，并put道ConCurrentMap<Node> cacheMap中。
		if (set(CAMP_KEY_PREFIX + id, JSON.toJSONString(campaign))) {
			cache.put(id, new Node(campaign, systemTime.getTime()));
			return true;
		} else {
			log.error("更新Campaign发生错误");
			return false;
		}
	}

	/**
	 * 把AD的尺寸ID，创意组ID存下来<br/>
	 * 取出活动中的广告数据，使用“CRE创意id”为key，创意组id_尺寸id，为value，存在本地reids中
	 * @param campaign
	 */
	private void updateAd(Campaign campaign) {
		Map<String, List<Ad>> ads = campaign.getAds();
		for (List<Ad> adl : ads.values()) {
			for (Ad ad : adl) {
				long creativeid = ad.getCreativeId();
				long cregrpid = ad.getGroupId();
				long sizeid = ad.getSizeid();
				/**
				 * key=CRE+创意id
				 * value=创意组id_尺寸大小id
				 */
				set(CREATIVE_KEY_PREFIX + creativeid, cregrpid + "_" + sizeid);
			}
		}
	}
	/**
	 * 取出活动中推广组的指标数据，放到Long数组中；以"GRP推广组id"key，推广组指标数据数组json串，为值存到本地redis数据库中。
	 * @param c
	 * @return void
	 */
	private void updateGrp(Campaign c) {
		long groupid = c.getGroupId();
		Long[] value = new Long[] { 
				//推广组日预算
				c.getGroupDailyBudgetGoal(),
				//推广组总预算
				c.getGroupTotalBudgetGoal(), 
				//推广组日展现量目标
				c.getGroupDailyPvGoal(),
				//推广组总展现量目标
				c.getGroupTotalPvGoal(), 
				//推广组日点击量目标
				c.getGroupDailyClickGoal(),
				//推广组总点击量目标
				c.getGroupTotalClickGoal() };
		updateCampgrp(groupid, value);
	}
	
	/**
	 * 将推广组数据存储到reids中，并把推广组数据从groupCache本地缓存中删除，
	 * 从MQ中获取推广组各项指标数据
	 * 参数 values说明：
	 * 		[0]=推广组日预算
	 * 		[1]=推广组总预算
	 * 		[2]=日展现量目标
	 * 		[3]=总展现量目标
	 * 		[4]=日点击量目标
	 * 		[5]=总点击量目标
	 */
	public boolean updateCampgrp(long groupid, Long[] values) {
		boolean ret = set(CAMP_GRP_KEY_PREFIX + groupid, JSON.toJSONString(values));
		groupCache.remove(groupid);
		return ret;
	}

	/**
	 * 根据推广组id，获取推广组各项指标数据
	 * 返回值：
	 *  	[0]=推广组日预算
	 * 		[1]=推广组总预算
	 * 		[2]=日展现量目标
	 * 		[3]=总展现量目标
	 * 		[4]=日点击量目标
	 * 		[5]=总点击量目标
	 */
	public Long[] getCampgrp(long groupid) {
		//从本地缓存中，取出推广组的node
		Node node = groupCache.get(groupid);
		//如果不为null，则将lastAccessTime设置为当前时间
		if (node != null) {
			node.lastAccessTime = systemTime.getTime();
			//返回指标数据
			return (Long[]) node.data;
		}
		//如果本地缓存没有，则从redis中区数据
		String jsonstr = get(CAMP_GRP_KEY_PREFIX + groupid);
		//如果redis中没有这个推广组数据，则返回null
		if (jsonstr == null) {
			return null;
		}
		//将从reids中取出的推广组指标数据，转换成数组对象
		Long[] grp = JSON.parseObject(jsonstr, Long[].class);

		long now = systemTime.getTime();
		//使用node封装推广组指标数据
		node = new Node(grp, now);
		//将推广组数据，添加到ConcurrentMap中,如果groupCache中有groupid的key，则拿到这个key对应的值，并重新设置数据和最后一次访问的时间
		Node oldNode = groupCache.putIfAbsent(groupid, node);
		if(oldNode != null) {
			oldNode.data = grp;
			oldNode.lastAccessTime = now;
		}
		return grp;
	}

	/**
	 * 根据创意id，获取推广组id和创意的尺寸id
	 * 返回：[0]为创意组id，[1]为创意尺寸id
	 */
	public long[] getGroupidSizeIdByCreativeId(long creativeid) {
		//从本地缓存中取出node数据
		Node node = creativeCache.get(creativeid);
		//如果拿到数据，则更新最后一次访问时间
		if (node != null) {
			node.lastAccessTime = systemTime.getTime();
			return (long[]) node.data;
		}
		//如果本地缓存中没有，则从redis中获取数据
		String idstr = get(CREATIVE_KEY_PREFIX + creativeid);
		if (idstr == null) {
			return null;
		}
		//拆分value，拆分后 [0]为创意组id，[1]为创意尺寸id
		String[] ids = idstr.split("_");
		//把创意所属的创意组id，和创意尺寸id，放到数组中。
		long[] id = new long[] { Long.parseLong(ids[0]), Long.parseLong(ids[1]) };

		long now = systemTime.getTime();
		//使用node封装创意对应的id数组数据
		node = new Node(id, now);
		Node oldNode = creativeCache.putIfAbsent(creativeid, node);
		if(oldNode != null) {
			oldNode.data = id;
			oldNode.lastAccessTime = now;
		}
		return id;
	}

	/**
	 * 改变活动的自动状态
	 */
	@Override
	public boolean changeAutoStatus(long campid, CampaignAutoStatus status) {
		//从本地reids中获取活动数据
		String json = get(CAMP_KEY_PREFIX + campid);
		if (json == null) {
			return false;
		}
		//将拿到的json字符串转换成活动对象
		Campaign cmp = JSON.parseObject(json, Campaign.class);
		//设置活动的自动状态
		cmp.setAutoStatus(status);
		//将修改状态后活动对象，放到reids数据库中
		set(CAMP_KEY_PREFIX + campid, JSON.toJSONString(cmp));
		//将自动状态更新到本地缓存的campaign中
		Node node = cache.get(campid);
		if (node != null) {
			((Campaign) node.data).setAutoStatus(status);
		}
		return true;
	}

	/**
	 * 修改活动的手动状态，逻辑与修改自动状态类似
	 */
	@Override
	public boolean changeManulStatus(long campid, CampaignManulStatus status) {
		String json = get(CAMP_KEY_PREFIX + campid);
		if (json == null) {
			return false;
		}
		Campaign cmp = JSON.parseObject(json, Campaign.class);
		cmp.setManulStatus(status);
		set(CAMP_KEY_PREFIX + campid, JSON.toJSONString(cmp));
		Node node = cache.get(campid);
		if (node != null) {
			((Campaign) node.data).setManulStatus(status);
		}
		return true;
	}

	/**
	 * 根据活动id，获取活动对象
	 */
	@Override
	public Campaign getCampaignById(long id) {
		//先从本地拿活动数据，并更新lastAccessTime；如果没有从本地reids中拿
		Node node = cache.get(id);
		if(node != null) {
			node.lastAccessTime = systemTime.getTime();
			return (Campaign) node.data;
		}
		//从本地redis中拿活动数据
		String json = get(CAMP_KEY_PREFIX + id);
		if (json == null) {
			return null;
		}
		//将拿到的活动json串转换成活动对象，并将活动对象更新到本地换从中
		Campaign cmp = JSON.parseObject(json, Campaign.class);
		long now = systemTime.getTime();
		node = new Node(cmp, now);
		Node oldNode = cache.putIfAbsent(id, node);
		if(oldNode != null) {
			oldNode.data = cmp;
			oldNode.lastAccessTime = now;
		}
		
		return cmp;
	}

	/**
	 * 删除活动数据，删除redis、chache中的活动数据，删除价格因子数据
	 */
	@Override
	public boolean delCampaignById(long id) {
		boolean ret = del(CAMP_KEY_PREFIX + id) == 1l;
		cache.remove(id);
		priceFactor.remove(id);
		return ret;
	}

	/**
	 * 获取本地reids中所有的活动数据
	 */
	@Override
	public List<Campaign> getAllCampaigns() {
		List<Campaign> camps = new ArrayList<Campaign>();
		Set<String> keys = keys(CAMP_KEY_PREFIX + "*");
		if(keys == null) return camps;
		for (String key : keys) {
			String json = get(key);
			camps.add(JSON.parseObject(json, Campaign.class));
		}
		return camps;
	}

	/**
	 * 添加或修改价格因子数据
	 */
	@Override
	public void changeCampPriceFactor(long campid, float factor) {
		priceFactor.put(campid, factor);
	}

	/**
	 * 获取价格因子数据
	 */
	@Override
	public float getCampPriceFactor(long id) {
		Float f = priceFactor.get(id);
		return f == null ? 1.0f : f;
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

	private static class Node {
		private Object data;
		private long lastAccessTime;
		private Node(Object data, long lastAccessTime) {
			this.data = data;
			this.lastAccessTime = lastAccessTime;
		}
	}
	
	/**
	 * 首屏、非首屏；频次数据，实体类
	 * @作者 Amos Xu
	 * @版本 V1.0
	 * @配置 
	 * @创建日期 2016年11月10日
	 * @项目名称 dsp-cache-collector
	 * @描述 <p>
	 * 		{freq:[24,5,5], firsts: 0}<br/>
	 * 		firsts: 取值0，1，2，分别对应不限，首屏，非首屏
	 * 		freq: 对应频次控制，长度为3的数组，格式为[界面上的小时数（24或者48），要控制的展现次数，要控制的点击次数]
	 * 				界面上展现和点击是单选的，选择其中一个的时候，另外一个置0。
	 * 		</p>
	 */
	public static class FreqAndScreen {
		private int[] freq;
		private int firsts;

		public int[] getFreq() {
			return freq;
		}

		public void setFreq(int[] freq) {
			this.freq = freq;
		}

		public int getFirsts() {
			return firsts;
		}

		public void setFirsts(int firsts) {
			this.firsts = firsts;
		}
	}
}
