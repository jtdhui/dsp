package com.jtd.web.controller.admin;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jtd.web.controller.BaseController;
import com.jtd.web.po.ActiveUser;

@Controller
@RequestMapping("/admin")
public class AdminHomeController extends BaseController {

	public static final String DIRECTORY="/admin/";

	@RequestMapping("/home")
	public String home(Model model){
		setUserPageInfo(model);
		return DIRECTORY+"home";
	}
}
