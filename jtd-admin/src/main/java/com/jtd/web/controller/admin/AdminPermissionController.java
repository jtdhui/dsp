package com.jtd.web.controller.admin;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jtd.web.controller.BaseController;
import com.jtd.web.po.SysPermission;
import com.jtd.web.service.admin.IAdminPermissionService;

@Controller
@RequestMapping("/admin/permission")
public class AdminPermissionController extends BaseController{

	public static final String PAGE_DIRECTORY="/admin/permission/";
	public static final String ACTION_PATH="redirect:/admin/permission/";

	@Autowired
	private IAdminPermissionService adminPermissionService;
	
	@RequestMapping("/list")
	public String list(Model model){
		
		// Shiro用户信息
		setUserPageInfo(model);
		SysPermission sp = new SysPermission();
		sp.setType("permission");
		List<Map<String,Object>> poList = adminPermissionService.listMapBy(sp);
		model.addAttribute("poList",poList);
		return PAGE_DIRECTORY+"admin_permission_list";
	}
	
	@RequestMapping("/add")
	public String add(Model model){
		// Shiro用户信息
		setUserPageInfo(model);
		SysPermission sp = new SysPermission();
		sp.setType("permission");
		List<SysPermission> poList = adminPermissionService.listBy(sp);
		
		sp.setType("menu");
		sp.setLevel("2");
		List<SysPermission> levelTwoMenuList = adminPermissionService.listBy(sp);
		
		model.addAttribute("poList",poList);
		model.addAttribute("levelTwoMenuList",levelTwoMenuList);
		
		return PAGE_DIRECTORY+"admin_permission_add";
	}
	
	@RequestMapping("/insert")
	public String add(SysPermission sp){
		sp.setAvailable("1");
		sp.setType("permission");
		adminPermissionService.insert(sp);
		return ACTION_PATH+"list.action";
	}
	
	@RequestMapping("/edit")
	public String edit(Model model,String id){
		
		// Shiro用户信息
		setUserPageInfo(model);
		
		SysPermission sp = adminPermissionService.getById(Long.parseLong(id));
		model.addAttribute("po", sp);
		
		SysPermission spParam = new SysPermission();
		spParam.setType("menu");
		spParam.setLevel("2");
		List<SysPermission> levelTwoMenuList = adminPermissionService.listBy(spParam);
		model.addAttribute("levelTwoMenuList",levelTwoMenuList);
		
		return PAGE_DIRECTORY+"admin_permission_edit";
	}
	
	@RequestMapping("/update")
	public String update(Model model, SysPermission sp){
		if(sp.getUrl().isEmpty()){
			sp.setUrl("");
		}
		adminPermissionService.update(sp);
		return ACTION_PATH+"list.action";
	}
	
	@RequestMapping("/changeStatus")
	public String changeStatus(SysPermission sp,long id,String available){
		sp.setId(id);
		sp.setAvailable(available);
		adminPermissionService.update(sp);
		return ACTION_PATH+"list.action";
	}
	
	
}
