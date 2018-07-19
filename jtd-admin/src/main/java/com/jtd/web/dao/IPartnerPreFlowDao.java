package com.jtd.web.dao;

import com.jtd.commons.dao.BaseDao;
import com.jtd.web.po.PartnerPreFlow;

/**
 * Created by duber on 16/12/26.
 */
public interface IPartnerPreFlowDao extends BaseDao<PartnerPreFlow> {

    public int updateInvoice(PartnerPreFlow partnerPreFlow);
}
