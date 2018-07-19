package com.jtd.web.controller.admin;

import com.jtd.commons.mybatis.support.CustomerContextHolder;
import com.jtd.commons.page.Pagination;
import com.jtd.utils.AccountAmountUtil;
import com.jtd.utils.DateUtil;
import com.jtd.web.constants.*;
import com.jtd.web.controller.BaseController;
import com.jtd.web.dao.*;
import com.jtd.web.exception.PlatformException;
import com.jtd.web.po.*;
import com.jtd.web.service.admin.IAdminCampService;
import com.jtd.web.service.admin.IAdminPartnerPreFlowService;
import com.jtd.web.service.admin.IAdminPartnerService;
import com.jtd.web.service.admin.IAdminUserPartnerService;
import com.jtd.web.vo.ChannelCategory;
import com.jtd.web.vo.TreeNodeVo;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/admin/partner")
public class AdminPartnerController extends BaseController {

	public static final String PAGE_DIRECTORY = "/admin/partner/";
	public static final String ACTION_PATH = "redirect:/admin/partner/";

	@Autowired
	private IAdminPartnerService partnerService;
	
	@Autowired
	private IAdminUserPartnerService adminUserPartnerService;

	@Autowired
	private IAdminCampService adminCampService;
	
	@Autowired
	private IPartnerDao partnerDao ;
	
	@Autowired
	private ISysUserRoleDao userRoleDao ;
	
	@Autowired
	private IUserPartnerDao userPartnerDao ;

	@Autowired
	private IAdminPartnerPreFlowService adminPartnerPreFlowService;
	
	@Autowired
	private IBossRequestLogDao bossRequestLogDao ;

    @Autowired
    private IPartnerAccFlowDao partnerAccFlowDao;
	
    /**
     * 分页显示 + 条件查找
     * 
     * @param model
     * @param pageNo
     * @param pageSize
     * @param request
     * @return
     * @throws PlatformException
     */
	@RequestMapping("/list")
	public String list(Model model, Integer pageNo,Integer pageSize, HttpServletRequest request)
			throws PlatformException {

		// Shiro用户信息
		setUserPageInfo(model);

		// String partnerName = request.getParameter("partnerName");
		// String partnerType = request.getParameter("partnerType");
		// String partnerPName = request.getParameter("partnerPName");

		HashMap<String, Object> paraMap = new HashMap<String, Object>();
		if (StringUtils.isEmpty(request.getParameter("partnerName")) == false) {
			paraMap.put("partnerName", request.getParameter("partnerName"));
		}
		if (StringUtils.isEmpty(request.getParameter("partnerType")) == false) {
			paraMap.put("partnerType", request.getParameter("partnerType"));
		}
		if (StringUtils.isEmpty(request.getParameter("partnerPName")) == false) {
			paraMap.put("partnerPName", request.getParameter("partnerPName"));
		}
		if (StringUtils.isEmpty(request.getParameter("status")) == false) {
			paraMap.put("status", request.getParameter("status"));
		}
		if (StringUtils.isEmpty(request.getParameter("region")) == false) {
			paraMap.put("region", request.getParameter("region"));
		}
		if (StringUtils.isEmpty(request.getParameter("city")) == false) {
			paraMap.put("city", request.getParameter("city"));
		}
		if (StringUtils.isEmpty(request.getParameter("updateFristOnlineStratTime")) == false) {
			paraMap.put("updateFristOnlineStratTime",request.getParameter("updateFristOnlineStratTime"));
		}
		if (StringUtils.isEmpty(request.getParameter("updateFristOnlineEndTime")) == false) {
			paraMap.put("updateFristOnlineEndTime", request.getParameter("updateFristOnlineEndTime"));
		}
		
		ActiveUser user = getUserInfo();
		
		//普通用户
		if(!user.isAdminOrManager() && !user.isOperateDirectorOrManager()) { 
			// 如果不是管理员，也不是运营经理主管，就要受partner访问权限的控制（如运营或代理公司用户）
			paraMap.put("userId", user.getUserId());
			//只看直属一级的广告主
            paraMap.put("pId", user.getPartner().getPid());
			// 普通用户在后台中不能编辑操作自己所在的公司，只能由上级公司的用户去编辑；无上级的公司，由【jtd公司】运营去编辑
			paraMap.put("noPartnerId",user.getPartner().getId());   
		}
		//运营经理和主管可以查看所有运营负责的广告主
		if(user.isOperateDirectorOrManager()){
			//只看直属一级的广告主
            paraMap.put("pId", user.getPartner().getPid());
            //运营经理和主管在后台中不能编辑操作自己所在的公司【jtd公司】
            paraMap.put("noPartnerId", user.getPartner().getId());
        }
		model.addAttribute("queryMap", paraMap);

		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		Pagination<Partner> page = partnerService.findPageBy(paraMap,pageNo, pageSize);
		
		//如果不是admin登录，就不显示【jtd公司】
		List<Partner> list = page.getList();
		List<Partner> filteredList = new ArrayList<Partner>();
		if(list != null){
			
			for (Partner partner : list) {
				
				if(partner.getId() == Constants.CE_PARTNER_ID){
					if(!user.isAdminOrManager()){
						continue ;
					}
				}
				//读取行业信息
				ChannelCategory cc = partnerService.getPartnerCategory(partner.getId());
				if(cc != null){
					partner.setCategoryName(cc.getName());
				}
				
				//如果是从boss系统同步过来的，要查询该用户是否已经跟boss系统同步成功
				if(StringUtils.isEmpty(partner.getBossPartnerCode()) == false){
					
					//查询boss接口交互日志
					BossRequestLog param = new BossRequestLog();
					//
					param.setBossPartnerCode(partner.getBossPartnerCode());
					//类型为回调
					param.setRequestType(BossApiType.CALLBACK_BOSS.getCode());
					//成功的标识=1
					param.setResultCode("1"); 
					List<BossRequestLog> logs = bossRequestLogDao.listBy(param);
					if(logs != null && logs.size() > 0){
						//表示成功
						partner.setBossCallbackResult(1);
					}
					else{
						partner.setBossCallbackResult(0);
					}
				}
				
				filteredList.add(partner);
			}
			page.setList(filteredList);
		}

		model.addAttribute("page", page);
		model.addAttribute("userRole",user.getRoleId());

		return PAGE_DIRECTORY + "partner_list";
	}
	
	public static boolean filter(Partner partner , List<Partner> allPartnerList , Map<String, Object> paraMap){
		
		boolean c = false;
		if (paraMap.get("partnerName") != null) {
			String partnerNam = (String) paraMap.get("partnerName");
			if (partnerNam != partner.getPartnerName()) {
				c = true;
			}
		}
		
		
		
		long partnerId = partner.getId();
		
		//先找到指定广告主的下级
		List<Partner> sublist = new ArrayList<Partner>();
		
		for (Partner subPartner : allPartnerList) {
			
			long pid = subPartner.getPid();
			
			if(pid == partnerId){
				sublist.add(subPartner);
			}
			
		}
		
		//开始看下级或下级的下级有没有符合条件的
		
		for (Partner subPartner : sublist) {
			
			if(filter(subPartner , allPartnerList , paraMap)){
				return true ;
			}
			
		}
		
		return false ;
	}
	
	/**
     * 广告主运营管理列表
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

		ActiveUser user = getUserInfo();
		
		model.addAttribute("queryMap", paraMap);
		
		//只看直属一级的广告主
       // paraMap.put("pId", user.getPartner().getPid());
        
		//普通运营
		if(!user.isAdminOrManager() && !user.isOperateDirectorOrManager()) { 
			// 如果不是管理员，也不是运营经理主管，就要受partner访问权限的控制
			//paraMap.put("userId", user.getUserId()); 
		}

		partnerService.partnerOperatorList(model , paraMap, pageNo, 50 , user);
	
		
		return PAGE_DIRECTORY + "partner_operation_list";
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
			
			List<Map<String,Object>> childrenList =  partnerService.findChildren(paraMap);
		
			resultMap.put("success", true);
			resultMap.put("partner", childrenList);
		} catch (Exception e) {
			logger.error("根据partnerId查询广告主下级代理出错",e);
		}
		
		
//		//读取行业信息
//		for (Partner partner : childrenList) {
//			ChannelCategory cc = partnerService.getPartnerCategory(partner.getId());
//			if(cc != null){
//				partner.setCategoryName(cc.getName());
//			}
//		}
		
		return resultMap;
	}
	
	/**
	 * 处理广告主列表的查询参数
	 * 
	 * @param model
	 * @param request
	 * @return
	 */
	private HashMap<String, Object> processPartnerOperationParam(HttpServletRequest request){

		HashMap<String, Object> paraMap = new HashMap<String, Object>();
		
		//广告主名称
		if (StringUtils.isEmpty(request.getParameter("partnerName")) == false) {
			paraMap.put("partnerName", request.getParameter("partnerName"));
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
		//上级代理名称
		if (StringUtils.isEmpty(request.getParameter("partnerPName")) == false) {
			paraMap.put("partnerPName", request.getParameter("partnerPName"));
		}
		//区域
		if (StringUtils.isEmpty(request.getParameter("region")) == false) {
			paraMap.put("region", request.getParameter("region"));
		}
		//城市
		if (StringUtils.isEmpty(request.getParameter("city")) == false) {
			paraMap.put("city", request.getParameter("city"));
		}
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		
		//上线时间
		String updateFristOnlineStratTimeString = request.getParameter("updateFristOnlineStratTime");
		if (StringUtils.isEmpty(updateFristOnlineStratTimeString) == false) {
			try {
				paraMap.put("updateFristOnlineStratTimeString", updateFristOnlineStratTimeString);
				paraMap.put("updateFristOnlineStratTime", df.parse(updateFristOnlineStratTimeString));
			} catch (Exception e) {
			}
		}
		String updateFristOnlineEndTimeString = request.getParameter("updateFristOnlineEndTime");
		if (StringUtils.isEmpty(updateFristOnlineEndTimeString) == false) {
			try {
				paraMap.put("updateFristOnlineEndTimeString", updateFristOnlineEndTimeString);
				paraMap.put("updateFristOnlineEndTime", df.parse(updateFristOnlineEndTimeString));
			} catch (Exception e) {
			}
		}
		
		return paraMap ;
	}
	
	/**
	 * 根据ID查询广告主及下面的子集数据
	 * @param model
	 * @param partner
	 * @return
	 * @throws PlatformException 
	 */
	@RequestMapping("/listChildren")
	@ResponseBody
	public Map<String,Object> listChildren(Model model,Partner partner,String userId) throws PlatformException{
		Partner obj= partnerService.getById(partner.getId());
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("id", obj.getId());
		params.put("pid", obj.getPid());
		if(!StringUtils.isEmpty(userId)){
			params.put("userId", userId);
		}
		List<Map<String,Object>> partnerList = partnerService.listAllChildren(params);

		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("partnerList", partnerList);
		resultMap.put("success", true);
		return resultMap;
	}
	
	/**
	 * 根据ID查询广告主及下面的子集数据
	 * @param model
	 * @param partner
	 * @return
	 * @throws PlatformException 
	 */
	@RequestMapping("/listTree")
	@ResponseBody
	public Map<String,Object> listTree(Model model,Partner partner,String userId) throws PlatformException{
		Partner obj= partnerService.getById(partner.getId());
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		Map<String,Object> partnerListQueryMap = new HashMap<String,Object>();
		partnerListQueryMap.put("id", obj.getId());
		partnerListQueryMap.put("pid", obj.getPid());
		partnerListQueryMap.put("userId",userId);
		List<Map<String,Object>> partnerList = partnerService.listTreeByMap(partnerListQueryMap);
        ActiveUser activeUser  = getUserInfo();
		if(partnerList.size()>0) {
			partnerList = partnerService.sortTreeTable(partner,partnerList,activeUser);
		}
		resultMap.put("partnerList", partnerList);

        List<Map<String,Object>> poList = partnerService.findPartnerAndChild(partnerListQueryMap);
        if(poList.size()>0) {
            poList = partnerService.sortTreeTable(partner,poList,activeUser);
        }
        resultMap.put("poList", poList);

		List<Map<String,Object>> roleTypeList = new ArrayList<Map<String,Object>>();
		Map<String,Object> map = null;
		for (RoleType r : RoleType.values()) {

			map = new HashMap<String,Object>();
			if(r == RoleType.ADMIN){
				continue;
			}
			map.put("code",r.getCode());
			map.put("desc",r.getDesc());
			if(partner.getId() > 1){ //其它公司
				// 广告主直客 广告主代理
				if(r == RoleType.PARTNER || r ==RoleType.PARTNER_PROXY || r == RoleType.PARTNER_READONLY){
					roleTypeList.add(map);
				}
			}else {
				if(r != RoleType.PARTNER && r !=RoleType.PARTNER_PROXY && r != RoleType.PARTNER_READONLY ){
					roleTypeList.add(map);
				}
			}
		}
		resultMap.put("roleTypeList", roleTypeList);

		//用户与广告主关联数据
		UserPartner up = new UserPartner();
		up.setUserId(userId);
		List<UserPartner> upList = adminUserPartnerService.listBy(up);
		resultMap.put("upList",upList);
		
		resultMap.put("success", true);
		return resultMap;
	}

	/**
	 * 到添加页面
	 * 
	 * @param model
	 * @return
	 * @throws PlatformException
	 */
	@RequestMapping("/add")
	@RequiresPermissions("partner:create")
	public String add(Model model) throws PlatformException {

		// Shiro用户信息
		setUserPageInfo(model);
        CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		
		ActiveUser user = getUserInfo();
		if(user != null){
			Partner userBelongingPartner = user.getPartner();
			
			//1.将user所属partner发送到页面 & 把所属partner的个性化参数发送到页面，直接填写到新建partner的输入框中
			if(userBelongingPartner != null){
				
				model.addAttribute("userBelongingPartnerId", userBelongingPartner.getId());
				
				//向页面发送当前user所属partner的个性化参数，直接填写到新建partner的输入框中
				Partner p = new Partner();
				p.setLoginUrl(userBelongingPartner.getLoginUrl());
				p.setStyleCode(userBelongingPartner.getStyleCode());
				p.setCopyrightStartYear(userBelongingPartner.getCopyrightStartYear());
				p.setWebsiteRecordCode(userBelongingPartner.getWebsiteRecordCode());
				model.addAttribute("partner", p);
			}
			
			//2.查询【jtd公司】运营人员列表&发送到页面
			//只有当前操作用户为【jtd公司】员工（比如admin和其他运营人员）, 并且正在编辑的partner不是"【jtd公司】"，才显示“【jtd公司】运营人员”选择框
			if(user.getPartner().getId() == Constants.CE_PARTNER_ID){
				
				List<SysUser> operatorList = new ArrayList<SysUser>();
				//如果是管理员或运营主管，查询出所有的运营人员发送到页面提供选择(角色是运营的user)
				if(user.isAdminOrManager() || user.isOperateDirectorOrManager()){
					operatorList = userRoleDao.listUserByRole(RoleType.OPERATE);
					model.addAttribute("operatorList", operatorList);
				}
				//如果是运营人员，“【jtd公司】运营人员”选项默认为自己
				if(user.isOperateUser()){
					SysUser sysuser = new SysUser();
					sysuser.setId(user.getUserId());
					sysuser.setUserName(user.getUserName());
					operatorList.add(sysuser);
					model.addAttribute("operatorList", operatorList);
				}
			}
			
			
			//3.查询上级代理列表&发送到页面
			HashMap<String, Object> params = new HashMap<String, Object>();
			/**
			 * 运营可以为【jtd公司】建立直属下级，或者【jtd公司】的平级（即上级是“无”）
			 * 普通用户只能为自己的公司建立直属下级
			 * admin和管理员没有限制，但只显示代理类型和oem类型的，直客不显示
			 */
			if(user.isAdminOrManager()) {
				params.put("partnerType", "0,2");
			}
			else{
				params.put("id", userBelongingPartner.getId());
			}
			List<Partner> partnerPList = partnerDao.listByMap(params);
			model.addAttribute("pList", partnerPList);
		}
		
		
		return PAGE_DIRECTORY + "partner_edit";
	}

	/**
	 * 到修改页面
	 * 
	 * @param model
	 * @param id
	 * @return
	 * @throws PlatformException
	 */
	@RequestMapping("/edit")
	@RequiresPermissions("partner:update")
	public String edit(Model model, long id) throws PlatformException {

		// Shiro用户信息
		setUserPageInfo(model);
		
		//1.查询partner信息，发送到页面
		Partner editingPartner = partnerService.getById(id);
		model.addAttribute("partner", editingPartner);
		
		ActiveUser user = getUserInfo();
		if(user != null){
			Partner userBelongingPartner = user.getPartner();
			
			//2.将user所属partner发送到页面
			if(userBelongingPartner != null){
				
				model.addAttribute("userBelongingPartnerId", userBelongingPartner.getId());
				
			}
			
			//3.查询【jtd公司】运营人员列表&发送到页面
			//只有当前操作用户为【jtd公司】员工（比如admin和其他运营人员）, 并且正在编辑的partner不是"【jtd公司】"，才显示“【jtd公司】运营人员”选择框
			List<SysUser> operatorList = new ArrayList<SysUser>();
            CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
			if(editingPartner.getId() != Constants.CE_PARTNER_ID){
				//查找该广告主目前的运营人员id
				Map<String,Object> operatorInfo = userPartnerDao.findByPartnerAndRole(id, RoleType.OPERATE);
				if(operatorInfo != null){
					model.addAttribute("operatorId", operatorInfo.get("user_id"));
				}
				
				if(user.isAdminOrManager() || user.isOperateDirectorOrManager()){
					//查询出所有的运营人员发送到页面提供选择(角色是运营的user)
					operatorList = userRoleDao.listUserByRole(RoleType.OPERATE);
					model.addAttribute("operatorList", operatorList);
				}
				if(user.isOperateUser()){
					SysUser sysuser = new SysUser();
					sysuser.setId(user.getUserId());
					sysuser.setUserName(user.getUserName());
					operatorList.add(sysuser);
					model.addAttribute("operatorList", operatorList);
				}
			}
			
			//4.查询上级代理列表&发送到页面
			HashMap<String, Object> params = new HashMap<String, Object>();
			/**
			 * 运营可以为【jtd公司】建立直属下级，或者【jtd公司】的平级（即上级是“无”）
			 * 普通用户只能为自己的公司建立直属下级
			 * admin和管理员没有限制
			 * 上级代理列表只显示代理类型和oem类型的，不显示直客类型
			 */
			if(user.isAdminOrManager()) {
				params.put("partnerType", "0,2");
			}
			else{
				params.put("id", userBelongingPartner.getId());
			}
			List<Partner> partnerPList = partnerDao.listByMap(params);
			model.addAttribute("pList", partnerPList);
		}
		
		return PAGE_DIRECTORY + "partner_edit";
	}
	
	/**
	 * ajax验证广告主名称不能重复
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/validatePartnerName")
	@ResponseBody
	public String validatePartnerName(HttpServletRequest request)
			throws Exception {
		String partnerName = request.getParameter("partnerName");
		String editingPartnerId = request.getParameter("editingPartnerId");
		
		if(StringUtils.isEmpty(partnerName) == false){
			partnerName = URLDecoder.decode(partnerName, "UTF-8"); 
		}
		else{
			return "0" ;
		}
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("exactPartnerName",partnerName);
		
		if(StringUtils.isEmpty(editingPartnerId) == false){
			params.put("editingPartnerId",editingPartnerId);
		}
        CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		List<Partner> resultList = partnerDao.listByMap(params);
		if(resultList != null && resultList.size() > 0){
			return "0" ;
		}
		else{
			return "1" ;
		}
	}

	/**
	 * 添加页面或修改页面都调用的保存方法
	 * 
	 * @param partner
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/save")
	@RequiresPermissions("partner:create")
	public String save(Partner partner, HttpServletRequest request)
			throws Exception {

		
		//设置【jtd公司】运营人员
		//如果当前操作用户不是【jtd公司】员工，或者当前编辑的partner是【jtd公司】，都不会有operatorUserId参数  2017-1-4
		Long newOperatorUserId = null ;
		if(StringUtils.isEmpty(request.getParameter("operatorUserId")) == false){
			newOperatorUserId = Long.valueOf(request.getParameter("operatorUserId"));
		}
		
		// “版权所有的网址” 参数的处理，需要去掉http://
		if (partner.getLoginUrl() != null) {
			if (partner.getLoginUrl().startsWith("http://")
					|| partner.getLoginUrl().startsWith("https://")) {
				String s = partner.getLoginUrl()
						.replaceAll("http://", "")
						.replaceAll("https://", "");
				partner.setLoginUrl(s);
			}
		}
		
		//“官方网站”参数的处理，需要加上http://
		if (partner.getWebsiteUrl() != null) {
			if (partner.getWebsiteUrl().startsWith("http://") == false && partner.getWebsiteUrl().startsWith("https://") == false) {
				String s = "http://" + partner.getWebsiteUrl();
				partner.setWebsiteUrl(s);
			}
		}
		
		ActiveUser user = getUserInfo() ;
		
		boolean isInsert = true ;
		if (partner.getId() != null) {
			isInsert = false ;
		}
		
		partnerService.saveOrUpdate(partner, newOperatorUserId, user , request , isInsert);
		
		if(!user.isAdminOrManager() && !user.isOperateDirectorOrManager() && !user.isOperateUser()) { 
			
			return ACTION_PATH + "list.action";
			
		} else {
			
			return ACTION_PATH + "operationList.action";
		}
		 
	}

	/**
	 * 修改广告主开启/停用状态
	 * 
	 * 停用操作：级联查询广告主及其所有下级，将其所有下级置为同样状态，并把所有广告主在投的活动手动暂停,并且发送到mq
	 * 
	 * @param id
	 * @param status
	 * @param pid
	 * @return
	 * @throws PlatformException
	 */
	@RequestMapping("/changeStatus")
	@RequiresPermissions("partner:changeStatus")
	@ResponseBody
	public Map<String,Object> changeStatus(long id, Integer status , long pid )
			throws PlatformException {
		
		boolean flag = false ;
		String retStr = "";
		
		PartnerStatus statusEnum = PartnerStatus.fromCode(status);
		
		
		if (statusEnum != null) {
			
			//开启广告主
			if(statusEnum == PartnerStatus.START){
				
				// 开启广告主状态
				long result = partnerService.updateStatus( id, statusEnum);
				
				if(result == -1){
					retStr += "广告主ID:["+id+"]资质未通过平台内部审核,无法开启.<br/>" ;
				}
				else if(result == -2){
					retStr += "广告主ID:["+id+"]资质未提交渠道审核或存在被拒绝的渠道审核记录,无法开启.<br/>" ;
				}
				
			}
			//停用广告主
			else{
				
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("id",id);
				map.put("pid", pid);
				
				List<Map<String, Object>> partnerList = partnerService.listTreeByMap(map);
				
				Long[] stopCampaignPartnerIds = null ;
				
				if(partnerList != null && partnerList.size() > 0){
					
					stopCampaignPartnerIds = new Long[partnerList.size()] ;
					
					int cnt = 0 ;
					for (Map<String, Object> partnerMap : partnerList) {
						
						// 停止广告主状态
						long partnerId = Long.parseLong(partnerMap.get("id").toString());
						long result = partnerService.updateStatus( id, statusEnum);
						if(result < 0){
							retStr += "广告主ID:["+id+"]未能置为停用，请联系管理员.<br/>" ;
						}
						stopCampaignPartnerIds[cnt++] = partnerId ;
						
					}
					
				}
				
				//广告主停用时把广告主建立的活动(手动状态是投放中的)全部暂停
				retStr += adminCampService.stopCampaigns(stopCampaignPartnerIds);
				
			}
		}
		
		
		if(StringUtils.isEmpty(retStr)) {
			flag = true ;  //表示均成功执行
			retStr = "ok";
		}
		
		Map<String,Object> retMap = new HashMap<String, Object>();
		retMap.put("flag",flag);
		retMap.put("msg",retStr);
		return retMap;
	}

	/**
	 * 记录第一次上线时间
	 * 
	 * @param partnerId
	 * @return
	 * @throws PlatformException
	 */
	@RequestMapping("/updateFirstOnlineTime")
	@RequiresPermissions("partner:firstOnlineTime")
	@ResponseBody
	public Map<String, Object> updateFirstOnlineTime(long partnerId) throws PlatformException{
		partnerService.updateFirstOnlineTime(partnerId);
		Map<String, Object> updateFirstOnlineTime = new HashMap<String, Object>();
		updateFirstOnlineTime.put("msg", "ok");
		return updateFirstOnlineTime;
	}
	
	
	/**
	 * 到充值页面
	 * 
	 * @param model
	 * @param id
	 * @return
	 * @throws PlatformException
	 */
	@RequestMapping("/recharge")
	public String recharge(Model model, long id) throws PlatformException {

		// Shiro用户信息
		setUserPageInfo(model);

		PartnerPreFlow partnerPreFlow = adminPartnerPreFlowService.getById(id);

		model.addAttribute("partnerPreFlow", partnerPreFlow);

		return PAGE_DIRECTORY + "partner_recharge";

	}

	/**
	 * 财务退款
	 * @param model
	 * @param id
	 * @return
	 * @throws PlatformException
     */
	@RequestMapping("/refund")
	public String refund(Model model, long id) throws PlatformException {

		// Shiro用户信息
		setUserPageInfo(model);

		PartnerPreFlow partnerPreFlow = adminPartnerPreFlowService.getById(id);
        partnerPreFlow.setPreAmount(-partnerPreFlow.getPreAmount());
		model.addAttribute("partnerPreFlow", partnerPreFlow);

		return PAGE_DIRECTORY + "partner_refund";

	}


    /**
     * 财务充值/退款
     * @param model
     * @param partnerPreFlow
     * @return
     * @throws PlatformException
     */
	@RequestMapping("/rechargeSave")
	@ResponseBody
	public Map<String, Object> rechargeSave(Model model, @RequestBody PartnerPreFlow partnerPreFlow) throws PlatformException {

		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		ActiveUser user = getUserInfo();

		long partnerId = partnerPreFlow.getPartnerId();
		// 财务输入的实际金额
		long amountFen = partnerPreFlow.getAmount();

		if (user != null) {

			// 验证广告主的账户余额没有超过限制
			long allowAmount = 0;
			try {

			    //被操作广告主
                Partner p = partnerService.getById(partnerId);

                if(partnerPreFlow.getType()==0) {
                    //充值金额,广告主的余额总数不能超过10万
                    allowAmount = partnerService.validAcountRechargeLimit(partnerId);
                }else {
                    //退款不能超过被退帐户的余额
                    allowAmount = p.getAccBalance();
                }
                
                if (amountFen > allowAmount) {
                    resultMap.put("msg", "拒绝提交：账户最高限额为：￥" + AccountAmountUtil.getAmountYuanString(Constants.PARTNER_BALANCE_LIMIT) 
                    		+ "，广告主可充余额为：￥" + AccountAmountUtil.getAmountYuanString(allowAmount));
                    
                    //"，广告主可充余额为：￥" + ( Double.parseDouble(String.valueOf(allowAmount)) / 100) + "")
                    
                } else {
                    if (partnerPreFlow.getStatus() == 1) {// 确认
                        int toTradeType = TradeType.RECHARGE.getCode(); //默认充值
                        if(partnerPreFlow.getType()==1){
                            toTradeType = TradeType.REFUND.getCode(); //退款
                        }
                        partnerService.updateAccountBalance(p, amountFen, user, 1, -1, toTradeType, partnerPreFlow);

                    } else {
                        //拒绝操作,没有流水信息
                        partnerPreFlow.setOperatorId(user.getUserId());
                        partnerPreFlow.setOperatorName(user.getUserName());
                        partnerPreFlow.setModifyTime(new Date());
                        adminPartnerPreFlowService.update(partnerPreFlow);
                    }
                    resultMap.put("msg", "ok");
                }
			} catch (PlatformException e) {
				resultMap.put("msg", e.getMessage());
			} catch (Exception e) {
				logger.error("充值发生内部错误 , partner_id=" + partnerId, e);
				resultMap.put("msg", "发生内部错误");
			}

		} else {
			throw new PlatformException(
					PlatformException.USER_INFO_NULL_MESSAGE);
		}

		return resultMap;
	}

	/**
	 * 跳转到代理广告主充值页面(代理广告主只能给其下面的代理或者直客充值,不能给自己充值)
	 * @param model
	 * @param id
	 * @return
	 * @throws PlatformException
	 */
	@RequestMapping("/proxyRecharge")
	public String proxyRecharge(Model model, long id) throws PlatformException {

		// Shiro用户信息
		setUserPageInfo(model);

		Partner partner = partnerService.getById(id);

		model.addAttribute("partner", partner);

		return PAGE_DIRECTORY + "partner_proxy_recharge";

	}


    /**
     * 广告主代理充值(代理广告主只能给其下面的代理或者直客充值,不能给自己充值)
     * @param model
     * @param request
     * @return
     * @throws PlatformException
     */
	@RequestMapping("/proxyRechargeSave")
	@ResponseBody
	public Map<String,Object> proxyRechargeSave(Model model, HttpServletRequest request) throws PlatformException {

		HashMap<String,Object> map = new HashMap<String, Object>();

		ActiveUser activeUser = getUserInfo();

		long id = 0;
		long amountFen = 0;
		try {
			id = Long.parseLong(request.getParameter("id"));
			amountFen = Long.parseLong(request.getParameter("amount"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new PlatformException(PlatformException.PARAMETER_ERROR_MESSAGE);
		}

		if (activeUser != null) {

			long allowAmount = partnerService.validAcountRechargeLimit(id);
			Partner partner = activeUser.getPartner(); //当前登录用户所属广告主
			long amount = partner.getAccBalance();
            allowAmount = (allowAmount>amount)?amount:allowAmount;

			if (amountFen > allowAmount) {

				map.put("msg", "充值失败：广告主还能充值的金额是：￥" + (allowAmount / 100) + "");

			} else {

				int fromTradeType = TradeType.PROXY_TRANSFER.getCode(); //代理转帐
				int toTradeType = TradeType.PROXY_RECHARGE.getCode(); //代理充值
				//被操作广告主
				Partner p = partnerService.getById(id);
				long newAccBalance = partnerService.updateAccountBalance(p, amountFen, activeUser,0,fromTradeType,toTradeType,null);

				map.put("msg", "ok");
				map.put("newAccBalance", AccountAmountUtil.getAmountYuanString(newAccBalance));
			}
			return map;

		} else {
			throw new PlatformException(PlatformException.USER_INFO_NULL_MESSAGE);
		}

	}


	/**
	 * 跳转到代理广告主退款页面(代理广告主只能给其下面的代理或者直客退款,不能给自己退款)
	 * @param model
	 * @param id
	 * @return
	 * @throws PlatformException
	 */
	@RequestMapping("/proxyRefund")
	public String proxyRefund(Model model, long id) throws PlatformException {

		// Shiro用户信息
		setUserPageInfo(model);

		Partner partner = partnerService.getById(id);

		model.addAttribute("partner", partner);

		return PAGE_DIRECTORY + "partner_proxy_refund";

	}

	/**
	 * 广告主退款(代理广告主只能给其下面的代理或者直客充值,不能给自己充值)
	 * @param model
	 * @param id 广告主ID
	 * @return
	 * @throws PlatformException
	 */
	@RequestMapping("/proxyRefundSave")
	@ResponseBody
	public Map<String,Object> proxyRefundSave(Model model,long id,long amountFen) throws PlatformException {

		HashMap<String,Object> map = new HashMap<String, Object>();
		ActiveUser activeUser = getUserInfo();

		if (activeUser != null) {
			Partner partner = partnerService.getById(id);
			long amount = partner.getAccBalance();
			if(amountFen > amount) {
				map.put("msg", "退款失败：退款金额大于广告主用户的余额：￥" + (amount / 100) + "");
			}else {
				int fromTradeType = TradeType.AMOUNT_BACK.getCode(); //广告主发起退款
				int toTradeType = TradeType.CLAW_BACK.getCode(); //广告主被退款
				long newAccBalance =  partnerService.updateAccountBalance(partner, amountFen, activeUser,0,fromTradeType,toTradeType,null);

				map.put("msg", "ok");
				map.put("newAccBalance", AccountAmountUtil.getAmountYuanString(newAccBalance));
			}
			return map;

		} else {
			throw new PlatformException(PlatformException.USER_INFO_NULL_MESSAGE);
		}

	}

	@RequestMapping("/blacklist")
	public String blacklist(Model model, long id) throws PlatformException {

		// Shiro用户信息
		setUserPageInfo(model);

		Partner partner = partnerService.getById(id);
		
		PartnerDim dim = partnerService.loadBlacklist(id);

		model.addAttribute("partner", partner);
		model.addAttribute("dim", dim);

		return PAGE_DIRECTORY + "partner_blacklist";
	}

	/**
	 * 保存黑名单
	 * 
	 * @param model
	 * @param request
	 * @return
	 * @throws PlatformException
	 */
	@RequestMapping("/blacklistSave")
	public String blacklistSave(Model model,HttpServletRequest request) throws PlatformException {

		// Shiro用户信息
		setUserPageInfo(model);

		long partnerId = 0;
		String dimValue = "";
		try {
			partnerId = Long.parseLong(request.getParameter("partner_id"));
			dimValue = request.getParameter("dim_value");
		} catch (Exception e) {
			e.printStackTrace();
			throw new PlatformException(
					PlatformException.PARAMETER_ERROR_MESSAGE);
		}
		
		ActiveUser user = getUserInfo() ;
		
		partnerService.saveOrUpdateBlacklist(partnerId, dimValue);

		if(!user.isAdminOrManager() && !user.isOperateDirectorOrManager() && !user.isOperateUser()) { 
			
			return ACTION_PATH + "list.action";
			
		} else {
			
			return ACTION_PATH + "operationList.action";
		}
		
	}
	
	/**
	 *根据partner的id设置毛利
	 *
	 *@param id
	 * 
	 */
	@RequestMapping("/grossProfit")
	@RequiresPermissions("partner:grossProfit")
	public String grossProfit(Model model, long id) throws PlatformException {
		
		// Shiro用户信息
		setUserPageInfo(model);
		
		Partner partner = partnerService.getById(id);
		
		model.addAttribute("partner",partner);
		
		return PAGE_DIRECTORY + "partner_gross_profit";
	}
	
	/**
	 *根据partner保存毛利设置
	 * @param model
	 * @param request
	 * @return
	 * @throws PlatformException  
	 */
	@RequestMapping("grossProfitSave")
	public String grossProfitSave(Model model, HttpServletRequest request) throws PlatformException{
		
		// Shiro用户信息
		setUserPageInfo(model);
		
		Partner partner = new Partner();
		long partnerId = 0;
		String grossType = request.getParameter("gross_type");
		String grossOther = request.getParameter("gross_other");
		partnerId = Long.parseLong(request.getParameter("partner_id"));
		if (grossType != null && grossType.equals("0")) {
			partner.setGrossProfit(0.3);
		} else {
			try {
				partner.setGrossProfit(Double.parseDouble(grossOther) / 100d);
			} catch (Exception e) {
				e.printStackTrace();
				throw new PlatformException(
						PlatformException.PARAMETER_ERROR_MESSAGE);
			}
		}
		partnerService.updateGrossProfit(partnerId, partner);
		
		ActiveUser user = getUserInfo();
		
		if(!user.isAdminOrManager() && !user.isOperateDirectorOrManager() && !user.isOperateUser()) { 
			
			return ACTION_PATH + "list.action";
			
		} else {
			
			return ACTION_PATH + "operationList.action";
		}
		
	}
	
	/**
	 * 根据partner的id读取相应logo并写图片流
	 * 
	 * @param request
	 */
	@RequestMapping("/showLogoImage")
	public void showLogoImage(HttpServletResponse response,
			HttpServletRequest request) {
		
		Partner partner = null;
		
		if(request.getParameter("partnerId") != null ){
			long partnerId = Long.parseLong(request.getParameter("partnerId"));
			partner = partnerDao.getById(partnerId);
		}
		else{
			ActiveUser user = getUserInfo();
			partner = user.getPartner();
		}

		if(partner != null){
			
			String logoPath = "" ;
			
			try {
				
				logoPath = Constants.LOGO_IMG_PATH + File.separator + partner.getLogoImg() ;
				
				File file = new File(logoPath);
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
				logger.error("AdminPartnerController----showLogo----读取logo文件出错,partner=" + partner.getId() + ",logoPath=" + logoPath , e);
			}
		}

	}
	
	/**
	 * 调用boss广告主开户回调接口
	 * 
	 * @param partnerId
	 * @param bossPartnerCode
	 * @return
	 * @throws PlatformException
	 */
	@RequestMapping("/callbackBoss")
	@RequiresPermissions("partner:callbackBoss")
	@ResponseBody
	public Map<String,Object> callbackBoss(long partnerId,String bossPartnerCode) throws PlatformException {
		
		Map<String,Object> retMap = new HashMap<String, Object>();
		long code = 1 ;
		String msg = "" ;
		try {
			retMap = partnerService.callbackBoss(partnerId,bossPartnerCode);
		} catch (Exception e) {
			
			logger.error("callbackBoss----调用boss系统回调接口出错,partner=" + partnerId + ",bossPartnerCode=" + bossPartnerCode , e);
			
			retMap.put("code",code);
			retMap.put("msg",msg);
		}
		
		return retMap;
	}


    @RequestMapping("/partnerTree")
    @ResponseBody
    public List<TreeNodeVo> partnerTree(Model model) {

        ActiveUser activeUser = getUserInfo();
        List<TreeNodeVo> resultList = new ArrayList<TreeNodeVo>();

        //广告主列表(侧边切换)
        List<Partner> list = partnerService.findPartnerListByLoginUser(activeUser);
        TreeNodeVo vo = null;
        for(Partner p : list){
            vo = new TreeNodeVo();
            vo.setId(p.getId());
            vo.setpId(p.getPid());
            vo.setName(p.getPartnerName());
            vo.setOpen(true);
            resultList.add(vo);
        }
        return resultList;
    }
    
    @RequestMapping("/fatherPartnerTree")
    @ResponseBody
    public List<TreeNodeVo> fatherPartnerTree(Model model){

        ActiveUser activeUser = getUserInfo();
        List<TreeNodeVo> resultList = new ArrayList<TreeNodeVo>();

        //如果是运营，营运总监，管理员，运营经理
        List<Partner> list = new ArrayList<Partner>();
        if (activeUser.isAdminOrManager() || activeUser.isOperateUser() || activeUser.isOperateDirectorOrManager()) {
        	Partner partner = new Partner();
			partner.setId(0L);
			partner.setPartnerName("无");
			list.add(partner);
			List<Partner> list2 = partnerService.findPartnerListByLoginUser(activeUser);
			list.addAll(list2);
		}else if (activeUser.isProxy()) { //如果是代理用户
			list.add(activeUser.getPartner());
		}
        TreeNodeVo vo = null;
        for(Partner p : list){
            vo = new TreeNodeVo();
            vo.setId(p.getId());
            vo.setpId(p.getPid());
            vo.setName(p.getPartnerName());
            vo.setOpen(true);
            resultList.add(vo);
        }
        return resultList;
    }
}
