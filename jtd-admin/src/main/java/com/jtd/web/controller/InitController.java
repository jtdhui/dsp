package com.jtd.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/init")
public class InitController {
	
	@Autowired
	private InitControllerService initDocService;

	/**
	 * 调用这个方法可以根据所有partner的信息重新发送一遍MQ
	 * 
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/initPartnerMQ")
	public void initPartnerMQ(HttpServletRequest request , HttpServletResponse response) throws Exception{
		
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		
		String partnerId = request.getParameter("partnerId");
		
		initDocService.initPartnerMQ(partnerId , response);
	}

}
