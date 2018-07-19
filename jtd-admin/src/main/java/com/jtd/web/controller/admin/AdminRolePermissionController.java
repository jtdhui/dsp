package com.jtd.web.controller.admin;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jtd.web.controller.BaseController;
import com.jtd.web.po.SysPermission;
import com.jtd.web.po.SysRole;
import com.jtd.web.po.SysRolePermission;
import com.jtd.web.service.admin.IAdminPermissionService;
import com.jtd.web.service.admin.IAdminRolePermissionService;
import com.jtd.web.service.admin.IAdminRoleService;

@Controller
@RequestMapping("/admin/rolepermission")
public class AdminRolePermissionController extends BaseController{

	public static final String PAGE_DIRECTORY="/admin/rolepermission/";
	public static final String ACTION_PATH="redirect:/admin/rolepermission/";

	@Autowired
	private IAdminRolePermissionService adminRolePermissionService;
	@Autowired
	private IAdminRoleService adminRoleService;
	@Autowired
	private IAdminPermissionService adminPermissionService;
	
	@RequestMapping("/list")
	public String list(Model model){
		
		// Shiro用户信息
		setUserPageInfo(model);
		
		List<SysRole> roleList=adminRoleService.listAll();
		model.addAttribute("roleList",roleList);

		return PAGE_DIRECTORY+"admin_rolepermission";
	}
	
	
	@RequestMapping("/roleSelect")
	public String roleSelect(Model model,String id){
		
		// Shiro用户信息
		setUserPageInfo(model);
		
		List<SysRole> roleList=adminRoleService.listAll();
		model.addAttribute("roleList",roleList);

		List<SysPermission> pageList = new ArrayList<SysPermission>();

		SysPermission sp = new SysPermission();
		sp.setType("menu");
		sp.setLevel("1");
		List<SysPermission> menuOneList=adminPermissionService.listBy(sp);

		sp.setLevel("2");
		List<SysPermission> menuTwoList=adminPermissionService.listBy(sp);

		sp = new SysPermission();
		sp.setType("permission");
		sp.setLevel("1");
		List<SysPermission> permissionList=adminPermissionService.listBy(sp);
		//待优化
		for (SysPermission oneMenu : menuOneList ){
			pageList.add(oneMenu);
			for(SysPermission twoMenu : menuTwoList){
				if(twoMenu.getParentId() == oneMenu.getId()){
					pageList.add(twoMenu);
					for(SysPermission permObj : permissionList){
						if(permObj.getParentId() == twoMenu.getId()){
							pageList.add(permObj);
						}
					}
				}
			}
		}
		model.addAttribute("permissionList",pageList);

		sp.setLevel("2");
		List<SysPermission> permissionFrontList=adminPermissionService.listBy(sp);
		model.addAttribute("permissionFrontList",permissionFrontList);
		
		SysRolePermission SysRolePermission = new SysRolePermission();
		SysRolePermission.setSysRoleId(id);
		List<SysRolePermission> SysRolePermissionList =  adminRolePermissionService.listBy(SysRolePermission);
		if(SysRolePermissionList.size()>0){
			model.addAttribute("sysRolePermissionList",SysRolePermissionList);
		}
		model.addAttribute("role_id",id);
		return PAGE_DIRECTORY+"admin_rolepermission";
	}
	
	
	@RequestMapping("/grant")
	public String grant(Model model,String sysPermissionId,String sysRoleId){
		
		// Shiro用户信息
		setUserPageInfo(model);
		
		//授权
		List<SysRolePermission> list = new ArrayList<SysRolePermission>();
		SysRolePermission sur = null;
		for (String retval: sysPermissionId.split(",")) {
			sur = new SysRolePermission();
			sur.setSysRoleId(sysRoleId);
			sur.setSysPermissionId(retval);
	        list.add(sur);
	     }
		//先删除用户与角色之间的关系
		long id = adminRolePermissionService.deleteById(Long.parseLong(sysRoleId));
		//批量插入用户与角色数据
		if(id>=0){
			adminRolePermissionService.insertBatch(list);
		}
		//查询用户
		List<SysRole> roleList=adminRoleService.listAll();
		model.addAttribute("roleList",roleList);

		List<SysPermission> pageList = new ArrayList<SysPermission>();

		SysPermission sp = new SysPermission();
		sp.setType("menu");
		sp.setLevel("1");
		List<SysPermission> menuOneList=adminPermissionService.listBy(sp);

		sp.setLevel("2");
		List<SysPermission> menuTwoList=adminPermissionService.listBy(sp);

		sp = new SysPermission();
		sp.setType("permission");
		List<SysPermission> permissionList=adminPermissionService.listBy(sp);
		//待优化
		for (SysPermission oneMenu : menuOneList ){
			pageList.add(oneMenu);
			for(SysPermission twoMenu : menuTwoList){
				if(twoMenu.getParentId() == oneMenu.getId()){
					pageList.add(twoMenu);
					for(SysPermission permObj : permissionList){
						if(permObj.getParentId() == twoMenu.getId()){
							pageList.add(permObj);
						}
					}
				}
			}
		}
		model.addAttribute("permissionList",pageList);

		sp.setLevel("2");
		List<SysPermission> permissionFrontList=adminPermissionService.listBy(sp);
		model.addAttribute("permissionFrontList",permissionFrontList);

		SysRolePermission SysRolePermission = new SysRolePermission();
		SysRolePermission.setSysRoleId(sysRoleId);
		List<SysRolePermission> sysRolePermissionList =  adminRolePermissionService.listBy(SysRolePermission);
		if(sysRolePermissionList.size()>0){
			model.addAttribute("sysRolePermissionList",sysRolePermissionList);
		}
		model.addAttribute("role_id",sysRoleId);
		return PAGE_DIRECTORY+"admin_rolepermission";
	}
	
}
