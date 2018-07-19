package com.jtd.effect.dao;

import com.jtd.effect.po.Track;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月12日
 * @项目名称 dsp-tracker
 * @描述 <p></p>
 */
public interface TrackDAO {
	
	/**
	 * 保存效果
	 * @param track
	 */
	public void saveTrack(Track track);
}
