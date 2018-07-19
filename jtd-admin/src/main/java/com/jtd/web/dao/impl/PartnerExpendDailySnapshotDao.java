package com.jtd.web.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.jtd.commons.dao.BaseDaoImpl;
import com.jtd.web.dao.IPartnerExpendDailySnapshotDao;
import com.jtd.web.po.count.PartnerExpendDailySnapshot;

/**
 * 广告主按天统计
 * 
 * @作者 duber
 * @版本 V1.0
 * @创建日期 2016年11月2日
 * @描述
 */
@Repository
public class PartnerExpendDailySnapshotDao extends
		BaseDaoImpl<PartnerExpendDailySnapshot> implements
		IPartnerExpendDailySnapshotDao {

	@Override
	public Map<String, Object> findByPartnerAndDate(long partnerId, Date date) {

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("partnerId", partnerId);
		params.put("dateTime", date);

		return getSqlSession().selectOne(getStatement("findBy"), params);
	}

}
