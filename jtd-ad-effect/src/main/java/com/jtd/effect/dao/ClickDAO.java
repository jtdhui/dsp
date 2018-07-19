package com.jtd.effect.dao;

import com.jtd.effect.po.Click;


/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月12日
 * @项目名称 dsp-tracker
 * @描述 <p></p>
 */
public interface ClickDAO {

	/**
	 * 获取点击
	 * 
	 * @param click
	 */
	public Click getClick(String clickKey);
}
