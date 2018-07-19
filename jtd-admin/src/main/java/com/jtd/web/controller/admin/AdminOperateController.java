package com.jtd.web.controller.admin;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jtd.utils.DateUtil;
import com.jtd.web.controller.BaseController;
import com.jtd.web.exception.PlatformException;
import com.jtd.web.po.ActiveUser;
import com.jtd.web.service.admin.IAdminOperatorService;

@Controller
@RequestMapping("/admin/operate")
public class AdminOperateController extends BaseController {
	
	private Logger log = LoggerFactory.getLogger(AdminOperateController.class);

	public static final String PAGE_DIRECTORY = "/admin/operator/";
	public static final String ACTION_PATH = "redirect:/admin/operator/";
	
	@Autowired
	private IAdminOperatorService operatorService;
	
	/**
	 * 运营人员-广告主报表
	 * 
	 * @param model
	 * @param pageNo
	 * @param pageSize
	 * @param request
	 * @return
	 * @throws PlatformException
	 */
	@RequestMapping("/partnerReport")
	public String partnerReport(Model model, Integer pageNo,Integer pageSize, HttpServletRequest request )
			throws PlatformException {
		
		// Shiro用户信息
		setUserPageInfo(model);

		HashMap<String, Object> paraMap = processPartnerReportParam(request);
		
		model.addAttribute("queryMap", paraMap);
		Long userId = null ;
		
		//如果从是“运营人员报表”进入，则会有operatorId参数，如果没有参数，则为从“广告主报表”进入的
		String operatorId = request.getParameter("operatorId");
		if(StringUtils.isEmpty(operatorId) == false){
			
			paraMap.put("operatorId", operatorId);
			try {
				userId = Long.parseLong(operatorId);
			} catch (Exception e) {
			}
		}
		else{
			
			ActiveUser user = getUserInfo();
			
			//如果不是“运营人员报表”进入，如果是管理员或运营主管，则不受数据权限限制，如果是普通运营，则要根据userId查询数据权限
			if(user.isAdminOrManager() == false && user.isOperateDirectorOrManager() == false){
				userId = user.getUserId();
			}
		}
		
		operatorService.partnerReport(model , paraMap,pageNo, 50 , null, userId);

		return PAGE_DIRECTORY + "partner_report";
	}
	
	/**
	 * 广告主报表，点击折叠图标查询广告主的下级
	 * @param request
	 * @return
	 * @throws PlatformException
	 */
	@RequestMapping("/getChildrenListForPartnerReport")
	@ResponseBody
	public Map<String,Object> getChildrenListForPartnerReport(HttpServletRequest request )
			throws PlatformException {
		
		HashMap<String, Object> paraMap = processPartnerReportParam(request);
		
		Map<String,Object> resultMap = new HashMap<String, Object>();
		try {
			long partnerId = Long.parseLong(request.getParameter("partnerId"));
			
			List<Map<String,Object>> childrenList = operatorService.getChildrenListForPartnerReport(partnerId, paraMap);
			
			resultMap.put("code", "1");
			resultMap.put("list", childrenList);
			
		} catch (Exception e) {
			log.error("getChildrenListForPartnerReport发生错误" , e);
			resultMap.put("code", "0");
		}
		
		return resultMap ;
	}
	
	/**
	 * 处理广告主报表的查询参数
	 * 
	 * @param model
	 * @param request
	 * @return
	 */
	private HashMap<String, Object> processPartnerReportParam(HttpServletRequest request){

		HashMap<String, Object> paraMap = new HashMap<String, Object>();
		
		//id或名称
		if (StringUtils.isEmpty(request.getParameter("partnerIdOrName")) == false) {
			paraMap.put("partnerIdOrName", request.getParameter("partnerIdOrName"));
		}
		//广告主类型
		if (StringUtils.isEmpty(request.getParameter("partnerType")) == false) {
			try {
				paraMap.put("partnerType", Integer.parseInt(request.getParameter("partnerType")));
			} catch (NumberFormatException e) {
			}
		}
		//广告主状态
		if (StringUtils.isEmpty(request.getParameter("status")) == false) {
			try {
				paraMap.put("status", Integer.parseInt(request.getParameter("status")));
			} catch (NumberFormatException e) {
			}
		}
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		//上线时间
		String firstOnlineStartTimeString = request.getParameter("firstOnlineStartTime");
		if (StringUtils.isEmpty(firstOnlineStartTimeString) == false) {
			try {
				paraMap.put("updateFristOnlineStratTime", firstOnlineStartTimeString);
				paraMap.put("firstOnlineStartTime", df.parse(firstOnlineStartTimeString + " 00:00:00"));
			} catch (Exception e) {
			}
		}
		String firstOnlineEndTimeString = request.getParameter("firstOnlineEndTime");
		if (StringUtils.isEmpty(firstOnlineEndTimeString) == false) {
			try {
				paraMap.put("updateFristOnlineEndTime", firstOnlineEndTimeString);
				paraMap.put("firstOnlineEndTime", df.parse(firstOnlineEndTimeString + " 23:59:59"));
			} catch (Exception e) {
			}
		}
		
		processCountStartDateAndEndDate(request,paraMap,"startDay","endDay");
		
		return paraMap ;
	}
	
	/**
	 * 生成广告主报表excel
	 * 
	 * @param request
	 * @return
	 * @throws PlatformException
	 */
	@RequestMapping("/downloadPartnerReportExcel")
	public void downloadPartnerReportExcel(Model model, HttpServletRequest request , HttpServletResponse response) throws PlatformException {

		HashMap<String, Object> paraMap = processPartnerReportParam(request);
		
		Long userId = null ;
		
		String operatorId = request.getParameter("operatorId");
		if(StringUtils.isEmpty(operatorId) == false){
			
			paraMap.put("operatorId", operatorId);
			try {
				userId = Long.parseLong(operatorId);
			} catch (Exception e) {
			}
		}
		else{
			
			ActiveUser user = getUserInfo();
			
			//如果不是“运营人员报表”进入，如果是管理员或运营主管，则不受数据权限限制，如果是普通运营，则要根据userId查询数据权限
			if(user.isAdminOrManager() == false && user.isOperateDirectorOrManager() == false){
				userId = user.getUserId();
			}
		}
		
		operatorService.partnerReport(model, paraMap, null, null, response, userId);
		
	}

	/**
	 * 运营总监-运营人员报表
	 * 
	 * @param model
	 * @param pageNo
	 * @param pageSize
	 * @param request
	 * @return
	 * @throws PlatformException
	 */
	@RequestMapping("/operatorReport")
	public String operatorReport(Model model, Integer pageNo,Integer pageSize, HttpServletRequest request)
			throws PlatformException {

		// Shiro用户信息
		setUserPageInfo(model);

		HashMap<String, Object> paraMap = new HashMap<String, Object>();
		if (StringUtils.isEmpty(request.getParameter("userName")) == false) {
			paraMap.put("userName", request.getParameter("userName"));
		}
		
		processCountStartDateAndEndDate(request,paraMap,"startDay","endDay");
		
		model.addAttribute("queryMap", paraMap);
		
		List<Map<String,Object>> resultList = operatorService.operatorReport(paraMap,null);

		model.addAttribute("list", resultList);

		return PAGE_DIRECTORY + "operator_report";
	}
	
	/**
	 * 运营总监-运营人员报表
	 * 
	 * @param model
	 * @param pageNo
	 * @param pageSize
	 * @param request
	 * @return
	 * @throws PlatformException
	 */
	@RequestMapping("/downloadOperatorReportExcel")
	public void downloadOperatorReportExcel(Model model, HttpServletRequest request, HttpServletResponse response)
			throws PlatformException {

		// Shiro用户信息
		setUserPageInfo(model);

		HashMap<String, Object> paraMap = new HashMap<String, Object>();
		if (StringUtils.isEmpty(request.getParameter("userName")) == false) {
			paraMap.put("userName", request.getParameter("userName"));
		}
		
		processCountStartDateAndEndDate(request,paraMap,"startDay","endDay");
		
		model.addAttribute("queryMap", paraMap);
		
		operatorService.operatorReport(paraMap,response);

	}
	
	/**
	 * 跳转到百度分创意报表jsp
	 * 
	 * @param model
	 * @param pageNo
	 * @param pageSize
	 * @param request
	 * @return
	 * @throws PlatformException
	 */
	@RequestMapping("/gotoBesCreativeRTBReport")
	public String gotoBesCreativeRTBReport(Model model, HttpServletRequest request)
			throws PlatformException {

		// Shiro用户信息
		setUserPageInfo(model);
		
		String today = DateUtil.getDateStr(new Date(), "yyyy-MM-dd");
		
		model.addAttribute("startDate", today);
		model.addAttribute("endDate", today);

		return PAGE_DIRECTORY + "bes_creative_report";
	}
	
	/**
	 * 百度分创意报表ajax查询
	 * 
	 * @param model
	 * @param pageNo
	 * @param pageSize
	 * @param request
	 * @return
	 * @throws PlatformException
	 */
	@RequestMapping("/ajaxBesCreativeRTBReport")
	@ResponseBody
	public HashMap<String, Object> ajaxBesCreativeRTBReport(Model model, HttpServletRequest request)
			throws PlatformException {

		String today = DateUtil.getDateStr(new Date(), "yyyy-MM-dd");

		String startCreativeId = request.getParameter("startCreativeId");
		String endCreativeId = request.getParameter("endCreativeId");
		String startDate = request.getParameter("startDate") != null ? request.getParameter("startDate") : today ;
		String endDate = request.getParameter("endDate") != null ? request.getParameter("endDate") : today ;
		String orderBy = request.getParameter("orderBy") != null ? request.getParameter("orderBy") : "showDate" ;
		String desc = request.getParameter("desc") != null ? request.getParameter("desc") : "desc" ;
		
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		
		List<Map<String,Object>> resultList = null ;
		try {
			resultList = operatorService.besCreativeRTBReport(startCreativeId, endCreativeId, startDate, endDate, orderBy, desc);
			
			if(resultList != null){
				resultMap.put("code", "1");
				resultMap.put("resultList", resultList);
			}
			else{
				resultMap.put("code", "0");
				resultMap.put("msg", "获取报表数据为空");
			}
			
			return resultMap ;
			
		} catch (Exception e) {
			
			resultMap.put("code", "0");
			resultMap.put("msg", e.getMessage());
			
			return resultMap ;
		}
		
	}

	/**
	 * 下载百度分创意报表
	 * 
	 * @param request
	 * @return
	 * @throws PlatformException
	 */
	@RequestMapping("/exportBesCreativeRTBReport")
	public void exportBesCreativeRTBReport(HttpServletRequest request , HttpServletResponse response) throws Exception {

		operatorService.downloadBesCreativeRTBReport(response);
		
	}
	
	/**
	 * 活动渠道报表
	 * 
	 * @param model
	 * @param pageNo
	 * @param pageSize
	 * @param request
	 * @return
	 * @throws PlatformException
	 */
	@RequestMapping("/campChannelReport")
	public String campChannelReport(Model model, Integer pageNo,Integer pageSize, HttpServletRequest request) throws Exception {
		
		// Shiro用户信息
		setUserPageInfo(model);
		
		HashMap<String, Object> params = processCampChannelReportParams(model , request);
		
		operatorService.campChannelReport(model, params, pageNo, 50 , null);
	
		return PAGE_DIRECTORY + "camp_channel_report";
	}
	
	private HashMap<String, Object> processCampChannelReportParams(Model model, HttpServletRequest request){
		
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		
		if(StringUtils.isEmpty(request.getParameter("campaignIdOrName")) == false){
			params.put("campaignIdOrName", request.getParameter("campaignIdOrName"));
		}
		
		if(StringUtils.isEmpty(request.getParameter("partnerIdOrName")) == false){
			params.put("partnerIdOrName", request.getParameter("partnerIdOrName"));
		}
		
		if(StringUtils.isEmpty(request.getParameter("host")) == false){
			params.put("host", request.getParameter("host"));
		}
		
		String channelIdStr = request.getParameter("channelId");
		if(StringUtils.isEmpty(channelIdStr) == false){
			params.put("channelId", channelIdStr);
			model.addAttribute("channelId", channelIdStr);
		}
		
		String orderBy = StringUtils.isEmpty(request.getParameter("orderBy")) == false ? request.getParameter("orderBy") : "camp_id";
		String desc = StringUtils.isEmpty(request.getParameter("desc")) == false ? request.getParameter("desc") : "desc";
		params.put("orderBy", orderBy);
		params.put("desc", desc);
		
		ActiveUser user = this.getUserInfo();
		// 如果不是“运营人员报表”进入，如果是管理员或运营主管，则不受数据权限限制，如果是普通运营，则要根据userId查询数据权限
		if (user.isAdminOrManager() == false
				&& user.isOperateDirectorOrManager() == false) {
			params.put("userId", user.getUserId());
		}
		
		processCountStartDateAndEndDate(request,params,"startDateNum", "endDateNum");
		
		model.addAttribute("params", params);
		
		return params ;
		
	}
	
	/**
	 * 下载活动渠道报表
	 * 
	 * @param request
	 * @return
	 * @throws PlatformException
	 */
	@RequestMapping("/downloadCampChannelReport")
	public void downloadCampChannelReport(Model model, HttpServletRequest request , HttpServletResponse response) throws Exception {

		HashMap<String, Object> params = processCampChannelReportParams(model , request);
		
		operatorService.campChannelReport(model, params, null, null, response);
		
		
	}
	
	/**
	 * 处理统计周期开始结束日期
	 * 
	 * @param request
	 * @param params
	 */
	private void processCountStartDateAndEndDate(HttpServletRequest request , HashMap<String, Object> params , String startDayFinalKey , String endDayFinalKey){
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		
		String startDateStr = request.getParameter("startDate");
		String endDateStr = request.getParameter("endDate");
		
		if(StringUtils.isEmpty(startDateStr) == false){
			
			params.put("startDate", startDateStr);
			try {
				params.put(startDayFinalKey, DateUtil.getDateInt(df.parse(startDateStr)));
			} catch (ParseException e) {
				log.error("campChannelReport转换开始日期参数发生错误" , e);
			}
		}
		else {
			if(params.get("startDateNum") == null){
				// 如果日期参数为空，则日期默认查最近七天
				Calendar c = Calendar.getInstance();
				c.set(Calendar.DATE, c.get(Calendar.DATE) - 6);
				params.put("startDate",
						DateUtil.getDateStr(c.getTime(), "yyyy-MM-dd"));
				params.put(startDayFinalKey, DateUtil.getDateInt(c.getTime()));
			}
		}
		
		if(StringUtils.isEmpty(endDateStr) == false){
			params.put("endDate", endDateStr);
			try {
				params.put(endDayFinalKey, DateUtil.getDateInt(df.parse(endDateStr)));
			} catch (ParseException e) {
				log.error("campChannelReport转换结束日期参数发生错误" , e);
			}
		}
		else {
			
			if(params.get("endDateNum") == null){
				// 如果日期参数为空，则日期默认查最近七天
				params.put("endDate", DateUtil.getDateStr(new Date(), "yyyy-MM-dd"));
				params.put(endDayFinalKey, DateUtil.getDateInt(new Date()));
			}
			
		}
		
	}
}
