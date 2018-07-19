package com.jtd.web.service.admin;

import com.jtd.web.po.SysUserRole;
import com.jtd.web.service.IBaseService;

import org.springframework.ui.Model;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月29日
 * @项目名称 dsp-admin
 * @描述 <p></p>
 */
public interface IAdminUserRoleService  extends IBaseService<SysUserRole>{

    public void queryUserRoleDate(Model model, String user_id);
}
