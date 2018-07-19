package com.jtd.engine.dao;


/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月8日
 * @项目名称 dsp-engine
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
}
