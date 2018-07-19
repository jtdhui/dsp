package com.jtd.web.dao.impl;

import java.util.HashMap;

import com.jtd.commons.dao.BaseDaoImpl;
import com.jtd.web.dao.IReportDemoDataSettingDao;
import com.jtd.web.po.ReportDemoDataSetting;

import org.springframework.stereotype.Repository;

/**
 * Created by duber on 2017/3/10.
 */
@Repository
public class ReportDemoDataSettingDao extends BaseDaoImpl<ReportDemoDataSetting> implements IReportDemoDataSettingDao {

	public long deleteByPartnerId(long partnerId){
		
		HashMap<String,Object> map = new HashMap<String, Object>();
		map.put("partnerId", partnerId);
		
		return getSqlSession().delete(getStatement("deleteByPartnerId"), map);
	}
	
}
