package com.jtd.web.dao;

import java.util.List;
import java.util.Map;

import com.jtd.commons.dao.BaseDao;
import com.jtd.commons.page.Pagination;
import com.jtd.web.constants.RoleType;
import com.jtd.web.po.SysUser;
import com.jtd.web.po.SysUserRole;

public interface ISysUserRoleDao extends BaseDao<SysUserRole> {
	
	/**
	 * 按角色id去返回拥有该角色的user信息
	 * 
	 * @param roleType
	 * @return
	 */
	public List<SysUser> listUserByRole(RoleType roleType);
	
	public List<SysUser> listUserByRole(Map<String, Object> params , RoleType roleType);
	/**
	 * 按角色id去返回拥有该角色的user信息(分页)
	 * 
	 * @param roleType
	 * @return
	 */
	public List<SysUser> findUserPageByRole(Pagination<SysUser> page, RoleType roleType);
	
}
