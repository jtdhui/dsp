package com.jtd.engine.message.v1;

import java.util.List;
import com.jtd.engine.adserver.message.v1.MessageV1;

/**
 * adView广告请求
 * @author zl
 *
 */
public class AVBidRequest extends MessageV1 {

	private static final long serialVersionUID = 4894275696338167349L;
	
	public Type type() {
		return Type.AVBidRequest;
	}
	
	/** 广告请求唯一标识，由adView生成 */
	private String id;
	/** 需要广告的描述  */
	private List<Imp> imp;
	/** 应用信息 */
	private App app;
	/** 设备信息 */
	private Device device;
	/** 用户信息 */
	private User user;
	/** 竞拍类型 0:最高价格成交，1:以次高价成交 ，2:优先购买（不参与竞价）*/
	private int at;
	/** 超时时间（单位：秒） */
	private Integer tmax;
	/** 广告主缩略名称数组，只接受这些广告主 */ //adView反馈可以不用次参数
	private List<String> weat;
	/** 广告创意数组，本次请求只接受这些创意 */ //adView反馈可以不用次参数
	private List<String> wcid;
	/** 允许结算的货币 USD,RMB */
	private List<String> cur;
	/** 拒绝接受的行业广告类型 */
	private List<String> bcat;
	/** 拒绝接受的广告主名单，采用域名标示广告主 */
	private List<String> badv;
	/** IOS的激活回到URL，仅对下载IOS类广告有效 */
	private String callback;
	/** 相关法律、政府或行业法规对象 */
	private Regs regs;
	/** 扩展内容 */
	private Object ext;

	public static class Imp 
	{
		/** 唯一标识，adView生成 */
		private String id;
		/** 横幅或插屏广告信息 */
		private Banner banner;
		/** 原生广告信息 */
		private Nativead nativead;
		/** 视频广告信息 */
		private Video video;
		/** 私有交易 */
		private Pmp pmp;
		/** 0：横幅广告，1：插屏或全屏广告，4：开屏广告，5：视频广告，6：原生广告 */
		private Integer instl;
		/** 用于发起拍卖的特定广告位置或广告牌的标识 */
		private String tagid;
		/** 低价，CMP或CPC价格*10000，如CMP价格为0.6元，取值为0.6*10000 */
		private Integer bidfloor;
		/** 价格单位：USD或RMB，缺省RMB */
		private String bidfloorcur;
		/** 标识曝光是否需要安全的HTTPS URL创意素材或标记，0：不安全，1：安全 */
		private Integer secure;
		/** 扩展内容 */
		private Object ext;

		public static class Banner 
		{
			/** 宽度 */
			private int w;
			/** 高度 */
			private int h;
			/** 广告在屏幕位置 */
			private Integer pos;
			/** 拒绝的广告类型 */
			private List<Integer> btype;
			/** 拒绝的广告创意属性 */
			private List<Integer> battr;
			/** 广告物料形式支持 */
			private List<Integer> mimes;
			/** 支持的广告API */
			private List<Integer> api;
			/** 扩展内容 */
			private Object ext;
			public int getW() {
				return w;
			}
			public void setW(int w) {
				this.w = w;
			}
			public int getH() {
				return h;
			}
			public void setH(int h) {
				this.h = h;
			}
			public Integer getPos() {
				return pos;
			}
			public void setPos(Integer pos) {
				this.pos = pos;
			}
			public List<Integer> getBtype() {
				return btype;
			}
			public void setBtype(List<Integer> btype) {
				this.btype = btype;
			}
			public List<Integer> getBattr() {
				return battr;
			}
			public void setBattr(List<Integer> battr) {
				this.battr = battr;
			}
			public List<Integer> getMimes() {
				return mimes;
			}
			public void setMimes(List<Integer> mimes) {
				this.mimes = mimes;
			}
			public List<Integer> getApi() {
				return api;
			}
			public void setApi(List<Integer> api) {
				this.api = api;
			}
			public Object getExt() {
				return ext;
			}
			public void setExt(Object ext) {
				this.ext = ext;
			}
		}
		
		public static class Nativead
		{
			/** 原生广告协议 */
			private Request request;
			/** 原生广告版本 */
			private String ver;
			/** 支持的广告API */
			private List<Integer> api;
			/** 拒绝的广告创意属性 */
			private List<Integer> battr;
			/** 扩展内容 */
			private Ext ext;
			
			public static class Request{}
			public static class Ext{}
			public Request getRequest() {
				return request;
			}
			public void setRequest(Request request) {
				this.request = request;
			}
			public String getVer() {
				return ver;
			}
			public void setVer(String ver) {
				this.ver = ver;
			}
			public List<Integer> getApi() {
				return api;
			}
			public void setApi(List<Integer> api) {
				this.api = api;
			}
			public List<Integer> getBattr() {
				return battr;
			}
			public void setBattr(List<Integer> battr) {
				this.battr = battr;
			}
			public Ext getExt() {
				return ext;
			}
			public void setExt(Ext ext) {
				this.ext = ext;
			}
			
			
		}
		
		public static class Video
		{
			/** 支持的视频格式 */
			private List<String> mimes;
			/** 支持的视频广告投放协议 */
			private List<Integer> protocols;
			/** 广告展现样式1：线性，2-非线性 */
			private String linearity;
			/** 曝光所处的位序，比如贴片的话就表明该曝光是第几贴 */
			private Integer sequence;
			/** 拒绝的广告创意属性 */
			private List<Integer> battr;
			/** 视频最小kb */
			private Integer minbitrate;
			/** 视频最大kb */
			private Integer maxbitrate;
			/** 视频最短播放时长 */
			private Integer minduration;
			/** 视频最长播放时长 */
			private Integer maxduration;
			/** 宽度 */
			private Integer w;
			/** 高度 */
			private Integer h;
			/** 扩展内容 */
			private Ext ext;
			
			public static class Ext{}

			public List<String> getMimes() {
				return mimes;
			}

			public void setMimes(List<String> mimes) {
				this.mimes = mimes;
			}

			public List<Integer> getProtocols() {
				return protocols;
			}

			public void setProtocols(List<Integer> protocols) {
				this.protocols = protocols;
			}

			public String getLinearity() {
				return linearity;
			}

			public void setLinearity(String linearity) {
				this.linearity = linearity;
			}

			public Integer getSequence() {
				return sequence;
			}

			public void setSequence(Integer sequence) {
				this.sequence = sequence;
			}

			public List<Integer> getBattr() {
				return battr;
			}

			public void setBattr(List<Integer> battr) {
				this.battr = battr;
			}

			public Integer getMinbitrate() {
				return minbitrate;
			}

			public void setMinbitrate(Integer minbitrate) {
				this.minbitrate = minbitrate;
			}

			public Integer getMaxbitrate() {
				return maxbitrate;
			}

			public void setMaxbitrate(Integer maxbitrate) {
				this.maxbitrate = maxbitrate;
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

			public Ext getExt() {
				return ext;
			}

			public void setExt(Ext ext) {
				this.ext = ext;
			}
			
		}
		
		public static class Pmp{}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public Banner getBanner() {
			return banner;
		}

		public void setBanner(Banner banner) {
			this.banner = banner;
		}

		public Nativead getNativead() {
			return nativead;
		}

		public void setNativead(Nativead nativead) {
			this.nativead = nativead;
		}

		public Video getVideo() {
			return video;
		}

		public void setVideo(Video video) {
			this.video = video;
		}

		public Pmp getPmp() {
			return pmp;
		}

		public void setPmp(Pmp pmp) {
			this.pmp = pmp;
		}

		public Integer getInstl() {
			return instl;
		}

		public void setInstl(Integer instl) {
			this.instl = instl;
		}

		public String getTagid() {
			return tagid;
		}

		public void setTagid(String tagid) {
			this.tagid = tagid;
		}

		public Integer getBidfloor() {
			return bidfloor;
		}

		public void setBidfloor(Integer bidfloor) {
			this.bidfloor = bidfloor;
		}

		public String getBidfloorcur() {
			return bidfloorcur;
		}

		public void setBidfloorcur(String bidfloorcur) {
			this.bidfloorcur = bidfloorcur;
		}

		public Integer getSecure() {
			return secure;
		}

		public void setSecure(Integer secure) {
			this.secure = secure;
		}

		public Object getExt() {
			return ext;
		}

		public void setExt(Object ext) {
			this.ext = ext;
		}
	}

	public static class App 
	{
		/** APP唯一标识，由adView生成 */
		private String id;
		/** App名称 */
		private String name;
		/** App官网地址 */
		private String domain;
		/** App类型 */
		private List<Integer> cat;
		/** 版本 */
		private String ver;
		/** BundleID或包名 */
		private String bundle;
		/** 是否为付费App,0:不是，1:是付费，2:应用内付费 */
		private Integer paid;
		/** APP关键词，多个逗号分隔 */
		private String keywords;
		/** 市场下载地址 */
		private String storeurl;
		/** 扩展内容 */
		private Object ext;
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
		public String getDomain() {
			return domain;
		}
		public void setDomain(String domain) {
			this.domain = domain;
		}
		public List<Integer> getCat() {
			return cat;
		}
		public void setCat(List<Integer> cat) {
			this.cat = cat;
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
		public Integer getPaid() {
			return paid;
		}
		public void setPaid(Integer paid) {
			this.paid = paid;
		}
		public String getKeywords() {
			return keywords;
		}
		public void setKeywords(String keywords) {
			this.keywords = keywords;
		}
		public String getStoreurl() {
			return storeurl;
		}
		public void setStoreurl(String storeurl) {
			this.storeurl = storeurl;
		}
		public Object getExt() {
			return ext;
		}
		public void setExt(Object ext) {
			this.ext = ext;
		}
		
	}
	
	public static class Device 
	{
		/** 是否允许广告追踪，0：允许，1：不允许 */
		private Integer dnt;
		/** 浏览器user agent值 */
		private String ua;
		/**设备IP地址  */
		private String ip;
		/** 地理信息对象 */
		private Geo geo;
		/** 原始IDFA值 */
		private String idfa;
		/** 广告用户使用的ID */
		private String ifa;
		/** EMEI的SHA1值 */
		private String didsha1;
		/** AndroidID或IDFA的SHA1值 */
		private String dpidsha1;
		/** MAC地址的SHA1值 */
		private String macsha1;
		/** IMEI的md5值 */
		private String didmd5;
		/** AndroidID或IDFA的md5值 */
		private String dpidmd5;
		/** MAC得知的md5值 */
		private String macmd5;
		/** 设备使用的运营商MCC+MNC */
		private String carrier;
		/** 设备的语言设置 */
		private String language;
		/** 设备制造商 */
		private String make;
		/** 设备型号 */
		private String model;
		/** 操作系统 */
		private String os;
		/** 系统版本号 */
		private String osv;
		/** 是否支持js脚本，1：支持，0：不支持 */
		private Integer js;
		/** 设备电量百分比 */
		private Integer battery;
		/** 设备联网类型 0-未知,1-局域网(PC),2-WIFI,3-蜂窝数据网络-未知,4-蜂窝数据网络–2G,5-蜂窝数据网络–3G,6-蜂窝数据网络–4G */
		private Integer connectiontype;
		/** 设备类型 0:未知，1：iPhone，2：android手机，3：iPad,
		 * 4:windowsPhone，5:Android平板，6：智能TV，7：PC端个人电脑 */
		private Integer devicetype;
		/** 设备屏幕像素必读 */
		private Float s_density;
		/** 屏幕分辨率宽度 */
		private Integer sw;
		/** 屏幕分辨率高度 */
		private Integer sh;
		/** 设备屏幕方向，0：竖屏，1：横屏 */
		private Integer orientation;
		/** 扩展内容 */
		private Object ext;
		public Integer getDnt() {
			return dnt;
		}
		public void setDnt(Integer dnt) {
			this.dnt = dnt;
		}
		public String getUa() {
			return ua;
		}
		public void setUa(String ua) {
			this.ua = ua;
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
		public String getIdfa() {
			return idfa;
		}
		public void setIdfa(String idfa) {
			this.idfa = idfa;
		}
		public String getIfa() {
			return ifa;
		}
		public void setIfa(String ifa) {
			this.ifa = ifa;
		}
		public String getDidsha1() {
			return didsha1;
		}
		public void setDidsha1(String didsha1) {
			this.didsha1 = didsha1;
		}
		public String getDpidsha1() {
			return dpidsha1;
		}
		public void setDpidsha1(String dpidsha1) {
			this.dpidsha1 = dpidsha1;
		}
		public String getMacsha1() {
			return macsha1;
		}
		public void setMacsha1(String macsha1) {
			this.macsha1 = macsha1;
		}
		public String getDidmd5() {
			return didmd5;
		}
		public void setDidmd5(String didmd5) {
			this.didmd5 = didmd5;
		}
		public String getDpidmd5() {
			return dpidmd5;
		}
		public void setDpidmd5(String dpidmd5) {
			this.dpidmd5 = dpidmd5;
		}
		public String getMacmd5() {
			return macmd5;
		}
		public void setMacmd5(String macmd5) {
			this.macmd5 = macmd5;
		}
		public String getCarrier() {
			return carrier;
		}
		public void setCarrier(String carrier) {
			this.carrier = carrier;
		}
		public String getLanguage() {
			return language;
		}
		public void setLanguage(String language) {
			this.language = language;
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
		public String getOsv() {
			return osv;
		}
		public void setOsv(String osv) {
			this.osv = osv;
		}
		public Integer getJs() {
			return js;
		}
		public void setJs(Integer js) {
			this.js = js;
		}
		public Integer getBattery() {
			return battery;
		}
		public void setBattery(Integer battery) {
			this.battery = battery;
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
		public Float getS_density() {
			return s_density;
		}
		public void setS_density(Float s_density) {
			this.s_density = s_density;
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
		public Integer getOrientation() {
			return orientation;
		}
		public void setOrientation(Integer orientation) {
			this.orientation = orientation;
		}
		public Object getExt() {
			return ext;
		}
		public void setExt(Object ext) {
			this.ext = ext;
		}
		

	}

	public static class User 
	{
		/** 用户唯一标识 */
		private String id;
		/** 出生年，4位数字 */
		private Integer yob;
		/** 年龄范围，最小年龄 */
		private Integer age_low;
		/** 年龄范围，最大年龄 */
		private Integer age_high;
		/** 性别，M：男性，F：女性,O其它，null：未知 */
		private String gender;
		/** 用户兴趣或倾向，多个逗号分隔 */
		private String keywords;
		/** 额外的用户数据 */
		private List<Data> data;
		/** 用户家庭位置 */
		private Geo geo;
		/**  */
		private Object ext;
		
		public static class Data
		{
			/** 数据源唯一标识ID */
			private String id;
			/** 数据源名称 */
			private String name;
			/**  */
			private List<Segment> segment;
			/** 扩展字段 */
			private Ext ext;
			
			public static class Segment
			{
				/** 数据段编号 */
				private String id;
				/** 数据段名称 */
				private String name;
				/** 数据段内容ID */
				private String value;
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
				public String getValue() {
					return value;
				}
				public void setValue(String value) {
					this.value = value;
				}
				
			}
			public static class Ext{}
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
			public List<Segment> getSegment() {
				return segment;
			}
			public void setSegment(List<Segment> segment) {
				this.segment = segment;
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

		public Integer getYob() {
			return yob;
		}

		public void setYob(Integer yob) {
			this.yob = yob;
		}

		public Integer getAge_low() {
			return age_low;
		}

		public void setAge_low(Integer age_low) {
			this.age_low = age_low;
		}

		public Integer getAge_high() {
			return age_high;
		}

		public void setAge_high(Integer age_high) {
			this.age_high = age_high;
		}

		public String getGender() {
			return gender;
		}

		public void setGender(String gender) {
			this.gender = gender;
		}

		public String getKeywords() {
			return keywords;
		}

		public void setKeywords(String keywords) {
			this.keywords = keywords;
		}

		public List<Data> getData() {
			return data;
		}

		public void setData(List<Data> data) {
			this.data = data;
		}

		public Geo getGeo() {
			return geo;
		}

		public void setGeo(Geo geo) {
			this.geo = geo;
		}

		public Object getExt() {
			return ext;
		}

		public void setExt(Object ext) {
			this.ext = ext;
		}

		

	}

	public static class Geo 
	{
        /** 维度 */
		private Float lat;
		/** 经度 */
		private Float lon;
		/** 获取经纬度时的时间戳 */
		private Integer timestamp;
		/** 国家 */
		private String country;
		/** 地区 */
		private String region;
		/** 城市 */
		private String city;
		/** 数据来源，1：GPS,2：IP,3：User provided */
		private Integer type;
		/** 扩展内容 */
		private Object ext;
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
		public Integer getTimestamp() {
			return timestamp;
		}
		public void setTimestamp(Integer timestamp) {
			this.timestamp = timestamp;
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
		public Integer getType() {
			return type;
		}
		public void setType(Integer type) {
			this.type = type;
		}
		public Object getExt() {
			return ext;
		}
		public void setExt(Object ext) {
			this.ext = ext;
		}
	}

	
	public static class Regs
	{
		/** 事宜儿童内容标示，美国地区流量必须：
		 * coppa=0 不是面向儿童
		 * coppa=1 是面向儿童
		 *  */
		private Integer coppa;
		/** 扩展内容 */
		private Ext ext;
		
		public static class Ext{}

		public Integer getCoppa() {
			return coppa;
		}

		public void setCoppa(Integer coppa) {
			this.coppa = coppa;
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


	public List<Imp> getImp() {
		return imp;
	}


	public void setImp(List<Imp> imp) {
		this.imp = imp;
	}


	public App getApp() {
		return app;
	}


	public void setApp(App app) {
		this.app = app;
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


	public int getAt() {
		return at;
	}


	public void setAt(int at) {
		this.at = at;
	}


	public Integer getTmax() {
		return tmax;
	}


	public void setTmax(Integer tmax) {
		this.tmax = tmax;
	}


	public List<String> getWeat() {
		return weat;
	}


	public void setWeat(List<String> weat) {
		this.weat = weat;
	}


	public List<String> getWcid() {
		return wcid;
	}


	public void setWcid(List<String> wcid) {
		this.wcid = wcid;
	}


	public List<String> getCur() {
		return cur;
	}


	public void setCur(List<String> cur) {
		this.cur = cur;
	}


	public List<String> getBcat() {
		return bcat;
	}


	public void setBcat(List<String> bcat) {
		this.bcat = bcat;
	}


	public List<String> getBadv() {
		return badv;
	}


	public void setBadv(List<String> badv) {
		this.badv = badv;
	}


	public String getCallback() {
		return callback;
	}


	public void setCallback(String callback) {
		this.callback = callback;
	}


	public Regs getRegs() {
		return regs;
	}


	public void setRegs(Regs regs) {
		this.regs = regs;
	}


	public Object getExt() {
		return ext;
	}


	public void setExt(Object ext) {
		this.ext = ext;
	}
	
	
}
