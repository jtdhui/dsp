package com.jtd.web.controller.admin;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jtd.web.controller.BaseController;
import com.jtd.web.po.SysRole;
import com.jtd.web.service.admin.IAdminRoleService;

@Controller
@RequestMapping("/admin/role")
public class AdminRoleController extends BaseController{

	public static final String PAGE_DIRECTORY="/admin/role/";
	public static final String ACTION_PATH="redirect:/admin/role/";

	@Autowired
	private IAdminRoleService adminRoleService;
	
	@RequestMapping("/list")
	public String list(Model model){
		
		// Shiro用户信息
		setUserPageInfo(model);
		
		List<SysRole> poList=adminRoleService.listAll();
		model.addAttribute("poList",poList);
		return PAGE_DIRECTORY+"admin_role_list";
	}
	
	@RequestMapping("/add")
	@RequiresPermissions("role:create")
	public String add(Model model){
		// Shiro用户信息
		setUserPageInfo(model);
		return PAGE_DIRECTORY+"admin_role_add";
	}
	
	@RequestMapping("/insert")
	@RequiresPermissions("role:create")
	public String add(SysRole role){
		role.setAvailable("1");
		adminRoleService.insert(role);
		return ACTION_PATH+"list.action";
	}
	
	@RequestMapping("/edit")
	@RequiresPermissions("role:update")
	public String edit(Model model,String id){
		SysRole role = adminRoleService.getById(Long.parseLong(id));
		model.addAttribute("po", role);
		// Shiro用户信息
		setUserPageInfo(model);
		return PAGE_DIRECTORY+"admin_role_edit";
	}
	
	@RequestMapping("/update")
	@RequiresPermissions("role:update")
	public String update(Model model, SysRole role){
		adminRoleService.update(role);
		return ACTION_PATH+"list.action";
	}
	
	@RequestMapping("/changeStatus")
	@RequiresPermissions("role:available")
	public String changeStatus(SysRole role,long id,String available){
		role.setId(id);
		role.setAvailable(available);
		adminRoleService.update(role);
		return ACTION_PATH+"list.action";
	}
	
	
}
