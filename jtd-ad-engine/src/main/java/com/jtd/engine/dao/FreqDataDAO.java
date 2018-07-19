package com.jtd.engine.dao;

import java.util.Map;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月8日
 * @项目名称 dsp-engine
 * @描述 <p></p>
 */
public interface FreqDataDAO {

	/**
	 * 查询频次
	 * @param cid
	 * @param campid
	 * @return
	 */
	public Map<String, String> getFreq(String cid, long campid);

	public void increPv(String cid, long campid, int expire);
	public void increClick(String cid, long campid, int expire);
}
