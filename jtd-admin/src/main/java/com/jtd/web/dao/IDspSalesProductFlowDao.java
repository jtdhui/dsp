package com.jtd.web.dao;

import com.jtd.commons.dao.BaseDao;
import com.jtd.web.po.DspSalesProductFlow;

public interface IDspSalesProductFlowDao extends BaseDao<DspSalesProductFlow> {

	/**
	 * 获取广告主最后一次升级记录（以得知广告主现在什么级别）
	 * 
	 * @param partnerId
	 * @return
	 */
	public DspSalesProductFlow getPartnerLastUpgrade(long partnerId);

}
