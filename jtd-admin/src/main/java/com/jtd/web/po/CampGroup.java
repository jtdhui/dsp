package com.jtd.web.po;

import java.util.Date;

import com.jtd.commons.entity.BaseEntity;

/**
 * 
 * @作者 duber
 * @版本 V1.0
 * @创建日期 2016年10月18日
 * @描述	推广(广告组)组
 */
public class CampGroup  extends BaseEntity{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    private Long partnerId;

    private String groupName;

    private Long dailybudget;

    private Long totalbudget;

    private Long dailypv;

    private Long dailyclick;

    private Long totalpv;

    private Long totalclick;

    private String description;

    private Date modifytime;

    private Integer deletestatus;

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
        this.groupName = groupName == null ? null : groupName.trim();
    }

    public Long getDailybudget() {
        return dailybudget;
    }

    public void setDailybudget(Long dailybudget) {
        this.dailybudget = dailybudget;
    }

    public Long getTotalbudget() {
        return totalbudget;
    }

    public void setTotalbudget(Long totalbudget) {
        this.totalbudget = totalbudget;
    }

    public Long getDailypv() {
        return dailypv;
    }

    public void setDailypv(Long dailypv) {
        this.dailypv = dailypv;
    }

    public Long getDailyclick() {
        return dailyclick;
    }

    public void setDailyclick(Long dailyclick) {
        this.dailyclick = dailyclick;
    }

    public Long getTotalpv() {
        return totalpv;
    }

    public void setTotalpv(Long totalpv) {
        this.totalpv = totalpv;
    }

    public Long getTotalclick() {
        return totalclick;
    }

    public void setTotalclick(Long totalclick) {
        this.totalclick = totalclick;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Date getModifytime() {
        return modifytime;
    }

    public void setModifytime(Date modifytime) {
        this.modifytime = modifytime;
    }

    public Integer getDeletestatus() {
        return deletestatus;
    }

    public void setDeletestatus(Integer deletestatus) {
        this.deletestatus = deletestatus;
    }
}