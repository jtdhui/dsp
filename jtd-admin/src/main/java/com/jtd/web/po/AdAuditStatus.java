package com.jtd.web.po;

import java.util.Date;

import com.jtd.commons.entity.BaseEntity;
import com.jtd.web.constants.AuditStatus;

/**
 * 
 * @作者 duber
 * @版本 V1.0
 * @创建日期 2016年10月18日
 * @描述	广告素材渠道审核状态
 */
public class AdAuditStatus extends BaseEntity {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    private Long adId;

    private Long channelId;

    private AuditStatus status;

    private String auditInfo;
    
    private Date auditTime ;
    
    private Integer auditMqSuccess ;
    
    private Integer internalAuditMqSuccess ;

    public Long getAdId() {
        return adId;
    }

    public void setAdId(Long adId) {
        this.adId = adId;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public AuditStatus getStatus() {
        return status;
    }

    public void setStatus(AuditStatus status) {
        this.status = status;
    }

    public String getAuditInfo() {
        return auditInfo;
    }

    public void setAuditInfo(String auditInfo) {
        this.auditInfo = auditInfo == null ? null : auditInfo.trim();
    }

	public Date getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}

	public Integer getAuditMqSuccess() {
		return auditMqSuccess;
	}

	public void setAuditMqSuccess(Integer auditMqSuccess) {
		this.auditMqSuccess = auditMqSuccess;
	}

	public Integer getInternalAuditMqSuccess() {
		return internalAuditMqSuccess;
	}

	public void setInternalAuditMqSuccess(Integer internalAuditMqSuccess) {
		this.internalAuditMqSuccess = internalAuditMqSuccess;
	}
    
    
}