package com.jtd.web.service.admin.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jtd.commons.dao.BaseDao;
import com.jtd.commons.mybatis.support.CustomerContextHolder;
import com.jtd.web.dao.ISysPermissionDao;
import com.jtd.web.po.SysPermission;
import com.jtd.web.service.admin.IAdminPermissionService;
import com.jtd.web.service.impl.BaseService;

/**
 * 权限与菜单Service
 * @作者 duber
 * @版本 V1.0
 * @创建日期 2016年10月13日
 * @描述
 */
@Service
public class AdminPermissionService extends BaseService<SysPermission>  implements IAdminPermissionService {

	@Autowired
	private ISysPermissionDao sysPermissionDao;
	
	@Override
	protected BaseDao<SysPermission> getDao() {
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		return sysPermissionDao;
	}

	@Override
	public List<SysPermission> list() {
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		SysPermission sp = new SysPermission();
		sp.setType("menu");
		sp.setLevel("1");
		
		List<SysPermission> levelOneList = getDao().listBy(sp);
		
		
		List<SysPermission> resultList = new ArrayList<SysPermission>();
		if(levelOneList != null ){
			for (SysPermission menulevelOne : levelOneList) {
				
				resultList.add(menulevelOne);
				
				long id = menulevelOne.getId();
				
				sp.setParentId(id);
				sp.setLevel("2");
				List<SysPermission> levelTwoList = getDao().listBy(sp);
				
				if(levelTwoList != null){
					for (SysPermission menuLevelTwo : levelTwoList) {
						if(menuLevelTwo.getParentId() == id){
							resultList.add(menuLevelTwo);
							
							menuLevelTwo.setName("&nbsp;&nbsp;&nbsp;&nbsp;" + menuLevelTwo.getName());
						}
					}
				}
			}
		}
		
		return resultList;
	}

	@Override
	public List<Map<String,Object>> listMapBy(SysPermission sp) {
		CustomerContextHolder.setContextType(CustomerContextHolder.SESSION_FACTORY_BUSINESS);
		
		return sysPermissionDao.listMapBy(sp);
	}

	@Override
	public void updateStatus(SysPermission sp) {
		sysPermissionDao.updateStatus(sp);
	}

}
