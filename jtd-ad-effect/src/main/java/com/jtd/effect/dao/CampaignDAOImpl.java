package com.jtd.effect.dao;

import com.alibaba.fastjson.JSON;
import com.jtd.effect.util.SystemTime;
import com.jtd.effect.util.Timer;
import com.jtd.effect.util.TimerTask;
import com.jtd.web.constants.CampaignAutoStatus;
import com.jtd.web.constants.CampaignManulStatus;
import com.jtd.web.constants.ExpendType;
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
 * @项目名称 dsp-tracker
 * @描述 <p></p>
 */
public class CampaignDAOImpl extends AbstractRedisDAO implements CampaignDAO {

	private static final Log log = LogFactory.getLog(CampaignDAOImpl.class);
	//系统默认的毛利率
	private static final int SYS_GROSS_PROFIT = 30;
	//广告活动在redis中的过期时间，时间为15分钟
	private static final long EXPIRE = 1000L * 60 * 15;
	//广告活动在reids中key的前缀
	private static final String CAMP_KEY_PREFIX = "CMP";
	//af在redis中的前缀
	private static final String AF_KEY_PREFIX = "AF";
	//广告主毛利率的key前缀
	private static final String PARTNER_GROSS_PROFIT_KEY_PREFIX = "PGF";
	//广告活动毛利率的key前缀
	private static final String CAMP_GROSS_PROFIT_KEY_PREFIX = "CGF";

	// 查询缓存, 15分钟不使用的数据退出内存；这里存放的是 广告活动数据
	private ConcurrentHashMap<Long, Node> cache = new ConcurrentHashMap<Long, Node>();

	// 预算控制,key campid value 出价系统
	private ConcurrentHashMap<Long, Float> priceFactor = new ConcurrentHashMap<Long, Float>();

	// CTR key campid value 点击率为千分之N时, value = N
	private ConcurrentHashMap<Long, Float> ctr = new ConcurrentHashMap<Long, Float>();

	// AF key campid
	private ConcurrentHashMap<Long, Integer> af = new ConcurrentHashMap<Long, Integer>();

	// key partnerid, value grossprofit
	private ConcurrentHashMap<Long, Integer> partnerGrossProfit = new ConcurrentHashMap<Long, Integer>();

	// key campid, value grossprofit
	private ConcurrentHashMap<Long, Integer> campGrossProfit = new ConcurrentHashMap<Long, Integer>();
	
	// key campid
	private ConcurrentHashMap<Long, int[]> freqType = new ConcurrentHashMap<Long, int[]>();

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

		// 加载全部AF，key=AF+"_"+活动id
		//加载redis中所有广告活动的af值，缓存到ConcurrentHashMap<Long,Integer> af。
		Set<String> keys = keys(AF_KEY_PREFIX + "*");
		for(String key : keys) {
			af.put(Long.parseLong(key.substring(AF_KEY_PREFIX.length())), Integer.parseInt(get(key)));
		}
		
		//加载redis中所有广告主的毛利率，缓存到ConcurrentHashMap<Long,Integer> partnerGrossProfit
		keys = keys(PARTNER_GROSS_PROFIT_KEY_PREFIX + "*");
		for(String key : keys) {
			partnerGrossProfit.put(Long.parseLong(key.substring(PARTNER_GROSS_PROFIT_KEY_PREFIX.length())), Integer.parseInt(get(key)));
		}
		
		//加载redis中所有广告活动的毛利率，缓存到ConcurrentHashMap<Long,Integer> campGrossProfit
		keys = keys(CAMP_GROSS_PROFIT_KEY_PREFIX + "*");
		for(String key : keys) {
			campGrossProfit.put(Long.parseLong(key.substring(CAMP_GROSS_PROFIT_KEY_PREFIX.length())), Integer.parseInt(get(key)));
		}
		
		//每相隔一分钟，检查一下，用户缓存广告活动的 ConcurrentHashMap<Long,Node> cache，本地缓存中的广告活动是否过期。
		timer.timing(new TimerTask(){
			@Override
			public void run() {
				long now = systemTime.getTime();
				for(Iterator<Entry<Long, Node>> it = cache.entrySet().iterator(); it.hasNext();) {
					if(it.next().getValue().lastAccessTime + EXPIRE <= now) it.remove();
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

	/**
	 * 更新投放
	 * 如果存在则更新,不存在则添加
	 * @param pub
	 * @return
	 */
	@Override
	public boolean updateCampaign(Campaign campaign) {
		long id = campaign.getId();
		//把活动向reids中写入成功时，向cache中添加一个Node
		if (set(CAMP_KEY_PREFIX + id, JSON.toJSONString(campaign))) {
			cache.put(id, new Node(campaign, systemTime.getTime()));
			//将本地缓存freqType中，关于这个活动id的，频次数据删除。
			freqType.remove(id);
			return true;
		} else {
			log.error("更新Campaign发生错误");
			return false;
		}
	}

	/**
	 * 修改状态,自动状态
	 * @param campid
	 * @param status
	 * @return
	 */
	@Override
	public boolean changeAutoStatus(long campid, CampaignAutoStatus status) {
		//从redis中取出，这个活动的json
		String json = get(CAMP_KEY_PREFIX + campid);
		if (json == null) {
			return false;
		}
		//将json转换成，活动对象
		Campaign cmp = JSON.parseObject(json, Campaign.class);
		//修改自动状态
		cmp.setAutoStatus(status);
		//将修改后的内容，更新到redis中
		set(CAMP_KEY_PREFIX + campid, JSON.toJSONString(cmp));
		//从本地缓存中找到活动，并修改自动状态
		Node node = cache.get(campid);
		if (node != null) {
			node.data.setAutoStatus(status);
		}
		return true;
	}

	/**
	 * 修改状态，手动状态
	 * @param campid
	 * @param status
	 * @return
	 */
	@Override
	public boolean changeManulStatus(long campid, CampaignManulStatus status) {
		//从redis中取出活动
		String json = get(CAMP_KEY_PREFIX + campid);
		if (json == null) {
			return false;
		}
		//将取出的json字符串，转换成活动对象
		Campaign cmp = JSON.parseObject(json, Campaign.class);
		//修改活动的手动状态
		cmp.setManulStatus(status);
		//更新本地redis中的活动数据
		set(CAMP_KEY_PREFIX + campid, JSON.toJSONString(cmp));
		Node node = cache.get(campid);
		if (node != null) {
			node.data.setManulStatus(status);
		}
		return true;
	}

	/**
	 * 根据ID查找投放<<<<<<<<<<<<<<<<<<<<<
	 * @param pub
	 * @return
	 */
	@Override
	public Campaign getCampaignById(long id) {
		//先从本地缓存中找活动数据
		Node node = cache.get(id);
		if(node != null) {
			node.lastAccessTime = systemTime.getTime();
			return node.data;
		}
		//本地缓存中没有活动数据，则从redis中去活动数据
		String json = get(CAMP_KEY_PREFIX + id);
		if (json == null) {
			return null;
		}
		
		Campaign cmp = JSON.parseObject(json, Campaign.class);
		long now = systemTime.getTime();
		//重建一个新的Node，封装活动对象，并设置好过期时间。
		node = new Node(cmp, now);
		Node oldNode = cache.putIfAbsent(id, node);
		if(oldNode != null) {
			oldNode.data = cmp;
			oldNode.lastAccessTime = now;
		}
		//这只广告活动的频次数据
		setFretType(cmp);
		return cmp;
	}
	
	/**
	 * 在当前类 getCampaignById 中调用
	 * 设置广告活动的频次和是否非首屏
	 * @param campid
	 * @return [1 pv 2 click 3 pv和click,  小时]
	 */
	private void setFretType(Campaign camp) {
		if(freqType.containsKey(camp.getId())) return;
		Map<String, String> dims = camp.getCampaignDims();
		if(dims == null) return;
		//拿到广告活动，频次和是否首屏的数据。{freq:[24,5,5], firsts: 0},freq:[24,5,5],第一个是24小时，第二个是展示次数，第三个是点击次数
		String json = dims.get("freqandscreen");
		if(json == null) return;
		FreqAndScreen freq = JSON.parseObject(json, FreqAndScreen.class);
		if(freq.freq == null) return;
		//频次控制中，展示次数是否大于0，表示设置了控制展示
		boolean pv = freq.freq[1] > 0;
		//频次控制中，点击次数是否大于0，表示设置了控制点击
		boolean click = freq.freq[2] > 0;
		
		if(pv && click) {
			//如果pv和click都设置了，则key=活动id，value=[3,获取小时数据]
			freqType.put(camp.getId(), new int[]{3, freq.freq[0]});
		} else if(pv) {
			//如果pv设置了，则key=活动id，value=[1,获取小时数据]
			freqType.put(camp.getId(), new int[]{1, freq.freq[0]});
		} else if(click) {
			//如果click设置了，则key=活动id，value=[2,获取小时数据]
			freqType.put(camp.getId(), new int[]{2, freq.freq[0]});
		}
	}

	/**
	 * 根据ID删除投放	<<<<<<<<<<<<<<<<<<<<
	 * @param pub
	 * @return
	 */
	@Override
	public boolean delCampaignById(long id) {
		//删除redis中的广告活动数据
		boolean ret = del(CAMP_KEY_PREFIX + id) == 1l;
		//删除本地缓存中的广告活动数据
		cache.remove(id);
		//删除本地缓存中广告活动的价格因子数据
		priceFactor.remove(id);
		//删除本地缓存中，点击率数据
		ctr.remove(id);
		//删除本地缓存中，af值数据
		af.remove(id);
		//删除本地缓存中，频次类型数据
		freqType.remove(id);
		//删除本地redis中，广告活动对应的af数据
		del(AF_KEY_PREFIX + id);
		return ret;
	}

	/**
	 * 获取所有的投放，获取所有在投的广告活动<<<<<<<<<<<<<<<<<
	 * @return
	 */
	@Override
	public List<Campaign> getAllCampaigns() {
		List<Campaign> camps = new ArrayList<Campaign>();
		//从redis中获取，所有前缀为CMP的key
		Set<String> keys = keys(CAMP_KEY_PREFIX + "*");
		if(keys == null) return camps;
		for (String key : keys) {
			//根据key，一个一个的从redis中取出，广告活动的json字符串
			String json = get(key);
			//将json串转换成活动对象，添加到活动列表中。
			camps.add(JSON.parseObject(json, Campaign.class));
		}
		return camps;
	}

	/**
	 * 获取活动的af值<<<<<<<<<<<<<<<<<<<
	 * 从本地缓存中，根据活动id取af值，如果本地缓存中没有af值，则返回0，否则返回取出的值
	 */
	@Override
	public int getAf(long campid) {
		Integer a = af.get(campid);
		return a == null ? 0 : a;
	}

	/**
	 * 设置活动的af值<<<<<<<<<<<<<
	 */
	@Override
	public void setAf(long campid, int af) {
		//向本地缓存中添加af值
		this.af.put(campid, af);
		//向redis中添加af值
		set(AF_KEY_PREFIX + campid, String.valueOf(af));
	}

	/**
	 * 用来做均衡投放<<<<<<<<<<<<<<
	 * 修改价格因子
	 */
	@Override
	public void changeCampPriceFactor(long campid, float factor) {
		priceFactor.put(campid, factor);
	}

	/**
	 * 获取价格因子<<<<<<<<<<<<<<<<
	 * 从本地缓存中获取价格因子，如果没有，则返回1，如果有则返回取出的值
	 */
	@Override
	public float getCampPriceFactor(long id) {
		Float f = priceFactor.get(id);
		return f == null ? 1.0f : f;
	}

	/**
	 * 获取活动的价格<<<<<<<<<<<<<<
	 */
	@Override
	public long getCpmPrice(Campaign camp) {
		if(camp.getExpendType() == ExpendType.CPM) {
			//如果活动的结算方式是cpm,则直接返回价格
			return camp.getPrice();
		} else if(camp.getExpendType() == ExpendType.CPC) {
			//如果活动的结算方式是cpc，则从本地缓存 ctr 中取出活动的点击率
			Float c = ctr.get(camp.getId());
			if(c == null) {
				// 没有ctr信息，默认用千分之一，也就是直接返回cpm的价格
				return camp.getPrice();
			}
			//如果有ctr，则返回的价格是，曝光价格*点击率。这里点击率最低就是1；
			//如果点击率大于1，说明广告活动可以提高竞价价格，平台也不会亏
			return (long)(camp.getPrice() * c);
		}
		// 其他的计费方式，ADX里应该没有
		return 0l;
	}
	
	/**
	 * 修改广告主的毛利率<<<<<<<<<<<<<<<<<
	 */
	@Override
	public void setPartnerGrossProfit(long partnerId, int gross) {
		//将广告主的毛利率设置到，本地缓存中
		partnerGrossProfit.put(partnerId, gross);
		//将广告主的毛利率，写到reids中
		set(PARTNER_GROSS_PROFIT_KEY_PREFIX + partnerId, String.valueOf(gross));
	}

	/**
	 * 设置广告活动的毛利率<<<<<<<<<<<<<<<<<
	 */
	@Override
	public void setCampGrossProfit(long campid, int gross) {
		campGrossProfit.put(campid, gross);
		set(CAMP_GROSS_PROFIT_KEY_PREFIX + campid, String.valueOf(gross));
	}
	
	/**
	 * 获取广告主的毛利率<<<<<<<<<<<<<<<<<
	 */
	@Override
	public int getPartnerGrossProfit(long partnerId) {
		Integer ret = partnerGrossProfit.get(partnerId);
		return ret == null ? sysGrosspProfit : ret;
	}
	
	/**
	 * 获取广告主毛利<<<<<<<<<<<<<<<<<<<<<
	 */
	@Override
	public int getCampGrossProfit(long partnerId, long campid) {
		Integer ret = campGrossProfit.get(campid);
		return ret == null ? getPartnerGrossProfit(partnerId) : ret;
	}
	
	/**
	 * 获取广告活动的频次<<<<<<<<<<<<<<<<<<
	 */
	@Override
	public int[] getCampFreqType(long campid) {
		return freqType.get(campid);
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
