package com.jtd.web.service.front.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jtd.commons.dao.BaseDao;
import com.jtd.commons.mybatis.support.CustomerContextHolder;
import com.jtd.web.dao.IChannelDao;
import com.jtd.web.po.Channel;
import com.jtd.web.service.front.IFrontChannelService;
import com.jtd.web.service.impl.BaseService;

@Service
public class FrontChannelService extends BaseService<Channel> implements IFrontChannelService {

	@Autowired
	private IChannelDao channelDao;
	
	@Override
	protected BaseDao<Channel> getDao() {
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		return channelDao;
	}


}
