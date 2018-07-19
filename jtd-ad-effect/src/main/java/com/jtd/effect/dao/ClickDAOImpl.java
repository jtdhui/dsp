package com.jtd.effect.dao;

import com.alibaba.fastjson.JSON;
import com.jtd.effect.po.Click;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月12日
 * @项目名称 dsp-tracker
 * @描述 <p></p>
 */
public class ClickDAOImpl extends AbstractDBSelectedShardedRedisDAO implements ClickDAO {

	/* (non-Javadoc)
	 * @see com.doddata.net.dao.ClickDAO#getClick(java.lang.String)
	 * 这里的clickKey是，页面发起tracker请求的时候发送过来的参数
	 */
	@Override
	public Click getClick(String clickKey) {
		String c = get(clickKey);
		if (c != null) return JSON.parseObject(c, Click.class);
		return null;
	}
}