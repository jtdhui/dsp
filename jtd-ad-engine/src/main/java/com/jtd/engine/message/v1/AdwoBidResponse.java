package com.jtd.engine.message.v1;

import java.util.List;

import com.jtd.engine.adserver.message.v1.MessageV1;

/**
 * 安沃广告响应response
 * @author zl
 *
 */
public class AdwoBidResponse extends MessageV1{

	private static final long serialVersionUID = 3336611006866276322L;

	@Override
	public Type type() {
		return Type.AdwoBidResponse;
	}
	
	/** 出价ID */
	private String id;
	/** 请求ID */
	private String bidid;
	/** DSP出价 */
	private List<Seatbid> seatbid;
	/** 货币单位 */
	private String cur;
	/** 扩展字段 */
	private Ext ext;
	
	/** DSP出价类 */
	public static class Seatbid
	{
		/** 出价对象 */
		private Bid bid;
		/** 出价席位 */
		private String seat;
		
		/** 出价对象类 */
		public static class Bid
		{
			/** 出价ID */
			private String id;
			/** 广告ID */
			private String adid;
			/** 中标连接，支持宏替换 */
			private String nurl;
			/** 中标价格，CPM单位：分 */
			private Float price;
			/** 广告ID */
			private String crid;
			/** 广告内容、图片 */
			private String adm;
			/** 广告主的主要域名 */
			private List<String> adomain;
			/** 图片地址 */
			private String iurl;
			/** 展示ID */
			private String impid;
			/** 活动ID */
			private String cid;
			public String getId() {
				return id;
			}
			public void setId(String id) {
				this.id = id;
			}
			public String getAdid() {
				return adid;
			}
			public void setAdid(String adid) {
				this.adid = adid;
			}
			public String getNurl() {
				return nurl;
			}
			public void setNurl(String nurl) {
				this.nurl = nurl;
			}
			public Float getPrice() {
				return price;
			}
			public void setPrice(Float price) {
				this.price = price;
			}
			public String getCrid() {
				return crid;
			}
			public void setCrid(String crid) {
				this.crid = crid;
			}
			public String getAdm() {
				return adm;
			}
			public void setAdm(String adm) {
				this.adm = adm;
			}
			public List<String> getAdomain() {
				return adomain;
			}
			public void setAdomain(List<String> adomain) {
				this.adomain = adomain;
			}
			public String getIurl() {
				return iurl;
			}
			public void setIurl(String iurl) {
				this.iurl = iurl;
			}
			public String getImpid() {
				return impid;
			}
			public void setImpid(String impid) {
				this.impid = impid;
			}
			public String getCid() {
				return cid;
			}
			public void setCid(String cid) {
				this.cid = cid;
			}
		}
		public Bid getBid() {
			return bid;
		}
		public void setBid(Bid bid) {
			this.bid = bid;
		}
		public String getSeat() {
			return seat;
		}
		public void setSeat(String seat) {
			this.seat = seat;
		}
	}
	
	/** 扩展字段类 */
	public static class Ext
	{
		/** 点击目标地址 */
		private String ldp;
		/** 请求ID */
		private List<String> pm;
		/** 点击监测URL，可以有多个 */
		private List<String> cm;
		public String getLdp() {
			return ldp;
		}
		public void setLdp(String ldp) {
			this.ldp = ldp;
		}
		public List<String> getPm() {
			return pm;
		}
		public void setPm(List<String> pm) {
			this.pm = pm;
		}
		public List<String> getCm() {
			return cm;
		}
		public void setCm(List<String> cm) {
			this.cm = cm;
		}
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBidid() {
		return bidid;
	}
	public void setBidid(String bidid) {
		this.bidid = bidid;
	}
	public List<Seatbid> getSeatbid() {
		return seatbid;
	}
	public void setSeatbid(List<Seatbid> seatbid) {
		this.seatbid = seatbid;
	}
	public String getCur() {
		return cur;
	}
	public void setCur(String cur) {
		this.cur = cur;
	}
	public Ext getExt() {
		return ext;
	}
	public void setExt(Ext ext) {
		this.ext = ext;
	}
}
