package com.jtd.web.vo;

import com.jtd.web.constants.PartnerType;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月25日
 * @项目名称 dsp-admin
 * @描述 <p>合作伙伴</p>
 */
public class PartnerVo {
	private Long id;
	/**
	 * 父ID
	 */
	private Long pid;

	/**
	 * 类型
	 */
	private PartnerType partnerType;
	/**
	 * 简称
	 */
	private String simpleName;
	/**
	 * 企业主体名称
	 */
	private String partnerName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPid() {
		return pid;
	}

	public void setPid(Long pid) {
		this.pid = pid;
	}

	public PartnerType getPartnerType() {
		return partnerType;
	}

	public void setPartnerType(PartnerType partnerType) {
		this.partnerType = partnerType;
	}

	public String getSimpleName() {
		return simpleName;
	}

	public void setSimpleName(String simpleName) {
		this.simpleName = simpleName;
	}

	public String getPartnerName() {
		return partnerName;
	}

	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
	}

}
