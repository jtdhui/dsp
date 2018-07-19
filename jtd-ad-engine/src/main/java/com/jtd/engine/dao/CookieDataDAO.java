package com.jtd.engine.dao;

import java.util.Map;

/**
 * @author asmezy
 *
 */
public interface CookieDataDAO {

	/**
	 * 保存Cookie数据
	 * 
	 * @param cid
	 * @param data key:统一人群ID value:[过期时间,权重,...]
	 */
	public void addCookieData(String cid, Map<Long, String[]> data);

	public void addCookieXtraderData(String cid, Map<String, String> data);
	/**
	 * 查询cookieid对应的cookie数据
	 * @param cid
	 * @return 统一人群ID的集合
	 */
	public Map<Long, String[]> getCookieData(String cid);
	
	/**
	 * 查询特殊人群对应的数据，集群A中数据库0中存放的是特殊人群数据，key=cookieId,value={pid,广告主id},{cid,活动id}
	 * 
	 * @param cid
	 * @return
	 */
	public Map<String,String> getCustomInfoByDspCkId(String dspCkId);
}
