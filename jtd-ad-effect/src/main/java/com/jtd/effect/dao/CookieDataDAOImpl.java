package com.jtd.effect.dao;

import com.alibaba.fastjson.JSON;
import com.jtd.effect.util.SystemTime;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月12日
 * @项目名称 dsp-tracker
 * @描述 <p></p>
 */
public class CookieDataDAOImpl extends AbstractDBSelectedShardedRedisDAO
		implements CookieDataDAO {
	
	// cookie数据，缺省60天过期
	private static final int DEFAULT_EXPIRE = 60 * 60 * 24 * 60;

	// 过期时间和系统时间工具
	private int expire = DEFAULT_EXPIRE;
	private SystemTime systemTime;

	/**
	 * 向redis集群A中添加，cookie数据
	 */
	@Override
	public void addCookieData(String cid, Map<Long, String[]> data) {
		for (Iterator<Entry<Long, String[]>> it = data.entrySet().iterator(); it.hasNext();) {
			Entry<Long, String[]> e = it.next();
			hsetex(cid, expire, String.valueOf(e.getKey()), JSON.toJSONString(e.getValue()));
		}
	}

	/**
	 * 从redis中获取cookieData
	 */
	@Override
	public Map<Long, String[]> getCookieData(String cid) {
		//系统当前时间
		long now = systemTime.getTime();
		//创建一个TreeMap
		Map<Long, String[]> ret = new TreeMap<Long, String[]>();
		//获取cid对应的hash中，所有的field及field对应的value，并放到Map中
		Map<String, String> data = hgetAll(cid);
		for (Iterator<Entry<String, String>> it = data.entrySet().iterator(); it.hasNext();) {
			Entry<String, String> e = it.next();
			//cookie组id
			Long ckgroupid = Long.parseLong(e.getKey());
			//拿到组id对应的value
			String[] attr = JSON.parseObject(e.getValue(), String[].class);
			//取出对应的过期时间
			long exp = Long.parseLong(attr[0]);
			// 过期的直接跳过，不去删除了，删除操作留给CookieMapping的时候做
			if(exp > now) continue;
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
