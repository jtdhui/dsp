package com.jtd.collector.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import redis.clients.jedis.DBIndexSelectedShardedJedisPool;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ShardedJedis;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月12日
 * @项目名称 dsp-cache-collector
 * @描述 <p></p>
 */
public abstract class AbstractDBSelectedShardedRedisDAO {

	protected Log log = LogFactory.getLog(AbstractDBSelectedShardedRedisDAO.class);

	// redis连接池 所有的连接都已经选中了集群上的dbindex
	protected DBIndexSelectedShardedJedisPool pool;

	/**
	 * 初始化
	 */
	public void init() {
		ShardedJedis sjedis = null;
		try {
			sjedis = pool.getResource();
			Collection<Jedis> jediss = sjedis.getAllShards();
			for (Jedis j : jediss) {
				String host = j.getClient().getHost();
				int port = j.getClient().getPort();
				log.debug(this.getClass().getName() + " Ping Redis Server[" + host + ":" + port + "/" + j.getDB() + "]: " + j.ping());
			}
			pool.returnResource(sjedis);
			log.debug(this.getClass().getName() + " Redis服务器连接成功");
		} catch (Exception e) {
			log.error(this.getClass().getName() + " Redis测试连接出错", e);
			if (sjedis != null) pool.returnBrokenResource(sjedis);
		}
	}

	public void setPool(DBIndexSelectedShardedJedisPool pool) {
		this.pool = pool;
	}

	protected void expire(String key, int seconds) {
		ShardedJedis sjedis = null;
		try {
			sjedis = pool.getResource();
			sjedis.expire(key, seconds);
			pool.returnResource(sjedis);
		} catch (Exception e) {
			log.error("访问Redis EXPIRE出错, key:" + key + ",expire:" + seconds, e);
			if (sjedis != null) pool.returnBrokenResource(sjedis);
		}
	}

	protected String get(String key) {
		ShardedJedis sjedis = null;
		String value = null;
		try {
			sjedis = pool.getResource();
			value = sjedis.get(key);
			pool.returnResource(sjedis);
		} catch (Exception e) {
			log.error("访问Redis GET出错key:" + key, e);
			if (sjedis != null) pool.returnBrokenResource(sjedis);
		}
		return value;
	}

	protected String rem(String key) {
		ShardedJedis sjedis = null;
		String value = null;
		try {
			sjedis = pool.getResource();
			value = sjedis.get(key);
			sjedis.del(key);
			pool.returnResource(sjedis);
		} catch (Exception e) {
			log.error("访问Redis GET出错key:" + key, e);
			if (sjedis != null) pool.returnBrokenResource(sjedis);
		}
		return value;
	}

	protected String set(String key, String value) {
		ShardedJedis sjedis = null;
		String ret = null;
		try {
			sjedis = pool.getResource();
			ret = sjedis.set(key, value);
			pool.returnResource(sjedis);
		} catch (Exception e) {
			log.error("访问Redis SET出错key:" + key + ",value:" + value, e);
			if (sjedis != null) pool.returnBrokenResource(sjedis);
		}
		return ret;
	}

	protected String setex(String key, String value, int seconds) {
		ShardedJedis sjedis = null;
		String ret = null;
		try {
			sjedis = pool.getResource();
			ret = sjedis.setex(key, seconds, value);
			pool.returnResource(sjedis);
		} catch (Exception e) {
			log.error("访问Redis SET出错key:" + key + ",value:" + value, e);
			if (sjedis != null) pool.returnBrokenResource(sjedis);
		}
		return ret;
	}

	protected void doubleSetex(String kv1, String kv2, int seconds) {
		ShardedJedis sjedis = null;
		try {
			sjedis = pool.getResource();
			sjedis.setex(kv1, seconds, kv2);
			sjedis.setex(kv2, seconds, kv1);
			pool.returnResource(sjedis);
		} catch (Exception e) {
			log.error("访问Redis DOUBLESETEX出错kv1:" + kv1 + ",kv2:" + kv2, e);
			if (sjedis != null) pool.returnBrokenResource(sjedis);
		}
	}

	protected String hget(String key, String field) {
		ShardedJedis sjedis = null;
		String value = null;
		try {
			sjedis = pool.getResource();
			value = sjedis.hget(key, field);
			pool.returnResource(sjedis);
		} catch (Exception e) {
			log.error("访问Redis HGET出错key:" + key + ",field:" + field, e);
			if (sjedis != null) pool.returnBrokenResource(sjedis);
		}
		return value;
	}

	protected Map<String, String> hgetAll(String key) {
		ShardedJedis sjedis = null;
		Map<String, String> value = null;
		try {
			sjedis = pool.getResource();
			value = sjedis.hgetAll(key);
			pool.returnResource(sjedis);
		} catch (Exception e) {
			log.error("访问Redis HGET出错key:" + key, e);
			if (sjedis != null) pool.returnBrokenResource(sjedis);
		}
		return value;
	}

	protected Long hset(String key, String field, String value) {
		ShardedJedis sjedis = null;
		Long ret = null;
		try {
			sjedis = pool.getResource();
			ret = sjedis.hset(key, field, value);
			pool.returnResource(sjedis);
		} catch (Exception e) {
			log.error("访问Redis HSET出错key:" + key + ",field:" + field + ",value:" + value, e);
			if (sjedis != null) pool.returnBrokenResource(sjedis);
		}
		return ret;
	}

	protected Long hsetex(String key, int seconds, String field, String value) {
		return hsetex(key, seconds, field, value, true);
	}

	protected Long hsetex(String key, int seconds, String field, String value, boolean updateExpire) {
		ShardedJedis sjedis = null;
		Long ret = null;
		try {
			sjedis = pool.getResource();
			ret = sjedis.hset(key, field, value);
			if (updateExpire || (ret != null && ret == 1l)) {
				sjedis.expire(key, seconds);
			}
			pool.returnResource(sjedis);
		} catch (Exception e) {
			log.error("访问Redis HSETEX出错key:" + key + ",field:" + field + ",value:" + value, e);
			if (sjedis != null) pool.returnBrokenResource(sjedis);
		}
		return ret;
	}

	protected Long increexby(String key, int seconds, int increby) {
		return increexby(key, seconds, increby, true);
	}

	protected Long increexby(String key, int seconds, int increby, boolean updateExpire) {
		ShardedJedis sjedis = null;
		Long ret = null;
		try {
			sjedis = pool.getResource();
			ret = sjedis.incrBy(key, increby);
			if (updateExpire || (ret != null && ret == increby)) {
				sjedis.expire(key, seconds);
			}
			pool.returnResource(sjedis);
		} catch (Exception e) {
			log.error("访问Redis INCREEXBY出错key:" + key + ",increby:" + increby, e);
			if (sjedis != null) pool.returnBrokenResource(sjedis);
		}
		return ret;
	}

	protected Long hincreby(String key, String field, int increby) {
		ShardedJedis sjedis = null;
		Long ret = null;
		try {
			sjedis = pool.getResource();
			ret = sjedis.hincrBy(key, field, increby);
			pool.returnResource(sjedis);
		} catch (Exception e) {
			log.error("访问Redis HINCREBY出错key:" + key + ",field:" + field + ",increby:" + increby, e);
			if (sjedis != null) pool.returnBrokenResource(sjedis);
		}
		return ret;
	}

	protected Long hincreexby(String key, int seconds, String field, int increby) {
		return hincreexby(key, seconds, field, increby, true);
	}

	protected Long hincreexby(String key, int seconds, String field, int increby, boolean updateExpire) {
		ShardedJedis sjedis = null;
		Long ret = null;
		try {
			sjedis = pool.getResource();
			ret = sjedis.hincrBy(key, field, increby);
			if (updateExpire || (ret != null && ret == increby)) {
				sjedis.expire(key, seconds);
			}
			pool.returnResource(sjedis);
		} catch (Exception e) {
			log.error("访问Redis HINCREEXBY出错key:" + key + ",field:" + field + ",increby:" + increby, e);
			if (sjedis != null) pool.returnBrokenResource(sjedis);
		}
		return ret;
	}
	
	/**
	 * 向key对应的 set中添加字符串
	 * @param key
	 * @param member
	 * @return
	 * @return Long
	 */
	protected Long sadd(String key, String member) {
		ShardedJedis sjedis = null;
		Long ret = null;
		try {
			sjedis = pool.getResource();
			ret = sjedis.sadd(key, member);
			pool.returnResource(sjedis);
		} catch (Exception e) {
			log.error("访问Redis SADD出错key:" + key + ",member:" + member, e);
			if (sjedis != null) pool.returnBrokenResource(sjedis);
		}
		return ret;
	}

	protected Long srem(String key, String member) {
		ShardedJedis sjedis = null;
		Long ret = null;
		try {
			sjedis = pool.getResource();
			ret = sjedis.srem(key, member);
			pool.returnResource(sjedis);
		} catch (Exception e) {
			log.error("访问Redis SREM出错key:" + key + ",member:" + member, e);
			if (sjedis != null) pool.returnBrokenResource(sjedis);
		}
		return ret;
	}

	protected Set<String> smembers(String key) {
		ShardedJedis sjedis = null;
		Set<String> ret = null;
		try {
			sjedis = pool.getResource();
			ret = sjedis.smembers(key);
			pool.returnResource(sjedis);
		} catch (Exception e) {
			log.error("访问Redis SMEMBERS出错key:" + key, e);
			if (sjedis != null) pool.returnBrokenResource(sjedis);
		}
		return ret;
	}

	protected Set<String> keys(String pattern) {
		ShardedJedis sjedis = null;
		Set<String> ret = new TreeSet<String>();
		try {
			sjedis = pool.getResource();
			Collection<Jedis> jediss = sjedis.getAllShards();
			for (Jedis j : jediss) {
				ret.addAll(j.keys(pattern));
			}
			pool.returnResource(sjedis);
		} catch (Exception e) {
			log.error("访问Redis KEYS出错pattern:" + pattern, e);
			if (sjedis != null) pool.returnBrokenResource(sjedis);
		}
		return ret;
	}
}
