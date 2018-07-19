package com.jtd.web.service.front.impl;

import com.jtd.commons.dao.BaseDao;
import com.jtd.commons.mybatis.support.CustomerContextHolder;
import com.jtd.web.dao.IRetargetPacketDao;
import com.jtd.web.po.RetargetPacket;
import com.jtd.web.service.front.IFrontRetargetPacketService;
import com.jtd.web.service.impl.BaseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by duber on 16/12/20.
 */
@Service
public class FrontRetargetPacketService extends BaseService<RetargetPacket> implements IFrontRetargetPacketService{

    @Autowired
    private IRetargetPacketDao retargetPacketDao;

    @Override
    protected BaseDao<RetargetPacket> getDao() {
        CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
        return retargetPacketDao;
    }

    @Override
    public List<RetargetPacket> listByPartnerId(long partnerId){
        return retargetPacketDao.listByPartnerId(partnerId);
    }

}
