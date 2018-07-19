package com.jtd.web.vo;

import java.util.List;

import com.jtd.web.model.AdPlace;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月25日
 * @项目名称 dsp-admin
 * @描述 <p></p>
 */
public class AdPlaceVo {
	private String website;
	private List<AdPlace> places;
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	public List<AdPlace> getPlaces() {
		return places;
	}
	public void setPlaces(List<AdPlace> places) {
		this.places = places;
	}
}
