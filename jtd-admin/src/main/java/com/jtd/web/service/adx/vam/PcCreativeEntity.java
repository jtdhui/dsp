package com.jtd.web.service.adx.vam;

/**
 * pc创意实体类
 * 
 */
public class PcCreativeEntity extends CreativeEntity {

	/**
	 * 1-3000之间的整数
	 */
	private Integer width;
	/**
	 * 1-3000之间的整数
	 */
	private Integer height;
	/**
	 * 创意展示格式：1-STATIC_PIC , 2-DYNAMIC_PIC , 3-SWF ,4-TXT
	 */
	private Integer format;
	/**
	 * 创意行业类别
	 */
	private Integer category;
	/**
	 * 创意代码
	 */
	private String html_snippet;
	/**
	 * 点击跳转目标地址主域名（对应系统的落地页域名）
	 */
	private String[] adomain_list;

	public PcCreativeEntity() {
	}

	public PcCreativeEntity(String id, Integer width, Integer height,
			Integer format, Integer category, String html_snippet,
			String[] adomain_list) {
		super.id = id;
		this.width = width;
		this.height = height;
		this.format = format;
		this.category = category;
		this.html_snippet = html_snippet;
		this.adomain_list = adomain_list;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Integer getFormat() {
		return format;
	}

	public void setFormat(Integer format) {
		this.format = format;
	}

	public Integer getCategory() {
		return category;
	}

	public void setCategory(Integer category) {
		this.category = category;
	}

	public String getHtml_snippet() {
		return html_snippet;
	}

	public void setHtml_snippet(String html_snippet) {
		this.html_snippet = html_snippet;
	}

	public String[] getAdomain_list() {
		return adomain_list;
	}

	public void setAdomain_list(String[] adomain_list) {
		this.adomain_list = adomain_list;
	}
}
