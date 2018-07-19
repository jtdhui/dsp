package com.jtd.effect.service;

import com.jtd.effect.po.Track;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月12日
 * @项目名称 dsp-tracker
 * @描述 <p></p>
 */
public interface TrackService {

	/**
	 * 统计访客召回JS发送回来的信息
	 */
	public void track(Track track);
}
