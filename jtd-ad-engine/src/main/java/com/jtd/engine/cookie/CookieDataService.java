package com.jtd.engine.cookie;

import java.util.Map;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月8日
 * @项目名称 dsp-engine
 * @描述 <p></p>
 */
public interface CookieDataService {

	/**
	 * TANX CM，添加tanx和dsp的cookieid映射
	 * @param tid
	 * @param tver
	 * @param cid
	 */
	public void tanxCookieMapping(String tid, int tver, String cid);
	/**
	 * 根据tanx的cookieid获取dsp的cookieid
	 * @param tid
	 * @param tver
	 * @return
	 * @return String
	 */
	public String getCidByTanxid(String tid, int tver);
	/**
	 * 根据dsp的cookieid获取tanx的cookieid
	 * @param cid
	 * @return
	 * @return String
	 */
	public String getTanxidByCid(String cid);

	/**
	 * BES CM,添加百度和dsp的cookieid映射
	 * @param besid
	 * @param uid
	 */
	public void besCookieMapping(String besid, String besver, String cid);
	/**
	 * 根据百度id获取cookieid
	 * @param besid
	 * @param besver
	 * @return
	 * @return String
	 */
	public String getCidByBesid(String besid, String besver);
	/**
	 * 根据dsp的cookieid获取百度的id
	 * @param cid
	 * @return
	 * @return String
	 */
	public String getBesidByCid(String cid);
	
	public void xtraderCookieMapping(String xid, String cid);
	
	public String getCidByXtraderid(String xid);
	
	public String getXtraderidByCid(String cid);
	
	public void addDataByXtraderUserId(String xid, Map<String, String> data);

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
	 * 根据dspCookie 获取特殊用户数据
	 * @param dspCkId
	 * @return
	 */
	public Map<String,String> getCustomInfo(String dspCkId);
}
