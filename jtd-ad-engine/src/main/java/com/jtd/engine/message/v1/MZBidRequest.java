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
public class MZBidRequest extends MessageV1 {

	private static final long serialVersionUID = -1705468674848871181L;

	private String id;
	//曝光对象，一次request可以有多个imp
	private List<Imp> imp;
	//广告请求的媒体对象
	private Site site;
	//广告请求的设备对象
	private Device device;
	//广告请求的用户对象数据
	private User user;
	//广告请求的移动端，app数据
	private App app;
	
	/***
	 这两个参数是否添加进去
	 wseat	array of string	DSP允许竞价的seat白名单
	 ext	object	扩展字段，参看request.ext字段说明
	 */

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
	 * @param site
	 *            the site to set
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
	 * @param user
	 *            the user to set
	 */
	public void setUser(User user) {
		this.user = user;
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

	public static class Imp {
		
		//曝光id
		private String id;
		//广告位id
		private String tagid;
		//底价,单位是分/千次曝光,即CPM
		private float bidfloor;
		//banner类型的广告位
		private Banner banner;
		//video类型的广告位
		private Video video;
		
		/**
		 这几项是否添加进来，其中ext中有广告为可接受的广告展示类型。
		 
		 nativead	object	原生类型的广告位，NativeAd定义详见文档
		 AdExchange RTB原生广告接口文档
		 pmp	object	pmp字段，deal相关的参数，参看pmp字段的说明
		 secure	Integer	用来标识流量端对监测、广告创意、落地页等资源HTTPS/HTTP协议请求，secure=1 监测、广告创意、落地页等资源必须返回https的广告信息，secure=0 监测、广告创意、落地页等资源需要返回http的广告信息
         ext	object	扩展字段,参看imp.ext字段的说明
		 */
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
		 * @return the video
		 */
		public Video getVideo() {
			return video;
		}

		/**
		 * @param video
		 *            the video to set
		 */
		public void setVideo(Video video) {
			this.video = video;
		}

		public static class Banner {
			//广告位宽度
			private int w;
			//广告位高度
			private int h;
			
			/**
			 是否添加以下两项
			 
			 pos	integer	广告位位置,兼容openRTB2.2中6.5表格关于广告位置的规定,见附录C
			 0	未知	Unknown
				1	首屏	Above the fold
				3	首屏以下	Below the fold
				4	页面顶部	Header
				5	页面底部	Footer
				6	侧边栏	Sidebar
				7	全屏	Fullscreen
				8	二屏	
				9	三屏	
				10	四屏	
				11	五屏	
				12	五屏以下

			 mimes	array of string	允许投放的物料类型["image/png","application/x-shockwave-flash","text/html"]
			 */
			

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
		}

		public static class Video {
			//支持播放的视频格式，目前支持： video/x-flv，application/x-shockwave-flash
			private List<String> mimes;
			//广告展现样式，1为in-stream, 2为overlay。"In-stream" or "linear" video refers to pre-roll, post-roll, or mid-roll video ads where the user is forced to watch ad in order to see the video content. “Overlay” or “non-linear” refer to ads that are shown on top of the video content.
			private int linearity;
			//视频广告最短播放时长，单位是秒
			private int minduration;
			//视频广告最长播放时长，单位是秒
			private int maxduration;
			//广告位宽度
			private int w;
			//广告位高度
			private int h;
			
			/**
			 是否添加的内容
			 pos	integer	广告位位置,兼容openRTB2.2中6.5表格关于广告位置的规定,见附录C

			 */

			/**
			 * @return the mimes
			 */
			public List<String> getMimes() {
				return mimes;
			}

			/**
			 * @param mimes
			 *            the mimes to set
			 */
			public void setMimes(List<String> mimes) {
				this.mimes = mimes;
			}

			/**
			 * @return the linearity
			 */
			public int getLinearity() {
				return linearity;
			}

			/**
			 * @param linearity
			 *            the linearity to set
			 */
			public void setLinearity(int linearity) {
				this.linearity = linearity;
			}

			/**
			 * @return the minduration
			 */
			public int getMinduration() {
				return minduration;
			}

			/**
			 * @param minduration
			 *            the minduration to set
			 */
			public void setMinduration(int minduration) {
				this.minduration = minduration;
			}

			/**
			 * @return the maxduration
			 */
			public int getMaxduration() {
				return maxduration;
			}

			/**
			 * @param maxduration
			 *            the maxduration to set
			 */
			public void setMaxduration(int maxduration) {
				this.maxduration = maxduration;
			}

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
		}
	}

	public static class Site {
		//媒体网站名称
		private String name;
		//当前页面URL,就是发起广告请求的页面地址
		private String page;
		//referrer URL,发起广告请求的上一页地址
		private String ref;
		//视频的内容相关信息。只有视频贴片类型的广告位才会有这个字段，参见site.content对象描述
		private Content content;

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
		 * @return the content
		 */
		public Content getContent() {
			return content;
		}

		/**
		 * @param content
		 *            the content to set
		 */
		public void setContent(Content content) {
			this.content = content;
		}

		public static class Content {
			//	视频标题名称
			private String title;
			// 视频标签关键字，如果是多个关键字，则使用英文逗号分隔
			private String keyword;
			// 参见site.content.ext描述
			private Ext ext;

			/**
			 * @return the title
			 */
			public String getTitle() {
				return title;
			}

			/**
			 * @param title
			 *            the title to set
			 */
			public void setTitle(String title) {
				this.title = title;
			}

			/**
			 * @return the keyword
			 */
			public String getKeyword() {
				return keyword;
			}

			/**
			 * @param keyword
			 *            the keyword to set
			 */
			public void setKeyword(String keyword) {
				this.keyword = keyword;
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
				// 视频的频道ID，例如"1"。注：网站的频道字典可以线下获取，将来我们会增加这个字典的映射获取接口
				private String channel;
				// 二级频道ID
				private String cs;
				
				/**
				 copyright	Integer	版权信息 0---版权信息未知 1---有版权
			     quality	Integer	流量质量 1---流量质量保障 2---流量质量未知
				 */

				/**
				 * @return the channel
				 */
				public String getChannel() {
					return channel;
				}

				/**
				 * @param channel
				 *            the channel to set
				 */
				public void setChannel(String channel) {
					this.channel = channel;
				}

				/**
				 * @return the cs
				 */
				public String getCs() {
					return cs;
				}

				/**
				 * @param cs
				 *            the cs to set
				 */
				public void setCs(String cs) {
					this.cs = cs;
				}
			}
		}

	}

	public static class Device {
		//user agent(Browser user agent string)
		private String ua;
		//客户端ip或Ipdx IP.1.dsp在灵集平台上开启了IPDX服务,在灵集平台查询到ipdx的情况下是ipdxip,未查到ipdx的情况下是客户端ip 2.dsp在灵集平台上未开启IPDX服务,ip字段传的是客户端ip
		private String ip;
		//设备的当前地理位置信息,参见geo对象详细字段描述
		private Geo geo;
		//使用MD5哈希的Device ID(对应Android系统MMA字段的IMEI值,iOS系统api限制获取不到该值)
		private String didmd5;
		/*这个字段不需要，dpidsha1	string	使用SHA1哈希的Device ID值(对应Android系统MMA字段的IMEI值,iOS系统api限制获取不到该值)*/
		//使用MD5哈希的平台相关ID,不同的系统会传不同的值，具体对应的移动端MMA字段： Android系统会传--Android ID，iOS系统会传--openudid，Windows Phone系统会传--DUID
		private String dpidmd5;
		//设备生产商，如"Apple"
		private String make;
		//设备型号,如"iPhone"
		private String model;
		//操作系统 "0-Android"/"1-iOS"/"2-WP"("Windows Phone")/"3-Others" (忽略大小写)
		private String os;
		//操作系统版本号，如"4.1"
		private String osv;
		//运营商的ID，参见OpenRTB协议及http://en.wikipedia.org/wiki/Mobile_Network_Code。
		private String carrier;
		//目前使用的国家—语言,如"zh_CN"
		private String language;
		//是否启用Javascript，1—启用（默认值），0—未启用
		private int js;
		//网络连接类型，和OpenRTB一致：0—未知，1—Ethernet，2—wifi，3—蜂窝网络，未知代，4—蜂窝网络，2G，5—蜂窝网络，3G，6—蜂窝网络，4G。
		private int connectiontype;
		//设备类型，和0—手机，1—平板，2—PC，3—互联网电视。
		private int devicetype;
		private Ext ext;

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
		 * @return the connectiontype
		 */
		public int getConnectiontype() {
			return connectiontype;
		}

		/**
		 * @param connectiontype
		 *            the connectiontype to set
		 */
		public void setConnectiontype(int connectiontype) {
			this.connectiontype = connectiontype;
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

		public static class Geo {
			//纬度（-90~90）
			private float lat;
			//经度（-180~180）
			private float lon;
			private Ext ext;

			/**
			 * @return the lat
			 */
			public float getLat() {
				return lat;
			}

			/**
			 * @param lat
			 *            the lat to set
			 */
			public void setLat(float lat) {
				this.lat = lat;
			}

			/**
			 * @return the lon
			 */
			public float getLon() {
				return lon;
			}

			/**
			 * @param lon
			 *            the lon to set
			 */
			public void setLon(float lon) {
				this.lon = lon;
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
				//GPS的精确度，单位为米。如：100表示精确度为100米
				private int accuracy;

				/**
				 * @return the accuracy
				 */
				public int getAccuracy() {
					return accuracy;
				}

				/**
				 * @param accuracy
				 *            the accuracy to set
				 */
				public void setAccuracy(int accuracy) {
					this.accuracy = accuracy;
				}
			}

		}

		public static class Ext {
			//对应的移动端MMA字段：iOS的IDFA字段(iOS系统 osv>=6时会传该字段，传的是原始值未经过md5 sum)，如："1E2DFA89-496A-47FD-9941-DF1FC4E6484A"
			private String idfa;
			//去除分隔符”:”(保持大写)的MAC地址取MD5摘要,eg:3D8A278F33E4F97181DF1EAEFE500D05
			private String mac;
			//保留分隔符”:”(保持大写)的MAC地址取MD5摘要,eg:DC7D41E352D13D60765414D53F40BC25
			private String macmd5;
			//WIFI的
			private String ssid;
			//设备的屏幕宽度，以像素为单位
			private int w;
			//设备的屏幕高度，以像素为单位
			private int h;
			//设备是否越狱，1—已启用（默认），0—未启用。
			private int brk;
			//发送请求时的本地UNIX时间戳（秒数，10进制）
			private int ts;
			//是否使用全屏/互动方式来展现广告。1—是，0—否（默认值）
			private int interstitial;
			
			/**
			 realip	string	客户端ip地址
			 isipdx	boolean	true:device中ip的值为ipdx ip；false:device中ip的值是客户端ip

			 */
			/**
			 * @return the idfa
			 */
			public String getIdfa() {
				return idfa;
			}

			/**
			 * @param idfa
			 *            the idfa to set
			 */
			public void setIdfa(String idfa) {
				this.idfa = idfa;
			}

			/**
			 * @return the mac
			 */
			public String getMac() {
				return mac;
			}

			/**
			 * @param mac
			 *            the mac to set
			 */
			public void setMac(String mac) {
				this.mac = mac;
			}

			/**
			 * @return the macmd5
			 */
			public String getMacmd5() {
				return macmd5;
			}

			/**
			 * @param macmd5
			 *            the macmd5 to set
			 */
			public void setMacmd5(String macmd5) {
				this.macmd5 = macmd5;
			}

			/**
			 * @return the ssid
			 */
			public String getSsid() {
				return ssid;
			}

			/**
			 * @param ssid
			 *            the ssid to set
			 */
			public void setSsid(String ssid) {
				this.ssid = ssid;
			}

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
			 * @return the brk
			 */
			public int getBrk() {
				return brk;
			}

			/**
			 * @param brk
			 *            the brk to set
			 */
			public void setBrk(int brk) {
				this.brk = brk;
			}

			/**
			 * @return the ts
			 */
			public int getTs() {
				return ts;
			}

			/**
			 * @param ts
			 *            the ts to set
			 */
			public void setTs(int ts) {
				this.ts = ts;
			}

			/**
			 * @return the interstitial
			 */
			public int getInterstitial() {
				return interstitial;
			}

			/**
			 * @param interstitial
			 *            the interstitial to set
			 */
			public void setInterstitial(int interstitial) {
				this.interstitial = interstitial;
			}
		}
	}

	public static class User {
		//Xtrader用户ID(即灵集域的cookie id)
		private String id;
		//以下这两个属性，在新的接口中不用了
		//private String gender;
		//private int yob;
		
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

		
//		public String getGender() {
//			return gender;
//		}
//
//		public void setGender(String gender) {
//			this.gender = gender;
//		}
//
//		
//		public int getYob() {
//			return yob;
//		}
//
//		public void setYob(int yob) {
//			this.yob = yob;
//		}

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
			private List<String> models;
			/**
			 List中String的取值
			 10000	性别_男	DataPlus提供	
			10001	性别_女	DataPlus提供	
			10002	个人月收入_1000RMB以下	DataPlus提供	
			10003	个人月收入_1001-3000RMB	DataPlus提供	
			10004	个人月收入_3001-5000RMB	DataPlus提供	
			10005	个人月收入_5001-10000RMB	DataPlus提供	
			10006	个人月收入_10000RMB以上	DataPlus提供	
			10007	年龄_14-19岁	DataPlus提供	
			10008	年龄_20-24岁	DataPlus提供	
			10009	年龄_25-29岁	DataPlus提供	
			10010	年龄_30-34岁	DataPlus提供	
			10011	年龄_35-39岁	DataPlus提供	
			10012	年龄_40-44岁	DataPlus提供	
			10013	年龄_45-49岁	DataPlus提供	
			10014	年龄_其他年龄	DataPlus提供
			 * */
			/**
			 * @return the models
			 */
			public List<String> getModels() {
				return models;
			}

			/**
			 * @param models
			 *            the models to set
			 */
			public void setModels(List<String> models) {
				this.models = models;
			}
		}
	}

	public static class App {
		
		//App的名称
		private String name;
		private String bundle; 	//APP应用的包名称或bundleID,如果android内容是packageName，如果是ios的话，则是bundleID
		private String itid;	//	iOS App iTunes ID	628677149
		//content	object	视频的内容相关信息。只有视频贴片类型的广告位才会有这个字段，同site.content对象
		//cat	Array of string	广告位内容分类，兼容IAB分类，符合openRTB 2.2表格6.1的分类方法。对应的编号和中英文对照表见附录

		private Ext ext;

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
		
		public String getBundle() {
			return bundle;
		}

		public void setBundle(String bundle) {
			this.bundle = bundle;
		}

		public String getItid() {
			return itid;
		}

		public void setItid(String itid) {
			this.itid = itid;
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
			//投放SDK的版本，例如“91_v1”
			private String sdk;
			//	应用商店，1—iOS Appstore，2—Google Play，3—91Market。
			private int market;
			//这几个暂时用不到
//			private String appid;
//			private String cat;
//			private String tag;

			/**
			 * @return the sdk
			 */
			public String getSdk() {
				return sdk;
			}

			/**
			 * @param sdk
			 *            the sdk to set
			 */
			public void setSdk(String sdk) {
				this.sdk = sdk;
			}

			/**
			 * @return the market
			 */
			public int getMarket() {
				return market;
			}

			/**
			 * @param market
			 *            the market to set
			 */
			public void setMarket(int market) {
				this.market = market;
			}

			
//			public String getAppid() {
//				return appid;
//			}
//			public void setAppid(String appid) {
//				this.appid = appid;
//			}
//			public String getCat() {
//				return cat;
//			}
//			public void setCat(String cat) {
//				this.cat = cat;
//			}
//			public String getTag() {
//				return tag;
//			}
//			public void setTag(String tag) {
//				this.tag = tag;
//			}
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.maxit.adserver.message.v1.MessageV1#type()
	 */
	public Type type() {
		return Type.MZBidRequest;
	}
}