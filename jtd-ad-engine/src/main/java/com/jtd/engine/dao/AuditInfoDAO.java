package com.jtd.engine.dao;

import com.jtd.engine.ad.em.Adx;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月8日
 * @项目名称 dsp-engine
 * @描述 <p></p>
 */
public interface AuditInfoDAO {

	/**
	 * 开启和暂停广告主
	 * 
	 * @param partnerid
	 */
	public void startPartner(long partnerid);
	public void stopPartner(long partnerid);
	public boolean isPartnerStarted(long partnerid);

	/**
	 * DSP拒绝和通过广告素材
	 * 
	 * @param adx
	 * @param creativeid
	 */
	public void refuseAd(long creativeid);
	public void passAd(long creativeid);

	/**
	 * ADX拒绝和通过广告
	 * 
	 * @param adx
	 * @param adid
	 */
	public void refuseAd(Adx adx, long adid);
	public void passAd(Adx adx, long adid);

	/**
	 * DSP拒绝和通过广告主
	 * 
	 * @param adx
	 * @param partnerid
	 */
	public void refusePartner(long partnerid);
	public void passPartner(long partnerid);
	
	/**
	 * 向ADX提交广告主
	 * @param adx
	 * @param partnerid
	 */
	public void commitPartner(Adx adx, long partnerid);
	public boolean iscommittedPartner(Adx adx, long partnerid);
	
	/**
	 * 向ADX提交广告主
	 * @param adx
	 * @param partnerid
	 */
	public void commitAd(Adx adx, long adid);
	public boolean iscommittedAd(Adx adx, long adid);

	/**
	 * ADX拒绝和通过广告主
	 * 
	 * @param adx
	 * @param partnerid
	 */
	public void refusePartner(Adx adx, long partnerid);
	public void passPartner(Adx adx, long partnerid);

	/**
	 * 查询DSP状态
	 * 
	 * @param adx
	 * @param partnerid
	 * @return
	 */
	public boolean isRefusedAd(long creativeid);
	public boolean isRefusedPartner(long partnerid);

	/**
	 * 查询ADX状态
	 * 
	 * @param adx
	 * @param partnerid
	 * @return
	 */
	public boolean isRefusedAd(Adx adx, long adid);
	public boolean isRefusedPartner(Adx adx, long partnerid);
}
