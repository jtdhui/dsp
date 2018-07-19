package com.jtd.web.service.admin.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jtd.commons.dao.BaseDao;
import com.jtd.commons.mybatis.support.CustomerContextHolder;
import com.jtd.web.dao.ISysRoleDao;
import com.jtd.web.po.SysRole;
import com.jtd.web.po.SysUser;
import com.jtd.web.service.admin.IAdminRoleService;
import com.jtd.web.service.impl.BaseService;

/**
 * 角色管理
 * @作者 duber
 * @版本 V1.0
 * @创建日期 2016年10月12日
 * @描述
 */
@Service
public class AdminRoleService extends BaseService<SysRole>  implements IAdminRoleService {

	@Autowired
	private ISysRoleDao sysRoleDao;

	@Override
	protected BaseDao<SysRole> getDao() {
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		return sysRoleDao;
	}
	
}
