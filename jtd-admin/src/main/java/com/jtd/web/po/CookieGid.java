package com.jtd.web.po;

import com.jtd.commons.entity.BaseEntity;

/**
 * 
 * @作者 duber
 * @版本 V1.0
 * @创建日期 2016年10月18日
 * @描述	人群属性
 */
public class CookieGid  extends BaseEntity{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String ckGroupName;

    private Integer ckType;

    private String ckProperty;

    private String dimName;

    private Integer cookieNum;

    private Integer deleteStatus;

    public String getCkGroupName() {
        return ckGroupName;
    }

    public void setCkGroupName(String ckGroupName) {
        this.ckGroupName = ckGroupName == null ? null : ckGroupName.trim();
    }

    public Integer getCkType() {
        return ckType;
    }

    public void setCkType(Integer ckType) {
        this.ckType = ckType;
    }

    public String getCkProperty() {
        return ckProperty;
    }

    public void setCkProperty(String ckProperty) {
        this.ckProperty = ckProperty == null ? null : ckProperty.trim();
    }

    public String getDimName() {
        return dimName;
    }

    public void setDimName(String dimName) {
        this.dimName = dimName == null ? null : dimName.trim();
    }

    public Integer getCookieNum() {
        return cookieNum;
    }

    public void setCookieNum(Integer cookieNum) {
        this.cookieNum = cookieNum;
    }

    public Integer getDeleteStatus() {
        return deleteStatus;
    }

    public void setDeleteStatus(Integer deleteStatus) {
        this.deleteStatus = deleteStatus;
    }
}