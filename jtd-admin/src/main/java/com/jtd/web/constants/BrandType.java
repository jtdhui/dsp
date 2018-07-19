package com.jtd.web.constants;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月24日
 * @项目名称 dsp-common-entity
 * @描述 品牌类型
 */
public enum BrandType {
	
	UNKNOW("unknow", "未知"), 
	APPLE("apple", "苹果"), 
	SAMSUNG("samsung", "三星"), 
	XIAOMI("xiaomi", "小米"),
    HUAWEI("huawei","华为"),
    OPPO("oppo","oppo"),
    COOLPAD("coolpad","酷派"),
    LENOVO("lenovo","联想"),
    MEIZU("meizu","魅族"),
    VIVO("vivo","VIVO"),
    GIONEE("gionee","金立"),
    ZTE("zte","中兴"),
    SONY("sony","索尼"),
    HTC("htc","htc"),
    LG("lg","LG"),
    MICROMAX("micromax","GMICROMAX"),
    MOTOROLA("motorola","摩托罗拉"),
    NUBIA("nubia","努比亚"),
    TCL("tcl","TCL"),
    NOKIA("nokia","诺基亚"),
    MICROSOFT("microsoft","微软手机"),
    ASUS("asus","华硕"),
    ACER("acer","宏基"),
    BENQ("benq","明基"),
    DELL("dell","戴尔"),
    HP("hp","惠普"),
    LETV("letv","乐视"),
    OTHERS("others","其他")
	// ... 等产品经理的品牌列表
	;

	private BrandType(String brand, String brandName) {
		this.brand = brand;
		this.brandName = brandName;
	}

	private final String brand;// 品牌小写字母名
	private final String brandName;// 品牌中文名

	public String getBrand() {
		return brand;
	}

	public String getBrandName() {
		return brandName;
	}

    public static BrandType fromCode(String brand) {
        for (BrandType r : BrandType.values()) {
            if(r.brand.equals(brand)){
                return r ;
            }
        }
        return null ;
    }
}
