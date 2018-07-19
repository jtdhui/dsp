package com.jtd.web.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.jtd.commons.page.Pagination;
import com.jtd.web.controller.BaseController;
import com.jtd.web.po.SysLog;
import com.jtd.web.service.admin.IAdminSysLogService;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by duber on 2017/2/24.
 */
@Controller
@RequestMapping("/admin/log")
public class AdminLogController extends BaseController {

    public static final String PAGE_DIRECTORY = "/admin/log/";
    public static final String ACTION_PATH = "redirect:/admin/log/";

    @Autowired
    private IAdminSysLogService sysLogService;

    @RequestMapping("/list")
    public String list(Model model, Integer pageNo, Integer pageSize,String name,String type ) {

        // Shiro用户信息
        setUserPageInfo(model);
        Map<String,Object> paraMap = new HashMap<String,Object>();
        if(StringUtils.isNotEmpty(name)) {
            paraMap.put("name",name);
        }
        if(StringUtils.isNotEmpty(type)) {
            paraMap.put("type",type);
        }
        Pagination<SysLog> page = sysLogService.findPageBy(paraMap,pageNo,pageSize);
        model.addAttribute("page", page);
        model.addAttribute("queryMap", paraMap);
        return PAGE_DIRECTORY + "admin_log_list";
    }

    @RequestMapping("/edit")
    public String edit(Model model,String id){

        if(StringUtils.isNotEmpty(id)){
            SysLog sysLog = sysLogService.getById(Long.parseLong(id));
            String descStr = sysLog.getOperateDesc();
            JSONObject jsonObject = JSONObject.parseObject(descStr);
            JSONObject before = JSONObject.parseObject(jsonObject.get("before").toString());
            JSONObject after = JSONObject.parseObject(jsonObject.get("after").toString());
            JSONObject desc = JSONObject.parseObject(jsonObject.get("desc").toString());

            model.addAttribute("desc",desc);
            model.addAttribute("before",before);
            model.addAttribute("after",after);
            model.addAttribute("po",sysLog);
        }
        return PAGE_DIRECTORY + "admin_log_edit";
    }

}
