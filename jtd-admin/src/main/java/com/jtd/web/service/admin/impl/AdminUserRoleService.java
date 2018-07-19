package com.jtd.web.service.admin.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jtd.commons.dao.BaseDao;
import com.jtd.commons.mybatis.support.CustomerContextHolder;
import com.jtd.web.dao.ISysUserRoleDao;
import com.jtd.web.po.ActiveUser;
import com.jtd.web.po.SysRole;
import com.jtd.web.po.SysUser;
import com.jtd.web.po.SysUserRole;
import com.jtd.web.service.admin.IAdminRoleService;
import com.jtd.web.service.admin.IAdminUserRoleService;
import com.jtd.web.service.admin.IAdminUserService;
import com.jtd.web.service.impl.BaseService;

import org.springframework.ui.Model;

/**
 * 用户角色管理
 * @作者 duber
 * @版本 V1.0
 * @创建日期 2016年10月12日
 * @描述
 */
@Service
public class AdminUserRoleService extends BaseService<SysUserRole>  implements IAdminUserRoleService {

	@Autowired
	private ISysUserRoleDao sysUserRoleDao;
	@Autowired
	private IAdminRoleService adminRoleService;
	@Autowired
	private IAdminUserService adminUserService;

	@Override
	protected BaseDao<SysUserRole> getDao() {
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		return sysUserRoleDao;
	}

	@Override
	public void queryUserRoleDate(Model model,String user_id) {
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		SysUserRole sysUserRole = new SysUserRole();
		sysUserRole.setSysUserId(user_id);

		List<SysUserRole> sysUserRoleList =  sysUserRoleDao.listBy(sysUserRole);
		boolean flag = false; // 当前登录用户是否具有管理员角色
		if(sysUserRoleList.size()>0){
			for (SysUserRole userRole: sysUserRoleList){
				if(userRole.getSysRoleId().equals("1")){
					flag = true;
					break;
				}
			}

		}

		SysUser user = new SysUser();
		user.setStatus(0);
		List<SysUser> userList=adminUserService.listBy(user); //查询启用的用户
		List<SysUser> list = new ArrayList<SysUser>();
		if(!flag){ //如果不是管理员
			sysUserRole = new SysUserRole();
			sysUserRole.setSysRoleId("1");
			// 角色是管理员的用户
			List<SysUserRole> urList =  sysUserRoleDao.listBy(sysUserRole);

			for(SysUser su : userList){
				boolean isExist = false;
				for(SysUserRole sur : urList) {
                    // 把角色是管理员的用户从列表中去除
					if (su.getId().toString().equals(sur.getSysUserId())) {
						isExist = true;
						break;
					}
				}
				if(!isExist) {
					list.add(su);
				}
			}
		}else {
			list = userList;
		}
		model.addAttribute("userList",list);

		// 如果当前用户的角色不是管理员,角色列表不显示管理员的角色
		SysRole sysRole = new SysRole();
		sysRole.setAvailable("1");
		List<SysRole> roleList=adminRoleService.listBy(sysRole); // 查询启用的角色
		for(SysRole sr : roleList){
			if(sr.getId()==1 && !flag){
				roleList.remove(sr);
				break;
			}
		}
		model.addAttribute("roleList",roleList);
	}


}
