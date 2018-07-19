package com.jtd.web.constants;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置
 * @创建日期 2016年8月24日
 * @项目名称 dsp-common-entity
 * @描述 所有的渠道
 */
public enum CatgSerial {

	DSP("DSP", "DSP", 0L),  //DSP自身
	TANX("TANX", "TANX", 1L), //淘宝ADX
	BES("BES", "BES", 2L),  //百度ADX
	ADVIEW("ADVIEW","ADVIEW",5L),/** adView */
	VAM("VAM", "VAM", 6L),  //万流客ADX
    TENCENT("TENCENT","TENCENT",7L), //腾讯ADX
    XTRADER("XTRADER","XTRADER",8L), //灵集ADX
    HZ("HZ","HZ",9L),  //互众ADX
	XHDT("XHDT","XHDT",10L), /** 新汇达通 */
	ADWO("ADWO","ADWO",11L); /** 安沃 */
	

	private String name;
	private String code;
	private Long channelId;

	private CatgSerial(String code, String name, Long channelId) {
		this.code = code;
		this.name = name;
		this.channelId = channelId;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public Long getChannelId() {
		return channelId;
	}
	
	public static CatgSerial fromChannelId(long channelId){
		
		for (CatgSerial c : CatgSerial.values()) {
			if(c.channelId == channelId){
				return c ;
			}
		}
		return null ;
	}

	public static CatgSerial fromCode(String code) {
		for (CatgSerial c : CatgSerial.values()) {
			if(c.code.equals(code) ){
				return c ;
			}
		}
		return null ;
	}
}
