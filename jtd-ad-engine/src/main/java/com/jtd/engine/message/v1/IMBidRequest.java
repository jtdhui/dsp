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
public class IMBidRequest extends MessageV1 {

	private static final long serialVersionUID = -3444666909823602241L;

	private String id;
	private List<Imp> imp;
	private Site site;
	private App app;
	private Device device;
	private User user;
	private int at;
	private Integer tmax;
	private List<String> weat;
	private Integer allimps;
	private List<String> cur;
	private List<String> bcat;
	private List<String> badv;
	private Object ext;

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.maxit.adserver.message.v1.MessageV1#type()
	 */
	public Type type() {
		return Type.IMBidRequest;
	}

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
	 * @return the imp
	 */
	public List<Imp> getImp() {
		return imp;
	}

	/**
	 * @param imp
	 *            the imp to set
	 */
	public void setImp(List<Imp> imp) {
		this.imp = imp;
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
	 * @return the app
	 */
	public App getApp() {
		return app;
	}

	/**
	 * @param app
	 *            the app to set
	 */
	public void setApp(App app) {
		this.app = app;
	}

	/**
	 * @return the device
	 */
	public Device getDevice() {
		return device;
	}

	/**
	 * @param device
	 *            the device to set
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
	 * @return the at
	 */
	public int getAt() {
		return at;
	}

	/**
	 * @param at
	 *            the at to set
	 */
	public void setAt(int at) {
		this.at = at;
	}

	/**
	 * @return the tmax
	 */
	public Integer getTmax() {
		return tmax;
	}

	/**
	 * @param tmax
	 *            the tmax to set
	 */
	public void setTmax(Integer tmax) {
		this.tmax = tmax;
	}

	/**
	 * @return the weat
	 */
	public List<String> getWeat() {
		return weat;
	}

	/**
	 * @param weat
	 *            the weat to set
	 */
	public void setWeat(List<String> weat) {
		this.weat = weat;
	}

	/**
	 * @return the allimps
	 */
	public Integer getAllimps() {
		return allimps;
	}

	/**
	 * @param allimps the allimps to set
	 */
	public void setAllimps(Integer allimps) {
		this.allimps = allimps;
	}

	/**
	 * @return the cur
	 */
	public List<String> getCur() {
		return cur;
	}

	/**
	 * @param cur
	 *            the cur to set
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
	 * @param bcat
	 *            the bcat to set
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
	 * @param badv
	 *            the badv to set
	 */
	public void setBadv(List<String> badv) {
		this.badv = badv;
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

	public static class Device {

		private Integer dnt;
		private String ua;
		private String ip;
		private Geo geo;
		private String didsha1;
		private String didmd5;
		private String dpidsha1;
		private String dpidmd5;
		private String ipv6;
		private String carrier;
		private String language;
		private String make;
		private String model;
		private String os;
		private String osv;
		private Integer js;
		private Integer connectiontype;
		private Integer devicetype;
		private Integer flashver;
		private Ext ext;

		/**
		 * @return the dnt
		 */
		public Integer getDnt() {
			return dnt;
		}

		/**
		 * @param dnt
		 *            the dnt to set
		 */
		public void setDnt(Integer dnt) {
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
		 * @return the geo
		 */
		public Geo getGeo() {
			return geo;
		}

		/**
		 * @param geo
		 *            the geo to set
		 */
		public void setGeo(Geo geo) {
			this.geo = geo;
		}

		/**
		 * @return the didsha1
		 */
		public String getDidsha1() {
			return didsha1;
		}

		/**
		 * @param didsha1
		 *            the didsha1 to set
		 */
		public void setDidsha1(String didsha1) {
			this.didsha1 = didsha1;
		}

		/**
		 * @return the didmd5
		 */
		public String getDidmd5() {
			return didmd5;
		}

		/**
		 * @param didmd5
		 *            the didmd5 to set
		 */
		public void setDidmd5(String didmd5) {
			this.didmd5 = didmd5;
		}

		/**
		 * @return the dpidsha1
		 */
		public String getDpidsha1() {
			return dpidsha1;
		}

		/**
		 * @param dpidsha1
		 *            the dpidsha1 to set
		 */
		public void setDpidsha1(String dpidsha1) {
			this.dpidsha1 = dpidsha1;
		}

		/**
		 * @return the dpidmd5
		 */
		public String getDpidmd5() {
			return dpidmd5;
		}

		/**
		 * @param dpidmd5
		 *            the dpidmd5 to set
		 */
		public void setDpidmd5(String dpidmd5) {
			this.dpidmd5 = dpidmd5;
		}

		/**
		 * @return the ipv6
		 */
		public String getIpv6() {
			return ipv6;
		}

		/**
		 * @param ipv6
		 *            the ipv6 to set
		 */
		public void setIpv6(String ipv6) {
			this.ipv6 = ipv6;
		}

		/**
		 * @return the carrier
		 */
		public String getCarrier() {
			return carrier;
		}

		/**
		 * @param carrier
		 *            the carrier to set
		 */
		public void setCarrier(String carrier) {
			this.carrier = carrier;
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
		 * @return the make
		 */
		public String getMake() {
			return make;
		}

		/**
		 * @param make
		 *            the make to set
		 */
		public void setMake(String make) {
			this.make = make;
		}

		/**
		 * @return the model
		 */
		public String getModel() {
			return model;
		}

		/**
		 * @param model
		 *            the model to set
		 */
		public void setModel(String model) {
			this.model = model;
		}

		/**
		 * @return the os
		 */
		public String getOs() {
			return os;
		}

		/**
		 * @param os
		 *            the os to set
		 */
		public void setOs(String os) {
			this.os = os;
		}

		/**
		 * @return the osv
		 */
		public String getOsv() {
			return osv;
		}

		/**
		 * @param osv
		 *            the osv to set
		 */
		public void setOsv(String osv) {
			this.osv = osv;
		}

		/**
		 * @return the js
		 */
		public Integer getJs() {
			return js;
		}

		/**
		 * @param js
		 *            the js to set
		 */
		public void setJs(Integer js) {
			this.js = js;
		}

		/**
		 * @return the connectiontype
		 */
		public Integer getConnectiontype() {
			return connectiontype;
		}

		/**
		 * @param connectiontype
		 *            the connectiontype to set
		 */
		public void setConnectiontype(Integer connectiontype) {
			this.connectiontype = connectiontype;
		}

		/**
		 * @return the devicetype
		 */
		public Integer getDevicetype() {
			return devicetype;
		}

		/**
		 * @param devicetype
		 *            the devicetype to set
		 */
		public void setDevicetype(Integer devicetype) {
			this.devicetype = devicetype;
		}

		/**
		 * @return the flashver
		 */
		public Integer getFlashver() {
			return flashver;
		}

		/**
		 * @param flashver
		 *            the flashver to set
		 */
		public void setFlashver(Integer flashver) {
			this.flashver = flashver;
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

			private String idfasha1;
			private String idfamd5;
			private Integer age;
			private String gender;
			private String ifa;

			/**
			 * @return the idfasha1
			 */
			public String getIdfasha1() {
				return idfasha1;
			}

			/**
			 * @param idfasha1
			 *            the idfasha1 to set
			 */
			public void setIdfasha1(String idfasha1) {
				this.idfasha1 = idfasha1;
			}

			/**
			 * @return the idfamd5
			 */
			public String getIdfamd5() {
				return idfamd5;
			}

			/**
			 * @param idfamd5 the idfamd5 to set
			 */
			public void setIdfamd5(String idfamd5) {
				this.idfamd5 = idfamd5;
			}

			/**
			 * @return the age
			 */
			public Integer getAge() {
				return age;
			}

			/**
			 * @param age
			 *            the age to set
			 */
			public void setAge(Integer age) {
				this.age = age;
			}

			/**
			 * @return the gender
			 */
			public String getGender() {
				return gender;
			}

			/**
			 * @param gender
			 *            the gender to set
			 */
			public void setGender(String gender) {
				this.gender = gender;
			}

			/**
			 * @return the ifa
			 */
			public String getIfa() {
				return ifa;
			}

			/**
			 * @param ifa
			 *            the ifa to set
			 */
			public void setIfa(String ifa) {
				this.ifa = ifa;
			}
		}

	}

	public static class User {

		private String id;
		private String buyeruid;
		private Integer yob;
		private String gender;
		private String keywords;
		private String customdata;
		private Geo geo;
		private List<Object> data;
		
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
		 * @return the buyeruid
		 */
		public String getBuyeruid() {
			return buyeruid;
		}
		/**
		 * @param buyeruid the buyeruid to set
		 */
		public void setBuyeruid(String buyeruid) {
			this.buyeruid = buyeruid;
		}
		/**
		 * @return the yob
		 */
		public Integer getYob() {
			return yob;
		}
		/**
		 * @param yob the yob to set
		 */
		public void setYob(Integer yob) {
			this.yob = yob;
		}
		/**
		 * @return the gender
		 */
		public String getGender() {
			return gender;
		}
		/**
		 * @param gender the gender to set
		 */
		public void setGender(String gender) {
			this.gender = gender;
		}
		/**
		 * @return the keywords
		 */
		public String getKeywords() {
			return keywords;
		}
		/**
		 * @param keywords the keywords to set
		 */
		public void setKeywords(String keywords) {
			this.keywords = keywords;
		}
		/**
		 * @return the customdata
		 */
		public String getCustomdata() {
			return customdata;
		}
		/**
		 * @param customdata the customdata to set
		 */
		public void setCustomdata(String customdata) {
			this.customdata = customdata;
		}
		/**
		 * @return the geo
		 */
		public Geo getGeo() {
			return geo;
		}
		/**
		 * @param geo the geo to set
		 */
		public void setGeo(Geo geo) {
			this.geo = geo;
		}
		/**
		 * @return the data
		 */
		public List<Object> getData() {
			return data;
		}
		/**
		 * @param data the data to set
		 */
		public void setData(List<Object> data) {
			this.data = data;
		}
		
		
		

	}
	

	public static class Geo {

		private Float lat;
		private Float lon;
		private String country;
		private String region;
		private String regionfips104;
		private String metro;
		private String city;
		private String zip;
		private Integer type;
		private Object ext;

		/**
		 * @return the lat
		 */
		public Float getLat() {
			return lat;
		}

		/**
		 * @param lat
		 *            the lat to set
		 */
		public void setLat(Float lat) {
			this.lat = lat;
		}

		/**
		 * @return the lon
		 */
		public Float getLon() {
			return lon;
		}

		/**
		 * @param lon
		 *            the lon to set
		 */
		public void setLon(Float lon) {
			this.lon = lon;
		}

		/**
		 * @return the country
		 */
		public String getCountry() {
			return country;
		}

		/**
		 * @param country
		 *            the country to set
		 */
		public void setCountry(String country) {
			this.country = country;
		}

		/**
		 * @return the region
		 */
		public String getRegion() {
			return region;
		}

		/**
		 * @param region
		 *            the region to set
		 */
		public void setRegion(String region) {
			this.region = region;
		}

		/**
		 * @return the regionfips104
		 */
		public String getRegionfips104() {
			return regionfips104;
		}

		/**
		 * @param regionfips104
		 *            the regionfips104 to set
		 */
		public void setRegionfips104(String regionfips104) {
			this.regionfips104 = regionfips104;
		}

		/**
		 * @return the metro
		 */
		public String getMetro() {
			return metro;
		}

		/**
		 * @param metro
		 *            the metro to set
		 */
		public void setMetro(String metro) {
			this.metro = metro;
		}

		/**
		 * @return the city
		 */
		public String getCity() {
			return city;
		}

		/**
		 * @param city
		 *            the city to set
		 */
		public void setCity(String city) {
			this.city = city;
		}

		/**
		 * @return the zip
		 */
		public String getZip() {
			return zip;
		}

		/**
		 * @param zip
		 *            the zip to set
		 */
		public void setZip(String zip) {
			this.zip = zip;
		}

		/**
		 * @return the type
		 */
		public Integer getType() {
			return type;
		}

		/**
		 * @param type
		 *            the type to set
		 */
		public void setType(Integer type) {
			this.type = type;
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

	public static class Imp {

		private String id;
		private Banner banner;
		private String displaymanager;
		private String displaymanagerver;
		private Integer instl;
		private String tagid;
		private Integer bidfloor;
		private String bidfloorcur;
		private List<String> iframebuster;
		private Object ext;

		public static class Banner {

			private int w;
			private int h;
			private String id;
			private Integer pos;
			private List<Integer> btype;
			private List<Integer> battr;
			private List<Integer> mimes;
			private Integer topframe;
			private List<Integer> expdir;
			private List<Integer> api;
			private Ext ext;

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
			 * @return the pos
			 */
			public Integer getPos() {
				return pos;
			}

			/**
			 * @param pos
			 *            the pos to set
			 */
			public void setPos(Integer pos) {
				this.pos = pos;
			}

			/**
			 * @return the btype
			 */
			public List<Integer> getBtype() {
				return btype;
			}

			/**
			 * @param btype
			 *            the btype to set
			 */
			public void setBtype(List<Integer> btype) {
				this.btype = btype;
			}

			/**
			 * @return the battr
			 */
			public List<Integer> getBattr() {
				return battr;
			}

			/**
			 * @param battr
			 *            the battr to set
			 */
			public void setBattr(List<Integer> battr) {
				this.battr = battr;
			}

			/**
			 * @return the mimes
			 */
			public List<Integer> getMimes() {
				return mimes;
			}

			/**
			 * @param mimes
			 *            the mimes to set
			 */
			public void setMimes(List<Integer> mimes) {
				this.mimes = mimes;
			}

			/**
			 * @return the topframe
			 */
			public Integer getTopframe() {
				return topframe;
			}

			/**
			 * @param topframe
			 *            the topframe to set
			 */
			public void setTopframe(Integer topframe) {
				this.topframe = topframe;
			}

			/**
			 * @return the expdir
			 */
			public List<Integer> getExpdir() {
				return expdir;
			}

			/**
			 * @param expdir
			 *            the expdir to set
			 */
			public void setExpdir(List<Integer> expdir) {
				this.expdir = expdir;
			}

			/**
			 * @return the api
			 */
			public List<Integer> getApi() {
				return api;
			}

			/**
			 * @param api
			 *            the api to set
			 */
			public void setApi(List<Integer> api) {
				this.api = api;
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

				private Integer adh;
				private String orientation;
				private Integer orientationlock;

				/**
				 * @return the adh
				 */
				public Integer getAdh() {
					return adh;
				}

				/**
				 * @param adh
				 *            the adh to set
				 */
				public void setAdh(Integer adh) {
					this.adh = adh;
				}

				/**
				 * @return the orientation
				 */
				public String getOrientation() {
					return orientation;
				}

				/**
				 * @param orientation
				 *            the orientation to set
				 */
				public void setOrientation(String orientation) {
					this.orientation = orientation;
				}

				/**
				 * @return the orientationlock
				 */
				public Integer getOrientationlock() {
					return orientationlock;
				}

				/**
				 * @param orientationlock
				 *            the orientationlock to set
				 */
				public void setOrientationlock(Integer orientationlock) {
					this.orientationlock = orientationlock;
				}
			}

		}

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
		 * @return the displaymanager
		 */
		public String getDisplaymanager() {
			return displaymanager;
		}

		/**
		 * @param displaymanager the displaymanager to set
		 */
		public void setDisplaymanager(String displaymanager) {
			this.displaymanager = displaymanager;
		}

		/**
		 * @return the displaymanagerver
		 */
		public String getDisplaymanagerver() {
			return displaymanagerver;
		}

		/**
		 * @param displaymanagerver the displaymanagerver to set
		 */
		public void setDisplaymanagerver(String displaymanagerver) {
			this.displaymanagerver = displaymanagerver;
		}

		/**
		 * @return the instl
		 */
		public Integer getInstl() {
			return instl;
		}

		/**
		 * @param instl
		 *            the instl to set
		 */
		public void setInstl(Integer instl) {
			this.instl = instl;
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
		public Integer getBidfloor() {
			return bidfloor;
		}

		/**
		 * @param bidfloor
		 *            the bidfloor to set
		 */
		public void setBidfloor(Integer bidfloor) {
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

		/**
		 * @return the iframebuster
		 */
		public List<String> getIframebuster() {
			return iframebuster;
		}

		/**
		 * @param iframebuster
		 *            the iframebuster to set
		 */
		public void setIframebuster(List<String> iframebuster) {
			this.iframebuster = iframebuster;
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

	public static class Site {

		private String id;
		private String name;
		private String domain;
		private List<String> cat;
		private List<String> sectioncat;
		private List<String> pagecat;
		private String page;
		private Integer privacypolicy;
		private String ref;
		private String search;
		private String keywords;

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
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * @param name
		 *            the name to set
		 */
		public void setName(String name) {
			this.name = name;
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
		 * @return the cat
		 */
		public List<String> getCat() {
			return cat;
		}

		/**
		 * @param cat
		 *            the cat to set
		 */
		public void setCat(List<String> cat) {
			this.cat = cat;
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
		 * @return the pagecat
		 */
		public List<String> getPagecat() {
			return pagecat;
		}

		/**
		 * @param pagecat
		 *            the pagecat to set
		 */
		public void setPagecat(List<String> pagecat) {
			this.pagecat = pagecat;
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
		 * @return the privacypolicy
		 */
		public Integer getPrivacypolicy() {
			return privacypolicy;
		}

		/**
		 * @param privacypolicy
		 *            the privacypolicy to set
		 */
		public void setPrivacypolicy(Integer privacypolicy) {
			this.privacypolicy = privacypolicy;
		}

		/**
		 * @return the ref
		 */
		public String getRef() {
			return ref;
		}

		/**
		 * @param ref
		 *            the ref to set
		 */
		public void setRef(String ref) {
			this.ref = ref;
		}

		/**
		 * @return the search
		 */
		public String getSearch() {
			return search;
		}

		/**
		 * @param search
		 *            the search to set
		 */
		public void setSearch(String search) {
			this.search = search;
		}

		/**
		 * @return the keywords
		 */
		public String getKeywords() {
			return keywords;
		}

		/**
		 * @param keywords
		 *            the keywords to set
		 */
		public void setKeywords(String keywords) {
			this.keywords = keywords;
		}

	}

	public static class App {

		private String id;
		private String name;
		private String domain;
		private List<Integer> cat;
		private List<String> sectioncat;
		private List<String> pagecat;
		private String ver;
		private String bundle;
		private Integer privacypolicy;
		private Integer paid;
		private String keywords;
		private String storeurl;
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
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * @param name
		 *            the name to set
		 */
		public void setName(String name) {
			this.name = name;
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
		 * @return the cat
		 */
		public List<Integer> getCat() {
			return cat;
		}

		/**
		 * @param cat
		 *            the cat to set
		 */
		public void setCat(List<Integer> cat) {
			this.cat = cat;
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
		 * @return the pagecat
		 */
		public List<String> getPagecat() {
			return pagecat;
		}

		/**
		 * @param pagecat
		 *            the pagecat to set
		 */
		public void setPagecat(List<String> pagecat) {
			this.pagecat = pagecat;
		}

		/**
		 * @return the ver
		 */
		public String getVer() {
			return ver;
		}

		/**
		 * @param ver
		 *            the ver to set
		 */
		public void setVer(String ver) {
			this.ver = ver;
		}

		/**
		 * @return the bundle
		 */
		public String getBundle() {
			return bundle;
		}

		/**
		 * @param bundle
		 *            the bundle to set
		 */
		public void setBundle(String bundle) {
			this.bundle = bundle;
		}

		/**
		 * @return the privacypolicy
		 */
		public Integer getPrivacypolicy() {
			return privacypolicy;
		}

		/**
		 * @param privacypolicy
		 *            the privacypolicy to set
		 */
		public void setPrivacypolicy(Integer privacypolicy) {
			this.privacypolicy = privacypolicy;
		}

		/**
		 * @return the paid
		 */
		public Integer getPaid() {
			return paid;
		}

		/**
		 * @param paid
		 *            the paid to set
		 */
		public void setPaid(Integer paid) {
			this.paid = paid;
		}

		/**
		 * @return the keywords
		 */
		public String getKeywords() {
			return keywords;
		}

		/**
		 * @param keywords
		 *            the keywords to set
		 */
		public void setKeywords(String keywords) {
			this.keywords = keywords;
		}

		/**
		 * @return the storeurl
		 */
		public String getStoreurl() {
			return storeurl;
		}

		/**
		 * @param storeurl
		 *            the storeurl to set
		 */
		public void setStoreurl(String storeurl) {
			this.storeurl = storeurl;
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
