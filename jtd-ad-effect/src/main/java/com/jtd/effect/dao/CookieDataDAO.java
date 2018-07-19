package com.jtd.effect.dao;

import java.util.Map;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月12日
 * @项目名称 dsp-tracker
 * @描述 <p></p>
 */
public interface CookieDataDAO {

	/**
	 * 保存Cookie数据
	 * 
	 * @param cid
	 * @param data key:统一人群ID value:[过期时间,权重,...]
	 */
	public void addCookieData(String cid, Map<Long, String[]> data);

	/**
	 * 查询cookieid对应的cookie数据
	 * @param cid
	 * @return 统一人群ID的集合
	 */
	public Map<Long, String[]> getCookieData(String cid);
}
