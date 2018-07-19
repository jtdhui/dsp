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
public class SaxBidResponse extends MessageV1 {

	private static final long serialVersionUID = 7111722535943207240L;

	private String version;
	private String bid;
	private List<AdCreative> ad_creative;
	private String cm_flag;

	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * @return the bid
	 */
	public String getBid() {
		return bid;
	}

	/**
	 * @param bid
	 *            the bid to set
	 */
	public void setBid(String bid) {
		this.bid = bid;
	}

	/**
	 * @return the ad_creative
	 */
	public List<AdCreative> getAd_creative() {
		return ad_creative;
	}

	/**
	 * @param ad_creative
	 *            the ad_creative to set
	 */
	public void setAd_creative(List<AdCreative> ad_creative) {
		this.ad_creative = ad_creative;
	}

	/**
	 * @return the cm_flag
	 */
	public String getCm_flag() {
		return cm_flag;
	}

	/**
	 * @param cm_flag
	 *            the cm_flag to set
	 */
	public void setCm_flag(String cm_flag) {
		this.cm_flag = cm_flag;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.maxit.adserver.message.v1.MessageV1#type()
	 */
	public Type type() {
		return Type.SaxBidResponse;
	}

	public static class AdCreative {

		private String id;
		private long max_cpm_price;
		private List<Creative> creative;

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
		 * @return the max_cpm_price
		 */
		public long getMax_cpm_price() {
			return max_cpm_price;
		}

		/**
		 * @param max_cpm_price
		 *            the max_cpm_price to set
		 */
		public void setMax_cpm_price(long max_cpm_price) {
			this.max_cpm_price = max_cpm_price;
		}

		/**
		 * @return the creative
		 */
		public List<Creative> getCreative() {
			return creative;
		}

		/**
		 * @param creative
		 *            the creative to set
		 */
		public void setCreative(List<Creative> creative) {
			this.creative = creative;
		}
	}

	public static class Creative {

		private String id;
		private String html_snippet;

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
		 * @return the html_snippet
		 */
		public String getHtml_snippet() {
			return html_snippet;
		}

		/**
		 * @param html_snippet
		 *            the html_snippet to set
		 */
		public void setHtml_snippet(String html_snippet) {
			this.html_snippet = html_snippet;
		}
	}
}
