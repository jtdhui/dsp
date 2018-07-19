package com.jtd.effect.service;

import com.jtd.effect.dao.CookieDataDAO;
import com.jtd.effect.dao.CookieMappingDAO;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月12日
 * @项目名称 dsp-tracker
 * @描述 <p></p>
 */
@Component
public class CookieDataServiceImpl implements CookieDataService {

	private CookieMappingDAO tanxCookieMappingDAO;
	private CookieMappingDAO besCookieMappingDAO;
	private CookieMappingDAO cookieMappingDAO;
	private CookieMappingDAO xtraderCookieMappingDAO;
	private CookieMappingDAO customCookieMappingDAO;

	private CookieDataDAO cookieDataDAO;
	private CookieDataDAO besCookieDataDAO;
	
	
	//---------------------tanx映射相关
	/**
	 * 写入dsp和tanx的cookie映射
	 */
	@Override
	public void tanxCookieMapping(String tid, int tver, String cid) {
		tanxCookieMappingDAO.mapping(tid + "_" + tver, cid);
	}
	/**
	 * 根据tanx的用户id和id版本，获取dsp的cookieId
	 */
	@Override
	public String getCidByTanxid(String tid, int tver) {
		if(tid == null) return null;
		return tanxCookieMappingDAO.getCookieid(tid + "_" + tver);
	}
	/**
	 * 根据dsp的cookieId获取tanx的用户id
	 */
	@Override
	public String getTanxidByCid(String cid) {
		return tanxCookieMappingDAO.getCookieid(cid);
	}
	
	//------------------百度与映射相关
	/**
	 * 写入dsp的cookieId和百度的用户id映射
	 */
	@Override
	public void besCookieMapping(String besid, String besver, String cid) {
		besCookieMappingDAO.mapping(besid + "_" + besver, cid);
	}
	/**
	 * 根据百度的用户id和id版本，获取dsp的cookieId
	 */
	@Override
	public String getCidByBesid(String besid, String besver) {
		if(besid == null) return null;
		return besCookieMappingDAO.getCookieid(besid + "_" + besver);
	}
	/**
	 * 根据dsp的cookieId，获取dsp的cookieId
	 */
	@Override
	public String getBesidByCid(String cid) {
		return besCookieMappingDAO.getCookieid(cid);
	}
	
	//------------------xtrader与映射相关
	/**
	 * 写入dsp的cookieId和百度的用户id映射
	 */
	@Override
	public void xtraderCookieMapping(String xtraderid, String cid) {
		xtraderCookieMappingDAO.mapping(xtraderid, cid);
	}
	/**
	 * 根据xtrader的用户id，获取dsp的cookieId
	 */
	@Override
	public String getCidByXtraderid(String xtraderid) {
		if(xtraderid == null) return null;
		return xtraderCookieMappingDAO.getCookieid(xtraderid);
	}
	/**
	 * 根据dsp的cookieId，获取dsp的cookieId
	 */
	@Override
	public String getXtraderidByCid(String cid) {
		return xtraderCookieMappingDAO.getCookieid(cid);
	}
	
	
//	//互众cookie操作
//	@Override
//	public void hzCookieMapping(String hzId, String cid) {
//		
//	}
//	@Override
//	public String getCidByHzid(String hzId) {
//		
//		return null;
//	}
//	@Override
//	public String getHzIdByCid(String cId) {
//		
//		return null;
//	}
	
	//----------------------其它adx用户id和dsp的cookieId映射，目前这里只有vam一家
	/**
	 * 写入adx和dsp cookieId映射
	 */
	@Override
	public void cookieMapping(String partner, String partnercid, String dspcid) {
		cookieMappingDAO.mapping(partner + "_" + partnercid, partner + "_" + dspcid);
	}

	/**
	 * 根据adxId(这里的adxId是dsp自定义的常量),和adx的用户id，获取dsp的cookieid
	 */
	@Override
	public String getCidByPartercid(String partner, String partnercid) {
		String v = cookieMappingDAO.getCookieid(partner + "_" + partnercid);
		//由于这里存储的dsp的cookieId格式是 adxId_dspCookieId，所以得到dsp的id后，要使用 "_" 分割，取后半部分。
		if(v == null) return null;
		int i = v.indexOf("_");
		if(i == -1) return null;
		return v.substring(i + 1);
	}

	/**
	 * 根据adxId和dsp的cookieid，获取adx的用户id
	 */
	@Override
	public String getPartercidByCid(String partner, String cid) {
		String v = cookieMappingDAO.getCookieid(partner + "_" + cid);
		int i = v.indexOf("_");
		if(i == -1) return null;
		return v.substring(i + 1);
	}

	/**
	 * 添加用户数据
	 */
	@Override
	public void addDataByUserId(String cid, Map<Long, String[]> data) {
		cookieDataDAO.addCookieData(cid, data);
	}

	/**
	 * 根据cookieId获取用户数据
	 */
	@Override
	public Map<Long, String[]> getDataByUserId(String cid) {
		return cookieDataDAO.getCookieData(cid);
	}

	/**
	 * 添加百度的用户数据
	 */
	@Override
	public void addDataByBesid(String besid, String besver, Map<Long, String[]> data) {
		besCookieDataDAO.addCookieData(besid + "_" + besver, data);
		String cid = besCookieMappingDAO.getCookieid(besid + "_" + besver);
		if(cid != null) cookieDataDAO.addCookieData(cid, data);
	}

	/**
	 * 获取百度的用户数据
	 */
	@Override
	public Map<Long, String[]> getDataByBesid(String besid, String besver) {
		return besCookieDataDAO.getCookieData(besid + "_" + besver);
	}

	/**
	 * @return the tanxCookieMappingDAO
	 */
	public CookieMappingDAO getTanxCookieMappingDAO() {
		return tanxCookieMappingDAO;
	}

	/**
	 * @param tanxCookieMappingDAO the tanxCookieMappingDAO to set
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
	 * @param besCookieMappingDAO the besCookieMappingDAO to set
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
	 * @param cookieMappingDAO the cookieMappingDAO to set
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
	 * @param cookieDataDAO the cookieDataDAO to set
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
	 * @param besCookieDataDAO the besCookieDataDAO to set
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
	
	
	public CookieMappingDAO getCustomCookieMappingDAO() {
		return customCookieMappingDAO;
	}
	public void setCustomCookieMappingDAO(CookieMappingDAO customCookieMappingDAO) {
		this.customCookieMappingDAO = customCookieMappingDAO;
	}
	/**
	 * 写入特殊数据
	 * @param key
	 * @param field
	 * @param value
	 */
	public void writeCustomCookie(String key,String field,String value){
		customCookieMappingDAO.writeCustomCookie(key, field, value);
	}
	/**
	 * 删除特殊数据
	 */
	public long delCustomCookie(String key){
		return customCookieMappingDAO.delKeyRetLong(key);
	}
}
