package com.jtd.web.controller.admin;

import com.jtd.commons.page.Pagination;
import com.jtd.web.constants.RoleType;
import com.jtd.web.controller.BaseController;
import com.jtd.web.exception.PlatformException;
import com.jtd.web.po.ActiveUser;
import com.jtd.web.po.Partner;
import com.jtd.web.po.PartnerPreFlow;
import com.jtd.web.service.admin.IAdminPartnerPreFlowService;
import com.jtd.web.service.admin.IAdminPartnerService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by duber on 16/12/26.
 */
@Controller
@RequestMapping("/admin/partnerPre")
public class AdminPartnerPreFlowController extends BaseController {
	
	private Logger logger = Logger.getLogger(AdminPartnerPreFlowController.class);

	public static final String PAGE_DIRECTORY = "/admin/partnerPre/";
	public static final String ACTION_PATH = "redirect:/admin/partnerPre/";

	@Autowired
	private IAdminPartnerPreFlowService adminPartnerPreFlowService;
	
	@Autowired
	private IAdminPartnerService partnerService;

	@RequestMapping("/list")
	public String list(Model model, Integer pageNo, Integer pageSize,
			HttpServletRequest request) throws PlatformException {

		// Shiro用户信息
		setUserPageInfo(model);
		HashMap<String, Object> paraMap = new HashMap<String, Object>();

		if (StringUtils.isEmpty(request.getParameter("status")) == false) {
			String status = request.getParameter("status");
			paraMap.put("status", status);
		}
		if (StringUtils.isEmpty(request.getParameter("type")) == false) {
			String type = request.getParameter("type");
			paraMap.put("type", type);
		}
        if (StringUtils.isEmpty(request.getParameter("invoice")) == false) {
            String invoice = request.getParameter("invoice");
            paraMap.put("invoice", invoice);
        }
        if (StringUtils.isEmpty(request.getParameter("payType")) == false) {
            String payType = request.getParameter("payType");
            paraMap.put("payType", payType);
        }
        if (StringUtils.isEmpty(request.getParameter("isInvoice")) == false) {
            String isInvoice = request.getParameter("isInvoice");
            paraMap.put("isInvoice", isInvoice);
        }

        if (StringUtils.isEmpty(request.getParameter("partnerId")) == false) {
            Long partnerId = Long.parseLong(request.getParameter("partnerId"));
            Partner partner = partnerService.getById(partnerId);
            model.addAttribute("partner", partner);
            paraMap.put("partnerName", partner.getPartnerName());
        }

        if (StringUtils.isEmpty(request.getParameter("partnerName")) == false) {
            paraMap.put("partnerName", request.getParameter("partnerName"));
        }

		model.addAttribute("queryMap", paraMap);

		Pagination<PartnerPreFlow> partnerPreFlowPagination = adminPartnerPreFlowService.findPageBy(paraMap, pageNo, pageSize);
		model.addAttribute("page", partnerPreFlowPagination);
		return PAGE_DIRECTORY + "admin_partnerPre_list";
	}

	@RequestMapping("/waterList")
	public String waterList(Model model, Integer pageNo, Integer pageSize, HttpServletRequest request) throws PlatformException {

		// Shiro用户信息
		setUserPageInfo(model);
		ActiveUser activeUser = getUserInfo();
		HashMap<String, Object> pMap = new HashMap<String, Object>();

		if (StringUtils.isEmpty(request.getParameter("status")) == false) {
			String status = request.getParameter("status");
			pMap.put("status", status);
		}
		int roleId = Integer.parseInt(activeUser.getRoleId());
		if (roleId == RoleType.OPERATE.getCode()) { // 如果当前用户角色是运营
			pMap.put("preOperatorId", activeUser.getUserId());
		}

		model.addAttribute("queryMap", pMap);

		Pagination<PartnerPreFlow> partnerPreFlowPagination = adminPartnerPreFlowService.findPageBy(pMap, pageNo, pageSize);
		model.addAttribute("page", partnerPreFlowPagination);
		return PAGE_DIRECTORY + "admin_partnerPre_waterList";
	}

	@RequestMapping("/add")
	@ResponseBody
	public Map<String, Object> add(PartnerPreFlow pf) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();

		//验证客户的账户余额没有超过限制
		long allowAmount = 0 ;
		try {
            Partner partner = partnerService.getById(pf.getPartnerId());
			if(pf.getType() == 0) {
				allowAmount = partnerService.validAcountRechargeLimit(pf.getPartnerId());
			}else{
				allowAmount = partner.getAccBalance();
			}
			if(pf.getPreAmount() > allowAmount){
				resultMap.put("msg", "拒绝提交：广告主还能操作的金额是：￥" + (allowAmount/100) + "");
			} else{

			    if(pf.getType() == 1){ //退款金额为负
                    pf.setPreAmount(-pf.getPreAmount());
                }
				Date now = new Date();
                pf.setPartnerName(partner.getPartnerName());
				pf.setCreateTime(now);
				pf.setModifyTime(now);
				pf.setPreOperatorId(getUserInfo().getUserId());
				pf.setPreOperatorName(getUserInfo().getUserName());
				pf.setStatus(0);

				adminPartnerPreFlowService.insert(pf);
				resultMap.put("success", true);
			}
		}
		catch (PlatformException e) {
			resultMap.put("msg", e.getMessage());
		}
		catch (Exception e) {
			logger.error("预充值发生内部错误 , partner_id=" + pf.getPartnerId() ,e);
			resultMap.put("msg", "发生内部错误");
		}
		
		return resultMap;
	}

    @RequestMapping("/invoice")
	public String invoice(Model model, Long id){
        PartnerPreFlow partnerPreFlow = adminPartnerPreFlowService.getById(id);
        model.addAttribute("po", partnerPreFlow);
        return PAGE_DIRECTORY + "admin_partnerPre_invoice";
    }


    @RequestMapping("/saveInvoice")
    public String saveInvoice(Model model, Long id,int isInvoice,String invoice){
        PartnerPreFlow partnerPreFlow = new PartnerPreFlow();
        partnerPreFlow.setIsInvoice(isInvoice);
        partnerPreFlow.setId(id);
        partnerPreFlow.setInvoice(invoice);
        adminPartnerPreFlowService.updateInvoice(partnerPreFlow);
        return ACTION_PATH + "list.action";
    }
	
}
