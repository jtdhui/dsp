package com.jtd.web.dao.impl; 


import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.jtd.commons.dao.BaseDaoImpl;
import com.jtd.web.dao.ISysPermissionDao;
import com.jtd.web.po.SysPermission;


/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月29日
 * @项目名称 dsp-admin
 * @描述 <p></p>
 */
@Repository
public class SysPermissionDao extends BaseDaoImpl<SysPermission> implements ISysPermissionDao  {


	@Override
	public List<SysPermission> findMenuListByUserId(Long userid) throws Exception {
		return getSqlSession().selectList(getStatement("findMenuListByUserId"),userid);
	}

	@Override
	public List<SysPermission> findPermissionListByUserId(Long userid) throws Exception {
		return getSqlSession().selectList(getStatement("findPermissionListByUserId"),userid);
	}

	@Override
	public List<Map<String, Object>> listMapBy(SysPermission sp) {
		return getSqlSession().selectList(getStatement("listMapBy"),sp);
	}

	@Override
	public void updateStatus(SysPermission sp) {
		getSqlSession().update(getStatement("updateStatus"), sp);
	}

}
 