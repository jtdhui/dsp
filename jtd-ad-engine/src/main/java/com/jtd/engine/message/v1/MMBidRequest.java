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
public class MMBidRequest extends MessageV1 {

	private static final long serialVersionUID = -5917255829793077885L;
	
	private String id;
	private int tmax;
	private Site site;
	private Device device;
	private User user;
	private List<Imp> imp;
	private List<String> cur;
	private List<String> bcat;
	private List<String> badv;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the tmax
	 */
	public int getTmax() {
		return tmax;
	}

	/**
	 * @param tmax the tmax to set
	 */
	public void setTmax(int tmax) {
		this.tmax = tmax;
	}

	/**
	 * @return the site
	 */
	public Site getSite() {
		return site;
	}

	/**
	 * @param site the site to set
	 */
	public void setSite(Site site) {
		this.site = site;
	}

	/**
	 * @return the device
	 */
	public Device getDevice() {
		return device;
	}

	/**
	 * @param device the device to set
	 */
	public void setDevice(Device device) {
		this.device = device;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return the imp
	 */
	public List<Imp> getImp() {
		return imp;
	}

	/**
	 * @param imp the imp to set
	 */
	public void setImp(List<Imp> imp) {
		this.imp = imp;
	}

	/**
	 * @return the cur
	 */
	public List<String> getCur() {
		return cur;
	}

	/**
	 * @param cur the cur to set
	 */
	public void setCur(List<String> cur) {
		this.cur = cur;
	}

	/**
	 * @return the bcat
	 */
	public List<String> getBcat() {
		return bcat;
	}

	/**
	 * @param bcat the bcat to set
	 */
	public void setBcat(List<String> bcat) {
		this.bcat = bcat;
	}

	/**
	 * @return the badv
	 */
	public List<String> getBadv() {
		return badv;
	}

	/**
	 * @param badv the badv to set
	 */
	public void setBadv(List<String> badv) {
		this.badv = badv;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.maxit.adserver.message.v1.MessageV1#type()
	 */
	public Type type() {
		return Type.MMBidRequest;
	}
	
	public static class Site {

		private String id;
		private String domain;
		private List<String> sectioncat;
		private String page;
		private Publisher publisher;
		private String allyessitetype;
		private String allyespageform;

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
		 * @return the domain
		 */
		public String getDomain() {
			return domain;
		}

		/**
		 * @param domain
		 *            the domain to set
		 */
		public void setDomain(String domain) {
			this.domain = domain;
		}

		/**
		 * @return the sectioncat
		 */
		public List<String> getSectioncat() {
			return sectioncat;
		}

		/**
		 * @param sectioncat
		 *            the sectioncat to set
		 */
		public void setSectioncat(List<String> sectioncat) {
			this.sectioncat = sectioncat;
		}

		/**
		 * @return the page
		 */
		public String getPage() {
			return page;
		}

		/**
		 * @param page
		 *            the page to set
		 */
		public void setPage(String page) {
			this.page = page;
		}

		/**
		 * @return the publisher
		 */
		public Publisher getPublisher() {
			return publisher;
		}

		/**
		 * @param publisher
		 *            the publisher to set
		 */
		public void setPublisher(Publisher publisher) {
			this.publisher = publisher;
		}

		/**
		 * @return the allyessitetype
		 */
		public String getAllyessitetype() {
			return allyessitetype;
		}

		/**
		 * @param allyessitetype
		 *            the allyessitetype to set
		 */
		public void setAllyessitetype(String allyessitetype) {
			this.allyessitetype = allyessitetype;
		}

		/**
		 * @return the allyespageform
		 */
		public String getAllyespageform() {
			return allyespageform;
		}

		/**
		 * @param allyespageform
		 *            the allyespageform to set
		 */
		public void setAllyespageform(String allyespageform) {
			this.allyespageform = allyespageform;
		}
		
		public static class Publisher {
			private String id;

			/**
			 * @return the id
			 */
			public String getId() {
				return id;
			}

			/**
			 * @param id the id to set
			 */
			public void setId(String id) {
				this.id = id;
			}
		}
	}
	
	public static class Device {

		private int dnt;
		private String ua;
		private String ip;
		private String language;
		private int js;
		private int devicetype;

		/**
		 * @return the dnt
		 */
		public int getDnt() {
			return dnt;
		}

		/**
		 * @param dnt
		 *            the dnt to set
		 */
		public void setDnt(int dnt) {
			this.dnt = dnt;
		}

		/**
		 * @return the ua
		 */
		public String getUa() {
			return ua;
		}

		/**
		 * @param ua
		 *            the ua to set
		 */
		public void setUa(String ua) {
			this.ua = ua;
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
		 * @return the language
		 */
		public String getLanguage() {
			return language;
		}

		/**
		 * @param language
		 *            the language to set
		 */
		public void setLanguage(String language) {
			this.language = language;
		}

		/**
		 * @return the js
		 */
		public int getJs() {
			return js;
		}

		/**
		 * @param js
		 *            the js to set
		 */
		public void setJs(int js) {
			this.js = js;
		}

		/**
		 * @return the devicetype
		 */
		public int getDevicetype() {
			return devicetype;
		}

		/**
		 * @param devicetype
		 *            the devicetype to set
		 */
		public void setDevicetype(int devicetype) {
			this.devicetype = devicetype;
		}
	}
	
	public static class User {

		private String id;
		private int cver;

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
		 * @return the cver
		 */
		public int getCver() {
			return cver;
		}

		/**
		 * @param cver
		 *            the cver to set
		 */
		public void setCver(int cver) {
			this.cver = cver;
		}
	}
	
	public static class Imp {

		private String id;
		private Banner banner;
		private String tagid;
		private float bidfloor;
		private String bidfloorcur;

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
		 * @return the banner
		 */
		public Banner getBanner() {
			return banner;
		}

		/**
		 * @param banner
		 *            the banner to set
		 */
		public void setBanner(Banner banner) {
			this.banner = banner;
		}

		/**
		 * @return the tagid
		 */
		public String getTagid() {
			return tagid;
		}

		/**
		 * @param tagid
		 *            the tagid to set
		 */
		public void setTagid(String tagid) {
			this.tagid = tagid;
		}

		/**
		 * @return the bidfloor
		 */
		public float getBidfloor() {
			return bidfloor;
		}

		/**
		 * @param bidfloor
		 *            the bidfloor to set
		 */
		public void setBidfloor(float bidfloor) {
			this.bidfloor = bidfloor;
		}

		/**
		 * @return the bidfloorcur
		 */
		public String getBidfloorcur() {
			return bidfloorcur;
		}

		/**
		 * @param bidfloorcur
		 *            the bidfloorcur to set
		 */
		public void setBidfloorcur(String bidfloorcur) {
			this.bidfloorcur = bidfloorcur;
		}

		public static class Banner {

			private int w;
			private int h;
			private int pos;
			private List<Integer> allyesadformat;
			private String allyesadform;

			/**
			 * @return the w
			 */
			public int getW() {
				return w;
			}

			/**
			 * @param w
			 *            the w to set
			 */
			public void setW(int w) {
				this.w = w;
			}

			/**
			 * @return the h
			 */
			public int getH() {
				return h;
			}

			/**
			 * @param h
			 *            the h to set
			 */
			public void setH(int h) {
				this.h = h;
			}

			/**
			 * @return the pos
			 */
			public int getPos() {
				return pos;
			}

			/**
			 * @param pos
			 *            the pos to set
			 */
			public void setPos(int pos) {
				this.pos = pos;
			}

			/**
			 * @return the allyesadformat
			 */
			public List<Integer> getAllyesadformat() {
				return allyesadformat;
			}

			/**
			 * @param allyesadformat
			 *            the allyesadformat to set
			 */
			public void setAllyesadformat(List<Integer> allyesadformat) {
				this.allyesadformat = allyesadformat;
			}

			/**
			 * @return the allyesadform
			 */
			public String getAllyesadform() {
				return allyesadform;
			}

			/**
			 * @param allyesadform
			 *            the allyesadform to set
			 */
			public void setAllyesadform(String allyesadform) {
				this.allyesadform = allyesadform;
			}
		}
	}
}
