package com.jtd.engine.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.jtd.engine.utils.SystemTime;
import com.jtd.engine.utils.Timer;
import com.jtd.engine.utils.TimerTask;
import com.jtd.web.constants.CampaignAutoStatus;
import com.jtd.web.constants.CampaignManulStatus;
import com.jtd.web.constants.ExpendType;
import com.jtd.web.model.Campaign;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月8日
 * @项目名称 dsp-engine
 * @描述 <p></p>
 */
public class CampaignDAOImpl extends AbstractRedisDAO implements CampaignDAO {

	private final Logger xtarderDebugLog = LogManager.getLogger("xtarderDebugLog");
	private final Logger besDebugLog = LogManager.getLogger("besDebugLog");
	
	private static final Log log = LogFactory.getLog(CampaignDAOImpl.class);
	
	//系统毛利率
	private static final int SYS_GROSS_PROFIT = 30;
	//过期时间，单位毫秒。这里设置了15分钟
	private static final long EXPIRE = 1000L * 60 * 15;
	//本地redis中，广告活动key的前缀
	//广告活动key前缀
	private static final String CAMP_KEY_PREFIX = "CMP";
	//af值key前缀
	private static final String AF_KEY_PREFIX = "AF";
	//广告主毛利率key前缀
	private static final String PARTNER_GROSS_PROFIT_KEY_PREFIX = "PGF";
	//广告活动毛利率key前缀
	private static final String CAMP_GROSS_PROFIT_KEY_PREFIX = "CGF";
	//广告活动小时暂停数据key前缀
	private static final String CAMP_HOUR_PAUSE = "CHP";

	// 查询缓存, 15分钟不使用的数据退出内存，Node是对广告活动的封装，Node中包含，Campaign对象，
	//Node中lastAccessTime属性，是用户计算这个活动是否15分钟没有使用了，如果是则视为过期将从本地的cache中山城
	private ConcurrentHashMap<Long, Node> cache = new ConcurrentHashMap<Long, Node>();

	// 预算控制,key campid value 出价系统
	private ConcurrentHashMap<Long, Float> priceFactor = new ConcurrentHashMap<Long, Float>();

	// CTR key campid value 点击率为千分之N时, value = N
	private ConcurrentHashMap<Long, Float> ctr = new ConcurrentHashMap<Long, Float>();

	// AF key campid
	private ConcurrentHashMap<Long, Integer> af = new ConcurrentHashMap<Long, Integer>();

	// key partnerid, value grossprofit 专门用于存放每个partner的毛利率
	private ConcurrentHashMap<Long, Integer> partnerGrossProfit = new ConcurrentHashMap<Long, Integer>();

	// key campid, value grossprofit，专门用于存放每个活动对应的毛利率的
	private ConcurrentHashMap<Long, Integer> campGrossProfit = new ConcurrentHashMap<Long, Integer>();

	// key campid, value hour
	private ConcurrentHashMap<Long, Integer> hourPause = new ConcurrentHashMap<Long, Integer>();

	// 系统时间
	private SystemTime systemTime;

	// 计时器, 定期清理内存里过期的数据
	private Timer timer;

	// 系统默认的毛利率
	private int sysGrosspProfit = SYS_GROSS_PROFIT;

	/**
	 * 初始化时启动定时任务
	 */
	public void init() {
		super.init();

		// 加载全部AF
		Set<String> keys = keys(AF_KEY_PREFIX + "*");
		for(String key : keys) {
			af.put(Long.parseLong(key.substring(AF_KEY_PREFIX.length())), Integer.parseInt(get(key)));
		}
		//加载本地redis中，所有的广告主毛利率
		keys = keys(PARTNER_GROSS_PROFIT_KEY_PREFIX + "*");
		for(String key : keys) {
			partnerGrossProfit.put(Long.parseLong(key.substring(PARTNER_GROSS_PROFIT_KEY_PREFIX.length())), Integer.parseInt(get(key)));
		}
		//加载本地reids中，所有广告活动的毛利
		keys = keys(CAMP_GROSS_PROFIT_KEY_PREFIX + "*");
		for(String key : keys) {
			campGrossProfit.put(Long.parseLong(key.substring(CAMP_GROSS_PROFIT_KEY_PREFIX.length())), Integer.parseInt(get(key)));
		}
		//加载本地redis中所有，小时暂停活动数据
		keys = keys(CAMP_HOUR_PAUSE + "*");
		for(String key : keys) {
			hourPause.put(Long.parseLong(key.substring(CAMP_HOUR_PAUSE.length())), Integer.parseInt(get(key)));
		}

		timer.timing(new TimerTask(){
			@Override
			public void run() {
				long now = systemTime.getTime();
				//线程每次执行的时候，会检查cache中的广告活动是否超过15分钟没有使用，如果是则从本地缓存删除
				for(Iterator<Entry<Long, Node>> it = cache.entrySet().iterator(); it.hasNext();) {
					if(it.next().getValue().lastAccessTime + EXPIRE <= now) it.remove();
				}
				//线程每次执行的时候，会检查hourPause中活动小时是否为当前小时，如果不是则删除
				int hour = systemTime.getHour();
				for(Iterator<Entry<Long, Integer>> it = hourPause.entrySet().iterator(); it.hasNext();) {
					if (it.next().getValue() != hour) it.remove();
				}
			}
			@Override
			public Type type() { return Type.INTERVAL; }
			@Override
			public long delayOrIntervalMillis() { return 60000; }
			@Override
			public boolean isTriggerIndependently() { return false; }
		});
	}

	/* (non-Javadoc)
	 * @see com.asme.dao.CampaignDAO#updateCampaign(net.doddata.web.dsp.model.Campaign)
	 */
	/**
	 * 更新投放
	 * 如果存在则更新,不存在则添加
	 * @param pub
	 * @return
	 */
	@Override
	public boolean updateCampaign(Campaign campaign) {
		long id = campaign.getId();
		//将campaign转换中json字符串写到redis中，并判断如果写入成功则将广告活动封装成Node，存放到cache中
		if (set(CAMP_KEY_PREFIX + id, JSON.toJSONString(campaign))) {
			cache.put(id, new Node(campaign, systemTime.getTime()));
			return true;
		} else {
			log.error("更新Campaign发生错误");
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see com.asme.dao.CampaignDAO#changeAutoStatus(long, int)
	 */
	/**
	 * 修改活动自动状态
	 * @param campid
	 * @param status
	 * @return
	 */
	@Override
	public boolean changeAutoStatus(long campid, CampaignAutoStatus status) {
		//从本地redis中取出活动，如果为空则直接返回false
		String json = get(CAMP_KEY_PREFIX + campid);
		if (json == null) {
			return false;
		}
		//将redis中取出的活动转换成Campaign对象
		Campaign cmp = JSON.parseObject(json, Campaign.class);
		//修改Campaign中的自动状态
		cmp.setAutoStatus(status);
		//将修改完状态的对象转换成json字符串后，重新写回redis中
		set(CAMP_KEY_PREFIX + campid, JSON.toJSONString(cmp));
		//从本地的cache中按照活动id取出，封装有Campaign的node
		Node node = cache.get(campid);
		//如果活动不为空，则修改自动状态，否则不处理
		if (node != null) {
			node.data.setAutoStatus(status);
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see com.asme.dao.CampaignDAO#changeManulStatus(long, int)
	 */
	/**
	 * 修改活动手动状态
	 * @param campid
	 * @param status
	 * @return
	 * <p>
	 * 逻辑过程与修改自动状态类似
	 * </p>
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
			node.data.setManulStatus(status);
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see com.asme.dao.CampaignDAO#getCampaignById(long)
	 */
	/**
	 * 根据ID查找投放
	 * @param pub
	 * @return
	 */
	@Override
	public Campaign getCampaignById(long id) {
		//先从本地的cache中，根据id取出node
		Node node = cache.get(id);
		//如果不为null在修改node.lastAccessTime为系统当前时间后，直接返回node.data,node.data就是Campaign对象。
		if(node != null) {
			node.lastAccessTime = systemTime.getTime();
			return node.data;
		}
		//如果本地的cache中没有Campaign，则从redis中根据前缀取广告活动
		String json = get(CAMP_KEY_PREFIX + id);
		//如果本地cache中没有活动，则直接返回null
		if (json == null) {
			return null;
		}
		
		Campaign cmp = JSON.parseObject(json, Campaign.class);
		long now = systemTime.getTime();
		//使用活动，系统当前时间，创建一个node
		node = new Node(cmp, now);
		Node oldNode = cache.putIfAbsent(id, node);
		if(oldNode != null) {
			oldNode.data = cmp;
			oldNode.lastAccessTime = now;
		}
		return cmp;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.asme.ad.PubDAO#delPubById(long)
	 */
	/**
	 * 根据ID删除投放
	 * @param pub
	 * @return
	 */
	@Override
	public boolean delCampaignById(long id) {
		//删除本地redis中的Campaign
		boolean ret = del(CAMP_KEY_PREFIX + id) == 1l;
		//删除cache中的活动
		cache.remove(id);
		//删除缓存中的预算数据
		priceFactor.remove(id);
		//删除缓存总的点击率
		ctr.remove(id);
//		af.remove(id);
//		del(AF_KEY_PREFIX + id);
		return ret;
	}

	/* (non-Javadoc)
	 * @see com.asme.dao.CampaignDAO#getAllCampaigns()
	 */
	/**
	 * 获取所有的投放
	 * @return
	 */
	@Override
	public List<Campaign> getAllCampaigns() {
		//先取出reids中所有活动的key，之后再使用key一个一个的从reids中取出Campaign对应的json字符窜，转换成对象后，放到List集合中返回。
		List<Campaign> camps = new ArrayList<Campaign>();
		Set<String> keys = keys(CAMP_KEY_PREFIX + "*");
		if(keys == null) return camps;
		for (String key : keys) {
			String json = get(key);
			camps.add(JSON.parseObject(json, Campaign.class));
		}
		return camps;
	}

	/* (non-Javadoc)
	 * @see com.asme.dao.CampaignDAO#getAf(com.asme.ad.em.Adx, long)
	 */
	/**
	 * 获取AF
	 * @param campid
	 * @return
	 */
	@Override
	public int getAf(long campid) {
		//直接从本地缓存af中取af值，如果没有则返回0
		Integer a = af.get(campid);
		return a == null ? 0 : a;
	}

	/* (non-Javadoc)
	 * @see com.asme.dao.CampaignDAO#setAf(com.asme.ad.em.Adx, long, int)
	 */
	/**
	 * 修改AF
	 * @param campid
	 * @return
	 */
	@Override
	public void setAf(long campid, int af) {
		//先将af值设置到本地缓存中，再将af设置到redis中。
		this.af.put(campid, af);
		set(AF_KEY_PREFIX + campid, String.valueOf(af));
	}

	/* (non-Javadoc)
	 * @see com.asme.dao.CampaignDAO#changeCampPriceFactor(long, float)
	 */
	/**
	 * 用来做均衡投放
	 * 修改价格因子
	 * @param delta
	 */
	@Override
	public void changeCampPriceFactor(long campid, float factor) {
		priceFactor.put(campid, factor);
	}

	/* (non-Javadoc)
	 * @see com.asme.dao.CampaignDAO#changeCampCtr(long, float)
	 */
	/**
	 * 修改活动当前实际的CTR
	 * @param campid
	 * @param ctr
	 */
	@Override
	public void changeCampCtr(long campid, float ctr) {
		this.ctr.put(campid, ctr);
	}

	/* (non-Javadoc)
	 * @see com.asme.dao.CampaignDAO#getCampPriceFactor(long)
	 */
	/**
	 * 获取价格因子
	 */
	@Override
	public float getCampPriceFactor(long id) {
		Float f = priceFactor.get(id);
		f = f == null ? 1.0f : f;

		// 控制上下限
		if (f > 3.0f) f = 3.0f;
		if (f < 0.3f) f = 0.3f;
		return f;
	}

	/* (non-Javadoc)
	 * @see com.asme.dao.CampaignDAO#getCpmPrice(long)
	 */
	/**
	 * 获取活动的价格
	 */
	@Override
	public long getCpmPrice(Campaign camp) {
		//从camp中取出 expendType（结算类型）
		if (camp.getExpendType() == ExpendType.CPM) {
			//如果是cpm方式结算，则直接将创建活动时用户填写的价格返回
			return camp.getPrice();
		} else if (camp.getExpendType() == ExpendType.CPC) {
			//如果是cpc方式结算，取出这个活动的以往点击率
			Float c = ctr.get(camp.getId());
			if (c == null || c < 0.1f) {
				// 没有ctr信息，或者CTR实在太低，默认用千分之一
				return camp.getPrice();
			}
			// 如果点击率超过1%，按1%算
			if(c >= 10) c = 10.0f;
			return (long) (camp.getPrice() * c);
		}
		// 其他的计费方式，ADX里应该没有
		return 0l;
	}
	
	/**
	 * 设置广告主毛利率
	 */
	@Override
	public void setPartnerGrossProfit(long partnerId, int gross) {
		partnerGrossProfit.put(partnerId, gross);
		set(PARTNER_GROSS_PROFIT_KEY_PREFIX + partnerId, String.valueOf(gross));
	}
	
	/**
	 * 设置活动毛利率
	 */
	@Override
	public void setCampGrossProfit(long campid, int gross) {
		campGrossProfit.put(campid, gross);
		set(CAMP_GROSS_PROFIT_KEY_PREFIX + campid, String.valueOf(gross));
	}
	
	/**
	 * 获取广告主的毛利率，如果广告主没有设置，则获取系统的毛利率
	 */
	@Override
	public int getPartnerGrossProfit(long partnerId) {
		Integer ret = partnerGrossProfit.get(partnerId);
		return ret == null ? sysGrosspProfit : ret;
	}
	
	/**
	 * 根据campid获取毛利率，如果活动中没有设置，则使用广告主的毛利率
	 */
	@Override
	public int getCampGrossProfit(long partnerId, long campid) {
		Integer ret = campGrossProfit.get(campid);
		return ret == null ? getPartnerGrossProfit(partnerId) : ret;
	}

	/**
	 * 小时限制暂停
	 * @param campaignId
	 * @param hour
	 * @return
	 */
	@Override
	public boolean setCampHourPause(long campaignId, int hour) {
//		xtarderDebugLog.info("存放小时数据:"+hour+">>systemTime.getHour()>>活动id>>"+campaignId);
		hourPause.put(campaignId, hour);
		setex(CAMP_HOUR_PAUSE + campaignId, String.valueOf(hour), 3600);
		return false;
	}
	@Override
	public boolean isPause(long campaignId) {
		Integer h = hourPause.get(campaignId);
		
//		xtarderDebugLog.info("nn:"+h+">>systemTime.getHour()>>"+systemTime.getHour()+">>活动id>>"+campaignId);
		return h != null && h == systemTime.getHour();
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

	/**
	 * @param sysGrosspProfit the sysGrosspProfit to set
	 */
	public void setSysGrosspProfit(int sysGrosspProfit) {
		this.sysGrosspProfit = sysGrosspProfit;
	}

	private static class Node {
		private Campaign data;
		private long lastAccessTime;
		private Node(Campaign data, long lastAccessTime) {
			this.data = data;
			this.lastAccessTime = lastAccessTime;
		}
	}
}
