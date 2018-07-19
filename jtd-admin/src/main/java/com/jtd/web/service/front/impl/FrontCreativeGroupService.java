package com.jtd.web.service.front.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jtd.commons.dao.BaseDao;
import com.jtd.commons.mybatis.support.CustomerContextHolder;
import com.jtd.web.dao.ICreativeGroupDao;
import com.jtd.web.po.CreativeGroup;
import com.jtd.web.service.front.IFrontCreativeGroupService;
import com.jtd.web.service.impl.BaseService;

/**
 * 
 * @作者 duber
 * @版本 V1.0
 * @创建日期 2016年11月13日
 * @描述
 */
@Service
public class FrontCreativeGroupService extends BaseService<CreativeGroup> implements IFrontCreativeGroupService {

	@Autowired
	private ICreativeGroupDao creativeGroupDao;
	
	@Override
	protected BaseDao<CreativeGroup> getDao() {
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		return creativeGroupDao;
	}


}
