package com.jtd.web.service.admin.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jtd.commons.dao.BaseDao;
import com.jtd.commons.mybatis.support.CustomerContextHolder;
import com.jtd.web.dao.ISysRolePermissionDao;
import com.jtd.web.po.SysRolePermission;
import com.jtd.web.po.SysUser;
import com.jtd.web.service.admin.IAdminRolePermissionService;
import com.jtd.web.service.impl.BaseService;

/**
 * 
 * @作者 duber
 * @版本 V1.0
 * @创建日期 2016年10月14日
 * @描述 角色授权
 */
@Service
public class AdminRolePermissionService extends BaseService<SysRolePermission>  implements IAdminRolePermissionService {

	@Autowired
	private ISysRolePermissionDao sysRolePermissionDao;
	
	@Override
	protected BaseDao<SysRolePermission> getDao() {
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		return sysRolePermissionDao;
	}
	
}
