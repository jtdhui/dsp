package com.jtd.effect.service;

import java.util.Map;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月12日
 * @项目名称 dsp-tracker
 * @描述 <p></p>
 */
public interface CookieDataService {

	/**
	 * TANX CM
	 * @param tid
	 * @param tver
	 * @param cid
	 */
	public void tanxCookieMapping(String tid, int tver, String cid);
	public String getCidByTanxid(String tid, int tver);
	public String getTanxidByCid(String cid);

	/**
	 * BES CM
	 * @param besid
	 * @param uid
	 */
	public void besCookieMapping(String besid, String besver, String cid);
	public String getCidByBesid(String besid, String besver);
	public String getBesidByCid(String cid);
	
	/******************Xtrader cookie操作****************/
	/**
	 * 写入dsp的cookieId和百度的用户id映射
	 */
	public void xtraderCookieMapping(String xtraderid, String cid);
	/**
	 * 根据xtrader的用户id，获取dsp的cookieId
	 */
	public String getCidByXtraderid(String xtraderid);
	/**
	 * 根据dsp的cookieId，获取dsp的cookieId
	 */
	public String getXtraderidByCid(String cid);
	
//	/*******互众cookie操作*********/
//	//双向写入互众cookieId和dsp cookieId
//	public void hzCookieMapping(String hzId,String cid);
//	//通过互众id获取dspid
//	public String getCidByHzid(String hzId);
//	//通过dspId获取互众id
//	public String getHzIdByCid(String cId);

	/**
	 * @param partner 名称不能包含_
	 * @param partnercid
	 * @param dspcid
	 */
	public void cookieMapping(String partner, String partnercid, String dspcid);
	public String getCidByPartercid(String partner, String partnercid);
	public String getPartercidByCid(String partner, String cid);

	/**
	 * 保存Cookie/设备号数据
	 * 
	 * @param cid
	 * @param data key:统一人群ID value:[过期时间,权重,...]
	 */
	public void addDataByUserId(String userId, Map<Long, String[]> data);

	/**
	 * 查询Cookie/设备号对应的数据
	 * @param cid
	 * @return 统一人群ID的集合
	 */
	public Map<Long, String[]> getDataByUserId(String userId);

	/**
	 * 保存百度的Cookie数据
	 * 
	 * @param cid
	 * @param data key:统一人群ID value:[过期时间,权重,...]
	 */
	public void addDataByBesid(String besid, String besver, Map<Long, String[]> data);

	/**
	 * 查询cookieid对应的cookie数据
	 * @param cid
	 * @return 统一人群ID的集合
	 */
	public Map<Long, String[]> getDataByBesid(String besid, String besver);
	
	/**
	 * 写入特殊数据
	 * @param key
	 * @param field
	 * @param value
	 */
	public void writeCustomCookie(String key, String field, String value);
	
	/**
	 * 删除特殊数据
	 * @param key
	 */
	public long delCustomCookie(String key);
}
