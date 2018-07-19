package com.jtd.web.controller.admin;

import com.jtd.web.constants.RoleType;
import com.jtd.web.controller.BaseController;
import com.jtd.web.po.*;
import com.jtd.web.service.admin.IAdminPartnerService;
import com.jtd.web.service.admin.IAdminUserPartnerService;
import com.jtd.web.service.admin.IAdminUserRoleService;
import com.jtd.web.service.admin.IAdminUserService;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/user")
public class AdminUserController extends BaseController {

	public static final String PAGE_DIRECTORY = "/admin/user/";
	public static final String ACTION_PATH = "redirect:/admin/user/";

	@Autowired
	private IAdminUserService adminUserService;

	@Autowired
	private IAdminPartnerService partnerService;

	@Autowired
	private IAdminUserPartnerService adminUserPartnerService;

	@Autowired
	private IAdminUserRoleService adminUserRoleService;

	@RequestMapping("/list")
	public String list(Model model, Integer pageNo, Integer pageSize, HttpServletRequest request) {

		// Shiro用户信息
		setUserPageInfo(model);

		ActiveUser activeUser = getUserInfo();
		adminUserService.findUserList(model,request,pageNo,pageSize,activeUser);
		return PAGE_DIRECTORY + "admin_user_list";
	}

	@RequestMapping("/add")
	@RequiresPermissions("user:create")
	public String add(Model model) {
		// Shiro用户信息
		setUserPageInfo(model);
		return PAGE_DIRECTORY + "admin_user_add";
	}

	@RequestMapping("/addsubmit")
	@RequiresPermissions("user:create")
	@ResponseBody
	public Map<String, Object> addsubmit(@RequestBody SysUser user,String roleId) {
		user.setStatus(0); //默认启用
        SysUser sysUser = adminUserService.findUserByLoginName(user.getLoginName());
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if(sysUser == null) {
			long id = adminUserService.insert(user);
			SysUserRole userRole = new SysUserRole();
			userRole.setSysUserId(user.getId().toString());
			userRole.setSysRoleId(roleId);
			adminUserRoleService.insert(userRole);
			resultMap.put("url", "list.action");
			resultMap.put("userId", id);
			resultMap.put("success", true);
		}else {
			resultMap.put("msg", "登录名已存在,请重新定义用户名!");
			resultMap.put("success", false);
		}
		return resultMap;
	}

	@RequestMapping("/edit")
	@RequiresPermissions("user:update")
	public String edit(Model model, String id) {
		// Shiro用户信息
		setUserPageInfo(model);
//        ActiveUser activeUser = getUserInfo();

		SysUser user = adminUserService.getById(Long.parseLong(id));
		model.addAttribute("po", user);

        //所属公司
        Partner partner = partnerService.getById(user.getPartnerId());
        model.addAttribute("partner", partner);

		List<Map<String,Object>> roleTypeList = new ArrayList<Map<String,Object>>();
		Map<String,Object> map = null;
		for (RoleType r : RoleType.values()) {

			map = new HashMap<String,Object>();
			if(r == RoleType.ADMIN && user.getId()>1){
				continue;
			}
			map.put("code",r.getCode());
			map.put("desc",r.getDesc());
			if(user.getPartnerId() > 1){ //其它公司
				// 直客 代理 直客只读
				if(r == RoleType.PARTNER || r == RoleType.PARTNER_PROXY || r == RoleType.PARTNER_READONLY){
					roleTypeList.add(map);
				}
			}else {
				if(r != RoleType.PARTNER && r != RoleType.PARTNER_PROXY && r != RoleType.PARTNER_READONLY ){
					roleTypeList.add(map);
				}
			}
		}
		model.addAttribute("roleTypeList", roleTypeList);
		SysUserRole userRole = new SysUserRole();
		userRole.setSysUserId(id);
		List<SysUserRole> sysUserRoleList = adminUserRoleService.listBy(userRole);
		if(sysUserRoleList.size()>0){
			model.addAttribute("userRole",sysUserRoleList.get(0));
		}

		// 用户与广告主关联数据
        UserPartner up = new UserPartner();
		up.setUserId(id);
		List<UserPartner> upList = adminUserPartnerService.listBy(up);
		model.addAttribute("upList", upList);

		return PAGE_DIRECTORY + "admin_user_edit";
	}

	@RequestMapping("/update")
	@RequiresPermissions("user:update")
	@ResponseBody
	public Map<String, Object> update(Model model, @RequestBody SysUser user,String roleId) {
		adminUserService.update(user);
		SysUserRole userRole = new SysUserRole();
		userRole.setSysUserId(user.getId().toString());

		List<SysUserRole> sysUserRoleList = adminUserRoleService.listBy(userRole);
        userRole.setSysRoleId(roleId);
		if(sysUserRoleList.size()>0) {
			adminUserRoleService.update(userRole);
		}else{
			adminUserRoleService.insert(userRole);
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("url", "list.action");
		resultMap.put("userId", user.getId());
		resultMap.put("success", true);
		return resultMap;
	}

	@RequestMapping("/changeStatus")
	@RequiresPermissions("user:status")
	public String changeStatus(SysUser user, long id, int status) {
		user.setId(id);
		user.setStatus(status);
		adminUserService.update(user);
		return ACTION_PATH + "list.action";
	}

	@RequestMapping("/changePassword")
	@RequiresPermissions("user:password")
	public String changePassword(Model model, HttpServletRequest request) {

		// Shiro用户信息
		setUserPageInfo(model);

		String action = request.getParameter("action");
		if (action != null && action.equals("save")) {
			
			long userId = Long.parseLong(request.getParameter("userId"));
			String newPassword = request.getParameter("password");
			
			adminUserService.changePassword(userId, newPassword);
			
			return ACTION_PATH + "list.action";
		}
		else{
			
			long id = Long.parseLong(request.getParameter("id"));
			
			SysUser user = adminUserService.getById(id);
			
			model.addAttribute("user", user);
			return PAGE_DIRECTORY + "admin_user_password";
		}
	}

}
