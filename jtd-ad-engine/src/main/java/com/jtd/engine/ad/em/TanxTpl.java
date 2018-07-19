package com.jtd.engine.ad.em;

import java.util.HashSet;
import java.util.Set;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月8日
 * @项目名称 dsp-engine
 * @描述 <p></p>
 */
public enum TanxTpl {
	
	TPL1(1, "信息流","320x150", new String[]{"img_url", "title", "click_url", "open_type"}, new String[]{"price", "promoprice", "sell"}),
	TPL2(2, "信息流","640x290", new String[]{"img_url", "title", "click_url", "open_type"}, new String[]{}),
	TPL3(3, "信息流","200x100", new String[]{"img_url", "title", "ad_words", "click_url", "open_type"}, new String[]{}),
	TPL4(4, "信息流","640x100", new String[]{"img_url", "title", "click_url", "open_type"}, new String[]{}),
	TPL5(5, "信息流","800x450", new String[]{"img_url", "title", "click_url", "open_type"}, new String[]{}),
	TPL6(6, "信息流","640x300", new String[]{"img_url", "title", "click_url", "open_type"}, new String[]{}),
	TPL7(7, "信息流","640x200", new String[]{"img_url", "title", "click_url", "open_type"}, new String[]{}),
	TPL8(8, "信息流","480x150", new String[]{"img_url", "title", "ad_words", "click_url", "open_type"}, new String[]{}),
	TPL9(9, "轮播图","640x320", new String[]{"img_url", "click_url", "open_type"}, new String[]{}),
	TPL10(10, "轮播图","640x290", new String[]{"img_url", "click_url", "open_type"}, new String[]{}),
	TPL11(11, "信息流","200x100", new String[]{"img_url", "title", "ad_words", "click_url", "open_type"}, new String[]{}),
	TPL20(20, "信息流","450x300", new String[]{"img_url", "title", "click_url", "open_type"}, new String[]{"ad_words"}),
	TPL25(25, "信息流","640x320", new String[]{"img_url", "title", "ad_words", "click_url", "open_type"}, new String[]{}),
	TPL40(40, "信息流","320x150", new String[]{"img_url", "download_url", "download_type", "title"}, new String[]{}),
	TPL42(42, "轮播图","640x290", new String[]{"img_url", "download_url", "download_type"}, new String[]{}),
	TPL43(43, "轮播图","640x320", new String[]{"img_url", "download_url", "download_type"}, new String[]{}),
	TPL44(44, "信息流","640x300", new String[]{"img_url", "title", "download_url", "download_type"}, new String[]{}),
	TPL45(45, "信息流","640x100", new String[]{"img_url", "title", "download_url", "download_type"}, new String[]{}),
	TPL46(46, "信息流","450x300", new String[]{"img_url", "download_url", "download_type"}, new String[]{}),
	TPL48(48, "信息流","480x480", new String[]{"img_url", "title", "click_url"}, new String[]{}),
	TPL50(50, "信息流","150x200", new String[]{"img_url", "title", "ad_words", "description", "click_url", "download_url"}, new String[]{"download_type", "deeplink_url"}),
	TPL51(51, "信息流","720x405", new String[]{"img_url", "title", "ad_words", "click_url", "download_url"}, new String[]{"download_type", "deeplink_url"}),
	TPL52(52, "信息流","200x200", new String[]{"img_url", "title", "description", "click_url", "download_url"}, new String[]{"download_type", "deeplink_url"}),
	TPL53(53, "信息流","720x240", new String[]{"img_url", "title", "ad_words", "click_url", "download_url"}, new String[]{"download_type", "deeplink_url"}),
	TPL54(54, "信息流","720x360", new String[]{"img_url", "click_url", "download_url"}, new String[]{"download_type", "deeplink_url"}),
	TPL55(55, "轮播图","720x240", new String[]{"img_url", "click_url"}, new String[]{});

	private final int tplid;
	private final String style;
	private final String imgSize;
	private final Set<String> params;
	private final Set<String> options;

	private TanxTpl(int tplid, String style, String imgSize, String[] params, String[] options) {
		this.tplid = tplid;
		this.style = style;
		this.imgSize = imgSize;
		this.params = new HashSet<String>();
		for(String p : params) this.params.add(p);
		this.options = new HashSet<String>();
		for(String o : options) this.options.add(o);
	}

	public int getTplid() {
		return tplid;
	}

	public String getStyle() {
		return style;
	}

	public String getImgSize() {
		return imgSize;
	}

	public Set<String> getParams() {
		return params;
	}

	public Set<String> getOptions() {
		return options;
	}

	public static TanxTpl fromTplId(String tplid) {
		if (tplid == null) return null;
		try {
			return fromTplId(Integer.parseInt(tplid));
		} catch (Exception e) {
		}
		return null;
	}

	public static TanxTpl fromTplId(int tplid) {
		switch (tplid) {
		case 1:
			return TPL1;
		case 2:
			return TPL2;
		case 3:
			return TPL3;
		case 4:
			return TPL4;
		case 5:
			return TPL5;
		case 6:
			return TPL6;
		case 7:
			return TPL7;
		case 8:
			return TPL8;
		case 9:
			return TPL9;
		case 10:
			return TPL10;
		case 11:
			return TPL11;
		case 20:
			return TPL20;
		case 25:
			return TPL25;
		case 40:
			return TPL40;
		case 42:
			return TPL42;
		case 43:
			return TPL43;
		case 44:
			return TPL44;
		case 45:
			return TPL45;
		case 46:
			return TPL46;
		case 48:
			return TPL48;
		case 50:
			return TPL50;
		case 51:
			return TPL51;
		case 52:
			return TPL52;
		case 53:
			return TPL53;
		case 54:
			return TPL54;
		case 55:
			return TPL55;
		}
		return null;
	}
}
