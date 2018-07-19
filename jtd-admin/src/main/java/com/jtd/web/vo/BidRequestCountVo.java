package com.jtd.web.vo;

import java.util.HashMap;
import java.util.Map;
/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年8月25日
 * @项目名称 dsp-admin
 * @描述 <p>竞价请求vo</p>
 */
public class BidRequestCountVo {

	private String adplaceid; // 渠道的广告位ID
	private Long channelid; // 渠道ID
	private String size; // 广告位尺寸
	private Integer adtype;
	private Integer currentprice;// 当前底价
	private Integer minprice; // 最低价
	private Integer maxprice; // 最高价
	private Integer adplacetype; // 广告位类型:FIX(0,"固定"),FLOW(1,"浮窗"),DOUBLE(2,"双边对联"),SINGLE(3,"单边对联"),EXTEND(4,"固定可扩展"),HOVER(5,"悬停"),FOLD(6,"折叠"),RP(7,"背投"),OPEN_SCREEN(8,"开屏"),TABLE_SCREEN(9,"插屏"),BANNER(10,"横幅")
	private Long totalFlow; // 广告位总流量
	private Integer currentCountDate;// 当前统计的是哪天
	private Integer countDays;// 一共统计了几天的数据

	private String pageurl; // 广告位所在页面的URL，APP为包名，可能为空
	private String domain; // host部分
	private String websiteType; // 网站类型，用字符串存, 有些ADX可能会不是数值型的
	private Integer screenType;// 屏数类型，0为其他，1为首屏，2为二屏幕

	private String pkname; // 应用包名
	private String appname; // 应用名称
	private String apptype; // 应用类型
	private Integer osType; // 操作系统类型 0为其他， 1为android，2为ios，3为winphone

	public Map<String, String> toMap() {
		Map<String, String> data = new HashMap<String, String>();
		data.put("adplaceid", adplaceid);
		data.put("channelid", channelid.toString());
		data.put("size", size);
		data.put("adtype", adtype.toString());
		data.put("currentprice", currentprice.toString());
		data.put("minprice", minprice.toString());
		data.put("maxprice", maxprice.toString());
		data.put("adplacetype", adplacetype == null ? null : adplacetype.toString());
		data.put("totalFlow", totalFlow.toString());
		data.put("currentCountDate", currentCountDate.toString());
		data.put("countDays", countDays.toString());
		data.put("pageurl", pageurl);
		data.put("domain", domain);
		data.put("websiteType", websiteType);
		data.put("screenType", screenType == null ? null : screenType.toString());
		data.put("pkname", pkname);
		data.put("appname", appname);
		data.put("apptype", apptype);
		data.put("osType", osType == null ? null : osType.toString());
		return data;
	}

	public Integer getCurrentprice() {
		return currentprice;
	}

	public void setCurrentprice(Integer currentprice) {
		this.currentprice = currentprice;
	}

	public String getAdplaceid() {
		return adplaceid;
	}

	public Integer getAdplacetype() {
		return adplacetype;
	}

	public void setAdplacetype(Integer adplacetype) {
		this.adplacetype = adplacetype;
	}

	public void setAdplaceid(String adplaceid) {
		this.adplaceid = adplaceid;
	}

	public Long getChannelid() {
		return channelid;
	}

	public void setChannelid(Long channelid) {
		this.channelid = channelid;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public Integer getAdtype() {
		return adtype;
	}

	public void setAdtype(Integer adtype) {
		this.adtype = adtype;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public Integer getMinprice() {
		return minprice;
	}

	public void setMinprice(Integer minprice) {
		this.minprice = minprice;
	}

	public Integer getMaxprice() {
		return maxprice;
	}

	public void setMaxprice(Integer maxprice) {
		this.maxprice = maxprice;
	}

	public Long getTotalFlow() {
		return totalFlow;
	}

	public void setTotalFlow(Long totalFlow) {
		this.totalFlow = totalFlow;
	}

	public Integer getCurrentCountDate() {
		return currentCountDate;
	}

	public void setCurrentCountDate(Integer currentCountDate) {
		this.currentCountDate = currentCountDate;
	}

	public Integer getCountDays() {
		return countDays;
	}

	public void setCountDays(Integer countDays) {
		this.countDays = countDays;
	}

	public String getPageurl() {
		return pageurl;
	}

	public void setPageurl(String pageurl) {
		this.pageurl = pageurl;
	}

	public String getWebsiteType() {
		return websiteType;
	}

	public void setWebsiteType(String websiteType) {
		this.websiteType = websiteType;
	}

	public Integer getScreenType() {
		return screenType;
	}

	public void setScreenType(Integer screenType) {
		this.screenType = screenType;
	}

	public String getPkname() {
		return pkname;
	}

	public void setPkname(String pkname) {
		this.pkname = pkname;
	}

	public String getAppname() {
		return appname;
	}

	public void setAppname(String appname) {
		this.appname = appname;
	}

	public String getApptype() {
		return apptype;
	}

	public void setApptype(String apptype) {
		this.apptype = apptype;
	}

	public Integer getOsType() {
		return osType;
	}

	public void setOsType(Integer osType) {
		this.osType = osType;
	}

	public static BidRequestCountVo parse(Map<String, String> data) {
		BidRequestCountVo countVo = new BidRequestCountVo();
		String adplaceid = data.get("adplaceid");
		countVo.setAdplaceid(adplaceid);
		Long channelid = Long.valueOf(data.get("channelid"));
		countVo.setChannelid(channelid);
		String size = data.get("size");
		countVo.setSize(size);
		Integer adtype = Integer.valueOf(data.get("adtype"));
		countVo.setAdtype(adtype);
		Integer currentprice = Integer.valueOf(data.get("currentprice"));
		countVo.setCurrentprice(currentprice);
		Integer minprice = Integer.valueOf(data.get("minprice"));
		countVo.setMinprice(minprice);
		Integer maxprice = Integer.valueOf(data.get("maxprice"));
		countVo.setMaxprice(maxprice);
		String adplacetypeStr = data.get("adplacetype");
		Integer adplacetype = null;
		if (adplacetypeStr != null) {
			adplacetype = Integer.valueOf(adplacetypeStr);
		}
		countVo.setAdplacetype(adplacetype);
		Long totalFlow = Long.valueOf(data.get("totalFlow"));
		countVo.setTotalFlow(totalFlow);
		Integer currentCountDate = Integer.valueOf(data.get("currentCountDate"));
		countVo.setCurrentCountDate(currentCountDate);
		Integer countDays = Integer.valueOf(data.get("countDays"));
		countVo.setCountDays(countDays);
		String pageurl = data.get("pageurl");
		countVo.setPageurl(pageurl);
		String domain = data.get("domain");
		countVo.setDomain(domain);
		String websiteType = data.get("websiteType");
		countVo.setWebsiteType(websiteType);
		String screenTypeStr = data.get("screenType");
		Integer screenType = null;
		if (screenTypeStr != null) {
			screenType = Integer.valueOf(screenTypeStr);
		}
		countVo.setScreenType(screenType);
		String pkname = data.get("pkname");
		countVo.setPkname(pkname);
		String appname = data.get("appname");
		countVo.setAppname(appname);
		String apptype = data.get("apptype");
		countVo.setApptype(apptype);
		String osTypeStr = data.get("osType");
		Integer osType = null;
		if (osTypeStr != null) {
			osType = Integer.valueOf(osTypeStr);
		}
		countVo.setOsType(osType);
		return countVo;
	}
}
