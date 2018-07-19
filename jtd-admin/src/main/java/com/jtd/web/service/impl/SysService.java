package com.jtd.web.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jtd.commons.mybatis.support.CustomerContextHolder;
import com.jtd.web.constants.RoleType;
import com.jtd.web.dao.ISysUserRoleDao;
import com.jtd.web.dao.impl.SysPermissionDao;
import com.jtd.web.po.ActiveUser;
import com.jtd.web.po.SysPermission;
import com.jtd.web.po.SysUser;
import com.jtd.web.po.SysUserRole;
import com.jtd.web.service.ISysService;
import com.jtd.web.service.IUserService;

@Service("sysService")
public class SysService implements ISysService {
	
	@Autowired
	private IUserService userService;

	@Autowired
	private SysPermissionDao sysPermissionDao;

	@Autowired
	private ISysUserRoleDao userRoleDao;

	@Override
	public ActiveUser authenticat(String loginName, String password) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SysUser findSysUserByLoginName(String loginName) throws Exception {
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		SysUser user = userService.findUserByLoginName(loginName);
		return user;
	}

	@Override
	public List<SysPermission> findMenuListByUserId(Long userid) throws Exception {
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		List<SysPermission> sysPermissions = null;
		SysUserRole userRole = new SysUserRole();
		userRole.setSysUserId(userid.toString());
		List<SysUserRole> sysUserRoleList = userRoleDao.listBy(userRole);
		if(sysUserRoleList.size()>0){
			userRole = sysUserRoleList.get(0);
			int roleId = Integer.parseInt(userRole.getSysRoleId());
			if(roleId == RoleType.ADMIN.getCode()){ //如果是管理员角色
				SysPermission sp = new SysPermission();
				sp.setType("menu");
//				sp.setAvailable("1");
				sysPermissions = sysPermissionDao.listBy(sp);
			}else {
				sysPermissions = sysPermissionDao.findMenuListByUserId(userid);
			}
		}
		return sysPermissions;
	}

	@Override
	public List<SysPermission> findPermissionListByUserId(Long userid) throws Exception {
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		List<SysPermission> sysPermissions = null;
		SysUserRole userRole = new SysUserRole();
		userRole.setSysUserId(userid.toString());
		List<SysUserRole> sysUserRoleList = userRoleDao.listBy(userRole);
		if(sysUserRoleList.size()>0){
			userRole = sysUserRoleList.get(0);
			int roleId = Integer.parseInt(userRole.getSysRoleId());
			if(roleId == RoleType.ADMIN.getCode()){ //如果是管理员角色
				SysPermission sp = new SysPermission();
				sp.setType("permission");
//				sp.setAvailable("1");
				sysPermissions = sysPermissionDao.listBy(sp);
			}else {
				sysPermissions = sysPermissionDao.findPermissionListByUserId(userid);
			}
		}
		return sysPermissions;
	}

}
