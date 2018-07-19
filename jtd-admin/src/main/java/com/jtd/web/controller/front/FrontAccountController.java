package com.jtd.web.controller.front;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jtd.commons.mybatis.support.CustomerContextHolder;
import com.jtd.utils.DateUtil;
import com.jtd.web.controller.BaseController;
import com.jtd.web.dao.IPartnerDao;
import com.jtd.web.dao.IUserFavDao;
import com.jtd.web.dao.IUserPartnerDao;
import com.jtd.web.po.ActiveUser;
import com.jtd.web.po.Partner;
import com.jtd.web.po.QualiDocType;
import com.jtd.web.po.SysUser;
import com.jtd.web.po.UserFav;
import com.jtd.web.po.UserPartner;
import com.jtd.web.service.front.IFrontAccountService;
import com.jtd.web.service.front.IFrontPartnerService;

@Controller
@RequestMapping("/front/account")
public class FrontAccountController extends BaseController {

	public static final String PAGE_DIRECTORY = "/front/account/";
	public static final String ACTION_PATH = "redirect:/front/account/";

	@Autowired
	private IFrontAccountService service;

	@Autowired
	private IPartnerDao partnerDao;

	@Autowired
	private IUserFavDao userFavDao;
	
	@Autowired
	private IUserPartnerDao userPartnerDao ;

	@Autowired
	private IFrontPartnerService frontPartnerService;

	/**
	 * 到account_info.jsp显示用户信息（注意是user个人信息，不是所属partner信息）
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/toInfo")
	public String toInfo(Model model, HttpServletRequest request) {

		setUserPageInfo(model);

		ActiveUser user = getUserInfo();
		Long userId = user.getUserId();

		service.toInfo(userId, model);

		return PAGE_DIRECTORY + "account_info";
	}

	/**
	 * 保存用户个人信息
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping("/ajaxInfoSave")
	@ResponseBody
	public String ajaxInfoSave(SysUser user) {

		long result = service.infoSave(user);

		if (result > 0) {
			return "ok";
		} else {
			return "error";
		}
	}

	/**
	 * 修改密码
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping("/changePassword")
	@ResponseBody
	public String changePassword(HttpServletRequest request) {

		ActiveUser user = getUserInfo();
		long userId = user.getUserId();

		String inputOldPwd = request.getParameter("oldPassword");
		String newPwd = request.getParameter("newPassword");

		long result = service.changePassword(userId, inputOldPwd, newPwd);

		if (result > 0) {
			return "ok";
		} else if (result == -1) {
			return "oldpwd";
		} else {
			return "error";
		}
	}

	/**
	 * 到资质上传页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/toQualiDoc")
	public String toQualiDoc(Model model, HttpServletRequest request) {

		setUserPageInfo(model);

		ActiveUser user = getUserInfo();
		UserFav fav = userFavDao.findOperatePartnerId(user.getUserId());
		long partnerId = Long.parseLong(fav.getSetting());
		
		service.toQualiDoc(partnerId, model);
		return PAGE_DIRECTORY + "account_qualidoc";
	}

	/**
	 * 资质上传页面通过ajax查询所需资质种类列表
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/ajaxGetDocTypeList")
	@ResponseBody
	public List<QualiDocType> ajaxGetDocTypeList(Integer qualiDocCustomerTypeId) {

		return service.listQualiDocTypeByCustomerType(qualiDocCustomerTypeId);

	}

	/**
	 * 资质上传页面通过ajax查询所需资质种类列表
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/saveQualiDoc")
	public String saveQualiDoc(Model model, HttpServletRequest request)
			throws Exception {

		setUserPageInfo(model);

		ActiveUser user = getUserInfo();
		Long userId = user.getUserId();
		UserFav fav = userFavDao.findOperatePartnerId(userId);
		long partnerId = Long.parseLong(fav.getSetting());

		String str = service.saveQualiDoc(userId, partnerId, request);
		
		return ACTION_PATH + "toQualiDoc.action?str=" + str;
	}

	/**
	 * 资质上传页面通过ajax查询所需资质种类列表
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/deleteQualiDoc")
	public String deleteQualiDoc(Model model, HttpServletRequest request) {

		setUserPageInfo(model);

		ActiveUser user = getUserInfo();
		UserFav fav = userFavDao.findOperatePartnerId(user.getUserId());
		long partnerId = Long.parseLong(fav.getSetting());

		// 前台传来的资质类型id，会把当前用户此类资质都删除掉
		long qualiDocTypeId = Long.parseLong(request
				.getParameter("qualiDocTypeId"));

		service.deleteQualiDoc(partnerId, qualiDocTypeId);

		return ACTION_PATH + "toQualiDoc.action?msg=deleteOk";
	}
	
	/**
	 * 到财务明细页面(账户流水)
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/toFinance")
	public String toFinance(Model model, HttpServletRequest request) {

		setUserPageInfo(model);

		ActiveUser user = getUserInfo();
		UserFav fav = userFavDao.findOperatePartnerId(user.getUserId());
		long partnerId = Long.parseLong(fav.getSetting());

		HashMap<String, Object> paraMap = new HashMap<String, Object>();

		paraMap.put("partnerId", partnerId);

		if (StringUtils.isEmpty(request.getParameter("tradeType")) == false) {
			paraMap.put("tradeType", request.getParameter("tradeType"));
		}
		if (StringUtils.isEmpty(request.getParameter("startDate")) == false) {
			paraMap.put("startDate", request.getParameter("startDate"));
		} else {
			// 日期默认查最近七天
			Calendar c = Calendar.getInstance();
			c.set(Calendar.DATE, c.get(Calendar.DATE) - 6);
			paraMap.put("startDate",
					DateUtil.getDateStr(c.getTime(), "yyyy-MM-dd"));
		}

		if (StringUtils.isEmpty(request.getParameter("endDate")) == false) {
			paraMap.put("endDate", request.getParameter("endDate"));
		} else {
			// 日期默认查最近七天
			paraMap.put("endDate",
					DateUtil.getDateStr(new Date(), "yyyy-MM-dd"));
		}
		int pageNo = 1;
		if (StringUtils.isEmpty(request.getParameter("pageNo")) == false) {
			pageNo = Integer.parseInt(request.getParameter("pageNo"));
		}
		if (StringUtils.isEmpty(request.getParameter("pageSize")) == false) {
			pageSize = Integer.parseInt(request.getParameter("pageSize"));
		}

		//servcie查询
		service.toFinance(partnerId, model, paraMap, pageNo, pageSize);

		return PAGE_DIRECTORY + "account_finance";
	}
	
	/**
	 * 到财务明细页面(账户流水)
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/getFinanceExcel")
	public void getFinanceExcel(HttpServletRequest request , HttpServletResponse response) {

		HashMap<String, Object> paraMap = new HashMap<String, Object>();
		
		ActiveUser user = getUserInfo();
		UserFav fav = userFavDao.findOperatePartnerId(user.getUserId());
		long partnerId = Long.parseLong(fav.getSetting());

		paraMap.put("partnerId", partnerId);

		if (StringUtils.isEmpty(request.getParameter("tradeType")) == false) {
			paraMap.put("tradeType", request.getParameter("tradeType"));
		}
		if (StringUtils.isEmpty(request.getParameter("startDate")) == false) {
			paraMap.put("startDate", request.getParameter("startDate"));
		}
		if (StringUtils.isEmpty(request.getParameter("endDate")) == false) {
			paraMap.put("endDate", request.getParameter("endDate"));
		}
		
		service.getFinanceExcel(paraMap, response);
		
	}
	
	

	@RequestMapping("/toGetCode")
	public String toGetCode(Model model, HttpServletRequest request) {

		setUserPageInfo(model);

		ActiveUser user = getUserInfo();
		UserFav fav = userFavDao.findOperatePartnerId(user.getUserId());
		long partnerId = Long.parseLong(fav.getSetting());
		
		Partner p = partnerDao.getById(partnerId);
		String pName = p.getPartnerName();
		
		service.getRetargetPacketMap(model, partnerId, pName);

		return PAGE_DIRECTORY + "account_getcode";
	}

	@RequestMapping("/ajaxGetPartner")
	@ResponseBody
	public Partner ajaxGetPartner() {
		
		Partner partner = null ;
		
		// 使用业务库
		CustomerContextHolder
				.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);

		ActiveUser user = getUserInfo();
		//先找该user常用的partner
		UserFav fav = userFavDao.findOperatePartnerId(user.getUserId());
		if(fav != null){
			long partnerId = Long.parseLong(fav.getSetting());
			partner = partnerDao.getById(partnerId);
		}
		//如果查不到常用广告主（比如第一次进入系统），则查找user_partner关系表中第一条，并写入user_fav
		else{
			UserPartner up = new UserPartner();
			up.setUserId(user.getUserId()+"");
			List<UserPartner> upList = userPartnerDao.listBy(up);
			if(upList != null && upList.size() > 0 ){
				String partnerId = upList.get(0).getPartnerId();
				long partnerIdL = Long.parseLong(partnerId);
				partner = partnerDao.getById(partnerIdL);
				
				if(partner != null){
					
					//向user_fav插入一条常用广告主数据
					fav = new UserFav();
					fav.setUserId(user.getUserId());
					fav.setSettingName("operatePartnerId");
					fav.setSetting(partner.getId()+"");
					userFavDao.insert(fav);
					
				}
			}
		}
		
		if(partner == null){
			partner = new Partner();
		}
		return partner ;
	}

}
