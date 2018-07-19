package com.jtd.engine.dao;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jtd.engine.ad.em.Adx;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月8日
 * @项目名称 dsp-engine
 * @描述 <p>验证广告主和广告是否通过内部和adx审核</p>
 */
public class AuditInfoDAOImpl extends AbstractRedisDAO implements AuditInfoDAO {

	private static final Log log = LogFactory.getLog(CampaignDAOImpl.class);
	
	private static final byte[] B0 = new byte[0];
	
	//dsp拒绝的，广告key 前缀
	private static final String DSP_AD_PREFIX = "DA_";
	//dsp拒绝的，广告主key 前缀
	private static final String DSP_PARTNER_PREFIX = "DP_";
	//adx拒绝的，广告key 前缀
	private static final String ADX_AD_PREFIX = "A_";
	//adx拒绝的，广告主key 前缀
	private static final String ADX_PARTNER_PREFIX = "AP_";
	//adx提交的广告主key 前缀
	private static final String ADX_COMMIT_PARTNER_PREFIX = "ACP_";
	//adx 提交的广告key 前缀
	private static final String ADX_COMMIT_AD_PREFIX = "ACA_";
	//开启的广告主前缀
	private static final String PARTNER_PREFIX = "PRT_";
	
	//存放dsp拒绝的广告的集合
	private ConcurrentHashMap<Long, Object> dspRefuseAd = new ConcurrentHashMap<Long, Object>();
	//存放dsp拒绝的广告主的集合
	private ConcurrentHashMap<Long, Object> dspRefusePartner = new ConcurrentHashMap<Long, Object>();
	//存放adx审核拒绝的广告
	private ConcurrentHashMap<String, Object> adxRefuseAd = new ConcurrentHashMap<String, Object>();
	//存放adx审核拒绝的广告主
	private ConcurrentHashMap<String, Object> adxRefusePartner = new ConcurrentHashMap<String, Object>();
	//存放提交adx提交审核的广告主
	private ConcurrentHashMap<String, Object> adxCommitPartner = new ConcurrentHashMap<String, Object>();
	//存放提交adx提交审核的广告
	private ConcurrentHashMap<String, Object> adxCommitAd = new ConcurrentHashMap<String, Object>();
	//存放开启的广告主
	private ConcurrentHashMap<Long, Object> startedPartner = new ConcurrentHashMap<Long, Object>();

	/**
	 * 初始化
	 */
	public void init() {
		super.init();
		//取出所有开启的广告主帮到startedPartner ConcurrentHashMap中
		Set<String> keys = keys(PARTNER_PREFIX + "*");
		for (String key : keys) {
			//去掉key的前缀，使用partentid为key，value为btye数组
			startedPartner.put(Long.parseLong(key.substring(PARTNER_PREFIX.length())), B0);
		}
		log.info("已经开启了" + startedPartner.size() + "个广告主");
		
		//取出所有已经提交审核的广告主数据放到 adxCommitPartner ConcurrentHashMap中
		keys = keys(ADX_COMMIT_PARTNER_PREFIX + "*");
		for (String key : keys) {
			adxCommitPartner.put(key.substring(ADX_COMMIT_PARTNER_PREFIX.length()), B0);
		}
		log.info("已经提交了" + adxCommitPartner.size() + "个广告主");
		
		//取出所有已经提交审核的广告数据放到 adxCommitAd 的 ConcurrentHashMap中
		keys = keys(ADX_COMMIT_AD_PREFIX + "*");
		for (String key : keys) {
			adxCommitAd.put(key.substring(ADX_COMMIT_AD_PREFIX.length()), B0);
		}
		log.info("已经提交了" + adxCommitAd.size() + "个广告");
		//取出所有dsp拒绝的广告
		keys = keys(DSP_AD_PREFIX + "*");
		for (String key : keys) {
			dspRefuseAd.put(Long.parseLong(key.substring(DSP_AD_PREFIX.length())), B0);
		}
		log.info("DSP拒绝了" + dspRefuseAd.size() + "个广告");
		//取出dsp拒绝的广告主
		keys = keys(DSP_PARTNER_PREFIX + "*");
		for (String key : keys) {
			dspRefusePartner.put(Long.parseLong(key.substring(DSP_PARTNER_PREFIX.length())), B0);
		}
		log.info("DSP拒绝了" + dspRefusePartner.size() + "个广告主");
		//取出adx拒绝的广告
		keys = keys(ADX_AD_PREFIX + "*");
		for (String key : keys) {
			adxRefuseAd.put(key.substring(ADX_AD_PREFIX.length()), B0);
		}
		log.info("ADX拒绝了" + adxRefuseAd.size() + "个广告");
		//取出adx拒绝的广告主
		keys = keys(ADX_PARTNER_PREFIX + "*");
		for (String key : keys) {
			adxRefusePartner.put(key.substring(ADX_PARTNER_PREFIX.length()), B0);
		}
		log.info("ADX拒绝了" + adxRefusePartner.size() + "个广告主");
	}
	
	/**
	 * 向redis和本地缓存中，添加已经开启的广告主id
	 */
	@Override
	public void startPartner(long partnerid) {
		startedPartner.put(partnerid, B0);
		set(PARTNER_PREFIX + partnerid, "");
	}
	/**
	 * 从redis和本地缓存中移除广告主
	 */
	@Override
	public void stopPartner(long partnerid) {
		startedPartner.remove(partnerid);
		del(PARTNER_PREFIX + partnerid);
	}
	
	/**
	 * 判断广告主是否已经开启
	 */
	@Override
	public boolean isPartnerStarted(long partnerid) {
		return startedPartner.containsKey(partnerid);
	}
	/**
	 * 向redis和本地缓存中添加dsp拒绝审核的广告
	 */
	@Override
	public void refuseAd(long creativeid) {
		dspRefuseAd.put(creativeid, B0);
		set(DSP_AD_PREFIX + creativeid, "");
	}
	/**
	 * 删除reids和本地缓存中dsp拒绝审核的广告
	 */
	@Override
	public void passAd(long creativeid) {
		dspRefuseAd.remove(creativeid);
		del(DSP_AD_PREFIX + creativeid);
	}
	
	/**
	 * 向redis中和本地缓存中添加adx审核拒绝的广告
	 */
	@Override
	public void refuseAd(Adx adx, long adid) {
		adxRefuseAd.put(adx.channelId() + "_" + adid, B0);
		set(ADX_AD_PREFIX + adx.channelId() + "_" + adid, "");
	}
	/**
	 * 从redis和本地缓存删除adx拒绝审核的广告
	 */
	@Override
	public void passAd(Adx adx, long adid) {
		adxRefuseAd.remove(adx.channelId() + "_" + adid);
		del(ADX_AD_PREFIX + adx.channelId() + "_" + adid);
	}
	/**
	 * 向redis和本地缓存中添加dsp审核拒绝的广告主
	 */
	@Override
	public void refusePartner(long partnerid) {
		dspRefusePartner.put(partnerid, B0);
		set(DSP_PARTNER_PREFIX + partnerid, "");
	}
	/**
	 * 从redis和本地缓存中删除dsp审核通过的广告主
	 */
	@Override
	public void passPartner(long partnerid) {
		dspRefusePartner.remove(partnerid);
		del(DSP_PARTNER_PREFIX + partnerid);
	}
	/**
	 * 向redis和本地缓存中添加adx审核拒绝的广告主
	 */
	@Override
	public void refusePartner(Adx adx, long partnerid) {
		adxRefusePartner.put(adx.channelId() + "_" + partnerid, B0);
		set(ADX_PARTNER_PREFIX + adx.channelId() + "_" + partnerid, "");
	}
	/**
	 * 从redis和本地缓存中删除adx审核通过的广告主。
	 * 从拒绝审核的redis和本地缓存中删除
	 */
	@Override
	public void passPartner(Adx adx, long partnerid) {
		adxRefuseAd.remove(adx.channelId() + "_" + partnerid);
		del(ADX_PARTNER_PREFIX + adx.channelId() + "_" + partnerid);
	}
	/**
	 * 验证广告是否审核未通过
	 */
	@Override
	public boolean isRefusedAd(long creativeid) {
		return dspRefuseAd.containsKey(creativeid);
	}
	/**
	 * 验证广告主是否审核未通过
	 */
	@Override
	public boolean isRefusedPartner(long partnerid) {
		return dspRefusePartner.containsKey(partnerid);
	}
	/**
	 * 验证广告是否adx审核未通过
	 */
	@Override
	public boolean isRefusedAd(Adx adx, long adid) {
		return adxRefuseAd.containsKey(adx.channelId() + "_" + adid);
	}
	/**
	 * 验证广告主是否adx审核未通过
	 */
	@Override
	public boolean isRefusedPartner(Adx adx, long partnerid) {
		return adxRefusePartner.containsKey(adx.channelId() + "_" + partnerid);
	}

	/**
	 * 向redis和本地缓存中添加，已经提交adx审核的广告主
	 */
	@Override
	public void commitPartner(Adx adx, long partnerid) {
		adxCommitPartner.put(adx.channelId() + "_" + partnerid, B0);
		set(ADX_COMMIT_PARTNER_PREFIX + adx.channelId() + "_" + partnerid, "");
	}
	/**
	 * 验证广告主是否提交adx审核
	 */
	@Override
	public boolean iscommittedPartner(Adx adx, long partnerid) {
		return adxCommitPartner.containsKey(adx.channelId() + "_" + partnerid);
	}
	/**
	 * 向redis和本地缓存中添加，提交adx审核的广告
	 */
	@Override
	public void commitAd(Adx adx, long adid) {
		adxCommitAd.put(adx.channelId() + "_" + adid, B0);
		set(ADX_COMMIT_AD_PREFIX + adx.channelId() + "_" + adid, "");
	}
	/**
	 * 验证广告是否已经提交adx审核
	 */
	@Override
	public boolean iscommittedAd(Adx adx, long adid) {
		return adxCommitAd.containsKey(adx.channelId() + "_" + adid);
	}
}
