package com.jtd.web.service;

import java.util.List;

import com.jtd.web.po.ActiveUser;
import com.jtd.web.po.SysUser;

import org.apache.shiro.session.Session;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月29日
 * @项目名称 dsp-admin
 * @描述 <p></p>
 */
public interface IUserService {
	
	/**
	 * 根据登陆名，查找用户信息
	 * @param loginName
	 * @return
	 */
	public SysUser findUserByLoginName(String loginName);

	public List<SysUser> listBy(SysUser user);

	public ActiveUser setActiveUserSession(SysUser user, Session session);
}
