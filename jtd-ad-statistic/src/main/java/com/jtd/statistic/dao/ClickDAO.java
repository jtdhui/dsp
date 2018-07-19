package com.jtd.statistic.dao;

import com.jtd.statistic.po.Click;

/**
 * @author asme 2015年12月31日 下午1:29:29
 * 
 */
public interface ClickDAO {

	/**
	 * 添加和获取点击
	 * 
	 * @param click
	 */
	public void addClick(Click click);
	public Click getClick(String clickKey);
}
