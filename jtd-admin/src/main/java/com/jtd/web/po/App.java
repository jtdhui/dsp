package com.jtd.web.po;

import com.jtd.commons.entity.BaseEntity;

public class App extends BaseEntity{

    private Long channelId;

    private String pkName;

    private String appId;

    private String company;

    private String appName;

    private Long floorPrice;

    private String appType;

    private Long dailyTraffics;

    private Integer deleteStatus;

    private Integer placeType;

    private String apptypeId;

    private String parentId;

    private String osType;

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public String getPkName() {
        return pkName;
    }

    public void setPkName(String pkName) {
        this.pkName = pkName == null ? null : pkName.trim();
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName == null ? null : appName.trim();
    }

    public Long getFloorPrice() {
        return floorPrice;
    }

    public void setFloorPrice(Long floorPrice) {
        this.floorPrice = floorPrice;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType == null ? null : appType.trim();
    }

    public Long getDailyTraffics() {
        return dailyTraffics;
    }

    public void setDailyTraffics(Long dailyTraffics) {
        this.dailyTraffics = dailyTraffics;
    }

    public Integer getDeleteStatus() {
        return deleteStatus;
    }

    public void setDeleteStatus(Integer deleteStatus) {
        this.deleteStatus = deleteStatus;
    }

    public Integer getPlaceType() {
        return placeType;
    }

    public void setPlaceType(Integer placeType) {
        this.placeType = placeType;
    }

    public String getApptypeId() {
        return apptypeId;
    }

    public void setApptypeId(String apptypeId) {
        this.apptypeId = apptypeId == null ? null : apptypeId.trim();
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId == null ? null : parentId.trim();
    }

    public String getOsType() {
        return osType;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setOsType(String osType) {
        this.osType = osType == null ? null : osType.trim();
    }
}

