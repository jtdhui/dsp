package com.jtd.engine.message.v1;

import com.jtd.engine.adserver.message.v1.MessageV1;

import java.util.List;

/**
 * adView 广告响应
 * @author zl
 *
 */
public class AVBidResponse extends MessageV1 {

	private static final long serialVersionUID = -1914606106996731320L;

	@Override
	public Type type() {
		return Type.AVBidResponse;
	}
	
	/** bidRequest的唯一标识 */
	private String id;
	/** 0或过个Seatbid对象 */
	private List<Seatbid> seatbid;
	/** BidResponse唯一标识，由DSP生成 */
	private String bidid;
	/** 价格单位，取值USD,RMB缺省RMB */
	private String cur;
	/** 不出价原因 */
	private Integer nbr;
	/** 扩展对象 */
	private Object ext;

	public static class Seatbid 
	{
		/** DSP给的竞价广告对象 */
		private List<Bid> bid;
		/** SeatBid标识，DSP生成 */
		private String seat;
		/** 扩展字段 */
		private Object ext;

		public static class Bid 
		{
			/** 对应Impression的唯一标识 */
			private String impid;
			/** 价格,CPM或CPC价格*10000 */
			private Integer price;
			/** DSP计费方式  1：CPM，2：CPC */
			private Integer paymode;
			/** 广告点击行为类型 */
			private Integer adct;
			/** 广告ID */
			private String adid;
			/** 广告类型 */
			private Integer admt;
			/** 广告物料，admt=4为HTML代码段，
			 * 或admt=6为视频广告VAST时必须 */
			private String adm;
			
			/** 原生广告协议 */
			//private Nativead nativead;
			
			/** 图片/视频物料URL */
			private String adi;
			/** 文本物料标题 */
			private String adt;
			/** 文本物料副标题或描述，多个已换行分隔 */
			private String ads;
			/** 广告物料宽度 */
			private Integer adw;
			/** 广告物料高度 */
			private Integer adh;
			/** 广告来源标识 */
			private String adLogo;
			/** 广告需要播放时长，针对插屏和开屏 */
			private Integer adtm;
			/** 广告过期时间 */
			private String ade;
			/** 广告点击调转落地页，支持重定向 */
			private String adurl;
			/** 包含deeplink的点击跳转地址 */
			private String deeplink;
			/** 广告主域名 */
			private String adomain;
			/** 赢价通知，有adView服务器发出 */
			private String wurl;
			/** 带延迟的展示汇报，有客户端发出 */
			private List<Object> nurl;
			/** 点击监控地址，客户端逐个发送通知 */
			private List<String> curl;
			/** 判断是否投标属于私有交易 */
			private String dealid;
			/** 广告创意ID */
			private String cid;
			/** 广告物料ID */
			private String crid;
			/** 扩展对象 */
			private List<String> attr;
			/**  */
			private Ext ext;
			
			public static class Ext{}
			
			public String getImpid() {
				return impid;
			}
			public void setImpid(String impid) {
				this.impid = impid;
			}
			public Integer getPrice() {
				return price;
			}
			public void setPrice(Integer price) {
				this.price = price;
			}
			public Integer getPaymode() {
				return paymode;
			}
			public void setPaymode(Integer paymode) {
				this.paymode = paymode;
			}
			public Integer getAdct() {
				return adct;
			}
			public void setAdct(Integer adct) {
				this.adct = adct;
			}
			public String getAdid() {
				return adid;
			}
			public void setAdid(String adid) {
				this.adid = adid;
			}
			public Integer getAdmt() {
				return admt;
			}
			public void setAdmt(Integer admt) {
				this.admt = admt;
			}
			public String getAdm() {
				return adm;
			}
			public void setAdm(String adm) {
				this.adm = adm;
			}
			public String getAdi() {
				return adi;
			}
			public void setAdi(String adi) {
				this.adi = adi;
			}
			public String getAdt() {
				return adt;
			}
			public void setAdt(String adt) {
				this.adt = adt;
			}
			public String getAds() {
				return ads;
			}
			public void setAds(String ads) {
				this.ads = ads;
			}
			public Integer getAdw() {
				return adw;
			}
			public void setAdw(Integer adw) {
				this.adw = adw;
			}
			public Integer getAdh() {
				return adh;
			}
			public void setAdh(Integer adh) {
				this.adh = adh;
			}
			public String getAdLogo() {
				return adLogo;
			}
			public void setAdLogo(String adLogo) {
				this.adLogo = adLogo;
			}
			public Integer getAdtm() {
				return adtm;
			}
			public void setAdtm(Integer adtm) {
				this.adtm = adtm;
			}
			public String getAde() {
				return ade;
			}
			public void setAde(String ade) {
				this.ade = ade;
			}
			public String getAdurl() {
				return adurl;
			}
			public void setAdurl(String adurl) {
				this.adurl = adurl;
			}
			public String getDeeplink() {
				return deeplink;
			}
			public void setDeeplink(String deeplink) {
				this.deeplink = deeplink;
			}
			public String getAdomain() {
				return adomain;
			}
			public void setAdomain(String adomain) {
				this.adomain = adomain;
			}
			public String getWurl() {
				return wurl;
			}
			public void setWurl(String wurl) {
				this.wurl = wurl;
			}
			public List<Object> getNurl() {
				return nurl;
			}
			public void setNurl(List<Object> nurl) {
				this.nurl = nurl;
			}
			public List<String> getCurl() {
				return curl;
			}
			public void setCurl(List<String> curl) {
				this.curl = curl;
			}
			public String getDealid() {
				return dealid;
			}
			public void setDealid(String dealid) {
				this.dealid = dealid;
			}
			public String getCid() {
				return cid;
			}
			public void setCid(String cid) {
				this.cid = cid;
			}
			public String getCrid() {
				return crid;
			}
			public void setCrid(String crid) {
				this.crid = crid;
			}
			public List<String> getAttr() {
				return attr;
			}
			public void setAttr(List<String> attr) {
				this.attr = attr;
			}
			public Ext getExt() {
				return ext;
			}
			public void setExt(Ext ext) {
				this.ext = ext;
			}
		
			
		}

		public List<Bid> getBid() {
			return bid;
		}

		public void setBid(List<Bid> bid) {
			this.bid = bid;
		}

		public String getSeat() {
			return seat;
		}

		public void setSeat(String seat) {
			this.seat = seat;
		}

		public Object getExt() {
			return ext;
		}

		public void setExt(Object ext) {
			this.ext = ext;
		}
		
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public List<Seatbid> getSeatbid() {
		return seatbid;
	}


	public void setSeatbid(List<Seatbid> seatbid) {
		this.seatbid = seatbid;
	}


	public String getBidid() {
		return bidid;
	}


	public void setBidid(String bidid) {
		this.bidid = bidid;
	}


	public String getCur() {
		return cur;
	}


	public void setCur(String cur) {
		this.cur = cur;
	}


	public Integer getNbr() {
		return nbr;
	}


	public void setNbr(Integer nbr) {
		this.nbr = nbr;
	}


	public Object getExt() {
		return ext;
	}


	public void setExt(Object ext) {
		this.ext = ext;
	}
	
}
