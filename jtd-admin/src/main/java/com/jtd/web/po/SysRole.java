package com.jtd.web.po;

import com.jtd.commons.entity.BaseEntity;

public class SysRole extends BaseEntity{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    private String name;

    private String available;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available == null ? null : available.trim();
    }
}