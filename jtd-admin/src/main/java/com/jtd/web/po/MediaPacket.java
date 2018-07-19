package com.jtd.web.po;

import com.jtd.commons.entity.BaseEntity;

/**
 * 
 * @作者 duber
 * @版本 V1.0
 * @创建日期 2016年10月18日
 * @描述	存储媒体包数据
 */
public class MediaPacket extends BaseEntity {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long ownerPartnerId;

    private String packetName;

    private Integer isPublic;

    private Integer mediaNum;

    private Integer deletestatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getMediaNum() {
        return mediaNum;
    }

    public void setMediaNum(Integer mediaNum) {
        this.mediaNum = mediaNum;
    }

    public Integer getDeletestatus() {
        return deletestatus;
    }

    public void setDeletestatus(Integer deletestatus) {
        this.deletestatus = deletestatus;
    }
}