package com.jtd.web.controller.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jtd.web.controller.BaseController;
import com.jtd.web.po.UserFav;
import com.jtd.web.po.UserPartner;
import com.jtd.web.service.admin.IAdminUserFavService;
import com.jtd.web.service.admin.IAdminUserPartnerService;

@Controller
@RequestMapping("/admin/userPartner")
public class AdminUserPartnerController extends BaseController {

	
	@Autowired
	private IAdminUserPartnerService adminUserPartnerService;

	@Autowired
	private IAdminUserFavService adminUserFavService;
	
	@RequestMapping("/save")
	@ResponseBody
	public Map<String,Object> save(Model model,String userId,String partnerId){

		List<UserPartner> list = new ArrayList<UserPartner>();
		UserPartner sur = null;
		for (String retval: partnerId.split(",")) {
			sur = new UserPartner();
			sur.setUserId(userId);
			sur.setPartnerId(retval);
	        list.add(sur);
	     }
		//先删除用户与partner之间的关系
		long id = adminUserPartnerService.deleteById(Long.parseLong(userId));
		//批量插入用户与partner数据
		if(id>=0){
			adminUserPartnerService.insertBatch(list);
		}
		//清空缓存用户与partner
		UserFav userFav = new UserFav();
		userFav.setUserId(Long.parseLong(userId));
		userFav.setSettingName("operatePartnerId");
		adminUserFavService.deleteUserFav(userFav);

		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("success", true);
		return resultMap;
	}
}
