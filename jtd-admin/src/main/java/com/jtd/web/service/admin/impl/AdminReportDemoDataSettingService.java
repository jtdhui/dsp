package com.jtd.web.service.admin.impl;

import com.jtd.commons.dao.BaseDao;
import com.jtd.commons.mybatis.support.CustomerContextHolder;
import com.jtd.web.dao.IReportDemoDataSettingDao;
import com.jtd.web.po.ReportDemoDataSetting;
import com.jtd.web.service.admin.IAdminReportDemoDataSettingService;
import com.jtd.web.service.impl.BaseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by duber on 2017/3/10.
 */
@Service
public class AdminReportDemoDataSettingService extends BaseService<ReportDemoDataSetting> implements IAdminReportDemoDataSettingService{

    @Autowired
    private IReportDemoDataSettingDao reportDemoDataSettingDao;

    @Override
    protected BaseDao<ReportDemoDataSetting> getDao() {
        CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
        return reportDemoDataSettingDao;
    }
}
