package com.jtd.web.po.count;

import com.jtd.commons.entity.BaseEntity;

public class CampAdTpd extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long campId;

	private Integer adType;

	private Integer bid;

	private Integer pv;

	private Integer uv;

	private Integer click;

	private Integer uclick;

	private Integer arrpv;

	private Integer arruv;

	private Integer expend;

	private Integer cost;

	private Integer date;

	public Long getCampId() {
		return campId;
	}

	public void setCampId(Long campId) {
		this.campId = campId;
	}

	public Integer getAdType() {
		return adType;
	}

	public void setAdType(Integer adType) {
		this.adType = adType;
	}

	public Integer getBid() {
		return bid;
	}

	public void setBid(Integer bid) {
		this.bid = bid;
	}

	public Integer getPv() {
		return pv;
	}

	public void setPv(Integer pv) {
		this.pv = pv;
	}

	public Integer getUv() {
		return uv;
	}

	public void setUv(Integer uv) {
		this.uv = uv;
	}

	public Integer getClick() {
		return click;
	}

	public void setClick(Integer click) {
		this.click = click;
	}

	public Integer getUclick() {
		return uclick;
	}

	public void setUclick(Integer uclick) {
		this.uclick = uclick;
	}

	public Integer getArrpv() {
		return arrpv;
	}

	public void setArrpv(Integer arrpv) {
		this.arrpv = arrpv;
	}

	public Integer getArruv() {
		return arruv;
	}

	public void setArruv(Integer arruv) {
		this.arruv = arruv;
	}

	public Integer getExpend() {
		return expend;
	}

	public void setExpend(Integer expend) {
		this.expend = expend;
	}

	public Integer getCost() {
		return cost;
	}

	public void setCost(Integer cost) {
		this.cost = cost;
	}

	public Integer getDate() {
		return date;
	}

	public void setDate(Integer date) {
		this.date = date;
	}
}