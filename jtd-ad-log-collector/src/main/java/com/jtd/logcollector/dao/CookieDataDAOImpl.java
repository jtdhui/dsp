package com.jtd.logcollector.dao;

import com.alibaba.fastjson.JSON;
import com.jtd.logcollector.util.SystemTime;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月12日
 * @项目名称 dsp-log-collector
 * @描述 <p></p>
 */
public class CookieDataDAOImpl extends AbstractDBSelectedShardedRedisDAO
		implements CookieDataDAO {
	
	// 缺省60天过期
	private static final int DEFAULT_EXPIRE = 60 * 60 * 24 * 60;

	// 过期时间和系统时间工具
	private int expire = DEFAULT_EXPIRE;
	private SystemTime systemTime;

	/**
	 * <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	 * 根据添加cookie数据
	 */
	@Override
	public void addCookieData(String cid, Map<Long, String[]> data) {
		for (Iterator<Entry<Long, String[]>> it = data.entrySet().iterator(); it.hasNext();) {
			Entry<Long, String[]> e = it.next();
			hsetex(cid, expire, String.valueOf(e.getKey()), JSON.toJSONString(e.getValue()));
		}
	}

	/**
	 * 从redisA中活用用户数据
	 * @param  				cid 			cookieid
	 * 返回值：
	 * 		ret
	 * 			key：		gid			就是cookiegid表中的数据
	 * 			value：		String[]	[0]=这条数据的过期时间
	 * 									[1]=其它的用户属性数据
	 */
	@Override
	public Map<Long, String[]> getCookieData(String cid) {
		long now = systemTime.getTime();
		Map<Long, String[]> ret = new TreeMap<Long, String[]>();
		//获取redisA中，cookieid对应的hash中所有的field和field对应的值
		Map<String, String> data = hgetAll(cid);
		//如果redis中没有数据，则返回空的Map
		if(data == null) return ret;
		//循环所有field
		for (Iterator<Entry<String, String>> it = data.entrySet().iterator(); it.hasNext();) {
			Entry<String, String> e = it.next();
			//cookie 分组id
			Long ckgroupid = Long.parseLong(e.getKey());
			//这个gid对应的用户属性，现在只有过期时间
			String[] attr = JSON.parseObject(e.getValue(), String[].class);
			//获取数组中对应的第一个值，是这条数据的过期时间
			long exp = Long.parseLong(attr[0]);

			// 过期的直接跳过，不去删除了，删除操作留给CookieMapping的时候做
			if(exp < now) continue;
			//将数据放到返回的map中
			ret.put(ckgroupid, attr);
		}
		return ret;
	}

	/**
	 * @param expire the expire to set
	 */
	public void setExpire(int expire) {
		this.expire = expire;
	}

	/**
	 * @param systemTime the systemTime to set
	 */
	public void setSystemTime(SystemTime systemTime) {
		this.systemTime = systemTime;
	}
}
