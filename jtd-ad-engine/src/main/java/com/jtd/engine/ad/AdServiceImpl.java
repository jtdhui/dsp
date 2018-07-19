package com.jtd.engine.ad;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jtd.engine.ad.bidder.Bidder;
import com.jtd.engine.ad.displayer.AdDisplayer;
import com.jtd.engine.ad.em.Adx;
import com.jtd.engine.ad.matcher.AdMatcher;
import com.jtd.engine.ad.matcher.CampMatcher;
import com.jtd.engine.cookie.CookieDataService;
import com.jtd.engine.dao.CampaignDAO;
import com.jtd.engine.dao.HeartBeatDAO;
import com.jtd.engine.dao.OtherDAO;
import com.jtd.engine.message.BaiduRealtimeBiddingV26;
import com.jtd.engine.message.BaiduRealtimeBiddingV26.BidRequest.AdSlot;
import com.jtd.engine.message.BaiduRealtimeBiddingV26.BidRequest.AdSlot.VideoInfo;
import com.jtd.engine.message.BaiduRealtimeBiddingV26.BidRequest.Mobile;
import com.jtd.engine.message.BaiduRealtimeBiddingV26.BidRequest.Mobile.MobileApp;
import com.jtd.engine.message.BaiduRealtimeBiddingV26.BidRequest.Mobile.MobileDeviceType;
import com.jtd.engine.message.v1.*;
import com.jtd.engine.po.Param;
import com.jtd.engine.utils.*;
import com.jtd.engine.utils.Timer;
import com.jtd.engine.utils.TimerTask;
import com.jtd.web.constants.*;
import com.jtd.web.model.Ad;
import com.jtd.web.model.Campaign;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jms.core.JmsTemplate;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author asme 2015年12月24日 下午1:35:33
 *
 */
public class AdServiceImpl implements AdService {

	private static final Log log = LogFactory.getLog(AdServiceImpl.class);
	private final Logger xtarderDebugLog = LogManager.getLogger("xtarderDebugLog");
	
	/** 新汇达通日志  */
	private final Logger xhdtDebugLog = LogManager.getLogger("xhdtDebugLog");
	
	/** 安沃日志  */
	private final Logger adwoDebugLog = LogManager.getLogger("adwoDebuyLog");
	
	/** adView日志 */
	private final Logger adViewDebugLog = LogManager.getLogger("adViewDebuyLog");
	
	/** 百度bes日志 */
	private final Logger besDebugLog = LogManager.getLogger("besDebuyLog");
	

	// 缺省60天过期
	private static final int DEFAULT_EXPIRE = 60 * 60 * 24 * 60;

	// ---------------------------------------
	// 零集的竞价请求、响应、以及空响应次数
	private static final AtomicLong XTRADER_BID_REQ = new AtomicLong(0);
	private static final AtomicLong XTRADER_BID_RSP = new AtomicLong(0);
	private static final AtomicLong XTRADER_EMPTY_RSP = new AtomicLong(0);
	
	/** 新汇达通竞价次数  */
	private static final AtomicLong XHDT_BID_REQ = new AtomicLong(0);   //请求次数
	private static final AtomicLong XHDT_BID_RSP = new AtomicLong(0);   //响应次数
	private static final AtomicLong XHDT_EMPTY_RSP = new AtomicLong(0); //空响应次数
	
	
	/** 安沃竞价次数  */
	private static final AtomicLong ADWO_BID_REQ = new AtomicLong(0);   //请求次数
	private static final AtomicLong ADWO_BID_RSP = new AtomicLong(0);   //响应次数
	private static final AtomicLong ADWO_EMPTY_RSP = new AtomicLong(0); //空响应次数
	
	/** AdView竞价次数  */
	private static final AtomicLong ADVIEW_BID_REQ = new AtomicLong(0);   //请求次数
	private static final AtomicLong ADVIEW_BID_RSP = new AtomicLong(0);   //响应次数
	private static final AtomicLong ADVIEW_EMPTY_RSP = new AtomicLong(0); //空响应次数
	
	
	/** 百度竞价次数  */
	private static final AtomicLong BES_BID_REQ = new AtomicLong(0);   //请求次数
	private static final AtomicLong BES_BID_RSP = new AtomicLong(0);   //响应次数
	private static final AtomicLong BES_EMPTY_RSP = new AtomicLong(0); //空响应次数
	

	private static volatile int bidqps = 0;

	// 用来发送竞价次数
	private JmsTemplate jmsTemplate;

	// 推广活动匹配器
	private CampMatcher campMatcher;

	// 素材匹配器
	private AdMatcher adMatcher;

	// 计算价格浮动值
	private Bidder bidder;

	// 广告播放器
	private AdDisplayer adDisplayer;

	// 活动DAO
	private CampaignDAO campaignDAO;

	// 心跳DAO
	private HeartBeatDAO heartBeatDAO;

	// 记录各个渠道总请求数，及其它相关测试内容
	private OtherDAO otherDao;

	private CookieDataService cookieDataService;


	// 计时器
	private Timer timer;

	private SystemTime systemTime;


	private Monitor monitor;

	/** 三种客户端的广告，如果admin中设置部分渠道，那就把所有渠道都存一份广告，所有渠道都有广告，也就是部分渠道了 */
	// PC,移动和视频的在投活动列表 key: channelId + "_" + size  
	private ConcurrentHashMap<String, CopyOnWriteArrayList<Campaign>> pc = new ConcurrentHashMap<String, CopyOnWriteArrayList<Campaign>>();
	private ConcurrentHashMap<String, CopyOnWriteArrayList<Campaign>> app = new ConcurrentHashMap<String, CopyOnWriteArrayList<Campaign>>();
	private ConcurrentHashMap<String, CopyOnWriteArrayList<Campaign>> wap = new ConcurrentHashMap<String, CopyOnWriteArrayList<Campaign>>();

	// 零集用户数据队列
	private LinkedTransferQueue<String[]> xtraderDataQueue = new LinkedTransferQueue<String[]>();

	// 百度用户数据队列
	private LinkedTransferQueue<String[]> besDataQueue = new LinkedTransferQueue<String[]>();
	
	//是否进行特殊用户匹配
	private static int isCustomMatch=0;

	/**
	 * 初始化
	 */
	public void init() {

		XTRADER_BID_REQ.set(otherDao.getReq("xtrader_req"));
		XTRADER_BID_RSP.set(otherDao.getReq("xtrader_bid"));
		XTRADER_EMPTY_RSP.set(otherDao.getReq("xtrader_emp"));
		
		/** 初始化安沃请求计数器  */
		ADWO_BID_REQ.set(otherDao.getReq("adwo_req"));
		ADWO_BID_RSP.set(otherDao.getReq("adwo_bid"));
		ADWO_EMPTY_RSP.set(otherDao.getReq("adwo_emp"));

		/** 初始化AdView请求计数器  */
		ADVIEW_BID_REQ.set(otherDao.getReq("adView_req"));
		ADVIEW_BID_RSP.set(otherDao.getReq("adView_bid"));
		ADVIEW_EMPTY_RSP.set(otherDao.getReq("adView_emp"));
		
		/** 初始化百度请求计数器  */
		BES_BID_REQ.set(otherDao.getReq("bes_req"));
		BES_BID_RSP.set(otherDao.getReq("bes_bid"));
		BES_EMPTY_RSP.set(otherDao.getReq("bes_emp"));
		
		
		// 启动监控
		monitor = new Monitor(jmsTemplate, pc, app, wap, campaignDAO);
		timer.timing(monitor);

		// 每10秒显示请求数
		timer.timing(new TimerTask() {

			private StringBuilder sb = new StringBuilder();
			private long lxtrareq, lxtrabid, lxtraemp,
					xhdtreq, xhdtbid, xhdtemp,  /** 新汇达通变量  */
					adworeq,adwobid,adwoemp,   /** 安沃变量 */
					adViewreq,adViewbid,adViewemp, /** adView变量 */
					 besreq,besbid,besemp;   /** 百度变量 */

			@Override
			public void run() {

				long req = XTRADER_BID_REQ.get();
				long bid = XTRADER_BID_RSP.get();
				long emp = XTRADER_EMPTY_RSP.get();
				otherDao.setReq("xtrader_req", req + "");// 设置xtrader的总请求次数
				otherDao.setReq("xtrader_bid", bid + "");
				otherDao.setReq("xtrader_emp", emp + "");
				sb.append("XTRADER\tREQ: ").append(req).append("\tQPS: ").append((req - lxtrareq) / 10)
						.append("\tBID: ").append((bid - lxtrabid) / 10).append("\tEMPTY: ")
						.append((emp - lxtraemp) / 10).append("\tsumBid:" + bid + "\tsumEmp:" + emp);
				lxtrareq = req;
				lxtrabid = bid;
				lxtraemp = emp;
				log.info(sb);
				sb.setLength(0);

				log.info("-----------------------------------------------");
				
				//是否进行特殊用户匹配
				isCustomMatch=otherDao.getIsCustom();
				
				
				/** 安沃打印信息  */
				req = ADWO_BID_REQ.get();
				bid = ADWO_BID_RSP.get();
				emp = ADWO_EMPTY_RSP.get();
				otherDao.setReq("adwo_req", req + "");// 设置xtrader的总请求次数
				otherDao.setReq("adwo_bid", bid + "");
				otherDao.setReq("adwo_emp", emp + "");
				sb.append("ADWO\tREQ: ").append(req).append("\tQPS: ").append((req - adworeq) / 10)
						.append("\tBID: ").append((bid - adwobid) / 10).append("\tEMPTY: ")
						.append((emp - adwoemp) / 10).append("\tsumBid:" + bid + "\tsumEmp:" + emp);
				adworeq = req;
				adwobid = bid;
				adwoemp = emp;
				log.info(sb);
				sb.setLength(0);

				log.info("-----------------------------------------------");
				
				/** 新汇达通打印信息 */
				req = XHDT_BID_REQ.get();
				bid = XHDT_BID_RSP.get();
				emp = XHDT_EMPTY_RSP.get();
				otherDao.setReq("xhdt_req", req + "");// 设置xtrader的总请求次数
				otherDao.setReq("xhdt_bid", bid + "");
				otherDao.setReq("xhdt_emp", emp + "");
				sb.append("XHDT\tREQ: ").append(req).append("\tQPS: ").append((req - xhdtreq) / 10)
						.append("\tBID: ").append((bid - xhdtbid) / 10).append("\tEMPTY: ")
						.append((emp - xhdtemp) / 10).append("\tsumBid:" + bid + "\tsumEmp:" + emp);
				xhdtreq = req;
				xhdtbid = bid;
				xhdtemp = emp;
				log.info(sb);
				sb.setLength(0);

				log.info("-----------------------------------------------");
				
				/** AdView打印信息 */
				req = ADVIEW_BID_REQ.get();
				bid = ADVIEW_BID_RSP.get();
				emp = ADVIEW_EMPTY_RSP.get();
				otherDao.setReq("adView_req", req + "");// 设置xtrader的总请求次数
				otherDao.setReq("adView_bid", bid + "");
				otherDao.setReq("adView_emp", emp + "");
				sb.append("ADVIEW\tREQ: ").append(req).append("\tQPS: ").append((req - adViewreq) / 10)
						.append("\tBID: ").append((bid - adViewbid) / 10).append("\tEMPTY: ")
						.append((emp - adViewemp) / 10).append("\tsumBid:" + bid + "\tsumEmp:" + emp);
				adViewreq = req;
				adViewbid = bid;
				adViewemp = emp;
				log.info(sb);
				sb.setLength(0);

				log.info("-----------------------------------------------");
				

				/**百度打印信息*/
				req = BES_BID_REQ.get();
				bid = BES_BID_RSP.get();
				emp = BES_EMPTY_RSP.get();
				otherDao.setReq("bes_req", req + "");// 设置bes的总请求次数
				otherDao.setReq("bes_bid", bid + "");
				otherDao.setReq("bes_emp", emp + "");
				sb.append("BES\tREQ: ").append(req).append("\tQPS: ").append((req - xhdtreq) / 10)
						.append("\tBID: ").append((bid - xhdtbid) / 10).append("\tEMPTY: ")
						.append((emp - xhdtemp) / 10).append("\tsumBid:" + bid + "\tsumEmp:" + emp);
				besreq = req;
				besbid = bid;
				besemp = emp;
				log.info(sb);
				sb.setLength(0);

				log.info("-----------------------------------------------");
			}

			@Override
			public Type type() {
				return Type.INTERVAL;
			}

			@Override
			public long delayOrIntervalMillis() {
				return 10000;
			}

			@Override
			public boolean isTriggerIndependently() {
				return false;
			}
		});

		// 每秒刷新心跳
		timer.timing(new TimerTask() {
			@Override
			public void run() {
				heartBeatDAO.refresh();
			}

			@Override
			public Type type() {
				return Type.INTERVAL;
			}

			@Override
			public long delayOrIntervalMillis() {
				return 1000;
			}

			@Override
			public boolean isTriggerIndependently() {
				return false;
			}
		});
		
		/** 加载adView App分类 */
		Thread xt = new Thread("load adView App category") 
		{
		};
		xt.setDaemon(true);
		xt.start();

		/** 加载besDataQueue队列数据到redis中 */
		Thread xt2 = new Thread("load besDataQueue to redis")
		{
			public void run() {
				for (; !Thread.interrupted();) {
					for (String[] user = besDataQueue.poll(); user != null; user = besDataQueue.poll()) {
						Map<Long,String[]> map = new HashMap<Long,String[]>();
						for(int i=0;i<user.length;i++){
							 if(i!=0){
								 map.put(Long.parseLong(user[i]),new String[]{user[i].toString()});
							 }
						}
						cookieDataService.addDataByBesid(user[0],"2",map);
					}
				}

			}

		};
		xt.setDaemon(true);
		xt.start();

}

	/**
	 * 接收配置请求处理
	 */
	public SystemConfigResponse sysConfig(SystemConfigRequest sysConfigReq){
		SystemConfigResponse sysConfigRsp=new SystemConfigResponse();
		/**
		 * =1 修改是否进行特殊人群匹配的操作
		 * =2 修改是否log日志输出级别
		 * =3 查看 特殊 人群匹配操作标识
		 */
		String opt=sysConfigReq.getOpt();
		if(opt.equals("1")){
			String isCustom=sysConfigReq.getCustomMatcher();
			if(isCustom!=null&&isCustom.length()>0){
				if(isCustom.equals("0")||isCustom.equals("1")){
					otherDao.setIsCustom(isCustom);
					sysConfigRsp.setCode("9999");
					sysConfigRsp.setMsg("设置特殊人群匹配标识成功。");
				}else{
					sysConfigRsp.setCode("1-1");
					sysConfigRsp.setMsg("是否进行人群匹配操作，超出范围");
				}
			}else{
				sysConfigRsp.setCode("9000");
				sysConfigRsp.setMsg("操作项参数opt，超出范围");
			}
		}else if(opt.equals("3")){
			int isCustom=otherDao.getIsCustom();
			sysConfigRsp.setCode("9999");
			sysConfigRsp.setMsg("获取特殊用户匹配标识成功。");
			Map<String,Object> dataMap=new HashMap<String,Object>();
			dataMap.put("renTag", isCustom+"");
			dataMap.put("data", (isCustom==0) ? "不进行特殊用户匹配。" : "特殊用户匹配中。" );
			sysConfigRsp.setData(dataMap);
		}
		return sysConfigRsp;
	}
	/**
	 * 特殊用户请求触发的方法
	 * 
	 * @param adxCkId
	 *            adx cookie id
	 * @param adxIdVer
	 *            adx cookie id 版本
	 * @param session
	 *            session，记录了广告请求的相关信息
	 * @param camps
	 *            符合这次广告请求尺寸的，所有活动
	 * @param ret
	 *            从每个adx方法中传递过来的，每个adx返回的对象 
	 *            key=code value=1,表示不做特殊处理，系统正常投放。
	 *            		   value=2,表示需要对特殊人群，做特殊投放。 
	 *                     value=3,表示系统没有做特殊匹配，但每个adx方法要直接返回。
	 * 
	 *            key=bes		value=BaiduRealtimeBiddingV26.BidResponse.Builder builder
	 *            key=xtrader	value=XTraderBidResponse rsp = new XTraderBidResponse();
	 *            key=tnax		value=BidResponse.Builder builder 
	 *            key=vam		value=VamResponse.Builder
	 *            key=adin		value=
	 * 
	 *            key=pstart value=处理的开始时间
	 * 
	 *            key=besReq 		value=BaiduRealtimeBiddingV26.BidRequest bidReq
	 *            key=xtraderReq 	value=XTraderBidRequest
	 *            key=tanxReq 		 value=BidRequest bidReq
	 *            key=vamReq 		value=VamRequest bidReq
	 *            key=adin value=
	 * @param size
	 *            广告尺寸
	 */
	public void customCookieMatcher(String adxCkId, String adxIdVer, Session session, List<Campaign> camps,
			Map<String, Object> ret, String size) {
		if (adxCkId == null) {
			ret.put("code", "1");
			return;
		}
		// 判断是否为pc端，如果不是则直接返回
		if (session.isInApp()) {
			ret.put("code", "1");
			return;
		}
		Map<String, String> customInfo = null;
		// 判断渠道，根据不同的渠道做不同的操作
		// 获取这次请求 adx的用户唯一表示，根据该标识从不同的adx映射缓存中取出对应的 dspcookieid，所对应的值。
		// 从CustomCookie缓存中取出 key=dspCookieid,value={pid,广告主id},{cid,活动id}
		if (session.getAdx() == Adx.BES) {
			// 根据adxCkId取出dspid
			String dspCkId = cookieDataService.getCidByBesid(adxCkId, adxIdVer);
			// 在bes缓存中没有找到用户id
			if (dspCkId == null || dspCkId.length() <= 0) {
				ret.put("code", "1");
				return;
			}
			// 使用dspid从 customCookiePool 中取出特殊用户
			customInfo = cookieDataService.getCustomInfo(dspCkId);
			if (customInfo == null) {
				ret.put("code", "1");
				return;
			}
		} else if (session.getAdx() == Adx.XTRADER) {
			// 根据adxCkId取出dspid
			String dspCkId = cookieDataService.getCidByXtraderid(adxCkId);
			// 在bes缓存中没有找到用户id
			if (dspCkId == null || dspCkId.length() <= 0) {
				ret.put("code", "1");
				return;
			}
			// 使用dspid从 customCookiePool 中取出特殊用户
			customInfo = cookieDataService.getCustomInfo(dspCkId);
			if (customInfo == null) {
				ret.put("code", "1");
				return;
			}

		} else if (session.getAdx() == Adx.TANX) {
			// 根据adxCkId取出dspid
			String dspCkId = cookieDataService.getCidByTanxid(adxCkId, Integer.parseInt(adxIdVer));
			// 在bes缓存中没有找到用户id
			if (dspCkId == null || dspCkId.length() <= 0) {
				ret.put("code", "1");
				return;
			}
			// 使用dspid从 customCookiePool 中取出特殊用户
			customInfo = cookieDataService.getCustomInfo(dspCkId);
			if (customInfo == null) {
				ret.put("code", "1");
				return;
			}

		} else if (session.getAdx() == Adx.VAM) {
			// 根据adxCkId取出dspid
			String dspCkId = cookieDataService.getCidByPartercid(String.valueOf(Adx.VAM),
					adxCkId + "_" + adxIdVer);

			// 在bes缓存中没有找到用户id
			if (dspCkId == null || dspCkId.length() <= 0) {
				ret.put("code", "1");
				return;
			}
			// 使用dspid从 customCookiePool 中取出特殊用户
			customInfo = cookieDataService.getCustomInfo(dspCkId);
			if (customInfo == null) {
				ret.put("code", "1");
				return;
			}

		} else if (session.getAdx() == Adx.HZ) {
			// 根据adxCkId取出dspid
			String dspCkId = cookieDataService.getCidByPartercid(String.valueOf(Adx.HZ),adxCkId);

			// 在一般adx缓存中没有找到用户id
			if (dspCkId == null || dspCkId.length() <= 0) {
				ret.put("code", "1");
				return;
			}
			// 使用dspid从 customCookiePool 中取出特殊用户
			customInfo = cookieDataService.getCustomInfo(dspCkId);
			if (customInfo == null) {
				ret.put("code", "1");
				return;
			}
		}

		String pid = null;
		String cid = null;

		try {
			pid = customInfo.get("pid");
			if (pid == null || pid.length() <= 0) {
				pid = "0";
			}
		} catch (Exception ex) {
			pid = "0";
		}

		try {
			cid = customInfo.get("cid");
			if (cid == null || cid.length() <= 0) {
				cid = "0";
			}
		} catch (Exception ex) {
			cid = "0";
		}
		if(pid.equals("0")&&cid.equals("0")){
			if (customInfo == null) {
				ret.put("code", "1");
				return;
			}
		}

		// 存放广告活动的list，就是筛选出来的广告活动的集合
		List<Campaign> passcamps = new ArrayList<Campaign>();
		// 存放广告的容器，就是所有筛选出来的广告的集合
		Map<Long, List<Ad>> passads = new HashMap<Long, List<Ad>>();

		if (cid.equals("0") && pid.equals("0")) {
			ret.put("code", "1");
			return;
		}

		if (!cid.equals("0")) {
			// 如果活动id不为 “0” 则筛选活动
			for (Campaign camp : camps) {
				if (camp.getId() == Long.parseLong(cid)) {
					// 从广告活动中获取，这个广告活动下的所有广告
					Map<String, List<Ad>> ads = camp.getAds();
					if (ads == null || ads.size() == 0) {
						break;
					}
					List<Ad> ad = ads.get(size);
					if (ad == null || ad.size() == 0) {
						break;
					}
					passcamps.add(camp);
					passads.put(camp.getId(), ad);
					break;
				}
			}
		}

		if (!pid.equals("0")) {
			// 如果广告主id不为 “0” 并且 活动为 “0” 时，按照广告主筛选活动
			for (Campaign camp : camps) {
				if (camp.getPartnerId() == Long.parseLong(pid)) {
					// 从广告活动中获取，这个广告活动下的所有广告
					Map<String, List<Ad>> ads = camp.getAds();
					if (ads == null || ads.size() == 0)
						continue;
					List<Ad> ad = ads.get(size);
					if (ad == null || ad.size() == 0)
						continue;
					passcamps.add(camp);
					passads.put(camp.getId(), ad);
				}
			}
		}
		// 给出出价，拼装返回
		customCookieDisplay(session, passcamps, passads, ret, size);
	}


	/**
	 * 零集竞价
	 */
	@Override
	public XTraderBidResponse bidXTrader(XTraderBidRequest bidReq) {
		// 前期记录零集的日志
		// xtarderDebugLog.info(bidReq);
		
		// 零集竞价请求次数+1
		XTRADER_BID_REQ.getAndIncrement();
		// 准备好response对象，作为返回使用
		XTraderBidResponse rsp = new XTraderBidResponse();
		// 拿到零集竞价时发出的请求id
		String id = bidReq.getId();
//		xtarderDebugLog.info("1、零集请求id>>xtrader>>" + id);
		// 拿到竞价时用户数据
		XTraderBidRequest.User user = bidReq.getUser();
//		xtarderDebugLog.info("2、user>>xtrader>>" + JSON.toJSONString(user));
		// 拿到竞价时广告位数据
		XTraderBidRequest.Imp imp = bidReq.getImp().get(0);
//		xtarderDebugLog.info("3、imp>>xtrader>>" + JSON.toJSONString(imp));
		// 拿到竞价时设备数据
		XTraderBidRequest.Device dev = bidReq.getDevice();
//		xtarderDebugLog.info("4、dev>>dev>>" + JSON.toJSONString(dev));
		// 拿到竞价时媒体数据
		XTraderBidRequest.Site site = bidReq.getSite();
//		xtarderDebugLog.info("5、site>>site>>" + JSON.toJSONString(site));
		// 拿到竞价时app数据
		XTraderBidRequest.App xtraderApp = bidReq.getApp();
//		xtarderDebugLog.info("6、xtraderApp>>xtrader>>" + JSON.toJSONString(xtraderApp));

		int hasWinnotice = -1;
		try {
			hasWinnotice = imp.getExt().getHas_winnotice();
		} catch (Exception e) {
		}
//		xtarderDebugLog.info("7、xtraderApp>>hasWinnotice>>" + JSON.toJSONString(hasWinnotice));
		// if(hasWinnotice==-1){
		// //如果零集没有发送这个值，那么证明证明这次请求过来，
		// XTRADER_EMPTY_RSP.getAndIncrement();
		// return rsp;
		// }

		// 拿到广告位展示类型
		/**
		 * 1 PC端网页banner 2 PC端网页video 3 PC端网页背投 4 PC端网页视频暂停 5 PC端网页弹窗 6
		 * PC客户端banner 7 PC客户端video 8 PC客户端弹窗 9 PC端网页视频悬浮 10 PC端网页端信息流 11
		 * 移动WAP端banner 12 移动WAP端video 13 移动WAP端信息流 14 移动APP端横幅 15 移动APP端开屏 16
		 * 移动APP端插屏 17 移动APP端video 18 移动端视频暂停 19 移动APP端应用墙 20 移动APP端信息流
		 */
		int showType = 0;
		try {
			showType = imp.getExt().getShowType();
		} catch (Exception e) {
			// 如果showType没有值，则直接返回
			XTRADER_EMPTY_RSP.getAndIncrement();
			return rsp;
		}
//		xtarderDebugLog.info("8、imp->ext->showType>>xtrader>>" + showType);
		// 从设备中取出UA
		// String ua = dev.getUa();
		// 判断是否进行cookie映射
		boolean isCookieMapping = false;
		if (user != null) {
			String[] users = null;
			// 决定是否进行cookie映射
			isCookieMapping = cookieDataService.getCidByXtraderid(user.getId()) == null;
//			xtarderDebugLog.info("9、isCookieMapping>>xtrader>>" + isCookieMapping);
			// 将用户信息存储到cookie中
			XTraderBidRequest.User.Ext userExt = user.getExt();
			if (userExt != null) {
				// 取出零集在竞价请求时自带的用户标签数据
				List<String> models = userExt.getModels();
				if (models != null && models.size() > 0) {
					users = new String[models.size() + 1];
					users[0] = user.getId();
					for (int i = 0; i < models.size(); i++) {
						users[i + 1] = models.get(i);
					}
				}
			}
//			xtarderDebugLog.info("10、users>>xtrader>>" + JSON.toJSONString(users));
			// 将用户标签数据放进队列中，使用单独的线程，将标签数据写入到redis中
			if (users != null) {
				xtraderDataQueue.tryTransfer(users);
			}
		}
//		xtarderDebugLog.info("11、isCookieMapping>>xtrader>>" + isCookieMapping);

		// 设置response中的请求id
		rsp.setId(id);
		// 设置response中的bidid，竞价id。这个id零集没有特殊要求，所有直接使用请求id代替。
		rsp.setBidid(id);
		// 设置出价对象数组
		List<XTraderBidResponse.Seatbid> seatbids = new ArrayList<XTraderBidResponse.Seatbid>();
		rsp.setSeatbid(seatbids);

		// 从竞价请求中，拿到banner对象
		XTraderBidRequest.Imp.Banner banner = imp.getBanner();
		// 从竞价请求中，拿到Video对象
		XTraderBidRequest.Imp.Video video = imp.getVideo();

		// 拿到广告位曝光的低价
		long minCpmPrice = (long) (imp.getBidfloor());
//		xtarderDebugLog.info("12、minCpmPrice>>xtrader>>" + minCpmPrice);

		int devicetype = -1;
		try {
			devicetype = dev.getDevicetype();
		} catch (Exception e) {
		}

		// 判断是移动端还是pc端,0为pc，1为移动，2为wap
		int pcOrMobileOrWap = 0;
		if (devicetype == -1) {
			if (showType == 1 || showType == 2 || showType == 3 || showType == 4 || showType == 5 || showType == 6
					|| showType == 7 || showType == 8 || showType == 9) {
				pcOrMobileOrWap = 0;
			} else if (showType == 11 || showType == 12 || showType == 13) {
				pcOrMobileOrWap = 2;
			} else if (showType == 14 || showType == 15 || showType == 16 || showType == 17 || showType == 18
					|| showType == 19 || showType == 20) {
				pcOrMobileOrWap = 1;
			} else {
				XTRADER_EMPTY_RSP.getAndIncrement();
				return rsp;
			}
		} else if (devicetype == 0 || devicetype == 1) {
			/** 一定是移动端 **/
			if (showType == 11 || showType == 12 || showType == 13) {
				pcOrMobileOrWap = 2;
			} else if (showType == 14 || showType == 15 || showType == 16 || showType == 17 || showType == 18
					|| showType == 19 || showType == 20) {
				pcOrMobileOrWap = 1;
			} else {
				XTRADER_EMPTY_RSP.getAndIncrement();
				return rsp;
			}
		} else if (devicetype == 2) {
			/** 一定是pc **/
			if (showType == 1 || showType == 2 || showType == 3 || showType == 4 || showType == 5 || showType == 6
					|| showType == 7 || showType == 8 || showType == 9) {
				pcOrMobileOrWap = 0;
			} else {
				XTRADER_EMPTY_RSP.getAndIncrement();
				return rsp;
			}
		} else if (devicetype == 3) {
			/** 一定是互联网电视，暂时不支持这种终端 **/
			XTRADER_EMPTY_RSP.getAndIncrement();
			return rsp;
		} else {
			XTRADER_EMPTY_RSP.getAndIncrement();
			return rsp;
		}

//		xtarderDebugLog.info("13、pcOrMobileOrWap>>xtrader,0为pc，1为移动，2为wap>>" + pcOrMobileOrWap);
		// if(null!=site&&null==xtraderApp){
		// if(showType==1||showType==2||showType==3||showType==4||showType==5||showType==6||showType==7||showType==8||showType==9){
		// pcOrMobileOrWap=0;
		// }else if(showType==11||showType==12||showType==13){
		// pcOrMobileOrWap=2;
		// }
		// }else if(null!=xtraderApp&&null==site){
		// if(showType==14||showType==15||showType==16||showType==17||showType==18||showType==19||showType==20){
		// pcOrMobileOrWap=1;
		// }else if(showType==11||showType==12||showType==13){
		// pcOrMobileOrWap=2;
		// }
		// }

		// 用于判断是否为banner广告,判断是视频广告还是非视频广告
		boolean isBanner = banner != null;
		// 拿到广告位的宽和高
		int width = isBanner ? banner.getW() : video.getW();
		int height = isBanner ? banner.getH() : video.getH();
//		xtarderDebugLog.info("14、isBanner>>xtrader>>" + isBanner);
		String size = width + "x" + height;
//		xtarderDebugLog.info("15、size>>xtrader>>" + size);
		// 返回的是动态广告还是静态广告
		boolean xtraderUseHtml = false;
		boolean isMimes = false;
		if (isBanner) {
			/** 是banner请求 **/
			List<String> mimes = banner.getMimes();
//			xtarderDebugLog.info("16、size>>xtrader>>" + size);
			if (mimes != null && mimes.size() > 0) {
				isMimes = true;
				for (String m : mimes) {
					if (m.equals("text/html")) {
						xtraderUseHtml = true;
						break;
					}
				}
			}
		} else {
			List<String> mimes = video.getMimes();
			if (mimes != null && mimes.size() > 0) {
				isMimes = true;
				for (String m : mimes) {
					if (m.equals("text/html")) {
						xtraderUseHtml = true;
						break;
					}
				}
			}
		}
		if (!isMimes) {
//			xtarderDebugLog.info("17、isMimes,为空>>xtrader>>" + isMimes);
			/** 表示无论是banner还是video都没有这个参数，返回空 **/
			// 如果非以上广告类型则直接返回空响应
			XTRADER_EMPTY_RSP.getAndIncrement();
			return rsp;
		}

		// 创建一个session对象
		Session session = new Session();
		session.setCookieMapping(isCookieMapping);
		session.setAdx(Adx.XTRADER);
		session.setBid(bidReq.getId());
		session.setUserip(dev.getIp());
		session.setReq(bidReq);
		session.setAdp(imp.getTagid());
		// 从xtrader发送过来的数据中找到，广告位的低价，并设置到session中
		session.setBidFloor((int) minCpmPrice);

		List<Campaign> camps = null;
		// 0为pc，1为移动，2为wap
		if (pcOrMobileOrWap == 1) {
			/*** 移动端广告 ****/
			session.setInApp(true);
			// 获取媒体上能够展示的广告位类型
			// 判断媒体可接受的广告展示类型
			if (showType == 15 || showType == 16) {
				session.setAdType(AdType.APP_FULLSCREEN);
			} else if (showType == 14) {
				session.setAdType(AdType.APP_BANNER);
			} else if (showType == 17) {
				session.setAdType(AdType.APP_VIDEO);
			} else if (showType == 18) {
				session.setAdType(AdType.APP_VIDEO_PAUSE);
			} else if (showType == 20) {
				// 原生信息流广告
				session.setAdType(AdType.APP_MESSAGE);
			} else {
//				xtarderDebugLog.info("18、showType,不是app范围内的值>>xtrader>>" + showType);
				// 如果非以上广告类型则直接返回空响应
				XTRADER_EMPTY_RSP.getAndIncrement();
				return rsp;
			}
			// 获取应用对象信息
			if (null != xtraderApp) {
				// 取出xtrader发过来的应用的分类数据
				String appcatg = "";
				if (xtraderApp.getCat() != null && xtraderApp.getCat().size() > 0) {
					appcatg = xtraderApp.getCat().get(0);
				}
//				xtarderDebugLog.info("19、appcatg>>xtrader>>" + appcatg);
				// 从xtrader发过来的数据中，取出应用的唯一标识，设置到session中
				// APP应用的包名称或bundleID,如果应用是android则为packageName，如果是ios则为Bundle
				session.setAppPackageName(xtraderApp.getBundle());
				// 将app应用的分类数据设置到session中
				session.setAppCatg(appcatg);
			}
			// 根据 渠道id_宽*高 为key 从app广告列表中取出对应的广告活动
			camps = app.get(Adx.XTRADER.channelId() + "_" + size);

		} else if (pcOrMobileOrWap == 2) {
			session.setInApp(true);
			/** 如果广告请求是wap **/
			/**
			 * 11 移动WAP端banner 12 移动WAP端video 13 移动WAP端信息流
			 */
			// 如果此次广告请求是wap类型
			if (showType == 11) {
				session.setAdType(AdType.WAP);
			} else if (showType == 12) {
				session.setAdType(AdType.WAP_VIDEO);
			} else if (showType == 13) {
				session.setAdType(AdType.WAP_VIDEO_PAUSE);
			} else {
//				xtarderDebugLog.info("20、showType,不是wap范围内的值>>xtrader>>" + showType);
				XTRADER_EMPTY_RSP.getAndIncrement();
				return rsp;
			}

			// 设置当前广告为所在的页面
			String pageUrl = "";
			try {
				pageUrl = site.getPage();
			} catch (Exception ex) {
				pageUrl = "";
			}
			session.setPageUrl(pageUrl);
			// 从bes请求时发过来的数据中，取出网站分类信息，并设置到session中
			String catg = "";
			if (null != xtraderApp && null != xtraderApp.getCat() && xtraderApp.getCat().size() > 0) {
				catg = xtraderApp.getCat().get(0);
			} else if (null != site && null != site.getCat() && site.getCat().size() > 0) {
				catg = site.getCat().get(0);
			} else {
				catg = "";
			}
//			xtarderDebugLog.info("21、wap catg>>xtrader>>" + catg);
			session.setWebCatg(catg);
			// 根据 渠道id_宽*高 为key从wap map中去符合这个尺寸的wap广告
			camps = wap.get(Adx.XTRADER.channelId() + "_" + size);

		} else if (pcOrMobileOrWap == 0) {
			session.setInApp(false);
			/** 如果广告请求是pc **/
			// 1 PC端网页banner
			// 2 PC端网页video
			// 3 PC端网页背投
			// 4 PC端网页视频暂停
			// 5 PC端网页弹窗
			// 6 PC客户端banner
			// 7 PC客户端video
			// 8 PC客户端弹窗
			// 9 PC端网页视频悬浮
			// 10 PC端网页端信息流
			if (showType == 1 || showType == 3 || showType == 5 || showType == 6 || showType == 8) {
				session.setAdType(AdType.PC_BANNER);
			} else if (showType == 2 || showType == 7 || showType == 9) {
				session.setAdType(AdType.PC_VIDEO);
			} else if (showType == 4) {
				session.setAdType(AdType.PC_VIDEO_PAUSE);
			} else {
//				xtarderDebugLog.info("22、showType,不是pc范围内的值>>xtrader>>" + showType);
				// 如果非以上广告类型则直接返回空响应
				XTRADER_EMPTY_RSP.getAndIncrement();
				return rsp;
			}
			try {
				session.setPageReferer(site.getRef());
			} catch (Exception e) {
			}
			try {
				// 从xtrader请求数据中获取，广告为所在的url地址
				session.setPageUrl(site.getPage());
			} catch (Exception e) {
			}

			// 从请求数据中获取，网站的分类数据，设置到session的webcatg
			String catg = "";
			if (null != site && null != site.getCat() && site.getCat().size() > 0) {
				catg = site.getCat().get(0);
			} else {
				catg = "";
			}
//			xtarderDebugLog.info("23、pc catg>>xtrader>>" + catg);
			session.setWebCatg(catg);
			// 根据 渠道id_宽*高 为key 从pc map中取出所有适合这个尺寸广告
			camps = pc.get(Adx.XTRADER.channelId() + "_" + size);

			// 获取xtrader的用户标识列表
		}

		// 如果camps中没有数据，说明没有找到适合的广告，则把处理的时长返回，并设置百度的空响应变量+1；返回构建的对象。
		if (camps == null || camps.size() == 0) {
			XTRADER_EMPTY_RSP.getAndIncrement();
			return rsp;
		}
		
		session.setAppId(imp.getTagid());
		/*******************************************************/
		//isCustomMatch非0时标识需要做特殊用户 广告匹配
		if(isCustomMatch!=0){
			boolean fag=true;
			//特殊人群匹配调用
			//特殊参数
			Map<String, Object> retCustom=new HashMap<String,Object>();
			retCustom.put("xtraderReq", bidReq);
			retCustom.put("xtraderUseHtml",xtraderUseHtml);
			retCustom.put("hasWinnotice",hasWinnotice);
			retCustom.put(Adx.XTRADER.channelName(),rsp);
			try{
				customCookieMatcher(user.getId(), "0", session, camps, retCustom, size);
			}catch(Exception ex){
				log.error("做特殊人群匹配错误: " + ex.getMessage());
				fag=false;
			}
			String code="1";
			try{
				code=retCustom.get("code").toString();
			}catch(Exception ex){
				code="1";
			}
			if(fag){
				if(code.equals("1")){
					//系统不做特殊用户匹配，系统正常匹配
				}else if(code.equals("2")){
					//系统做特殊用户匹配，程序返回
					try{
						return rsp;
					}catch(Exception ex){
						log.error("做特殊人群匹配错误:" + ex.getMessage());
					}
				}else if(code.equals("3")){
					return rsp;
				}
			}
		}
		/*******************************************************/	

//		xtarderDebugLog.info("24、活动——camps:>>xtrader>>" + camps.size());

		// 筛选计划和广告
		// 存放广告活动的list，就是筛选出来的广告活动的集合
		List<Campaign> passcamps = new ArrayList<Campaign>();
		// 存放广告的容器，就是所有筛选出来的广告的集合
		Map<Long, List<Ad>> passads = new HashMap<Long, List<Ad>>();
		// 将备选的广告活动，从camps中取出（camps，仅仅是通过渠道和尺寸做的最基本的筛选）
		for (Campaign camp : camps) {
			// 这里的match是检查广告主，广告活动的状态
			if (campMatcher.match(session, camp)) {
				// 从广告活动中获取，这个广告活动下的所有广告
				Map<String, List<Ad>> ads = camp.getAds();
				if (ads == null || ads.size() == 0)
					continue;
				List<Ad> ad = ads.get(size);
				if (ad == null || ad.size() == 0)
					continue;

				List<Ad> passed = new ArrayList<Ad>();
				// 匹配所有广告
				for (Ad a : ad) {
					if (adMatcher.match(session, a))
						passed.add(a);
				}
				if (passed.size() > 0) {
					passcamps.add(camp);
					passads.put(camp.getId(), passed);
				}
			}
		}
//		xtarderDebugLog.info("25、Matcher后活动:>>xtrader>>" + passcamps.size());
		// 如果没有备选的广告活动，则直接给百度返回空响应
		if (passcamps.size() == 0) {
			XTRADER_EMPTY_RSP.getAndIncrement();
			return rsp;
		}

		// 挑选活动
		Campaign camp = null;
		float score = Float.MAX_VALUE;
		// 经过下面循环，找出所有符合投放条件的广告活动，出价最低的广告活动
		for (Campaign pcamp : passcamps) {
			// 返回活动的竞价次数
			float bid = (float) monitor.getCampBid(Adx.XTRADER, pcamp);
			// 比如：有两个活动，一个活动的单价是10快，一个活动的单价是5快，那么讲过下面规则就是，出价高的永远比出价低的，投出广告的次数要多
			// 假设，每次经过过滤后，剩下的广告活动都是
			// A、B，A的价格是5块，B的价格是10块，那么第一次使用竞价次数，分别除以价格，得到的都是0
			// 那么就先第一个活动，第二次的时候，1/5=0.2,1/10=0.1,则选B活动
			// 第三次，1/5=0.2,2/10-0.2,选择A活动
			// 第4次，2/5=0.4，2/10=0.3,选择B活动
			// 第5次，2/5=0.4,3/10=0.4,选择A活动
			// 以此类推下去，保证出价高的活动，能够投放的次数多，而出价低的不会一直投不出去。
			long cpmprice = campaignDAO.getCpmPrice(pcamp);
			if (cpmprice == 0l)
				continue;
			float s = bid / (float) cpmprice;
			if (s < score) {
				score = s;
				camp = pcamp;
			}
		}

		if (camp == null) {
			XTRADER_EMPTY_RSP.getAndIncrement();
			return rsp;
		}
//		xtarderDebugLog.info("26、挑选出的活动:>>xtrader>>" + camp.getId());

		// 挑选广告
		List<Ad> ads = passads.get(camp.getId());
//		xtarderDebugLog.info("27、挑选出的广告:>>xtrader>>" + JSON.toJSONString(ads));
		if (ads == null || ads.size() == 0) {
			XTRADER_EMPTY_RSP.getAndIncrement();
			return rsp;
		}
		// 找出广告活动中，参与竞价次数最少的广告
		Ad ad = null;
		int bidtimes = Integer.MAX_VALUE;
		for (Ad a : ads) {
			int bid = monitor.getAdBid(Adx.XTRADER, a);
			if (bid < bidtimes) {
				bidtimes = bid;
				ad = a;
			}
		}

		// 出价，根据命中的筛选条件，计算出要提升价格的百分比
		float incre = bidder.bid(session, camp, ad);
		// 获取价格因子
		float priceFactor = campaignDAO.getCampPriceFactor(camp.getId());
		// 这次请求的最高限价价格，这个价格实在match过程中在，bidFloorMatcher时产生的。
		int maxBidPrice = session.getMaxBidPrice(camp.getId());
		// 这次广告请求的底价
		int bidFloor = session.getBidFloor();
		// 这次请求的真正竞价价格
		int bidPrice = bidFloor + (maxBidPrice - bidFloor) * 5 / 10;
		bidPrice = (int) (bidPrice * (1.0f + incre) * priceFactor);
		if (bidPrice > maxBidPrice)
			bidPrice = maxBidPrice;
		if (bidPrice < bidFloor)
			bidPrice = bidFloor + 1;

		// xtarderDebugLog.info("系统最终的给出的竞价价格>>>>>>>>>"+bidPrice);
		session.setBidPrice(bidPrice);
//		xtarderDebugLog.info("28、竞价价格:>>xtrader>>" + bidPrice + ">>活动id:" + camp.getId());
	
		// 判断是否可以使用html方式还是非html方式返回
		// 填充响应
		if (xtraderUseHtml) {
//			xtarderDebugLog.info("29、xtraderUseHtml:>>xtrader>>" + xtraderUseHtml);
			// 获取html
			String[] ret = adDisplayer.html4XTrader(session, camp, ad);
			// String[] ret = adDisplayer.iframeXTrader(session, camp, ad);
			// String[] ret = adDisplayer.iframeScriptXTrader(session, camp,
			// ad);

//			xtarderDebugLog.info("30、adDisplayer:>>xtrader>>" + JSON.toJSONString(ret));
			if (ret == null) {
				XTRADER_EMPTY_RSP.getAndIncrement();
				return rsp;
			}
			// ------------------------------------------------------------
			// xtarderDebugLog.info("系统相应的内容>>>>>>>>>"+ret[0]);
			// xtarderDebugLog.info("系统相应的点击地址>>>>>"+ret[1]);
			XTraderBidResponse.Seatbid seatbid = new XTraderBidResponse.Seatbid();
			seatbids.add(seatbid);
			XTraderBidResponse.Seatbid.Bid bid = new XTraderBidResponse.Seatbid.Bid();
			XTraderBidResponse.Seatbid.Bid.Ext ext = new XTraderBidResponse.Seatbid.Bid.Ext();
			List<XTraderBidResponse.Seatbid.Bid> bids = new ArrayList<XTraderBidResponse.Seatbid.Bid>();
			bids.add(bid);
			seatbid.setBid(bids);
			// id string DSP对该次出价分配的ID
			bid.setId(rsp.getBidid());
			// impid string Bid Request中对应的曝光ID
			bid.setImpid(imp.getId());
			// price float DSP出价，单位是分/千次曝光，即CPM
			bid.setPrice(bidPrice);
			// nurl string win notice url,处理和曝光监测一样，nurl是否支持发送 参见
			// bid.setNurl(ret[2]);
			// adm string
			// 广告物料URL。如果是动态创意，这个字段存放的是创意的HTML标签，标签中支持三种宏替换，%%CLICK_URL_ESC%%（encode的Exchange的点击监测地址）、%%CLICK_URL_UNESC%%(未encode的Exchange点击监测地址)和%%WINNING_PRICE%%（竞价最终价格）。
			bid.setAdm(ret[0]);
			// crid string DSP系统中的创意ID，对于后审核的创意(即动态创意)，这个字段可以留作历史查证。
			bid.setCrid(String.valueOf(ad.getId()));
			if (ad.getLandingType() == LandingType.APPDOWNLOAD) {
				ext.setAction_type(1);
			} else {
				ext.setAction_type(2);
			}
			// type string 物料的类型，包括png，gif，jpg，swf，flv，c和x。具体参见
			// 判断是否支持返回的是动态广告，如果是则物料类型是c
			if (xtraderUseHtml) {
				ext.setType("c");
			} else {
				if (ad.getCreativeType() == CreativeType.FLASH) {
					ext.setType("swf");
				} else if (ad.getCreativeType() == CreativeType.FLV) {
					ext.setType("flv");
				} else if (ad.getCreativeType() == CreativeType.MP4) {
					ext.setType("mp4");
				} else {
					ext.setType("gif");
				}
			}
			bid.setExt(ext);
			monitor.increCampBid(Adx.XTRADER, camp);
			monitor.increAdBid(Adx.XTRADER, ad, camp.getPartnerId(), camp.getGroupId(), camp.getId(),
					ad.getCreativeId());
			// ext object 扩展字段
			// ldp string 点击目标URL。广告点击后会跳转到物料上绑定的landingpage，还是取实时返回的ldp，参见
			// ext.setLdp(ad.getLandingPage());
			// deeplinkurl String
			// 应用直达URL，当返回了deeplinkurl，优先唤醒本地app，如果无法唤醒，则调用ldp(打开或者下载)
			// pm array of strings
			// 曝光监测URL，监测数组支持的曝光条数和广告展现时是否会发物料上绑定的monitor地址，参见
			// List<String> pm=new ArrayList<String>();
			// String pvurls = ad.getPvUrls();
			// if (!StringUtils.isEmpty(pvurls)) {
			// String[] ps = JSON.parseObject(pvurls, String[].class);
			// for (String p : ps) {
			// if(!StringUtils.isEmpty(p)) {
			// pm.add(p);
			// }
			// }
			// }
			// ext.setPm(pm);
			// cm array of strings 点击监测URL，监测数组支持的点击监测条数，参见
			// List<String> cm=new ArrayList<String>();
			// cm.add(ret[1]);
			// ext.setCm(cm);
			// action_type integer 媒体资源位置支持的交互类型：1.download---下载类广告
			// 2.landingpage---打开落地页型广告

		} else {
//			xtarderDebugLog.info("31、xtraderUseHtml:>>xtrader>>" + xtraderUseHtml);

			String[] ret = adDisplayer.urls4Xtrader(session, camp, ad);
//			xtarderDebugLog.info("32、adDisplayer.urls4Xtrader:>>xtrader>>" + JSON.toJSONString(ret));
			if (ret == null) {
				XTRADER_EMPTY_RSP.getAndIncrement();
				return rsp;
			}
			// ------------------------------------------------------------
			// xtarderDebugLog.info("系统相应的内容>>>>>>>>>"+ret[0]);
			// xtarderDebugLog.info("系统相应的点击地址>>>>>"+ret[1]);
			XTraderBidResponse.Seatbid seatbid = new XTraderBidResponse.Seatbid();
			seatbids.add(seatbid);
			XTraderBidResponse.Seatbid.Bid bid = new XTraderBidResponse.Seatbid.Bid();
			XTraderBidResponse.Seatbid.Bid.Ext ext = new XTraderBidResponse.Seatbid.Bid.Ext();
			List<XTraderBidResponse.Seatbid.Bid> bids = new ArrayList<XTraderBidResponse.Seatbid.Bid>();
			bids.add(bid);
			seatbid.setBid(bids);
			// id string DSP对该次出价分配的ID
			bid.setId(rsp.getBidid());
			// impid string Bid Request中对应的曝光ID
			bid.setImpid(imp.getId());
			// price float DSP出价，单位是分/千次曝光，即CPM
			bid.setPrice(bidPrice);

			List<String> pm = new ArrayList<String>();
			ext.setPm(pm);
			// nurl string win notice url,处理和曝光监测一样，nurl是否支持发送 参见
			if (hasWinnotice == 1) {
				bid.setNurl(ret[0]);
				// if(session.isCookieMapping()){
				// pm.add(ret[3]);
				// }
				String pvurls = ad.getPvUrls();
				if (!StringUtils.isEmpty(pvurls)) {
					String[] ps = JSON.parseObject(pvurls, String[].class);
					for (String p : ps) {
						if (!StringUtils.isEmpty(p)) {
							pm.add(p);
						}
					}
				}
				// cm array of strings 点击监测URL，监测数组支持的点击监测条数，参见
				List<String> cm = new ArrayList<String>();
				// cm.add(ret[1]);
				ext.setLdp(ret[1]);
				ext.setCm(cm);
			} else if (hasWinnotice == 0) {
				pm.add(ret[0]);
				ext.setLdp(ret[1]);
			} else {
//				xtarderDebugLog.info("33、hasWinnotice,不符合>>xtrader>>" + hasWinnotice);
				XTRADER_EMPTY_RSP.getAndIncrement();
				return rsp;
			}

			// adm string
			// 广告物料URL。如果是动态创意，这个字段存放的是创意的HTML标签，标签中支持三种宏替换，%%CLICK_URL_ESC%%（encode的Exchange的点击监测地址）、%%CLICK_URL_UNESC%%(未encode的Exchange点击监测地址)和%%WINNING_PRICE%%（竞价最终价格）。
			bid.setAdm(ret[2]);
			// crid string DSP系统中的创意ID，对于后审核的创意(即动态创意)，这个字段可以留作历史查证。
			bid.setCrid(String.valueOf(ad.getId()));

			// action_type integer 媒体资源位置支持的交互类型：1.download---下载类广告
			// 2.landingpage---打开落地页型广告
			if (ad.getLandingType() == LandingType.APPDOWNLOAD) {
				ext.setAction_type(1);
			} else {
				ext.setAction_type(2);
			}
			// type string 物料的类型，包括png，gif，jpg，swf，flv，c和x。具体参见
			// 判断是否支持返回的是动态广告，如果是则物料类型是c
			if (xtraderUseHtml) {
				ext.setType("c");
			} else {
				if (ad.getCreativeType() == CreativeType.FLASH) {
					ext.setType("swf");
				} else if (ad.getCreativeType() == CreativeType.FLV) {
					ext.setType("flv");
				} else if (ad.getCreativeType() == CreativeType.MP4) {
					ext.setType("mp4");
				} else {
					ext.setType("gif");
				}
			}
			bid.setExt(ext);

			monitor.increCampBid(Adx.XTRADER, camp);
			monitor.increAdBid(Adx.XTRADER, ad, camp.getPartnerId(), camp.getGroupId(), camp.getId(),
					ad.getCreativeId());
		}

//		xtarderDebugLog.info("34、rsp:>>xtrader>>" + JSON.toJSONString(rsp) + ">>返回的活动id>>" + camp.getId());
		XTRADER_BID_RSP.getAndIncrement();
		return rsp;
	}

	
	/**
	 * 安沃广告请求响应，安沃没有cookieMapping接口
	 * @param bidReq 广告请求request
	 * @return 广告响应response
	 */
	public AdwoBidResponse bidAdwo(AdwoBidRequest bidReq)
	{
		/** 前期记录安沃的日志 */
		adwoDebugLog.info(bidReq);
		/** 安沃竞价请求次数+1 */
		ADWO_BID_REQ.getAndIncrement(); 
		/** 准备好response对象，作为返回使用*/
		AdwoBidResponse rsp = new AdwoBidResponse();
		/** 拿到安沃竞价时发出的请求id */
		String id = bidReq.getId();
		adwoDebugLog.info("1、安沃请求id>>adwo>>" + id);

		/** 拿到竞价时用户数据 */
		AdwoBidRequest.User user = bidReq.getUser();
		adwoDebugLog.info("2、user>>adwo>>" + JSON.toJSONString(user));
		
		/** 拿到竞价时广告位数据 */
		AdwoBidRequest.Imp imp = bidReq.getImp();
		adwoDebugLog.info("3、imp>>adwo>>" + JSON.toJSONString(imp));
		
		/** 拿到竞价时设备数据 */
		AdwoBidRequest.Device dev = bidReq.getDevice();
		adwoDebugLog.info("4、dev>>adwo>>" + JSON.toJSONString(dev));
		
		/** 拿到竞价时媒体数据 */
		AdwoBidRequest.Site site = bidReq.getSite();
		adwoDebugLog.info("5、site>>adwo>>" + JSON.toJSONString(site));
		
		/** 拿到竞价时app数据 */
		AdwoBidRequest.App adwoApp = bidReq.getApp();
		adwoDebugLog.info("6、adwoApp>>adwo>>" + JSON.toJSONString(adwoApp));

		/** 设置response中的id（出价ID） */
		rsp.setId(id);
		/** 设置response中的bidid（请求ID）*/
		rsp.setBidid(id);
		/** 设置出价对象数组 */
		List<AdwoBidResponse.Seatbid> seatbids = new ArrayList<AdwoBidResponse.Seatbid>();
		rsp.setSeatbid(seatbids);
		/** 设置响应扩展参数  */
		AdwoBidResponse.Ext ext = new AdwoBidResponse.Ext();
		rsp.setExt(ext);

		/** 从竞价请求中，拿到banner对象*/
		AdwoBidRequest.Imp.Banner banner = imp.getBanner();
		adwoDebugLog.info("10、banner>>adwo>>" + JSON.toJSONString(banner));
		
		/** 从竞价请求中，拿到Video对象 */
		AdwoBidRequest.Imp.Video video = imp.getVideo();
		adwoDebugLog.info("11、video>>adwo>>" + JSON.toJSONString(video));

		/** 拿到广告位曝光的低价 */
		Float minCpmPrice = imp.getBidfloor();
		adwoDebugLog.info("12、minCpmPrice>>adwo>>" + minCpmPrice);

		/** 打到设备类型 0 未知，1 iphone，2 android手机，3 ipad，4 WindowsPhone， 5 android平板，6 智能TV */
		int devicetype = -1;
		try {
			devicetype = dev.getDevicetype();
		} catch (Exception e) 
		{
			ADWO_EMPTY_RSP.getAndIncrement();
			return rsp;
		}
		
		/** 如果是智能TV，直接返回空响应（目前不支持智能TV） */
		if(devicetype == 6){
			ADWO_EMPTY_RSP.getAndIncrement();
			return rsp;
		}
		adwoDebugLog.info("13、devicetype>>adwo>>" + devicetype);


		/** 用于判断是否为banner广告,判断是视频广告还是非视频广告 */
		boolean isBanner = banner != null;
		adwoDebugLog.info("14、isBanner>>adwo>>" + isBanner);
		
		/** 拿到广告位的宽和高 */
		int width = isBanner ? banner.getW() : video.getW();
		int height = isBanner ? banner.getH() : video.getH();
		
		String size = width + "x" + height;
		adwoDebugLog.info("15、size>>adwo>>" + size);
		
		/** 创建一个session对象 */
		Session session = new Session();
		session.setCookieMapping(false); //是否cookieMapping
		session.setAdx(Adx.ADWO);        //广告渠道
		session.setBid(bidReq.getId());  //广告请求ID
		session.setUserip(dev.getIp());  //用户IP
		session.setReq(bidReq);          //广告请求对象
		session.setAdp(imp.getTagid());  //广告位ID
		session.setBidFloor(minCpmPrice.intValue());// 广告位的低价（从adwo发送过来的数据中找到，，并设置到session中）
		
		session.setPageReferer(site.getRef()); //页面来源URL
		session.setPageUrl(site.getPage());    //当前页面URL
		
		session.setInApp(true);   //是否来自APP的流量（安沃只有APP的流量）
			
		List<Campaign> camps = null;
		if(isBanner) /** 如果是APP的banner广告 */
		{
			session.setAdType(AdType.APP_BANNER);
		}else{
			session.setAdType(AdType.APP_VIDEO);
		}
		/** 获取APP应用对象信息 */
		if(adwoApp != null)
		{
			/** 将APP ID设置到session中 */
			session.setAppId(adwoApp.getId());
			/** APP名称设置到session中  */
			session.setAppName(adwoApp.getName());
			/** APP应用的包名称或bundleID,如果应用是android则为packageName，如果是ios则为Bundle */
			session.setAppPackageName(adwoApp.getBundle());
		}
		/** 根据 渠道id_宽*高 为key 从app广告列表中取出对应的广告活动 */
		camps = app.get(Adx.ADWO.channelId() + "_" + size);
		
		/** 如果camps中没有数据，说明没有找到适合的广告，则把处理的时长返回，并设置百度的空响应变量+1；返回构建的对象。*/
		if (camps == null || camps.size() == 0) {
			ADWO_EMPTY_RSP.getAndIncrement();
			return rsp;
		}
		
		adwoDebugLog.info("24、活动——camps:>>adwo>>" + camps.size());

		/** 筛选计划和广告 */
		/** 存放广告活动的list，就是筛选出来的广告活动的集合 */
		List<Campaign> passcamps = new ArrayList<Campaign>();
		/** 存放广告创意的容器，就是所有筛选出来的广告的集合 **/
		Map<Long, List<Ad>> passads = new HashMap<Long, List<Ad>>();
		/** 筛选出的广告活动，从camps中取出（camps，仅仅是通过渠道和尺寸做的最基本的筛选）*/
		for (Campaign camp : camps) //循环备选广告
		{
			/** 这里的match是检查广告主，广告活动的状态 */
			if (campMatcher.match(session, camp)) {
				/** 从广告活动中获取，这个广告活动下的所有广告 */
				Map<String, List<Ad>> ads = camp.getAds();
				if (ads == null || ads.size() == 0)
					continue;
				List<Ad> ad = ads.get(size);
				if (ad == null || ad.size() == 0)
					continue;

				List<Ad> passed = new ArrayList<Ad>();
				/** 匹配所有广告创意 */
				for (Ad a : ad) {
					if (adMatcher.match(session, a))
						passed.add(a);
				}
				if (passed.size() > 0) {
					passcamps.add(camp);
					passads.put(camp.getId(), passed);
				}
			}
		}
		adwoDebugLog.info("25、Matcher后活动:>>adwo>>" + passcamps.size());
		
		/** 如果没有备选的广告活动，则直接给百度返回空响应 */
		if (passcamps.size() == 0) {
			ADWO_EMPTY_RSP.getAndIncrement();
			return rsp;
		}

		/** 挑选活动 */
		Campaign camp = null;
		float score = Float.MAX_VALUE;
		/** 经过下面循环，找出所有符合投放条件的广告活动，出价最低的广告活动 */
		for (Campaign pcamp : passcamps) 
		{
			/** 返回活动的竞价次数 */
			float bid = (float) monitor.getCampBid(Adx.ADWO, pcamp);
			// 比如：有两个活动，一个活动的单价是10快，一个活动的单价是5快，那么讲过下面规则就是，出价高的永远比出价低的，投出广告的次数要多
			// 假设，每次经过过滤后，剩下的广告活动都是
			// A、B，A的价格是5块，B的价格是10块，那么第一次使用竞价次数，分别除以价格，得到的都是0
			// 那么就先第一个活动，第二次的时候，1/5=0.2,1/10=0.1,则选B活动
			// 第三次，1/5=0.2,2/10-0.2,选择A活动
			// 第4次，2/5=0.4，2/10=0.3,选择B活动
			// 第5次，2/5=0.4,3/10=0.4,选择A活动
			// 以此类推下去，保证出价高的活动，能够投放的次数多，而出价低的不会一直投不出去。
			long cpmprice = campaignDAO.getCpmPrice(pcamp);
			if (cpmprice == 0l)
				continue;
			float s = bid / (float) cpmprice;
			if (s < score) {
				score = s;
				camp = pcamp;
			}
		}

		if (camp == null) {
			ADWO_EMPTY_RSP.getAndIncrement();
			return rsp;
		}
		adwoDebugLog.info("26、挑选出的活动ID:>>adwo>>" + camp.getId());

		/** 挑选广告创意 */
		List<Ad> ads = passads.get(camp.getId());
		adwoDebugLog.info("27、挑选出的广告:>>xtrader>>" + JSON.toJSONString(ads));
		
		if (ads == null || ads.size() == 0) {
			ADWO_EMPTY_RSP.getAndIncrement();
			return rsp;
		}
		/** 找出广告活动中，参与竞价次数最少的广告 */
		Ad ad = null;
		int bidtimes = Integer.MAX_VALUE;
		for (Ad a : ads) {
			int bid = monitor.getAdBid(Adx.AdView, a);
			if (bid < bidtimes) {
				bidtimes = bid;
				ad = a;
			}
		}

		/** 出价，根据命中的筛选条件，计算出要提升价格的百分比 */
		float incre = bidder.bid(session, camp, ad);
		/** 获取价格因子 */
		float priceFactor = campaignDAO.getCampPriceFactor(camp.getId());
		/** 这次请求的最高限价价格，这个价格实在match过程中在，bidFloorMatcher时产生的。 */
		int maxBidPrice = session.getMaxBidPrice(camp.getId());
		/** 这次广告请求的底价 */
		int bidFloor = session.getBidFloor();
		/** 这次请求的真正竞价价格 */
		int bidPrice = bidFloor + (maxBidPrice - bidFloor) * 5 / 10;
		bidPrice = (int) (bidPrice * (1.0f + incre) * priceFactor);
		if (bidPrice > maxBidPrice)
			bidPrice = maxBidPrice;
		if (bidPrice < bidFloor)
			bidPrice = bidFloor + 1;

		adwoDebugLog.info("系统最终的给出的竞价价格>>>>adwo>>>>>"+bidPrice);
		 
		session.setBidPrice(bidPrice);
		adwoDebugLog.info("28、竞价价格:>>adwo>>" + bidPrice + ">>活动id:" + camp.getId());
	
		/** 非html方式放回  */
		String[] ret = adDisplayer.urls4Adwo(session, camp, ad);
		if (ret == null) {
			XTRADER_EMPTY_RSP.getAndIncrement();
			return rsp;
		}
		
		AdwoBidResponse.Seatbid seatbid = new AdwoBidResponse.Seatbid();      /** DSP出价数组 */
		seatbids.add(seatbid);
		AdwoBidResponse.Seatbid.Bid bid = new AdwoBidResponse.Seatbid.Bid();  /** 出价对象 */
		seatbid.setBid(bid);
		
		/** id string DSP对该次出价分配的ID */
		bid.setId(rsp.getBidid());
		/** impid string Bid Request中对应的曝光ID */
		bid.setImpid(imp.getId());
		/** price float DSP出价，单位是分/千次曝光，即CPM */
		bid.setPrice((float)bidPrice);
		
		List<String> pm = new ArrayList<String>();
		ext.setPm(pm);
		
		/** nurl string win notice url,处理和曝光监测一样，nurl是否支持发送 参见 */
		bid.setNurl(ret[0]);
		
		String pvurls = ad.getPvUrls();
		if (!StringUtils.isEmpty(pvurls)) {
			String[] ps = JSON.parseObject(pvurls, String[].class);
			for (String p : ps) {
				if (!StringUtils.isEmpty(p)) {
					pm.add(p);
				}
			}
		}
		
		/** cm array of strings 点击监测URL，监测数组支持的点击监测条数，参见 */
		List<String> cm = new ArrayList<String>();
		/** cm.add(ret[1]); */
		ext.setLdp(ret[1]);
		ext.setCm(cm);

		// adm string
		/** 广告物料URL。如果是动态创意，这个字段存放的是创意的HTML标签，标签中支持三种宏替换，%%CLICK_URL_ESC%%（encode的Exchange的点击监测地址）、%%CLICK_URL_UNESC%%(未encode的Exchange点击监测地址)和%%WINNING_PRICE%%（竞价最终价格）。*/
		bid.setAdm(ret[2]);
		/** crid string DSP系统中的创意ID，对于后审核的创意(即动态创意)，这个字段可以留作历史查证。 */
		bid.setCrid(String.valueOf(ad.getId()));

		monitor.increCampBid(Adx.ADWO, camp);
		monitor.increAdBid(Adx.ADWO, ad, camp.getPartnerId(), camp.getGroupId(), camp.getId(),
				ad.getCreativeId());
		
		
		XTRADER_BID_RSP.getAndIncrement();
		return rsp;
		
	}
	
	
	
	/** 新汇达通竞价  */
	@Override
	public XHDTBidResponse bidXHDT(XHDTBidRequest bidReq) 
	{
		XHDT_BID_REQ.getAndIncrement();              /** 新汇达通竞价请求次数+1 */
		XHDTBidResponse rsp = new XHDTBidResponse(); /** 新汇达通response，返回使用 */
		return rsp;
	}

	
	/** adView竞价 */
	@Override
	public AVBidResponse bidAdView(AVBidRequest bidReq) 
	{
		/** 前期记录adview的日志 */
		adViewDebugLog.info(bidReq);
		
		/** 零集竞价请求次数+1 */
		ADVIEW_BID_REQ.getAndIncrement();
		/** 准备好response对象，作为返回使用 */
		AVBidResponse rsp = new AVBidResponse();
		
		/** 拿到adView竞价时发出的请求id */
		String id = bidReq.getId();
		
		adViewDebugLog.info("1、adView请求id>>adView>>" + id);
		
		/** 拿到竞价时用户数据 */
		AVBidRequest.User user = bidReq.getUser();
		adViewDebugLog.info("2、user>>adView>>" + JSON.toJSONString(user));
		
		/** 拿到竞价时广告位数据 */
		AVBidRequest.Imp imp = bidReq.getImp().get(0);
		adViewDebugLog.info("3、imp>>adView>>" + JSON.toJSONString(imp));
		
		/** 拿到竞价时设备数据 */
		AVBidRequest.Device dev = bidReq.getDevice();
		adViewDebugLog.info("4、dev>>adView>>" + JSON.toJSONString(dev));
		
		/** 拿到竞价时媒体数据 
		AVBidRequest.Site site = bidReq.getSite();
		adViewDebugLog.info("5、site>>site>>" + JSON.toJSONString(site));
		*/
		
		/** 拿到竞价时app数据 */
		AVBidRequest.App adViewApp = bidReq.getApp();
		adViewDebugLog.info("6、adViewApp>>adView>>" + JSON.toJSONString(adViewApp));

		/** 拿到adView广告位展示类型 0：横幅广告，1：插屏或全屏广告，4：开屏广告，5：视频广告，6：原生广告 */
		int showType = -1;
		try {
			showType = imp.getInstl();
		} catch (Exception e) {
			ADVIEW_EMPTY_RSP.getAndIncrement();
			return rsp;
		}
		if(showType == -1)  /** adView广告展示类型参数为空，直接返回rsp */
		{
			ADVIEW_EMPTY_RSP.getAndIncrement();
			return rsp;
		}
		adViewDebugLog.info("8、imp->instl>>adView>>" + showType);
		
		int pcOrMobile = -1; //1-pc，2-移动
		/**adView设备类型  0:未知，1：iPhone，2：android手机，3：iPad,4:windowsPhone，5:Android平板，6：智能TV，7：PC端个人电脑  */
		int deviceType = -1;
		try {
			deviceType = dev.getDevicetype(); 
		} catch (Exception e) {
		}
		if(deviceType == -1 || deviceType == 0) //adView没有传设备类型参数
		{
			if(adViewApp == null) pcOrMobile = 1;
			else pcOrMobile = 2;
		}else{
			
			if(deviceType == 1 || deviceType == 2 || deviceType == 3 ||deviceType==4||deviceType==5 || deviceType == 6)
				pcOrMobile = 2;
			else
				pcOrMobile = 1;
		}
		adViewDebugLog.info("13、pcOrMobile>>adView,1为pc，2为移动>>" + pcOrMobile);
		
		/** 从竞价请求中，拿到banner对象  */
		AVBidRequest.Imp.Banner banner = imp.getBanner();
		/** 从竞价请求中，拿到Video对象  */
		AVBidRequest.Imp.Video video = imp.getVideo();
		/** 拿到广告位曝光的低价  */
		long minCpmPrice = (long) (imp.getBidfloor()/10000);
		adViewDebugLog.info("12、minCpmPrice>>adView>>" + minCpmPrice);

		
		/** 用于判断是否为banner广告,判断是视频广告还是非视频广告 */
		boolean isBanner = banner != null;
		/** 拿到广告位的宽和高 */
		int width = isBanner ? banner.getW() : video.getW();
		int height = isBanner ? banner.getH() : video.getH();
		adViewDebugLog.info("14、isBanner>>adView>>" + isBanner);
		
		String size = width + "x" + height;
		adViewDebugLog.info("15、size>>adView>>" + size);
		

		/** 创建一个session对象 */
		Session session = new Session();
		session.setCookieMapping(false);
		session.setAdx(Adx.AdView);
		session.setBid(bidReq.getId());
		session.setUserip(dev.getIp());
		session.setReq(bidReq);
		session.setAdp(imp.getTagid());
		/** 从adView发送过来的数据中找到，广告位的低价，并设置到session中 */
		session.setBidFloor((int) minCpmPrice);

		List<Campaign> camps = null;
		/** 1为pc，2为移动 */
		if (pcOrMobile == 2) {
			/*** 移动端广告 */
			session.setInApp(true);
			/** 获取媒体上能够展示的广告位类型 */
			/** 判断媒体可接受的广告展示类型(showType广告位展示类型 0：横幅广告，1：插屏或全屏广告，4：开屏广告，5：视频广告，6：原生广告) */
			if (showType == 1 || showType == 4) {
				session.setAdType(AdType.APP_FULLSCREEN);
			} else if (showType == 0) {
				session.setAdType(AdType.APP_BANNER);
			} else if (showType == 5) {
				ADVIEW_EMPTY_RSP.getAndIncrement();
				return rsp;
			} else if (showType == 6) {
				session.setAdType(AdType.APP_MESSAGE);
			} else {
				ADVIEW_EMPTY_RSP.getAndIncrement();
				return rsp;
			}
			/** 获取应用对象信息 */
			if (null != adViewApp) {
				/** 取出adView发过来的应用的分类数据 */
				String appcatg = "";
				if (adViewApp.getCat() != null && adViewApp.getCat().size() > 0) {
					appcatg = String.valueOf(adViewApp.getCat().get(0));
				}
				adViewDebugLog.info("19、appcatg>>adView>>" + appcatg);
				
				/** 从xtrader发过来的数据中，取出应用的唯一标识，设置到session中 */
				session.setAppId(adViewApp.getId());
				/** APP应用的包名称或bundleID,如果应用是android则为packageName，如果是ios则为Bundle */
				session.setAppPackageName(adViewApp.getBundle());
				/** 将app应用的分类数据设置到session中 */
				session.setAppCatg(appcatg);
			}
			/** 根据 渠道id_宽*高 为key 从app广告列表中取出对应的广告活动 */
			camps = app.get(Adx.AdView.channelId() + "_" + size);

		}else{
			session.setInApp(false);
			/** 如果广告请求是pc **/
			/** 广告位展示类型 0：横幅广告，1：插屏或全屏广告，4：开屏广告，5：视频广告，6：原生广告 */
			if (showType == 0) {
				session.setAdType(AdType.PC_BANNER);
			} else if (showType == 5) {
				session.setAdType(AdType.PC_VIDEO);
			} else {
				ADVIEW_EMPTY_RSP.getAndIncrement();
				return rsp;
			}
			/** 根据 渠道id_宽*高 为key 从pc map中取出所有适合这个尺寸广告 **/
			camps = pc.get(Adx.AdView.channelId() + "_" + size);
		}

		/** 如果camps中没有数据，说明没有找到适合的广告，则把处理的时长返回，并设置百度的空响应变量+1；返回构建的对象。*/
		if (camps == null || camps.size() == 0) {
			ADVIEW_EMPTY_RSP.getAndIncrement();
			return rsp;
		}
		adViewDebugLog.info("24、活动——camps:>>adView>>" + camps.size());

		// 筛选计划和广告
		// 存放广告活动的list，就是筛选出来的广告活动的集合
		List<Campaign> passcamps = new ArrayList<Campaign>();
		// 存放广告的容器，就是所有筛选出来的广告的集合
		Map<Long, List<Ad>> passads = new HashMap<Long, List<Ad>>();
		// 将备选的广告活动，从camps中取出（camps，仅仅是通过渠道和尺寸做的最基本的筛选）
		for (Campaign camp : camps) {
			// 这里的match是检查广告主，广告活动的状态
			if (campMatcher.match(session, camp)) {
				// 从广告活动中获取，这个广告活动下的所有广告
				Map<String, List<Ad>> ads = camp.getAds();
				if (ads == null || ads.size() == 0)
					continue;
				List<Ad> ad = ads.get(size);
				if (ad == null || ad.size() == 0)
					continue;

				List<Ad> passed = new ArrayList<Ad>();
				// 匹配所有广告
				for (Ad a : ad) {
					if (adMatcher.match(session, a))
						passed.add(a);
				}
				if (passed.size() > 0) {
					passcamps.add(camp);
					passads.put(camp.getId(), passed);
				}
			}
		}
		adViewDebugLog.info("25、Matcher后活动:>>adView>>" + passcamps.size());
		
		// 如果没有备选的广告活动，则直接给百度返回空响应
		if (passcamps.size() == 0) {
			ADVIEW_EMPTY_RSP.getAndIncrement();
			return rsp;
		}

		// 挑选活动
		Campaign camp = null;
		float score = Float.MAX_VALUE;
		// 经过下面循环，找出所有符合投放条件的广告活动，出价最低的广告活动
		for (Campaign pcamp : passcamps) {
			// 返回活动的竞价次数
			float bid = (float) monitor.getCampBid(Adx.AdView, pcamp);
			// 比如：有两个活动，一个活动的单价是10快，一个活动的单价是5快，那么讲过下面规则就是，出价高的永远比出价低的，投出广告的次数要多
			// 假设，每次经过过滤后，剩下的广告活动都是
			// A、B，A的价格是5块，B的价格是10块，那么第一次使用竞价次数，分别除以价格，得到的都是0
			// 那么就先第一个活动，第二次的时候，1/5=0.2,1/10=0.1,则选B活动
			// 第三次，1/5=0.2,2/10-0.2,选择A活动
			// 第4次，2/5=0.4，2/10=0.3,选择B活动
			// 第5次，2/5=0.4,3/10=0.4,选择A活动
			// 以此类推下去，保证出价高的活动，能够投放的次数多，而出价低的不会一直投不出去。
			long cpmprice = campaignDAO.getCpmPrice(pcamp);
			if (cpmprice == 0l)
				continue;
			float s = bid / (float) cpmprice;
			if (s < score) {
				score = s;
				camp = pcamp;
			}
		}

		if (camp == null) {
			ADVIEW_EMPTY_RSP.getAndIncrement();
			return rsp;
		}
		adViewDebugLog.info("26、挑选出的活动:>>adView>>" + camp.getId());

		// 挑选广告
		List<Ad> ads = passads.get(camp.getId());
		adViewDebugLog.info("27、挑选出的广告:>>adView>>" + JSON.toJSONString(ads));
		if (ads == null || ads.size() == 0) {
			ADVIEW_EMPTY_RSP.getAndIncrement();
			return rsp;
		}
		// 找出广告活动中，参与竞价次数最少的广告
		Ad ad = null;
		int bidtimes = Integer.MAX_VALUE;
		for (Ad a : ads) {
			int bid = monitor.getAdBid(Adx.AdView, a);
			if (bid < bidtimes) {
				bidtimes = bid;
				ad = a;
			}
		}

		// 出价，根据命中的筛选条件，计算出要提升价格的百分比
		float incre = bidder.bid(session, camp, ad);
		// 获取价格因子
		float priceFactor = campaignDAO.getCampPriceFactor(camp.getId());
		// 这次请求的最高限价价格，这个价格实在match过程中在，bidFloorMatcher时产生的。
		int maxBidPrice = session.getMaxBidPrice(camp.getId());
		// 这次广告请求的底价
		int bidFloor = session.getBidFloor();
		// 这次请求的真正竞价价格
		int bidPrice = bidFloor + (maxBidPrice - bidFloor) * 5 / 10;
		bidPrice = (int) (bidPrice * (1.0f + incre) * priceFactor);
		if (bidPrice > maxBidPrice)
			bidPrice = maxBidPrice;
		if (bidPrice < bidFloor)
			bidPrice = bidFloor + 1;

		// xtarderDebugLog.info("系统最终的给出的竞价价格>>>>>>>>>"+bidPrice);
		session.setBidPrice(bidPrice);
		adViewDebugLog.info("28、竞价价格:>>adView>>" + bidPrice + ">>活动id:" + camp.getId());
	
		/** 获取广告HTML和count统计地址  [0]:HTML,[1]:统计地址 */
		String[] ret = adDisplayer.htmlAdView(session, camp, ad);
		if(ret == null || ret.length == 0)
		{
			ADVIEW_EMPTY_RSP.getAndIncrement();
			return rsp;
		}
		
		
		rsp.setId(id); /** 设置response中的请求id，就是request中的ID */
		rsp.setBidid(id); /** 唯一标识，ADVIEW不读取使用 */
		rsp.setCur("RMB"); /** 价格单位 */
		
		/** 设置广告对象数组  */
		List<AVBidResponse.Seatbid> seatbids = new ArrayList<AVBidResponse.Seatbid>();
		rsp.setSeatbid(seatbids);
		
		/**设置广告对象 */
		AVBidResponse.Seatbid seatbid = new AVBidResponse.Seatbid();
		seatbids.add(seatbid);
		List<AVBidResponse.Seatbid.Bid> bids = new ArrayList<AVBidResponse.Seatbid.Bid>(); /** 出价对象 */
		seatbid.setBid(bids);
		AVBidResponse.Seatbid.Bid bid = new AVBidResponse.Seatbid.Bid();
		bids.add(bid);
		
		bid.setImpid(imp.getId());
		bid.setPrice(bidPrice * 10000); //出价价格
		bid.setPaymode(1); //DSP计费方式 1：CPM，2:CPC
		bid.setAdct(0); //广告点击行为类型 0：未确定
		bid.setAdid(String.valueOf(ad.getId())); //广告ID
		bid.setAdmt(4); //广告类型：1-图片广告，2-GIF动画广告，3-图形文字链广告，4-HTML5广告，5-MRAID v2.0广告，6-视频广告，7-FLASH广告（AV赞不支持），8-NATIVE广告
		bid.setAdm(ret[0]); //广告物料 admt=4或6是必填
		String[] sizes = ad.getSize().split("x");
		
		bid.setAdw(Integer.parseInt(sizes[0])); //广告物料宽度
		bid.setAdh(Integer.parseInt(sizes[1])); //广告物料高度
		bid.setAdurl(""); //广告点击跳转落地页，支持重定向

		adViewDebugLog.info("34、rsp:>>adView>>" + JSON.toJSONString(rsp) + ">>返回的活动id>>" + camp.getId());
		
		monitor.increCampBid(Adx.AdView, camp);
		monitor.increAdBid(Adx.AdView, ad, camp.getPartnerId(), camp.getGroupId(), camp.getId(),
				ad.getCreativeId());
		
		ADVIEW_BID_RSP.getAndIncrement();
		return rsp;
	}

	/**
	 * 根据筛选出来的广告和广告活动，进行出价和展示拼装
	 * 
	 * @param session
	 * @param passads
	 * @param ret
	 */
	private void customCookieDisplay(Session session, List<Campaign> passcamps, Map<Long, List<Ad>> passads,
			Map<String, Object> ret, String size) {
		if (session.getAdx() == Adx.XTRADER) {
			customCookieDisplayXtrader(session, passcamps, passads, ret, size);
		} 
	}


	/**
	 * 零集特殊人群投放，广告出价及拼装
	 * 
	 * @param session
	 * @param passcamps
	 * @param passads
	 * @param retMap
	 * 			key=xtraderUseHtml value=
	 * 			key=hasWinnotice   value=
	 * 			
	 * 			key=xtraderReq		value=
	 * @param size
	 */
	private void customCookieDisplayXtrader(Session session, List<Campaign> passcamps, Map<Long, List<Ad>> passads,
			Map<String, Object> retMap, String size) {
		
		XTraderBidResponse rsp=(XTraderBidResponse)retMap.get(Adx.XTRADER.channelName());
		int hasWinnotice=Integer.parseInt(retMap.get("hasWinnotice").toString());
		XTraderBidRequest bidReq=(XTraderBidRequest)retMap.get("xtraderReq");
		XTraderBidRequest.Imp imp = bidReq.getImp().get(0);
		// 挑选活动
		Campaign camp = null;
		float score = Float.MAX_VALUE;
		// 经过下面循环，找出所有符合投放条件的广告活动，出价最低的广告活动
		for (Campaign pcamp : passcamps) {
			// 返回活动的竞价次数
			float bid = (float) monitor.getCampBid(Adx.XTRADER, pcamp);
			// 比如：有两个活动，一个活动的单价是10快，一个活动的单价是5快，那么讲过下面规则就是，出价高的永远比出价低的，投出广告的次数要多
			// 假设，每次经过过滤后，剩下的广告活动都是
			// A、B，A的价格是5块，B的价格是10块，那么第一次使用竞价次数，分别除以价格，得到的都是0
			// 那么就先第一个活动，第二次的时候，1/5=0.2,1/10=0.1,则选B活动
			// 第三次，1/5=0.2,2/10=0.2,选择A活动
			// 第4次，2/5=0.4，2/10=0.3,选择B活动
			// 第5次，2/5=0.4,3/10=0.4,选择A活动
			// 以此类推下去，保证出价高的活动，能够投放的次数多，而出价低的不会一直投不出去。
			long cpmprice = campaignDAO.getCpmPrice(pcamp);
			if (cpmprice == 0l)
				continue;
			float s = bid / (float) cpmprice;
			if (s < score) {
				score = s;
				camp = pcamp;
			}
		}

		if (camp == null) {
			retMap.put("code", "1");
			return;
		}

		// 挑选广告
		List<Ad> ads = passads.get(camp.getId());
		if (ads == null || ads.size() == 0) {
			retMap.put("code", "1");
			return;
		}
		// 找出广告活动中，参与竞价次数最少的广告
		Ad ad = null;
		int bidtimes = Integer.MAX_VALUE;
		for (Ad a : ads) {
			int bid = monitor.getAdBid(Adx.XTRADER, a);
			if (bid < bidtimes) {
				bidtimes = bid;
				ad = a;
			}
		}

		// 这次广告请求的底价
		int bidFloor = session.getBidFloor();
		int bidPrice = bidFloor * 5;
		session.setBidPrice(bidPrice);
		// 判断是否可以使用html方式还是非html方式返回
		// 填充响应
		boolean xtraderUseHtml=retMap.get("xtraderUseHtml").toString().equals("true") ? true : false;
		List<XTraderBidResponse.Seatbid> seatbids=rsp.getSeatbid();
		if (xtraderUseHtml) {
			// 获取html
			String[] ret = adDisplayer.html4XTrader(session, camp, ad);

			if (ret == null) {
				retMap.put("code", "1");
				return;
			}
			
			XTraderBidResponse.Seatbid seatbid = new XTraderBidResponse.Seatbid();
			seatbids.add(seatbid);
			XTraderBidResponse.Seatbid.Bid bid = new XTraderBidResponse.Seatbid.Bid();
			XTraderBidResponse.Seatbid.Bid.Ext ext = new XTraderBidResponse.Seatbid.Bid.Ext();
			List<XTraderBidResponse.Seatbid.Bid> bids = new ArrayList<XTraderBidResponse.Seatbid.Bid>();
			bids.add(bid);
			seatbid.setBid(bids);
			// id string DSP对该次出价分配的ID
			bid.setId(rsp.getBidid());
			// impid string Bid Request中对应的曝光ID
			bid.setImpid(imp.getId());
			// price float DSP出价，单位是分/千次曝光，即CPM
			bid.setPrice(bidPrice);
			// nurl string win notice url,处理和曝光监测一样，nurl是否支持发送 参见
			// bid.setNurl(ret[2]);
			// adm string
			// 广告物料URL。如果是动态创意，这个字段存放的是创意的HTML标签，标签中支持三种宏替换，%%CLICK_URL_ESC%%（encode的Exchange的点击监测地址）、%%CLICK_URL_UNESC%%(未encode的Exchange点击监测地址)和%%WINNING_PRICE%%（竞价最终价格）。
			bid.setAdm(ret[0]);
			// crid string DSP系统中的创意ID，对于后审核的创意(即动态创意)，这个字段可以留作历史查证。
			bid.setCrid(String.valueOf(ad.getId()));
			if (ad.getLandingType() == LandingType.APPDOWNLOAD) {
				ext.setAction_type(1);
			} else {
				ext.setAction_type(2);
			}
			// type string 物料的类型，包括png，gif，jpg，swf，flv，c和x。具体参见
			// 判断是否支持返回的是动态广告，如果是则物料类型是c
			if (xtraderUseHtml) {
				ext.setType("c");
			} else {
				if (ad.getCreativeType() == CreativeType.FLASH) {
					ext.setType("swf");
				} else if (ad.getCreativeType() == CreativeType.FLV) {
					ext.setType("flv");
				} else if (ad.getCreativeType() == CreativeType.MP4) {
					ext.setType("mp4");
				} else {
					ext.setType("gif");
				}
			}
			bid.setExt(ext);
			monitor.increCampBid(Adx.XTRADER, camp);
			monitor.increAdBid(Adx.XTRADER, ad, camp.getPartnerId(), camp.getGroupId(), camp.getId(),
					ad.getCreativeId());

		} else {
//			xtarderDebugLog.info("31、xtraderUseHtml:>>xtrader>>" + xtraderUseHtml);

			String[] ret = adDisplayer.urls4Xtrader(session, camp, ad);
			if (ret == null) {
				retMap.put("code", "1");
				return;
			}
			XTraderBidResponse.Seatbid seatbid = new XTraderBidResponse.Seatbid();
			seatbids.add(seatbid);
			XTraderBidResponse.Seatbid.Bid bid = new XTraderBidResponse.Seatbid.Bid();
			XTraderBidResponse.Seatbid.Bid.Ext ext = new XTraderBidResponse.Seatbid.Bid.Ext();
			List<XTraderBidResponse.Seatbid.Bid> bids = new ArrayList<XTraderBidResponse.Seatbid.Bid>();
			bids.add(bid);
			seatbid.setBid(bids);
			// id string DSP对该次出价分配的ID
			bid.setId(rsp.getBidid());
			// impid string Bid Request中对应的曝光ID
			bid.setImpid(imp.getId());
			// price float DSP出价，单位是分/千次曝光，即CPM
			bid.setPrice(bidPrice);

			List<String> pm = new ArrayList<String>();
			ext.setPm(pm);
			// nurl string win notice url,处理和曝光监测一样，nurl是否支持发送 参见
			if (hasWinnotice == 1) {
				bid.setNurl(ret[0]);
				// if(session.isCookieMapping()){
				// pm.add(ret[3]);
				// }
				String pvurls = ad.getPvUrls();
				if (!StringUtils.isEmpty(pvurls)) {
					String[] ps = JSON.parseObject(pvurls, String[].class);
					for (String p : ps) {
						if (!StringUtils.isEmpty(p)) {
							pm.add(p);
						}
					}
				}
				// cm array of strings 点击监测URL，监测数组支持的点击监测条数，参见
				List<String> cm = new ArrayList<String>();
				// cm.add(ret[1]);
				ext.setLdp(ret[1]);
				ext.setCm(cm);
			} else if (hasWinnotice == 0) {
				pm.add(ret[0]);
				ext.setLdp(ret[1]);
			} else {
//				XTRADER_EMPTY_RSP.getAndIncrement();
				retMap.put("code", "1");
				return;
			}

			// adm string
			// 广告物料URL。如果是动态创意，这个字段存放的是创意的HTML标签，标签中支持三种宏替换，%%CLICK_URL_ESC%%（encode的Exchange的点击监测地址）、%%CLICK_URL_UNESC%%(未encode的Exchange点击监测地址)和%%WINNING_PRICE%%（竞价最终价格）。
			bid.setAdm(ret[2]);
			// crid string DSP系统中的创意ID，对于后审核的创意(即动态创意)，这个字段可以留作历史查证。
			bid.setCrid(String.valueOf(ad.getId()));

			// action_type integer 媒体资源位置支持的交互类型：1.download---下载类广告
			// 2.landingpage---打开落地页型广告
			if (ad.getLandingType() == LandingType.APPDOWNLOAD) {
				ext.setAction_type(1);
			} else {
				ext.setAction_type(2);
			}
			// type string 物料的类型，包括png，gif，jpg，swf，flv，c和x。具体参见
			// 判断是否支持返回的是动态广告，如果是则物料类型是c
			if (xtraderUseHtml) {
				ext.setType("c");
			} else {
				if (ad.getCreativeType() == CreativeType.FLASH) {
					ext.setType("swf");
				} else if (ad.getCreativeType() == CreativeType.FLV) {
					ext.setType("flv");
				} else if (ad.getCreativeType() == CreativeType.MP4) {
					ext.setType("mp4");
				} else {
					ext.setType("gif");
				}
			}
			bid.setExt(ext);

			monitor.increCampBid(Adx.XTRADER, camp);
			monitor.increAdBid(Adx.XTRADER, ad, camp.getPartnerId(), camp.getGroupId(), camp.getId(),
					ad.getCreativeId());
		}

		XTRADER_BID_RSP.getAndIncrement();
		retMap.put("code", "2");
		return;
	}

	@Override
	public BesBidResponse bidBes(BesBidRequest message) {
		// 读取百度传过来的HttpRequest对象
//		BaiduRealtimeBiddingV26.BidRequest bidRequest= message.getBidReq();
//		besDebugLog.info("0>>" + bidRequest.toString());
//		besDebugLog.info("1>>" + bidRequest.toBuilder());
//		String id=bidRequest.getId();
//		//		//用户兴趣和用户性别
//		List<Long> list=bidRequest.getUserCategoryList();
//		besDebugLog.info("0-1、百度用户属性个数>>" + bidRequest.getUserCategoryCount());
//		besDebugLog.info("0-2、百度用户属性>>" + list);
//		//给百度响应竞价请求
//		BesBidResponse besBidResponse=new BesBidResponse();
//		BaiduRealtimeBiddingV26.BidResponse.Builder bidResponse=BaiduRealtimeBiddingV26.BidResponse.newBuilder();
//		BaiduRealtimeBiddingV26.BidResponse.Ad.Builder ad=BaiduRealtimeBiddingV26.BidResponse.Ad.newBuilder();
//		BaiduRealtimeBiddingV26.BidResponse.Ad.LinkUnitKeyword.Builder link=BaiduRealtimeBiddingV26.BidResponse.Ad.LinkUnitKeyword.newBuilder();
//		ad.setSequenceId(bidRequest.getAdslotList().get(0).getSequenceId());
//		ad.setCreativeId(2);
//		link.setKeyword("1");
//		ad.setLinkUnitKeyword(link);
//		ad.setAdvertiserId(8L);
//		ad.setWidth(bidRequest.getAdslotList().get(0).getWidth());
//		ad.setHeight(bidRequest.getAdslotList().get(0).getHeight());
//		ad.setCategory(11);
//		ad.setType(12);
//		ad.setMaxCpm(bidRequest.getAdslotList().get(0).getMinimumCpm()+100);
//		ad.setLandingPage("13");
//		ad.setIsCookieMatching(true);
//		String monitorUrl="http://localhost/np?reqid=%%ID%%&price=%%PRICE%%";
//		ad.addMonitorUrls(monitorUrl);
//		bidResponse.addAd(ad);
//		bidResponse.setId(id);
//		BaiduRealtimeBiddingV26.BidResponse response= bidResponse.build();
//		besBidResponse.setBidRsp(response);
//
//		return besBidResponse;
		/** 前期记录bes的日志 */

		// 百度竞价请求次数+1
		BES_BID_REQ.getAndIncrement();
		// 准备好response对象，作为返回使用
		BesBidResponse besBidResponse=new BesBidResponse();
		// 读取百度传过来的HttpRequest对象
		BaiduRealtimeBiddingV26.BidRequest bidReq= message.getBidReq();
		// 拿到百度竞价时发出的请求id
		String id=bidReq.getId();
		/**拿到竞价时用户信息*/
		//用户IP地址
		String ip=bidReq.getIp();
		//用户年龄
		String usreAgent=bidReq.getUserAgent();
		//百度用户id
		String baiduUserId=bidReq.getBaiduUserId();
		//百度用户ID版本
	    int userVersion=bidReq.getBaiduUserIdVersion();
		//用户兴趣和用户性别
		List<Long> list=bidReq.getUserCategoryList();
		besDebugLog.info("0-1、百度用户属性个数>>" + bidReq.getUserCategoryCount());
		besDebugLog.info("0-2、百度用户属性>>" + list);
        //页面语言
		String detectedLanguage=bidReq.getDetectedLanguage();
		/** 拿到竞价时页面信息*/
		// 当前页面 URL
		// 当流量来源是 IOS 的 APP 时， IOS 的应用下载链接会在 URL 字段中传输
		String url=bidReq.getUrl();
		//请求的 referer(页面来源的URL)
		String referer=bidReq.getReferer();
		//网站分类
		int site= bidReq.getSiteCategory();
		/**拿到广告位信息**/
		AdSlot adSlot=BaiduRealtimeBiddingV26.BidRequest.AdSlot.newBuilder().build();
		if(bidReq.getAdslotList().size()>0){
			adSlot = bidReq.getAdslotList().get(0);
		}
		/**拿到请求的移动信息*/
		Mobile mobile=bidReq.getMobile();
		/**拿到移动应用信息*/
		MobileApp mobileApp=mobile.getMobileApp();

		// 拿到广告位展示类型
		/**
		* 0 固定   1悬浮   8图+ICON  11插屏  12开屏  13原生  21视频前贴片 22视频中贴片 23视频后贴片  26视频暂停
		*/
		int showType = -1;
		try {
			showType = adSlot.getAdslotType();
		} catch (Exception e) {
			BES_EMPTY_RSP.getAndIncrement();
			BaiduRealtimeBiddingV26.BidResponse.Builder bidResponse=BaiduRealtimeBiddingV26.BidResponse.newBuilder();
			bidResponse.setId(id);
			besBidResponse.setBidRsp( bidResponse.build());
			return besBidResponse;
		}
		if(showType == -1)  /** bes广告展示类型参数为空，直接返回rsp */
		{
			BES_EMPTY_RSP.getAndIncrement();
			BaiduRealtimeBiddingV26.BidResponse.Builder bidResponse=BaiduRealtimeBiddingV26.BidResponse.newBuilder();
			BaiduRealtimeBiddingV26.BidResponse response= bidResponse.build();
			besBidResponse.setBidRsp(response);
			return besBidResponse;
		}
		besDebugLog.info("1、adSlot->adSlotType>>bes>>" + showType);


		// 判断是移动端还是wap和PC端,0为wap,1为移动,2为PC
		int pcOrMobileOrWap =-1; //0 -wap, 1-app, 2-pc
		if(mobileApp.getAppId()==null  || mobileApp.getAppId()==""){//为pc流量
		    	pcOrMobileOrWap = 2;
		}else{
			  if(null!=url){//为app移动流量
				  pcOrMobileOrWap=1;
			  }else{//为wap流量
				  pcOrMobileOrWap = 0;
			  }
		}
		besDebugLog.info("2、pcOrMobileOrWap>>bes,0 -wap, 1-app, 2-pc>>" +  pcOrMobileOrWap);
		int adslotType=adSlot.getAdslotType();


		// 判断是否进行cookie映射
		boolean isCookieMapping = false;
		if (baiduUserId != null) {
			String[] users = null;
			// 决定是否进行cookie映.
			isCookieMapping = cookieDataService.getCidByBesid(baiduUserId,userVersion+"") == null;
			besDebugLog.info("9、isCookieMapping>>bes>>" + isCookieMapping);
			// 将用户分类标签存储到cookie中
				// 取出零集在竞价请求时自带的用户标签数据
				if (list != null && list.size() > 0) {
					users = new String[list.size() + 1];
					users[0] = baiduUserId;
					for (int i = 0; i < list.size(); i++) {
						BesUserCatgUtil besUserCatgUtil=new BesUserCatgUtil();
						Integer besCatId=besUserCatgUtil.getIdFromBedCatgId(list.get(i));
						users[i + 1] = String.valueOf(besCatId);
					}
				}
			//besDebugLog.info("10、users>>bes>>" + JSON.toJSONString(users));
			// 将用户标签数据放进队列中，使用单独的线程，将标签数据写入到redis中
			if (users != null) {
				besDataQueue.tryTransfer(users);
			}
		}

       //给百度响应竞价请求
		BaiduRealtimeBiddingV26.BidResponse.Builder rsp=BaiduRealtimeBiddingV26.BidResponse.newBuilder();
		// 设置response中的请求id
		rsp.setId(id);
        // 拿到广告位曝光的低价
		int minCpmPrice = adSlot.getMinimumCpm();
		besDebugLog.info("3、minCpmPrice>>minCpmPrice>>" + minCpmPrice);
		/** 从竞价请求中，拿到video对象*/
	    VideoInfo videoInfo=adSlot.getVideoInfo();


        /** 拿到广告位的宽和高 */
		int width = adSlot.getWidth();
		int height = adSlot.getHeight();
		String size = width + "x" + height;

		besDebugLog.info("4、size>>size>>" + size);
		// 创建一个session对象
		Session session = new Session();
		session.setCookieMapping(isCookieMapping);//是否cookieMapping
		session.setAdx(Adx.BES);//广告渠道
		session.setBid(bidReq.getId());//广告请求ID
		session.setUserip(bidReq.getIp());//用户IP
		session.setReq(bidReq);//广告请求对象
		session.setAdp(adSlot.getAdBlockKey()+"");//广告位ID
		// 从bes发送过来的数据中找到，广告位的低价，并设置到session中
		session.setBidFloor((int) minCpmPrice);
        session.setPageReferer(referer); //页面来源URL
		session.setPageUrl(url);//当前页面URL
		session.setWebCatg(site+"");//网站分类
		List<Campaign> camps = null;
		/** 0 -wap, 1-移动, 2-pc*/
		if (pcOrMobileOrWap== 1) {
			/*** 移动端广告 */
			session.setInApp(true);
			/** 获取媒体上能够展示的广告位类型 */
			/** 判断媒体可接受的广告展示类型(showType广告位展示类型  0 固定   1悬浮   8图+ICON  11插屏  12开屏  13原生  21视频前贴片 22视频中贴片 23视频后贴片  26视频暂停) */
			if (showType == 11 || showType == 12) {
				session.setAdType(AdType.APP_FULLSCREEN);
			} else if (showType == 0 || showType == 1 || showType == 8) {
				session.setAdType(AdType.APP_BANNER);
			} else if (showType == 13) {
				session.setAdType(AdType.APP_MESSAGE);
			} else if (showType == 21 || showType == 22 || showType == 23) {
				session.setAdType(AdType.APP_VIDEO);
			}else if (showType == 26) {
				session.setAdType(AdType.APP_VIDEO_PAUSE);
			}else {
				ADVIEW_EMPTY_RSP.getAndIncrement();
				BaiduRealtimeBiddingV26.BidResponse.Builder bidResponse=BaiduRealtimeBiddingV26.BidResponse.newBuilder();
				bidResponse.setId(id);
				besBidResponse.setBidRsp(bidResponse.build());
				return besBidResponse;
			}
			/** 获取应用对象信息 */
			if (null != mobileApp) {
				/** 取出bes发过来的应用的分类数据 */
				String appcatg =  String.valueOf(mobileApp.getAppCategory());
				besDebugLog.info("19、appcatg>>bes>>" + appcatg);
				/** 从bes发过来的数据中，取出应用的唯一标识，设置到session中 */
				session.setAppId(mobileApp.getAppId());
				/** APP应用的包名称或bundleID,如果应用是android则为packageName，如果是ios则为Bundle */
				session.setAppPackageName(mobileApp.getAppBundleId());
				/** 将app应用的分类数据设置到session中 */
				session.setAppCatg(appcatg);
			}
		/** 根据 渠道id_宽*高 为key 从app广告列表中取出对应的广告活动 */
		camps = app.get(Adx.BES.channelId() + "_" + size);
		}else if (pcOrMobileOrWap== 2){

			session.setInApp(false);
			/** 如果广告请求是pc **/
			/** 广告位展示类型 0： 固定   1悬浮   8图+ICON  11插屏  12开屏  13原生  21视频前贴片 22视频中贴片 23视频后贴片  26视频暂停*/
	       if (showType == 0 || showType == 1 || showType == 8) {
				session.setAdType(AdType.PC_BANNER);
			} else if (showType == 21 || showType == 22 || showType == 23) {
				session.setAdType(AdType.PC_VIDEO);
			}else if (showType == 26) {
				session.setAdType(AdType.PC_VIDEO_PAUSE);
			}else {
				BES_EMPTY_RSP.getAndIncrement();
				BaiduRealtimeBiddingV26.BidResponse.Builder bidResponse=BaiduRealtimeBiddingV26.BidResponse.newBuilder();
				bidResponse.setId(id);
				besBidResponse.setBidRsp(bidResponse.build());
				return besBidResponse;
			}
			/** 根据 渠道id_宽*高 为key 从pc map中取出所有适合这个尺寸广告 **/
			camps = pc.get(Adx.BES.channelId() + "_" + size);
		}
		/** 如果camps中没有数据，说明没有找到适合的广告，则把处理的时长返回，并设置百度的空响应变量+1；返回构建的对象。*/
		if (camps == null || camps.size() == 0) {
			BES_EMPTY_RSP.getAndIncrement();
			BaiduRealtimeBiddingV26.BidResponse.Builder bidResponse=BaiduRealtimeBiddingV26.BidResponse.newBuilder();
			bidResponse.setId(id);
			besBidResponse.setBidRsp(bidResponse.build());
			return besBidResponse;
		}
        /** 筛选计划和广告 */
		/** 存放广告活动的list，就是筛选出来的广告活动的集合 */
		List<Campaign> passcamps = new ArrayList<Campaign>();
		/** 存放广告创意的容器，就是所有筛选出来的广告的集合 **/
		Map<Long, List<Ad>> passads = new HashMap<Long, List<Ad>>();
		/** 筛选出的广告活动，从camps中取出（camps，仅仅是通过渠道和尺寸做的最基本的筛选）*/
		for (Campaign camp : camps) //循环备选广告
		{
			/** 这里的match是检查广告主，广告活动的状态 */
			if (campMatcher.match(session, camp)) {
			/** 从广告活动中获取，这个广告活动下的所有广告 */
			Map<String, List<Ad>> ads = camp.getAds();
			if (ads == null || ads.size() == 0)
					continue;
			List<Ad> ad = ads.get(size);
			if (ad == null || ad.size() == 0)
					continue;

			List<Ad> passed = new ArrayList<Ad>();
			/** 匹配所有广告创意 */
			for (Ad a : ad) {
				if (adMatcher.match(session, a))
					passed.add(a);
				}
				if (passed.size() > 0) {
					passcamps.add(camp);
					passads.put(camp.getId(), passed);
						}
					}
				}

				/** 如果没有备选的广告活动，则直接给百度返回空响应 */
				if (passcamps.size() == 0) {
					BES_EMPTY_RSP.getAndIncrement();
					BaiduRealtimeBiddingV26.BidResponse.Builder bidResponse=BaiduRealtimeBiddingV26.BidResponse.newBuilder();
					bidResponse.setId(id);
					besBidResponse.setBidRsp(bidResponse.build());
					return besBidResponse;
				}

				/** 挑选活动 */
				Campaign camp = null;
				float score = Float.MAX_VALUE;
				/** 经过下面循环，找出所有符合投放条件的广告活动，出价最低的广告活动 */
				for (Campaign pcamp : passcamps)
				{
					/** 返回活动的竞价次数 */
					float bid = (float) monitor.getCampBid(Adx.BES, pcamp);
					// 比如：有两个活动，一个活动的单价是10快，一个活动的单价是5快，那么讲过下面规则就是，出价高的永远比出价低的，投出广告的次数要多
					// 假设，每次经过过滤后，剩下的广告活动都是
					// A、B，A的价格是5块，B的价格是10块，那么第一次使用竞价次数，分别除以价格，得到的都是0
					// 那么就先第一个活动，第二次的时候，1/5=0.2,1/10=0.1,则选B活动
					// 第三次，1/5=0.2,2/10-0.2,选择A活动
					// 第4次，2/5=0.4，2/10=0.3,选择B活动
					// 第5次，2/5=0.4,3/10=0.4,选择A活动
					// 以此类推下去，保证出价高的活动，能够投放的次数多，而出价低的不会一直投不出去。
					long cpmprice = campaignDAO.getCpmPrice(pcamp);
					if (cpmprice == 0l)
						continue;
					float s = bid / (float) cpmprice;
					if (s < score) {
						score = s;
						camp = pcamp;
					}
				}

				if (camp == null) {
					BES_EMPTY_RSP.getAndIncrement();
					BaiduRealtimeBiddingV26.BidResponse.Builder bidResponse=BaiduRealtimeBiddingV26.BidResponse.newBuilder();
					bidResponse.setId(id);
					besBidResponse.setBidRsp(bidResponse.build());
					return besBidResponse;
				}

				/** 挑选广告创意 */
				List<Ad> ads = passads.get(camp.getId());

				if (ads == null || ads.size() == 0) {
					BES_EMPTY_RSP.getAndIncrement();
					BaiduRealtimeBiddingV26.BidResponse.Builder bidResponse=BaiduRealtimeBiddingV26.BidResponse.newBuilder();
					bidResponse.setId(id);
					besBidResponse.setBidRsp(bidResponse.build());
					return besBidResponse;
				}
				/** 找出广告活动中，参与竞价次数最少的广告 */
				Ad ad = null;
				int bidtimes = Integer.MAX_VALUE;
				for (Ad a : ads) {
					int bid = monitor.getAdBid(Adx.BES, a);
					if (bid < bidtimes) {
						bidtimes = bid;
						ad = a;
					}
				}

				// 出价，根据命中的筛选条件，计算出要提升价格的百分比
				float incre = bidder.bid(session, camp, ad);
				// 获取价格因子
				float priceFactor = campaignDAO.getCampPriceFactor(camp.getId());
				// 这次请求的最高限价价格，这个价格实在match过程中在，bidFloorMatcher时产生的。
				int maxBidPrice = session.getMaxBidPrice(camp.getId());
				// 这次广告请求的底价
				int bidFloor = session.getBidFloor();
				// 这次请求的真正竞价价格
				int bidPrice = bidFloor + (maxBidPrice - bidFloor) * 5 / 10;
				bidPrice = (int) (bidPrice * (1.0f + incre) * priceFactor);
				if (bidPrice > maxBidPrice)
					bidPrice = maxBidPrice;
				if (bidPrice < bidFloor)
					bidPrice = bidFloor + 1;
				// xtarderDebugLog.info("系统最终的给出的竞价价格>>>>>>>>>"+bidPrice);
				session.setBidPrice(bidPrice);
				besDebugLog.info("28、系统最终的给出的竞价价格:>>bes>>" + bidPrice + ">>活动id:" + camp.getId());
				//给百度响应广告竞价请求
				BaiduRealtimeBiddingV26.BidResponse.Builder bidResponse=BaiduRealtimeBiddingV26.BidResponse.newBuilder();
				BaiduRealtimeBiddingV26.BidResponse.Ad.Builder besAd=BaiduRealtimeBiddingV26.BidResponse.Ad.newBuilder();
				BaiduRealtimeBiddingV26.BidResponse.Ad.LinkUnitKeyword.Builder link=BaiduRealtimeBiddingV26.BidResponse.Ad.LinkUnitKeyword.newBuilder();
				 besAd.setSequenceId(bidReq.getAdslotList().get(0).getSequenceId());
				 besAd.setCreativeId(ad.getId());
				 besAd.setAdvertiserId(camp.getPartnerId());
				String[] sizes = ad.getSize().split("x");
				 besAd.setWidth(Integer.parseInt(sizes[0]));//广告物料宽度
				 besAd.setHeight(Integer.parseInt(sizes[1]));//广告物料高度
		        besAd.setMaxCpm(bidPrice);

		        String monitorUrl="http://58.87.67.115/np?reqid=%%ID%%&ext_data=%%EXT_DATA%%&price=%%PRICE%%";
		        String baseUrl = "http://58.87.67.115/nc?q=%%EXT_DATA%%";
		        String baseUrl2 = "http://58.87.67.115/nl?u="+ad.getClickUrl();
		         boolean isapp=false;
		      if(pcOrMobileOrWap== 1){
				  isapp=true;
			  }
		       Param param=new Param();
		       param.setT(new Date().getTime());
		        param.setB(id);
		       param.setBf(minCpmPrice);
		       param.setBp(bidPrice);
		       param.setTt(pcOrMobileOrWap);
		       param.setApp(isapp);
		       param.setUrl(url);
		       param.setWc(site+"");
		       param.setIp(bidReq.getIp());
		      param.setC(cookieDataService.getCidByBesid(baiduUserId,userVersion+""));
		       param.setCh(2);
		       param.setAdp(adSlot.getAdBlockKey()+"");
		       param.setTs(1);
		       param.setP(camp.getPartnerId());
		       param.setG(camp.getGroupId());
		       param.setCp(camp.getId());
		       param.setCpt(camp.getCampType().getCode());
		       param.setAg(ad.getGroupId());
		       param.setA(ad.getId());
		       param.setAt(camp.getAdType().getCode());
		       param.setCr(ad.getCreativeId());
		        besAd.setExtdata(LikeBase64.encode(JSON.toJSONString(param)));
		        besAd.addMonitorUrls(monitorUrl);
		        besAd.setLandingPage(baseUrl2+"&r="+ad.getLandingPage());
		        besAd.addTargetUrl(baseUrl + "&r=" + LikeBase64.encode(ad.getClickUrl()));
		        besAd.setIsCookieMatching(isCookieMapping);
				bidResponse.addAd(besAd);
				bidResponse.setId(id);
				BaiduRealtimeBiddingV26.BidResponse besresponse= bidResponse.build();
				besBidResponse.setBidRsp(besresponse);
		        besDebugLog.info("30、给百度的响应:>>bes>>"+"creativeId:" +besAd.getCreativeId() + ">>advertiserId:" + camp.getPartnerId()+"creative:"+Integer.parseInt(sizes[0])+"_"+Integer.parseInt(sizes[1])+"maxCpm:"+besAd.getMaxCpm());

				monitor.increCampBid(Adx.BES, camp);
				monitor.increAdBid(Adx.BES, ad, camp.getPartnerId(), camp.getGroupId(), camp.getId(),
						ad.getCreativeId());
				BES_BID_RSP.getAndIncrement();

				return besBidResponse;

	}


	/*
	 * com.jtd.engine.adserver.jms.JMSMessageListener，中接收到更新活动数据的消息时调用该方法。
	 */
	@Override
	public boolean updateCampaign(Campaign campaign) {
		return monitor.updateCampaign(campaign);
	}

	/*
	 * 中接收到changeAutoStatus修改自动状态的消息时调用该方法。
	 */
	@Override
	public boolean changeAutoStatus(long campaignId, CampaignAutoStatus status) {
		return monitor.changeAutoStatus(campaignId, status);
	}

	/*
	 * com.jtd.engine.adserver.jms.JMSMessageListener，中接收到修改手动状态的消息时调用该方法。
	 */
	@Override
	public boolean changeManulStatus(long campaignId, CampaignManulStatus status) {
		return monitor.changeManulStatus(campaignId, status);
	}

	/*
	 * com.jtd.engine.adserver.jms.JMSMessageListener，中接收到修改活动行业类别的消息时调用该方法。
	 */
	@Override
	public void changeCampCatg(long campid, CatgSerial serial, String catgid) {
		monitor.changeCampCatg(campid, serial, catgid);
	}

	/*
	 * com.jtd.engine.adserver.jms.JMSMessageListener，中接收到修改广告行业类别的消息时调用该方法。
	 */
	@Override
	public void changeAdCatg(long adid, CatgSerial serial, String catgid) {
		monitor.changeAdCatg(adid, serial, catgid);
	}

	/**
	 * @param adDisplayer
	 *            the adDisplayer to set
	 */
	public void setAdDisplayer(AdDisplayer adDisplayer) {
		this.adDisplayer = adDisplayer;
	}

	/**
	 * @param timer
	 *            the timer to set
	 */
	public void setTimer(Timer timer) {
		this.timer = timer;
	}


	/**
	 * @param jmsTemplate
	 *            the jmsTemplate to set
	 */
	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	/**
	 * @param campMatcher
	 *            the campMatcher to set
	 */
	public void setCampMatcher(CampMatcher campMatcher) {
		this.campMatcher = campMatcher;
	}

	/**
	 * @param adMatcher
	 *            the adMatcher to set
	 */
	public void setAdMatcher(AdMatcher adMatcher) {
		this.adMatcher = adMatcher;
	}

	/**
	 * @param bidder
	 *            the bidder to set
	 */
	public void setBidder(Bidder bidder) {
		this.bidder = bidder;
	}

	/**
	 * @param campaignDAO
	 *            the campaignDAO to set
	 */
	public void setCampaignDAO(CampaignDAO campaignDAO) {
		this.campaignDAO = campaignDAO;
	}

	/**
	 * @param heartBeatDAO
	 *            the heartBeatDAO to set
	 */
	public void setHeartBeatDAO(HeartBeatDAO heartBeatDAO) {
		this.heartBeatDAO = heartBeatDAO;
	}

	/**
	 * @param cookieDataService
	 *            the cookieDataService to set
	 */
	public void setCookieDataService(CookieDataService cookieDataService) {
		this.cookieDataService = cookieDataService;
	}

	/**
	 * @param systemTime
	 *            the systemTime to set
	 */
	public void setSystemTime(SystemTime systemTime) {
		this.systemTime = systemTime;
	}

	public void setOtherDao(OtherDAO otherDao) {
		this.otherDao = otherDao;
	}
	
}
