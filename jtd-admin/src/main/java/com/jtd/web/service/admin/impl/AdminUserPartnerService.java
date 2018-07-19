package com.jtd.web.service.admin.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jtd.commons.dao.BaseDao;
import com.jtd.commons.mybatis.support.CustomerContextHolder;
import com.jtd.web.dao.IUserPartnerDao;
import com.jtd.web.po.SysUser;
import com.jtd.web.po.UserPartner;
import com.jtd.web.service.admin.IAdminUserPartnerService;
import com.jtd.web.service.impl.BaseService;

@Service
public class AdminUserPartnerService extends BaseService<UserPartner>  implements IAdminUserPartnerService {

	@Autowired
	private IUserPartnerDao userPartnerDao;
	
	@Override
	protected BaseDao<UserPartner> getDao() {
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		return userPartnerDao;
	}
	
}
