package com.jtd.engine.message.v1;

import com.jtd.engine.adserver.message.v1.MessageV1;

import java.util.List;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2017年6月27日
 * @项目名称 dsp-engine
 * @描述 <p>互众响应类</p>
 */
public class HzBidResponse extends MessageV1{

	
	private static final long serialVersionUID = 8476874741858450157L;
	//请求ID,request.id
	private String id;
	//由dsp生成的竞价id，这里直接将请求的id返回
	private String bidid;
	//出价数据对象
	private List<Seatbid> seatbid;

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
	 * @return the bidid
	 */
	public String getBidid() {
		return bidid;
	}

	/**
	 * @param bidid
	 *            the bidid to set
	 */
	public void setBidid(String bidid) {
		this.bidid = bidid;
	}

	/**
	 * @return the seatbid
	 */
	public List<Seatbid> getSeatbid() {
		return seatbid;
	}

	/**
	 * @param seatbid
	 *            the seatbid to set
	 */
	public void setSeatbid(List<Seatbid> seatbid) {
		this.seatbid = seatbid;
	}
	
	/**
	 * @作者 Amos Xu
	 * @版本 V1.0
	 * @配置 
	 * @创建日期 2017年6月27日
	 * @项目名称 dsp-engine
	 * @描述 <p></p>
	 */
	public static class Seatbid {
		//针对单次曝光的出价
		private List<Bid> bid;
		
		/*
		 暂时不用
		 seat	string	有灵集平台提供给DSP的本次出价的seat ID
		 * */
		
		/**
		 * @return the bid
		 */
		public List<Bid> getBid() {
			return bid;
		}

		/**
		 * @param bid
		 *            the bid to set
		 */
		public void setBid(List<Bid> bid) {
			this.bid = bid;
		}
		
		/**
		 * Response.seatbid.bid
		 * @作者 Amos Xu
		 * @版本 V1.0
		 * @配置 
		 * @创建日期 2017年3月20日
		 * @项目名称 dsp-engine
		 * @描述 <p></p>
		 */
		public static class Bid {
			//DSP对该次出价分配的ID
			//和Response.bidid,一个值
			private String id;
			//Bid Request中对应的曝光ID
			//Request.imp.id
			private String impid;
			//DSP出价，单位是分/千次曝光，即CPM
			private float price;
			//win notice url,处理和曝光监测一样，nurl是否支持发送
			private String nurl;
			
			//dsp的创意id
			private String creative_id;
			/*
			 * 富媒体动态创意时生效。字段内容将会被替换到创意中CreativeSsi字段中的宏变量${HTML_SNIPPET},
			 * 此外该字段内需监控点击链接，需要DSP将点击链接使用如下格式进行封装。1-CLICK标签：{CLICK}http://www.a.com{/CLICK}
			 * 														  2-点击前缀宏变量：${CLICK_PREFIX},替换值：平台点击监控前缀URL，值使用*urlencode*编码，
			 * 例如：例:http%3a%2f%2fc.fastapi.net%2fj%3f e%3d1yu6M4eZjWRs2%26v%3d3%26pvid%3d5000016.1.56%26url%3 durldecode(${CLICK_PREFIX}) + urlencode(‘DSP的点击链接') 可以生成平台监控到的点击链接。
			*/
			private String html_snippet;
			
			/*
			 图片动态创意时生效。 
			 {
			 "url" : "http://dsp.com/ad.jpg", //图片url "c_url" : "http://dsp.com/click.php", //点击地址 "width" : 200, //图片宽
			 "height" : 200, //图片高
			 "action" : 0, //交互类型, 0=下载, 1=网页浏览
			 "imp" : [ "曝光监控1", "曝光监控2" ], //曝光监控,可选 "clk" : [ "点击监控1", "点击监控2" ] //点击监控,可选
             }
			 * */
			private String image_snippet;
			
			
			/*
			 flash动态创意时生效。 
			 {
				"url" : "http://dsp.com/ad.swf", //flash地址
				"c_url" : "http://dsp.com/click.php", //点击地址 "width" : 200, //flash宽
				"height" : 200, //flash高
				"duration" : 10, //播放时长,单位秒,可选,
				"imp" : [ "曝光监控1", "曝光监控2" ], //曝光监控,可选 "clk" : [ "点击监控1", "点击监控2" ] //点击监控,可选
			 }
			 * */
			private String flash_snippet;
			
			
			/*
			 视频动态创意时生效。 
			 {
				"url" : "http://dsp.com/ad.flv", //视频地址
				"c_url" : "http://dsp.com/click.php", //点击地址 "width" : 200, //视频宽
				"height" : 200, //视频高
				"duration" : 10, //播放时长,单位秒,必选
				"imp" : [ "曝光监控1", "曝光监控2" ], //曝光监控,可选 "clk" : [ "点击监控1", "点击监控2" ] //点击监控,可选
			}
			 * */
			private String video_snippet;
			
			/*
			 图文(信息流)动态创意时生效。 
			 {
				"title" : "标题",
				"desc" : "描述",
				"c_url" : "http://dsp.com/click.php", //点击地址
				"url" : "http://dsp.com/ad.jpg", //图片url
				"ext_urls" : ["http://dsp.com/ad.jpg1", "http://dsp.com/ad.jpg2"]
				//可选,如果超过1张图片,第2~n张放这里 "width" : 200, //图片高度
				"height" : 200, //图片宽度
				"action" : 0, //交互类型, 0=下载, 1=网页浏览
				"imp" : [ "曝光监控1", "曝光监控2" ], //曝光监控,可选 "clk" : [ "点击监控1", "点击监控2" ] //点击监控,可选
			}
			 * */
			private String text_icon_snippet;
			//创意宽,该字段仅当投放动态创意时生效。该字段若不填写,则会默认取上 传Creative时,填写的创意宽
			private int width;
			//创意高,该字段仅当投放动态创意时生效。该字段若不填写,则会默认取上 传Creative时,填写的创意高
			private int height;
			//DSP自定义字段(将会在曝光和点击监测地址后拼接)
			private String ext;
			/*
			 {
				"url" : "deeplink URL",
				"package_name" : "app包名"
			 }
			 * */
			private String deeplink;
			
			//点击行为，0为打开网页，1 下载
			private int click_action;

			public String getId() {
				return id;
			}

			public void setId(String id) {
				this.id = id;
			}

			public String getImpid() {
				return impid;
			}

			public void setImpid(String impid) {
				this.impid = impid;
			}

			public float getPrice() {
				return price;
			}

			public void setPrice(float price) {
				this.price = price;
			}

			public String getNurl() {
				return nurl;
			}

			public void setNurl(String nurl) {
				this.nurl = nurl;
			}

			public String getCreative_id() {
				return creative_id;
			}

			public void setCreative_id(String creative_id) {
				this.creative_id = creative_id;
			}

			public String getHtml_snippet() {
				return html_snippet;
			}

			public void setHtml_snippet(String html_snippet) {
				this.html_snippet = html_snippet;
			}

			public String getImage_snippet() {
				return image_snippet;
			}

			public void setImage_snippet(String image_snippet) {
				this.image_snippet = image_snippet;
			}

			public String getFlash_snippet() {
				return flash_snippet;
			}

			public void setFlash_snippet(String flash_snippet) {
				this.flash_snippet = flash_snippet;
			}

			public String getVideo_snippet() {
				return video_snippet;
			}

			public void setVideo_snippet(String video_snippet) {
				this.video_snippet = video_snippet;
			}

			public String getText_icon_snippet() {
				return text_icon_snippet;
			}

			public void setText_icon_snippet(String text_icon_snippet) {
				this.text_icon_snippet = text_icon_snippet;
			}

			public int getWidth() {
				return width;
			}

			public void setWidth(int width) {
				this.width = width;
			}

			public int getHeight() {
				return height;
			}

			public void setHeight(int height) {
				this.height = height;
			}

			public String getExt() {
				return ext;
			}

			public void setExt(String ext) {
				this.ext = ext;
			}

			public String getDeeplink() {
				return deeplink;
			}

			public void setDeeplink(String deeplink) {
				this.deeplink = deeplink;
			}

			public int getClick_action() {
				return click_action;
			}

			public void setClick_action(int click_action) {
				this.click_action = click_action;
			}
			
			
			
		}
	}
	
	
	public Type type() {
		return Type.HzBidResponse;
	}
}
