package com.jtd.web.service.admin.impl;

import com.jtd.commons.dao.BaseDao;
import com.jtd.commons.mybatis.support.CustomerContextHolder;
import com.jtd.web.dao.IAdAuditStatusDao;
import com.jtd.web.po.AdAuditStatus;
import com.jtd.web.service.IMQConnectorService;
import com.jtd.web.service.admin.IAdminAdAuditStatusService;
import com.jtd.web.service.impl.BaseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AdminAdAuditStatusService extends BaseService<AdAuditStatus> implements IAdminAdAuditStatusService {

	@Autowired
	private IAdAuditStatusDao adAuditStatusDao;
	
	@Autowired
	private IMQConnectorService mQConnectorService;
	
	@Override
	protected BaseDao<AdAuditStatus> getDao() {
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		return adAuditStatusDao;
	}

	@Override
	public List<Map<String,Object>> findListByAdId(Map<String, Object> paraMap) {
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		List<Map<String,Object>> list = adAuditStatusDao.getListByAdId(paraMap);
		return list;
	}

	@Override
	public long removeByAdId(Map<String, Object> paraMap) {
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		long ret = adAuditStatusDao.deleteByAdId(paraMap);
		return ret;
	}

	@Override
	public AdAuditStatus findBy(AdAuditStatus adAuditStatus) {
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		return adAuditStatusDao.getBy(adAuditStatus);
	}

	@Override
	public void updateAdAuditStatus(AdAuditStatus adAuditStatus) {
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		AdAuditStatus aaEntity = findBy(adAuditStatus);

		if(aaEntity == null){
			adAuditStatusDao.insert(adAuditStatus);
		}else{
			adAuditStatusDao.update(adAuditStatus);
		}

	}

}
