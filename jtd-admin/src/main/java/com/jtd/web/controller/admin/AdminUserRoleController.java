package com.jtd.web.controller.admin;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jtd.web.controller.BaseController;
import com.jtd.web.po.ActiveUser;
import com.jtd.web.po.SysRole;
import com.jtd.web.po.SysUser;
import com.jtd.web.po.SysUserRole;
import com.jtd.web.service.admin.IAdminRoleService;
import com.jtd.web.service.admin.IAdminUserRoleService;
import com.jtd.web.service.admin.IAdminUserService;

@Controller
@RequestMapping("/admin/userrole")
public class AdminUserRoleController extends BaseController{

	public static final String PAGE_DIRECTORY="/admin/userrole/";
	public static final String ACTION_PATH="redirect:/admin/userrole/";

	@Autowired
	private IAdminUserRoleService adminUserRoleService;
	
	@RequestMapping("/list")
	public String list(Model model){
		
		// Shiro用户信息
		setUserPageInfo(model);

		ActiveUser activeUser = getUserInfo();
		String user_id = activeUser.getUserId().toString();
		adminUserRoleService.queryUserRoleDate(model,user_id);

		return PAGE_DIRECTORY+"admin_userrole";
	}
	
	
	@RequestMapping("/userSelect")
	public String userSelect(Model model,String id){
		
		// Shiro用户信息
		setUserPageInfo(model);

		SysUserRole sysUserRole = new SysUserRole();
		sysUserRole.setSysUserId(id);
		List<SysUserRole> sysUserRoleList =  adminUserRoleService.listBy(sysUserRole);
		model.addAttribute("sysUserRoleList",sysUserRoleList);

		model.addAttribute("user_id",id);
		ActiveUser activeUser = getUserInfo();
		String user_id = activeUser.getUserId().toString();
		adminUserRoleService.queryUserRoleDate(model,user_id);

		return PAGE_DIRECTORY+"admin_userrole";
	}

	@RequestMapping("/grant")
	public String grant(Model model,String sysUserId,String sysRoleId){
		
		// Shiro用户信息
		setUserPageInfo(model);
		model.addAttribute("user_id",sysUserId);
		
		//授权
		List<SysUserRole> list = new ArrayList<SysUserRole>();
		SysUserRole sur = null;
		for (String retval: sysRoleId.split(",")) {
			sur = new SysUserRole();
			sur.setSysUserId(sysUserId);
			sur.setSysRoleId(retval);
	        list.add(sur);
	     }
		//先删除用户与角色之间的关系
		long id = adminUserRoleService.deleteById(Long.parseLong(sysUserId));
		//批量插入用户与角色数据
		if(id>=0){
			adminUserRoleService.insertBatch(list);
		}
		ActiveUser activeUser = getUserInfo();
		String user_id = activeUser.getUserId().toString();
		adminUserRoleService.queryUserRoleDate(model,user_id);

		SysUserRole sysUserRole = new SysUserRole();
		sysUserRole.setSysUserId(sysUserId);
		List<SysUserRole> sysUserRoleList =  adminUserRoleService.listBy(sysUserRole);
		model.addAttribute("sysUserRoleList",sysUserRoleList);

		return PAGE_DIRECTORY+"admin_userrole";
	}

}
