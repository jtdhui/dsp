package com.jtd.statistic.service;

import com.jtd.statistic.po.Click;
import com.jtd.statistic.po.Landing;
import com.jtd.statistic.po.PV;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月11日
 * @项目名称 dsp-counter
 * @描述 <p></p>
 */
public interface CountService {
	
	public void increPvReq();

	/**
	 * 统计PV点击和到达
	 */
	public void increPv(PV pv);
	public void increClick(Click click);
	public void increLanding(Landing landing);
}
