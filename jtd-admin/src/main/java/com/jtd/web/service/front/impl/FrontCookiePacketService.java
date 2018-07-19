package com.jtd.web.service.front.impl;

import com.jtd.commons.dao.BaseDao;
import com.jtd.commons.mybatis.support.CustomerContextHolder;
import com.jtd.web.dao.ICookiePacketDao;
import com.jtd.web.po.CookiePacket;
import com.jtd.web.service.front.IFrontCookiePacketService;
import com.jtd.web.service.impl.BaseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by duber on 16/12/20.
 */
@Service
public class FrontCookiePacketService extends BaseService<CookiePacket> implements IFrontCookiePacketService {

    @Autowired
    private ICookiePacketDao cookiePacketDao;

    @Override
    protected BaseDao<CookiePacket> getDao() {
        CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
        return cookiePacketDao;
    }
}
