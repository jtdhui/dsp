package com.jtd.web.constants;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月24日
 * @项目名称 dsp-common-entity
 * @描述 广告类型
 */
public enum AdType {
	//pc
	PC_BANNER(0, "pc banner"), 
	PC_VIDEO(1, "pc视频"), 
	PC_URL(2, "文字链"), 
	PC_IMGANDTEXT(3, "图文"), 
	//app
	APP_BANNER(4, "banner in app"), 
	APP_FULLSCREEN(5, "插屏全屏 in app"), 
	APP_MESSAGE(6, "原生-信息流"),
	APP_FOCUS(7, "原生-焦点图"),
	APP_VIDEO(8,"视频贴片 in app"),
	/**
	APP_MESSAGE(7, "原生-信息流"),
	APP_FOCUS(8, "原生-焦点图"),
	APP_VIDEO(9,"视频贴片 in app"),
	**/
	//wap
//	WAP(6, "wap"), 
	WAP(9, "wap"), 
	//贴片
    WAP_VIDEO(10, "视频贴片-wap"),
    PC_VIDEO_PAUSE(11,"视频暂停-PC"),
    APP_VIDEO_PAUSE(12,"视频暂停-APP"),
    WAP_VIDEO_PAUSE(13,"视频暂停-WAP");

 
	private AdType(int code, String desc) {
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
	
	public static AdType getAdType(int code){
		
		AdType[] array = AdType.values();
		for (int i = 0; i < array.length; i++) {
			AdType adType = array[i];
			if(adType.getCode() == code){
				return adType ;
			}
		}
		
		return null ;
	}
}
