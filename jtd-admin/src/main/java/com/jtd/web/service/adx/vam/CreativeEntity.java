package com.jtd.web.service.adx.vam;

public class CreativeEntity {
	/**
	 * DSP 自己系统的创意ID（对应 AD的id）
	 */
	protected String id;
	/**
	 * 创意审核状态 <br>
	 * 1:待审核 <br>
	 * 2:通过 <br>
	 * 3:不通过
	 */
	protected Integer status;
	
	/**
	 * 创意行业类别id
	 */
	protected Integer category;

	public CreativeEntity() {
	}

	public CreativeEntity(String id, Integer status) {
		super();
		this.id = id;
		this.status = status;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	

	public Integer getCategory() {
		return category;
	}

	public void setCategory(Integer category) {
		this.category = category;
	}

}
