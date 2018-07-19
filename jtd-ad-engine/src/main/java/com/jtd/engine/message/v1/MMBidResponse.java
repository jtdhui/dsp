package com.jtd.engine.message.v1;

import java.util.List;
import com.jtd.engine.adserver.message.v1.MessageV1;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月8日
 * @项目名称 dsp-engine
 * @描述 <p></p>
 */
public class MMBidResponse extends MessageV1 {

	private static final long serialVersionUID = 7111722535943207240L;

	private String id;
	private List<Seatbid> seatbid;
	private String bidid;
	private String cur;

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
	 * @return the cur
	 */
	public String getCur() {
		return cur;
	}

	/**
	 * @param cur
	 *            the cur to set
	 */
	public void setCur(String cur) {
		this.cur = cur;
	}

	/**
	 * @return the seatbid
	 */
	public List<Seatbid> getSeatbid() {
		return seatbid;
	}

	/**
	 * @param seatbid the seatbid to set
	 */
	public void setSeatbid(List<Seatbid> seatbid) {
		this.seatbid = seatbid;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.maxit.adserver.message.v1.MessageV1#type()
	 */
	public Type type() {
		return Type.MMBidResponse;
	}
	
	public static class Seatbid {

		private List<Bid> bid;

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
			private String curl;
			private int cmflag;
			private String adid;
			private String nurl;
			private String surl;
			private String crid;

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
			 * @return the curl
			 */
			public String getCurl() {
				return curl;
			}

			/**
			 * @param curl
			 *            the curl to set
			 */
			public void setCurl(String curl) {
				this.curl = curl;
			}

			/**
			 * @return the cmflag
			 */
			public int getCmflag() {
				return cmflag;
			}

			/**
			 * @param cmflag
			 *            the cmflag to set
			 */
			public void setCmflag(int cmflag) {
				this.cmflag = cmflag;
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
			 * @return the surl
			 */
			public String getSurl() {
				return surl;
			}

			/**
			 * @param surl
			 *            the surl to set
			 */
			public void setSurl(String surl) {
				this.surl = surl;
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
		}
	}
	
}
