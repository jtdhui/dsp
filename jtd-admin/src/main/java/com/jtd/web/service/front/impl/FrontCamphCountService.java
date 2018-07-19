package com.jtd.web.service.front.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jtd.commons.dao.BaseDao;
import com.jtd.commons.mybatis.support.CustomerContextHolder;
import com.jtd.web.dao.ICamphCountDao;
import com.jtd.web.po.count.Camph;
import com.jtd.web.service.front.IFrontCamphCountService;
import com.jtd.web.service.impl.BaseService;

@Service
public class FrontCamphCountService extends BaseService<Camph> implements IFrontCamphCountService {

	@Autowired
	private ICamphCountDao camphCountDao;
	
	@Override
	protected BaseDao<Camph> getDao() {
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_COUNT);
		return camphCountDao;
	}

}
