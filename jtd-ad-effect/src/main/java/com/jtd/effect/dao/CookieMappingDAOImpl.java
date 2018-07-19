package com.jtd.effect.dao;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月12日
 * @项目名称 dsp-tracker
 * @描述 <p></p>
 */
public class CookieMappingDAOImpl extends AbstractDBSelectedShardedRedisDAO
		implements CookieMappingDAO {

	// 缺省60天过期
	private static final int DEFAULT_EXPIRE = 60 * 60 * 24 * 60;

	// 过期时间和系统时间工具
	private int expire = DEFAULT_EXPIRE;

	/**
	 * 写入adx cookieId和dsp cookieid映射
	 */
	@Override
	public void mapping(String adxcid, String dspcid) {
		mapping(adxcid, dspcid, expire);
	}
	@Override
	public void mapping(String adxcid, String dspcid, int seconds) {
		doubleSetex(adxcid, dspcid, seconds);
	}

	/**
	 * 根据id获取值
	 */
	@Override
	public String getCookieid(String cid) {
		return get(cid);
	}

	/**
	 * @param expire
	 *            the expire to set
	 */
	public void setExpire(int expire) {
		this.expire = expire;
	}
	
	public void writeCustomCookie(String key,String field, String value){
		hset(key,field,value);
	}
	
	public void delKey(String key){
		rem(key);
	}
	
	/**
	 * 删除key并返回受影响的行数
	 * @param key
	 */
	public long delKeyRetLong(String key){
		return remRetLong(key);
	}
}
