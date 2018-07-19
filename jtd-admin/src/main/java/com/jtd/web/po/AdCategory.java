package com.jtd.web.po;

import com.jtd.commons.entity.BaseEntity;

/**
 * 
 * @作者 duber
 * @版本 V1.0
 * @创建日期 2016年10月18日
 * @描述	ad行业类别
 */
public class AdCategory  extends BaseEntity{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long campaignId;

    private Long adId;

    private String catgserial;

    private String catgId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Long campaignId) {
        this.campaignId = campaignId;
    }

    public Long getAdId() {
        return adId;
    }

    public void setAdId(Long adId) {
        this.adId = adId;
    }

    public String getCatgserial() {
        return catgserial;
    }

    public void setCatgserial(String catgserial) {
        this.catgserial = catgserial == null ? null : catgserial.trim();
    }

    public String getCatgId() {
        return catgId;
    }

    public void setCatgId(String catgId) {
        this.catgId = catgId == null ? null : catgId.trim();
    }
}