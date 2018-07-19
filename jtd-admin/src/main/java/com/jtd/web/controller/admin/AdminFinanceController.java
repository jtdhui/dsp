package com.jtd.web.controller.admin;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jtd.commons.mybatis.support.CustomerContextHolder;
import com.jtd.utils.DateUtil;
import com.jtd.web.controller.BaseController;
import com.jtd.web.exception.PlatformException;
import com.jtd.web.po.Partner;
import com.jtd.web.service.admin.IAdminFinanceService;
import com.jtd.web.service.admin.IAdminPartnerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.*;


/**
 * 财务管理
 * Created by duber on 2017/6/19.
 */
@Controller
@RequestMapping("/admin/finance")
public class AdminFinanceController extends BaseController {

    public static final String PAGE_DIRECTORY = "/admin/finance/";
    public static final String ACTION_PATH = "redirect:/admin/finance/";

    @Autowired
    private IAdminFinanceService adminFinanceService;
    
    @Autowired
    private IAdminPartnerService adminPartnerService; 

    /**
     * 财务报表
     * @param model
     * @param request
     * @return
     */
    @RequestMapping("/list")
    public String list(Model model, HttpServletRequest request) {
    	
        Map<String,Object> paraMap  = new HashMap<String,Object>();
        if (StringUtils.isEmpty(request.getParameter("partnerName")) == false) {
            paraMap.put("partnerName", request.getParameter("partnerName"));
        }
        String partner_type = "0";
        if (StringUtils.isEmpty(request.getParameter("partner_type")) == false) {
            paraMap.put("partner_type", request.getParameter("partner_type"));
        }
        if("2".equals(partner_type)){
            paraMap.put("boss_partner_code", 1);
        }
        Calendar calendar = Calendar.getInstance();
        String startDate = calendar.get(calendar.YEAR)+"-01-01";
        if (StringUtils.isEmpty(request.getParameter("startDate")) == false) {
            startDate = request.getParameter("startDate");
        }
        paraMap.put("startDate", startDate);

        String endDate = DateUtil.getDateStr(new Date(),"yyyy-MM-dd");
        if (StringUtils.isEmpty(request.getParameter("endDate")) == false) {
            endDate = request.getParameter("endDate");
        }

        paraMap.put("endDate", endDate + " 23:59:59");
        model.addAttribute("queryMap",paraMap);

        List<Map<String,Object>> resultList = adminFinanceService.listFinanceBy(paraMap);
        paraMap.put("endDate", endDate);
        model.addAttribute("mapList",resultList);

        return PAGE_DIRECTORY+"admin_finance_list";
    }
    
    /**
     * 充值明细
     * @param model
     * @param request
     * @return
     */
    @RequestMapping("/finaceDetailList")
    public String finaceDetailList(Model model, HttpServletRequest request){
    	
    	CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
    	Map<String, Object> paraMap = new HashMap<String, Object>();
    	if (StringUtils.isEmpty(request.getParameter("partnerId")) == false) {
    		paraMap.put("partnerId", request.getParameter("partnerId"));
		}
    	if (StringUtils.isEmpty(request.getParameter("partnerName")) == false) {
    		paraMap.put("partnerName", request.getParameter("partnerName"));
		}
    	Calendar calendar = Calendar.getInstance();
        String startDate = calendar.get(calendar.YEAR)+"-01-01";
        if (StringUtils.isEmpty(request.getParameter("startDate")) == false) {
            startDate = request.getParameter("startDate");
        }
        paraMap.put("startDate", startDate);

        String endDate = DateUtil.getDateStr(new Date(),"yyyy-MM-dd");
        if (StringUtils.isEmpty(request.getParameter("endDate")) == false) {
            endDate = request.getParameter("endDate");
        }
        paraMap.put("endDate", endDate);
    	
    	model.addAttribute("paraMap", paraMap);
    	model.addAttribute("po",paraMap);
    	//查询充值明细广告主名称
        Partner p = new Partner();
        p.setPid(-1L);
        p.setPartnerType(1L);
        p.setBossPartnerCode("123");
        try {
        	if (StringUtils.isEmpty(request.getParameter("partnerId")) == false) {
        		long id = Long.parseLong(request.getParameter("partnerId"));
        		Partner partner = adminPartnerService.getById(id);
        		model.addAttribute("po",partner);
    		}
        	
        	List<Partner> listPartners = adminPartnerService.listOneLevelPartnerBy(p);

        	//查询明细统计
        	List<Map<String, Object>> resultList = adminFinanceService.getListFinaceDetail(paraMap,null);

        	model.addAttribute("list", resultList);
        	model.addAttribute("partners", listPartners);

		} catch (Exception e) {
			logger.error("查询明细统计出错",e);
		}
		
    	return PAGE_DIRECTORY+"admin_finance_detail_list";
    }
    
    /**
	 * 充值明细报表下载
	 * 
	 * @param model
	 * @param request
	 * @return
	 * @throws PlatformException
	 */
    @RequestMapping("/downloadFinaceDetailExcelExcel")
    public void downloadFinaceDetailExcelExcel(Model model, HttpServletRequest request, HttpServletResponse response)
    	throws PlatformException{
    	
    	CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
    	Map<String, Object> paraMap = new HashMap<String, Object>();
//    	if (StringUtils.isEmpty(request.getParameter("partnerId")) == false) {
//    		paraMap.put("partnerId", request.getParameter("partnerId"));
//		}
    	if (StringUtils.isEmpty(request.getParameter("partnerName")) == false) {
    		paraMap.put("partnerName", request.getParameter("partnerName"));
		}
    	Calendar calendar = Calendar.getInstance();
        String startDate = calendar.get(calendar.YEAR)+"-01-01";
        if (StringUtils.isEmpty(request.getParameter("startDate")) == false) {
            startDate = request.getParameter("startDate");
        }
        paraMap.put("startDate", startDate);

        String endDate = DateUtil.getDateStr(new Date(),"yyyy-MM-dd");
        if (StringUtils.isEmpty(request.getParameter("endDate")) == false) {
            endDate = request.getParameter("endDate");
        }
        paraMap.put("endDate", endDate);
    	
    	model.addAttribute("paraMap", paraMap);
    	//查询充值明细广告主名称
        Partner p = new Partner();
        p.setPid(-1L);
        p.setPartnerType(1L);
        p.setBossPartnerCode("123");
        try {
        	 adminFinanceService.getListFinaceDetail(paraMap,response);

		} catch (Exception e) {
			logger.error("查询明细统计出错",e);
		}
    	
    }
    
    /**
     * 代理商充值明细
     * @param model
     * @param request
     * @return
     */
    @RequestMapping("/proxyFinaceDetailList")
    public String proxyFinaceDetailList(Model model, HttpServletRequest request){
		
    	CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
    	Map<String, Object> paraMap = new HashMap<String, Object>();
    	if (StringUtils.isEmpty(request.getParameter("partnerId")) == false) {
    		paraMap.put("partnerId", request.getParameter("partnerId"));
		}
		if (StringUtils.isEmpty(request.getParameter("partnerId2")) == false) {
    		paraMap.put("partnerId2", request.getParameter("partnerId2"));
		}
    	Calendar calendar = Calendar.getInstance();
        String startDate = calendar.get(calendar.YEAR)+"-01-01";
        if (StringUtils.isEmpty(request.getParameter("startDate")) == false) {
            startDate = request.getParameter("startDate");
        }
        paraMap.put("startDate", startDate);

        String endDate = DateUtil.getDateStr(new Date(),"yyyy-MM-dd");
        if (StringUtils.isEmpty(request.getParameter("endDate")) == false) {
            endDate = request.getParameter("endDate");
        }
        paraMap.put("endDate", endDate);

    	model.addAttribute("paraMap", paraMap);
    	//查询代理充值明细广告主名称
        Partner p = new Partner();
        p.setPid(-1L);
        p.setPartnerType(0L);
		try {
			List<Partner> listPartners = adminPartnerService.listOneLevelPartnerBy(p);
			//查询明细统计
			List<Map<String,Object>> resultList = adminFinanceService.getProxyFinaceDetailList(paraMap,null);
			
			Collections.sort(resultList,new Comparator<Map<String,Object>>() {

	            public int compare(Map<String, Object> o1,Map<String, Object> o2) {

	                //o1，o2是list中的Map，可以在其内取得值，按其排序，此例为降序，s1和s2是排序字段值
	            	Date s1 = (Date) o1.get("trade_time");
	            	Date s2 = (Date) o2.get("trade_time");
	                
	                return s1.compareTo(s2);
	            }
	        });
			
			model.addAttribute("list", resultList);
			model.addAttribute("partners", listPartners);
		} catch (Exception e) {
			logger.error("查询明细统计出错",e);
		}
    	
    	return PAGE_DIRECTORY+"admin_proxy_finance_detail_list";
    }
    
    /**
	 * 代理充值明细报表下载
	 * 
	 * @param model
	 * @param request
	 * @return
	 * @throws PlatformException
	 */
    @RequestMapping("/downloadProxyFinaceDetailExcelExcel")
    public void downloadProxyFinaceDetailExcelExcel(Model model, HttpServletRequest request, HttpServletResponse response)
    	throws PlatformException{

    	CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
    	Map<String, Object> paraMap = new HashMap<String, Object>();
    	if (StringUtils.isEmpty(request.getParameter("partnerId")) == false) {
    		paraMap.put("partnerId", request.getParameter("partnerId"));
		}
		if (StringUtils.isEmpty(request.getParameter("partnerId2")) == false) {
    		paraMap.put("partnerId2", request.getParameter("partnerId2"));
		}
    	Calendar calendar = Calendar.getInstance();
        String startDate = calendar.get(calendar.YEAR)+"-01-01";
        if (StringUtils.isEmpty(request.getParameter("startDate")) == false) {
            startDate = request.getParameter("startDate");
        }
        paraMap.put("startDate", startDate);
        String endDate = DateUtil.getDateStr(new Date(),"yyyy-MM-dd");
        if (StringUtils.isEmpty(request.getParameter("endDate")) == false) {
            endDate = request.getParameter("endDate");
        }
        paraMap.put("endDate", endDate);

    	model.addAttribute("paraMap", paraMap);
    	//查询代理充值明细广告主名称
        Partner p = new Partner();
        p.setPid(-1L);
        p.setPartnerType(0L);
		try {
			//List<Partner> listPartners = adminPartnerService.listOneLevelPartnerBy(p);
			//查询明细统计
			 adminFinanceService.getProxyFinaceDetailList(paraMap,response);
		} catch (Exception e) {
			logger.error("代理商明细报表下载出错",e);
		}
    }
    
    
    /**
     * 查询代理商下级广告主
     * @param request
     * @return
     */
    @RequestMapping("/getLevelPartnerName")
    public void getLevelPartnerName(HttpServletRequest request,HttpServletResponse response) throws PlatformException {
    	List<Partner> list = new ArrayList<Partner>();
    	long pid = Long.parseLong(request.getParameter("pid"));
    	try {
            CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
    		list = adminPartnerService.listById(pid);
		} catch (Exception e) {
			logger.error("根据pid查询代理商下级广告主出错",e);
		}
    	JSONObject json = new JSONObject();
    	json.put("list", list);
    	try {
    		response.setCharacterEncoding("utf-8");
			response.getWriter().print(json);
		} catch (IOException e) {
			e.printStackTrace();
		}

    }


    /**
     * 代理广告主帐户余额
     * @param model
     * @param request
     * @return
     */
    @RequestMapping("/proxyAccBalanceList")
    public String proxyAccBalanceList(Model model, HttpServletRequest request) {

        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("pid",0);

        List<Partner> list = new ArrayList<Partner>();
        if(StringUtils.isEmpty(request.getParameter("partnerId")) == false) {
            Long partnerId = Long.parseLong(request.getParameter("partnerId"));
            paraMap.put("id",partnerId);
            list = adminPartnerService.listChildPartnerByMap(paraMap);
        }
        model.addAttribute("list",list);

        return PAGE_DIRECTORY + "admin_finance_accBalance_list";
    }


    /**
     * 消费明细
     * @param model
     * @param request
     * @return
     */
    @RequestMapping("/expendList")
    public String expendList(Model model, HttpServletRequest request) {

        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> paraMap = new HashMap<String, Object>();

        if (StringUtils.isEmpty(request.getParameter("partnerId")) == false) {
            Long partnerId = Long.parseLong(request.getParameter("partnerId"));
            Partner partner = adminPartnerService.getById(partnerId);
            model.addAttribute("partner", partner);
            paraMap.put("partnerName", partner.getPartnerName());
        }

        if (StringUtils.isEmpty(request.getParameter("partnerName")) == false) {
            paraMap.put("partnerName", request.getParameter("partnerName"));
        }

        Calendar calendar = Calendar.getInstance();
        String startDate = calendar.get(calendar.YEAR)+"-01-01";
        if (StringUtils.isEmpty(request.getParameter("startDay")) == false) {
            startDate = request.getParameter("startDay");
        }
        paraMap.put("startDay", startDate);

        String endDate = DateUtil.getDateStr(new Date(),"yyyy-MM-dd");
        if (StringUtils.isEmpty(request.getParameter("endDay")) == false) {
            endDate = request.getParameter("endDay");
        }
        paraMap.put("endDay", endDate + " 23:59:59");

        Set<String> keySet = adminFinanceService.initTableColum(startDate,endDate);
        model.addAttribute("keySet", keySet);

        Partner partner = new Partner();
        if(StringUtils.isEmpty(paraMap.get("partnerName")) == false) {
            partner.setPartnerName(paraMap.get("partnerName").toString());
        }
        CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
        List<Partner> partnerList = adminPartnerService.listBy(partner);
        List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
        for(Partner p:partnerList){
            paraMap.put("partnerId",p.getId());
            resultMap = adminFinanceService.getExpendList(paraMap);
            resultMap.put("partnerName",p.getPartnerName());
            resultList.add(resultMap);
        }

        paraMap.put("endDay", endDate);
        model.addAttribute("paraMap", paraMap);

        model.addAttribute("resultList", resultList);

        return PAGE_DIRECTORY + "admin_finance_expend_list";
    }


    /**
     * 代理商消费明细
     * @param model
     * @param request
     * @return
     */
    @RequestMapping("/proxyExpendList")
    public String proxyExpendList(Model model, HttpServletRequest request) {

        Map<String, Object> paraMap = new HashMap<String, Object>();
        if (StringUtils.isEmpty(request.getParameter("partnerId")) == false) {
            paraMap.put("partnerId", request.getParameter("partnerId"));
        }
        Calendar calendar = Calendar.getInstance();
        String startDay = calendar.get(calendar.YEAR)+"-01-01";
        if (StringUtils.isEmpty(request.getParameter("startDay")) == false) {
            startDay = request.getParameter("startDay");
        }
        paraMap.put("startDay", startDay);

        String endDate = DateUtil.getDateStr(new Date(),"yyyy-MM-dd");
        if (StringUtils.isEmpty(request.getParameter("endDay")) == false) {
            endDate = request.getParameter("endDay");
        }

        paraMap.put("endDay", endDate + " 23:59:59");
        model.addAttribute("paraMap", paraMap);

        Set<String> keySet = adminFinanceService.initTableColum(startDay,endDate);
        model.addAttribute("keySet", keySet);

        //广告主列表
        Partner p = new Partner();
        p.setPid(-1L);
        p.setPartnerType(0L);
        List<Partner> listPartners = adminPartnerService.listOneLevelPartnerBy(p);
        model.addAttribute("partners", listPartners);

        List<Map<String,Object>> resultList = adminFinanceService.getProxyExpendList(model,paraMap);
        model.addAttribute("resultList",resultList);

        paraMap.put("endDay", endDate);

        return PAGE_DIRECTORY + "admin_finance_proxyExpend_list";
    }



    /**
     * 返佣金详情
     * @param model
     * @param request
     * @return
     */
    @RequestMapping("/brokerage")
    public String brokerage(Model model, HttpServletRequest request){

        CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);

        Map<String, Object> paraMap = new HashMap<String, Object>();

        if(StringUtils.isEmpty(request.getParameter("backStatus")) == false){
            paraMap.put("backStatus", request.getParameter("backStatus"));
        }

        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> list = null;
        if (StringUtils.isEmpty(request.getParameter("partnerId")) == false) {
            Long partnerId = Long.parseLong(request.getParameter("partnerId"));
            Partner partner = adminPartnerService.getById(partnerId);
            paraMap.put("partnerName",partner.getPartnerName());
        }

        if (StringUtils.isEmpty(request.getParameter("partnerName")) == false) {
            paraMap.put("partnerName", request.getParameter("partnerName"));
        }

        Partner partner = new Partner();
        if(StringUtils.isEmpty(paraMap.get("partnerName")) == false) {
            partner.setPartnerName(paraMap.get("partnerName").toString());
        }
        CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
        List<Partner> partnerList = adminPartnerService.listBy(partner);
        for(Partner p:partnerList){
            paraMap.put("partnerId",p.getId());
            list = adminFinanceService.getBrokerageList(paraMap);
            resultList.addAll(list);
        }

        model.addAttribute("paraMap", paraMap);

        //返佣金详情long
        model.addAttribute("brokerageList", resultList);

        return PAGE_DIRECTORY+"admin_finance_brokerage";
    }

    @RequestMapping("/downloadFinanceReport")
    public void downloadFinanceReport(Model model, HttpServletRequest request, HttpServletResponse response) throws PlatformException {

        // Shiro用户信息
        setUserPageInfo(model);

        Map<String,Object> paraMap  = new HashMap<String,Object>();
        if (StringUtils.isEmpty(request.getParameter("partnerName")) == false) {
            paraMap.put("partnerName", request.getParameter("partnerName"));
        }
        String partner_type = "0";
        if (StringUtils.isEmpty(request.getParameter("partner_type")) == false) {
            paraMap.put("partner_type", request.getParameter("partner_type"));
        }
        if("2".equals(partner_type)){
            paraMap.put("boss_partner_code", 1);
        }
        Calendar calendar = Calendar.getInstance();
        String startDate = calendar.get(calendar.YEAR)+"-01-01";
        if (StringUtils.isEmpty(request.getParameter("startDate")) == false) {
            startDate = request.getParameter("startDate");
        }
        paraMap.put("startDate", startDate);

        String endDate = DateUtil.getDateStr(new Date(),"yyyy-MM-dd");
        if (StringUtils.isEmpty(request.getParameter("endDate")) == false) {
            endDate = request.getParameter("endDate");
        }

        paraMap.put("endDate", endDate + " 23:59:59");
        model.addAttribute("queryMap",paraMap);

        adminFinanceService.downloadFinanceReport(paraMap, response);
        paraMap.put("endDate", endDate);
    }

    @RequestMapping("/downloadBrokerageReport")
    public void downloadBrokerageReport(Model model, HttpServletRequest request, HttpServletResponse response) throws PlatformException {

        // Shiro用户信息
        setUserPageInfo(model);

        Map<String, Object> paraMap = new HashMap<String, Object>();

        if(StringUtils.isEmpty(request.getParameter("backStatus")) == false){
            paraMap.put("backStatus", request.getParameter("backStatus"));
        }

        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> list = null;
        if (StringUtils.isEmpty(request.getParameter("partnerId")) == false) {
            Long partnerId = Long.parseLong(request.getParameter("partnerId"));
            Partner partner = adminPartnerService.getById(partnerId);
            paraMap.put("partnerName",partner.getPartnerName());
        }

        if (StringUtils.isEmpty(request.getParameter("partnerName")) == false) {
            paraMap.put("partnerName", request.getParameter("partnerName"));
        }

        Partner partner = new Partner();
        if(StringUtils.isEmpty(paraMap.get("partnerName")) == false) {
            partner.setPartnerName(paraMap.get("partnerName").toString());
        }
        CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
        List<Partner> partnerList = adminPartnerService.listBy(partner);
        for(Partner p:partnerList){
            paraMap.put("partnerId",p.getId());
            list = adminFinanceService.getBrokerageList(paraMap);
            resultList.addAll(list);
        }

        model.addAttribute("paraMap", paraMap);

        //返佣金详情long
        model.addAttribute("brokerageList", resultList);

        adminFinanceService.downloadBrokerageReport(resultList,response);

    }

    @RequestMapping("/downloadExpendReport")
    public void downloadExpendReport(Model model, HttpServletRequest request, HttpServletResponse response) throws PlatformException {

        // Shiro用户信息
        setUserPageInfo(model);

        HashMap<String, Object> paraMap = new HashMap<String, Object>();
        if ( StringUtils.isEmpty(request.getParameter("partnerId")) == false ) {
            Long partnerId = Long.parseLong(request.getParameter("partnerId"));
            paraMap.put("partnerId", partnerId);
            Partner partner = adminPartnerService.getById(partnerId);
            model.addAttribute("partner", partner);
            paraMap.put("partnerName",partner.getPartnerName());
        }

        if (StringUtils.isEmpty(request.getParameter("partnerName")) == false) {
            paraMap.put("partnerName", request.getParameter("partnerName"));
        }

        model.addAttribute("paraMap", paraMap);

        Calendar calendar = Calendar.getInstance();
        String startDate = calendar.get(calendar.YEAR)+"-01-01";
        if (StringUtils.isEmpty(request.getParameter("startDay")) == false) {
            startDate = request.getParameter("startDay");
        }
        paraMap.put("startDay", startDate);

        String endDate = DateUtil.getDateStr(new Date(),"yyyy-MM-dd");
        if (StringUtils.isEmpty(request.getParameter("endDay")) == false) {
            endDate = request.getParameter("endDay");
        }
        paraMap.put("endDay", endDate + " 23:59:59");

        Set<String> keySet = adminFinanceService.initTableColum(startDate,endDate);
        model.addAttribute("keySet", keySet);

        String proxyFlag = "0";
        if (StringUtils.isEmpty(request.getParameter("proxyFlag")) == false) {
            proxyFlag = request.getParameter("proxyFlag");
        }

        List<Map<String,Object>> mapList = new ArrayList<Map<String,Object>>();
        String fileName = "";
        if("0".equals(proxyFlag)) {
            Map<String, Object> resultMap = new HashMap<String, Object>();

            Partner partner = new Partner();
            if(StringUtils.isEmpty(paraMap.get("partnerName")) == false) {
                partner.setPartnerName(paraMap.get("partnerName").toString());
            }
            CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
            List<Partner> partnerList = adminPartnerService.listBy(partner);
            List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();

            for(Partner p:partnerList){
                paraMap.put("partnerId",p.getId());
                resultMap = adminFinanceService.getExpendList(paraMap);
                resultMap.put("partnerName",p.getPartnerName());
                mapList.add(resultMap);
            }

            fileName = "消费明细";
        }else {
            fileName = "代理消费明细";
            mapList = adminFinanceService.getProxyExpendList(model,paraMap);
        }

        paraMap.put("endDay", endDate);
        model.addAttribute("paraMap", paraMap);

        JSONArray headerJson = new JSONArray();
        for (String str : keySet) {
            headerJson.add(str);
        }
        adminFinanceService.downloadExpendReport(fileName,headerJson,mapList,response);

    }

    @RequestMapping("/changeStatus")
    @ResponseBody
    public Map<String,Object> changeStatus(Model model,Long id){
        Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("success", true);
        try {
            adminFinanceService.changeStatus(id);
        }catch (Exception e){
            resultMap.put("message", "支付佣金失败!");
            resultMap.put("success", false);
        }
        return resultMap;
    }
    

}
