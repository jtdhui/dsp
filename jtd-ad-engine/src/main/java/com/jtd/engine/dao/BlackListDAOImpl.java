package com.jtd.engine.dao;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.jtd.engine.utils.SystemTime;
import com.jtd.engine.utils.Timer;
import com.jtd.engine.utils.TimerTask;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月8日
 * @项目名称 dsp-engine
 * @描述 <p></p>
 */
public class BlackListDAOImpl extends AbstractRedisDAO implements BlackListDAO {

	private static final long EXPIRE = 1000L * 60 * 15;
	private static final String CAMP_BLACKLIST_KEY_PREFIX = "BL";
	private static final String GLOBAL_BLKEY= "GBL";
	
	private volatile Set<String> globalBlackList = new HashSet<String>();

	// 查询缓存, 15分钟不使用的数据退出内存
	private ConcurrentHashMap<Long, Node> cache = new ConcurrentHashMap<Long, Node>();

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
				/**
				 * 如果本地的缓存 cache 存储的Node过了15分钟，则删除
				 */
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
	 * 设置全局黑名单
	 * <p>
	 * 1、将黑名单从list转到set。<br/>
	 * 2、将转换后的set赋值给成员变量，globalBlackList<br/>
	 * 3、删除本地redis中存储的全局黑名单。<br/>
	 * 4、将黑名单 list 写入到本地reids中，redis中采用的数据格式是set<String> <br/>
	 * </p>
	 */
	@Override
	public boolean setBlackList(List<String> blackList) {
		Set<String> globalBlackList = new HashSet<String>();
		if(blackList != null) {
			globalBlackList.addAll(blackList);
		}
		this.globalBlackList = globalBlackList;
		del(GLOBAL_BLKEY);
		if(blackList == null || blackList.size() == 0) return true;
		Long ret = sadd(GLOBAL_BLKEY, blackList);
		return ret != null && ret > 0l;
	}
	/**
	 * 设置活动的黑名单
	 * 
	 * @param blackList
	 * @param campid
	 * @return
	 * <p>
	 * 1、将黑名单从list转到set。<br/>
	 * 2、将转换后的set赋值给成员变量，ConcurrentHashMap<Long, Node> cache本地缓存中<br/>
	 * 	  2.1、从本地的cache中，按照活动id取出黑名单。
	 * 	  2.2、如果取出的node不为空，则将黑名单转换后的set赋值给node.data
	 * 	  2.3、如果为空，则不做处理。
	 *    2.4、如果黑名单参数为空，则删除 cache 中key=campid 对应的node，也就是删除黑名单。
	 * 3、删除本地redis中存储的活动黑名单。<br/>
	 * 4、将黑名单 list 写入到本地reids中，redis中采用的数据格式是set<String> <br/>
	 * </p>
	 */
	@Override
	public boolean setBlackList(List<String> blackList, long campid) {
		Set<String> bl = new HashSet<String>();
		if(blackList != null) {
			bl.addAll(blackList);
		}
		if (bl.size() > 0) {
			Node node = cache.get(campid);
			if (node != null) {
				node.data = bl;
			}
		} else {
			cache.remove(campid);
		}
		String key = CAMP_BLACKLIST_KEY_PREFIX + campid;
		del(key);
		if(blackList == null || blackList.size() == 0) return true;
		Long ret = sadd(key, blackList);
		return ret != null && ret > 0l;
	}
	/**
	 * 获取全局黑名单
	 */
	@Override
	public Set<String> getBlackList() {
		return globalBlackList;
	}
	
	/**
	 * 获取活动黑名单
	 */
	@Override
	public Set<String> getBlackList(long campid) {
		//从本地缓存 ConcurrentHashMap<String,Node> cache 中获取活动id对应的Node
		Node node = cache.get(campid);
		//如果不为空，则重置过期时间，并返回。
		if(node != null) {
			node.lastAccessTime = systemTime.getTime();
			return node.data;
		}
		//如果本地chache中没有，则拼装redis key，从redis中取出活动的黑名单
		Set<String> bl = smembers(CAMP_BLACKLIST_KEY_PREFIX + campid);
		//如果redis中没有，则直接返回空。
		if(bl == null) return null; 
		long now = systemTime.getTime();
		//将redis中的黑名单，写入到本地cache中。
		node = new Node(bl, now);
		Node oldNode = cache.putIfAbsent(campid, node);
		if(oldNode != null) {
			oldNode.data = bl;
			oldNode.lastAccessTime = now;
		}
		return bl;
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
		private volatile Set<String> data;
		private long lastAccessTime;
		private Node(Set<String> data, long lastAccessTime) {
			this.data = data;
			this.lastAccessTime = lastAccessTime;
		}
	}
}
