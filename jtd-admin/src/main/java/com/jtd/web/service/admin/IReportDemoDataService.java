package com.jtd.web.service.admin;

import com.jtd.web.po.Campaign;
import com.jtd.web.po.Partner;
import com.jtd.web.po.ReportDemoDataSetting;
import com.jtd.web.service.IBaseService;

public interface IReportDemoDataService extends
		IBaseService<ReportDemoDataSetting> {

	public void saveCampDataPerHour(ReportDemoDataSetting rdds, Campaign camp,
			Partner partner,int hour);

}
