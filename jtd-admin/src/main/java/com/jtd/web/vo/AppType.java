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
 * @描述 <p>应用类型</p>
 */
public class AppType {
	private String id;
	private String name;
	private Map<String, AppType> subAppTypes;
	private Map<CatgSerial, String> channelAppTypes;

	public AppType() {
	}

	public AppType(String id, String name) {
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

	public Map<String, AppType> getSubAppTypes() {
		return subAppTypes;
	}

	public void setSubAppTypes(Map<String, AppType> subAppTypes) {
		this.subAppTypes = subAppTypes;
	}

	public Map<CatgSerial, String> getChannelAppTypes() {
		return channelAppTypes;
	}

	public void setChannelAppTypes(Map<CatgSerial, String> channelAppTypes) {
		this.channelAppTypes = channelAppTypes;
	}

	public void addSubAppType(AppType appType) {
		if (subAppTypes == null) {
			subAppTypes = new HashMap<String, AppType>();
		}
		subAppTypes.put(appType.getId(), appType);
	}
}
