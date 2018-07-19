package com.jtd.web.service.adx.vam;

import com.alibaba.fastjson.JSONArray;

/**
 * 视频创意实体
 */
public class VideoCreativeEntity extends CreativeEntity {
	

	/**
	 * 视频创意时长
	 */
	private Integer duration;
	/**
	 * 存放于DSP的CDN的视频素材地址
	 */
	private String fileurl;
	/**
	 * 点击跳转url，需包含双方 点击检测宏{!vam_click_url}(或 {!vam_click_url_esc})和
	 * {!dsp_click_url}。
	 */
	private String landingpage;
	/**
	 * 落地页主域名
	 */
	private String[] adomain_list;
	/**
	 * 素材分辨率宽,1-3000 之间整数
	 */
	private Integer width;
	/**
	 * 素材分辨率高,1-2000 之间整数
	 */
	private Integer height;
	/**
	 * 创意素材格式:1-STATIC_PIC, 2-DYNAMIC_PIC,3-SWF,5-FLV, 6-MP4
	 */
	private Integer format;
	/**
	 * 广告主的中文名称(youku 要求必 填项)
	 */
	private String advertiser;
	/**
	 * 创意类型:0-贴片(默认),1- 角标,2-overlay
	 */
	private Integer creative_type;

	/**
	 * 各个媒体同步结果,当创意审核通过时 会返回<br>
	 * sellerid string 媒体账号 id。<br>
	 * status number 媒体的视频创意审核状态,状态如下: 1:媒体待审核, 2:媒体审核通过, 3:媒体审核不通过。
	 */
	private JSONArray syncstatus;

	public VideoCreativeEntity() {
		super();
	}

	public VideoCreativeEntity(String id, Integer category, Integer duration,
			String fileurl, String landingpage, String[] adomain_list,
			Integer width, Integer height, Integer format, String advertiser,
			Integer creative_type) {
		super();
		this.id = id;
		this.category = category;
		this.duration = duration;
		this.fileurl = fileurl;
		this.landingpage = landingpage;
		this.adomain_list = adomain_list;
		this.width = width;
		this.height = height;
		this.format = format;
		this.advertiser = advertiser;
		this.creative_type = creative_type;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public String getFileurl() {
		return fileurl;
	}

	public void setFileurl(String fileurl) {
		this.fileurl = fileurl;
	}

	public String getLandingpage() {
		return landingpage;
	}

	public void setLandingpage(String landingpage) {
		this.landingpage = landingpage;
	}

	public String[] getAdomain_list() {
		return adomain_list;
	}

	public void setAdomain_list(String[] adomain_list) {
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

	public String getAdvertiser() {
		return advertiser;
	}

	public void setAdvertiser(String advertiser) {
		this.advertiser = advertiser;
	}

	public Integer getCreative_type() {
		return creative_type;
	}

	public void setCreative_type(Integer creative_type) {
		this.creative_type = creative_type;
	}

	public JSONArray getSyncstatus() {
		return syncstatus;
	}

	public void setSyncstatus(JSONArray syncstatus) {
		this.syncstatus = syncstatus;
	}
}
