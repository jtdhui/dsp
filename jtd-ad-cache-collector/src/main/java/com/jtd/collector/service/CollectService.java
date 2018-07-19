package com.jtd.collector.service;


import com.jtd.web.constants.CampaignManulStatus;
import com.jtd.web.model.Campaign;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月12日
 * @项目名称 dsp-cache-collector
 * @描述 <p></p>
 */
public interface CollectService {

	/**
	 * 汇总Redis里的统计数据然后写入MySQL
	 */
	public void collect();
	
	public boolean updateCampaign(Campaign campaign);
	public boolean changeManulStatus(long campaignId, CampaignManulStatus status);
}
