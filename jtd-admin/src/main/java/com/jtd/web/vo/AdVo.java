package com.jtd.web.vo;
import java.io.Serializable;
import java.util.Map;

import com.jtd.web.constants.CampaignType;
import com.jtd.web.constants.CreativeType;
import com.jtd.web.constants.LandingType;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月25日
 * @项目名称 dsp-admin
 * @描述 <p>广告值传递对象</p>
 */
public class AdVo implements Serializable {
	private static final long serialVersionUID = 3150400763091984345L;
	private Long id;
	/**
	 * 所属推广活动ID
	 */

	private Long campId;
	/**
	 * 活动名称
	 */

	private String campaignName;

	private CampaignType campType;
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
	public String getCampaignName() {
		return campaignName;
	}
	public void setCampaignName(String campaignName) {
		this.campaignName = campaignName;
	}
	public CampaignType getCampType() {
		return campType;
	}
	public void setCampType(CampaignType campType) {
		this.campType = campType;
	}
	public Long getCreativeId() {
		return creativeId;
	}
	public void setCreativeId(Long creativeId) {
		this.creativeId = creativeId;
	}
	public Long getGroupId() {
		return groupId;
	}
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getCreativeName() {
		return creativeName;
	}
	public void setCreativeName(String creativeName) {
		this.creativeName = creativeName;
	}
	public CreativeType getCreativeType() {
		return creativeType;
	}
	public void setCreativeType(CreativeType creativeType) {
		this.creativeType = creativeType;
	}
	public Long getSizeid() {
		return sizeid;
	}
	public void setSizeid(Long sizeid) {
		this.sizeid = sizeid;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getCreativeUrl() {
		return creativeUrl;
	}
	public void setCreativeUrl(String creativeUrl) {
		this.creativeUrl = creativeUrl;
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
	public Map<String, String> getAdCategorys() {
		return adCategorys;
	}
	public void setAdCategorys(Map<String, String> adCategorys) {
		this.adCategorys = adCategorys;
	}
	public Boolean getInternalAudit() {
		return internalAudit;
	}
	public void setInternalAudit(Boolean internalAudit) {
		this.internalAudit = internalAudit;
	}
	public Boolean getDeleteStatus() {
		return deleteStatus;
	}
	public void setDeleteStatus(Boolean deleteStatus) {
		this.deleteStatus = deleteStatus;
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

}
