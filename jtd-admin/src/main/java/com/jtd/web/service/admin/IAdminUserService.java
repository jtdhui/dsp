package com.jtd.web.service.admin;

import com.jtd.web.po.ActiveUser;
import com.jtd.web.po.SysUser;
import com.jtd.web.service.IBaseService;

import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置
 * @创建日期 2016年9月29日
 * @项目名称 dsp-admin
 * @描述 <p>
 *     </p>
 */
public interface IAdminUserService extends IBaseService<SysUser> {

	/**
	 * 根据登陆名，查找用户信息
	 * 
	 * @param loginName
	 * @return
	 */
	public SysUser findUserByLoginName(String loginName);

	/**
	 * 修改用户密码
	 * 
	 * @param userId
	 * @param passwordOrgin
	 * @return
	 */
	public long changePassword(long userId, String passwordOrgin);

    public void findUserList(Model model, HttpServletRequest request, Integer pageNo, Integer pageSize, ActiveUser activeUser);
}
