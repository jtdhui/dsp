package com.jtd.web.service.front.impl;

import com.jtd.commons.dao.BaseDao;
import com.jtd.commons.mybatis.support.CustomerContextHolder;
import com.jtd.web.dao.IUserPartnerDao;
import com.jtd.web.po.UserPartner;
import com.jtd.web.service.front.IFrontUserPartnerService;
import com.jtd.web.service.impl.BaseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class FrontUserPartnerService extends BaseService<UserPartner> implements IFrontUserPartnerService {

	@Autowired
	private IUserPartnerDao userPartnerDao;
	
	@Override
	protected BaseDao<UserPartner> getDao() {
		//使用业务库
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		return userPartnerDao;
	}

	@Override
	public List<Map<String, Object>> findUserPartnerBy(Map<String,Object> map) {
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		return userPartnerDao.findUserPartnerBy(map);
	}


}
