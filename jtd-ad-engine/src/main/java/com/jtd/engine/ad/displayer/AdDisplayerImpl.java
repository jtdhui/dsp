package com.jtd.engine.ad.displayer;

import Tanx.TanxBidding.BidRequest;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.jtd.engine.ad.Session;
import com.jtd.engine.ad.em.Adx;
import com.jtd.engine.dao.CampaignDAO;
import com.jtd.engine.message.BaiduRealtimeBiddingV26;
import com.jtd.engine.message.VamRealtimeBidding.VamRequest;
import com.jtd.engine.utils.LikeBase64;
import com.jtd.engine.utils.SystemTime;
import com.jtd.web.constants.CreativeType;
import com.jtd.web.constants.TransType;
import com.jtd.web.model.Ad;
import com.jtd.web.model.Campaign;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年10月25日
 * @项目名称 dsp-engine
 * @描述 <p></p>
 */
public class AdDisplayerImpl implements AdDisplayer {

	private static final ThreadLocal<StringBuilder> SB = new ThreadLocal<StringBuilder>(){
	    protected StringBuilder initialValue() {
	        return new StringBuilder();
	    }
	};

	// key campid，value counter
	private static final ConcurrentHashMap<Long, AtomicInteger> AFC_MAP = new ConcurrentHashMap<Long, AtomicInteger>();

	private String counturl;
	
	private String counturlhttps;

	// tanx做CM的URL
	private String tanxCookieMappingImg;
	
	// xtrader做CM的URL
	private String xtraderCookieMappingUrl;
	private String xtraderCookieMappingImg;

	private SystemTime systemTime;

	private CampaignDAO campaignDAO;

	// 模板
	private String pcImg;
	private String pcFlash;
	private String tanxMobileImg;
	private String tanxMobileAppImg;
	private String xtraderIframeTpl;
	private String xtraderIframeScriptTpl;
	private String pcImgHz;
//	private String mraidImg;
//	private String mraidAndroidAppDownLoad;
//	private String mraidIosAppDownLoad;
	private String adviewImg;

	public void init() throws Throwable {
		File dir = new File("resources/tpl");
		pcImg = FileUtils.readFileToString(new File(dir,"pcImg.tpl"), "utf-8");
		pcFlash = FileUtils.readFileToString(new File(dir,"pcFlash.tpl"), "utf-8");
		tanxMobileImg = FileUtils.readFileToString(new File(dir,"tanxMobileImg.tpl"), "utf-8");
		tanxMobileAppImg = FileUtils.readFileToString(new File(dir,"tanxMobileAppImg.tpl"), "utf-8");
		xtraderIframeTpl = FileUtils.readFileToString(new File(dir,"xtraderiframe.tpl"), "utf-8");
		xtraderIframeScriptTpl = FileUtils.readFileToString(new File(dir,"xtraderiframescript.tpl"), "utf-8");
		pcImgHz = FileUtils.readFileToString(new File(dir,"pcImgHz.tpl"), "utf-8");
//		mraidImg = FileUtils.readFileToString(new File(dir,"mraidImg.tpl"), "utf-8");
//		mraidAndroidAppDownLoad = FileUtils.readFileToString(new File(dir,"mraidAndroidAppDownLoad.tpl"), "utf-8");
//		mraidIosAppDownLoad = FileUtils.readFileToString(new File(dir,"mraidIosAppDownLoad.tpl"), "utf-8");
		
		
		adviewImg = FileUtils.readFileToString(new File(dir,"adviewImg.tpl"), "utf-8");
	}
	

	@Override
	public String[] urls4Adwo(Session session, Campaign camp, Ad ad) 
	{
		Param param = buildParams(session, camp, ad);
		
		StringBuilder sb = SB.get();
		sb.setLength(0);
		sb.append(counturl).append("/np?q=").append(LikeBase64.encode(JSON.toJSONString(param))).append("&id=${AUCTION_ID}");
		sb.append("&impid=${AUCTION_IMP_ID}");
		sb.append("&p=${AUCTION_PRICE}");
		String pv = sb.toString();
//		id	${AUCTION_ID}	Bid Request ID
//		bidid	${AUCTION_BID_ID}	Bid Response ID per request
//		impid	${AUCTION_IMP_ID}	Impression ID
//		price	${AUCTION_PRICE}	Settlement price，曝光的最终拍卖价。这个字段会使用AES加密，加密的Key是DSP的token。为了和其他Exchange 统一，加密之前我们会在价格之后加上时间戳后缀，即形式如”PRICE_TIME”这样（例如：’201_1376468920380’），再进行加密。解密时请注意需要split出来分别获得价格和时间戳。这里的时间戳是精确到毫秒的。另外，对于动态物料，也可以在"bid":"adm"字段中通过宏%%WINNING_PRICE%%来获取价格。

		sb.setLength(0);
		sb.append(counturl).append("/nc?q=").append(LikeBase64.encode(JSON.toJSONString(param))).append("&r=").append(LikeBase64.encode(ad.getClickUrl()));
		String ctu = sb.toString();

		String creativeUrl=ad.getCreativeUrl();
		
		//判断是否需要做CM操作
		if(session.isCookieMapping()){
			return new String[]{pv,ctu,creativeUrl,this.xtraderCookieMappingUrl};
		}else{
			return new String[]{pv,ctu,creativeUrl};
		}
	}

	@Override
	public String[] htmlAdView(Session session, Campaign camp, Ad ad) 
	{
		/** <meta http-equiv='Content-Type' content='text/html; charset=UTF-8' /><styletype='text/css'>*{padding:0px;margin:0px;} 
		 * a:link{text-decoration:none;}<\/style><a href='%%CLICK_URL%%'onclick='%%CLICK_SCRIPT%%'>
		 * <img width='100%' height='100%' src='http://www.adview.com/image/rtb/6f9f860f77aa2b509b319fee7cb96626_320x50.png'>
		 * <\/img><\/a>%%IMPTRACK_SCRIPT%% */
		
		Param param = buildParams(session, camp, ad);
		
		StringBuilder sb = SB.get();
		sb.setLength(0);
		sb.append(counturl).append("/np?q=").append(LikeBase64.encode(JSON.toJSONString(param))).append("&p=%%SETTLE_PRICE%%");
		String pv = sb.toString();
		
		sb.setLength(0);
		sb.append(counturl).append("/nc?q=").append(LikeBase64.encode(JSON.toJSONString(param))).append("&r=").append(LikeBase64.encode(ad.getClickUrl()));
		String ctu = sb.toString();
		
		String size = ad.getSize();
		String[] wh = size.split("x");
		long campid = camp.getId();
		CreativeType crt = ad.getCreativeType();//物料类型
		if(crt == CreativeType.IMG) //IMG(0, "图片"), FLASH(1, "Flash"), FLV(2, "Flv"), MP4(3, "MP4");
		{
			String html = adviewImg;
			html = html.replaceAll("#WIDTH#", wh[0]);
			html = html.replaceAll("#HEIGHT#", wh[1]);
			html = html.replaceAll("#ADURL#", ad.getCreativeUrl());
			html = html.replaceAll("#PV#", pv);
			int af = campaignDAO.getAf(campid);
			if(af > 0) {
				AtomicInteger afc = AFC_MAP.get(campid);
				if (afc == null) {
					afc = new AtomicInteger(0);
					AtomicInteger old = AFC_MAP.putIfAbsent(campid, afc);
					if (old != null) {
						afc = old;
					}
				}

				if (afc.getAndIncrement() >= af) {
					afc.set(0);
					html = html.replaceAll("#IFM#", "<div style=\"border:0px;position:absolute;top:0px;left:0;width:" + wh[0] + "px;height:" + wh[1] + "px;z-index:1;\"><iframe frameborder=\"no\" src=\"" + ctu + "\" width=\"" + wh[0] + "\" height=\"" + wh[1] + "\"></iframe></div>");
				}
			}
			html = html.replaceAll("#IFM#", "");
			//获取pv监测地址
			String pvurls = ad.getPvUrls();
			//如果不为null，则将pv监测地址拼装到广告展示的html字符串后面
			if (!StringUtils.isEmpty(pvurls)) {
				String[] ps = JSON.parseObject(pvurls, String[].class);
				for (String p : ps) {
					if(!StringUtils.isEmpty(p)) {
						html += "<iframe frameborder=\"no\" width=\"0\" height=\"0\"src=\"" + p + "\"></iframe>";
					}
				}
			}

			return new String[]{html, ctu};
		}
		return null;
	}


	@Override
	public String[] html4Tanx(Session session, Campaign camp, Ad ad) {

		Param param = buildParams(session, camp, ad);

		StringBuilder sb = SB.get();
		sb.setLength(0);
		sb.append(counturl).append("/np?q=").append(LikeBase64.encode(JSON.toJSONString(param))).append("&p=%%SETTLE_PRICE%%");
		String pv = sb.toString();

		sb.setLength(0);
		sb.append(counturl).append("/nc?q=").append(LikeBase64.encode(JSON.toJSONString(param))).append("&r=").append(LikeBase64.encode(ad.getClickUrl()));
		String ctu = sb.toString();

		String size = ad.getSize();
		String[] wh = size.split("x");
		long campid = camp.getId();
		CreativeType crt = ad.getCreativeType();
		if(crt == CreativeType.IMG) {
//			<div style="border:0px;position:relative;width:#WIDTH#px;height:#HEIGHT#px;z-index:2147483647;">
//			<a href="http://www.doddata.net" target="_blank" style="cursor:pointer"><div onmouseover="this.style.display='none';this.parentNode.lastChild.style.display='block'" style="position:absolute;right:0;bottom:0;width:24px;height:17px;z-index:2147483646;background-image:url('http://pic.wonnder.com/bia/corner1.png');"></div><div onmouseout="setTimeout((function(me){return function(){me.style.display='none';me.parentNode.firstChild.style.display='block';};})(this), 500);" style="display:none;position:absolute;right:0;bottom:0;width:81px;height:17px;z-index:2147483646;background-image:url('http://pic.wonnder.com/bia/corner2.png');"></div></a>
//			<div style="border:0px;position:absolute;top:0px;left:0;width:#WIDTH#px;height:#HEIGHT#px;z-index:2147483645;">
//			<a href="#CLICK#" target="_blank"><img border="0" width="#WIDTH#" height="#HEIGHT#" src="#ADURL#" /></a>
//			</div>#IFM#
//			</div>
//			<img width="0" height="0" src="#PV#" />
			String html = pcImg;
			html = html.replaceAll("#CLICK#", "%%CLICK_URL%%");
			html = html.replaceAll("#WIDTH#", wh[0]);
			html = html.replaceAll("#HEIGHT#", wh[1]);
			html = html.replaceAll("#ADURL#", ad.getCreativeUrl());
			html = html.replaceAll("#PV#", pv);
			int af = campaignDAO.getAf(campid);
			if(af > 0) {
				AtomicInteger afc = AFC_MAP.get(campid);
				if (afc == null) {
					afc = new AtomicInteger(0);
					AtomicInteger old = AFC_MAP.putIfAbsent(campid, afc);
					if (old != null) {
						afc = old;
					}
				}

				if (afc.getAndIncrement() >= af) {
					afc.set(0);
					html = html.replaceAll("#IFM#", "<div style=\"border:0px;position:absolute;top:0px;left:0;width:" + wh[0] + "px;height:" + wh[1] + "px;z-index:1;\"><iframe frameborder=\"no\" src=\"" + ctu + "\" width=\"" + wh[0] + "\" height=\"" + wh[1] + "\"></iframe></div>");
				}
			}
			html = html.replaceAll("#IFM#", "");
			//获取pv监测地址
			String pvurls = ad.getPvUrls();
			//如果不为null，则将pv监测地址拼装到广告展示的html字符串后面
			if (!StringUtils.isEmpty(pvurls)) {
				String[] ps = JSON.parseObject(pvurls, String[].class);
				for (String p : ps) {
					if(!StringUtils.isEmpty(p)) {
						html += "<iframe frameborder=\"no\" width=\"0\" height=\"0\"src=\"" + p + "\"></iframe>";
					}
				}
			}
			//做tanx cookieMapping
			if (StringUtils.isEmpty(session.getCid())) {
				html += tanxCookieMappingImg;
			}

			return new String[]{html, ctu};
		} else if(crt == CreativeType.FLASH) {
//			<div style="border:0px;position:relative;width:#WIDTH#px;height:#HEIGHT#px;z-index:2147483647;">
//			<a href="#CLICK#" target="_blank" style="cursor:pointer">
//			<div style="position:relative;filter:alpha(opacity=0);opacity:0;-moz-opacity:0;left:0;top:0;background:#FFFFFF;width:#WIDTH#px;height:#HEIGHT#px;z-index:2147483645"></div>
//			</a>
//			<a href="http://www.doddata.net" target="_blank" style="cursor:pointer"><div onmouseover="this.style.display='none';this.parentNode.lastChild.style.display='block'" style="position:absolute;right:0;bottom:0;width:24px;height:17px;z-index:2147483646;background-image:url('http://pic.wonnder.com/bia/corner1.png');"></div><div onmouseout="setTimeout((function(me){return function(){me.style.display='none';me.parentNode.firstChild.style.display='block';};})(this), 500);" style="display:none;position:absolute;right:0;bottom:0;width:81px;height:17px;z-index:2147483646;background-image:url('http://pic.wonnder.com/bia/corner2.png');"></div></a>
//			<div style="border:0px;position:absolute;top:0px;left:0;width:#WIDTH#px;height:#HEIGHT#px;z-index:2147483644;">
//			<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"
//					codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=7"
//					width="#WIDTH#" height="#HEIGHT#">
//			<param name="movie" value="#ADURL#">
//			<param name="wmode" value="transparent">
//			<param name="quality" value="high">
//			<embed src="#ADURL#" width="#WIDTH#" height="#HEIGHT#" quality="high" type="application/x-shockwave-flash" wmode="transparent" pluginspage="http://www.macromedia.com/go/getflashplayer">
//			</embed>
//			</object>
//			</div>#IFM#
//			</div>
//			<img width="0" height="0" src="#PV#" />
			String html = pcFlash;
			html = html.replaceAll("#CLICK#", "%%CLICK_URL%%");
			html = html.replaceAll("#WIDTH#", wh[0]);
			html = html.replaceAll("#HEIGHT#", wh[1]);
			html = html.replaceAll("#ADURL#", ad.getCreativeUrl());
			html = html.replaceAll("#PV#", pv);
			int af = campaignDAO.getAf(campid);
			if(af > 0) {
				AtomicInteger afc = AFC_MAP.get(campid);
				if (afc == null) {
					afc = new AtomicInteger(0);
					AtomicInteger old = AFC_MAP.putIfAbsent(campid, afc);
					if (old != null) {
						afc = old;
					}
				}

				if (afc.getAndIncrement() >= af) {
					afc.set(0);
					html = html.replaceAll("#IFM#", "<div style=\"border:0px;position:absolute;top:0px;left:0;width:" + wh[0] + "px;height:" + wh[1] + "px;z-index:1;\"><iframe frameborder=\"no\" src=\"" + ctu + "\" width=\"" + wh[0] + "\" height=\"" + wh[1] + "\"></iframe></div>");
				}
			}
			html = html.replaceAll("#IFM#", "");

			String pvurls = ad.getPvUrls();
			if (!StringUtils.isEmpty(pvurls)) {
				String[] ps = JSON.parseObject(pvurls, String[].class);
				for (String p : ps) {
					if(!StringUtils.isEmpty(p)) {
						html += "<iframe frameborder=\"no\" width=\"0\" height=\"0\"src=\"" + p + "\"></iframe>";
					}
				}
			}

			if (StringUtils.isEmpty(session.getCid())) {
				html += tanxCookieMappingImg;
			}

			return new String[]{html, ctu};
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.asme.ad.displayer.AdDisplayer#mobileHtml4Tanx(com.asme.ad.Session, net.doddata.web.dsp.model.Campaign, net.doddata.web.dsp.model.Ad)
	 */
	@Override
	public String[] mobileHtml4Tanx(Session session, Campaign camp, Ad ad) {

		Param param = buildParams(session, camp, ad);

		StringBuilder sb = SB.get();
		sb.setLength(0);
		sb.append(counturl).append("/np?q=").append(LikeBase64.encode(JSON.toJSONString(param))).append("&p=%%SETTLE_PRICE%%");
//		String pvurls = ad.getPvUrls();
//		if (!StringUtils.isEmpty(pvurls)) {
//			String[] ps = JSON.parseObject(pvurls, String[].class);
//			if(ps.length > 0) {
//				sb.append("&r=").append(LikeBase64.encode(ps[0]));
//			}
//		}
		String pv = sb.toString();

		sb.setLength(0);
		sb.append(counturl).append("/nc?q=").append(LikeBase64.encode(JSON.toJSONString(param))).append("&r=").append(LikeBase64.encode(ad.getClickUrl()));
		String ctu = sb.toString();

		String size = ad.getSize();
		String[] wh = size.split("x");
//		long campid = camp.getId();
		CreativeType crt = ad.getCreativeType();
		if(crt == CreativeType.IMG) {
//			<div style="border:0px;position:relative;width:#WIDTH#px;height:#HEIGHT#px;z-index:2147483647;">
//			<div style="border:0px;position:absolute;top:0px;left:0;width:#WIDTH#px;height:#HEIGHT#px;z-index:2147483645;">
//			<a href="#CLICK#" target="_blank"><img border="0" width="#WIDTH#" height="#HEIGHT#" src="#ADURL#" /></a>
//			</div>
//			</div>
			String html = tanxMobileImg;
			html = html.replaceAll("#CLICK#", "%%CLICK_URL%%");
			html = html.replaceAll("#WIDTH#", wh[0]);
			html = html.replaceAll("#HEIGHT#", wh[1]);
			html = html.replaceAll("#ADURL#", ad.getCreativeUrl());
			html = html.replaceAll("#PV#", pv);
//			int af = campaignDAO.getAf(campid);
//			if(af > 0) {
//				AtomicInteger afc = AFC_MAP.get(campid);
//				if (afc == null) {
//					afc = new AtomicInteger(0);
//					AtomicInteger old = AFC_MAP.putIfAbsent(campid, afc);
//					if (old != null) {
//						afc = old;
//					}
//				}
//
//				if (afc.getAndIncrement() >= af) {
//					afc.set(0);
//					html = html.replaceAll("#IFM#", "<div style=\"border:0px;position:absolute;top:0px;left:0;width:" + wh[0] + "px;height:" + wh[1] + "px;z-index:1;\"><iframe frameborder=\"no\" src=\"" + ctu + "\" width=\"" + wh[0] + "\" height=\"" + wh[1] + "\"></iframe></div>");
//				}
//			}
//			html = html.replaceAll("#IFM#", "");

			String pvurls = ad.getPvUrls();
			if (!StringUtils.isEmpty(pvurls)) {
				String[] ps = JSON.parseObject(pvurls, String[].class);
				for (String p : ps) {
					if(!StringUtils.isEmpty(p)) {
						html += "<iframe frameborder=\"no\" width=\"0\" height=\"0\"src=\"" + p + "\"></iframe>";
					}
				}
			}

			return new String[]{html, ctu, pv};
		}

		return null;
	}

	/* (non-Javadoc)
	 * @see com.asme.ad.displayer.AdDisplayer#mobileAppHtml4Tanx(com.asme.ad.Session, net.doddata.web.dsp.model.Campaign, net.doddata.web.dsp.model.Ad)
	 */
	@Override
	public String[] mobileAppHtml4Tanx(Session session, Campaign camp, Ad ad) {

		Param param = buildParams(session, camp, ad);

		StringBuilder sb = SB.get();
		sb.setLength(0);
		sb.append(counturl).append("/np?q=").append(LikeBase64.encode(JSON.toJSONString(param))).append("&p=%%SETTLE_PRICE%%");
		String pvurls = ad.getPvUrls();
		if (!StringUtils.isEmpty(pvurls)) {
			String[] ps = JSON.parseObject(pvurls, String[].class);
			if(ps.length > 0) {
				sb.append("&r=").append(LikeBase64.encode(ps[0]));
			}
		}
		String pv = sb.toString();

		sb.setLength(0);
		sb.append(counturl).append("/nc?q=").append(LikeBase64.encode(JSON.toJSONString(param))).append("&r=").append(LikeBase64.encode(ad.getClickUrl()));
		String ctu = sb.toString();

		CreativeType crt = ad.getCreativeType();
		if(crt == CreativeType.IMG) {
//			<html><head><meta http-equiv="Content-Type" content="text/html; charset=utf-8"/> <meta name="viewport" content="user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0"/><style type=\"text/css\">html, body {margin: 0;padding: 0;} img {width: 100%;height: 100%;}</style></head><body><a href="#CLICK#"  target="_blank"><img src="#ADURL#" border=0/></a></body></html>
			String html = tanxMobileAppImg;
			html = html.replaceAll("#CLICK#", ctu);
			html = html.replaceAll("#ADURL#", ad.getCreativeUrl());
			
//			String pvurls = ad.getPvUrls();
//			if (!StringUtils.isEmpty(pvurls)) {
//				String[] ps = JSON.parseObject(pvurls, String[].class);
//				for (String p : ps) {
//					if(!StringUtils.isEmpty(p)) {
//						html += "<iframe frameborder=\"no\" width=\"0\" height=\"0\"src=\"" + p + "\"></iframe>";
//					}
//				}
//			}
			return new String[]{html, ctu, pv};
		}

		return null;
	}

	/* (non-Javadoc)
	 * @see com.asme.ad.displayer.AdDisplayer#appMraid4Tanx(com.asme.ad.Session, net.doddata.web.dsp.model.Campaign, net.doddata.web.dsp.model.Ad)
	 */
	@Override
	public String[] appMraid4Tanx(Session session, Campaign camp, Ad ad) {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.asme.ad.displayer.AdDisplayer#extData4Bes(com.asme.ad.Session, net.doddata.web.dsp.model.Campaign, net.doddata.web.dsp.model.Ad)
	 */
	@Override
	public String extData4Bes(Session session, Campaign camp, Ad ad) {
		return LikeBase64.encode(JSON.toJSONString(buildParams(session, camp, ad)));
	}

	/* (non-Javadoc)
	 * @see com.asme.ad.displayer.AdDisplayer#html4Bes(com.asme.ad.Session, net.doddata.web.dsp.model.Campaign, net.doddata.web.dsp.model.Ad)
	 */
	@Override
	public String[] html4Bes(Session session, Campaign camp, Ad ad) {

		Param param = buildParams(session, camp, ad);

		StringBuilder sb = SB.get();
		sb.setLength(0);
		sb.append(counturl).append("/np?q=").append(LikeBase64.encode(JSON.toJSONString(param))).append("&p=%%PRICE%%");
		String pv = sb.toString();

		sb.setLength(0);
		sb.append(counturl).append("/nc?q=").append(LikeBase64.encode(JSON.toJSONString(param))).append("&r=").append(LikeBase64.encode(ad.getClickUrl()));
		String ctu = sb.toString();

		String size = ad.getSize();
		String[] wh = size.split("x");
		long campid = camp.getId();
		CreativeType crt = ad.getCreativeType();
		if(crt == CreativeType.IMG) {
//			<div style="border:0px;position:relative;width:#WIDTH#px;height:#HEIGHT#px;z-index:2147483647;">
//			<a href="http://www.doddata.net" target="_blank" style="cursor:pointer"><div onmouseover="this.style.display='none';this.parentNode.lastChild.style.display='block'" style="position:absolute;right:0;bottom:0;width:24px;height:17px;z-index:2147483646;background-image:url('http://pic.wonnder.com/bia/corner1.png');"></div><div onmouseout="setTimeout((function(me){return function(){me.style.display='none';me.parentNode.firstChild.style.display='block';};})(this), 500);" style="display:none;position:absolute;right:0;bottom:0;width:81px;height:17px;z-index:2147483646;background-image:url('http://pic.wonnder.com/bia/corner2.png');"></div></a>
//			<div style="border:0px;position:absolute;top:0px;left:0;width:#WIDTH#px;height:#HEIGHT#px;z-index:2147483645;">
//			<a href="#CLICK#" target="_blank"><img border="0" width="#WIDTH#" height="#HEIGHT#" src="#ADURL#" /></a>
//			</div>#IFM#
//			</div>
//			<img width="0" height="0" src="#PV#" />
			String html = pcImg;
			html = html.replaceAll("#CLICK#", "%%CLICK_URL_0%%");
			html = html.replaceAll("#WIDTH#", wh[0]);
			html = html.replaceAll("#HEIGHT#", wh[1]);
			html = html.replaceAll("#ADURL#", ad.getCreativeUrl());
			html = html.replaceAll("#PV#", pv);
			int af = campaignDAO.getAf(campid);
			if(af > 0) {
				AtomicInteger afc = AFC_MAP.get(campid);
				if (afc == null) {
					afc = new AtomicInteger(0);
					AtomicInteger old = AFC_MAP.putIfAbsent(campid, afc);
					if (old != null) {
						afc = old;
					}
				}

				if (afc.getAndIncrement() >= af) {
					afc.set(0);
					html = html.replaceAll("#IFM#", "<div style=\"border:0px;position:absolute;top:0px;left:0;width:" + wh[0] + "px;height:" + wh[1] + "px;z-index:1;\"><iframe frameborder=\"no\" src=\"" + ctu + "\" width=\"" + wh[0] + "\" height=\"" + wh[1] + "\"></iframe></div>");
				}
			}
			html = html.replaceAll("#IFM#", "");
			//这句是如果有第三方监控的url地址，则在页面加上一次第三方监控的请求
			String pvurls = ad.getPvUrls();
			if (!StringUtils.isEmpty(pvurls)) {
				String[] ps = JSON.parseObject(pvurls, String[].class);
				for (String p : ps) {
					if(!StringUtils.isEmpty(p)) {
						html += "<iframe frameborder=\"no\" width=\"0\" height=\"0\"src=\"" + p + "\"></iframe>";
					}
				}
			}

			return new String[]{html, ctu};
		} else if(crt == CreativeType.FLASH) {
//			<div style="border:0px;position:relative;width:#WIDTH#px;height:#HEIGHT#px;z-index:2147483647;">
//			<a href="#CLICK#" target="_blank" style="cursor:pointer">
//			<div style="position:relative;filter:alpha(opacity=0);opacity:0;-moz-opacity:0;left:0;top:0;background:#FFFFFF;width:#WIDTH#px;height:#HEIGHT#px;z-index:2147483645"></div>
//			</a>
//			<a href="http://www.doddata.net" target="_blank" style="cursor:pointer"><div onmouseover="this.style.display='none';this.parentNode.lastChild.style.display='block'" style="position:absolute;right:0;bottom:0;width:24px;height:17px;z-index:2147483646;background-image:url('http://pic.wonnder.com/bia/corner1.png');"></div><div onmouseout="setTimeout((function(me){return function(){me.style.display='none';me.parentNode.firstChild.style.display='block';};})(this), 500);" style="display:none;position:absolute;right:0;bottom:0;width:81px;height:17px;z-index:2147483646;background-image:url('http://pic.wonnder.com/bia/corner2.png');"></div></a>
//			<div style="border:0px;position:absolute;top:0px;left:0;width:#WIDTH#px;height:#HEIGHT#px;z-index:2147483644;">
//			<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"
//					codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=7"
//					width="#WIDTH#" height="#HEIGHT#">
//			<param name="movie" value="#ADURL#">
//			<param name="wmode" value="transparent">
//			<param name="quality" value="high">
//			<embed src="#ADURL#" width="#WIDTH#" height="#HEIGHT#" quality="high" type="application/x-shockwave-flash" wmode="transparent" pluginspage="http://www.macromedia.com/go/getflashplayer">
//			</embed>
//			</object>
//			</div>#IFM#
//			</div>
//			<img width="0" height="0" src="#PV#" />
			String html = pcFlash;
			html = html.replaceAll("#CLICK#", "%%CLICK_URL_0%%");
			html = html.replaceAll("#WIDTH#", wh[0]);
			html = html.replaceAll("#HEIGHT#", wh[1]);
			html = html.replaceAll("#ADURL#", ad.getCreativeUrl());
			html = html.replaceAll("#PV#", pv);
			int af = campaignDAO.getAf(campid);
			if(af > 0) {
				AtomicInteger afc = AFC_MAP.get(campid);
				if (afc == null) {
					afc = new AtomicInteger(0);
					AtomicInteger old = AFC_MAP.putIfAbsent(campid, afc);
					if (old != null) {
						afc = old;
					}
				}

				if (afc.getAndIncrement() >= af) {
					afc.set(0);
					html = html.replaceAll("#IFM#", "<div style=\"border:0px;position:absolute;top:0px;left:0;width:" + wh[0] + "px;height:" + wh[1] + "px;z-index:1;\"><iframe frameborder=\"no\" src=\"" + ctu + "\" width=\"" + wh[0] + "\" height=\"" + wh[1] + "\"></iframe></div>");
				}
			}
			html = html.replaceAll("#IFM#", "");

			String pvurls = ad.getPvUrls();
			if (!StringUtils.isEmpty(pvurls)) {
				String[] ps = JSON.parseObject(pvurls, String[].class);
				for (String p : ps) {
					if(!StringUtils.isEmpty(p)) {
						html += "<iframe frameborder=\"no\" width=\"0\" height=\"0\"src=\"" + p + "\"></iframe>";
					}
				}
			}

			return new String[]{html, ctu};
		}
		return null;
	}
	
	
	/**零集返回的广告内容**/
	@Override
	public String[] html4XTrader(Session session, Campaign camp, Ad ad) {
		Param param = buildParams(session, camp, ad);
		
		StringBuilder sb = SB.get();
		sb.setLength(0);
		sb.append(counturl).append("/np?q=").append(LikeBase64.encode(JSON.toJSONString(param))).append("&p=%%WINNING_PRICE%%");
		//sb.append("&clickm=%%CLICK_URL_ESC%%");
		String pv = sb.toString();

		sb.setLength(0);
		sb.append(counturl).append("/nc?q=").append(LikeBase64.encode(JSON.toJSONString(param))).append("&r=").append(LikeBase64.encode(ad.getClickUrl()));
		String ctu = sb.toString();

		String size = ad.getSize();
		String[] wh = size.split("x");
		long campid = camp.getId();
		CreativeType crt = ad.getCreativeType();
		if(crt == CreativeType.IMG) {
//			<div style="border:0px;position:relative;width:#WIDTH#px;height:#HEIGHT#px;z-index:2147483647;">
//			<a href="http://www.doddata.net" target="_blank" style="cursor:pointer"><div onmouseover="this.style.display='none';this.parentNode.lastChild.style.display='block'" style="position:absolute;right:0;bottom:0;width:24px;height:17px;z-index:2147483646;background-image:url('http://pic.wonnder.com/bia/corner1.png');"></div><div onmouseout="setTimeout((function(me){return function(){me.style.display='none';me.parentNode.firstChild.style.display='block';};})(this), 500);" style="display:none;position:absolute;right:0;bottom:0;width:81px;height:17px;z-index:2147483646;background-image:url('http://pic.wonnder.com/bia/corner2.png');"></div></a>
//			<div style="border:0px;position:absolute;top:0px;left:0;width:#WIDTH#px;height:#HEIGHT#px;z-index:2147483645;">
//			<a href="#CLICK#" target="_blank"><img border="0" width="#WIDTH#" height="#HEIGHT#" src="#ADURL#" /></a>
//			</div>#IFM#
//			</div>
//			<img width="0" height="0" src="#PV#" />
			String html = pcImg;
//			html = html.replaceAll("#CLICK#", "%%CLICK_URL_ESC%%");
			html = html.replaceAll("#CLICK#", "%%CLICK_URL_UNESC%%"+ctu);
			html = html.replaceAll("#WIDTH#", wh[0]);
			html = html.replaceAll("#HEIGHT#", wh[1]);
			html = html.replaceAll("#ADURL#", ad.getCreativeUrl());
			html = html.replaceAll("#PV#", pv);
			int af = campaignDAO.getAf(campid);
			if(af > 0) {
				AtomicInteger afc = AFC_MAP.get(campid);
				if (afc == null) {
					afc = new AtomicInteger(0);
					AtomicInteger old = AFC_MAP.putIfAbsent(campid, afc);
					if (old != null) {
						afc = old;
					}
				}

				if (afc.getAndIncrement() >= af) {
					afc.set(0);
					html = html.replaceAll("#IFM#", "<div style=\"border:0px;position:absolute;top:0px;left:0;width:" + wh[0] + "px;height:" + wh[1] + "px;z-index:1;\"><iframe frameborder=\"no\" src=\"" + ctu + "\" width=\"" + wh[0] + "\" height=\"" + wh[1] + "\"></iframe></div>");
				}
			}
			html = html.replaceAll("#IFM#", "");
			//这句是如果有第三方监控的url地址，则在页面加上一次第三方监控的请求
			String pvurls = ad.getPvUrls();
			if (!StringUtils.isEmpty(pvurls)) {
				String[] ps = JSON.parseObject(pvurls, String[].class);
				for (String p : ps) {
					if(!StringUtils.isEmpty(p)) {
						html += "<iframe frameborder=\"no\" width=\"0\" height=\"0\"src=\"" + p + "\"></iframe>";
					}
				}
			}
			//判断是否需要做CM操作
//			if(session.isCookieMapping()){
//				html += this.xtraderCookieMappingImg;
//			}
			
			return new String[]{html, ctu, pv,this.xtraderCookieMappingUrl};
		} else if(crt == CreativeType.FLASH) {
//			<div style="border:0px;position:relative;width:#WIDTH#px;height:#HEIGHT#px;z-index:2147483647;">
//			<a href="#CLICK#" target="_blank" style="cursor:pointer">
//			<div style="position:relative;filter:alpha(opacity=0);opacity:0;-moz-opacity:0;left:0;top:0;background:#FFFFFF;width:#WIDTH#px;height:#HEIGHT#px;z-index:2147483645"></div>
//			</a>
//			<a href="http://www.doddata.net" target="_blank" style="cursor:pointer"><div onmouseover="this.style.display='none';this.parentNode.lastChild.style.display='block'" style="position:absolute;right:0;bottom:0;width:24px;height:17px;z-index:2147483646;background-image:url('http://pic.wonnder.com/bia/corner1.png');"></div><div onmouseout="setTimeout((function(me){return function(){me.style.display='none';me.parentNode.firstChild.style.display='block';};})(this), 500);" style="display:none;position:absolute;right:0;bottom:0;width:81px;height:17px;z-index:2147483646;background-image:url('http://pic.wonnder.com/bia/corner2.png');"></div></a>
//			<div style="border:0px;position:absolute;top:0px;left:0;width:#WIDTH#px;height:#HEIGHT#px;z-index:2147483644;">
//			<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"
//					codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=7"
//					width="#WIDTH#" height="#HEIGHT#">
//			<param name="movie" value="#ADURL#">
//			<param name="wmode" value="transparent">
//			<param name="quality" value="high">
//			<embed src="#ADURL#" width="#WIDTH#" height="#HEIGHT#" quality="high" type="application/x-shockwave-flash" wmode="transparent" pluginspage="http://www.macromedia.com/go/getflashplayer">
//			</embed>
//			</object>
//			</div>#IFM#
//			</div>
//			<img width="0" height="0" src="#PV#" />
			String html = pcFlash;
			html = html.replaceAll("#CLICK#", ctu);
			html = html.replaceAll("#WIDTH#", wh[0]);
			html = html.replaceAll("#HEIGHT#", wh[1]);
			html = html.replaceAll("#ADURL#", ad.getCreativeUrl());
			html = html.replaceAll("#PV#", pv);
			int af = campaignDAO.getAf(campid);
			if(af > 0) {
				AtomicInteger afc = AFC_MAP.get(campid);
				if (afc == null) {
					afc = new AtomicInteger(0);
					AtomicInteger old = AFC_MAP.putIfAbsent(campid, afc);
					if (old != null) {
						afc = old;
					}
				}

				if (afc.getAndIncrement() >= af) {
					afc.set(0);
					html = html.replaceAll("#IFM#", "<div style=\"border:0px;position:absolute;top:0px;left:0;width:" + wh[0] + "px;height:" + wh[1] + "px;z-index:1;\"><iframe frameborder=\"no\" src=\"" + ctu + "\" width=\"" + wh[0] + "\" height=\"" + wh[1] + "\"></iframe></div>");
				}
			}
			html = html.replaceAll("#IFM#", "");

			String pvurls = ad.getPvUrls();
			if (!StringUtils.isEmpty(pvurls)) {
				String[] ps = JSON.parseObject(pvurls, String[].class);
				for (String p : ps) {
					if(!StringUtils.isEmpty(p)) {
						html += "<iframe frameborder=\"no\" width=\"0\" height=\"0\"src=\"" + p + "\"></iframe>";
					}
				}
			}
			
			//判断是否需要做CM操作
//			if(session.isCookieMapping()){
//				html += this.xtraderCookieMappingImg;
//			}
			
			return new String[]{html, ctu, pv,this.xtraderCookieMappingUrl};
		}
		return null;
	}
	public String[] iframeXTrader(Session session, Campaign camp, Ad ad){
		
		Param param = buildParams(session, camp, ad);
		
		StringBuilder sb = SB.get();
		sb.setLength(0);
		sb.append(counturl).append("/np?q=").append(LikeBase64.encode(JSON.toJSONString(param))).append("&p=%%WINNING_PRICE%%");
		String pv = sb.toString();

		sb.setLength(0);
		sb.append(counturl).append("/nc?q=").append(LikeBase64.encode(JSON.toJSONString(param))).append("&r=").append(LikeBase64.encode(ad.getClickUrl()));
		String ctu = sb.toString();

		String size = ad.getSize();
		String[] wh = size.split("x");
		long campid = camp.getId();
		CreativeType crt = ad.getCreativeType();

		String html = xtraderIframeTpl;
		html.replace("#URL#", pv);
		html.replace("#WIDTH#", wh[0]);
		html.replace("#HEIGHT#", wh[1]);
		
		int af = campaignDAO.getAf(campid);
		if(af > 0) {
			AtomicInteger afc = AFC_MAP.get(campid);
			if (afc == null) {
				afc = new AtomicInteger(0);
				AtomicInteger old = AFC_MAP.putIfAbsent(campid, afc);
				if (old != null) {
					afc = old;
				}
			}

			if (afc.getAndIncrement() >= af) {
				afc.set(0);
				html = html.replaceAll("#IFM#", "<div style=\"border:0px;position:absolute;top:0px;left:0;width:" + wh[0] + "px;height:" + wh[1] + "px;z-index:1;\"><iframe frameborder=\"no\" src=\"" + ctu + "\" width=\"" + wh[0] + "\" height=\"" + wh[1] + "\"></iframe></div>");
			}
		}else{
			html = html.replaceAll("#IFM#", "");
		}
//		html = html.replaceAll("#IFM#", "");
		//这句是如果有第三方监控的url地址，则在页面加上一次第三方监控的请求
		String pvurls = ad.getPvUrls();
		if (!StringUtils.isEmpty(pvurls)) {
			String[] ps = JSON.parseObject(pvurls, String[].class);
			for (String p : ps) {
				if(!StringUtils.isEmpty(p)) {
					html += "<iframe frameborder=\"no\" width=\"0\" height=\"0\"src=\"" + p + "\"></iframe>";
				}
			}
		}
		//判断是否需要做CM操作
		if(session.isCookieMapping()){
			html += this.xtraderCookieMappingImg;
		}
		
		return new String[]{html, ctu, pv,this.xtraderCookieMappingUrl};
	}
	public String[] urls4Xtrader(Session session,Campaign camp, Ad ad){
		
		Param param = buildParams(session, camp, ad);
		
		StringBuilder sb = SB.get();
		sb.setLength(0);
		sb.append(counturl).append("/np?q=").append(LikeBase64.encode(JSON.toJSONString(param))).append("&id=${AUCTION_ID}");
		sb.append("&bidid=${AUCTION_BID_ID}");
		sb.append("&impid=${AUCTION_IMP_ID}");
		sb.append("&p=${AUCTION_PRICE}");
		String pv = sb.toString();
//		id	${AUCTION_ID}	Bid Request ID
//		bidid	${AUCTION_BID_ID}	Bid Response ID per request
//		impid	${AUCTION_IMP_ID}	Impression ID
//		price	${AUCTION_PRICE}	Settlement price，曝光的最终拍卖价。这个字段会使用AES加密，加密的Key是DSP的token。为了和其他Exchange 统一，加密之前我们会在价格之后加上时间戳后缀，即形式如”PRICE_TIME”这样（例如：’201_1376468920380’），再进行加密。解密时请注意需要split出来分别获得价格和时间戳。这里的时间戳是精确到毫秒的。另外，对于动态物料，也可以在"bid":"adm"字段中通过宏%%WINNING_PRICE%%来获取价格。

		sb.setLength(0);
		sb.append(counturl).append("/nc?q=").append(LikeBase64.encode(JSON.toJSONString(param))).append("&r=").append(LikeBase64.encode(ad.getClickUrl()));
		String ctu = sb.toString();

		String creativeUrl=ad.getCreativeUrl();
		
		//判断是否需要做CM操作
		if(session.isCookieMapping()){
			return new String[]{pv,ctu,creativeUrl,this.xtraderCookieMappingUrl};
		}else{
			return new String[]{pv,ctu,creativeUrl};
		}
	}
	public String[] iframeScriptXTrader(Session session, Campaign camp, Ad ad){
		Param param = buildParams(session, camp, ad);
		StringBuilder sb = SB.get();
		sb.setLength(0);
		sb.append(counturl).append("/np?q=").append(LikeBase64.encode(JSON.toJSONString(param))).append("&p=%%WINNING_PRICE%%");
		sb.append("&clickm=%%CLICK_URL_ESC%%");
		String pv = sb.toString();

		sb.setLength(0);
		sb.append(counturl).append("/nc?q=").append(LikeBase64.encode(JSON.toJSONString(param))).append("&r=").append(LikeBase64.encode(ad.getClickUrl()));
		String ctu = sb.toString();

		String size = ad.getSize();
		String[] wh = size.split("x");
		long campid = camp.getId();
		CreativeType crt = ad.getCreativeType();
		if(crt == CreativeType.IMG) {
			String html = xtraderIframeScriptTpl;
			html = html.replaceAll("#CLICKDSP#", ctu);
			html = html.replaceAll("#WIDTH#", wh[0]);
			html = html.replaceAll("#HEIGHT#", wh[1]);
			html = html.replaceAll("#ADURL#", ad.getCreativeUrl());
			html = html.replaceAll("#PV#", pv);
			int af = campaignDAO.getAf(campid);
			if(af > 0) {
				AtomicInteger afc = AFC_MAP.get(campid);
				if (afc == null) {
					afc = new AtomicInteger(0);
					AtomicInteger old = AFC_MAP.putIfAbsent(campid, afc);
					if (old != null) {
						afc = old;
					}
				}

				if (afc.getAndIncrement() >= af) {
					afc.set(0);
					html = html.replaceAll("#IFM#", "<div style=\"border:0px;position:absolute;top:0px;left:0;width:" + wh[0] + "px;height:" + wh[1] + "px;z-index:1;\"><iframe frameborder=\"no\" src=\"" + ctu + "\" width=\"" + wh[0] + "\" height=\"" + wh[1] + "\"></iframe></div>");
				}
			}
			html = html.replaceAll("#IFM#", "");
			//这句是如果有第三方监控的url地址，则在页面加上一次第三方监控的请求
			String pvurls = ad.getPvUrls();
			if (!StringUtils.isEmpty(pvurls)) {
				String[] ps = JSON.parseObject(pvurls, String[].class);
				for (String p : ps) {
					if(!StringUtils.isEmpty(p)) {
						html += "<iframe frameborder=\"no\" width=\"0\" height=\"0\"src=\"" + p + "\"></iframe>";
					}
				}
			}
			//判断是否需要做CM操作
			if(session.isCookieMapping()){
				html += this.xtraderCookieMappingImg;
			}
			
			return new String[]{html, ctu, pv,this.xtraderCookieMappingUrl};
		} else if(crt == CreativeType.FLASH) {
			String html = xtraderIframeScriptTpl;
			html = html.replaceAll("#CLICK#", "%%CLICK_URL_UNESC%%");
			html = html.replaceAll("#WIDTH#", wh[0]);
			html = html.replaceAll("#HEIGHT#", wh[1]);
			html = html.replaceAll("#ADURL#", ad.getCreativeUrl());
			html = html.replaceAll("#PV#", pv);
			int af = campaignDAO.getAf(campid);
			if(af > 0) {
				AtomicInteger afc = AFC_MAP.get(campid);
				if (afc == null) {
					afc = new AtomicInteger(0);
					AtomicInteger old = AFC_MAP.putIfAbsent(campid, afc);
					if (old != null) {
						afc = old;
					}
				}

				if (afc.getAndIncrement() >= af) {
					afc.set(0);
					html = html.replaceAll("#IFM#", "<div style=\"border:0px;position:absolute;top:0px;left:0;width:" + wh[0] + "px;height:" + wh[1] + "px;z-index:1;\"><iframe frameborder=\"no\" src=\"" + ctu + "\" width=\"" + wh[0] + "\" height=\"" + wh[1] + "\"></iframe></div>");
				}
			}
			html = html.replaceAll("#IFM#", "");

			String pvurls = ad.getPvUrls();
			if (!StringUtils.isEmpty(pvurls)) {
				String[] ps = JSON.parseObject(pvurls, String[].class);
				for (String p : ps) {
					if(!StringUtils.isEmpty(p)) {
						html += "<iframe frameborder=\"no\" width=\"0\" height=\"0\"src=\"" + p + "\"></iframe>";
					}
				}
			}
			
			//判断是否需要做CM操作
			if(session.isCookieMapping()){
				html += this.xtraderCookieMappingImg;
			}
			
			return new String[]{html, ctu, pv,this.xtraderCookieMappingUrl};
		}
		
		return null;
	}
	
	/**
	 * 互众返回的广告内容
	 */
	@Override
	public String[] htmlHz(Session session, Campaign camp, Ad ad) {
		Param param = buildParams(session, camp, ad);

		StringBuilder sb = SB.get();
		sb.setLength(0);
		sb.append(counturl).append("/np?q=").append(LikeBase64.encode(JSON.toJSONString(param))).append("&p=${AUCTION_PRICE}");
		String pv = sb.toString();

		sb.setLength(0);
		sb.append(counturl).append("/nc?q=").append(LikeBase64.encode(JSON.toJSONString(param))).append("&r=").append(LikeBase64.encode(ad.getClickUrl()));
		String ctu = sb.toString();

		String size = ad.getSize();
		String[] wh = size.split("x");
		long campid = camp.getId();
		CreativeType crt = ad.getCreativeType();
		if(crt == CreativeType.IMG) {
			String html = pcImgHz;
			html = html.replaceAll("#CLICK#", ctu);
			html = html.replaceAll("#WIDTH#", wh[0]);
			html = html.replaceAll("#HEIGHT#", wh[1]);
			html = html.replaceAll("#ADURL#", ad.getCreativeUrl());
			html = html.replaceAll("#PV#", pv);
			int af = campaignDAO.getAf(campid);
			if(af > 0) {
				AtomicInteger afc = AFC_MAP.get(campid);
				if (afc == null) {
					afc = new AtomicInteger(0);
					AtomicInteger old = AFC_MAP.putIfAbsent(campid, afc);
					if (old != null) {
						afc = old;
					}
				}

				if (afc.getAndIncrement() >= af) {
					afc.set(0);
					html = html.replaceAll("#IFM#", "<div style=\"border:0px;position:absolute;top:0px;left:0;width:" + wh[0] + "px;height:" + wh[1] + "px;z-index:1;\"><iframe frameborder=\"no\" src=\"" + ctu + "\" width=\"" + wh[0] + "\" height=\"" + wh[1] + "\"></iframe></div>");
				}
			}
			html = html.replaceAll("#IFM#", "");
			//这句是如果有第三方监控的url地址，则在页面加上一次第三方监控的请求
			String pvurls = ad.getPvUrls();
			if (!StringUtils.isEmpty(pvurls)) {
				String[] ps = JSON.parseObject(pvurls, String[].class);
				for (String p : ps) {
					if(!StringUtils.isEmpty(p)) {
						html += "<iframe frameborder=\"no\" width=\"0\" height=\"0\"src=\"" + p + "\"></iframe>";
					}
				}
			}

			return new String[]{html, ctu};
		} else if(crt == CreativeType.FLASH) {
			String html = pcImgHz;
			html = html.replaceAll("#CLICK#", ctu);
			html = html.replaceAll("#WIDTH#", wh[0]);
			html = html.replaceAll("#HEIGHT#", wh[1]);
			html = html.replaceAll("#ADURL#", ad.getCreativeUrl());
			html = html.replaceAll("#PV#", pv);
			int af = campaignDAO.getAf(campid);
			if(af > 0) {
				AtomicInteger afc = AFC_MAP.get(campid);
				if (afc == null) {
					afc = new AtomicInteger(0);
					AtomicInteger old = AFC_MAP.putIfAbsent(campid, afc);
					if (old != null) {
						afc = old;
					}
				}

				if (afc.getAndIncrement() >= af) {
					afc.set(0);
					html = html.replaceAll("#IFM#", "<div style=\"border:0px;position:absolute;top:0px;left:0;width:" + wh[0] + "px;height:" + wh[1] + "px;z-index:1;\"><iframe frameborder=\"no\" src=\"" + ctu + "\" width=\"" + wh[0] + "\" height=\"" + wh[1] + "\"></iframe></div>");
				}
			}
			html = html.replaceAll("#IFM#", "");

			String pvurls = ad.getPvUrls();
			if (!StringUtils.isEmpty(pvurls)) {
				String[] ps = JSON.parseObject(pvurls, String[].class);
				for (String p : ps) {
					if(!StringUtils.isEmpty(p)) {
						html += "<iframe frameborder=\"no\" width=\"0\" height=\"0\"src=\"" + p + "\"></iframe>";
					}
				}
			}

			return new String[]{html, ctu};
		}
		return null;
	}

	
	
	@Override
	public Map<String,String> jsonHzApp(Session session, Campaign camp, Ad ad,int width,int height) {
		//拼装参数
		Param param = buildParams(session, camp, ad);
		Map<String,String> ret=new HashMap<String,String>();
		ImageSnippet imgSnippet=new ImageSnippet();
		
		StringBuilder sb = SB.get();
		sb.setLength(0);
		sb.append(counturl).append("/np?q=").append(LikeBase64.encode(JSON.toJSONString(param))).append("&p=${AUCTION_PRICE}");
		//曝光地址
		String pv = sb.toString();

		sb.setLength(0);
		sb.append(counturl).append("/nc?q=").append(LikeBase64.encode(JSON.toJSONString(param))).append("&r=").append(LikeBase64.encode(ad.getClickUrl()));
		//点击地址
		String ctu = sb.toString();
		//广告图片url地址
		imgSnippet.setUrl(ad.getCreativeUrl());//图片url
		imgSnippet.setC_url(ctu);
		imgSnippet.setWidth(width);
		imgSnippet.setHeight(height);
		imgSnippet.setAction(0);
		//曝光地址
		List<String> imp=new ArrayList<String>();
		imp.add(pv);
		String pvurls = ad.getPvUrls();
		if (!StringUtils.isEmpty(pvurls)) {
			String[] ps = JSON.parseObject(pvurls, String[].class);
			for (String p : ps) {
				if(!StringUtils.isEmpty(p)) {
					imp.add(p);
				}
			}
		}
		imgSnippet.setImp(imp);
		//点击地址
		List<String> clk=new ArrayList<String>();
		imgSnippet.setClk(clk);
		
		ret.put("image_snippet", JSON.toJSONString(imgSnippet));
		return ret;
	}

	
	
	
	/**
	 * 互众静态广告
	 */
	@Override
	public String[] extDataHz(Session session, Campaign camp, Ad ad) {
		String[] retStrs={counturl,LikeBase64.encode(JSON.toJSONString(buildParams(session, camp, ad)))};
		return retStrs;
	}
	
	
	

	@Override
	public String html4VAM(Session session, Campaign camp, Ad ad) {
		Param param = buildParams(session, camp, ad);
		
		VamRequest req=(VamRequest)session.getReq();
		int secure=0;
		try{
			secure=req.getSecure();
		}catch(Exception ex){
			secure=0;
		}

		StringBuilder sb = SB.get();
		sb.setLength(0);
		if(secure==1){
			sb.append(counturlhttps).append("/np?q=").append(LikeBase64.encode(JSON.toJSONString(param))).append("&p=\\${AUCTION_PRICE_ENC}");
		}else{
			sb.append(counturl).append("/np?q=").append(LikeBase64.encode(JSON.toJSONString(param))).append("&p=\\${AUCTION_PRICE_ENC}");
		}
		String pv = sb.toString();
		
		sb.setLength(0);
		if(secure==1){
			sb.append(counturlhttps).append("/nc?q=").append(LikeBase64.encode(JSON.toJSONString(param))).append("&r=").append(LikeBase64.encode(ad.getClickUrl()));
		}else{
			sb.append(counturl).append("/nc?q=").append(LikeBase64.encode(JSON.toJSONString(param))).append("&r=").append(LikeBase64.encode(ad.getClickUrl()));
		}
		
		String ctu = sb.toString();

		String size = ad.getSize();
		String[] wh = size.split("x");
		long campid = camp.getId();
		CreativeType crt = ad.getCreativeType();
		if(crt == CreativeType.IMG) {
//			<div style="border:0px;position:relative;width:#WIDTH#px;height:#HEIGHT#px;z-index:2147483647;">
//			<a href="http://www.doddata.net" target="_blank" style="cursor:pointer"><div onmouseover="this.style.display='none';this.parentNode.lastChild.style.display='block'" style="position:absolute;right:0;bottom:0;width:24px;height:17px;z-index:2147483646;background-image:url('http://pic.wonnder.com/bia/corner1.png');"></div><div onmouseout="setTimeout((function(me){return function(){me.style.display='none';me.parentNode.firstChild.style.display='block';};})(this), 500);" style="display:none;position:absolute;right:0;bottom:0;width:81px;height:17px;z-index:2147483646;background-image:url('http://pic.wonnder.com/bia/corner2.png');"></div></a>
//			<div style="border:0px;position:absolute;top:0px;left:0;width:#WIDTH#px;height:#HEIGHT#px;z-index:2147483645;">
//			<a href="#CLICK#" target="_blank"><img border="0" width="#WIDTH#" height="#HEIGHT#" src="#ADURL#" /></a>
//			</div>#IFM#
//			</div>
//			<img width="0" height="0" src="#PV#" />
			String html = pcImg;
			try {
				html = html.replaceAll("#CLICK#", "{!vam_click_url}" + URLEncoder.encode(ctu,"UTF-8"));
			} catch (Exception e) {}
			html = html.replaceAll("#WIDTH#", wh[0]);
			html = html.replaceAll("#HEIGHT#", wh[1]);
			html = html.replaceAll("#ADURL#", ad.getCreativeUrl());
			html = html.replaceAll("#PV#", pv);
			int af = campaignDAO.getAf(campid);
			if(af > 0) {
				AtomicInteger afc = AFC_MAP.get(campid);
				if (afc == null) {
					afc = new AtomicInteger(0);
					AtomicInteger old = AFC_MAP.putIfAbsent(campid, afc);
					if (old != null) {
						afc = old;
					}
				}

				if (afc.getAndIncrement() >= af) {
					afc.set(0);
					html = html.replaceAll("#IFM#", "<div style=\"border:0px;position:absolute;top:0px;left:0;width:" + wh[0] + "px;height:" + wh[1] + "px;z-index:1;\"><iframe frameborder=\"no\" src=\"" + ctu + "\" width=\"" + wh[0] + "\" height=\"" + wh[1] + "\"></iframe></div>");
				}
			}
			html = html.replaceAll("#IFM#", "");
			
			String pvurls = ad.getPvUrls();
			if (!StringUtils.isEmpty(pvurls)) {
				String[] ps = JSON.parseObject(pvurls, String[].class);
				for (String p : ps) {
					if(!StringUtils.isEmpty(p)) {
						html += "<iframe frameborder=\"no\" width=\"0\" height=\"0\"src=\"" + p + "\"></iframe>";
					}
				}
			}

			return html;
		} else if(crt == CreativeType.FLASH) {
//			<div style="border:0px;position:relative;width:#WIDTH#px;height:#HEIGHT#px;z-index:2147483647;">
//			<a href="#CLICK#" target="_blank" style="cursor:pointer">
//			<div style="position:relative;filter:alpha(opacity=0);opacity:0;-moz-opacity:0;left:0;top:0;background:#FFFFFF;width:#WIDTH#px;height:#HEIGHT#px;z-index:2147483645"></div>
//			</a>
//			<a href="http://www.doddata.net" target="_blank" style="cursor:pointer"><div onmouseover="this.style.display='none';this.parentNode.lastChild.style.display='block'" style="position:absolute;right:0;bottom:0;width:24px;height:17px;z-index:2147483646;background-image:url('http://pic.wonnder.com/bia/corner1.png');"></div><div onmouseout="setTimeout((function(me){return function(){me.style.display='none';me.parentNode.firstChild.style.display='block';};})(this), 500);" style="display:none;position:absolute;right:0;bottom:0;width:81px;height:17px;z-index:2147483646;background-image:url('http://pic.wonnder.com/bia/corner2.png');"></div></a>
//			<div style="border:0px;position:absolute;top:0px;left:0;width:#WIDTH#px;height:#HEIGHT#px;z-index:2147483644;">
//			<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"
//					codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=7"
//					width="#WIDTH#" height="#HEIGHT#">
//			<param name="movie" value="#ADURL#">
//			<param name="wmode" value="transparent">
//			<param name="quality" value="high">
//			<embed src="#ADURL#" width="#WIDTH#" height="#HEIGHT#" quality="high" type="application/x-shockwave-flash" wmode="transparent" pluginspage="http://www.macromedia.com/go/getflashplayer">
//			</embed>
//			</object>
//			</div>#IFM#
//			</div>
//			<img width="0" height="0" src="#PV#" />
			String html = pcFlash;
			try {
				html = html.replaceAll("#CLICK#", "{!vam_click_url}" + URLEncoder.encode(ctu,"UTF-8"));
			} catch (Exception e) {}
			html = html.replaceAll("#WIDTH#", wh[0]);
			html = html.replaceAll("#HEIGHT#", wh[1]);
			html = html.replaceAll("#ADURL#", ad.getCreativeUrl());
			html = html.replaceAll("#PV#", pv);
			int af = campaignDAO.getAf(campid);
			if(af > 0) {
				AtomicInteger afc = AFC_MAP.get(campid);
				if (afc == null) {
					afc = new AtomicInteger(0);
					AtomicInteger old = AFC_MAP.putIfAbsent(campid, afc);
					if (old != null) {
						afc = old;
					}
				}

				if (afc.getAndIncrement() >= af) {
					afc.set(0);
					html = html.replaceAll("#IFM#", "<div style=\"border:0px;position:absolute;top:0px;left:0;width:" + wh[0] + "px;height:" + wh[1] + "px;z-index:1;\"><iframe frameborder=\"no\" src=\"" + ctu + "\" width=\"" + wh[0] + "\" height=\"" + wh[1] + "\"></iframe></div>");
				}
			}
			html = html.replaceAll("#IFM#", "");

			String pvurls = ad.getPvUrls();
			if (!StringUtils.isEmpty(pvurls)) {
				String[] ps = JSON.parseObject(pvurls, String[].class);
				for (String p : ps) {
					html += "<iframe frameborder=\"no\" width=\"0\" height=\"0\"src=\"" + p + "\"></iframe>";
				}
			}

			return html;
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.asme.ad.displayer.AdDisplayer#url4VAMMobile(com.asme.ad.Session, net.doddata.web.dsp.model.Campaign, net.doddata.web.dsp.model.Ad)
	 */
	@Override
	public String[] url4VAMMobile(Session session, Campaign camp, Ad ad) {

		Param param = buildParams(session, camp, ad);
		VamRequest req=(VamRequest)session.getReq();
		int secure=0;
		try{
			secure=req.getSecure();
		}catch(Exception ex){
			secure=0;
		}
		
		StringBuilder sb = SB.get();
		sb.setLength(0);
		if(secure==1){
			sb.append(counturlhttps).append("/np?q=").append(LikeBase64.encode(JSON.toJSONString(param))).append("&p=${AUCTION_PRICE_ENC}");
		}else{
			sb.append(counturl).append("/np?q=").append(LikeBase64.encode(JSON.toJSONString(param))).append("&p=${AUCTION_PRICE_ENC}");
		}
		String pv = sb.toString();

		sb.setLength(0);
		if(secure==1){
			sb.append(counturlhttps).append("/nc?q=").append(LikeBase64.encode(JSON.toJSONString(param))).append("&r=").append(LikeBase64.encode(ad.getClickUrl()));
		}else{
			sb.append(counturl).append("/nc?q=").append(LikeBase64.encode(JSON.toJSONString(param))).append("&r=").append(LikeBase64.encode(ad.getClickUrl()));
		}
		String ctu = sb.toString();
		
		List<String> urls = new ArrayList<String>();
		try {
			urls.add(URLEncoder.encode(ctu,"UTF-8"));
		} catch (UnsupportedEncodingException e) {
		}
		urls.add(pv);

		String pvurls = ad.getPvUrls();
		if (!StringUtils.isEmpty(pvurls)) {
			String[] ps = JSON.parseObject(pvurls, String[].class);
			for (String p : ps) {
				urls.add(p);
			}
		}
		return urls.toArray(new String[urls.size()]);
	}

	/* (non-Javadoc)
	 * @see com.asme.ad.displayer.AdDisplayer#url4VAMVideo(com.asme.ad.Session, net.doddata.web.dsp.model.Campaign, net.doddata.web.dsp.model.Ad)
	 */
	@Override
	public String[] url4VAMVideo(Session session, Campaign camp, Ad ad) {
		
		Param param = buildParams(session, camp, ad);
		
		VamRequest req=(VamRequest)session.getReq();
		int secure=0;
		try{
			secure=req.getSecure();
		}catch(Exception ex){
			secure=0;
		}
		
		StringBuilder sb = SB.get();
		sb.setLength(0);
		if(secure==1){
			sb.append(counturlhttps).append("/np?q=").append(LikeBase64.encode(JSON.toJSONString(param))).append("&p=${AUCTION_PRICE_ENC}");
		}else{
			sb.append(counturl).append("/np?q=").append(LikeBase64.encode(JSON.toJSONString(param))).append("&p=${AUCTION_PRICE_ENC}");
		}
		String pv = sb.toString();

		sb.setLength(0);
		if(secure==1){
			sb.append(counturlhttps).append("/nc?q=").append(LikeBase64.encode(JSON.toJSONString(param))).append("&r=").append(LikeBase64.encode(ad.getClickUrl()));
		}else{
			sb.append(counturl).append("/nc?q=").append(LikeBase64.encode(JSON.toJSONString(param))).append("&r=").append(LikeBase64.encode(ad.getClickUrl()));
		}
		
		String ctu = sb.toString();
		
		List<String> urls = new ArrayList<String>();
		try {
			urls.add(URLEncoder.encode(ctu,"UTF-8"));
		} catch (UnsupportedEncodingException e) {
		}
		urls.add(pv);

		String pvurls = ad.getPvUrls();
		if (!StringUtils.isEmpty(pvurls)) {
			String[] ps = JSON.parseObject(pvurls, String[].class);
			for (String p : ps) {
				urls.add(p);
			}
		}
		return urls.toArray(new String[urls.size()]);
	}
	
	/**
	 * 参数
	 * @param session
	 * @param camp
	 * @param ad
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Param buildParams(Session session, Campaign camp, Ad ad) {

		Param p = new Param();
		
		// 时间
		p.setT(systemTime.getTime());
		// 竞价请求id，bid
		p.setB(session.getBid());
		// 底价和出价
		p.setBf(session.getBidFloor());//底价
		p.setBp(session.getBidPrice());//出价

		// 流量类型和是否是app
//		p.setTt(session.getAdType().ordinal()); // 流量类型在V2R2后取消，与AdType一对一
		p.setApp(session.isInApp());

		// 当前页面或者包名
		String pageurl = session.getPageUrl();
		if (!StringUtils.isEmpty(pageurl)) {
			p.setUrl(pageurl);
		}
		//网站分类类型，这里是adx请求时传递过来的分类类型
		String webc = session.getWebCatg();
		if (!StringUtils.isEmpty(webc)) {
			p.setWc(webc);
		}
		//app id
		String appid=session.getAppId();
		if(!StringUtils.isEmpty(appid)){
			p.setAppid(appid);
		}
		//应用名称，如果是app请求，才会有这个值
		String appName = session.getAppName();
		if (!StringUtils.isEmpty(appName)) {
			p.setAppn(appName);
		}
		//appPack，如果是app请求，才会有这个值
		String appPackName = session.getAppPackageName();
		if (!StringUtils.isEmpty(appPackName)) {
			p.setAppp(appPackName);
		}
		//app分类数据，如果是app请求，才会有这个值
		String appCatg = session.getAppCatg();
		if (!StringUtils.isEmpty(appCatg)) {
			p.setAppc(appCatg);
		}
		//用户请求时的ip
		String userip = session.getUserip();
		if (userip != null) {
			p.setIp(session.getUserip());
		}
		//DSP用户的cookieID
		String cid = session.getCid();
		if(!StringUtils.isEmpty(cid)) {
			p.setC(cid);
		}

		// 设备号
		if (session.getAdx() == Adx.TANX) {
			String dev = (String) session.getAttr("TANXDEVID");
			if (!StringUtils.isEmpty(dev)) {
				p.setDev(dev);
			}
			p.setAdp(((BidRequest) session.getReq()).getAdzinfo(0).getPid());
		} else if (session.getAdx() == Adx.BES) {
			List<String> ids = (List<String>) session.getAttr("BESDEVIDS");
			if (ids != null && ids.size() > 0) {
				p.setDev(ids.get(0));
			}
			p.setAdp(String.valueOf(((BaiduRealtimeBiddingV26.BidRequest) session.getReq()).getAdslot(0).getAdBlockKey()));
		} else if(session.getAdx() == Adx.XTRADER){
			//如果渠道是秒针的渠道则在匹配的时候，将广告为id tagid设置到了session中的appid中,所以这里直接从session中获取
			p.setAdp(session.getAdp());
		} else if(session.getAdx() == Adx.HZ){
			p.setAdp(session.getAdp());
		} else if(session.getAdx()==Adx.VAM){
			p.setAdp(session.getAdp());
		}
		//城市id
		Integer cityId = (Integer)session.getAttr("cityid");
		if(cityId != null) {
			p.setCt(cityId);
		}
		//渠道
		p.setCh(session.getAdx().channelId());
		//交易类型
		p.setTs(TransType.RTB.getCode());
		//partentid,广告主id
		p.setP(camp.getPartnerId());
		//推广组id
		p.setG(camp.getGroupId());
		//推广活动id
		p.setCp(camp.getId());
		//推广活动类型
		p.setCpt(camp.getCampType().getCode());
		//创意组id
		p.setAg(ad.getGroupId());
		//广告id
		p.setA(ad.getId());
		//广告类型
		p.setAt(camp.getAdType().getCode());
		//创意id
		p.setCr(ad.getCreativeId());
		return p;
	}

	/**
	 * @param counturl the counturl to set
	 */
	public void setCounturl(String counturl) {
		this.counturl = counturl;
	}

	public void setCounturlhttps(String counturlhttps) {
		this.counturlhttps = counturlhttps;
	}

	/**
	 * @param tanxCookieMappingUrl the tanxCookieMappingUrl to set
	 */
	public void setTanxCookieMappingUrl(String tanxCookieMappingUrl) {
		this.tanxCookieMappingImg = new StringBuilder("<img width=\"0\" height=\"0\" src=\"").append(tanxCookieMappingUrl).append("\" />").toString();
	}
	
	public void setXtraderCookieMappingUrl(String xtraderCookieMappingUrl){
		this.xtraderCookieMappingUrl=xtraderCookieMappingUrl;
		this.xtraderCookieMappingImg=new StringBuilder("<img width=\"0\" height=\"0\" src=\"").append(xtraderCookieMappingUrl).append("\" />").toString();
	}

	/**
	 * @param systemTime the systemTime to set
	 */
	public void setSystemTime(SystemTime systemTime) {
		this.systemTime = systemTime;
	}

	/**
	 * @param campaignDAO the campaignDAO to set
	 */
	public void setCampaignDAO(CampaignDAO campaignDAO) {
		this.campaignDAO = campaignDAO;
	}

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年10月25日
 * @项目名称 dsp-engine
 * @描述 <p></p>
 */
	public static class Param {
		private long t; // 时间
		private String b; // bidrequest id
		private int bf; // 底价
		private int bp; // 出价
		private int tt; // 流量类型
		private boolean app; // 是否是APP
		private String url; // 页面URL
		private String wc; // 网站类型
		private String appid; //appid
		private String appn; // 应用名
		private String appp; // 应用包名
		private String appc; // 应用类别
		private String ip; // IP
		private String c; // cookieid
		private String dev; // 设备号
		private Integer ct; // cityid
		private int ch; // 渠道id
		private String adp; // 广告位ID
		private int ts; // 交易类型
		private long p; // partnerid
		private long g; // 推广组ID
		private long cp; // 推广活动ID
		private int cpt; // 推广活动类型
		private long ag; // 创意组ID
		private long a; // 广告ID
		private int at; // 广告类型
		private long cr; // 创意ID

		/**
		 * @return the t
		 */
		public long getT() {
			return t;
		}
		/**
		 * @param t the t to set
		 */
		public void setT(long t) {
			this.t = t;
		}
		/**
		 * @return the b
		 */
		public String getB() {
			return b;
		}
		/**
		 * @param b the b to set
		 */
		public void setB(String b) {
			this.b = b;
		}
		/**
		 * @return the bf
		 */
		public int getBf() {
			return bf;
		}
		/**
		 * @param bf the bf to set
		 */
		public void setBf(int bf) {
			this.bf = bf;
		}
		/**
		 * @return the bp
		 */
		public int getBp() {
			return bp;
		}
		/**
		 * @param bp the bp to set
		 */
		public void setBp(int bp) {
			this.bp = bp;
		}
		/**
		 * @return the tt
		 */
		public int getTt() {
			return tt;
		}
		/**
		 * @param tt the tt to set
		 */
		public void setTt(int tt) {
			this.tt = tt;
		}
		/**
		 * @return the app
		 */
		public boolean isApp() {
			return app;
		}
		/**
		 * @param app the app to set
		 */
		public void setApp(boolean app) {
			this.app = app;
		}
		/**
		 * @return the url
		 */
		public String getUrl() {
			return url;
		}
		/**
		 * @param url the url to set
		 */
		public void setUrl(String url) {
			this.url = url;
		}
		/**
		 * @return the wc
		 */
		public String getWc() {
			return wc;
		}
		/**
		 * @param wc the wc to set
		 */
		public void setWc(String wc) {
			this.wc = wc;
		}
		/**
		 * @return the appn
		 */
		public String getAppn() {
			return appn;
		}
		/**
		 * @param appn the appn to set
		 */
		public void setAppn(String appn) {
			this.appn = appn;
		}
		/**
		 * @return the appp
		 */
		public String getAppp() {
			return appp;
		}
		/**
		 * @param appp the appp to set
		 */
		public void setAppp(String appp) {
			this.appp = appp;
		}
		/**
		 * @return the appc
		 */
		public String getAppc() {
			return appc;
		}
		/**
		 * @param appc the appc to set
		 */
		public void setAppc(String appc) {
			this.appc = appc;
		}
		/**
		 * @return the ip
		 */
		public String getIp() {
			return ip;
		}
		/**
		 * @param ip the ip to set
		 */
		public void setIp(String ip) {
			this.ip = ip;
		}
		/**
		 * @return the c
		 */
		public String getC() {
			return c;
		}
		/**
		 * @param c the c to set
		 */
		public void setC(String c) {
			this.c = c;
		}
		/**
		 * @return the dev
		 */
		public String getDev() {
			return dev;
		}
		/**
		 * @param dev the dev to set
		 */
		public void setDev(String dev) {
			this.dev = dev;
		}
		/**
		 * @return the ct
		 */
		public Integer getCt() {
			return ct;
		}
		/**
		 * @param ct the ct to set
		 */
		public void setCt(Integer ct) {
			this.ct = ct;
		}
		/**
		 * @return the ch
		 */
		public int getCh() {
			return ch;
		}
		/**
		 * @param ch the ch to set
		 */
		public void setCh(int ch) {
			this.ch = ch;
		}
		/**
		 * @return the adp
		 */
		public String getAdp() {
			return adp;
		}
		/**
		 * @param adp the adp to set
		 */
		public void setAdp(String adp) {
			this.adp = adp;
		}
		/**
		 * @return the ts
		 */
		public int getTs() {
			return ts;
		}
		/**
		 * @param ts the ts to set
		 */
		public void setTs(int ts) {
			this.ts = ts;
		}
		/**
		 * @return the p
		 */
		public long getP() {
			return p;
		}
		/**
		 * @param p the p to set
		 */
		public void setP(long p) {
			this.p = p;
		}
		/**
		 * @return the g
		 */
		public long getG() {
			return g;
		}
		/**
		 * @param g the g to set
		 */
		public void setG(long g) {
			this.g = g;
		}
		/**
		 * @return the cp
		 */
		public long getCp() {
			return cp;
		}
		/**
		 * @param cp the cp to set
		 */
		public void setCp(long cp) {
			this.cp = cp;
		}
		/**
		 * @return the cpt
		 */
		public int getCpt() {
			return cpt;
		}
		/**
		 * @param cpt the cpt to set
		 */
		public void setCpt(int cpt) {
			this.cpt = cpt;
		}
		/**
		 * @return the ag
		 */
		public long getAg() {
			return ag;
		}
		/**
		 * @param ag the ag to set
		 */
		public void setAg(long ag) {
			this.ag = ag;
		}
		/**
		 * @return the a
		 */
		public long getA() {
			return a;
		}
		/**
		 * @param a the a to set
		 */
		public void setA(long a) {
			this.a = a;
		}
		/**
		 * @return the at
		 */
		public int getAt() {
			return at;
		}
		/**
		 * @param at the at to set
		 */
		public void setAt(int at) {
			this.at = at;
		}
		/**
		 * @return the cr
		 */
		public long getCr() {
			return cr;
		}
		/**
		 * @param cr the cr to set
		 */
		public void setCr(long cr) {
			this.cr = cr;
		}
		public String getAppid() {
			return appid;
		}
		public void setAppid(String appid) {
			this.appid = appid;
		}
		
	}


	public static class ImageSnippet{
		private String url;
		private String c_url;
		private int width;
		private int height;
		private int action;
		private List<String> imp;
		private List<String> clk;
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public String getC_url() {
			return c_url;
		}
		public void setC_url(String c_url) {
			this.c_url = c_url;
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
		public int getAction() {
			return action;
		}
		public void setAction(int action) {
			this.action = action;
		}
		public List<String> getImp() {
			return imp;
		}
		public void setImp(List<String> imp) {
			this.imp = imp;
		}
		public List<String> getClk() {
			return clk;
		}
		public void setClk(List<String> clk) {
			this.clk = clk;
		}		
		
		
	}


}
