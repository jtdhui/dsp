package com.jtd.engine.ad.displayer;


import java.util.Map;

import com.jtd.engine.ad.Session;
import com.jtd.web.model.Ad;
import com.jtd.web.model.Campaign;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年10月25日
 * @项目名称 dsp-engine
 * @描述 <p></p>
 */
public interface AdDisplayer {

	/**
	 * 安沃拼接返回
	 * @param session
	 * @param camp
	 * @param ad
	 * @return
	 */
	public String[] urls4Adwo(Session session,Campaign camp, Ad ad);
	
	/**
	 * AdView html拼接返回
	 * @param session
	 * @param camp
	 * @param ad
	 * @return
	 */
	public String[] htmlAdView(Session session,Campaign camp,Ad ad);
	
	/**
	 * @param userAttr
	 * @param pub
	 * @param pubexe
	 * @param extParams
	 * @return [html, click_through_url]
	 */
	public String[] html4Tanx(Session session, Campaign camp, Ad ad);

	/**
	 * 
	 * @param userAttr
	 * @param pub
	 * @param pubexe
	 * @param extParams
	 * @return
	 */
	public String[] mobileHtml4Tanx(Session session, Campaign camp, Ad ad);

	/**
	 * @param session
	 * @param camp
	 * @param ad
	 * @return
	 */
	public String[] mobileAppHtml4Tanx(Session session, Campaign camp, Ad ad);

	/**
	 * 
	 * @param userAttr
	 * @param pb
	 * @param p
	 * @param extParams
	 * @return
	 */
	public String[] appMraid4Tanx(Session session, Campaign camp, Ad ad);

	/**
	 * 获取BES需要替换的宏
	 * 
	 * @param userAttr
	 * @param pub
	 * @param pubexe
	 * @param extParams
	 * @return
	 */
	public String extData4Bes(Session session, Campaign camp, Ad ad);

	/**
	 * @param userAttr
	 * @param pub
	 * @param pubexe
	 * @param extParams
	 * @return
	 */
	public String[] html4Bes(Session session, Campaign camp, Ad ad);
	
	/**
	 * 零集的拼装返回
	 * @param session
	 * @param camp
	 * @param ad
	 * @return
	 */
	public String[] html4XTrader(Session session, Campaign camp, Ad ad);
	public String[] iframeXTrader(Session session, Campaign camp, Ad ad);
	public String[] urls4Xtrader(Session session,Campaign camp, Ad ad);
	public String[] iframeScriptXTrader(Session session, Campaign camp, Ad ad);

	/**
	 * 万流客
	 * @param session
	 * @param camp
	 * @param ad
	 * @return
	 */
	public String html4VAM(Session session, Campaign camp, Ad ad);
	
	/**
	 * 万流客移动静态创意
	 * @param session
	 * @param camp
	 * @param ad
	 * @return
	 */
	public String[] url4VAMMobile(Session session, Campaign camp, Ad ad);
	
	/**
	 * 万流客视频
	 * @param session
	 * @param camp
	 * @param ad
	 * @return
	 */
	public String[] url4VAMVideo(Session session, Campaign camp, Ad ad);
	
	/**
	 * 互众返回html类型广告
	 * @param session
	 * @param camp
	 * @param ad
	 * @return
	 */
	public String[] htmlHz(Session session, Campaign camp, Ad ad);
	
	/**
	 * 互众，返回app动态广告
	 * @param session
	 * @param camp
	 * @param ad
	 * @return
	 */
	public Map<String,String> jsonHzApp(Session session, Campaign camp, Ad ad,int width,int height);
	
	/**
	 * 互众广告返回非html形式广告
	 * @param session
	 * @param camp
	 * @param ad
	 * @return
	 */
	public String[] extDataHz(Session session, Campaign camp, Ad ad);
}
