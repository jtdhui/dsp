package com.jtd.web.vo;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月25日
 * @项目名称 dsp-admin
 * @描述 <p></p>
 */
public class City {
	public City(int id, String name, int parent, int cityLevel, int type) {
		super();
		this.id = id;
		this.name = name;
		this.parent = parent;
		this.cityLevel = cityLevel;
		this.type = type;
	}

	private int id;
	private String name;
	private int parent;
	private int cityLevel;// 1、2、3、4、海外（10）线城市
	private int type;// 1：大区 2：省 3：市

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getParent() {
		return parent;
	}

	public void setParent(int parent) {
		this.parent = parent;
	}

	public int getCityLevel() {
		return cityLevel;
	}

	public void setCityLevel(int cityLevel) {
		this.cityLevel = cityLevel;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
