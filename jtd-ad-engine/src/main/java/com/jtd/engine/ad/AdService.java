package com.jtd.engine.ad;


import com.jtd.engine.message.v1.AVBidRequest;
import com.jtd.engine.message.v1.AVBidResponse;
import com.jtd.engine.message.v1.AdwoBidRequest;
import com.jtd.engine.message.v1.AdwoBidResponse;
import com.jtd.engine.message.v1.BesBidRequest;
import com.jtd.engine.message.v1.BesBidResponse;
import com.jtd.engine.message.v1.SystemConfigRequest;
import com.jtd.engine.message.v1.SystemConfigResponse;
import com.jtd.engine.message.v1.XHDTBidRequest;
import com.jtd.engine.message.v1.XHDTBidResponse;
import com.jtd.engine.message.v1.XTraderBidRequest;
import com.jtd.engine.message.v1.XTraderBidResponse;
import com.jtd.web.constants.CampaignAutoStatus;
import com.jtd.web.constants.CampaignManulStatus;
import com.jtd.web.constants.CatgSerial;
import com.jtd.web.model.Campaign;

/**
 * @author asme
 *
 */
public interface AdService {

	/**
	 * 更新活动
	 * 
	 */
	public boolean updateCampaign(Campaign campaign);

	/**
	 * 控制广告投放计划的状态
	 * @param campaignId
	 * @param status
	 * @return
	 */
	public boolean changeAutoStatus(long campaignId, CampaignAutoStatus status);

	/**
	 * 控制广告投放计划的状态
	 * @param campaignId
	 * @param status
	 * @return
	 */
	public boolean changeManulStatus(long campaignId, CampaignManulStatus status);

	/**
	 * 修改活动和广告的行业类别
	 * @param serial
	 * @param catgid
	 */
	public void changeCampCatg(long campid, CatgSerial serial, String catgid);
	public void changeAdCatg(long adid, CatgSerial serial, String catgid);

	/**
	 * 零集竞价
	 * 
	 * @param bidReq
	 * @return
	 */
	public XTraderBidResponse bidXTrader(XTraderBidRequest bidReq);
	
	/**
	 * 新汇达通广告竞价
	 * @param bidReq
	 * @return
	 */
	public XHDTBidResponse bidXHDT(XHDTBidRequest bidReq);
	
	/***
	 * 安沃广告竞价请求
	 * @param bidReq
	 * @return
	 */
	public AdwoBidResponse bidAdwo(AdwoBidRequest bidReq);
	
	public AVBidResponse bidAdView(AVBidRequest bidReq);
	
	/***
	 * 百度广告竞价请求
	 * @param bidReq
	 * @return
	 */

	public BesBidResponse bidBes(BesBidRequest message);
	
	/**
	 * 系统配置
	 */
	public SystemConfigResponse sysConfig(SystemConfigRequest sysConfigReq);
	
}
