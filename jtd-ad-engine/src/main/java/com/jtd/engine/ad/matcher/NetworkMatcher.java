package com.jtd.engine.ad.matcher;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.jtd.engine.ad.Session;
import com.jtd.engine.ad.em.MatchType;
import com.jtd.engine.message.BaiduRealtimeBiddingV26;
import com.jtd.engine.message.BaiduRealtimeBiddingV26.BidRequest.Mobile.WirelessNetworkType;
import com.jtd.engine.message.VamRealtimeBidding.VamRequest;
import com.jtd.engine.message.v1.AVBidRequest;
import com.jtd.engine.message.v1.AdwoBidRequest;
import com.jtd.engine.message.v1.HzBidRequest;
import com.jtd.engine.message.v1.XTraderBidRequest;
import com.jtd.web.constants.NetWorkType;
import com.jtd.web.constants.OperatorType;
import com.jtd.web.model.Campaign;

import Tanx.TanxBidding.BidRequest;
import Tanx.TanxBidding.BidRequest.Mobile;
import Tanx.TanxBidding.BidRequest.Mobile.Device;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月8日
 * @项目名称 dsp-engine
 * @描述 <p>网络类型与运营商匹配</p>
 */
public class NetworkMatcher extends AbstractChannelCampMatcher {
	
//	private static final Log log = LogFactory.getLog(NetworkMatcher.class);
	private final Logger logMyDebug = LogManager.getLogger("myDebugLog");
	private final Logger hzDebugLog = LogManager.getLogger("hzDebugLog");
	
	private static final Map<Long, OperatorType> OP_MCC_MNC_MAP = new HashMap<Long, OperatorType>();
	static {
		OP_MCC_MNC_MAP.put(46000l, OperatorType.CHINAMOBILE); // 中国移动TD 
		OP_MCC_MNC_MAP.put(46001l, OperatorType.CHINAUNICOM); // 中国联通 
		OP_MCC_MNC_MAP.put(46002l, OperatorType.CHINAMOBILE); // 中国移动GSM
		OP_MCC_MNC_MAP.put(46003l, OperatorType.CHINATELECOM);// 中国电信CDMA
	}
	
	@Override
	protected boolean matchADWO(Session session, Campaign camp) {
		logMyDebug.info("adwo:NetworkMatcher--------------------------------------------");
		// PC流量不匹配网络类型和运营商
		if(!session.isInApp()) return true;
		AdwoBidRequest req = session.getReq();
		//"netop": "{\"op\":[1],\"net\":[1]}",
		String netopjson = getDim("netop", camp);
		
		if(StringUtils.isEmpty(netopjson)) return true;

		NetOp netop = JSON.parseObject(netopjson, NetOp.class);
		
		boolean netmatched = false;
		Set<Integer> net = netop.getNet();
		/**net 网络：0:未知，1:WIFI，2:2G，3:3G，4:4G  */
		if(net == null || net.size() == 0 || net.size() == 5) {
			// 5是全选
			netmatched = true;
		} else {
			AdwoBidRequest.Device device = req.getDevice();
			if(device == null) {
				netmatched = net.contains(NetWorkType.UNKNOW.getCode());
			} else {
				//安沃网络类型： 0 未知，1 局域网(PC)，2 WIFI，3 蜂窝网络未知，4 蜂窝网络2G，5 蜂窝网络3G，6蜂窝网络4G
				int connectionType=device.getConnectiontype();
				int netType=0;
				if(connectionType==0){
					netType=0;
				}else if(connectionType == 1 || connectionType==2){
					netType=1;
				}else if(connectionType==3){
					netType=2;
				}else if(connectionType==4){
					netType=3;
				}
				else{
					netType=4;
				}
				netmatched = net.contains(netType);
				if(netmatched) session.addMatched(camp.getId(), MatchType.NETWORK);
			}
		}
		
//		logMyDebug.info("51、netmatched>>xtrader:网络类型-NetworkMatcher>>"+netmatched);
		//如果网络类型没有匹配到，则这个活动不匹配
		if(!netmatched) return false;

		Set<Integer> op = netop.getOp();
		/**op 运营商：0未知，1中国移动，2 中国联通，3中国电信  */
		if(op == null || op.size() == 0 || op.size() == 4) {
			// 4是全选
			return true;
		} else {
			AdwoBidRequest.Device device = req.getDevice();
			if(device == null) {
				return op.contains(OperatorType.UNKNOW.getCode());
			} else {
				/** 获取安沃发送过来的运营商数据 运营商：0未知，1中国移动，2 中国联通，3中国电信 （我们定义给安沃） */
				String o = device.getCarrier();
//				logMyDebug.info("52、device.getCarrier()>>xtrader:运营商类型>>"+o);
				if(o==null||o.length()<=0){
					//如果没有收到运营商数据，则这个条件不限制
					return true;
				}
				long ol=0;
				try{
					ol=Long.parseLong(o);
				}catch(Exception e){}
				if(ol==0){
					return true;
				}
				OperatorType opt = OP_MCC_MNC_MAP.get(ol);
				if(opt == null) {
					return op.contains(OperatorType.UNKNOW.getCode());
				} else {
					boolean t = op.contains(opt.getCode());
					if(t) session.addMatched(camp.getId(), MatchType.OPERATOR);
					return t;
				}
			}
		}
	}

	@Override
	protected boolean matchXTRADER(Session session, Campaign camp) {
//		logMyDebug.info("xtrader:NetworkMatcher--------------------------------------------");
		// PC流量不匹配网络类型和运营商
		if(!session.isInApp()) return true;
		XTraderBidRequest req = session.getReq();
		//"netop": "{\"op\":[1],\"net\":[1]}",
		String netopjson = getDim("netop", camp);
		
		if(StringUtils.isEmpty(netopjson)) return true;

		NetOp netop = JSON.parseObject(netopjson, NetOp.class);
		
		boolean netmatched = false;
		Set<Integer> net = netop.getNet();
		if(net == null || net.size() == 0 || net.size() == 5) {
			// 5是全选
			netmatched = true;
		} else {
			XTraderBidRequest.Device device = req.getDevice();
			if(device == null) {
				netmatched = net.contains(NetWorkType.UNKNOW.getCode());
			} else {
				// XTrader 0—未知，1—Ethernet，2—wifi，3—蜂窝网络，未知代，4—蜂窝网络，2G，5—蜂窝网络，3G，6—蜂窝网络，4G
				int connectionType=device.getConnectiontype();
				int netType=0;
				if(connectionType==0||connectionType==1){
					netType=0;
				}else if(connectionType==2){
					netType=1;
				}else if(connectionType==3||connectionType==4){
					netType=2;
				}else if(connectionType==5){
					netType=3;
				}else{
					netType=4;
				}
				netmatched = net.contains(netType);
				if(netmatched) session.addMatched(camp.getId(), MatchType.NETWORK);
			}
		}
		
//		logMyDebug.info("51、netmatched>>xtrader:网络类型-NetworkMatcher>>"+netmatched);
		//如果网络类型没有匹配到，则这个活动不匹配
		if(!netmatched) return false;

		Set<Integer> op = netop.getOp();
		if(op == null || op.size() == 0 || op.size() == 4) {
			// 4是全选
			return true;
		} else {
			XTraderBidRequest.Device device = req.getDevice();
			if(device == null) {
				return op.contains(OperatorType.UNKNOW.getCode());
			} else {
				//获取零集发送过来的运营商数据
				String o = device.getCarrier();
//				logMyDebug.info("52、device.getCarrier()>>xtrader:运营商类型>>"+o);
				if(o==null||o.length()<=0){
					//如果没有收到运营商数据，则这个条件不限制
					return true;
				}
				long ol=0;
				try{
					ol=Long.parseLong(o);
				}catch(Exception e){}
				if(ol==0){
					return true;
				}
				OperatorType opt = OP_MCC_MNC_MAP.get(ol);
				if(opt == null) {
					return op.contains(OperatorType.UNKNOW.getCode());
				} else {
					boolean t = op.contains(opt.getCode());
					if(t) session.addMatched(camp.getId(), MatchType.OPERATOR);
					return t;
				}
			}
		}
	}
	
	@Override
	protected boolean matchXHDT(Session session, Campaign camp) {
		return false;
	}

	@Override
	protected boolean matchAdBES(Session session, Campaign camp) {
		logMyDebug.info("bes:NetworkMatcher--------------------------------------------");
		// PC流量不匹配网络类型和运营商
		if(!session.isInApp()) return true;

		BaiduRealtimeBiddingV26.BidRequest req= session.getReq();
		//"netop": "{\"op\":[1],\"net\":[1]}",
		String netopjson = getDim("netop", camp);

		if(StringUtils.isEmpty(netopjson)) return true;

		NetOp netop = JSON.parseObject(netopjson, NetOp.class);

		boolean netmatched = false;
		Set<Integer> net = netop.getNet();
		if(net == null || net.size() == 0 || net.size() == 5) {
			// 5是全选
			netmatched = true;
		} else {
			com.jtd.engine.message.BaiduRealtimeBiddingV26.BidRequest.Mobile mobile=req.getMobile();

			if(mobile == null) {
				netmatched = net.contains(NetWorkType.UNKNOW.getCode());
			} else {
				//bes获取网络类型：0-未识别  1-wifi   2-2G  3-3G   4-4G
				WirelessNetworkType wirelessNetworkType=mobile.getWirelessNetworkType();
				int netType=0;
				if( wirelessNetworkType.equals(0)){
					netType=0;
				 }else if(wirelessNetworkType.equals(1)){
					 netType=1;
				 }else if(wirelessNetworkType.equals(2)){
					 netType=2;
				 }else if(wirelessNetworkType.equals(3)){
					 netType=3;
				 }else if(wirelessNetworkType.equals(4)){
					 netType=4;
				 }else{
					 netType=4;
				 }
				netmatched = net.contains(netType);
				if(netmatched) session.addMatched(camp.getId(), MatchType.NETWORK);
			}
		}

//		logMyDebug.info("51、netmatched>>xtrader:网络类型-NetworkMatcher>>"+netmatched);
		//如果网络类型没有匹配到，则这个活动不匹配
		if(!netmatched) return false;

		Set<Integer> op = netop.getOp();
		if(op == null || op.size() == 0 || op.size() == 4) {
			// 4是全选
			return true;
		} else {
			com.jtd.engine.message.BaiduRealtimeBiddingV26.BidRequest.Mobile mobile=req.getMobile();
			if(mobile == null) {
				return op.contains(OperatorType.UNKNOW.getCode());
			} else {
				//获取百度bes发送过来的运营商数据
		        	Long ol= mobile.getCarrierId();
//				logMyDebug.info("52、device.getCarrier()>>xtrader:运营商类型>>"+o);
				if(ol==null||ol<=0){
					//如果没有收到运营商数据，则这个条件不限制
					return true;
				}

				OperatorType opt = OP_MCC_MNC_MAP.get(ol);
				if(opt == null) {
					return op.contains(OperatorType.UNKNOW.getCode());
				} else {
					boolean t = op.contains(opt.getCode());
					if(t) session.addMatched(camp.getId(), MatchType.OPERATOR);
					return t;
				}
			}
		}

	}

	@Override
	protected boolean matchAdView(Session session, Campaign camp) 
	{
//		logMyDebug.info("xtrader:NetworkMatcher--------------------------------------------");
		// PC流量不匹配网络类型和运营商
		if(!session.isInApp()) return true;
		
		AVBidRequest req = session.getReq();
		//"netop": "{\"op\":[1],\"net\":[1]}",
		String netopjson = getDim("netop", camp);
		
		if(StringUtils.isEmpty(netopjson)) return true;

		NetOp netop = JSON.parseObject(netopjson, NetOp.class);
		
		boolean netmatched = false;
		Set<Integer> net = netop.getNet();
		if(net == null || net.size() == 0 || net.size() == 5) {
			// 5是全选
			netmatched = true;
		} else {
			AVBidRequest.Device device = req.getDevice();
			if(device == null) {
				netmatched = net.contains(NetWorkType.UNKNOW.getCode());
			} else {
				// adView获取网络类型：0-未知,1-局域网(PC),2-WIFI,3-蜂窝数据网络-未知,4-蜂窝数据网络–2G,5-蜂窝数据网络–3G,6-蜂窝数据网络–4G
				/** dsp网络：0:未知，1:WIFI，2:2G，3:3G，4:4G  */
				int connectionType=device.getConnectiontype();
				int netType=0;
				if(connectionType==0||connectionType==1||connectionType==3){
					netType=0;
				}else if(connectionType==2){
					netType=1;
				}else if(connectionType==4){
					netType=2;
				}else if(connectionType==5){
					netType=3;
				}else{
					netType=4;
				}
				netmatched = net.contains(netType);
				if(netmatched) session.addMatched(camp.getId(), MatchType.NETWORK);
			}
		}
		
//		logMyDebug.info("51、netmatched>>xtrader:网络类型-NetworkMatcher>>"+netmatched);
		//如果网络类型没有匹配到，则这个活动不匹配
		if(!netmatched) return false;

		Set<Integer> op = netop.getOp();
		if(op == null || op.size() == 0 || op.size() == 4) {
			// 4是全选
			return true;
		} else {
			AVBidRequest.Device device = req.getDevice();
			if(device == null) {
				return op.contains(OperatorType.UNKNOW.getCode());
			} else {
				//获取零集发送过来的运营商数据
				String o = device.getCarrier();
//				logMyDebug.info("52、device.getCarrier()>>xtrader:运营商类型>>"+o);
				if(o==null||o.length()<=0){
					//如果没有收到运营商数据，则这个条件不限制
					return true;
				}
				long ol=0;
				try{
					ol=Long.parseLong(o);
				}catch(Exception e){}
				if(ol==0){
					return true;
				}
				OperatorType opt = OP_MCC_MNC_MAP.get(ol);
				if(opt == null) {
					return op.contains(OperatorType.UNKNOW.getCode());
				} else {
					boolean t = op.contains(opt.getCode());
					if(t) session.addMatched(camp.getId(), MatchType.OPERATOR);
					return t;
				}
			}
		}
	}





	public static class NetOp {
		/** 网络：0:未知，1:WIFI，2:2G，3:3G，4:4G  */
		private Set<Integer> net;
		/** 运营商：0未知，1中国移动，2 中国联通，3中国电信  */
		private Set<Integer> op;
		public Set<Integer> getNet() {
			return net;
		}
		public void setNet(Set<Integer> net) {
			this.net = net;
		}
		public Set<Integer> getOp() {
			return op;
		}
		public void setOp(Set<Integer> op) {
			this.op = op;
		}
	}
}
