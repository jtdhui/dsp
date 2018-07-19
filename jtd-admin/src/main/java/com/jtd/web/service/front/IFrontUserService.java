package com.jtd.web.service.front;

import java.util.List;

import com.jtd.web.po.SysUser;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月29日
 * @项目名称 dsp-admin
 * @描述 <p></p>
 */
public interface IFrontUserService {
	
	/**
	 * 根据用户名和密码查询用户数据
	 * @param user
	 * @return
	 */
	public SysUser findUserInfo(SysUser user);
	
	/**
	 * 根据ID查找记录.
	 * 
	 * @param id
	 *            .
	 * @return entity .
	 */
	SysUser getById(long id);
	
	//获取用户列表
	public List<SysUser> findUserAll();
	
	/**
	 * 根据条件获取列表
	 * @param t
	 * @return
	 */
	public List<SysUser> listBy(SysUser user);
	
	/**
	 * 根据登陆名，查找用户信息
	 * @param loginName
	 * @return
	 */
	public SysUser findUserByLoginName(String loginName);
	
	/**
	 * 根据实体对象新增记录.
	 * 
	 * @param entity
	 *            .
	 * @return id .
	 */
	long insert(SysUser user);

	/**
	 * 批量保存对象.
	 * 
	 * @param entity
	 *            .
	 * @return id .
	 */
	long insertBatch();

	/**
	 * 更新实体对应的记录.
	 * 
	 * @param entity
	 *            .
	 * @return
	 */
	long update(SysUser user);

	/**
	 * 批量更新对象.
	 * 
	 * @param entity
	 *            .
	 * @return int .
	 */
	long update(List<SysUser> list);

	/**
	 * 根据ID删除记录.
	 * 
	 * @param id
	 *            .
	 * @return
	 */
	long deleteById(long id);
	
	/**
	 * 批量删除数据
	 * @param list
	 * @return
	 */
	long delete(List<SysUser> list);
}
