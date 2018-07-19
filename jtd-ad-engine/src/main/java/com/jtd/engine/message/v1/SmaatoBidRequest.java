package com.jtd.engine.message.v1;

import java.util.List;

import com.jtd.engine.adserver.message.v1.MessageV1;

/**
 * Smaato RTB2.4 广告请求
 * @author zl
 *
 */
public class SmaatoBidRequest extends MessageV1{

	private static final long serialVersionUID = -2657242443280721657L;

	@Override
	public Type type() {
		return Type.SmaatoBidRequest;
	}
	
	/** 出价请求的唯一ID */
	private String id;
	/** 展示位对象数组  */
	private List<Imp> imp;
	/** 媒体站点对象 */
	private Site site;
	/** app信息 */
	private App app;
	/** 设备信息 */
	private Device device;
	/** 用户信息 */
	private User user;
	/** 拍卖不能计费的测试模式指标，其中0 =实时模式，1 =测试模式 */
	private Integer test;
	/** 拍卖类型，其中1 = First Price，2 = Second Price Plus */
	private Integer at;
	/** 提交出价以避免超时的最长时间（以毫秒为单位）。这个值通常是离线传递的  */
	private Integer tmax;
	/** 买方白名单（例如，广告客户，代理机构） */
	private List<String> wseat;
	private Integer allimps;
	/** 支持出价货币种类 */
	private List<String> cur;
	/** 使用IAB内容类别阻止的广告客户类别 */
	private List<String> bcat;
	/** 阻止其域名的广告客户列表（例如，“ ford.com ”） */
	private List<String> badv;
	/** 阻止应用程序列表。在Android上，这些应该是捆绑或包名称（例如，com.foo.mygame）。在iOS上，这些是数字ID。 */
	private List<String> bapp;
	/** 规定了有关此请求的任何行业，法律或政府法规 */
	private Regs regs;
	/** 扩展内容 */
	private Ext ext;
	
	/** 展示位对象 */
	public static class Imp
	{
		/** 展示位ID */
		private String id;
		/** banner广告 */
		private Banner banner;
		/** 视频广告 */
		private Video video;
		/** 音频广告，目前不支持 */
		private Audio audio;
		/** 原生广告，目前不支持 */
		private Nativead nativead;
		/** PMP私人交易市场，目前不支持pmp接入 */
		private Pmp pmp;
		/** 广告中介合作伙伴 */
		private String displaymanager;
		/** 版本的广告中介合作伙伴 */
		private String displaymanagerver;
		/** 1 =广告是非页内广告或全屏，0 =不是非页内广告 */
		private Integer instal;
		/** 用于发起拍卖的特定广告展示位置或广告代码的标识符。这对调试任何问题或由买方进行优化都是有用的 */
		private String tagid;
		/** 以每千次展示费用表示的此展示的最低出价   */
		private Float bidfloor;
		/** 货币使用ISO-4217 alpha代码指定 */
		private String bidfloorcur;
		/** 指示在应用中单击广告素材时打开的浏览器类型，其中0 = embedded，1 = native。 */
		private Integer clickbrowser;
		/** 标记以指示展示是否需要安全的HTTPS网址广告素材资源和标记，其中0 =不安全，1 =安全。 */
		private Integer secure;
		/** 支持的iframe破坏者的特定于Exchange的名称的数组 */
		private List<String> iframebuster;
		/** 竞价与广告位之间可能经过的秒数说明 */
		private Integer exp;
		/** 扩展内容 */
		private Ext ext;
		
		/** banner广告 */
		public static class Banner
		{
			/** 宽度 */
			private Integer x;
			/** 高度 */
			private Integer h;
			/** 格式数组 */
			private List<Format> format;
			/** ID */
			private String id;
			/** 广告类型 */
			private List<Integer> btype;
			/** 广告素材属性 */
			private List<Integer> battr;
			/** 广告位位置 */
			private Integer pos;
			/** 支持内容MIME类型。流行的MIME类型可能包括“application / x-shockwave-flash”，“image / jpg”和“image / gif”。 */
			private List<String> mimes;
			/** 是否在顶部框架中，而不是iframe，其中0 =否，1 =是 */
			private Integer topframe;
			/** 广告的方向 */
			private List<Integer> expdir;
			/** 支持的API框架 */
			private List<Integer> api;
			/** 扩展内容 */
			private Ext ext;
			
			public static class Format
			{
				/** 宽度 */
				private Integer w;
				/** 高度 */
				private Integer h;
				/** 扩展字段 */
				private Ext ext;
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

			public Integer getX() {
				return x;
			}

			public void setX(Integer x) {
				this.x = x;
			}

			public Integer getH() {
				return h;
			}

			public void setH(Integer h) {
				this.h = h;
			}

			public List<Format> getFormat() {
				return format;
			}

			public void setFormat(List<Format> format) {
				this.format = format;
			}

			public String getId() {
				return id;
			}

			public void setId(String id) {
				this.id = id;
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

			public Integer getPos() {
				return pos;
			}

			public void setPos(Integer pos) {
				this.pos = pos;
			}

			public List<String> getMimes() {
				return mimes;
			}

			public void setMimes(List<String> mimes) {
				this.mimes = mimes;
			}

			public Integer getTopframe() {
				return topframe;
			}

			public void setTopframe(Integer topframe) {
				this.topframe = topframe;
			}

			public List<Integer> getExpdir() {
				return expdir;
			}

			public void setExpdir(List<Integer> expdir) {
				this.expdir = expdir;
			}

			public List<Integer> getApi() {
				return api;
			}

			public void setApi(List<Integer> api) {
				this.api = api;
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
			/** 视频类型
			  * 对于iOS，我们发送：视频/ 3GPP，视频/ MOV，视频/ MP4，视频/ MP
			  * 对于所有其他操作系统，我们发送：视频/ 3GPP，视频/ MP4 
			*/
			private List<String> mimes;
			/** 最短视频广告持续时间（秒） */
			private Integer minduration;
			/** 视频广告持续时间（秒） */
			private Integer maxduration;
			/** 支持的视频协议数组 */
			private List<Integer> protocols;
			/** 宽度 */
			private Integer w;
			/** 高度  */
			private Integer h;
			/** 表示前贴片广告，插播广告或后贴片广告展示位置的开始延迟（以秒为单位）。 */
			private Integer startdelay;
			/** 指示展示是否必须是线性，非线性等。如果没有指定，则假定所有都是允许的 */
			private Integer linearity;
			/** 指示播放器是否允许跳过视频，其中0 =否，1 =是 */
			private Integer skip;
			/** 总持续时间大于此秒数的视频可以跳过; 只适用于广告可跳过 */
			private Integer skipmin;
			/** 视频必须播放的秒数才能跳过; 只适用于广告可跳过。 */
			private Integer skipafter;
			/** 如果在同一出价请求中提供多个广告展示，则序列号将允许协调传递多个广告素材。  */
			private Integer sequence;
			/** 广告素材属性 */
			private List<Integer> battr;
			/** 如果允许扩展，则延长广告持续时间的最大值 如果为空或0，则不允许扩展。如果-1，则允许扩展，并且没有时间限制。如果大于0，则该值表示超出maxduration值的扩展播放的秒数。 */
			private Integer maxextended;
			/** 最小比特率（Kbps） */
			private Integer minbitrate;
			/** 最大比特率（Kbps） */
			private Integer maxbitrate;
			/** 指示是否允许将4：3内容信封在16：9窗口中，其中0 =否，1 =是 */
			private Integer boxingallowed;
			/** 正在使用的播放方法。如果没有指定，可以使用任何方法 */
			private List<Integer> playbackmethod;
			/** 支持的传送方式（如流式传输，逐行扫描）。如果没有指定，则假定所有都被支持。 */
			private List<Integer> delivery;
			/** 屏幕上的广告位置  */
			private Integer pos;
			/** 如果随播广告可用，Banner对象数组。 */
			private List<Banner> companionad;
			/** 该展示支持的API框架列表 */
			private List<Integer> api;
			/** 支持的VAST随播广告类型 */
			private List<Integer> companiontype;
			/** 扩展内容  */
			private Ext ext;
			public List<String> getMimes() {
				return mimes;
			}
			public void setMimes(List<String> mimes) {
				this.mimes = mimes;
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
			public List<Integer> getProtocols() {
				return protocols;
			}
			public void setProtocols(List<Integer> protocols) {
				this.protocols = protocols;
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
			public Integer getStartdelay() {
				return startdelay;
			}
			public void setStartdelay(Integer startdelay) {
				this.startdelay = startdelay;
			}
			public Integer getLinearity() {
				return linearity;
			}
			public void setLinearity(Integer linearity) {
				this.linearity = linearity;
			}
			public Integer getSkip() {
				return skip;
			}
			public void setSkip(Integer skip) {
				this.skip = skip;
			}
			public Integer getSkipmin() {
				return skipmin;
			}
			public void setSkipmin(Integer skipmin) {
				this.skipmin = skipmin;
			}
			public Integer getSkipafter() {
				return skipafter;
			}
			public void setSkipafter(Integer skipafter) {
				this.skipafter = skipafter;
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
			public Integer getMaxextended() {
				return maxextended;
			}
			public void setMaxextended(Integer maxextended) {
				this.maxextended = maxextended;
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
			public Integer getBoxingallowed() {
				return boxingallowed;
			}
			public void setBoxingallowed(Integer boxingallowed) {
				this.boxingallowed = boxingallowed;
			}
			public List<Integer> getPlaybackmethod() {
				return playbackmethod;
			}
			public void setPlaybackmethod(List<Integer> playbackmethod) {
				this.playbackmethod = playbackmethod;
			}
			public List<Integer> getDelivery() {
				return delivery;
			}
			public void setDelivery(List<Integer> delivery) {
				this.delivery = delivery;
			}
			public Integer getPos() {
				return pos;
			}
			public void setPos(Integer pos) {
				this.pos = pos;
			}
			public List<Banner> getCompanionad() {
				return companionad;
			}
			public void setCompanionad(List<Banner> companionad) {
				this.companionad = companionad;
			}
			public List<Integer> getApi() {
				return api;
			}
			public void setApi(List<Integer> api) {
				this.api = api;
			}
			public List<Integer> getCompaniontype() {
				return companiontype;
			}
			public void setCompaniontype(List<Integer> companiontype) {
				this.companiontype = companiontype;
			}
			public Ext getExt() {
				return ext;
			}
			public void setExt(Ext ext) {
				this.ext = ext;
			}
			
		}
		
		/** 音频广告，目前不支持 */
		public static class Audio{}
		
		/** 原生广告，目前不支持  */
		public static class Nativead
		{
			/**  */
			private String request;
			/**  */
			private String ver;
			/**  */
			private List<Integer> api;
			/**  */
			private List<Integer> battr;
			/**  */
			private Ext ext;
			public String getRequest() {
				return request;
			}
			public void setRequest(String request) {
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
		
		/** PMP私人交易市场，目前不支持pmp接入 */
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

		public Video getVideo() {
			return video;
		}

		public void setVideo(Video video) {
			this.video = video;
		}

		public Audio getAudio() {
			return audio;
		}

		public void setAudio(Audio audio) {
			this.audio = audio;
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

		public String getDisplaymanager() {
			return displaymanager;
		}

		public void setDisplaymanager(String displaymanager) {
			this.displaymanager = displaymanager;
		}

		public String getDisplaymanagerver() {
			return displaymanagerver;
		}

		public void setDisplaymanagerver(String displaymanagerver) {
			this.displaymanagerver = displaymanagerver;
		}

		public Integer getInstal() {
			return instal;
		}

		public void setInstal(Integer instal) {
			this.instal = instal;
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

		public Integer getClickbrowser() {
			return clickbrowser;
		}

		public void setClickbrowser(Integer clickbrowser) {
			this.clickbrowser = clickbrowser;
		}

		public Integer getSecure() {
			return secure;
		}

		public void setSecure(Integer secure) {
			this.secure = secure;
		}

		public List<String> getIframebuster() {
			return iframebuster;
		}

		public void setIframebuster(List<String> iframebuster) {
			this.iframebuster = iframebuster;
		}

		public Integer getExp() {
			return exp;
		}

		public void setExp(Integer exp) {
			this.exp = exp;
		}

		public Ext getExt() {
			return ext;
		}

		public void setExt(Ext ext) {
			this.ext = ext;
		}
	}
	
	/** 媒体站点对象 */
	public static class Site
	{
		/** 广告请求的"应用程序ID" */
		private String id;
		/** 网站名称 */
		private String name;
		/** 网站域名 */
		private String domain;
		/** 网站分类 */
		private List<String> cat;
		/** 网站分类描述 */
		private List<String> sectioncat;
		/** 页面分类描述 */
		private List<String> pagecat;
		/** 页面URL */
		private String page;
		/** 来源URL */
		private String ref;
		/** 搜索关键词 */
		private String search;
		/** 指示该站点是否已编程为在移动设备上查看时优化布局，其中0 =否，1 =是 */
		private Integer mobile;
		/**指定站点是否具有隐私策略。“1”表示有一项政策。“0”表示没有。  */
		private Integer privacypolicy;
		/** 有关网站发布商的详细信息 */
		private Publisher publisher;
		/** 有关网站内容的详细信息 */
		private Content content;
		/** 逗号分隔的网站关键字列表 */
		private String keywords;
		/** 扩展字段 */
		private Ext ext;
		
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

		public List<String> getCat() {
			return cat;
		}

		public void setCat(List<String> cat) {
			this.cat = cat;
		}

		public List<String> getSectioncat() {
			return sectioncat;
		}

		public void setSectioncat(List<String> sectioncat) {
			this.sectioncat = sectioncat;
		}

		public List<String> getPagecat() {
			return pagecat;
		}

		public void setPagecat(List<String> pagecat) {
			this.pagecat = pagecat;
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

		public String getSearch() {
			return search;
		}

		public void setSearch(String search) {
			this.search = search;
		}

		public Integer getMobile() {
			return mobile;
		}

		public void setMobile(Integer mobile) {
			this.mobile = mobile;
		}

		public Integer getPrivacypolicy() {
			return privacypolicy;
		}

		public void setPrivacypolicy(Integer privacypolicy) {
			this.privacypolicy = privacypolicy;
		}

		public Publisher getPublisher() {
			return publisher;
		}

		public void setPublisher(Publisher publisher) {
			this.publisher = publisher;
		}

		public Content getContent() {
			return content;
		}

		public void setContent(Content content) {
			this.content = content;
		}

		public String getKeywords() {
			return keywords;
		}

		public void setKeywords(String keywords) {
			this.keywords = keywords;
		}

		public Ext getExt() {
			return ext;
		}

		public void setExt(Ext ext) {
			this.ext = ext;
		}
	}
	
	/** app信息 */
	public static class App
	{
		/** 应用ID */
		private String id;
		/** 应用名称 */
		private String name;
		/** 应用包名  */
		private String bundle;
		/** 应用域名  */
		private String domain;
		/** 安装应用的商店URL */
		private String storeurl;
		/** 应用分类  */
		private List<String> cat;
		/** 应用页面分类  */
		private List<String> sectioncat;
		/** 应用程序的当前页面/视图的IAB内容类别数组 */
		private List<String> pagecat;
		/** 应用版本 */
		private String ver;
		/** 指示应用程序是否有隐私政策，其中0 =否，1 =是 */
		private Integer privacypolicy;
		/** 1”如果应用程序是付费版本; 否则“0”（即免费）。   */
		private Integer paid;
		/** 出版商信息 */
		private Publisher publisher;
		/** 内容详细信息 */
		private Content content;
		/** 应用程序关键词，多个逗号分隔 */
		private String keywords;
		/** 扩展内容  */
		private Ext ext;
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
		public String getStoreurl() {
			return storeurl;
		}
		public void setStoreurl(String storeurl) {
			this.storeurl = storeurl;
		}
		public List<String> getCat() {
			return cat;
		}
		public void setCat(List<String> cat) {
			this.cat = cat;
		}
		public List<String> getSectioncat() {
			return sectioncat;
		}
		public void setSectioncat(List<String> sectioncat) {
			this.sectioncat = sectioncat;
		}
		public List<String> getPagecat() {
			return pagecat;
		}
		public void setPagecat(List<String> pagecat) {
			this.pagecat = pagecat;
		}
		public String getVer() {
			return ver;
		}
		public void setVer(String ver) {
			this.ver = ver;
		}
		public Integer getPrivacypolicy() {
			return privacypolicy;
		}
		public void setPrivacypolicy(Integer privacypolicy) {
			this.privacypolicy = privacypolicy;
		}
		public Integer getPaid() {
			return paid;
		}
		public void setPaid(Integer paid) {
			this.paid = paid;
		}
		public Publisher getPublisher() {
			return publisher;
		}
		public void setPublisher(Publisher publisher) {
			this.publisher = publisher;
		}
		public Content getContent() {
			return content;
		}
		public void setContent(Content content) {
			this.content = content;
		}
		public String getKeywords() {
			return keywords;
		}
		public void setKeywords(String keywords) {
			this.keywords = keywords;
		}
		public Ext getExt() {
			return ext;
		}
		public void setExt(Ext ext) {
			this.ext = ext;
		}
		
	}
	
	/** 设备信息 */
	public static class Device
	{
		
	}
	
	/** 用户信息 */
	public static class User
	{
		
	}
	
	/** 规定了有关此请求的任何行业，法律或政府法规 */
	public static class Regs
	{
		
	}
	
	/** 有关网站发布商的详细信息 */
	public static class Publisher
	{
		/** 发行人ID */
		private String id;
		/** 发布商名称 */
		private String name;
		/** 发布商类别数组 */
		private List<String> cat;
		/** 发布商顶级域名 */
		private String domain;
		/** 扩展字段 */
		private Ext ext;
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
		public List<String> getCat() {
			return cat;
		}
		public void setCat(List<String> cat) {
			this.cat = cat;
		}
		public String getDomain() {
			return domain;
		}
		public void setDomain(String domain) {
			this.domain = domain;
		}
		public Ext getExt() {
			return ext;
		}
		public void setExt(Ext ext) {
			this.ext = ext;
		}
	}
	
	/** 有关网站内容的详细信息 */
	public static class Content
	{
		/** 内容唯一标识ID */
		private String id;
		/** 内容编号 */
		private Integer episode;
		/** 内容标题 */
		private String title;
		/** 内容系列 */
		private String series;
		/** 内容季，例如：第3季 */
		private String season;
		/** 内容原始URL */
		private String url;
		/** 内容分类 */
		private List<String> cat;
		/** 制品质量 */
		private Integer videoquality;
		/** 内容关键词，逗号分隔 */
		private String keywords;
		/** 内容评级 */
		private String contentrating;
		/** 内容评分（例如，星号，喜欢等等） */
		private String userrating;
		/** 指定内容的类型（游戏，视频，文字等） */
		private Integer content;
		/** 内容是否存在？例如，直播视频流，实况博客。“1”表示内容是活的。“0”表示不存在。 */
		private Integer livestream;
		/** 1为“直接”; 0为“间接”  */
		private Integer sourcerelationship;
		/** 产品 */
		private Producer producer;
		/** 内容长度 */
		private Integer len;
		/** 媒体评级的内容 */
		private Integer qagmediarating;
		/** 从QAG视频附录。如果内容可以嵌入（如嵌入式视频播放器），则该值应设置为“1”。如果内容无法嵌入，则应将其设置为“0”。 */
		private Integer embeddable;
		/** 内容的语言 使用alpha - 2 / ISO 639--1代码。 */
		private String language;
		/** 扩展字段 */
		private Ext ext;
		
		public static class Producer
		{
			/** 内容制作人或发件人ID。如果内容合并，并且可能会使用嵌入标记发布在网站上，这是有用的。 */
			private String id;
			/** 内容制作人或创始人名称（例如“华纳兄弟”）。  */
			private String name;
			/** 内容制作者的IAB内容类别数组 */
			private List<String> cat;
			/** 内容制作者的URL。 */
			private String domain;
			/** 扩展内容 */
			private Ext ext;
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
			public List<String> getCat() {
				return cat;
			}
			public void setCat(List<String> cat) {
				this.cat = cat;
			}
			public String getDomain() {
				return domain;
			}
			public void setDomain(String domain) {
				this.domain = domain;
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

		public Integer getEpisode() {
			return episode;
		}

		public void setEpisode(Integer episode) {
			this.episode = episode;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getSeries() {
			return series;
		}

		public void setSeries(String series) {
			this.series = series;
		}

		public String getSeason() {
			return season;
		}

		public void setSeason(String season) {
			this.season = season;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public List<String> getCat() {
			return cat;
		}

		public void setCat(List<String> cat) {
			this.cat = cat;
		}

		public Integer getVideoquality() {
			return videoquality;
		}

		public void setVideoquality(Integer videoquality) {
			this.videoquality = videoquality;
		}

		public String getKeywords() {
			return keywords;
		}

		public void setKeywords(String keywords) {
			this.keywords = keywords;
		}

		public String getContentrating() {
			return contentrating;
		}

		public void setContentrating(String contentrating) {
			this.contentrating = contentrating;
		}

		public String getUserrating() {
			return userrating;
		}

		public void setUserrating(String userrating) {
			this.userrating = userrating;
		}

		public Integer getContent() {
			return content;
		}

		public void setContent(Integer content) {
			this.content = content;
		}

		public Integer getLivestream() {
			return livestream;
		}

		public void setLivestream(Integer livestream) {
			this.livestream = livestream;
		}

		public Integer getSourcerelationship() {
			return sourcerelationship;
		}

		public void setSourcerelationship(Integer sourcerelationship) {
			this.sourcerelationship = sourcerelationship;
		}

		public Producer getProducer() {
			return producer;
		}

		public void setProducer(Producer producer) {
			this.producer = producer;
		}

		public Integer getLen() {
			return len;
		}

		public void setLen(Integer len) {
			this.len = len;
		}

		public Integer getQagmediarating() {
			return qagmediarating;
		}

		public void setQagmediarating(Integer qagmediarating) {
			this.qagmediarating = qagmediarating;
		}

		public Integer getEmbeddable() {
			return embeddable;
		}

		public void setEmbeddable(Integer embeddable) {
			this.embeddable = embeddable;
		}

		public String getLanguage() {
			return language;
		}

		public void setLanguage(String language) {
			this.language = language;
		}

		public Ext getExt() {
			return ext;
		}

		public void setExt(Ext ext) {
			this.ext = ext;
		}
		
		
	}
	
	/** 扩展字段 */
	public static class Ext{}

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

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
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

	public Integer getTest() {
		return test;
	}

	public void setTest(Integer test) {
		this.test = test;
	}

	public Integer getAt() {
		return at;
	}

	public void setAt(Integer at) {
		this.at = at;
	}

	public Integer getTmax() {
		return tmax;
	}

	public void setTmax(Integer tmax) {
		this.tmax = tmax;
	}

	public List<String> getWseat() {
		return wseat;
	}

	public void setWseat(List<String> wseat) {
		this.wseat = wseat;
	}

	public Integer getAllimps() {
		return allimps;
	}

	public void setAllimps(Integer allimps) {
		this.allimps = allimps;
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

	public List<String> getBapp() {
		return bapp;
	}

	public void setBapp(List<String> bapp) {
		this.bapp = bapp;
	}

	public Regs getRegs() {
		return regs;
	}

	public void setRegs(Regs regs) {
		this.regs = regs;
	}

	public Ext getExt() {
		return ext;
	}

	public void setExt(Ext ext) {
		this.ext = ext;
	}
}
