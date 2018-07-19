package com.jtd.web.service;

import java.util.List;

import com.jtd.web.po.ActiveUser;
import com.jtd.web.po.SysPermission;
import com.jtd.web.po.SysUser;
/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年10月9日
 * @项目名称 dsp-admin
 * @描述 <p></p>
 */
public interface ISysService {
	// 根据用户的身份和密码 进行认证，如果认证通过，返回用户身份信息
	public ActiveUser authenticat(String loginName, String password) throws Exception;

	// 根据用户账号查询用户信息
	public SysUser findSysUserByLoginName(String loginName) throws Exception;

	// 根据用户id查询权限范围的菜单
	public List<SysPermission> findMenuListByUserId(Long userid) throws Exception;

	// 根据用户id查询权限范围的url
	public List<SysPermission> findPermissionListByUserId(Long userid) throws Exception;
}
