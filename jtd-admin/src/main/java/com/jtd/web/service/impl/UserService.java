package com.jtd.web.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jtd.commons.PropertyConfig;
import com.jtd.commons.mybatis.support.CustomerContextHolder;
import com.jtd.utils.UserPwd;
import com.jtd.web.dao.*;
import com.jtd.web.po.*;
import com.jtd.web.service.ISysService;
import com.jtd.web.service.IUserService;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置
 * @创建日期 2016年9月29日
 * @项目名称 dsp-admin
 * @描述
 *     <p>
 *     </p>
 */
@Service
public class UserService implements IUserService {

	@Autowired
	private ISysUserDao sysUserDao;

	//注入service
	@Autowired
	private ISysService sysService;

	@Autowired
	private IUserPartnerDao userPartnerDao;

	@Autowired
	private IPartnerDao partnerDao;

	@Autowired
	private IUserFavDao userFavDao;

	@Autowired
	private ISysUserRoleDao userRoleDao;

	@Override
	public List<SysUser> listBy(SysUser user) {
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		return sysUserDao.listBy(user);
	}

	@Override
	public ActiveUser setActiveUserSession(SysUser user, Session session) {

		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		//ActiveUser就是用户的身份信息
		ActiveUser activeUser=new ActiveUser();
		activeUser.setUserId(user.getId());
		activeUser.setLoginName(user.getLoginName());
		activeUser.setUserName(user.getUserName());

		//查询用户所属partner
		UserFav uf = userFavDao.findByUserIdAndSettingName(user.getId(),"operatePartnerId");  /** 获取用户所属广告主配置，user_fav表 */

		Partner partner = null ;
		// 广告主
		if( uf != null ){
			partner = partnerDao.getById(Long.parseLong(uf.getSetting()));  /** 获取用户所属广告主对象，partner表 */
		}
		//如果查不到常用广告主（比如第一次进入系统），则查找user_partner关系表中第一条，并写入user_fav
		else {
			UserPartner param = new UserPartner();
			param.setUserId(user.getId() + "");
			List<UserPartner> userPartnerList = userPartnerDao.listBy(param);

			if (userPartnerList != null && userPartnerList.size() > 0) {
				UserPartner up  = userPartnerList.get(0);
				long partnerId = Long.parseLong(up.getPartnerId());
				partner = partnerDao.getById(partnerId);
				if(partner != null) {
					//向user_fav插入一条常用广告主数据
					uf = new UserFav();
					uf.setUserId(user.getId());
					uf.setSettingName("operatePartnerId");
					uf.setSetting(partner.getId()+"");
					userFavDao.insert(uf);
				}
			}
		}

		// 关联广告主信息
		activeUser.setFavPartner(partner);

		//用户所属广告主信息
		partner = partnerDao.getById(user.getPartnerId());  /** 获取用户所属广告主的父级广告主，partner表 */
		activeUser.setPartner(partner);

		//当前登录用户角色
		SysUserRole userRole = new SysUserRole();
		userRole.setSysUserId(user.getId().toString());
		List<SysUserRole> userRoleList = userRoleDao.listBy(userRole);
		if(userRoleList.size()>0){
			userRole = userRoleList.get(0);
			activeUser.setRoleId(userRole.getSysRoleId());
		}

		//根据用户id取出菜单
		List<SysPermission> menus = null;
		try {
			menus=sysService.findMenuListByUserId(activeUser.getUserId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		//将用户的菜单设置到activeUser
		activeUser.setMenus(menus);

		return activeUser;
	}

	@Override
	public SysUser findUserByLoginName(String loginName) {
		SysUser tmp=new SysUser();
		tmp.setLoginName(loginName);
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		List<SysUser> listUser=listBy(tmp);
		if (listUser!=null&&listUser.size()>0) {
			return listUser.get(0);
		}
		return null;
	}

	

}
