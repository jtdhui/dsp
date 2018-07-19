package com.jtd.web.controller.front;

import com.jtd.commons.mybatis.support.CustomerContextHolder;
import com.jtd.commons.page.Pagination;
import com.jtd.web.controller.BaseController;
import com.jtd.web.po.App;
import com.jtd.web.service.front.IFrontAppService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by duber on 2017/3/15.
 */
@Controller
@RequestMapping("/front/app")
public class AppController extends BaseController {

    @Autowired
    private IFrontAppService appService;

    @RequestMapping("/list")
    @ResponseBody
    public Map<String,Object> list(Model model, Integer pageNo,Integer pageSize, String appName,String channelId) {

        Map<String,Object> resultMap = new HashMap<String,Object>();
        // Shiro用户信息
        setUserPageInfo(model);

        Map<String,Object> paraMap = new HashMap<String,Object>();
        if(!StringUtils.isEmpty(appName)) {
            paraMap.put("appName", appName);
        }
        if(!StringUtils.isEmpty(channelId)) {
            paraMap.put("channelId", channelId);
        }

        CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
        Pagination<App> page = appService.findPageBy(paraMap,pageNo, pageSize);
        resultMap.put("page", page);
        resultMap.put("success", true);
        return resultMap;
    }

}
