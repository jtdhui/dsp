package com.jtd.engine.ad.matcher;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jtd.engine.ad.Session;
import com.jtd.engine.ad.em.MatchType;
import com.jtd.engine.utils.IPBUtil;
import com.jtd.web.model.Campaign;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月8日
 * @项目名称 dsp-engine
 * @描述 <p>匹配用户的城市和投放项是否投放该城市</p>
 */
public class CityMatcher extends AbstractCampMatcher {
	
	private static final Log log = LogFactory.getLog(CityMatcher.class);
	private final Logger xtarderDebugLog = LogManager.getLogger("xtarderDebugLog");
	private final Logger besDebugLog = LogManager.getLogger("besDebugLog");
	private final Logger hzDebugLog = LogManager.getLogger("hzDebugLog");
	
	private IPBUtil iPBUtil;

	/* (non-Javadoc)
	 * @see com.asme.ad.matcher.AbstractChainedMatcher#doMatch(com.asme.ad.Session, net.doddata.web.dsp.model.Campaign)
	 */
	@Override
	protected boolean doMatch(Session session, Campaign camp) {
		besDebugLog.info("bes:CityMatcher--------------------------------------------");
		String userip = session.getUserip();
//		String userip ="118.192.170.71";

		xtarderDebugLog.info("46、matched，用户ip是否为空>>xtrader:地域匹配-CityMatcher>>"+StringUtils.isEmpty(userip)+"ip>>活动id:"+camp.getId());
		besDebugLog.info("matched，用户ip是否为空>>bes:地域匹配-CityMatcher>>"+StringUtils.isEmpty(userip)+"ip=="+userip+"活动id>>"+camp.getId());
		// 没取到用户的IP
		if (StringUtils.isEmpty(userip)) return true;

		Integer cityid = iPBUtil.getCityIdByIp(userip);
		besDebugLog.info("matched，userip是否存在ip库>>bes:ip库匹配-UseripMatcher>>cityid=="+cityid+"活动id>>"+camp.getId());

		if (cityid != null) {
			// 查出来的放进session
			session.setAttr("cityid", cityid);
		}

//		//从缓存中获取区域定向数据
		String cityids = getDim("cityids", camp);

//		// 没有做地域定向，没有做地域定向，则直接通过
		if (StringUtils.isEmpty(cityids)) return true;

//		//如果adx过来的用户的ip在ip库中没有，并且这个活动又做了地域定向，那么这个活动将不会匹配，这次广告请求
		if(cityid == null) return false;
		if (cityid > cityids.length()) {
			log.error("查询到的cityid: " + cityid + "超过了系统定义的cityid范围");
		}

		boolean matched = cityids.charAt(cityid - 1) == '1';
		besDebugLog.info("matched>>bes:CityMatcher>>"+matched+"ip==活动id>>"+camp.getId());
 		if(matched) session.addMatched(camp.getId(), MatchType.CITY );

		return matched;


	}

	/**
	 * @param iPBUtil the iPBUtil to set
	 */
	public void setiPBUtil(IPBUtil iPBUtil) {
		this.iPBUtil = iPBUtil;
	}
}
