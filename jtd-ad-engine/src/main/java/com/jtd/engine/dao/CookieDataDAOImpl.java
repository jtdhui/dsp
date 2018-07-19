package com.jtd.engine.dao;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.alibaba.fastjson.JSON;
import com.jtd.engine.utils.SystemTime;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月8日
 * @项目名称 dsp-engine
 * @描述 <p></p>
 */
public class CookieDataDAOImpl extends AbstractDBSelectedShardedRedisDAO
		implements CookieDataDAO {
	
	// 缺省60天过期
	private static final int DEFAULT_EXPIRE = 60 * 60 * 24 * 60;

	// 过期时间和系统时间工具
	private int expire = DEFAULT_EXPIRE;
	private SystemTime systemTime;
	
	private int index;
	
	/**
	 * 保存Cookie数据
	 * 
	 * @param cid
	 * @param data key:统一人群ID value:[过期时间,权重,...]
	 */
	/* (non-Javadoc)
	 * @see com.asme.dao.CookieDataDAO#addCookieData(java.lang.String, java.util.Map)
	 */
	@Override
	public void addCookieData(String cid, Map<Long, String[]> data) {
		for (Iterator<Entry<Long, String[]>> it = data.entrySet().iterator(); it.hasNext();) {
			Entry<Long, String[]> e = it.next();
			hsetex(cid, expire, String.valueOf(e.getKey()), JSON.toJSONString(e.getValue()));
		}
	}
	
	@Override
	public void addCookieXtraderData(String cid, Map<String, String> data) {
		for (Iterator<Entry<String, String>> it = data.entrySet().iterator(); it.hasNext();) {
			Entry<String, String> e = it.next();
			hsetex(cid, expire, String.valueOf(e.getKey()), JSON.toJSONString(e.getValue()));
		}
	}
	
	/**
	 * 查询cookieid对应的cookie数据
	 * @param cid
	 * @return 统一人群ID的集合
	 * 返回值Map<Long, String[]>，key是用户的分类id，value是存放内容的字符串数据组，目前看只有过期时间。
	 */
	/* (non-Javadoc)
	 * @see com.asme.dao.CookieDataDAO#getCookieData(java.lang.String)
	 */
	@Override
	public Map<Long, String[]> getCookieData(String cid) {
		long now = systemTime.getTime();
		Map<Long, String[]> ret = new TreeMap<Long, String[]>();
		Map<String, String> data = hgetAll(cid);
		for (Iterator<Entry<String, String>> it = data.entrySet().iterator(); it.hasNext();) {
			Entry<String, String> e = it.next();
			Long ckgroupid = Long.parseLong(e.getKey());
			String[] attr = JSON.parseObject(e.getValue(), String[].class);
			long exp = Long.parseLong(attr[0]);

			// 过期的直接跳过，不去删除了，删除操作留给CookieMapping的时候做
			if(exp < now) continue;
			ret.put(ckgroupid, attr);
		}
		return ret;
	}

	
	/**
	 * 从集群A，数据库0中获取特殊人群数据
	 */
	@Override
	public Map<String, String> getCustomInfoByDspCkId(String dspCkId) {
		return hgetAll(dspCkId);
	}

	/**
	 * @param expire the expire to set
	 */
	public void setExpire(int expire) {
		this.expire = expire;
	}

	/**
	 * @param systemTime the systemTime to set
	 */
	public void setSystemTime(SystemTime systemTime) {
		this.systemTime = systemTime;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	@Override
	protected int getIndex() {
		return index;
	}
}
