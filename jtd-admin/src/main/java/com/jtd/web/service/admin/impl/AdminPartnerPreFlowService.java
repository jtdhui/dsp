package com.jtd.web.service.admin.impl;

import com.jtd.commons.dao.BaseDao;
import com.jtd.commons.mybatis.support.CustomerContextHolder;
import com.jtd.web.dao.IPartnerPreFlowDao;
import com.jtd.web.po.PartnerPreFlow;
import com.jtd.web.service.admin.IAdminPartnerPreFlowService;
import com.jtd.web.service.impl.BaseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Created by duber on 16/12/26.
 */
@Service
public class AdminPartnerPreFlowService extends BaseService<PartnerPreFlow> implements IAdminPartnerPreFlowService{

    @Autowired
    private IPartnerPreFlowDao partnerPreFlowDao;

    @Override
    protected BaseDao<PartnerPreFlow> getDao() {
        CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
        return partnerPreFlowDao;
    }

    @Override
    public void updateInvoice(PartnerPreFlow partnerPreFlow) {
        CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
        if(partnerPreFlow.getIsInvoice().equals(2)){
            partnerPreFlow.setInvoice(null);
        }
        partnerPreFlowDao.updateInvoice(partnerPreFlow);
    }
}
