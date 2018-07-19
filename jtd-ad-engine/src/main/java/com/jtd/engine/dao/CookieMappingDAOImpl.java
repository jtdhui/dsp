package com.jtd.engine.dao;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月8日
 * @项目名称 dsp-engine
 * @描述 <p></p>
 */
public class CookieMappingDAOImpl extends AbstractDBSelectedShardedRedisDAO
		implements CookieMappingDAO {

	// 缺省60天过期
	private static final int DEFAULT_EXPIRE = 60 * 60 * 24 * 60;

	// 过期时间和系统时间工具
	private int expire = DEFAULT_EXPIRE;
	
	private int index;

	/**
	 * 双向保存cookie映射关系
	 * @param adxcid
	 * @param dspcid
	 */
	@Override
	public void mapping(String adxcid, String dspcid) {
		mapping(adxcid, dspcid, expire);
	}

	/**
	 * 双向保存cookie映射关系
	 * @param adxcid
	 * @param dspcid
	 * @param seconds
	 */
	@Override
	public void mapping(String adxcid, String dspcid, int seconds) {
		doubleSetex(adxcid, dspcid, seconds);
	}

	/**
	 * 根据cookieid，查询数据
	 * 
	 * @param cid
	 * @return
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

	@Override
	protected int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
}
