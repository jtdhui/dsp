package com.jtd.web.model;


import com.jtd.web.constants.AdType;


/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月25日
 * @项目名称 dsp-admin
 * @描述 <p>广告位资源</p>
 */
public class AdPlace implements java.io.Serializable {

	private static final long serialVersionUID = -7942769911980291072L;
	private Long id;
	/**
	 * 广告位ID
	 */
	private String channelAdplaceId;
	/**
	 * 广告位名称
	 */
	private String channelAdplaceName;
	/**
	 * 所属渠道名称
	 */
	private String channelName;
	/**
	 * 预览URL
	 */
	private String previewUrl;
	/**
	 * 尺寸
	 */
	private String size;

	/**
	 * 流量类型
	 */
	private AdType adType;
	
	/**
	 * 所属网站
	 */
	private String website;
	/**
	 * 网站类型
	 */
	private String websiteType;

	private String websiteTypeDesc;


	/**
	 * 日均流量
	 */
	private Long dailyTraffics;
	/**
	 * 广告位点击率
	 */
	private Double ctr;

	private Long channelId;
	/**
	 * 广告位类型 广告位类型:FIX(0,"固定"),FLOW(1,"浮窗"),DOUBLE(2,"双边对联"),
	 * SINGLE(3,"单边对联"),EXTEND(4,"固定可扩展"),HOVER(5,"悬停"),FOLD(6,"折叠"),
	 * RP(7,"背投"),OPEN_SCREEN(8,"开屏"),TABLE_SCREEN(9,"插屏"),BANNER(10,"横幅")
	 * */
	private Integer placeType;
	/**
	 * 添加屏数类型字段screentype，0为其他，1为首屏，2为二屏幕
	 * */
	private Integer screenType;
	
	private String showScreenType;
	

	/**
	 * 添加媒体类型 websitetypeid,由于一个adx类型id有可能对应这多个dsp类型，所以数据有可能为 20_30_2
	 * */
	private String webSiteTypeId;
	/**
	 * 添加dsp父类型id，由于一个adx类型id有可能对应这多个dsp一级类型，所以数据有可能为 20_30_2
	 * */
	private String parentId;
	
	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	/**
	 * 底价范围查询
	 */
	private Long floorPriceRange;

	public AdPlace() {
		super();
	}

	public AdPlace(Long id, String channelAdplaceId, String channelAdplaceName, String channelName, String previewUrl, String size, AdType adType, String website, String websiteType,
			Long dailyTraffics, Double ctr) {
		super();
		this.id = id;
		this.channelAdplaceId = channelAdplaceId;
		this.channelAdplaceName = channelAdplaceName;
		this.channelName = channelName;
		this.previewUrl = previewUrl;
		this.size = size;
		// this.trafficsType = trafficsType;
		this.adType = adType;
		this.website = website;
		this.websiteType = websiteType;
		this.dailyTraffics = dailyTraffics;
		this.ctr = ctr;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPreviewUrl() {
		return previewUrl;
	}

	public void setPreviewUrl(String previewUrl) {
		this.previewUrl = previewUrl;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getWebsiteType() {
		return websiteType;
	}

	public void setWebsiteType(String websiteType) {
		this.websiteType = websiteType;
	}

	public Long getDailyTraffics() {
		return dailyTraffics;
	}

	public void setDailyTraffics(Long dailyTraffics) {
		this.dailyTraffics = dailyTraffics;
	}

	public Double getCtr() {
		return ctr;
	}

	public void setCtr(Double ctr) {
		this.ctr = ctr;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getChannelAdplaceId() {
		return channelAdplaceId;
	}

	public void setChannelAdplaceId(String channelAdplaceId) {
		this.channelAdplaceId = channelAdplaceId;
	}

	public String getChannelAdplaceName() {
		return channelAdplaceName;
	}

	public void setChannelAdplaceName(String channelAdplaceName) {
		this.channelAdplaceName = channelAdplaceName;
	}
	
	public AdType getAdType() {
		return adType;
	}

	public void setAdType(AdType adType) {
		this.adType = adType;
	}


	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}

	public Integer getPlaceType() {
		return placeType;
	}

	public void setPlaceType(Integer placeType) {
		this.placeType = placeType;
	}

	public Long getFloorPriceRange() {
		if (null == this.floorPriceRange) {
			floorPriceRange = new Long(0);
		}
		return floorPriceRange;
	}

	public void setFloorPriceRange(Long floorPriceRange) {
		this.floorPriceRange = floorPriceRange;
	}

	public Integer getScreenType() {
		return screenType;
	}

	public void setScreenType(Integer screenType) {
		this.screenType = screenType;
	}

	

	public String getWebSiteTypeId() {
		return webSiteTypeId;
	}

	public void setWebSiteTypeId(String webSiteTypeId) {
		this.webSiteTypeId = webSiteTypeId;
	}
    // 添加屏数类型字段screentype，0为其他，1为首屏，2为二屏幕
	public String getShowScreenType() {
		 
		if(null == this.screenType){
			return "";
		}
		if(0 == this.screenType){
			showScreenType="其他";
		 }else if(1 == this.screenType){
				showScreenType="首屏";
		 }
		 else if(2 == this.screenType){
				showScreenType="二屏幕";
		 }
		return showScreenType;
	}

	public void setShowScreenType(String showScreenType) {
		this.showScreenType = showScreenType;
	}
	public String getWebsiteTypeDesc() {
		return websiteTypeDesc;
	}

	public void setWebsiteTypeDesc(String websiteTypeDesc) {
		this.websiteTypeDesc = websiteTypeDesc;
	}
}
