package com.jtd.engine.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月8日
 * @项目名称 dsp-engine
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
	/**
	 * 获取所有的的key对应的value
	 * @param pattern
	 * @return
	 * @return Set<String>
	 */
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
	/**
	 * 获取指定的key对应的value
	 * @param key
	 * @return
	 * @return String
	 */
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
	/**
	 * 是指key及其value
	 * @param key
	 * @param value
	 * @return
	 * @return boolean
	 */
	protected boolean set(String key, String value) {
		Jedis jedis = null;
		String ret = null;
		try {
			jedis = pool.getResource();
			ret = jedis.set(key, value);
			pool.returnResource(jedis);
		} catch (Exception e) {
			log.error("访问Redis SET出错, key:" + key + ",value:" + value, e);
			if (jedis != null) pool.returnBrokenResource(jedis);
		}
		return "ok".equalsIgnoreCase(ret);
	}
	
	/**
	 * 向库中设置一个key及其对应的value，并且指定过期时间
	 * @param key
	 * @param value
	 * @param expire
	 * @return
	 * @return boolean
	 */
	protected boolean setex(String key, String value, int expire) {
		Jedis jedis = null;
		String ret = null;
		try {
			jedis = pool.getResource();
			ret = jedis.setex(key, expire, value);
			pool.returnResource(jedis);
		} catch (Exception e) {
			log.error("访问Redis SETEX出错, key:" + key + ",value:" + value + ",expire:" + expire, e);
			if (jedis != null) pool.returnBrokenResource(jedis);
		}
		return "ok".equalsIgnoreCase(ret);
	}
	
	/**
	 * 删除指定的key
	 * @param key
	 * @return
	 * @return Long
	 */
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
	/**
	 * 获取key对应的hash中field对应的value
	 * @param key
	 * @param field
	 * @return
	 * @return String
	 */
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
	/**
	 * 获取key对应的hash中所有的field对应的value
	 * @param key
	 * @return
	 * @return Map<String,String>
	 */
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
	/**
	 * 向名称为key的set中添加元素一个list
	 * @param key
	 * @param value
	 * @return
	 * @return Long
	 */
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
	/**
	 * 返回名称为key对应的set中所有内容
	 * @param key
	 * @return
	 * @return Set<String>
	 */
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
