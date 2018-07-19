package com.jtd.engine.message.v1;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.alibaba.fastjson.JSON;
import com.jtd.engine.adserver.message.v1.MessageV1;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月8日
 * @项目名称 dsp-engine
 * @描述 <p></p>
 */
public class SaxBidRequest extends MessageV1 {

	private static final long serialVersionUID = -5917255829793077885L;

	private String version;
	private String bid;
	private String ip;
	private String sid;
	private String user_agent;
	private String page_url;
	private List<String> excluded_product_category;
	private List<String> excluded_sensitive_category;
	private List<String> excluded_click_through_url;
	private List<AdUnit> ad_unit;

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
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * @param ip
	 *            the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * @return the sid
	 */
	public String getSid() {
		return sid;
	}

	/**
	 * @param sid
	 *            the sid to set
	 */
	public void setSid(String sid) {
		this.sid = sid;
	}

	/**
	 * @return the user_agent
	 */
	public String getUser_agent() {
		return user_agent;
	}

	/**
	 * @param user_agent
	 *            the user_agent to set
	 */
	public void setUser_agent(String user_agent) {
		this.user_agent = user_agent;
	}

	/**
	 * @return the page_url
	 */
	public String getPage_url() {
		return page_url;
	}

	/**
	 * @param page_url
	 *            the page_url to set
	 */
	public void setPage_url(String page_url) {
		this.page_url = page_url;
	}

	/**
	 * @return the excluded_product_category
	 */
	public List<String> getExcluded_product_category() {
		return excluded_product_category;
	}

	/**
	 * @param excluded_product_category
	 *            the excluded_product_category to set
	 */
	public void setExcluded_product_category(
			List<String> excluded_product_category) {
		this.excluded_product_category = excluded_product_category;
	}

	/**
	 * @return the excluded_sensitive_category
	 */
	public List<String> getExcluded_sensitive_category() {
		return excluded_sensitive_category;
	}

	/**
	 * @param excluded_sensitive_category
	 *            the excluded_sensitive_category to set
	 */
	public void setExcluded_sensitive_category(
			List<String> excluded_sensitive_category) {
		this.excluded_sensitive_category = excluded_sensitive_category;
	}

	/**
	 * @return the excluded_click_through_url
	 */
	public List<String> getExcluded_click_through_url() {
		return excluded_click_through_url;
	}

	/**
	 * @param excluded_click_through_url
	 *            the excluded_click_through_url to set
	 */
	public void setExcluded_click_through_url(
			List<String> excluded_click_through_url) {
		this.excluded_click_through_url = excluded_click_through_url;
	}

	/**
	 * @return the ad_unit
	 */
	public List<AdUnit> getAd_unit() {
		return ad_unit;
	}

	/**
	 * @param ad_unit
	 *            the ad_unit to set
	 */
	public void setAd_unit(List<AdUnit> ad_unit) {
		this.ad_unit = ad_unit;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.maxit.adserver.message.v1.MessageV1#type()
	 */
	public Type type() {
		return Type.SaxBidRequest;
	}

	public static class AdUnit {
		public static final Map<String, String[]> SIZE = new HashMap<String, String[]>();
		static {
			SIZE.put("001", new String[]{"950","90"});
			SIZE.put("002", new String[]{"300","250"});
			SIZE.put("003", new String[]{"250","230"});
			SIZE.put("004", new String[]{"640","90"});
			SIZE.put("005", new String[]{"300","120"});
			SIZE.put("006", new String[]{"240","120"});
			SIZE.put("007", new String[]{"340","120"});
			SIZE.put("008", new String[]{"1000","90"});
			SIZE.put("009", new String[]{"240","60"});
			SIZE.put("010", new String[]{"240","200"});
			SIZE.put("011", new String[]{"250","90"});
			SIZE.put("012", new String[]{"250","120"});
			SIZE.put("013", new String[]{"355","200"});
			SIZE.put("014", new String[]{"360","110"});
			SIZE.put("015", new String[]{"585","90"});
			SIZE.put("016", new String[]{"260","120"});
			SIZE.put("017", new String[]{"240","170"});
			SIZE.put("018", new String[]{"350","110"});
			SIZE.put("019", new String[]{"125","95"});
			SIZE.put("020", new String[]{"200","200"});
			SIZE.put("021", new String[]{"190","130"});
			SIZE.put("022", new String[]{"320","115"});
			SIZE.put("023", new String[]{"230","90"});
			SIZE.put("024", new String[]{"300","60"});
			SIZE.put("025", new String[]{"300","100"});
			SIZE.put("026", new String[]{"320","190"});
			SIZE.put("027", new String[]{"210","220"});
		}
		private String id;
		private String type;
		private String size;
		private String pos;
		private int creative_num;
		private List<String> excluded_creative_category;
		private long min_cpm_price;

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

		/**
		 * @return the size
		 */
		public String getSize() {
			return size;
		}

		/**
		 * @param size
		 *            the size to set
		 */
		public void setSize(String size) {
			this.size = size;
		}

		/**
		 * @return the pos
		 */
		public String getPos() {
			return pos;
		}

		/**
		 * @param pos
		 *            the pos to set
		 */
		public void setPos(String pos) {
			this.pos = pos;
		}

		/**
		 * @return the creative_num
		 */
		public int getCreative_num() {
			return creative_num;
		}

		/**
		 * @param creative_num
		 *            the creative_num to set
		 */
		public void setCreative_num(int creative_num) {
			this.creative_num = creative_num;
		}

		/**
		 * @return the excluded_creative_category
		 */
		public List<String> getExcluded_creative_category() {
			return excluded_creative_category;
		}

		/**
		 * @param excluded_creative_category
		 *            the excluded_creative_category to set
		 */
		public void setExcluded_creative_category(
				List<String> excluded_creative_category) {
			this.excluded_creative_category = excluded_creative_category;
		}

		/**
		 * @return the min_cpm_price
		 */
		public long getMin_cpm_price() {
			return min_cpm_price;
		}

		/**
		 * @param min_cpm_price
		 *            the min_cpm_price to set
		 */
		public void setMin_cpm_price(long min_cpm_price) {
			this.min_cpm_price = min_cpm_price;
		}
	}
	
	public static void main(String[] args) {
		String s = "{\"version\": \"1.0\",\"bid\": \"123456\",\"ip\": \"123.126.53.109\",\"sid\": \" dGhpcyBpcyBhbiBleGFtGxl\",\"user_agent\": \"Mozilla/5.0 AppleWebKit/537.1 (KHTML, like Gecko) Chrome/21.0.1180.89\",\"page_url\": \"http://www.example.com\",\"excluded_product_category\": [ \"010\",\"007\" ],\"excluded_sensitive_category\": [ \"politics\",\"religion\" ],\"excluded_click_through_url\": [\"http://www.example1.com\",\"http://www.example2.com\" ],\"ad_unit\": [{\"id\": \" PDPS000000025794\",\"type\": \"003\",\"size\": \"003\",\"pos\": \"true\",\"creative_num\": 2, \"excluded_creative_category\": [\"007\",  \"010\"], \"min_cpm_price\": 30},{\"id\": \" PDPS000000025798\", \"type\": \"001\",\"size\": \" 001\",\"pos\": \"false\", \"creative_num\": 1, \"excluded_creative_category\": [],\"min_cpm_price\": 50}]}";
		SaxBidRequest br = JSON.parseObject(s, SaxBidRequest.class);
		System.out.print(br.getAd_unit().get(0).getPos());
	}
}
