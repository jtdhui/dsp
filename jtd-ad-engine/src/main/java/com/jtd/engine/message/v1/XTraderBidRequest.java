package com.jtd.engine.message.v1;

import java.util.List;


import com.jtd.engine.adserver.message.v1.MessageV1;


/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2017年3月20日
 * @项目名称 dsp-engine
 * @描述 <p></p>
 */
public class XTraderBidRequest extends MessageV1{
	
	
	private static final long serialVersionUID = -1705468674848871181L;
	
	//请求ID
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
	
	//request 对象中的 ext 扩展属性。
	private String media_source;
	

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

	public String getMedia_source() {
		return media_source;
	}
	public void setMedia_source(String media_source) {
		this.media_source = media_source;
	}


	/**
	 * @作者 Amos Xu
	 * @版本 V1.0
	 * @配置 
	 * @创建日期 2017年3月20日
	 * @项目名称 dsp-engine
	 * @描述 <p>这次广告请求中，描述广告的对象</p>
	 */
	public static class Imp {
		
		//曝光id，由零集发过来的曝光id
		private String id;
		//广告位id
		private String tagid;
		//底价,单位是分/千次曝光,即CPM
		private float bidfloor;
		//banner类型的广告位
		private Banner banner;
		//video类型的广告位
		private Video video;
		//原生信息流(201703这版暂时不支持原生信息流)
		private Nativead nativead;
		//pmp字段(201703这版暂时不支持pmp接入)
		private Pmp pmp;
		//用来标识流量端对监测、广告创意、落地页等资源HTTPS/HTTP协议请求，
		//secure=1 监测、广告创意、落地页等资源必须返回https的广告信息，
		//secure=0 监测、广告创意、落地页等资源需要返回http的广告信息
		private Integer secure;
		
		
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
		
		public Nativead getNativead() {
			return nativead;
		}

		public void setNativead(Nativead nativead) {
			this.nativead = nativead;
		}

		public Pmp getPmp() {
			return pmp;
		}

		public void setPmp(Pmp pmp) {
			this.pmp = pmp;
		}

		public Integer getSecure() {
			return secure;
		}

		public void setSecure(Integer secure) {
			this.secure = secure;
		}

		public Ext getExt() {
			return ext;
		}

		public void setExt(Ext ext) {
			this.ext = ext;
		}

		/**
		 * XTraderBidRequest.Imp.Banner
		 * @作者 Amos Xu
		 * @版本 V1.0
		 * @配置 
		 * @创建日期 2017年3月20日
		 * @项目名称 dsp-engine
		 * @描述 <p>描述竞价请求中，imp中banner数据的</p>
		 */
		public static class Banner {
			//广告位宽度
			private int w;
			//广告位高度
			private int h;
			//广告位位置,兼容openRTB2.2中6.5表格关于广告位置的规定,见附录C
			/**
			 *  0	未知	Unknown
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
			 */
			private int pos;
			//允许投放的物料类型["image/png","application/x-shockwave-flash","text/html"]
			/**
			 png	*.png	image/png	PNG图片
			 jpg	*.jpg,*.jpeg	image/jpg	JPG或JPEG图片
			 gif	*.gif	image/gif	GIF图片
			 flv	*.flv	video/x-flv	FLV视频
			 swf	*.swf	application/x-shockwave-flash	Flash动画
			 mp4	*.mp4	video/mp4	MP4视频
			 x	    *.swf,*.flv	application/x-shockwave-flash,video/x-flv	富媒体类型的Flash动画。注意，Exchange处理x类型物料时，会忽略掉落地页字段（即bidobject中"bid":"clickm"或"bid":"ext":"ldp"字段），因为x类型物料是应当自带点击跳转地址的。但是点击监测地址可以传送过来。
			 c	    HTML	text/html	HTML类型的动态物料，包括iframe或javascript格式。可以内嵌任何形式的HTML。
			 * */
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

			public int getPos() {
				return pos;
			}

			public void setPos(int pos) {
				this.pos = pos;
			}

			public List<String> getMimes() {
				return mimes;
			}

			public void setMimes(List<String> mimes) {
				this.mimes = mimes;
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
			//
			/**
			编号	中文名	英文名
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
			 * */
			private int pos;
			
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

			public int getPos() {
				return pos;
			}

			public void setPos(int pos) {
				this.pos = pos;
			}
			
		}
		
		/**
		 * XtraderBidRequest.imp.Nativead
		 * @作者 Amos Xu
		 * @版本 V1.0
		 * @配置 
		 * @创建日期 2017年3月20日
		 * @项目名称 dsp-engine
		 * @描述 <p>描述原生信息流数据(201703这版暂时用不上)</p>
		 */
		public static class Nativead{
			
		}
		
		/**
		 * XtraderBidRequest.imp.pmp
		 * @作者 Amos Xu
		 * @版本 V1.0
		 * @配置 
		 * @创建日期 2017年3月20日
		 * @项目名称 dsp-engine
		 * @描述 <p>描述pmp对接数据</p>
		 */
		public static class Pmp{
			
		}
		
		/**
		 * XTraderBidRequest.imp.ext
		 * @作者 Amos Xu
		 * @版本 V1.0
		 * @配置 
		 * @创建日期 2017年3月20日
		 * @项目名称 dsp-engine
		 * @描述 <p></p>
		 */
		public static class Ext{
			//展示类型,灵集对广告展示形式的一种分类,具体见附录B
			/**
			 1	PC端网页banner
			2	PC端网页video
			3	PC端网页背投
			4	PC端网页视频暂停
			5	PC端网页弹窗
			6	PC客户端banner
			7	PC客户端video
			8	PC客户端弹窗
			9	PC端网页视频悬浮
			10	PC端网页端信息流
			11	移动WAP端banner
			12	移动WAP端video
			13	移动WAP端信息流
			14	移动APP端横幅
			15	移动APP端开屏
			16	移动APP端插屏
			17	移动APP端video
			18	移动端视频暂停
			19	移动APP端应用墙
			20	移动APP端信息流
			 * */
			private Integer showType;
			//该字段表示客户端是否支持发送winnotice(nurl字段)以及支持曝光的条数：
			//1表示会发送winnotice并且支持多条曝光,
			//0表示不会发送winnotice并且只支持一条曝光
			private Integer has_winnotice;
			/**
			 * 该字段表示是否支持异步点击监测(cm),以及dsp点击监测是否需要302跳转到落地页。
				if has_winnotice=0,都不支持异步的点击监测(cm),只支持ldp字段。 
				has_clickthrough=1表示ldp字段dsp返回点击监测url必须302 redirect到广告落地页 
				has_clickthrough=0表示ldp字段只返回dsp点击监测url，不用302 redirect到落地页
			 * */
			private Integer has_clickthrough;
			//媒体资源位置支持的交互类型：1.支持网页打开类+下载类广告 2.只支持打开类广告 3.只支持下载类广告
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
	 * XTraderBidRequest.Site
	 * @作者 Amos Xu
	 * @版本 V1.0
	 * @配置 
	 * @创建日期 2017年3月20日
	 * @项目名称 dsp-engine
	 * @描述 <p>用户描述媒体信息数据类</p>
	 */
	public static class Site {
		//媒体网站名称
		private String name;
		//当前页面URL,就是发起广告请求的页面地址
		private String page;
		//referrer URL,发起广告请求的上一页地址
		private String ref;
		//视频的内容相关信息。只有视频贴片类型的广告位才会有这个字段，参见site.content对象描述
		private Content content;
		//请求时零集发送过来的，媒体的分类信息（这是和零集那边确认后的结果）
		private List<String> cat;

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
		
		
		
		public List<String> getCat() {
			return cat;
		}

		public void setCat(List<String> cat) {
			this.cat = cat;
		}



		/**
		 * XTraderBidRequest.Site.Content
		 * @作者 Amos Xu
		 * @版本 V1.0
		 * @配置 
		 * @创建日期 2017年3月20日
		 * @项目名称 dsp-engine
		 * @描述 <p>竞价请求中，如果 XTraderBidRequest.Site.Content 中有值，则说明是一次视频广告请求</p>
		 */
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
			
			/**
			 * XtraderBidRequest.site.content.ext
			 * @作者 Amos Xu
			 * @版本 V1.0
			 * @配置 
			 * @创建日期 2017年3月20日
			 * @项目名称 dsp-engine
			 * @描述 <p>网站视频媒体发起广告请求，content扩展信息</p>
			 */
			public static class Ext {
				// 视频的频道ID，例如"1"。注：网站的频道字典可以线下获取，将来我们会增加这个字典的映射获取接口
				private String channel;
				// 二级频道ID
				private String cs;
				
				//版权信息 0---版权信息未知 1---有版权
				private int copyright;
				
				//流量质量 1---流量质量保障 2---流量质量未知
				private int quality;
				

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

				public int getCopyright() {
					return copyright;
				}

				public void setCopyright(int copyright) {
					this.copyright = copyright;
				}

				public int getQuality() {
					return quality;
				}

				public void setQuality(int quality) {
					this.quality = quality;
				}
			}
		}

	}
	
	/**
	 * XTraderBidRequest.Device
	 * @作者 Amos Xu
	 * @版本 V1.0
	 * @配置 
	 * @创建日期 2017年3月20日
	 * @项目名称 dsp-engine
	 * @描述 <p>竞价请求描述设备信息类</p>
	 */
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
		//扩展信息
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
		
		/**
		 * XTraderBidRequest.Device.Geo
		 * @作者 Amos Xu
		 * @版本 V1.0
		 * @配置 
		 * @创建日期 2017年3月20日
		 * @项目名称 dsp-engine
		 * @描述 <p>经纬度信息</p>
		 */
		public static class Geo {
			//纬度（-90~90）
			private float lat;
			//经度（-180~180）
			private float lon;
			//经纬度信息的扩展信息
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
			
			/**
			 * XTraderBidRequest.Device.Geo.Ext
			 * @作者 Amos Xu
			 * @版本 V1.0
			 * @配置 
			 * @创建日期 2017年3月20日
			 * @项目名称 dsp-engine
			 * @描述 <p>请求时经纬度信息的扩展信息</p>
			 */
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
		
		/***
		 * XtraderBidRequest.Device.Ext
		 * @作者 Amos Xu
		 * @版本 V1.0
		 * @配置 
		 * @创建日期 2017年3月20日
		 * @项目名称 dsp-engine
		 * @描述 <p>设备的扩展信息</p>
		 */
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
			//客户端ip地址
			private String realip;
			//true:device中ip的值为ipdx ip；false:device中ip的值是客户端ip
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
	}
	
	/**
	 * XTraderBidRequest.User
	 * @作者 Amos Xu
	 * @版本 V1.0
	 * @配置 
	 * @创建日期 2017年3月20日
	 * @项目名称 dsp-engine
	 * @描述 <p>竞价请求时发送过来的用户信息数据</p>
	 */
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
		
		/**
		 * XTraderBidRequest.User.Ext
		 * @作者 Amos Xu
		 * @版本 V1.0
		 * @配置 
		 * @创建日期 2017年3月20日
		 * @项目名称 dsp-engine
		 * @描述 <p>竞价请求中用户的扩展信息</p>
		 */
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
	
	/**
	 * XTraderBidRequest.App
	 * @作者 Amos Xu
	 * @版本 V1.0
	 * @配置 
	 * @创建日期 2017年3月20日
	 * @项目名称 dsp-engine
	 * @描述 <p>竞价请求时发送过来的app应用信息</p>
	 */
	public static class App {
		
		//App的名称
		private String name;
		//APP应用的包名称或bundleID,如果android内容是packageName，如果是ios的话，则是bundleID
		private String bundle;
		//iOS App iTunes ID	628677149
		private String itid;	
		//content	object	视频的内容相关信息。只有视频贴片类型的广告位才会有这个字段，同site.content对象
		private Content content;
		//cat	Array of string	广告位内容分类，兼容IAB分类，符合openRTB 2.2表格6.1的分类方法。对应的编号和中英文对照表见附录
		//该值为app这个媒体的分类数据
		private List<String> cat;
		//app 扩展信息
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

		public Content getContent() {
			return content;
		}

		public void setContent(Content content) {
			this.content = content;
		}


		public List<String> getCat() {
			return cat;
		}

		public void setCat(List<String> cat) {
			this.cat = cat;
		}






		/**
		 * XTraderBidRequest.App.Content
		 * @作者 Amos Xu
		 * @版本 V1.0
		 * @配置 
		 * @创建日期 2017年3月20日
		 * @项目名称 dsp-engine
		 * @描述 <p>竞价请求中，如果请求是一个视频广告则该类中有数据</p>
		 */
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
			
			/**
			 * XtraderBidRequest.app.content.ext
			 * @作者 Amos Xu
			 * @版本 V1.0
			 * @配置 
			 * @创建日期 2017年3月20日
			 * @项目名称 dsp-engine
			 * @描述 <p>网站视频媒体发起广告请求，content扩展信息</p>
			 */
			public static class Ext {
				// 视频的频道ID，例如"1"。注：网站的频道字典可以线下获取，将来我们会增加这个字典的映射获取接口
				private String channel;
				// 二级频道ID
				private String cs;
				
				//版权信息 0---版权信息未知 1---有版权
				private int copyright;
				
				//流量质量 1---流量质量保障 2---流量质量未知
				private int quality;
				

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

				public int getCopyright() {
					return copyright;
				}

				public void setCopyright(int copyright) {
					this.copyright = copyright;
				}

				public int getQuality() {
					return quality;
				}

				public void setQuality(int quality) {
					this.quality = quality;
				}
			}
		}
		
		/**
		 * XtraderBidRequest.app.ext
		 * @作者 Amos Xu
		 * @版本 V1.0
		 * @配置 
		 * @创建日期 2017年3月20日
		 * @项目名称 dsp-engine
		 * @描述 <p></p>
		 */
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
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.maxit.adserver.message.v1.MessageV1#type()
	 */
	public Type type() {
		return Type.XTraderBidRequest;
	}

}
