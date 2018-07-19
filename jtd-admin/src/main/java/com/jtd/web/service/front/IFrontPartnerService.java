package com.jtd.web.service.front;

import com.jtd.web.po.ActiveUser;
import com.jtd.web.po.Partner;
import com.jtd.web.service.IBaseService;

import java.util.List;

public interface IFrontPartnerService extends IBaseService<Partner> {

    public List<Partner> findPartnerSideList(ActiveUser activeUser);
    
    /**
	 * 检查广告主可以进行投放哪些渠道
	 * 
     * @param partnerId
	 * @return 返回的list里包含可投放渠道的id
	 */
	public List<Long> checkPartnerWhenStartCampaign(long partnerId);
}
