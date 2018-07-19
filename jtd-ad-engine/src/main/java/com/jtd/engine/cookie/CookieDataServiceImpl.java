package com.jtd.engine.cookie;

import java.util.Map;

import com.jtd.engine.dao.CookieDataDAO;
import com.jtd.engine.dao.CookieMappingDAO;


/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置
 * @创建日期 2016年9月8日
 * @项目名称 dsp-engine
 * @描述
 *     <p>
 *     </p>
 */
public class CookieDataServiceImpl implements CookieDataService {

	private CookieMappingDAO tanxCookieMappingDAO;
	private CookieMappingDAO besCookieMappingDAO;
	private CookieMappingDAO cookieMappingDAO;
	private CookieMappingDAO xtraderCookieMappingDAO;
	
	private CookieDataDAO cookieDataDAO;
	private CookieDataDAO besCookieDataDAO;
	//特殊人群数据访问dao
	private CookieDataDAO customCookieDAO;

//	private CookieDataDAO xtraderCookieDataDAO;
	
	

	// ---------------------------tanx 和 dsp
	// cookieid映射的方法-----------------------------
	/**
	 * TANX CM，添加tanx和dsp的cookieid映射
	 * 
	 * @param tid
	 * @param tver
	 * @param cid
	 */
	@Override
	public void tanxCookieMapping(String tid, int tver, String cid) {
		tanxCookieMappingDAO.mapping(tid + "_" + tver, cid);
	}

	/**
	 * 根据tanx的cookieid获取dsp的cookieid
	 * 
	 * @param tid
	 * @param tver
	 * @return
	 * @return String
	 */
	@Override
	public String getCidByTanxid(String tid, int tver) {
		if (tid == null)
			return null;
		return tanxCookieMappingDAO.getCookieid(tid + "_" + tver);
	}

	/**
	 * 根据dsp的cookieid获取tanx的cookieid
	 * 
	 * @param cid
	 * @return
	 * @return String
	 */
	@Override
	public String getTanxidByCid(String cid) {
		return tanxCookieMappingDAO.getCookieid(cid);
	}

	// ------------------------百度 和 dsp
	// cookieid映射的方法-----------------------------
	/**
	 * BES CM,添加百度和dsp的cookieid映射
	 * 
	 * @param besid
	 * @param uid
	 */
	@Override
	public void besCookieMapping(String besid, String besver, String cid) {
		besCookieMappingDAO.mapping(besid + "_" + besver, cid);
	}

	/**
	 * 根据百度id获取cookieid
	 * 
	 * @param besid
	 * @param besver
	 * @return
	 * @return String
	 */
	@Override
	public String getCidByBesid(String besid, String besver) {
		if (besid == null)
			return null;
		return besCookieMappingDAO.getCookieid(besid + "_" + besver);
	}

	/**
	 * 根据dsp的cookieid获取百度的id
	 * 
	 * @param cid
	 * @return
	 * @return String
	 */
	@Override
	public String getBesidByCid(String cid) {
		return besCookieMappingDAO.getCookieid(cid);
	}

	// ------------------------零集 和 dsp
	// cookieid映射的方法-----------------------------

	@Override
	public void addDataByXtraderUserId(String xid, Map<String, String> data) {
		cookieDataDAO.addCookieXtraderData(xid, data);
	}

	/**
		 * xtrander CM，添加xtrander和dsp的cookieid映射
		 * @param tid
		 * @param tver
		 * @param cid
		 */
		@Override
		public void xtraderCookieMapping(String xid, String cid) {
			xtraderCookieMappingDAO.mapping(xid , cid);
		}

	/**
	 * 根据xtrader的cookieid获取dsp的cookieid
	 * 
	 * @param xid
	 * @param tver
	 * @return
	 * @return String
	 */
	@Override
	public String getCidByXtraderid(String xid) {
		 if(xid == null) return null;
		 return xtraderCookieMappingDAO.getCookieid(xid);
	}

	/**
	 * 根据dsp的cookieid获取xtrader的cookieid
	 * 
	 * @param cid
	 * @return
	 * @return String
	 */
	@Override
	public String getXtraderidByCid(String cid) {
		return xtraderCookieMappingDAO.getCookieid(cid);
	}

	/**
	 * 向缓存集群A中写入adx和dsp cookie映射
	 * @param partner  				这里为channelId，比如：String.valueOf(Adx.VAM.channelId())
	 * @param partnercid			adxCkId和adx cookieid 的版本号连在一起的字符串，例如:  adxCkId_2
	 * @param dspcid				dspCkId
	 */
	@Override
	public void cookieMapping(String partner, String partnercid, String dspcid) {
		cookieMappingDAO.mapping(partner + "_" + partnercid, partner + "_" + dspcid);
	}
	
	/**
	 * 根据adxid获取dspCkId
	 * @param partner 				这里为channelId，比如：String.valueOf(Adx.VAM.channelId())
	 * @param partnercid 			adxCkId和adx cookieid 的版本号连在一起的字符串，例如:  adxCkId_2
	 */
	@Override
	public String getCidByPartercid(String partner, String partnercid) {
		String v = cookieMappingDAO.getCookieid(partner + "_" + partnercid);
		if (v == null)
			return null;
		int i = v.indexOf("_");
		if (i == -1)
			return null;
		return v.substring(i + 1);
	}
	
	/**
	 * 根据dspCkId获取，adx cookie id
	 * @param partner 				这里为channelId，比如：String.valueOf(Adx.VAM.channelId())
	 * @param cid					dsp cookie id
	 */
	@Override
	public String getPartercidByCid(String partner, String cid) {
		String v = cookieMappingDAO.getCookieid(partner + "_" + cid);
		int i = v.indexOf("_");
		if (i == -1)
			return null;
		return v.substring(i + 1);
	}

	/**
	 * 保存Cookie/设备号数据
	 * 
	 * @param cid
	 * @param data
	 *            key:统一人群ID value:[过期时间,权重,...]
	 */
	@Override
	public void addDataByUserId(String cid, Map<Long, String[]> data) {
		cookieDataDAO.addCookieData(cid, data);
	}

	/**
	 * 根据cookieid获取用户数据
	 */
	@Override
	public Map<Long, String[]> getDataByUserId(String cid) {
		return cookieDataDAO.getCookieData(cid);
	}

	/**
	 * 添加百度的用户信息
	 */
	@Override
	public void addDataByBesid(String besid, String besver, Map<Long, String[]> data) {
		besCookieDataDAO.addCookieData(besid + "_" + besver, data);
		String cid = besCookieMappingDAO.getCookieid(besid + "_" + besver);
		if (cid != null)
			cookieDataDAO.addCookieData(cid, data);
	}

	/**
	 * 根据cookieid获取百度的用户数据
	 */
	@Override
	public Map<Long, String[]> getDataByBesid(String besid, String besver) {
		return besCookieDataDAO.getCookieData(besid + "_" + besver);
	}
	
	

	@Override
	public Map<String, String> getCustomInfo(String dspCkId) {
		return customCookieDAO.getCustomInfoByDspCkId(dspCkId);
	}

	/**
	 * @return the tanxCookieMappingDAO
	 */
	public CookieMappingDAO getTanxCookieMappingDAO() {
		return tanxCookieMappingDAO;
	}

	/**
	 * @param tanxCookieMappingDAO
	 *            the tanxCookieMappingDAO to set
	 */
	public void setTanxCookieMappingDAO(CookieMappingDAO tanxCookieMappingDAO) {
		this.tanxCookieMappingDAO = tanxCookieMappingDAO;
	}

	/**
	 * @return the besCookieMappingDAO
	 */
	public CookieMappingDAO getBesCookieMappingDAO() {
		return besCookieMappingDAO;
	}

	/**
	 * @param besCookieMappingDAO
	 *            the besCookieMappingDAO to set
	 */
	public void setBesCookieMappingDAO(CookieMappingDAO besCookieMappingDAO) {
		this.besCookieMappingDAO = besCookieMappingDAO;
	}

	/**
	 * @return the cookieMappingDAO
	 */
	public CookieMappingDAO getCookieMappingDAO() {
		return cookieMappingDAO;
	}

	/**
	 * @param cookieMappingDAO
	 *            the cookieMappingDAO to set
	 */
	public void setCookieMappingDAO(CookieMappingDAO cookieMappingDAO) {
		this.cookieMappingDAO = cookieMappingDAO;
	}

	/**
	 * @return the cookieDataDAO
	 */
	public CookieDataDAO getCookieDataDAO() {
		return cookieDataDAO;
	}

	/**
	 * @param cookieDataDAO
	 *            the cookieDataDAO to set
	 */
	public void setCookieDataDAO(CookieDataDAO cookieDataDAO) {
		this.cookieDataDAO = cookieDataDAO;
	}

	/**
	 * @return the besCookieDataDAO
	 */
	public CookieDataDAO getBesCookieDataDAO() {
		return besCookieDataDAO;
	}

	/**
	 * @param besCookieDataDAO
	 *            the besCookieDataDAO to set
	 */
	public void setBesCookieDataDAO(CookieDataDAO besCookieDataDAO) {
		this.besCookieDataDAO = besCookieDataDAO;
	}

	public CookieMappingDAO getXtraderCookieMappingDAO() {
		return xtraderCookieMappingDAO;
	}

	public void setXtraderCookieMappingDAO(CookieMappingDAO xtraderCookieMappingDAO) {
		this.xtraderCookieMappingDAO = xtraderCookieMappingDAO;
	}

	public CookieDataDAO getCustomCookieDAO() {
		return customCookieDAO;
	}

	public void setCustomCookieDAO(CookieDataDAO customCookieDAO) {
		this.customCookieDAO = customCookieDAO;
	}
	
	
	
}
