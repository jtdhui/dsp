package com.jtd.tencent.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jtd.tencent.Config;
import com.jtd.tencent.service.AuthService;

/**
 * 腾讯回调地址控制器
 * @author zl
 *
 */

@Controller
@RequestMapping("/qq")
public class CallbackController 
{
	@Autowired(required=false)
	private HttpServletRequest request;
	
	@Autowired(required=false)
	private HttpServletResponse response;
	
	@Autowired
	private AuthService authService;
	
	@RequestMapping("/toAuth")
	public String toAuthPath(Model model)
	{
		model.addAttribute("requestUrl", Config.TENCENT_HOST + "/oauth/authorize?redirect_uri=http://" + Config.SERVER_HOST + "/qq/auth.action");
		return "/tencent/auth/doAuth";
	}
	
	/**
	 * 腾讯授权token
	 * 授权需要获取AuthorizationCode，再请求token地址，传入AuthorizationCode获取token
	 */
	@RequestMapping("/auth")
	public void authToken()
	{
		String authorizationCode = request.getParameter("authorization_code");
		if(authorizationCode == null || authorizationCode.trim().length() == 0)
			return ;
		authService.getToken(1, authorizationCode);
	}
	
}
