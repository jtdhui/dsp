package com.jtd.web.service.front.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jtd.commons.dao.BaseDao;
import com.jtd.commons.mybatis.support.CustomerContextHolder;
import com.jtd.web.dao.ICreativeSizeFlowDao;
import com.jtd.web.po.CreativeSizeFlow;
import com.jtd.web.service.front.IFrontCreativeSizeFlowService;
import com.jtd.web.service.impl.BaseService;

/**
 * 渠道对应的素材尺寸
 * @作者 duber
 * @版本 V1.0
 * @创建日期 2016年11月10日
 * @描述
 */
@Service
public class FrontCreativeSizeFlowService extends BaseService<CreativeSizeFlow> implements IFrontCreativeSizeFlowService {

	@Autowired
	private  ICreativeSizeFlowDao creativeSizeFlowDao; 
	
	@Override
	protected BaseDao<CreativeSizeFlow> getDao() {
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		return creativeSizeFlowDao;
	}

	@Override
	public List<Map<String, Object>> selectListByMap(Map<String, Object> map) {
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		return creativeSizeFlowDao.selectListByMap(map);
	}

	@Override
	public Map<String, Object> selectSumFlowByMap(Map<String, Object> map) {
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		return creativeSizeFlowDao.selectSumFlowByMap(map);
	}
}
