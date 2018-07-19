package com.jtd.engine.dao;

import java.util.List;

import com.jtd.web.constants.CampaignAutoStatus;
import com.jtd.web.constants.CampaignManulStatus;
import com.jtd.web.model.Campaign;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月8日
 * @项目名称 dsp-engine
 * @描述 <p></p>
 */
public interface CampaignDAO {

	/**
	 * 更新投放
	 * 如果存在则更新,不存在则添加
	 * @param pub
	 * @return
	 */
	public boolean updateCampaign(Campaign campaign);

	/**
	 * 修改状态
	 * @param campid
	 * @param status
	 * @return
	 */
	public boolean changeAutoStatus(long campaignId, CampaignAutoStatus status);
	public boolean changeManulStatus(long campaignId, CampaignManulStatus status);
	
	/**
	 * 小时限制暂停
	 * @param campaignId
	 * @param hour
	 * @return
	 */
	public boolean setCampHourPause(long campaignId, int hour);
	public boolean isPause(long campaignId);

	/**
	 * 根据ID查找投放
	 * @param pub
	 * @return
	 */
	public Campaign getCampaignById(long id);

	/**
	 * 根据ID删除投放
	 * @param pub
	 * @return
	 */
	public boolean delCampaignById(long id);

	/**
	 * 获取所有的投放
	 * @return
	 */
	public List<Campaign> getAllCampaigns();

	/**
	 * 用来做均衡投放
	 * 修改价格因子
	 * @param delta
	 */
	public void changeCampPriceFactor(long campid, float factor);
	
	/**
	 * 修改活动当前实际的CTR
	 * @param campid
	 * @param ctr
	 */
	public void changeCampCtr(long campid, float ctr);

	/**
	 * @param id
	 * @return
	 */
	public float getCampPriceFactor(long id);

	/**
	 * @param id
	 * @return
	 */
	public long getCpmPrice(Campaign camp);

	/**
	 * 修改AF
	 * @param campid
	 * @return
	 */
	public int getAf(long campid);
	public void setAf(long campid, int af);
	
	public void setPartnerGrossProfit(long partnerId, int gross);
	public void setCampGrossProfit(long campid, int gross);
	public int getPartnerGrossProfit(long partnerId);
	public int getCampGrossProfit(long partnerId, long campid);
}
