package com.jtd.engine.ad.matcher;

import com.jtd.engine.ad.Session;
import com.jtd.web.model.Campaign;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月8日
 * @项目名称 dsp-engine
 * @描述 <p></p>
 */
public interface CampMatcher {
	
	/**
	 * 判断活动是否可以投放
	 * @param session
	 * @param camp
	 * @return
	 */
	public boolean match(Session session, Campaign camp);
}
