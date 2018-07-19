package com.jtd.web.service.adx.vam;

public class MobileCreativeEntity extends CreativeEntity {
	/**
	 * 创意上传方式 1-inapp，2-web
	 */
	private Integer mode;
	/**
	 * 创意代码 <br>
	 * mode =2时有效
	 */
	private String html_snippet;
	/**
	 * 1-3000之间的整数
	 */
	private Integer width;
	/**
	 * 1-2000之间的整数
	 */
	private Integer height;
	/**
	 * 1-Banner广告 <br>
	 * 2-开屏广告 <br>
	 * 3-插屏广告 <br>
	 * 4-信息流广告
	 */
	private Integer adtype;
	/**
	 * 创意展示格式：1-STATIC_PIC , 2-DYNAMIC_PIC , 3-SWF ,4-TXT
	 */
	private Integer format;
	/**
	 * 创意行业类别
	 */
	private Integer category;
	/**
	 * 点击跳转目标地址主域名（对应系统的落地页域名）
	 */
	private String[] adomain_list;
	/**
	 * 点击跳转地址URL<br>
	 * 需要包含双方点击检测宏{!vam_click_url}(或{!vam_click_url_esc})和{!dsp_click_url}<br>
	 * mode =1 有效
	 */
	private String landingpage;
	/**
	 * 素材连接地址 <br>
	 * (如果adtype =1 ,pic_urls,title,text不能同时为空) <br>
	 * 如果adtype=2或adtype=3，必填<br>
	 * mode =1 有效
	 */
	private String[] pic_urls;
	/**
	 * 创意标题 <br>
	 * (如果adtype =1 ,pic_urls,title,text不能同时为空) <br>
	 * 如果adtype=4，必填<br>
	 * mode =1 有效
	 */
	private String title;
	/**
	 * 创意文字内容 <br>
	 * (如果adtype =1 ,pic_urls,title,text不能同时为空) <br>
	 * 如果adtype=4，必填<br>
	 * mode =1 有效
	 */
	private String text;

	public MobileCreativeEntity() {
	}

	public MobileCreativeEntity(String id, String html_snippet, Integer width,
			Integer height, Integer adtype, Integer format, Integer category,
			String[] adomain_list) {
		super.id = id;
		this.html_snippet = html_snippet;
		this.width = width;
		this.height = height;
		this.adtype = adtype;
		this.format = format;
		this.category = category;
		this.adomain_list = adomain_list;
	}
	
	public MobileCreativeEntity(String id, String landingpage, Integer width,
			Integer height, Integer adtype, Integer format, Integer category,
			String[] adomain_list, String[] pic_urls, String title, String text) {
		super.id = id;
		this.landingpage = landingpage;
		this.width = width;
		this.height = height;
		this.adtype = adtype;
		this.format = format;
		this.category = category;
		this.adomain_list = adomain_list;
		this.pic_urls = pic_urls;
		this.title = title;
		this.text = text;
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

	public String[] getAdomain_list() {
		return adomain_list;
	}

	public void setAdomain_list(String[] adomain_list) {
		this.adomain_list = adomain_list;
	}

	public String getLandingpage() {
		return landingpage;
	}

	public void setLandingpage(String landingpage) {
		this.landingpage = landingpage;
	}

	public Integer getAdtype() {
		return adtype;
	}

	public void setAdtype(Integer adtype) {
		this.adtype = adtype;
	}

	public String[] getPic_urls() {
		return pic_urls;
	}

	public void setPic_urls(String[] pic_urls) {
		this.pic_urls = pic_urls;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Integer getMode() {
		return mode;
	}

	public void setMode(Integer mode) {
		this.mode = mode;
	}

	public String getHtml_snippet() {
		return html_snippet;
	}

	public void setHtml_snippet(String html_snippet) {
		this.html_snippet = html_snippet;
	}
}