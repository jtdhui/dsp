package com.jtd.engine.message.v1;

import java.util.HashMap;
import java.util.List;

import com.jtd.engine.adserver.message.v1.MessageV1;

/**
 * 新汇达通广告求情响应response
 * @author zl
 * 2017-09-26
 */
public class XHDTBidResponse  extends MessageV1{

	private static final long serialVersionUID = 1L;
	
	@Override
	public Type type() {
		return Type.XHDTBidResponse;
	}
	
	/** 求情ID */
	private String requestId;
	/** 广告列表 */
	private List<XhdtAd> ads; 
	
	public static class XhdtAd
	{
		private Object pubContent;
		private String adtype;
		private Integer width;
		private Integer height;
		
		private String landingPage;
		private String beaconUrl;
		private EventTracking evenTracking;
		
		public static class PubContent
		{
			private String title;
			private String description;
			private Icon icon;
			private Screenshots screenshots;
			private String landingURL;
			private String cta;
			private String rating;
			
			public static class Icon
			{
				private Integer aspectRatio;
				private Integer height;
				private Integer width;
				
				public Integer getAspectRatio() {
					return aspectRatio;
				}
				public void setAspectRatio(Integer aspectRatio) {
					this.aspectRatio = aspectRatio;
				}
				public Integer getHeight() {
					return height;
				}
				public void setHeight(Integer height) {
					this.height = height;
				}
				public Integer getWidth() {
					return width;
				}
				public void setWidth(Integer width) {
					this.width = width;
				}
			}
			
			public static class Screenshots
			{
				private String url;
				private Float aspectRatio;
				private Integer height;
				private Integer width;
				
				public String getUrl() {
					return url;
				}
				public void setUrl(String url) {
					this.url = url;
				}
				public Float getAspectRatio() {
					return aspectRatio;
				}
				public void setAspectRatio(Float aspectRatio) {
					this.aspectRatio = aspectRatio;
				}
				public Integer getHeight() {
					return height;
				}
				public void setHeight(Integer height) {
					this.height = height;
				}
				public Integer getWidth() {
					return width;
				}
				public void setWidth(Integer width) {
					this.width = width;
				}
			}

			public String getTitle() {
				return title;
			}

			public void setTitle(String title) {
				this.title = title;
			}

			public String getDescription() {
				return description;
			}

			public void setDescription(String description) {
				this.description = description;
			}

			public Icon getIcon() {
				return icon;
			}

			public void setIcon(Icon icon) {
				this.icon = icon;
			}

			public Screenshots getScreenshots() {
				return screenshots;
			}

			public void setScreenshots(Screenshots screenshots) {
				this.screenshots = screenshots;
			}

			public String getLandingURL() {
				return landingURL;
			}

			public void setLandingURL(String landingURL) {
				this.landingURL = landingURL;
			}

			public String getCta() {
				return cta;
			}

			public void setCta(String cta) {
				this.cta = cta;
			}

			public String getRating() {
				return rating;
			}

			public void setRating(String rating) {
				this.rating = rating;
			}
		}
		
		public static class EventTracking
		{
			private HashMap<String,String> urls;
			private Boolean isApp;
			public HashMap<String, String> getUrls() {
				return urls;
			}
			public void setUrls(HashMap<String, String> urls) {
				this.urls = urls;
			}
			public Boolean getIsApp() {
				return isApp;
			}
			public void setIsApp(Boolean isApp) {
				this.isApp = isApp;
			}
		}
		
		public Object getPubContent() {
			return pubContent;
		}
		public void setPubContent(Object pubContent) {
			this.pubContent = pubContent;
		}
		public String getAdtype() {
			return adtype;
		}
		public void setAdtype(String adtype) {
			this.adtype = adtype;
		}
		public Integer getWidth() {
			return width;
		}
		public void setWidth(Integer width) {
			this.width = width;
		}
		public Integer getHeight() {
			return height;
		}
		public void setHeight(Integer height) {
			this.height = height;
		}
		public String getLandingPage() {
			return landingPage;
		}
		public void setLandingPage(String landingPage) {
			this.landingPage = landingPage;
		}
		public String getBeaconUrl() {
			return beaconUrl;
		}
		public void setBeaconUrl(String beaconUrl) {
			this.beaconUrl = beaconUrl;
		}
		public EventTracking getEvenTracking() {
			return evenTracking;
		}
		public void setEvenTracking(EventTracking evenTracking) {
			this.evenTracking = evenTracking;
		}
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public List<XhdtAd> getAds() {
		return ads;
	}

	public void setAds(List<XhdtAd> ads) {
		this.ads = ads;
	}
}
