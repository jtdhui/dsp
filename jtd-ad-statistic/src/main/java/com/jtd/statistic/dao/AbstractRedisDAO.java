package com.jtd.statistic.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月11日
 * @项目名称 dsp-counter
 * @描述 <p></p>
 */
public abstract class AbstractRedisDAO {

	protected Log log = LogFactory.getLog(AbstractRedisDAO.class);
	
	// redis连接池
	protected JedisPool pool;

	/**
	 * 初始化
	 */
	public void init() {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			String host = jedis.getClient().getHost();
			int port = jedis.getClient().getPort();
			log.info(this.getClass().getName() + " Ping Redis Server[" + host + ":" + port + "/" + jedis.getDB() + "]: " + jedis.ping());
			pool.returnResource(jedis);
			log.info(this.getClass().getName() + " Redis服务器连接成功");
		} catch (Exception e) {
			log.error(this.getClass().getName() + " Redis测试连接出错", e);
			if (jedis != null) pool.returnBrokenResource(jedis);
		}
	}

	public void setPool(JedisPool pool) {
		this.pool = pool;
	}
	
	protected Set<String> keys(String pattern) {
		Jedis jedis = null;
		Set<String> value = null;
		try {
			jedis = pool.getResource();
			value = jedis.keys(pattern);
			pool.returnResource(jedis);
		} catch (Exception e) {
			log.error("访问Redis KEYS出错, pattern:" + pattern, e);
			if (jedis != null) pool.returnBrokenResource(jedis);
		}
		return value;
	}

	protected String get(String key) {
		Jedis jedis = null;
		String value = null;
		try {
			jedis = pool.getResource();
			value = jedis.get(key);
			pool.returnResource(jedis);
		} catch (Exception e) {
			log.error("访问Redis GET出错, key:" + key, e);
			if (jedis != null) pool.returnBrokenResource(jedis);
		}
		return value;
	}

	protected boolean set(String key, String value) {
		Jedis jedis = null;
		String ret = null;
		try {
			jedis = pool.getResource();
			//当调用set方法向redis中写入数据成功时，返回ok
			ret = jedis.set(key, value);
			pool.returnResource(jedis);
		} catch (Exception e) {
			log.error("访问Redis SET出错, key:" + key + ",value:" + value, e);
			if (jedis != null) pool.returnBrokenResource(jedis);
		}
		return "ok".equalsIgnoreCase(ret);
	}

	protected Long del(String key) {
		Jedis jedis = null;
		Long ret = 0l;
		try {
			jedis = pool.getResource();
			ret = jedis.del(key);
			pool.returnResource(jedis);
		} catch (Exception e) {
			log.error("访问Redis DEL出错, key:" + key, e);
			if (jedis != null) pool.returnBrokenResource(jedis);
		}
		return ret;
	}

	protected String hget(String key, String field) {
		Jedis jedis = null;
		String value = null;
		try {
			jedis = pool.getResource();
			value = jedis.hget(key, field);
			pool.returnResource(jedis);
		} catch (Exception e) {
			log.error("访问Redis HGET出错, key:" + key + ",field:" + field, e);
			if (jedis != null) pool.returnBrokenResource(jedis);
		}
		return value;
	}
	
	protected Map<String, String> hgetAll(String key) {
		Jedis jedis = null;
		Map<String, String> value = null;
		try {
			jedis = pool.getResource();
			value = jedis.hgetAll(key);
			pool.returnResource(jedis);
		} catch (Exception e) {
			log.error("访问Redis HGET出错key:" + key, e);
			if (jedis != null) pool.returnBrokenResource(jedis);
		}
		return value;
	}
	
	/**
	 * 返回0是field已存在做更新操作,返回1是插入操作
	 * @param key
	 * @param field
	 * @param value
	 * @return
	 */
	protected Long hset(String key, String field, String value) {
		Jedis jedis = null;
		Long ret = null;
		try {
			jedis = pool.getResource();
			ret = jedis.hset(key, field, value);
			pool.returnResource(jedis);
		} catch (Exception e) {
			log.error("访问Redis HSET出错, key:" + key + ",field:" + field + ",value:" + value, e);
			if (jedis != null) pool.returnBrokenResource(jedis);
		}
		return ret;
	}

	protected Long sadd(String key, List<String> value) {
		Jedis jedis = null;
		Long ret = null;
		try {
			jedis = pool.getResource();
			for(String v : value) {
				ret = jedis.sadd(key, v);
			}
			pool.returnResource(jedis);
		} catch (Exception e) {
			log.error("访问Redis SADD出错, key:" + key, e);
			if (jedis != null) pool.returnBrokenResource(jedis);
		}
		return ret;
	}
	
	protected Set<String> smembers(String key) {
		Jedis jedis = null;
		Set<String> ret = null;
		try {
			jedis = pool.getResource();
			ret = jedis.smembers(key);
			pool.returnResource(jedis);
		} catch (Exception e) {
			log.error("访问Redis SMEMBERS出错, key:" + key, e);
			if (jedis != null) pool.returnBrokenResource(jedis);
		}
		return ret;
	}
}
