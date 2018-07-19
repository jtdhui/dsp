package com.jtd.web.service.admin.impl;

import com.jtd.commons.dao.BaseDao;
import com.jtd.commons.mybatis.support.CustomerContextHolder;
import com.jtd.web.dao.ISysLogDao;
import com.jtd.web.po.SysLog;
import com.jtd.web.service.admin.IAdminSysLogService;
import com.jtd.web.service.impl.BaseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by duber on 2017/4/20.
 */
@Service
public class AdminSysLogService extends BaseService<SysLog> implements IAdminSysLogService {

    @Autowired
    private ISysLogDao sysLogDao;

    @Override
    protected BaseDao<SysLog> getDao() {
        CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
        return sysLogDao;
    }
}
