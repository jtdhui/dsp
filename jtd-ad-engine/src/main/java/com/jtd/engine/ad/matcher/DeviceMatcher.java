package com.jtd.engine.ad.matcher;

import java.util.Set;

import com.jtd.engine.message.BaiduRealtimeBiddingV26;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.jtd.engine.ad.Session;
import com.jtd.engine.ad.em.MatchType;
import com.jtd.engine.message.v1.AVBidRequest;
import com.jtd.engine.message.v1.AdwoBidRequest;
import com.jtd.engine.message.v1.XTraderBidRequest;
import com.jtd.web.constants.BrandType;
import com.jtd.web.constants.DeviceType;
import com.jtd.web.constants.OSType;
import com.jtd.web.model.Campaign;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月8日
 * @项目名称 dsp-engine
 * @描述 <p></p>
 */
public class DeviceMatcher extends AbstractChannelCampMatcher {

//	private static final Log log = LogFactory.getLog(DeviceMatcher.class);
	private final Logger logMyDebug = LogManager.getLogger("myDebugLog"); 
	private final Logger hzDebugLog = LogManager.getLogger("hzDebugLog");

	
	
	@Override
	protected boolean matchADWO(Session session, Campaign camp) 
	{
		logMyDebug.info("adwo:DeviceMatcher--------------------------------------------");
		/** PC流量目前没有匹配设备和操作系统 */
		if(!session.isInApp()) return true;

		AdwoBidRequest req = session.getReq();

		String devjson = getDim("device", camp);
		/** 如果没有做设备定向则直接通过 */
		if(StringUtils.isEmpty(devjson)) return true;
		
		Dev dev = JSON.parseObject(devjson, Dev.class);
		
		boolean devmatched = false;
		Set<Integer> devs = dev.getDev();
		if(devs == null || devs.size() == 0 || devs.size() == 3) {
			// 3是全选
			devmatched = true;
		} else {
			AdwoBidRequest.Device adwoDev = req.getDevice();
			if(adwoDev == null) {
				devmatched = devs.contains(DeviceType.UNKNOW.getCode());
			} else {
				/** 0 未知，1 iphone，2 android手机，3 ipad，4 WindowsPhone， 5 android平板，6 智能TV  */
				int deviceType = 0;
				try{
					deviceType=adwoDev.getDevicetype();
				}catch(Exception ex){
					deviceType=9999;
				}
				if(deviceType == 9999) {
					devmatched = devs.contains(DeviceType.UNKNOW.getCode());
				} else {
					int devType=0;  //0:未知,1:手机2:平板电脑 
					if(deviceType==1 || deviceType == 2 || deviceType == 4){
						devType=1;
					}else if(deviceType==3 || deviceType == 5){
						devType=2;
					}else{
						devType=0;
					}
					devmatched = devs.contains(devType);
					if(devmatched) session.addMatched(camp.getId(), MatchType.DEVICETYPE);
				}
			}
		}
		
//		logMyDebug.info("54、devmatched>>xtrader:终端类型匹配-DeviceMatcher>>"+devmatched);
		//如果没有匹配上设备类型，则直接返回
		if(!devmatched) return false;
		//用于匹配操作系统类型
		boolean osmatched = false;
		Set<String> os = dev.getOs();
//		logMyDebug.info("55、操作系统类型,页面传递过来的类型>>xtrader:终端类型匹配-DeviceMatcher>>"+JSON.toJSONString(os));
		if(os == null || os.size() == 0 || os.size() == 4) {
			// 4是全选
			osmatched = true;
		} else {
			AdwoBidRequest.Device adwoDev = req.getDevice();
			if(adwoDev == null) {
				osmatched = os.contains(OSType.UNKNOW.getCode());
			} else {
				String adwoOs = adwoDev.getOs();
				if(adwoOs == null) {
					osmatched = os.contains(OSType.UNKNOW.getCode());
				} else {
//					logMyDebug.info("操作系统类型xtraderDev.getOs()>>xtrader:操作系统类型-DeviceMatcher>>"+xtraderOs);
					// 零集 操作系统 "0-Android"/"1-iOS"/"2-WP"("Windows Phone")/"3-Others" (忽略大小写)
					if(adwoOs.toLowerCase().indexOf(OSType.ANDROID.getCode())>=0){
						osmatched = os.contains(OSType.ANDROID.getCode());
						if(osmatched) session.addMatched(camp.getId(), MatchType.OS);
					}else if(adwoOs.toLowerCase().indexOf(OSType.IOS.getCode())>=0){
						osmatched = os.contains(OSType.IOS.getCode());
						if(osmatched) session.addMatched(camp.getId(), MatchType.OS);
					}else if(adwoOs.toLowerCase().indexOf("wp")>=0){
						osmatched = os.contains(OSType.WINDOWSPHONE.getCode());
						if(osmatched) session.addMatched(camp.getId(), MatchType.OS);
					}else{
						osmatched = os.contains(OSType.UNKNOW.getCode());
					}
				}
			}
		}
		
//		logMyDebug.info("56、osmatched>>xtrader:DeviceMatcher>>"+osmatched);
		//如果没有匹配上操作系统，则直接返回，这个活动不会被匹配上。
		if(!osmatched) return false;
		
		//以下是手机品牌的匹配
		Set<String> brand = dev.getBrand();
//		logMyDebug.info("57、页面传递过来的手机品牌>>xtrader:DeviceMatcher>>"+JSON.toJSONString(brand));
		if(brand == null || brand.size() == 0 || brand.size() == 4) {
			// 4是全选
			return true;
		} else {
			AdwoBidRequest.Device adwoDev = req.getDevice();
//			logMyDebug.info("58、零集的device信息-req.getDevice()>>xtrader:DeviceMatcher>>"+req.getDevice());
			if(adwoDev == null) {
				return brand.contains(BrandType.UNKNOW.getBrand());
			} else {
				String br = adwoDev.getMake().toLowerCase();
//				logMyDebug.info("59、零集的手机品牌信息-xtraderDev.getMake()>>xtrader:DeviceMatcher>>"+xtraderDev.getMake());
				if(br == null) {
					return brand.contains(BrandType.UNKNOW.getBrand());
				} else {
					if(brand.contains(br)){
						session.addMatched(camp.getId(), MatchType.BRAND);
						return true;
					} else {
						return false;
					}
				}
			}
		}
	}


	@Override
	protected boolean matchXTRADER(Session session, Campaign camp) {
//		logMyDebug.info("xtrader:DeviceMatcher--------------------------------------------");
		// PC流量目前没有匹配设备和操作系统
		if(!session.isInApp()) return true;

		XTraderBidRequest req = session.getReq();

		String devjson = getDim("device", camp);
		//如果没有做设备定向则直接通过
		if(StringUtils.isEmpty(devjson)) return true;
		
		Dev dev = JSON.parseObject(devjson, Dev.class);
		
		boolean devmatched = false;
		Set<Integer> devs = dev.getDev();
		if(devs == null || devs.size() == 0 || devs.size() == 3) {
			// 3是全选
			devmatched = true;
		} else {
			XTraderBidRequest.Device xtraderDev = req.getDevice();
			if(xtraderDev == null) {
				devmatched = devs.contains(DeviceType.UNKNOW.getCode());
			} else {
				//设备类型，和0—手机，1—平板，2—PC，3—互联网电视
				int deviceType = 0;
				try{
//					logMyDebug.info("53、xtraderDev.getDevicetype()-0—手机，1—平板，2—PC，3—互联网电视>>xtrader:终端类型匹配-DeviceMatcher>>"+xtraderDev.getDevicetype());
					deviceType=xtraderDev.getDevicetype();
				}catch(Exception ex){
					deviceType=9999;
				}
				if(deviceType == 9999) {
					devmatched = devs.contains(DeviceType.UNKNOW.getCode());
				} else {
					int devType=0;
					if(deviceType==0){
						devType=1;
					}else if(deviceType==1){
						devType=2;
					}else{
						devType=0;
					}
					devmatched = devs.contains(devType);
					if(devmatched) session.addMatched(camp.getId(), MatchType.DEVICETYPE);
				}
			}
		}
		
//		logMyDebug.info("54、devmatched>>xtrader:终端类型匹配-DeviceMatcher>>"+devmatched);
		//如果没有匹配上设备类型，则直接返回
		if(!devmatched) return false;
		//用于匹配操作系统类型
		boolean osmatched = false;
		Set<String> os = dev.getOs();
//		logMyDebug.info("55、操作系统类型,页面传递过来的类型>>xtrader:终端类型匹配-DeviceMatcher>>"+JSON.toJSONString(os));
		if(os == null || os.size() == 0 || os.size() == 4) {
			// 4是全选
			osmatched = true;
		} else {
			XTraderBidRequest.Device xtraderDev = req.getDevice();
			if(xtraderDev == null) {
				osmatched = os.contains(OSType.UNKNOW.getCode());
			} else {
				String xtraderOs = xtraderDev.getOs();
				if(xtraderOs == null) {
					osmatched = os.contains(OSType.UNKNOW.getCode());
				} else {
//					logMyDebug.info("操作系统类型xtraderDev.getOs()>>xtrader:操作系统类型-DeviceMatcher>>"+xtraderOs);
					// 零集 操作系统 "0-Android"/"1-iOS"/"2-WP"("Windows Phone")/"3-Others" (忽略大小写)
					if(xtraderOs.toLowerCase().indexOf(OSType.ANDROID.getCode())>=0){
						osmatched = os.contains(OSType.ANDROID.getCode());
						if(osmatched) session.addMatched(camp.getId(), MatchType.OS);
					}else if(xtraderOs.toLowerCase().indexOf(OSType.IOS.getCode())>=0){
						osmatched = os.contains(OSType.IOS.getCode());
						if(osmatched) session.addMatched(camp.getId(), MatchType.OS);
					}else if(xtraderOs.toLowerCase().indexOf("wp")>=0){
						osmatched = os.contains(OSType.WINDOWSPHONE.getCode());
						if(osmatched) session.addMatched(camp.getId(), MatchType.OS);
					}else{
						osmatched = os.contains(OSType.UNKNOW.getCode());
					}
				}
			}
		}
		
//		logMyDebug.info("56、osmatched>>xtrader:DeviceMatcher>>"+osmatched);
		//如果没有匹配上操作系统，则直接返回，这个活动不会被匹配上。
		if(!osmatched) return false;
		
		//以下是手机品牌的匹配
		Set<String> brand = dev.getBrand();
//		logMyDebug.info("57、页面传递过来的手机品牌>>xtrader:DeviceMatcher>>"+JSON.toJSONString(brand));
		if(brand == null || brand.size() == 0 || brand.size() == 4) {
			// 4是全选
			return true;
		} else {
			XTraderBidRequest.Device xtraderDev = req.getDevice();
//			logMyDebug.info("58、零集的device信息-req.getDevice()>>xtrader:DeviceMatcher>>"+req.getDevice());
			if(xtraderDev == null) {
				return brand.contains(BrandType.UNKNOW.getBrand());
			} else {
				String br = xtraderDev.getMake().toLowerCase();
//				logMyDebug.info("59、零集的手机品牌信息-xtraderDev.getMake()>>xtrader:DeviceMatcher>>"+xtraderDev.getMake());
				if(br == null) {
					return brand.contains(BrandType.UNKNOW.getBrand());
				} else {
					if(brand.contains(br)){
						session.addMatched(camp.getId(), MatchType.BRAND);
						return true;
					} else {
						return false;
					}
				}
			}
		}
	}

	
	@Override
	protected boolean matchXHDT(Session session, Campaign camp) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean matchAdBES(Session session, Campaign camp) {
		logMyDebug.info("bes:DeviceMatcher--------------------------------------------");
//		// PC流量目前没有匹配设备和操作系统
		if(!session.isInApp()) return true;

		BaiduRealtimeBiddingV26.BidRequest  req = session.getReq();

		String devjson = getDim("device", camp);
		//如果没有做设备定向则直接通过
		if(StringUtils.isEmpty(devjson)) return true;

		Dev dev = JSON.parseObject(devjson, Dev.class);

		boolean devmatched = false;
		Set<Integer> devs = dev.getDev();
		if(devs == null || devs.size() == 0 || devs.size() == 3) {
			// 3是全选
			devmatched = true;
		} else {
			BaiduRealtimeBiddingV26.BidRequest.Mobile mobile = req.getMobile();
			if(mobile == null) {
				devmatched = devs.contains(DeviceType.UNKNOW.getCode()); //UNKNOW(0, "未知"), MOBILEPHONE(1, "手机"), TABLET(2, "平板电脑"),PC(3,"pc"), INTETV(4,"互联网电视");
			} else {
				/** bes设备类型-0:未知，1:手机 , 2:平板电脑 */
				int deviceType = 0;
				try{
//					logMyDebug.info("53、xtraderDev.getDevicetype()-0:未知，1:手机 , 2:平板电脑>>bes:终端类型匹配-DeviceMatcher>>"+xtraderDev.getDevicetype());
				BaiduRealtimeBiddingV26.BidRequest.Mobile.MobileDeviceType mobileDeviceType=mobile.getDeviceType();
					deviceType =mobileDeviceType.getNumber();
				}catch(Exception ex){
					deviceType=9999;
				}
				if(deviceType == 9999) {
					devmatched = devs.contains(DeviceType.UNKNOW.getCode());
				} else {
					int devType=0;
					if(deviceType==1 ){
						devType=1; //手机
					}else if(deviceType==2){
						devType=2; //平板
					}
					else{
						devType=0;
					}
					devmatched = devs.contains(devType);
					if(devmatched) session.addMatched(camp.getId(), MatchType.DEVICETYPE);
				}
			}
		}
//		logMyDebug.info("54、devmatched>>xtrader:终端类型匹配-DeviceMatcher>>"+devmatched);
		//如果没有匹配上设备类型，则直接返回
		if(!devmatched) return false;



		//用于匹配操作系统类型
		boolean osmatched = false;
		Set<String> os = dev.getOs();
//		logMyDebug.info("55、操作系统类型,页面传递过来的类型>>bes:终端类型匹配-DeviceMatcher>>"+JSON.toJSONString(os));
		if(os == null || os.size() == 0 || os.size() == 4) {
			// 4是全选
			osmatched = true;
		} else {
			BaiduRealtimeBiddingV26.BidRequest.Mobile mobile = req.getMobile();
			if(mobile == null) {
				osmatched = os.contains(OSType.UNKNOW.getCode());
			} else {
				String avOs = mobile.getPlatform().name();
//					logMyDebug.info("操作系统类型xtraderDev.getOs()>>xtrader:操作系统类型-DeviceMatcher>>"+xtraderOs);
					// bes 操作系统 "0-未知 1-IOS 2-安卓 3-windows_phone" (忽略大小写)
					if(avOs.toLowerCase().indexOf(OSType.ANDROID.getCode())>=0){
						osmatched = os.contains(OSType.ANDROID.getCode());
						if(osmatched) session.addMatched(camp.getId(), MatchType.OS);
					}else if(avOs.toLowerCase().indexOf(OSType.IOS.getCode())>=0){
						osmatched = os.contains(OSType.IOS.getCode());
						if(osmatched) session.addMatched(camp.getId(), MatchType.OS);
					}else if(avOs.toLowerCase().indexOf("wp")>=0){
						osmatched = os.contains(OSType.WINDOWSPHONE.getCode());
						if(osmatched) session.addMatched(camp.getId(), MatchType.OS);
					}else{
						osmatched = os.contains(OSType.UNKNOW.getCode());
					}
				}
		}

//		logMyDebug.info("56、osmatched>>xtrader:DeviceMatcher>>"+osmatched);
		//如果没有匹配上操作系统，则直接返回，这个活动不会被匹配上。
		if(!osmatched) return false;

		//以下是手机品牌的匹配
		Set<String> brand = dev.getBrand();
//		logMyDebug.info("57、页面传递过来的手机品牌>>xtrader:DeviceMatcher>>"+JSON.toJSONString(brand));
		if(brand == null || brand.size() == 0 || brand.size() == 4) {
			// 4是全选
			return true;
		} else {
			BaiduRealtimeBiddingV26.BidRequest.Mobile mobile = req.getMobile();
//			logMyDebug.info("58、零集的device信息-req.getDevice()>>xtrader:DeviceMatcher>>"+req.getDevice());
			if(mobile == null) {
				return brand.contains(BrandType.UNKNOW.getBrand());
			} else {
				String br = mobile.getBrand().toLowerCase();
//				logMyDebug.info("59、零集的手机品牌信息-xtraderDev.getMake()>>xtrader:DeviceMatcher>>"+xtraderDev.getMake());
				if(br == null) {
					return brand.contains(BrandType.UNKNOW.getBrand());
				} else {
					if(brand.contains(br)){
						session.addMatched(camp.getId(), MatchType.BRAND);
						return true;
					} else {
						return false;
					}
				}
			}
		}
	}


	@Override
	protected boolean matchAdView(Session session, Campaign camp) 
	{
		logMyDebug.info("adView:DeviceMatcher--------------------------------------------");
		// PC流量目前没有匹配设备和操作系统
		if(!session.isInApp()) return true;

		AVBidRequest req = session.getReq();

		String devjson = getDim("device", camp);
		//如果没有做设备定向则直接通过
		if(StringUtils.isEmpty(devjson)) return true;
		
		Dev dev = JSON.parseObject(devjson, Dev.class);
		
		boolean devmatched = false;
		Set<Integer> devs = dev.getDev();
		if(devs == null || devs.size() == 0 || devs.size() == 3) {
			// 3是全选
			devmatched = true;
		} else {
			AVBidRequest.Device avDev = req.getDevice();
			if(avDev == null) {
				devmatched = devs.contains(DeviceType.UNKNOW.getCode()); //UNKNOW(0, "未知"), MOBILEPHONE(1, "手机"), TABLET(2, "平板电脑"),PC(3,"pc"), INTETV(4,"互联网电视");
			} else {
				 /** adView驱动类型-0:未知，1：iPhone，2：android手机，3：iPad,4:windowsPhone，5:Android平板，6：智能TV，7：PC端个人电脑 */
				int deviceType = 0;
				try{
//					logMyDebug.info("53、xtraderDev.getDevicetype()-0—手机，1—平板，2—PC，3—互联网电视>>xtrader:终端类型匹配-DeviceMatcher>>"+xtraderDev.getDevicetype());
					deviceType=avDev.getDevicetype();
				}catch(Exception ex){
					deviceType=9999;
				}
				if(deviceType == 9999) {
					devmatched = devs.contains(DeviceType.UNKNOW.getCode());
				} else {
					int devType=0;
					if(deviceType==1 || deviceType == 2 || deviceType==4){
						devType=1; //手机
					}else if(deviceType==3 || deviceType == 5){
						devType=2; //平板
					}else if(deviceType == 7)
						devType = 3; //PC
					else{
						devType=0;
					}
					devmatched = devs.contains(devType);
					if(devmatched) session.addMatched(camp.getId(), MatchType.DEVICETYPE);
				}
			}
		}
//		logMyDebug.info("54、devmatched>>xtrader:终端类型匹配-DeviceMatcher>>"+devmatched);
		//如果没有匹配上设备类型，则直接返回
		if(!devmatched) return false;
		
		
		
		//用于匹配操作系统类型
		boolean osmatched = false;
		Set<String> os = dev.getOs();
//		logMyDebug.info("55、操作系统类型,页面传递过来的类型>>xtrader:终端类型匹配-DeviceMatcher>>"+JSON.toJSONString(os));
		if(os == null || os.size() == 0 || os.size() == 4) {
			// 4是全选
			osmatched = true;
		} else {
			AVBidRequest.Device avDev = req.getDevice();
			if(avDev == null) {
				osmatched = os.contains(OSType.UNKNOW.getCode());
			} else {
				String avOs = avDev.getOs();
				if(avOs == null) {
					osmatched = os.contains(OSType.UNKNOW.getCode());
				} else {
//					logMyDebug.info("操作系统类型xtraderDev.getOs()>>xtrader:操作系统类型-DeviceMatcher>>"+xtraderOs);
					// 零集 操作系统 "0-Android"/"1-iOS"/"2-WP"("Windows Phone")/"3-Others" (忽略大小写)
					if(avOs.toLowerCase().indexOf(OSType.ANDROID.getCode())>=0){
						osmatched = os.contains(OSType.ANDROID.getCode());
						if(osmatched) session.addMatched(camp.getId(), MatchType.OS);
					}else if(avOs.toLowerCase().indexOf(OSType.IOS.getCode())>=0){
						osmatched = os.contains(OSType.IOS.getCode());
						if(osmatched) session.addMatched(camp.getId(), MatchType.OS);
					}else if(avOs.toLowerCase().indexOf("wp")>=0){
						osmatched = os.contains(OSType.WINDOWSPHONE.getCode());
						if(osmatched) session.addMatched(camp.getId(), MatchType.OS);
					}else{
						osmatched = os.contains(OSType.UNKNOW.getCode());
					}
				}
			}
		}
		
//		logMyDebug.info("56、osmatched>>xtrader:DeviceMatcher>>"+osmatched);
		//如果没有匹配上操作系统，则直接返回，这个活动不会被匹配上。
		if(!osmatched) return false;
		
		//以下是手机品牌的匹配
		Set<String> brand = dev.getBrand();
//		logMyDebug.info("57、页面传递过来的手机品牌>>xtrader:DeviceMatcher>>"+JSON.toJSONString(brand));
		if(brand == null || brand.size() == 0 || brand.size() == 4) {
			// 4是全选
			return true;
		} else {
			AVBidRequest.Device xtraderDev = req.getDevice();
//			logMyDebug.info("58、零集的device信息-req.getDevice()>>xtrader:DeviceMatcher>>"+req.getDevice());
			if(xtraderDev == null) {
				return brand.contains(BrandType.UNKNOW.getBrand());
			} else {
				String br = xtraderDev.getMake().toLowerCase();
//				logMyDebug.info("59、零集的手机品牌信息-xtraderDev.getMake()>>xtrader:DeviceMatcher>>"+xtraderDev.getMake());
				if(br == null) {
					return brand.contains(BrandType.UNKNOW.getBrand());
				} else {
					if(brand.contains(br)){
						session.addMatched(camp.getId(), MatchType.BRAND);
						return true;
					} else {
						return false;
					}
				}
			}
		}


	}




	public static class Dev {
		private Set<Integer> dev;
		private Set<String> os;
		private Set<String> brand;
		public Set<Integer> getDev() {
			return dev;
		}
		public void setDev(Set<Integer> dev) {
			this.dev = dev;
		}
		public Set<String> getOs() {
			return os;
		}
		public void setOs(Set<String> os) {
			this.os = os;
		}
		public Set<String> getBrand() {
			return brand;
		}
		public void setBrand(Set<String> brand) {
			this.brand = brand;
		}
	}
}
