package com.jtd.web.controller.third;

import com.jtd.commons.mybatis.support.CustomerContextHolder;
import com.jtd.web.po.SysContack;
import com.jtd.web.service.third.ISysContackService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by duber on 2017/6/22.
 */
@Controller
@RequestMapping("/contact")
public class LandingPageController {

    private Logger log = Logger.getLogger(LandingPageController.class);

    public static final String PAGE_DIRECTORY = "/landingpage/";
    public static final String ACTION_PATH = "redirect:/landingpage/";

    @Autowired
    private ISysContackService sysContackService;

    @RequestMapping("/save")
    @ResponseBody
    public String save(Model model, SysContack sysContack,HttpServletRequest request){
        String retStr = "您提交的信息成功，我们会快速和您联系。";

        log.info(sysContack.getName() + "----" + sysContack.getMobile() );
        try {
            CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
            sysContack.setRequestUrl(request.getRequestURL().toString()+"?"+request.getQueryString());
            sysContackService.insert(sysContack);
        }catch (Exception e){
            e.printStackTrace();
            retStr ="您提交的信息失败,请您重新提交,谢谢。";
        }
        return retStr;
    }

}
