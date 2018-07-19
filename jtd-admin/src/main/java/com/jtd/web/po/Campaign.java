package com.jtd.web.po;

import com.jtd.commons.entity.BaseEntity;
import com.jtd.web.constants.CampaignAutoStatus;
import com.jtd.web.constants.CampaignManulStatus;
import com.jtd.web.constants.CampaignStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Campaign  extends BaseEntity{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    private Long partnerId;

    private Long groupId;

    private String campaignName;

    private Integer campaignType;

    private Integer adType;

    private Integer transType;

    private Integer budgetctrltype;

    private Date startTime;

    private Date endTime;

    private String weekHour;

    private Integer expendType;

    private Integer price;

    /** 日预算，单位：分 */
    private Long dailyBudget;

    private Long totalBudget;

    private Long dailyPv;

    private Long dailyClick;

    private Long totalPv;

    private Long totalClick;

    private Integer grossProfit;

    private Integer maxNegrossProfit;

    private Long creatorId;

    private Date lastModifyTime;

    private Long modifierId;

    private Integer editStepStatus;

    private volatile CampaignAutoStatus autoStatus;

    private volatile CampaignManulStatus manulStatus;

    private Integer deleteStatus;

    private String clickUrl;

    private String landingPage;

    private Integer landingtype;

    private Integer afValue;

    private Integer puttarget;

    private String phoneTitle;

    private String phonePropaganda;

    private String phoneDescribe;

    private String originalPrice;

    private String discountPrice;

    private String salesVolume;

    private String optional;

    private String deepLink;

    private String monitor;

    private String phoneDeepLinkUrl;

    private CampaignStatus campaignStatus;

    private String pvUrls;

    private Integer orderBy;

    public Integer getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(Integer orderBy) {
        this.orderBy = orderBy;
    }

    public Long getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Long partnerId) {
        this.partnerId = partnerId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName == null ? null : campaignName.trim();
    }

    public Integer getCampaignType() {
        return campaignType;
    }

    public void setCampaignType(Integer campaignType) {
        this.campaignType = campaignType;
    }

    public Integer getAdType() {
        return adType;
    }

    public void setAdType(Integer adType) {
        this.adType = adType;
    }

    public Integer getTransType() {
        return transType;
    }

    public void setTransType(Integer transType) {
        this.transType = transType;
    }

    public Integer getBudgetctrltype() {
        return budgetctrltype;
    }

    public void setBudgetctrltype(Integer budgetctrltype) {
        this.budgetctrltype = budgetctrltype;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getWeekHour() {
        return weekHour;
    }

    public void setWeekHour(String weekHour) {
        this.weekHour = weekHour == null ? null : weekHour.trim();
    }

    public Integer getExpendType() {
        return expendType;
    }

    public void setExpendType(Integer expendType) {
        this.expendType = expendType;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
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

    public Long getDailyPv() {
        return dailyPv;
    }

    public void setDailyPv(Long dailyPv) {
        this.dailyPv = dailyPv;
    }

    public Long getDailyClick() {
        return dailyClick;
    }

    public void setDailyClick(Long dailyClick) {
        this.dailyClick = dailyClick;
    }

    public Long getTotalPv() {
        return totalPv;
    }

    public void setTotalPv(Long totalPv) {
        this.totalPv = totalPv;
    }

    public Long getTotalClick() {
        return totalClick;
    }

    public void setTotalClick(Long totalClick) {
        this.totalClick = totalClick;
    }

    public Integer getGrossProfit() {
        return grossProfit;
    }

    public void setGrossProfit(Integer grossProfit) {
        this.grossProfit = grossProfit;
    }

    public Integer getMaxNegrossProfit() {
        return maxNegrossProfit;
    }

    public void setMaxNegrossProfit(Integer maxNegrossProfit) {
        this.maxNegrossProfit = maxNegrossProfit;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Date getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(Date lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public Long getModifierId() {
        return modifierId;
    }

    public void setModifierId(Long modifierId) {
        this.modifierId = modifierId;
    }

    public Integer getEditStepStatus() {
        return editStepStatus;
    }

    public void setEditStepStatus(Integer editStepStatus) {
        this.editStepStatus = editStepStatus;
    }

    public CampaignAutoStatus getAutoStatus() {
        return autoStatus;
    }

    public void setAutoStatus(CampaignAutoStatus autoStatus) {
        this.autoStatus = autoStatus;
        processCampaignStatus();
    }

    public CampaignManulStatus getManulStatus() {
        return manulStatus;
    }

    public void setManulStatus(CampaignManulStatus manulStatus) {
        this.manulStatus = manulStatus;
        processCampaignStatus();
    }

    public Integer getDeleteStatus() {
        return deleteStatus;
    }

    public void setDeleteStatus(Integer deleteStatus) {
        this.deleteStatus = deleteStatus;
    }

    public String getClickUrl() {
        return clickUrl;
    }

    public void setClickUrl(String clickUrl) {
        this.clickUrl = clickUrl == null ? null : clickUrl.trim();
    }

    public String getLandingPage() {
        return landingPage;
    }

    public void setLandingPage(String landingPage) {
        this.landingPage = landingPage == null ? null : landingPage.trim();
    }

    public Integer getLandingtype() {
        return landingtype;
    }

    public void setLandingtype(Integer landingtype) {
        this.landingtype = landingtype;
    }

    public Integer getAfValue() {
        return afValue;
    }

    public void setAfValue(Integer afValue) {
        this.afValue = afValue;
    }

    public Integer getPuttarget() {
        return puttarget;
    }

    public void setPuttarget(Integer puttarget) {
        this.puttarget = puttarget;
    }

    public String getPhoneTitle() {
        return phoneTitle;
    }

    public void setPhoneTitle(String phoneTitle) {
        this.phoneTitle = phoneTitle == null ? null : phoneTitle.trim();
    }

    public String getPhonePropaganda() {
        return phonePropaganda;
    }

    public void setPhonePropaganda(String phonePropaganda) {
        this.phonePropaganda = phonePropaganda == null ? null : phonePropaganda.trim();
    }

    public String getPhoneDescribe() {
        return phoneDescribe;
    }

    public void setPhoneDescribe(String phoneDescribe) {
        this.phoneDescribe = phoneDescribe == null ? null : phoneDescribe.trim();
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(String originalPrice) {
        this.originalPrice = originalPrice == null ? null : originalPrice.trim();
    }

    public String getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(String discountPrice) {
        this.discountPrice = discountPrice == null ? null : discountPrice.trim();
    }

    public String getSalesVolume() {
        return salesVolume;
    }

    public void setSalesVolume(String salesVolume) {
        this.salesVolume = salesVolume == null ? null : salesVolume.trim();
    }

    public String getOptional() {
        return optional;
    }

    public void setOptional(String optional) {
        this.optional = optional == null ? null : optional.trim();
    }

    public String getDeepLink() {
        return deepLink;
    }

    public void setDeepLink(String deepLink) {
        this.deepLink = deepLink == null ? null : deepLink.trim();
    }

    public String getMonitor() {
        return monitor;
    }

    public void setMonitor(String monitor) {
        this.monitor = monitor == null ? null : monitor.trim();
    }

    public String getPhoneDeepLinkUrl() {
        return phoneDeepLinkUrl;
    }

    public void setPhoneDeepLinkUrl(String phoneDeepLinkUrl) {
        this.phoneDeepLinkUrl = phoneDeepLinkUrl == null ? null : phoneDeepLinkUrl.trim();
    }

    public CampaignStatus getCampaignStatus() {
    	if (campaignStatus == null) {
			processCampaignStatus();
		}
		return campaignStatus;
    }

    public void setCampaignstatus(CampaignStatus campaignStatus) {
        this.campaignStatus = campaignStatus;
    }

    public String getPvUrls() {
        return pvUrls;
    }

    public void setPvUrls(String pvUrls) {
        this.pvUrls = pvUrls == null ? null : pvUrls.trim();
    }
    
    
    public void processCampaignStatus() {
		if ((autoStatus == null || manulStatus == null)) {
			return;
		}
		switch (this.manulStatus) {
		case DELETE:
			campaignStatus = CampaignStatus.DELETE;
			break;
		case EDIT:
			campaignStatus = CampaignStatus.EDIT;
			break;
		case TOCOMMIT:
			campaignStatus = CampaignStatus.TOCOMMIT;
			break;
		case ONLINE:
			switch (this.autoStatus) {
			case READY:
				campaignStatus = CampaignStatus.READY;
				break;
			case ONLINE:
				campaignStatus = CampaignStatus.ONLINE;
				break;
			case PAUSE_NOTTIME:
			case PAUSE_ACCOMPLISH_GOALS:
				campaignStatus = CampaignStatus.OFFLINE;
				break;
			case FINISHED:
				campaignStatus = CampaignStatus.FINISHED;
				break;
			}
			break;
		case PAUSE:
			switch (this.autoStatus) {
			case READY:
			case ONLINE:
			case PAUSE_NOTTIME:
			case PAUSE_ACCOMPLISH_GOALS:
				campaignStatus = CampaignStatus.PAUSED;
				break;
			case FINISHED:
				campaignStatus = CampaignStatus.FINISHED;
				break;
			}
			break;
		case OFFLINE:
			campaignStatus = CampaignStatus.FINISHED;
			break;
		}
	}
    
    /**
	 * 根据campaignStatus返回所有可能的手动状态
	 * @param campaignStatus
	 * @return
	 */
	public List<CampaignManulStatus> getManualStatusList(
			CampaignStatus campaignStatus) {

		if (campaignStatus == null) {
			return null;
		}
		ArrayList<CampaignManulStatus> manualList = new ArrayList<CampaignManulStatus>();
		switch (campaignStatus) {
		case EDIT:
			manualList.add(CampaignManulStatus.EDIT);
			break;
		case TOCOMMIT:
			manualList.add(CampaignManulStatus.TOCOMMIT);
			break;
		case READY:
			manualList.add(CampaignManulStatus.ONLINE);
			break;
		case ONLINE:
			manualList.add(CampaignManulStatus.ONLINE);
			break;
		case PAUSED:
			manualList.add(CampaignManulStatus.PAUSE);
			break;
		case OFFLINE:
			manualList.add(CampaignManulStatus.OFFLINE);
			break;
		case FINISHED:
			manualList.add(CampaignManulStatus.OFFLINE);
			manualList.add(CampaignManulStatus.PAUSE);
			manualList.add(CampaignManulStatus.ONLINE);
			break;
		case DELETE:
			manualList.add(CampaignManulStatus.DELETE);
			break;
		}
		return manualList ;
	}
	
	/**
	 * 根据campaignStatus返回所有可能的自动状态
	 * @param campaignStatus
	 * @return
	 */
	public List<CampaignAutoStatus> getAutoStatusList(
			CampaignStatus campaignStatus) {

		if (campaignStatus == null) {
			return null;
		}
		ArrayList<CampaignAutoStatus> automaticList = new ArrayList<CampaignAutoStatus>();
		switch (campaignStatus) {
		case EDIT:
			break;
		case TOCOMMIT:
			break;
		case READY:
			break;
		case ONLINE:
			automaticList.add(CampaignAutoStatus.ONLINE);
			break;
		case PAUSED:
			automaticList.add(CampaignAutoStatus.ONLINE);
			automaticList.add(CampaignAutoStatus.READY);
			automaticList.add(CampaignAutoStatus.ONLINE);
			automaticList.add(CampaignAutoStatus.PAUSE_NOTTIME);
			automaticList.add(CampaignAutoStatus.PAUSE_ACCOMPLISH_GOALS);
			break;
		case OFFLINE:
			automaticList.add(CampaignAutoStatus.PAUSE_NOTTIME);
			automaticList.add(CampaignAutoStatus.PAUSE_ACCOMPLISH_GOALS);
			break;
		case FINISHED:
			automaticList.add(CampaignAutoStatus.FINISHED);
			break;
		case DELETE:
			break;
		}
		
		return automaticList ;
	}
}