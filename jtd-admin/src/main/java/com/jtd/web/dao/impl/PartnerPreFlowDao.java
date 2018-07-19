package com.jtd.web.dao.impl;

import com.jtd.commons.dao.BaseDaoImpl;
import com.jtd.web.dao.IPartnerPreFlowDao;
import com.jtd.web.po.PartnerPreFlow;

import org.springframework.stereotype.Repository;

/**
 * 广告主预充值明细
 * Created by duber on 16/12/26.
 */
@Repository
public class PartnerPreFlowDao extends BaseDaoImpl<PartnerPreFlow> implements IPartnerPreFlowDao {

    @Override
    public int updateInvoice(PartnerPreFlow partnerPreFlow) {
        return getSqlSession().update(getStatement("updateInvoice"), partnerPreFlow);
    }
}
