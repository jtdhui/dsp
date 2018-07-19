package com.jtd.engine.ad.matcher;

import com.jtd.engine.ad.Session;
import com.jtd.web.model.Campaign;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月8日
 * @项目名称 dsp-engine
 * @描述 <p></p>
 */
public abstract class AbstractCampMatcher implements com.jtd.engine.ad.matcher.CampMatcher {
	
//	private static final Log log = LogFactory.getLog(AbstractCampMatcher.class);

	// 下一个匹配器
	private com.jtd.engine.ad.matcher.CampMatcher nextMatcher;

	private final Logger logMyDebug = LogManager.getLogger("myDebugLog"); 
	
	@Override
	public boolean match(Session session, Campaign camp) {
		
		if (doMatch(session, camp)) {
			if (nextMatcher != null) {
				//boolean ret=nextMatcher.match(session, camp);
				//logMyDebug.info("活动id>>>>"+camp.getId()+"。匹配类名："+nextMatcher.getClass().getName()+"匹配结果:"+ret);
				//return ret;
				return nextMatcher.match(session, camp);
			} else {
				return true;
			}
		} else {
//			log.info(camp.getId() + ": " + this.getClass());
			return false;
		}
	}

	/**
	 * 执行匹配动作
	 * @param session
	 * @param camp
	 * @return
	 */
	protected abstract boolean doMatch(Session session, Campaign camp);
	
	/**
	 * 是否没有做任何维度的定向
	 * @param camp
	 * @return
	 */
	protected boolean isEmptyDim(Campaign camp) {
		Map<String, String> dims = camp.getCampaignDims();
		return dims == null || dims.size() == 0;
	}
	
	/**
	 * 获取定向维度值
	 * @param dimName
	 * @param camp
	 * @return
	 */
	protected String getDim(String dimName, Campaign camp) {
		if(isEmptyDim(camp)) return null;
		Map<String, String> dims = camp.getCampaignDims();
		return dims.get(dimName);
	}

	/**
	 * @param nextMatcher
	 *            the nextMatcher to set
	 */
	public void setNextMatcher(com.jtd.engine.ad.matcher.CampMatcher nextMatcher) {
		this.nextMatcher = nextMatcher;
	}

	/**
	 * @return the nextMatcher
	 */
	public com.jtd.engine.ad.matcher.CampMatcher getNextMatcher() {
		return nextMatcher;
	}
}
