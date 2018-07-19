package com.jtd.web.shiro;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.jtd.commons.mybatis.support.CustomerContextHolder;
import com.jtd.web.po.ActiveUser;
import com.jtd.web.po.SysPermission;
import com.jtd.web.po.SysUser;
import com.jtd.web.service.ISysService;
import com.jtd.web.service.IUserService;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年10月8日
 * @项目名称 dsp-admin
 * @描述 <p></p>
 */

public class PlatformRealm extends AuthorizingRealm {
	
	//注入service
	@Autowired
	private ISysService sysService;

	@Autowired
	private IUserService userService;
	
	// 设置realm的名称
	@Override
	public void setName(String name) {
		super.setName("customRealm");
	}

	
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo( AuthenticationToken token) throws AuthenticationException {

		//使用业务库
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		
		// token是用户输入的用户名和密码
		// 第一步从token中取出用户名
		String loginName=(String)token.getPrincipal();
		if(StringUtils.isEmpty(loginName)){
			return null ;
		}
		
		// 第二步：根据用户输入的userCode从数据库查询
		SysUser user=null;
		try {
			user=sysService.findSysUserByLoginName(loginName); /** 根据登录账号获取登录用户信息，sys_user表 */
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		if (user==null) {
			return null;
		}
		
		//ActiveUser就是用户的身份信息
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		ActiveUser activeUser = userService.setActiveUserSession(user,session);
		session.setAttribute("activeUser",activeUser);

		//从数据库中取出的密码
		String password=user.getPwd();
		//散列算法时，干扰字符串
		String salt=user.getSalt();
		//将activeUser设置simpleAuthenticationInfo
		SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(
				user.getLoginName(), password,ByteSource.Util.bytes(salt), this.getName());

		return simpleAuthenticationInfo;
	}
	
	// 用于授权
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		
		//从 principals获取主身份信息
		//将getPrimaryPrincipal方法返回值转为真实身份类型（在上边的doGetAuthenticationInfo认证通过填充到SimpleAuthenticationInfo中身份类型），
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		//ActiveUser activeUser =  (ActiveUser) principals.getPrimaryPrincipal();
		ActiveUser activeUser = (ActiveUser)session.getAttribute("activeUser");
		//根据身份信息获取权限信息
        //使用业务库
        CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		if(activeUser == null) {
			String loginName=(String) principals.getPrimaryPrincipal();
			if(StringUtils.isEmpty(loginName)){
				return null ;
			}

			// 第二步：根据用户输入的userCode从数据库查询
			SysUser user=null;
			try {
				user=sysService.findSysUserByLoginName(loginName);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			activeUser = userService.setActiveUserSession(user,session);
			session.setAttribute("activeUser",activeUser);
		}
		//从数据库获取到权限数据
		List<SysPermission> permissionList = null;
		try {
			permissionList = sysService.findPermissionListByUserId(activeUser.getUserId());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//单独定一个集合对象 
		List<String> permissions = new ArrayList<String>();
		if(permissionList!=null){
			for(SysPermission sysPermission:permissionList){
				//将数据库中的权限标签 符放入集合
				permissions.add(sysPermission.getPercode());
			}
		}
		
		//查到权限数据，返回授权信息(要包括 上边的permissions)
		SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
		//将上边查询到授权信息填充到simpleAuthorizationInfo对象中
		simpleAuthorizationInfo.addStringPermissions(permissions);

		return simpleAuthorizationInfo;
	}
	
	//清除缓存
	public void clearCached() {
		PrincipalCollection principals = SecurityUtils.getSubject().getPrincipals();
		super.clearCache(principals);
	}


}
