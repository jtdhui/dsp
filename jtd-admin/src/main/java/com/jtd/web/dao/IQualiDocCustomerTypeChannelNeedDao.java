package com.jtd.web.dao;

import java.util.List;
import java.util.Map;

import com.jtd.commons.dao.BaseDao;
import com.jtd.web.po.QualiDocCustomerTypeChannelNeed;
import com.jtd.web.po.QualiDocType;

public interface IQualiDocCustomerTypeChannelNeedDao extends
		BaseDao<QualiDocCustomerTypeChannelNeed> {

	public List<QualiDocCustomerTypeChannelNeed> listByMap(
            Map<String, Object> params);

	/**
	 * 在前台按资质类型id查找所有渠道所需文档类型（去重）
	 * 
	 * @param params
	 * @return
	 */
	public List<QualiDocType> listDocTypeByCustomerTypeId(long customerTypeId);

	/**
	 * 在后台提交adx审核时，查找adx需要的资质类型
	 * 
	 * @param channelId
	 * @param customerTypeId
	 * @param isMainQualidoc
	 * @return
	 */
	public List<QualiDocType> listDocTypeForChannel(long channelId,
                                                    long customerTypeId, int isMainQualidoc);
}
