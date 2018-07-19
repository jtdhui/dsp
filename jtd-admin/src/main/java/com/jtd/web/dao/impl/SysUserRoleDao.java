package com.jtd.web.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.jtd.commons.dao.BaseDaoImpl;
import com.jtd.commons.page.Pagination;
import com.jtd.web.constants.RoleType;
import com.jtd.web.dao.ISysUserRoleDao;
import com.jtd.web.po.SysUser;
import com.jtd.web.po.SysUserRole;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置
 * @创建日期 2016年9月29日
 * @项目名称 dsp-admin
 * @描述 <p>
 *     </p>
 */
@Repository
public class SysUserRoleDao extends BaseDaoImpl<SysUserRole> implements
		ISysUserRoleDao {

	@Override
	public List<SysUser> listUserByRole(RoleType roleType) {
		
		if (roleType == null)
			throw new RuntimeException("roleType is null");

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("sysRoleId", roleType.getCode());

		return getSqlSession().selectList(getStatement("listUserBy"), params);

	}
	
	@Override
	public List<SysUser> listUserByRole(Map<String, Object> params , RoleType roleType) {

		if (params == null)
			throw new RuntimeException("params is null");
		if (roleType == null)
			throw new RuntimeException("roleType is null");
		
		params.put("sysRoleId", roleType.getCode());

		return getSqlSession().selectList(getStatement("listUserBy"), params);

	}

	@Override
	public List<SysUser> findUserPageByRole(Pagination<SysUser> page,RoleType roleType) {
		if (page == null)
			throw new RuntimeException("page is null");
		if (page.getCondition() == null)
			throw new RuntimeException("page.condition is null");
		if (roleType == null)
			throw new RuntimeException("roleType is null");
		
		page.getCondition().put("sysRoleId", roleType.getCode());
		
		return getSqlSession().selectList(getStatement("findUserPageBy"), page);
	}

	
}
