package com.jtd.web.controller.admin;

import com.jtd.commons.mybatis.support.CustomerContextHolder;
import com.jtd.commons.page.Pagination;
import com.jtd.web.controller.BaseController;
import com.jtd.web.exception.PlatformException;
import com.jtd.web.po.ReportDemoDataSetting;
import com.jtd.web.service.admin.IAdminReportDemoDataSettingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by duber on 2017/3/10.
 */
@Controller
@RequestMapping("/admin/simulated")
public class AdminSimulatedController extends BaseController {

    public static final String PAGE_DIRECTORY = "/admin/simulated/";
    public static final String ACTION_PATH = "redirect:/admin/simulated/";

    @Autowired
    private IAdminReportDemoDataSettingService settingService;

    /**
     * 测试用户模拟投放列表
     * @param model
     * @param pageNo
     * @param pageSize
     * @param request
     * @return
     * @throws PlatformException
     */
    @RequestMapping("/list")
    public String list(Model model, Integer pageNo, Integer pageSize, HttpServletRequest request) throws PlatformException {

        HashMap<String, Object> paraMap = new HashMap<String, Object>();
        if (StringUtils.isEmpty(request.getParameter("partnerName")) == false) {
            paraMap.put("partnerName", request.getParameter("partnerName"));
        }
        model.addAttribute("queryMap", paraMap);
        CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
        Pagination<Map<String, Object>> page = settingService.findMapPageBy(paraMap,pageNo, pageSize);
        model.addAttribute("page", page);
        return PAGE_DIRECTORY + "list";
    }

    @RequestMapping("/edit")
    public String edit(Model model,String id,String partnerId) throws PlatformException {

        HashMap<String, Object> paraMap = new HashMap<String, Object>();
        ReportDemoDataSetting po = new ReportDemoDataSetting();
        if(StringUtils.isEmpty(id)){ //新增
            if(!StringUtils.isEmpty(partnerId)){
                po.setPartnerId(Long.parseLong(partnerId));
            }
            po.setGrossProfit(BigDecimal.valueOf(0.3));
            po.setMinPv(10000);
            po.setMaxPv(20000);
            po.setMinUvRatio(BigDecimal.valueOf(0.9));
            po.setMaxUvRatio(BigDecimal.valueOf(0.95));
            po.setMinUclickRatio(BigDecimal.valueOf(0.9));
            po.setMaxUclickRatio(BigDecimal.valueOf(0.95));
        }else { //编辑
            long pk_id = Long.parseLong(id);
            CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
            po = settingService.getById(pk_id);
        }
        model.addAttribute("po",po);
        return PAGE_DIRECTORY + "edit";
    }

    @RequestMapping("/save")
    @ResponseBody
    public Map<String,Object> save( @RequestBody ReportDemoDataSetting po) {

        if(po.getId()==null){
            settingService.insert(po);
        }else{
            settingService.update(po);
        }
        Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("url", "list.action");
        return resultMap;
    }


    @RequestMapping("/delete")
    public String delete(Model model,String id) throws PlatformException {

        if(StringUtils.isEmpty(id)){

        }else { //
            long pk_id = Long.parseLong(id);
            CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
            settingService.deleteById(pk_id);
        }
        return ACTION_PATH + "list.action";
    }
}
