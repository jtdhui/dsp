package com.jtd.effect.dao;
/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月12日
 * @项目名称 dsp-tracker
 * @描述 <p></p>
 */
public interface CookieMappingDAO {

	/**
	 * 双向保存cookie映射关系
	 * @param adxcid
	 * @param dspcid
	 */
	public void mapping(String adxcid, String dspcid);

	/**
	 * 双向保存cookie映射关系
	 * @param adxcid
	 * @param dspcid
	 * @param seconds
	 */
	public void mapping(String adxcid, String dspcid, int seconds);

	/**
	 * 查询
	 * 
	 * @param cid
	 * @return
	 */
	public String getCookieid(String cid);
	
	/**
	 * 向redis中写入特殊客户的cookie和想要看到的活动id
	 * @param key
	 * @param field
	 * @param value
	 */
	public void writeCustomCookie(String key, String field, String value);
	
	/**
	 * 删除key
	 * @param key
	 */
	public void delKey(String key);
	
	/**
	 * 删除key并返回受影响的行数
	 * @param key
	 */
	public long delKeyRetLong(String key);
	
}
