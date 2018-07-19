package com.jtd.web.po;

import com.jtd.commons.entity.BaseEntity;

public class DspSalesProduct extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private String name;

	private Integer money;

	private Integer openMoney;

	private Integer serviceMoney;

	private Integer level;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name == null ? null : name.trim();
	}

	public Integer getMoney() {
		return money;
	}

	public void setMoney(Integer money) {
		this.money = money;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getOpenMoney() {
		return openMoney;
	}

	public void setOpenMoney(Integer openMoney) {
		this.openMoney = openMoney;
	}

	public Integer getServiceMoney() {
		return serviceMoney;
	}

	public void setServiceMoney(Integer serviceMoney) {
		this.serviceMoney = serviceMoney;
	}

}