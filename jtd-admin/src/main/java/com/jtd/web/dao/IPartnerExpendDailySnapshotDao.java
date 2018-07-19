package com.jtd.web.dao;

import java.util.Date;
import java.util.Map;

import com.jtd.commons.dao.BaseDao;
import com.jtd.web.po.count.PartnerExpendDailySnapshot;

public interface IPartnerExpendDailySnapshotDao extends BaseDao<PartnerExpendDailySnapshot> {

	public Map<String,Object> findByPartnerAndDate(long partnerId, Date date);

}
