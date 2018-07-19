package com.jtd.web.constants;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月24日
 * @项目名称 dsp-common-entity
 * @描述 着陆页类型
 */
public enum LandingType {

	// OPENURL(0, "打开网页"), ANDROIDDOWNLOAD(1, "安卓-下载"),IOSAPPSTORE(2,"苹果-打开AppStore");
	BROWSEROPEN(0, "浏览器打开"), WEBVIEWOPEN(1, "webview打开"),APPDOWNLOAD(2,"应用下载");
	private LandingType(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	private final int code;
	private final String desc;

	public int getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}
	
	public static LandingType fromCode(int code) {
		if (code < 0 || code > 2)
			return null;
		return new LandingType[] { BROWSEROPEN, WEBVIEWOPEN, APPDOWNLOAD}[code];
	}
}
