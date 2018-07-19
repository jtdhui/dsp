package com.jtd.web.po;

import com.jtd.commons.entity.BaseEntity;

import java.util.Date;

/**
 * 预充值流水表
 */
public class PartnerPreFlow  extends BaseEntity {

	private static final long serialVersionUID = 951388198808444948L;

	private Long partnerId;

    private String partnerName;
    
    /** 发起充值金额 */
    private Long preAmount;
    
    /** 审核充值金额 */
    private Long amount;

    private Long preGift;

    private Integer status;

    private Integer type;

    private Integer payType;

    private Long preOperatorId;

    private String preOperatorName;

    private Long operatorId;

    private String operatorName;

    private Long accFlowId;

    private Date modifyTime;

    private Integer isInvoice;

    private String invoice;

    public Long getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Long partnerId) {
        this.partnerId = partnerId;
    }

    public Long getPreAmount() {
        return preAmount;
    }

    public void setPreAmount(Long preAmount) {
        this.preAmount = preAmount;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getPreOperatorId() {
        return preOperatorId;
    }

    public void setPreOperatorId(Long preOperatorId) {
        this.preOperatorId = preOperatorId;
    }

    public String getPreOperatorName() {
        return preOperatorName;
    }

    public void setPreOperatorName(String preOperatorName) {
        this.preOperatorName = preOperatorName == null ? null : preOperatorName.trim();
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName == null ? null : operatorName.trim();
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public Long getAccFlowId() {
        return accFlowId;
    }

    public void setAccFlowId(Long accFlowId) {
        this.accFlowId = accFlowId;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Long getPreGift() {
        return preGift;
    }

    public void setPreGift(Long preGift) {
        this.preGift = preGift;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public Integer getIsInvoice() {
        return isInvoice;
    }

    public void setIsInvoice(Integer isInvoice) {
        this.isInvoice = isInvoice;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }
}