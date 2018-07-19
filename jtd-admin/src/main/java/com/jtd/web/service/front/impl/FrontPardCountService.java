package com.jtd.web.service.front.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jtd.commons.dao.BaseDao;
import com.jtd.commons.mybatis.support.CustomerContextHolder;
import com.jtd.web.dao.IPardCountDao;
import com.jtd.web.po.count.Pard;
import com.jtd.web.service.front.IFrontPardCountService;
import com.jtd.web.service.impl.BaseService;

/**
 * 广告主按天查询Service
 * @作者 duber
 * @版本 V1.0
 * @创建日期 2016年11月2日
 * @描述
 */
@Service
public class FrontPardCountService extends BaseService<Pard> implements IFrontPardCountService {

	@Autowired
	private IPardCountDao pardCountDao;
	
	@Override
	protected BaseDao<Pard> getDao() {
		// 选择统计库查询
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_COUNT);
		return pardCountDao;
	}

	@Override
	public Pard getByMap(Map<String, Object> pdMap) {
		// 选择统计库查询
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_COUNT);
		return pardCountDao.getByMap(pdMap);
	}
}
