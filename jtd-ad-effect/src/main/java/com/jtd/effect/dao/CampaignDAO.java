package com.jtd.effect.dao;

import com.jtd.web.constants.CampaignAutoStatus;
import com.jtd.web.constants.CampaignManulStatus;
import com.jtd.web.model.Campaign;

import java.util.List;


/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月12日
 * @项目名称 dsp-tracker
 * @描述 <p></p>
 */
public interface CampaignDAO {

	/**
	 * 更新投放
	 * 如果存在则更新,不存在则添加
	 * @param campaign
	 * @return
	 */
	public boolean updateCampaign(Campaign campaign);

	/**
	 * 修改状态
	 * @param campaignId
	 * @param status
	 * @return
	 */
	public boolean changeAutoStatus(long campaignId, CampaignAutoStatus status);
	public boolean changeManulStatus(long campaignId, CampaignManulStatus status);

	/**
	 * 根据ID查找投放
	 * @param id
	 * @return
	 */
	public Campaign getCampaignById(long id);
	
	/**
	 * 
	 * @param campid
	 * @return [1 pv 2 click 3 pv和click,  小时]
	 */
	public int[] getCampFreqType(long campid);

	/**
	 * 根据ID删除投放
	 * @param id
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
	 * @param campid
	 */
	public void changeCampPriceFactor(long campid, float factor);

	/**
	 * @param id
	 * @return
	 */
	public float getCampPriceFactor(long id);

	/**
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
