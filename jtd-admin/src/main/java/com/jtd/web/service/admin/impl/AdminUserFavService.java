package com.jtd.web.service.admin.impl;

import com.jtd.commons.dao.BaseDao;
import com.jtd.commons.mybatis.support.CustomerContextHolder;
import com.jtd.web.controller.BaseController;
import com.jtd.web.dao.IUserFavDao;
import com.jtd.web.po.UserFav;
import com.jtd.web.service.admin.IAdminUserFavService;
import com.jtd.web.service.impl.BaseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by duber on 16/12/8.
 */
@Service
public class AdminUserFavService extends BaseService<UserFav> implements IAdminUserFavService {

    @Autowired
    private IUserFavDao userFavDao;

    @Override
    protected BaseDao<UserFav> getDao() {
        CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
        return userFavDao;
    }

    @Override
    public void deleteUserFav(UserFav userFav) {
        CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
        userFavDao.deleteUserFav(userFav);
    }
}
