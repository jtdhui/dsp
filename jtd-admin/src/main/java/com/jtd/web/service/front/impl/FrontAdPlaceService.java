package com.jtd.web.service.front.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jtd.commons.dao.BaseDao;
import com.jtd.commons.mybatis.support.CustomerContextHolder;
import com.jtd.web.dao.IAdPlaceDao;
import com.jtd.web.po.AdPlace;
import com.jtd.web.service.front.IFrontAdPlaceService;
import com.jtd.web.service.impl.BaseService;

@Service
public class FrontAdPlaceService extends BaseService<AdPlace> implements IFrontAdPlaceService   {
	
	@Autowired
	private IAdPlaceDao dao ;

	@Override
	protected BaseDao<AdPlace> getDao() {
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		return dao;
	}

}
