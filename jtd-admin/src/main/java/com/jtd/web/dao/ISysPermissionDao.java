package com.jtd.web.dao;

import java.util.List;
import java.util.Map;

import com.jtd.commons.dao.BaseDao;
import com.jtd.web.po.SysPermission;

public interface ISysPermissionDao extends BaseDao<SysPermission> {

	// 根据用户id查询菜单
	public List<SysPermission> findMenuListByUserId(Long userid) throws Exception;

	// 根据用户id查询权限url
	public List<SysPermission> findPermissionListByUserId(Long userid) throws Exception;
	
	public List<Map<String,Object>> listMapBy(SysPermission sp) ;

    public void updateStatus(SysPermission sp);
}
