package com.jtd.web.service.front.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jtd.commons.dao.BaseDao;
import com.jtd.commons.mybatis.support.CustomerContextHolder;
import com.jtd.web.dao.ITopDomainDao;
import com.jtd.web.po.TopDomain;
import com.jtd.web.service.front.IFrontTopDomainService;
import com.jtd.web.service.impl.BaseService;

@Service
public class FrontTopDomainService extends BaseService<TopDomain> implements IFrontTopDomainService {

	@Autowired
	private ITopDomainDao topDomainDao;
	
	@Override
	protected BaseDao<TopDomain> getDao() {
		// 切换业务库数据源
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		return topDomainDao;
	}

}
