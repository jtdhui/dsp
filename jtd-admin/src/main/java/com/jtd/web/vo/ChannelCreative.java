package com.jtd.web.vo;


@Deprecated
public class ChannelCreative {
	private Long id;
	private String name;
	private String catgserial;
	private Long baidu;
	private Long taobao;
	private Long vam;

	public ChannelCreative() {
	}

	public ChannelCreative(Long id, String name, String catgserial) {
		this.id = id;
		this.name = name;
		this.catgserial = catgserial;
	}

	public ChannelCreative(Long id, String name, Long baidu, Long taobao, Long vam) {
		this.id = id;
		this.name = name;
		this.baidu = baidu;
		this.taobao = taobao;
		this.vam = vam;
	}

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

	public String getCatgserial() {
		return catgserial;
	}

	public void setCatgserial(String catgserial) {
		this.catgserial = catgserial;
	}

	public Long getBaidu() {
		return baidu;
	}

	public void setBaidu(Long baidu) {
		this.baidu = baidu;
	}

	public Long getTaobao() {
		return taobao;
	}

	public void setTaobao(Long taobao) {
		this.taobao = taobao;
	}

	public Long getVam() {
		return vam;
	}

	public void setVam(Long vam) {
		this.vam = vam;
	}
}
