package com.jtd.web.controller;

import com.jtd.commons.mybatis.support.CustomerContextHolder;
import com.jtd.web.po.Partner;
import com.jtd.web.service.front.IFrontPartnerService;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置
 * @创建日期 2016年9月30日
 * @项目名称 dsp-admin
 * @描述
 *     <p>
 *     </p>
 */
@Controller
public class LoginController {

	@Autowired
	private IFrontPartnerService partnerService;

	// 登陆提交地址，和applicationContext-shiro.xml中配置的loginurl一致
	@RequestMapping("login")
	public String login(Model model , HttpServletRequest request) throws Exception {
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		Partner partner = new Partner();
		if(StringUtils.isNotEmpty(request.getParameter("p"))) { // 如果有广告主ID参数存在,则以该广告主的模板为准
			String partner_id = request.getParameter("p");
			partner = partnerService.getById(Long.parseLong(partner_id));
		}else{
			String host = request.getHeader("Host");
            if(host.contains("www.xinmt.com")){
                partner.setId(1L);
            }
			partner.setLoginUrl(host);
			List<Partner> partnerList = partnerService.listBy(partner);
			if(partnerList.size()>0){
				partner = partnerList.get(0);
			}
		}
		if(partner != null) {
			model.addAttribute("partner", partner);
		}
		Calendar calendar=Calendar.getInstance();
		model.addAttribute("year", calendar.get(Calendar.YEAR));
        model.addAttribute("d",new Date());
		// 如果登陆失败从request中获取认证异常信息，shiroLoginFailure就是shiro异常类的全限定名
		String exceptionClassName = (String) request.getAttribute("shiroLoginFailure");
		
		String msg = "" ;
		// 根据shiro返回的异常类路径判断，抛出指定异常信息
		if (exceptionClassName != null) {
			if (UnknownAccountException.class.getName().equals(exceptionClassName)) {
				
				msg = "账号不存在" ;
				
				// 最终会抛给异常处理器
				//throw new PlatformException("账号不存在");
			} else if (IncorrectCredentialsException.class.getName().equals(exceptionClassName)) {
				
				msg = "用户名/密码错误" ;
				
				//throw new PlatformException("用户名/密码错误");
			} else if ("randomCodeError".equals(exceptionClassName)) {
				
				msg = "验证码错误" ;
				
				//throw new PlatformException("验证码错误 ");
			} else {
				
				msg = "未知错误" ;
				
				//throw new PlatformException("未知错误");// 最终在异常处理器生成未知错误
			}
		}
		else{
			Subject subject = SecurityUtils.getSubject();
			if(subject != null && (subject.isAuthenticated() || subject.isRemembered())){
				return "redirect:/front/home.action" ;
			}
		}
		
		model.addAttribute("msg", msg);
		
		// 此方法不处理登陆成功（认证成功），shiro认证成功会自动跳转到上一个请求路径
		// 登陆失败还到login页面
		return "/login";
	}

}
