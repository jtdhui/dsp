package com.jtd.web.po;

import com.jtd.commons.entity.BaseEntity;

import java.util.Date;

public class CeCompany extends BaseEntity{

    private String code;

    private String companyName;

    private long regionId;

    private Byte source;

    private Date createTime;

    private String remark;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName == null ? null : companyName.trim();
    }

    public long getRegionId() {
        return regionId;
    }

    public void setRegionId(long regionId) {
        this.regionId = regionId;
    }

    public Byte getSource() {
        return source;
    }

    public void setSource(Byte source) {
        this.source = source;
    }

}