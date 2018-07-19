package com.jtd.web.service.front.impl;

import com.jtd.commons.dao.BaseDao;
import com.jtd.commons.mybatis.support.CustomerContextHolder;
import com.jtd.web.dao.IAppDao;
import com.jtd.web.po.App;
import com.jtd.web.service.front.IFrontAppService;
import com.jtd.web.service.impl.BaseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by duber on 2017/3/15.
 */
@Service
public class FrontAppService extends BaseService<App> implements IFrontAppService {

    @Autowired
    private IAppDao appDao;

    @Override
    protected BaseDao<App> getDao() {
        CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
        return appDao;
    }
}
