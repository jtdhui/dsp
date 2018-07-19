package com.jtd.web.service.third.impl;

import com.jtd.commons.dao.BaseDao;
import com.jtd.commons.mybatis.support.CustomerContextHolder;
import com.jtd.web.dao.ISysContackDao;
import com.jtd.web.po.SysContack;
import com.jtd.web.service.impl.BaseService;
import com.jtd.web.service.third.ISysContackService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by duber on 2017/6/29.
 */
@Service
public class SysContackService extends BaseService<SysContack> implements ISysContackService {

    @Autowired
    private ISysContackDao sysContackDao;

    @Override
    protected BaseDao<SysContack> getDao() {
        CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
        return sysContackDao;
    }

}
