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
public class FreqDataDAOImpl extends AbstractDBSelectedShardedRedisDAO
		implements FreqDataDAO {
	
	private int index;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.asme.dao.FreqDataDAO#getFreq(java.lang.String, long)
	 */
	@Override
	public Map<String, String> getFreq(String cid, long campid) {
		return hgetAll(cid + "_" + campid);
	}

	@Override
	public void increPv(String cid, long campid, int expire) {
		hincreexby(cid + "_" + campid, expire, "p", 1, false);
	}

	@Override
	public void increClick(String cid, long campid, int expire) {
		hincreexby(cid + "_" + campid, expire, "c", 1, false);
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
}
