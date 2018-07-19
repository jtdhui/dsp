package com.jtd.web.model;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jtd.web.constants.CampGroupPubStatus;
import com.jtd.web.constants.CampaignStatus;
import com.jtd.web.vo.CountResult;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置
 * @创建日期 2016年8月25日
 * @项目名称 dsp-admin
 * @描述
 *     <p>
 * 	推广组
 *     </p>
 */
public class CampGroup implements java.io.Serializable {

	private static final long serialVersionUID = 6518591348509445519L;

	private Long id;
	/**
	 * 所属伙伴ID
	 */
	private Long partnerId;
	/**
	 * 推广组名
	 */
	private String groupName;
	/**
	 * 日预算
	 */
	private Long dailyBudgetGoal;
	/**
	 * 总预算
	 */
	private Long totalBudgetGoal;
	/**
	 * 日展现量目标
	 */
	private Long dailyPvGoal;
	/**
	 * 日点击量目标
	 */
	private Long dailyClickGoal;
	/**
	 * 总展现量目标
	 */
	private Long totalPvGoal;
	/**
	 * 总点击量目标
	 */
	private Long totalClickGoal;
	/**
	 * 目标描述
	 */
	private String description;
	/**
	 * 修改时间
	 */
	private Date modifyTime;
	/**
	 * 删除状态
	 */
	private Boolean deleteStatus = Boolean.FALSE;

	private CountResult todayCountResult;
	private CountResult totalCountResult;

	/**
	 * 推广组投放状态
	 */
	private CampGroupPubStatus campaignGroupPubStatus;

	/**
	 * 该推广组的推广活动集合<br>
	 * Map<campaignId,campaign>
	 */
	private Map<Long, Campaign> campaigns = null;
	/**
	 * 在投的活动数
	 */
	private Integer onlineCampainCount;

	/**
	 * 该推广组所有的推广活动数
	 */
	private Integer allCampainCount;

	/**
	 * 处理组状态
	 */
	public void processStatus() {
		allCampainCount = 0;
		onlineCampainCount = 0;
		if (campaigns != null && !campaigns.isEmpty()) {
			allCampainCount = campaigns.size();
			for (Campaign campaign : campaigns.values()) {
				if (CampaignStatus.ONLINE.equals(campaign.getCampaignStatus())) {
					onlineCampainCount++;
				}
			}
		}
		if (onlineCampainCount > 0) {
			campaignGroupPubStatus = CampGroupPubStatus.ONLINE;
		} else {
			campaignGroupPubStatus = CampGroupPubStatus.OFFLINE;
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(Long partnerId) {
		this.partnerId = partnerId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Long getDailyBudgetGoal() {
		return dailyBudgetGoal;
	}

	public void setDailyBudgetGoal(Long dailyBudgetGoal) {
		this.dailyBudgetGoal = dailyBudgetGoal;
	}

	public Long getTotalBudgetGoal() {
		return totalBudgetGoal;
	}

	public void setTotalBudgetGoal(Long totalBudgetGoal) {
		this.totalBudgetGoal = totalBudgetGoal;
	}

	public Long getDailyPvGoal() {
		return dailyPvGoal;
	}

	public void setDailyPvGoal(Long dailyPvGoal) {
		this.dailyPvGoal = dailyPvGoal;
	}

	public Long getDailyClickGoal() {
		return dailyClickGoal;
	}

	public void setDailyClickGoal(Long dailyClickGoal) {
		this.dailyClickGoal = dailyClickGoal;
	}

	public Long getTotalPvGoal() {
		return totalPvGoal;
	}

	public void setTotalPvGoal(Long totalPvGoal) {
		this.totalPvGoal = totalPvGoal;
	}

	public Long getTotalClickGoal() {
		return totalClickGoal;
	}

	public void setTotalClickGoal(Long totalClickGoal) {
		this.totalClickGoal = totalClickGoal;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getDeleteStatus() {
		return deleteStatus;
	}

	public void setDeleteStatus(Boolean deleteStatus) {
		this.deleteStatus = deleteStatus;
	}

	public Double getTodayExpendCompletionRate() {
		Double todayExpendCompletionRate = 0D;
		if (todayCountResult != null && todayCountResult.getExpend() != null && dailyBudgetGoal != null) {
			todayExpendCompletionRate = (todayCountResult.getExpend() * 1d) / dailyBudgetGoal;
		}
		return todayExpendCompletionRate;
	}

	public Double getShowTodayExpendCompletionRate() {
		Double todayExpendCompletionRate = getTodayExpendCompletionRate();
		todayExpendCompletionRate = todayExpendCompletionRate * 100;
		return todayExpendCompletionRate;
	}

	public Double getTotalExpendCompletionRate() {
		Double totalExpendCompletionRate = 0D;
		if (totalCountResult != null && totalCountResult.getExpend() != null && totalBudgetGoal != null) {
			totalExpendCompletionRate = (totalCountResult.getExpend() * 1d) / totalBudgetGoal;
		}
		return totalExpendCompletionRate;
	}

	public Double getShowTotalExpendCompletionRate() {
		Double totalExpendCompletionRate = getTotalExpendCompletionRate();
		totalExpendCompletionRate = totalExpendCompletionRate * 100;
		return totalExpendCompletionRate;
	}

	public Double getTodayPvCompletionRate() {
		Double todayPvCompletionRate = 0D;
		if (todayCountResult != null && todayCountResult.getPv() != null && dailyPvGoal != null) {
			todayPvCompletionRate = (todayCountResult.getPv() * 1d) / dailyPvGoal;
		}
		return todayPvCompletionRate;
	}

	public Double getShowTodayPvCompletionRate() {
		Double todayPvCompletionRate = getTodayPvCompletionRate();
		todayPvCompletionRate = todayPvCompletionRate * 100;
		return todayPvCompletionRate;
	}

	public Double getTotalPvCompletionRate() {
		Double totalPvCompletionRate = 0D;
		if (totalCountResult != null && totalCountResult.getPv() != null && totalPvGoal != null) {
			totalPvCompletionRate = (totalCountResult.getPv() * 1d) / totalPvGoal;
		}
		return totalPvCompletionRate;
	}

	public Double getShowTotalPvCompletionRate() {
		Double totalPvCompletionRate = getTotalPvCompletionRate();
		totalPvCompletionRate = totalPvCompletionRate * 100;
		return totalPvCompletionRate;
	}

	public Double getTodayClickCompletionRate() {
		Double todayClickCompletionRate = 0D;
		if (todayCountResult != null && todayCountResult.getClick() != null && dailyClickGoal != null) {
			todayClickCompletionRate = (todayCountResult.getClick() * 1d) / dailyClickGoal;
		}
		return todayClickCompletionRate;
	}

	public Double getShowTodayClickCompletionRate() {
		Double todayClickCompletionRate = getTodayClickCompletionRate();
		todayClickCompletionRate = todayClickCompletionRate * 100;
		return todayClickCompletionRate;
	}

	public Double getTotalClickCompletionRate() {
		Double totalClickCompletionRate = 0D;
		if (totalCountResult != null && totalCountResult.getClick() != null && totalClickGoal != null) {
			totalClickCompletionRate = (totalCountResult.getClick() * 1d) / totalClickGoal;
		}
		return totalClickCompletionRate;
	}

	public Double getShowTotalClickCompletionRate() {
		Double totalClickCompletionRate = getTotalClickCompletionRate();
		totalClickCompletionRate = totalClickCompletionRate * 100;
		return totalClickCompletionRate;
	}

	public Map<Long, Campaign> getCampaigns() {
		return campaigns;
	}

	public void setCampaigns(List<Campaign> campaigns) {
		this.campaigns = new HashMap<Long, Campaign>();
		if (campaigns == null) {
			return;
		}
		for (Campaign campaign : campaigns) {
			this.campaigns.put(campaign.getId(), campaign);
		}
	}

	public void setCampaigns(Map<Long, Campaign> campaigns) {
		this.campaigns = campaigns;
	}

	public CampGroupPubStatus getCampaignGroupPubStatus() {
		return campaignGroupPubStatus;
	}

	public void setCampaignGroupPubStatus(CampGroupPubStatus campaignGroupPubStatus) {
		this.campaignGroupPubStatus = campaignGroupPubStatus;
	}

	public Integer getOnlineCampainCount() {
		return onlineCampainCount;
	}

	public void setOnlineCampainCount(Integer onlineCampainCount) {
		this.onlineCampainCount = onlineCampainCount;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public Double getShowDailyBudgetGoal() {
		if (dailyBudgetGoal != null) {
			return dailyBudgetGoal / 100d;
		}
		return null;
	}

	public Double getShowTotalBudgetGoal() {
		if (totalBudgetGoal != null) {
			return totalBudgetGoal / 100d;
		}
		return null;
	}

	public CountResult getTodayCountResult() {
		return todayCountResult;
	}

	public void setTodayCountResult(CountResult todayCountResult) {
		this.todayCountResult = todayCountResult;
	}

	public CountResult getTotalCountResult() {
		return totalCountResult;
	}

	public void setTotalCountResult(CountResult totalCountResult) {
		this.totalCountResult = totalCountResult;
	}

}
