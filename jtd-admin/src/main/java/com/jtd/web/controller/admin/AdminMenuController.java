package com.jtd.web.controller.admin;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jtd.web.controller.BaseController;
import com.jtd.web.po.SysPermission;
import com.jtd.web.service.admin.IAdminPermissionService;

@Controller
@RequestMapping("/admin/menu")
public class AdminMenuController extends BaseController{

	public static final String PAGE_DIRECTORY="/admin/menu/";
	public static final String ACTION_PATH="redirect:/admin/menu/";

	@Autowired
	private IAdminPermissionService adminPermissionService;
	
	@RequestMapping("/list")
	public String list(Model model){
		
		// Shiro用户信息
		setUserPageInfo(model);
		
		List<SysPermission> poList=adminPermissionService.list();
		model.addAttribute("poList",poList);
		return PAGE_DIRECTORY+"admin_menu_list";
	}
	
	@RequestMapping("/add")
	@RequiresPermissions("menu:create")
	public String add(Model model){
		// Shiro用户信息
		setUserPageInfo(model);
		SysPermission sp = new SysPermission();
		sp.setType("menu");
		sp.setLevel("1");
		List<SysPermission> poList=adminPermissionService.listBy(sp);
		model.addAttribute("poList",poList);
		return PAGE_DIRECTORY+"admin_menu_add";
	}
	
	@RequestMapping("/insert")
	@RequiresPermissions("menu:create")
	public String add(SysPermission sp){
		sp.setType("menu");
		sp.setAvailable("1");
		if(sp.getParentId()==0){
			sp.setLevel("1");
		}else{
			sp.setLevel("2");
		}
		adminPermissionService.insert(sp);
		return ACTION_PATH+"list.action";
	}
	
	@RequestMapping("/edit")
	@RequiresPermissions("menu:update")
	public String edit(Model model,String id){
		SysPermission sp = adminPermissionService.getById(Long.parseLong(id));
		model.addAttribute("po", sp);
		// Shiro用户信息
		setUserPageInfo(model);
		
		SysPermission obj = new SysPermission();
		obj.setType("menu");
		obj.setLevel("1");
		List<SysPermission> poList=adminPermissionService.listBy(obj);
		model.addAttribute("poList",poList);
		
		return PAGE_DIRECTORY+"admin_menu_edit";
	}
	
	@RequestMapping("/update")
	@RequiresPermissions("menu:update")
	public String update(Model model, SysPermission sp){
		if(sp.getParentId()==0){
			sp.setLevel("1");
		}else{
			sp.setLevel("2");
		}
		adminPermissionService.update(sp);
		return ACTION_PATH+"list.action";
	}
	
	@RequestMapping("/changeStatus")
	@RequiresPermissions("menu:available")
	public String changeStatus(SysPermission sp,long id,String available){
		sp.setId(id);
		sp.setAvailable(available);
		adminPermissionService.updateStatus(sp);
		return ACTION_PATH+"list.action";
	}
	
	
}
