package com.jtd.web.service.front;

import com.jtd.web.po.ActiveUser;
import com.jtd.web.po.Campaign;
import com.jtd.web.po.CampaignDim;
import com.jtd.web.po.SysLog;
import com.jtd.web.service.IBaseService;

import java.util.List;

/**
 * Created by duber on 2017/4/18.
 */
public interface IFrontSysLogService extends IBaseService<SysLog> {

    public void saveCampaignStep1(ActiveUser activeUser,Campaign campaign,Campaign oldCampaign);

    public void saveCampaignStep2(ActiveUser activeUser,Campaign campaign,Campaign oldCampaign,List<CampaignDim> listCD);

    public void saveCampaignStep3(ActiveUser activeUser,Campaign campaign, Campaign oldCampaign, String operateDesc);
}
