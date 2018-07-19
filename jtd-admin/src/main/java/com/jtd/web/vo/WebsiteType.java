package com.jtd.web.vo;

import java.util.HashMap;
import java.util.Map;

import com.jtd.web.constants.CatgSerial;


/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月25日
 * @项目名称 dsp-admin
 * @描述 <p></p>
 */
public class WebsiteType {
	private String id;
	private String name;
	private Map<String, WebsiteType> subWebsiteTypes;
	private Map<CatgSerial, String> channelWebsiteTypes;

	public WebsiteType() {
	}

	public WebsiteType(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, WebsiteType> getSubWebsiteTypes() {
		return subWebsiteTypes;
	}

	public void setSubWebsiteTypes(Map<String, WebsiteType> subWebsiteTypes) {
		this.subWebsiteTypes = subWebsiteTypes;
	}

	public Map<CatgSerial, String> getChannelWebsiteTypes() {
		return channelWebsiteTypes;
	}

	public void setChannelWebsiteTypes(Map<CatgSerial, String> channelWebsiteTypes) {
		this.channelWebsiteTypes = channelWebsiteTypes;
	}

	public void addSubWebsiteType(WebsiteType websiteType) {
		if (subWebsiteTypes == null) {
			subWebsiteTypes = new HashMap<String, WebsiteType>();
		}
		subWebsiteTypes.put(websiteType.getId(), websiteType);
	}
}
