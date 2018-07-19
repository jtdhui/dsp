package com.jtd.web.controller.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jtd.web.po.SysUser;
import com.jtd.web.service.admin.IAdminUserService;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年10月5日
 * @项目名称 dsp-admin
 * @描述 <p></p>
 */
@Controller
@RequestMapping("/test")
public class Test {
	
	public static final String DIRECTORY="/test/";
	
	@Autowired
	private IAdminUserService adminUserService;
	
//	@Autowired
//	private ISysService sysService;
	
	@RequestMapping("/home")
	public String home(){
		return DIRECTORY+"home";
	}
	
	@RequestMapping("/testlist")
	public String userAllList(Model model) throws Exception{
		
//		List<SysPermission> list=sysService.findMenuListByUserId(new Integer(1));
		
		List<SysUser> userList=adminUserService.listAll();
		model.addAttribute("userList",userList);
		return DIRECTORY+"userlist";
	}
	
	@RequestMapping("/testaddpage")
	public String testAddPage(SysUser user){
		
		return DIRECTORY+"useradd";
	}
	
	@RequestMapping("/testadd")
	public String testAddSubmit(SysUser user){
		adminUserService.insert(user);
		return "redirect:testlist.action";
	}
	
	//,method=RequestMethod.POST
	@RequestMapping(value="/testupdate")
	public @ResponseBody Map<String, String> testUpdate(Model model, @RequestBody SysUser user){
		adminUserService.update(user);
		
		Map<String, String> retMap=new HashMap<String,String>();
		retMap.put("url", "testlist.action");
		return retMap;
	}
	
	@RequestMapping("/testdelete")
	public @ResponseBody Map<String, String> testDelete(@RequestBody SysUser user){
		adminUserService.deleteById(user.getId());
		Map<String, String> retMap=new HashMap<String,String>();
		retMap.put("url", "testlist.action");
		return retMap;
	}
	
}
