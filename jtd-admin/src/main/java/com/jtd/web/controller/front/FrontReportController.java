package com.jtd.web.controller.front;

import com.jtd.commons.mybatis.support.CustomerContextHolder;
import com.jtd.utils.DateUtil;
import com.jtd.web.constants.Constants;
import com.jtd.web.controller.BaseController;
import com.jtd.web.dao.IUserFavDao;
import com.jtd.web.po.ActiveUser;
import com.jtd.web.po.UserFav;
import com.jtd.web.service.front.IFrontPartnerService;
import com.jtd.web.service.front.IFrontReportService;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/front/report")
public class FrontReportController extends BaseController {

	public static final String PAGE_DIRECTORY = "/front/report/";
	public static final String ACTION_PATH = "redirect:/front/report/";
	
	@Autowired
	private IFrontReportService service;
	
	@Autowired
	private IUserFavDao userFavDao ;

	@Autowired
	private IFrontPartnerService frontPartnerService;
	
	private HashMap<String, Object> getParamMap(Model model , long userId , HttpServletRequest request) throws Exception {

		//把params从session中取出来处理
		HashMap<String, Object> params = (HashMap<String, Object>)request.getSession().getAttribute(Constants.FRONT_REPORT_PARAMS_MAP);
		
		if(params == null){
			params = new HashMap<String, Object>();
			request.getSession().setAttribute(Constants.FRONT_REPORT_PARAMS_MAP , params);
		}

		// 活动名称(放到业务库campaign表中查询)
		if (StringUtils.isEmpty(request.getParameter("campaignName")) == false) {
			params.put("campaignName", request.getParameter("campaignName"));
		}
		else{
			params.put("campaignName", "");
		}
		// 活动类型(放到业务库campaign表中查询)
		if (StringUtils.isEmpty(request.getParameter("campaignType")) == false) {
			
			String campType = request.getParameter("campaignType");
			//如果是-1则为查询全部
			if(campType.equals("-1")){
				params.remove("campaignType");
			}
			else{
				params.put("campaignType", campType);
			}
			
		}
		// 活动状态(放到业务库campaign表中查询)
		if (StringUtils.isEmpty(request.getParameter("campaignStatus")) == false) {
			int campaignStatus = Integer.parseInt(request.getParameter("campaignStatus"));
			if(campaignStatus == -1){
				params.remove("campaignStatus");
			}
			else{
				params.put("campaignStatus", campaignStatus);
			}
			
		}
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		
		// 开始日期(放到统计库campd表中查询)
		if (StringUtils.isEmpty(request.getParameter("startDate")) == false) {
			params.put("startDate", request.getParameter("startDate"));
			params.put("startDateNum", DateUtil.getDateInt(df.parse(request.getParameter("startDate"))));
		} else {
			if(params.get("startDateNum") == null){
				// 如果session中的日期参数也为空，则日期默认查最近七天
				Calendar c = Calendar.getInstance();
				c.set(Calendar.DATE, c.get(Calendar.DATE) - 6);
				params.put("startDate",
						DateUtil.getDateStr(c.getTime(), "yyyy-MM-dd"));
				params.put("startDateNum", DateUtil.getDateInt(c.getTime()));
			}
		}

		// 结束日期(放到统计库campd表中查询)
		if (StringUtils.isEmpty(request.getParameter("endDate")) == false) {
			params.put("endDate", request.getParameter("endDate"));
			params.put("endDateNum", DateUtil.getDateInt(df.parse(request.getParameter("endDate"))));
		} else {
			
			if(params.get("endDateNum") == null){
				// 如果session中的日期参数也为空，则日期默认查最近七天
				params.put("endDate", DateUtil.getDateStr(new Date(), "yyyy-MM-dd"));
				params.put("endDateNum", DateUtil.getDateInt(new Date()));
			}
			
		}
		
		//由于params存在session中，先清空一次，来保证报表页面之间跳转不会受到上一张报表的排定的影响
		params.put("orderBy", null);
		//自定义排序
		if (StringUtils.isEmpty(request.getParameter("orderBy")) == false) {
			String orderByRequest = request.getParameter("orderBy");
			if(orderByRequest.equals("clear")){
				params.put("orderBy", null);
			}
			else{
				params.put("orderBy", orderByRequest);
			}
		}
		if (StringUtils.isEmpty(request.getParameter("desc")) == false) {
			params.put("desc", request.getParameter("desc"));
		}
		
		//自定义列
		List<String> colList = getColumnSetting(model,userId,request);
		if(colList != null){
			params.put("cols", StringUtils.join(colList, ","));
		}
		
		model.addAttribute("params", params);
		
		return params;

	}

	/**
	 * 获取/修改自定义列（如果自定义列时发生改动时，要把params中的排序条件都去掉，使页面恢复为按默认字段排序）
	 * 
	 * @param model
	 * @param userId
	 * @param request
	 * @return
	 */
	private List<String> getColumnSetting(Model model , long userId , HttpServletRequest request) {
		
		String defaultSetting = "pv_sum,uv_sum,click_sum,click_rate_sum,cpm_sum_yuan,cpc_sum_yuan,expend_sum_yuan" ;
		String settingName = "reportColumnSetting" ;
		
		// 切换业务库数据源
		CustomerContextHolder
						.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		
		UserFav uf = userFavDao.findByUserIdAndSettingName(userId, settingName);
		
		
		// 如果request中存在字段设置
		if (request.getParameterValues("columnSetting") != null) {
			
			String[] array = request.getParameterValues("columnSetting");
			
			if(uf != null){
				
				String newSetting = StringUtils.join(array,",");
				
				if(uf.getSetting() != null){
					if(uf.getSetting().equals(newSetting) == false){
						uf.setSetting(newSetting);
					}
				}
				else{
					uf.setSetting(newSetting);
				}
			}
			
		}
		// 如果request中不存在字段设置（比如第一次进入）
		else{
			
			if(uf != null){
				if(uf.getSetting() == null){
					uf.setSetting(defaultSetting);
				}
			}
			else{
				
				uf = new UserFav();
				uf.setUserId(userId);
				uf.setSettingName(settingName);
				uf.setSetting(defaultSetting);
				uf.setCreateTime(new Date());
			}
			
		}
		
		List<String> colList = Arrays.asList(uf.getSetting().split(","));
		
		HashMap<String,Integer> columnSetting = new HashMap<String, Integer>();
		for (String s : colList) {
			columnSetting.put(s, 1);
		}
		model.addAttribute("columnSettingMap", columnSetting);
		model.addAttribute("columnSettingList", colList);
		
		// 切换业务库数据源
		CustomerContextHolder
						.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);

		if(uf.getId() == null){
			userFavDao.insert(uf);
		}
		else{
			userFavDao.update(uf);
		}
		
		return colList ;
	}

	/**
	 * 时间报表
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/time")
	public String time(Model model, Integer pageNo , Integer pageSize , HttpServletRequest request) throws Exception{
		
		//由于time报表是报表模块的默认入口，所以在这里要处理一下：如果不是从其他报表页面跳转过来，就把之前保存的查询条件session给清除掉
		String refererURL = request.getHeader("referer");
		if(StringUtils.isEmpty(refererURL) == false){
			if(refererURL.indexOf("report") < 0){
				request.getSession().removeAttribute(Constants.FRONT_REPORT_PARAMS_MAP);
			}
		}
		
		setUserPageInfo(model);
		
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		ActiveUser user = getUserInfo();
		UserFav fav = userFavDao.findOperatePartnerId(user.getUserId());
		long partnerId = Long.parseLong(fav.getSetting());
		Long userId = user.getUserId();

		service.time(model, partnerId, getParamMap(model,userId,request),
				pageNo, pageSize);
		

		return PAGE_DIRECTORY + "report_time";
	}
	
	/**
	 * 活动类型报表
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/campType")
	public String campType(Model model, Integer pageNo , Integer pageSize , HttpServletRequest request) throws Exception{

		setUserPageInfo(model);
		
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		ActiveUser user = getUserInfo();
		UserFav fav = userFavDao.findOperatePartnerId(user.getUserId());
		long partnerId = Long.parseLong(fav.getSetting());
		Long userId = user.getUserId();
		
		service.campType(model, partnerId, getParamMap(model,userId,request),
				pageNo, pageSize);

		return PAGE_DIRECTORY + "report_camp_type";
	}

	/**
	 * 活动报表
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/camp")
	public String camp(Model model, Integer pageNo , Integer pageSize , HttpServletRequest request) throws Exception{

		setUserPageInfo(model);
		
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		ActiveUser user = getUserInfo();
		UserFav fav = userFavDao.findOperatePartnerId(user.getUserId());
		long partnerId = Long.parseLong(fav.getSetting());
		Long userId = user.getUserId();

		service.camp(model, partnerId, getParamMap(model,userId,request),
				pageNo, pageSize);

		return PAGE_DIRECTORY + "report_camp";
	}

	/**
	 * 创意报表
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/creative")
	public String creative(Model model, Integer pageNo , Integer pageSize , HttpServletRequest request) throws Exception{

		setUserPageInfo(model);
		
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		ActiveUser user = getUserInfo();
		UserFav fav = userFavDao.findOperatePartnerId(user.getUserId());
		long partnerId = Long.parseLong(fav.getSetting());
		Long userId = user.getUserId();
		
		service.creative(model, partnerId, getParamMap(model,userId,request),
				pageNo, pageSize);

		return PAGE_DIRECTORY + "report_creative";
	}

	/**
	 * 地域（城市）报表
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/city")
	public String city(Model model, Integer pageNo , Integer pageSize , HttpServletRequest request) throws Exception{

		setUserPageInfo(model);
		
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		ActiveUser user = getUserInfo();
		UserFav fav = userFavDao.findOperatePartnerId(user.getUserId());
		long partnerId = Long.parseLong(fav.getSetting());
		Long userId = user.getUserId();
		
		
		Map<String,Object> params = getParamMap(model,userId,request);
		
		boolean isProvince = true ;
		try {
			if(StringUtils.isEmpty(request.getParameter("provinceName")) == false){
				
				isProvince = false ;
				
				String provinceName = request.getParameter("provinceName");
				
				provinceName = URLDecoder.decode(provinceName, "UTF-8"	);
				
				params.put("provinceName", provinceName);
			}
			else{
				params.put("provinceName", null);
			}
		} catch (Exception e) {
			params.put("provinceName", null);
		}
		
		service.city(model, partnerId, params ,
				pageNo, pageSize);

		if(isProvince){
			model.addAttribute("isProvince", "1");
		}
		return PAGE_DIRECTORY + "report_city";
	}
	
	/**
	 * 时段报表
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/hour")
	public String hour(Model model, Integer pageNo , Integer pageSize , HttpServletRequest request) throws Exception{

		setUserPageInfo(model);
		
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		ActiveUser user = getUserInfo();
		UserFav fav = userFavDao.findOperatePartnerId(user.getUserId());
		long partnerId = Long.parseLong(fav.getSetting());
		Long userId = user.getUserId();
		
		
		Map<String,Object> params = getParamMap(model,userId,request);
		
		String sumOrAvg = request.getParameter("sumOrAvg");
		if(StringUtils.isEmpty(sumOrAvg) == false){
			params.put("sumOrAvg", sumOrAvg);
		}
		else{
			params.put("sumOrAvg", "sum");
		}
		
		service.hour(model, partnerId, params );
		
		String legend = request.getParameter("legend");
		if(StringUtils.isEmpty(legend) == false){
			model.addAttribute("legend", legend);
		}
		else{
			model.addAttribute("legend", 1);
		}

		return PAGE_DIRECTORY + "report_hour";
	}
	
	/**
	 * 获取时间报表excel
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/getTimeExcel")
	public void getTimeExcel(Model model, HttpServletRequest request , HttpServletResponse response) throws Exception{
		service.getExcel(0, response);
		
	}
	/**
	 * 获取活动类型报表excel
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/getCampTypeExcel")
	public void getCampTypeExcel(Model model, HttpServletRequest request , HttpServletResponse response) throws Exception{
		service.getExcel(1, response);
		
	}
	/**
	 * 获取活动报表excel
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/getCampExcel")
	public void getCampExcel(Model model, HttpServletRequest request , HttpServletResponse response) throws Exception{
		service.getExcel(2, response);
		
	}
	/**
	 * 获取创意报表excel
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/getCreativeExcel")
	public void getCreativeExcel(Model model, HttpServletRequest request , HttpServletResponse response) throws Exception{
		service.getExcel(3, response);
		
	}
	/**
	 * 获取地域报表excel
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/getCityExcel")
	public void getCityExcel(Model model, HttpServletRequest request , HttpServletResponse response) throws Exception{
		
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);

		ActiveUser user = getUserInfo();
		Long userId = user.getUserId();
		UserFav fav = userFavDao.findOperatePartnerId(user.getUserId());
		long partnerId = Long.parseLong(fav.getSetting());
		
		Map<String,Object> params = getParamMap(model,userId,request);
		
		try {
			if(StringUtils.isEmpty(request.getParameter("provinceName")) == false){
				
				params.put("provinceName", request.getParameter("provinceName"));
				
			}
			else{
				params.put("provinceName", null);
			}
			
		} catch (Exception e) {
			params.put("provinceName", null);
		}
		
		service.downloadCityReport(model, partnerId, params, response);
		
	}
	/**
	 * 获取时段报表excel
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/getHourExcel")
	public void getHourExcel(Model model, HttpServletRequest request , HttpServletResponse response) throws Exception{
		
		service.getExcel(5, response);
		
	}
}
