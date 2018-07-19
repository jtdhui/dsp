package com.jtd.web.controller.admin;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jtd.commons.page.Pagination;
import com.jtd.web.constants.RoleType;
import com.jtd.web.controller.BaseController;
import com.jtd.web.exception.PlatformException;
import com.jtd.web.po.ActiveUser;
import com.jtd.web.po.Partner;
import com.jtd.web.po.QualiDoc;
import com.jtd.web.service.admin.IAdminPartnerService;
import com.jtd.web.service.admin.IAdminQualiDocService;
import com.jtd.web.service.admin.IAdminUserPartnerService;
import com.jtd.web.vo.ChannelCategory;

@Controller
@RequestMapping("/admin/qualidoc")
public class AdminQualiDocController extends BaseController {

	public static final String PAGE_DIRECTORY = "/admin/qualidoc/";
	public static final String ACTION_PATH = "redirect:/admin/qualidoc/";

	@Autowired
	private IAdminQualiDocService qualiDocService;
	
	@Autowired
	private IAdminPartnerService partnerService;
	
	@Autowired
	private IAdminUserPartnerService adminUserPartnerService;

	@RequestMapping("/list")
	public String list(Model model, Integer pageNo,Integer pageSize, HttpServletRequest request)
			throws PlatformException {

		// Shiro用户信息
		setUserPageInfo(model);

		HashMap<String, Object> paraMap = new HashMap<String, Object>();
		if (StringUtils.isEmpty(request.getParameter("partnerName")) == false) {
			paraMap.put("partnerName", request.getParameter("partnerName"));
		}
		if (StringUtils.isEmpty(request.getParameter("internalAuditStatus")) == false) {
			paraMap.put("internalAuditStatus", Integer.parseInt(request
					.getParameter("internalAuditStatus")));
		}
		if (StringUtils.isEmpty(request.getParameter("adxAuditStatus")) == false) {
			paraMap.put("adxAuditStatus",
					Integer.parseInt(request.getParameter("adxAuditStatus")));
		}
		
		//登录后台的普通user，只能查看user_partner表中已关联的partner的数据
		ActiveUser user = getUserInfo();
		
		//普通用户
		if(!user.isAdminOrManager() && !user.isOperateDirectorOrManager()) { 
			// 如果不是管理员，也不是运营经理主管，就要受partner访问权限的控制（如运营或代理公司用户）
			paraMap.put("userId", user.getUserId());
			// 普通用户在后台中不能编辑操作自己所在的公司，只能由上级公司的用户去编辑；无上级的公司，由【jtd公司】运营去编辑
			paraMap.put("noPartnerId",user.getPartner().getId());   
		}
		//运营经理和主管可以查看所有运营负责的广告主
		if(user.isOperateDirectorOrManager()){
            paraMap.put("pId", user.getPartner().getPid());
            //运营经理和主管在后台中不能编辑操作自己所在的公司【jtd公司】
            paraMap.put("noPartnerId", user.getPartner().getId());
        }

		Pagination<Map<String, Object>> page = qualiDocService.findPageMap(paraMap,pageNo, pageSize);

		model.addAttribute("page", page);
		model.addAttribute("queryMap", paraMap);

		return PAGE_DIRECTORY + "qualidoc_list";
	}

	/**
     * 运营资质管理列表
     * 分页显示 + 条件查找
     * 
     * @param model
     * @param pageNo
     * @param pageSize
     * @param request
     * @return
     * @throws PlatformException
     */
	@RequestMapping("/operationList")
	public String operationList(Model model, Integer pageNo,Integer pageSize, HttpServletRequest request)
			throws PlatformException {

		// Shiro用户信息
		setUserPageInfo(model);
		HashMap<String, Object> paraMap = processPartnerOperationParam(request);
		
		model.addAttribute("queryMap", paraMap);
		//登录后台的普通user，只能查看user_partner表中已关联的partner的数据
		ActiveUser user = getUserInfo();
		
		//只看直属一级的广告主
		//paraMap.put("pId", user.getPartner().getPid());
		
		//普通用户
		if(!user.isAdminOrManager() && !user.isOperateDirectorOrManager()) { 
			// 如果不是管理员，也不是运营经理主管，就要受partner访问权限的控制
			//paraMap.put("userId", user.getUserId());
		}
		
		
		qualiDocService.findOperation(model , paraMap,pageNo, 50 , null, user);
		

		return PAGE_DIRECTORY + "qualidoc_operation_list";
	}
	
	/**
	 * 根据partnerId查询广告主的下一级代理
	 * @param partnerId
	 * @throws PlatformException
	 * 
	 */
	@RequestMapping("/getChildrenName")
	@ResponseBody
	public Map<String, Object> getChildrenName(HttpServletRequest request) throws PlatformException{
		
		HashMap<String, Object> paraMap = processPartnerOperationParam(request);
		
		Map<String,Object> resultMap = new HashMap<String, Object>();
		
		try {
			long partnerId = Long.parseLong(request.getParameter("partnerId"));
			
			paraMap.put("pid", partnerId);
			
			List<Map<String,Object>> childrenList =  qualiDocService.findChildren(paraMap);
			
			resultMap.put("success", true);
			resultMap.put("partner", childrenList);
		} catch (Exception e) {
			logger.error("根据partnerId查询广告主下级代理出错",e);
		}
		
		return resultMap;
	}
	
	/**
	 * 处理运营资质列表的查询参数
	 * 
	 * @param model
	 * @param request
	 * @return
	 */
	private HashMap<String, Object> processPartnerOperationParam(HttpServletRequest request){

		HashMap<String, Object> paraMap = new HashMap<String, Object>();
		if (StringUtils.isEmpty(request.getParameter("partnerName")) == false) {
			paraMap.put("partnerName", request.getParameter("partnerName"));
		}
		if (StringUtils.isEmpty(request.getParameter("internalAuditStatus")) == false) {
			paraMap.put("internalAuditStatus", Integer.parseInt(request
					.getParameter("internalAuditStatus")));
		}
		
		return paraMap ;
	}
	
	
	/**
	 * 跳到qualidoc_detail_audit.jsp进行内部审核
	 * 
	 * @param model
	 * @param id
	 * @return
	 * @throws PlatformException
	 */
	@RequestMapping("/toAudit")
	public String toAudit(Model model, long id) throws PlatformException {

		// Shiro用户信息
		setUserPageInfo(model);

		HashMap<String, Object> map = qualiDocService.toAudit(id);

		model.addAttribute("resultMap", map);
		
		ActiveUser user = getUserInfo();
		
		model.addAttribute("isManager", user.isAdminOrManager());
		model.addAttribute("partnerId", id);

		return PAGE_DIRECTORY + "qualidoc_detail_audit";
	}

	/**
	 * 保存内部审核结果，如果内部审核通过提交adx审核
	 * 
	 * @param model
	 * @param request
	 * @return
	 * @throws PlatformException
	 */
	@RequestMapping("/auditSave")
	public String auditSave(Model model, HttpServletRequest request)
			throws PlatformException {

		// Shiro用户信息
		setUserPageInfo(model);
		
		ActiveUser user = getUserInfo();
		Long userId = user.getUserId();

		Integer partnerId = 0 ;
		try {
			partnerId = Integer.parseInt(request.getParameter("partnerId"));
		} catch (NumberFormatException e) {
			return PAGE_DIRECTORY + "list";
		}
		
		Integer internalAuditStatus = 0;
		try {
			internalAuditStatus = Integer.parseInt(request
					.getParameter("internalAuditStatus"));
		} catch (NumberFormatException e) {
			return PAGE_DIRECTORY + "list";
		}
		
		String internalRejectRemark = request.getParameter("rejectRemark");

		qualiDocService.saveAudit(userId , partnerId, internalAuditStatus, internalRejectRemark);

		return ACTION_PATH + "toAudit.action?id=" + partnerId;
	}
	
	/**
	 * 单独重新提交某个adx
	 * 
	 * @param model
	 * @param request
	 * @return
	 * @throws PlatformException
	 */
	@RequestMapping("/submitToSingleChannel")
	public String submitToSingleChannel(Model model, HttpServletRequest request)
			throws PlatformException {

		// Shiro用户信息
		setUserPageInfo(model);
		
		ActiveUser user = getUserInfo();
		Long userId = user.getUserId();

		Long partnerId = 0L;
		try {
			partnerId = Long.parseLong(request.getParameter("partnerId"));
		} catch (NumberFormatException e) {
			return PAGE_DIRECTORY + "list";
		}
		
		Long channelId = 0L;
		try {
			channelId = Long.parseLong(request.getParameter("channelId"));
		} catch (NumberFormatException e) {
			return PAGE_DIRECTORY + "list";
		}

		qualiDocService.submitToChannel(userId, partnerId, channelId);

		return ACTION_PATH + "toAudit.action?id=" + partnerId;
	}
	
	/**
	 * 单独重新提交某个adx的审核状态MQ
	 * 
	 * @param model
	 * @param request
	 * @return
	 * @throws PlatformException
	 */
	@RequestMapping("/sendMqForSingleChannel")
	public String sendMqForSingleChannel(Model model, HttpServletRequest request)
			throws PlatformException {

		// Shiro用户信息
		setUserPageInfo(model);
		
		Long partnerId = 0L;
		try {
			partnerId = Long.parseLong(request.getParameter("partnerId"));
		} catch (NumberFormatException e) {
			return PAGE_DIRECTORY + "list";
		}
		
		Long channelId = 0L;
		try {
			channelId = Long.parseLong(request.getParameter("channelId"));
		} catch (NumberFormatException e) {
			return PAGE_DIRECTORY + "list";
		}

		qualiDocService.sendChannelAuditMQ(partnerId, channelId);

		return ACTION_PATH + "toAudit.action?id=" + partnerId;
	}
	
	/**
	 * 单独提交某个广告主的内部审核状态MQ
	 * 
	 * @param model
	 * @param request
	 * @return
	 * @throws PlatformException
	 */
	@RequestMapping("/sendInternalAuditMQ")
	public String sendInternalAuditMQ(Model model, HttpServletRequest request)
			throws PlatformException {

		// Shiro用户信息
		setUserPageInfo(model);
		
		Long partnerId = 0L;
		try {
			partnerId = Long.parseLong(request.getParameter("partnerId"));
		} catch (NumberFormatException e) {
			return PAGE_DIRECTORY + "list";
		}
		
		qualiDocService.sendInternalAuditMQ(partnerId);

		return ACTION_PATH + "toAudit.action?id=" + partnerId;
	}

	/**
	 * 根据qualidoc的id读取相应并写图片流
	 * 
	 * @param request
	 */
	@RequestMapping("/showImage")
	public void showImage(HttpServletResponse response,
			HttpServletRequest request) {

		int id = Integer.parseInt(request.getParameter("id"));
		QualiDoc doc = qualiDocService.getById(id);
		
		//String host = request.getHeader("Host");
		//String s = host + request.getContextPath() + "/admin/qualidoc/showImage.action?id=" ;
		//logger.info("ssssssssssssssssssssssss============" + s);

		if(doc != null){
			try {
				File file = new File(doc.getDocPath() + "/" + doc.getDocName());
				FileInputStream inputStream = new FileInputStream(file);
		        byte[] data = new byte[(int)file.length()];
		        inputStream.read(data);
		        inputStream.close();

		        response.setContentType("image/png");

		        OutputStream stream = response.getOutputStream();
		        stream.write(data);
		        stream.flush();
		        stream.close();
		        
			} catch (Exception e) {
				logger.error("AdminQualiDocController----showImage----读取资质文件出错,docId=" + id , e);
			}
		}

	}
	
}
