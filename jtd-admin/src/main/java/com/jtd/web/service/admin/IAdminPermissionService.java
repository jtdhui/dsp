package com.jtd.web.service.admin;

import java.util.List;
import java.util.Map;

import com.jtd.web.po.SysPermission;
import com.jtd.web.service.IBaseService;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月29日
 * @项目名称 dsp-admin
 * @描述 <p></p>
 */
public interface IAdminPermissionService  extends IBaseService<SysPermission>{
	
	public List<SysPermission> list();
	
	public List<Map<String,Object>> listMapBy(SysPermission sp);

	/**
	 * 修改状态
	 * @param sp
	 */
    public void updateStatus(SysPermission sp);
}
