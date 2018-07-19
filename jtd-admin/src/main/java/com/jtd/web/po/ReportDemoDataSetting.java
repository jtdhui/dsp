package com.jtd.web.po;

import com.jtd.commons.entity.BaseEntity;

import java.math.BigDecimal;

public class ReportDemoDataSetting extends BaseEntity {

    private Long partnerId;

    private Integer minPv;

    private Integer maxPv;

    private BigDecimal minUvRatio;

    private BigDecimal maxUvRatio;

    private BigDecimal minUclickRatio;

    private BigDecimal maxUclickRatio;

    private BigDecimal grossProfit;

    public Long getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Long partnerId) {
        this.partnerId = partnerId;
    }

    public Integer getMinPv() {
        return minPv;
    }

    public void setMinPv(Integer minPv) {
        this.minPv = minPv;
    }

    public Integer getMaxPv() {
        return maxPv;
    }

    public void setMaxPv(Integer maxPv) {
        this.maxPv = maxPv;
    }

    public BigDecimal getMinUvRatio() {
        return minUvRatio;
    }

    public void setMinUvRatio(BigDecimal minUvRatio) {
        this.minUvRatio = minUvRatio;
    }

    public BigDecimal getMaxUvRatio() {
        return maxUvRatio;
    }

    public void setMaxUvRatio(BigDecimal maxUvRatio) {
        this.maxUvRatio = maxUvRatio;
    }

    public BigDecimal getMinUclickRatio() {
        return minUclickRatio;
    }

    public void setMinUclickRatio(BigDecimal minUclickRatio) {
        this.minUclickRatio = minUclickRatio;
    }

    public BigDecimal getMaxUclickRatio() {
        return maxUclickRatio;
    }

    public void setMaxUclickRatio(BigDecimal maxUclickRatio) {
        this.maxUclickRatio = maxUclickRatio;
    }

    public BigDecimal getGrossProfit() {
        return grossProfit;
    }

    public void setGrossProfit(BigDecimal grossProfit) {
        this.grossProfit = grossProfit;
    }
}

