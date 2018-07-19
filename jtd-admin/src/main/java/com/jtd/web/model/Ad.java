package com.jtd.web.model;

import com.jtd.web.constants.AdType;
import com.jtd.web.constants.CampaignType;
import com.jtd.web.constants.CreativeType;
import com.jtd.web.constants.LandingType;

import java.util.Map;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月25日
 * @项目名称 dsp-admin
 * @描述 <p>推广活动广告</p>
 */
public class Ad implements java.io.Serializable {

	private static final long serialVersionUID = 3895087053089246882L;
	private Long id;
	/**
	 * 所属推广活动ID
	 */
	private Long campId;
	/**
	 * 活动名称
	 */
	private String campaignName;
	/**
	 * PC(0, "pc广告"), APP(1, "app广告"), WAP(2, "wap广告");
	 */
	private CampaignType campType;
	/**
	 * PC_BANNER(0, "pc banner"), 
	PC_VIDEO(1, "pc视频"), 
	PC_URL(2, "文字链"), 
	PC_IMGANDTEXT(3, "图文"), 
	//app
	APP_BANNER(4, "banner in app"), 
	APP_FULLSCREEN(5, "插屏全屏 in app"), 
	APP_MESSAGE(6, "原生-信息流"),
	APP_FOCUS(7, "原生-焦点图"),
	APP_VIDEO(8,"视频贴片 in app"),
	 */
	private AdType adType;

	/**
	 * 创意ID
	 */
	private Long creativeId;

	/* 以下字段在提交到引擎时请填充 */
	/**
	 * 创意组ID
	 */
	private Long groupId;
	/**
	 * 创意组名称
	 */
	private String groupName;
	/**
	 * 创意名称
	 */
	private String creativeName;
	/**
	 * 物料类型
	 */
	private CreativeType creativeType;

    /**
     * 物料后辍
     */
    private String suffix;
    /**
	 * 创意尺寸ID
	 */
	private Long sizeid;
	/**
	 * 创意尺寸
	 */
	private String size;
	/**
	 * 素材地址
	 */
	private String creativeUrl;

	/**
	 * 点击连接
	 */
	private String clickUrl;

	/**
	 * pv监测连接（JSON数组）"['http://www.monitor.com/aaa', ...]"
	 */
	private String pvUrls;
	/**
	 * 着陆页
	 */
	private String landingPage;
	/**
	 * 着陆页类型
	 */
	private LandingType landingType;
	// key:类别体系，枚举CatgSerial的name,如TANX, value:该体系的类别ID
	private Map<String, String> adCategorys;
	/* 以上字段在提交到引擎时请填充 */

	/**
	 * 内部审核
	 */
	private Boolean internalAudit;
	/**
	 * 删除状态
	 */
	private Boolean deleteStatus = Boolean.FALSE;

	/**
	 * 广告主
	 */
	private String partnerName;
	/**
	 * 广告主行业
	 */
	private String catgname;

	/**
	 * Map<channelId,审核状态>
	 */
	private Map<Long, AdAuditStatus> adAuditStatuses;

	/*********** prd v2r2 新加的字段 ****************/
	// 标题
	private String phoneTitle;
	// 广告语
	private String phonePropaganda;
	// 广告描述
	private String phoneDescribe;

	// 原价
	private String originalPrice;

	// 折后价
	private String discountPrice;
	// 销量
	private Integer salesVolume;

	// 选填
	private String optional;

	// 是否启动Deeplink打开
	private String deepLink;
	/**
	 * 设置监控
	 */
	private String monitor;

	// Deeplink目标地址
	private String phoneDeepLinkUrl;

	private Long duration;

	public String getCampaignName() {
		return campaignName;
	}

	public void setCampaignName(String campaignName) {
		this.campaignName = campaignName;
	}

	public String getPartnerName() {
		return partnerName;
	}

	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
	}

	public String getCatgname() {
		return catgname;
	}

	public void setCatgname(String catgname) {
		this.catgname = catgname;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCampId() {
		return campId;
	}

	public void setCampId(Long campId) {
		this.campId = campId;
	}

	public Long getCreativeId() {
		return creativeId;
	}

	public void setCreativeId(Long creativeId) {
		this.creativeId = creativeId;
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

	public Boolean getDeleteStatus() {
		return deleteStatus;
	}

	public void setDeleteStatus(Boolean deleteStatus) {
		this.deleteStatus = deleteStatus;
	}

	/**
	 * @return the groupName
	 */
	public String getGroupName() {
		return groupName;
	}

	/**
	 * @param groupName
	 *            the groupName to set
	 */
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	/**
	 * @return the creativeName
	 */
	public String getCreativeName() {
		return creativeName;
	}

	/**
	 * @param creativeName
	 *            the creativeName to set
	 */
	public void setCreativeName(String creativeName) {
		this.creativeName = creativeName;
	}

	/**
	 * @return the creativeType
	 */
	public CreativeType getCreativeType() {
		return creativeType;
	}

	/**
	 * @param creativeType
	 *            the creativeType to set
	 */
	public void setCreativeType(CreativeType creativeType) {
		this.creativeType = creativeType;
	}

	/**
	 * @return the sizeid
	 */
	public Long getSizeid() {
		return sizeid;
	}

	/**
	 * @param sizeid
	 *            the sizeid to set
	 */
	public void setSizeid(Long sizeid) {
		this.sizeid = sizeid;
	}

	/**
	 * @return the size
	 */
	public String getSize() {
		return size;
	}

	/**
	 * @param size
	 *            the size to set
	 */
	public void setSize(String size) {
		this.size = size;
	}

	/**
	 * @return the creativeUrl
	 */
	public String getCreativeUrl() {
		return creativeUrl;
	}

	/**
	 * @param creativeUrl
	 *            the creativeUrl to set
	 */
	public void setCreativeUrl(String creativeUrl) {
		this.creativeUrl = creativeUrl;
	}

	/**
	 * @return the groupId
	 */
	public Long getGroupId() {
		return groupId;
	}

	/**
	 * @param groupId
	 *            the groupId to set
	 */
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public Boolean getInternalAudit() {
		return internalAudit;
	}

	public void setInternalAudit(Boolean internalAudit) {
		this.internalAudit = internalAudit;
	}

	public void setAdAuditStatuses(Map<Long, AdAuditStatus> adAuditStatuses) {
		this.adAuditStatuses = adAuditStatuses;
	}

	public Map<String, String> getAdCategorys() {
		return adCategorys;
	}

	public void setAdCategorys(Map<String, String> adCategorys) {
		this.adCategorys = adCategorys;
	}

	public CampaignType getCampType() {
		return campType;
	}

	public void setCampType(CampaignType campType) {
		this.campType = campType;
	}

	public AdType getAdType() {
		return adType;
	}

	public void setAdType(AdType adType) {
		this.adType = adType;
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

	public String getPhoneDeepLinkUrl() {
		return phoneDeepLinkUrl;
	}

	public void setPhoneDeepLinkUrl(String phoneDeepLinkUrl) {
		this.phoneDeepLinkUrl = phoneDeepLinkUrl;
	}

	public String getOptional() {
		return optional;
	}

	public void setOptional(String optional) {
		this.optional = optional;
	}

	public String getMonitor() {
		return monitor;
	}

	public void setMonitor(String monitor) {
		this.monitor = monitor;
	}

	public String getDeepLink() {
		return deepLink;
	}

	public void setDeepLink(String deepLink) {
		this.deepLink = deepLink;
	}

	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

	public Map<Long, AdAuditStatus> getAdAuditStatuses() {
		return adAuditStatuses;
	}

	public AdAuditStatus getAuditStatus(Integer channelId) {
		Long channelIdLong = channelId.longValue();
		if (adAuditStatuses == null) {
			return null;
		} else {
			AdAuditStatus adAuditStatus = adAuditStatuses.get(channelIdLong);
			return adAuditStatus;
		}

	}

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

}
