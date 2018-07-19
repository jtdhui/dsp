package com.jtd.web.po;

import com.jtd.commons.entity.BaseEntity;

/**
 * 
 * @作者 duber
 * @版本 V1.0
 * @创建日期 2016年10月18日
 * @描述 广告位数据
 */
public class AdPlace  extends BaseEntity{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    private String channelAdPlaceId;

    private String channelAdPlaceName;

    private String channelName;

    private String previewUrl;

    private String size;

    private Integer adType;

    private String webSite;

    private String webSiteType;

    private Long dailyTraffics;

    private Double ctr;

    private Long channelId;

    private Integer placeType;

    private Integer floorPriceRange;
    
    private Integer screenType ;
    
    private String webSiteTypeId ;
    
    private String parentId ;

    public String getChannelAdPlaceId() {
        return channelAdPlaceId;
    }

    public void setChannelAdPlaceId(String channelAdPlaceId) {
        this.channelAdPlaceId = channelAdPlaceId == null ? null : channelAdPlaceId.trim();
    }

    public String getChannelAdPlaceName() {
        return channelAdPlaceName;
    }

    public void setChannelAdPlaceName(String channelAdPlaceName) {
        this.channelAdPlaceName = channelAdPlaceName == null ? null : channelAdPlaceName.trim();
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName == null ? null : channelName.trim();
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl == null ? null : previewUrl.trim();
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size == null ? null : size.trim();
    }

    public Integer getAdType() {
        return adType;
    }

    public void setAdType(Integer adType) {
        this.adType = adType;
    }

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite == null ? null : webSite.trim();
    }

    public String getWebSiteType() {
        return webSiteType;
    }

    public void setWebSiteType(String webSiteType) {
        this.webSiteType = webSiteType == null ? null : webSiteType.trim();
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

    public Integer getFloorPriceRange() {
        return floorPriceRange;
    }

    public void setFloorPriceRange(Integer floorPriceRange) {
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

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
    
}