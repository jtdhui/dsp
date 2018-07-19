package com.jtd.web.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.jtd.commons.dao.BaseDaoImpl;
import com.jtd.web.dao.IParhCountDao;
import com.jtd.web.po.CampaignCategory;
import com.jtd.web.po.count.Parh;

/**
 * 广告主按小时统计
 * @作者 duber
 * @版本 V1.0
 * @创建日期 2016年11月2日
 * @描述
 */
@Repository
public class ParhCountDao extends BaseDaoImpl<Parh> implements IParhCountDao {

	@Override
	public List<Parh> listByMap(Map<String, Object> phMap) {
		List<Parh> list = getSqlSession().selectList(getStatement("listByMap"),phMap);
		return list;
	}

}
