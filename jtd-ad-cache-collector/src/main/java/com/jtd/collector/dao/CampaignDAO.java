package com.jtd.collector.dao;

import com.jtd.web.constants.CampaignAutoStatus;
import com.jtd.web.constants.CampaignManulStatus;
import com.jtd.web.model.Campaign;

import java.util.List;



/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月12日
 * @项目名称 dsp-cache-collector
 * @描述 <p></p>
 */
public interface CampaignDAO {

	/**
	 * 更新投放
	 * 如果存在则更新,不存在则添加
	 * @return
	 */
	public boolean updateCampaign(Campaign campaign);
	
	public boolean updateCampgrp(long groupid, Long[] values);
	public Long[] getCampgrp(long groupid);

	/**
	 * 修改状态
	 * @param status
	 * @return
	 */
	public boolean changeAutoStatus(long campaignId, CampaignAutoStatus status);
	public boolean changeManulStatus(long campaignId, CampaignManulStatus status);

	/**
	 * 根据ID查找投放
	 * @return
	 */
	public Campaign getCampaignById(long id);

	/**
	 * 根据ID删除投放
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
	 */
	public void changeCampPriceFactor(long campid, float factor);

	/**
	 * @param id
	 * @return
	 */
	public float getCampPriceFactor(long id);

	/**
	 * 查创意对应的尺寸和素材组ID
	 * @param creativeid
	 * @return
	 */
	public long[] getGroupidSizeIdByCreativeId(long creativeid);
}
