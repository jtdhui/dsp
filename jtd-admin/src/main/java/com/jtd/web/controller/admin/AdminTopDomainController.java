package com.jtd.web.controller.admin;

import com.jtd.commons.page.Pagination;
import com.jtd.web.controller.BaseController;
import com.jtd.web.po.TopDomain;
import com.jtd.web.service.admin.IAdminTopDomainService;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


import java.util.*;

/**
 * Created by duber on 2017/2/24.
 */
@Controller
@RequestMapping("/admin/domain")
public class AdminTopDomainController extends BaseController {

    public static final String PAGE_DIRECTORY = "/admin/topdomain/";
    public static final String ACTION_PATH = "redirect:/admin/domain/";

    @Autowired
    private IAdminTopDomainService adminTopDomainService;

    @RequestMapping("/list")
    public String list(Model model, Integer pageNo, Integer pageSize,String domain,String manualEntry ) {

        // Shiro用户信息
        setUserPageInfo(model);
        Map<String,Object> paraMap = new HashMap<String,Object>();
        if(StringUtils.isNotEmpty(domain)) {
            paraMap.put("domain",domain);
        }
        if(StringUtils.isNotEmpty(manualEntry)) {
            paraMap.put("manualEntry",manualEntry);
        }
        Pagination<TopDomain> page = adminTopDomainService.findDomainList(model,pageNo,pageSize,paraMap);
        model.addAttribute("page", page);
        model.addAttribute("queryMap", paraMap);
        return PAGE_DIRECTORY + "admin_domain_list";
    }

    @RequestMapping("/edit")
    public String edit(Model model,String id){

        if(StringUtils.isNotEmpty(id)){
            TopDomain topDomain = adminTopDomainService.getById(Long.parseLong(id));
            model.addAttribute("po",topDomain);
        }
        return PAGE_DIRECTORY + "admin_domain_edit";
    }

    @RequestMapping("/save")
    public String save(Model model,TopDomain topDomain){

        if(topDomain.getId()==null){
            adminTopDomainService.insert(topDomain);
        }else{
            adminTopDomainService.update(topDomain);
        }
        return ACTION_PATH + "list.action";
    }
}
