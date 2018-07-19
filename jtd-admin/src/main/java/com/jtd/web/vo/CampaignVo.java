package com.jtd.web.vo;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.jtd.web.constants.AuditStatus;
import com.jtd.web.constants.CampaignStatus;
import com.jtd.web.model.Ad;
import com.jtd.web.model.AdAuditStatus;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月25日
 * @项目名称 dsp-admin
 * @描述 <p>广告活动Vo</p>
 */
public class CampaignVo {

	/**
	 * 活动id
	 */
	private Long id;
	/**
	 * 活动名称
	 */
	private String campaignName;
	/**
	 * 推广组名称
	 */
	private String campGroupName;
	/**
	 * 活动状态
	 */
	private String campaignStatus;
	/**
	 * 所有广告数
	 */
	private int adCount;
	/**
	 * 被部分渠道拒绝的广告数
	 */
	private int someChannelRejectAdCount;
	/**
	 * 被所有渠道拒绝的广告数
	 */
	private int allChannelRejectAdCount;
	/**
	 * 是否设置完整
	 */
	private String isSetComplete;
	/**
	 * 计费方式
	 */
	private String expendType;
	/**
	 * 最高限价
	 */
	private Double maxPrice;

	/**
	 * 竞价量
	 */
	private Long bid;
	/**
	 * 展现量
	 */
	private Long pv;
	/**
	 * 竞价成功率
	 */
	private Double ratioOfBid;
	/**
	 * 点击量
	 */
	private Long click;
	/**
	 * 点击率
	 */
	private Double ctr;
	/**
	 * 投放花费
	 */
	private Double expend;
	/**
	 * 投放cpm
	 */
	private Double cpm;
	/**
	 * 投放cpc
	 */
	private Double cpc;
	/**
	 * 成本花费
	 */
	private Double cost;
	/**
	 * 成本cpm
	 */
	private Double costCpm;
	/**
	 * 成本cpc
	 */
	private Double costCpc;
	/**
	 * 毛利额
	 */
	private Double grossMargin;
	/**
	 * 毛利率
	 */
	private Double ratioOfMargin;
	/**
	 * 日预算
	 */
	private Long dailyBudget;
	/**
	 * 总预算
	 */
	private Long totalBudget;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCampaignName() {
		return campaignName;
	}

	public void setCampaignName(String campaignName) {
		this.campaignName = campaignName;
	}

	public String getCampGroupName() {
		return campGroupName;
	}

	public void setCampGroupName(String campGroupName) {
		this.campGroupName = campGroupName;
	}

	public String getCampaignStatus() {
		return campaignStatus;
	}

	public void setCampaignStatus(CampaignStatus campaignStatus) {
		this.campaignStatus = campaignStatus.getDesc();
		if (campaignStatus != null && !campaignStatus.equals(CampaignStatus.EDIT)) {
			isSetComplete = "是";
		} else if (campaignStatus != null && campaignStatus.equals(CampaignStatus.EDIT)) {
			isSetComplete = "否";
		}
	}

	public int getAdCount() {
		return adCount;
	}

	public void setAdCount(int adCount) {
		this.adCount = adCount;
	}

	public int getSomeChannelRejectAdCount() {
		return someChannelRejectAdCount;
	}

	public void setSomeChannelRejectAdCount(int someChannelRejectAdCount) {
		this.someChannelRejectAdCount = someChannelRejectAdCount;
	}

	public int getAllChannelRejectAdCount() {
		return allChannelRejectAdCount;
	}

	public void setAllChannelRejectAdCount(int allChannelRejectAdCount) {
		this.allChannelRejectAdCount = allChannelRejectAdCount;
	}

	public String isSetComplete() {
		return isSetComplete;
	}

	public String getExpendType() {
		return expendType;
	}

	public void setExpendType(String expendType) {
		this.expendType = expendType;
	}

	public Double getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(Double maxPrice) {
		this.maxPrice = maxPrice;
	}

	public Long getBid() {
		return bid;
	}

	public void setBid(Long bid) {
		this.bid = bid;
	}

	public Long getPv() {
		return pv;
	}

	public void setPv(Long pv) {
		this.pv = pv;
	}

	public Double getRatioOfBid() {
		return ratioOfBid;
	}

	public void setRatioOfBid(Double ratioOfBid) {
		this.ratioOfBid = ratioOfBid;
	}

	public Long getClick() {
		return click;
	}

	public void setClick(Long click) {
		this.click = click;
	}

	public Double getCtr() {
		return ctr;
	}

	public void setCtr(Double ctr) {
		this.ctr = ctr;
	}

	public Double getCpm() {
		return cpm;
	}

	public void setCpm(Double cpm) {
		this.cpm = cpm;
	}

	public Double getCpc() {
		return cpc;
	}

	public void setCpc(Double cpc) {
		this.cpc = cpc;
	}

	public Double getCostCpm() {
		return costCpm;
	}

	public void setCostCpm(Double costCpm) {
		this.costCpm = costCpm;
	}

	public Double getCostCpc() {
		return costCpc;
	}

	public void setCostCpc(Double costCpc) {
		this.costCpc = costCpc;
	}

	public Double getGrossMargin() {
		return grossMargin;
	}

	public void setGrossMargin(Double grossMargin) {
		this.grossMargin = grossMargin;
	}

	public Double getRatioOfMargin() {
		return ratioOfMargin;
	}

	public void setRatioOfMargin(Double ratioOfMargin) {
		this.ratioOfMargin = ratioOfMargin;
	}

	public Long getDailyBudget() {
		return dailyBudget;
	}

	public void setDailyBudget(Long dailyBudget) {
		this.dailyBudget = dailyBudget;
	}

	public Long getTotalBudget() {
		return totalBudget;
	}

	public void setTotalBudget(Long totalBudget) {
		this.totalBudget = totalBudget;
	}

	public Double getExpend() {
		return expend;
	}

	public void setExpend(Double expend) {
		this.expend = expend;
	}

	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

	public void setAds(List<Ad> ads) {
		if (ads == null) {
			return;
		}
		adCount = ads.size();
		for (Ad ad : ads) {
			Map<Long, AdAuditStatus> adAuditStatuses = ad.getAdAuditStatuses();
			if (adAuditStatuses == null || adAuditStatuses.isEmpty()) {
				continue;
			}
			int failCount = 0;
			for (Entry<Long, AdAuditStatus> entry : adAuditStatuses.entrySet()) {
				AdAuditStatus adAuditStatus = entry.getValue();
				if (adAuditStatus != null && AuditStatus.STATUS_FAIL.equals(adAuditStatus.getStatus())) {
					failCount++;
				}
			}
			//TODO 目前有3个渠道，但是这个值不能硬编码，得改成其他方式，以防止以后添加渠道时该判断出错
			if (failCount == 3) {
				// 被全部渠道拒绝
				allChannelRejectAdCount++;
			} else if (failCount > 0) {
				// 被部分渠道拒绝
				someChannelRejectAdCount++;
			}
		}
	}
}
