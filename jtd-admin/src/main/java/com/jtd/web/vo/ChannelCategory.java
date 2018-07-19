package com.jtd.web.vo;

import java.util.Map;

import com.jtd.web.constants.CatgSerial;


/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月25日
 * @项目名称 dsp-admin
 * @描述 <p>渠道行业类别</p>
 */
public class ChannelCategory {
	private Long id;
	private String name;
	/**
	 * DSP的行业类别和其他渠道行业类别的对应
	 */
	private Map<CatgSerial, Long> channelCategorys;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<CatgSerial, Long> getChannelCategorys() {
		return channelCategorys;
	}

	public void setChannelCategorys(Map<CatgSerial, Long> channelCategorys) {
		this.channelCategorys = channelCategorys;
	}

}
