package com.jtd.web.po;

import com.jtd.commons.entity.BaseEntity;

/**
 * 
 * @作者 duber
 * @版本 V1.0
 * @创建日期 2016年10月18日
 * @描述	人群包数据
 */
public class CookiePacket  extends BaseEntity{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long cookieGid;

    private Long ownerPartnerId;

    private String packetName;

    private Integer isPublic;

    private Integer cookieNum;

    private Integer deleteStatus;

    public Long getCookieGid() {
        return cookieGid;
    }

    public void setCookieGid(Long cookieGid) {
        this.cookieGid = cookieGid;
    }

    public Long getOwnerPartnerId() {
        return ownerPartnerId;
    }

    public void setOwnerPartnerId(Long ownerPartnerId) {
        this.ownerPartnerId = ownerPartnerId;
    }

    public String getPacketName() {
        return packetName;
    }

    public void setPacketName(String packetName) {
        this.packetName = packetName == null ? null : packetName.trim();
    }

    public Integer getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Integer isPublic) {
        this.isPublic = isPublic;
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