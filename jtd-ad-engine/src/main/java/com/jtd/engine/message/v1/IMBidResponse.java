package com.jtd.engine.message.v1;

import com.jtd.engine.adserver.message.v1.MessageV1;

import java.util.List;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月8日
 * @项目名称 dsp-engine
 * @描述 <p></p>
 */
public class IMBidResponse extends MessageV1 {

	private static final long serialVersionUID = -1914606106996731320L;

	private String id;
	private List<Seatbid> seatbid;
	private String bidid;
	private Object ext;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the seatbid
	 */
	public List<Seatbid> getSeatbid() {
		return seatbid;
	}

	/**
	 * @param seatbid
	 *            the seatbid to set
	 */
	public void setSeatbid(List<Seatbid> seatbid) {
		this.seatbid = seatbid;
	}

	/**
	 * @return the bidid
	 */
	public String getBidid() {
		return bidid;
	}

	/**
	 * @param bidid
	 *            the bidid to set
	 */
	public void setBidid(String bidid) {
		this.bidid = bidid;
	}

	/**
	 * @return the ext
	 */
	public Object getExt() {
		return ext;
	}

	/**
	 * @param ext
	 *            the ext to set
	 */
	public void setExt(Object ext) {
		this.ext = ext;
	}

	public static class Seatbid {
		private List<Bid> bid;
		private String seat;

		/**
		 * @return the seat
		 */
		public String getSeat() {
			return seat;
		}

		/**
		 * @param seat
		 *            the seat to set
		 */
		public void setSeat(String seat) {
			this.seat = seat;
		}

		/**
		 * @return the bid
		 */
		public List<Bid> getBid() {
			return bid;
		}

		/**
		 * @param bid
		 *            the bid to set
		 */
		public void setBid(List<Bid> bid) {
			this.bid = bid;
		}

		public static class Bid {

			private String id;
			private String impid;
			private float price;
			private String adid;
			private String nurl;
			private String adm;
			private List<String> adomain;
			private String iurl;
			private String cid;
			private String crid;
			private List<String> attr;
			private Object ext;

			/**
			 * @return the id
			 */
			public String getId() {
				return id;
			}

			/**
			 * @param id
			 *            the id to set
			 */
			public void setId(String id) {
				this.id = id;
			}

			/**
			 * @return the impid
			 */
			public String getImpid() {
				return impid;
			}

			/**
			 * @param impid
			 *            the impid to set
			 */
			public void setImpid(String impid) {
				this.impid = impid;
			}

			/**
			 * @return the price
			 */
			public float getPrice() {
				return price;
			}

			/**
			 * @param price
			 *            the price to set
			 */
			public void setPrice(float price) {
				this.price = price;
			}

			/**
			 * @return the adid
			 */
			public String getAdid() {
				return adid;
			}

			/**
			 * @param adid
			 *            the adid to set
			 */
			public void setAdid(String adid) {
				this.adid = adid;
			}

			/**
			 * @return the nurl
			 */
			public String getNurl() {
				return nurl;
			}

			/**
			 * @param nurl
			 *            the nurl to set
			 */
			public void setNurl(String nurl) {
				this.nurl = nurl;
			}

			/**
			 * @return the adm
			 */
			public String getAdm() {
				return adm;
			}

			/**
			 * @param adm
			 *            the adm to set
			 */
			public void setAdm(String adm) {
				this.adm = adm;
			}

			/**
			 * @return the adomain
			 */
			public List<String> getAdomain() {
				return adomain;
			}

			/**
			 * @param adomain
			 *            the adomain to set
			 */
			public void setAdomain(List<String> adomain) {
				this.adomain = adomain;
			}

			/**
			 * @return the iurl
			 */
			public String getIurl() {
				return iurl;
			}

			/**
			 * @param iurl
			 *            the iurl to set
			 */
			public void setIurl(String iurl) {
				this.iurl = iurl;
			}

			/**
			 * @return the cid
			 */
			public String getCid() {
				return cid;
			}

			/**
			 * @param cid
			 *            the cid to set
			 */
			public void setCid(String cid) {
				this.cid = cid;
			}

			/**
			 * @return the crid
			 */
			public String getCrid() {
				return crid;
			}

			/**
			 * @param crid
			 *            the crid to set
			 */
			public void setCrid(String crid) {
				this.crid = crid;
			}

			/**
			 * @return the attr
			 */
			public List<String> getAttr() {
				return attr;
			}

			/**
			 * @param attr
			 *            the attr to set
			 */
			public void setAttr(List<String> attr) {
				this.attr = attr;
			}

			/**
			 * @return the ext
			 */
			public Object getExt() {
				return ext;
			}

			/**
			 * @param ext
			 *            the ext to set
			 */
			public void setExt(Object ext) {
				this.ext = ext;
			}

		}
	}

	@Override
	public Type type() {
		return Type.IMBidResponse;
	}
}
