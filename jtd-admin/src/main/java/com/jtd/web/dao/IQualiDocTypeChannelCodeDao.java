package com.jtd.web.dao;

import com.jtd.commons.dao.BaseDao;
import com.jtd.web.po.QualiDocTypeChannelCode;

public interface IQualiDocTypeChannelCodeDao extends BaseDao<QualiDocTypeChannelCode> {

	public QualiDocTypeChannelCode findBy(long channelId, long docTypeId);

}
