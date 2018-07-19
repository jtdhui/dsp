package com.jtd.engine.message.v1;

import java.util.List;

import com.jtd.engine.adserver.message.v1.MessageV1;

/**
 * 安沃广告请求request
 * @author zl
 *
 */
public class AdwoBidRequest extends MessageV1{

	private static final long serialVersionUID = 7877205133721439512L;

	@Override
	public Type type() {
		return Type.AdwoBidRequest;
	}
	
	/** 请求ID */
	private String id;
	/** 曝光对象(只包含一个banner或video对象，不会同时包含) */
	private Imp imp;
	/** 媒体站点对象 */
	private Site site;
	/** 设备对象 */
	private Device device;
	/** 用户对象 */
	private User user;
	/** 应用对象(请求非移动应用时，此字段为空) */
	private App app;
	
	/** 曝光对象类 */
	public static class Imp
	{
		/** 曝光ID */
		private String id;
		/** 广告位ID */
		private String tagid;
		/** 底价，单位是分/千次曝光，即CPM */
		private Float bidfloor;
		/** 底价单位，目前CNY */
		private String bidfloorcur;
		/** banner类型广告位 */
		private Banner banner;
		/** video类型广告位 */
		private Video video;
		/** 扩展字段 */
		private Ext ext;
		public static class Ext{}
		
		public static class Banner
		{
			/** 广告位宽度*/
			private Integer w;
			/** 广告位高度 */
			private Integer h;
			public Integer getW() {
				return w;
			}
			public void setW(Integer w) {
				this.w = w;
			}
			public Integer getH() {
				return h;
			}
			public void setH(Integer h) {
				this.h = h;
			}
		}
		public static class Video
		{
			/** 广告位宽度*/
			private Integer w;
			/** 广告位高度 */
			private Integer h;
			/** 支持播放的视频格式image/jpg, image/png, image/gif, video/x-flv，application/x-shockwave-flash */
			private List<String> mimes;
			/** 视频广告展现样式， 1：in-stream，2：overlay */
			private Integer linearity;
			/** 视频广告最短播放时长,单位：秒 */
			private Integer minduration;
			/** 视频广告最长播放时长，单位：秒 */
			private Integer maxduration;
			public Integer getW() {
				return w;
			}
			public void setW(Integer w) {
				this.w = w;
			}
			public Integer getH() {
				return h;
			}
			public void setH(Integer h) {
				this.h = h;
			}
			public List<String> getMimes() {
				return mimes;
			}
			public void setMimes(List<String> mimes) {
				this.mimes = mimes;
			}
			public Integer getLinearity() {
				return linearity;
			}
			public void setLinearity(Integer linearity) {
				this.linearity = linearity;
			}
			public Integer getMinduration() {
				return minduration;
			}
			public void setMinduration(Integer minduration) {
				this.minduration = minduration;
			}
			public Integer getMaxduration() {
				return maxduration;
			}
			public void setMaxduration(Integer maxduration) {
				this.maxduration = maxduration;
			}
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getTagid() {
			return tagid;
		}
		public void setTagid(String tagid) {
			this.tagid = tagid;
		}
		public Float getBidfloor() {
			return bidfloor;
		}
		public void setBidfloor(Float bidfloor) {
			this.bidfloor = bidfloor;
		}
		public String getBidfloorcur() {
			return bidfloorcur;
		}
		public void setBidfloorcur(String bidfloorcur) {
			this.bidfloorcur = bidfloorcur;
		}
		public Banner getBanner() {
			return banner;
		}
		public void setBanner(Banner banner) {
			this.banner = banner;
		}
		public Video getVideo() {
			return video;
		}
		public void setVideo(Video video) {
			this.video = video;
		}
		public Ext getExt() {
			return ext;
		}
		public void setExt(Ext ext) {
			this.ext = ext;
		}
	}
	
	/** 媒体站点对象类 */
	public static class Site
	{
		/** 媒体网站名称 */
		private String name;
		/** 当前页面URL */
		private String page;
		/** referrer URL */
		private String ref;
		/** 站点域名 */
		private String domain;
		/** 站点关键字，如果是多个使用英文逗号分隔 */
		private String keywords;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getPage() {
			return page;
		}
		public void setPage(String page) {
			this.page = page;
		}
		public String getRef() {
			return ref;
		}
		public void setRef(String ref) {
			this.ref = ref;
		}
		public String getDomain() {
			return domain;
		}
		public void setDomain(String domain) {
			this.domain = domain;
		}
		public String getKeywords() {
			return keywords;
		}
		public void setKeywords(String keywords) {
			this.keywords = keywords;
		}
	}
	
	/** 设备对象类 */
	public static class Device
	{
		/** 设备IP地址 */
		private String ip;
		/** 地理位置 */
		private Geo geo;
		/** 厂商 */
		private String make;
		/** 机型 */
		private String model;
		/** 操作系统 */
		private String os;
		/** 操作系统版本 */
		private String ovs;
		/** 是否支持js */
		private Integer js;
		/** 连接类型 0 未知，1 局域网(PC)，2 WIFI，3 蜂窝网络未知，4 蜂窝网络2G，5 蜂窝网络3G，6蜂窝网络4G */
		private Integer connectiontype;
		/** 设备类型 0 未知，1 iphone，2 android手机，3 ipad，4 WindowsPhone， 5 android平板，6 智能TV */
		private Integer devicetype;
		/** 浏览器语言 */
		private String language;
		/** 运营商 0未知，1中国移动，2 中国联通，3中国电信 */
		private String carrier;
		/** androidid的MD5 */
		private String apidmd5;
		/** androidid的SHA1 */
		private String dpidsha1;
		/** Imei的SHA1 */
		private String didsha1;
		/** Imei的MD5 */
		private String didmd5;
		/** Mac地址的SHA1 */
		private String macsha1;
		/** Mac地址的MD5 */
		private String macmd5;
		/** 浏览器的UA */
		private String ua;
		/** 广告主的本地标识 */
		private String ifa;
		/** 设备信息扩展参数 */
		private Ext ext;
		
		/** 设备地理位置类 */
		public static class Geo
		{
			/** 纬度 */
			private Float lat;
			/** 经度 */
			private Float lon;
			/** 国家 */
			private String country;
			/** 地区 */
			private String region;
			/** 城市 */
			private String city;
			
			public Float getLat() {
				return lat;
			}
			public void setLat(Float lat) {
				this.lat = lat;
			}
			public Float getLon() {
				return lon;
			}
			public void setLon(Float lon) {
				this.lon = lon;
			}
			public String getCountry() {
				return country;
			}
			public void setCountry(String country) {
				this.country = country;
			}
			public String getRegion() {
				return region;
			}
			public void setRegion(String region) {
				this.region = region;
			}
			public String getCity() {
				return city;
			}
			public void setCity(String city) {
				this.city = city;
			}
		}
		
		/** 设备信息扩展参数类 */
		public static class Ext
		{
			private Udi udi;
			/** 屏幕宽 */
			private Integer sw;
			/** 屏幕高 */
			private Integer sh;
			/** 是否越狱/root */
			private Integer jbk;
			
			public static class Udi
			{
				/** Android设备的imei */
				private String imei;
				/** Android设备的androidid */
				private String androidid;
				/** Ios设备的idfa */
				private String idfa;
				/** Android设备的advertising */
				private String advertising;
				public String getImei() {
					return imei;
				}
				public void setImei(String imei) {
					this.imei = imei;
				}
				public String getAndroidid() {
					return androidid;
				}
				public void setAndroidid(String androidid) {
					this.androidid = androidid;
				}
				public String getIdfa() {
					return idfa;
				}
				public void setIdfa(String idfa) {
					this.idfa = idfa;
				}
				public String getAdvertising() {
					return advertising;
				}
				public void setAdvertising(String advertising) {
					this.advertising = advertising;
				}
			}

			public Udi getUdi() {
				return udi;
			}

			public void setUdi(Udi udi) {
				this.udi = udi;
			}

			public Integer getSw() {
				return sw;
			}

			public void setSw(Integer sw) {
				this.sw = sw;
			}

			public Integer getSh() {
				return sh;
			}

			public void setSh(Integer sh) {
				this.sh = sh;
			}

			public Integer getJbk() {
				return jbk;
			}

			public void setJbk(Integer jbk) {
				this.jbk = jbk;
			}
		}
		public String getIp() {
			return ip;
		}
		public void setIp(String ip) {
			this.ip = ip;
		}
		public Geo getGeo() {
			return geo;
		}
		public void setGeo(Geo geo) {
			this.geo = geo;
		}
		public String getMake() {
			return make;
		}
		public void setMake(String make) {
			this.make = make;
		}
		public String getModel() {
			return model;
		}
		public void setModel(String model) {
			this.model = model;
		}
		public String getOs() {
			return os;
		}
		public void setOs(String os) {
			this.os = os;
		}
		public String getOvs() {
			return ovs;
		}
		public void setOvs(String ovs) {
			this.ovs = ovs;
		}
		public Integer getJs() {
			return js;
		}
		public void setJs(Integer js) {
			this.js = js;
		}
		public Integer getConnectiontype() {
			return connectiontype;
		}
		public void setConnectiontype(Integer connectiontype) {
			this.connectiontype = connectiontype;
		}
		public Integer getDevicetype() {
			return devicetype;
		}
		public void setDevicetype(Integer devicetype) {
			this.devicetype = devicetype;
		}
		public String getLanguage() {
			return language;
		}
		public void setLanguage(String language) {
			this.language = language;
		}
		public String getCarrier() {
			return carrier;
		}
		public void setCarrier(String carrier) {
			this.carrier = carrier;
		}
		public String getApidmd5() {
			return apidmd5;
		}
		public void setApidmd5(String apidmd5) {
			this.apidmd5 = apidmd5;
		}
		public String getDpidsha1() {
			return dpidsha1;
		}
		public void setDpidsha1(String dpidsha1) {
			this.dpidsha1 = dpidsha1;
		}
		public String getDidsha1() {
			return didsha1;
		}
		public void setDidsha1(String didsha1) {
			this.didsha1 = didsha1;
		}
		public String getDidmd5() {
			return didmd5;
		}
		public void setDidmd5(String didmd5) {
			this.didmd5 = didmd5;
		}
		public String getMacsha1() {
			return macsha1;
		}
		public void setMacsha1(String macsha1) {
			this.macsha1 = macsha1;
		}
		public String getMacmd5() {
			return macmd5;
		}
		public void setMacmd5(String macmd5) {
			this.macmd5 = macmd5;
		}
		public String getUa() {
			return ua;
		}
		public void setUa(String ua) {
			this.ua = ua;
		}
		public String getIfa() {
			return ifa;
		}
		public void setIfa(String ifa) {
			this.ifa = ifa;
		}
		public Ext getExt() {
			return ext;
		}
		public void setExt(Ext ext) {
			this.ext = ext;
		}
	}
	
	/** 用户对象类 */
	public static class User
	{
		/** 用户ID */
		private String id;
		/** 出生日期 */
		private String yob;
		/** 性别 */
		private String gender;
		/** 扩展参数 */
		private Ext ext;
		
		public static class Ext{}

		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getYob() {
			return yob;
		}
		public void setYob(String yob) {
			this.yob = yob;
		}
		public String getGender() {
			return gender;
		}
		public void setGender(String gender) {
			this.gender = gender;
		}
		public Ext getExt() {
			return ext;
		}
		public void setExt(Ext ext) {
			this.ext = ext;
		}
	}
	
	/** 应用对象类型 */
	public static class App
	{
		/** APP的唯一标识 */
		private String id;
		/** APP的名称 */
		private String name;
		/** APP的版本 */
		private String ver;
		/** APP的包名 */
		private String bundle;
		/** APP的域名 */
		private String domain;
		/** APP的关键字 */
		private String keywords;
		/** 是否付费 */
		private Integer paid;
		/** APP下载地址 */
		private String storeurl;
		/** APP在被访问的内容 */
		private Content content;
		/** 开发者对象 */
		private Publisher publisher;
		
		/** APP在被访问的内容类 */
		public static class Content
		{
			/** APP里浏览内容的title */
			private String title;
			/** 扩展对象 */
			private Ext ext;
			public static class Ext{}
			public String getTitle() {
				return title;
			}
			public void setTitle(String title) {
				this.title = title;
			}
			public Ext getExt() {
				return ext;
			}
			public void setExt(Ext ext) {
				this.ext = ext;
			}
		}
		/** 开发者对象类 */
		public static class Publisher
		{
			/** 开发者名称 */
			private String name;
			/** 扩展对象 */
			private Ext ext;
			public static class Ext{}
			public String getName() {
				return name;
			}
			public void setName(String name) {
				this.name = name;
			}
			public Ext getExt() {
				return ext;
			}
			public void setExt(Ext ext) {
				this.ext = ext;
			}
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getVer() {
			return ver;
		}
		public void setVer(String ver) {
			this.ver = ver;
		}
		public String getBundle() {
			return bundle;
		}
		public void setBundle(String bundle) {
			this.bundle = bundle;
		}
		public String getDomain() {
			return domain;
		}
		public void setDomain(String domain) {
			this.domain = domain;
		}
		public String getKeywords() {
			return keywords;
		}
		public void setKeywords(String keywords) {
			this.keywords = keywords;
		}
		public Integer getPaid() {
			return paid;
		}
		public void setPaid(Integer paid) {
			this.paid = paid;
		}
		public String getStoreurl() {
			return storeurl;
		}
		public void setStoreurl(String storeurl) {
			this.storeurl = storeurl;
		}
		public Content getContent() {
			return content;
		}
		public void setContent(Content content) {
			this.content = content;
		}
		public Publisher getPublisher() {
			return publisher;
		}
		public void setPublisher(Publisher publisher) {
			this.publisher = publisher;
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Imp getImp() {
		return imp;
	}

	public void setImp(Imp imp) {
		this.imp = imp;
	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public App getApp() {
		return app;
	}

	public void setApp(App app) {
		this.app = app;
	}
}
