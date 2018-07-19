package com.jtd.web.po;

import com.jtd.commons.entity.BaseEntity;

public class TopDomain extends BaseEntity{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String domain;

    private String siteName;

    private Integer siteNum;

    private String priceRange;

    private Long flow;

    private Long channelId;

    private String webSiteType;

    private Long pid;

    private Integer maxPrice;

    private Integer minPrice;

    private String adxWebsiteType;

    private String parentid;

    private Integer manualEntry;

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain == null ? null : domain.trim();
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName == null ? null : siteName.trim();
    }

    public Integer getSiteNum() {
        return siteNum;
    }

    public void setSiteNum(Integer siteNum) {
        this.siteNum = siteNum;
    }

    public String getPriceRange() {
        return priceRange;
    }

    public void setPriceRange(String priceRange) {
        this.priceRange = priceRange == null ? null : priceRange.trim();
    }

    public Long getFlow() {
        return flow;
    }

    public void setFlow(Long flow) {
        this.flow = flow;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public String getWebSiteType() {
        return webSiteType;
    }

    public void setWebSiteType(String webSiteType) {
        this.webSiteType = webSiteType == null ? null : webSiteType.trim();
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public Integer getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Integer maxPrice) {
        this.maxPrice = maxPrice;
    }

    public Integer getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Integer minPrice) {
        this.minPrice = minPrice;
    }

    public String getAdxWebsiteType() {
        return adxWebsiteType;
    }

    public void setAdxWebsiteType(String adxWebsiteType) {
        this.adxWebsiteType = adxWebsiteType == null ? null : adxWebsiteType.trim();
    }

    public String getParentid() {
        return parentid;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid == null ? null : parentid.trim();
    }

    public Integer getManualEntry() {
        return manualEntry;
    }

    public void setManualEntry(Integer manualEntry) {
        this.manualEntry = manualEntry;
    }
}