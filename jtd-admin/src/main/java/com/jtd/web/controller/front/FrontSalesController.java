package com.jtd.web.controller.front;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jtd.web.controller.BaseController;

@Controller
@RequestMapping("/front/sales")
public class FrontSalesController extends BaseController {

	public static final String PAGE_DIRECTORY = "/front/sales/";
	public static final String ACTION_PATH = "redirect:/front/sales/";

	/**
	 * 到sales_list.jsp
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/toList")
	public String toList(Model model) {
		
		setUserPageInfo(model);
		
		return PAGE_DIRECTORY + "sales_list";
	}

}
