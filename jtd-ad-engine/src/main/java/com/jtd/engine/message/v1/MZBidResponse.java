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
public class MZBidResponse extends MessageV1 {

	private static final long serialVersionUID = 8476874741858450157L;
	//请求ID,request.id
	private String id;
	//DSP给出的该次竞价的ID
	private String bidid;
	private List<Seatbid> seatbid;

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

	public static class Seatbid {
		private List<Bid> bid;
		
		/*
		 seat	string	有灵集平台提供给DSP的本次出价的seat ID
		 * */
		
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
			//DSP对该次出价分配的ID
			private String id;
			//Bid Request中对应的曝光ID
			private String impid;
			//DSP出价，单位是分/千次曝光，即CPM
			private float price;
			private String nurl;
			//广告物料URL。如果是动态创意，这个字段存放的是创意的HTML标签，标签中支持三种宏替换，%%CLICK_URL_ESC%%（encode的Exchange的点击监测地址）、%%CLICK_URL_UNESC%%(未encode的Exchange点击监测地址)和%%WINNING_PRICE%%（竞价最终价格）。
			private String adm;
			//DSP系统中的创意ID，对于后审核的创意(即动态创意)，这个字段可以留作历史查证。
			private String crid;
			//该字段已经废弃
			private List<String> pvm;
			//该字段已经废弃
			private String clickm;
			//扩展对象
			private Ext ext;

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
			 * @return the pvm
			 */
			public List<String> getPvm() {
				return pvm;
			}

			/**
			 * @param pvm
			 *            the pvm to set
			 */
			public void setPvm(List<String> pvm) {
				this.pvm = pvm;
			}

			/**
			 * @return the clickm
			 */
			public String getClickm() {
				return clickm;
			}

			/**
			 * @param clickm
			 *            the clickm to set
			 */
			public void setClickm(String clickm) {
				this.clickm = clickm;
			}

			/**
			 * @return the ext
			 */
			public Ext getExt() {
				return ext;
			}

			/**
			 * @param ext
			 *            the ext to set
			 */
			public void setExt(Ext ext) {
				this.ext = ext;
			}

			public static class Ext {
				//点击目标URL。广告点击后会跳转到物料上绑定的landingpage，还是取实时返回的ld
				private String ldp;
				//曝光监测URL，监测数组支持的曝光条数和广告展现时是否会发物料上绑定的monitor地址
				private List<String> pm;
				//点击监测URL，监测数组支持的点击监测条数
				private List<String> cm;
				//物料的类型，包括png，gif，jpg，swf，flv，c和x。具体参见
				private String type;
				
				/*
				 action_type	integer	媒体资源位置支持的交互类型：1.download---下载类广告 2.landingpage---打开落地页型广告
				 * */

				/**
				 * @return the ldp
				 */
				public String getLdp() {
					return ldp;
				}

				/**
				 * @param ldp
				 *            the ldp to set
				 */
				public void setLdp(String ldp) {
					this.ldp = ldp;
				}

				/**
				 * @return the pm
				 */
				public List<String> getPm() {
					return pm;
				}

				/**
				 * @param pm
				 *            the pm to set
				 */
				public void setPm(List<String> pm) {
					this.pm = pm;
				}

				/**
				 * @return the cm
				 */
				public List<String> getCm() {
					return cm;
				}

				/**
				 * @param cm
				 *            the cm to set
				 */
				public void setCm(List<String> cm) {
					this.cm = cm;
				}

				/**
				 * @return the type
				 */
				public String getType() {
					return type;
				}

				/**
				 * @param type
				 *            the type to set
				 */
				public void setType(String type) {
					this.type = type;
				}

			}
		}
	}

	@Override
	public Type type() {
		return Type.MZBidResponse;
	}
}
