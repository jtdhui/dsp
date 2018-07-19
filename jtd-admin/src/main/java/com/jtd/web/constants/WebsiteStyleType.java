package com.jtd.web.constants;

public enum WebsiteStyleType {
	
	DEFAULT("default","系统默认",""),
	BLUE("blue", "蓝色经典" , ""), 
	RED("red", "黑红炫酷" , ""),
	GREEN("green", "绿意清新" , ""),
	YELLOW("yellow", "橙黄活力" , "");
 
	private WebsiteStyleType(String code, String desc , String iconImg) {
		this.code = code;
		this.desc = desc;
		this.iconImg = iconImg;
	}

	private final String code;
	private final String desc;
	private final String iconImg ;
	
	public String getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}
	
	public String getIconImg() {
		return iconImg;
	}
	
	public static WebsiteStyleType fromCode(String code){
		
		for (WebsiteStyleType em : WebsiteStyleType.values()) {
			if(em.getCode().equals(code)){
				return em ;
			}
		}
		
		return null ;
	}
}
