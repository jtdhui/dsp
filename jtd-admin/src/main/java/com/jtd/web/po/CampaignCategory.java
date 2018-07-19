package com.jtd.web.po;

import com.jtd.commons.entity.BaseEntity;

/**
 * 
 * @作者 duber
 * @版本 V1.0
 * @创建日期 2016年10月18日
 * @描述	广告活动选择的渠道的行业类别
 */
public class CampaignCategory  extends BaseEntity{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long campaignId;

    private String catgserial;

    private String catgid;

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

    public String getCatgserial() {
        return catgserial;
    }

    public void setCatgserial(String catgserial) {
        this.catgserial = catgserial == null ? null : catgserial.trim();
    }

    public String getCatgid() {
        return catgid;
    }

    public void setCatgid(String catgid) {
        this.catgid = catgid == null ? null : catgid.trim();
    }
}