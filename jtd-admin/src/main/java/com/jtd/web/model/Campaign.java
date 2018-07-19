package com.jtd.web.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.jtd.web.constants.AdType;
import com.jtd.web.constants.BudgetCtrlType;
import com.jtd.web.constants.CampaignAutoStatus;
import com.jtd.web.constants.CampaignManulStatus;
import com.jtd.web.constants.CampaignStatus;
import com.jtd.web.constants.CampaignType;
import com.jtd.web.constants.ExpendType;
import com.jtd.web.constants.LandingType;
import com.jtd.web.constants.PutTarget;
import com.jtd.web.constants.TransType;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置
 * @创建日期 2016年8月25日
 * @项目名称 dsp-admin
 * @描述 <p>
 *     推广活动
 *     </p>
 */
public class Campaign implements java.io.Serializable {
	public Campaign() {
	}

	/**
	 * 保存活动第三步时需要如下属性
	 * 
	 * @param id
	 * @param clickUrl
	 * @param pvUrls
	 * @param landingPage
	 * @param landingType
	 */
	public Campaign(Long id, String clickUrl, String pvUrls,
			String landingPage, LandingType landingType) {
		super();
		this.id = id;
		this.clickUrl = clickUrl;
		this.pvUrls = pvUrls;
		this.landingPage = landingPage;
		this.landingType = landingType;
	}

	private static final long serialVersionUID = 5845755654123120084L;
	// 活动id
	private Long id;
	/**
	 * 广告主id
	 */
	private Long partnerId;
	/**
	 * 所属推广组ID
	 */
	private Long groupId;
	/**
	 * 活动名称
	 */
	private String campName;
	/**
	 * 活动类型
	 */
	private CampaignType campType;

	/**
	 * 广告形式 PC_BANNER(0, "pc banner"), PC_VIDEO(1, "pc视频"), PC_URL(2, "文字链"),
	 * PC_IMGANDTEXT(3, "图文"), //app APP_BANNER(4, "banner in app"),
	 * APP_FULLSCREEN(5, "插屏全屏 in app"), APP_MESSAGE(6, "原生-信息流"), APP_FOCUS(7,
	 * "原生-焦点图"), APP_VIDEO(8,"视频贴片 in app"),
	 */
	private AdType adType;
	/**
	 * 交易类型 RTB(0,"RTB"),PMP(1,"PMP");
	 */
	private TransType transType;
	/**
	 * 预算控制方式 STANDARD(0, "标准"), FAST(1, "尽速");
	 */
	private BudgetCtrlType budgetCtrlType;

	/**
	 * 开始时间
	 */
	private Date startTime;
	/**
	 * 结束时间
	 */
	private Date endTime;
	/**
	 * 投放时段
	 */
	private String weekhour;
	/**
	 * 计费方式 CPM(0, "CPM"), CPC(1, "CPC")
	 */
	private ExpendType expendType;
	/**
	 * 价格
	 */
	private Integer price;
	/**
	 * 每日预算
	 */
	private Long dailyBudget;

	/**
	 * 总预算
	 */
	private Long totalBudget;
	/**
	 * 日展现量目标
	 */
	private Long dailyPv;
	/**
	 * 日点击量目标
	 */
	private Long dailyClick;
	/**
	 * 总展现量目标
	 */
	private Long totalPv;
	/**
	 * 总点击量目标
	 */
	private Long totalClick;
	/**
	 * 毛利率
	 */
	private Integer grossProfit;
	/**
	 * 最大负毛利率
	 */
	private Integer maxNeGrossProfit;
	/**
	 * af值
	 */
	private Integer afValue;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 创建者id
	 */
	private Long creatorId;
	/**
	 * 最后修改时间
	 */
	private Date lastModifyTime;
	/**
	 * 修改者id
	 */
	private Long modifierId;
	/**
	 * 自动状态 READY(0,"未开始"),ONLINE(1, "投放中"), PAUSE_NOTTIME(2, "自动下线-不在投放时段"),
	 * PAUSE_ACCOMPLISH_GOALS(3, "自动下线-到达当日目标"), FINISHED(4, "自动结束");
	 */
	private volatile CampaignAutoStatus autoStatus;
	/**
	 * 手动状态 EDIT(0,
	 * "编辑中"),TOCOMMIT(1,"暂停"),ONLINE(2,"投放中"),PAUSE(3,"暂停"),OFFLINE
	 * (4,"结束"),DELETE(5,"已删除");
	 */
	private volatile CampaignManulStatus manulStatus;

	private Integer editStepStatus;
	/**
	 * 删除状态
	 */
	private Boolean deleteStatus = Boolean.FALSE;
	/**
	 * 点击连接
	 */
	private String clickUrl;
	/**
	 * pv监测连接（集合）
	 */
	private String pvUrls;
	/**
	 * 着陆页
	 */
	private String landingPage;
	/**
	 * 着陆页类型 BROWSEROPEN(0, "浏览器打开"), WEBVIEWOPEN(1,
	 * "webview打开"),APPDOWNLOAD(2,"应用下载");
	 */
	private LandingType landingType;

	/**
	 * prd v2r2 新加的投放目标 投放目标
	 */
	private PutTarget putTarget;

	/**
	 * 推广活动的状态，该状态由自动状态和手动状态来确定
	 */
	private CampaignStatus campaignStatus;

	private JSONObject campaignDimJsonObject;

	private Map<String, String> campaignCategorys;

	private List<Ad> adList;
	private String partnerName;
	private String groupName;
	/**
	 * 推广组日预算
	 */
	private Long groupDailyBudgetGoal;
	/**
	 * 推广组总预算
	 */
	private Long groupTotalBudgetGoal;
	/**
	 * 推广组日展现量目标
	 */
	private Long groupDailyPvGoal;
	/**
	 * 推广组日点击量目标
	 */
	private Long groupDailyClickGoal;
	/**
	 * 推广组总展现量目标
	 */
	private Long groupTotalPvGoal;
	/**
	 * 推广组总点击量目标
	 */
	private Long groupTotalClickGoal;

	/**
	 * 提交到TANX后，TANX那边返回的广告主ID
	 */
	private String tanxPartnerId;
	/**
	 * 推广维度和广告 key:维度名称 value:维度值 见《推广维度存储格式.txt》
	 */
	private Map<String, String> campaignDims;

	// key widthxheight 例如300x250 value:所有这个尺寸的广告
	private Map<String, List<Ad>> ads;

	private String queryStatus;

	/*********** prd v2r2 新加的字段 ****************/
	// 标题 如果找不到，则不用填充
	private String phoneTitle;
	// 广告语 如果找不到，则不用填充
	private String phonePropaganda;
	// 广告描述 如果找不到，则不用填充
	private String phoneDescribe;

	// 原价 如果找不到，则不用填充
	private String originalPrice;

	// 折后价 如果找不到，则不用填充
	private String discountPrice;
	// 销量 如果找不到，则不用填充
	private Integer salesVolume;

	// 选填 如果找不到，则不用填充
	private String optional;

	// 是否启动Deeplink打开
	private String deepLink;
	/**
	 * 设置监控
	 */
	private String monitor;

	// Deeplink目标地址
	private String phoneDeepLinkUrl;

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

	public AdType getAdType() {
		return adType;
	}

	public void setAdType(AdType adType) {
		this.adType = adType;
	}

	public TransType getTransType() {
		return transType;
	}

	public void setTransType(TransType transType) {
		this.transType = transType;
	}

	public BudgetCtrlType getBudgetCtrlType() {
		return budgetCtrlType;
	}

	public void setBudgetCtrlType(BudgetCtrlType budgetCtrlType) {
		this.budgetCtrlType = budgetCtrlType;
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

	public String getWeekhour() {
		return weekhour;
	}

	public void setWeekhour(String weekhour) {
		this.weekhour = weekhour;
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

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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

	public CampaignAutoStatus getAutoStatus() {
		return autoStatus;
	}

	/**
	 * public CampaignStatus getStatus() { switch (this.manulStatus) { case
	 * DELETE: return CampaignStatus.DELETE; case EDIT: return
	 * CampaignStatus.EDIT; case TOCOMMIT: return CampaignStatus.TOCOMMIT; case
	 * ONLINE: switch (this.autoStatus) { case READY: return
	 * CampaignStatus.READY; case ONLINE: return CampaignStatus.ONLINE; case
	 * PAUSE_NOTTIME: case PAUSE_ACCOMPLISH_GOALS: return
	 * CampaignStatus.OFFLINE; case FINISHED: return CampaignStatus.FINISHED; }
	 * case PAUSE: switch (this.autoStatus) { case READY: case ONLINE: case
	 * PAUSE_NOTTIME: case PAUSE_ACCOMPLISH_GOALS: return CampaignStatus.PAUSED;
	 * case FINISHED: return CampaignStatus.FINISHED; } case OFFLINE: return
	 * CampaignStatus.FINISHED;
	 * 
	 * }
	 * 
	 * return null; }
	 **/

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

	public Boolean getDeleteStatus() {
		return deleteStatus;
	}

	public void setDeleteStatus(Boolean deleteStatus) {
		this.deleteStatus = deleteStatus;
	}

	public Integer getEditStepStatus() {
		return editStepStatus;
	}

	public void setEditStepStatus(Integer editStepStatus) {
		this.editStepStatus = editStepStatus;
	}

	public ExpendType getExpendType() {
		return expendType;
	}

	public void setExpendType(ExpendType expendType) {
		this.expendType = expendType;
	}

	public Integer getGrossProfit() {
		return grossProfit;
	}

	public void setGrossProfit(Integer grossProfit) {
		this.grossProfit = grossProfit;
	}

	public Integer getMaxNeGrossProfit() {
		return maxNeGrossProfit;
	}

	public void setMaxNeGrossProfit(Integer maxNeGrossProfit) {
		this.maxNeGrossProfit = maxNeGrossProfit;
	}

	public String getClickUrl() {
		return clickUrl;
	}

	public void setClickUrl(String clickUrl) {
		this.clickUrl = clickUrl;
	}

	public String getPvUrls() {
		return pvUrls;
	}

	public void setPvUrls(String pvUrls) {
		this.pvUrls = pvUrls;
	}

	public String getLandingPage() {
		return landingPage;
	}

	public void setLandingPage(String landingPage) {
		this.landingPage = landingPage;
	}

	public LandingType getLandingType() {
		return landingType;
	}

	public void setLandingType(LandingType landingType) {
		this.landingType = landingType;
	}

	public CampaignStatus getCampaignStatus() {
		if (campaignStatus == null) {
			processCampaignStatus();
		}
		return campaignStatus;
	}

	public void setCampaignStatus(CampaignStatus campaignStatus) {
		this.campaignStatus = campaignStatus;
	}

	public Integer getAfValue() {
		return afValue;
	}

	public void setAfValue(Integer afValue) {
		this.afValue = afValue;
	}

	public JSONObject getCampaignDimJsonObject() {
		return campaignDimJsonObject;
	}

	public void setCampaignDimJsonObject(JSONObject campaignDimJsonObject) {
		this.campaignDimJsonObject = campaignDimJsonObject;
	}

	public Map<String, String> getCampaignCategorys() {
		return campaignCategorys;
	}

	public void setCampCategorys(Map<String, String> campaignCategorys) {
		this.campaignCategorys = campaignCategorys;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public String getCampName() {
		return campName;
	}

	public void setCampName(String campName) {
		this.campName = campName;
	}

	public CampaignType getCampType() {
		return campType;
	}

	public void setCampType(CampaignType campType) {
		this.campType = campType;
	}

	public List<Ad> getAdList() {
		return adList;
	}

	public void setAdList(List<Ad> adList) {
		this.adList = adList;
	}

	public String getPartnerName() {
		return partnerName;
	}

	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Long getGroupDailyBudgetGoal() {
		return groupDailyBudgetGoal;
	}

	public void setGroupDailyBudgetGoal(Long groupDailyBudgetGoal) {
		this.groupDailyBudgetGoal = groupDailyBudgetGoal;
	}

	public Long getGroupTotalBudgetGoal() {
		return groupTotalBudgetGoal;
	}

	public void setGroupTotalBudgetGoal(Long groupTotalBudgetGoal) {
		this.groupTotalBudgetGoal = groupTotalBudgetGoal;
	}

	public Long getGroupDailyPvGoal() {
		return groupDailyPvGoal;
	}

	public void setGroupDailyPvGoal(Long groupDailyPvGoal) {
		this.groupDailyPvGoal = groupDailyPvGoal;
	}

	public Long getGroupDailyClickGoal() {
		return groupDailyClickGoal;
	}

	public void setGroupDailyClickGoal(Long groupDailyClickGoal) {
		this.groupDailyClickGoal = groupDailyClickGoal;
	}

	public Long getGroupTotalPvGoal() {
		return groupTotalPvGoal;
	}

	public void setGroupTotalPvGoal(Long groupTotalPvGoal) {
		this.groupTotalPvGoal = groupTotalPvGoal;
	}

	public Long getGroupTotalClickGoal() {
		return groupTotalClickGoal;
	}

	public void setGroupTotalClickGoal(Long groupTotalClickGoal) {
		this.groupTotalClickGoal = groupTotalClickGoal;
	}

	public String getTanxPartnerId() {
		return tanxPartnerId;
	}

	public void setTanxPartnerId(String tanxPartnerId) {
		this.tanxPartnerId = tanxPartnerId;
	}

	public Map<String, String> getCampaignDims() {
		return campaignDims;
	}

	public void setCampaignDims(Map<String, String> campaignDims) {
		this.campaignDims = campaignDims;
	}

	public Map<String, List<Ad>> getAds() {
		return ads;
	}

	public void setAds(Map<String, List<Ad>> ads) {
		this.ads = ads;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setCampaignCategorys(Map<String, String> campaignCategorys) {
		this.campaignCategorys = campaignCategorys;
	}

	public PutTarget getPutTarget() {
		return putTarget;
	}

	public void setPutTarget(PutTarget putTarget) {
		this.putTarget = putTarget;
	}

	public String getPhoneTitle() {
		return phoneTitle;
	}

	public void setPhoneTitle(String phoneTitle) {
		this.phoneTitle = phoneTitle;
	}

	public String getPhonePropaganda() {
		return phonePropaganda;
	}

	public void setPhonePropaganda(String phonePropaganda) {
		this.phonePropaganda = phonePropaganda;
	}

	public String getPhoneDescribe() {
		return phoneDescribe;
	}

	public void setPhoneDescribe(String phoneDescribe) {
		this.phoneDescribe = phoneDescribe;
	}

	public String getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(String originalPrice) {
		this.originalPrice = originalPrice;
	}

	public String getDiscountPrice() {
		return discountPrice;
	}

	public void setDiscountPrice(String discountPrice) {
		this.discountPrice = discountPrice;
	}

	public Integer getSalesVolume() {
		return salesVolume;
	}

	public void setSalesVolume(Integer salesVolume) {
		this.salesVolume = salesVolume;
	}

	public String getOptional() {
		return optional;
	}

	public void setOptional(String optional) {
		this.optional = optional;
	}

	public String getDeepLink() {
		return deepLink;
	}

	public void setDeepLink(String deepLink) {
		this.deepLink = deepLink;
	}

	public String getMonitor() {
		return monitor;
	}

	public void setMonitor(String monitor) {
		this.monitor = monitor;
	}

	public String getPhoneDeepLinkUrl() {
		return phoneDeepLinkUrl;
	}

	public void setPhoneDeepLinkUrl(String phoneDeepLinkUrl) {
		this.phoneDeepLinkUrl = phoneDeepLinkUrl;
	}

	public String getQueryStatus() {
		return queryStatus;
	}

	public void setQueryStatus(String queryStatus) {
		this.queryStatus = queryStatus;
	}

}
