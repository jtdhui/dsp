package com.jtd.web.po;

import com.jtd.commons.entity.BaseEntity;

/**
 * 
 * @作者 duber
 * @版本 V1.0
 * @创建日期 2016年10月18日
 * @描述	素材尺寸字典表
 */
public class CreativeSize  extends BaseEntity{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String size;

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size == null ? null : size.trim();
    }
}