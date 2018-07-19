package com.jtd.web.service.front.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jtd.commons.dao.BaseDao;
import com.jtd.commons.mybatis.support.CustomerContextHolder;
import com.jtd.web.dao.ICreativeSizeDao;
import com.jtd.web.po.CreativeSize;
import com.jtd.web.service.front.IFrontCreativeSizeService;
import com.jtd.web.service.impl.BaseService;

/**
 * 创意尺寸
 * @作者 duber
 * @版本 V1.0
 * @创建日期 2016年11月10日
 * @描述
 */
@Service
public class FrontCreativeSizeService extends BaseService<CreativeSize> implements IFrontCreativeSizeService {

	@Autowired
	private ICreativeSizeDao creativeSizeDao;
	
	@Override
	protected BaseDao<CreativeSize> getDao() {
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		return creativeSizeDao;
	}

}
