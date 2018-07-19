package com.jtd.statistic.dao;

import com.alibaba.fastjson.JSON;
import com.jtd.statistic.po.Click;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月11日
 * @项目名称 dsp-counter
 * @描述 <p></p>
 */
public class ClickDAOImpl extends AbstractDBSelectedShardedRedisDAO implements ClickDAO {

	private static final int CLICK_EXPIRE = 30;
	private int clickExpire = CLICK_EXPIRE;
	
	private int index;

	/* (non-Javadoc)
	 * @see com.doddata.net.dao.ClickDAO#addClick(com.doddata.net.po.Click)
	 * 向redis中添加click对象
	 */
	@Override
	public void addClick(Click click) {
		setex(click.getClickKey(), JSON.toJSONString(click), clickExpire);
	}

	/* (non-Javadoc)
	 * @see com.doddata.net.dao.ClickDAO#getClick(java.lang.String)
	 * 获取clickKey对应的click对象，然后再从reids中删除clickKey对应的数据
	 */
	@Override
	public Click getClick(String clickKey) {
		//获取clickKey对应的点击数据，然后删除
		String c = rem(clickKey);
		if (c != null) return JSON.parseObject(c, Click.class);
		return null;
	}

	/**
	 * @param clickExpire the clickExpire to set
	 */
	public void setClickExpire(int clickExpire) {
		this.clickExpire = clickExpire;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
}