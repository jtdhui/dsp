package com.jtd.web.dao;

import com.jtd.commons.dao.BaseDao;
import com.jtd.web.po.ReportDemoDataSetting;

/**
 * Created by duber on 2017/3/10.
 */
public interface IReportDemoDataSettingDao extends BaseDao<ReportDemoDataSetting> {

	/**
	 * 按广告主id删除记录
	 * 
	 * @param partnerId
	 * @return
	 */
	public long deleteByPartnerId(long partnerId);
	
}
