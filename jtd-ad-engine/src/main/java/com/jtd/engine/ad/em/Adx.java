package com.jtd.engine.ad.em;


/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月8日
 * @项目名称 dsp-engine
 * @描述 <p>Adx,枚举</p>
 */
public enum Adx {

	TANX(1, "TANX"), 
	BES(2, "BES"), 
	AdView(5, "AdView"),
	VAM(6, "ValueMaker"),
	TENCENT(7,"Tencent"),
	XTRADER(8,"Xtrader"),
	HZ(9,"HZ"),
	/** 新汇达通 */
	XHDT(10,"XHDT"),
	/** 安沃 */
	ADWO(11,"Adwo");

	private final int channelId;
	private final String channelName;

	private Adx(int channelId, String channelName) {
		this.channelId = channelId;
		this.channelName = channelName;
	}

	public int channelId() {
		return channelId;
	}

	public String channelName() {
		return channelName;
	}
	
	public static Adx fromChannelId(int channelid) {
		switch (channelid) {
		case 1:
			return TANX;
		case 2:
			return BES;
		case 5:
			return AdView;
		case 6:
			return VAM;
		case 7:
			return TENCENT;
		case 8:
			return XTRADER;
		case 9:
			return HZ;
		}
		return null;
	}
	
	public static void main(String[] args)
	{
		System.out.println(Adx.XHDT.channelId);
	}
}
