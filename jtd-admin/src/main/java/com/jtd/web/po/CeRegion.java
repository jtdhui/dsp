package com.jtd.web.po;

import com.jtd.commons.entity.BaseEntity;

public class CeRegion extends BaseEntity {

    private String code;

    private String name;

    private Byte source;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Byte getSource() {
        return source;
    }

    public void setSource(Byte source) {
        this.source = source;
    }
}

