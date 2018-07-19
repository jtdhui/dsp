package com.jtd.engine.dao;

import com.alibaba.fastjson.JSON;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;


/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月8日
 * @项目名称 dsp-engine
 * @描述 <p>链接redis抽象类，基类</p>
 */
public abstract class AbstractDBSelectedShardedRedisDAO {

	protected Log log = LogFactory.getLog(AbstractDBSelectedShardedRedisDAO.class);

	// redis连接池 所有的连接都已经选中了集群上的dbindex
	protected ShardedJedisPool pool;

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
				log.info(this.getClass().getName() + " Ping Redis Server[" + host + ":" + port + "/" + j.getDB() + "]: " + j.ping());
			}
			pool.returnResource(sjedis);
			log.info(this.getClass().getName() + " Redis服务器连接成功");
		} catch (Exception e) {
			log.error(this.getClass().getName() + " Redis测试连接出错", e);
			if (sjedis != null) pool.returnBrokenResource(sjedis);
		}
	}
	
	protected abstract int getIndex();

	public void setPool(ShardedJedisPool pool) {
		this.pool = pool;
	}
	
	/**
	 * 设置key的过期时间
	 * @param key
	 * @param seconds
	 * @return void
	 */
	protected void expire(String key, int seconds) {
		ShardedJedis sjedis = null;
		try {
			sjedis = pool.getResource();
			sjedis.expire(key, seconds, getIndex());
			//释放资源
			pool.returnResource(sjedis);
		} catch (Exception e) {
			log.error("访问Redis EXPIRE出错, key:" + key + ",expire:" + seconds, e);
			if (sjedis != null) pool.returnBrokenResource(sjedis);
		}
	}
	
	/**
	 * 获取某个key对应的value
	 * @param key
	 * @return
	 * @return String
	 */
	protected String get(String key) {
		ShardedJedis sjedis = null;
		String value = null;
		try {
			//获取资源
			sjedis = pool.getResource();
			value = sjedis.get(key, getIndex());
			//释放资源
			pool.returnResource(sjedis);
		} catch (Exception e) {
			log.error("访问Redis GET出错key:" + key, e);
			if (sjedis != null) pool.returnBrokenResource(sjedis);
		}
		return value;
	}
	
	/**
	 * 删除一个key，并返回这个key对应的value
	 * @param key
	 * @return
	 * @return String
	 */
	protected String rem(String key) {
		ShardedJedis sjedis = null;
		String value = null;
		try {
			sjedis = pool.getResource();
			value = sjedis.get(key, getIndex());
			sjedis.del(key, getIndex());
			pool.returnResource(sjedis);
		} catch (Exception e) {
			log.error("访问Redis GET出错key:" + key, e);
			if (sjedis != null) pool.returnBrokenResource(sjedis);
		}
		return value;
	}
	/**
	 * 给数据库中指定的key，赋值
	 * @param key
	 * @param value
	 * @return
	 * @return String
	 */
	protected String set(String key, String value) {
		ShardedJedis sjedis = null;
		String ret = null;
		try {
			sjedis = pool.getResource();
			ret = sjedis.set(key, value, getIndex());
			pool.returnResource(sjedis);
		} catch (Exception e) {
			log.error("访问Redis SET出错key:" + key + ",value:" + value, e);
			if (sjedis != null) pool.returnBrokenResource(sjedis);
		}
		return ret;
	}
	/**
	 * 向库中添加key，value，同时设置过期时间
	 * @param key
	 * @param value
	 * @param seconds
	 * @return
	 * @return String
	 */
	protected String setex(String key, String value, int seconds) {
		ShardedJedis sjedis = null;
		String ret = null;
		try {
			sjedis = pool.getResource();
			ret = sjedis.setex(key, seconds, value, getIndex());
			pool.returnResource(sjedis);
		} catch (Exception e) {
			log.error("访问Redis SET出错key:" + key + ",value:" + value, e);
			if (sjedis != null) pool.returnBrokenResource(sjedis);
		}
		return ret;
	}
	/**
	 * 设置两个key，同时设置过期时间
	 * @param kv1
	 * @param kv2
	 * @param seconds
	 * @return void
	 */
	protected void doubleSetex(String kv1, String kv2, int seconds) {
		ShardedJedis sjedis = null;
		try {
			sjedis = pool.getResource();
			sjedis.setex(kv1, seconds, kv2, getIndex());
			sjedis.setex(kv2, seconds, kv1, getIndex());
			pool.returnResource(sjedis);
		} catch (Exception e) {
			log.error("访问Redis DOUBLESETEX出错kv1:" + kv1 + ",kv2:" + kv2, e);
			if (sjedis != null) pool.returnBrokenResource(sjedis);
		}
	}
	/**
	 * 返回名称为key的hash，field为参数field的value
	 * @param key
	 * @param field
	 * @return
	 * @return String
	 */
	protected String hget(String key, String field) {
		ShardedJedis sjedis = null;
		String value = null;
		try {
			sjedis = pool.getResource();
			value = sjedis.hget(key, field, getIndex());
			pool.returnResource(sjedis);
		} catch (Exception e) {
			log.error("访问Redis HGET出错key:" + key + ",field:" + field, e);
			if (sjedis != null) pool.returnBrokenResource(sjedis);
		}
		return value;
	}
	/**
	 * 返回名称为key的hash中所有的键（field）及其对应的value
	 * @param key
	 * @return
	 * @return Map<String,String>
	 */
	protected Map<String, String> hgetAll(String key) {
		ShardedJedis sjedis = null;
		Map<String, String> value = null;
		try {
			sjedis = pool.getResource();
			value = sjedis.hgetAll(key, getIndex());
			pool.returnResource(sjedis);
		} catch (Exception e) {
			log.error("访问Redis HGET出错key:" + key, e);
			if (sjedis != null) pool.returnBrokenResource(sjedis);
		}
		return value;
	}
	/**
	 * 向名称为key的hash中添加field及对应的value
	 * @param key
	 * @param field
	 * @param value
	 * @return
	 * @return Long
	 */
	protected Long hset(String key, String field, String value) {
		ShardedJedis sjedis = null;
		Long ret = null;
		try {
			sjedis = pool.getResource();
			ret = sjedis.hset(key, field, value, getIndex());
			pool.returnResource(sjedis);
		} catch (Exception e) {
			log.error("访问Redis HSET出错key:" + key + ",field:" + field + ",value:" + value, e);
			if (sjedis != null) pool.returnBrokenResource(sjedis);
		}
		return ret;
	}
	
	/**
	 * 向名称为key的hash中添加field及对应的value，并设置key对应的hash过期的时间
	 * @param key
	 * @param seconds
	 * @param field
	 * @param value
	 * @return
	 * @return Long
	 */

	protected Long hsetex(String key, int seconds, String field, String value) {
		return hsetex(key, seconds, field, value, true);
	}
	
	/**
	 * 向名称为key的hash中添加field及对应的value，并设置key对应的hash过期的时间
	 * @param key
	 * @param seconds
	 * @param field
	 * @param value
	 * @return
	 * @return Long
	 */
	protected Long hsetex(String key, int seconds, String field, String value, boolean updateExpire) {
		ShardedJedis sjedis = null;
		Long ret = null;
		try {
			sjedis = pool.getResource();
			ret = sjedis.hset(key, field, value, getIndex());
			if (updateExpire || (ret != null && ret == 1l)) {
				sjedis.expire(key, seconds, getIndex());
			}
			pool.returnResource(sjedis);
		} catch (Exception e) {
			log.error("访问Redis HSETEX出错key:" + key + ",field:" + field + ",value:" + value, e);
			if (sjedis != null) pool.returnBrokenResource(sjedis);
		}
		return ret;
	}
	/**
	 * 将key对应的value，增加increby，并指定过期时间
	 * @param key
	 * @param seconds
	 * @param increby
	 * @return
	 * @return Long
	 */
	protected Long increexby(String key, int seconds, int increby) {
		return increexby(key, seconds, increby, true);
	}
	
	/**
	 * 将key对应的value，增加increby，并指定过期时间
	 * @param key
	 * @param seconds
	 * @param increby
	 * @return
	 * @return Long
	 */
	protected Long increexby(String key, int seconds, int increby, boolean updateExpire) {
		ShardedJedis sjedis = null;
		Long ret = null;
		try {
			sjedis = pool.getResource();
			ret = sjedis.incrBy(key, increby, getIndex());
			if (updateExpire || (ret != null && ret == increby)) {
				sjedis.expire(key, seconds, getIndex());
			}
			pool.returnResource(sjedis);
		} catch (Exception e) {
			log.error("访问Redis INCREEXBY出错key:" + key + ",increby:" + increby, e);
			if (sjedis != null) pool.returnBrokenResource(sjedis);
		}
		return ret;
	}
	
	/**
	 * 将key对应的hash中的，field对应的value增加指定的值，这里是increby
	 * @param key
	 * @param field
	 * @param increby
	 * @return
	 * @return Long
	 */
	protected Long hincreby(String key, String field, int increby) {
		ShardedJedis sjedis = null;
		Long ret = null;
		try {
			sjedis = pool.getResource();
			ret = sjedis.hincrBy(key, field, increby, getIndex());
			pool.returnResource(sjedis);
		} catch (Exception e) {
			log.error("访问Redis HINCREBY出错key:" + key + ",field:" + field + ",increby:" + increby, e);
			if (sjedis != null) pool.returnBrokenResource(sjedis);
		}
		return ret;
	}
	
	/**
	 * 将key对应的hash中的，field对应的value增加指定的值，这里是increby，并指定过期时间
	 * @param key
	 * @param field
	 * @param increby
	 * @return
	 * @return Long
	 */
	protected Long hincreexby(String key, int seconds, String field, int increby) {
		return hincreexby(key, seconds, field, increby, true);
	}

	protected Long hincreexby(String key, int seconds, String field, int increby, boolean updateExpire) {
		ShardedJedis sjedis = null;
		Long ret = null;
		try {
			sjedis = pool.getResource();
			ret = sjedis.hincrBy(key, field, increby, getIndex());
			if (updateExpire || (ret != null && ret == increby)) {
				sjedis.expire(key, seconds, getIndex());
			}
			pool.returnResource(sjedis);
		} catch (Exception e) {
			log.error("访问Redis HINCREEXBY出错key:" + key + ",field:" + field + ",increby:" + increby, e);
			if (sjedis != null) pool.returnBrokenResource(sjedis);
		}
		return ret;
	}
	
	/**
	 * 向名称为key的set中添加元素
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
			ret = sjedis.sadd(key, getIndex(), member);
			pool.returnResource(sjedis);
		} catch (Exception e) {
			log.error("访问Redis SADD出错key:" + key + ",member:" + member, e);
			if (sjedis != null) pool.returnBrokenResource(sjedis);
		}
		return ret;
	}
	
	/**
	 * 删除名称为key的set中的元素member
	 * @param key
	 * @param member
	 * @return
	 * @return Long
	 */
	protected Long srem(String key, String member) {
		ShardedJedis sjedis = null;
		Long ret = null;
		try {
			sjedis = pool.getResource();
			ret = sjedis.srem(key, getIndex(), member);
			pool.returnResource(sjedis);
		} catch (Exception e) {
			log.error("访问Redis SREM出错key:" + key + ",member:" + member, e);
			if (sjedis != null) pool.returnBrokenResource(sjedis);
		}
		return ret;
	}
	
	/**
	 * 返回名称为key的set的所有元素
	 * @param key
	 * @return
	 * @return Set<String>
	 */
	protected Set<String> smembers(String key) {
		ShardedJedis sjedis = null;
		Set<String> ret = null;
		try {
			sjedis = pool.getResource();
			ret = sjedis.smembers(key, getIndex());
			pool.returnResource(sjedis);
		} catch (Exception e) {
			log.error("访问Redis SMEMBERS出错key:" + key, e);
			if (sjedis != null) pool.returnBrokenResource(sjedis);
		}
		return ret;
	}
	
	/**
	 * 获取所有节点的key对应的value
	 * @param pattern
	 * @return
	 * @return Set<String>
	 */
	protected Set<String> keys(String pattern) {
		ShardedJedis sjedis = null;
		Set<String> ret = new TreeSet<String>();
		try {
			sjedis = pool.getResource();
			Collection<Jedis> jediss = sjedis.getAllShards();
			for (Jedis j : jediss) {
				j.select(getIndex());
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
