package com.jtd.web.controller;

import java.util.*;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import com.jtd.web.exception.PlatformException;
import com.jtd.web.po.ActiveUser;
import com.jtd.web.po.SysUser;
import com.jtd.web.service.ISysService;
import com.jtd.web.service.IUserService;

/**
 * 
 * @author duber
 *
 */
@Transactional
public class BaseController {

	protected int pageNo = 1;
	public static int pageSize = 10;
	protected final static Logger logger = Logger
			.getLogger(BaseController.class);
	private final static String PARAM_PAGE_NO = "pageNo";
	protected String pageSizeName = "pageSize";

	//注入service
	@Autowired
	private ISysService sysService;

	@Autowired
	private IUserService userService;
	
	@Autowired(required=false)
	private HttpServletRequest request ;

	/**
	 * [获取session]
	 * 
	 * @param request
	 * @return
	 */
	public static HttpSession getSession(HttpServletRequest request) {
		return request.getSession();
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		BaseController.pageSize = pageSize;
	}

	@SuppressWarnings("unchecked")
	protected Map<String, Object> prepareParams(Object obj,
			HttpServletRequest request) throws Exception {
		if (request != null) {
			String pageNoStr = (String) request.getParameter(PARAM_PAGE_NO), pageSizeStr = (String) request
					.getParameter(pageSizeName);
			if (StringUtils.isNotBlank(pageNoStr)) {
				pageNo = Integer.parseInt(pageNoStr);
			}
			if (StringUtils.isNotBlank(pageSizeStr)) {
				pageSize = Integer.parseInt(pageSizeStr);
			}
		}

		Map<String, Object> params = new HashMap<String, Object>();
		params = BeanUtils.describe(obj);
		params = handleParams(params);
		// 回填值项
		BeanUtils.populate(obj, params);
		return params;
	}

	private Map<String, Object> handleParams(Map<String, Object> params) {
		Map<String, Object> result = new HashMap<String, Object>();
		if (null != params) {
			Set<Entry<String, Object>> entrySet = params.entrySet();

			for (Iterator<Entry<String, Object>> it = entrySet.iterator(); it
					.hasNext();) {
				Entry<String, Object> entry = it.next();
				if (entry.getValue() != null) {
					result.put(entry.getKey(),
							StringUtils.trimToEmpty((String) entry.getValue()));
				}
			}
		}
		return result;
	}

	public void setUserPageInfo(Model model) {
		// 从shiro的session中取activeUser
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		ActiveUser activeUser = (ActiveUser) session.getAttribute("activeUser");

		// 取身份信息
		//ActiveUser activeUser = (ActiveUser) subject.getPrincipal();
		if(activeUser == null){
			String loginName=(String) subject.getPrincipal();
			// 第二步：根据用户输入的userCode从数据库查询
			SysUser user=null;
			try {
				if(StringUtils.isNotEmpty(loginName)) {
					user = sysService.findSysUserByLoginName(loginName);
					if(user != null){
						activeUser = userService.setActiveUserSession(user, session);
					}
					else{
						PlatformException ex = new PlatformException("找不到'" + loginName + "'的用户数据，请联系管理员");
						ex.setLoginError(true);
						throw ex ;
					}
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}

		//TODO 这里其实应该在PlatformRealm中去判断，在登录时就直接告诉用户
		if(activeUser.getPartner() == null){
			PlatformException ex = new PlatformException("该用户没有相关的广告主数据权限,请联系管理员分配相应权限!");
			ex.setLoginError(true);
			throw ex ;
		}
		// 通过model传到页面
		session.setAttribute("activeUser",activeUser);
		model.addAttribute("activeUser", activeUser);
		Calendar calendar=Calendar.getInstance();
		model.addAttribute("year", calendar.get(Calendar.YEAR));
		
		// 如果是后台页面，会收到菜单链接中传来的 menuId参数
		if(request != null){
			String menuId = request.getParameter("menuId");
			request.setAttribute("menuId", menuId);
		}
	}

	/**
	 * 获取登录用户信息
	 * 
	 * @return
	 */
	public ActiveUser getUserInfo() {
		// 从shiro的session中取activeUser
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		ActiveUser activeUser = (ActiveUser) session.getAttribute("activeUser");
		// 取身份信息
		return activeUser;
	}

}
