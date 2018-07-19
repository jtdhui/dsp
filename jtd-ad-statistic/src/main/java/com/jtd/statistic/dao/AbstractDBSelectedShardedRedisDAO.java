package com.jtd.statistic.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPipeline;
import redis.clients.jedis.ShardedJedisPool;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月11日
 * @项目名称 dsp-counter
 * @描述 <p></p>
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

	protected void expire(String key, int seconds) {
		ShardedJedis sjedis = null;
		try {
			sjedis = pool.getResource();
			sjedis.expire(key, seconds, getIndex());
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
			value = sjedis.get(key, getIndex());
			pool.returnResource(sjedis);
		} catch (Exception e) {
			log.error("访问Redis GET出错key:" + key, e);
			if (sjedis != null) pool.returnBrokenResource(sjedis);
		}
		return value;
	}
	/**
	 * 获取key对应的值，然后再删除
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
	 * 返回key对应的hash，返回值为Map，其中map的key为field，value为field对应的value
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

	protected Long hsetex(String key, int seconds, String field, String value) {
		return hsetex(key, seconds, field, value, true);
	}

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

	protected Long increexby(String key, int seconds, int increby) {
		return increexby(key, seconds, increby, true);
	}
	
	/**
	 * 给指定的key，增加指定的值，如果updateExpire为true，则更新过期时间，如果updateExpire为false则看看添加后的
	 * 返回值 ret 是否和设置的 increby 相等，如果是也说名是新增的，则设置过期时间
	 * @param key						
	 * @param seconds			过期时间
	 * @param increby			增加的次数
	 * @param updateExpire		是否更新
	 * @return
	 * @return Long
	 */
	protected Long increexby(String key, int seconds, int increby, boolean updateExpire) {
		ShardedJedis sjedis = null;
		Long ret = null;
		try {
			sjedis = pool.getResource();
			//将redis中的key对应的值，增加 increby，
			ret = sjedis.incrBy(key, increby, getIndex());
			/**
			 * 如果是更新操作，则直接更新过期时间
			 * 如果不更新，则判断ret是否为null，并且ret返回值是否和increby相等，如果相等，则说明这个数据是新增的数据
			 */
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
	 * 批量执行hincreby  key，filed, value, (seconds)
	 * @param values
	 */
	protected void plhincreby(List<String[]> values) {
		ShardedJedis sjedis = null;
		try {
			sjedis = pool.getResource();
			ShardedJedisPipeline pl = sjedis.pipelined(getIndex());
			for(String[] v : values) {
				if(v.length == 4) {//数组的长度为4的，表示有过期时间
					//v每个值说明
					//[0]=key="DHC_" + yyyyMMdd + "_" + hour + "_" + campid
					//[1]=b，表示redis中hash的field，就是竞价
					//[2]=incrBy,增加的次数，就是field对应的值
					//[3]=过期时间，单位秒
					//为redis中key=v[0]，的hash，field为b的值，增加数值为 v[2] 。如果不存在，则新增
					pl.hincrBy(v[0], v[1], Integer.parseInt(v[2]));
					//指定key的过期时间
					pl.expire(v[0], Integer.parseInt(v[3]));
				} else if(v.length == 3){//数组长度为3，表示没有过期时间，目前是广告活动和推广组的汇总数据
					pl.hincrBy(v[0], v[1], Integer.parseInt(v[2]));
				} else {
					//向名称为IDS的set中添加，value=partnerid_groupid_campid_creativeid的数据。
					pl.sadd("IDS", v[0]);
				}
			}
			//将上面的操作，一次性提交
			pl.sync();
			pool.returnResource(sjedis);
		} catch (Exception e) {
			log.error("访问Redis PLHINCREBY出错key", e);
			if (sjedis != null) pool.returnBrokenResource(sjedis);
		}
	}

	protected Long hincreexby(String key, int seconds, String field, int increby) {
		return hincreexby(key, seconds, field, increby, true);
	}
	
	/**
	 * 
	 * @param key
	 * @param seconds
	 * @param field
	 * @param increby
	 * @param updateExpire					为是否更新过期时间
	 * @return
	 * @return Long
	 */
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
