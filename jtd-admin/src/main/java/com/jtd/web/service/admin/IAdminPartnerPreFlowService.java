package com.jtd.web.service.admin;

import com.jtd.web.po.PartnerPreFlow;
import com.jtd.web.service.IBaseService;

/**
 * Created by duber on 16/12/26.
 */
public interface IAdminPartnerPreFlowService extends IBaseService<PartnerPreFlow> {

    public void updateInvoice(PartnerPreFlow partnerPreFlow);
}
