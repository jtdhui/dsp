package com.jtd.engine.message.v1;

import com.jtd.engine.adserver.message.v1.MessageV1;

import java.util.List;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置
 * @创建日期 2017年3月20日
 * @项目名称 dsp-engine
 * @描述
 *     <p>
 *     </p>
 */
public class HzBidRequest extends MessageV1 {

	private static final long serialVersionUID = -1705468674848871181L;

	// 请求ID
	private String id;
	// 曝光对象，一次request可以有多个imp
	private List<Imp> imp;
	// 广告请求的媒体对象
	private Site site;
	// 广告请求的设备对象
	private Device device;
	// 广告请求的用户对象数据
	private User user;
	// 竞标方式,系统默认为2(次高价成交)
	private int at;
	// 流量类型，0 为pc，1为移动端
	private int is_mobil;
	// 流量约定交易号
	private String deal_id;

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

	public int getAt() {
		return at;
	}

	public void setAt(int at) {
		this.at = at;
	}

	public int getIs_mobil() {
		return is_mobil;
	}

	public void setIs_mobil(int is_mobil) {
		this.is_mobil = is_mobil;
	}

	public String getDeal_id() {
		return deal_id;
	}

	public void setDeal_id(String deal_id) {
		this.deal_id = deal_id;
	}

	/**
	 * @作者 Amos Xu
	 * @版本 V1.0
	 * @配置
	 * @创建日期 2017年3月20日
	 * @项目名称 dsp-engine
	 * @描述
	 *     <p>
	 *     这次广告请求中，描述广告的对象
	 *     </p>
	 */
	public static class Imp {

		// 曝光id
		private String id;
		// 广告位id
		private String tagid;
		// 底价,单位是分/千次曝光,即CPM
		private float bidfloor;
		// banner类型的广告位
		private Banner banner;
		// video类型的广告位
		private Video video;
		// 标记是否为贴底打折流量，1为是，0为否
		private int discount;

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

		public int getDiscount() {
			return discount;
		}

		public void setDiscount(int discount) {
			this.discount = discount;
		}

		/**
		 * Request.Imp.Banner
		 * 
		 * @作者 Amos Xu
		 * @版本 V1.0
		 * @配置
		 * @创建日期 2017年3月20日
		 * @项目名称 dsp-engine
		 * @描述
		 *     <p>
		 *     描述竞价请求中，imp中banner数据的
		 *     </p>
		 */
		public static class Banner {
			// 广告位宽度
			private int w;
			// 广告位高度
			private int h;

			// 支持的物料格式，jpg/png/gif/swf/c/icon , c-动态创意，icon-图文创意
			private List<String> mimes;

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

			public List<String> getMimes() {
				return mimes;
			}

			public void setMimes(List<String> mimes) {
				this.mimes = mimes;
			}

		}

		/**
		 * Reqest.imp.video
		 * 
		 * @作者 Amos Xu
		 * @版本 V1.0
		 * @配置
		 * @创建日期 2017年6月27日
		 * @项目名称 dsp-engine
		 * @描述
		 *     <p>
		 *     </p>
		 */
		public static class Video {
			// 支持播放的广告格式，flv/swf/mp4
			private List<String> mimes;
			// 广告展现样式，1为in-stream, 2为overlay。"In-stream" or "linear" video
			// refers to pre-roll, post-roll, or mid-roll video ads where the
			// user is forced to watch ad in order to see the video content.
			// “Overlay” or “non-linear” refer to ads that are shown on top of
			// the video content.
			private int linearity;
			// 视频广告最短播放时长，单位是秒
			private int minduration;
			// 视频广告最长播放时长，单位是秒
			private int maxduration;
			// 广告位宽度
			private int w;
			// 广告位高度
			private int h;

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

		/**
		 * XtraderBidRequest.imp.Nativead
		 * 
		 * @作者 Amos Xu
		 * @版本 V1.0
		 * @配置
		 * @创建日期 2017年3月20日
		 * @项目名称 dsp-engine
		 * @描述
		 *     <p>
		 *     描述原生信息流数据(201703这版暂时用不上)
		 *     </p>
		 */
		public static class Nativead {

		}

		/**
		 * XtraderBidRequest.imp.pmp
		 * 
		 * @作者 Amos Xu
		 * @版本 V1.0
		 * @配置
		 * @创建日期 2017年3月20日
		 * @项目名称 dsp-engine
		 * @描述
		 *     <p>
		 *     描述pmp对接数据
		 *     </p>
		 */
		public static class Pmp {

		}

		/**
		 * XTraderBidRequest.imp.ext
		 * 
		 * @作者 Amos Xu
		 * @版本 V1.0
		 * @配置
		 * @创建日期 2017年3月20日
		 * @项目名称 dsp-engine
		 * @描述
		 *     <p>
		 *     </p>
		 */
		public static class Ext {
			// 展示类型,灵集对广告展示形式的一种分类,具体见附录B
			/**
			 * 1 PC端网页banner 2 PC端网页video 3 PC端网页背投 4 PC端网页视频暂停 5 PC端网页弹窗 6
			 * PC客户端banner 7 PC客户端video 8 PC客户端弹窗 9 PC端网页视频悬浮 10 PC端网页端信息流 11
			 * 移动WAP端banner 12 移动WAP端video 13 移动WAP端信息流 14 移动APP端横幅 15 移动APP端开屏
			 * 16 移动APP端插屏 17 移动APP端video 18 移动端视频暂停 19 移动APP端应用墙 20 移动APP端信息流
			 */
			private Integer showType;
			// 该字段表示客户端是否支持发送winnotice(nurl字段)以及支持曝光的条数：
			// 1表示会发送winnotice并且支持多条曝光,
			// 0表示不会发送winnotice并且只支持一条曝光
			private Integer has_winnotice;
			/**
			 * 该字段表示是否支持异步点击监测(cm),以及dsp点击监测是否需要302跳转到落地页。 if
			 * has_winnotice=0,都不支持异步的点击监测(cm),只支持ldp字段。
			 * has_clickthrough=1表示ldp字段dsp返回点击监测url必须302 redirect到广告落地页
			 * has_clickthrough=0表示ldp字段只返回dsp点击监测url，不用302 redirect到落地页
			 */
			private Integer has_clickthrough;
			// 媒体资源位置支持的交互类型：1.支持网页打开类+下载类广告 2.只支持打开类广告 3.只支持下载类广告
			private Integer action_type;

			public Integer getShowType() {
				return showType;
			}

			public void setShowType(Integer showType) {
				this.showType = showType;
			}

			public Integer getHas_winnotice() {
				return has_winnotice;
			}

			public void setHas_winnotice(Integer has_winnotice) {
				this.has_winnotice = has_winnotice;
			}

			public Integer getHas_clickthrough() {
				return has_clickthrough;
			}

			public void setHas_clickthrough(Integer has_clickthrough) {
				this.has_clickthrough = has_clickthrough;
			}

			public Integer getAction_type() {
				return action_type;
			}

			public void setAction_type(Integer action_type) {
				this.action_type = action_type;
			}

		}
	}

	/**
	 * Request.Site
	 * 
	 * @作者 Amos Xu
	 * @版本 V1.0
	 * @配置
	 * @创建日期 2017年6月20日
	 * @项目名称 dsp-engine
	 * @描述
	 *     <p>
	 *     用户描述媒体信息数据类
	 *     </p>
	 */
	public static class Site {
		// 媒体网站名称或app名称
		private String name;
		// 当前页面URL,就是发起广告请求的页面地址
		private String page;
		// referrer URL,发起广告请求的上一页地址
		private String ref;
		// 媒体类型（0-其他，1-wap、2-app、3-web、4-client）
		private int type;
		// app当前版本
		private String ver;
		// app包名或者bundleId
		private String bundle;
		// 付费方式（0-其他、1-免费、2-付费、3-应用内付费）
		private int paid;

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

		public int getType() {
			return type;
		}

		public void setType(int type) {
			this.type = type;
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

		public int getPaid() {
			return paid;
		}

		public void setPaid(int paid) {
			this.paid = paid;
		}
	}

	/**
	 * Request.Device
	 * 
	 * @作者 Amos Xu
	 * @版本 V1.0
	 * @配置
	 * @创建日期 2017年6月20日
	 * @项目名称 dsp-engine
	 * @描述
	 *     <p>
	 *     竞价请求描述设备信息类
	 *     </p>
	 */
	public static class Device {
		// user agent(Browser user agent string)
		private String ua;
		// 客户端ip
		private String ip;
		// 设备id号，id类型由identify_type定义
		private String udid;
		// udid的类型，可选：idfa,imei,mac,idfv,发送给DSP时会经过MD5加密
		private String identify_type;
		// idfa，明文
		private String idfa;
		// imei,明文
		private String imei;
		// 设备制造商
		private String vendor;
		// 设备型号
		private String model;

		// 操作系统 1:android,2:ios,3:windowsPhone,0:其他
		private int os;
		// 操作系统版本号
		private String os_version;

		// 网络方式，0：其他，1：wifi，2：2G，3：3G，4：4G
		private int network;
		// 运营商，0：其他，1：移动，2：联通，3：电信
		private int operator;
		// 设备的语言
		private String language;
		// 设备屏幕物理分辨率宽
		private int dwidth;
		// 设备屏幕物理分辨率高
		private int dheight;
		// 纬度
		private String lat;
		// 经度
		private String lon;

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

		public String getUdid() {
			return udid;
		}

		public void setUdid(String udid) {
			this.udid = udid;
		}

		public String getIdentify_type() {
			return identify_type;
		}

		public void setIdentify_type(String identify_type) {
			this.identify_type = identify_type;
		}

		public String getIdfa() {
			return idfa;
		}

		public void setIdfa(String idfa) {
			this.idfa = idfa;
		}

		public String getImei() {
			return imei;
		}

		public void setImei(String imei) {
			this.imei = imei;
		}

		public String getVendor() {
			return vendor;
		}

		public void setVendor(String vendor) {
			this.vendor = vendor;
		}

		public String getModel() {
			return model;
		}

		public void setModel(String model) {
			this.model = model;
		}

		public int getOs() {
			return os;
		}

		public void setOs(int os) {
			this.os = os;
		}

		public String getOs_version() {
			return os_version;
		}

		public void setOs_version(String os_version) {
			this.os_version = os_version;
		}

		public int getNetwork() {
			return network;
		}

		public void setNetwork(int network) {
			this.network = network;
		}

		public int getOperator() {
			return operator;
		}

		public void setOperator(int operator) {
			this.operator = operator;
		}

		public String getLanguage() {
			return language;
		}

		public void setLanguage(String language) {
			this.language = language;
		}

		public int getDwidth() {
			return dwidth;
		}

		public void setDwidth(int dwidth) {
			this.dwidth = dwidth;
		}

		public int getDheight() {
			return dheight;
		}

		public void setDheight(int dheight) {
			this.dheight = dheight;
		}

		public String getLat() {
			return lat;
		}

		public void setLat(String lat) {
			this.lat = lat;
		}

		public String getLon() {
			return lon;
		}

		public void setLon(String lon) {
			this.lon = lon;
		}

	}

	/***
	 * XtraderBidRequest.Device.Ext
	 * 
	 * @作者 Amos Xu
	 * @版本 V1.0
	 * @配置
	 * @创建日期 2017年3月20日
	 * @项目名称 dsp-engine
	 * @描述
	 *     <p>
	 *     设备的扩展信息
	 *     </p>
	 */
	public static class Ext {
		// 对应的移动端MMA字段：iOS的IDFA字段(iOS系统 osv>=6时会传该字段，传的是原始值未经过md5
		// sum)，如："1E2DFA89-496A-47FD-9941-DF1FC4E6484A"
		private String idfa;
		// 去除分隔符”:”(保持大写)的MAC地址取MD5摘要,eg:3D8A278F33E4F97181DF1EAEFE500D05
		private String mac;
		// 保留分隔符”:”(保持大写)的MAC地址取MD5摘要,eg:DC7D41E352D13D60765414D53F40BC25
		private String macmd5;
		// WIFI的
		private String ssid;
		// 设备的屏幕宽度，以像素为单位
		private int w;
		// 设备的屏幕高度，以像素为单位
		private int h;
		// 设备是否越狱，1—已启用（默认），0—未启用。
		private int brk;
		// 发送请求时的本地UNIX时间戳（秒数，10进制）
		private int ts;
		// 是否使用全屏/互动方式来展现广告。1—是，0—否（默认值）
		private int interstitial;
		// 客户端ip地址
		private String realip;
		// true:device中ip的值为ipdx ip；false:device中ip的值是客户端ip
		private boolean isipdx;

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

		public String getRealip() {
			return realip;
		}

		public void setRealip(String realip) {
			this.realip = realip;
		}

		public boolean isIsipdx() {
			return isipdx;
		}

		public void setIsipdx(boolean isipdx) {
			this.isipdx = isipdx;
		}

	}

	/**
	 * Request.User
	 * 
	 * @作者 Amos Xu
	 * @版本 V1.0
	 * @配置
	 * @创建日期 2017年6月20日
	 * @项目名称 dsp-engine
	 * @描述
	 *     <p>
	 *     竞价请求时发送过来的用户信息数据
	 *     </p>
	 */
	public static class User {
		// adx用户id
		private String id;
		// 已经映射的DSP的用户id
		private String buyerid;
		// 1或0，标识uid是否是稳定的
		private int stable;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getBuyerid() {
			return buyerid;
		}

		public void setBuyerid(String buyerid) {
			this.buyerid = buyerid;
		}

		public int getStable() {
			return stable;
		}

		public void setStable(int stable) {
			this.stable = stable;
		}
	}

	@Override
	public Type type() {
		return Type.HzBidRequest;
	}

}
